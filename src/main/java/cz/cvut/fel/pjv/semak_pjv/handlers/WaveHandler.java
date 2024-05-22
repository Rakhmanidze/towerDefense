package cz.cvut.fel.pjv.semak_pjv.handlers;

import cz.cvut.fel.pjv.semak_pjv.events.Wave;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manages waves of enemies in the game, including their creation, timing, and progression.
 */
public class WaveHandler {
    private final Playing playing;
    private final ArrayList<Wave> waves =new ArrayList<>();
    private final int maxEnemySpawnCounter = 60;
    private int enemySpawnCounter = maxEnemySpawnCounter;
    private int enemyIndex, waveIndex;
    private final int maxWaveCounter = 60 * 3; // 3 seconds
    private int waveCounter = 0;
    private boolean waveStartCounter, currentWaveCounterFinished;

    /**
     * Constructs a WaveHandler object to manage waves of enemies within the game.
     *
     * @param playing  the Playing object representing the game area
     */
    public WaveHandler(Playing playing) {
        this.playing = playing;
        createWaves();
    }

    /**
     * Updates the wave handler, including wave and enemy counters.
     */
    public void update() {
        if (enemySpawnCounter < maxEnemySpawnCounter)
            enemySpawnCounter++;

        if (waveStartCounter) {
            waveCounter++;
            if (waveCounter >= maxWaveCounter) {
                currentWaveCounterFinished = true;
            }
        }
    }

    public void startWaveCounter() {
        waveStartCounter = true;
    }

    public boolean isWaveCounterFinished() {
        return currentWaveCounterFinished;
    }

    /**
     * Increases the wave index and resets counters for the next wave.
     */
    public void increaseWaveIndex () {
        waveIndex++;
        waveCounter = 0;
        currentWaveCounterFinished = false;
        waveStartCounter = false;
    }

    /**
     * Checks if it is time to spawn the next enemy.
     *
     * @return True if it is time to spawn the next enemy, false otherwise.
     */
    public boolean isTimeForSpawningNextEnemy() {
        return enemySpawnCounter >= maxEnemySpawnCounter;
    }

    public void resetEnemyIndex() {
        enemyIndex = 0;
    }

    /**
     * Creates waves of enemies.
     */
    private void createWaves() {
        waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(1,1,1,1,1,1,1,1,1,2))));
        waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(0,0,1,1,2,2,2,1,1,0))));
    }


    /**
     * Resets the wave handler to its initial state.
     */
    public void reset() {
        waves.clear();
        createWaves();
        enemyIndex = 0;
        waveIndex = 0;
        waveStartCounter = false;
        currentWaveCounterFinished = false;
        enemySpawnCounter = maxEnemySpawnCounter;
    }

//    Getters
    public boolean isThereNextEnemyInWave() {
        return enemyIndex < waves.get(waveIndex).getEnemyList().size();
    }

    public boolean isNextWaveExists() {
        return waveIndex + 1 < waves.size();
    }

    public boolean isWaveCounterStarted() {
        return  waveStartCounter;
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

    public int getNextEnemy() {
        enemySpawnCounter = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public float getRemainingTime() {
        float remainingCounter = maxWaveCounter - waveCounter;
        return remainingCounter / 60f;
    }
}
