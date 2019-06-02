package vector.shape;

import vector.util.Coordinate;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.util.List;

/**
 * A {@link VectorShape} which extends {@link BoxLikeShape}
 */
public class Ellipse extends BoxLikeShape {

    public Ellipse() { }

    public Ellipse(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
    }

    /**
     * Creates an ellipse instance using provided VectorPoints
     * @param points A list of VectorPoints
     */
    public Ellipse(List<VectorPoint> points) {
        super(points);
    }
    /**
     *
     * @return Name of tool used
     */
    public String getName() { return "ELLIPSE"; }

    /**
     * Draws a filled in ellipse
     * @param g
     * @param startX top left x
     * @param startY top left y
     * @param width  width
     * @param height height
     */
    void drawFill(Graphics g, int startX, int startY, int width, int height) {
        g.fillOval(startX, startY, width, height);
    }

    /**
     * Draws ellipse border
     * @param g
     * @param startX top left x
     * @param startY top left y
     * @param width  width
     * @param height height
     *
     */
    void drawPen(Graphics g, int startX, int startY, int width, int height) {
        g.drawOval(startX, startY, width, height);
    }
}