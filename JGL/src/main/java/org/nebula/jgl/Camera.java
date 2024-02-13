package org.nebula.jgl;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    public Vector2f position;
    private final Matrix4f projection, view;
    private float left;
    private float right;
    private float bottom;
    private float top;
    private float zNear;
    private float zFar;

    public Camera(Vector2f position, float left, float right, float bottom, float top, float zNear, float zFar) {
        this.position = position;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.zNear = zNear;
        this.zFar = zFar;
        projection = new Matrix4f().ortho(left, right, bottom, top, zNear, zFar);
        view = new Matrix4f().translate(position.x, position.y, 0);
    }

    public Camera(Vector2f position) {
        this(position, -1, 1, -1, 1, -1, 1);
    }

    public Camera() {
        this(new Vector2f(0, 0));
    }

    public void updateView() {
        view.translate(position.x, position.y, 0);
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return view;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getzNear() {
        return zNear;
    }

    public void setzNear(float zNear) {
        this.zNear = zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public void setzFar(float zFar) {
        this.zFar = zFar;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "position=" + position +
                ", projection=" + projection +
                ", view=" + view +
                ", left=" + left +
                ", right=" + right +
                ", bottom=" + bottom +
                ", top=" + top +
                ", zNear=" + zNear +
                ", zFar=" + zFar +
                '}';
    }
}