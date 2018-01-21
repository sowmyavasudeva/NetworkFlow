package graphCode; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

/**
 * Computes the maximum flow in the network using Ford Fulkerson algorithm
 * @author keerthanaa
 *
 */

public class FordFulkerson {

	// augmented path initialization
	static List<Object> augmentedPath;
	
	
	/**
	 * Depth first search to compute a simple s-t path, updates the augmentedPath with the list of edges in s-t path
	 * uses hashset to track visited nodes
	 * @param v
	 * @param visited
	 * @param path
	 */
	public static void depthFirstSearchFord(Vertex v, HashSet<Object> visited, List<Object> path) {
		
		//if visited already contains the vertex v
		if(visited.contains(v.getName()) || augmentedPath != null){
			return;
		}
		
		//add vertex v to visited hashset
		visited.add(v.getName());
		
		//if vertex v is sink, return the augmented path
		if (v.getName().equals("t")) {
			augmentedPath = new ArrayList<> (path);
		}
		
		//Recursive case if none of the boundary cases check out
		List<Edge> edges = v.incidentEdgeList;
		for (Edge e : edges) {
			if(e.getFirstEndpoint().getName().equals(v.getName())) {
				if((Double) e.getData() >= 1) {
					path.add(e);
					//recursively call dfs
					depthFirstSearchFord(e.getSecondEndpoint(), visited, path);
					//to remove an unsuccesful attempt at a node
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	/**
	 * function to compute the augmenting path, calculate bottleneck and update residual graph
	 * returns the new flow that can be pushed
	 * @param newgraph
	 * @param ht
	 * @return
	 */
	public static double augmentedPathFord(SimpleGraph newgraph, Hashtable ht) {
		//Initializations
		double bottleneck = 0;
		augmentedPath = null;
		List<Object> path = new ArrayList<>();
		HashSet<Object> visited = new HashSet<>();
		
		//get source to call dfs upon
		Vertex source = (Vertex)ht.get("s");
		depthFirstSearchFord(source, visited, path);
		if(augmentedPath == null) {
			return 0;
		}
		
		//finding the bottleneck
		bottleneck = (double) ((Edge) augmentedPath.get(0)).getData();
		for (Object i : augmentedPath) {
			bottleneck = Math.min(bottleneck, (double)(((Edge) i).getData()));
		}
		
		//updating the residual graph based on the updated flow value
		for (Object e : augmentedPath) {
			Edge edge = (Edge) e;
			edge.setData((double)edge.getData()-bottleneck);
			Vertex reverseStart = edge.getSecondEndpoint();
			boolean flag=false;
			for (Object r : reverseStart.incidentEdgeList) {
				Edge revedge = (Edge)r;
				
				if(revedge.getSecondEndpoint().getName().equals(edge.getFirstEndpoint().getName())) {
					revedge.setData((double)revedge.getData()+bottleneck);
					flag=true;
					break;
				}
			}
			
			if(!flag) {
				newgraph.insertEdge(edge.getSecondEndpoint(), edge.getFirstEndpoint(), bottleneck, null);
			}	
		}
		
		return bottleneck;
	}
	
	/**
	 * Function that calls the augmentingpath, updates the flow value and returns the max flow
	 * @param newgraph
	 * @param ht
	 * @return
	 */
	public static double findMaxFlow(SimpleGraph newgraph, Hashtable ht) {
		double maxFlow = 0;
		double flow;
		
		flow = augmentedPathFord(newgraph, ht);
		
		while(flow >= 1) {
			maxFlow += flow;
			
			//increment
			flow = augmentedPathFord(newgraph, ht);
		}
		return maxFlow;
	}

}
