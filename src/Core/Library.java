package Core;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by aa on 03 May 2017.
 */
public class Library {
    private static Library instance = null;

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    private final String HOME = "A:\\Docs\\Programming\\Noted";

    private static ArrayList<Book> books = new ArrayList<>();

    private Library() {
        populate();
    }

    private void populate() {
        File home = new File(HOME);

        for (File f : home.listFiles()) {
            if (Files.isDirectory(f.toPath())) {
                books.add(new Book(f));
            }
        }

        books.sort(Comparator.comparing(o -> o.name().toLowerCase()));
    }

    public List<Book> getBooks() {
        return books;
    }
}
