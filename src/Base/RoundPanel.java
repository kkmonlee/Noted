package Base;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by aa on 10 May 2017.
 */
public class RoundPanel extends JPanel {
    private static final long serialVersionUID = 124351161880306474L;

    static Image[] note = new Image[8];

    static {
        try {
            for (int i = 0; i < 8; i++) {
                note[i] = ImageIO.read(RoundPanel.class.getClass().getResourceAsStream(String.format("images/note%d.png", i + 1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension d = getSize();
        Rectangle r = new Rectangle(0, 0, d.width, d.height);
        g.drawImage(note[0], 0, 0, null);

        for (int x = 5; x < r.width - 5; x += 5) {
            g.drawImage(note[1], x, 0, null);
        }

        g.drawImage(note[2], r.width - 5, 0, null);

        for (int y = 5; y < r.height - 5; y += 5) {
            g.drawImage(note[3], 0, y, null);
            g.drawImage(note[4], r.width - 5, y, null);
        }

        g.drawImage(note[5], 0, r.height - 5, null);

        for (int x = 5; x < r.width - 5; x += 5) {
            g.drawImage(note[6], x, r.height - 5, null);
        }

        g.drawImage(note[7], r.width - 5, r.height - 5, null);
    }
}
