package j2g.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.bcel.generic.MethodGen;

/**
 * 
 * @author Fuchs
 *
 */
public class Graph {

	private Set<Node> nodes;
	private List<Edge> edges;
	private MethodGen methodGen;
	
	public Graph(MethodGen methodGen) {
		this.methodGen = methodGen;
		this.edges = new LinkedList<Edge>();
		this.nodes = new HashSet<Node>();
	}
	
	public void addEdge(Edge e) {
		if(e.getFrom() != null) this.nodes.add(e.getFrom());
		if(e.getTo() != null) this.nodes.add(e.getTo());
		this.edges.add(e);
	}
	
	public Set<Node> getNodes() {
		return this.nodes;
	}
	
	public List<Edge> getEdges() {
		return this.edges;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph for method: " + methodGen.toString() + "\n");
		for(Edge e : edges) {
			sb.append(e + "\n");
		}
		return sb.toString();
	}
}
