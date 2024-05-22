package cz.cvut.fel.pjv.semak_pjv.ui;

import cz.cvut.fel.pjv.semak_pjv.helperMethods.DownloadSave;
import cz.cvut.fel.pjv.semak_pjv.objects.Block;
import cz.cvut.fel.pjv.semak_pjv.scenes.Editing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.helperMethods.GameData.Blocks.getBLockTypeString;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.MENU;
import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.setGameState;

/**
 * Represents the bottom panel used in the editing scene.
 * Extends the BottomPanel class.
 */
public class EditingBottomPanel extends BottomPanel {
    private CustomButton buttonMenu, buttonSave;
    private CustomButton buttonPathStart, buttonPathEnd;
    private Image pathStart, pathEnd;
    private final Editing editing;
    private final ArrayList<CustomButton> blockButtons = new ArrayList<>();
    private final ArrayList<CustomButton> pathButtons = new ArrayList<>();
    private Block selectedBlock;
    private boolean isSaved;
    private static final Logger LOGGER = Logger.getLogger(EditingBottomPanel.class.getName());

    /**
     * Constructs an EditingBottomPanel object with the specified parameters.
     *
     * @param x        the x-coordinate of the bottom panel
     * @param y        the y-coordinate of the bottom panel
     * @param width    the width of the bottom panel
     * @param height   the height of the bottom panel
     * @param editing  the Editing scene associated with the bottom panel
     */
    public EditingBottomPanel(int x, int y, int width, int height, Editing editing) {
        super(x, y, width, height);
        this.editing = editing;
        initPathImages();
        initButtons();
    }

    /**
     * Initializes the path images used for path buttons.
     */
    private void initPathImages() {
        Image image = DownloadSave.getImage("images.png");
        ImageView imageView = new ImageView(image);
        PixelReader reader = imageView.getImage().getPixelReader();
        pathStart = new WritableImage(reader, 32, 2*32, 32, 32);
        pathEnd = new WritableImage(reader, 2*32, 3*32, 32, 32);
    }

    /**
     * Initializes the buttons in the bottom panel.
     */
    private void initButtons() {
        buttonMenu = new CustomButton("Menu", 538, 642, 100, 30);
        buttonSave= new CustomButton("Save", 538, 674, 100, 30);

        int blockWidth = 50;
        int blockHeight = 50;
        int startingX = 10;
        int startingY = 650;
        int xOffset  = (int) (blockWidth * 1.1f);
        int i = 0;

        for (Block block : editing.getBlockHandler().blocks) {
            blockButtons.add(new CustomButton(String.valueOf(block.getBlockType()), startingX + xOffset * i, startingY, blockWidth, blockHeight, i));
            i++;
        }

        buttonPathStart = new CustomButton("Path Start", startingX + xOffset * 3, startingY, blockWidth, blockHeight, 3);
        buttonPathEnd = new CustomButton("Path End", startingX + xOffset * 4, startingY, blockWidth, blockHeight, 4);
        pathButtons.add(buttonPathStart);
        pathButtons.add(buttonPathEnd);
    }

    /**
     * Draws the bottom panel on the canvas.
     * @param gc The graphics context to render on.
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(249, 213, 187));
        gc.fillRect(x, y, width, height);
        drawButtons(gc);
    }

    /**
     * Draws the buttons in the bottom panel.
     * @param gc The graphics context to render on.
     */
    private void drawButtons(GraphicsContext gc) {
        buttonMenu.draw(gc);
        buttonSave.draw(gc);

        drawPathButton(gc, buttonPathStart, pathStart);
        drawPathButton(gc, buttonPathEnd, pathEnd);

        drawBlockButtons(gc);
        drawSelectedButton(gc);
    }

    /**
     * Draws a path button on the canvas.
     * @param gc     The graphics context to render on.
     * @param b      The path button to draw.
     * @param image  The image to be displayed on the button.
     */
    private void drawPathButton(GraphicsContext gc, CustomButton b, Image image) {
        gc.drawImage(image, b.getBounds().getX(), b.getBounds().getY(), b.getBounds().getWidth(), b.getBounds().getHeight());

        makeHoverEffect(gc, b);
    }

    /**
     * Draws the selected block button on the canvas.
     * @param gc The graphics context to render on.
     */
    private void drawSelectedButton(GraphicsContext gc) {
        if (selectedBlock != null) {
            gc.drawImage(selectedBlock.getImage(), 480, 650, 50, 50);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(480,650,50,50);
        }
    }

    /**
     * Draws the block buttons on the canvas.
     * @param gc The graphics context to render on.
     */
    private void drawBlockButtons(GraphicsContext gc) {
        for (CustomButton b : blockButtons) {
            gc.drawImage(getButtonImage(b.getId()), b.getBounds().getX(), b.getBounds().getY(), b.getBounds().getWidth(), b.getBounds().getHeight());

            makeHoverEffect(gc, b);
        }
    }

    /**
     * Handles mouse click events on the bottom panel.
     * @param x The x-coordinate of the click.
     * @param y The y-coordinate of the click.
     */
    public void mouseClicked(int x, int y) {
        isSaved = false;
        if (buttonMenu.getBounds().contains(x, y)) {
            setGameState(MENU);
            LOGGER.info("Menu scene opened");
        } else if (buttonSave.getBounds().contains(x, y)) {
            saveLevel();
            LOGGER.info("Game map was saved");
            isSaved = true;
        } else if (buttonPathStart.getBounds().contains(x, y)) {
            selectedBlock = new Block(pathStart, -1, -1);
            editing.setSelectedBlock(selectedBlock);
            LOGGER.info("Was selected path start block");
        } else if (buttonPathEnd.getBounds().contains(x, y)) {
            selectedBlock = new Block(pathEnd, -2, -2);
            editing.setSelectedBlock(selectedBlock);
            LOGGER.info("Was selected path end block");
        } else {
            for (CustomButton b : blockButtons) {
                if (b.getBounds().contains(x, y)) {
                    selectedBlock = editing.getBlockHandler().getBlock(b.getId());
                    editing.setSelectedBlock(selectedBlock);
                    LOGGER.info("Was selected " +getBLockTypeString(selectedBlock.getBlockType()));
                    return;
                }
            }
        }
    }

    private void saveLevel() {
        editing.saveLevel();
    }

    /**
     * Handles mouse movement events on the bottom panel.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     */
    public void mouseMoved(int x, int y) {
        buttonMenu.setButtonHovered(buttonMenu.getBounds().contains(x, y));
        buttonSave.setButtonHovered(buttonSave.getBounds().contains(x, y));
        buttonPathStart.setButtonHovered(buttonSave.getBounds().contains(x, y));
        buttonPathEnd.setButtonHovered(buttonSave.getBounds().contains(x, y));
        for (CustomButton b : blockButtons)
            b.setButtonHovered(b.getBounds().contains(x, y));
        for (CustomButton b: pathButtons)
            b.setButtonHovered(b.getBounds().contains(x, y));
    }

//    Getters and setters
    public Image getButtonImage(int id) {
    return editing.getBlockHandler().getImage(id);
}

    public Image getPathStart() {
        return pathStart;
    }

    public Image getPathEnd() {
        return pathEnd;
    }

    public boolean isSaved() {
        return isSaved;
    }
}
