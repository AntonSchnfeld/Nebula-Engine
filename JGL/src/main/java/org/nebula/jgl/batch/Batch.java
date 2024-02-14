package org.nebula.jgl.batch;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;

import java.util.MissingFormatWidthException;

import static org.lwjgl.opengl.GL33C.*;

/**
 * <br>
 * <h2>Batch</h2>
 * <br>
 * The Batch interface provides methods for rendering 2D graphics using a batch rendering approach.
 * <p>
 * Batch rendering allows efficient rendering of multiple sprites in a single draw call, improving
 * performance in graphics applications.
 * </p>
 * <p>
 * Implementation of this interface should support the rendering of textures, quads, triangles, and lines,
 * as well as color and blending settings.
 * </p>
 *
 * @author Anton Schoenfeld
 * @see Texture
 * @see Color
 * @see org.joml.Matrix4f
 * @see org.joml.Vector2f
 * @see org.joml.Vector4f
 */
public abstract class Batch implements IDisposable {
    protected Color color;
    protected Shader shader;
    protected boolean blendingEnabled, wireFrameEnabled;
    protected Matrix4f projectionMatrix;
    protected float lineWidth;
    protected Matrix4f viewMatrix;
    protected boolean rendering;

    public Batch() {
        this.lineWidth = 1;
        this.color = new Color(1, 1, 1, 1);
        this.blendingEnabled = true;
        this.wireFrameEnabled = false;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.rendering = false;
    }

    /**
     * Prepares the Batch for rendering. This method is called before drawing any sprites.
     * It sets up the necessary states and buffers to start the rendering process.
     * Subsequent calls to draw sprites should be made between {@code begin()} and {@code end()}.
     */
    public void begin() {
        if (rendering)
            throw new IllegalStateException("Can not call RenderBatch.begin when RenderBatch is already rendering");

        if (blendingEnabled) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        if (wireFrameEnabled) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        rendering = true;
    }

    /**
     * Finishes the rendering process and performs any necessary cleanup.
     * This method is called after all sprites have been drawn between {@code begin()} and {@code end()}.
     * It ensures that any remaining buffered data is flushed and the rendering state is restored.
     */
    public void end() {
        if (!rendering)
            throw new IllegalStateException("Can not call RenderBatch.end when RenderBatch is not rendering");

        rendering = false;
        flush();
    }

    /**
     * Flushes any remaining batched rendering commands to the graphics pipeline.
     * This method is typically called automatically by {@code end()}, but can be used manually
     * to force an immediate rendering of any pending batched data.
     * It is essential to call {@code flush()} to ensure that all sprites are rendered correctly.
     */
    public abstract void flush();

    public boolean isWireFrameEnabled() {
        return wireFrameEnabled;
    }

    public void setWireFrameEnabled(boolean wireFrameEnabled) {
        this.wireFrameEnabled = wireFrameEnabled;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float width) {
        this.lineWidth = width;
    }

    public void setColor(float r, float g, float b, float a) {
        color.setRed(r);
        color.setGreen(g);
        color.setBlue(b);
        color.setAlpha(a);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Retrieves the current color used for rendering.
     *
     * @return the current color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color for subsequent rendering operations.
     *
     * @param color the color to be set
     */
    public void setColor(Color color) {
        this.color.set(color);
    }

    /**
     * Checks if blending is currently enabled for rendering operations.
     *
     * @return true if blending is enabled, false otherwise
     */
    public boolean isBlendingEnabled() {
        return blendingEnabled;
    }

    /**
     * Enables or disables blending for subsequent rendering operations.
     *
     * @param enabled true to enable blending, false to disable
     */
    public void setBlendingEnabled(boolean enabled) {
        this.blendingEnabled = enabled;
    }

    /**
     * Retrieves the current projection matrix used for rendering.
     *
     * @return the current projection matrix
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Sets the projection matrix for subsequent rendering operations.
     *
     * @param projectionMatrix the projection matrix to be set
     */
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}
