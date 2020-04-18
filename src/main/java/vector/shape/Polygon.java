package vector.shape;

import vector.event_handlers.CanvasMouse;
import vector.ui_components.VectorCanvas;
import vector.util.Coordinate;
import vector.util.VectorColor;
import vector.util.VectorPoint;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link VectorShape} Polygon
 */
public class Polygon extends VectorShape {

    private boolean finished = false;
    LinkedList<VectorPoint> polygonPoints = new LinkedList<>();

    public Polygon() {
        this.finished = false;
    }

    public Polygon(Coordinate startingCoordinate, VectorColor penColor, VectorColor fillColor) {
        super(startingCoordinate, penColor, fillColor);
    }

    /**
     * Creates a polygon based on a list of points
     * @param points
     */
    public Polygon(List<VectorPoint> points) {
        super(points);
    }

    public void initialise(Coordinate coordinate, VectorColor penColor, VectorColor fillColor) {
        addPoint(coordinate);
        addPoint(coordinate);
        setPen(penColor);
        setFill(fillColor);
    }

    /**
     * An override of the drag function. This will stop
     * the polygon from finalising until the user presses 'enter'
     * @param canvas relevant canvas to be used
     */
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

    /**
     * Draws the polygon
     * @param g graphic created by canvas in the paint phase. Shape will be drawn to this graphic.
     * @param size size of canvas.
     */
    @Override
    public void draw(Graphics g, int size) {
        int nPoints = getVectorPoints().size();
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];

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

    public void setFinished(boolean finished) {
        this.finished = finished;
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

