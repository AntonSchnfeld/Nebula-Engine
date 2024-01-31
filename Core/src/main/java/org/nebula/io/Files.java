package org.nebula.io;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.stb.STBImage.*;

public class Files
{
    public static ByteBufferedImage loadImage (String filePath)
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

    public static byte[] readFile (String filePath) throws IOException
    {
        return java.nio.file.Files.readAllBytes(Path.of(filePath));
    }

    public static String readFileAsString (String filePath) throws IOException
    {
        return new String(readFile(filePath));
    }
}
