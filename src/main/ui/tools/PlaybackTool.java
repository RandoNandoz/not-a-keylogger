package ui.tools;

import model.KeyPress;
import model.KeyboardInputTime;
import model.TimeSeries;

import java.awt.*;

public class PlaybackTool {
    private final Robot playbackRb;

    public PlaybackTool() throws AWTException, SecurityException {
        this.playbackRb = new Robot();
    }

    // EFFECTS: plays back given keyboard inputs.
    public void playKbInputs(TimeSeries<KeyboardInputTime> kbInputs) {
        kbInputs.getInputs().forEach(
                (e) -> {
                    if (e.getKeyPress() == KeyPress.UP) {
                        playbackRb.keyPress(e.getKeyId());
                    } else {
                        playbackRb.keyRelease(e.getKeyId());
                    }
                });
    }
}
