package org.nebula.jgl.data.buffer;

import org.lwjgl.system.MemoryUtil;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.math.Transform;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.stream.Stream;

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
    private IntBuffer indices;
    private final Transform transform;

    /**
     * Constructs a Mesh object with the specified size for the vertex buffer.
     *
     * @param size The size of the vertex buffer.
     */
    public Mesh(int size) {
        vertices = MemoryUtil.memAllocFloat(size);
        indices = MemoryUtil.memAllocInt(size);
        transform = new Transform();
    }

    /**
     * Constructs a Mesh object with the given array of vertices.
     *
     * @param vertices The array of vertices representing the mesh.
     */
    public Mesh(float[] vertices) {
        this.vertices = MemoryUtil.memAllocFloat(vertices.length);
        this.indices = MemoryUtil.memAllocInt(vertices.length);
        this.vertices.put(0, vertices);
        initIndices();
        transform = new Transform();
    }

    /**
     * Constructs a Mesh object with the given arrays of vertices and indices.
     *
     * @param vertices The array of vertices representing the mesh.
     * @param indices The array of indices representing the mesh.
     */
    public Mesh(float[] vertices, int[] indices) {
        this.vertices = MemoryUtil.memAllocFloat(vertices.length);
        this.indices = MemoryUtil.memAllocInt(indices.length);
        this.vertices.put(0, vertices);
        this.indices.put(0, indices);
        transform = new Transform();
    }

    /**
     * Constructs a Mesh object with the specified FloatBuffer of vertices.
     *
     * @param vertices The FloatBuffer containing the vertices of the mesh.
     * @throws IllegalArgumentException If an indirect FloatBuffer of vertices is provided.
     */
    public Mesh(FloatBuffer vertices) {
        validateBuffer(vertices, "Tried to use indirect FloatBuffer as vertices for Mesh");
        this.vertices = vertices;
        this.indices = MemoryUtil.memAllocInt(vertices.limit());
        initIndices();
        transform = new Transform();
    }

    /**
     * Constructs a Mesh object with the specified FloatBuffer of vertices and IntBuffer of indices.
     *
     * @param vertices The FloatBuffer containing the vertices of the mesh.
     * @param indices The IntBuffer containing the indices of the mesh.
     * @throws IllegalArgumentException If an indirect FloatBuffer or IntBuffer is provided.
     */
    public Mesh(FloatBuffer vertices, IntBuffer indices) {
        validateBuffer(vertices, "Tried to use indirect FloatBuffer as vertices for Mesh");
        validateBuffer(indices, "Tried to use indirect IntBuffer as indices for Mesh");
        this.vertices = vertices;
        this.indices = indices;
        transform = new Transform();
    }

    /**
     * Initializes the indices of the mesh. The indices will have sequential values starting from 0.
     */
    private void initIndices() {
        for (int i = 0; i < indices.limit(); i++)
            indices.put(i, i);
    }

    /**
     * Validates if the provided buffer is direct (non-indirect).
     *
     * @param buffer The Buffer to validate.
     * @param errorMessage The error message to throw if the buffer is indirect.
     * @throws IllegalArgumentException If the buffer is not direct.
     */
    private void validateBuffer(Buffer buffer, String errorMessage) {
        if (!buffer.isDirect())
            throw new IllegalArgumentException(errorMessage);
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
     * Gets a read-only view of the mesh's indices as an IntBuffer.
     *
     * @return A read-only IntBuffer containing the indices of the mesh.
     */
    public IntBuffer getIndices() {
        return indices.asReadOnlyBuffer();
    }

    /**
     * Sets the vertices of the mesh using the provided FloatBuffer.
     *
     * @param vertices The new FloatBuffer containing the vertices of the mesh.
     * @throws IllegalArgumentException If an indirect FloatBuffer is provided.
     */
    public void setVertices(FloatBuffer vertices) {
        if (vertices == this.vertices) return;
        validateBuffer(vertices, "Tried to use indirect FloatBuffer as vertices for Mesh");
        MemoryUtil.memFree(this.vertices);
        this.vertices = vertices;
        MemoryUtil.memFree(indices);
        indices = MemoryUtil.memAllocInt(vertices.limit());
        initIndices();
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
     * Sets the indices of the mesh using the provided IntBuffer.
     *
     * @param indices The new IntBuffer containing the indices of the mesh.
     * @throws IllegalArgumentException If an indirect IntBuffer is provided.
     */
    public void setIndices(IntBuffer indices) {
        if (indices == this.indices) return;
        validateBuffer(indices, "Tried to use indirect IntBuffer as indices for Mesh");
        MemoryUtil.memFree(this.indices);
        this.indices = indices;
    }

    /**
     * Sets the indices of the mesh using the provided array of integers.
     *
     * @param indices The new array of integers representing the indices of the mesh.
     */
    public void setIndices(int[] indices) {
        IntBuffer buffer = MemoryUtil.memAllocInt(indices.length);
        buffer.put(0, indices);
        setIndices(buffer);
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
        if (!indices.equals(mesh.indices)) return false;
        return transform.equals(mesh.transform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + indices.hashCode();
        result = 31 * result + transform.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "vertices=" + vertices +
                ", indices=" + indices +
                ", transform=" + transform +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        MemoryUtil.memFree(vertices);
        MemoryUtil.memFree(indices);
    }
}
