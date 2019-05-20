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

public class Polygon extends VectorShape {

    private boolean finished = false;
    LinkedList<VectorPoint> polygonPoints = new LinkedList<>();

    public Polygon() {
        this.finished = false;
    }

    public Polygon(vector.util.Point startingPoint, VectorColor penColor, VectorColor fillColor) {
        super(startingPoint, penColor, fillColor);
    }


    @Override
    public void drag(VectorCanvas canvas) {
        CanvasMouse mouseListener = canvas.getMouseListener();
        polygonPoints.add(this.getPoint(0));
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
                this.getPoint(1).update(mouseListener);

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
                polygonPoints.add(point);
                System.out.println(polygonPoints);
            }
        }
        mouseListener.shapeCreating = false;
        System.out.println("x position:"+mouseListener.getX()+"y"+mouseListener.getX());
    }

    @Override
    public void draw(Graphics g, int size) {

//        if(finished){
            int nPoints = polygonPoints.size();
            int xPoints[]=new int[nPoints];
            int yPoints[]=new int[nPoints];
            for(int i=0; i<nPoints;i++){
                Point point = polygonPoints.get(i).getAbsPoint(size);
                xPoints[i] = point.x;
                yPoints[i] = point.y;
            }
            java.awt.Polygon polygon = new java.awt.Polygon(xPoints,yPoints,nPoints);

        if (getPen().isActive()) {
            g.setColor(getPen().asColor());
            g.drawPolygon(polygon);
        }
        if(getFill().isActive()){
            g.setColor(getFill().asColor());
            g.fillPolygon(polygon);
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




//    public void drag(VectorCanvas canvas){
//        System.out.println("yeehaw");
//        CanvasMouse mouseListener = canvas.getMouseListener();
//        mouseListener.shapeCreating =false;
//    }
//
//    @Override
//    public void draw(Graphics g, int size) {
//        int[] xpoints = {40,20,20,40};
//        int[] ypoints ={40,40,20,20};
//        Polygon p = new Polygon(xpoints,ypoints,4);
////        int x = ren.isLeftmost() ? 3 : 0;
////        int y = 0;
////
////        int width = ren.isLeftmost() ? c.getWidth() - 3 : c.getWidth();
////        int height = c.getHeight() - 4;
//
//        //Modified to return rectangle
////        p.addPoint(20,20);
////        p.addPoint(20 , 40);
////        p.addPoint(40, 40);
////        p.addPoint(20, 40);
//
//
//    }
//
//    @Override
//    public int getMaxPoints() {
//        return 0;
//    }
//
//    @Override
//    public String getName() {
//        return "POLYGON";
//    }
//}
