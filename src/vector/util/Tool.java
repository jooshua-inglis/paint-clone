package vector.util;

import vector.shape.*;
import vector.shape.Polygon;
import vector.shape.Rectangle;
import vector.shape.Ellipse;
import vector.shape.Line;


//import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;

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






    public VectorShape getCls() {
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
                assert (true);
                return null;
        }
    }
}
