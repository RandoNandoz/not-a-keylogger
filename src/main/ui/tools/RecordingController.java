package ui.tools;

import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;

import java.util.ArrayList;

public class RecordingController {
    private static RecordingController singleton;
    private final CaptureTool captureTool;
    private final ArrayList<TimeSeries<KeyboardInputTime>> kbCaptures;
    private final ArrayList<TimeSeries<MouseInputTime>> mouseCaptures;

    private RecordingController() {
        this.captureTool = new CaptureTool();
        this.kbCaptures = new ArrayList<>();
        this.mouseCaptures = new ArrayList<>();
    }

    public static RecordingController getInstance() {
        if (null == RecordingController.singleton) {
            singleton = new RecordingController();
        }
        return singleton;
    }

    public void addNewCapture() {
        var newKbCapture = new TimeSeries<KeyboardInputTime>();
        var newMouseCapture = new TimeSeries<MouseInputTime>();

        this.kbCaptures.add(newKbCapture);
        this.mouseCaptures.add(newMouseCapture);

        this.captureTool.setKeyboardCaptures(newKbCapture);
        this.captureTool.setMouseCaptures(newMouseCapture);
    }

    public ArrayList<TimeSeries<KeyboardInputTime>> getKbCaptures() {
        return kbCaptures;
    }

    public ArrayList<TimeSeries<MouseInputTime>> getMouseCaptures() {
        return mouseCaptures;
    }
}
