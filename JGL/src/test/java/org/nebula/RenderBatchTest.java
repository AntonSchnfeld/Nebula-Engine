package org.nebula;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;
import org.nebula.jglfw.GLFWWindow;
import org.nebula.math.Maths;
import org.nebula.math.Transform;

import static org.lwjgl.opengl.GL33C.*;

public class RenderBatchTest {
    private final RenderBatch batch;
    private final GLFWWindow window;
    private final Camera camera;
    private final TextureRegion texture, triangleTexture;
    private Vector2i windowSize;
    private Transform transform;

    public RenderBatchTest() {
        final float size = 1;
        camera = new Camera(new Vector2f(0, 0), -size, size, -size, size, -size, size);
        window = new GLFWWindow(getClass().getName());
        window.setRenderer(this::draw);
        window.createGLCapabilities();

        this.batch = new RenderBatch();
        Shader shader = new Shader(Files.readResourceAsString("shaders/default/default.vert"),
                Files.readResourceAsString("shaders/default/default.frag"));
        batch.setShader(shader);

        windowSize = new Vector2i();

        window.setWindowIcon(Files.readImageFromResource("images/nebula.png"));
        Texture nebula = new Texture(Files.readImageFromResource("images/nebula.png"), true);
        texture = new TextureRegion(nebula);
        triangleTexture = new TextureRegion(nebula, new float[]{0, 0, 1, 0, 0.5f, 1});

        transform = new Transform(new Vector2f(0, 0), new Vector2f(1, 1), 0);

        window.loop();
        window.dispose();
        batch.dispose();
        texture.getTexture().dispose();
    }

    public static void main(String[] args) {
        new RenderBatchTest();
    }

    private void draw() {
        windowSize = window.getSize(windowSize);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, windowSize.x, windowSize.y);
        glClearColor(0, 0, 0, 1);

        batch.setViewMatrix(camera.getView());
        batch.setProjectionMatrix(camera.getProjection());
        batch.begin();
        transform.setRotation(transform.getRotation() + 0.25f);
        batch.setColor(1, 1, 1, 0.5f);
        batch.texture(texture, -0.75f, -0.75f, 1.5f, 1.5f);
        batch.setColor(Color.WHITE);
        batch.texturedTriangle(triangleTexture,
                Maths.transform(new Vector2f(-0.5f, -0.5f), transform),
                Maths.transform(new Vector2f(0.5f, -0.5f), transform),
                Maths.transform(new Vector2f(0, 0.5f), transform));
        batch.end();
    }
}
