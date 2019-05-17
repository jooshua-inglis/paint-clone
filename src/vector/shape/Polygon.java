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

public class Polygon extends VectorShape {

    boolean finished = false;

    public Polygon() {
    }

    public Polygon(vector.util.Point startingPoint, VectorColor penColor, VectorColor fillColor) {
        super(startingPoint, penColor, fillColor);
    }

    public void lineMaker(Graphics g, java.awt.Point p1, java.awt.Point p2) {
        int startX = p1.x;
        int startY = p1.y;
        int endX = p2.x;
        int endY = p2.y;

        if (getPen().isActive()) {
            g.setColor(getPen().asColor());
        }
        g.drawLine(startX, startY, endX, endY);
    }


    @Override
    public void drag(VectorCanvas canvas) {
        CanvasMouse mouseListener = canvas.getMouseListener();
        finished = false;
        boolean first = true;
        ArrayList<java.awt.Point> polyArray = new ArrayList<>();
//        CanvasKeys canvasKey = new CanvasKeys(canvas);

        while(!finished) {
            while (mouseListener.shapeCreating) {
                try {
                    Thread.sleep(0, 500);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                    canvas.repaint();
                    return;
                }
                if(first){

                    this.getPoint(1).update(mouseListener);
                    canvas.repaint();
                    first = false;
                    System.out.println("First");
                }
                else{
                    this.getPoint(1).update(mouseListener);
                    canvas.repaint();
                    System.out.println("not First");
                    first = false;
                }



            }
            if(canvas.keysListener.key == KeyEvent.VK_ENTER) {

                finished = true;
            }


            System.out.println("YAAASSS Biiittcchhh");

            //System.out.println("check");

        }


        System.out.println("check");
        finished = false;

    }


    @Override
    public void draw(Graphics g, int size) {
        ArrayList<java.awt.Point> polyArray = new ArrayList<>();
        //ArrayList<Line> lineArray = new ArrayList<>();
        //polyFinished = false;




                polyArray.add(getPoint(0).getAbsPoint(size));
                polyArray.add(getPoint(1).getAbsPoint(size));
                lineMaker(g, polyArray.get(polyArray.size() - 2), polyArray.get(polyArray.size() - 1));



//            } else {
//                polyArray.add(polyArray.get(polyArray.size()));
//                polyArray.add(getPoint(0).getAbsPoint(size));
//                lineMaker(g, polyArray.get(polyArray.size() - 2), polyArray.get(polyArray.size() - 1));
//
//            }


    }

    @Override
    public int getMaxPoints() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
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
