package vector;

import vector.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.LinkedHashMap;
import static java.awt.Color.*;

/**
 * GUI class controls the what is output to the window. It contains one canvas object that is read to
 * determine what is printed to the window.
 */
public class GUI  {

    JFrame frame;
    JPanel mainPanel;
    JPanel shapePallet;
    JPanel toolPallet;
    VectorCanvas canvas;
    boolean penPressed = false;
    boolean fillPressed = false;

    GUI() {
        JFrame.setDefaultLookAndFeelDecorated(false);
        frame = new JFrame("VectorTool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700+20, 900));
        frame.setLocation(970,50);
        frame.getContentPane().setLayout(new BorderLayout());

        showMenuBar();
        showToolPalette();
        showCanvas();

        frame.pack();
        frame.setVisible(true);
    }

    //Opens File Chooser - Open Dialog
    //Currently chooses a file then prints out the directory path
    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.toString());
        }
    }

    //Opens new blank window
    //Currently not very useful, if you close one window, all close
    private void newFile() {
        canvas = new VectorCanvas();
        GUI untitled = new GUI();
        System.out.println("new");

    }

    //Opens File Chooser - Save Dialog
    //Currently allows the user to insert a file name then prints out the directory path
    private void save() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.toString());
        }

    }

    //Opens File Chooser - Save Dialog
    //Currently allows the user to insert a file name then prints out the directory path
    private void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.toString());
        }

    }

    private void export() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Bitmap", "bmp");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        fileChooser.setSelectedFile(new File("export.bmp"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.toString());
            FileIO.toImage(canvas, file);
        }
    }

    private JMenuItem createMenuItem(String text, ActionListener e) {
        JMenuItem newMenuItem = new JMenuItem(text);
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.addActionListener(e);
        return newMenuItem;
    }
    private void showMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(createMenuItem("New", (event) -> newFile()));
        fileMenu.add(createMenuItem("Open", (event) -> open()));
        fileMenu.add(createMenuItem("Save", (event) -> save()));
        fileMenu.add(createMenuItem("Save As...", (event) -> saveAs()));
        fileMenu.add(createMenuItem("Export", (event) -> export()));

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }

    private void addListener(AbstractButton button, ActionListener e){
        button.addActionListener(e);
    }

    private void zoom(int amount) {
        canvas.zoom(amount);
        mainPanel.setPreferredSize(canvas.getSize());
        frame.pack();
    }


    private void toolPanelFunctions(Tool tool){
        canvas.selectTool(tool);
    }
    private LinkedHashMap<Tool, JToggleButton> toolPanel(){
        LinkedHashMap<Tool, JToggleButton> toolButtonMap = new LinkedHashMap<>();
        ButtonGroup group = new ButtonGroup();
        for (Tool tool : Tool.values()) {
            JToggleButton toolButton = new JToggleButton(tool.getImage());
            group.add(toolButton);

            toolButton.setFocusPainted(false);
            toolButton.setBorderPainted(true);
            toolButton.setRolloverEnabled(true);
            toolButton.setContentAreaFilled(true);
            toolButton.setRequestFocusEnabled(true);
            toolButton.addActionListener((event) -> toolPanelFunctions(tool));
            toolButtonMap.put(tool, toolButton);
        }
        return toolButtonMap;
    }


    private void utilityPanelFunctions(JButton button, Utilities utility) {
/*        switch (Utility.name()) {
            case "ZOOM_IN":
                addListener(button, (event) -> zoom(100));
                break;
            case "ZOOM_OUT":
                addListener(button, (event) -> zoom(-100));
                break;
            case "UNDO":
                addListener(button, (event) -> canvas.undo());
                break;
        }*/
        switch (utility) {
            case ZOOM_IN:
                addListener(button, (event) -> zoom(100));
                break;
            case ZOOM_OUT:
                addListener(button, (event) -> zoom(-100));
                break;
            case UNDO:
                addListener(button, (event) -> canvas.undo());
                break;
        }

    }
    private LinkedHashMap<Utilities, JButton> utilityPanel(){

        LinkedHashMap<Utilities, JButton> utilityButtonMap = new LinkedHashMap<>();
        for(Utilities utility : Utilities.values()){
            JButton utilityButton = new JButton(utility.getImage());
            utilityPanelFunctions(utilityButton, utility);
            utilityButtonMap.put(utility,utilityButton);
        }
        return utilityButtonMap;
    }


    private int ColortoInt(Color colour){
        int r = colour.getRed();
        int g = colour.getGreen();
        int b = colour.getBlue();
        return (r*65536)+(g*256)+b;
    }
    private void colourPanelTools(LinkedHashMap<ColourTools, AbstractButton> colourPanelButtons){
        ButtonGroup toggleGroup = new ButtonGroup();

        for(ColourTools colourTools : ColourTools.values()){
           if (colourTools.equals(ColourTools.PEN) || colourTools.equals(ColourTools.FILL) || colourTools.equals(ColourTools.FILL_OFF)) {
               JToggleButton colourToolToggleButton = new JToggleButton(colourTools.getImage());
               colourToolToggleButton.setName(colourTools.name());
               colourTools.setSize(colourToolToggleButton);
               toggleGroup.add(colourToolToggleButton);
               colourPanelButtons.put(colourTools,colourToolToggleButton);
           }
           else{
               JButton colourToolButton = new JButton(colourTools.getImage());
               colourToolButton.setName(colourTools.name());
               colourTools.setSize(colourToolButton);
               System.out.println("JButton");
               colourPanelButtons.put(colourTools,colourToolButton);
           }

       }
      // return colourPanelButtons; // return type ArrayList<JToggleButton>
    }
    private void colourPanelQuickSelect(LinkedHashMap<ColourQuickSelect, AbstractButton> colourPanelButtons){
        for(ColourQuickSelect quickSelect : ColourQuickSelect.values()){
            JButton button = new JButton();
            button.setName(quickSelect.name());
            quickSelect.setSize(button);
            button.setBackground(quickSelect.getValue(quickSelect.getEnum(button)));

           colourPanelButtons.put(quickSelect,button);
        }
       // return colourPanelButtons; // return type ArrayList<JToggleButton>
    }
    private void combineToolsandQuickSelect( LinkedHashMap<Object, AbstractButton> combinedMap, LinkedHashMap<ColourTools, AbstractButton> toolsMap, LinkedHashMap<ColourQuickSelect, AbstractButton> quickSelectMap){
/*        toolsMap.forEach((k, v) ->
                quickSelectMap.merge(k, v, (v1, v2) ->
                {throw new AssertionError("duplicate values for key: "+k);}));*/
        // PUT FUNCTION TO CHECK IF SAME KEYS
        combinedMap.putAll(toolsMap);
        combinedMap.putAll(quickSelectMap);

    }
    private void colourPanelPressed( LinkedHashMap<Object, AbstractButton> colourPanelButtons){
        for (AbstractButton button : colourPanelButtons.values()){
            // remove to make these buttons functional
            if(!button.getName().equals("PENCOLOUR") || button.getName().equals("FILLCOLOUR") || button.getName().equals(ColourTools.FILL_OFF.toString())) {
                addListener(button, (event) -> colourPanelFunctions(button, colourPanelButtons));
            }
        }
    }


    private Color selectColor(AbstractButton button){
        Color colour;
        if(button.getName().equals("PICKER") && penPressed || button.getName().equals("PICKER") && fillPressed){
            Color previousColor;
            if(penPressed){
                previousColor = canvas.getSelectedPenColor().asColor();
            }
            else{
                previousColor = canvas.getSelectedFillColor().asColor();
            }
            colour = JColorChooser.showDialog(null, "Choose a Color", Color.black);
            if(colour == null){
                colour = previousColor;
            }
        }
        else{
            colour = button.getBackground();
        }
        return colour;
    }
    private void colourPanelFunctions(AbstractButton button, LinkedHashMap<Object, AbstractButton>colourPanelButtons){
        Color colour;
        boolean fillOffPressed = false;
        if(button.getName().equals("PEN")){
            fillPressed = false;
            fillOffPressed = false;
            penPressed = true;
        }
        else if(button.getName().equals("FILL")){
            penPressed = false;
            fillOffPressed = false;
            fillPressed = true;
        }
        else if (button.getName().equals(ColourTools.FILL_OFF.toString())){
            fillPressed = false;
            penPressed = false;
            fillOffPressed = true;
        }

        if(fillOffPressed){

        }


        Color x  = colourPanelButtons.get(ColourTools.FILL_OFF).getBackground();
        colour = selectColor(button);

        int rgb = ColortoInt(colour);

System.out.println(colourPanelButtons.get(ColourTools.FILL_COLOUR).getBackground().toString());
//        else{
//            // error message to till user to click pen or fill first
//        }
        if(!button.getName().equals("PEN") && !button.getName().equals("FILL") && !fillPressed && penPressed){
            canvas.setSelectedPenColor(new VectorColor(rgb));
            colourPanelButtons.get(ColourTools.PEN_COLOUR).setBackground(colour);
        }
        else if( !button.getName().equals("FILL")&& !button.getName().equals("PEN") && !penPressed && fillPressed){
            canvas.setSelectedFillColor(new VectorColor(rgb));
            colourPanelButtons.get(ColourTools.FILL_COLOUR).setBackground(colour);
        }
        else if (fillOffPressed){
            canvas.setSelectedFillColor(new VectorColor(rgb, false));
           colourPanelButtons.get(ColourTools.FILL_COLOUR).setBackground(x);
        }
    }
    private LinkedHashMap<Object, AbstractButton> colourPanel(){
        LinkedHashMap<ColourTools, AbstractButton> colourToolsMap = new LinkedHashMap<>();
        LinkedHashMap<ColourQuickSelect, AbstractButton> quickSelectMap = new LinkedHashMap<>();
        LinkedHashMap<Object, AbstractButton> combinedMap = new LinkedHashMap<>();

        colourPanelTools(colourToolsMap);
        colourPanelQuickSelect(quickSelectMap);
        combineToolsandQuickSelect(combinedMap, colourToolsMap, quickSelectMap);
        colourPanelPressed(combinedMap);
        return combinedMap;
    }


    private void showToolPalette(){
        JPanel basePallet = new JPanel();
        basePallet.setBackground(lightGray);
        JPanel pallet = new JPanel();
        basePallet.add(pallet);
        BoxLayout boxLayout = new BoxLayout(pallet, BoxLayout.Y_AXIS);
        pallet.setLayout(boxLayout);


       // pallet.setMaximumSize(new Dimension(60, 800));
        //pallet.setMinimumSize(new Dimension(60,650));
        pallet.setPreferredSize(new Dimension(61, 800));

        shapePallet = new JPanel(new GridLayout(Tool.values().length,1));
        shapePallet.setMaximumSize(new Dimension(60,220));

        toolPallet = new JPanel(new GridLayout(Utilities.values().length,1));
        toolPallet.setMaximumSize(new Dimension(70,140));

        JPanel colourPallet = new JPanel();
        //colourPallet.setPreferredSize(new Dimension(70,500));
        colourPallet.setMaximumSize(new Dimension(70,400));

        pallet.setBackground(Color.lightGray);


        shapePallet.setBackground(Color.lightGray);
        shapePallet.setBorder(BorderFactory.createTitledBorder("Tools"));

        toolPallet.setBackground(Color.lightGray);
        toolPallet.setBorder(BorderFactory.createTitledBorder("Utilities"));

        colourPallet.setBackground(black);
        colourPallet.setBorder(BorderFactory.createTitledBorder("Colours"));


        for(JToggleButton button : toolPanel().values()){
           // button.setPreferredSize(new Dimension(30,30));
            shapePallet.add(button);
        }
        for(JButton button : utilityPanel().values()){
          //  button.setPreferredSize(new Dimension(30,30));
            toolPallet.add(button);
        }
        for (AbstractButton button : colourPanel().values()){
            colourPallet.add(button);
        }

        pallet.add(shapePallet);
        pallet.add(toolPallet);
        pallet.add(colourPallet);


        frame.getContentPane().add(basePallet, BorderLayout.LINE_START);



    }


    private void showCanvas() {
        canvas = new VectorCanvas();
        canvas.setBackground(WHITE);
        canvas.setBorder(new LineBorder(BLACK));
        canvas.setPreferredSize(new Dimension(500, 500));
        canvas.setSize(500, 500);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.add(canvas);

        JScrollPane scrPane = new JScrollPane(mainPanel);
        frame.getContentPane().add(scrPane);
    }
}
