package org.nebula;

import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.Batch;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.data.FrameBuffer;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.shader.Shader;
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
    private Mesh mesh;

    public FrameBufferTest() {
        this.window = new GLFWWindow(getClass().getName(), 500, 500);
        window.createGLCapabilities();
        window.setResizable(false);
        window.setWindowIcon(Files.readImageFromResource("nebula.png"));

        camera = new Camera();


        float[] vertices = new float[]{
                //Pos       Color           Uvs
                -1, -1, 1, 1, 1, 1, 0, 0, // Lower left
                -1, 1, 1, 1, 1, 1, 0, 1, // Upper left
                1, 1, 1, 1, 1, 1, 1, 1, // Upper right

                1, 1, 1, 1, 1, 1, 1, 1, // Upper right
                1, -1, 1, 1, 1, 1, 1, 0, // Lower right
                -1, -1, 1, 1, 1, 1, 0, 0  // Lower left
        };

        texture = new TextureRegion(new Texture(
                Files.readImageFromResource("nebula.png"), true));
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

    public static void main(String[] args) {
        new FrameBufferTest();
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
        postProcessingShader.uploadUniformInt("uScreen", 0);
        frameBuffer.getTexture().bindToSlot(0);

        postProcessingShader.unbind();
    }

    private void dispose() {
        frameBuffer.dispose();
        texture.getTexture().dispose();
        postProcessingShader.dispose();
        renderingShader.dispose();
        batch.dispose();
        window.dispose();
        mesh.dispose();
    }
}
