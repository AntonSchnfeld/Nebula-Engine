package org.nebula.jgl.batch;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;

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
    protected boolean blendingEnabled;
    protected Matrix4f projectionMatrix;
    protected float lineWidth;

    protected Matrix4f viewMatrix;

    public Batch() {
        this.lineWidth = 10;
        this.color = new Color(1, 1, 1, 1);
        this.blendingEnabled = true;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
    }

    /**
     * Prepares the Batch for rendering. This method is called before drawing any sprites.
     * It sets up the necessary states and buffers to start the rendering process.
     * Subsequent calls to draw sprites should be made between {@code begin()} and {@code end()}.
     */
    public abstract void begin();

    /**
     * Finishes the rendering process and performs any necessary cleanup.
     * This method is called after all sprites have been drawn between {@code begin()} and {@code end()}.
     * It ensures that any remaining buffered data is flushed and the rendering state is restored.
     */
    public abstract void end();

    /**
     * Flushes any remaining batched rendering commands to the graphics pipeline.
     * This method is typically called automatically by {@code end()}, but can be used manually
     * to force an immediate rendering of any pending batched data.
     * It is essential to call {@code flush()} to ensure that all sprites are rendered correctly.
     */
    public abstract void flush();

    /**
     * Renders a textured quad with specified coordinates and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     * @param width   the width of the quad
     * @param height  the height of the quad
     */
    public abstract void texture(TextureRegion texture, float x, float y, float width, float height);

    public abstract void texture(TextureRegion texture, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4);

    public abstract void texture(TextureRegion texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    /**
     * Renders a textured quad with the specified coordinates and texture coordinates, using the width and height
     * of the texture region.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     */
    public abstract void texture(TextureRegion texture, float x, float y);

    /**
     * Renders a textured quad with specified coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     * @param width   the width of the quad
     * @param height  the height of the quad
     */
    public abstract void texture(Texture texture, float x, float y, float width, float height);

    /**
     * Renders a textured quad with specified coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     */
    public abstract void texture(Texture texture, float x, float y);

    /**
     * Renders a quad with specified coordinates.
     *
     * @param x1 the x-coordinate of the first vertex
     * @param y1 the y-coordinate of the first vertex
     * @param x2 the x-coordinate of the second vertex
     * @param y2 the y-coordinate of the second vertex
     * @param x3 the x-coordinate of the third vertex
     * @param y3 the y-coordinate of the third vertex
     * @param x4 the x-coordinate of the fourth vertex
     * @param y4 the y-coordinate of the fourth vertex
     */
    public abstract void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    /**
     * Renders a quad with specified coordinates using {@link Vector2f} vertices.
     *
     * @param v1 the position of the first vertex
     * @param v2 the position of the second vertex
     * @param v3 the position of the third vertex
     * @param v4 the position of the fourth vertex
     */
    public abstract void quad(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4);

    public abstract void quad(Vector2f positon, Vector2f dimensions);

    public abstract void quad(float x, float y, float width, float height);

    /**
     * Renders a textured triangle with specified coordinates and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param x1      the x-coordinate of the first vertex
     * @param y1      the y-coordinate of the first vertex
     * @param x2      the x-coordinate of the second vertex
     * @param y2      the y-coordinate of the second vertex
     * @param x3      the x-coordinate of the third vertex
     * @param y3      the y-coordinate of the third vertex
     */
    public abstract void texturedTriangle(TextureRegion texture, float x1, float y1, float x2, float y2,
                                          float x3, float y3);

    /**
     * Renders a textured triangle with specified {@link Vector2f} vertices and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param xy1     the position of the first vertex
     * @param xy2     the position of the second vertex
     * @param xy3     the position of the third vertex
     */
    public abstract void texturedTriangle(TextureRegion texture, Vector2f xy1, Vector2f xy2, Vector2f xy3);

    /**
     * Renders a triangle with specified coordinates.
     *
     * @param x1 the x-coordinate of the first vertex
     * @param y1 the y-coordinate of the first vertex
     * @param x2 the x-coordinate of the second vertex
     * @param y2 the y-coordinate of the second vertex
     * @param x3 the x-coordinate of the third vertex
     * @param y3 the y-coordinate of the third vertex
     */
    public abstract void triangle(float x1, float y1, float x2, float y2, float x3, float y3);

    /**
     * Renders a triangle with specified {@link Vector2f} vertices.
     *
     * @param v1 the position of the first vertex
     * @param v2 the position of the second vertex
     * @param v3 the position of the third vertex
     */
    public abstract void triangle(Vector2f v1, Vector2f v2, Vector2f v3);

    /**
     * Renders a line between two specified coordinates.
     *
     * @param x1 the x-coordinate of the starting point
     * @param y1 the y-coordinate of the starting point
     * @param x2 the x-coordinate of the ending point
     * @param y2 the y-coordinate of the ending point
     */
    public abstract void line(float x1, float y1, float x2, float y2);

    /**
     * Renders a line between two specified {@link Vector2f} points.
     *
     * @param v1 the starting point
     * @param v2 the ending point
     */
    public abstract void line(Vector2f v1, Vector2f v2);

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
