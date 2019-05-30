package vector.util;

import vector.GUI;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FrameResize implements ComponentListener {
    GUI gui;
    public FrameResize(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        SwingUtilities.invokeLater(() -> gui.updateCanvasSize()); // new thread created to improve performance
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }
}
