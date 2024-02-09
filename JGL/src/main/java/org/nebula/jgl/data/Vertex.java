package org.nebula.jgl.data;

import org.joml.Vector2f;

public class Vertex {
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
}
