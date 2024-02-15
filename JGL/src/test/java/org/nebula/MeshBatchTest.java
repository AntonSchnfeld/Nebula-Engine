package org.nebula;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.MeshBatch;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jglfw.GLFWWindow;
import org.nebula.jglfw.listeners.IGLFWInputListener;
import org.nebula.math.Maths;
import org.nebula.math.Transform;

public class MeshBatchTest {
    private final MeshBatch meshBatch;
    private final Mesh mesh;
    private final Camera camera;

    public MeshBatchTest() {
        final float size = 2;
        camera = new Camera(new Vector2f(0, 0), size, -size, size, -size, size, -size);
        GLFWWindow window = new GLFWWindow(getClass().getName());
        window.createGLCapabilities();
        window.setWindowIcon(Files.readImageFromResource("images/nebula.png"));

        Shader shader = new Shader(Files.readResourceAsString("shaders/meshbatch/meshbatch.vert"),
                Files.readResourceAsString("shaders/meshbatch/meshbatch.frag"));
        meshBatch = new MeshBatch();
        final float[] vertices = {
                -0.75f, -0.75f, 1, 0, 0, 1, // Lower left
                0.75f, -0.75f, 0, 0, 1, 1, // Lower right
                -0.75f, 0.75f, 0, 1, 0, 1, // Upper left
                0.75f, 0.75f, 1, 0, 1, 1, // Upper right
        };
        final int[] indices = {
                0, 1, 2,
                1, 3, 2
        };
        mesh = new Mesh(vertices, indices);

        meshBatch.setShader(shader);

        window.addInputListener(new IGLFWInputListener() {
            @Override
            public void onCursorPositionChange(GLFWWindow window, double x, double y) {

            }
            @Override
            public void onKeyAction(GLFWWindow window, int key, int scanCode, int action, int mods) {
                if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_R)
                    System.out.println("R clicked");
            }
            @Override
            public void onMouseButtonAction(GLFWWindow window, int button, int action, int mods) {

            }
        });

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
        final Transform transform = mesh.getTransform();
        transform.setRotation(transform.getRotation() + 1f);
        transform.getScale().set(Math.sin(GLFW.glfwGetTime()), Math.sin(GLFW.glfwGetTime()));
        transform.getTranslation().set(Math.sin(GLFW.glfwGetTime()), Math.sin(GLFW.glfwGetTime()));
        for (int i = 0; i < 1_000_000; i++)
            meshBatch.mesh(mesh);
        meshBatch.end();
    }
}
