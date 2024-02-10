package org.nebula;

import org.joml.Vector2i;
import org.nebula.jgl.Camera;
import org.nebula.jgl.data.buffer.Buffer;
import org.nebula.jgl.data.buffer.VertexArray;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL11C.*;

public class CameraTest {

    private static final String vertex = """
            #version 330 core

            uniform mat4 uView;
            uniform mat4 uProjection;

            layout(location = 0) in vec2 vPos;
            layout(location = 1) in vec4 vCol;

            out vec4 fCol;

            void main() {
                fCol = vCol;
                
                gl_Position = uView * uProjection * vec4(vPos, 1.0, 1.0);
            }
            """;
    private static final String fragment = """
            #version 330 core

            in vec4 fCol;

            void main() {
                gl_FragColor = fCol;
            }
            """;

    private final Camera camera;
    private final GLFWWindow window;
    private final VertexArray vertexArray;
    private final Buffer buffer;
    private final Shader shader;

    public CameraTest() {
        window = new GLFWWindow(getClass().getName());
        window.createGLCapabilities();

        vertexArray = new VertexArray();
        buffer = new Buffer(Buffer.Type.ARRAY_BUFFER);
        shader = new Shader(vertex, fragment);

        camera = new Camera();

        window.setRenderer(this::drawTriangle);
        init();
        window.loop();
        window.dispose();
        buffer.dispose();
        vertexArray.dispose();
    }

    public static void main(String[] args) {
        new CameraTest();
    }

    private void init() {
        vertexArray.bind();
        buffer.bind();
        buffer.data(new float[]{
                -0.5f, -0.5f, 1f, 0f, 0f, 1f,
                0.5f, -0.5f, 0f, 1f, 0f, 1f,
                0f, 0.5f, 0f, 0f, 1f, 1f,

                -0.4f, -0.45f, 0f, 1f, 1f, 1f,
                0.4f, -0.45f, 1f, 0f, 1f, 1f,
                0f, 0.35f, 1f, 1f, 0f, 1f
        }, Buffer.BufferUsage.STATIC_DRAW);

        vertexArray.vertexAttribPointer(0, 2, Buffer.Datatype.FLOAT, 6 * Float.BYTES, 0);
        vertexArray.vertexAttribPointer(1, 4, Buffer.Datatype.FLOAT, 6 * Float.BYTES, 2 * Float.BYTES);
        vertexArray.disableVertexAttribArray(0);
        vertexArray.disableVertexAttribArray(1);
    }

    private void drawTriangle() {
        final Vector2i winSize = window.getSize();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, (int) winSize.x, (int) winSize.y);
        glClearColor(0, 0, 0, 1);

        shader.uploadUniformMat4f("uView", camera.getView());
        shader.uploadUniformMat4f("uProjection", camera.getProjection());

        shader.bind();

        vertexArray.bind();
        vertexArray.enableVertexAttributeArray(0);
        vertexArray.enableVertexAttributeArray(1);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
        vertexArray.disableVertexAttribArray(0);
        vertexArray.disableVertexAttribArray(1);

        vertexArray.unbind();
        shader.unbind();
    }
}
