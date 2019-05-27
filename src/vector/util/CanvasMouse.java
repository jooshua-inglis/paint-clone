package vector.util;

import vector.VectorCanvas;
import vector.shape.VectorShape;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.List;

public class CanvasMouse implements MouseListener, MouseMotionListener, Point {

    private VectorCanvas vectorCanvas;
    public boolean shapeCreating = false;
    public boolean polygon = false;
    private int x;
    private int y;

    public void attachCanvas(VectorCanvas c) {
        vectorCanvas = c;
    }

    public void mouseClicked(MouseEvent mouseEvent) { }

    public void mousePressed(MouseEvent mouseEvent) {
        System.out.println("Clicked on " + this.getX());
        System.out.println("Clicked on " + this.getY());

        if (!shapeCreating) {
            if(!polygon){
                VectorShape s = vectorCanvas.createShape();
                s.addPoint(new VectorPoint(this));
                s.addPoint(new VectorPoint(this));
                shapeCreating = true;
                vectorCanvas.repaint();
                Thread createShape = new Thread(() -> s.drag(vectorCanvas));
                createShape.start();
            }
        } else {
            shapeCreating = false;
        }
        System.out.println("Creating: " + vectorCanvas.getselectTool().name());
    }

    public boolean getShapeCreating(){
        return this.shapeCreating;
    }

    public void mouseReleased(MouseEvent mouseEvent) { }

    public void mouseEntered(MouseEvent mouseEvent) { }

    public void mouseExited(MouseEvent mouseEvent) { }

    public void mouseDragged(MouseEvent mouseEvent) { }

    public void mouseMoved(MouseEvent mouseEvent) { }

    public double getX() {
        try { x = vectorCanvas.getMousePosition().x; }
        catch (NullPointerException e) { }
        return (double) x / vectorCanvas.getWidth();
    }
    public double getY() {
        try {y = vectorCanvas.getMousePosition().y; }
        catch (NullPointerException e) { }
        return (double) y / vectorCanvas.getHeight();
    }
    public List<Double> asList() {
        return Arrays.asList(getX(), getY());
    }
    public java.awt.Point getAbsPoint(int canvasSideLength) {
        return new java.awt.Point((int) getX() * canvasSideLength, (int) getY() * canvasSideLength);
    }
    public java.awt.Point asPoint() {
        return getAbsPoint(vectorCanvas.getSideWith());
    }
}
