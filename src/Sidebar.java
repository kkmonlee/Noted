import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Created by aa on 03 May 2017.
 */
public class Sidebar extends Background {

    private static final long serialVersionUID = -1733165438644879034L;
    private static Image tile;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/sidebar.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Sidebar() {
        super(tile);
    }
}
