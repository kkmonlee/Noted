import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Created by aa on 03 May 2017.
 */
public class NoteEditor extends Background {

    private static final long serialVersionUID = 5226641103819463968L;
    private static Image tile;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/noteeditor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NoteEditor() {
        super(tile);
    }
}
