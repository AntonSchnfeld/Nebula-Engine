package org.nebula.jgl.data;

import org.joml.Vector2f;

public class Vertex {

    public static final int POSITION_LOC = 0;
    public static final int POSITION_SIZE = 2;
    public static final int POSITION_SIZE_BYTES = POSITION_SIZE * Float.BYTES;
    public static final int POSITION_STRIDE = 0;

    public static final int COLOR_LOC = 1;
    public static final int COLOR_SIZE = 4;
    public static final int COLOR_SIZE_BYTES = COLOR_SIZE * Float.BYTES;
    public static final int COLOR_STRIDE = POSITION_STRIDE + POSITION_SIZE;

    public static final int UV_LOC = 2;
    public static final int UV_SIZE = 2;
    public static final int UV_SIZE_BYTES = UV_SIZE * Float.BYTES;
    public static final int UV_STRIDE = COLOR_STRIDE + COLOR_SIZE_BYTES;

    public static final int TEXTURE_ID_LOC = 3;
    public static final int TEXTURE_ID_SIZE = 1;
    public static final int TEXTURE_ID_SIZE_BYTES = TEXTURE_ID_SIZE * Float.BYTES;
    public static final int TEXTURE_ID_STRIDE = UV_STRIDE + UV_SIZE_BYTES;

    public static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + UV_SIZE + TEXTURE_ID_SIZE;
    public static final int VERTEX_SIZE_BYTES = TEXTURE_ID_STRIDE + TEXTURE_ID_SIZE;

    private Vector2f position;
    private Color color;
    private Vector2f uv;
    private float textureId;

    public Vertex(Vector2f position, Color color, Vector2f uv, float textureId) {
        this.position = position;
        this.color = color;
        this.uv = uv;
        this.textureId = textureId;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2f getUv() {
        return uv;
    }

    public void setUv(Vector2f uv) {
        this.uv = uv;
    }

    public float getTextureId() {
        return textureId;
    }

    public void setTextureId(float textureId) {
        this.textureId = textureId;
    }

    public float[] toArray() {
        return new float[] {
                position.x, position.y,
                color.getRed(), color.getBlue(), color.getGreen(), color.getAlpha(),
                uv == null ? -1 : uv.x, uv == null ? -1 : uv.y,
                textureId
        };
    }
}
