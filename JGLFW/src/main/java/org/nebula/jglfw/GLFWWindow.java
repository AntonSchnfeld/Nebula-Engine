package org.nebula.jglfw;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.io.ByteBufferedImage;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindow implements IDisposable
{
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;

    private long windowObject;
    private int width, height;
    private String title;
    private GLFWErrorCallback errorCallback;
    private ByteBufferedImage currentIcon;

    public GLFWWindow (final String title, final int x, final int y, final int width, final int height)
    {
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        this.width = width;
        this.height = height;
        this.title = title;

        init();
    }

    public GLFWWindow (final String title, final int width, final int height)
    {
        this(title,
                (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (width / 2),
                (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (height / 2),
                width, height);
    }

    public GLFWWindow (final String title)
    {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void init ()
    {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        errorCallback.set();

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be non-resizable
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        // Create the window
        windowObject = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowObject == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowObject, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(windowObject, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowObject);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(windowObject);
    }

    public void loop ()
    {

        IntBuffer screenWidth = BufferUtils.createIntBuffer(1);
        IntBuffer screenHeight = BufferUtils.createIntBuffer(1);

        while (!glfwWindowShouldClose(windowObject)) {
            glfwGetFramebufferSize(windowObject, screenWidth, screenHeight);

            screenWidth.clear();
            screenHeight.clear();

            width = screenWidth.get(0);
            height = screenHeight.get(0);

            glfwSwapBuffers(windowObject);

            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
    }

    public int getWidth ()
    {
        return width;
    }

    public int getHeight ()
    {
        return height;
    }

    public void setWidth (int width)
    {
        glfwSetWindowSize(windowObject, width, height);
    }

    public void setHeight (int height)
    {
        glfwSetWindowSize(windowObject, width, height);
    }

    public void setTitle (String title)
    {
        glfwSetWindowTitle(windowObject, title);
    }

    public void setWindowIcon (ByteBufferedImage icon)
    {
        if (currentIcon != null) currentIcon.dispose();
        currentIcon = icon;

        GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
        GLFWImage glfwImage = GLFWImage.malloc();
        glfwImage.set(icon.getWidth(), icon.getHeight(), icon.getBytes());
        imageBuffer.put(0, glfwImage);

        glfwSetWindowIcon(windowObject, imageBuffer);
    }

    @Override
    public void dispose ()
    {
        if (currentIcon != null) currentIcon.dispose();
        errorCallback.free();
        glfwDestroyWindow(windowObject);
        glfwTerminate();
    }
}
