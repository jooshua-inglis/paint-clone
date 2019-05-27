package vector.shape;

import vector.VectorCanvas;
import vector.util.*;
import java.awt.Graphics;
import java.util.List;
import vector.util.Point;

public class Line extends VectorShape{

//    public int startX;
//    public int startY;
//    public int endX;
//    public int endY;

    public Line() { }

    public Line(Point startingPoint, VectorColor penColor, VectorColor fillColor) {
        super(startingPoint, penColor, fillColor);
    }
    public Line(List<VectorPoint> points) {
        super(points);
    }

    public void initialise(Point point, VectorColor penColor, VectorColor fillColor) {
        addPoint(point);
        addPoint(point);
        setPen(penColor);
        setFill(fillColor);
    }

    public void lineMaker(Graphics g, java.awt.Point p1, java.awt.Point p2) {
        int startX = p1.x;
        int startY = p1.y;
        int endX =p2.x;
        int endY = p2.y;

        if (getPen().isActive()) {
            g.setColor(getPen().asColor());
        }
        g.drawLine(startX, startY,endX, endY);
    }

    @Override
    public void draw(Graphics g, int size) {
        java.awt.Point p1 = getPoint(0).getAbsPoint(size);
        java.awt.Point p2 = getPoint(1).getAbsPoint(size);
        lineMaker(g,p1,p2);

    }

    @Override
    public int getMaxPoints() {
        return 2;
    }

    public String getName() { return "LINE"; }


}



