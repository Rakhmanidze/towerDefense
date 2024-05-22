package cz.cvut.fel.pjv.semak_pjv.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a bottom panel in the game.
 * Only subclasses or classes within the same package can access its members.
 */
public class BottomPanel {
//    cause of protected only sub or inside that package classes can reach them
    protected final int x, y, width, height;

    /**
     * Constructs a BottomPanel object with the specified parameters.
     *
     * @param x       the x-coordinate of the bottom panel
     * @param y       the y-coordinate of the bottom panel
     * @param width   the width of the bottom panel
     * @param height  the height of the bottom panel
     */
    public BottomPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Applies hover effect to a button on the bottom panel.
     * @param gc The graphics context to render on.
     * @param b  The custom button to apply hover effect to.
     */
    protected void makeHoverEffect(GraphicsContext gc, CustomButton b) {
        if (b.isButtonHovered())
            gc.setStroke(Color.WHITE);
        else
            gc.setStroke(Color.BLACK);
        gc.strokeRect(b.getBounds().getX(), b.getBounds().getY(), b.getBounds().getWidth(), b.getBounds().getHeight());
    }
}
