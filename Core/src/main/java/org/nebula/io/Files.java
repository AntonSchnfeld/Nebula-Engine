package org.nebula.io;

import org.lwjgl.system.MemoryStack;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.Objects;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Files {
    public static ByteBufferedImage readImage(String filePath) {
        ByteBufferedImage bbi;

        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            ByteBuffer bytes = stbi_load(filePath, width, height, channels, STBI_default);
            if (bytes == null)
                throw new RuntimeException("stb Failure Reason: " + stbi_failure_reason());

            bytes.flip();

            bbi = new ByteBufferedImage(bytes, width.get(), height.get(), channels.get());
        }

        return bbi;
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
        byte[] byteArr = readResource(resourceName);

        ByteBufferedImage bbi;

        try (MemoryStack stack = stackPush()) {
            ByteBuffer byteBuffer = stack.malloc(byteArr.length);
            byteBuffer.put(byteArr, 0, byteArr.length);
            byteBuffer.flip();

            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = stbi_load_from_memory(byteBuffer, width, height, channels, STBI_default);
            if (image == null)
                throw new RuntimeException("STB Failure reason: " + stbi_failure_reason());

            bbi = new ByteBufferedImage(image, width.get(), height.get(), channels.get());
        }

        return bbi;
    }

    public static byte[] readResource(String resourceName) {
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        Objects.requireNonNull(url);

        byte[] bytes;

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
