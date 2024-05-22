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
 * Represents the main menu scene where players can start playing the game, access the level editor, or exit the game.
 * Extends the GameScene class and implements SceneMethods.
 */
public class Menu extends GameScene implements SceneMethods {
    GameWindow gameWindow = new GameWindow();
    private CustomButton buttonPlay, buttonEdit, bExit;
    private Image menuBackgroundImage;
    private static final Logger LOGGER = Logger.getLogger(GameOver.class.getName());

    /**
     * Constructs a Menu object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game instance
     */
    public Menu(TowerDefense towerDefense) {
        super(towerDefense);
        initButtons();

        initMenuBackgroundImage();
    }

    private void initMenuBackgroundImage() {
        menuBackgroundImage = DownloadSave.getImage("menu.jpg");
    }

    /**
     * Initializes the buttons displayed in the menu scene.
     */
    private void initButtons() {
        int buttonWidth = 150;
        int buttonHeight = buttonWidth / 3;
        int x = 640 / 2 - buttonWidth / 2;
        int y = 300;
        int yOffset = 100;

        buttonPlay = new CustomButton("Play", x, y, buttonWidth, buttonHeight);
        buttonEdit = new CustomButton("Edit", x, y + yOffset, buttonWidth, buttonHeight);
        bExit = new CustomButton("Exit", x, y + yOffset * 2, buttonWidth, buttonHeight);
    }

    public void drawMenuBackgroundImage(GraphicsContext gc) {
        gc.drawImage(menuBackgroundImage, 0, 0);
    }


    /**
     * Draws the buttons displayed in the menu scene.
     * @param gc The graphics context to draw on.
     */
    private void drawButtons(GraphicsContext gc) {
        gc.setFont(Font.font(18));
        buttonPlay.draw(gc);
        buttonEdit.draw(gc);
        bExit.draw(gc);
    }

    /**
     * Renders the menu scene, displaying background, buttons, and text.
     * @param gc The graphics context to render on.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(242, 244, 250));
        gc.fillRect(0, 0, gameWindow.getWindowWidth(), gameWindow.getWindowHeight());
        drawMenuBackgroundImage(gc);
        drawButtons(gc);
    }

    /**
     * Handles mouse click events in the menu scene.
     * @param x The x-coordinate of the mouse click.
     * @param y The y-coordinate of the mouse click.
     */
    @Override
    public void mouseClicked(int x, int y) {
        if (buttonPlay.getBounds().contains(x, y)) {
            LOGGER.info("Playing scene opened");
            setGameState(PLAYING);
        } else if (buttonEdit.getBounds().contains(x, y)) {
            LOGGER.info("Editing scene opened");
            setGameState(EDITING);
        } else if (bExit.getBounds().contains(x, y)) {
            LOGGER.info("Game exit");
            System.exit(0);
        }
    }

    /**
     * Handles mouse movement events in the menu scene.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    @Override
    public void mouseMoved(int x, int y) {
        buttonPlay.setButtonHovered(buttonPlay.getBounds().contains(x, y));
        buttonEdit.setButtonHovered(buttonEdit.getBounds().contains(x, y));
        bExit.setButtonHovered(bExit.getBounds().contains(x, y));
    }

    @Override
    public void mouseDragged(int x, int y) {
    }
}
