package org.nebula.jgl.data;

import org.lwjgl.BufferUtils;
import org.nebula.base.interfaces.IDisposable;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;

/**
 * <br>
 * <h2>Buffer</h2>
 * <br>
 * The Buffer class provides a convenient abstraction for managing OpenGL buffer objects, such as Vertex Buffer Objects (VBOs)
 * and Element Buffer Objects (EBOs). It includes methods for binding, unbinding, and storing data in the buffer.
 * This class also implements the IDisposable interface for efficient resource cleanup.
 * @author Anton Schoenfeld
 */
public class Buffer implements IDisposable
{
    /**
     * Enum representing different buffer usage patterns, specifying how the data will be accessed and modified.
     * Each enum constant corresponds to an OpenGL constant for buffer usage.
     */
    public enum BufferUsage {
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
        DYNAMIC_COPY(GL_DYNAMIC_COPY),
        DYNAMIC_READ(GL_DYNAMIC_READ),
        STATIC_DRAW(GL_STATIC_DRAW),
        STATIC_COPY(GL_STATIC_COPY),
        STATIC_READ(GL_STATIC_READ),
        STREAM_DRAW(GL_STREAM_DRAW),
        STREAM_COPY(GL_STREAM_COPY),
        STREAM_READ(GL_STREAM_READ);

        private final int glConstant;

        BufferUsage(int glConstant) {
            this.glConstant = glConstant;
        }

        /**
         * Get the OpenGL constant associated with the buffer usage pattern.
         *
         * @return The OpenGL constant for the buffer usage.
         */
        public int getGlConstant() {
            return glConstant;
        }
    }

    private final int id;
    private final int bufferType;

    /**
     * Constructs a Buffer object with the specified buffer type (e.g., GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER).
     *
     * @param bufferType The OpenGL buffer type.
     */
    public Buffer(final int bufferType) {
        id = glGenBuffers();
        this.bufferType = bufferType;
    }

    /**
     * Binds the buffer, making it the current buffer of the specified type.
     */
    public void bind() {
        glBindBuffer(bufferType, id);
    }

    /**
     * Unbinds the buffer, switching back to the default buffer for the specified type.
     */
    public void unbind() {
        glBindBuffer(bufferType, 0);
    }

    /**
     * Stores the specified float array data in the buffer with the given usage pattern.
     *
     * @param data   The float array data to be stored in the buffer.
     * @param usage  The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void put(float[] data, BufferUsage usage) {
        bind();
        glBufferData(id, BufferUtils.createFloatBuffer(data.length).put(data), usage.getGlConstant());
        unbind();
    }

    /**
     * Stores the specified FloatBuffer data in the buffer with the given usage pattern.
     *
     * @param data   The FloatBuffer data to be stored in the buffer.
     * @param usage  The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void put(FloatBuffer data, BufferUsage usage) {
        bind();
        glBufferData(id, data, usage.getGlConstant());
        unbind();
    }

    /**
     * Stores the specified int array data in the buffer with the given usage pattern.
     *
     * @param data   The int array data to be stored in the buffer.
     * @param usage  The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void put(int[] data, BufferUsage usage) {
        bind();
        glBufferData(id, BufferUtils.createIntBuffer(data.length).put(data), usage.getGlConstant());
        unbind();
    }

    /**
     * Stores the specified IntBuffer data in the buffer with the given usage pattern.
     *
     * @param data   The IntBuffer data to be stored in the buffer.
     * @param usage  The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void put(IntBuffer data, BufferUsage usage) {
        bind();
        glBufferData(id, data, usage.getGlConstant());
        unbind();
    }

    /**
     * Disposes of the buffer, releasing associated OpenGL resources.
     * This method should be called when the buffer is no longer needed to avoid memory leaks.
     */
    @Override
    public void dispose() {
        glDeleteBuffers(id);
    }
}