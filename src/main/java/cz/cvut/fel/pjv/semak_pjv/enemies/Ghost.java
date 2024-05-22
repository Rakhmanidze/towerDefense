package cz.cvut.fel.pjv.semak_pjv.enemies;

import cz.cvut.fel.pjv.semak_pjv.handlers.EnemyHandler;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.GHOST;

/**
 * Represents a Ghost enemy.
 */
public class Ghost extends Enemy {
    /**
     * Constructs a Ghost enemy object with the specified position, identification number, and enemy handler.
     *
     * @param x             the x-coordinate of the Ghost's position
     * @param y             the y-coordinate of the Ghost's position
     * @param id            the identification number of the Ghost
     * @param enemyHandler  the EnemyHandler object responsible for managing enemies
     */
    public Ghost(float x, float y, int id, EnemyHandler enemyHandler) {
        super(x, y, id, GHOST, enemyHandler);
    }
}