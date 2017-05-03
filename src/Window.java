import Core.Notebook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by aa on 03 May 2017.
 */
public class Window extends JFrame {
    private final String VERSION = "0.1 Alpha";
    private static final long serialVersionUID = -8255319694373975038L;
    JSplitPane splitLeft, splitRight;

    private Sidebar sidebar = new Sidebar();
    private NoteList noteList = new NoteList();
    private NoteEditor noteEditor = new NoteEditor();
    private Notebooks notebooks = new Notebooks(this);

    public Window() {
        setTitle("Noted " + VERSION);
        setSize(1227, 724);

        createMenu();

        splitLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitLeft.setResizeWeight(0.2);
        splitLeft.setContinuousLayout(true);
        splitLeft.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitRight.setResizeWeight(0.5);
        splitRight.setContinuousLayout(true);
        splitRight.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JTextField t1 = new JTextField("SIDEBAR");
        JTextField t2 = new JTextField("NOTELIST");
        JTextField t3 = new JTextField("NOTES");

        splitLeft.setLeftComponent(sidebar);
        splitLeft.setRightComponent(splitRight);

        splitRight.setLeftComponent(noteList);
        splitRight.setRightComponent(noteEditor);

        add(splitLeft, BorderLayout.CENTER);
    }

    private void showNotes() {
        splitLeft.setRightComponent(splitRight);
    }

    private void showNotebooks() {
        splitLeft.setRightComponent(notebooks);
    }

    private void showTags() {

    }

    public void showNotebook(Notebook notebook) {
        showNotes();
        noteList.load(notebook);
    }

    ActionListener newNotebookAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNotebooks();
        }
    };

    ActionListener showNotesAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNotes();
        }
    };

    ActionListener showNotebooksAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNotebooks();
        }
    };

    ActionListener showTagsAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showTags();
        }
    };

    private void createMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");

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

}
