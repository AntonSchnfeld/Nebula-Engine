package org.nebula.io;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.MissingResourceException;
import java.util.Objects;

import static org.lwjgl.stb.STBImage.*;

public class Files
{
    public static ByteBufferedImage readImage(String filePath)
    {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer bytes = stbi_load(filePath, width, height, channels, STBI_default);
        if (bytes == null)
            throw new RuntimeException("stb Failure Reason: " + stbi_failure_reason());

        bytes.flip();

        return new ByteBufferedImage(bytes, width.get(), height.get(), channels.get());
    }

    public static byte[] readFile(String filePath) {
        try {
            return java.nio.file.Files.readAllBytes(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFileAsString(String filePath) {
        return new String(readFile(filePath));
    }

    public static ByteBufferedImage readImageFromResource(String resourceName) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        byte[] byteArr = readResource(resourceName);

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(byteArr.length);
        byteBuffer.put(byteArr, 0, byteArr.length);
        byteBuffer.flip();

        ByteBuffer image = stbi_load_from_memory(byteBuffer, width, height, channels, STBI_default);
        if (image == null)
            throw new RuntimeException("STB Failure reason: " + stbi_failure_reason());

        ByteBufferedImage bbi = new ByteBufferedImage(image, width.get(), height.get(), channels.get());

        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);
        MemoryUtil.memFree(channels);

        return bbi;
    }

    public static byte[] readResource(String resourceName) {
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        Objects.requireNonNull(url);

        byte[] bytes = null;

        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {
            bytes = in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bytes == null)
            throw new RuntimeException(new FileNotFoundException("Could not read resource: " + resourceName));

        return bytes;
    }

    public static String readResourceAsString(String resourceName) {
        return new String(readResource(resourceName));
    }
}
