package vector.util;
import javax.swing.*;
import java.awt.*;

public enum ColourTools {
    PEN,
    PEN_COLOUR,
    FILL,
    FILL_COLOUR,
    PICKER;

    public Icon getImage(){
        switch (this){
            case PEN:
                return new ImageIcon("src/vector/util/UtilImages/pen.png");
            case FILL:
                return new ImageIcon("src/vector/util/UtilImages/fill.png");
            case PICKER:
                return new ImageIcon("src/vector/util/UtilImages/picker.png");
            default:
                assert (true);
                return null;
        }
    }

    public void setSize(JButton button){
        switch (this){
            case PEN:
                button.setPreferredSize(new Dimension(20,20));
                break;
            case PEN_COLOUR:
                button.setPreferredSize(new Dimension(20,20));
                break;
            case FILL:
                button.setPreferredSize(new Dimension(20,20));
                break;
            case FILL_COLOUR:
                button.setPreferredSize(new Dimension(20,20));
                break;
            case PICKER:
                button.setPreferredSize(new Dimension(50,50));
                break;

        }
    }


}
