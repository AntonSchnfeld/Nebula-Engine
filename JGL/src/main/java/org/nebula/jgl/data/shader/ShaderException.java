package org.nebula.jgl.data.shader;

public class ShaderException extends RuntimeException {
    public ShaderException(String msg) {
        super(msg);
    }

    public ShaderException() {
        super();
    }

    public ShaderException(Throwable cause) {
        super(cause);
    }
}
