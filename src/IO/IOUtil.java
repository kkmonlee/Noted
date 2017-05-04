package IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by aa on 03 May 2017.
 */
public class IOUtil {
    private static final JSONObject emptyJson = new JSONObject();

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size is greater than 2 GB");
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }
    }

    public static void writeFile(File file, String text) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
            f.writeBytes(text);
            f.setLength(text.length());
        }
    }

    public static JSONObject loadJson(File file) {
        try {
            String json = null;
            if (file.exists()) {
                json = new String(IOUtil.readFile(file), "UTF-8");
            }

            if (json == null || json.isEmpty()) {
                json = "{}";
            }

            return new JSONObject(json);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return emptyJson;
    }
}
