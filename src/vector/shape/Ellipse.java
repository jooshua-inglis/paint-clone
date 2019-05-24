package vector.shape;

import vector.util.Point;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.util.List;

public class Ellipse extends BoxLikeShape {

    public Ellipse() { }

    public Ellipse(Point startingPoint, VectorColor penColor, VectorColor fillColor) {
        super(startingPoint, penColor, fillColor);
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