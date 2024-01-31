package org.nebula.engine;

import org.nebula.io.Files;
import org.nebula.jglfw.GLFWWindow;

public class NebulaApplication
{
    private ApplicationListener applicationListener;
    private final GLFWWindow window;

    public NebulaApplication (String title, int width, int height, ApplicationListener listener)
    {
        window = new GLFWWindow(title, width, height);
        window.center();
        window.setRenderer(listener::render);
        window.setWindowIcon(Files.readImage("assets/nebula.png"));

        listener.init();

        Thread updateThread = new Thread(this::update);
        updateThread.start();

        window.loop();
    }

    public void update ()
    {
        applicationListener.update();
    }
}
