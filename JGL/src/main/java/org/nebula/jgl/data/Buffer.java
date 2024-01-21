package org.nebula.jgl.data;

import org.lwjgl.BufferUtils;
import org.nebula.base.interfaces.IDisposable;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;

public class Buffer implements IDisposable
{
    public enum BufferUsage
    {
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW), DYNAMIC_COPY(GL_DYNAMIC_COPY), DYNAMIC_READ(GL_DYNAMIC_READ),
        STATIC_DRAW(GL_STATIC_DRAW), STATIC_COPY(GL_STATIC_COPY), STATIC_READ(GL_STATIC_READ),
        STREAM_DRAW(GL_STREAM_DRAW), STREAM_COPY(GL_STREAM_COPY), STREAM_READ(GL_STREAM_READ);

        private int glConstant;

        BufferUsage(int glConstant)
        {
            this.glConstant = glConstant;
        }

        public int getGlConstant ()
        {
            return glConstant;
        }
    }
    private final int id;
    private final int bufferType;

    public Buffer (final int bufferType)
    {
        id = glGenBuffers();
        this.bufferType = bufferType;
    }

    public void bind()
    {
        glBindBuffer(bufferType, id);
    }

    public void unbind()
    {
        glBindBuffer(bufferType, 0);
    }

    public void put (float[] data, BufferUsage usage)
    {
        bind();
        glBufferData(id, BufferUtils.createFloatBuffer(data.length).put(data), usage.glConstant);
        unbind();
    }

    public void put (FloatBuffer data, BufferUsage usage)
    {
        bind();
        glBufferData(id, data, usage.glConstant);
        unbind();
    }

    public void put (int[] data, BufferUsage usage)
    {
        bind();
        glBufferData(id, BufferUtils.createIntBuffer(data.length).put(data), usage.glConstant);
        unbind();
    }

    public void put (IntBuffer data, BufferUsage usage)
    {
        bind();
        glBufferData(id, data, usage.glConstant);
        unbind();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose()
    {
        glDeleteBuffers(id);
    }
}
