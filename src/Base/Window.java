package Base;

import Core.Library;
import Core.LibraryEvent;
import Core.Note;
import Core.Notebook;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Created by aa on 03 May 2017.
 */
public class Window extends JFrame {

    public static final Font fontH1 = Font.decode("Helvetica-BOLD-16");
    public static final Font fontSmall = Font.decode("Helvetica-10");
    public static final Font fontEditor = Font.decode("Arial-13");
    public static final Font fontBoldNormal = Font.decode("Helvetica-BOLD-14");
    public static final Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    private static final long serialVersionUID = -8255319694373975038L;
    private final String VERSION = "0.1 Alpha";
    private Sidebar sidebar = new Sidebar(this);
    private NoteList noteList = new NoteList(this);
    ActionListener saveNoteAction = e -> unfocusedEditor();
    private NoteEditor noteEditor = new NoteEditor(this);
    private JSplitPane splitLeft, splitRight;
    private Modes modes;
    private ActionListener showNotesAction = e -> showNotes();
    private ActionListener showTagsAction = e -> showTags();
    private ActionListener newNoteAction = e -> {
        showNotes();
        newNote();
    };
    private Notebooks notebooks = new Notebooks(this);

    private ActionListener newNotebookAction = e -> {
        showNotebooks();
        notebooks.newNotebook();
    };

    private ActionListener showNotebooksAction = e -> showNotebooks();

    public Window() {
        setTitle("Base.Noted " + VERSION);
        setSize(1080, 1050);

        Noted.eventBus.register(this);

        createMenu();
        createSplit();
        createToolbar();

        showNotebook(Library.getInstance().getBooks().get(1));

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());

        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/Noted-logo_icon_128.png"));
        ImageIcon icon = new ImageIcon(image);
        setIconImage(icon.getImage());
    }

    private void showNotes() {
        splitLeft.setRightComponent(splitRight);
        modes = Modes.notes;
    }

    private void showNotebooks() {
        splitLeft.setRightComponent(notebooks);
        modes = Modes.notebooks;
    }

    private void showTags() {
        modes = Modes.tags;
    }

    public void showNotebook(Notebook notebook) {
        showNotes();
        noteEditor.clear();
        noteList.load(notebook);
        noteList.changeSelection(1, 0);
        noteList.unfocusEditor();
    }

    public void showNote(Note note) {
        showNotes();
        noteEditor.clear();
        noteEditor.load(note);
    }

    public void unfocusedEditor() {
        noteList.unfocusEditor();
    }

    public void newNote() {
        noteList.newNote();
        focusEditor();
    }

    private void createMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem newNote = new JMenuItem("New Note");
        newNote.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
        newNote.addActionListener(newNoteAction);
        file.add(newNote);

        JMenuItem newNotebook = new JMenuItem("New Notebook");
        newNotebook.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK | ActionEvent.SHIFT_MASK));
        newNotebook.addActionListener(newNotebookAction);
        file.add(newNotebook);

        file.addSeparator();

        JMenuItem saveNote = new JMenuItem("Save");
        saveNote.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
        saveNote.addActionListener(saveNoteAction);
        file.add(saveNote);

        JMenu edit = new JMenu("Edit");
        JMenu view = new JMenu("View");

        JMenuItem notes = new JMenuItem("Notes");
        notes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.META_MASK | ActionEvent.ALT_MASK));
        notes.addActionListener(showNotesAction);
        view.add(notes);

        JMenuItem tags = new JMenuItem("Tags");
        tags.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.META_MASK | ActionEvent.ALT_MASK));
        tags.addActionListener(showTagsAction);
        view.add(tags);

        mb.add(file);
        mb.add(edit);
        mb.add(view);

        setJMenuBar(mb);
    }

    private void createSplit() {
        splitLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitLeft.setResizeWeight(0.2);
        splitLeft.setContinuousLayout(true);
        splitLeft.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitRight.setResizeWeight(0.5);
        splitRight.setContinuousLayout(true);
        splitRight.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        splitLeft.setLeftComponent(sidebar);
        splitLeft.setRightComponent(splitRight);

        splitRight.setLeftComponent(noteList);
        splitRight.setRightComponent(noteEditor);

        add(splitLeft, BorderLayout.CENTER);
    }

    private void createToolbar() {
        JPanel tools = new JPanel();
        tools.setBackground(Color.decode("#b6b6b6"));
        tools.setPreferredSize(new Dimension(1920, 40));

        add(tools, BorderLayout.NORTH);
    }

    public void onNoteListClicked(MouseEvent e) {
        noteEditor.unfocus();
    }

    @Subscribe
    public void handleNoteChanged(NoteChangedEvent event) {
        noteList.updateThumb(event.note);
    }

    public void openShortcut(String target) {
        File f = new File(target);
        if (f.exists()) {
            noteEditor.saveChanges();
            if (f.isDirectory()) {
                Notebook notebook = Library.getInstance().findNotebook(f);
                if (notebook != null) {
                    showNotebook(notebook);
                }
            } else {
                File folder = f.getParentFile();
                Notebook notebook = Library.getInstance().findNotebook(folder);
                if (notebook != null) {
                    Note note = notebook.find(f.getName());
                    if (note != null) {
                        showNotebook(notebook);
                        noteList.selectNote(note);
                        showNote(note);
                    }
                }
            }
        }
    }

    public void focusEditor() {
        noteEditor.focusTitle();
    }

    public void sortAndUpdate() {
        noteList.sortAndUpdate();
    }

    @Subscribe
    public void handleLibraryEvent(LibraryEvent event) {
        if (event.kind == LibraryEvent.Kind.notebookListChanged) {
            notebooks.refresh();
        }
    }

    enum Modes {
        notebooks, notes, tags
    }

    private class KeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            switch (modes) {
                case notes:
                    if (!noteEditor.hasFocus()) {
                        switch (e.getID()) {
                            case KeyEvent.KEY_PRESSED:
                                switch (e.getKeyCode()) {
                                    case KeyEvent.VK_UP:
                                        noteList.changeSelection(-1, e.getKeyCode());
                                        break;
                                    case KeyEvent.VK_DOWN:
                                        noteList.changeSelection(1, e.getKeyCode());
                                        break;
                                    case KeyEvent.VK_LEFT:
                                        noteList.changeSelection(-1, e.getKeyCode());
                                        break;
                                    case KeyEvent.VK_RIGHT:
                                        noteList.changeSelection(1, e.getKeyCode());
                                        break;
                                    case KeyEvent.VK_BACK_SPACE:
                                        noteEditor.clear();
                                        noteList.deleteSelected();
                                }
                                break;
                        }
                    }
                    break;
                case notebooks:
                    if (!notebooks.isEditing()) {
                        switch (e.getID()) {
                            case KeyEvent.KEY_PRESSED:
                                switch (e.getKeyCode()) {
                                    case KeyEvent.VK_UP:
                                    case KeyEvent.VK_LEFT:
                                        notebooks.changeSelection(-1, e.getKeyCode());
                                        break;
                                    case KeyEvent.VK_DOWN:
                                    case KeyEvent.VK_RIGHT:
                                        notebooks.changeSelection(1, e.getKeyCode());
                                        break;
                                    case KeyEvent.VK_ENTER:
                                        notebooks.openSelected();
                                        break;
                                }
                        }
                    }
                    break;
            }
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED:
                    if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_9) {
                        if ((e.getModifiers() & KeyEvent.META_MASK) == KeyEvent.META_MASK && (e.getModifiers() & KeyEvent.ALT_MASK) == 0) {
                            String target = sidebar.shortcuts.getTarget(e.getKeyCode() - KeyEvent.VK_1);
                            openShortcut(target);
                        }
                    }
                    break;
            }
            return false;
        }
    }
}
