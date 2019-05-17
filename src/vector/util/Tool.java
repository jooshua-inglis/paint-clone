package vector.util;

import vector.shape.Rectangle;
import vector.shape.Ellipse;
import vector.shape.Line;
import vector.shape.Plot;
import vector.shape.Polygon;


import vector.shape.VectorShape;

public enum Tool {
    RECTANGLE,
    ELLIPSE,
    POLYGON,
    LINE,
    PLOT;


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
