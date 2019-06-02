package vector.eventHandlers;

import vector.GUI;
import vector.uiComponents.VectorCanvas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CanvasKeys implements KeyListener {

    private VectorCanvas VectorCanvas;

    public int key;

    public void attachCanvas(VectorCanvas c) {
        VectorCanvas = c;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.key = e.getKeyCode();
        if((e.getKeyCode() == KeyEvent.VK_Z) && e.isControlDown()) {
            GUI.undo();
        }
        else if((e.getKeyCode() == KeyEvent.VK_UP) && e.isControlDown()) {
            GUI.zoomInGrid();
        }
        else if((e.getKeyCode() == KeyEvent.VK_DOWN) && e.isControlDown()) {
            GUI.zoomOutGrid();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}
