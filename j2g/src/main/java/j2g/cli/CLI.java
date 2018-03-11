package j2g.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * @author Fuchs
 *
 */
public class CLI {

	protected String[] args;
	protected Options options;
	
	protected String classPath;
	protected String className;
	protected String methodName;
	protected String methodSignature;
	protected String outputDirectory;
	
	public CLI(String[] args) {
		this.args = args;
		this.options = new Options();
		this.options.addOption("h", "help", false, "show help.");
		this.options.addOption("cp", "classPath", true, "extra class path for the web application");
		this.options.addRequiredOption("c", "className", true, "the full qualified name of the class under test");
		this.options.addRequiredOption("mn", "methodName", true, "the name of the method under test");
		this.options.addRequiredOption("ms", "signature", true, "the signature of the method under test");
		this.options.addRequiredOption("o", "output", true, "the path to a directory where to generate the output file");
	}
	
	public void parse() throws ParseException {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch(MissingOptionException e) {
			e.printStackTrace();
			printHelp();
			System.exit(0);
		}
		
		if(cmd.hasOption("c")) {
			this.className = cmd.getOptionValue("c");
		}
		if(cmd.hasOption("mn")) {
			this.methodName = cmd.getOptionValue("mn");
		}
		if(cmd.hasOption("ms")) {
			this.methodSignature = cmd.getOptionValue("ms");
		}
		if(cmd.hasOption("cp")) {
			this.classPath = cmd.getOptionValue("cp");
		}
		if(cmd.hasOption("o")) {
			this.outputDirectory = cmd.getOptionValue("o");
		}
	}
	
	private void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("cfg", options);
	}

	public String getClassPath() {
		return this.classPath;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getMethodSignature() {
		return methodSignature;
	}
	
	public String getoOutputDirectory() {
		return outputDirectory;
	}
	
}
