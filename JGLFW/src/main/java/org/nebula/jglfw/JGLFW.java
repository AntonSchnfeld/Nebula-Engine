package org.nebula.jglfw;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

/**
 * <br>
 * <h2>GLFW</h2>
 * <br>
 * The GLFW class provides methods for initializing and terminating the GLFW library.
 * GLFW is a library for creating windows with OpenGL contexts and managing input.
 * This class ensures that GLFW is initialized and terminated in a controlled manner.
 *
 * @author Anton Schoenfeld
 * @see org.lwjgl.glfw.GLFW
 */
public class JGLFW {

    private static boolean initialized = false;
    private static boolean terminated = true;

    /**
     * Initializes the GLFW library.
     * This method should be called before any GLFW functionality is used.
     * If the library is already initialized, this method has no effect.
     * It also registers a shutdown hook to automatically terminate GLFW when the application exits.
     *
     * @throws IllegalStateException If the GLFW library cannot be initialized.
     */
    public synchronized static void init() {
        if (!initialized) {
            if (!glfwInit())
                throw new IllegalStateException("Could not initialize GLFW library");

            initialized = true;
            terminated = false;

            // Register a shutdown hook to ensure GLFW termination on application exit
            Runtime.getRuntime().addShutdownHook(new Thread(JGLFW::terminate));
        }
    }

    /**
     * Terminates the GLFW library.
     * This method should be called when GLFW is no longer needed.
     * If the library is already terminated, this method has no effect.
     */
    public synchronized static void terminate() {
        if (!terminated) {
            glfwTerminate();
            terminated = true;
            initialized = false;
        }
    }
}
