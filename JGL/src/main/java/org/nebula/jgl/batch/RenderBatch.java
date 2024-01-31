package org.nebula.jgl.batch;

import org.joml.Vector2f;
import org.nebula.jgl.data.Vertex;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class RenderBatch extends Batch {
    private final VertexArray triVao;
    private final Buffer triBuffer, triElementBuffer;
    private final List<Vertex> triVertices;

    private final List<Texture> textures;
    private int maxTextures;
    private boolean rendering;

    public RenderBatch(int maxTextures) {
        super();
        this.maxTextures = maxTextures;
        triVao = new VertexArray();
        triBuffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);
        triElementBuffer = new Buffer(Buffer.BufferType.ELEMENT_ARRAY_BUFFER);
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
        triElementBuffer.bind();
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {

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
}
