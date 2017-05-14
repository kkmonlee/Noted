package Core;

import Base.NotebookEvent;
import Base.NotebookEvent.Kind;
import Base.Noted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 03 May 2017.
 */
public class Notebook {
    public ArrayList<Note> notes = new ArrayList<>();
    private String name;
    private File folder;

    public Notebook(File folder) {
        this.name = folder.getName();
        this.folder = folder;
        populate();
    }

    public static Notebook newNotebook() throws IOException {
        String baseName = Library.getInstance().getHome() + File.separator + "New notebook";
        File f = new File(baseName);
        int n = 2;
        while (f.exists()) {
            f = new File(baseName + " " + n);
            n++;
        }

        if (!f.mkdirs()) {
            throw new IOException();
        }

        return new Notebook(f);
    }

    private void populate() {
        notes.clear();
        for (File f : folder.listFiles()) {
            String name = f.getName();
            if (name.charAt(0) != '.' && !name.endsWith("~")) {
                notes.add(new Note(f));
            }
        }
        sortNotes();
    }

    public boolean equals(File f) {
        return folder.equals(f);
    }

    public File folder() {
        return folder;
    }

    public boolean rename(String s) {
        File newFile = new File(folder.getParentFile() + File.separator + s);
        try {
            if (folder.renameTo(newFile)) {
                folder = newFile;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sortNotes() {
        notes.sort((o1, o2) -> o1.lastModified() > o2.lastModified() ? -1 : 1);
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

    public Note newNote() throws IOException {
        String fullPath = this.folder.getAbsolutePath() + File.separator + Long.toString(System.currentTimeMillis(), 36) + ".txt";
        File f = new File(fullPath);

        f.createNewFile();
        Note n = new Note(f);
        n.getMeta().title("Untitled");

        notes.add(0, n);

        Noted.eventBus.post(new NotebookEvent(Kind.noteCreated));

        return n;
    }

    public void deleteNote(Note note) {
        notes.remove(note);
        note.moveTo(Library.getInstance().getTrash());
    }

    public Note find(String name) {
        File note = new File(folder + File.separator + name);
        for (Note n : notes) {
            if (n.equals(note)) {
                return n;
            }
        }

        return null;
    }

    public void refresh() {
        populate();
    }
}
