package cz.cvut.fel.pjv.semak_pjv.main;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import javafx.scene.canvas.GraphicsContext;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.*;

/**
 * Handles rendering based on the current game state.
 */
public class Render {
    private final TowerDefense towerDefense;

    public Render(TowerDefense towerDefense) {
        this.towerDefense = towerDefense;
    }

    /**
     * Renders the game based on the current game state.
     * @param gc The GraphicsContext used for rendering.
     */
    public void render(GraphicsContext gc) {
        if (GameStates.gameState == MENU)
            towerDefense.getMenu().render(gc);
        else if (GameStates.gameState == PLAYING)
            towerDefense.getPlaying().render(gc);
        else if (GameStates.gameState == EDITING)
            towerDefense.getEditing().render(gc);
        else if (GameStates.gameState == GAME_OVER)
            towerDefense.getGameOver().render(gc);
        else if (GameStates.gameState == GAME_WON)
            towerDefense.getGameWon().render(gc);
    }
}