package ui.tools;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import model.InputRecording;
import model.InputTime;
import model.KeyboardInputTime;
import model.MouseInputTime;
import ui.GuiApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecordingController {
    private final CaptureTool captureTool;
    private final GuiApp app;
    private final List<InputRecording<? extends InputTime>> uiList;
    private final ArrayList<InputRecording<KeyboardInputTime>> kbCaptures;
    private final ArrayList<InputRecording<MouseInputTime>> mouseCaptures;

    // EFFECTS: creates a new object that controls the capture of keyboard/mouse inputs.
    public RecordingController(GuiApp app) throws NativeHookException {
        this.uiList = new ArrayList<>();
        this.app = app;
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

    public void refreshListView() {
        uiList.clear();
        uiList.addAll(mouseCaptures);
        uiList.addAll(kbCaptures);
        uiList.removeIf(e -> e.getInputs().isEmpty());
        this.app.refreshList(uiList);
    }

    public List<InputRecording<? extends InputTime>> getUiList() {
        return uiList;
    }
}
