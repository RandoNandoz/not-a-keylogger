package ui;

import javax.swing.*;

import model.InputTime;
import model.TimeSeries;
import ui.tools.RecordingController;
import ui.tools.gui.ChangeRecordingModeListener;

import java.awt.*;

public class GuiApp {
    private final RecordingController rc;
    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;
    private static final String WINDOW_TITLE = "Not a Keylogger";

    private final JFrame window;
    private final JPanel viewPanel;

    private JList<TimeSeries<? extends InputTime>> timeSeriesList;

    // EFFECTS: starts the GUI version of this app
    public GuiApp() {
        this.rc = RecordingController.getInstance();
        setUpNativeLookAndFeel();
        this.window = new JFrame(WINDOW_TITLE);
        this.viewPanel = new JPanel();
        initApp();
        this.window.repaint(); // for good measure
    }

    // MODIFIES: this
    // EFFECTS: sets up java app to look more like a native OS app
    private void setUpNativeLookAndFeel() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            setUpMacOSMenuBar();
        }

        String nativeLookAndFeel = UIManager.getSystemLookAndFeelClassName();
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

    // MODIFIES: this
    // EFFECTS: sets up layouts, calls helper methods and makes app visible.
    private void initApp() {
        this.window.setLayout(new CardLayout());

        this.viewPanel.setBorder(BorderFactory.createEmptyBorder());
        this.viewPanel.setLayout(new GridLayout());

        this.window.add(viewPanel, BorderLayout.CENTER);

        addButtons();
        addList();
        addMenuBar();

        this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.window.setSize(WIDTH, HEIGHT);
        this.window.setResizable(false);
        this.window.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Adds buttons to control playback and recording
    private void addButtons() {
        JButton recordButton = new JButton("Start Recording");
        JButton playbackButton = new JButton("Playback");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 0));

        buttonPanel.add(recordButton);
        buttonPanel.add(playbackButton);

        recordButton.addActionListener(new ChangeRecordingModeListener(recordButton));

        this.viewPanel.add(buttonPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds list UI element to app
    private void addList() {
        this.timeSeriesList = new JList<>();
        this.timeSeriesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.timeSeriesList.setLayoutOrientation(JList.VERTICAL);

        this.viewPanel.add(timeSeriesList);
    }

    public void refreshList(TimeSeries<? extends InputTime> series) {
//        this.timeSeriesList.setModel(rc.getMouseCaptures());
        this.timeSeriesList = new JList<>(series.getInputs().toArray(new TimeSeries[series.getInputs().size()]));
    }

    // MODIFIES: this
    // EFFECTS: sets up macOS menu bar
    private void setUpMacOSMenuBar() {
        // https://alvinalexander.com/java/make-java-application-look-feel-native-mac-osx/
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", WINDOW_TITLE);
        System.setProperty("apple.awt.application.name", WINDOW_TITLE);
        System.out.println("Setting up macOS native menu bar behaviour!");
    }

    // MODIFIES: this
    // EFFECTS: adds save/load menu bar
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveOption = new JMenuItem("Save");
        JMenuItem loadOption = new JMenuItem("Load");

        fileMenu.add(saveOption);
        fileMenu.add(loadOption);

        menuBar.add(fileMenu);

        this.window.setJMenuBar(menuBar);
    }
}
