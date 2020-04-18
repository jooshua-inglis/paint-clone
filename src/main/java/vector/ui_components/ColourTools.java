package vector.ui_components;
import javax.swing.*;
import java.awt.*;

/**
 * This enum class is used to determine what tool buttons will be used for the Colour panel on the sidebar. The enum
 * values will form the names of buttons
 */
public enum ColourTools {
    PEN("resources/pen.png"),
    PEN_COLOUR(null),
    FILL("resources/fill.png"),
    FILL_COLOUR(null),
    FILL_OFF("resources/FILL_OFF.png"),
    PICKER("resources/picker.png");

    private ImageIcon icon;
    private Dimension size;

    ColourTools(String iconFileName, Dimension size) {
        this.icon = new ImageIcon(iconFileName);
        this.size = size;
    }

    ColourTools(String iconFileName) {
        this(iconFileName, new Dimension(20, 20));
    }

    /**
     * This method is used to set the image icon for the button corresponding to a specific enum. The images are
     * located in the UtilImages package.
     * @return ImageIcon used to set button icon
     */
    public Icon getImage() {
        return icon;
    }

    /**
     * This method is used to set the size of the specified button.
     * @param button The button that is being resized
     */
    public void setSize(AbstractButton button) {
        button.setPreferredSize(size);
    }
}
