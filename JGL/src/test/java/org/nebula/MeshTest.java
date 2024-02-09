package org.nebula;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.texture.Texture;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL11C.*;

public class MeshTest {
    private Mesh mesh;
    private Shader shader;
    private GLFWWindow window;
    private Camera camera;
    private Texture texture;
    private Vector2i windowSize;

    public MeshTest() {
        window = new GLFWWindow(getClass().getName());
        window.setWindowIcon(Files.readImageFromResource("nebula.png"));
        window.createGLCapabilities();
        windowSize = new Vector2i();

        float size = 3;
        camera = new Camera(new Vector2f(0, 0), -size, size, -size, size, -size, size);

        shader = new Shader(Files.readResourceAsString("default.vert"),
                Files.readResourceAsString("default.frag"));
        texture = new Texture(Files.readImageFromResource("nebula.png"), true);
        float[] vertices = {
                -0.75f, -0.75f, 0,     1, 1, 1, 1,     0, 0,   0,
                0.75f, -0.75f, 0,      1, 1, 1, 1,     1, 0,   0,
                0, 0.75f, 0,           1, 1, 1, 1,     0.5f, 1,   0
        };
        mesh = new Mesh(shader, vertices.length, Buffer.BufferUsage.STATIC_DRAW);
        mesh.addVertices(vertices);

        window.setRenderer(this::drawTriangle);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        window.loop();

        window.dispose();
        mesh.dispose();
        shader.dispose();
        texture.dispose();
    }

    public static void main(String[] args) {
        new MeshTest();
    }

    private void drawTriangle() {
        window.getSize(windowSize);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, windowSize.x, windowSize.y);
        glClearColor(0, 0, 0, 1);

        shader.uploadUniformInt("uTextures", 0);
        texture.bindToSlot(0);

        shader.uploadUniformMat4f("uView", camera.getView());
        shader.uploadUniformMat4f("uProjection", camera.getProjection());
        mesh.draw(GL_TRIANGLES);
    }
}
