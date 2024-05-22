package cz.cvut.fel.pjv.semak_pjv;

import cz.cvut.fel.pjv.semak_pjv.handlers.TowerHandler;
import cz.cvut.fel.pjv.semak_pjv.objects.Tower;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TowerHandlerTest {
    @Test
    public void testAddTowerWithMock() {
        // Arrange
        Playing playingMock = mock(Playing.class);

        Tower towerMock1 = mock(Tower.class);
        Tower towerMock2 = mock(Tower.class);
        Tower towerMock3 = mock(Tower.class);

        // Stub the behavior of the tower mocks
        when(towerMock1.getTowerType()).thenReturn(1);
        when(towerMock2.getTowerType()).thenReturn(2);
        when(towerMock3.getTowerType()).thenReturn(3);

        TowerHandler towerHandler = new TowerHandler(playingMock);

        // Act
        towerHandler.addTower(towerMock1, 20, 20);
        towerHandler.addTower(towerMock2, 30, 30);
        towerHandler.addTower(towerMock3, 40, 40);

        // Assert
        assertEquals(3, towerHandler.getTowers().size());
    }

    @Test
    public void testReset() {
        TowerHandler mockedTowerHandler = mock(TowerHandler.class);

        // Arrange
        Tower tower = new Tower(10, 10, 0, 1, 1);
        Mockito.when(mockedTowerHandler.getTowers()).thenReturn(new ArrayList<>()); // Mock the behavior of getTowers()

        // Act
        mockedTowerHandler.addTower(tower, 10, 10);
        mockedTowerHandler.reset();

        // Assert
        Assertions.assertEquals(0, mockedTowerHandler.getTowers().size());
    }
}
