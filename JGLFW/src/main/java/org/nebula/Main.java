package org.nebula;

import org.nebula.io.Files;
import org.nebula.jglfw.GLFWWindow;

public class Main
{
    public static void main(String[] args)
    {
        GLFWWindow window = new GLFWWindow("Hello GLFW");
        window.setWindowIcon(Files.loadImage("assets\\nebula.png"));
        window.loop();
        window.dispose();
    }
}