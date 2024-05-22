package cz.cvut.fel.pjv.semak_pjv.main;

/**
 * Represents the game window with its dimensions and update count.
 */
public class GameWindow {
    private int updates = 0;

    public void update() {
        updates++;
    }

    public void resetUpdates() {
        updates = 0;
    }

//    Getters
    public int getWindowWidth() {
        return 640;
    }

    public int getWindowHeight() {
        return 770;
    }

    public int getUpdates() {
        return updates;
    }
}
