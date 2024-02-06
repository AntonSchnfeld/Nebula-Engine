package org.nebula.jglfw;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLX;
import org.lwjgl.opengl.GLXCapabilities;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.io.ByteBufferedImage;
import org.nebula.jglfw.listeners.IGLFWInputListener;
import org.nebula.jglfw.listeners.IGLFWWindowListener;
import org.nebula.jglfw.listeners.RenderListener;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindow implements IDisposable
{
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;

    private long windowObject;
    private RenderListener renderListener;
    private GLFWErrorCallback errorCallback;
    private ByteBufferedImage currentIcon;
    private String title;

    public GLFWWindow (final String title, final int x, final int y, final int width, final int height)
    {
        JGLFW.init();
        this.title = title;
        init(title, x, y, width, height);
    }
    public GLFWWindow (final String title, final int width, final int height)
    {
        this(title, 0, 0,
                width, height);
        center();
    }
    public GLFWWindow (final String title)
    {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        center();
    }

    private void init (String title, int x, int y, int width, int height)
    {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        renderListener = () -> {};
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
            glfwSetWindowPos(windowObject, (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowObject);
        // Enable v-sync
        glfwSwapInterval(1);

        setPosition(x, y);

        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwShowWindow(windowObject);
    }

    public GLCapabilities createGLCapabilities()
    {
        return GL.createCapabilities();
    }

    public void loop ()
    {
        long now;
        long then = System.currentTimeMillis();
        int frame = 0;

        while (!glfwWindowShouldClose(windowObject))
        {
            glfwSwapBuffers(windowObject);

            renderListener.render();
            frame++;
            now = System.currentTimeMillis();
            if (now - then >= 1000)
            {
                System.out.print("\rFPS: " + frame);
                frame = 0;
                then = System.currentTimeMillis();
            }

            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
    }

    public void center ()
    {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode == null)
            throw new IllegalStateException("Could not retrieve glfw vid mode");

        Vector2i size = getSize();

        setPosition((int) ((vidMode.width() - size.x) / 2), (int) ((vidMode.height() - size.y) / 2));
    }

    public void setWindowListener (IGLFWWindowListener listener)
    {
        glfwSetWindowSizeCallback(windowObject,
                (window, width, height) -> listener.onWindowResize(this, width, height));
        glfwSetWindowPosCallback(windowObject,
                (window, x, y) -> listener.onWindowPositionChange(this, x, y));
        glfwSetWindowCloseCallback(windowObject,
                window -> listener.onClose(this));
        glfwSetFramebufferSizeCallback(windowObject,
                (window, width, height) -> listener.onFrameBufferResize(this, width, height));
    }
    public void setWindowInputListener (IGLFWInputListener listener)
    {
        glfwSetCursorPosCallback(windowObject,
                (window, x, y) -> listener.onCursorPositionChange(this, x, y));
        glfwSetKeyCallback(windowObject,
                (window, key, scancode, action, mods) -> listener.onKeyAction(this, key, scancode, action, mods));
        glfwSetMouseButtonCallback(windowObject,
                (window, button, action, mods) -> listener.onMouseButtonAction(this, button, action, mods));
    }
    public void setSize (int width, int height)
    {
        glfwSetWindowSize(windowObject, width, height);
    }
    public void setPosition (int x, int y)
    {
        glfwSetWindowPos(windowObject, x, y);
    }
    public void setWindowIcon (ByteBufferedImage icon)
    {
        // Dispose of previous icon if it exists
        if (currentIcon != null) currentIcon.dispose();
        currentIcon = icon;

        // Allocate native resources
        GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
        GLFWImage glfwImage = GLFWImage.malloc();
        glfwImage.set(icon.getWidth(), icon.getHeight(), icon.getBytes());
        imageBuffer.put(0, glfwImage);

        // Seg window icon
        glfwSetWindowIcon(windowObject, imageBuffer);

        // Free allocated resources
        imageBuffer.free();
        glfwImage.free();
    }
    public void setTitle (String title)
    {
        this.title = title;
        glfwSetWindowTitle(windowObject, title);
    }
    public void setRenderer (RenderListener renderListener)
    {
        this.renderListener = renderListener;
    }

    public RenderListener getRenderer ()
    {
        return renderListener;
    }
    public Vector2i getSize ()
    {
        return getSize(new Vector2i());
    }
    public Vector2i getSize(Vector2i vector) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(windowObject, width, height);

        vector.x = width.get();
        vector.y = height.get();

        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);

        return vector;
    }
    public String getTitle ()
    {
        return title;
    }
    public Vector2f getPosition ()
    {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);

        glfwGetWindowPos(windowObject, x, y);

        return new Vector2f(x.get(), y.get());
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
