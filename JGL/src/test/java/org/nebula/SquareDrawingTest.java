package org.nebula;

import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL33C.*;

public class SquareDrawingTest {

    public static final String fragment = """
            #version 330 core

            in vec4 fCol;

            out vec4 FragColor;

            void main() {
                FragColor = fCol;
            }""";
    private static final String vertex = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            layout (location = 1) in vec4 aCol;

            out vec4 fCol;

            void main() {
                fCol = aCol;
                gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
            }""";
    private final GLFWWindow window;
    private final Shader shader;
    private final VertexArray VAO;
    private final Buffer EBO, VBO;

    public SquareDrawingTest() {
        window = new GLFWWindow("Hello Nebula!");
        window.createGLCapabilities();

        shader = new Shader(vertex, fragment);

        VAO = new VertexArray();
        EBO = new Buffer(Buffer.Type.ELEMENT_ARRAY_BUFFER);
        VBO = new Buffer(Buffer.Type.ARRAY_BUFFER);

        init();

        window.setRenderer(this::drawSquare);
        window.loop();

        dispose();
    }

    public static void main(String[] args) {
        new SquareDrawingTest();
    }

    private void init() {
        float[] vertices = {
                0.5f, 0.5f, 0.0f, 1, 0, 0, 1, // top right
                0.5f, -0.5f, 0.0f, 0, 1, 0, 1,  // bottom right
                -0.5f, -0.5f, 0.0f, 0, 0, 1, 1,  // bottom left
                -0.5f, 0.5f, 0.0f, 0.5f, 0, 0.5f, 1   // top left
        };
        int[] indices = {
                0, 1, 3,   // first triangle
                1, 2, 3    // second triangle
        };

        VAO.bind();

        VBO.data(vertices, Buffer.BufferUsage.STATIC_DRAW);

        EBO.data(indices, Buffer.BufferUsage.STATIC_DRAW);

        VAO.vertexAttribPointer(0, 3, Buffer.Datatype.FLOAT, Float.BYTES * 7, 0);
        VAO.enableVertexAttributeArray(0);
        VAO.vertexAttribPointer(1, 4, Buffer.Datatype.FLOAT, Float.BYTES * 7, Float.BYTES * 3);
        VAO.enableVertexAttributeArray(1);
    }

    private void dispose() {
        window.dispose();
        shader.dispose();
        VAO.dispose();
        EBO.dispose();
        VBO.dispose();
    }

    private void drawSquare() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, (int) window.getSize().x, (int) window.getSize().y);
        glClearColor(0, 0, 0, 1);
        shader.bind();

        VAO.bind();
        EBO.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
        VAO.unbind();
        EBO.unbind();
        shader.unbind();
    }
}
