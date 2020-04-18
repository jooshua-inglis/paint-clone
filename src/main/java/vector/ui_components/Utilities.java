package vector.ui_components;
import javax.swing.*;
import java.awt.*;

public enum Utilities {
    ZOOM_IN("resources/zoomIn.png"),
    ZOOM_OUT("resources/zoomOut.png"),
    GRID("resources/grid.png"),
    UNDO("resources/undo.png");

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
