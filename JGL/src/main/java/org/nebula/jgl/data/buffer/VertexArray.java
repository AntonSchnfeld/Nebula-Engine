package org.nebula.jgl.data.buffer;

import org.nebula.base.interfaces.IDisposable;

import static org.lwjgl.opengl.GL33C.*;

public class VertexArray implements IDisposable {
    private static VertexArray current;
    public final int id;

    public VertexArray() {
        this.id = glGenVertexArrays();
    }

    public void bind() {
        if (!isBound()) {
            glBindVertexArray(id);
            current = this;
        }
    }

    public void unbind() {
        if (isBound()) {
            glBindVertexArray(0);
        }
    }

    public void enableVertexAttributeArray(int position) {
        bind();
        glEnableVertexAttribArray(position);
    }

    public void vertexAttribPointer(int index, int size, Buffer.Datatype dataType, int stride, int pointer) {
        bind();
        glVertexAttribPointer(index, size, dataType.getGlConstant(), false, stride, pointer);
        enableVertexAttributeArray(index);
    }

    public void disableVertexAttribArray(int position) {
        bind();
        glDisableVertexAttribArray(position);
    }

    private boolean isBound() {
        return current == this;
    }

    @Override
    public void dispose() {
        glDeleteVertexArrays(id);
    }
}
