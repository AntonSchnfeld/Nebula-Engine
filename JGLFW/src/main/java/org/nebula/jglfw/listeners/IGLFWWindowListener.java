package org.nebula.jglfw.listeners;

import org.nebula.jglfw.GLFWWindow;

/**<br>
 * <h2>GLFWWindow</h2>
 * <br>
 *
 * The interface for listeners that handle GLFW window events.
 *
 * @author Anton Schoenfeld
 * @see GLFWWindow
 */
public interface IGLFWWindowListener
{

    /**
     * Called when the GLFW window is resized.
     *
     * @param window The GLFW window that was resized.
     * @param width  The new width of the window.
     * @param height The new height of the window.
     */
    void onWindowResize(GLFWWindow window, int width, int height);

    /**
     * Called when the framebuffer of the GLFW window is resized.
     *
     * @param window The GLFW window whose framebuffer was resized.
     * @param width  The new width of the framebuffer.
     * @param height The new height of the framebuffer.
     */
    void onFrameBufferResize(GLFWWindow window, int width, int height);

    /**
     * Called when the position of the GLFW window changes.
     *
     * @param window The GLFW window whose position changed.
     * @param x      The new x-coordinate of the window position.
     * @param y      The new y-coordinate of the window position.
     */
    void onWindowPositionChange(GLFWWindow window, int x, int y);

    /**
     * Called when the GLFW window is requested to close.
     *
     * @param window The GLFW window that is requested to close.
     */
    void onClose(GLFWWindow window);
}