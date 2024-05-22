package cz.cvut.fel.pjv.semak_pjv.inputs;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import cz.cvut.fel.pjv.semak_pjv.main.GameStates;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.*;

/**
 * EventHandler implementation for handling keyboard input in the TowerDefense game.
 * It delegates the key events to the appropriate method in the Playing class based on the game state.
 */
public class KeyboardListener implements EventHandler<KeyEvent> {
    private final TowerDefense towerDefense;

    /**
     * Constructs a KeyboardListener object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game to delegate keyboard events to
     */
    public KeyboardListener (TowerDefense towerDefense) {
        this.towerDefense = towerDefense;
    }

    /**
     * Handles keyboard events.
     * @param event The KeyEvent to handle.
     */
    @Override
    public void handle(KeyEvent event) {
        if (GameStates.gameState == PLAYING)
            towerDefense.getPlaying().keyPressed(event);
    }
}
