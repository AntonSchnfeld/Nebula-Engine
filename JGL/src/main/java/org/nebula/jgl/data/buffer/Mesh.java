package org.nebula.jgl.data.buffer;

public class Mesh {
    private VertexArray vertexArray;
    private Buffer vertexBuffer, elementBuffer;

    public Mesh() {
        vertexArray = new VertexArray();
        vertexBuffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);
        elementBuffer = new Buffer(Buffer.BufferType.ELEMENT_ARRAY_BUFFER);
    }
}