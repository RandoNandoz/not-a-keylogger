package ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;
import ui.tools.CaptureTool;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    private final ArrayList<TimeSeries<KeyboardInputTime>> keyboardCaptures;
    private final ArrayList<TimeSeries<MouseInputTime>> mouseCaptures;
    private final Scanner scanner;
    int currentIndexCapture;


    // EFFECTS: starts a new console version of the kb capture tool, with no captures,
    public ConsoleApp() {
        currentIndexCapture = 0;
        scanner = new Scanner(System.in);
        keyboardCaptures = new ArrayList<>();
        mouseCaptures = new ArrayList<>();
        runApp();
    }

    // EFFECTS: starts the app, terminates when q is selected.
    private void runApp() {
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
        }
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
                System.out.printf("Input number %d: <X: %d, Y: %d, Event type: %s>\n",
                        j, eventTime.getEvent().getX(), eventTime.getEvent().getY(), eventTime.getEvent().paramString()
                );
            }
        }
    }

    // EFFECTS: shows options avail. to user.
    private void showOptions() {
        System.out.println("r - record");
        System.out.println("l - list out recorded inputs");
        System.out.println("e - edit recorded inputs by recording number, and input number.");
        System.out.println("q - quit");
    }

    // MODIFIES: this
    // EFFECTS: captures user mouse and kb inputs, registers native hook, and prompts user (if needed, by os) for
    // permission
    private void capture(int index) {
        var keyboardCapture = keyboardCaptures.get(index);
        var mouseCapture = mouseCaptures.get(index);
        System.out.println("Beginning keyboard/mouse capture... Press enter to stop.");
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException exception) {
            System.err.println("Error registering native hook, make sure correct permissions/libraries are installed");
            System.exit(1);
        }

        CaptureTool tool = new CaptureTool(
                keyboardCapture,
                mouseCapture
        );

        GlobalScreen.addNativeKeyListener(tool);
        GlobalScreen.addNativeMouseListener(tool);

        scanner.nextLine();
        stopCapture(tool);
        System.out.println("Capture made.");
    }

    // MODIFIES: this
    // EFFECTS: de-regs nativehook with given capturetool, stops recording user inputs, and removes last input (which
    // is instruction to stop recording)
    private void stopCapture(CaptureTool tool) {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            System.out.println("Error in de-registering the native hook, this shouldn't happen.");
        }
    }
}
