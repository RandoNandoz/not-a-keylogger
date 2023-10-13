package ui.tools;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;

public class CaptureTool implements NativeKeyListener, NativeMouseInputListener {
    private final TimeSeries<KeyboardInputTime> keyboardCaptures;
    private final TimeSeries<MouseInputTime> mouseCaptures;


    // EFFECTS: creates a new CaptureTool that captures user inputs, with time series to be modified.
    public CaptureTool(TimeSeries<KeyboardInputTime> keyboardCaptures, TimeSeries<MouseInputTime> mouseCaptures) {
        this.keyboardCaptures = keyboardCaptures;
        this.mouseCaptures = mouseCaptures;
    }

    // MODIFIES: this
    // EFFECTS: adds the key id, key press type (either up or down),
    // and the time it was pressed to the keyboard captures list
    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        this.keyboardCaptures.addKey(new KeyboardInputTime(
                nativeEvent.getKeyCode(),
                KeyPress.UP,
                System.nanoTime()
        ));
    }

    // MODIFIES: this
    // EFFECTS: adds the key id, key press type (either up or down),
    // and the time it was pressed to the keyboard captures list
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        this.keyboardCaptures.addKey(new KeyboardInputTime(
                nativeEvent.getKeyCode(),
                KeyPress.DOWN,
                System.nanoTime()
        ));
    }

    // MODIFIES: this
    // EFFECTS: adds mouse event when mouse pressed, and the time it was pressed.
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        this.mouseCaptures.addKey(
                new MouseInputTime(
                        e,
                        System.nanoTime()
                )
        );
    }

    // EFFECTS: adds mouse event when mouse released and its time it pressed.
    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        this.mouseCaptures.addKey(
                new MouseInputTime(
                        e,
                        System.nanoTime()
                )
        );
    }

    // MODIFIES: this
    // EFFECTS: adds mouse event when mouse moved and time pressed.
    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        this.mouseCaptures.addKey(
                new MouseInputTime(
                        e,
                        System.nanoTime()
                )
        );
    }
}
