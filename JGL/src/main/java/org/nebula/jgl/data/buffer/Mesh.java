package org.nebula.jgl.data.buffer;

import org.lwjgl.system.MemoryUtil;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.math.Transform;

import java.nio.FloatBuffer;

/**
 * <br>
 * <h2>Mesh</h2>
 * <br>
 * The Mesh class represents a 3D mesh composed of vertices. It is designed to be used in conjunction
 * with the JGL (Java Graphics Library) for handling OpenGL-related functionalities in Nebula Engine
 * applications. The class provides methods to create and manipulate a mesh by specifying its vertices
 * and transformation. The mesh can be rendered using OpenGL's glDrawArrays method.
 *
 * @author Anton Schoenfeld
 */
public class Mesh implements IDisposable {

    private FloatBuffer vertices;
    private final Transform transform;

    /**
     * Constructs a Mesh object with the specified size for the vertex buffer.
     *
     * @param size The size of the vertex buffer.
     */
    public Mesh(int size) {
        vertices = MemoryUtil.memAllocFloat(size);
        transform = new Transform();
    }

    /**
     * Constructs a Mesh object with the given array of vertices.
     *
     * @param vertices The array of vertices representing the mesh.
     */
    public Mesh(float[] vertices) {
        this.vertices = MemoryUtil.memAllocFloat(vertices.length);
        this.vertices.put(0, vertices);
        transform = new Transform();
    }

    /**
     * Constructs a Mesh object with the specified FloatBuffer of vertices.
     *
     * @param vertices The FloatBuffer containing the vertices of the mesh.
     */
    public Mesh(FloatBuffer vertices) {
        if (!vertices.isDirect())
            throw new IllegalArgumentException("Tried to use indirect FloatBuffer as vertices for mesh");
        this.vertices = vertices;
        transform = new Transform();
    }

    /**
     * Gets the transformation applied to the mesh.
     *
     * @return The transformation object associated with the mesh.
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Gets a read-only view of the mesh's vertices as a FloatBuffer.
     *
     * @return A read-only FloatBuffer containing the vertices of the mesh.
     */
    public FloatBuffer getVertices() {
        return vertices.asReadOnlyBuffer();
    }

    /**
     * Sets the vertices of the mesh using the provided FloatBuffer.
     *
     * @param vertices The new FloatBuffer containing the vertices of the mesh.
     * @throws IllegalArgumentException If an indirect FloatBuffer is provided.
     */
    public void setVertices(FloatBuffer vertices) {
        if (vertices == this.vertices) return;
        if (!vertices.isDirect())
            throw new IllegalArgumentException("Tried to use indirect FloatBuffer as vertices for mesh");
        MemoryUtil.memFree(this.vertices);
        this.vertices = vertices;
    }

    /**
     * Sets the vertices of the mesh using the provided array of floats.
     *
     * @param vertices The new array of floats representing the vertices of the mesh.
     */
    public void setVertices(float[] vertices) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(0, vertices);
        setVertices(buffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mesh mesh = (Mesh) o;

        if (!vertices.equals(mesh.vertices)) return false;
        return transform.equals(mesh.transform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + transform.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Mesh{" +
                "vertices=" + vertices +
                ", transform=" + transform +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        MemoryUtil.memFree(vertices);
    }
}
