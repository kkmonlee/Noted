package Base;

import Core.Note;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by aa on 03 May 2017.
 */
public class NoteEditor extends Background implements CustomEditor.EditorEventListener {

    private static final long serialVersionUID = 5226641103819463968L;
    private static Image tile;

    static {
        try {
            tile = ImageIO.read(Sidebar.class.getClass().getResourceAsStream("/images/noteeditor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final int border = 14;
    private final int noteOffset = 65;
    private final int minNoteOffset = 288;
    private JPanel main, area;
    private CustomEditor editor;
    private Window window;
    private Note currentNote;

    public NoteEditor(Window w) {
        super(tile);
        window = w;
        createComponents();
    }

    private void createComponents() {
        main = new JPanel();
        main.setLayout(null);
        main.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        JPanel tools = new JPanel();
        tools.setBackground(Color.MAGENTA);
        tools.setBounds(0, 0, 1920, 65);
        main.add(tools);

        area = new JPanel();
        area.setLayout(new GridLayout(1, 1));
        area.setBackground(Color.WHITE);

        editor = new CustomEditor();
        area.add(editor);
        area.setBounds(border, noteOffset + border, 200, minNoteOffset);

        main.add(area);

        add(main, BorderLayout.CENTER);

        addComponentListener(new ResizeListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle mb = main.getBounds();
                Rectangle ab = area.getBounds();
                ab.width = mb.width - border * 2;
                area.setBounds(ab);

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        NoteEditor.this.editor.revalidate();
                    }
                });
            }
        });

        main.addMouseListener(new CustomMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                unfocus();
            }
        });
    }

    public void clear() {
        saveChanges();
        currentNote = null;
        editor.clear();
        visible(false);
    }

    public void load(Note note) {
        currentNote = note;
        editor.setTitle(note.getMeta().title());
        editor.setText(note.contents());
        visible(true);
    }

    private void visible(boolean b) {
        main.setVisible(b);
    }

    public boolean hasFocus() {
        return editor.hasFocus();
    }

    public void unfocus() {
        window.unfocusedEditor();
    }

    public void saveChanges() {
        if (currentNote != null) {
            boolean changed = false;
            boolean contentChanged = false;

            try {
                String fileTitle = currentNote.getMeta().title();
                String editedTitle = editor.getTitle();
                if (!fileTitle.equals(editedTitle)) {
                    currentNote.getMeta().title(editedTitle);
                    changed = true;
                }

                String fileText = currentNote.contents();
                String editedText = editor.getText();
                if (!fileText.equals(editedText)) {
                    currentNote.save(editedText);
                    changed = true;
                    contentChanged = true;
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            if (changed) {
                Noted.eventBus.post(new NoteChangedEvent(currentNote));
                if (contentChanged) {
                    window.sortAndUpdate();
                }
            }
        }
    }

    public void focusTitle() {
        editor.focusTitle();
    }

    @Override
    public void editingFocusLost() {
        saveChanges();
    }

    @Override
    public void caretChanged(final JTextPane text) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Rectangle y = text.modelToView(text.getDocument().getLength());
                    if (y != null) {
                        int height = y.y + y.height + noteOffset;
                        if (height < minNoteOffset) {
                            height = minNoteOffset;
                        }
                        Rectangle b = area.getBounds();
                        area.setBounds(b.x, b.y, b.width, height);
                        area.revalidate();
                    }
                } catch (BadLocationException ignored) {

                }
            }
        });
    }
}
