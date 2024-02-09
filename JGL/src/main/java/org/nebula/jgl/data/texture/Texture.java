package org.nebula.jgl.data.texture;

import org.nebula.base.interfaces.IDisposable;
import org.nebula.io.ByteBufferedImage;
import org.nebula.io.Files;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Texture implements IDisposable {
    private final int id, width, height, channels;


    public Texture(int width, int height) {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        this.channels = 3;

        bind();
        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGB, width, height, 0,
                GL_RGB, GL_UNSIGNED_BYTE, NULL
        );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        unbind();
    }

    public Texture(final String resourceName) {
        this(Files.readImageFromResource(resourceName), false);
    }

    public Texture(final ByteBufferedImage image) {
        this(image, false);
    }

    public Texture(final String resourceName, boolean useAntiAliasing) {
        this(Files.readImageFromResource(resourceName), useAntiAliasing);
    }

    public Texture(final ByteBufferedImage image, boolean useAntiAliasing) {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // Repeat texture when stretched
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        final int magFilter = useAntiAliasing ? GL_LINEAR : GL_NEAREST;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, magFilter);

        width = image.getWidth();
        height = image.getHeight();
        channels = image.getChannels();

        final int colorMode = image.getChannels() == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, colorMode, image.getWidth(), image.getHeight(),
                0, colorMode, GL_UNSIGNED_BYTE, image.getBytes());

        image.dispose();
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void bindToSlot(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getChannels() {
        return channels;
    }

    @Override
    public void dispose() {
        glDeleteTextures(id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
