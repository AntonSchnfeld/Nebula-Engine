package org.nebula;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.nebula.io.Files;
import org.nebula.jgl.data.Buffer;
import org.nebula.jgl.data.Shader;
import org.nebula.jglfw.GLFWWindow;
import org.nebula.jglfw.listeners.RenderListener;

import java.io.*;
import java.lang.reflect.Field;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        GLFWWindow window = new GLFWWindow("Hello Marlon!");
        window.setWindowIcon(Files.loadImage("assets\\nebula.png"));
        window.createGLCapabilities();
        Shader shader = new Shader(
                Files.readFileAsString("assets/shaders/triangle.vert"), "assets/shaders/triangle.frag");
        shader.bind();
        window.setRenderer(Main::drawTriangle);
        window.loop();
        shader.dispose();
        window.dispose();
    }

    public static void drawTriangle ()
    {
        int VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        Buffer buffer = new Buffer(Buffer.BufferType.ARRAY_BUFFER);
        buffer.data(new float[]{
                -0.5f, -0.5f, 1,
                0.5f, -0.5f, 1,
                0.5f,  0.5f, 1,
                -0.5f,  0.5f, 1
        }, Buffer.BufferUsage.STATIC_DRAW);

        Buffer ebo = new Buffer(Buffer.BufferType.ELEMENT_ARRAY_BUFFER);
        ebo.data(new int[]{
                0, 1, 2,
                2, 3, 0
        }, Buffer.BufferUsage.STATIC_DRAW);

        ebo.bind();
        buffer.bind();

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        int error = glGetError();
        if (error != 0) System.out.println(error);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        error = glGetError();
        if (error != 0) System.out.println(error);

        ebo.dispose();
        glDeleteVertexArrays(VAO);
        buffer.dispose();
    }
}