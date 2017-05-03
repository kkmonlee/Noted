package Core;

import java.io.File;

/**
 * Created by aa on 03 May 2017.
 */
public class Note {
    private File file;
    private String fileName;

    public Note(File file) {
        this.file = file;
        readInfo();
    }

    private void readInfo() {
        fileName = file.getName();
    }

    public String name() {
        return fileName;
    }
}
