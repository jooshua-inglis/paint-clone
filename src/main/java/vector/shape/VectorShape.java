package vector.shape;


import vector.eventHandlers.CanvasMouse;
import vector.uiComponents.VectorCanvas;
import vector.util.Coordinate;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * VectorShapes are the shapes that appear on the window that the user can create and manipulate.
 * Each type of shape is an object of a class that extends this this class.
 * An example
 * <pre>
 *     {@code
 *      VectorShape shape = new Rectangle();
 *      shape.addPoint(0.2, 0.5);
 *      shape.addPoint(0.6,0.3);
 *      shape.setFillRGB("#ff0000");
 *      shape.setPenRGB("#00ff00");
 *     }
 * </pre>
 */
public abstract class VectorShape  {
    private VectorColor fillColor;
    private VectorColor penColor;
    private LinkedList<VectorPoint> vectorPoints;

    /**
     * A helper to initialise the class to minimise code duplication.
     * @param fillColor determines fill color
     * @param penColor determines pen color
     */
    private void init(VectorColor fillColor, VectorColor penColor) {
        this.fillColor = fillColor;
        this.penColor = penColor;
        vectorPoints = new LinkedList<>();
    }

    public VectorShape() {
        init(new VectorColor(0, false), new VectorColor(0));
    }

    public VectorShape(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor)  {
        init(fillColor, penColor);
        addPoint(startingCoordinate);
    }

    public VectorShape(List<VectorPoint> points) {
        init(new VectorColor(0, false), new VectorColor(0));
        addPoints(points);
    }

    /**
     * Draws self to graphic g
     * @param g graphic created by canvas in the paint phase. Shape will be drawn to this graphic.
     * @param size size of canvas.
     */
    public abstract void draw(Graphics g, int size);

    /**
     * Initialises function when created by canvas
     * @param coordinate Initial coordinate
     * @param penColor Pen color
     * @param fillColor Fill color
     */
    public abstract void initialise(Coordinate coordinate, VectorColor penColor, VectorColor fillColor);

    /**
     * Drags the shape across the screen so user can resize shape
     * @param canvas relevant canvas to be used
     */
    public void drag(VectorCanvas canvas) {
        CanvasMouse mouseListener = canvas.getMouseListener();
        while (mouseListener.shapeCreating) {
            try {
                Thread.sleep(0, 500);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                canvas.repaint();
                return;
            }
            this.getPoint(1).update(mouseListener);
            canvas.repaint();
        }
        mouseListener.clicked = false;
        System.out.println("Done");
    }

    public void addPoints(List<VectorPoint> points) {
        if ( getMaxPoints() != 0 && points.size() + getVectorPoints().size() > getMaxPoints()) {
            throw new IllegalArgumentException("Too many points");
        }
        for (Coordinate coordinate : points ) {
            addPoint(coordinate);
        }
    }

    public void addPoint(Coordinate vectorCoordinate) throws IllegalStateException {
        if (getMaxPoints() != 0 && vectorPoints.size() >= getMaxPoints() ) {
            throw new IllegalStateException("Exceeded max VectorPoints");
        }
        vectorPoints.add(new VectorPoint(vectorCoordinate));
    }

    /**
     * Creates a new VectorPoint with points x and y
     * @param x horizontal component
     * @param y vertical component
     * @throws IllegalArgumentException throws exception if point is less than 0 or greater than 1
     */
    public void addPoint(double x, double y) throws IllegalArgumentException {
        addPoint(new VectorPoint(x, y));
    }

    /**
     * Updates the ith coordinate with the x and y values of coordinate.
     * @param i the index of the coordinate to update
     * @param coordinate Coordinate object
     */
    public void editPoint(int i, Coordinate coordinate) {
        vectorPoints.get(i).update(coordinate);
    }

    /**
     * Removes the ith point
     * @param i index of the point to remove
     */
    public void remove(int i) {
        vectorPoints.remove(i);
    }

    /**
     * Converts x and y of all points into a list
     * @return ArrayList of doubles, [x1, y1, x2, y2, x3, y3...].
     */
    public List<Double> asList() {
        ArrayList<Double> output = new ArrayList<>();
        for (Coordinate vectorCoordinate : vectorPoints) {
            output.addAll(vectorCoordinate.asList());
        }
        return output;
    }

    /**
     * VectorPoints getter
     * @return ArrayList of VectorPoints
     */
    public LinkedList<VectorPoint> getVectorPoints() {
        return this.vectorPoints;
    }

    /**
     * Gets the point at index i
     * @param i index of the point to get
     * @return the point at index i
     */
    public VectorPoint getPoint(int i) { return this.vectorPoints.get(i); }

    /**
     * The maximum number of points a shape can have, for example squares, ellipses and lines have two points,
     * however a polygon has no limit. (If there is no limit, this function returns 0.
     * @return Max number of points a shape can have
     */
    public abstract int getMaxPoints();

    /**
     * VEC command corresponding to this shape
     * @return name
     */
    public abstract String getName();


    public void setFill(VectorColor color) {
        fillColor.update(color);
    }

    public void setFill(int color) { fillColor.setRgb(color); }

    public VectorColor getFill() {
        return fillColor;
    }

    public boolean isFillActive() {
        return fillColor.isActive();
    }

    public void setFillActive(boolean fillActive) {
        fillColor.setActive(fillActive);
    }

    public void setPen(VectorColor color) {
        penColor.update(color);
    }

    public void setPen(int color) {fillColor.setRgb(color);}

    public VectorColor getPen() {
        return penColor;
    }

    /**
     * Returns a RGB string representation of the pen color.
     * For example if the pen color was red, this command would return "#ff0000"
     * @return rgb string of color
     */
    public String getPenRGB() { return getPen().toString(); }

    /**
     * Returns a RGB string representation of the pen color.
     * For example if the pen color was red, this command would return "#ff0000"
     * @return rgb string of color
     */
    public String getFillRGB() {
        return getFill().toString();
    }

    /**
     * Gets the VEC representation of the shape.
     * @param includePenColor Whether to include the PEN command
     * @param includeFillColor Whether to include the FILL command
     * @return String containing VEC
     * @throws IllegalStateException throws exception if input is invalid (not proper hex string) or hex number is out
     * of bounds ie less than 0 or greater than 0xffffff
     */
    public String getVec(boolean includePenColor, boolean includeFillColor) throws IllegalStateException {
        if ( getMaxPoints() != 0 && vectorPoints.size() != getMaxPoints()) {
            throw new IllegalStateException("Invalid number of VectorPoints");
        }
        StringBuilder output = new StringBuilder();

        if (includePenColor) {
            output.append("PEN ");
            output.append(getPenRGB());
            output.append('\n');
        }
        if (includeFillColor) {
            output.append("FILL ");
            output.append(getFillRGB());
            output.append('\n');
        }
        output.append(getName());

        for (VectorPoint vectorPoint : getVectorPoints() ) {
            output.append(" ");
            output.append(vectorPoint.toString());
        }
        return output.toString();
    }

    /**
     *
     * @return VEC representation including both pen and fill
     */
    public String toString() {
        return getVec(true, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VectorShape) {
            VectorShape shape = (VectorShape) obj;
            return getVectorPoints().containsAll(shape.getVectorPoints());
        } else {
            return false;
        }
    }
}
