import java.util.Hashtable;
import java.util.Map;

import graphCode.FordFulkerson;
import graphCode.PreflowPush;
import graphCode.ScalingFordFulkerson;
import graphCode.GraphInput;
import graphCode.SimpleGraph;

public class tcss543 {
	
	public static void main(String[] args) {
		//To compute using Ford Fulkerson 
		FordFulkerson FF = new FordFulkerson();
		SimpleGraph graphFF = new SimpleGraph();
		Hashtable htFF = GraphInput.LoadSimpleGraph(graphFF, args[0]);
		double maxFlowFF =  0;
		long startTimeFF = System.currentTimeMillis();
        maxFlowFF = FF.findMaxFlow(graphFF, htFF);
        long stopTimeFF = System.currentTimeMillis();
        long elapsedTimeFF = stopTimeFF - startTimeFF;
        System.out.println("The maximum flow value using Ford Fulkerson algorithm is " + maxFlowFF);
        System.out.println("The runtime for the Ford Fulkerson algorithm is "+elapsedTimeFF+"ms");
        
        //To computing using scaling Ford Fulkerson
        ScalingFordFulkerson SFF = new ScalingFordFulkerson();
        SimpleGraph graphSFF = new SimpleGraph();
        Hashtable htSFF = GraphInput.LoadSimpleGraph(graphFF, args[0]);
        double maxFlowSFF = 0;
        long startTimeSFF = System.currentTimeMillis();
        maxFlowSFF = SFF.findMaxFlow(graphSFF,htSFF);
        long stopTimeSFF = System.currentTimeMillis();
        long elapsedTimeSFF = stopTimeSFF - startTimeSFF;
        System.out.println("The maximum flow value using Scaling Ford Fulkerson algorithm is " + maxFlowSFF);
        System.out.println("The runtime for the Scaling Ford Fulkerson algorithm is "+elapsedTimeSFF+"ms");
        
        //To compute using preflow-push
        PreflowPush PP = new PreflowPush();
        SimpleGraph graphPP = new SimpleGraph();
        GraphInput.LoadSimpleGraph(graphPP, args[0]);
        //map to hold the vertices of the graph
        Map<String, Integer> verticesMap = PreflowPush.buildVerticesMap(graphPP);
        //matrix to hold the capacity between vertices
        int[][] capacity = PreflowPush.buildCapacityMatrix(graphPP, verticesMap);
        double maxFlowPP = 0;
        long startTimePP = System.currentTimeMillis();
        maxFlowPP = PP.findMaxFlow(graphPP, verticesMap, capacity);
        long stopTimePP = System.currentTimeMillis();
        long elapsedTimePP = stopTimePP - startTimePP;
        System.out.println("The maximum flow value using Preflow-Push algorithm is " + maxFlowPP);
        System.out.println("The runtime for the Preflow-Push algorithm is "+elapsedTimePP+"ms");
	}
	
}
