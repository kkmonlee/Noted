package Base;

import Core.Library;
import Core.LibraryEvent;
import Core.Notebook;
import com.google.common.eventbus.Subscribe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private int itemsPerRow;
    private boolean isEditing = false;
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

        addComponentListener(new ResizeListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutItems();
            }
        });
    }

    @Subscribe
    public void handleNotebookEvent(NotebookEvent event) {
        refresh();
        revalidate();
    }

    public void refresh() {
        update();
        layoutItems();
    }

    private void createComponents() {
        main = new JPanel();
        main.setLayout(null);

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(Window.emptyBorder);
        scroll.getHorizontalScrollBar().setUnitIncrement(5);

        add(scroll);

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

        java.util.List<Notebook> list = Library.getInstance().getBooks();

        for (Notebook nb : list) {
            NotebookItem item = new NotebookItem(nb);
            main.add(item);
            notebookItems.add(item);
        }

    }

    private void layoutItems() {
        Insets insets = main.getInsets();
        Dimension size = new Dimension();

        int xOff = 12 + insets.left;
        int x = 0;
        int y = 12;

        Rectangle b = main.getBounds();

        for (NotebookItem item : notebookItems) {
            size = item.getPreferredSize();
            itemsPerRow = b.height / size.height;

            item.setBounds(xOff + x, y + insets.top, size.width, size.height);

            y += size.height;

            if (y + size.height > b.height) {
                x += size.width + xOff;
                y = 12;
            }
        }

        Dimension d = main.getPreferredSize();
        d.width = x + size.width + xOff * 2;
        main.setPreferredSize(d);

        revalidate();
    }

    private void deselectAll() {
        if (selectedNotebook != null) {
            selectedNotebook.setSelected(false);
            selectedNotebook = null;
        }
    }

    public void changeSelection(int delta, int keyCode) {
        int len = notebookItems.size();
        int select = -1;

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            delta *= itemsPerRow;
        }

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

    public void newNotebook() {
        try {
            Notebook nb = Notebook.newNotebook();
            NotebookItem newItem = new NotebookItem(nb);
            newItem.setEditable();
            notebookItems.add(0, newItem);
            main.add(newItem, 0);
            layoutItems();

            deselectAll();
            newItem.edit.requestFocusInWindow();
            isEditing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEditing() {
        return isEditing;
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
        private JTextField edit;

        public NotebookItem(Notebook nb) {
            super(notebookBg);

            setLayout(null);

            notebook = nb;

            name = new JLabel(nb.name());
            name.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            name.setForeground(Color.DARK_GRAY);

            count = new JLabel(String.valueOf(nb.count()), SwingConstants.CENTER);
            count.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
            count.setForeground(Color.LIGHT_GRAY);

            add(name);
            add(count);

            name.setBounds(0, 0, 200, 51);
            count.setBounds(202, 0, 52, 51);

            addMouseListener(this);
        }

        public void setEditable() {
            edit = new JTextField();
            edit.setText(notebook.name());
            edit.setSelectionStart(0);
            edit.setSelectionEnd(notebook.name().length());
            edit.setMaximumSize(new Dimension(200, 30));
            remove(name);
            add(edit);

            edit.setBounds(12, 10, 180, 31);

            edit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    doneEditing();
                }
            });

            edit.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {

                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        cancelEditing();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {

                }
            });
        }

        protected void doneEditing() {
            String s = edit.getText();
            if (notebook.rename(s)) {
                isEditing = false;
                Noted.eventBus.post(new LibraryEvent(LibraryEvent.Kind.notebookCreated));
                Noted.eventBus.post(new LibraryEvent(LibraryEvent.Kind.notebookListChanged));

                for (NotebookItem item : notebookItems) {
                    if (item.notebook.equals(notebook.folder())) {
                        selectedNotebook = item;
                        item.setSelected(true);
                    }
                }
            }
        }

        protected void cancelEditing() {
            try {
                notebook.folder().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

            isEditing = false;
            Noted.eventBus.post(new LibraryEvent(LibraryEvent.Kind.notebookListChanged));
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
            if (isEditing) {
                return;
            }
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
