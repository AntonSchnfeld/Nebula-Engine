package org.nebula.jgl.data;

import org.nebula.jgl.data.texture.Texture;

import static org.lwjgl.opengl.GL33C.*;

public class FrameBuffer {
    private final int id;
    private final Texture texture;
    private final int renderBuffer;

    public FrameBuffer(int width, int height) {
        this.id = glGenFramebuffers();

        glBindFramebuffer(GL_FRAMEBUFFER, id);

        texture = new Texture(width, height);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getId(), 0);
        texture.unbind();

        this.renderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("FrameBuffer construction was not completed | FrameBuffer status: " + glCheckFramebufferStatus(GL_FRAMEBUFFER));

        unbind();
    }

    public Texture getTexture() {
        return texture;
    }

    public int getRenderBuffer() {
        return renderBuffer;
    }

    public int getId() {
        return id;
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void dispose() {
        glDeleteFramebuffers(id);
        texture.dispose();
        glDeleteRenderbuffers(renderBuffer);
    }
}
