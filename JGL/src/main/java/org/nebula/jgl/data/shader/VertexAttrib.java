package org.nebula.jgl.data.shader;

import org.nebula.jgl.data.buffer.Buffer;

import java.util.Objects;

public class VertexAttrib implements Comparable<VertexAttrib> {
    private String name;
    private int size, bytes;
    private int location;
    private Buffer.BufferDataType dataType;

    public VertexAttrib(String name, int size, int bytes, int location, Buffer.BufferDataType dataType) {
        this.name = name;
        this.size = size;
        this.bytes = bytes;
        this.location = location;
        this.dataType = dataType;
    }

    public VertexAttrib() {
        this(null, 0, 0, 0, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Buffer.BufferDataType getDataType() {
        return dataType;
    }

    public void setDataType(Buffer.BufferDataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexAttrib that = (VertexAttrib) o;
        return size == that.size && bytes == that.bytes && location == that.location && Objects.equals(name, that.name) && dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, bytes, location, dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(VertexAttrib o) {
        return location - o.location;
    }

    @Override
    public String toString() {
        return "VertexAttrib[Name=" + name + ", Location=" + location +
                ", Size=" + size + ", Bytes=" + bytes + ", Datatype=" + dataType + "]";
    }
}
