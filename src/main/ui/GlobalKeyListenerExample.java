package ui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListenerExample implements NativeKeyListener {
    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Modifiers: " + e.getModifiers());
        System.out.println("Raw Code: " + e.getRawCode());
        System.out.println("Key Pressed: " + e.getKeyCode());
        System.out.println("Key Char: " + e.getKeyChar());
        System.out.println("Key Location: " + e.getKeyLocation());

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Modifiers: " + e.getModifiers());
        System.out.println("Raw Code: " + e.getRawCode());
        System.out.println("Key Pressed: " + e.getKeyCode());
        System.out.println("Key Char: " + e.getKeyChar());
        System.out.println("Key Location: " + e.getKeyLocation());
    }
}