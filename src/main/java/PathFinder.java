import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.AStarShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class PathFinder {

    private final Graph<GraphNode, DefaultEdge> graph;

    public PathFinder(MapGraph mapGraph) {
        this.graph = new SimpleGraph<>(DefaultEdge.class);
        mapGraph.getNodes().forEach(graph::addVertex);
        mapGraph.getNodes().forEach(node -> node.getNeighbours().forEach(neighbour -> graph.addEdge(node, neighbour)));
    }

    public GraphPath<GraphNode, DefaultEdge> findPath(GraphNode startNode, GraphNode endNode) {
        return new AStarShortestPath<>(graph).getShortestPath(startNode, endNode, this::countDistance);
    }

    private double countDistance(GraphNode source, GraphNode target) {
        int dx = Math.abs(source.getImageX() - target.getImageX());
        int dy = Math.abs(source.getImageY() - target.getImageY());
        return (dx + dy) + (Math.sqrt(2) - 2) * Math.min(dx, dy);
    }


}
