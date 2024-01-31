package org.nebula.jgl.data;

import org.nebula.base.interfaces.IDisposable;
import org.nebula.io.Files;
import org.nebula.io.ByteBufferedImage;

import static org.lwjgl.opengl.GL33C.*;

public class Texture implements IDisposable
{
    private final int id, width, height, channels;

    public Texture (final String filepath)
    {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // Repeat texture when stretched
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        ByteBufferedImage image = Files.loadImage(filepath);

        width = image.getWidth();
        height = image.getHeight();
        channels = image.getChannels();

        final int colorMode = image.getChannels() == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, colorMode, image.getWidth(), image.getHeight(),
                0, colorMode, GL_UNSIGNED_BYTE, image.getBytes());

        image.dispose();
    }

    public int getId ()
    {
        return id;
    }

    public int getWidth ()
    {
        return width;
    }

    public int getHeight ()
    {
        return height;
    }

    public int getChannels ()
    {
        return channels;
    }

    @Override
    public void dispose()
    {
        glDeleteTextures(id);
    }
}
