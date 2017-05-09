package Base;

import Core.Library;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by aa on 03 May 2017.
 */
public class Sidebar extends Background {

    private static final long serialVersionUID = -1733165438644879034L;
    private static Image tile;
    private Window window;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/sidebar.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SidebarList shortcuts;

    public Sidebar(Window w) {
        super(tile);
        window = w;

        shortcuts = new SidebarList(window);
        shortcuts.load(new File(Library.getInstance().getHome() + File.separator + ".shortcuts"));

        shortcuts.setOpaque(false);
        add(shortcuts, BorderLayout.NORTH);
    }
}
