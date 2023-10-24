package ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tools.CaptureTool;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    private final ArrayList<TimeSeries<KeyboardInputTime>> keyboardCaptures;
    private final ArrayList<TimeSeries<MouseInputTime>> mouseCaptures;
    private final Scanner scanner;
    private final CaptureTool captureTool;
    int currentIndexCapture;


    // EFFECTS: starts a new console version of the kb capture tool, with no captures,
    // Registers native hook, and prompts user for input as well
    public ConsoleApp() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException exception) {
            System.err.println("Error registering native hook, make sure correct permissions/libraries are installed");
            System.exit(1);
        }
        captureTool = new CaptureTool();
        GlobalScreen.addNativeKeyListener(captureTool);
        GlobalScreen.addNativeMouseListener(captureTool);
        currentIndexCapture = 0;
        scanner = new Scanner(System.in);
        keyboardCaptures = new ArrayList<>();
        mouseCaptures = new ArrayList<>();
        runApp();
    }

    // EFFECTS: starts the app, terminates when q is selected.
    private void runApp() {
        // inspired by TellerApp
        String cmd;
        boolean keepGoing = true;

        while (keepGoing) {
            showOptions();
            cmd = scanner.nextLine().toLowerCase();

            if (cmd.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(cmd);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: dispatches calls to helper methods when different options are picked,
    // adds new capture when capture picked
    private void processCommand(String cmd) {
        switch (cmd) {
            case "r":
                this.keyboardCaptures.add(new TimeSeries<>());
                this.mouseCaptures.add(new TimeSeries<>());
                capture(currentIndexCapture);
                currentIndexCapture++;
                break;
            case "l":
                showRecordings();
                break;
            case "e":
                editMouseOrKeyboard();
                break;
            case "s":
                saveAllInputs();
                break;
            case "l-k":
                loadKeyInput();
            case "l-m":
                loadMouseInput();
                break;
        }
    }

    // EFFECTS: save all inputs into files, numbered, and with unix-ts
    private void saveAllInputs() {
        saveKeyboardInputs();
        saveMouseInputs();
    }

    private void saveKeyboardInputs() {
        long unixTimestamp = Instant.now().getEpochSecond();
        for (int i = 0; i < this.keyboardCaptures.size(); i++) {
            JsonWriter<TimeSeries<KeyboardInputTime>> kbWriter;
            kbWriter = new JsonWriter<>(String.format("./data/kbRecording-%d-%d.kbinput", unixTimestamp, i));
            try {
                kbWriter.open();
            } catch (IOException e) {
                System.out.println("Unable to write to ./data folder, do you have permissions to write to that folder?");
                e.printStackTrace();
            }
            kbWriter.write(this.keyboardCaptures.get(i));
            kbWriter.close();
        }
    }

    private void saveMouseInputs() {
        long unixTimestamp = Instant.now().getEpochSecond();
        for (int i = 0; i < this.mouseCaptures.size(); i++) {
            JsonWriter<TimeSeries<MouseInputTime>> mouseWriter;
            mouseWriter = new JsonWriter<>(String.format("./data/mouseRecording-%d-%d.minput", unixTimestamp, i));
            try {
                mouseWriter.open();
            } catch (IOException e) {
                System.out.println("Unable to write to ./data folder, do you have permissions to write to that folder?");
                e.printStackTrace();
            }
            mouseWriter.write(this.mouseCaptures.get(i));
            mouseWriter.close();
        }
    }

    // EFFECTS: adds a mouse input json schema to list, increments capture position
    private void loadMouseInput() {
        System.out.print("Please type the path of the .minput file (including name of file) to load: ");
        String path = scanner.next();
        JsonReader reader = new JsonReader(path);

        TimeSeries<MouseInputTime> loaded = null;
        currentIndexCapture++;

        try {
            loaded = (TimeSeries<MouseInputTime>) reader.readTimeSeries(MouseInputTime.class);
        } catch (NoSuchFileException e) {
            System.out.println("No such file exists!");
        } catch (IOException e) {
            System.out.println("Unable to read your .minput file, is it valid, and do you have access to read it?");
            e.printStackTrace();
        }

        this.mouseCaptures.add(loaded);
    }

    // EFFECTS: adds a key input to list, increments capture position.
    private void loadKeyInput() {
        System.out.print("Please type the path of the .kinput file (including name of file) to load: ");
        String path = scanner.next();
        JsonReader reader = new JsonReader(path);

        TimeSeries<KeyboardInputTime> loaded = null;
        currentIndexCapture++;

        try {
            loaded = (TimeSeries<KeyboardInputTime>) reader.readTimeSeries(KeyboardInputTime.class);
        } catch (IOException e) {
            System.out.println("Unable to read your .minput file, is it valid, and do you have access to read it?");
            e.printStackTrace();
        }

        this.keyboardCaptures.add(loaded);

    }

    // EFFECTS: edits user's desired mouse/keyboard input
    private void editMouseOrKeyboard() {
        System.out.println("Input type of capture: ");
        String type = scanner.nextLine().toLowerCase();
        if (!(type.equals("keyboard") || type.equals("mouse"))) {
            System.out.println("Invalid option");
            return;
        }

        System.out.print("Input recording number: ");
        int recordingNumber = scanner.nextInt();
        System.out.println();
        System.out.print("Input input number: ");
        int inputNumber = scanner.nextInt();

        if (type.equals("keyboard")) {
            handleKeyboard(recordingNumber, inputNumber);
        }
    }

    // MODIFIES: this
    // EFFECTS: edits keyboard on user choice
    private void handleKeyboard(int recordingNumber, int inputNumber) {
        KeyPress keyPress;
        System.out.print("Input Key ID: ");
        int keyID = scanner.nextInt();
        System.out.println();
        System.out.print("Keyup or keydown? (type down/up): ");
        String keyUpOrDown = scanner.next();
        if (keyUpOrDown.equals("up")) {
            keyPress = KeyPress.UP;
        } else if (keyUpOrDown.equals("down")) {
            keyPress = KeyPress.DOWN;
        } else {
            return;
        }
        System.out.print("Nanoseconds after start?");
        long newDeltaTime = scanner.nextLong();
        this.keyboardCaptures.get(recordingNumber).editKey(inputNumber,
                new KeyboardInputTime(keyID, keyPress,
                        this.keyboardCaptures.get(recordingNumber).getStartTime() + newDeltaTime)
        );
    }

    // EFFECTS: display all recorded inputs
    private void showRecordings() {
        showKeyboard();
        showMouse();
    }

    // EFFECTS: shows all keyboard recordings
    private void showKeyboard() {
        System.out.println("Keyboard input captures:");
        for (int i = 0; i < keyboardCaptures.size(); i++) {
            System.out.printf("Recording number %d\n", (i + 1));
            TimeSeries<KeyboardInputTime> keyboardInputs = keyboardCaptures.get(i);
            for (int j = 0; j < keyboardInputs.getInputs().size(); j++) {
                KeyboardInputTime eventTime = keyboardInputs.getInputs().get(j);
                System.out.printf("Input number %d: <Character: %d, Stroke Type %s, Ms %d>\n",
                        j, eventTime.getKeyId(), eventTime.getKeyPress(), eventTime.getDeltaTime(
                                keyboardInputs.getStartTime()
                        ) / 1000000); // ns->ms conversion ratio
            }
        }
    }

    // EFFECTS: shows all mouse recordings
    private void showMouse() {
        System.out.println("Mouse input captures: ");
        for (int i = 0; i < mouseCaptures.size(); i++) {
            System.out.printf("Recording number %d\n", (i + 1));
            TimeSeries<MouseInputTime> mouseInputs = mouseCaptures.get(i);
            for (int j = 0; j < mouseInputs.getInputs().size(); j++) {
                MouseInputTime eventTime = mouseInputs.getInputs().get(j);
                System.out.printf("Input number %d: <X: %d, Y: %d, Event detailsz: %s>\n",
                        j, eventTime.getEvent().getX(), eventTime.getEvent().getY(), eventTime.getEvent().paramString()
                );
            }
        }
    }

    // EFFECTS: shows options avail. to user.
    private void showOptions() {
        // inspired by TellerApp
        System.out.println("r - record");
        System.out.println("l - list out recorded inputs");
        System.out.println("e - edit recorded inputs by recording number, and input number.");
        System.out.println("q - quit");
        System.out.println("s - save all inputs");
        System.out.println("l-k - load keyboard inputs");
        System.out.println("l-m - load mouse inputs");
    }

    // MODIFIES: this
    // EFFECTS: captures user mouse and kb inputs
    private void capture(int index) {
        var keyboardCapture = keyboardCaptures.get(index);
        var mouseCapture = mouseCaptures.get(index);
        System.out.println("Beginning keyboard/mouse capture... Press enter to stop.");
        this.captureTool.setKeyboardCaptures(keyboardCapture);
        this.captureTool.setMouseCaptures(mouseCapture);
        startCapture();
        scanner.nextLine();
        // remove last elem of kb captures as that's an enter to stop input
        keyboardCapture.deleteIndex(this.keyboardCaptures.size() - 1);
        stopCapture();
        System.out.println("Capture made.");
    }

    private void startCapture() {
        AppState.getInstance().setRecording(true);
    }

    // MODIFIES: this
    // EFFECTS: sets state of app to not record
    private void stopCapture() {
        AppState.getInstance().setRecording(false);
    }
}
