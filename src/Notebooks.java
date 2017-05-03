import Core.Library;
import Core.Notebook;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Created by aa on 03 May 2017.
 */
public class Notebooks extends Background {

    private static final long serialVersionUID = 1789204685570992009L;
    private static Image tile, notebookBg, notebookBgSelected;
    private Window window;
    private NotebookItem selectedNotebook;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notebooks.png"));
            notebookBg = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notebookBg.png"));
            notebookBgSelected = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notebookBgSelected.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Notebooks(Window w) {
        super(tile);
        window = w;
        createComponents();
        update();
    }

    private JPanel main;

    private void createComponents() {
        main = new JPanel();
        main.setLayout(null);
        add(main);

        main.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deselectAll();
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
        });
    }

    private void update() {
        main.removeAll();

        Insets insets = main.getInsets();
        Dimension size;
        int y = 12;

        java.util.List<Notebook> list = Library.getInstance().getBooks();
        int n = 0, len = list.size();

        for (Notebook nb : list) {
            NotebookItem item = new NotebookItem(nb);
            main.add(item);

            size = item.getPreferredSize();
            item.setBounds(12 + insets.left, y + insets.top, size.width, size.height);
            y += size.height;
            n++;
        }
    }

    private void deselectAll() {
        if (selectedNotebook != null) {
            selectedNotebook.setSelected(false);
        }
    }

    class NotebookItem extends Background implements MouseListener {
        private static final long serialVersionUID = -7285867977183764620L;

        private Notebook notebook;
        private Dimension size = new Dimension(252, 51);
        private JLabel name;
        private JLabel count;

        public NotebookItem(Notebook nb) {
            super(notebookBg);

            notebook = nb;

            JSplitPane p = new JSplitPane();
            p.setResizeWeight(0.82);
            p.setDividerSize(0);
            p.setBorder(null);

            name = new JLabel(nb.name());
            name.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            name.setForeground(Color.DARK_GRAY);
            p.setLeftComponent(name);

            count = new JLabel(String.valueOf(nb.count()));
            count.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            count.setForeground(Color.LIGHT_GRAY);
            p.setRightComponent(count);

            add(p);

            p.addMouseListener(this);
        }

        public void setSelected(boolean b) {
            if (b) {
                setImage(notebookBgSelected);
                selectedNotebook = this;
            } else {
                setImage(notebookBg);
            }
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

            if (e.getClickCount() == 2) {
                window.showNotebook(notebook);
            }
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
