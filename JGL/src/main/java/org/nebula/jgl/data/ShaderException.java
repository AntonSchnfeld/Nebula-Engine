package org.nebula.jgl.data;

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
