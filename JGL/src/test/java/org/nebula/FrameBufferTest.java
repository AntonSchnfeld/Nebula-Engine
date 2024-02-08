package org.nebula;

import org.joml.Vector2i;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.JGL;
import org.nebula.jgl.batch.Batch;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.data.FrameBuffer;
import org.nebula.jgl.data.Shader;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL33C.*;

public class FrameBufferTest {
    private final GLFWWindow window;
    private final Batch batch;
    private final FrameBuffer frameBuffer;
    private final Shader renderingShader, postProcessingShader;
    private final Camera camera;
    private final TextureRegion texture;
    private final VertexArray postProcessVAO;
    private final Buffer postProcessBuffer, postProcessEBO;

    public FrameBufferTest() {
        this.window = new GLFWWindow(getClass().getName(), 500, 500);
        window.createGLCapabilities();
        window.setResizable(false);
        window.setWindowIcon(Files.readImageFromResource("nebula.png"));

        camera = new Camera();

        postProcessBuffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);
        postProcessEBO = new Buffer(Buffer.BufferType.ELEMENT_ARRAY_BUFFER);
        postProcessVAO = new VertexArray();

        postProcessVAO.bind();
        postProcessBuffer.bind();
        postProcessEBO.unbind();
        postProcessVAO.vertexAttribPointer(0, 2, Buffer.BufferDataType.FLOAT, 0, 8 * Float.BYTES);
        postProcessVAO.vertexAttribPointer(1, 2, Buffer.BufferDataType.UNSIGNED_INT, 2 * Float.BYTES, 8 * Float.BYTES);
        postProcessVAO.vertexAttribPointer(2, 4, Buffer.BufferDataType.FLOAT, 4 * Float.BYTES, 8 * Float.BYTES);
        postProcessVAO.disableVertexAttribArray(0);
        postProcessVAO.disableVertexAttribArray(1);
        postProcessVAO.disableVertexAttribArray(2);
        postProcessBuffer.data(new float[]{
                -1, -1,     0, 0,    1, 1, 1, 1, // Lower left
                -1, 1,      0, 1,     1, 1, 1, 1, // Upper left
                1, 1,       1, 1,      1, 1, 1, 1, // Upper right
                1, -1,      1, 0,     1, 1, 1, 1  // Lower right

        }, Buffer.BufferUsage.STATIC_DRAW);
        postProcessEBO.data(new int[]{
                3, 2, 0,
                0, 2, 1
        }, Buffer.BufferUsage.STATIC_DRAW);

        texture = new TextureRegion(new Texture(Files.readImageFromResource("nebula.png"), true));
        batch = new RenderBatch();
        frameBuffer = new FrameBuffer(500, 500);
        renderingShader = new Shader(Files.readResourceAsString("default.vert"),
                Files.readResourceAsString("default.frag"));
        postProcessingShader = new Shader(Files.readResourceAsString("postprocess.vert"),
                Files.readResourceAsString("postprocess.frag"));

        window.setRenderer(this::draw);
        window.loop();
        dispose();
    }

    private void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, 500, 500);
        glClearColor(0, 0, 0, 1);

        batch.setProjectionMatrix(camera.getProjection());
        batch.setViewMatrix(camera.getView());

        // Render Texture to frame buffer
        frameBuffer.bind();
        batch.setShader(renderingShader);
        batch.begin();
        batch.texture(texture, -0.75f, -0.75f, 1.5f, 1.5f);
        batch.end();
        frameBuffer.unbind();

        // Apply post-processing to frame buffer texture
        postProcessingShader.bind();
        postProcessingShader.uploadUniformInt("uScreen", 0);
        frameBuffer.getTexture().bindToSlot(0);
        postProcessVAO.enableVertexAttributeArray(0);
        postProcessVAO.enableVertexAttributeArray(1);
        postProcessVAO.enableVertexAttributeArray(2);
        postProcessEBO.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        JGL.checkForOpenGLError();
        postProcessEBO.unbind();
        postProcessVAO.disableVertexAttribArray(0);
        postProcessVAO.disableVertexAttribArray(1);
        postProcessVAO.disableVertexAttribArray(2);
        postProcessingShader.unbind();
    }

    private void dispose() {
        frameBuffer.dispose();
        texture.getTexture().dispose();
        postProcessingShader.dispose();
        renderingShader.dispose();
        batch.dispose();
        window.dispose();
        postProcessBuffer.dispose();
        postProcessEBO.dispose();
        postProcessVAO.dispose();
    }

    public static void main(String[] args) {
        new FrameBufferTest();
    }
}
