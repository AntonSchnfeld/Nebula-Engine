package org.nebula.engine;

public interface ApplicationListener
{
    void init();
    void render();
    void update();
    void dispose();
}
