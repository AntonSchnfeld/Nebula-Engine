package org.nebula.jgl.batch;

import org.joml.Vector2f;
import org.nebula.jgl.data.Shader;
import org.nebula.jgl.data.Vertex;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;
import static org.nebula.jgl.data.buffer.Buffer.*;
import static org.nebula.jgl.data.Vertex.*;

public class RenderBatch extends Batch {
    private final VertexArray triVao;
    private final Buffer triBuffer;
    private final List<Vertex> triVertices;

    private final List<Texture> textures;
    private int maxTextures;
    private boolean rendering;

    public RenderBatch(int maxTextures) {
        super();
        this.maxTextures = maxTextures;
        triVao = new VertexArray();
        triBuffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);
        triVertices = new ArrayList<>();

        textures = new ArrayList<>();

        rendering = false;

        init();
    }

    public RenderBatch() {
        this(8);
    }

    private void init() {
        triVao.bind();
        triBuffer.bind();

        triVao.vertexAttribPointer(POSITION_LOC, POSITION_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, POSITION_STRIDE);
        triVao.vertexAttribPointer(COLOR_LOC, COLOR_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, COLOR_STRIDE);
        triVao.vertexAttribPointer(UV_LOC, UV_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, UV_STRIDE);
        triVao.vertexAttribPointer(TEXTURE_ID_LOC, TEXTURE_ID_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, TEXTURE_ID_STRIDE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        if (rendering)
            throw new IllegalStateException("Can not call RenderBatch.begin when RenderBatch is already rendering");

        rendering = true;

        triVertices.clear();
        textures.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() {
        if (!rendering)
            throw new IllegalStateException("Can not call RenderBatch.end when RenderBatch is not rendering");

        rendering = false;
        flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        System.out.println("#################################################");

        float[] triangleVertices = new float[VERTEX_SIZE * triVertices.size()];

        for (int i = 0; i < triangleVertices.length; i+=Vertex.VERTEX_SIZE) {
            Vertex vertex = triVertices.get(i / VERTEX_SIZE);
            float[] array = vertex.toArray();
            System.arraycopy(array, 0, triangleVertices, i, array.length);
        }

        for (int i = 0; i < triangleVertices.length; i++) {
            if (i % VERTEX_SIZE == 0) System.out.println();
            System.out.print(triangleVertices[i] + ((i + 1) % VERTEX_SIZE != 0 ? ", " : ""));
        }

        triVao.bind();
        triBuffer.data(triangleVertices, Buffer.BufferUsage.DYNAMIC_DRAW);

        shader.bind();

        shader.uploadUniformMat4f(Shader.PROJECTION_MAT_NAME, projectionMatrix);
        shader.uploadUniformMat4f(Shader.VIEW_MAT_NAME, viewMatrix);

        triVao.bind();
        triBuffer.bind();
        glDrawArrays(GL_TRIANGLES, 0, triangleVertices.length);
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(TextureRegion texture, float x, float y, float width, float height) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(Texture texture, float x, float y, float width, float height) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(Texture texture, float x, float y) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quad(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texturedTriangle(TextureRegion texture, float x1, float y1, float x2, float y2, float x3, float y3) {
        texturedTriangle(texture, new Vector2f(x1, y1), new Vector2f(x2, y2), new Vector2f(x3, y3));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texturedTriangle(TextureRegion texture, Vector2f v1, Vector2f v2, Vector2f v3) {
        // Using this function without a texture is virtually the same as triangle
        if (texture == null) {
            triangle(v1, v2, v3);
            return;
        }

        if (!textures.contains(texture.getTexture()) && textures.size() >= maxTextures)
            throw new RuntimeException("Tried to add texture to RenderBatch even though there was no space");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        triangle(new Vector2f(x1, y1), new Vector2f(x2, y2), new Vector2f(x3, y3));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triangle(Vector2f v1, Vector2f v2, Vector2f v3) {
        triVertices.add(new Vertex(v1, color, null, -1));
        triVertices.add(new Vertex(v2, color, null, -1));
        triVertices.add(new Vertex(v3, color, null, -1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void line(float x1, float y1, float x2, float y2) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void line(Vector2f v1, Vector2f v2) {

    }

    @Override
    public void dispose() {
        triVao.dispose();
        triBuffer.dispose();
    }
}
