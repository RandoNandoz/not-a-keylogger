package ui;


import com.github.kwhat.jnativehook.NativeHookException;
import model.InputRecording;
import model.InputTime;
import ui.tools.RecordingController;
import ui.tools.gui.ChangeRecordingModeListener;
import ui.tools.gui.InputRecordingSelectListener;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class GuiApp {
    private final RecordingController rc;
    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;
    private static final String WINDOW_TITLE = "Not a Keylogger";

    private final JFrame window;
    private final JPanel viewPanel;

    private JList<InputTime> detailedViewList;

    private JList<InputRecording<? extends InputTime>> timeSeriesList;

    // EFFECTS: starts the GUI version of this app
    public GuiApp() {
        setUpNativeLookAndFeel();
        try {
            this.rc = new RecordingController(this);
        } catch (NativeHookException e) {
            popUpError("Unable to add input listener, check if you have granted permissions for this app.",
                    "Error!");
            System.exit(1);
            throw new RuntimeException("This shouldn't ever be thrown!");
        }
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
        this.viewPanel.setLayout(new GridLayout(1, 3));

        this.window.add(viewPanel, BorderLayout.CENTER);

        addButtons();
        addDetailsPanel();
        addRecordingList();
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
        JButton deleteButton = new JButton("Delete Recording");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 0));

        buttonPanel.add(recordButton);
        buttonPanel.add(deleteButton);

//        deleteButton.addActionListener();
        recordButton.addActionListener(new ChangeRecordingModeListener(recordButton, rc));

        this.viewPanel.add(buttonPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds list UI element to app
    private void addRecordingList() {
        this.timeSeriesList = new JList<>();
        this.timeSeriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.timeSeriesList.setLayoutOrientation(JList.VERTICAL);

        this.timeSeriesList.addListSelectionListener(new InputRecordingSelectListener(detailedViewList, this.rc));

        this.viewPanel.add(timeSeriesList);
    }

    private void addDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        JLabel detailsDesc = new JLabel("Details");

        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
        this.detailedViewList = new JList<>();
        this.detailedViewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.detailedViewList.setLayoutOrientation(JList.VERTICAL);



        detailsPanel.add(detailsDesc);
        detailsPanel.add(detailedViewList);

        this.viewPanel.add(detailsPanel);
    }

    public void refreshList(Collection<InputRecording<? extends InputTime>> series) {
//        this.timeSeriesList = new JList<InputRecording<InputTime>>(series.toArray(new InputRecording[0]));
        DefaultListModel<InputRecording<? extends InputTime>> model = new DefaultListModel<>();
        for (var i : series) {
            model.addElement(i);
        }
        this.timeSeriesList.setModel(model);
        this.timeSeriesList.updateUI();
    }

    private void popUpError(String errMsg, String title) {
        JOptionPane.showMessageDialog(new JFrame(), errMsg, title,
                JOptionPane.ERROR_MESSAGE);
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
