package vector.util;
import javax.swing.*;
import java.awt.*;

public enum Utilities {
    ZOOM_IN,
    ZOOM_OUT,
    UNDO;

    public Icon getImage(){
        switch (this){
            case ZOOM_IN:
                return new ImageIcon("src/vector/util/UtilImages/zoomIn.png");
            case ZOOM_OUT:
                return new ImageIcon("src/vector/util/UtilImages/zoomOut.png");
            case UNDO:
                return new ImageIcon("src/vector/util/UtilImages/undo.png");
            default:
                assert (true);
                return null;
        }
    }



}
