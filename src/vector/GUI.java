package vector;

import vector.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static java.awt.Color.*;

/**
 * GUI class controls the what is output to the window. It contains one canvas object that is read to
 * determine what is printed to the window.
 */
public class GUI  {

    JFrame frame;
    JPanel mainPanel;
    VectorCanvas canvas;
    boolean penPressed = false;
    boolean fillPressed = false;

    GUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("VectorTool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            try {
                FileIO.toImage(canvas, file);
            }
            catch (IOException e) {
                System.err.println("failed to save");
            }
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


    private void addListener(JButton button, ActionListener e){
        button.addActionListener(e);
      //  return button;
    }


    private void zoom(int amount) {
        canvas.zoom(amount);
        mainPanel.setPreferredSize(canvas.getSize());
        frame.pack();
    }


    private void toolPanelFunctions(Tool tool){
        canvas.selectTool(tool);
    }
    private LinkedHashMap<Tool, JButton> toolPanel(){
        LinkedHashMap<Tool, JButton> toolButtonMap = new LinkedHashMap<>();

        for (Tool tool : Tool.values()) {
            JButton toolButton = new JButton(tool.getImage());

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

    private LinkedHashMap<Object, JButton> combineToolsandQuickSelect( LinkedHashMap<Object, JButton> combinedMap, LinkedHashMap<ColourTools, JButton> toolsMap, LinkedHashMap<ColourQuickSelect, JButton> quickSelectMap){
/*        toolsMap.forEach((k, v) ->
                quickSelectMap.merge(k, v, (v1, v2) ->
                {throw new AssertionError("duplicate values for key: "+k);}));*/

        combinedMap.putAll(toolsMap);
        combinedMap.putAll(quickSelectMap);
        return  combinedMap;
    }

    private int ColortoInt(Color colour){
        int r = colour.getRed();
        int g = colour.getGreen();
        int b = colour.getBlue();
        return (r*65536)+(g*256)+b;
    }
    private void colourPanelTools(LinkedHashMap<ColourTools, JButton> colourPanelButtons){
       for(ColourTools ColourTools : ColourTools.values()){

            JButton colourToolButton = new JButton(ColourTools.getImage());
            colourToolButton.setName(ColourTools.name());
            ColourTools.setSize(colourToolButton);

            colourPanelButtons.put(ColourTools,colourToolButton);
       }
      // return colourPanelButtons; // return type ArrayList<JButton>
    }
    private void colourPanelQuickSelect(LinkedHashMap<ColourQuickSelect, JButton> colourPanelButtons){
        for(ColourQuickSelect quickSelect : ColourQuickSelect.values()){
            JButton button = new JButton();
            button.setName(quickSelect.name());
            quickSelect.setSize(button);
            button.setBackground(quickSelect.getValue(quickSelect.getEnum(button)));

           colourPanelButtons.put(quickSelect,button);
        }
       // return colourPanelButtons; // return type ArrayList<JButton>
    }
    private void colourPanelPressed(  LinkedHashMap<Object, JButton> colourPanelButtons){
        for (JButton button : colourPanelButtons.values()){
            // remove to make these buttons functional
            if(!button.getName().equals("PENCOLOUR") || button.getName().equals("FILLCOLOUR")) {
                addListener(button, (event) -> colourPanelFunctions(button, colourPanelButtons));
            }
        }
    }
    private void colourPanelFunctions(JButton button, LinkedHashMap<Object, JButton>colourPanelButtons){
        Color colour;
        if(button.getName().equals("PICKER") && penPressed || button.getName().equals("PICKER") &&fillPressed){
            colour = JColorChooser.showDialog(null, "Choose a Color", Color.black);
        }
        else{
            colour = button.getBackground();
        }

        int rgb = ColortoInt(colour);


        if(button.getName().equals("PEN")){
            fillPressed = false;
            penPressed = true;
        }

        else if(button.getName().equals("FILL")){
            penPressed = false;
            fillPressed = true;
        }
        else{
            // error message to till user to click pen or fill first
        }
        if(!button.getName().equals("PEN") && !button.getName().equals("FILL") && !fillPressed && penPressed){
            canvas.setSelectedPenColor(new VectorColor(rgb));
            colourPanelButtons.get(ColourTools.PEN_COLOUR).setBackground(colour);
            System.out.println(canvas.getSelectedPenColor().toString());
        }
        else if( !button.getName().equals("FILL")&& !button.getName().equals("PEN") && !penPressed && fillPressed){
            canvas.setSelectedFillColor(new VectorColor(rgb));
            colourPanelButtons.get(ColourTools.FILL_COLOUR).setBackground(colour);
            System.out.println(canvas.getSelectedFillColor().toString());
        }
    }

    private LinkedHashMap<Object, JButton> colourPanel(){
        LinkedHashMap<ColourTools, JButton> colourToolsMap = new LinkedHashMap<>();
        LinkedHashMap<ColourQuickSelect, JButton> quickSelectMap = new LinkedHashMap<>();
        LinkedHashMap<Object, JButton> combinedMap = new LinkedHashMap<>();

        colourPanelTools(colourToolsMap);
        colourPanelQuickSelect(quickSelectMap);
        combineToolsandQuickSelect(combinedMap, colourToolsMap, quickSelectMap);
        colourPanelPressed(combinedMap);
        System.out.println(combinedMap.keySet());
        return combinedMap;
    }

/*    private ArrayList<JButton> colourPanel(){
        ArrayList<JButton> colourPanelButtons = new ArrayList<>();
        colourPanelTools(colourPanelButtons);
        colourPanelQuickSelect(colourPanelButtons);
        colourPanelPressed(colourPanelButtons);

        return colourPanelButtons;
    }*/

    private void showToolPalette(){
        JPanel basePallet = new JPanel();
        basePallet.setBackground(pink);
        JPanel pallet = new JPanel();
        basePallet.add(pallet);
        BoxLayout boxLayout = new BoxLayout(pallet, BoxLayout.Y_AXIS);
        pallet.setLayout(boxLayout);



       // pallet.setMaximumSize(new Dimension(60, 800));
        pallet.setMinimumSize(new Dimension(60,650));
        pallet.setPreferredSize(new Dimension(61, 650));

        JPanel shapePallet = new JPanel(new GridLayout(5,1));
        shapePallet.setMaximumSize(new Dimension(60,100));

        JPanel toolPallet = new JPanel(new GridLayout(3,1));
        toolPallet.setMaximumSize(new Dimension(70,120));

        JPanel colourPallet = new JPanel();

        pallet.setBackground(Color.lightGray);
      //  pallet.setLayout(new GridBagLayout());
        GridBagConstraints palletConstraints = new GridBagConstraints();



        //shapePallet.setPreferredSize(new Dimension(50,100));
        shapePallet.setBackground(Color.BLACK);
        shapePallet.setBorder(BorderFactory.createTitledBorder("Shapes"));
     //   shapePallet.setLayout(new GridLayout(5,1));
       // toolPallet.setPreferredSize(new Dimension(50,300));
        toolPallet.setBackground(Color.GREEN);
        toolPallet.setBorder(BorderFactory.createTitledBorder("Tools"));
      //  toolPallet.setLayout(new GridLayout(3,1));
       // colourPallet.setPreferredSize(new Dimension(50,300));
        colourPallet.setBackground(RED);
        colourPallet.setBorder(BorderFactory.createTitledBorder("Colours"));
        //colourPallet.setLayout(new GridLayout(3,1));

        for(JButton button : toolPanel().values()){
           // button.setPreferredSize(new Dimension(30,30));
            shapePallet.add(button);
        }
        for(JButton button : utilityPanel().values()){
          //  button.setPreferredSize(new Dimension(30,30));
            toolPallet.add(button);
        }
        for (JButton button : colourPanel().values()){
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

        mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.add(canvas);

        JScrollPane scrPane = new JScrollPane(mainPanel);
        frame.getContentPane().add(scrPane);
    }
}
