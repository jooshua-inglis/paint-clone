package vector.shape;

import vector.VectorCanvas;
import vector.util.CanvasMouse;
import vector.util.VectorColor;

import java.awt.*;

public class Plot extends VectorShape {

    public Plot() {
    }

    public Plot(vector.util.Point startingPoint, VectorColor penColor, VectorColor fillColor) {
        super(startingPoint, penColor, fillColor);
    }

    @Override
    public void draw(Graphics g, int size) {
        java.awt.Point p1 = getPoint(0).getAbsPoint(size);
        int width = 2;
        int height = 2;
        int startX = (p1.x)-(width/2);
        int startY = p1.y-(height/2);


        if (getPen().isActive()) {
            g.setColor(getPen().asColor());
        }
        g.fillOval(startX, startY,width, height);
    }

    public void initialise(vector.util.Point point, VectorColor penColor, VectorColor fillColor) {
        addPoint(point);
        setPen(penColor);
        setFill(fillColor);
    }


    public void drag(VectorCanvas canvas){
        System.out.println("yeehaw");
        CanvasMouse mouseListener = canvas.getMouseListener();
        mouseListener.shapeCreating =false;
    }

    @Override
    public int getMaxPoints() {
        return 1;
    }

    @Override
    public String getName() {
        return "PLOT";
    }
}
