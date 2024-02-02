package org.nebula.io;

import org.lwjgl.BufferUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import static org.lwjgl.stb.STBImage.*;

public class Files
{
    public static ByteBufferedImage readImageFromResource(String resourcePath) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        byte[] byteArr = null;

        try (InputStream in = Files.class.getResourceAsStream(resourcePath)) {
            if (in != null) byteArr = in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (byteArr == null)
            throw new RuntimeException(new FileNotFoundException("Could not read resource: " + resourcePath));

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(byteArr.length);
        byteBuffer.put(byteArr, 0, byteArr.length);

        ByteBuffer image = stbi_load_from_memory(byteBuffer, width, height, channels, STBI_default);

        return new ByteBufferedImage(image, width.get(), height.get(), channels.get());
    }

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

    public static byte[] readFile (String filePath) {
        try {
            return java.nio.file.Files.readAllBytes(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFileAsString (String filePath) {
        return new String(readFile(filePath));
    }
}
