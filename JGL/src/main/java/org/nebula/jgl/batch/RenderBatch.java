package org.nebula.jgl.batch;

import org.joml.Vector2f;
import org.nebula.base.util.Pool;
import org.nebula.jgl.JGL;
import org.nebula.jgl.data.Vertex;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;
import static org.nebula.jgl.data.Vertex.*;
import static org.nebula.jgl.data.buffer.Buffer.*;

/**
 * <br>
 * <h2>RenderBatch</h2>
 * <br>
 * The RenderBatch class extends the Batch class and provides a versatile and efficient rendering system for textured
 * quads, triangles, and lines. It supports batching multiple renderable elements into a single draw call, optimizing
 * performance. The class uses vertex arrays and buffers for OpenGL rendering and includes features such as texture
 * management, depth testing, and wireframe rendering.
 *
 * @see Batch
 * @see VertexArray
 * @see Buffer
 * @see Texture
 * @see TextureRegion
 * @see Vertex
 * @see Shader
 * @see Pool
 */
public class RenderBatch extends Batch {
    private final VertexArray triVao, quadVao, lineVao;
    private final Buffer triBuffer, quadBuffer, quadElementBuffer, lineBuffer;
    private final List<Vertex> triVertices, quadVertices, lineVertices;
    private final List<Texture> textures;
    private final Pool<Vertex> vertexPool;
    private final int maxTextures;
    private final int[] slots;
    private boolean rendering;
    private float z;
    private boolean wireframeEnabled;

    /**
     * Constructs a RenderBatch with the specified maximum number of textures.
     *
     * @param maxTextures The maximum number of unique textures this RenderBatch can handle.
     */
    public RenderBatch(int maxTextures) {
        super();
        this.maxTextures = maxTextures;

        this.vertexPool = new Pool<>() {
            @Override
            protected Vertex newPoolable() {
                return new Vertex();
            }
        };

        slots = new int[maxTextures];
        for (int i = 0; i < maxTextures; i++)
            slots[i] = i;

        triVao = new VertexArray();
        quadVao = new VertexArray();
        lineVao = new VertexArray();

        triBuffer = new Buffer(Type.ARRAY_BUFFER);
        quadBuffer = new Buffer(Type.ARRAY_BUFFER);
        quadElementBuffer = new Buffer(Type.ELEMENT_ARRAY_BUFFER);
        lineBuffer = new Buffer(Type.ARRAY_BUFFER);

        triVertices = new ArrayList<>();
        quadVertices = new ArrayList<>();
        lineVertices = new ArrayList<>();

        textures = new ArrayList<>();

        rendering = false;

        wireframeEnabled = false;

        z = 0;

        init();
    }

    /**
     * Constructs a RenderBatch with the default maximum number of textures based on the OpenGL hardware limit.
     */
    public RenderBatch() {
        this(JGL.getMaxTextureImageUnits());
    }

    /**
     * Initializes vertex arrays and buffers for each type of primitive (triangle, quad, line).
     */
    private void init() {
        initVertexArray(triVao, triBuffer);
        initVertexArray(quadVao, quadBuffer);
        initVertexArray(lineVao, lineBuffer);
    }

    /**
     * Initializes a VertexArray with the specified Buffer configuration.
     *
     * @param vertexArray The VertexArray to initialize.
     * @param buffer      The Buffer to bind to the VertexArray.
     */
    private void initVertexArray(VertexArray vertexArray, Buffer buffer) {
        vertexArray.bind();
        buffer.bind();
        vertexArray.vertexAttribPointer(POSITION_LOC, POSITION_SIZE, Datatype.FLOAT, VERTEX_SIZE_BYTES, POSITION_POINTER);
        vertexArray.vertexAttribPointer(COLOR_LOC, COLOR_SIZE, Datatype.FLOAT, VERTEX_SIZE_BYTES, COLOR_POINTER);
        vertexArray.vertexAttribPointer(UV_LOC, UV_SIZE, Datatype.FLOAT, VERTEX_SIZE_BYTES, UV_POINTER);
        vertexArray.vertexAttribPointer(TEXTURE_ID_LOC, TEXTURE_ID_SIZE, Datatype.FLOAT, VERTEX_SIZE_BYTES, TEXTURE_ID_POINTER);
        vertexArray.disableVertexAttribArray(POSITION_LOC);
        vertexArray.disableVertexAttribArray(COLOR_LOC);
        vertexArray.disableVertexAttribArray(UV_LOC);
        vertexArray.disableVertexAttribArray(TEXTURE_ID_LOC);
    }

    /**
     * Begins the rendering process, clearing stored vertices and textures.
     */
    @Override
    public void begin() {
        super.begin();

        vertexPool.addAll(triVertices);
        vertexPool.addAll(quadVertices);
        vertexPool.addAll(lineVertices);

        triVertices.clear();
        quadVertices.clear();
        lineVertices.clear();
        textures.clear();

        z = 0f;
    }

    /**
     * Flushes the stored vertices and textures, preparing for rendering.
     */
    @Override
    public void flush() {
        glLineWidth(lineWidth);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        float[] triangleVertices = getVerticesFromList(triVertices);
        float[] quadVertices = getVerticesFromList(this.quadVertices);
        float[] lineVertices = getVerticesFromList(this.lineVertices);

        triBuffer.data(triangleVertices, Usage.STATIC_DRAW);
        quadBuffer.data(quadVertices, Usage.STATIC_DRAW);
        lineBuffer.data(lineVertices, Usage.STATIC_DRAW);

        shader.bind();

        shader.uploadUniformIntArray("uTextures", slots);
        for (int i = 0; i < textures.size(); i++) {
            Texture texture = textures.get(i);
            if (texture != null)
                texture.bindToSlot(i);
        }

        shader.uploadUniformMat4f(Shader.PROJECTION_MAT_NAME, projectionMatrix);
        shader.uploadUniformMat4f(Shader.VIEW_MAT_NAME, viewMatrix);

        triVao.bind();
        triVao.enableVertexAttributeArray(POSITION_LOC);
        triVao.enableVertexAttributeArray(COLOR_LOC);
        triVao.enableVertexAttributeArray(UV_LOC);
        triVao.enableVertexAttributeArray(TEXTURE_ID_LOC);
        glDrawArrays(GL_TRIANGLES, 0, triVertices.size());
        JGL.checkForOpenGLError();
        triVao.disableVertexAttribArray(POSITION_LOC);
        triVao.disableVertexAttribArray(COLOR_LOC);
        triVao.disableVertexAttribArray(UV_LOC);
        triVao.disableVertexAttribArray(TEXTURE_ID_LOC);
        triVao.unbind();

        quadVao.bind();
        generateQuadElementBuffer();
        quadVao.enableVertexAttributeArray(POSITION_LOC);
        quadVao.enableVertexAttributeArray(COLOR_LOC);
        quadVao.enableVertexAttributeArray(UV_LOC);
        quadVao.enableVertexAttributeArray(TEXTURE_ID_LOC);
        glDrawElements(GL_TRIANGLES, (int) (this.quadVertices.size() * 1.5f), GL_UNSIGNED_INT, 0);
        JGL.checkForOpenGLError();
        quadVao.disableVertexAttribArray(POSITION_LOC);
        quadVao.disableVertexAttribArray(COLOR_LOC);
        quadVao.disableVertexAttribArray(UV_LOC);
        quadVao.disableVertexAttribArray(TEXTURE_ID_LOC);
        quadVao.unbind();

        lineVao.bind();
        lineVao.enableVertexAttributeArray(POSITION_LOC);
        lineVao.enableVertexAttributeArray(COLOR_LOC);
        lineVao.enableVertexAttributeArray(UV_LOC);
        lineVao.enableVertexAttributeArray(TEXTURE_ID_LOC);
        glDrawArrays(GL_LINES, 0, this.lineVertices.size());
        JGL.checkForOpenGLError();
        lineVao.disableVertexAttribArray(POSITION_LOC);
        lineVao.disableVertexAttribArray(COLOR_LOC);
        lineVao.disableVertexAttribArray(UV_LOC);
        lineVao.disableVertexAttribArray(TEXTURE_ID_LOC);
        lineVao.unbind();

        shader.unbind();

        glDisable(GL_DEPTH_TEST);
    }

    /**
     * Generates the element buffer for quad rendering.
     */
    private void generateQuadElementBuffer() {
        int[] indices = new int[quadVertices.size() * 6 / 4];

        for (int i = 0; i < quadVertices.size(); i += 4) {
            int offsetArrayIndex = i * 6 / 4;
            int offset = i;

            // Triangle 1
            indices[offsetArrayIndex] = offset + 3;
            indices[offsetArrayIndex + 1] = offset + 2;
            indices[offsetArrayIndex + 2] = offset;

            // Triangle 2
            indices[offsetArrayIndex + 3] = offset;
            indices[offsetArrayIndex + 4] = offset + 2;
            indices[offsetArrayIndex + 5] = offset + 1;
        }

        quadElementBuffer.data(indices, Usage.STATIC_DRAW);
    }

    /**
     * Converts the stored vertices from a list to a float array for rendering.
     *
     * @param vertexList The list of vertices to convert.
     * @return The float array containing vertex data.
     */
    private float[] getVerticesFromList(List<Vertex> vertexList) {
        float[] vertices = new float[VERTEX_SIZE * vertexList.size()];

        for (int i = 0; i < vertices.length; i += Vertex.VERTEX_SIZE) {
            Vertex vertex = vertexList.get(i / VERTEX_SIZE);
            float[] array = vertex.toArray();
            System.arraycopy(array, 0, vertices, i, array.length);
        }

        return vertices;
    }

    /**
     * Renders a textured quad with specified coordinates and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     * @param width   the width of the quad
     * @param height  the height of the quad
     */
    public void texture(TextureRegion texture, float x, float y, float width, float height) {
        texture(texture, x, y, x + width, y, x, y + width, x + width, y + height);
    }

    public void texture(TextureRegion texture, Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4) {
        texture(texture, v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y);
    }

    /**
     * Renders a textured quad with specified coordinates and texture coordinates.
     *
     * @param texture The texture to be rendered.
     * @param x1      The x-coordinate of the first vertex.
     * @param y1      The y-coordinate of the first vertex.
     * @param x2      The x-coordinate of the second vertex.
     * @param y2      The y-coordinate of the second vertex.
     * @param x3      The x-coordinate of the third vertex.
     * @param y3      The y-coordinate of the third vertex.
     * @param x4      The x-coordinate of the fourth vertex.
     * @param y4      The y-coordinate of the fourth vertex.
     */
    public void texture(TextureRegion texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (texture == null) {
            quad(x1, y1, x3, y3, x4, y4, x2, y2);
            return;
        }

        final Texture tex = texture.getTexture();

        addTexture(tex);

        final float[] uvs = texture.getUvs();
        final int texId = textures.indexOf(texture.getTexture());



        quadVertices.add(vertexPool.get().set(x1, y1, z, color, uvs[0], uvs[1], texId));
        quadVertices.add(vertexPool.get().set(x3, y3, z, color, uvs[2], uvs[3], texId));
        quadVertices.add(vertexPool.get().set(x4, y4, z, color, uvs[4], uvs[5], texId));
        quadVertices.add(vertexPool.get().set(x2, y2, z, color, uvs[6], uvs[7], texId));

        incrementZ();
    }

    /**
     * Renders a textured quad with the specified coordinates and texture coordinates, using the width and height
     * of the texture region.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     */
    public void texture(TextureRegion texture, float x, float y) {
        texture(texture, x, y, texture.getTexture().getWidth(), texture.getTexture().getHeight());
    }

    /**
     * Renders a textured quad with specified coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     * @param width   the width of the quad
     * @param height  the height of the quad
     */
    public void texture(Texture texture, float x, float y, float width, float height) {
        texture(new TextureRegion(texture), x, y, width, height);
    }

    /**
     * Renders a textured quad with specified coordinates.
     *
     * @param texture the texture to be rendered
     * @param x       the x-coordinate of the quad's position
     * @param y       the y-coordinate of the quad's position
     */
    public void texture(Texture texture, float x, float y) {
        TextureRegion region = new TextureRegion(texture);
        texture(region, x, y, texture.getWidth(), texture.getHeight());
    }

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
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        quadVertices.add(vertexPool.get().set(x1, y1, z, color, -1, -1, -1));
        quadVertices.add(vertexPool.get().set(x2, y2, z, color, -1, -1, -1));
        quadVertices.add(vertexPool.get().set(x3, y3, z, color, -1, -1, -1));
        quadVertices.add(vertexPool.get().set(x4, y4, z, color, -1, -1, -1));
        incrementZ();
    }

    /**
     * Renders a quad with specified coordinates using {@link Vector2f} vertices.
     *
     * @param v1 the position of the first vertex
     * @param v2 the position of the second vertex
     * @param v3 the position of the third vertex
     * @param v4 the position of the fourth vertex
     */
    public void quad(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4) {
        quad(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y);
    }

    public void quad(Vector2f position, Vector2f dimensions) {
        quad(position.x, position.y, dimensions.x, dimensions.y);
    }

    public void quad(float x, float y, float width, float height) {
        quad(x, y, x + width, y, x, y + width, x + width, y + height);
    }

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
    public void texturedTriangle(TextureRegion texture, float x1, float y1, float x2, float y2, float x3, float y3) {
        // Using this function without a texture is virtually the same as triangle
        if (texture == null || texture.getTexture() == null) {
            triangle(x1, y1, x2, y2, x3, y3);
            return;
        }

        final Texture tex = texture.getTexture();

        addTexture(tex);

        final float[] uvs = texture.getUvs();
        final int texId = textures.indexOf(texture.getTexture());

        triVertices.add(vertexPool.get().set(x1, y1, z, color, uvs[0], uvs[1], texId));
        triVertices.add(vertexPool.get().set(x2, y2, z, color, uvs[2], uvs[3], texId));
        triVertices.add(vertexPool.get().set(x3, y3, z, color, uvs[4], uvs[5], texId));
        incrementZ();
    }

    private void addTexture(Texture texture) {
        if (!textures.contains(texture) && canFit(texture))
            textures.add(texture);
        else if (!textures.contains(texture) && !canFit(texture))
            throw new RuntimeException("Tried to add texture to RenderBatch even though there was no space");
    }

    /**
     * Renders a textured triangle with specified {@link Vector2f} vertices and texture coordinates.
     *
     * @param texture the texture to be rendered
     * @param v1     the position of the first vertex
     * @param v2     the position of the second vertex
     * @param v3     the position of the third vertex
     */
    public void texturedTriangle(TextureRegion texture, Vector2f v1, Vector2f v2, Vector2f v3) {
        texturedTriangle(texture, v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
    }

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
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        triVertices.add(vertexPool.get().set(x1, y1, z, color, -1, -1, -1));
        triVertices.add(vertexPool.get().set(x2, y2, z, color, -1, -1, -1));
        triVertices.add(vertexPool.get().set(x3, y3, z, color, -1, -1, -1));
        incrementZ();
    }

    /**
     * Renders a triangle with specified {@link Vector2f} vertices.
     *
     * @param v1 the position of the first vertex
     * @param v2 the position of the second vertex
     * @param v3 the position of the third vertex
     */
    public void triangle(Vector2f v1, Vector2f v2, Vector2f v3) {
        triangle(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
    }

    /**
     * Renders a line between two specified coordinates.
     *
     * @param x1 the x-coordinate of the starting point
     * @param y1 the y-coordinate of the starting point
     * @param x2 the x-coordinate of the ending point
     * @param y2 the y-coordinate of the ending point
     */
    public void line(float x1, float y1, float x2, float y2) {
        lineVertices.add(vertexPool.get().set(x1, y1, z, color, -1, -1, -1));
        lineVertices.add(vertexPool.get().set(x2, y2, z, color, -1, -1, -1));
        incrementZ();
    }

    private void incrementZ() {
        z += 0.0000001f;
    }

    /**
     * Renders a line between two specified {@link Vector2f} points.
     *
     * @param v1 the starting point
     * @param v2 the ending point
     */
    public void line(Vector2f v1, Vector2f v2) {
        line(v1.x, v1.y, v2.x, v2.y);
    }

    /**
     * Checks if a given texture can fit into the RenderBatch based on the maximum number of textures allowed.
     *
     * @param texture The texture to check for fit.
     * @return True if the texture can fit, false otherwise.
     */
    public boolean canFit(Texture texture) {
        return textures.contains(texture) || textures.size() < maxTextures;
    }

    /**
     * Checks if a given texture region can fit into the RenderBatch based on the maximum number of textures allowed.
     *
     * @param texture The texture region to check for fit.
     * @return True if the texture region can fit, false otherwise.
     */
    public boolean canFit(TextureRegion texture) {
        return canFit(texture.getTexture());
    }

    /**
     * Disposes of the vertex arrays and buffers used by the RenderBatch.
     */
    @Override
    public void dispose() {
        triVao.dispose();
        quadVao.dispose();
        lineVao.dispose();
        triBuffer.dispose();
        quadBuffer.dispose();
        quadElementBuffer.dispose();
        lineBuffer.dispose();
    }
}
