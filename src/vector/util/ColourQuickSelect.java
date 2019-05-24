package vector.util;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

import static java.awt.Color.*;

public enum ColourQuickSelect {
    RED (red),
    BLUE(blue),
    GREEN (green),
    WHITE (white),
    BLACK (black),
    YELLOW (yellow),
    ORANGE (orange),
    PINK (pink),
    CYAN (cyan),
    GRAY (gray);

    private static LinkedHashMap<ColourQuickSelect, Color> quickSelectColours = new LinkedHashMap<>();
    public Color color;

    ColourQuickSelect(Color color) {
        this.color = color;
    }

    static {
        for (ColourQuickSelect option : ColourQuickSelect.values()) {
            quickSelectColours.put(option, option.color);
        }
    }

    public Color getValue(ColourQuickSelect enumColour) {
        return quickSelectColours.get(enumColour);
    }
    public ColourQuickSelect getEnum (AbstractButton button){
        for(ColourQuickSelect Enum : ColourQuickSelect.values()){
            if(Enum.toString().equals(button.getName())){
                return Enum;
            }
        }
        return null;
    }
    public void setSize(AbstractButton button){
        switch (this){
            case RED:
            case BLUE:
            case GREEN:
            case WHITE:
            case BLACK:
            case YELLOW:
            case ORANGE:
            case PINK:
            case CYAN:
            case GRAY:
                button.setPreferredSize(new Dimension(20,20));
                break;
        }
    }
}





