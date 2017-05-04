import Core.Library;
import IO.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by aa on 04 May 2017.
 */
public class SidebarList extends JPanel {
    private static final long serialVersionUID = 1292849331825419909L;
    private ArrayList<SidebarListItem> items = new ArrayList<>();

    public SidebarList() {

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
        private Icon icon;
        private String label;
        String target;

        public SidebarListItem(String target) {
            setOpaque(false);

            String path = Library.getInstance().getHome() + File.separator + target;

            File f = new File(path);
            if (f.isDirectory()) {
                icon = Icon.notebookSmall;
            } else {
                icon = Icon.noteSmall;
            }

            this.target = f.getAbsolutePath();
            label = f.getName();

            JLabel l = new JLabel(label);
            l.setForeground(Color.LIGHT_GRAY);
            l.setFont(Window.fontBoldNormal);
            add(l);
        }
    }
}
