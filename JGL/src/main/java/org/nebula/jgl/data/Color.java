package org.nebula.jgl.data;

/**
 * <br>
 * <h2>Color</h2>
 * <br>
 * The Color class represents a color in the RGBA color space.
 * <p>
 * It provides standard color constants and methods for creating and manipulating colors.
 * </p>
 *
 * @author Anton Schoenfeld
 *
 * @see Math#clamp(float, float, float)
 */
public class Color {

    /** Constant for the color white (fully opaque). */
    public static final Color WHITE = new Color(1, 1, 1, 1);

    /** Constant for the color black (fully opaque). */
    public static final Color BLACK = new Color(0, 0, 0, 1);

    /** Constant for the color red (fully opaque). */
    public static final Color RED = new Color(1, 0, 0, 1);

    /** Constant for the color green (fully opaque). */
    public static final Color GREEN = new Color(0, 1, 0, 1);

    /** Constant for the color blue (fully opaque). */
    public static final Color BLUE = new Color(0, 0, 1, 1);

    /** Constant for the color yellow (fully opaque). */
    public static final Color YELLOW = new Color(1, 1, 0, 1);
    public static final Color ORANGE = new Color(1, 0.5f, 0, 1);

    /** Constant for the color magenta (fully opaque). */
    public static final Color MAGENTA = new Color(1, 0, 1, 1);

    /** Constant for the color cyan (fully opaque). */
    public static final Color CYAN = new Color(0, 1, 1, 1);

    private float red, green, blue, alpha;

    /**
     * Creates a new Color with specified RGBA values.
     *
     * @param red   the red component, within the range [0, 1]
     * @param green the green component, within the range [0, 1]
     * @param blue  the blue component, within the range [0, 1]
     * @param alpha the alpha (opacity) component, within the range [0, 1]
     */
    public Color(float red, float green, float blue, float alpha) {
        this.red = Math.clamp(red, 0, 1);
        this.green = Math.clamp(green, 0, 1);
        this.blue = Math.clamp(blue, 0, 1);
        this.alpha = Math.clamp(alpha, 0, 1);
    }

    /**
     * Creates a new Color with specified RGB values and fully opaque alpha.
     *
     * @param red   the red component, within the range [0, 1]
     * @param green the green component, within the range [0, 1]
     * @param blue  the blue component, within the range [0, 1]
     */
    public Color(float red, float green, float blue) {
        this(red, green, blue, 1);
    }

    /**
     * Gets the red component of the color.
     *
     * @return the red component, within the range [0, 1]
     */
    public float getRed() {
        return red;
    }

    /**
     * Sets the red component of the color.
     *
     * @param red the red component, within the range [0, 1]
     */
    public void setRed(float red) {
        this.red = red;
    }

    /**
     * Gets the green component of the color.
     *
     * @return the green component, within the range [0, 1]
     */
    public float getGreen() {
        return green;
    }

    /**
     * Sets the green component of the color.
     *
     * @param green the green component, within the range [0, 1]
     */
    public void setGreen(float green) {
        this.green = green;
    }

    /**
     * Gets the blue component of the color.
     *
     * @return the blue component, within the range [0, 1]
     */
    public float getBlue() {
        return blue;
    }

    /**
     * Sets the blue component of the color.
     *
     * @param blue the blue component, within the range [0, 1]
     */
    public void setBlue(float blue) {
        this.blue = blue;
    }

    /**
     * Gets the alpha (opacity) component of the color.
     *
     * @return the alpha component, within the range [0, 1]
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Sets the alpha (opacity) component of the color.
     *
     * @param alpha the alpha component, within the range [0, 1]
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        if (Float.compare(red, color.red) != 0) return false;
        if (Float.compare(green, color.green) != 0) return false;
        if (Float.compare(blue, color.blue) != 0) return false;
        return Float.compare(alpha, color.alpha) == 0;
    }

    @Override
    public int hashCode() {
        int result = (red != 0.0f ? Float.floatToIntBits(red) : 0);
        result = 31 * result + (green != 0.0f ? Float.floatToIntBits(green) : 0);
        result = 31 * result + (blue != 0.0f ? Float.floatToIntBits(blue) : 0);
        result = 31 * result + (alpha != 0.0f ? Float.floatToIntBits(alpha) : 0);
        return result;
    }
}
