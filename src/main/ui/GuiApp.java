package ui;

import javax.swing.*;
import java.awt.*;

public class GuiApp {
    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;
    private static final String WINDOW_TITLE = "Not a Keylogger";

    private final JFrame window;

    public GuiApp() {
        setUpNativeLookAndFeel();
        this.window = new JFrame(WINDOW_TITLE);
        initApp();
    }

    void setUpNativeLookAndFeel() {
        String nativeLookAndFeel = UIManager.getSystemLookAndFeelClassName();
        // String nativeLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(nativeLookAndFeel);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e1) {
                String errString = "After failing to set native l&f, tried to set cross-platform l&f and still failed";
                throw new RuntimeException(errString);
            }
        }
    }

    void initApp() {
        this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.window.setSize(WIDTH, HEIGHT);
        this.window.setResizable(false);
        this.window.setVisible(true);
        this.window.setLayout(new CardLayout());

        addButton();
    }

    void addButton() {
        JButton b = new JButton("Hello!");
        b.setBounds(new Rectangle(0, 0, WIDTH / 3, HEIGHT / 3));
        this.window.add(b);
    }
}
