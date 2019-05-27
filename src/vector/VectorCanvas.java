package vector;

import vector.exception.CanvasException;
import vector.shape.VectorShape;
import vector.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Contains all data to be displayed to be displayed on the window. Extends Canvas.
 * Contains all shapes created by user, the currently selected tool and the dimensions of the window.
 * Example use.
 * <pre>
 * {@code
 * VectorCanvas canvas = new VectorCanvas();
 * canvas.selectTool(Tool.RECTANGLE);
 * canvas.createShape();
 * }
 * </pre>
 */
public class VectorCanvas extends JPanel {
    /** List of all shapes */
    private LinkedList<VectorShape> shapes;
    private Tool selectedTool;
    private VectorColor selectedPenColor, selectedFillColor;
    private CanvasMouse mouseListener;
    public CanvasKeys keysListener;
    private int sideWidth;

    /**
     * Method required by Canvas class to be able to be printed to the window
     * @param g Graphic
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.paintBorder(g);
        // Set keyboard focus to a component
        this.setFocusable(true);
//        System.out.println(this.isFocusable());
        this.requestFocusInWindow();
//        System.out.println(this.isFocusOwner());
        int num = 0;
        for (VectorShape shape : shapes) {
//            System.out.println("Drawing:" + shape.getName() + num);
//            System.out.println(getWidth());
            shape.draw(g, getWidth());
            num++;
        }
        g.dispose();
    }

    public VectorCanvas() {
        selectedFillColor = new VectorColor(0xfffff, false);
        selectedPenColor = new VectorColor(0);
        shapes = new LinkedList<>();
        selectedTool = Tool.LINE;
        mouseListener = new CanvasMouse();
        mouseListener.attachCanvas(this);
        this.addMouseListener(mouseListener);

        keysListener = new CanvasKeys();
        keysListener.attachCanvas(this);
        this.addKeyListener(keysListener);
    }

    public int getSideWith() {
        return sideWidth;
    }

    public void setSideWidth(int sideWidth) {
        this.sideWidth = sideWidth;
    }

    /**
     * Creates a shape given the value of {@link Tool selectedTool} and adds it to the canvas
     */
    public VectorShape createShape() throws CanvasException {
        VectorShape s = selectedTool.getCls();
        s.initialise(mouseListener, selectedPenColor, selectedFillColor);
        addShape(s);
        return s;
    }

    public boolean undo() {
        try {
            shapes.removeLast();
            repaint();
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("No more undoes");
            return false;
        }
    }

    public void zoom(int amount) {
        Dimension d = getPreferredSize();
        d.width += amount;
        d.height += amount;
        setPreferredSize(d);
        setSize(d);
    }

    /**
     * Add {@link VectorShape shape} to canvas
     * @param shape
     */
    public void addShape(VectorShape shape) {
        shapes.add(shape);
    }

    /**
     * Gets a list of {@link VectorShape shapes} on the canvas
     * @return
     */

    public void copyShapes(VectorCanvas canvas) {
        for (VectorShape shape: canvas.getShapes()) {
            addShape(shape);
        }
    }
    public List<VectorShape> getShapes() {
        return shapes;
    }

    public VectorColor getSelectedPenColor() {
        return selectedPenColor;
    }

    public void setSelectedPenColor(VectorColor color) {
        selectedPenColor.update(color);
    }

    public VectorColor getSelectedFillColor() {
        return selectedFillColor;
    }

    public void setSelectedFillColor(VectorColor color) {
        selectedFillColor.update(color);
    }


    /**
     * gets {@link Tool currently selected tool}
     * @param tool
     */
    public void selectTool(Tool tool) { selectedTool = tool;}

    public Tool getSelectedTool(){
        return selectedTool;
    }

    public CanvasMouse getMouseListener() {
        return mouseListener;
    }

    public boolean isShapeCreating(){
        return mouseListener.shapeCreating;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VectorCanvas) {
            VectorCanvas canvas = (VectorCanvas) obj;
            return canvas.getShapes().containsAll(getShapes());
        } else {
            return false;
        }
    }
}

