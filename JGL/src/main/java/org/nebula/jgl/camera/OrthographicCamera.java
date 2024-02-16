package org.nebula.jgl.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public non-sealed class OrthographicCamera extends Camera {

    public OrthographicCamera(Vector3f position, float left, float right, float bottom, float top, float zNear, float zFar) {
        super(position, new Vector2i(), new Matrix4f().ortho(left, right, bottom, top, zNear, zFar),
                new Matrix4f().translate(position.x, position.y, position.z), zNear, zFar);
    }

    public OrthographicCamera(Vector3f position) {
        this(position, -1, 1, -1, 1, -1, 1);
    }

    public OrthographicCamera() {
        this(new Vector3f());
    }

    @Override
    public void updateView() {
        view.identity();  // Reset the view matrix to identity

        // Assuming position contains the translation values
        view.translate(position);

        // Calculate the orthographic projection matrix based on width and height
        float left = -size.x / 2.0f;
        float right = size.x / 2.0f;
        float bottom = -size.y / 2.0f;
        float top = size.y / 2.0f;

        // Set up an orthographic projection matrix
        view.ortho2D(left, right, bottom, top);
    }

    @Override
    public String toString() {
        return "OrthographicCamera{" +
                "position=" + position +
                ", size=" + size +
                ", projection=" + projection +
                ", view=" + view +
                ", zNear=" + zNear +
                ", zFar=" + zFar +
                '}';
    }
}