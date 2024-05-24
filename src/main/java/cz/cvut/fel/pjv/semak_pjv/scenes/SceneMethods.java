package cz.cvut.fel.pjv.semak_pjv.scenes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Defines methods required for managing scenes in a game.
 */
public interface SceneMethods {
//    that class has all type of methods which scenes should have
    public void render(GraphicsContext gc);
    public void mouseClicked(int x, int y);
    public void mouseMoved(int x, int y);
}
