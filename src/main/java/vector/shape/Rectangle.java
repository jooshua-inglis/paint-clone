package vector.shape;

import vector.util.*;
import java.awt.Graphics;
import java.util.List;
import vector.util.Coordinate;

/**
 * A {@link VectorShape} rectangle
 */
public class Rectangle extends BoxLikeShape {

    public Rectangle() { }

    public Rectangle(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
    }

    public Rectangle(List<VectorPoint> points) {
        super(points);
    }

    public String getName() { return "RECTANGLE"; }

    /**
     * Draws a filled in rectangle
     * @param g
     * @param startX top left x
     * @param startY top left y
     * @param width  width
     * @param height height
     */

    void drawFill(Graphics g, int startX, int startY, int width, int height) {
        g.fillRect(startX, startY, width, height);
    }

    /**
     * Draws rectangle border
     * @param g
     * @param startX top left x
     * @param startY top left y
     * @param width  width
     * @param height height
     */
    void drawPen(Graphics g, int startX, int startY, int width, int height) {
        g.drawRect(startX, startY, width, height);
    }
}
