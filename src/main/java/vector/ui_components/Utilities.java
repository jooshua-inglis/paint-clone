package vector.ui_components;
import javax.swing.*;
import java.awt.*;

public enum Utilities {
    ZOOM_IN("zoomIn.png"),
    ZOOM_OUT("zoomOut.png"),
    GRID("grid.png"),
    UNDO("undo.png");

    private ImageIcon icon;
    private Dimension size;

    private ResourceLoader loader = new ResourceLoader();

    Utilities(String fileName, Dimension size) {
        this.icon = loader.getImageIcon(fileName);
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
