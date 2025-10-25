import algorithms.Kruskal;
import algorithms.Prim;
import entities.Edge;
import com.google.gson.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String inputPath = "data/ass_3_input.json";
            String inputJson = Files.readString(Paths.get(inputPath));

            JsonObject input = JsonParser.parseString(inputJson).getAsJsonObject();
            JsonArray graphs = input.getAsJsonArray("graphs");

            JsonArray resultsArray = new JsonArray();

            for (JsonElement graphElement : graphs) {
                JsonObject graph = graphElement.getAsJsonObject();
                int graphId = graph.get("id").getAsInt();
                JsonArray nodes = graph.getAsJsonArray("nodes");
                JsonArray edgesArray = graph.getAsJsonArray("edges");

                Map<String, Integer> nodeToIndex = new HashMap<>();
                int index = 0;
                for (JsonElement node : nodes) {
                    nodeToIndex.put(node.getAsString(), index++);
                }

                int vertices = nodes.size();
                Prim prim = new Prim(vertices);
                Kruskal kruskal = new Kruskal(vertices);

                for (JsonElement edgeElement : edgesArray) {
                    JsonObject edge = edgeElement.getAsJsonObject();
                    int source = nodeToIndex.get(edge.get("from").getAsString());
                    int dest = nodeToIndex.get(edge.get("to").getAsString());
                    int weight = edge.get("weight").getAsInt();

                    prim.addEdge(source, dest, weight);
                    kruskal.addEdge(source, dest, weight);
                }

                List<Edge> primMST = prim.findMST();
                List<Edge> kruskalMST = kruskal.findMST();

                JsonObject graphResult = new JsonObject();
                graphResult.addProperty("graph_id", graphId);
                graphResult.add("prim", edgesToJson(primMST, nodeToIndex));
                graphResult.add("kruskal", edgesToJson(kruskalMST, nodeToIndex));
                resultsArray.add(graphResult);
            }

            JsonObject output = new JsonObject();
            output.add("results", resultsArray);

            String outputPath = "data/ass_3_output.json";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(Paths.get(outputPath), gson.toJson(output));

            System.out.println("Results written to " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject edgesToJson(List<Edge> edges, Map<String, Integer> nodeToIndex) {
        Map<Integer, String> indexToNode = new HashMap<>();
        for (Map.Entry<String, Integer> entry : nodeToIndex.entrySet()) {
            indexToNode.put(entry.getValue(), entry.getKey());
        }

        JsonArray array = new JsonArray();
        int totalWeight = 0;

        for (Edge edge : edges) {
            JsonObject obj = new JsonObject();
            obj.addProperty("from", indexToNode.get(edge.source));
            obj.addProperty("to", indexToNode.get(edge.dest));
            obj.addProperty("weight", edge.weight);
            array.add(obj);
            totalWeight += edge.weight;
        }

        JsonObject result = new JsonObject();
        result.add("edges", array);
        result.addProperty("totalWeight", totalWeight);

        return result;
    }
}
