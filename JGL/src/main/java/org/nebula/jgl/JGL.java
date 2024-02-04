package org.nebula.jgl;

import static org.lwjgl.opengl.GL11C.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11C.glGetError;

public final class JGL
{
    private JGL() {}

    public static void checkForOpenGLError() {
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            throw new RuntimeException("GL Error code: " + error);
    }
}
