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
    ColourQuickSelect(){

    }
    ColourQuickSelect(Color color) {
        this.color = color;
    }

    static {
        for (ColourQuickSelect quickSelect : ColourQuickSelect.values()) {
            quickSelectColours.put(quickSelect, quickSelect.color);
        }
    }


    public Color getValue(ColourQuickSelect enumColour) {
        return quickSelectColours.get(enumColour);
    }
    public ColourQuickSelect getEnum (AbstractButton button){
        for(ColourQuickSelect quickSelect : ColourQuickSelect.values()){
            if(quickSelect.toString().equals(button.getName())){
                return quickSelect;
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

    public Class<? extends ColourQuickSelect> getType(){
        return ColourQuickSelect.values()[0].getClass();
    }


}





