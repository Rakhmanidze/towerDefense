package cz.cvut.fel.pjv.semak_pjv.ui;

import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData;
import cz.cvut.fel.pjv.semak_pjv.objects.Tower;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Towers.convertTowerTypeToString;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Towers.getTowerCost;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.*;

/**
 * Represents the bottom panel used during gameplay.
 * Extends the BottomPanel class.
 */
public class PlayingBottomPanel extends BottomPanel {
    private CustomButton buttonMenu, buttonPause;
    private final Playing playing;
    private CustomButton[] towerButtons;
    private Tower selectedTower;
    private Tower displayedTower;
    private int coins = 100;
    private boolean showTowerCost;
    private int towerCostType;
    private int lives = 20;
    private Image heart, coin;
    private static final Logger LOGGER = Logger.getLogger(PlayingBottomPanel.class.getName());

    /**
     * Constructs a PlayingBottomPanel object with the specified parameters.
     *
     * @param x        the x-coordinate of the bottom panel
     * @param y        the y-coordinate of the bottom panel
     * @param width    the width of the bottom panel
     * @param height   the height of the bottom panel
     * @param playing  the Playing scene associated with the bottom panel
     */
    public PlayingBottomPanel(int x, int y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        initButtons();

        initHeartAndCoinImages();
    }

    /**
     * Initializes the heart and coin images used for displaying lives and coins.
     */
    private void initHeartAndCoinImages() {
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        heart = new WritableImage(reader, 0, 5*32, 32, 32);
        coin = new WritableImage(reader, 32, 5*32, 24, 24);
    }

    /**
     * Initializes the buttons in the bottom panel.
     */
    private void initButtons() {
        buttonMenu = new CustomButton("Menu", 538, 642, 100, 30);
        buttonPause = new CustomButton("Pause", 538, 682, 100, 30);
        towerButtons = new CustomButton[3];
        int blockWidth = 50;
        int blockHeight = 50;
        int startingX = 10;
        int startingY = 650;
        int xOffset  = (int) (blockWidth * 1.1f);

        for (int i = 0; i < towerButtons.length; i++) {
            towerButtons[i] = new CustomButton("", startingX + xOffset * i, startingY, blockWidth, blockHeight, i);
        }
    }

    /**
     * Draws the bottom panel on the canvas.
     * @param gc The graphics context to render on.
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(249, 213, 187));
        gc.fillRect(x, y, width, height);
        drawButtons(gc);
        drawDisplayedTower(gc);
        drawWaveInfo(gc);
        drawCoinsAmount(gc);

        if (showTowerCost)
            drawTowerCost(gc);

        drawLivesAmount(gc);
    }

    /**
     * Draws the lives amount on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawLivesAmount(GraphicsContext gc) {
            gc.drawImage(heart, 568, 720);
            gc.setFill(Color.BLACK);
            gc.fillText("" + lives, 542, 744);
    }

    /**
     * Draws the tower cost on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawTowerCost(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRoundRect(10, 735, 190, 30, 10, 10);
        gc.setFill(Color.BLACK);
        gc.fillText(getTowerCostType(), 15, 755);
        gc.fillText("Costs: " + getTowerCostPrice(), 95, 755);
        gc.drawImage(coin, 175, 737);
        if (isPossibleBuyNewTower()) {
            gc.setFill(Color.RED);
            gc.fillText("NOT ENOUGH MONEY", 205, 755);
        }
    }

    /**
     * Checks if it's possible to buy a new tower based on its cost.
     * @return True if it's possible to buy a new tower, otherwise false.
     */
    private boolean isPossibleBuyNewTower() {
        return getTowerCostPrice() > coins;
    }

    /**
     * Resets all gameplay-related parameters to their initial values.
     */
    public void resetEverything() {
        lives = 20;
        towerCostType = 0;
        coins = 100;
        selectedTower = null;
        displayedTower = null;
    }

    /**
     * Draws the coins amount on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawCoinsAmount(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 18));
        gc.drawImage(coin, 10, 705);
        gc.fillText("" + coins, 35, 725);
    }

    /**
     * Draws information about the current wave on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawWaveInfo(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 18));

        drawWhenIsNextWave(gc);
        drawHowManyEnemiesLeft(gc);
        drawHowManyWavesLeft(gc);
    }

    /**
     * Draws information about how many waves are left on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawHowManyWavesLeft(GraphicsContext gc) {
        int currentWave = playing.getWaveHandler().getWaveIndex();
        int allWaves = playing.getWaveHandler().getWaves().size();
        gc.fillText("Wave " + (currentWave + 1) + " / " + allWaves, 220, 685);
    }

    /**
     * Draws information about how many enemies are left on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawHowManyEnemiesLeft(GraphicsContext gc) {
            int enemiesLeft = playing.getEnemyHandler().getAmountOfAliveEnemies();
            gc.fillText("Enemies alive: " + enemiesLeft, 220, 705);
    }

    /**
     * Draws information about when the next wave is on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawWhenIsNextWave(GraphicsContext gc) {
        if (playing.getWaveHandler().isWaveCounterStarted()) {
            float remainingTime = playing.getWaveHandler().getRemainingTime();

            String formattedTime = String.format("%.1f", remainingTime);

            gc.fillText("Next wave at: " + formattedTime, 220,665);
        }
    }

    /**
     * Draws the displayed tower information on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawDisplayedTower(GraphicsContext gc) {
        if (displayedTower != null) {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRoundRect(370, 645, 160, 85, 10, 10);
            gc.drawImage(playing.getTowerHandler().getTowerImages()[displayedTower.getTowerType()], 390, 650, 50, 50);

            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", 15));
            gc.fillText(convertTowerTypeToString(displayedTower.getTowerType()), 455, 670);
            gc.fillText("ID: " + displayedTower.getId(), 455, 690);

            drawDisplayedTowerBorders(gc);
            drawDisplayedTowerDamageArea(gc);
        }
    }

    /**
     * Draws the damage area of the displayed tower on the canvas.
     * @param gc The graphics context to render on.
     */
    private void drawDisplayedTowerDamageArea(GraphicsContext gc) {
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeOval(displayedTower.getX() + 16  - displayedTower.getDamageArea() , displayedTower.getY()
                     + 16 - displayedTower.getDamageArea(), displayedTower.getDamageArea() * 2, displayedTower.getDamageArea() * 2);
    }

    /**
     * Draws the borders of the displayed tower on the canvas.
     * @param gc The graphics context to render on.
     */
    private void drawDisplayedTowerBorders(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeRect(displayedTower.getX(), displayedTower.getY(), 32, 32);
    }

    /**
     * Draws the buttons on the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawButtons(GraphicsContext gc) {
            buttonMenu.draw(gc);
            buttonPause.draw(gc);

            for (CustomButton b:  towerButtons) {
                gc.setFill(Color.LIGHTGRAY);
                gc.fillRect(b.getBounds().getX(), b.getBounds().getY(), b.getBounds().getWidth(), b.getBounds().getHeight());

                gc.drawImage(playing.getTowerHandler().getTowerImages()[b.getId()], b.getBounds().getX(), b.getBounds().getY(), b.getBounds().getWidth(), b.getBounds().getHeight());
                makeHoverEffect(gc, b);
            }
    }

    public void displayTower(Tower tower) {
        displayedTower = tower;
    }

    public void buyTower(int towerType) {
        this.coins -= getTowerCost(towerType);
    }

    public void addCoins(int coinsForKilling) {
        this.coins += coinsForKilling;
    }

    /**
     * Removes one life from the player and checks if the game is over.
     */
    public void removeOneLife() {
        lives--;
        if (lives <= 0) {
            LOGGER.info("game over");
            setGameState(GAME_OVER);
        }
    }

    /**
     * Handles mouse click events in the playing bottom panel.
     * @param x The x-coordinate of the click.
     * @param y The y-coordinate of the click.
     */
    public void mouseClicked(int x, int y) {
        if (buttonMenu.getBounds().contains(x, y)) {
            setGameState(MENU);
            LOGGER.info("Menu scene was opened");
        } else if (buttonPause.getBounds().contains(x, y)) {
            togglePause();
            if (playing.isGamePaused())
                LOGGER.info("Game was paused");
            else
                LOGGER.info("Game was resumed");
        }
        else {
            for (CustomButton b : towerButtons)
                if (b.getBounds().contains(x, y))
                    if (isThereEnoughCoinsToBuyNewTower(b.getId())) {
                        selectedTower = new Tower(0, 0, -1, b.getId(), (int) GameData.Towers.getDefaultLifeTimeInSeconds(b.getId()));
                        playing.setSelectedTower(selectedTower);
                        LOGGER.info("Was selected " + convertTowerTypeToString(selectedTower.getTowerType()));
                        return;
                    }
        }
    }

    /**
     * Toggles the game pause state.
     */
    private void togglePause() {
        playing.setGamePaused(!playing.isGamePaused());

        if (playing.isGamePaused())
            buttonPause.setText("Resume");
        else
            buttonPause.setText("Pause");
    }

    /**
     * Checks if there are enough coins to buy a new tower of a given type.
     * @param towerType The type of the tower to buy.
     * @return True if there are enough coins, false otherwise.
     */
    private boolean isThereEnoughCoinsToBuyNewTower(int towerType) {
        return coins >= getTowerCost(towerType);
    }

    /**
     * Handles mouse movement events in the playing bottom panel.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    public void mouseMoved(int x, int y) {
            showTowerCost = false;
            buttonMenu.setButtonHovered(buttonMenu.getBounds().contains(x, y));
            buttonPause.setButtonHovered(buttonPause.getBounds().contains(x, y));
            for (CustomButton b: towerButtons) {
                b.setButtonHovered(b.getBounds().contains(x, y));
                if (b.getBounds().contains(x, y)) {
                    towerCostType = b.getId();
                    showTowerCost = true;
                }
            }
        }

//    Getters
    private int getTowerCostPrice() {
        return getTowerCost(towerCostType);
    }

    private String getTowerCostType() {
        return convertTowerTypeToString(towerCostType);
    }
}
