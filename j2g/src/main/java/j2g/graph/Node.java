package j2g.graph;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

/**
 * 
 * @author Fuchs
 *
 */
public class Node implements Comparable<Node> {
		
	private static Map<Integer, Integer> ID_MAPPING = new HashMap<Integer, Integer>();
	
	private final int id;
	private int externalId;
	private static int idCounter = 1;
	
	private MethodGen methodGen;
	private InstructionHandle ih;
	
	public Node(MethodGen methodGen, InstructionHandle ih) {
		this.methodGen = methodGen;
		this.ih = ih;
		this.id = (methodGen.getClassName()+" "+methodGen.toString()+" "+ih.getPosition()).hashCode();
		checkId();
	}

	private void checkId() { 
		if(!ID_MAPPING.containsKey(this.id)) {
			this.externalId = idCounter++;
			ID_MAPPING.put(this.id, this.externalId);
		}
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
	
	public int getExternalID() {
		if(!ID_MAPPING.containsKey(this.id)) {
			throw new RuntimeException("Should not happen!");
		}
		return ID_MAPPING.get(this.id);
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

	public int compareTo(Node o) {
		if(this.externalId == o.externalId) {
			return 0;
		}
		if(this.externalId > o.externalId) {
			return 1;
		}
		return -1;
	}
	
}
