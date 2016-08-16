import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphDrawer {

    private final MapImage image;
    private MapGraph graph;

    public GraphDrawer(MapImage image) {
        this.image = image;
    }

    public void drawGraph(MapGraph graph) {

        graph.getNodes().forEach(node -> {
            node.getNeighbours().forEach(neighbour -> {
                int dx = Math.abs(neighbour.getImageX() - node.getImageX());
                int dy = Math.abs(neighbour.getImageY() - node.getImageY());

                List<Integer> xCoords;
                List<Integer> yCoords;

                if (dx > dy) {
                    xCoords = createRange(node.getImageX(), neighbour.getImageX());
                    yCoords = countFunctionValueRange(node.getImageY(), neighbour.getImageY(), dx);
                } else {
                    xCoords = countFunctionValueRange(node.getImageX(), neighbour.getImageX(), dy);
                    yCoords = createRange(node.getImageY(), neighbour.getImageY());
                }
                IntStream.range(0, xCoords.size())
                        .forEach(i -> image.getImage().setRGB(xCoords.get(i), yCoords.get(i), 523255));
            });
        });


        try {
            ImageIO.write(image.getImage(), "png", new File("D:/file.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
