package Core;

import IO.IOUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;


/**
 * Created by aa on 03 May 2017.
 */
public class Note {
    private final HashMap<String, String> emptyMap = new HashMap<>();
    private File file, meta;
    private String fileName;
    private byte[] contents;

    public Note(File file) {
        this.file = file;
        meta = new File(file.getParentFile().getAbsolutePath() + File.separator + "." + file.getName() + ".meta");

        try {
            if (!meta.exists()) {
                meta.createNewFile();
            }
        } catch (IOException ignored) {

        }

        readInfo();
    }

    private void readInfo() {
        fileName = file.getName();
    }

    public String name() {
        return fileName;
    }

    public long lastModified() {
        long l1 = file.lastModified();
        long l2 = meta.lastModified();
        return l1 > l2 ? l1 : l2;
    }

    public String contents() {
        try {
            contents = IOUtil.readFile(file);
            return new String(contents, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void save(String newText) {
        try {
            IOUtil.writeFile(file, newText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getMetaMap() {
        try {
            String json = new String(IOUtil.readFile(meta), "UTF-8");
            if (json == null || json.isEmpty()) {
                return emptyMap;
            }

            JSONObject o = new JSONObject(json);
            HashMap<String, String> map = new HashMap<>();

            Iterator<?> i = o.keys();
            while (i.hasNext()) {
                String key = (String) i.next();
                String value = o.optString(key);
                map.put(key, value);
            }

            return map;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return emptyMap;
    }

    private void setMeta(String key, String value) {
        try {
            String json = new String(IOUtil.readFile(meta), "UTF-8");
            if (json == null || json.isEmpty()) {
                json = "{}";
            }

            JSONObject o = new JSONObject(json);
            o.put(key, value);
            IOUtil.writeFile(meta, o.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public Meta getMeta() {
        return new Metadata(getMetaMap());
    }

    public interface Meta {
        public String title();

        public void title(String newTitle);
    }

    private class Metadata implements Meta {

        private Map<String, String> map;

        private Metadata(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public String title() {
            String s = map.get("title");
            if (s == null) {
                s = "Untitled";
            }
            return s;
        }

        @Override
        public void title(String newTitle) {
            setMeta("title", newTitle);
            reload();
        }

        private void reload() {
            map = getMetaMap();
        }
    }

    public void moveTo(File dest) {
        try {
            FileUtils.moveFileToDirectory(file, dest, false);
            FileUtils.moveFileToDirectory(meta, dest, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
