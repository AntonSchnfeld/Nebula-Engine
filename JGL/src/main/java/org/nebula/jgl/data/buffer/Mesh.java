package org.nebula.jgl.data.buffer;

import org.lwjgl.system.MemoryUtil;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.jgl.JGL;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.shader.VertexAttribs;
import org.nebula.math.Transform;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33C.glDrawArrays;

public class Mesh implements IDisposable {

    private FloatBuffer vertices;
    private Transform transform;

    public Mesh(int size) {
        vertices = MemoryUtil.memAllocFloat(size);
        transform = new Transform();
    }

    public Mesh(float[] vertices) {
        this.vertices = MemoryUtil.memAllocFloat(vertices.length);
        this.vertices.put(0, vertices);
        transform = new Transform();
    }

    public Mesh(FloatBuffer vertices) {
        final int capacity = vertices.capacity();
        this.vertices = MemoryUtil.memAllocFloat(capacity);
        this.vertices.put(0, vertices, 0, capacity);
        transform = new Transform();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public FloatBuffer getVertices() {
        return vertices;
    }

    public void setVertices(FloatBuffer vertices) {
        if (vertices == this.vertices) return;
        if (!vertices.isDirect())
            throw new IllegalArgumentException("Tried to use indirect FloatBuffer as vertices for mesh");
        MemoryUtil.memFree(vertices);
        this.vertices = vertices;
    }

    public void setVertices(float[] vertices) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(0, vertices);
        setVertices(buffer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mesh mesh = (Mesh) o;

        if (!vertices.equals(mesh.vertices)) return false;
        return transform.equals(mesh.transform);
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + transform.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "vertices=" + vertices +
                ", transform=" + transform +
                '}';
    }

    @Override
    public void dispose() {
        MemoryUtil.memFree(vertices);
    }
}