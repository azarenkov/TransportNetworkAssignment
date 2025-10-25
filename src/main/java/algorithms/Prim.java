package algorithms;

import entities.Edge;
import interfaces.MinimumSpanningTree;

import java.util.*;

public class Prim implements MinimumSpanningTree {
    private int vertices;
    private LinkedList<Edge>[] adjacencyList;

    public Prim(int vertices) {
        this.vertices = vertices;
        adjacencyList = new LinkedList[vertices];
        for (int i = 0; i < vertices; i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int source, int dest, int weight) {
        adjacencyList[source].add(new Edge(source, dest, weight));
        adjacencyList[dest].add(new Edge(dest, source, weight));
    }

    @Override
    public List<Edge> findMST() {
        boolean[] inMST = new boolean[vertices];
        PriorityQueue<Edge> priorityQueue =
                new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        List<Edge> mst = new ArrayList<>();

        inMST[0] = true;
        priorityQueue.addAll(adjacencyList[0]);

        while (!priorityQueue.isEmpty()) {
            Edge currentEdge = priorityQueue.poll();

            if (!inMST[currentEdge.dest]) {
                mst.add(currentEdge);
                inMST[currentEdge.dest] = true;

                for (Edge edge : adjacencyList[currentEdge.dest]) {
                    if (!inMST[edge.dest]) {
                        priorityQueue.add(edge);
                    }
                }
            }
        }
        return mst;
    }
}
