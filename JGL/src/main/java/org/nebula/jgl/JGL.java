package org.nebula.jgl;

import static org.lwjgl.opengl.GL33C.*;

public final class JGL
{
    private JGL() {}

    public static void checkForOpenGLError() {
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
    }

    public static int getMaxTextureImageUnits() {
        int[] arr = new int[1];
        glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, arr);
        return arr[0];
    }
}
