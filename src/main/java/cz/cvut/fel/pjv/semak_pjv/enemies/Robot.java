package cz.cvut.fel.pjv.semak_pjv.enemies;

import cz.cvut.fel.pjv.semak_pjv.handlers.EnemyHandler;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.ROBOT;

/**
 * Represents a Robot enemy.
 */
public class Robot extends Enemy {
    /**
     * Constructs a Robot enemy object with the specified position, identification number, and enemy handler.
     *
     * @param x             the x-coordinate of the Ghost's position
     * @param y             the y-coordinate of the Ghost's position
     * @param id            the identification number of the Ghost
     * @param enemyHandler  the EnemyHandler object responsible for managing enemies
     */
    public Robot(float x, float y, int id, EnemyHandler enemyHandler) {
        super(x, y, id, ROBOT, enemyHandler);
    }
}