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

/**
 * Controller to hold data of app
 */
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

    // MODIFIES: this
    // EFFECTS: adds new kb recording, refreshes view
    public void addKbRecording(InputRecording<KeyboardInputTime> r) {
        this.kbCaptures.add(r);
        refreshListView();
    }

    // MODIFIES: this
    // EFFECTS: adds new mouse recording. refreshes view
    public void addMouseRecording(InputRecording<MouseInputTime> r) {
        this.mouseCaptures.add(r);
        refreshListView();
    }

    // MODIFIES: this
    // EFFECTS: removes given index of uilist from data lists
    public void removeFromDataList(int i) {
        var uiElement = uiList.get(i);
        this.kbCaptures.remove(uiElement);
        this.mouseCaptures.remove(uiElement);
    }

    // MODIFIES: this
    // EFFECTS: Refreshes list with current inputs
    public void refreshListView() {
        uiList.clear();
        uiList.addAll(mouseCaptures);
        uiList.addAll(kbCaptures);
        uiList.removeIf(e -> e.getInputs().isEmpty());
        this.app.refreshList(uiList);
    }

    // trivial getter

    public List<InputRecording<? extends InputTime>> getUiList() {
        return uiList;
    }
}
