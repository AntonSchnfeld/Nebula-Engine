package org.nebula.jgl.data;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FrameBuffer {
    public static final FrameBuffer DEFAULT_FRAMEBUFFER = new FrameBuffer(0);

    private final int id;
    private final int texture;

    private FrameBuffer(int id) {
        this.id = id;
        this.texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, 800, 600, 0,
                GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, NULL
        );

        glBindFramebuffer(GL_FRAMEBUFFER, id);


        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("FrameBuffer construction was not completed");
    }

    public FrameBuffer() {
        this(glGenFramebuffers());
    }

    public void dispose() {
        glDeleteFramebuffers(id);
        glDeleteTextures(texture);
    }
}
