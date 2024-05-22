package cz.cvut.fel.pjv.semak_pjv.events;

import java.util.ArrayList;

/**
 * Represents a wave of enemies in the game.
 */
public class Wave {
    private final ArrayList<Integer> enemyList; // List of enemy types in the wave.

    /**
     * Constructs a Wave object with the specified list of enemy types.
     *
     * @param enemyList  the list of enemy types in the wave
     */
    public Wave(ArrayList<Integer> enemyList) {
        this.enemyList = enemyList;
    }

//    Getters
    public ArrayList<Integer> getEnemyList() {
        return enemyList;
    }
}