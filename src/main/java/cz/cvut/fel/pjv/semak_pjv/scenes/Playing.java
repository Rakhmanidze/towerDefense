package cz.cvut.fel.pjv.semak_pjv.scenes;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import cz.cvut.fel.pjv.semak_pjv.enemies.Enemy;
import cz.cvut.fel.pjv.semak_pjv.handlers.*;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.LevelBuilder;
import cz.cvut.fel.pjv.semak_pjv.objects.PathCoordinate;
import cz.cvut.fel.pjv.semak_pjv.objects.Tower;
import cz.cvut.fel.pjv.semak_pjv.ui.PlayingBottomPanel;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.GRASS_BLOCK;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.getCoinsForKilling;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Towers.*;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.GAME_WON;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.setGameState;

/**
 * Represents the playing scene where the main gameplay occurs.
 * Extends the GameScene class and implements SceneMethods.
 */
public class Playing extends GameScene implements SceneMethods {
    private int[][] level;
    private final BlockHandler blockHandler;
    private final PlayingBottomPanel playingBottomPanel;
    private int mouseX, mouseY;
    private Image pathEnd;
    private final EnemyHandler enemyHandler;
    private final TowerHandler towerHandler;
    private final ProjectileHandler projectileHandler;
    private final WaveHandler waveHandler;
    private PathCoordinate start, end;
    private Tower selectedTower;
    private boolean isTowerDrawn;
    private int freeCoinsCounter;
    private boolean gamePaused;
    private GraphicsContext graphicsContext;
    private static final Logger LOGGER = Logger.getLogger(Playing.class.getName());

    /**
     * Constructs a Playing object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game instance
     */
    public Playing(TowerDefense towerDefense) {
        super(towerDefense);

        blockHandler = new BlockHandler();

        level =LevelBuilder.getLevelData();

        playingBottomPanel = new PlayingBottomPanel(0, 640, 640, 130, this);

        loadDefaultLevel();
        enemyHandler = new EnemyHandler(this, start, end);
        towerHandler = new TowerHandler(this);
        projectileHandler = new ProjectileHandler(this);
        waveHandler = new WaveHandler(this);

        initTowerImages();
    }

    /**
     * Initializes the images for towers.
     */
    private void initTowerImages() {
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        pathEnd = new WritableImage(reader, 2*32, 3*32, 32, 32);
    }

    /**
     * Loads the default level data.
     */
    private void loadDefaultLevel() {
        level = DownloadSave.getLevelData("currentLevel");
        ArrayList<PathCoordinate> coordinates = DownloadSave.getLevelPathCoordinate("currentLevel");
        assert coordinates != null;
        start = coordinates.get(0);
        end = coordinates.get(1);
    }

    /**
     * Draws the level and its components on the canvas.
     * @param gc The graphics context to draw on.
     */
    private void drawLevel(GraphicsContext gc) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                if (isAnimation(id))
                    gc.drawImage(getImage(id, animationIndex), x * 32, y * 32);
                else
                    gc.drawImage(getImage(id), x * 32, y * 32);
            }
        }
        drawTower(gc);
    }

    /**
     * Draws the towers on the canvas.
     * @param gc The graphics context to draw on.
     */
    public void drawTower(GraphicsContext gc) {
        gc.drawImage(pathEnd, end.getxCoord() * 32, end.getyCoord() * 32);
    }

    /**
     * Sets the level data.
     * @param level The level data to set.
     * @param start The starting path coordinate.
     * @param end The ending path coordinate.
     */
    public void setLevel(int[][] level, PathCoordinate start, PathCoordinate end) {
        this.level = level;
        this.start = start;
        this.end = end;
        if (this.graphicsContext != null)
            drawTower(this.graphicsContext);
    }

    /**
     * Updates the game state.
     */
    public void update() {
        if (!gamePaused) {
            updateCounter();
            waveHandler.update();

            freeCoinsCounter++;
            if (freeCoinsCounter % 300 == 0)
                playingBottomPanel.addCoins(1);

            if (isAllEnemiesDead())
                if (isNextWaveExists()) {
                    waveHandler.startWaveCounter();
                    if (isWaveCounterFinished()) {
                        waveHandler.increaseWaveIndex();
                        enemyHandler.getEnemies().clear();
                        waveHandler.resetEnemyIndex();
                    }
                } else {
                    int waitingTime = 3;
                    waitBeforeWinningAndSetWinState(waitingTime);
                }
            if (isTimeForSpawningNextEnemy())
                addEnemy();

            enemyHandler.update();
            towerHandler.update();
            projectileHandler.update();
        }
    }

    /**
     * Waits for a specified time before setting the game state to "Game Won".
     * @param waitingTime The waiting time in seconds.
     */
    private void waitBeforeWinningAndSetWinState(int waitingTime) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> setGameState(GAME_WON));
                timer.cancel();
            }
        }, waitingTime * 1000L);
    }

    /**
     * Checks if all enemies are dead.
     * @return True if all enemies are dead, false otherwise.
     */
    private boolean isAllEnemiesDead() {
        if (waveHandler.isThereNextEnemyInWave())
            return false;
        for (Enemy e : enemyHandler.getEnemies())
            if (e.isAlive())
                return false;
        return true;
    }

    /**
     * Adds an enemy to the game.
     */
    private void addEnemy() {
        enemyHandler.addEnemy(waveHandler.getNextEnemy());
    }

    /**
     * Checks if it's time to spawn the next enemy.
     * @return True if it's time to spawn the next enemy, false otherwise.
     */
    private boolean isTimeForSpawningNextEnemy() {
        if (waveHandler.isTimeForSpawningNextEnemy())
            return waveHandler.isThereNextEnemyInWave();
        return false;
    }

    /**
     * Draws borders around the current block.
     * @param gc The graphics context to draw on.
     */
    private void drawCurrentBlockBorders (GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeRect(mouseX, mouseY, 32, 32);
    }

    /**
     * Gets the type of block at the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The type of block.
     */
    public int getBlockType(int x, int y) {
        int xCoord = x / 32;
        int yCoord = y / 32;

        if (xCoord < 0 || xCoord > 19)
            return 0;

        if (yCoord < 0 || yCoord > 19)
            return 0;

        int id = level[y/32][x/32];
        return getTowerDefense().getBlockHandler().getBlock(id).getBlockType();
    }

    /**
     * Draws the selected tower on the canvas.
     * @param gc The graphics context to draw on.
     */
    private void drawSelectedTower(GraphicsContext gc) {
        if (selectedTower != null && isTowerDrawn)
            gc.drawImage(towerHandler.getTowerImages()[selectedTower.getTowerType()], mouseX, mouseY, 32, 32);
    }

    /**
     * Checks if a tower can be placed at the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if a tower can be placed, false otherwise.
     */
    private boolean isBlockAllowed(int x, int y) {
        int id = level[y/32][x/32];
        int blockType = getBlockHandler().getBlock(id).getBlockType();
        return blockType == GRASS_BLOCK;
    }

    /**
     * Handles key events.
     * @param event The key event.
     */
    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE)
            selectedTower = null;
    }

    /**
     * Initiates shooting from a tower to an enemy.
     * @param t The tower shooting.
     * @param e The enemy being shot.
     */
    public void shootEnemy(Tower t, Enemy e) {
        projectileHandler.newProjectile(t, e);
    }

    public void removeOneLife() {
        playingBottomPanel.removeOneLife();
    }

    /**
     * Renders the game scene.
     * @param gc The graphics context to render on.
     */
    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
        playingBottomPanel.draw(gc);

        enemyHandler.draw(gc);
        towerHandler.draw(gc);
        projectileHandler.draw(gc);
        drawSelectedTower(gc);
        drawCurrentBlockBorders(gc);
    }

    /**
     * Handles mouse click events.
     * @param x The x-coordinate of the click.
     * @param y The y-coordinate of the click.
     */
    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640)
            playingBottomPanel.mouseClicked(x, y);
        else {
            if (selectedTower != null && isBlockAllowed(mouseX, mouseY) && getTowerAtCoord(mouseX, mouseY) == null && !gamePaused) {
                towerHandler.addTower(selectedTower, mouseX, mouseY);

                decreaseCoinsAmount(selectedTower.getTowerType());
                LOGGER.info("Tower added: " + convertTowerTypeToString(selectedTower.getTowerType()));
                LOGGER.info("Coins amount decreased by: " + getTowerCost(selectedTower.getTowerType()));
                selectedTower = null;
            } else {
                Tower tower = getTowerAtCoord(mouseX, mouseY);

                playingBottomPanel.displayTower(tower);
            }
        }
    }

    private void decreaseCoinsAmount(int towerType) {
        playingBottomPanel.buyTower(towerType);
    }

    /**
     * Resets all game elements to their default state.
     */
    public void resetEverything() {
        playingBottomPanel.resetEverything();

       enemyHandler.reset();
       towerHandler.reset();
       projectileHandler.reset();
       waveHandler.reset();

       mouseX = 0;
       mouseY = 0;

       selectedTower = null;
       freeCoinsCounter = 0;
       gamePaused = false;
    }

    /**
     * Handles mouse movement events.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640) {
            playingBottomPanel.mouseMoved(x, y);
            isTowerDrawn = false;
        }  else {
            isTowerDrawn = true;
            mouseX = (x / 32) * 32;
            mouseY = (y / 32) * 32;
        }
    }

    public void giveToUserCoins(int enemyType) {
        playingBottomPanel.addCoins(getCoinsForKilling(enemyType));
    }

//    Getters and setters
    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public void setSelectedTower(Tower selectedTower) {
        this.selectedTower = selectedTower;
    }

    private boolean isWaveCounterFinished() {
        return waveHandler.isWaveCounterFinished();
    }

    private boolean isNextWaveExists() {
        return waveHandler.isNextWaveExists();
    }
    public BlockHandler getBlockHandler() {
        return blockHandler;
    }

    public TowerHandler getTowerHandler() {
        return towerHandler;
    }

    public EnemyHandler getEnemyHandler() {
        return enemyHandler;
    }

    public WaveHandler getWaveHandler() {
        return waveHandler;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    private Tower getTowerAtCoord(int x, int y) {
        return towerHandler.getTowerAtCoord(x, y);
    }
}