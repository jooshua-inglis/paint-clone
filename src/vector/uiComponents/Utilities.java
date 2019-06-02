package vector.uiComponents;
import javax.swing.*;
import java.awt.*;

public enum Utilities {
    ZOOM_IN("src/vector/util/UtilImages/zoomIn.png"),
    ZOOM_OUT("src/vector/util/UtilImages/zoomOut.png"),
    GRID("src/vector/util/UtilImages/grid.png"),
    UNDO("src/vector/util/UtilImages/undo.png");

    private ImageIcon icon;
    private Dimension size;

    Utilities(String iconFileName, Dimension size) {
        this.icon = new ImageIcon(iconFileName);
        this.size = size;
    }

    Utilities(String iconFileName) {
        this(iconFileName, new Dimension(50, 40));
    }

    public Icon getImage() {
        return icon;
    }

    public void setSize(AbstractButton button) {
        button.setPreferredSize(size);
    }
}
