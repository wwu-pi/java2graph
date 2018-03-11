package j2g.graph;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

/**
 * 
 * @author Fuchs
 *
 */
public class Node {
		
	private final int id;
	private MethodGen methodGen;
	private InstructionHandle ih;
	
	public Node(MethodGen methodGen, InstructionHandle ih) {
		this.methodGen = methodGen;
		this.ih = ih;
		this.id = (methodGen.getClassName()+" "+methodGen.toString()+" "+ih.getPosition()).hashCode();
	}

	public MethodGen getMethodGen() {
		return methodGen;
	}

	public InstructionHandle getIh() {
		return ih;
	}
	
	public int getID() {
		return this.id;
	}
	
	@Override
	public int hashCode() {
		return this.ih.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node) {
			return this.id == ((Node) obj).id;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return ih != null ? ""+ih.getPosition() : "null";
	}
	
}
