package ui.tools;

import model.KeyPress;
import model.KeyboardInputTime;
import model.InputRecording;

import java.awt.*;

public class PlaybackTool {
    private final Robot playbackRb;

    public PlaybackTool() throws AWTException, SecurityException {
        this.playbackRb = new Robot();
    }

    // EFFECTS: plays back given keyboard inputs.
    public void playKbInputs(InputRecording<KeyboardInputTime> kbInputs) {
        kbInputs.getInputs().forEach(
                (e) -> {
                    if (e.getKeyPress() == KeyPress.UP) {
                        playbackRb.keyPress(e.getKeyId());
                    } else {
                        playbackRb.keyRelease(e.getKeyId());
                    }
                });
    }

    // EFFECTS: plays back given
}
