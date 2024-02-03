package org.nebula.jgl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera
{
    private Matrix4f projection, view;
    public Vector2f position;
    private float left, right, bottom, top, zNear, zFar;

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
        Vector3f eye = new Vector3f(position, zFar);
        Vector3f center = new Vector3f(position.x, position.y, -1f);
        Vector3f up = new Vector3f(0, 1, 0);
        view.setLookAt(eye, center, up);
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return view;
    }
}