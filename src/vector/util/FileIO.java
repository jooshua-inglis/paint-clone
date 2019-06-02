package vector.util;

import vector.exception.ShapeException;
import vector.exception.VecFileException;
import vector.shape.*;
import vector.uiComponents.Tool;
import vector.uiComponents.VectorCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;

public class FileIO {

    /**
     * Converts canvas into VEC string
     * @param canvas input canvas
     * @return VEC string buffer
     */
    public static String getString(VectorCanvas canvas) {
        StringBuilder output = new StringBuilder();
        VectorColor penColor, fillColor;
        boolean includePen, includeFill;
        penColor = new VectorColor(0);
        fillColor = new VectorColor(0, false);
        for (VectorShape shape: canvas.getShapes() ) {
            includePen = !penColor.equals(shape.getPen());
            includeFill = !fillColor.equals(shape.getFill());
            penColor = shape.getPen();
            fillColor = shape.getFill();
            output.append(shape.getVec(includePen, includeFill));
            output.append('\n');
        }
        return output.toString();
    }

    /**
     * Interprets the list of strings that make up a shape and output a new shape.
     * @param parts command list, eg {"RECTANGLE, "0.2", "0.1", "0.7", "0.6"}
     * @return Shape corresponding to the list
     * @throws IllegalArgumentException if point is out of valid range
     */
    private static List<VectorPoint> parseShape(String[] parts) throws IllegalArgumentException {
        ArrayList<VectorPoint> output = new ArrayList<>();
        if (parts.length % 2 == 0) {
            throw new VecFileException("Invalid number of points");
        }
        for (int i = 1; i < parts.length; i+=2) {
            output.add(new VectorPoint(Double.parseDouble(parts[i]), Double.parseDouble(parts[i+1])));
        }
        return output;
    }

    /**
     * Interprets a shape command string and output a new shape.
     * @param input command list, eg RECTANGLE 0.2 0.1 0.7 0.6
     * @return Shape corresponding to the list
     * @throws IllegalArgumentException if point is out of valid range
     */
    public static VectorCanvas parseString(String input) throws VecFileException {
        return parseString(Arrays.asList(input.split("\n")));
    }

    /**
     * Interprets lines of a vec buffer as a list. Returns corresponding canvas.
     * @param input List of lines from a vec file
     * @return canvas
     * @throws VecFileException if there are unknown or invalid commands detected.
     */
    public static VectorCanvas parseString(List<String> input) throws VecFileException {
        VectorColor penColor = new VectorColor(0);
        VectorColor fillColor = new VectorColor(0xffffff);
        VectorCanvas output = new VectorCanvas();
        for (String line : input) {
            line = line.strip();
            String[] l = line.split("\\s+");
            String command = l[0];
            VectorShape newShape = null;

            try {
                switch (command) {
                    case "PEN":
                        penColor.setRgb(l[1]);
                        break;
                    case "FILL":
                        if (l[1].equals("OFF")) {
                            fillColor.setActive(false);
                        } else {
                            fillColor.setRgb(l[1]);
                        }
                        break;
                    default:
                        try { newShape = Tool.valueOf(command).getCls(); }
                        catch (ShapeException e){ throw new VecFileException(); }
                        break;
                }
            } catch (IllegalArgumentException e) {
                throw new VecFileException();
            }
            try {
                if (newShape != null) {
                    newShape.addPoints(parseShape(l));
                    newShape.setFill(fillColor);
                    newShape.setPen(penColor);
                    output.addShape(newShape);
                }
            } catch (IllegalArgumentException e) {
                throw new VecFileException();
            }
        }
        return output;
    }

    /**
     * Show a dialog for the user to pick side length of bmp image
     * @param additionMessage additional message for the user (eg invalid input)
     * @return user input
     * @throws CancellationException if the user presses cancel, the export operation is cancelled.
     */
    static private int showSizeDialog(String additionMessage) throws CancellationException {
        if (additionMessage == null) { additionMessage = "";}
        String message = "Please enter side width of bmp.\n(Output is always square)\n" + additionMessage;
        String a = JOptionPane.showInputDialog(
                null,
                message
        );

        if (a == null) {
            throw new CancellationException("Cancel creating bmp");
        }

        try {
            int size = Integer.parseInt(a);
            if (size <= 0) { return showSizeDialog("Invalid number"); }
            return size;
        }  catch (NumberFormatException e) {
            return showSizeDialog("Invalid number ");
        }
    }

    /**
     * Show a dialog for the user to pick side length of bmp image, with no additional message.
     * @return user input
     * @throws CancellationException if the user presses cancel, the export operation is cancelled.
     */
    static private int showSizeDialog() throws CancellationException {
        return showSizeDialog(null);
    }

    /**
     * Converts a canvas into a bmp file
     * @param canvas canvas to be converted
     * @param file file to save bmp to
     * @param size side width of image
     * @throws IOException If there is an error writting to file, export operation is canceled
     * @throws CancellationException if the user cancels, the export operation is canceled
     * @throws OutOfMemoryError if the user picks a size too large (typically 2Gb), user is informed of out of memory
     * error
     */
    static private void toImage(VectorCanvas canvas, File file, int size)
            throws IOException, CancellationException, OutOfMemoryError {
        VectorCanvas bmp = new VectorCanvas();
        bmp.copyShapes(canvas);
        bmp.setSize(size,size);
        BufferedImage i = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        bmp.paintComponent(i.getGraphics());
        FileOutputStream out = new FileOutputStream(file);
        ImageIO.write(i, "BMP", out);
        out.flush();
        out.close();
    }

    /**
     * Asks user for desired bmp size and converts canvas to bmp of that size.
     * @param canvas input VectorCanvas
     * @param file File to save to
     */
    static public void toImage(VectorCanvas canvas, File file) {
        int size = showSizeDialog();
        try {
            toImage(canvas, file, size);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to save bmp (IO error)",
                    "export error!",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (CancellationException e){
            System.out.println("User cancelled");
        } catch (OutOfMemoryError e) {
            JOptionPane.showMessageDialog(
                    null,
                    "System ran out of memory when saving to bmp, please chose smaller size!",
                    "export error!",
                    JOptionPane.ERROR_MESSAGE
            );
            toImage(canvas, file);
        }
    }
}