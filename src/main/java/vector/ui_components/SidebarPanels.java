package vector.ui_components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

/**
 * This enum class is used to determine what panels are used in the sidebar panel. The enums will for the names of the
 * panels.
 */
public enum SidebarPanels {
    TOOLS,
    UTILITIES,
    COLOURS;

    // LinkedHashMap to contain buttons for the Tool panel
    private LinkedHashMap<Tool, JToggleButton> toolButtons = new LinkedHashMap<>();
    // LinkedHashMap to contain buttons for the Utility Panel
    private LinkedHashMap<Utilities, JButton> utilityButtons = new LinkedHashMap<>();
    // LinkedHashMap to contain buttons for the Colour Panel
    private LinkedHashMap<Object, AbstractButton> colourButtons = new LinkedHashMap<>();

    /**
     * This method is used to get the panel buttons from the {@link vector.GUI} class and store them into the
     * local LinkedHashMap variables.
     * @param toolButtons // LinkedHashMap to contain buttons for Tool panel
     * @param utilityButtons // LinkedHashMap to contain buttons for Utility panel
     * @param colourButtons // LinkedHashMap to contain buttons for Colour panel
     */
    public void getButtons(LinkedHashMap<Tool, JToggleButton> toolButtons, LinkedHashMap<Utilities, JButton> utilityButtons, LinkedHashMap<Object, AbstractButton> colourButtons) {
        this.toolButtons = toolButtons;
        this.utilityButtons = utilityButtons;
        this.colourButtons = colourButtons;
    }

    /**
     * This method is used to set the background for the given panel
     * @param panel the panel that's background is being set
     */
    public void setBackground(JPanel panel){
        panel.setBackground(Color.lightGray);
    }

    /**
     * This method is used to set the border of the given panel. The name of panel was changed to make all letter
     * capital except the first one
     * @param panel the panel that's border is being set
     */
    public void setBorder(JPanel panel) {
        String name = panel.getName();
        name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        panel.setBorder(BorderFactory.createTitledBorder(name));
    }

    /**
     * This method is used to set the maximum size of the given panel
     * @param panel the panel that's maximum size is being set
     */
    public void setMaximumSize(JPanel panel) {
        // switch-statement to determine which panel maximum size is being set.
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

    /**
     * This method is used to set the panel height in regards to the number of buttons contained in the panel.
     * @param numButtons number of buttons in panel
     * @param buttonPreferredHeight preferred height of button
     * @return int determining size of panel to fit all buttons
     */
    private int setPanelHeight(int numButtons, int buttonPreferredHeight) {
        // Parity of numButtons
        int parity = numButtons % 2;
        int buffer;
        /*
        if-statement to determine which formula to use to calculate size of panel.
        if buttonPreferredHeight == 20 -> panel will contain 2 rows of buttons, otherwise it will be one row.
        buffer is different because of different button sizes
         */
        if (buttonPreferredHeight == 20) {
            buffer = 70;
            // if-statement to determine panel size for even or odd number of buttons
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

    /**
     * This method is used to add buttons to the specified panel. Buttons come from the local LinkedHashMaps.
     * @param panel panel that's is adding the buttons
     */
    public void addButtons(JPanel panel) {
        // switch-statement to determine which buttons go on what panel
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
