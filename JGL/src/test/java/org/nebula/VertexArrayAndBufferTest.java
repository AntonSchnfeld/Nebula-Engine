package org.nebula;

import org.joml.Vector2i;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL33C.*;

public class VertexArrayAndBufferTest {
    public static final String vertex = """
            #version 330 core
                        
            layout(location = 0) in vec2 vPos;
            layout(location = 1) in vec4 vCol;
                        
            out vec4 fCol;
                        
            void main() {
                fCol = vCol;
                gl_Position = vec4(vPos, 0.0, 1.0);
            }
            """;
    public static final String fragment = """
            #version 330 core
                        
            in vec4 fCol;
                        
            out vec4 pixelCol;
                        
            void main() {
                pixelCol = fCol;
            }
            """;

    private final VertexArray vertexArray;
    private final Buffer buffer;
    private final GLFWWindow window;
    private final Shader shader;
    private final float[] vertices;

    public VertexArrayAndBufferTest() {
        this.window = new GLFWWindow(getClass().getName());
        window.createGLCapabilities();

        this.shader = new Shader(vertex, fragment);

        this.vertexArray = new VertexArray();
        this.buffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);

        vertices = new float[]{
                -0.5f, -0.5f, 1, 0, 0, 1,
                0.5f, -0.5f, 0, 1, 0, 1,
                0, 0.5f, 0, 0, 1, 1
        };

        vertexArray.bind();
        buffer.data(vertices, Buffer.BufferUsage.STATIC_DRAW);
        vertexArray.vertexAttribPointer(0, 2, Buffer.BufferDataType.FLOAT, 6 * Float.BYTES, 0);
        vertexArray.vertexAttribPointer(1, 4, Buffer.BufferDataType.FLOAT, 6 * Float.BYTES, 2 * Float.BYTES);

        window.setRenderer(this::drawTriangle);
        shader.bind();
        window.loop();

        vertexArray.dispose();
        buffer.dispose();
        window.dispose();
        shader.dispose();
    }

    public static void main(String[] args) {
        new VertexArrayAndBufferTest();
    }

    private void drawTriangle() {
        Vector2i winSize = window.getSize();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, (int) winSize.x, (int) winSize.y);
        glClearColor(0, 0, 0, 1);

        vertexArray.bind();
        glDrawArrays(GL_TRIANGLES, 0, 3);
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
    }
}
