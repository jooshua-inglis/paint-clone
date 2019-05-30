package vector.util;

import vector.exception.VecFileException;
import vector.shape.Polygon;
import vector.shape.Rectangle;
import vector.shape.*;
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

         public static VectorCanvas parseString(String input) throws VecFileException {
                return parseString(Arrays.asList(input.split("\n")));
         }


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
                    case "RECTANGLE":
                        newShape = new Rectangle();
                        break;
                    case "ELLIPSE":
                        newShape = new Ellipse();
                        break;
                    case "LINE":
                        newShape = new Line();
                        break;
                    case "POLYGON":
                        newShape = new Polygon();
                        break;
                    case "PLOT":
                        newShape = new Plot();
                        break;
                    case "\n":
                        break;
                    default:
                        throw new VecFileException("Unknown command:" + command);
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

    private static JTextField constrain(String message, Component top, JPanel panel, SpringLayout layout) {
        JLabel label = new JLabel(message);
        JTextField text = new JTextField(5);
        layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.SOUTH, top);
        layout.putConstraint(SpringLayout.WEST, text, 5, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.NORTH, text, 5, SpringLayout.SOUTH, top);
        panel.add(label);
        panel.add(text);
        return text;
    }

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

    static private int showSizeDialog() throws CancellationException {
        return showSizeDialog(null);
    }

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