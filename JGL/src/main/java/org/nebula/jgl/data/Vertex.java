package org.nebula.jgl.data;

import org.joml.Vector2f;

public class Vertex {
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
}
