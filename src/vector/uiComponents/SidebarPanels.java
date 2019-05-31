package vector.uiComponents;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public enum SidebarPanels {
    TOOLS,
    UTILITIES,
    COLOURS;

    private LinkedHashMap<Tool, JToggleButton> toolButtons = new LinkedHashMap<>();
    private LinkedHashMap<Utilities, JButton> utilityButtons = new LinkedHashMap<>();
    private LinkedHashMap<Object, AbstractButton> colourButtons = new LinkedHashMap<>();

    public void getButtons(LinkedHashMap<Tool, JToggleButton> toolButtons, LinkedHashMap<Utilities, JButton> utilityButtons, LinkedHashMap<Object, AbstractButton> colourButtons) {
        this.toolButtons = toolButtons;
        this.utilityButtons = utilityButtons;
        this.colourButtons = colourButtons;
    }

    public void setBackground(JPanel panel){
        panel.setBackground(Color.lightGray);
    }

    public void setBorder(JPanel panel) {
        String name = panel.getName();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        panel.setBorder(BorderFactory.createTitledBorder(name));
    }

    public void setMaximumSize(JPanel panel) {
        switch (this) {
            case TOOLS:
                panel.setMaximumSize(new Dimension(65, setPanelHeight(toolButtons.size(), toolButtons.get(Tool.RECTANGLE).getPreferredSize().height)));
                break;
            case UTILITIES:
                panel.setMaximumSize(new Dimension(65, setPanelHeight(utilityButtons.size(), utilityButtons.get(Utilities.ZOOM_IN).getPreferredSize().height)));
                break;
            case COLOURS:
                panel.setMaximumSize(new Dimension(65, setPanelHeight(colourButtons.size(), colourButtons.get(ColourQuickSelect.BLACK).getPreferredSize().height)));
                break;
            default:
        }
    }

    private int setPanelHeight(int numButtons, int buttonPreferredHeight) {
        int parity = numButtons % 2;
        int buffer;
        if (buttonPreferredHeight == 20) {
            buffer = 70;
            if (parity == 0) {
                return (numButtons / 2) * buttonPreferredHeight + buffer;
            } else {
                return (((numButtons - 1) / 2) * buttonPreferredHeight) + buttonPreferredHeight + buffer;
            }
        } else {
            buffer = 55;
            return numButtons * buttonPreferredHeight + buffer;
        }
    }

    public void addButtons(JPanel panel) {
        switch (this) {
            case TOOLS:
                for(JToggleButton toggleButton : toolButtons.values()){
                    panel.add(toggleButton);
                }
                break;
            case UTILITIES:
                for(JButton button : utilityButtons.values()){
                    panel.add(button);
                }
                break;
            case COLOURS:
                for (AbstractButton button : colourButtons.values()){
                    panel.add(button);
                }
                break;
            default:
        }
    }
}
