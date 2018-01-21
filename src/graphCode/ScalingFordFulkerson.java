package graphCode; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Computes the maximum flow in the network using Scaling Ford Fulkerson algorithm
 * @author keerthanaa
 *
 */

public class ScalingFordFulkerson {

	//List to keep track of augmenting path edges
	static List<Object> augmentingPath;
	
	/**
	 * DFS to compute s-t paths.
	 * finds an augmenting paths such that the edge capacity is greater than  or equal to delta
	 * uses hashset to track visited nodes
	 * the s-t path is stored in augmentingPath in the end
	 * @param v
	 * @param delta
	 * @param visited
	 * @param path
	 */
	static void dfs(Vertex v, double delta, HashSet<Object> visited, List<Object> path) {
		//if node is already visited or if an augmenting path is found, return
		if(visited.contains(v.getName()) || augmentingPath != null) {
			return;
		}
		
		//add node to the visited hashset
		visited.add(v.getName());
		
		//to check if sink is reached and to initialize the augmentingPath if sink is reached
		if(v.getName().equals("t")) {
			augmentingPath = new ArrayList<>(path);
			return;
		}
		
		//recursively call dfs from the current node such that the edge in the path has a capacity >= delta
		List<Edge> edges = v.incidentEdgeList;
		for (Edge e : edges) {
			if (e.getFirstEndpoint().getName().equals(v.getName())) {
				if ((Double)e.getData() >= delta) {
					path.add(e);
					dfs(e.getSecondEndpoint(), delta, visited, path);
					path.remove(path.size()-1);
				}
			}
		}
		
	}
	
	
	/**
	 * gets an s-t path by invoking the dfs function, calculates the bottleneck and updates the residual graph
	 * returns new flow that can be pushed
	 * @param graph
	 * @param delta
	 * @param hashT
	 * @return
	 */
	static double augmentPath(SimpleGraph graph, double delta, Hashtable hashT) {
		//initializations
		augmentingPath = null;
		List<Object> path = new ArrayList<>();
		HashSet<Object> visited = new HashSet<>();
		
		//initialize source node
		Vertex s = (Vertex)hashT.get("s");
		dfs(s, delta, visited, path);
		
		//return if no augmenting path present
		if(augmentingPath == null) {
			return 0;
		}
		
		//calculate the bottleneck edge
		double bottleNeck = (Double)(((Edge)augmentingPath.get(0)).getData());
		for (Object x:augmentingPath) {
			bottleNeck = Math.min(bottleNeck, (Double)(((Edge)x).getData()));
		}
		
		//update the residual graph based on the updated flow value (to add/update forward and backward edges)
		for (Object e : augmentingPath) {
			Edge edge = (Edge)e;
			edge.setData((double)edge.getData()-bottleNeck);
			Vertex reverseStart = edge.getSecondEndpoint();
			boolean flag=false;
			for (Object r : reverseStart.incidentEdgeList) {
				
				Edge revedge = (Edge)r;
				if(revedge.getSecondEndpoint().getName().equals(edge.getFirstEndpoint().getName())) {
					revedge.setData((double)revedge.getData()+bottleNeck);
					flag=true;
					break;
				}
			}
			if(!flag) {
				graph.insertEdge(edge.getSecondEndpoint(), edge.getFirstEndpoint(), bottleNeck, null);
			}
			
		}
		return bottleNeck;
	}
	
	/**
	 * Computes delta and invokes augmentingPath function to find s-t paths
	 * Updates the flow value with the new value returned by the augmentingPath function
	 * returns maxflow vale
	 * @param graph
	 * @param hashT
	 * @return
	 */
	public static double findMaxFlow(SimpleGraph graph, Hashtable hashT) {
		//initialize source
		Object s = hashT.get("s");
		
		//calculation of delta based on the edges leaving the source s
		List<Edge> edges = ((Vertex)s).incidentEdgeList;
		double delta = 0;
		for (Edge e:edges) {
			if (e.getFirstEndpoint().getName().equals("s")) {
				delta = Math.max(delta, (Double)e.getData());
			}
		}
		
		double i=1;
		while(i<=delta) {
			i*=2;
		}
		delta = i/2;
		
		//calculate and update the flow as long as delta is greater than or equal to 1
		double maxFlow=0;
		while (delta >= 1) {
			double flow = augmentPath(graph, delta, hashT);
			maxFlow += flow;
			if (flow==0) {
				delta /= 2;
			}	
		}
		return maxFlow;
	}

}
