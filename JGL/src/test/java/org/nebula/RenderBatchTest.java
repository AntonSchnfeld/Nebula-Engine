package org.nebula;

import org.joml.Vector2f;
import org.nebula.io.ByteBufferedImage;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;
import org.nebula.jglfw.GLFWWindow;

import java.io.File;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33C.*;

public class RenderBatchTest
{
    private final RenderBatch batch;
    private final GLFWWindow window;
    private final Camera camera;

    private final TextureRegion texture, triangleTexture;

    public RenderBatchTest() {
        final float size = 1;
        camera = new Camera(new Vector2f(0, 0), -size, size, -size, size, -size, size);
        window = new GLFWWindow(getClass().getName());
        window.setRenderer(this::draw);
        window.createGLCapabilities();

        this.batch = new RenderBatch();
        Shader shader = new Shader(Files.readResourceAsString("default.vert"),
                Files.readResourceAsString("default.frag"));
        batch.setShader(shader);

        window.setWindowIcon(Files.readImageFromResource("nebula.png"));
        Texture nebula = new Texture(Files.readImageFromResource("nebula.png"));
        texture = new TextureRegion(nebula);
        triangleTexture = new TextureRegion(nebula, new float[] {0, 0, 1, 0, 0.5f, 1});


        window.loop();
        window.dispose();
        batch.dispose();
        texture.getTexture().dispose();
    }

    private void draw() {
        Vector2f winSize = window.getSize();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, (int) winSize.x, (int) winSize.y);
        glClearColor(0, 0, 0, 1);

        batch.setViewMatrix(camera.getView());
        batch.setProjectionMatrix(camera.getProjection());
        batch.begin();
        batch.texture(texture, -0.75f, -0.75f, 1.5f, 1.5f);
        batch.texturedTriangle(triangleTexture, -0.5f, -0.5f, 0.5f, -0.5f, 0, 0.5f);
        batch.end();
    }

    public static void main(String[] args) {
        new RenderBatchTest();
    }
}
