package vector.util;

import vector.VectorCanvas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CanvasKeys implements KeyListener {

    private VectorCanvas VectorCanvas;

    public void attachCanvas(VectorCanvas c) {
        VectorCanvas = c;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key Pressed:");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key Pressed:");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key Pressed:");

    }
}
