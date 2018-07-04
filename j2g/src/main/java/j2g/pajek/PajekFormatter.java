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
	
	private final String END_NODE_NAME = "-1";
	
	public PajekFormatter(Graph graph) {
		this.graph = graph;
	}
	
	public StringBuilder getNodes() {
		StringBuilder sb = new StringBuilder();
		sb.append("* Vertices " + (graph.getNodes().size()+1) + "\n");
		for(Node n : graph.getSortedNodes()) {
			sb.append(n.getExternalID() + " \"node for instruction handle: " + n.getIh() +"\"\n");
		}
		sb.append(END_NODE_NAME + "\"the end node\"\n");
		return sb;
	}
	
	public StringBuilder getEdges() {
		StringBuilder sb = new StringBuilder();
		sb.append("* Arcs " + graph.getEdges().size() + "\n");
		for(Edge e : graph.getEdges()) {
			if(e.getTo() == null) {
				sb.append(e.getFrom().getExternalID() + " " + END_NODE_NAME + " \n");
			} else {
				sb.append(e.getFrom().getExternalID() + " " + e.getTo().getExternalID() + "\n");
			}
		}
		return sb;
	}
}
