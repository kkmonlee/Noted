package Base;

import Core.Library;
import Core.Notebook;
import com.google.common.eventbus.Subscribe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by aa on 03 May 2017.
 */
public class Notebooks extends Background {

    private static final long serialVersionUID = 1789204685570992009L;
    private static Image tile, notebookBg, notebookBgSelected;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notebooks.png"));
            notebookBg = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notebookBg.png"));
            notebookBgSelected = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notebookBgSelected.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Window window;
    private NotebookItem selectedNotebook;
    private JPanel main;
    private ArrayList<NotebookItem> notebookItems = new ArrayList<>();

    public Notebooks(Window w) {
        super(tile);
        window = w;
        Noted.eventBus.register(this);
        createComponents();
        update();
    }

    @Subscribe
    public void handleNotebookEvent(NotebookEvent event) {
        refresh();
    }

    public void refresh() {
        update();
    }

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
        notebookItems.clear();

        Insets insets = main.getInsets();
        Dimension size;
        int y = 12;

        java.util.List<Notebook> list = Library.getInstance().getBooks();
        int n = 0, len = list.size();

        for (Notebook nb : list) {
            NotebookItem item = new NotebookItem(nb);
            main.add(item);
            notebookItems.add(item);

            size = item.getPreferredSize();
            item.setBounds(12 + insets.left, y + insets.top, size.width, size.height);
            y += size.height;
            n++;
        }
    }

    private void deselectAll() {
        if (selectedNotebook != null) {
            selectedNotebook.setSelected(false);
            selectedNotebook = null;
        }
    }

    public void changeSelection(int delta) {
        int len = notebookItems.size();
        int select = 01;

        if (selectedNotebook == null) {
            if (len > 0) {
                if (delta < 0) {
                    select = len - 1;
                } else {
                    select = 0;
                }
            }
        } else {
            int currentIndex = notebookItems.indexOf(selectedNotebook);
            select = currentIndex + delta;
        }

        if (select >= 0 && select < len) {
            deselectAll();

            NotebookItem item = notebookItems.get(select);
            item.setSelected(true);
            selectedNotebook = item;
        }
    }

    public void openSelected() {
        if (selectedNotebook != null) {
            window.showNotebook(selectedNotebook.notebook);
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

            name = new JLabel(nb.name());
            name.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            name.setForeground(Color.DARK_GRAY);

            count = new JLabel(String.valueOf(nb.count()));
            count.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
            count.setForeground(Color.LIGHT_GRAY);

            add(name, BorderLayout.WEST);
            add(count, BorderLayout.EAST);

            addMouseListener(this);
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
