package org.nebula.jgl.batch;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.system.MemoryUtil;
import org.nebula.jgl.JGL;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.shader.VertexAttribs;
import org.nebula.math.Transform;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;

public class MeshBatch extends Batch {
    private static final int TRANSFORM_SIZE = 5;
    private final List<Mesh> meshes;
    private final VertexArray vertexArray;
    private final Buffer buffer, elementBuffer;
    private VertexAttribs vertexAttribs;
    private Shader instanceShader;

    public MeshBatch() {
        super();
        this.meshes = new ArrayList<>();
        this.vertexArray = new VertexArray();
        this.buffer = new Buffer(Buffer.Type.ARRAY_BUFFER);
        this.elementBuffer = new Buffer(Buffer.Type.ELEMENT_ARRAY_BUFFER);
    }

    @Override
    public void setShader(Shader shader) {
        super.setShader(shader);
        this.vertexAttribs = shader.getVertexAttribs();
        buffer.bind();
        vertexAttribs.configure(vertexArray);
    }

    @Override
    public void begin() {
        super.begin();

        meshes.clear();
    }

    @Override
    public void flush() {

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        final int totalVerticesSize = calculateTotalVerticesSize();
        FloatBuffer vertices = getBatchVertices(totalVerticesSize);
        IntBuffer indices = getBatchIndices(totalVerticesSize);

        buffer.data(vertices, Buffer.Usage.STATIC_DRAW);
        elementBuffer.data(indices, Buffer.Usage.STATIC_DRAW);
        vertexArray.bind();
        elementBuffer.bind();
        final int totalVertexCount = totalVerticesSize / vertexAttribs.getVertexSize();
        shader.bind();
        shader.uploadUniformMat4f("uView", viewMatrix);
        shader.uploadUniformMat4f("uProjection", projectionMatrix);
        glDrawElements(GL_TRIANGLES, totalVerticesSize, GL_UNSIGNED_INT, 0);
        JGL.checkForOpenGLError();

        MemoryUtil.memFree(vertices);
        MemoryUtil.memFree(indices);
    }

    @Override
    public void dispose() {
        buffer.dispose();
        vertexArray.dispose();
    }

    /**
     * Returns the concatenated vertices of all stored Meshes in one native FloatBuffer. The transform values
     * are inserted after each vertex. This assumes that the shader accepts these values.
     *
     * @param len the result of {@link MeshBatch#calculateTotalVerticesSize()}
     * @return a native FloatBuffer containing the concatenated vertices of all stored meshes
     */
    private FloatBuffer getBatchVertices(final int len) {
        FloatBuffer meshVertices = MemoryUtil.memAllocFloat(len);
        for (Mesh mesh : meshes) {
            final FloatBuffer vertices = mesh.getVertices();
            final int count = vertices.limit() / (vertexAttribs.getVertexSize() - TRANSFORM_SIZE);
            final Transform transform = mesh.getTransform();

            // Add all the vertices of the current mesh into the native concatenated FloatBuffer
            // and insert the transform values after each vertex.
            // Vertex layout: {vertex data as defined by the shader}, translation, scale, rotation
            for (int i = 0; i < count; i++) {

                for (int j = 0; j < vertexAttribs.getVertexSize() - TRANSFORM_SIZE; j++) {
                    meshVertices.put(vertices.get());
                }

                meshVertices.put(transform.getTranslation().x);
                meshVertices.put(transform.getTranslation().y);
                meshVertices.put(transform.getScale().x);
                meshVertices.put(transform.getScale().y);
                meshVertices.put(transform.getRotation());
            }
        }

        return meshVertices.flip();
    }

    private IntBuffer getBatchIndices(final int len) {
        IntBuffer batchIndices = MemoryUtil.memAllocInt(len);
        int offset = 0; // Keep track of the offset for each mesh

        for (Mesh mesh : meshes) {
            IntBuffer indices = mesh.getIndices();

            // Iterate through the indices and add them to the batchIndices with offset
            while (indices.hasRemaining()) {
                int index = indices.get() + offset;
                batchIndices.put(index);
            }

            // Update the offset for the next mesh
            offset += indices.capacity(); // Assuming indices.capacity() is equivalent to the vertex count
        }

        return batchIndices.flip();
    }

    /**
     * Calculates the total size of the vertices of all currently stored Meshes. In other words,
     * the number of floats stored in each vertex in each mesh is combined.
     *
     * @return the total size of the vertices
     */
    private int calculateTotalVerticesSize() {
        int len = 0;
        for (Mesh mesh : meshes) {
            // Vertex buffer without transform values
            final FloatBuffer buffer = mesh.getVertices();
            // Calculate count of vertices in the current Mesh
            final int count = buffer.limit() / (vertexAttribs.getVertexSize() - TRANSFORM_SIZE);
            // Calculate the amount of floats in the current mesh
            len += count * vertexAttribs.getVertexSize();
        }
        return len;
    }

    public void mesh(Mesh mesh) {
        meshes.add(mesh);
    }

    public void meshInstanced(Mesh mesh, Transform[] transforms, int instances) {

    }

    public void meshInstanced(Mesh mesh, Transform[] transforms) {
        meshInstanced(mesh, transforms, transforms.length);
    }
}
