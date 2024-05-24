package cz.cvut.fel.pjv.semak_pjv.scenes;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.main.GameWindow;
import cz.cvut.fel.pjv.semak_pjv.ui.CustomButton;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.*;

/**
 * Represents the game over scene in the Tower Defense game.
 * This scene is displayed when the player loses the game.
 */
public class GameOver extends GameScene implements SceneMethods {
    private CustomButton buttonReplay, buttonMenu;
    GameWindow gameWindow = new GameWindow();
    private Image gameOverBackgroundImage;
    private static final Logger LOGGER = Logger.getLogger(GameOver.class.getName());

    /**
     * Constructs a GameOver object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game instance
     */
    public GameOver(TowerDefense towerDefense) {
        super(towerDefense);
        initButtons();

        initGameOverBackgroundImage();
    }

    private void initGameOverBackgroundImage() {
        gameOverBackgroundImage = DownloadSave.getImage("gameover.jpg");
    }

    public void drawGameOverBackgroundImage(GraphicsContext gc) {
        gc.drawImage(gameOverBackgroundImage, 0, 0);
    }

    /**
     * Initializes the replay and menu buttons.
     */
    private void initButtons() {
        int buttonWidth = 150;
        int buttonHeight = buttonWidth / 3;
        int x = 470 / 2 - buttonWidth / 2;
        int y = 480;
        int xOffset = 190;

        buttonReplay = new CustomButton("Replay", x, y, buttonWidth, buttonHeight);
        buttonMenu = new CustomButton("Menu",  x + xOffset, y, buttonWidth, buttonHeight);
    }

    /**
     * Restarts the game by resetting all game components.
     */
    private void replayGame() {
        resetEverything();

        setGameState(PLAYING);
    }

    /**
     * Resets all game components to their default state.
     */

    private void resetEverything() {
        getTowerDefense().getPlaying().resetEverything();
    }

    /**
     * Renders the game over scene on the canvas.
     * @param gc The GraphicsContext used for rendering.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(242, 244, 250));
        gc.fillRect(0, 0, gameWindow.getWindowWidth(), gameWindow.getWindowHeight());

        drawGameOverBackgroundImage(gc);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        buttonReplay.draw(gc);
        buttonMenu.draw(gc);
    }

    /**
     * Handles mouse click events within the game over scene.
     * @param x The x-coordinate of the mouse click.
     * @param y The y-coordinate of the mouse click.
     */
    @Override
    public void mouseClicked(int x, int y) {
        if (buttonMenu.getBounds().contains(x, y)) {
            LOGGER.info("Menu scene opened");
            resetEverything();
            setGameState(MENU);
        }
        else if (buttonReplay.getBounds().contains(x, y)) {
            LOGGER.info("Playing scene opened");
            replayGame();
        }
    }

    /**
     * Handles mouse move events within the game over scene.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    @Override
    public void mouseMoved(int x, int y) {
        buttonReplay.setButtonHovered(buttonReplay.getBounds().contains(x, y));
        buttonMenu.setButtonHovered(buttonMenu.getBounds().contains(x, y));
    }
}
