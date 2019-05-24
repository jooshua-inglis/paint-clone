package vector.util;

import vector.VectorCanvas;
import vector.exception.UnknownCommandException;
import vector.shape.Rectangle;
import vector.shape.VectorShape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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

    private static List<VectorPoint> parseShape(String[] parts) {
        ArrayList<VectorPoint> output = new ArrayList<>();
        for (int i = 1; i < parts.length; i+=2) {
            try {
                output.add(new VectorPoint(Double.parseDouble(parts[i]), Double.parseDouble(parts[i+1])));
            } catch (IllegalArgumentException e) { System.out.println("Error"); }
        }
        return output;
    }

    public static VectorCanvas parseString(List<String> input) throws UnknownCommandException {
        VectorColor penColor = new VectorColor(0);
        VectorColor fillColor = new VectorColor(0, false);
        VectorCanvas output = new VectorCanvas();
        VectorShape shape;
        for (String line : input) {
            String[] l = line.strip().split(" ");
            String command = l[0];
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
                    output.addShape(new Rectangle(parseShape(l)));
                    break;
//                case "Ellipse":
//                    output.addShape(new Ellipse(parseShape(l)));
//                    break;
//                case "Line":
//                    output.addShape(new Line(parseShape(l)));
//                    break;
                case "\n":
                    break;
                default:
                    throw new UnknownCommandException("Unknown command:" + command);

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
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (CancellationException e){
            System.out.println("User cancelled");
        } catch (OutOfMemoryError e) {
            JOptionPane.showMessageDialog(
                    null,
                    "System ran out of memory when saving to bmp, please chose smaller size!",
                    "export error!",
                    JOptionPane.WARNING_MESSAGE
            );
            toImage(canvas, file);
        }
    }
}