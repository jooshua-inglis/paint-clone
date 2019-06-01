package vector.shape;

import vector.util.Coordinate;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.util.List;

public class Ellipse extends BoxLikeShape {

    public Ellipse() { }

    public Ellipse(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
    }

    public Ellipse(List<VectorPoint> points) {
        super(points);
    }

    public String getName() { return "ELLIPSE"; }

    void drawFill(Graphics g, int startX, int startY, int width, int height) {
        g.fillOval(startX, startY, width, height);
    }

    void drawPen(Graphics g, int startX, int startY, int width, int height) {
        g.drawOval(startX, startY, width, height);
    }
}