package vector.ui_components;

import vector.exception.ShapeException;
import vector.shape.Polygon;
import vector.shape.Rectangle;
import vector.shape.*;

import javax.swing.*;
import java.awt.*;

/**
 * This enum class is used to determine what drawing tool buttons will be used in the Tools panel. The enums will form
 * the names of the buttons.
 */
public enum Tool {
    RECTANGLE("rectangle.png"),
    ELLIPSE("ellipse.png"),
    POLYGON("polygon.png"),
    LINE("line.png"),
    PLOT("plot.png");

    private ImageIcon icon;
    private Dimension size;

    private ResourceLoader loader = new ResourceLoader();

    Tool(String fileName, Dimension size) {
        this.icon = loader.getImageIcon(fileName);
        this.size = size;
    }

    Tool(String fileName) {
        this(fileName, new Dimension(40, 30));
    }

    /**
     * This method is used to set the image icon for the button corresponding to a specific enum. The images are
     * located in the UtilImages package.
     * @return ImageIcon used to set button icon
     */
    public Icon getImage() {
        return this.icon;
    }

    /**
     * This method is used to set the size of the specified button.
     * @param button The button that is being resized
     */
    public void setSize(AbstractButton button) {
        button.setPreferredSize(size);
    }

    /**
     * This method is used to execute the drawing method corresponding to the current enum value. The drawing methods
     * are located in the 'shape' package
     * @return a method of type VectorShape. The method contains drawing methods for the tool
     * @throws ShapeException exception no shape function is found
     */
    public VectorShape getCls() throws ShapeException {
        /*
         switch-statement to determine which enum button representation is being resized. Can be used to change the
         size of specific buttons
        */
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
                throw new ShapeException("Cannot create shape");

        }
    }
}

// Black
// Then
// White are
// All I see
// In my infancy
// Red and yellow then came to be
// Reaching out to me
// Lets me see
