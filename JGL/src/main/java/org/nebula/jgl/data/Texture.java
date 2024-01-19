package org.nebula.jgl.data;

import org.lwjgl.BufferUtils;
import org.nebula.base.interfaces.IDisposable;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements IDisposable
{
    private final int id, width, height, channels;

    public Texture (final String filepath, boolean hasAlpha)
    {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // Repeat texture when stretched
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load(filepath, widthBuffer, heightBuffer, channelBuffer, STBI_default);

        width = widthBuffer.get();
        height = heightBuffer.get();
        channels = channelBuffer.get();

        final int colorMode = hasAlpha ? GL_RGBA : GL_RGB;
        image.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, colorMode, widthBuffer.get(), heightBuffer.get(),
                0, colorMode, GL_UNSIGNED_BYTE, image);
        stbi_image_free(image);
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
