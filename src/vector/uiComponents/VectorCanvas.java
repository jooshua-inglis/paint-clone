package vector.uiComponents;

import vector.eventHandlers.CanvasKeys;
import vector.eventHandlers.CanvasMouse;
import vector.exception.ShapeException;
import vector.exception.UndoException;
import vector.shape.Line;
import vector.shape.VectorShape;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import java.awt.*;
import java.util.ArrayList;
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
    public List<Line> grid;
    public boolean gridToggle;
    public double nLines = 30;
    private Tool selectedTool;
    private VectorColor selectedPenColor, selectedFillColor;
    private CanvasMouse mouseListener;
    public CanvasKeys keysListener;
    private int sideWidth;
    private double scale;



    private List<Line> createGrid(){
        List<Line> Grid = new ArrayList<>();
        double buffer =1.0/nLines;
        double coordinate = 0;
        for(double i= 0; i<nLines;i++) {
            List<VectorPoint> horizontal = new ArrayList<>();
            List<VectorPoint> vertical = new ArrayList<>();
            for(int j=0;j<2;j++) {
                VectorPoint p1 = new VectorPoint(j, coordinate);
                horizontal.add(p1);
                VectorPoint p3 = new VectorPoint(coordinate, j);
                vertical.add(p3);
            }
            Line xLine = new Line(horizontal);
            Line yLine = new Line(vertical);
            VectorColor color = new VectorColor(0xCECECE);
            xLine.setPen(color);
            yLine.setPen(color);
            Grid.add(xLine);
            Grid.add(yLine);
            coordinate+=buffer;
        }
        return Grid;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.paintBorder(g);
        // Set keyboard focus to a component
        this.setFocusable(true);
        this.requestFocusInWindow();
        int num = 0;
        if(gridToggle) {
            grid = createGrid();
            //System.out.println(grid);
            for (Line line : grid) {
                line.draw(g, getWidth());
            }
        }
        for (VectorShape shape : shapes) {

            shape.draw(g, getWidth());
            num++;
        }
        g.dispose();

    }

    public VectorCanvas() {
        selectedFillColor = new VectorColor(0xfffff, false);
        selectedPenColor = new VectorColor(0);
        gridToggle = false;
        shapes = new LinkedList<>();
        selectedTool = Tool.LINE;
        mouseListener = new CanvasMouse();
        mouseListener.attachCanvas(this);
        this.addMouseListener(mouseListener);

        keysListener = new CanvasKeys();
        keysListener.attachCanvas(this);
        this.addKeyListener(keysListener);
        this.scale = 0.5;
    }

    public int getSideWith() {
        return sideWidth;
    }

    public void setSideWidth(int sideWidth) {
        this.sideWidth = sideWidth;
    }

    /**
     * Creates a shape given the value of {@link Tool selectedTool} and adds it to the canvas
     * @return VectorShape
     * @throws ShapeException
     */
    public VectorShape createShape() throws ShapeException {
        VectorShape s = selectedTool.getCls();
        s.initialise(mouseListener, selectedPenColor, selectedFillColor);
        addShape(s);
        return s;
    }

    public void undo() {
        if (mouseListener.shapeCreating) {throw new UndoException("Cannot undo when creating shape");}
        try {
            shapes.removeLast();
            repaint();

        } catch (NoSuchElementException e) {
            throw new UndoException("There are no more shapes to undo!");
        }
    }

    public void zoom(double amount) {
        scale += amount;
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
     * @param canvas
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

    public double getScale() { return scale; }


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

