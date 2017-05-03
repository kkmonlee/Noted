package Core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 03 May 2017.
 */
public class Notebook {
    private String name;
    private File folder;

    public ArrayList<Note> notes = new ArrayList<>();

    public Notebook(File folder) {
        this.name = folder.getName();
        this.folder = folder;
        populate();
    }

    private void populate() {
        notes.clear();
        for (File f : folder.listFiles()) {
            notes.add(new Note(f));
        }
    }

    public List<Note> getNotes() {
        return notes;
    }

    public String name() {
        return name;
    }

    public int count() {
        return notes.size();
    }

}
