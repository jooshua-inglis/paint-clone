package vector.uiComponents;

import vector.exception.CanvasException;
import vector.shape.Polygon;
import vector.shape.Rectangle;
import vector.shape.*;

import javax.swing.*;
import java.awt.*;

//import javax.sound.sampled.Line;

public enum Tool {
    RECTANGLE,
    ELLIPSE,
    POLYGON,
    LINE,
    PLOT;


    public Icon getImage(){
        switch (this){
            case RECTANGLE:
                return new ImageIcon("src/vector/shape/ShapeImages/rectangle.png");
            case ELLIPSE:
                return new ImageIcon("src/vector/shape/ShapeImages/ellipse.png");
            case POLYGON:
                return new ImageIcon("src/vector/shape/ShapeImages/polygon.png");
            case LINE:
                return new ImageIcon("src/vector/shape/ShapeImages/line.png");
            case PLOT:
                return new ImageIcon("src/vector/shape/ShapeImages/plot.png");
            default:
                assert (true);
                return null;
        }
    }
    public void setSize(AbstractButton button){
        switch (this){
            case RECTANGLE:
            case ELLIPSE:
            case POLYGON:
            case LINE:
            case PLOT:
                button.setPreferredSize(new Dimension(50,40));
                break;
        }
    }

    public VectorShape getCls() throws CanvasException {
        switch (this) {
            case RECTANGLE:
                return new Rectangle();
            case ELLIPSE:
                return new Ellipse();
            case POLYGON:
                return new Polygon();
           case LINE:
               return new Line();
           case PLOT:
               return new Plot();
            default:
                throw new CanvasException("Cannot create shape");

        }
    }
}
