package cz.cvut.fel.pjv.semak_pjv;

import cz.cvut.fel.pjv.semak_pjv.events.Wave;
import cz.cvut.fel.pjv.semak_pjv.handlers.WaveHandler;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import javafx.application.Platform;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WaveHandlerTest {
    private WaveHandler waveHandler;

    @BeforeMethod
    public void setUp() {
        // Check if the JavaFX Toolkit is already initialized
        if (!Platform.isFxApplicationThread()) {
            // Create a CountDownLatch to wait for the JavaFX runtime to start
            CountDownLatch latch = new CountDownLatch(1);

            // Start the JavaFX runtime asynchronously
            Platform.startup(() -> {
                // Execute the test setup code on the JavaFX Application Thread
                waveHandler = createWaveHandler();

                // Signal that the JavaFX runtime has started
                latch.countDown();
            });

            // Wait for the JavaFX runtime to start for up to 5 seconds
            try {
                latch.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for JavaFX runtime to start", e);
            }
        } else {
            // If the JavaFX Toolkit is already initialized, create waveHandler directly
            waveHandler = createWaveHandler();
        }
    }

    @Test
    public void testCreateWaves() {
        // arrange
        ArrayList<Integer> expectedFirstWave = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 2));
        ArrayList<Integer> expectedSecondWave = new ArrayList<>(Arrays.asList(0, 0, 1, 1, 2, 2, 2, 1, 1, 0));

        // act
        Assert.assertNotNull(waveHandler);
        ArrayList<Wave> waves = waveHandler.getWaves();

        // assert
        Assert.assertNotNull(waves);
        Assert.assertEquals(2, waves.size());
        Assert.assertEquals(expectedFirstWave, waves.get(0).getEnemyList());
        Assert.assertEquals(expectedSecondWave, waves.get(1).getEnemyList());
    }

    private WaveHandler createWaveHandler() {
        TowerDefense towerDefense = new TowerDefense();
        Playing mockPlaying = new Playing(towerDefense);
        return new WaveHandler(mockPlaying);
    }
}