import Core.Library;
import Core.Note;
import Core.Notebook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by aa on 03 May 2017.
 */
public class Window extends JFrame {
    public static final Font fontH1 = Font.decode("Arial-BOLD-16");
    public static final Font fontSmall = Font.decode("Arial-10");
    private static final long serialVersionUID = -8255319694373975038L;
    private final String VERSION = "0.1 Alpha";
    JSplitPane splitLeft, splitRight;
    Modes modes;

    ActionListener showNotesAction = e -> showNotes();

    ActionListener showTagsAction = e -> showTags();

    private Sidebar sidebar = new Sidebar();
    private NoteList noteList = new NoteList(this);
    private NoteEditor noteEditor = new NoteEditor(this);

    ActionListener newNoteAction = e -> {
        showNotes();
        newNote();
    };

    private Notebooks notebooks = new Notebooks(this);

    ActionListener newNotebookAction = e -> showNotebooks();

    ActionListener showNotebooksAction = e -> showNotebooks();

    public Window() {
        setTitle("Noted " + VERSION);
        setSize(1080, 1050);

        createMenu();
        createSplit();
        createToolbar();

        showNotebook(Library.getInstance().getBooks().get(1));

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
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
        noteList.changeSelection(1);
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

    public void updateThumb(Note note) {
        noteList.updateThumb(note);
    }

    enum Modes {
        notebooks, notes, tags
    }

    private class KeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            switch (modes) {
                case notes:
                    switch (e.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_UP:
                                    if (!noteEditor.hasFocus()) {
                                        noteList.changeSelection(-1);
                                    }
                                    break;
                                case KeyEvent.VK_DOWN:
                                    if (!noteEditor.hasFocus()) {
                                        noteList.changeSelection(1);
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    }

    public void focusEditor() {
        noteEditor.focusTitle();
    }

}
