package vector.uiComponents;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import static java.awt.Color.*;

/**
 * This enum class is used to determine what quick select colour buttons will be used for the colour panel. The enum values
 * will form the names of the buttons and have Color constructor values.
 */
public enum ColourQuickSelect {
    RED(red),
    BLUE(blue),
    GREEN(green),
    WHITE(white),
    BLACK(black),
    YELLOW(yellow),
    ORANGE(orange),
    PINK(pink),
    CYAN(cyan),
    GRAY(gray);

    /**
     * LinkedHashMap to hold enum values and their constructor values
     */
    private static LinkedHashMap<ColourQuickSelect, Color> quickSelectColours = new LinkedHashMap<>();
    /**
     * Variable to hold Color
     */
    public Color color;
    public Dimension size;
    /**
     * Constructor to determine specified Color
     *
     * @param color Color that is specified
     */
    ColourQuickSelect(Color color, Dimension size) {
        this.color = color;
        this.size = size;
    }

    ColourQuickSelect(Color color){
        this(color, new Dimension(20, 20));
    }

    /*
     * Used to fill quickSelectColour with class enum values and their constructor values
     */
    static {
        for (ColourQuickSelect quickSelect : ColourQuickSelect.values()) {
            quickSelectColours.put(quickSelect, quickSelect.color);
        }
    }

    /**
     * Used to get Color value from specified enum value
     *
     * @param quickSelect enum value that contains Color value being returned
     * @return Color value for specified enum value
     */
    public Color getValue(ColourQuickSelect quickSelect) {
        return quickSelectColours.get(quickSelect);
    }

    /**
     * This method is used to select an enum value
     *
     * @param button contains the name of the enum value being selected
     * @return the enum value that matches the button name
     */
    public ColourQuickSelect getEnum(AbstractButton button) {
        // loop through all the ColourQuickSelect enum values
        try {
            return ColourQuickSelect.valueOf(button.getName());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * This method is used to set the preferred size of the specified button
     *
     * @param button components that's being resized
     */
    public void setSize(AbstractButton button) {
        button.setPreferredSize(size);
    }
}
