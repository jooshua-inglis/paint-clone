package vector.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    GRAY ( gray);

/*
    RED ,
    BLUE,
    GREEN ,
    WHITE ,
    BLACK ,
    YELLOW ,
    ORANGE ,
    PINK ,
    CYAN ,
    GRAY ;
*/



    private int counter = 0;

    private static LinkedHashMap<ColourQuickSelect, Color> quickSelectColours = new LinkedHashMap<>();
   // private  static ArrayList<ColourQuickSelect> enums;

    public Color color;
    ColourQuickSelect(Color color) {
        this.color = color;
        counter = 0;
    }


    static {
        for (ColourQuickSelect option : ColourQuickSelect.values()) {
            quickSelectColours.put(option, option.color);
          //  enums.add(option);
        }
    }

/*
    public static ColourQuickSelect valueOf(Color pageType) {
        return (ColourQuickSelect) quickSelectColours.get(pageType);
    }
*/

    public Color getValue(ColourQuickSelect enumColour) {
        return quickSelectColours.get(enumColour);
    }
    public ColourQuickSelect getEnum (JButton button){
        for(ColourQuickSelect Enum : ColourQuickSelect.values()){
            if(Enum.toString().equals(button.getName())){
                return Enum;
            }
        }
        return null;
    }



//    public void setColor(JButton colourButton){
//   /*     ColourQuickSelect x = ColourQuickSelect.BLACK;
//        Color xxx = quickSelectColours.get(BLACK);
//        Color xx = enums.get(counter).getValue();*/
//        colourButton.setBackground(enums.get(counter).getValue());
//        counter++;
//    }

    public void setSize(JButton colourButton){
        colourButton.setPreferredSize(new Dimension(20,20));
    }


}





