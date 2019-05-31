package vector.uiComponents;
import javax.swing.*;
import java.awt.*;

public enum ColourTools {
    PEN,
    PEN_COLOUR,
    FILL,
    FILL_COLOUR,
    FILL_OFF,
    PICKER;

    public Icon getImage() {
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
    public void setSize(AbstractButton button) {
        switch (this) {
            case PEN:
            case PEN_COLOUR:
            case FILL:
            case FILL_COLOUR:
            case FILL_OFF:
            case PICKER:
                button.setPreferredSize(new Dimension(20,20));
                break;
        }
    }
}
