package cz.cvut.fel.pjv.semak_pjv.handlers;

import cz.cvut.fel.pjv.semak_pjv.enemies.Enemy;
import cz.cvut.fel.pjv.semak_pjv.enemies.Ghost;
import cz.cvut.fel.pjv.semak_pjv.enemies.Monster;
import cz.cvut.fel.pjv.semak_pjv.enemies.Robot;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.objects.PathCoordinate;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.*;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Direction.*;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.*;

/**
 * Manages the movement, actions, and rendering of enemies in the game.
 */
public class EnemyHandler {
    private final Playing playing;
    private final Image[] enemyImages;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private PathCoordinate start, end;  // Start and end points for enemy paths.
    private final int healthLineWidth = 20;
    private Image slowEffectImage;
    private static final Logger LOGGER = Logger.getLogger(EnemyHandler.class.getName());

    /**
     * Constructs an EnemyHandler object with the specified playing area and path coordinates.
     *
     * @param playing  the Playing object representing the game area
     * @param start    the starting point for enemy paths
     * @param end      the ending point for enemy paths
     */
    public EnemyHandler(Playing playing, PathCoordinate start, PathCoordinate end) {
        this.playing = playing;
        this.start = start;
        this.end = end;
        enemyImages = new Image[3];

        loadSlowedEffectImage();

        loadEnemyImages();
    }

    /**
     * Loads the image for enemy slowdown effect.
     */
        private void loadSlowedEffectImage() {
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        slowEffectImage = new WritableImage(reader, 5 * 32, 0, 32, 32);
    }

    /**
     * Loads images for different enemy types.
     */
    private void loadEnemyImages() {
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        enemyImages[0]  = new WritableImage(reader, 32, 3*32, 32, 32);
        enemyImages[1]  = new WritableImage(reader, 0, 3*32, 32, 32);
        enemyImages[2]  = new WritableImage(reader, 4*32, 0, 32, 32);
    }

    /**
     * Updates enemy movement and actions.
     */
    public void update() {
        for (Enemy enemy : enemies)
            if (enemy.isAlive())
                updateEnemyMove(enemy);
    }

    /**
     * Updates the movement of a specific enemy.
     */
    private void updateEnemyMove(Enemy enemy) {
        if (enemy.getLastDirection() == -1)
            setNewDirectionAndMove(enemy);
        int nextX = (int)(enemy.getX() + getSpeedAndWidth(enemy.getLastDirection(), enemy.getEnemyType()));
        int nextY = (int)(enemy.getY() + getSpeedAndHeight(enemy.getLastDirection(), enemy.getEnemyType()));

        if (getBlockType(nextX, nextY) == ROAD_BLOCK)
            enemy.move(enemy.getLastDirection(), getSpeed(enemy.getEnemyType()));
        else if (isAtTheEnd(enemy)) {
            enemy.die();
            LOGGER.info("One life is lost");
            playing.removeOneLife();
        } else
            setNewDirectionAndMove(enemy);
    }

    /**
     * Updates the start and end points of enemy paths.
     *
     * @param start The new start point for enemy paths.
     * @param end   The new end point for enemy paths.
     */
    public void updateStartAndEnd(PathCoordinate start, PathCoordinate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Sets a new direction for the enemy and moves it accordingly.
     * Adjusts the enemy's position to ensure it aligns with the current block.
     * If the enemy reaches the end of the path, it stops moving.
     *
     * @param enemy The enemy whose direction and movement need to be updated.
     */
    private void setNewDirectionAndMove(Enemy enemy) {
        int direction = enemy.getLastDirection();

//        move enemy for sure to current block
        int xCoord = (int)(enemy.getX() / 32);
        int yCoord = (int)(enemy.getY() / 32);

        fixEnemyOffsetBlock(enemy, direction, xCoord, yCoord);

        if (isAtTheEnd(enemy))
            return;

        if (direction == LEFT || direction == RIGHT) {
            int nextY = (int)(enemy.getY() + getSpeedAndHeight(UP, enemy.getEnemyType()));

            if (getBlockType((int)enemy.getX(), nextY) == ROAD_BLOCK)
                enemy.move(UP, getSpeed(enemy.getEnemyType()));
            else
                enemy.move(DOWN, getSpeed(enemy.getEnemyType()));
        } else {
            int nextX = (int)(enemy.getX() + getSpeedAndWidth(RIGHT, enemy.getEnemyType()));
            if (getBlockType(nextX, (int)enemy.getY()) == ROAD_BLOCK)
                enemy.move(RIGHT, getSpeed(enemy.getEnemyType()));
            else
                enemy.move(LEFT, getSpeed(enemy.getEnemyType()));
        }
    }

    /**
     * Adjusts the enemy's position to ensure it aligns with the current block,
     * taking into account its direction of movement.
     *
     * @param enemy     The enemy to be adjusted.
     * @param direction The direction of movement of the enemy.
     * @param xCoord    The x-coordinate of the current block.
     * @param yCoord    The y-coordinate of the current block.
     */
    private void fixEnemyOffsetBlock(Enemy enemy, int direction, int xCoord, int yCoord) {
        if (direction == RIGHT && xCoord < 19)
            xCoord++;
        else if (direction == DOWN && yCoord < 19)
            yCoord++;

        enemy.setPosition(xCoord * 32, yCoord * 32);
    }

    /**
     * Resets the enemy handler.
     */
    public void reset() {
        enemies.clear();
    }

    /**
     * Gives coins to the player for killing an enemy of a specific type.
     */
    public void giveCoinsForKillingEnemy(int enemyType) {
        playing.giveToUserCoins(enemyType);
    }

    /**
     * Checks if an enemy has reached the end of the path.
     */
    private boolean isAtTheEnd(Enemy enemy) {
        if (enemy.getX() == end.getxCoord() * 32)
            return enemy.getY() == end.getyCoord() * 32;
        return false;
    }

    /**
     * Adds a new enemy of the specified type to the game.
     */
    public void addEnemy(int enemyType) {
        int x = start.getxCoord() * 32;
        int y = start.getyCoord() * 32;

        if (enemyType == MONSTER)
            enemies.add(new Monster(x, y, 0, this));
        else if (enemyType == ROBOT)
            enemies.add(new Robot(x, y, 0, this));
        else if (enemyType == GHOST)
            enemies.add(new Ghost(x, y, 0, this));
    }

    /**
     * Draws enemies on the game canvas.
     */
    public void draw(GraphicsContext gc) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                drawEnemy(gc, enemy);
                drawHealthLine(gc, enemy);
                drawSlowedEffect(gc, enemy);
            }
        }
    }

    /**
     * Draws the visual effect indicating that the enemy is slowed down.
     *
     * @param gc    The graphics context used for drawing.
     * @param enemy The enemy for which the effect is drawn.
     */
    private void drawSlowedEffect(GraphicsContext gc, Enemy enemy) {
        if (enemy.isSlowed()) {
            gc.drawImage(slowEffectImage, enemy.getX(), enemy.getY());
        }
    }

    /**
     * Draws a health bar above the enemy to visualize its remaining health.
     *
     * @param gc    The graphics context used for drawing.
     * @param enemy The enemy for which the health bar is drawn.
     */
    private void drawHealthLine(GraphicsContext gc, Enemy enemy) {
        gc.setFill(Color.RED);
        gc.fillRect(enemy.getX() + 16 - ((double) getNewHealthLineWidth(enemy) / 2), enemy.getY() - 5,getNewHealthLineWidth(enemy), 3);
    }

    /**
     * Draws the image representing the enemy on the game canvas.
     *
     * @param gc    The graphics context used for drawing.
     * @param enemy The enemy to be drawn.
     */
    private void drawEnemy(GraphicsContext gc, Enemy enemy) {
        gc.drawImage(enemyImages[enemy.getEnemyType()], (int)enemy.getX(), (int)enemy.getY());
    }

//    Getters
    private float getSpeedAndWidth(int direction, int enemyType) {
        if (direction == LEFT)
            return -getSpeed(enemyType);
        else if (direction == RIGHT)
            return getSpeed(enemyType) + 32;
        return 0;
    }

    private float getSpeedAndHeight(int direction, int enemyType) {
        if (direction == UP)
            return -getSpeed(enemyType);
        else if (direction == DOWN)
            return getSpeed(enemyType) + 32;
        return 0;
    }

    private int getNewHealthLineWidth(Enemy enemy) {
        return (int) (healthLineWidth * enemy.getHealthLine());
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int getAmountOfAliveEnemies() {
        int amountOfAliveEnemies = 0;

        for (Enemy e : enemies)
            if (e.isAlive())
                amountOfAliveEnemies++;

        return amountOfAliveEnemies;
    }

    private int getBlockType(int x, int y) {
        return playing.getBlockType(x,y);
    }

    public Playing getPlaying() {
        return playing;
    }

    public PathCoordinate getStart() {
        return start;
    }

    public PathCoordinate getEnd() {
        return end;
    }
}
