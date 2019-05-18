package vector.util;

import vector.VectorCanvas;

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
//        if(key == KeyEvent.VK_ENTER){
//            System.out.println("YAAASSS Biiittcchhh");
//            CanvasMouse mouseListener = VectorCanvas.getMouseListener();
//            mouseListener.shapeCreating =false;
//            Thread closeShape = new Thread(() ->  mouseListener. );
//
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}
