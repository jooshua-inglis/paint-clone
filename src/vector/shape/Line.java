package vector.shape;

import vector.util.Coordinate;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.util.List;

public class Line extends VectorShape{

    public Line() { }

    public Line(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
    }
    public Line(List<VectorPoint> points) {
        super(points);
    }

    public void initialise(Coordinate coordinate, VectorColor penColor, VectorColor fillColor) {
        addPoint(coordinate);
        addPoint(coordinate);
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



