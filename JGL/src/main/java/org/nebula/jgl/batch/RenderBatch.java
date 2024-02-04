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
    private final VertexArray triVao, quadVao, lineVao;
    private final Buffer triBuffer, quadBuffer, quadElementBuffer, lineBuffer;
    private final List<Vertex> triVertices, quadVertices, lineVertices;

    private final List<Texture> textures;
    private final int maxTextures;
    private boolean rendering;

    public RenderBatch(int maxTextures) {
        super();
        this.maxTextures = maxTextures;

        triVao = new VertexArray();
        quadVao = new VertexArray();
        lineVao = new VertexArray();

        triBuffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);
        quadBuffer = new Buffer(BufferType.ARRAY_BUFFER);
        quadElementBuffer = new Buffer(BufferType.ELEMENT_ARRAY_BUFFER);
        lineBuffer = new Buffer(BufferType.ARRAY_BUFFER);

        triVertices = new ArrayList<>();
        quadVertices = new ArrayList<>();
        lineVertices = new ArrayList<>();

        textures = new ArrayList<>();

        rendering = false;

        init();
    }

    public RenderBatch() {
        this(8);
    }

    private void init() {
        initVertexArray(triVao, triBuffer);
        initVertexArray(quadVao, quadBuffer);
        initVertexArray(lineVao, lineBuffer);
    }

    private void initVertexArray(VertexArray vertexArray, Buffer buffer)
    {
        vertexArray.bind();
        buffer.bind();
        vertexArray.vertexAttribPointer(POSITION_LOC, POSITION_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, POSITION_POINTER);
        vertexArray.vertexAttribPointer(COLOR_LOC, COLOR_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, COLOR_POINTER);
        vertexArray.vertexAttribPointer(UV_LOC, UV_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, UV_POINTER);
        vertexArray.vertexAttribPointer(TEXTURE_ID_LOC, TEXTURE_ID_SIZE, BufferDataType.FLOAT, VERTEX_SIZE_BYTES, TEXTURE_ID_POINTER);
        vertexArray.disableVertexAttribArray(POSITION_LOC);
        vertexArray.disableVertexAttribArray(COLOR_LOC);
        vertexArray.disableVertexAttribArray(UV_LOC);
        vertexArray.disableVertexAttribArray(TEXTURE_ID_LOC);
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
        quadVertices.clear();
        lineVertices.clear();
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
        float[] triangleVertices = getVerticesFromList(triVertices);

        triBuffer.data(triangleVertices, Buffer.BufferUsage.DYNAMIC_DRAW);

        shader.bind();

        shader.uploadUniformMat4f(Shader.PROJECTION_MAT_NAME, projectionMatrix);
        shader.uploadUniformMat4f(Shader.VIEW_MAT_NAME, viewMatrix);

        triVao.bind();
        triVao.enableVertexAttributeArray(POSITION_LOC);
        triVao.enableVertexAttributeArray(COLOR_LOC);
        triVao.enableVertexAttributeArray(UV_LOC);
        triVao.enableVertexAttributeArray(TEXTURE_ID_LOC);
        glDrawArrays(GL_TRIANGLES, 0, triVertices.size());
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
        triVao.disableVertexAttribArray(POSITION_LOC);
        triVao.disableVertexAttribArray(COLOR_LOC);
        triVao.disableVertexAttribArray(UV_LOC);
        triVao.disableVertexAttribArray(TEXTURE_ID_LOC);
        triVao.unbind();

        shader.unbind();
    }

    private float[] getVerticesFromList(List<Vertex> vertexList) {
        float[] vertices = new float[VERTEX_SIZE * vertexList.size()];

        for (int i = 0; i < vertices.length; i+=Vertex.VERTEX_SIZE) {
            Vertex vertex = triVertices.get(i / VERTEX_SIZE);
            float[] array = vertex.toArray();
            System.arraycopy(array, 0, vertices, i, array.length);
        }

        return vertices;
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
    public void texture(TextureRegion texture, float x, float y) {
        texture(texture, x, y, texture.getTexture().getWidth(), texture.getTexture().getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(Texture texture, float x, float y, float width, float height) {
        texture(new TextureRegion(texture), x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(Texture texture, float x, float y) {
        TextureRegion region = new TextureRegion(texture);
        texture(region, x, y, texture.getWidth(), texture.getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        quad(new Vector2f(x1, y1), new Vector2f(x2, y2), new Vector2f(x3, y3), new Vector2f(x4, y4));
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
        triVertices.add(new Vertex(v1, color, new Vector2f(-1, -1), -1));
        triVertices.add(new Vertex(v2, color, new Vector2f(-1, -1), -1));
        triVertices.add(new Vertex(v3, color, new Vector2f(-1, -1), -1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void line(float x1, float y1, float x2, float y2) {
        line(new Vector2f(x1, y1), new Vector2f(x2, y2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void line(Vector2f v1, Vector2f v2) {
        lineVertices.add(new Vertex(v1, color, new Vector2f(-1, -1), -1));
        lineVertices.add(new Vertex(v2, color, new Vector2f(-1, -1), -1));
    }

    /**
     * {@inheritDoc}
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
