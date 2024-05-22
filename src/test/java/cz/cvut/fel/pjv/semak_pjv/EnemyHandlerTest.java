package cz.cvut.fel.pjv.semak_pjv;

import cz.cvut.fel.pjv.semak_pjv.enemies.*;
import cz.cvut.fel.pjv.semak_pjv.handlers.EnemyHandler;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData;
import cz.cvut.fel.pjv.semak_pjv.objects.PathCoordinate;
import cz.cvut.fel.pjv.semak_pjv.scenes.Playing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.ROAD_BLOCK;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.MONSTER;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Enemies.getSpeed;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class EnemyHandlerTest {
        @Mock
        private EnemyHandler enemyHandler;
        @Mock
        private Playing playing;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.initMocks(this);
            playing = mock(Playing.class);
        }

        @Test
        public void testMonsterSelfAttack() {
            Enemy monster = new Monster(0, 0, 1, enemyHandler);
            int initialHealth = monster.getHealth();
            int damage = 50;
            monster.selfAttack(damage);
            int expectedHealth = initialHealth - damage;

            System.out.println("actual: " + monster.getHealth());
            System.out.println("expectedHealth: " + expectedHealth);

            // Assert
            Assertions.assertEquals(expectedHealth, monster.getHealth());
        }

        @Test
        public void testUpdateStartAndEnd() {
            // Arrange
            enemyHandler = new EnemyHandler(playing, new PathCoordinate(0, 0), new PathCoordinate(5, 0));

            // Act
            PathCoordinate newStart = new PathCoordinate(1, 1);
            PathCoordinate newEnd = new PathCoordinate(6, 0);

            enemyHandler.updateStartAndEnd(newStart, newEnd);
            System.out.println("startX: " + enemyHandler.getStart().getxCoord() + " startY: " + enemyHandler.getStart().getyCoord());
            System.out.println("endX: " + enemyHandler.getEnd().getxCoord() + " endY: " + enemyHandler.getEnd().getyCoord());

            //Assert
            Assertions.assertEquals(newStart, enemyHandler.getStart());
            Assertions.assertEquals(newEnd, enemyHandler.getEnd());
        }

        @Test
        public void testAddEnemy() {
            // Mocking the behavior of Playing.getBlockType to return ROAD_BLOCK
            when(playing.getBlockType(anyInt(), anyInt())).thenReturn(ROAD_BLOCK);

            // Arrange
            enemyHandler = new EnemyHandler(playing, new PathCoordinate(0, 0), new PathCoordinate(5, 0));

            // Adding enemies
            enemyHandler.addEnemy(MONSTER);
            enemyHandler.addEnemy(GameData.Enemies.ROBOT);
            enemyHandler.addEnemy(GameData.Enemies.GHOST);

            // Assert
            Assertions.assertEquals(3, enemyHandler.getEnemies().size());
        }
}
