package org.nebula.jgl.camera;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

/**
 * <br>
 * <h2>Camera</h2>
 * <br>
 * The base class for cameras in a 3D graphics environment. This abstract class provides
 * common properties and methods that are shared among different camera types.
 *
 * @author Anton Schoenfeld
 */
public abstract sealed class Camera permits OrthographicCamera {

    /**
     * The position of the camera in 3D space.
     */
    public final Vector3f position;

    /**
     * The dimensions of the viewport represented by the camera.
     */
    public final Vector2i size;

    /**
     * The projection matrix used for transforming 3D coordinates to 2D screen space.
     */
    protected final Matrix4f projection;

    /**
     * The view matrix representing the camera's orientation and position in the scene.
     */
    protected final Matrix4f view;

    /**
     * The near clipping plane of the camera's view frustum.
     */
    protected float zNear;

    /**
     * The far clipping plane of the camera's view frustum.
     */
    protected float zFar;

    /**
     * Constructs a camera with specified initial parameters.
     *
     * @param position   The initial position of the camera.
     * @param size       The dimensions of the viewport.
     * @param projection The initial projection matrix.
     * @param view       The initial view matrix.
     * @param zNear      The near clipping plane.
     * @param zFar       The far clipping plane.
     */
    public Camera(Vector3f position, Vector2i size, Matrix4f projection, Matrix4f view,
                  float zNear, float zFar) {
        this.position = new Vector3f().set(position);
        this.size = new Vector2i().set(size);
        this.projection = new Matrix4f().set(projection);
        this.view = new Matrix4f().set(view);
        this.zNear = zNear;
        this.zFar = zFar;
    }

    /**
     * Constructs a camera with default parameters.
     */
    public Camera() {
        this(new Vector3f(), new Vector2i(), new Matrix4f(), new Matrix4f(), 0, 0);
    }

    /**
     * Updates the camera's view matrix. Subclasses should implement this method to
     * reflect changes in camera orientation or position.
     */
    public abstract void updateView();

    /**
     * Retrieves the projection matrix of the camera.
     *
     * @return The projection matrix.
     */
    public Matrix4f getProjection() {
        return projection;
    }

    /**
     * Retrieves the view matrix of the camera.
     *
     * @return The view matrix.
     */
    public Matrix4f getView() {
        return view;
    }

    /**
     * Retrieves the near clipping plane of the camera's view frustum.
     *
     * @return The near clipping plane.
     */
    public float getzNear() {
        return zNear;
    }

    /**
     * Sets the near clipping plane of the camera's view frustum.
     *
     * @param zNear The new value for the near clipping plane.
     */
    public void setzNear(float zNear) {
        this.zNear = zNear;
    }

    /**
     * Retrieves the far clipping plane of the camera's view frustum.
     *
     * @return The far clipping plane.
     */
    public float getzFar() {
        return zFar;
    }

    /**
     * Sets the far clipping plane of the camera's view frustum.
     *
     * @param zFar The new value for the far clipping plane.
     */
    public void setzFar(float zFar) {
        this.zFar = zFar;
    }

    /**
     * Checks if this camera is equal to another object.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Camera camera = (Camera) o;

        if (Float.compare(zNear, camera.zNear) != 0) return false;
        if (Float.compare(zFar, camera.zFar) != 0) return false;
        if (!position.equals(camera.position)) return false;
        if (!size.equals(camera.size)) return false;
        if (!projection.equals(camera.projection)) return false;
        return view.equals(camera.view);
    }

    /**
     * Generates a hash code for this camera.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + size.hashCode();
        result = 31 * result + projection.hashCode();
        result = 31 * result + view.hashCode();
        result = 31 * result + (zNear != 0.0f ? Float.floatToIntBits(zNear) : 0);
        result = 31 * result + (zFar != 0.0f ? Float.floatToIntBits(zFar) : 0);
        return result;
    }
}
