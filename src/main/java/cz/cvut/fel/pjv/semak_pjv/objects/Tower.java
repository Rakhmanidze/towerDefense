package cz.cvut.fel.pjv.semak_pjv.objects;

import cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Towers.*;

/**
 * Represents a tower in the game.
 */
public class Tower {
    private int x, y, id, towerType, intervalCounter, damage;
    private float damageArea, damageInterval;
    protected int lifeTime;
    private int maxLifetime;

    /**
     * Constructs a Tower object with the specified parameters.
     *
     * @param x          the x-coordinate of the tower
     * @param y          the y-coordinate of the tower
     * @param id         the identification number of the tower
     * @param towerType  the type of the tower
     * @param lifeTime   the lifetime of the tower
     */
    public Tower(int x, int y, int id, int towerType, int lifeTime) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        setDefaultDamage();
        setDefaultDamageArea();
        setDefaultDamageInterval();

        setDefaultLifetime();
        this.lifeTime = maxLifetime;
    }

    public void update() {
        intervalCounter++;
    }

    public void restartInterval() {
        intervalCounter = 0;
    }

//    Getters and setters

    private void setDefaultLifetime() {
        maxLifetime = (int) GameData.Towers.getDefaultLifeTimeInSeconds(towerType);
    }

    public boolean isIntervalEnd() {
        return intervalCounter >= damageInterval;
    }

    private void setDefaultDamage() {
        damage = getDefaultDamage(towerType);
    }

    private void setDefaultDamageArea() {
        damageArea = getDefaultDamageArea(towerType);
    }

    private void setDefaultDamageInterval() {
        damageInterval = getDefaultDamageInterval(towerType);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTowerType() {
        return towerType;
    }

    public void setTowerType(int towerType) {
        this.towerType = towerType;
    }

    public int getDamage() {
        return damage;
    }

    public float getDamageArea() {
        return damageArea;
    }

    public float getDamageInterval() {
        return damageInterval;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public float getLifeTimeLine() {
        return (float) lifeTime / maxLifetime;
    }

    public void decreaseLifetime() {
        lifeTime--;
    }
}
