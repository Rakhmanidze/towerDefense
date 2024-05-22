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
 * Represents the game scene displayed when the player wins the game.
 * Extends the GameScene class and implements SceneMethods.
 */
public class GameWon extends GameScene implements SceneMethods {
    private CustomButton buttonReplay, buttonMenu;
    GameWindow gameWindow = new GameWindow();
    private Image gameWonBackgroundImage;
    private static final Logger LOGGER = Logger.getLogger(GameOver.class.getName());

    /**
     * Constructs a GameWon object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game instance
     */
    public GameWon(TowerDefense towerDefense) {
        super(towerDefense);
        initGameWonBackgroundImage();
        initButtons();

    }

    private void initGameWonBackgroundImage() {
        gameWonBackgroundImage = DownloadSave.getImage("gameWin.jpg");
    }

    public void drawGameWonBackgroundImage(GraphicsContext gc) {
        gc.drawImage(gameWonBackgroundImage, 0, 0);
    }

    /**
     * Initializes the buttons displayed in the game won scene.
     */
    private void initButtons() {
        int buttonWidth = 150;
        int buttonHeight = buttonWidth / 3;
        int x = 470 / 2 - buttonWidth / 2;
        int y = 600;
        int xOffset = 190;

        buttonReplay = new CustomButton("Replay", x, y, buttonWidth, buttonHeight);
        buttonMenu = new CustomButton("Menu",  x + xOffset, y, buttonWidth, buttonHeight);
    }


    /**
     * Resets the game state and starts a new game when the replay button is clicked.
     */
    private void replayGame() {
        getTowerDefense().getPlaying().resetEverything();

        setGameState(PLAYING);
    }

    /**
     * Resets all game-related states and switches to the main menu when the menu button is clicked.
     */
    private void resetEverything() {
        getTowerDefense().getPlaying().resetEverything();
    }

    /**
     * Renders the game won scene, displaying background, buttons, and text.
     * @param gc The graphics context to render on.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(242, 244, 250));
        gc.fillRect(0, 0, gameWindow.getWindowWidth(), gameWindow.getWindowHeight());

        drawGameWonBackgroundImage(gc);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        buttonReplay.draw(gc);
        buttonMenu.draw(gc);
    }

    /**
     * Handles mouse click events in the game won scene.
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
     * Handles mouse movement events in the game won scene.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    @Override
    public void mouseMoved(int x, int y) {
        buttonReplay.setButtonHovered(buttonReplay.getBounds().contains(x, y));
        buttonMenu.setButtonHovered(buttonMenu.getBounds().contains(x, y));
    }

    @Override
    public void mouseDragged(int x, int y) {
    }
}
