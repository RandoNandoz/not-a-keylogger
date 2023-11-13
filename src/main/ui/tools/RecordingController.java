package ui.tools;

import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;

import java.util.ArrayList;

public class RecordingController {
    private final ArrayList<TimeSeries<KeyboardInputTime>> kbCaptures;
    private final ArrayList<TimeSeries<MouseInputTime>> mouseCaptures;
    private int currentKbCapturePtr;
    private int currentMouseCapturePtr;

    public RecordingController() {
        this.kbCaptures = new ArrayList<>();
        this.mouseCaptures = new ArrayList<>();
        this.currentKbCapturePtr = 0;
        this.currentMouseCapturePtr = 0;
    }
}
