package vector.uiComponents;
import javax.swing.*;
import java.awt.*;

/**
 * This enum class is used to determine what tool buttons will be used for the Colour panel on the sidebar. The enum
 * values will form the names of buttons
 */
public enum ColourTools {
    PEN,
    PEN_COLOUR,
    FILL,
    FILL_COLOUR,
    FILL_OFF,
    PICKER;

    /**
     * This method is used to set the image icon for the button corresponding to a specific enum. The images are
     * located in the UtilImages package.
     * @return ImageIcon used to set button icon
     */
    public Icon getImage() {
        // switch-statement to determine which image is returned
        switch (this) {
            case PEN:
                return new ImageIcon("src/vector/util/UtilImages/pen.png");
            case FILL:
                return new ImageIcon("src/vector/util/UtilImages/fill.png");
            case FILL_OFF:
                return new ImageIcon("src/vector/util/UtilImages/FILL_OFF.png");
            case PICKER:
                return new ImageIcon("src/vector/util/UtilImages/picker.png");
            default:
                assert (true);
                return null;
        }
    }

    /**
     * This method is used to set the size of the specified button.
     * @param button The button that is being resized
     */
    public void setSize(AbstractButton button) {
        /*
         switch-statement to determine which enum button representation is being resized. Can be used to change the
         size of specific buttons
         */
        switch (this) {
            case PEN:
            case PEN_COLOUR:
            case FILL:
            case FILL_COLOUR:
            case FILL_OFF:
            case PICKER:
                button.setPreferredSize(new Dimension(20,20));
                break;
            default:

        }
    }
}
