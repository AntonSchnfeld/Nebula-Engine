package org.nebula.jgl;

import static org.lwjgl.opengl.GL33C.*;

/**
 * <br>
 * <h2>JGL</h2>
 * <br>
 * The JGL class provides utility methods for working with OpenGL in a LWJGL-based graphics application.
 * <p>
 * It contains methods for checking OpenGL errors and retrieving information about the OpenGL context.
 * </p>
 *
 * @author Anton Schoenfeld
 * @see org.lwjgl.opengl.GL33C
 */
public final class JGL {
    private JGL() {
    }

    /**
     * Checks for OpenGL errors and throws a runtime exception if an error is detected.
     *
     * @throws RuntimeException If an OpenGL error is detected.
     */
    public static void checkForOpenGLError() {
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
    }

    /**
     * Retrieves the maximum number of texture image units supported by the OpenGL context.
     *
     * @return The maximum number of texture image units.
     */
    public static int getMaxTextureImageUnits() {
        int[] arr = new int[1];
        glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, arr);
        return arr[0];
    }
}
