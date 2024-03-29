package ui;

import ui.cli.ConsoleApp;
import ui.gui.GuiApp;

import javax.swing.*;

/**
 * Executor for app
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
//            new GuiApp();
            SwingUtilities.invokeLater(
                    GuiApp::new
            );
        } else if (args[0].equals("--cli")) {
            new ConsoleApp();
        } else {
            System.out.println("Unknown argument");
        }
    }
}
