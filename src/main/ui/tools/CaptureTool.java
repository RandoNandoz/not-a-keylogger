package ui.tools;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;
import ui.AppState;

public class CaptureTool implements NativeKeyListener, NativeMouseInputListener {
    private TimeSeries<KeyboardInputTime> keyboardCaptures;
    private TimeSeries<MouseInputTime> mouseCaptures;

    // EFFECTS: creates new capture tool with null captures, one must set the arrays to capture for.
    public CaptureTool() {
        this.keyboardCaptures = null;
        this.mouseCaptures = null;
    }

    // MODIFIES: this
    // EFFECTS: adds the key id, key press type (either up or down),
    // and the time it was pressed to the keyboard captures list
    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        if (AppState.getInstance().isRecording()) {
            this.keyboardCaptures.addKey(new KeyboardInputTime(
                    nativeEvent.getKeyCode(),
                    KeyPress.UP,
                    System.nanoTime()
            ));
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the key id, key press type (either up or down),
    // and the time it was pressed to the keyboard captures list
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        if (AppState.getInstance().isRecording()) {
            this.keyboardCaptures.addKey(new KeyboardInputTime(
                    nativeEvent.getKeyCode(),
                    KeyPress.DOWN,
                    System.nanoTime()
            ));
        }
    }

    // MODIFIES: this
    // EFFECTS: adds mouse event when mouse pressed, and the time it was pressed.
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (AppState.getInstance().isRecording()) {
            this.mouseCaptures.addKey(
                    new MouseInputTime(
                            e,
                            System.nanoTime()
                    )
            );
        }
    }

    // EFFECTS: adds mouse event when mouse released and its time it pressed.
    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if (AppState.getInstance().isRecording()) {
            this.mouseCaptures.addKey(
                    new MouseInputTime(
                            e,
                            System.nanoTime()
                    )
            );
        }
    }

    // MODIFIES: this
    // EFFECTS: adds mouse event when mouse moved and time pressed.
    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        if (AppState.getInstance().isRecording()) {
            this.mouseCaptures.addKey(
                    new MouseInputTime(
                            e,
                            System.nanoTime()
                    )
            );
        }
    }

    public void setKeyboardCaptures(TimeSeries<KeyboardInputTime> keyboardCaptures) {
        this.keyboardCaptures = keyboardCaptures;
    }

    public void setMouseCaptures(TimeSeries<MouseInputTime> mouseCaptures) {
        this.mouseCaptures = mouseCaptures;
    }
}
