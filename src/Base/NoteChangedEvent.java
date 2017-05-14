package Base;

import Core.Note;

/**
 * Created by aa on 15/05/17.
 */
public class NoteChangedEvent {
    Note note;

    public NoteChangedEvent(Note note) {
        this.note = note;
    }
}
