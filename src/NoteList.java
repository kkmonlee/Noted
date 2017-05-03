import Core.Notebook;
import Core.Note;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

/**
 * Created by aa on 03 May 2017.
 */
public class NoteList extends Background {

    private static final long serialVersionUID = 3048147426769410426L;
    private static Image tile;
    private JPanel main;

    final private Color colorNoteBorder = Color.decode("#e2e2e2");

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notelist.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NoteList() {
        super(tile);
        createComponents();
    }

    private void createComponents() {
        main = new JPanel();
        main.setLayout(null);
        add(main);
    }

    public void load(Notebook notebook) {
        main.removeAll();

        Insets insets = main.getInsets();
        Dimension size;
        int y = 12;

        List<Note> list = notebook.getNotes();

        for (Note n : list) {
            NoteItem item = new NoteItem(n);
            main.add(item);

            size = item.getPreferredSize();
            item.setBounds(12 + insets.left, y + insets.top, size.width, size.height);
            y += size.height;
        }
    }

    private void deselectAll() {
        for (int n = 0, len = main.getComponentCount(); n < len; n++) {
            NoteItem i = (NoteItem) main.getComponent(n);
            i.setSelected(false);
        }
    }

    class NoteItem extends JPanel implements MouseListener {

        private static final long serialVersionUID = 3619164083899104160L;
        private Note note;
        private Dimension size = new Dimension(182, 182);
        private JLabel name;

        public NoteItem(Note n) {
            super();
            note = n;

            setLayout(new BorderLayout());

            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.setBackground(Color.WHITE);
            p.setBorder(BorderFactory.createLineBorder(colorNoteBorder, 1));
            p.setMinimumSize(size);
            p.setMaximumSize(size);

            name = new JLabel(n.name());
            name.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            p.add(name, BorderLayout.NORTH);

            add(p, BorderLayout.CENTER);

            p.addMouseListener(this);
        }

        public void setSelected(boolean b) {
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return size;
        }

        @Override
        public Dimension getMinimumSize() {
            return size;
        }

        @Override
        public Dimension getMaximumSize() {
            return size;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            deselectAll();
            setSelected(true);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
