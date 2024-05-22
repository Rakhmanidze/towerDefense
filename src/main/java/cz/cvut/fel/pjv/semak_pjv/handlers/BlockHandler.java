package cz.cvut.fel.pjv.semak_pjv.handlers;

import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.objects.Block;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.*;

/**
 * Handles the loading and management of different block types in the game.
 */
public class BlockHandler {
    public Block GRASS, WATER, ROAD; // Specific block types.
    public Image bigImage; // Specific block types.
    public ArrayList<Block> blocks = new ArrayList<>(); // List of all block types.

    /**
     * Constructs a BlockHandler object, loading the main image and creating blocks.
     */
    public BlockHandler () {
        loadBigImage();
        createBlocks();
    }

    /**
     * Creates and initializes different block types.
     */
    private void createBlocks() {
        int id = 0;
        blocks.add(GRASS = new Block(getImage(4,1),id++, GRASS_BLOCK));
        blocks.add(WATER = new Block(getImages(0,0), id++, WATER_BLOCK));
        blocks.add(ROAD = new Block(getImage(3,1), id++, ROAD_BLOCK));
    }

    /**
     * Loads the main image containing all block graphics.
     */
    private void loadBigImage() {
        bigImage = DownloadSave.getImage("images.png");
    }

//    Getters
    public boolean isImageAnimation(int imageId) {
        return blocks.get(imageId).isAnimation();
    }

    public Image getAnimationImage(int id, int animationIndex) {
        return blocks.get(id).getImage(animationIndex);
    }

    private Image[] getImages(int xCoord, int yCoord) {
        Image[] array = new Image[4];

        for (int i = 0; i < 4; i++) {
            array[i] = getImage(xCoord + i, yCoord);
        }

        return array;
    }

    private Image getImage(int xCoord, int yCoord) {
        ImageView imageView = new ImageView(bigImage);
        imageView.setViewport(new Rectangle2D(xCoord * 32, yCoord * 32, 32, 32));
        return imageView.snapshot(null, null);
    }

    public Block getBlock(int id) {
        return blocks.get(id);
    }

    public Image getImage(int id) {
        return blocks.get(id).getImage();
    }
}
