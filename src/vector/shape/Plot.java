package vector.shape;

import vector.eventHandlers.CanvasMouse;
import vector.uiComponents.VectorCanvas;
import vector.util.Coordinate;
import vector.util.VectorColor;

import java.awt.*;

public class Plot extends VectorShape {

    public Plot() {
    }

    public Plot(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
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

    public void initialise(Coordinate coordinate, VectorColor penColor, VectorColor fillColor) {
        addPoint(coordinate);
        setPen(penColor);
        setFill(fillColor);
    }

    public void drag(VectorCanvas canvas){
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
