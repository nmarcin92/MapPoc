import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapImage {

    private final Table<Integer, Integer, Boolean> mapPassagesTable;
    private final BufferedImage image;

    public MapImage(BufferedImage image, int passageRGB) {
        this.image = image;
        this.mapPassagesTable = HashBasedTable.create();

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                mapPassagesTable.put(x, y, image.getRGB(x, y) == passageRGB);
            }
        }
    }

    public static MapImage fromFile(String filePath, int passageRGB) throws IOException {
        return new MapImage(ImageIO.read(MapImage.class.getResource(filePath)), passageRGB);
    }

    public Table<Integer, Integer, Boolean> getMapPassagesTable() {
        return mapPassagesTable;
    }

    public BufferedImage getImage() {
        return image;
    }
}
