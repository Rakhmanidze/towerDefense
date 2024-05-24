package cz.cvut.fel.pjv.semak_pjv.scenes;

import cz.cvut.fel.pjv.semak_pjv.TowerDefense;
import cz.cvut.fel.pjv.semak_pjv.handlers.BlockHandler;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.LevelBuilder;
import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.objects.Block;
import cz.cvut.fel.pjv.semak_pjv.objects.PathCoordinate;
import cz.cvut.fel.pjv.semak_pjv.ui.EditingBottomPanel;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.ROAD_BLOCK;
import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.getBLockTypeString;

/**
 * Represents the editing scene in the game.
 */
public class Editing extends GameScene implements SceneMethods, ExtraSceneMethods {
    private int[][] level;
    private final BlockHandler blockHandler;
    private final EditingBottomPanel editingBottomPanel;
    private Block selectedBlock;
    private int mouseX, mouseY;
    private int lastBlockX, lastBlockY, lastBlockId;
    private boolean isBlockDrawn;
    private PathCoordinate start, end;
    private static final Logger LOGGER = Logger.getLogger(Editing.class.getName());

    /**
     * Constructs an Editing object with the specified TowerDefense game.
     *
     * @param towerDefense  the TowerDefense game instance
     */
    public Editing(TowerDefense towerDefense) {
        super(towerDefense);

        level = LevelBuilder.getLevelData();
        blockHandler = new BlockHandler();
        editingBottomPanel = new EditingBottomPanel(0, 640, 640, 130, this);

        loadDefaultLevel();
    }

    /**
     * Loads the default level data from a save file and initializes path coordinates.
     */
    private void loadDefaultLevel() {
        level = DownloadSave.getLevelData("currentLevel");
        ArrayList<PathCoordinate> coordinates = DownloadSave.getLevelPathCoordinate("currentLevel");
        assert coordinates != null;
        start = coordinates.get(0);
        end = coordinates.get(1);
    }

    /**
     * Draws the current level on the canvas using the provided GraphicsContext.
     * @param gc The GraphicsContext used for rendering.
     */
    private void drawLevel(GraphicsContext gc) {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                if (isAnimation(id))
                    gc.drawImage(getImage(id, animationIndex), x * 32, y * 32);
                else
                    gc.drawImage(getImage(id), x * 32, y * 32);
            }
        }
    }

    public void update() {
        updateCounter();
    }

    /**
     * Renders the editing scene on the canvas using the provided GraphicsContext.
     * @param gc The GraphicsContext used for rendering.
     */
    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
        editingBottomPanel.draw(gc);
        drawSelectedBlock(gc);
        drawPathCoordinate(gc);
    }

    /**
     * Draws the path coordinates on the canvas.
     * @param gc The GraphicsContext used for rendering.
     */
    private void drawPathCoordinate(GraphicsContext gc) {
        if (start != null) {
            gc.drawImage(editingBottomPanel.getPathStart(), 32*start.getxCoord(), 32*start.getyCoord(), 32,32);
        }
        if (end != null) {
            gc.drawImage(editingBottomPanel.getPathEnd(), 32*end.getxCoord(), 32*end.getyCoord(), 32,32);
        }
    }

    /**
     * Handles mouse click events within the editing scene.
     * @param x The x-coordinate of the mouse click.
     * @param y The y-coordinate of the mouse click.
     */
    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640) {
            editingBottomPanel.mouseClicked(x, y);
        } else {
            changeBlock(mouseX, mouseY);
        }
    }

    /**
     * Handles mouse move events within the editing scene.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640) {
            editingBottomPanel.mouseMoved(x, y);
            isBlockDrawn = false;
        } else {
            isBlockDrawn = true;
            mouseX = (x / 32) * 32;
            mouseY = (y / 32) * 32;
        }
    }

    /**
     * Handles mouse drag events within the editing scene.
     * @param x The x-coordinate of the mouse drag.
     * @param y The y-coordinate of the mouse drag.
     */
    @Override
    public void mouseDragged(int x, int y) {
        if (y < 640)
            changeBlock(x, y);
    }

    /**
     * Draws the selected block at the mouse position on the canvas.
     * @param gc The GraphicsContext used for rendering.
     */
    private void drawSelectedBlock(GraphicsContext gc) {
        if (selectedBlock != null && isBlockDrawn) {
            gc.drawImage(selectedBlock.getImage(), mouseX, mouseY,32,32);
        }
    }

    /**
     * Changes the block type at the specified position based on the selected block.
     * @param x The x-coordinate of the block to change.
     * @param y The y-coordinate of the block to change.
     */
    private void changeBlock(int x, int y) {
        if (selectedBlock != null) {
            int blockX = x / 32;
            int blockY = y / 32;

            if (selectedBlock.getId() >= 0) {

                if (blockX >= 0 && blockX < level[0].length && blockY >= 0 && blockY < level.length) {
//            if mouse in the same btn it does nothing
                    if ((lastBlockX == blockX && lastBlockY == blockY && lastBlockId == selectedBlock.getId()))
                        return;

                    int currentBlockId = level[blockY][blockX];

                    // Check if the current block is the same type as the selected block
                    if (currentBlockId == selectedBlock.getId()) {
                        return;
                    }

                    lastBlockX = blockX;
                    lastBlockY = blockY;
                    lastBlockId = selectedBlock.getId();

                    level[blockY][blockX] = selectedBlock.getId();
                    LOGGER.info("Was changed block: " + getBLockTypeString(selectedBlock.getBlockType()));
                }
            } else {
                int id = level[blockY][blockX];
                if (getTowerDefense().getBlockHandler().getBlock(id).getBlockType() == ROAD_BLOCK) {
                    if (selectedBlock.getId() == -1) {
                        start = new PathCoordinate(blockX, blockY);
                        LOGGER.info("Path start block was changed");
                    } else {
                        end = new PathCoordinate(blockX, blockY);
                        LOGGER.info("Path end block was changed");
                    }
                }
            }
        }

    }

    /**
     * Saves the current level data and path coordinates to a save file and restarts game if its running.
     */
    public void saveLevel() {
        DownloadSave.saveLevel("currentLevel", level, start, end);
        getTowerDefense().getPlaying().setLevel(level, start, end);
        getTowerDefense().getPlaying().getEnemyHandler().updateStartAndEnd(start, end);

    }

//    Getters and setters
    public BlockHandler getBlockHandler() {
        return blockHandler;
    }

    public void setSelectedBlock(Block selectedBlock) {
        this.selectedBlock = selectedBlock;
        isBlockDrawn = true;
    }
}
