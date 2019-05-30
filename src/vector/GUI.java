package vector;

import vector.eventHandlers.FrameResize;
import vector.exception.VecFileException;
import vector.uiComponents.*;
import vector.util.FileIO;
import vector.uiComponents.Utilities;
import vector.util.VectorColor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import static java.awt.Color.*;
import static vector.uiComponents.ColourTools.*;

/**
 * GUI class controls the what is output to the window. It contains one JFrame that holds multiple JPanels to present
 * various information and functionality. The interface is differentiated into two sections, a sidebar containing
 * drawing tools and a canvas were the drawing occurs. There is also a menubar which provides file functionality.
 *
 * @author Joshua Inglis, Jonathan Salazar, Jordan Garland, Mikhayla Stephenson-Binstead
 * @version 59.0
 * @since 30/4/19
 */
public class GUI {
    /** Top-level window */
    private static JFrame frame;
    /** Panel to hold the canvas*/
    private JPanel canvasPanel;
    /** Scroll pane view for canvas */
    private JScrollPane scrPane;
    /** Check if PEN tool is pressed */
    private boolean penPressed = false;
    /** Check if FILL tool is pressed */
    private boolean fillPressed = false;
    /** Check if FILL_OFF tool is pressed */
    private boolean fillOffPressed = false;
    /** Canvas that will be drawn on  */
    static VectorCanvas canvas;
    /** Check if PEN tool is pressed */
    private AbstractButton currentSelectedTool = new JToggleButton();

    /**
     * This constructor is used to build and initialize the graphical interface. It contains various methods each
     * implementing a certain functionality for the interface.
     */
    public GUI() {
        showFrame();
        showMenuBar();
        showSidebar();
        showCanvas();
    }

    /**
     * Used to initialize the frame
     */
    private void showFrame(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("VectorTool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(720, 800));
        frame.setLocation(0,0);
        frame.getContentPane().setLayout(new BorderLayout());


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

    /**
     * This method is used to add an ActionListener to an AbstractButton
     * @param button This is the button that will hold the ActionListener
     * @param e This is the ActionListener
     */
    private void addListener(AbstractButton button, ActionListener e){
        button.addActionListener(e);
    }

    /**
     * This function is used to resize the canvas by changing the size of the
     * VectorCanvas object 'canvas'
     */
    public void updateCanvasSize() {
        double sideLength = canvas.getScale() * Math.min(frame.getHeight(), frame.getWidth());
        canvas.setPreferredSize(new Dimension((int) sideLength, (int) sideLength));
        canvas.setSize(new Dimension((int) sideLength, (int) sideLength));
        canvasPanel.setPreferredSize(canvas.getSize());
    }

    /**
     * This method is used to control the {@link VectorCanvas zoom} of the drawing canvas. It calls
     * updateCanvasSize() to resize the canvasPanel.
     * A dialog box is opened if the canvas is no longer visible on the frame.
     * @param amount This is the amount that the canvas will be zoomed in or out
     */
    private void zoom(double amount) {
        /*
        if-statement to determine when the canvas can be resized. A dialog box will be presented if the next zoom will
        make the canvas non-visible.
         */
        if (canvas.getSize().equals(new Dimension(100, 100)) && amount == -100){
            // Display dialog box
            JOptionPane.showMessageDialog(frame, "Canvas is at minimum size. It cannot be zoomed out any further!", "Zooming out too far", JOptionPane.ERROR_MESSAGE);
        } else {
            canvas.zoom(amount);
            updateCanvasSize();
            frame.pack();
        }
    }

    /**
     * This method is used to {@link VectorCanvas undo} the last shape drawn on the canvas.
     * A dialog box is presented if the method is called but there are no shapes to undo.
     */
    public static void undo() {
        /*
        Calling the undo() from VectorCanvas will return a boolean. It will return false when there are no more
        shapes to be undone, and a dialog box will inform the user.
        */
        if (!canvas.undo()) {
            JOptionPane.showMessageDialog(frame, "There are no more shapes to undo!", "Undo Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void toggleGrid() {
        canvas.gridToggle = !canvas.gridToggle;
        canvas.repaint();
    }

    /**
     * This method is used to select the type of {@link Tool tool} that will be used. A dialog box is presented if the
     * user attempts to select a shape without completing the currently selected shape. Nested if-statements and
     * while-statements are used to ensure the button of the current shape tool is selected.
     * @param tool This is the tool which will draw a certain shape
     * @param toolGroup This holds the group of buttons that will select a certain shape tool.
     */
    private void addToolFunctionality(Tool tool, ButtonGroup toolGroup) {
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

    /**
     * This method is used to initialize the toggle buttons used for the Tools panel on the Sidebar panel. Each button
     * is added to a LinkedHashMap, with a {@link Tool tool} as the Key and the toggle button as the Value.
     * @return returns the LinkedHashMap of buttons and Tool
     */
    private LinkedHashMap<Tool, JToggleButton> initializeTools() {
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

    /**
     * This function is used to add an {@link ActionListener} to a {@link Utilities Utilities} type button. The method
     * addListener(), adds the ActionListener and points to a method to be run in an event. A switch-statement
     * determines which event method is assigned to which button.
     * @param button a button that executes a method based on its Utilities functionality
     * @param utility an Enum of {@link Utilities Utilities}
     */
    private void addUtilityFunctionality(JButton button, Utilities utility) {
        switch (utility) {
            case ZOOM_IN:
                addListener(button, (event) -> zoom(0.2));
                break;
            case ZOOM_OUT:
                addListener(button, (event) -> zoom(-0.2));
                break;
            case UNDO:
                addListener(button, (event) -> undo());
                break;
            case GRID:
                addListener(button, (event) -> toggleGrid());
            default:
        }
    }

    /**
     * This method is used to initialize all the buttons used for the Utility panel on the Sidebar.
     * Each button is added to a LinkedHashMap, with a {@link Tool tool} as the Key and the toggle
     * button as the Value
     * @return the LinkedHashMap of buttons and Utilities
     */
    private LinkedHashMap<Utilities, JButton> initializeUtilities() {
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

    /**
     * This method is used to select a {@link Color colour} for the PEN or FILL tool.
     * Colours are determined via buttons on the Sidebar, which hold colours from
     * {@link ColourQuickSelect}. Colours can also be selected via a {@link JColorChooser colour chooser}
     * @param button used to determine if FILL or PEN button is selected
     * @return the colour selected
     */
    private Color selectColor(AbstractButton button) {
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

    /**
     * Used to convert {@link Color} to RGB
     * @param colour Color being converted
     * @return Color in RGB
     */
    private int colorToRGB(Color colour) {
        int r = colour.getRed();
        int g = colour.getGreen();
        int b = colour.getBlue();
        return (r*65536)+(g*256)+b;
    }

    /**
     * Used to add both buttons and toggle buttons to colourToolsMap.
     * Buttons are {@link ColourTools colour tools} located on the Colour panel.
     * @param colourToolsMap LinkedHashMap containing buttons for the Colour panel
     * on the Sidebar.
     */
    private void addColourTools(LinkedHashMap<ColourTools, AbstractButton> colourToolsMap) {
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

    /**
     * Used to add buttons to a LinkedHashMap containing quick select colour buttons
     * for the Colour panel on the Sidebar
     * @param colourQuickSelectMap LinkedHashMap containing quick select colour buttons
     */
    private void addColourQuickSelect(LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap) {
        for(ColourQuickSelect colourQuickSelect : ColourQuickSelect.values()){
            JButton button = new JButton();
            button.setName(colourQuickSelect.name());
            colourQuickSelect.setSize(button);
            button.setBackground(colourQuickSelect.getValue(colourQuickSelect.getEnum(button)));
            colourQuickSelectMap.put(colourQuickSelect,button);
        }
    }

    /**
     * Used to combine two LinkedHashMaps together.
     * @param colourMap result of combined LinkedHashMaps
     * @param colourToolsMap LinkedHashMap of Colour panel tools
     * @param colourQuickSelectMap LinkedHashMap of Colour panel quick select colours
     */
    private void combineColourToolsAndQuickSelect(LinkedHashMap<Object, AbstractButton> colourMap, LinkedHashMap<ColourTools, AbstractButton> colourToolsMap, LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap) {
        colourMap.putAll(colourToolsMap);
        colourMap.putAll(colourQuickSelectMap);
    }

    /**
     * Used to add {@link ActionListener} and event method to each button in colourMap.
     * @param colourMap LinkedHashMap containing buttons for the Colour panel in the Sidebar
     */
    private void addColourActionListeners(LinkedHashMap<Object, AbstractButton> colourMap) {
        for (AbstractButton button : colourMap.values()){
            // remove to make these buttons functional
            if(!button.getName().equals(PEN_COLOUR.toString()) || !button.getName().equals(FILL_COLOUR.toString())){
                addListener(button, (event) -> addColourFunctionality(button, colourMap));
            }
        }
    }

    /**
     * Used to execute the functionality of the buttons on the Color panel.
     * @param button the current button that was selected by the user
     * @param colourMap LinkedHashMap containing all the buttons on the Colour panel
     */
    private void addColourFunctionality(AbstractButton button, LinkedHashMap<Object, AbstractButton>colourMap) {
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

    /**
     * Used to initialise the buttons on the Colour panel.
     * @return LinkedHashMap containing all the buttons on the Colour panel
     */
    private LinkedHashMap<Object, AbstractButton> initializeColours() {
        LinkedHashMap<ColourTools, AbstractButton> colourToolsMap = new LinkedHashMap<>();
        LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap = new LinkedHashMap<>();
        LinkedHashMap<Object, AbstractButton> colourMap = new LinkedHashMap<>();
        addColourTools(colourToolsMap);
        addColourQuickSelect(colourQuickSelectMap);
        combineColourToolsAndQuickSelect(colourMap, colourToolsMap, colourQuickSelectMap);
        addColourActionListeners(colourMap);
        return colourMap;
    }

    /**
     * Used to initialize the Sidebar panel. This involves initializing
     * sub-panels outlined in {@link SidebarPanels} which provides the
     * drawing functionality of the applicaiton.
     */
    private void showSidebar() {
        JPanel sidebarPanel = new JPanel();
        JPanel sidebar = new JPanel();

        BoxLayout sidebarBoxLayout = new BoxLayout(sidebar, BoxLayout.Y_AXIS);
        sidebar.setLayout(sidebarBoxLayout);

        sidebarPanel.setBackground(lightGray);
        sidebar.setBackground(lightGray);

        sidebar.setPreferredSize(new Dimension(65,800));
        sidebar.setMinimumSize(new Dimension(65,700));

        for (SidebarPanels sidebarPanels : SidebarPanels.values()){
            JPanel panel = new JPanel();
            sidebarPanels.getButtons(initializeTools(), initializeUtilities(), initializeColours());
            panel.setName(sidebarPanels.name());
            sidebarPanels.setBackground(panel);
            sidebarPanels.setBorder(panel);
            sidebarPanels.setMaximumSize(panel);
            sidebarPanels.setButtons(panel);
            sidebar.add(panel);
        }

        sidebarPanel.add(sidebar);
        frame.getContentPane().add(sidebarPanel, BorderLayout.LINE_START);
    }

    /**
     * Used to initialize the {@link VectorCanvas canvas} where the drawing will
     * be done.
     */
    private void showCanvas() {
        canvas = new VectorCanvas();
        canvas.setBackground(WHITE);
        canvas.setBorder(new LineBorder(BLACK));
        canvas.setPreferredSize(new Dimension(500, 500));
        canvas.setSize(500, 500);

        canvasPanel = new JPanel();
        canvasPanel.setPreferredSize(new Dimension(500, 500));
        canvasPanel.add(canvas);

        scrPane = new JScrollPane(canvasPanel);
        frame.getContentPane().add(scrPane);
        frame.pack();
        frame.setVisible(true);

        FrameResize resize = new FrameResize(this);
        frame.addComponentListener(resize);
    }

    public JScrollPane getScrPane() {
        return scrPane;
    }

    public static VectorCanvas getCanvas() {
        return canvas;
    }

    public JPanel getCanvasPanel() {
        return canvasPanel;
    }
}
