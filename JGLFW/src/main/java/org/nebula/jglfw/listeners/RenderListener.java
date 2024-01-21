package org.nebula.jglfw.listeners;

import org.nebula.jglfw.GLFWWindow;

/**
 * <br>
 * <h2>RenderListener</h2>
 * <br>
 *
 * Functional interface for rendering logic in a GLFW window. The {@link RenderListener#render(GLFWWindow)}
 * method will be called whenever the associated window is rendering.
 *
 * @see GLFWWindow
 * @author Anton Schoenfeld
 */
@FunctionalInterface
public interface RenderListener
{

    /**
     * Renders content within a GLFW window.
     *
     * @param window The GLFW window where rendering occurs.
     */
    void render(GLFWWindow window);
}