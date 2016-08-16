import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphSparser {

    private final MapGraph mapGraph;

    public GraphSparser(MapGraph mapGraph) {
        this.mapGraph = mapGraph;
    }

    public MapGraph sparseGraph() {
        for (GraphNode graphNode : mapGraph.getNodes()) {
            if (graphNode.getNeighbours().size() == 8 && graphNode.getNeighbours().stream().allMatch(node -> node.getRank() == 0)) {
                collapseNode(graphNode);
            }
        }

        return mapGraph;
    }

    private void collapseNode(GraphNode graphNode) {
        Set<GraphNode> neighbours = graphNode.getNeighbours();
        graphNode.getNeighbours().clear();
        for (GraphNode neighbour : neighbours) {
            for (GraphNode secondNeighbour : neighbour.getNeighbours()) {
                secondNeighbour.addNeighbour(graphNode);
                graphNode.addNeighbour(secondNeighbour);
                secondNeighbour.getNeighbours().remove(neighbour);
            }
            graphNode.getNeighbours().remove(neighbour);
        }

        graphNode.incRank();
//        graphNode.getNeighbours().forEach(GraphNode::incRank);
    }

}
