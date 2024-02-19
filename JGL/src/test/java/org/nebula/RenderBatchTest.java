package org.nebula;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.nebula.io.Files;
import org.nebula.jgl.camera.Camera;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.camera.OrthographicCamera;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jgl.data.texture.TextureRegion;
import org.nebula.jglfw.GLFWWindow;
import org.nebula.math.Maths;
import org.nebula.math.Transform;

public class RenderBatchTest {
    private final RenderBatch batch;
    private final GLFWWindow window;
    private final OrthographicCamera camera;
    private final TextureRegion texture, triangleTexture;
    private Vector2i windowSize;
    private Transform transform;

    public RenderBatchTest() {
        final float size = 1;
        camera = new OrthographicCamera(new Vector3f(), -size, size, -size, size, -size, size);
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

        transform = new Transform(new Vector3f(), new Vector3f(1), new Vector2f());

        window.loop();
        window.dispose();
        batch.dispose();
        texture.getTexture().dispose();
    }

    public static void main(String[] args) {
        new RenderBatchTest();
    }

    private void draw() {
        batch.setViewMatrix(camera.getView());
        batch.setProjectionMatrix(camera.getProjection());

        batch.begin();
        transform.setRotation(transform.getRotation().set(transform.getRotation().x + 0.1f, transform.getRotation().y));
        batch.setColor(1, 1, 1, 0.5f);
        batch.texture(texture, -0.75f, -0.75f, 1.5f, 1.5f);
        batch.setColor(Color.WHITE);
        Vector3f lowerLeft = Maths.transform(new Vector3f(-0.75f, -0.75f, 0), transform);
        Vector3f lowerRight = Maths.transform(new Vector3f(0.75f, -0.75f, 0), transform);
        Vector3f middle = Maths.transform(new Vector3f(0, 0.75f, 0), transform);
        batch.texturedTriangle(triangleTexture,
                new Vector2f(lowerLeft.x, lowerLeft.y),
                new Vector2f(lowerRight.x, lowerRight.y),
                new Vector2f(middle.x, middle.y));
        batch.end();
    }
}
