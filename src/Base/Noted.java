package Base;

import com.google.common.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;

/**
 * Created by aa on 03 May 2017.
 */
public class Noted {

    public static final EventBus eventBus = new EventBus();

    public static void main(String args[]) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Base.Noted");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            Window w = new Window();
            w.setVisible(true);
        });
    }
}
