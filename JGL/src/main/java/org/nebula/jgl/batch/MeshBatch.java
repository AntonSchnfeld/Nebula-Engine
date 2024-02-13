package org.nebula.jgl.batch;

import org.lwjgl.system.MemoryUtil;
import org.nebula.jgl.JGL;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.shader.VertexAttribs;
import org.nebula.math.Transform;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;

public class MeshBatch extends Batch {
    private final List<Mesh> meshes;
    private final VertexArray vertexArray;
    private final Buffer buffer;

    public MeshBatch() {
        super();
        this.meshes = new ArrayList<>();
        this.vertexArray = new VertexArray();
        this.buffer = new Buffer(Buffer.Type.ARRAY_BUFFER);
    }

    @Override
    public void setShader(Shader shader) {
        super.setShader(shader);
        buffer.bind();
        shader.getVertexAttribs().configure(vertexArray);
    }

    @Override
    public void begin() {
        super.begin();

        meshes.clear();
    }

    @Override
    public void flush() {
        final int len = calculateTotalVerticesSize();
        FloatBuffer vertices = getBatchVertices(len);

        buffer.data(vertices, Buffer.Usage.STATIC_DRAW);
        vertexArray.bind();
        final int count = len / shader.getVertexAttribs().getVertexSize();
        shader.bind();
        shader.uploadUniformMat4f("uView", viewMatrix);
        shader.uploadUniformMat4f("uProjection", projectionMatrix);
        System.out.println(count);
        glDrawArrays(GL_TRIANGLES, 0, count);
        JGL.checkForOpenGLError();

        MemoryUtil.memFree(vertices);
    }

    @Override
    public void dispose() {
        buffer.dispose();
        vertexArray.dispose();
    }

    private FloatBuffer getBatchVertices(final int len) {
        FloatBuffer meshVertices = MemoryUtil.memAllocFloat(len);
        int index = 0;
        for (Mesh mesh : meshes) {
            FloatBuffer vertices = mesh.getVertices();
            final int limit = vertices.limit();
            meshVertices.put(index, vertices, 0, limit);
            index += len;
            final Transform transform = mesh.getTransform();
            meshVertices.put(transform.getTranslation().x);
            meshVertices.put(transform.getTranslation().y);
            meshVertices.put(transform.getScale().x);
            meshVertices.put(transform.getScale().y);
            meshVertices.put(transform.getRotation());
            index += 5;
        }

        return meshVertices;
    }

    private int calculateTotalVerticesSize() {
        int len = 0;
        VertexAttribs attribs = shader.getVertexAttribs();
        for (Mesh mesh : meshes) {
            final FloatBuffer buffer = mesh.getVertices();
            final int count = buffer.limit() / attribs.getVertexSize();
            len += count * 5 + buffer.limit();
        }
        return len;
    }

    public void mesh(Mesh mesh) {
        meshes.add(mesh);
    }
}
