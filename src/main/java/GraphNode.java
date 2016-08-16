import java.util.HashSet;
import java.util.Set;

public class GraphNode {

    private final int imageX;
    private final int imageY;

    private int rank = 0;

    private final Set<GraphNode> neighbours = new HashSet<>();

    public GraphNode(int imageX, int imageY) {
        this.imageX = imageX;
        this.imageY = imageY;
    }

    public void addNeighbour(GraphNode neighbour) {
        this.neighbours.add(neighbour);
    }

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public int getRank() {
        return rank;
    }

    public void incRank() {
        this.rank += 1;
    }

    public void resetRank() {
        this.rank = 0;
    }

    public Set<GraphNode> getNeighbours() {
        return neighbours;
    }
}
