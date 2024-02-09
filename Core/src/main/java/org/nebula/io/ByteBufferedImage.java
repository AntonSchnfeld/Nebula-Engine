package org.nebula.io;

import org.nebula.base.interfaces.IDisposable;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;

public class ByteBufferedImage implements IDisposable {
    private final int width, height, channels;
    private final ByteBuffer bytes;

    protected ByteBufferedImage(ByteBuffer bytes, int width, int height, int channels) {
        this.bytes = bytes;
        this.width = width;
        this.height = height;
        this.channels = channels;
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

    public ByteBuffer getBytes() {
        return bytes;
    }

    @Override
    public void dispose() {
        stbi_image_free(bytes);
    }
}
