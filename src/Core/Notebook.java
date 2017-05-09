package Core;

import Base.Noted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import Base.NotebookEvent.Kind;
import Base.NotebookEvent;
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
        String fullPath = this.folder.getAbsolutePath() + File.separator + Long.toString(System.currentTimeMillis(), 36);
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
