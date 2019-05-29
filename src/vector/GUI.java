package vector;

import vector.exception.VecFileException;
import vector.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;


import static java.awt.Color.*;
import static vector.util.ColourTools.*;

/**
 * GUI class controls the what is output to the window. It contains one canvas object that is read to
 * determine what is printed to the window.
 */
public class GUI  {

    private static JFrame frame;
    private JPanel canvasPanel;
    private boolean penPressed = false;
    private boolean fillPressed = false;
    private boolean fillOffPressed = false;
    static VectorCanvas canvas;
    private AbstractButton currentSelectedTool = new JToggleButton();

    GUI() {
        JFrame.setDefaultLookAndFeelDecorated(false);
        frame = new JFrame("VectorTool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700+20, 900));
        frame.setLocation(970,50);
        frame.getContentPane().setLayout(new BorderLayout());
        showMenuBar();
        showSidebar();
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
            try {
                List<String> a = Files.readAllLines(file.toPath());
                canvas.copyShapes(FileIO.parseString(a));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Could not load file (Unknown IO Error)",
                        "Open error!",
                        JOptionPane.ERROR_MESSAGE
                );
                System.err.println("could not open file");
            } catch (VecFileException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Not a valid vec file",
                        "Open error",
                        JOptionPane.ERROR_MESSAGE
                );
                System.err.println("Corrupted file " + e.getMessage()) ;
            }
            canvas.repaint();
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
//        JFileChooser fileChooser = new JFileChooser();
//        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            System.out.println(file.toString());
//        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Vector", "vec");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        fileChooser.setSelectedFile(new File("untitled.vec"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.toString());
            String content = FileIO.getString(canvas);
            try {
                FileWriter myWriter = new FileWriter(file);

                myWriter.write(content);
                myWriter.close();
                System.out.println("Successfully wrote to the file." + content);
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
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
        if (canvas.getSize().equals(new Dimension(100, 100)) && amount == -100){
            JOptionPane.showMessageDialog(frame, "Canvas is at minimum size. It cannot be zoomed out any further!", "Zooming out too far", JOptionPane.ERROR_MESSAGE);
        } else {
            canvas.zoom(amount);
            canvasPanel.setPreferredSize(canvas.getSize());
            frame.pack();
        }
    }

    public static void undo(){
        if (!canvas.undo()){
            JOptionPane.showMessageDialog(frame, "There are no more shapes to undo!", "Undo Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void toggleGrid(){
        canvas.gridToggle = !canvas.gridToggle;
        canvas.repaint();
    }

    private void addToolFunctionality(Tool tool, ButtonGroup toolGroup){
        Enumeration toolGroupElements = toolGroup.getElements();
        boolean showDialog = false;
        while (toolGroupElements.hasMoreElements()) {
            AbstractButton button = (AbstractButton)toolGroupElements.nextElement();
            if (button.isSelected() && canvas.isShapeCreating()) {
                button.setSelected(false);
                Enumeration toolGroupElementsCopy = toolGroup.getElements();
                while (toolGroupElementsCopy.hasMoreElements()) {
                    AbstractButton button2 = (AbstractButton) toolGroupElementsCopy.nextElement();
                    if (button2.getName().equals(currentSelectedTool.getName())){
                        button2.setSelected(true);
                    }
                }
                if (!showDialog) {
                    JOptionPane.showMessageDialog(frame, "You must finish the current selected shape!", "Shape not completed", JOptionPane.ERROR_MESSAGE);
                    showDialog = true;
                }
            } else if (button.isSelected() && !canvas.isShapeCreating()){
                canvas.selectTool(tool);
                currentSelectedTool = button;
            }
        }
    }
    private LinkedHashMap<Tool, JToggleButton> initializeTools(){
        LinkedHashMap<Tool, JToggleButton> toolButtonMap = new LinkedHashMap<>();
        ButtonGroup toolGroup = new ButtonGroup();
        for (Tool tool : Tool.values()) {
            JToggleButton toggleButton = new JToggleButton(tool.getImage());
            toggleButton.setName(tool.toString());
            toolGroup.add(toggleButton);
            tool.setSize(toggleButton);
            toggleButton.setFocusPainted(false);
            toggleButton.setBorderPainted(true);
            toggleButton.setRolloverEnabled(true);
            toggleButton.setContentAreaFilled(true);
            toggleButton.setRequestFocusEnabled(true);
            toggleButton.addActionListener((event) -> addToolFunctionality(tool,toolGroup));
            toolButtonMap.put(tool, toggleButton);
        }
        return toolButtonMap;
    }
    
    private void addUtilityFunctionality(JButton button, Utilities utility) {
        switch (utility) {
            case ZOOM_IN:
                addListener(button, (event) -> zoom(100));
                break;
            case ZOOM_OUT:
                addListener(button, (event) -> zoom(-100));
                break;
            case UNDO:
                addListener(button, (event) -> undo());
                break;
            case GRID:
                addListener(button, (event) -> toggleGrid());
            default:
        }
    }
    private LinkedHashMap<Utilities, JButton> initializeUtilities(){
        LinkedHashMap<Utilities, JButton> utilityMap = new LinkedHashMap<>();
        for(Utilities utility : Utilities.values()){
            JButton button = new JButton(utility.getImage());
            utility.setSize(button);
            button.setFocusPainted(false);
            button.setBorderPainted(true);
            button.setRolloverEnabled(true);
            button.setContentAreaFilled(true);
            button.setRequestFocusEnabled(true);
            addUtilityFunctionality(button, utility);
            utilityMap.put(utility,button);
        }
        return utilityMap;
    }
    
    private Color selectColor(AbstractButton button){
        Color selectedColour;
        if(button.getName().equals(PICKER.toString()) && penPressed || button.getName().equals(PICKER.toString()) && fillPressed){
            Color previousColor;
            if(penPressed){
                previousColor = canvas.getSelectedPenColor().asColor();
            }
            else{
                previousColor = canvas.getSelectedFillColor().asColor();
            }
            selectedColour = JColorChooser.showDialog(null, "Choose a Color", Color.black);
            if(selectedColour == null){
                selectedColour = previousColor;
            }
        }
        else{
            selectedColour = button.getBackground();
        }
        return selectedColour;
    }
    private int colorToRGB(Color colour){
        int r = colour.getRed();
        int g = colour.getGreen();
        int b = colour.getBlue();
        return (r*65536)+(g*256)+b;
    }
    private void addColourTools(LinkedHashMap<ColourTools, AbstractButton> colourToolsMap){
        ButtonGroup colourToolGroup = new ButtonGroup();
        for(ColourTools colourTool : ColourTools.values()){
           if (colourTool.equals(PEN) || colourTool.equals(FILL) || colourTool.equals(FILL_OFF)) {
               JToggleButton toggleButton = new JToggleButton(colourTool.getImage());
               toggleButton.setName(colourTool.name());
               colourTool.setSize(toggleButton);
               colourToolGroup.add(toggleButton);
               colourToolsMap.put(colourTool,toggleButton);
           }
           else{
               JButton button = new JButton(colourTool.getImage());
               button.setName(colourTool.name());
               colourTool.setSize(button);
               colourToolsMap.put(colourTool,button);
           }
       }
    }
    private void addColourQuickSelect(LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap){
        for(ColourQuickSelect colourQuickSelect : ColourQuickSelect.values()){
            JButton button = new JButton();
            button.setName(colourQuickSelect.name());
            colourQuickSelect.setSize(button);
            button.setBackground(colourQuickSelect.getValue(colourQuickSelect.getEnum(button)));
            colourQuickSelectMap.put(colourQuickSelect,button);
        }
       // return colourPanelButtons; // return type ArrayList<JToggleButton>
    }
    private void combineColourToolsAndQuickSelect(LinkedHashMap<Object, AbstractButton> colourMap, LinkedHashMap<ColourTools, AbstractButton> colourToolsMap, LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap){
        colourMap.putAll(colourToolsMap);
        colourMap.putAll(colourQuickSelectMap);
    }
    private void addColourActionListeners(LinkedHashMap<Object, AbstractButton> colourMap){
        for (AbstractButton button : colourMap.values()){
            // remove to make these buttons functional
            if(!button.getName().equals(PEN_COLOUR.toString()) || !button.getName().equals(FILL_COLOUR.toString())){
                addListener(button, (event) -> addColourFunctionality(button, colourMap));
            }
        }
    }
    private void addColourFunctionality(AbstractButton button, LinkedHashMap<Object, AbstractButton>colourMap){
        Color selectedColour;
        Color fillOffColour  = colourMap.get(FILL_OFF).getBackground();

        if(button.getName().equals(PEN.toString())){
            penPressed = true;
            fillPressed = false;
            fillOffPressed = false;
        } else if(button.getName().equals(FILL.toString())){
            fillPressed = true;
            fillOffPressed = false;
            penPressed = false;
        } else if (button.getName().equals(FILL_OFF.toString())){
            fillOffPressed = true;
            penPressed = false;
            fillPressed = false;
        } else if (!fillPressed && !fillOffPressed && !penPressed){
            JOptionPane.showMessageDialog(frame, "You must select Pen or Fill before choosing a colour!", "Colour not selected", JOptionPane.WARNING_MESSAGE);
        }

        selectedColour = selectColor(button);
        int rgb = colorToRGB(selectedColour);

        if(!button.getName().equals(FILL.toString()) && !button.getName().equals(PEN.toString()) && !fillPressed && penPressed){
            canvas.setSelectedPenColor(new VectorColor(rgb));
            colourMap.get(PEN_COLOUR).setBackground(selectedColour);
        }
        else if(!button.getName().equals(PEN.toString()) && !button.getName().equals(FILL.toString()) && !penPressed && fillPressed){
            canvas.setSelectedFillColor(new VectorColor(rgb));
            colourMap.get(FILL_COLOUR).setBackground(selectedColour);
        }
        else if (fillOffPressed){
            for (ColourQuickSelect quickSelect : ColourQuickSelect.values()){
                if (button.getName().equals(quickSelect.name()) || button.getName().equals(PICKER.toString())){
                    JOptionPane.showMessageDialog(frame, "You must select Pen or Fill before choosing a colour!", "Colour not selected", JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }

            canvas.setSelectedFillColor(new VectorColor(rgb, false));
            colourMap.get(FILL_COLOUR).setBackground(fillOffColour);
        }
    }
    private LinkedHashMap<Object, AbstractButton> initializeColours(){
        LinkedHashMap<ColourTools, AbstractButton> colourToolsMap = new LinkedHashMap<>();
        LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap = new LinkedHashMap<>();
        LinkedHashMap<Object, AbstractButton> colourMap = new LinkedHashMap<>();
        addColourTools(colourToolsMap);
        addColourQuickSelect(colourQuickSelectMap);
        combineColourToolsAndQuickSelect(colourMap, colourToolsMap, colourQuickSelectMap);
        addColourActionListeners(colourMap);
        return colourMap;
    }

    private int setPanelHeight(int numButtons, int buttonPreferredHeight) {
        int parity = numButtons % 2;
        int buffer;
        if (buttonPreferredHeight == 20){
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

    private void showSidebar(){
        JPanel sidebarPanel = new JPanel();
        JPanel sidebar = new JPanel();
        JPanel sidebarTools = new JPanel();
        JPanel sidebarUtilities = new JPanel();
        JPanel sidebarColours = new JPanel();

        BoxLayout sidebarBoxLayout = new BoxLayout(sidebar, BoxLayout.Y_AXIS);
        sidebar.setLayout(sidebarBoxLayout);

        sidebarPanel.setBackground(lightGray);
        sidebar.setBackground(lightGray);
        sidebarTools.setBackground(lightGray);
        sidebarUtilities.setBackground(lightGray);
        sidebarColours.setBackground(lightGray);

        sidebarTools.setBorder(BorderFactory.createTitledBorder("Tools"));
        sidebarUtilities.setBorder(BorderFactory.createTitledBorder("Utilities"));
        sidebarColours.setBorder(BorderFactory.createTitledBorder("Colours"));

        sidebar.setPreferredSize(new Dimension(65,800));
        sidebarTools.setMinimumSize(new Dimension(65,250));
        sidebarTools.setMaximumSize(new Dimension(65, setPanelHeight(initializeTools().size(), initializeTools().get(Tool.RECTANGLE).getPreferredSize().height)));
        sidebarUtilities.setMinimumSize(new Dimension(65, 170));
        sidebarUtilities.setMaximumSize(new Dimension(65, setPanelHeight(initializeUtilities().size(), initializeUtilities().get(Utilities.ZOOM_IN).getPreferredSize().height)));
        sidebarColours.setMinimumSize(new Dimension(65, 230));
        sidebarColours.setMaximumSize(new Dimension(65, setPanelHeight(initializeColours().size(), initializeColours().get(ColourQuickSelect.BLACK).getPreferredSize().height)));

        for(JToggleButton toggleButton : initializeTools().values()){
            sidebarTools.add(toggleButton);
        }
        for(JButton button : initializeUtilities().values()){
            sidebarUtilities.add(button);
        }
        for (AbstractButton button : initializeColours().values()){
            sidebarColours.add(button);
        }

        sidebar.add(sidebarTools);
        sidebar.add(sidebarUtilities);
        sidebar.add(sidebarColours);
        sidebarPanel.add(sidebar);

        frame.getContentPane().add(sidebarPanel, BorderLayout.LINE_START);
    }


    private void showCanvas() {
        canvas = new VectorCanvas();
        canvas.setBackground(WHITE);
        canvas.setBorder(new LineBorder(BLACK));
        canvas.setPreferredSize(new Dimension(500, 500));
        canvas.setSize(500, 500);

        canvasPanel = new JPanel();
        canvasPanel.setPreferredSize(new Dimension(500, 500));
        canvasPanel.add(canvas);

        JScrollPane scrPane = new JScrollPane(canvasPanel);
        frame.getContentPane().add(scrPane);
    }








}
