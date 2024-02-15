package ui.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import model.InputRecording;
import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tools.AppState;
import ui.tools.CaptureTool;

/**
 * Console UI
 */
public class ConsoleApp {
    private final ArrayList<InputRecording<KeyboardInputTime>> keyboardCaptures;
    private final ArrayList<InputRecording<MouseInputTime>> mouseCaptures;
    private final Scanner scanner;
    private final CaptureTool captureTool;
    private int currentMouseIndexCapture;
    private int currentKbIndexCapture;

    // EFFECTS: starts a new console version of the kb capture tool, with no
    // captures,
    // Registers native hook, and prompts user for input as well
    public ConsoleApp() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException exception) {
            System.err.println("Error registering native hook, make sure correct permissions/libraries are installed");
            System.exit(1);
        }
        this.captureTool = new CaptureTool();
        GlobalScreen.addNativeKeyListener(this.captureTool);
        GlobalScreen.addNativeMouseListener(this.captureTool);
        this.currentMouseIndexCapture = 0;
        this.currentKbIndexCapture = 0;
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        this.keyboardCaptures = new ArrayList<>();
        this.mouseCaptures = new ArrayList<>();
        this.runApp();
    }

    // EFFECTS: starts the app, terminates when q is selected.
    private void runApp() {
        // inspired by TellerApp
        String cmd;
        boolean keepGoing = true;

        while (keepGoing) {
            this.showOptions();
            cmd = this.scanner.nextLine().toLowerCase();

            if ("q".equals(cmd)) {
                keepGoing = false;
            } else {
                this.processCommand(cmd);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: dispatches calls to helper methods when different options are
    // picked,
    // adds new capture when capture picked
    private void processCommand(String cmd) {
        switch (cmd) {
            case "r":
                this.startCaptureMovePointers();
                break;
            case "l":
                this.showRecordings();
                break;
            case "e":
                this.editMouseOrKeyboard();
                break;
            case "s":
                this.saveAllInputs();
                break;
            case "l-k":
                this.loadKeyInput();
                break;
            case "l-m":
                this.loadMouseInput();
                break;
        }
    }

    private void startCaptureMovePointers() {
        this.keyboardCaptures.add(new InputRecording<>());
        this.mouseCaptures.add(new InputRecording<>());
        this.capture(this.currentKbIndexCapture, this.currentMouseIndexCapture);
        this.currentKbIndexCapture++;
        this.currentMouseIndexCapture++;
    }

    // EFFECTS: save all inputs into files, numbered, and with unix-ts
    private void saveAllInputs() {
        this.saveKeyboardInputs();
        this.saveMouseInputs();
    }

    // EFFECTS: save all kb inputs into files, with unix ts
    private void saveKeyboardInputs() {
        long unixTimestamp = Instant.now().getEpochSecond();
        for (int i = 0; i < this.keyboardCaptures.size(); i++) {
            JsonWriter<InputRecording<KeyboardInputTime>> kbWriter;
            kbWriter = new JsonWriter<>(String.format("./data/kbRecording-%d-%d.kbinput", unixTimestamp, i));
            try {
                kbWriter.open();
            } catch (IOException e) {
                System.out.println("Can't to write to ./data folder, do you have permissions to write to that folder?");
                e.printStackTrace();
            }
            kbWriter.write(this.keyboardCaptures.get(i));
            kbWriter.close();
        }
    }

    // EFFECTS: save all mouse inputs into files, with unix ts.
    private void saveMouseInputs() {
        long unixTimestamp = Instant.now().getEpochSecond();
        for (int i = 0; i < this.mouseCaptures.size(); i++) {
            JsonWriter<InputRecording<MouseInputTime>> mouseWriter;
            mouseWriter = new JsonWriter<>(String.format("./data/mouseRecording-%d-%d.minput", unixTimestamp, i));
            try {
                mouseWriter.open();
            } catch (IOException e) {
                System.out.println("Can't to write to ./data folder, do you have permissions to write to that folder?");
                e.printStackTrace();
            }
            mouseWriter.write(this.mouseCaptures.get(i));
            mouseWriter.close();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a mouse input json schema to list, increments capture position
    private void loadMouseInput() {
        System.out.print("Please type the path of the .minput file (including name of file) to load: ");
        String path = this.scanner.next();
        JsonReader reader = new JsonReader(path);

        InputRecording<MouseInputTime> loaded = null;
        this.currentMouseIndexCapture++;

        try {
            loaded = reader.readMouseInputRecording();
        } catch (NoSuchFileException e) {
            System.out.println("No such file exists!");
        } catch (IOException e) {
            System.out.println("Unable to read your .minput file, is it valid, and do you have access to read it?");
            e.printStackTrace();
        }

        this.mouseCaptures.add(loaded);
    }

    // MODIFIES: this
    // EFFECTS: adds a key input to list, increments capture position.
    private void loadKeyInput() {
        System.out.print("Please type the path of the .kinput file (including name of file) to load: ");
        String path = this.scanner.next();
        JsonReader reader = new JsonReader(path);

        InputRecording<KeyboardInputTime> loaded = null;
        this.currentKbIndexCapture++;

        try {
            loaded = reader.readKeyboardInputRecording();
        } catch (NoSuchFileException e) {
            System.out.println("Illegal path");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to read your .minput file, is it valid, and do you have access to read it?");
            e.printStackTrace();
        }

        this.keyboardCaptures.add(loaded);

    }

    // MODIFIES: this
    // EFFECTS: edits user's desired mouse/keyboard input
    private void editMouseOrKeyboard() {
        System.out.println("Input type of capture: ");
        String type = this.scanner.nextLine().toLowerCase();
        if (!("keyboard".equals(type) || "mouse".equals(type))) {
            System.out.println("Invalid option");
            return;
        }

        System.out.print("Input recording number: ");
        int recordingNumber = this.scanner.nextInt();
        System.out.println();
        System.out.print("Input input number: ");
        int inputNumber = this.scanner.nextInt();

        if ("keyboard".equals(type)) {
            this.handleKeyboard(recordingNumber, inputNumber);
        }
    }

    // MODIFIES: this
    // EFFECTS: edits keyboard on user choice
    private void handleKeyboard(int recordingNumber, int inputNumber) {
        KeyPress keyPress;
        System.out.print("Input Key ID: ");
        int keyID = this.scanner.nextInt();
        System.out.println();
        System.out.print("Keyup or keydown? (type down/up): ");
        String keyUpOrDown = this.scanner.next();
        if ("up".equals(keyUpOrDown)) {
            keyPress = KeyPress.UP;
        } else if ("down".equals(keyUpOrDown)) {
            keyPress = KeyPress.DOWN;
        } else {
            return;
        }
        System.out.print("Nanoseconds after start?");
        long newDeltaTime = this.scanner.nextLong();
        this.keyboardCaptures.get(recordingNumber).editKey(inputNumber,
                new KeyboardInputTime(keyID, keyPress,
                        this.keyboardCaptures.get(recordingNumber).getStartTime() + newDeltaTime));
    }

    // EFFECTS: display all recorded inputs
    private void showRecordings() {
        this.showKeyboard();
        this.showMouse();
    }

    // EFFECTS: shows all keyboard recordings
    private void showKeyboard() {
        System.out.println("Keyboard input captures:");
        for (int i = 0; i < this.keyboardCaptures.size(); i++) {
            System.out.printf("Recording number %d\n", (i + 1));
            InputRecording<KeyboardInputTime> keyboardInputs = this.keyboardCaptures.get(i);
            for (int j = 0; j < keyboardInputs.getInputs().size(); j++) {
                KeyboardInputTime eventTime = keyboardInputs.getInputs().get(j);
                System.out.printf("Input number %d: <Character: %d, Stroke Type %s, Ms %d>\n",
                        j, eventTime.getKeyId(), eventTime.getKeyPress(), eventTime.getDeltaTime(
                                keyboardInputs.getStartTime()) / 1000000); // ns->ms conversion ratio
            }
        }
    }

    // EFFECTS: shows all mouse recordings
    private void showMouse() {
        System.out.println("Mouse input captures: ");
        for (int i = 0; i < this.mouseCaptures.size(); i++) {
            System.out.printf("Recording number %d\n", (i + 1));
            InputRecording<MouseInputTime> mouseInputs = this.mouseCaptures.get(i);
            for (int j = 0; j < mouseInputs.getInputs().size(); j++) {
                MouseInputTime eventTime = mouseInputs.getInputs().get(j);
                System.out.printf("Input number %d: <X: %d, Y: %d, Event details: %s>\n",
                        j, eventTime.getEvent().getX(), eventTime.getEvent().getY(),
                        eventTime.getEvent().paramString());
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
    private void capture(int kbIndex, int mouseIndex) {
        var keyboardCapture = this.keyboardCaptures.get(kbIndex);
        var mouseCapture = this.mouseCaptures.get(mouseIndex);
        System.out.println("Beginning keyboard/mouse capture... Press enter to stop.");
        this.captureTool.setKeyboardCaptures(keyboardCapture);
        this.captureTool.setMouseCaptures(mouseCapture);
        this.startCapture();
        this.scanner.nextLine();
        // remove last elem of kb captures as that's an enter to stop input
        keyboardCapture.deleteIndex(this.keyboardCaptures.size() - 1);
        this.stopCapture();
        System.out.println("Capture made.");
    }

    // MODIFIES: this
    // EFFECTS: sets state of app to rceord
    private void startCapture() {
        AppState.getInstance().setRecording(true);
    }

    // MODIFIES: this
    // EFFECTS: sets state of app to not record
    private void stopCapture() {
        AppState.getInstance().setRecording(false);
    }
}
