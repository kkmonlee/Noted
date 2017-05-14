package Base;

import Core.Library;
import Core.Note;
import IO.IOUtil;
import com.google.common.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by aa on 04 May 2017.
 */
public class SidebarList extends JPanel {
    private static final long serialVersionUID = 1292849331825419909L;
    private ArrayList<SidebarListItem> items = new ArrayList<>();

    private Window window;

    public SidebarList(Window w) {
        this.window = w;
    }

    public String getTarget(int n) {
        if (n > 0 && n < items.size()) {
            return items.get(n).target;
        }

        return "";
    }

    public void load(File f) {
        items.clear();

        JSONObject o = IOUtil.loadJson(f);
        if (o.has("list")) {
            try {
                JSONArray array = o.getJSONArray("list");

                for (int n = 0, len = array.length(); n < len; n++) {
                    String s = array.getString(n);

                    SidebarListItem item = new SidebarListItem(s);
                    items.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        createComponents();
    }

    private void createComponents() {
        removeAll();
        setLayout(new GridLayout(0, 1));

        for (SidebarListItem item : items) {
            add(item);
        }
    }

    enum Icon {
        none, notebookSmall, notebookLarge, noteSmall, noteLarge
    }

    class SidebarListItem extends JPanel {
        private static final long serialVersionUID = -1786795327565836884L;

        File file;
        private Icon icon;
        String target;
        JLabel label;

        @Subscribe
        public void handleNoteChanged(NoteChangedEvent event) {
            if (event.note.equals(file)) {
                refresh();
            }
        }

        public void refresh() {
            if (file.isDirectory()) {
                icon = Icon.notebookSmall;
                label.setText(file.getName());
            } else {
                icon = Icon.noteSmall;
                Note note = new Note(file);
                label.setText(note.getMeta().title());
            }
        }

        public SidebarListItem(String targetFileName) {
            setOpaque(false);

            Noted.eventBus.register(this);

            String path = Library.getInstance().getHome() + File.separator + targetFileName;

            file = new File(path);
            target = file.getAbsolutePath();

            label = new JLabel("");
            label.setForeground(Color.LIGHT_GRAY);
            label.setFont(Window.fontBoldNormal);
            add(label);

            refresh();

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    window.openShortcut(target);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    label.setForeground(Color.WHITE);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    label.setForeground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
    }
}
