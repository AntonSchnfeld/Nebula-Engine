package org.nebula;

import org.joml.Vector2i;
import org.nebula.io.Files;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.Mesh;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL11C.*;

public class MeshTest {
    private Mesh mesh;
    private Shader shader;
    private GLFWWindow window;
    private Vector2i windowSize;

    public MeshTest() {
        window = new GLFWWindow(getClass().getName());
        window.setWindowIcon(Files.readImageFromResource("nebula.png"));
        window.createGLCapabilities();
        windowSize = new Vector2i();

        shader = new Shader(Files.readResourceAsString("triangles.vert"),
                Files.readResourceAsString("triangles.frag"));
        float[] vertices = {
                -0.75f, -0.75f, 1, 1, 1, 1,
                0.75f, -0.75f, 1, 1, 1, 1,
                0, 0.75f, 1, 1, 1, 1
        };
        mesh = new Mesh(shader, vertices.length, Buffer.BufferUsage.STATIC_DRAW);
        mesh.addVertices(vertices);

        window.setRenderer(this::drawTriangle);
        window.loop();

        window.dispose();
        mesh.dispose();
        shader.dispose();
    }

    public static void main(String[] args) {
        new MeshTest();
    }

    private void drawTriangle() {
        window.getSize(windowSize);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(windowSize.x, windowSize.y, 500, 500);
        glClearColor(0, 0, 0, 1);

        mesh.draw(GL_TRIANGLES);
    }
}
