package org.nebula.jgl.camera;

import org.joml.*;

public abstract sealed class Camera permits OrthographicCamera {

    public final Vector3f position;
    public final Vector2i size;
    protected final Matrix4f projection, view;

    protected float zNear;
    protected float zFar;

    public Camera(Vector3f position, Vector2i size, Matrix4f projection, Matrix4f view,
                  float zNear, float zFar) {
        this.position = new Vector3f().set(position);
        this.size = new Vector2i().set(size);
        this.projection = new Matrix4f().set(projection);
        this.view = new Matrix4f().set(view);
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public Camera() {
        this(new Vector3f(), new Vector2i(), new Matrix4f(), new Matrix4f(), 0, 0);
    }

    public abstract void updateView();

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return view;
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
}
