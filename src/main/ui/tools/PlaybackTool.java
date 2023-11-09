package ui.tools;

import model.KeyboardInputTime;
import model.TimeSeries;

import java.awt.*;

public class PlaybackTool {
    private final Robot playbackRb;

    public PlaybackTool() throws AWTException, SecurityException {
        this.playbackRb = new Robot();
    }

    // TODO: implement
    public void playKbInputs(TimeSeries<KeyboardInputTime> kbInputs) {
        // kbInputs.
    }
}
