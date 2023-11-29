package ui.gui;


import com.github.kwhat.jnativehook.NativeHookException;
import model.logging.EventLog;
import model.InputRecording;
import model.InputTime;
import ui.tools.RecordingController;
import ui.tools.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Graphical UI
 */
public class GuiApp {
    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;
    private static final String WINDOW_TITLE = "Not a Keylogger";
    private final RecordingController rc;
    private final JFrame window;
    private final JPanel viewPanel;

    private JList<InputTime> detailedViewList;

    private JList<InputRecording<? extends InputTime>> timeSeriesList;

    // EFFECTS: starts the GUI version of this app
    public GuiApp() {
        try {
            splash();
        } catch (IOException e) {
            System.out.println("Unable to find picture of carp! Check if images folder has image!");
        }
        setUpNativeLookAndFeel();
        try {
            this.rc = new RecordingController(this);
        } catch (NativeHookException e) {
            popUpError("Unable to add input listener, check if you have granted permissions for this app.", "Error!");
            System.exit(1);
            throw new RuntimeException("This shouldn't ever be thrown!");
        }
        this.window = new JFrame(WINDOW_TITLE);
        this.viewPanel = new JPanel();
        initApp();
        this.window.repaint(); // for good measure
    }

    private void splash() throws IOException {
//        var s = SplashScreen.getSplashScreen();
//        s.setImageURL(new URL("https://cdn.discordapp.com/attachments/1043274455826317392/1153782806644801729/image.png?ex=656f8179&is=655d0c79&hm=f271ee0eff397629308b56628e4138ae6b2c7830a460eb710a81d1e7536324ee&"));
        JFrame j = new JFrame("A");
        j.setSize(1000, 1000);
        // https://stackoverflow.com/questions/18027833/adding-image-to-jframe
        j.add(new JLabel(new ImageIcon(new File("./images/what_a_load_of_carp.gif").toURI().toURL())));
        j.setVisible(true);
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

        addDetailsPanel();
        addRecordingList();
        addButtons();
        addMenuBar();

        this.window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.window.setSize(WIDTH, HEIGHT);
        this.window.setResizable(false);
        this.window.setVisible(true);
        addLoggerListener();
    }

    private void addLoggerListener() {
        this.window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e); // idk what this does
                EventLog.getInstance().forEach(ev -> {
                    System.out.println(ev.getDate() + " "  + ev.getDescription());
                });
                e.getWindow().dispose();
                System.exit(0);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Adds buttons to control playback and recording
    private void addButtons() {
        JButton recordButton = new JButton("Start Recording");
        JButton deleteButton = new JButton("Delete Recording");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 0));

        buttonPanel.add(recordButton);
        buttonPanel.add(deleteButton);

        deleteButton.addActionListener(new DeleteViewListListener(timeSeriesList, rc));
        recordButton.addActionListener(new ChangeRecordingModeListener(recordButton, rc));

        this.viewPanel.add(buttonPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds list UI element to app
    private void addRecordingList() {
        this.timeSeriesList = new JList<>();
        this.timeSeriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.timeSeriesList.setLayoutOrientation(JList.VERTICAL);

        this.timeSeriesList.addListSelectionListener(
                new InputRecordingSelectListener(timeSeriesList, detailedViewList, this.rc));

        JScrollPane sp = new JScrollPane(timeSeriesList);

        this.viewPanel.add(sp);
    }

    // MODIFIES: this
    // EFFECTS: adds detailed view of input recordings
    private void addDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        JLabel detailsDesc = new JLabel("Details");

        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));
        this.detailedViewList = new JList<>();
        this.detailedViewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.detailedViewList.setLayoutOrientation(JList.VERTICAL);


        detailsPanel.add(detailsDesc);
        JScrollPane sp = new JScrollPane(detailedViewList);
        detailsPanel.add(sp);


        this.viewPanel.add(detailsPanel);
    }

    // MODIFIES: this
    // EFFECTS: updates ui recording list with given list
    public void refreshList(Collection<InputRecording<? extends InputTime>> series) {
//        this.timeSeriesList = new JList<InputRecording<InputTime>>(series.toArray(new InputRecording[0]));
        DefaultListModel<InputRecording<? extends InputTime>> model = new DefaultListModel<>();
        for (var i : series) {
            model.addElement(i);
        }
        this.timeSeriesList.setModel(model);
        this.timeSeriesList.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: Pops up error if user does something bad
    public void popUpError(String errMsg, String title) {
        JOptionPane.showMessageDialog(new JFrame(), errMsg, title, JOptionPane.ERROR_MESSAGE);
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

//        saveOption.addActionListener();
//        loadOption.addActionListener();

//        if (selected == null) {
//            popUpError("Cannot save with nothing selected!", "Error!");
//        }

        loadOption.addActionListener(new LoadListener(timeSeriesList, window, this, rc));
        saveOption.addActionListener(new SaveListener(timeSeriesList, window));

        fileMenu.add(saveOption);
        fileMenu.add(loadOption);

        menuBar.add(fileMenu);

        this.window.setJMenuBar(menuBar);
    }
}
