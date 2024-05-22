package cz.cvut.fel.pjv.semak_pjv.enemies;

import cz.cvut.fel.pjv.semak_pjv.handlers.EnemyHandler;
import java.awt.*;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Direction.*;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.*;

/**
 * The abstract Enemy class represents a general enemy in the game.
 * It serves as a base class for specific types of enemies like Monster, Robot, and Ghost.
 * The class handles the enemy's position, movement, health, hitbox and status(alive or not).
 */
public abstract class Enemy {
    protected float x, y;// Using float for precise speed handling.
    protected final Rectangle bounds;// Hitbox for detecting damage.
    protected int health;
    protected int maxHealth;
    protected final int id;
    protected final int enemyType;
    protected int lastDirection;
    protected boolean alive = true;
    protected int maxSlowCounter = 100;  // Maximum counter for the slow effect duration.
    protected int slowCounter = maxSlowCounter;
    protected EnemyHandler enemyHandler;

    /**
     * Constructs an Enemy with specified position, ID, type, and enemy handler.
     *
     * @param x           The initial x-coordinate of the enemy.
     * @param y           The initial y-coordinate of the enemy.
     * @param id          The unique identifier for the enemy.
     * @param enemyType   The type identifier for the enemy.
     * @param enemyHandler The handler responsible for managing the enemy.
     */
    public Enemy(float x, float y, int id, int enemyType, EnemyHandler enemyHandler) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.enemyType = enemyType;
        this.enemyHandler = enemyHandler;
        bounds = new Rectangle((int)x, (int)y, 32, 32);
        lastDirection = -1;
        setDefaultHealth();
    }

    /**
     * Inflicts damage to the enemy and checks if the enemy should be marked as dead.
     *
     * @param damage The amount of damage to inflict.
     */
    public void selfAttack(int damage) {
        this.health -= damage;
        if (health <= 0) {
            alive = false;
            enemyHandler.giveCoinsForKillingEnemy(enemyType);
        }

    }

    /**
     * Applies a slow effect to the enemy.
     */
    public void slow() {
        slowCounter = 0;
    }

    /**
     * Moves the enemy in a specified direction at a given speed.
     * If the enemy is slowed, the speed is halved.
     *
     * @param direction The direction to move (RIGHT, LEFT, UP, DOWN).
     * @param speed     The speed at which to move the enemy.
     */
    public void move(int direction, float speed) {
        lastDirection = direction;

        if (slowCounter < maxSlowCounter) {
            slowCounter++;
            speed *= 0.5f;
        }

        if (direction == RIGHT)
            this.x += speed;
        else if (direction == LEFT)
            this.x -= speed;
        else if (direction == UP)
            this.y -= speed;
        else if (direction == DOWN)
            this.y += speed;

        updateHurtArea(); // Update the hitbox position after moving.
    }


    /**
     * Updates the position of the hitbox to match the enemy's current position.
     */
    private void updateHurtArea() {
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    /**
     * Instantly kills the enemy by setting its health to 0 and marking it as dead.
     */
    public void die() {
        health = 0;
        alive = false;
    }

    /**
     * Sets the default health based on the enemy type.
     */
    private void setDefaultHealth () {
        health = getDefaultHealth(enemyType);
        maxHealth = health;
    }

    /**
     * Returns the health ratio of the enemy, used for health bar representation.
     *
     * @return The ratio of current health to maximum health.
     */
    public float getHealthLine() {
        return (float) health / maxHealth;
    }

    /**
     * Sets the enemy's position, used for fixing position issues.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    Getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHealth() {
        return health;
    }

    public int getId() {
        return id;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getLastDirection() {
        return lastDirection;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isSlowed() {
        return slowCounter < maxSlowCounter;
    }

    public void setLastDirection(int lastDirection) {
        this.lastDirection = lastDirection;
    }
}
