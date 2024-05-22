package cz.cvut.fel.pjv.semak_pjv.handlers;

import cz.cvut.fel.pjv.semak_pjv.enemies.Enemy;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.objects.Tower;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;

import static cz.cvut.fel.pjv.semak_pjv.helperMethods.LevelNavigator.getDistanceBetweenPoints;

/**
 * Manages towers in the game, including their placement, updates, and rendering.
 */
public class TowerHandler {
    private final Playing playing;
    private Image[] towerImages;
    private final ArrayList<Tower> towers = new ArrayList<>();
    private int towerCounter = 0;
    private final int lifeTimeUpdateInterval = 1000;  // Update interval for tower lifetime (1 second in milliseconds)
    private long lastLifeTimeUpdate;
    private final int lifeTimeLineWidth = 20;

    /**
     * Constructs a TowerHandler object to manage towers within the game.
     *
     * @param playing  the Playing object representing the game area
     */
    public TowerHandler(Playing playing) {
        this.playing = playing;

        lastLifeTimeUpdate = System.currentTimeMillis();
        loadTowerImages();
    }

    /**
     * Loads tower images from the image file.
     */
    private void loadTowerImages() {
        towerImages = new Image[3];
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        towerImages[0]  = new WritableImage(reader, 32, 32, 32, 32);
        towerImages[1]  = new WritableImage(reader, 2*32, 32, 32, 32);
        towerImages[2]  = new WritableImage(reader, 0, 2*32, 32, 32);
    }

    /**
     * Updates the tower handler, including tower lifetime and damage to enemies.
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLifeTimeUpdate >= lifeTimeUpdateInterval) {
            updateLifeTime();
            lastLifeTimeUpdate = currentTime;
        }

        for (Tower t : towers) {
            t.update();
            damageEnemyWhenIsClose(t);
        }
    }

    /**
     * Updates the lifetime of towers and removes expired towers.
     */
    private void updateLifeTime() {
        Iterator<Tower> iterator = towers.iterator();
        while (iterator.hasNext()) {
            Tower t = iterator.next();
            t.decreaseLifetime();

            if (t.getLifeTime() <= 0)
                iterator.remove();
        }
    }

    /**
     * Damages enemies within the range of the tower.
     *
     * @param t The tower.
     */
    private void damageEnemyWhenIsClose(Tower t) {
        for (Enemy e : playing.getEnemyHandler().getEnemies()) {
            if (e.isAlive())
                if (isEnemyInDamageArea(t, e)) {
                    if (t.isIntervalEnd()) {
                        playing.shootEnemy(t, e);
                        t.restartInterval();
                    }
                }
        }
    }

    /**
     * Resets the tower handler by clearing the list of towers.
     */
    public void reset() {
        towers.clear();
        towerCounter = 0;
    }

    /**
     * Determines if an enemy is within the damage area of a tower.
     *
     * @param t The tower.
     * @param e The enemy.
     * @return True if the enemy is within the tower's damage area, false otherwise.
     */
    private boolean isEnemyInDamageArea(Tower t, Enemy e) {
        int damageArea = getDistanceBetweenPoints(t.getX(), t.getY(), e.getX(), e.getY());
        return damageArea < t.getDamageArea();
    }

    /**
     * Adds a tower to the tower handler.
     *
     * @param selectedTower The selected tower type.
     * @param xCoord The x-coordinate of the tower.
     * @param yCoord The y-coordinate of the tower.
     */
    public void addTower(Tower selectedTower, int xCoord, int yCoord) {
        int towerType = selectedTower.getTowerType();
        towers.add(new Tower(xCoord, yCoord, towerCounter++, selectedTower.getTowerType(), towerType));
    }


    /**
     * Draws towers and their lifetime indicators on the canvas.
     *
     * @param gc The graphics context used for drawing.
     */
    public void draw(GraphicsContext gc) {
        for (Tower t: towers) {
            gc.drawImage(towerImages[t.getTowerType()], t.getX(), t.getY());
            drawLifeTimeLine(gc, t);
        }
    }

    /**
     * Draws the tower's lifetime indicator on the canvas.
     *
     * @param gc The graphics context used for drawing.
     * @param tower The tower.
     */
    private void drawLifeTimeLine(GraphicsContext gc, Tower tower) {
        gc.setFill(Color.RED);
        gc.fillRect(tower.getX() + 16 - ((double) getNewLifeTimeLineWidth(tower) / 2), tower.getY(),getNewLifeTimeLineWidth(tower), 3);
    }

//    Getters
    private int getNewLifeTimeLineWidth(Tower tower) {
        return (int) (lifeTimeLineWidth * tower.getLifeTimeLine());
    }

    public Image[] getTowerImages() {
        return towerImages;
    }

    public Tower getTowerAtCoord(int x, int y) {
        for (Tower t: towers)
            if (t.getX() == x && t.getY() == y)
                return t;
        return null;
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }
}