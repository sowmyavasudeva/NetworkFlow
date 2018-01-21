package graphCode;

import graphCode.SimpleGraph;
import graphCode.Vertex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import graphCode.Edge;
import graphCode.GraphInput;

public class PreflowPush {

    /**
     * This method is responsible for finding maxflow of the given graph using Preflow-push algorithm
     *
     * @param graph SimpleGraph
     * @return returns integer maxflow
     */
    public double findMaxFlow(SimpleGraph graph, Map<String, Integer> verticesMap, int[][] capacity) {



        //get source and sink vertex
        int source = verticesMap.get("s");
        int sink = verticesMap.get("t");

        //total number of nodes
        int numberOfNodes = graph.numVertices();

        //intialize excess and height properties
        int[] height = new int[numberOfNodes];
        height[source] = numberOfNodes - 1;
        int[] nodeWithExcess = new int[numberOfNodes];
        int[][] edgeflow = new int[numberOfNodes][numberOfNodes];
        int[] excess = new int[numberOfNodes];

        //saturated flow to the edges connected to source, also representing forward flow
        int NumOfnodesWithExcessFlow = 0;

        Iterator vertices = graph.vertices();
        while (vertices.hasNext()) {
            Vertex vertex = (Vertex) vertices.next();
            int nodeI = verticesMap.get((String) vertex.getName());
            edgeflow[source][nodeI] = capacity[source][nodeI];
            edgeflow[nodeI][source] = -edgeflow[source][nodeI];
            excess[nodeI] = capacity[source][nodeI];
        }

        while (NumOfnodesWithExcessFlow == 0) {
            //to choose node with excess with maximum height at each step
            vertices = graph.vertices();
            while (vertices.hasNext()) {
                Vertex vertex = (Vertex) vertices.next();
                int nodeI = verticesMap.get((String) vertex.getName());
                if (nodeI != source && nodeI != sink && excess[nodeI] > 0) {
                    if (NumOfnodesWithExcessFlow != 0 && height[nodeI] > height[nodeWithExcess[0]]) {
                        NumOfnodesWithExcessFlow = 0;
                    }
                    nodeWithExcess[NumOfnodesWithExcessFlow++] = nodeI;
                }
            }

            if (NumOfnodesWithExcessFlow == 0) {
                break;
            }

            while (NumOfnodesWithExcessFlow != 0) {
                int nodeI = nodeWithExcess[NumOfnodesWithExcessFlow - 1];
                boolean pushed = false;
                for (int nodeJ = 0; nodeJ < numberOfNodes && excess[nodeI] != 0; ++nodeJ) {
                    if (height[nodeI] == height[nodeJ] + 1 && capacity[nodeI][nodeJ] - edgeflow[nodeI][nodeJ] > 0) {
                        //push operation - push non saturated flow
                        int dflow = Math.min(capacity[nodeI][nodeJ] - edgeflow[nodeI][nodeJ], excess[nodeI]);
                        edgeflow[nodeI][nodeJ] = edgeflow[nodeI][nodeJ] + dflow;
                        edgeflow[nodeJ][nodeI] = edgeflow[nodeJ][nodeI] - dflow;
                        excess[nodeI] = excess[nodeI] - dflow;
                        excess[nodeJ] = excess[nodeJ] + dflow;
                        if (excess[nodeI] == 0) {
                            --NumOfnodesWithExcessFlow;
                        }
                        pushed = true;
                    }
                }
                if (!pushed) {
                    //relabel operation - relabel when the nodeI cannot be pushed with value as it is at lower height
                    height[nodeI]++;
                    if (height[nodeI] > height[nodeWithExcess[0]]) {
                        NumOfnodesWithExcessFlow = 0;
                        break;
                    }
                }
            }
        }

        double maxflow = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            maxflow += edgeflow[source][i];
        }

        return maxflow;
    }


    /**
     *
     * @param graph SimpleGraph
     * @return Map<String, Integer> verticesmap which contains the vertices name as the key and incremented index for the vertices as value
     */
    public static Map<String, Integer> buildVerticesMap(SimpleGraph graph) {
        Map<String, Integer> verticesMap = new HashMap<>();
        Iterator vertices = graph.vertices();
        int index = 0;

        while (vertices.hasNext()) {
            Vertex vertex = (Vertex) vertices.next();
            verticesMap.put((String) vertex.getName(), index++);
        }

        return verticesMap;
    }

    /**
     * This method is ued to build capacity matrix to store the capacity of forward and backward edges
     * @param graph given SimpleGraph
     * @param verticesMap map of vertices name as key and value as the index of the vertices
     * @return 2D array which contains the capacity foe the forward and backward edges
     */
    public static int[][] buildCapacityMatrix(SimpleGraph graph, Map<String, Integer> verticesMap) {
        int[][] capacityMatrix = new int[graph.numVertices()][graph.numVertices()];
        Iterator edges = graph.edges();
        while(edges.hasNext()) {
            Edge edge = (Edge) edges.next();
            String source = (String) edge.getFirstEndpoint().getName();
            String sink = (String) edge.getSecondEndpoint().getName();
            Integer capacity = ((Double) edge.getData()).intValue();
            capacityMatrix[verticesMap.get(source)][verticesMap.get(sink)] = capacity;
        }

        return capacityMatrix;
    }

    
}