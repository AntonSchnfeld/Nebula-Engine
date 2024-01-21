package org.nebula.jgl.batch;

import org.joml.Vector2f;
import org.nebula.jgl.data.Texture;
import org.nebula.jgl.data.TextureRegion;

public class RenderBatch extends Batch
{
    public RenderBatch ()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(TextureRegion texture, float x, float y, float width, float height)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(Texture texture, float x, float y, float width, float height)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texture(Texture texture, float x, float y)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quad(Vector2f v1, Vector2f v2, Vector2f v3, Vector2f v4)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texturedTriangle(TextureRegion texture, float x1, float y1, float x2, float y2, float x3, float y3)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void texturedTriangle(TextureRegion texture, Vector2f xy1, Vector2f xy2, Vector2f xy3)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triangle(Vector2f v1, Vector2f v2, Vector2f v3)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void line(float x1, float y1, float x2, float y2)
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void line(Vector2f v1, Vector2f v2)
    {

    }
}
