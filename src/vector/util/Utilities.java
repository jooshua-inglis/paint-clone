package vector.util;
import javax.swing.*;
import java.awt.*;

public enum Utilities {
    ZOOM_IN,
    ZOOM_OUT,
    GRID,
    UNDO;

    public Icon getImage(){
        switch (this){
            case ZOOM_IN:
                return new ImageIcon("src/vector/util/UtilImages/zoomIn.png");
            case ZOOM_OUT:
                return new ImageIcon("src/vector/util/UtilImages/zoomOut.png");
            case GRID:
                return new ImageIcon("src/vector/util/UtilImages/grid.png");
            case UNDO:
                return new ImageIcon("src/vector/util/UtilImages/undo.png");
            default:
                assert (true);
                return null;
        }
    }

    public void setSize(AbstractButton button){
        switch (this){
            case ZOOM_IN:
            case ZOOM_OUT:
            case GRID:
                button.setPreferredSize(new Dimension(50,40));
                break;
            case UNDO:
                button.setPreferredSize(new Dimension(50,40));
                break;
        }
    }

}
