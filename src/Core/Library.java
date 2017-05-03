package Core;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by aa on 03 May 2017.
 */
public class Library {
    private static Library instance = null;
    private static ArrayList<Notebook> notebooks = new ArrayList<>();
    private final String HOME = "A:\\Docs\\Programming\\Noted";

    private Library() {
        populate();
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    private void populate() {
        File home = new File(HOME);

        for (File f : home.listFiles()) {
            if (Files.isDirectory(f.toPath())) {
                notebooks.add(new Notebook(f));
            }
        }

        notebooks.sort(Comparator.comparing(o -> o.name().toLowerCase()));
    }

    public List<Notebook> getBooks() {
        return notebooks;
    }
}
