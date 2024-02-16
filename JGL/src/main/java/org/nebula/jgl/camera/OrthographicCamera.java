package org.nebula.jgl.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.HashMap;

/**
 * <br>
 * <h2>Orthographic Camera</h2>
 * <br>
 * An implementation of the Camera class representing an orthographic camera in a 3D graphics environment.
 * Orthographic cameras provide a parallel projection, making them suitable for 2D and isometric views.
 * This class extends the base Camera class and includes methods specific to orthographic cameras.
 *
 * @author Anton Schoenfeld
 */
public non-sealed class OrthographicCamera extends Camera {

    /**
     * Constructs an orthographic camera with the specified parameters.
     *
     * @param position The initial position of the camera.
     * @param left     The left clipping plane coordinate.
     * @param right    The right clipping plane coordinate.
     * @param bottom   The bottom clipping plane coordinate.
     * @param top      The top clipping plane coordinate.
     * @param zNear    The near clipping plane distance.
     * @param zFar     The far clipping plane distance.
     */
    public OrthographicCamera(Vector3f position, float left, float right, float bottom, float top, float zNear, float zFar) {
        super(position, new Vector2i(), new Matrix4f().ortho(left, right, bottom, top, zNear, zFar),
                new Matrix4f().translate(position.x, position.y, position.z), zNear, zFar);
    }

    /**
     * Constructs an orthographic camera with the specified position and default clipping plane coordinates.
     *
     * @param position The initial position of the camera.
     */
    public OrthographicCamera(Vector3f position) {
        this(position, -1, 1, -1, 1, -1, 1);
    }

    /**
     * Constructs an orthographic camera with the default position and clipping plane coordinates.
     */
    public OrthographicCamera() {
        this(new Vector3f());
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
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