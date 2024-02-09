package org.nebula.jgl.data.shader;

import org.nebula.jgl.data.buffer.VertexArray;

import java.util.Arrays;

public class VertexAttribs {
    private final VertexAttrib[] vertexAttribs;
    private final int vertexSize, vertexSizeBytes;

    public VertexAttribs(VertexAttrib[] vertexAttribs) {
        this.vertexAttribs = vertexAttribs;

        int size = 0, bytes = 0;
        for (VertexAttrib va : vertexAttribs) {
            size += va.getSize();
            bytes += va.getBytes();
        }
        vertexSizeBytes = bytes;
        vertexSize = size;


        Arrays.sort(vertexAttribs);
    }

    public int getVertexSizeBytes() {
        return vertexSizeBytes;
    }

    public int getVertexSize() {
        return vertexSize;
    }

    public VertexAttrib get(int index) {
        return vertexAttribs[index];
    }

    public void configure(VertexArray vertexArray) {
        int pointer = 0;

        for (VertexAttrib va : vertexAttribs) {
            vertexArray.vertexAttribPointer(va.getLocation(), va.getSize(), va.getDataType(), vertexSizeBytes, pointer);
            pointer += va.getBytes();
        }
    }

    public void enable(VertexArray vertexArray) {
        for (VertexAttrib va : vertexAttribs)
            vertexArray.enableVertexAttributeArray(va.getLocation());
    }

    public void disable(VertexArray vertexArray) {
        for (VertexAttrib va : vertexAttribs)
            vertexArray.disableVertexAttribArray(va.getLocation());
    }

    public VertexAttrib[] getVertexAttribs() {
        return vertexAttribs.clone();
    }

    @Override
    public String toString() {
        return Arrays.toString(vertexAttribs);
    }
}