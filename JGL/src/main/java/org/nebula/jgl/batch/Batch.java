package org.nebula.jgl.batch;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.Texture;

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
 *
 * @see Texture
 * @see Color
 * @see org.joml.Matrix4f
 * @see org.joml.Vector2f
 * @see org.joml.Vector4f
 */
public interface Batch
{
    /**
     * Renders a textured quad with specified coordinates and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     * @param width   the width of the quad
     * @param height  the height of the quad
     * @param u1      the first texture coordinate along the x-axis
     * @param v1      the first texture coordinate along the y-axis
     * @param u2      the second texture coordinate along the x-axis
     * @param v2      the second texture coordinate along the y-axis
     * @param u3      the third texture coordinate along the x-axis
     * @param v3      the third texture coordinate along the y-axis
     * @param u4      the fourth texture coordinate along the x-axis
     * @param v4      the fourth texture coordinate along the y-axis
     */
    void texture(Texture texture, float x, float y, float width, float height, float u1, float v1, float u2,
                 float v2, float u3, float v3, float u4, float v4);

    /**
     * Renders a textured quad with specified coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     * @param width   the width of the quad
     * @param height  the height of the quad
     */
    void texture(Texture texture, float x, float y, float width, float height);

    /**
     * Renders a textured quad with specified coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     */
    void texture(Texture texture, float x, float y);

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
    void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    /**
     * Renders a quad with specified coordinates using {@link Vector2f} vertices.
     *
     * @param v1 the position of the first vertex
     * @param v2 the position of the second vertex
     * @param v3 the position of the third vertex
     * @param v4 the position of the fourth vertex
     */
    void quad(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4);

    /**
     * Renders a textured triangle with specified coordinates and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param x1      the x-coordinate of the first vertex
     * @param y1      the y-coordinate of the first vertex
     * @param u1      the texture coordinate along the x-axis for the first vertex
     * @param v1      the texture coordinate along the y-axis for the first vertex
     * @param x2      the x-coordinate of the second vertex
     * @param y2      the y-coordinate of the second vertex
     * @param u2      the texture coordinate along the x-axis for the second vertex
     * @param v2      the texture coordinate along the y-axis for the second vertex
     * @param x3      the x-coordinate of the third vertex
     * @param y3      the y-coordinate of the third vertex
     * @param u3      the texture coordinate along the x-axis for the third vertex
     * @param v3      the texture coordinate along the y-axis for the third vertex
     */
    void texturedTriangle(Texture texture, float x1, float y1, float u1, float v1, float x2, float y2, float u2,
                          float v2, float x3, float y3, float u3, float v3);

    /**
     * Renders a textured triangle with specified {@link Vector2f} vertices and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param xy1     the position of the first vertex
     * @param uv1     the texture coordinate of the first vertex
     * @param xy2     the position of the second vertex
     * @param uv2     the texture coordinate of the second vertex
     * @param xy3     the position of the third vertex
     * @param uv3     the texture coordinate of the third vertex
     */
    void texturedTriangle(Texture texture, Vector2f xy1, Vector2f uv1, Vector2f xy2, Vector2f uv2,
                          Vector2f xy3, Vector2f uv3);

    /**
     * Renders a textured triangle with specified {@link Vector4f} vertices.
     *
     * @param texture the texture to be rendered
     * @param v1      the position and texture coordinate of the first vertex
     * @param v2      the position and texture coordinate of the second vertex
     * @param v3      the position and texture coordinate of the third vertex
     */
    void texturedTriangle(Texture texture, Vector4f v1, Vector4f v2, Vector4f v3);

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
    void triangle(float x1, float y1, float x2, float y2, float x3, float y3);

    /**
     * Renders a triangle with specified {@link Vector2f} vertices.
     *
     * @param v1 the position of the first vertex
     * @param v2 the position of the second vertex
     * @param v3 the position of the third vertex
     */
    void triangle(Vector2f v1, Vector2f v2, Vector2f v3);

    /**
     * Renders a line between two specified coordinates.
     *
     * @param x1 the x-coordinate of the starting point
     * @param y1 the y-coordinate of the starting point
     * @param x2 the x-coordinate of the ending point
     * @param y2 the y-coordinate of the ending point
     */
    void line(float x1, float y1, float x2, float y2);

    /**
     * Renders a line between two specified {@link Vector2f} points.
     *
     * @param v1 the starting point
     * @param v2 the ending point
     */
    void line(Vector2f v1, Vector2f v2);

    /**
     * Sets the color for subsequent rendering operations.
     *
     * @param color the color to be set
     */
    void setColor(Color color);

    /**
     * Enables or disables blending for subsequent rendering operations.
     *
     * @param enabled true to enable blending, false to disable
     */
    void setBlendingEnabled(boolean enabled);

    /**
     * Sets the projection matrix for subsequent rendering operations.
     *
     * @param projectionMatrix the projection matrix to be set
     */
    void setProjectionMatrix(Matrix4f projectionMatrix);

    /**
     * Retrieves the current color used for rendering.
     *
     * @return the current color
     */
    Color getColor();

    /**
     * Checks if blending is currently enabled for rendering operations.
     *
     * @return true if blending is enabled, false otherwise
     */
    boolean isBlendingEnabled();

    /**
     * Retrieves the current projection matrix used for rendering.
     *
     * @return the current projection matrix
     */
    Matrix4f getProjectionMatrix();
}
