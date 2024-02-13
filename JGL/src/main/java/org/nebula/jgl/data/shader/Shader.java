package org.nebula.jgl.data.shader;

import org.joml.*;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.jgl.data.ShaderException;
import org.nebula.jgl.data.buffer.Buffer;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL33C.*;

/**
 * <br>
 * <h2>Shader</h2>
 * <br>
 * The Shader class represents an OpenGL shader program with methods for setting uniform values.
 * <p>
 * This class encapsulates shader creation, compilation, and linking. It also provides methods for setting
 * uniform values of various types, such as floats, integers, vectors, and matrices.
 * </p>
 *
 * <p>
 * Note: This class implements the {@code IDisposable} interface, and it is essential to call the {@code dispose()}
 * method when the shader is no longer needed to release associated OpenGL resources.
 * </p>
 *
 * @author Anton Schoenfeld
 * @see IDisposable
 * @see org.joml.Vector2f
 * @see org.joml.Vector3f
 * @see org.joml.Vector4f
 * @see org.joml.Matrix2f
 * @see org.joml.Matrix3f
 * @see org.joml.Matrix4f
 */
public class Shader implements IDisposable {
    public static final String PROJECTION_MAT_NAME = "uProjection";
    public static final String VIEW_MAT_NAME = "uView";
    private static final Map<String, GLSLDatatype> glslDatatypeMap = Map.ofEntries(
            entry("float", new GLSLDatatype(1, Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("vec2", new GLSLDatatype(2, 2 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("vec3", new GLSLDatatype(3, 3 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("vec4", new GLSLDatatype(4, 4 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("double", new GLSLDatatype(1, Double.BYTES, Buffer.Datatype.DOUBLE)),
            entry("dvec2", new GLSLDatatype(2, 2 * Double.BYTES, Buffer.Datatype.DOUBLE)),
            entry("dvec3", new GLSLDatatype(3, 3 * Double.BYTES, Buffer.Datatype.DOUBLE)),
            entry("dvec4", new GLSLDatatype(4, 4 * Double.BYTES, Buffer.Datatype.DOUBLE)),
            entry("int", new GLSLDatatype(1, Integer.BYTES, Buffer.Datatype.INT)),
            entry("ivec2", new GLSLDatatype(2, 2 * Integer.BYTES, Buffer.Datatype.INT)),
            entry("ivec3", new GLSLDatatype(3, 3 * Integer.BYTES, Buffer.Datatype.INT)),
            entry("ivec4", new GLSLDatatype(4, 4 * Integer.BYTES, Buffer.Datatype.INT)),
            entry("uint", new GLSLDatatype(1, Integer.BYTES, Buffer.Datatype.UNSIGNED_INT)),
            entry("uvec2", new GLSLDatatype(2, 2 * Integer.BYTES, Buffer.Datatype.UNSIGNED_INT)),
            entry("uvec3", new GLSLDatatype(3, 3 * Integer.BYTES, Buffer.Datatype.UNSIGNED_INT)),
            entry("uvec4", new GLSLDatatype(4, 4 * Integer.BYTES, Buffer.Datatype.UNSIGNED_INT)),
            entry("ushort", new GLSLDatatype(1, Short.BYTES, Buffer.Datatype.UNSIGNED_SHORT)),
            entry("usvec2", new GLSLDatatype(2, 2 * Short.BYTES, Buffer.Datatype.UNSIGNED_SHORT)),
            entry("usvec3", new GLSLDatatype(3, 3 * Short.BYTES, Buffer.Datatype.UNSIGNED_SHORT)),
            entry("usvec4", new GLSLDatatype(4, 4 * Short.BYTES, Buffer.Datatype.UNSIGNED_SHORT)),
            entry("ubyte", new GLSLDatatype(1, Byte.BYTES, Buffer.Datatype.UNSIGNED_BYTE)),
            entry("ubvec2", new GLSLDatatype(2, 2 * Byte.BYTES, Buffer.Datatype.UNSIGNED_BYTE)),
            entry("ubvec3", new GLSLDatatype(3, 3 * Byte.BYTES, Buffer.Datatype.UNSIGNED_BYTE)),
            entry("ubvec4", new GLSLDatatype(4, 4 * Byte.BYTES, Buffer.Datatype.UNSIGNED_BYTE)),
            entry("bool", new GLSLDatatype(1, Byte.BYTES, Buffer.Datatype.BOOLEAN)),
            entry("bvec2", new GLSLDatatype(2, 2 * Byte.BYTES, Buffer.Datatype.BOOLEAN)),
            entry("bvec3", new GLSLDatatype(3, 3 * Byte.BYTES, Buffer.Datatype.BOOLEAN)),
            entry("bvec4", new GLSLDatatype(4, 4 * Byte.BYTES, Buffer.Datatype.BOOLEAN)),
            entry("mat2", new GLSLDatatype(2 * 2, 2 * 2 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat3", new GLSLDatatype(3 * 3, 3 * 3 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat4", new GLSLDatatype(4 * 4, 4 * 4 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat2x2", new GLSLDatatype(2 * 2, 2 * 2 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat2x3", new GLSLDatatype(2 * 3, 2 * 3 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat2x4", new GLSLDatatype(2 * 4, 2 * 4 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat3x2", new GLSLDatatype(3 * 2, 3 * 2 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat3x3", new GLSLDatatype(3 * 3, 3 * 3 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat3x4", new GLSLDatatype(3 * 4, 3 * 4 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat4x2", new GLSLDatatype(4 * 2, 4 * 2 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat4x3", new GLSLDatatype(4 * 3, 4 * 3 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("mat4x4", new GLSLDatatype(4 * 4, 4 * 4 * Float.BYTES, Buffer.Datatype.FLOAT)),
            entry("sampler2D", new GLSLDatatype(1, Integer.BYTES, Buffer.Datatype.INT)),
            entry("samplerCube", new GLSLDatatype(1, Integer.BYTES, Buffer.Datatype.INT))
    );
    private final int id;
    private final VertexAttribs vertexAttribs;
    private final HashMap<String, Integer> uniformLocations, attribLocations;

    /**
     * Creates a new Shader with specified vertex and fragment shader sources.
     *
     * @param vertexSource   the source code for the vertex shader
     * @param fragmentSource the source code for the fragment shader
     */
    public Shader(final String vertexSource, final String fragmentSource) {
        uniformLocations = new HashMap<>();
        attribLocations = new HashMap<>();

        this.vertexAttribs = parseAttribs(vertexSource);

        id = glCreateProgram();

        final int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        final int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexShader, vertexSource);
        glShaderSource(fragmentShader, fragmentSource);

        glCompileShader(vertexShader);
        glCompileShader(fragmentShader);

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new ShaderException(glGetShaderInfoLog(vertexShader));
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new ShaderException(glGetShaderInfoLog(fragmentShader));

        glAttachShader(id, vertexShader);
        glAttachShader(id, fragmentShader);

        glLinkProgram(id);

        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE)
            throw new ShaderException(glGetProgramInfoLog(id));

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        glValidateProgram(id);

        if (glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE)
            throw new ShaderException(glGetProgramInfoLog(id));
    }

    private static AbstractMap.SimpleEntry<String, GLSLDatatype> entry(String name, GLSLDatatype datatype) {
        return new AbstractMap.SimpleEntry<>(name, datatype);
    }

    public static VertexAttribs parseAttribs(String vertexSource) {
        String[] lines = vertexSource.split("\\r?\\n");

        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            if (line.matches("^\\s*layout.*")) {
                sb.append(line).append("\n");
            }
        }

        String[] attribDeclarations = sb.toString().split("\\n");
        VertexAttrib[] vertexAttribs = new VertexAttrib[attribDeclarations.length];

        Pattern pattern = Pattern.compile(
                "\\s*layout\\s*\\(\\s*location\\s*=\\s*(\\d+)\\s*\\)\\s*in\\s+(\\w+)\\s+(\\w+)\\s*;");

        for (int i = 0; i < vertexAttribs.length; i++) {
            String attribDeclaration = attribDeclarations[i];

            VertexAttrib attrib = new VertexAttrib();

            Matcher matcher = pattern.matcher(attribDeclaration);
            if (!matcher.matches()) {
                throw new RuntimeException("Invalid vertex attribute declaration: " + attribDeclaration);
            }

            int location = Integer.parseInt(matcher.group(1));
            String datatype = matcher.group(2);
            String name = matcher.group(3);

            attrib.setLocation(location);
            attrib.setName(name);
            GLSLDatatype glslDatatype = glslDatatypeMap.get(datatype);
            attrib.setBytes(glslDatatype.bytes);
            attrib.setSize(glslDatatype.size);
            attrib.setDataType(glslDatatype.dataType);

            vertexAttribs[i] = attrib;
        }

        return new VertexAttribs(vertexAttribs);
    }

    /**
     * Binds the shader for use in rendering.
     * <p>
     * This method sets the current OpenGL shader program to the one represented by this Shader object.
     * </p>
     */
    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public int getAttribLocation(final String attribLocation) {
        if (!attribLocations.containsKey(attribLocation)) {
            int loc = glGetAttribLocation(id, attribLocation);
            attribLocations.put(attribLocation, loc);
            return loc;
        }

        return attribLocations.get(attribLocation);
    }

    public VertexAttribs getVertexAttribs() {
        return vertexAttribs;
    }

    /**
     * Retrieves the location of the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @return the location of the uniform variable
     */
    public int getUniformLocation(final String uniformName) {
        return glGetUniformLocation(id, uniformName);
    }

    /**
     * Uploads a float value to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the float value to upload
     */
    public void uploadUniformFloat(final String uniformName, final float value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform1f(uniformLoc, value);
    }

    /**
     * Uploads an integer value to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the integer value to upload
     */
    public void uploadUniformInt(final String uniformName, final int value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform1i(uniformLoc, value);
    }

    /**
     * Uploads a 2D vector (Vec2f) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Vector2f value to upload
     */
    public void uploadUniformVec2f(final String uniformName, final Vector2f value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform2f(uniformLoc, value.x, value.y);
    }

    /**
     * Uploads a 3D vector (Vec3f) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Vector3f value to upload
     */
    public void uploadUniformVec3f(final String uniformName, final Vector3f value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform3f(uniformLoc, value.x, value.y, value.z);
    }

    /**
     * Uploads a 4D vector (Vec4f) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Vector4f value to upload
     */
    public void uploadUniformVec4f(final String uniformName, final Vector4f value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform4f(uniformLoc, value.x, value.y, value.z, value.w);
    }

    /**
     * Uploads a boolean value to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the boolean value to upload
     */
    public void uploadUniformBool(final String uniformName, final boolean value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform1i(uniformLoc, value ? 1 : 0);
    }

    /**
     * Uploads a 2D vector (Vec2i) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Vector2i value to upload
     */
    public void uploadUniformVec2i(final String uniformName, final Vector2i value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform2i(uniformLoc, value.x, value.y);
    }

    /**
     * Uploads a 3D vector (Vec3i) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Vector3i value to upload
     */
    public void uploadUniformVec3i(final String uniformName, final Vector3i value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform3i(uniformLoc, value.x, value.y, value.z);
    }

    /**
     * Uploads a 4D vector (Vec4i) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Vector4i value to upload
     */
    public void uploadUniformVec4i(final String uniformName, final Vector4i value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform4i(uniformLoc, value.x, value.y, value.z, value.w);
    }

    /**
     * Uploads a 2x2 matrix (Mat2f) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Matrix2f value to upload
     */
    public void uploadUniformMat2f(final String uniformName, final Matrix2f value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniformMatrix2fv(uniformLoc, false, value.get(new float[4]));
    }

    /**
     * Uploads a 3x3 matrix (Mat3f) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Matrix3f value to upload
     */
    public void uploadUniformMat3f(final String uniformName, final Matrix3f value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniformMatrix3fv(uniformLoc, false, value.get(new float[9]));
    }

    /**
     * Uploads a 4x4 matrix (Mat4f) to the specified uniform variable.
     *
     * @param uniformName the name of the uniform variable
     * @param value       the Matrix4f value to upload
     */
    public void uploadUniformMat4f(final String uniformName, final Matrix4f value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniformMatrix4fv(uniformLoc, false, value.get(new float[16]));
    }

    public void uploadUniformIntArray(String uniformName, int[] value) {
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, getUniformLocation(uniformName));

        bind();
        final int uniformLoc = uniformLocations.get(uniformName);
        glUniform1iv(uniformLoc, value);
    }

    /**
     * Checks if this shader is equal to another object.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof Shader that)
            return this.id == that.id;
        return false;
    }

    /**
     * Generates a hash code for this shader.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Releases associated OpenGL resources by deleting the shader program.
     */
    @Override
    public void dispose() {
        glDeleteProgram(id);
    }

    private record GLSLDatatype(int size, int bytes, Buffer.Datatype dataType) {
    }
}