package org.nebula.jgl.data.buffer;

import org.nebula.base.interfaces.IDisposable;
import org.nebula.jgl.JGL;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.shader.VertexAttribs;

import static org.lwjgl.opengl.GL33C.glDrawArrays;

public class Mesh implements IDisposable {
    private final VertexArray vertexArray;
    private final Buffer vertexBuffer;
    private final Shader shader;
    private final VertexAttribs vertexAttribs;
    private final long maxSize;
    private long vertexIndex;

    public Mesh(Shader shader, long maxSize, Buffer.BufferUsage usage) {
        this.shader = shader;
        this.maxSize = maxSize;
        this.vertexIndex = 0;
        this.vertexAttribs = shader.getVertexAttribs();

        vertexArray = new VertexArray();
        vertexBuffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);

        vertexBuffer.data(maxSize, usage, Buffer.BufferDataType.FLOAT);
        vertexAttribs.configure(vertexArray);
    }

    public void addVertices(float[] data) {
        if ((data.length + vertexIndex) > maxSize)
            throw new IllegalArgumentException("Mesh could not fit data");

        vertexBuffer.subData(data, vertexIndex);
        vertexIndex += data.length;
    }

    public void setVertices(float[] data) {
        if (data.length > maxSize)
            throw new IllegalArgumentException("Mesh could not fit data");

        vertexBuffer.subData(data, 0);
        vertexIndex += data.length;
    }

    public void draw(int mode, int first, int count) {
        shader.bind();
        vertexAttribs.enable(vertexArray);

        glDrawArrays(mode, first, count);
        JGL.checkForOpenGLError();

        vertexAttribs.disable(vertexArray);
        shader.unbind();
    }

    public void draw(int mode) {
        draw(mode, 0, (int) (vertexIndex / shader.getVertexAttribs().getVertexSize()));
    }

    public Shader getShader() {
        return shader;
    }

    public void dispose() {
        vertexBuffer.dispose();
        vertexArray.dispose();
    }
}