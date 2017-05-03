import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

/**
 * Created by aa on 03 May 2017.
 */
public class CustomEditor extends JPanel {
    private final Color dividerColor = Color.decode("#dbdbdb");
    private JTextField title;
    private JTextPane note;

    public CustomEditor() {
        super();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(dividerColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));

        title = new JTextField();
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        titlePanel.add(title, BorderLayout.CENTER);

        note = new JTextPane();
        note.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        title.setText("");

        add(titlePanel, BorderLayout.NORTH);
        add(note, BorderLayout.CENTER);
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
}
