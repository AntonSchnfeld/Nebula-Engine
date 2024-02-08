package org.nebula;

import org.nebula.io.Files;
import org.nebula.jgl.data.shader.Shader;
import org.nebula.jgl.data.shader.VertexAttribs;

public class ShaderTest {
    public static void main(String[] args) {
        VertexAttribs attribs = Shader.parseAttribs(Files.readResourceAsString("default.vert"));
        System.out.println(attribs);
    }
}
