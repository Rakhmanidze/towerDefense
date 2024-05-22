package cz.cvut.fel.pjv.semak_pjv.objects;

import javafx.scene.image.Image;

/**
 * Represents a block in the game.
 */
public class Block {
    private final Image[] image;
    private final int id, blockType;

    /**
     * Constructs a Block object with a single image.
     * @param image The image representing the block.
     * @param id The ID of the block.
     * @param blockType The type of the block.
     */
    public Block(Image image, int id, int blockType) {
        this.image = new Image[1];
        this.image[0] = image;
        this.id = id;
        this.blockType = blockType;
    }

    /**
     * Constructs a Block object with multiple images for animation.
     * @param image The array of images representing the animation frames.
     * @param id The ID of the block.
     * @param blockType The type of the block.
     */
    public Block(Image[] image, int id, int blockType) {
        this.image = image;
        this.id = id;
        this.blockType = blockType;
    }

//    Getters
    public boolean isAnimation() {
        return image.length > 1;
    }

    public Image getImage() {
        return image[0];
    }

    public Image getImage(int animationIndex) {
        return image[animationIndex];
    }

    public int getId() {
        return id;
    }

    public int getBlockType() {
        return blockType;
    }
}
