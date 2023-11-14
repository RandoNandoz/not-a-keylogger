package ui.tools;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import model.InputRecording;
import model.KeyboardInputTime;
import model.MouseInputTime;

import java.util.ArrayList;

public class RecordingController {
    private final CaptureTool captureTool;
    private final ArrayList<InputRecording<KeyboardInputTime>> kbCaptures;
    private final ArrayList<InputRecording<MouseInputTime>> mouseCaptures;

    // EFFECTS: creates a new object that controls the capture of keyboard/mouse inputs.
    public RecordingController() throws NativeHookException {
        this.captureTool = new CaptureTool();
        this.kbCaptures = new ArrayList<>();
        this.mouseCaptures = new ArrayList<>();
        this.bindListeners();
    }

    // MODIFIES: this
    // EFFECTS: Adds the capture listener, throws NativeHookException if unable to add due to security
    // issues.
    private void bindListeners() throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(this.captureTool);
        GlobalScreen.addNativeMouseListener(this.captureTool);
    }

    // EFFECTS: Gets controller to add
    public void addNewCapture() {
        var newKbCapture = new InputRecording<KeyboardInputTime>();
        var newMouseCapture = new InputRecording<MouseInputTime>();

        this.kbCaptures.add(newKbCapture);
        this.mouseCaptures.add(newMouseCapture);

        this.captureTool.setKeyboardCaptures(newKbCapture);
        this.captureTool.setMouseCaptures(newMouseCapture);
    }

    public ArrayList<InputRecording<KeyboardInputTime>> getKbCaptures() {
        return kbCaptures;
    }

    public ArrayList<InputRecording<MouseInputTime>> getMouseCaptures() {
        return mouseCaptures;
    }
}
