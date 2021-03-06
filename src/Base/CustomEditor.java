package Base;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by aa on 03 May 2017.
 */
public class CustomEditor extends JPanel {
    private static final long serialVersionUID = -5211473837722610412L;
    private final Color dividerColor = Color.decode("#dbdbdb");
    private JTextField title;
    private JTextPane note;
    private EditorEventListener listener;
    FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (listener != null) {
                listener.editingFocusLost();
            }
        }
    };

    public CustomEditor() {
        super();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(dividerColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));

        title = new JTextField();
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        title.addFocusListener(focusListener);

        final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        title.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    manager.focusNextComponent();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        titlePanel.add(title, BorderLayout.CENTER);

        note = new JTextPane();
        note.addFocusListener(focusListener);
        note.setFont(Window.fontEditor);
        note.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        note.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (listener != null) {
                    listener.caretChanged(note);
                }
            }
        });

        title.setText("");

        add(titlePanel, BorderLayout.NORTH);
        add(note, BorderLayout.CENTER);
    }

    public void setEditorEventListener(EditorEventListener listener) {
        this.listener = listener;
    }

    public String getTitle() {
        return title.getText();
    }

    public void setTitle(String s) {
        title.setText(s);
        title.setCaretPosition(0);
        title.setSelectionEnd(0);
    }

    public String getText() throws BadLocationException {
        Document doc = note.getDocument();
        return doc.getText(0, doc.getLength());
    }

    public void setText(String s) {
        note.setText(s);
    }

    public void clear() {
        setTitle("");
        setText("");
    }

    public boolean hasFocus() {
        return note.hasFocus() || title.hasFocus();
    }

    public void initialFocus() {
        note.setCaretPosition(0);
        note.requestFocusInWindow();
    }

    public void focusTitle() {
        title.setCaretPosition(0);
        title.requestFocusInWindow();
    }

    public interface EditorEventListener {
        void editingFocusLost();

        void caretChanged(JTextPane text);
    }
}
