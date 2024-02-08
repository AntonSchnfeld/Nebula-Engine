package org.nebula.jgl.data.shader;

import org.joml.*;
import org.nebula.base.interfaces.IDisposable;
import org.nebula.jgl.data.ShaderException;

import java.util.HashMap;

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
 *
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

    private final int id;
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

    public void uploadIntArray(String uniformName, int[] value) {
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
}