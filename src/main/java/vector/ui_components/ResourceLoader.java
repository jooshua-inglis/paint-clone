package vector.ui_components;

import javax.swing.ImageIcon;

public class ResourceLoader {
    public ImageIcon getImageIcon(String fileName) {
        return fileName != null ? new ImageIcon(getClass().getResource("/resources/" + fileName)) : null;
    }
}