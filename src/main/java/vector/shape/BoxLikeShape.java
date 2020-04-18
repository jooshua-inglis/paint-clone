package vector.shape;

import vector.util.Coordinate;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.util.List;

/** An extension of {@link VectorShape} with 2 points
 *
 */
public abstract class BoxLikeShape extends VectorShape {

    BoxLikeShape() {
    }

    BoxLikeShape(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
    }

    public BoxLikeShape(List<VectorPoint> points) {
        super(points);
    }

    public int getMaxPoints() { return 2; }

    public void initialise(Coordinate coordinate, VectorColor penColor, VectorColor fillColor) {
        addPoint(coordinate);
        addPoint(coordinate);
        setPen(penColor);
        setFill(fillColor);
    }

    /**
     * Draws the shape outline to g
     * @param g
     * @param startX top left x
     * @param startY top left y
     * @param width  width
     * @param height height
     */
    abstract void drawPen(Graphics g, int startX, int startY, int width, int height);

    /**
     * Draws the shape fill to g
     * @param g
     * @param startX top left x
     * @param startY top left y
     * @param width  width
     * @param height height
     */
    abstract void drawFill(Graphics g, int startX, int startY, int width, int height);

    public void draw(Graphics g, int size) {
        if (getVectorPoints().size() != getMaxPoints()) {
            throw new IllegalArgumentException("Invalid number of points");
        }
        java.awt.Point p1 = getPoint(0).getAbsPoint(size);
        java.awt.Point p2 = getPoint(1).getAbsPoint(size);

        int rootX = Math.min(p1.x, p2.x);
        int rootY = Math.min(p1.y, p2.y);

        int width = Math.abs(p2.x - p1.x);
        int height = Math.abs(p2.y - p1.y);


        if (getFill().isActive()) {
            g.setColor(getFill().asColor());
            drawFill(g, rootX, rootY, width, height);
        }
        if (getPen().isActive()) {
            g.setColor(getPen().asColor());
            drawPen(g, rootX, rootY, width, height);
        }
    }
}
