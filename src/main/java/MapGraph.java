import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.sun.istack.internal.Nullable;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapGraph {

    private final List<GraphNode> nodes;

    // TODO
    private static MapImage image;

    public MapGraph(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public static MapGraph analyzeImage(MapImage image, float xPrecision, float yPrecision) {
        Table<Integer, Integer, GraphNode> nodesTable = HashBasedTable.create();

        // TODO
        MapGraph.image = image;

        int xSize = image.getImage().getWidth() - 1;
        int ySize = image.getImage().getHeight() - 1;

        int xStep = Math.round(xSize * xPrecision);
        int yStep = Math.round(ySize * yPrecision);

        IntStream.rangeClosed(0, ySize).filter(y -> y % yStep == 0).forEach(y ->
            IntStream.rangeClosed(0, xSize).filter(x -> x % xStep == 0).forEach(x -> {
                int i = x / xStep;
                int j = y / yStep;

                /* create nodes for every n-th pixel */
                GraphNode currentNode = new GraphNode(x, y);
                nodesTable.put(i, j, currentNode);

                /* connect nodes, if there's path between them */
                connectIfPossible(currentNode, nodesTable.get(i - 1, j - 1), image.getMapPassagesTable());
                connectIfPossible(currentNode, nodesTable.get(i, j - 1), image.getMapPassagesTable());
                connectIfPossible(currentNode, nodesTable.get(i + 1, j - 1), image.getMapPassagesTable());
                connectIfPossible(currentNode, nodesTable.get(i - 1, j), image.getMapPassagesTable());
            })
        );

        return new MapGraph(new ArrayList<>(nodesTable.values()));
    }

    private static void connectIfPossible(GraphNode currentNode, @Nullable GraphNode otherNode,
                                          Table<Integer, Integer, Boolean> mapPassagesTable) {
        if (otherNode != null && isConnectionPossible(currentNode, otherNode, mapPassagesTable)) {
            currentNode.addNeighbour(otherNode);
            otherNode.addNeighbour(currentNode);
        }
    }

    private static boolean isConnectionPossible(GraphNode currentNode, GraphNode otherNode, Table<Integer, Integer, Boolean> mapPassagesTable) {
        int dx = Math.abs(otherNode.getImageX() - currentNode.getImageX());
        int dy = Math.abs(otherNode.getImageY() - currentNode.getImageY());

        List<Integer> xCoords;
        List<Integer> yCoords;

        if (dx > dy) {
            xCoords = createRange(currentNode.getImageX(), otherNode.getImageX());
            yCoords = countFunctionValueRange(currentNode.getImageY(), otherNode.getImageY(), dx);
        } else {
            xCoords = countFunctionValueRange(currentNode.getImageX(), otherNode.getImageX(), dy);
            yCoords = createRange(currentNode.getImageY(), otherNode.getImageY());
        }

        return IntStream.range(0, xCoords.size())
                .allMatch( i -> Boolean.TRUE.equals(mapPassagesTable.get(xCoords.get(i), yCoords.get(i))));
    }

    private static List<Integer> createRange(int startInclusive, int endInclusive) {
        if (startInclusive < endInclusive) {
            return IntStream.rangeClosed(startInclusive, endInclusive).boxed().collect(Collectors.toList());
        } else {
            return Lists.reverse(IntStream.rangeClosed(endInclusive, startInclusive).
                            boxed().collect(Collectors.toList()));
        }
    }

    private static List<Integer> countFunctionValueRange(int start, int end, int count) {
        float step = (float) (end - start) / count;
        return IntStream.rangeClosed(0, count).boxed()
                .map(i -> start + Math.round(step * i))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        try {
            MapGraph mapGraph = MapGraph.analyzeImage(new MapImage(ImageIO.read(new File("D:\\workspace\\MapPoc\\src\\main\\resources\\posnania_lvl_1.png")), -5000269), 0.02f, 0.04f);
            new GraphDrawer(image).drawGraph(new GraphSparser(mapGraph).sparseGraph());
//            Random r = new Random();
//
//            List<GraphNode> n = mapGraph.getNodes().stream().filter(x -> !x.getNeighbours().isEmpty()).collect(Collectors.toList());
//
//            GraphNode start = n.get(0);
//            GraphNode end = n.get(n.size() - 1);
//
//            GraphPath<GraphNode, DefaultEdge> path = new PathFinder(mapGraph).findPath(start, end);
//
//            List<GraphNode> newNodes = new ArrayList<>();
//            newNodes.add(new GraphNode(path.getStartVertex().getImageX(), path.getStartVertex().getImageY()));
//            GraphNode curNode = path.getStartVertex();
//            for (DefaultEdge defaultEdge : path.getEdgeList()) {
//                curNode.getNeighbours().add(path.getGraph().getEdgeTarget(defaultEdge));
//                curNode = path.getGraph().getEdgeTarget(defaultEdge);
//                newNodes.add(curNode);
//            }
//
//            new GraphDrawer(image).drawGraph(new MapGraph(newNodes));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
