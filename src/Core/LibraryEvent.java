package Core;

/**
 * Created by aa on 15/05/17.
 */
public class LibraryEvent {
    public Kind kind;

    public LibraryEvent(Kind kind) {
        this.kind = kind;
    }

    public enum Kind {
        notebookCreated, notebookListChanged
    }
}
