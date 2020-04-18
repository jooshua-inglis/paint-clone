package vector.util;

import java.awt.*;


public class VectorColor {
    private boolean active;
    private int rgb;

    public VectorColor(int rgb) {
        setRgb(rgb);
        active = true;
    }

    public VectorColor(int rgb, boolean active) {
        setRgb(rgb);
        this.active = active;
    }

    /**
     * if active is false, no color is to be displayed. 100% transparency.
     * @return active state
     */
    public boolean isActive() {
        return active;
    }

    /**
     * if active is false, no color is to be displayed. 100% transparency.
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRGB() {
        return rgb;
    }

    /**
     *
     * @param rgb input hex int
     * @throws IllegalArgumentException throws error if outside valid range (0 and 0xffffff)
     */
    public void setRgb(int rgb) throws IllegalArgumentException {
        if (rgb < 0 || rgb > 0xffffff) {
            throw new IllegalArgumentException("Color rgb outside of valid range");
        }
        this.rgb = rgb;
    }

    /**
     * interprets set color by interpreting hex string
     * @param rgb hex string
     * @throws IllegalArgumentException throws error if outside valid range (0 and 0xffffff) or invalid hex string
     */
    public void setRgb(String rgb) throws IllegalArgumentException {
        if (!rgb.startsWith("#")) {
            throw new IllegalArgumentException();
        }
        String[] num = rgb.split("#");
        if (num.length != 2) {
            throw new IllegalArgumentException();
        }

        setRgb(Integer.parseInt(num[1], 16));
    }

    /**
     * converts to awt Color object
     * @return awt Color object
     */
    public Color asColor() {
        return new Color(rgb);
    }

    public void update(VectorColor color) {
        setRgb(color.getRGB());
        setActive(color.isActive());
    }

    public String toString () {
        if (isActive()) { return  String.format( "#%06x", getRGB()).toUpperCase(); }
        else { return "OFF"; }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VectorColor) {
            VectorColor color = (VectorColor) obj;
            return (color.isActive() == isActive() && color.getRGB() == getRGB()) || (!color.isActive() && !isActive());
        } else { return false; }
    }
}
