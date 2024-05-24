package cz.cvut.fel.pjv.semak_pjv.handlers;

import cz.cvut.fel.pjv.semak_pjv.enemies.Enemy;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.objects.Projectile;
import cz.cvut.fel.pjv.semak_pjv.objects.Tower;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Projectiles.*;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Towers.*;

/**
 * The ProjectileHandler class manages the creation, movement, collision detection, and rendering of projectiles and explosions
 * within the game. It handles the firing of projectiles from towers towards enemies, updates their positions and states,
 * checks for collisions with enemies, and renders both active projectiles and explosions on the game canvas.
 */
public class ProjectileHandler {
    private final Playing playing;
    public final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Image[] projectiles_images, explosion_images;
    private int projectile_id = 0;
    private final ArrayList<Explosion> explosions =  new ArrayList<>();

    /**
     * Constructs a ProjectileHandler object to manage projectiles and explosions within the game.
     *
     * @param playing  the Playing object representing the game area
     */
    public ProjectileHandler(Playing playing) {
        this.playing = playing;
        projectiles_images = new Image[3];
        explosion_images = new Image[4];
        importImages();
    }

    /**
     * Imports images for projectiles and explosions from a sprite sheet and initializes the respective arrays.
     * This method reads the pixel data from the sprite sheet image, extracts regions corresponding to different
     * projectile and explosion images, and creates WritableImage instances for each. These images are stored in
     * the 'projectiles_images' and 'explosion_images' arrays for later use.
     *
     * @see #importExplosion(PixelReader) importExplosion
     */
    private void importImages() {
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        projectiles_images[0]  = new WritableImage(reader, 3 * 32, 3 * 32, 32, 32);
        projectiles_images[1]  = new WritableImage(reader, 0, 32, 32, 32);
        projectiles_images[2]  = new WritableImage(reader, 2 * 32, 2 * 32, 32, 32);

        importExplosion(reader);
    }

    /**
     * Imports explosion images from the sprite sheet and initializes the explosion_images array.
     * This method reads the pixel data from the sprite sheet image, extracts regions corresponding to
     * different stages of explosion animation, and creates WritableImage instances for each stage. These
     * images are stored in the 'explosion_images' array for later use.
     *
     * @param reader The PixelReader instance used to read pixel data from the sprite sheet image.
     * @see #importImages() importImages
     */
    private void importExplosion(PixelReader reader) {
        for (int i = 0; i < 4; i++)
            explosion_images[i] = new WritableImage(reader, i * 32, 4 * 32, 32, 32);
    }

    /**
     * Creates a new projectile fired from a tower at an enemy.
     *
     * @param t The tower firing the projectile.
     * @param e The enemy being targeted by the projectile.
     */
    public void newProjectile(Tower t, Enemy e) {
        int type = getProjectileType(t);

        int xDistance = (int) (e.getX() - t.getX());
        int yDistance = (int) (e.getY() - t.getY());
        int totalDistance = Math.abs(xDistance) + Math.abs(yDistance);

        float xPercentage = (float) Math.abs(xDistance) / totalDistance;
        float yPercentage = (float) Math.abs(yDistance) / totalDistance;

        float xSpeed = xPercentage * getSpeed(type);
        float ySpeed = yPercentage * getSpeed(type);

        if (t.getX() > e.getX())
            xSpeed *= -1;
        if (t.getY() > e.getY())
            ySpeed *= -1;

//        if (t.getX() > e.getX())
//            xSpeed *= -1;
//        else
//            xSpeed *= 1;
//
//        if (t.getY() > e.getY())
//            ySpeed *= -1;
//        else
//            ySpeed *= 1;

        float rotate = 0;
        if (type == ARROW) {
            float angle = (float) Math.atan((double) yDistance / xDistance);
            rotate = (float) Math.toDegrees(angle);

            if (xDistance < 0)
                rotate += 180;
        }

        for (Projectile projectile: projectiles)
            if (!projectile.isActive() && projectile.getProjectileType() == type) {
                projectile.reuse(t.getX() + 16,t.getY() + 16, xSpeed, ySpeed, t.getDamage(), rotate);
                return;
            }


        projectiles.add(new Projectile(t.getX() + 16,t.getY() + 16, xSpeed, ySpeed, t.getDamage(), rotate, projectile_id++, type));

    }

    /**
     * Updates the positions and states of projectiles and explosions.
     */
    public void update() {
        // tmp
        printProjectilesSize();
        for (Projectile pt : projectiles)
            if (pt.isActive()) {
                pt.move();
                if (IsProjectileDamageEnemy(pt)) {
                    pt.setActive(false);
                    if (pt.getProjectileType() == BOMB) {
                        explosions.add(new Explosion(pt.getPosition()));
                    }
                } else if (isProjOutOfGame(pt))
                    pt.setActive(false);
            }

        for (Explosion e : explosions)
            e.update();
    }

//    tmp
    boolean printSize =  false;
    float lastPrint = 0;
    private void printProjectilesSize() {
        if (printSize) {
            System.out.println("Project size: " + projectiles.size());
            printSize = false;
        } else {
            if (System.currentTimeMillis() >= lastPrint + 65000) {
                printSize = true;
                lastPrint = System.currentTimeMillis();
            }
        }
    }

    /**
     * Checks if the projectile is out of the game area.
     * This method determines whether the projectile's position is outside the boundaries of the game area,
     * which is defined as a 640x640 grid. If the projectile's position is outside these boundaries, it is
     * considered out of the game.
     *
     * @param pt The projectile to check.
     * @return True if the projectile is out of the game area, false otherwise.
     */
    private boolean isProjOutOfGame(Projectile pt) {
        if (pt.getPosition().x >= 0)
            if (pt.getPosition().x <= 640)
                if (pt.getPosition().y >= 0)
                    return !(pt.getPosition().y <= 640);
        return true;
    }

    /**
     * Checks if the projectile damages any enemy.
     * This method iterates through all the enemies in the game and checks if the projectile's position overlaps
     * with the bounding box of any alive enemy. If an overlap is found, the enemy's health is reduced by the damage
     * inflicted by the projectile. If the projectile type is SPELL, the enemy is also slowed down.
     *
     * @param pt The projectile to check for damage.
     * @return True if the projectile damages an enemy, false otherwise.
     */
    private boolean IsProjectileDamageEnemy(Projectile pt) {
        for (Enemy e : playing.getEnemyHandler().getEnemies())
            if (e.isAlive() && e.getBounds().contains(pt.getPosition())) {
                e.selfAttack(pt.getDamage());
                if (pt.getProjectileType() == SPELL)
                    e.slow();
                return true;
            }
        return false;
    }

    /**
     * Resets the list of projectiles and explosions.
     */
    public void reset() {
        projectiles.clear();
        explosions.clear();

        projectile_id = 0;
    }

    /**
     * Draws the active projectiles and explosions on the game canvas.
     *
     * @param gc The graphics context used for drawing.
     */
    public void draw(GraphicsContext gc) {
        for (Projectile pt : projectiles)
            if (pt.isActive()) {
                if (pt.getProjectileType() == ARROW) {
                    gc.save();
                    gc.translate(pt.getPosition().x, pt.getPosition().y);
                    gc.rotate(pt.getRotation() + 180);
                    gc.drawImage(projectiles_images[pt.getProjectileType()], -16, -16);
                    gc.restore();
                } else {
                    gc.drawImage(projectiles_images[pt.getProjectileType()], pt.getPosition().x - 16, pt.getPosition().y - 16);
                }
            }

        drawExplosion(gc);
    }

    /**
     * Draws explosions on the game canvas.
     * This method iterates through the list of explosions and draws them on the game canvas using the graphics context.
     * Each explosion is represented by a series of images, and this method draws the explosion image corresponding to
     * the current index of the explosion object. The explosions are drawn at their respective positions with an offset
     * of 16 pixels to center them on the canvas.
     *
     * @param gc The graphics context used to draw on the game canvas.
     */
    private void drawExplosion(GraphicsContext gc) {
        for (Explosion e : explosions)
            if (e.getIndex() < 4)
                gc.drawImage(explosion_images[e.getIndex()], e.getPosition().x - 16, e.getPosition().y - 16);
    }

//    Getters
    private int getProjectileType(Tower t) {
        if (t.getTowerType() == ARCHER)
            return ARROW;
        else if (t.getTowerType() == CANNON)
            return BOMB;
        else if (t.getTowerType() == WIZARD)
            return SPELL;
        return 0;
    }

    /**
     * Represents an explosion used in the game.
     * This inner class is defined within the ProjectileHandler class because it is only used internally
     * within that class. An Explosion object contains information about its position and the current
     * state of the explosion animation, which is updated over time.
     */
    public static class Explosion {
        private final Point2D.Float position;
        private int explosionCounter = 0, explosionIndex = 0;

        /**
         * Constructs a new Explosion object with the given position.
         *
         * @param position The position of the explosion.
         */
        public Explosion(Point2D.Float position) {
            this.position = position;
        }

        /**
         * Updates the explosion animation state.
         * This method increments the explosion counter and updates the explosion index based on the
         * current state of the animation. If the explosion counter reaches a certain threshold, the
         * animation resets to its initial state.
         */
        public void update() {
            explosionCounter++;
            if (explosionCounter >= 12) {
                explosionCounter = 0;
                explosionIndex++;
            }
        }

//        Getters
        public int getIndex() {
            return explosionIndex;
        }

        public Point2D.Float getPosition() {
            return position;
    }
    }
}
