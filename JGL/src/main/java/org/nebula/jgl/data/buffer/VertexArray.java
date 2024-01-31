package org.nebula.jgl.data.buffer;

import org.nebula.base.interfaces.IDisposable;

import static org.lwjgl.opengl.GL33C.*;

public class VertexArray implements IDisposable {
    public final int id;

    public VertexArray() {
        this.id = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void enableVertexAttributeArray(int position) {
        bind();
        glEnableVertexAttribArray(position);
    }

    public void vertexAttribPointer(int index, int size, Buffer.BufferDataType dataType, int stride, int pointer) {
        bind();
        glVertexAttribPointer(index, size, dataType.getGlConstant(), false, stride, pointer);
    }

    public void disableVertexAttribArray(int position) {
        bind();
        glDisableVertexAttribArray(position);
    }

    @Override
    public void dispose() {
        glDeleteVertexArrays(id);
    }
}
