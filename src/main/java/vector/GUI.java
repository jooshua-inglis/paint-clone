package vector;

import vector.event_handlers.FrameResize;
import vector.exception.UndoException;
import vector.exception.VecFileException;
import vector.ui_components.*;
import vector.util.FileIO;
import vector.ui_components.Utilities;
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
import static vector.ui_components.ColourTools.*;

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
    /** Tracks current filename */
    private File currentFile = null;

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
     * This method is used to open a pre-existing vec file.
     * The information stored in it will draw an image on the currenlty open canvas.
     */
    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("VEC", "vec"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file;
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


    private void save(File file) {
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

    /**
     * This method is used to save a pre-existing vec file.
     * It will store information about the canvas in a vec file.
     */
    private void save() {
        if (currentFile != null){
            File file = currentFile;
            save(file);
        } else {
            saveAs();
        }
    }

    /**
     * This method is used to save a non-existing vec file.
     * It will store information about the canvas in a vec file and allow the user to give the file a name.
     */
    private void saveAs() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("VEC", "vec");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        fileChooser.setSelectedFile(new File("untitled.vec"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file;
            save(file);
        }
    }
    /**
     * This method is used to export a vec file to a bmp file.
     * It will store information about the canvas in a bitmap image file.
     */
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
     * This method is used to select the type of {@link Tool tool} that will be used. A dialog box is presented if the
     * user attempts to select a shape without completing the currently selected shape. Nested if-statements and
     * while-statements are used to ensure the button of the current shape tool is selected.
     * @param tool This is the tool which will draw a certain shape
     * @param toolGroup This holds the group of buttons that will select a certain shape tool.
     */
    private void addToolFunctionality(Tool tool, ButtonGroup toolGroup) {
        // Variable to store elements of toolGroup
        Enumeration toolGroupElements = toolGroup.getElements();
        // check if dialog box is shown
        boolean showDialog = false;
        // loop through toolGroupElements
        while (toolGroupElements.hasMoreElements()) {
            // Initiate an AbstractButton set to the next element of toolGroupElements
            AbstractButton selectedButton = (AbstractButton)toolGroupElements.nextElement();
            /*
            if-else statement to determine whether a tool should be selected.
            If a tool is selected, but there is already a current tool being used -> toggle the selected tool OFF and
            then loop through all the buttons in toolGroup until the current tool found. Toggle that button ON and show
            a dialog box. Otherwise -> set the selected tool and store it as the current tool.
             */
            if (selectedButton.isSelected() && canvas.isShapeCreating()) {
                // Toggle button OFF
                selectedButton.setSelected(false);
                // Variable to store toolGroup elements
                Enumeration toolGroupElementsCopy = toolGroup.getElements();
                // Loop through toolGroupElementsCopy
                while (toolGroupElementsCopy.hasMoreElements()) {
                    // Abstract button set to the next element of toolGroupElementsCopy
                    AbstractButton currentTool = (AbstractButton) toolGroupElementsCopy.nextElement();
                    // Check if element is the current tool
                    if (currentTool.getName().equals(currentSelectedTool.getName())){
                        // toggle element ON
                        currentTool.setSelected(true);
                    }
                }
                // Show dialog box if it isn't currently shown
                if (!showDialog) {
                    if (currentSelectedTool.getName().equals(Tool.POLYGON.name())) {
                        // Dialog box for Polygon tool
                        JOptionPane.showMessageDialog(frame, "You must finish the current selected shape! Press Enter to finish shape.", "Shape not completed", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Dialog box for Tool other than Polygon
                        JOptionPane.showMessageDialog(frame, "You must finish the current selected shape! Click to finish shape.", "Shape not completed", JOptionPane.ERROR_MESSAGE);
                    }
                    // Dialog box is shown
                    showDialog = true;
                }
            } else if (selectedButton.isSelected() && !canvas.isShapeCreating()){
                // Set selected tool
                canvas.selectTool(tool);
                // Store selected tool
                currentSelectedTool = selectedButton;
            }
        }
    }

    /**
     * This method is used to initialize the toggle buttons used for the Tools panel on the Sidebar panel. Each button
     * is added to a LinkedHashMap, with a {@link Tool tool} as the Key and the toggle button as the Value.
     * @return returns the LinkedHashMap of buttons and Tool
     */
    private LinkedHashMap<Tool, JToggleButton> initializeTools() {
        // LinkedHashMap to store Tool panel buttons
        LinkedHashMap<Tool, JToggleButton> toolButtonMap = new LinkedHashMap<>();
        // Variable to group toggle buttons
        ButtonGroup toolGroup = new ButtonGroup();
        // Loop through each value of Tool enum class
        for (Tool tool : Tool.values()) {
            // Initialize a toggle button and set corresponding icon from Tool enum class
            JToggleButton toggleButton = new JToggleButton(tool.getImage());
            // Set toggle button name to Tool name
            toggleButton.setName(tool.toString());
            // Add toggle button to button group
            toolGroup.add(toggleButton);
            // Set dimension of toggle button
            tool.setSize(toggleButton);
            // Hide outline of image icon
            toggleButton.setFocusPainted(false);
            // Add ActionListener and event method to toggle button
            addListener(toggleButton, (event) -> addToolFunctionality(tool,toolGroup));
            // Add Tool value and toggle button into LinkedHashMap
            toolButtonMap.put(tool, toggleButton);
        }
        // Return LinkedHashMap
        return toolButtonMap;
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
     * @param amount This is the amount that the canvas will be zoomed in or out relative to screen size
     */
    private void zoom(double amount) {
        // Call VectorCanvas to zoom canvas
        canvas.zoom(amount);
        // Update the canvas size
        updateCanvasSize();
        // Pack contents of frame
        frame.pack();
        /*
        if-statement to check if the canvas is visible. If it isn't, a dialog box will be presented and the canvas will
        be zoomed back in
         */
        if (canvas.getSize().height <=0 && canvas.getSize().width <= 0){
            // Zoom back out to make canvas visible again
            zoom(0.2);
            // Display dialog box
            JOptionPane.showMessageDialog(frame, "Canvas is at minimum size. It cannot be zoomed out any further!", "Zooming out too far", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is used to display a grid on the canvas. Drawing operations will snap to this grid. See {@link VectorCanvas}
     * for specification
     */
    private void toggleGrid() {
        // Set the grid to its opposite state.
        canvas.gridToggle = !canvas.gridToggle;
        // Repaint grid
        canvas.repaint();
    }
    /**
     * This method is used to make the grid display larger. Drawing operations will snap to this grid. See {@link VectorCanvas}
     * for specification
     */
    public static void zoomInGrid(){
        //Check grid is above minimum number of lines/below maximum size
        if(canvas.nLines>5) {
            //adjust the number of lines the grid will display
            canvas.nLines--;
            // Repaint grid
            canvas.repaint();
        }
    }
    public static void zoomOutGrid(){
        //Check grid is below the maximum number of lines/above the minimum size
        if(canvas.nLines<50) {
            //adjust the number of lines the grid will display
            canvas.nLines++;
            // Repaint grid
            canvas.repaint();
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
        try {
            canvas.undo();
        } catch (UndoException e) {
            JOptionPane.showMessageDialog(
                    frame,
                   e.getMessage(),
                   "Undo Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This function is used to add an {@link ActionListener} to a {@link Utilities Utilities} type button. The method
     * addListener(), adds the ActionListener and points to a method to be run in an event. A switch-statement
     * determines which event method is assigned to which button.
     * @param button a button that executes a method based on its Utilities functionality
     * @param utility an Enum of {@link Utilities Utilities}
     */
    private void addUtilityFunctionality(JButton button, Utilities utility) {
        // switch-statement to add ActionListeners
        switch (utility) {
            case ZOOM_IN:
                // event method is zoom()
                addListener(button, (event) -> zoom(0.2));
                break;
            case ZOOM_OUT:
                // event method is zoom()
                addListener(button, (event) -> zoom(-0.2));
                break;
            case UNDO:
                // event method is undo()
                addListener(button, (event) -> undo());
                break;
            case GRID:
                // event method is toggleGrid()
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
        // LinkedHashMap to store buttons on Utility panel
        LinkedHashMap<Utilities, JButton> utilityMap = new LinkedHashMap<>();
        // Loop through all values of Utilities enum class
        for(Utilities utility : Utilities.values()){
            // Create new button and add icon from Utilities
            JButton button = new JButton(utility.getImage());
            // Set button size
            utility.setSize(button);
            // Hide icon image outline
            button.setFocusPainted(false);
            // Call method to add ActionListeners
            addUtilityFunctionality(button, utility);
            // Add Utility value and button to LinkedHashMap
            utilityMap.put(utility,button);
        }
        // return LinkedHashMap
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
        // Variable to store selected colour
        Color selectedColour;
        /*
        if-else statement to check if quick select or picker button was selected and return colour
        If PEN OR FILL colour tool is selected and so is the PICKER -> set current colour to previous colour and display
        colour picker, to select new colour. Otherwise, get the background colour of the button and set that as the new
        colour.
         */
        if (button.getName().equals(PICKER.toString()) && penPressed || button.getName().equals(PICKER.toString()) && fillPressed){
            // Variable to store previous colour
            Color previousColor;
            /* If PEN tool is pressed, set previousColour to current PEN colour. Otherwise set previousColour to
            current FILL colour.
            */
            if (penPressed){
                // Set previousColour to current PEN colour
                previousColor = canvas.getSelectedPenColor().asColor();
            } else {
                // Set previousColour to current FILL colour
                previousColor = canvas.getSelectedFillColor().asColor();
            }
            // Set selectedColour from colour picker
            selectedColour = JColorChooser.showDialog(null, "Choose a Color", Color.black);
            // If colour picker was cancelled then set colour to previous colour
            if (selectedColour == null){
                selectedColour = previousColor;
            }
        } else {
            // Set selectedColour to background colour of the button
            selectedColour = button.getBackground();
        }
        // Returned selectedColour
        return selectedColour;
    }

    /**
     * Used to convert {@link Color} to RGB
     * @param colour Color being converted
     * @return Color in RGB
     */
    private int colorToRGB(Color colour) {
        // Variable to store red component of colour
        int R = colour.getRed();
        // Variable to store green component of colour
        int G = colour.getGreen();
        // Variable to store blue component of colour
        int B = colour.getBlue();
        // Return RGB value of colour
        return (R*65536)+(G*256)+B;
    }

    /**
     * Used to add both buttons and toggle buttons to colourToolsMap.
     * Buttons are {@link ColourTools colour tools} located on the Colour panel.
     * @param colourToolsMap LinkedHashMap containing buttons for the Colour panel
     * on the Sidebar.
     */
    private void addColourTools(LinkedHashMap<ColourTools, AbstractButton> colourToolsMap) {
        // ButtonGroup to group buttons
        ButtonGroup colourToolGroup = new ButtonGroup();
        /*
        for-loop to loop through ColourTools values and initialize buttons based on those values. Button with PEN,FILL
        and FILL_OFF functionality will be toggle buttons and the rest will be regular buttons. These each type of
        button will be added to the colourToolsMap with there ColourTools value and button.
         */
        for(ColourTools colourTool : ColourTools.values()){
            // If colourTool is PEN, FILL or FILL_OFF create toggle button representations
           if (colourTool.equals(PEN) || colourTool.equals(FILL) || colourTool.equals(FILL_OFF)) {
               // Create new toggle button and add icon image form ColourTools
               JToggleButton toggleButton = new JToggleButton(colourTool.getImage());
               // Set name of button to current ColourTools enum
               toggleButton.setName(colourTool.name());
               // Set size of button
               colourTool.setSize(toggleButton);
               // Add toggle button to colourToolGroup button group
               colourToolGroup.add(toggleButton);
               // Added toggle button and ColourTools value to colourToolsMap
               colourToolsMap.put(colourTool,toggleButton);
           }
           else{
               // Create new button and add icon image from ColourTools
               JButton button = new JButton(colourTool.getImage());
               // Set name of button to current ColourTools enum
               button.setName(colourTool.name());
               // Set size of button
               colourTool.setSize(button);
               // Added button and ColourTools value to colourToolsMap
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
        // Loop through values of ColourQuickSelect and create buttons bases on these values
        for(ColourQuickSelect colourQuickSelect : ColourQuickSelect.values()){
            // Create a new button
            JButton button = new JButton();
            // Set name of button to ColourQuickSelect value
            button.setName(colourQuickSelect.name());
            // Set size of button
            colourQuickSelect.setSize(button);
            // Set background to Color value of ColourQuickSelect enum value
            button.setBackground(colourQuickSelect.getValue(colourQuickSelect.getEnum(button)));
            // Add button and ColourQuickSelect value to colourQuickSelectMap
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
        // Add contents of colourToolMap into colourMap
        colourMap.putAll(colourToolsMap);
        // Add contents of colourQuickSelectMap into colourMap
        colourMap.putAll(colourQuickSelectMap);
    }

    /**
     * Used to add {@link ActionListener} and event method to each button in colourMap.
     * @param colourMap LinkedHashMap containing buttons for the Colour panel in the Sidebar
     */
    private void addColourActionListeners(LinkedHashMap<Object, AbstractButton> colourMap) {
        // Loop through all values of colourMap
        for (AbstractButton button : colourMap.values()){
            // Add ActionListeners to all buttons except PEN_COLOUR and FILL_COLOUR
            if(!button.getName().equals(PEN_COLOUR.toString()) || !button.getName().equals(FILL_COLOUR.toString())){
                // Use method addListener() to add ActionListener and point to event method addColourFunctionality()
                addListener(button, (event) -> addColourFunctionality(button, colourMap));
            }
        }
        // Default colour for pen is black
        colourMap.get(PEN_COLOUR).setBackground(black);
    }

    /**
     * This method is used to determine which colour tool has been selected via a set of boolean variables. This will
     * determine which tool will used the selected colour
     * @param button button that is selected
     */
    private void isColourToolSelected(AbstractButton button){
        /*
        if-else statements to determine if PEN, FILL or FILL_OFF buttons are selected -> the corresponding boolean
        check is set to true and all other booleans are set to false. If none of the colour tools are pressed, display
         a dialog box
         */
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
            // Dialog box
            JOptionPane.showMessageDialog(frame, "You must select Pen or Fill before choosing a colour!", "Colour not selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is used set the selected colour with the selected tool. Methods from {@link VectorCanvas} will be
     * used to set the colour of the tool. The selected colour will be displayed on a button that's associated with
     * each tool. If the FILL_OFF tool is selected, a dialog box is displayed if the user tries to select a colour.
     * @param button button that is currently selected
     * @param colourMap LinkedHashMap that contains all the buttons on the Colour panel
     * @param selectedColour the current selected colour
     * @param fillOffColour the colour that a button will be set to when the FILL_OFF button is pressed
     * @param rgb RGB representation of the selectedColour
     */
    private void addColourToolFunctionality(AbstractButton button, LinkedHashMap<Object, AbstractButton>colourMap, Color selectedColour, Color fillOffColour, int rgb){
        // if-else statement to check which colour button is selected.
        if(!button.getName().equals(FILL.toString()) && !button.getName().equals(PEN.toString()) && !fillPressed && penPressed){
            // Call method from VectorCanvas to set the colour of the tool
            canvas.setSelectedPenColor(new VectorColor(rgb));
            // Change the colour of the chosen colour button with the selected colour
            colourMap.get(PEN_COLOUR).setBackground(selectedColour);
        }
        else if(!button.getName().equals(PEN.toString()) && !button.getName().equals(FILL.toString()) && !penPressed && fillPressed){
            // Call method from VectorCanvas to set the colour of the tool
            canvas.setSelectedFillColor(new VectorColor(rgb));
            // Change the colour of the chosen colour button with the selected colour
            colourMap.get(FILL_COLOUR).setBackground(selectedColour);

        }
        else if (fillOffPressed){
            // Loop through all values of ColourQuickSelect and display a dialog box is selected button is not a colour tool
            for (ColourQuickSelect quickSelect : ColourQuickSelect.values()){
                if (button.getName().equals(quickSelect.name()) || button.getName().equals(PICKER.toString())){
                    JOptionPane.showMessageDialog(frame, "You must select Pen or Fill before choosing a colour!", "Colour not selected", JOptionPane.WARNING_MESSAGE);
                    break;
                }
            }
            // Call method from VectorCanvas to clear the fill colour
            canvas.setSelectedFillColor(new VectorColor(rgb, false));
            // Change the colour of the chosen colour button with the selected colour
            colourMap.get(FILL_COLOUR).setBackground(fillOffColour);
        }
    }

    /**
     * Used to execute the functionality of the buttons on the Color panel.
     * @param button the current button that was selected by the user
     * @param colourMap LinkedHashMap containing all the buttons on the Colour panel
     */
    private void addColourFunctionality(AbstractButton button, LinkedHashMap<Object, AbstractButton>colourMap) {
        // Variable to store selected colour
        Color selectedColour;
        // Variable to store blank colour
        Color fillOffColour  = colourMap.get(FILL_OFF).getBackground();
        // Check which colour tool is selected
        isColourToolSelected(button);
        // Set selectedColour to Color returned from method selectColour()
        selectedColour = selectColor(button);
        // Set rbg to int returned from method colourToRGB()
        int rgb = colorToRGB(selectedColour);
        // Method to give buttons functionality
        addColourToolFunctionality(button,colourMap,selectedColour,fillOffColour,rgb);
    }

    /**
     * Used to initialise the buttons on the Colour panel. Buttons are stored in LinkedHashMaps
     * @return LinkedHashMap containing all the buttons on the Colour panel
     */
    private LinkedHashMap<Object, AbstractButton> initializeColours() {
        // LinkedHashMap for colour tool buttons
        LinkedHashMap<ColourTools, AbstractButton> colourToolsMap = new LinkedHashMap<>();
        // LinkedHashMap for quick select colour buttons
        LinkedHashMap<ColourQuickSelect, AbstractButton> colourQuickSelectMap = new LinkedHashMap<>();
        // LinkedHashMap to contain both colour tools and quick select buttons
        LinkedHashMap<Object, AbstractButton> colourMap = new LinkedHashMap<>();
        // Method to initialize colour tool buttons and store into colourToolsMap
        addColourTools(colourToolsMap);
        // Method to initialize quick select buttons and store into colourQuickSelectMap
        addColourQuickSelect(colourQuickSelectMap);
        // Method to store both colourToolsMap and colourQuickSelectMap into colourMap
        combineColourToolsAndQuickSelect(colourMap, colourToolsMap, colourQuickSelectMap);
        // Method to add ActionListeners to all buttons in colourMap
        addColourActionListeners(colourMap);
        // Return colourMap
        return colourMap;
    }

    /**
     * Used to initialize the frame
     */
    private void showFrame(){
        frame = new JFrame("VectorTool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700, 800));
        frame.setPreferredSize(new Dimension(720, 800));
        frame.setLocation(0,0);
        frame.getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Used to initialize the Sidebar panel. This involves initializing sub-panels outlined in {@link SidebarPanels}
     * and adding various buttons to add drawing the functionality of the application.
     */
    private void showSidebar() {
        // Panel to hold the side bar
        JPanel sidebarPanel = new JPanel();
        // Sidebar panel which holds the panels and buttons for drawing on canvas
        JPanel sidebar = new JPanel();
        // Initialize BoxLayout for sidebar
        BoxLayout sidebarBoxLayout = new BoxLayout(sidebar, BoxLayout.Y_AXIS);
        // Setting BoxLayout layout to sidebar
        sidebar.setLayout(sidebarBoxLayout);
        // Set background of sidebarPanel to lightGray
        sidebarPanel.setBackground(lightGray);
        // Set background of sidebar to lightGray
        sidebar.setBackground(lightGray);
        // Set sidebar preferred size
        sidebar.setPreferredSize(new Dimension(65,800));
        // Loop through values of SidebarPanels and create panels based on these values
        for (SidebarPanels sidebarPanels : SidebarPanels.values()){
            // Initialize new panel
            JPanel panel = new JPanel();
            // Send all buttons to SidebarPanels enum class
            sidebarPanels.getButtons(initializeTools(), initializeUtilities(), initializeColours());
            // Set the name of the panel to the current SidebarPanels value
            panel.setName(sidebarPanels.name());
            // Set background colour
            sidebarPanels.setBackground(panel);
            // Set border
            sidebarPanels.setBorder(panel);
            // Set maximum size
            sidebarPanels.setMaximumSize(panel);
            // Add buttons to panel
            sidebarPanels.addButtons(panel);
            // Add panel to sidebar
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
        // Initialize a new VectorCanvas
        canvas = new VectorCanvas();
        // Set background to white
        canvas.setBackground(WHITE);
        // Set border colour to black
        canvas.setBorder(new LineBorder(BLACK));
        // Set preferred size
        canvas.setPreferredSize(new Dimension(500, 500));
        // Set size
        canvas.setSize(500, 500);
        // Create panel to hold canvas
        canvasPanel = new JPanel();
        // Set preferred size
        canvasPanel.setPreferredSize(new Dimension(500, 500));
        // Add canvas to panel
        canvasPanel.add(canvas);
        // Create new scrPane instance
        scrPane = new JScrollPane(canvasPanel);
        // Add scrPane to frame
        frame.getContentPane().add(scrPane);
        // Pack contents
        frame.pack();
        // Show frame
        frame.setVisible(true);
        // Create new instance of FrameResize
        FrameResize resize = new FrameResize(this);
        // Add component resize as frame component listener
        frame.addComponentListener(resize);
    }

    /**
     * Get the JScrollPane that contains the VectorCanvas
     * @return scrollPane
     */
    public JScrollPane getScrPane() {
        return scrPane;
    }

    /**
     * Gets the currently active canvas.
     * @return
     */
    public static VectorCanvas getCanvas() {
        return canvas;
    }

    /**
     * Gets the panel the contains the main canvas
     * @return canvasPanel
     */
    public JPanel getCanvasPanel() {
        return canvasPanel;
    }
}
