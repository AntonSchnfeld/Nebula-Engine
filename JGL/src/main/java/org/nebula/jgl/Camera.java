package org.nebula.jgl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    private final Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera (Vector2f position, int width, int height)
    {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection(width, height);
    }

    public Camera (float x, float y, int width, int height)
    {
        this(new Vector2f(x, y), width, height);
    }

    public void adjustProjection (int width, int height)
    {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, width, 0.0f, height, 0.0f, 20.0f);
    }

    public Matrix4f getViewMatrix ()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp
        );

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix ()
    {
        return this.projectionMatrix;
    }

    public Vector2f getPosition ()
    {
        return position;
    }
}