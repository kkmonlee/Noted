package Base;

/**
 * Created by aa on 10 May 2017.
 */
public class NotebookEvent {
    Kind kind;

    public NotebookEvent(Kind k) {
        this.kind = k;
    }

    public enum Kind {
        noteMoved, noteCreated
    }
}
