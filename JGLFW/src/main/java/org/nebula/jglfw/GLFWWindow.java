package org.nebula.jglfw;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.io.ByteBufferedImage;
import org.nebula.jglfw.listeners.IGLFWInputListener;
import org.nebula.jglfw.listeners.IGLFWWindowListener;
import org.nebula.jglfw.listeners.RenderListener;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWWindow implements IDisposable {
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;
    private final List<IGLFWWindowListener> windowListeners;
    private final List<IGLFWInputListener> inputListeners;
    private final IGLFWWindowListener resizableListener;
    private long windowObject;
    private RenderListener renderListener;
    private GLFWErrorCallback errorCallback;
    private ByteBufferedImage currentIcon;
    private String title;
    private boolean resizable;

    public GLFWWindow(final String title, final int x, final int y, final int width, final int height) {
        JGLFW.init();
        this.title = title;
        windowListeners = new ArrayList<>();
        inputListeners = new ArrayList<>();
        resizable = true;
        resizableListener = new IGLFWWindowListener() {
            @Override
            public void onWindowResize(GLFWWindow window, int w, int h) {
                window.setSize(width, height);
            }

            @Override
            public void onFrameBufferResize(GLFWWindow window, int width, int height) {
            }

            @Override
            public void onWindowPositionChange(GLFWWindow window, int x, int y) {
            }

            @Override
            public void onClose(GLFWWindow window) {
            }
        };
        init(title, x, y, width, height);
    }

    public GLFWWindow(final String title, final int width, final int height) {
        this(title, 0, 0,
                width, height);
        center();
    }

    public GLFWWindow(final String title) {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void init(String title, int x, int y, int width, int height) {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        renderListener = () -> {
        };
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

        glfwSetWindowPosCallback(windowObject, (window, xPos, yPos) -> {
            for (IGLFWWindowListener listener : windowListeners)
                listener.onWindowPositionChange(this, xPos, yPos);
        });
        glfwSetWindowSizeCallback(windowObject, (window, w, h) -> {
            for (IGLFWWindowListener listener : windowListeners)
                listener.onWindowResize(this, w, h);
        });
        glfwSetWindowCloseCallback(windowObject, window -> {
            for (IGLFWWindowListener listener : windowListeners)
                listener.onClose(this);
        });
        glfwSetFramebufferSizeCallback(windowObject, (window, w, h) -> {
            for (IGLFWWindowListener listener : windowListeners)
                listener.onFrameBufferResize(this, w, h);
        });
        glfwSetKeyCallback(windowObject, (window, key, scanCode, action, mods) -> {
            for (IGLFWInputListener listener : inputListeners)
                listener.onKeyAction(this, key, scanCode, action, mods);
        });
        glfwSetMouseButtonCallback(windowObject, (window, button, action, mods) -> {
            for (IGLFWInputListener listener : inputListeners)
                listener.onMouseButtonAction(this, button, action, mods);
        });
        glfwSetCursorPosCallback(windowObject, (window, xPos, yPos) -> {
            for (IGLFWInputListener listener : inputListeners)
                listener.onCursorPositionChange(this, x, y);
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
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

    public GLCapabilities createGLCapabilities() {
        return GL.createCapabilities();
    }

    public void loop() {
        long now;
        long then = System.currentTimeMillis();
        int frame = 0;

        Vector2i size = new Vector2i();
        while (!glfwWindowShouldClose(windowObject)) {
            getSize(size);
            glfwSwapBuffers(windowObject);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glViewport(0, 0, size.x, size.y);
            glClearColor(0, 0, 0, 1);

            renderListener.render();
            frame++;
            now = System.currentTimeMillis();
            if (now - then >= 1000) {
                glfwSetWindowTitle(windowObject, title + " FPS: " + frame);
                frame = 0;
                then = System.currentTimeMillis();
            }

            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
    }

    public void center() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode == null)
            throw new IllegalStateException("Could not retrieve glfw vid mode");

        Vector2i size = getSize();

        setPosition(((vidMode.width() - size.x) / 2), ((vidMode.height() - size.y) / 2));
    }

    public void addWindowListener(IGLFWWindowListener windowListener) {
        windowListeners.add(windowListener);
    }

    public void removeWindowListener(IGLFWWindowListener windowListener) {
        windowListeners.remove(windowListener);
    }

    public void addInputListener(IGLFWInputListener inputListener) {
        inputListeners.add(inputListener);
    }

    public void removeInputListener(IGLFWInputListener inputListener) {
        inputListeners.remove(inputListener);
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        if (!resizable)
            windowListeners.add(resizableListener);
        else windowListeners.remove(resizableListener);
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(windowObject, width, height);
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(windowObject, x, y);
    }

    public void setWindowIcon(ByteBufferedImage icon) {
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

    public RenderListener getRenderer() {
        return renderListener;
    }

    public void setRenderer(RenderListener renderListener) {
        this.renderListener = renderListener;
    }

    public Vector2i getSize() {
        return getSize(new Vector2i());
    }

    public Vector2i getSize(Vector2i vector) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(windowObject, width, height);

            vector.x = width.get();
            vector.y = height.get();
        }
        return vector;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowObject, title);
    }

    public Vector2i getPosition() {
        return getPosition(new Vector2i());
    }

    public Vector2i getPosition(Vector2i position) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);

            glfwGetWindowPos(windowObject, x, y);

            position.x = x.get();
            position.y = y.get();
        }

        return position;
    }

    @Override
    public void dispose() {
        if (currentIcon != null) currentIcon.dispose();
        errorCallback.free();
        glfwDestroyWindow(windowObject);
        glfwTerminate();
    }
}
