package algorithms;

import entities.Edge;
import interfaces.MinimumSpanningTree;

import java.util.*;

public class Kruskal implements MinimumSpanningTree {
    private int vertices;
    private List<Edge> edges;

    public Kruskal(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int dest, int weight) {
        edges.add(new Edge(source, dest, weight));
    }

    private int find(int[] parent, int i) {
        if (parent[i] == -1)
            return i;
        return find(parent, parent[i]);
    }

    private void union(int[] parent, int x, int y) {
        int xset = find(parent, x);
        int yset = find(parent, y);
        parent[xset] = yset;
    }

    @Override
    public List<Edge> findMST() {
        List<Edge> result = new ArrayList<>();
        Collections.sort(edges, Comparator.comparingInt(e -> e.weight));

        int[] parent = new int[vertices];
        Arrays.fill(parent, -1);

        for (Edge edge : edges) {
            int x = find(parent, edge.source);
            int y = find(parent, edge.dest);

            if (x != y) {
                result.add(edge);
                union(parent, x, y);
                if (result.size() == vertices - 1)
                    break;
            }
        }
        return result;
    }
}
