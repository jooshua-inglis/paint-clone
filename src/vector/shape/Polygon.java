package vector.shape;

import vector.VectorCanvas;
import vector.util.CanvasKeys;
import vector.util.CanvasMouse;
import vector.util.VectorColor;

import vector.util.VectorPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Polygon extends VectorShape {

    private boolean finished = false;
    LinkedList<VectorPoint> polygonPoints = new LinkedList<>();

    public Polygon() {
        this.finished = false;
    }

    public Polygon(vector.util.Point startingPoint, VectorColor penColor, VectorColor fillColor) {
        super(startingPoint, penColor, fillColor);
    }

    public Polygon(List<VectorPoint> points) {
        super(points);
    }

    public void initialise(vector.util.Point point, VectorColor penColor, VectorColor fillColor) {
        addPoint(point);
        addPoint(point);
        setPen(penColor);
        setFill(fillColor);
    }

    @Override
    public void drag(VectorCanvas canvas) {
        CanvasMouse mouseListener = canvas.getMouseListener();
        finished = false;
        while(!finished) {
            mouseListener.shapeCreating=true;
            while (mouseListener.shapeCreating) {
                try {
                    Thread.sleep(0, 500);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                    canvas.repaint();
                    return;
                }
                this.getVectorPoints().getLast().update(mouseListener);

                canvas.repaint();
                if(canvas.keysListener.key == KeyEvent.VK_ENTER) {
                    canvas.keysListener.key =0;
                    finished = true;
                    mouseListener.shapeCreating=false;
                    mouseListener.polygon = false;
                    System.out.println("Finished");
                }
            }
            if(!finished) {
                VectorPoint point = new VectorPoint(this.getPoint(1).getX(), this.getPoint(1).getY());
                System.out.println(point);
                addPoint(point);
            }
        }
        mouseListener.shapeCreating = false;
        System.out.println("x position:"+mouseListener.getX()+"y"+mouseListener.getX());
    }

    @Override
    public void draw(Graphics g, int size) {

            int nPoints = getVectorPoints().size();
            int xPoints[]=new int[nPoints];
            int yPoints[]=new int[nPoints];
            for(int i=0; i<nPoints;i++){
                Point point = getVectorPoints().get(i).getAbsPoint(size);
                xPoints[i] = point.x;
                yPoints[i] = point.y;
            }
            java.awt.Polygon polygon = new java.awt.Polygon(xPoints,yPoints,nPoints);

        if(getFill().isActive()){
            g.setColor(getFill().asColor());
            if (finished) {g.fillPolygon(xPoints, yPoints, nPoints); }
        }
        if (getPen().isActive()) {
            g.setColor(getPen().asColor());
            if (finished) { g.drawPolygon(xPoints, yPoints, nPoints); }
            else { g.drawPolyline(xPoints, yPoints, nPoints); }
        }

    }

    @Override
    public int getMaxPoints() {
        return 0;
    }

    @Override
    public String getName() {
        return "POLYGON";
    }
}

