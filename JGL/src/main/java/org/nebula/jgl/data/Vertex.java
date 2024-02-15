package org.nebula.jgl.data;

import org.joml.Vector2f;
import org.nebula.base.util.Poolable;

public class Vertex implements Poolable {
    public static final int POSITION_LOC = 0;
    public static final int POSITION_SIZE = 3;
    public static final int POSITION_SIZE_BYTES = POSITION_SIZE * Float.BYTES;
    public static final int POSITION_POINTER = 0;
    public static final int COLOR_POINTER = POSITION_POINTER + POSITION_SIZE_BYTES;
    public static final int COLOR_LOC = 1;
    public static final int COLOR_SIZE = 4;
    public static final int COLOR_SIZE_BYTES = COLOR_SIZE * Float.BYTES;
    public static final int UV_POINTER = COLOR_POINTER + COLOR_SIZE_BYTES;
    public static final int UV_LOC = 2;
    public static final int UV_SIZE = 2;
    public static final int UV_SIZE_BYTES = UV_SIZE * Float.BYTES;
    public static final int TEXTURE_ID_POINTER = UV_POINTER + UV_SIZE_BYTES;
    public static final int TEXTURE_ID_LOC = 3;
    public static final int TEXTURE_ID_SIZE = 1;
    public static final int TEXTURE_ID_SIZE_BYTES = TEXTURE_ID_SIZE * Float.BYTES;
    public static final int VERTEX_SIZE_BYTES = TEXTURE_ID_POINTER + TEXTURE_ID_SIZE_BYTES;
    public static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + UV_SIZE + TEXTURE_ID_SIZE;
    private float x, y, z;
    private float red, green, blue, alpha;
    private float u, v;
    private float textureId;

    public Vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, float textureId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.u = u;
        this.v = v;
        this.textureId = textureId;
    }

    public Vertex(float x, float y, float z, Color color, float u, float v, float textureId) {
        this(x, y, z, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), u, v, textureId);
    }

    public Vertex(Vector2f position, Color color, Vector2f uv, float textureId) {
        this(position.x, position.y, 0, color.getRed(), color.getGreen(), color.getGreen(), color.getAlpha()
                , uv.x, uv.y, textureId);
    }

    public Vertex() {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, -1);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getU() {
        return u;
    }

    public void setU(float u) {
        this.u = u;
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }

    public float getTextureId() {
        return textureId;
    }

    public void setTextureId(float textureId) {
        this.textureId = textureId;
    }

    public float[] toArray() {
        return new float[]{
                x, y, z,
                red, green, blue, alpha,
                u, v,
                textureId
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (Float.compare(x, vertex.x) != 0) return false;
        if (Float.compare(y, vertex.y) != 0) return false;
        if (Float.compare(z, vertex.z) != 0) return false;
        if (Float.compare(red, vertex.red) != 0) return false;
        if (Float.compare(green, vertex.green) != 0) return false;
        if (Float.compare(blue, vertex.blue) != 0) return false;
        if (Float.compare(alpha, vertex.alpha) != 0) return false;
        if (Float.compare(u, vertex.u) != 0) return false;
        if (Float.compare(v, vertex.v) != 0) return false;
        return Float.compare(textureId, vertex.textureId) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != 0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != 0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != 0.0f ? Float.floatToIntBits(z) : 0);
        result = 31 * result + (red != 0.0f ? Float.floatToIntBits(red) : 0);
        result = 31 * result + (green != 0.0f ? Float.floatToIntBits(green) : 0);
        result = 31 * result + (blue != 0.0f ? Float.floatToIntBits(blue) : 0);
        result = 31 * result + (alpha != 0.0f ? Float.floatToIntBits(alpha) : 0);
        result = 31 * result + (u != 0.0f ? Float.floatToIntBits(u) : 0);
        result = 31 * result + (v != 0.0f ? Float.floatToIntBits(v) : 0);
        result = 31 * result + (textureId != 0.0f ? Float.floatToIntBits(textureId) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", alpha=" + alpha +
                ", u=" + u +
                ", v=" + v +
                ", textureId=" + textureId +
                '}';
    }

    @Override
    public void clean() {
        x = 0;
        y = 0;
        z = 0;
        red = 0;
        green = 0;
        blue = 0;
        alpha = 0;
        u = 0;
        v = 0;
        textureId = -1;
    }

    public Vertex set(float x, float y, float z, Color color, float u, float v, int textureId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getAlpha();
        this.u = u;
        this.v = v;
        this.textureId = textureId;

        return this;
    }
}
