package JonathanDraft;

import vector.uiComponents.ColourQuickSelect;
import vector.uiComponents.ColourTools;

import javax.swing.*;
import java.util.LinkedHashMap;

public class CombiningHashMap {
    public static void main(String[] args) {
        LinkedHashMap<ColourTools, JButton> colourToolsMap = new LinkedHashMap<>();
        LinkedHashMap<ColourQuickSelect, JButton> quickSelectMap = new LinkedHashMap<>();

        JButton x = new JButton();
        JButton y = new JButton();


        colourToolsMap.put(ColourTools.FILL, x);
        quickSelectMap.put(ColourQuickSelect.BLACK,y);

        LinkedHashMap<Object, JButton> combine = new LinkedHashMap<>(colourToolsMap);
        combine.putAll(quickSelectMap);

        /*colourToolsMap.forEach((k, v) ->
                quickSelectMap.merge(k, v, (v1, v2) ->
                {throw new AssertionError("duplicate values for key: "+k);}));
*/
System.out.println(combine.keySet().toString());
    }

}
