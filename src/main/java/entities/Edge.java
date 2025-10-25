package entities;

public class Edge {
    int source;
    int dest;
    int weight;

    Edge(int source, int dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source + " - " + dest + ": " + weight;
    }
}
