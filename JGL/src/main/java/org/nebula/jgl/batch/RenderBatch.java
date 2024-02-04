package org.nebula.jgl.batch;

import org.joml.Vector2f;
import org.nebula.jgl.JGL;
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
        float[] quadVertices = getVerticesFromList(this.quadVertices);
        float[] lineVertices = getVerticesFromList(this.lineVertices);

        triBuffer.data(triangleVertices, BufferUsage.STATIC_DRAW);
        quadBuffer.data(quadVertices, BufferUsage.STATIC_DRAW);
        lineBuffer.data(lineVertices, BufferUsage.STATIC_DRAW);

        shader.bind();

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
        glLineWidth(lineWidth);
        glDrawArrays(GL_LINES, 0, this.lineVertices.size());
        JGL.checkForOpenGLError();
        lineVao.disableVertexAttribArray(POSITION_LOC);
        lineVao.disableVertexAttribArray(COLOR_LOC);
        lineVao.disableVertexAttribArray(UV_LOC);
        lineVao.disableVertexAttribArray(TEXTURE_ID_LOC);
        lineVao.unbind();

        shader.unbind();
    }

    private void generateQuadElementBuffer() {
        int[] indices = new int[(int) (quadVertices.size() * 1.5)];

        for (int i = 0; i < indices.length; i+=6) {
            int offsetArrayIndex = 6 * i;
            int offset = 4 * i;

            // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
            // Triangle 1
            indices[offsetArrayIndex] = offset + 3;
            indices[offsetArrayIndex + 1] = offset + 2;
            indices[offsetArrayIndex + 2] = offset + 0;

            // Triangle 2
            indices[offsetArrayIndex + 3] = offset + 0;
            indices[offsetArrayIndex + 4] = offset + 2;
            indices[offsetArrayIndex + 5] = offset + 1;
        }

        quadElementBuffer.data(indices, BufferUsage.STATIC_DRAW);
    }

    private float[] getVerticesFromList(List<Vertex> vertexList) {
        float[] vertices = new float[VERTEX_SIZE * vertexList.size()];

        for (int i = 0; i < vertices.length; i+=Vertex.VERTEX_SIZE) {
            Vertex vertex = vertexList.get(i / VERTEX_SIZE);
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
        final Vector2f bottomLeft = new Vector2f(x, y),
        bottomRight = new Vector2f(x + width, y),
        topLeft = new Vector2f(x, y + width),
        topRight = new Vector2f(x + width, y + height);
        if (texture == null) {
            quad(bottomLeft, topLeft, topRight, bottomRight);
            return;
        }

        if (!canFit(texture))
            throw new RuntimeException("Tried to add texture to RenderBatch even though there was no space");

        final float[] uvs = texture.getUvs();
        final int texId = texture.getTexture().getId();

        quadVertices.add(new Vertex(bottomLeft, color, new Vector2f(uvs[0], uvs[1]), texId));
        quadVertices.add(new Vertex(topLeft, color, new Vector2f(uvs[2], uvs[3]), texId));
        quadVertices.add(new Vertex(topRight, color, new Vector2f(uvs[4], uvs[5]), texId));
        quadVertices.add(new Vertex(bottomRight, color, new Vector2f(uvs[6], uvs[7]), texId));
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
        final float r = color.getRed(), g = color.getGreen(), b = color.getBlue(),
                a = color.getAlpha();
        quadVertices.add(new Vertex(x1, y1, r, g, b, a, -1, -1, -1));
        quadVertices.add(new Vertex(x2, y2, r, g, b, a, -1, -1, -1));
        quadVertices.add(new Vertex(x3, y3, r, g, b, a, -1, -1, -1));
        quadVertices.add(new Vertex(x4, y4, r, g, b, a, -1, -1, -1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quad(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4) {
        quad(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texturedTriangle(TextureRegion texture, float x1, float y1, float x2, float y2, float x3, float y3) {
        // Using this function without a texture is virtually the same as triangle
        if (texture == null) {
            triangle(x1, y1, x2, y2, x3, y3);
            return;
        }

        final Texture tex = texture.getTexture();
        if (!canFit(tex))
            throw new RuntimeException("Tried to add texture to RenderBatch even though there was no space");
        textures.add(tex);

        final float[] uvs = texture.getUvs();
        final int texId = tex.getId();

        triVertices.add(new Vertex(x1, y1, color, uvs[0], uvs[1], texId));
        triVertices.add(new Vertex(x2, y2, color, uvs[2], uvs[3], texId));
        triVertices.add(new Vertex(x3, y3, color, uvs[4], uvs[5], texId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texturedTriangle(TextureRegion texture, Vector2f v1, Vector2f v2, Vector2f v3) {
        texturedTriangle(texture, v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        triVertices.add(new Vertex(x1, y1, color, -1, -1, -1));
        triVertices.add(new Vertex(x2, y2, color, -1, -1, -1));
        triVertices.add(new Vertex(x3, y3, color, -1, -1, -1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triangle(Vector2f v1, Vector2f v2, Vector2f v3) {
        triangle(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
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

    public boolean canFit(Texture texture) {
        if (!textures.contains(texture) && textures.size() >= maxTextures)
            return false;
        return true;
    }

    public boolean canFit(TextureRegion texture) {
        return canFit(texture.getTexture());
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
