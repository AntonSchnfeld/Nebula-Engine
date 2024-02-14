package org.nebula;

import org.joml.Vector2f;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.MeshBatch;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jglfw.GLFWWindow;
import org.nebula.math.Transform;

public class MeshBatchTest {
    private final MeshBatch meshBatch;
    private final Mesh mesh;
    private final Camera camera;

    public MeshBatchTest() {
        camera = new Camera();
        GLFWWindow window = new GLFWWindow(getClass().getName());
        window.createGLCapabilities();
        window.setWindowIcon(Files.readImageFromResource("images/nebula.png"));

        Shader shader = new Shader(Files.readResourceAsString("shaders/meshbatch/meshbatch.vert"),
                Files.readResourceAsString("shaders/meshbatch/meshbatch.frag"));
        meshBatch = new MeshBatch();
        final float[] vertices = {
                -0.75f, -0.75f, 1, 1, 1, 1, // Lower left
                -0.75f, 0.75f, 1, 1, 1, 1, // Upper left
                0.75f, 0.75f, 1, 1, 1, 1, // Upper right

                -0.75f, -0.75f, 1, 1, 1, 1, // Lower left
                0.75f, -0.75f, 1, 1, 1, 1, // Lower right
                0.75f, 0.75f, 1, 1, 1, 1, // Upper right
        };
        mesh = new Mesh(vertices);
        Transform transform = mesh.getTransform();
        transform.setRotation(0);
        transform.setScale(new Vector2f(1, 1));
        transform.setTranslation(new Vector2f(0, 0));

        meshBatch.setShader(shader);

        window.setRenderer(this::draw);
        window.loop();

        window.dispose();
        shader.dispose();
        mesh.dispose();
        meshBatch.dispose();
    }

    public static void main(String[] args) {
        new MeshBatchTest();
    }

    private void draw() {
        meshBatch.setViewMatrix(camera.getView());
        meshBatch.setProjectionMatrix(camera.getProjection());
        meshBatch.begin();
        meshBatch.mesh(mesh);
        meshBatch.end();
    }
}
