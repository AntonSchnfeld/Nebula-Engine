package org.nebula.jglfw.listeners;

import org.nebula.jglfw.GLFWWindow;

/**
 * <br>
 * <h2>RenderListener</h2>
 * <br>
 * <p>
 * Functional interface for rendering logic in a GLFW window. The {@link RenderListener#render()}
 * method will be called whenever the associated window is rendering.
 *
 * @author Anton Schoenfeld
 * @see GLFWWindow
 */
@FunctionalInterface
public interface RenderListener {

    /**
     * Renders content within a GLFW window.
     *
     * @param window The GLFW window where rendering occurs.
     */
    void render();
}