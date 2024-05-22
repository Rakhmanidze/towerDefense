package cz.cvut.fel.pjv.semak_pjv.inputs;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import cz.cvut.fel.pjv.semak_pjv.main.GameStates;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.*;

/**
 * EventHandler implementation for handling mouse input in the TowerDefense game.
 * It delegates mouse events to the appropriate method in the corresponding state classes based on the game state.
 */
public class MyMouseListener implements EventHandler<MouseEvent> {
    private final TowerDefense towerDefense;

    /**
     * Constructs a MyMouseListener object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game to delegate mouse events to
     */
    public MyMouseListener (TowerDefense towerDefense) {
        this.towerDefense = towerDefense;
    }

    /**
     * Handles mouse events such as mouse clicks, mouse moves, and mouse drags.
     * @param event The MouseEvent to handle.
     */
    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {

            if (GameStates.gameState == MENU)
                towerDefense.getMenu().mouseClicked((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == PLAYING)
                towerDefense.getPlaying().mouseClicked((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == EDITING)
                towerDefense.getEditing().mouseClicked((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == GAME_OVER)
                towerDefense.getGameOver().mouseClicked((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == GAME_WON)
                towerDefense.getGameWon().mouseClicked((int) event.getX(), (int) event.getY());

        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {

            if (GameStates.gameState == MENU)
                towerDefense.getMenu().mouseMoved((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == PLAYING)
                towerDefense.getPlaying().mouseMoved((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == EDITING)
                towerDefense.getEditing().mouseMoved((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == GAME_OVER)
                towerDefense.getGameOver().mouseMoved((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == GAME_WON)
                towerDefense.getGameWon().mouseMoved((int) event.getX(), (int) event.getY());

        }  else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

            if (GameStates.gameState == PLAYING)
                towerDefense.getPlaying().mouseDragged((int) event.getX(), (int) event.getY());
            else if (GameStates.gameState == EDITING)
                towerDefense.getEditing().mouseDragged((int) event.getX(), (int) event.getY());
        }
    }
}
