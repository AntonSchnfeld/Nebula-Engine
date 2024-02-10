package org.nebula;

import org.joml.Vector2i;
import org.nebula.jgl.JGL;
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
    private final GLFWWindow window;
    private final Shader shader;

    public VertexArrayAndBufferTest() {
        this.window = new GLFWWindow(getClass().getName());
        window.createGLCapabilities();

        this.shader = new Shader(vertex, fragment);

        this.vertexArray = new VertexArray();
        Buffer buffer = new Buffer(Buffer.Type.ARRAY_BUFFER);

        float[] vertices = new float[]{
                -0.5f, -0.5f, 1, 1, 1, Float.MIN_NORMAL,
                0.5f, -0.5f, 1, 1, 1, Float.MIN_NORMAL,
                0, 0.5f, 1, 1, 1, 1
        };

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        vertexArray.bind();
        buffer.data(vertices, Buffer.Usage.STATIC_DRAW);
        shader.getVertexAttribs().configure(vertexArray);

        window.setRenderer(this::drawTriangle);
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

        shader.bind();
        shader.getVertexAttribs().enable(vertexArray);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        JGL.checkForOpenGLError();
        shader.getVertexAttribs().disable(vertexArray);
        shader.unbind();
    }
}
