package org.nebula;

import org.joml.Vector2f;
import org.nebula.io.Files;
import org.nebula.jgl.Camera;
import org.nebula.jgl.batch.RenderBatch;
import org.nebula.jgl.data.Color;
import org.nebula.jgl.data.Shader;
import org.nebula.jglfw.GLFWWindow;

import static org.lwjgl.opengl.GL11C.*;

public class RenderBatchTest
{
    private RenderBatch batch;
    private GLFWWindow window;
    private Camera camera;

    private final String vertex = "#version 330 core\n" +
            "\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uView;\n" +
            "\n" +
            "layout(location = 0) in vec2 vPos;\n" +
            "layout(location = 1) in vec4 vCol;\n" +
            "layout(location = 2) in vec2 vUv;\n" +
            "layout(location = 3) in float vTexId;\n" +
            "\n" +
            "out vec4 fCol;\n" +
            "out vec2 fUv;\n" +
            "out float fTexId;\n" +
            "\n" +
            "void main() {\n" +
            "    fCol = vCol;\n" +
            "    fUv = vUv;\n" +
            "    fTexId = vTexId;\n" +
            "\n" +
            "    gl_Position = uView * uProjection * vec4(vPos, 0.0, 1.0);\n" +
            "}\n";
    private final String fragment = "#version 330 core\n" +
            "\n" +
            "in vec4 fCol;\n" +
            "in vec2 fUv;\n" +
            "in float fTexId;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_FragColor = fCol;\n" +
            "}\n";

    public RenderBatchTest() {
        camera = new Camera(0, 0, 0, 0);
        window = new GLFWWindow("RenderBatchTest");
        //window.setWindowIcon(Files.readImage("assets/nebula.png"));
        window.setRenderer(this::drawTriangle);
        window.createGLCapabilities();

        this.batch = new RenderBatch();
        batch.setColor(Color.WHITE);
        Shader shader = new Shader(vertex, fragment);
        batch.setShader(shader);

        window.loop();
        window.dispose();
        batch.dispose();
    }

    private void drawTriangle () {
        Vector2f winSize = window.getSize();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, (int) winSize.x, (int) winSize.y);
        glClearColor(0, 0, 0, 1);

        camera.adjustProjection((int) winSize.x, (int) winSize.y);
        batch.setViewMatrix(camera.getViewMatrix());
        batch.setProjectionMatrix(camera.getProjectionMatrix());
        batch.begin();
        for (int i = 0; i < 10; i++)
            batch.triangle(-5000 + i * 10, -5000 + i * 10,
                    5000 + i * 10, -5000 + i * 10, 0 + i * 10, 5000 + i * 10);
        batch.end();
    }

    public static void main(String[] args) {
        new RenderBatchTest();
    }
}
