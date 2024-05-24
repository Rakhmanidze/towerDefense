package cz.cvut.fel.pjv.semak_pjv.objects;

import java.awt.geom.Point2D;

/**
 * Represents a projectile in the game.
 */
public class Projectile {
    private Point2D.Float position;
    private int id, projectileType, damage;
    private boolean active = true;
    private float xSpeed, ySpeed, rotation;

    /**
     * Constructs a Projectile object with the specified parameters.
     * @param x The initial x-coordinate of the projectile.
     * @param y The initial y-coordinate of the projectile.
     * @param xSpeed The speed of the projectile along the x-axis.
     * @param ySpeed The speed of the projectile along the y-axis.
     * @param damage The damage inflicted by the projectile.
     * @param rotation The rotation angle of the projectile.
     * @param id The unique identifier of the projectile.
     * @param projectileType The type of the projectile.
     */
    public Projectile(float x, float y, float xSpeed, float ySpeed, int damage, float rotation, int id, int projectileType) {
        position = new Point2D.Float(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.damage = damage;
        this.rotation = rotation;
        this.id = id;
        this.projectileType = projectileType;
    }

    public void reuse(int x, int y, float xSpeed, float ySpeed, int damage, float rotate) {
        position = new Point2D.Float(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.damage = damage;
        this.rotation = rotate;
        active = true;
    }

    /**
     * Moves the projectile based on its speed.
     */
    public void move() {
        position.x += xSpeed;
        position.y += ySpeed;
    }

//    Getters and setters
    public Point2D.Float getPosition() {
        return position;
    }

    public void setPosition(Point2D.Float position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getProjectileType() {
        return projectileType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDamage() {
        return damage;
    }

    public float getRotation() {
        return rotation;
    }
}
