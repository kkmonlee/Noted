import Core.Note;
import Core.Notebook;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 03 May 2017.
 */
public class NoteList extends Background {

    private static final long serialVersionUID = 3048147426769410426L;
    private static Image tile, noteShadow, noteSelection;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/notelist.png"));
            noteShadow = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/noteShadow.png"));
            noteSelection = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/noteSelection.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final private Color colorNoteBorder = Color.decode("#cdcdcd");
    private JPanel main;
    private Window window;
    private Notebook notebook;
    private NoteItem selectedNote;
    private ArrayList<NoteItem> noteItems = new ArrayList<>();

    public NoteList(Window w) {
        super(tile);
        window = w;
        createComponents();
    }

    private void createComponents() {
        main = new JPanel();
        main.setLayout(null);
        add(main);

        main.addMouseListener(new CustomMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.onNoteListClicked(e);
            }
        });
    }

    public void load(Notebook notebook) {
        this.notebook = notebook;

        main.removeAll();
        noteItems.clear();

        Insets insets = main.getInsets();
        Dimension size;
        int y = 12;

        List<Note> list = notebook.getNotes();

        for (Note n : list) {
            NoteItem item = new NoteItem(n);
            main.add(item);
            noteItems.add(item);

            size = item.getPreferredSize();
            item.setBounds(12 + insets.left, y + insets.top, size.width, size.height);
            y += size.height;
        }
    }

    public void changeSelection(int delta) {
        int len = main.getComponentCount();
        int select = -1;

        if (selectedNote == null) {
            if (len > 0) {
                if (delta < 0) {
                    select = len - 1;
                } else {
                    select = 0;
                }
            }
        } else {
            int currentIndex = noteItems.indexOf(selectedNote);
            select = currentIndex + delta;
        }

        if (select >= 0 && select < len) {
            deselectAll();

            NoteItem noteItem = noteItems.get(select);
            noteItem.setSelected(true);
            selectedNote = noteItem;
            window.showNote(noteItem.note);
        }
    }

    private void deselectAll() {
        for (NoteItem i : noteItems) {
            i.setSelected(false);
        }
    }

    public void unfocusEditor() {
        if (selectedNote != null) {
            selectedNote.requestFocusInWindow();
        }
    }

    public void newNote() {
        try {
            Note newNote = notebook.newNote();
            load(notebook);
            selectNote(newNote);
            window.showNote(newNote);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateThumb(Note note) {
        for (NoteItem item : noteItems) {
            if (item.note == note) {
                item.updateThumb();
            }
        }
    }

    private void selectNote(Note n) {
        deselectAll();
        for (NoteItem i : noteItems) {
            if (i.note == n) {
                i.setSelected(true);
                selectedNote = i;
                return;
            }
        }
    }

    class NoteItem extends JPanel implements MouseListener {

        private static final long serialVersionUID = 3619164083899104160L;
        private Note note;
        private Dimension size = new Dimension(196, 196);
        private JLabel name;
        private JTextArea preview;
        private Background root;

        public NoteItem(Note n) {
            super();
            note = n;

            setLayout(new BorderLayout());

            root = new Background(noteShadow, 2);
            root.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
            root.setMinimumSize(size);
            root.setMaximumSize(size);

            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.setBackground(Color.WHITE);
            p.setBorder(BorderFactory.createLineBorder(colorNoteBorder, 1));

            name = new JLabel(n.getMeta().title());
            name.setFont(Window.fontH1);
            name.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            p.add(name, BorderLayout.NORTH);

            JPanel previewPane = new JPanel();
            previewPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
            previewPane.setLayout(new GridLayout(1, 1));
            previewPane.setBackground(Color.WHITE);

            preview = new JTextArea();
            preview.setEditable(false);
            preview.setFont(Window.fontSmall);
            preview.setText(getContentPreview());
            preview.setBackground(Color.WHITE);

            previewPane.add(preview);

            p.add(previewPane, BorderLayout.CENTER);

            root.addOpaque(p, BorderLayout.CENTER);
            add(root, BorderLayout.CENTER);

            p.addMouseListener(this);
            preview.addMouseListener(this);
        }

        private String getContentPreview() {
            String contents = note.contents();
            if (contents.length() > 200) {
                contents = contents.substring(0, 200) + "...";
            }
            return contents;
        }

        public void updateThumb() {
            name.setText(note.getMeta().title());
            preview.setText(getContentPreview());
        }

        public void setSelected(boolean b) {
            if (b)
                root.setImage(noteSelection);
            else
                root.setImage(noteShadow);
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

            if (e.getClickCount() == 1) {
                selectedNote = NoteItem.this;
                window.showNote(note);
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
