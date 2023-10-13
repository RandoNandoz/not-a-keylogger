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


    public ConsoleApp() {
        currentIndexCapture = 0;
        scanner = new Scanner(System.in);
        keyboardCaptures = new ArrayList<>();
        mouseCaptures = new ArrayList<>();
        runApp();
    }

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

    private void handleMouse() {

    }

    private void showRecordings() {
        showKeyboard();
        showMouse();
    }

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

    private void showMouse() {
        System.out.println("Mouse input captures: ");
        for (int i = 0; i < mouseCaptures.size(); i++) {
            System.out.printf("Recording number %d\n", (i + 1));
            TimeSeries<MouseInputTime> mouseInputs = mouseCaptures.get(i);
            for (int j = 0; j < mouseInputs.getInputs().size(); j++) {
                MouseInputTime eventTime = mouseInputs.getInputs().get(j);
                System.out.printf("Input number %d: <X: %d, Y: %d, Event type: %s>",
                        j, eventTime.getEvent().getX(), eventTime.getEvent().getY(), eventTime.getEvent().paramString()
                );
            }
        }
    }

    private void showOptions() {
        System.out.println("r - record");
        System.out.println("l - list out recorded inputs");
        System.out.println("e - edit recorded inputs by recording number, and input number.");
        System.out.println("q - quit");
    }

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

        GlobalScreen.addNativeKeyListener(new CaptureTool(
                keyboardCapture,
                mouseCapture
        ));

        scanner.nextLine();
        stopCapture();
        System.out.println("Capture made.");
    }

    private void stopCapture() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            System.out.println("Error in de-registering the native hook, this shouldn't happen.");
        }
    }
}
