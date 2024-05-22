package cz.cvut.fel.pjv.semak_pjv.scenes;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import javafx.scene.image.Image;

/**
 * Represents a game scene in the Tower Defense game.
 * This class serves as the base class for all scenes and provides common functionality.
 */
public class GameScene {
//    all in this class will be available in all child classes
    private final TowerDefense towerDefense;
    protected int animationIndex;
    protected int ANIMATION_SPEED = 15;
    protected int counter;

    /**
     * Constructs a GameScene object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game instance
     */
    public GameScene (TowerDefense towerDefense) {
        this.towerDefense = towerDefense;
    }

    /**
     * Updates the animation counter and index.
     */
    protected void updateCounter() {
        counter++;
        if (counter >= ANIMATION_SPEED) {
            counter = 0;
            animationIndex++;
            if (animationIndex >= 4)
                animationIndex = 0;
        }
    }

//    Getters
    public TowerDefense getTowerDefense() {
        return towerDefense;
    }

    protected boolean isAnimation(int imageId) {
        return getTowerDefense().getEditing().getBlockHandler().isImageAnimation(imageId);
    }

    protected Image getImage(int imageId) {
        return towerDefense.getBlockHandler().getImage(imageId);
    }

    protected Image getImage(int imageId, int animationIndex) {
        return towerDefense.getBlockHandler().getAnimationImage(imageId, animationIndex);
    }
}
