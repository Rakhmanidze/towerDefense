package cz.cvut.fel.pjv.semak_pjv.helperMethods;

public class GameData { //   this class will have game's mechanics and behaviour
    /**
     * Static inner class containing constants for directions.
     */
    public static class Direction {
        public static final int RIGHT = 2;
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int DOWN = 3;
    }

    /**
     * Static inner class containing constants for block types and methods to get block type strings.
     */
    public static class Blocks {
        public static final int WATER_BLOCK = 0;
        public static final int GRASS_BLOCK = 1;
        public static final int ROAD_BLOCK = 2;

        public static String getBLockTypeString(int blockType){
            if (blockType == 0)
                return "Water block";
            else if(blockType == 1)
                return "Grass block";
            else if (blockType == 2)
                return "Road block";
            else
                return "";
        }
    }

    public static class Enemies {

        public static final int MONSTER = 0;
        public static final int ROBOT = 1;
        public static final int GHOST = 2;

        public static int getCoinsForKilling(int enemyType){
            if (enemyType == MONSTER)
                return 15;
            else if (enemyType == ROBOT)
                return 20;
            else if (enemyType == GHOST)
                return 10;
            else
                return 0;
        }

        public static float getSpeed(int enemyType){
            if (enemyType == MONSTER)
                return 0.45f;
            else if (enemyType == ROBOT)
                return 0.35f;
            else if (enemyType == GHOST)
                return 0.55f;
            else
                return 0;
        }

        public static int getDefaultHealth(int enemyType) {
            if (enemyType == MONSTER)
                return 100;
            else if (enemyType == ROBOT)
                return 150;
            else if (enemyType == GHOST)
                return 80;
            else
                return 0;
        }
    }

    /**
     * Static inner class containing constants for tower types and methods to get related properties.
     */
    public static class Towers {
        public static final int ARCHER = 0;
        public static final int CANNON = 1;
        public static final int WIZARD = 2;

        public static String convertTowerTypeToString(int towerType) {
            if (towerType == ARCHER)
                return "Archer";
            else if (towerType == CANNON)
                return "Cannon";
            else if (towerType == WIZARD)
                return "Wizard";
            return "";
        }

        public static int getTowerCost(int towerType) {
            if (towerType == ARCHER)
                return 30;
            else if (towerType == CANNON)
                return 50;
            else if (towerType == WIZARD)
                return 20;
            return 0;
        }

        public static int getDefaultDamage(int towerType) {
            if (towerType == ARCHER)
                return 25;
            else if (towerType == CANNON)
                return 50;
            else if (towerType == WIZARD)
                return 15;
            return 0;
        }

        public static float getDefaultDamageArea(int towerType) {
            if (towerType == ARCHER)
                return 140;
            else if (towerType == CANNON)
                return 120;
            else if (towerType == WIZARD)
                return 100;
            return 0;
        }

        public static float getDefaultDamageInterval(int towerType) {
            if (towerType == ARCHER)
                return 60;
            else if (towerType == CANNON)
                return 130;
            else if (towerType == WIZARD)
                return 100;
            return 0;
        }

        public static float getDefaultLifeTimeInSeconds(int towerType) {
            if (towerType == ARCHER)
                return 75;
            else if (towerType == CANNON)
                return 80;
            else if (towerType == WIZARD)
                return 70;
            return 0;
        }
    }

    /**
     * Static inner class containing constants for projectile types and methods to get related properties.
     */
    public static class Projectiles {
        public static final int ARROW = 0;
        public static final int BOMB = 1;
        public static final int SPELL = 2;
        public static float getSpeed(int type) {
            if (type == ARROW)
                return 5.5f;
            else if (type == BOMB)
                return 4f;
            else if (type == SPELL)
                return 4f;
            return 0;
        }
    }
}
