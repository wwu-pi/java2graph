package j2g;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.commons.io.FilenameUtils;

import j2g.cli.CLI;
import j2g.graph.BranchEdge;
import j2g.graph.Edge;
import j2g.graph.Graph;
import j2g.graph.Node;
import j2g.graph.ReturnEdge;
import j2g.pajek.PajekFormatter;

/**
 * 
 * @author Fuchs
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		sayHello();
		
		CLI cli = new CLI(args);
		cli.parse();
		
		printCLIArguments(cli);
		
		System.out.println("*** Analyze additional class path: " + cli.getClassPath());
		if(cli.getClassPath()!=null) {
			analyzeClassPath(new File(cli.getClassPath()));	
		}
		
		MethodGen methodGen = getMethodGen(cli.getClassName(), cli.getMethodName() ,  cli.getMethodSignature());
		
		Graph g = generateGraph(methodGen);
		
		File outputDirectory = createOutputFile(cli.getoOutputDirectory());
		writeGraph(g, outputDirectory);
		
		System.out.println("*** Successfully generated graph and stored in file located at: "+ outputDirectory.getAbsolutePath());
	}
	
	public static Graph generateGraph(MethodGen methodGen) throws ClassNotFoundException {
		System.out.println("*** Generate graph for method: "+methodGen.getClassName() + " # " + methodGen);
		Graph g = new Graph(methodGen);
		InstructionList instructionList = methodGen.getInstructionList();
		InstructionHandle[] handles = instructionList.getInstructionHandles();
		for(InstructionHandle ih : handles) {			
			if(ih.getInstruction() instanceof BranchInstruction) {
				BranchInstruction bi = (BranchInstruction)ih.getInstruction();
				BranchEdge trueEdge = new BranchEdge(new Node(methodGen, ih), new Node(methodGen, bi.getTarget()));
				BranchEdge falseEdge = new BranchEdge(new Node(methodGen, ih), new Node(methodGen, ih.getNext()));
				g.addEdge(falseEdge);
				g.addEdge(trueEdge);;
			} else if(ih.getInstruction() instanceof ReturnInstruction) {
				ReturnEdge edge = new ReturnEdge(new Node(methodGen, ih));
				g.addEdge(edge);
//			} else if(ih.getInstruction() instanceof InvokeInstruction) {
//				InvokeInstruction ii = (InvokeInstruction)ih.getInstruction();
//				String className = ii.getClassName(methodGen.getConstantPool());
//				String methodName = ii.getMethodName(methodGen.getConstantPool());
//				String methodSignature = ii.getSignature(methodGen.getConstantPool());
//				MethodGen invokedMethod = getMethodGen(className, methodName, methodSignature);
//				Graph g_invoked = generateGraph(invokedMethod);
//				g_invoked.getEndNodes()) {
//					
//				}
//				Edge startEdge = new Edge(new Node(methodGen, ih), new Node(invokedMethod, ))
			} else {
				Edge edge = new Edge(new Node(methodGen, ih), new Node(methodGen, ih.getNext()));
				g.addEdge(edge);
			}
		}
		return g;
	}
	
	
	private static void sayHello() {
		System.out.println("********************");
		System.out.println("*** Java 2 Graph ***");
		System.out.println("********************\n");
		System.out.println("* Author:    Andreas Fuchs");
		System.out.println("* Main:      andreas.fuchs@wi.uni-muenster.de");
		System.out.println("* Contact me for questions, feedback, bug reports, etc.");
		System.out.println("* Thank you!\n");
	}
	
	private static void printCLIArguments(CLI cli) {		
		System.out.println("Given arguments:");
		System.out.println("  class name:       " + cli.getClassName());
		System.out.println("  method name:      " + cli.getMethodName());
		System.out.println("  method signature: " + cli.getMethodSignature());
		System.out.println("  generate test cases to folder: " + cli.getoOutputDirectory());
	}
	
	private static File createOutputFile(String outputDirectoryPath) {
		File outputDirectory = new File(outputDirectoryPath);
		if(!outputDirectory.exists()) {
			Scanner in = new Scanner(System.in);
			System.out.println("**********************************************");
			System.out.println("*** Output directory should be in : " + outputDirectoryPath);
			boolean isOk = false;
			boolean create = false;
			while(!isOk) {
				System.out.print("\tOutput directory does not exists, create it? y/n: ");
				String input = in.nextLine();
				if(input != null) {
					if(input.equals("yes") || input.equals("y")) {
						create = true; isOk = true;
					}
					if(input.equals("no") || input.equals("n")) {
						create = false; isOk = true;
					}
				}
			}
			in.close();
			System.out.println("**********************************************");
			if(!create) {
				System.out.println("** Good bye!");
				System.exit(0);
			} else {
				Path p = Paths.get(outputDirectory.getAbsolutePath());
				try {
					Files.createDirectories(p);
				} catch (IOException e) {
					throw new RuntimeException("Could not create output directory.", e);
				}
			}
		}
		if(outputDirectory.exists() && !outputDirectory.isDirectory()) {
			System.out.println("*** Path to output directory is not a directory: " + outputDirectoryPath);
			System.exit(0);
		}
		
		Path genTestsPath = Paths.get(outputDirectoryPath, "generated-graphs");
		int i = 0;
		while(genTestsPath.toFile().exists()) {
			genTestsPath = Paths.get(outputDirectoryPath, "generated-graphs-"+(i));
			i++;
		}	
		try {
			Files.createDirectories(genTestsPath);
		} catch (IOException e) {
			throw new RuntimeException("Could not create output directory.", e);
		}
		
		return genTestsPath.toFile();
	}
	
	private static void writeGraph(Graph g, File outputDirectory) {
		Path graphNodesFilePath = Paths.get(outputDirectory.getAbsolutePath(), "nodes.txt");
		Path graphEdgesFilePath = Paths.get(outputDirectory.getAbsolutePath(), "edges.txt");
		
		PajekFormatter pajek = new PajekFormatter(g);
		
		writeFile(graphNodesFilePath, pajek.getNodes());
		writeFile(graphEdgesFilePath, pajek.getEdges());
	}
	
	private static void writeFile(Path path, StringBuilder sb) {
		assert(!path.toFile().exists());
		File testCaseFile = path.toFile();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(testCaseFile));
			writer.write(sb.toString());
		} catch(Exception e) {
			throw new RuntimeException("Could not write test case to: " + testCaseFile, e);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					throw new RuntimeException("Could not close file writer.", e);
				}
		}
	}


	private static MethodGen getMethodGen(String className, String methodName, String signature) throws ClassNotFoundException {
		JavaClass jc = Repository.lookupClass(className);
		for(Method m : jc.getMethods()) {
			if(m.getName().equals(methodName)
			&& m.getSignature().equals(signature)) {
				return new MethodGen(m, jc.getClassName(), new ConstantPoolGen(jc.getConstantPool()));
			}
		}
		throw new RuntimeException("Could not find method in class path!");
	}	
	
	private static void analyzeClassPath(File f) throws ClassFormatException, IOException {
		if(!f.exists()) throw new RuntimeException("Given class path does not exists! ("+f.getAbsolutePath()+")");

		if(f.isDirectory()) {
			for(File ff : f.listFiles()) {
				analyzeClassPath(ff);
			}
		} else {
			String extension = FilenameUtils.getExtension(f.getName());
			if(extension.equals("class")) {
				InputStream is = new FileInputStream(f);
				ClassParser cp = new ClassParser(is, f.getName());
				JavaClass jc = cp.parse();
				Repository.addClass(jc);
				System.out.println("   * class successfully added to internal repository:        " + jc.getClassName());
			}
		}
	}
}
