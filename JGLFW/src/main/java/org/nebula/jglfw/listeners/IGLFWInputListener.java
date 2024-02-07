package org.nebula.jglfw.listeners;

import org.nebula.jglfw.GLFWWindow;

/**
 * <br>
 * <h2>IGLFWInputListener</h2>
 * <br>
 * The interface for listeners that handle GLFW input events.
 *
 * @author Anton Schoenfeld
 * @see GLFWWindow
 */
public interface IGLFWInputListener {

    /**
     * Called when the cursor position changes within the GLFW window.
     *
     * @param window The GLFW window where the cursor position changed.
     * @param x      The new x-coordinate of the cursor position.
     * @param y      The new y-coordinate of the cursor position.
     */
    void onCursorPositionChange(GLFWWindow window, double x, double y);

    /**
     * Called when a key action (press or release) occurs within the GLFW window.
     *
     * @param window The GLFW window where the key action occurred.
     * @param key    The key code of the pressed or released key.
     * @param action The action (press, release, repeat) of the key.
     * @param mods   The modifier keys that were held down during the key action.
     */
    void onKeyAction(GLFWWindow window, int key, int scanCode, int action, int mods);

    /**
     * Called when a mouse button action (press or release) occurs within the GLFW window.
     *
     * @param window The GLFW window where the mouse button action occurred.
     * @param button The button code of the pressed or released mouse button.
     * @param action The action (press or release) of the mouse button.
     * @param mods   The modifier keys that were held down during the mouse button action.
     */
    void onMouseButtonAction(GLFWWindow window, int button, int action, int mods);
}
