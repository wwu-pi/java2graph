package j2g.graph;

/**
 * 
 * @author Fuchs
 *
 */
public class Edge {

	private Node from;
	private Node to;
	private String label;
	
	public Edge(Node from, Node to) {
		this(from, to, null);
	}
	public Edge(Node from, Node to, String label) {
		this.from = from;
		this.to = to;
		this.label = label;
	}

	public Node getFrom() {
		return from;
	}

	public Node getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		String fromString = from != null ? from.toString() : "START";
		String toString = to != null ? to.toString() : "END";
		String labelString = label != null ? "'"+label+"'" : "";
		return fromString + " " + toString + " " + labelString;
	}
}
