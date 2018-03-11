package j2g.pajek;

import j2g.graph.Edge;
import j2g.graph.Graph;
import j2g.graph.Node;

/**
 * 
 * @author Fuchs
 *
 */
public class PajekFormatter {

	private Graph graph;
	
	public PajekFormatter(Graph graph) {
		this.graph = graph;
	}
	
	public StringBuilder getNodes() {
		StringBuilder sb = new StringBuilder();
		sb.append("* Vertices " + (graph.getNodes().size()+1) + "\n");
		for(Node n : graph.getNodes()) {
			sb.append(n.getID() + " \"node for instruction handle: " + n.getIh() +"\"\n");
		}
		sb.append("END \"the end node\"\n");
		return sb;
	}
	
	public StringBuilder getEdges() {
		StringBuilder sb = new StringBuilder();
		sb.append("* arcs " + graph.getEdges().size() + "\n");
		for(Edge e : graph.getEdges()) {
			if(e.getTo() == null) {
				sb.append(e.getFrom().getID() + " END \n");
			} else {
				sb.append(e.getFrom().getID() + " " + e.getTo().getID() + "\n");
			}
		}
		return sb;
	}
}
