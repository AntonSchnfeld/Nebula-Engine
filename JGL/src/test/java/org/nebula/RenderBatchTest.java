package org.nebula;

import org.joml.Vector2f;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.Shader;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL33C.*;

public class RenderBatchTest
{
    private final RenderBatch batch;
    private final GLFWWindow window;
    private final Camera camera;

    private static final String vertex = """
            #version 330 core

            uniform mat4 uProjection;
            uniform mat4 uView;

            layout(location = 0) in vec2 vPos;
            layout(location = 1) in vec4 vCol;
            layout(location = 2) in vec2 vUv;
            layout(location = 3) in float vTexId;

            out vec4 fCol;
            out vec2 fUv;
            out float fTexId;

            void main() {
                fCol = vCol;
                fUv = vUv;
                fTexId = vTexId;

                gl_Position = uView * uProjection * vec4(vPos, 1.0, 1.0);
            }
            """;
    private static final String fragment = """
            #version 330 core

            in vec4 fCol;
            in vec2 fUv;
            in float fTexId;

            void main() {
                gl_FragColor = fCol;
            }
            """;

    public RenderBatchTest() {
        camera = new Camera(new Vector2f(0, 0));
        window = new GLFWWindow(getClass().getName());
        window.setRenderer(this::drawTriangle);
        window.createGLCapabilities();

        this.batch = new RenderBatch();
        batch.setColor(Color.CYAN);
        Shader shader = new Shader(vertex, fragment);
        batch.setShader(shader);

        window.loop();
        window.dispose();
        batch.dispose();
    }

    private void drawTriangle() {
        Vector2f winSize = window.getSize();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, (int) winSize.x, (int) winSize.y);
        glClearColor(0, 0, 0, 1);

        batch.setViewMatrix(camera.getView());
        batch.setProjectionMatrix(camera.getProjection());
        batch.begin();
        batch.triangle(new Vector2f(-0.5f, -0.5f), new Vector2f(0.5f, -0.5f), new Vector2f(0, 0.5f));
        batch.end();
    }

    public static void main(String[] args) {
        new RenderBatchTest();
    }
}
