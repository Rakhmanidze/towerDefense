package cz.cvut.fel.pjv.semak_pjv.ui;

import cz.cvut.fel.pjv.semak_pjv.main.GameStates;
import javafx.scene.canvas.GraphicsContext;
import java.awt.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Represents a custom button used in the game.
 */
public class CustomButton {
    private final int x, y, width, height, id;
    private String text;
//    check if mouse inside button:
    private Rectangle bounds;
    private boolean isButtonHovered;

    /**
     * Initializes a new instance of the CustomButton class.
     * @param text   The text displayed on the button.
     * @param x      The x-coordinate of the button.
     * @param y      The y-coordinate of the button.
     * @param width  The width of the button.
     * @param height The height of the button.
     */
    public CustomButton(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = -1; // to ensure that we dont get any crashes

        initBounds();
    }

    /**
     * Initializes a new instance of the CustomButton class with a specified id.
     * This constructor is used for block buttons.
     * @param text   The text displayed on the button.
     * @param x      The x-coordinate of the button.
     * @param y      The y-coordinate of the button.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param id     The id of the button.
     */
    public CustomButton(String text, int x, int y, int width, int height, int id) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;

        initBounds();
    }

    private void initBounds() {
        this.bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Draws the button on the canvas.
     * @param gc The graphics context to render on.
     */
    public void draw(GraphicsContext gc) {
        drawBody(gc);
        drawBorder(gc);
        drawText(gc);
    }

    /**
     * Draws the border of the button.
     * @param gc The graphics context to render on.
     */
    private void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, width, height, 14, 14);
    }

    /**
     * Draws the body of the button.
     * @param gc The graphics context to render on.
     */
    private void drawBody(GraphicsContext gc) {
        if (isButtonHovered) {
            if (GameStates.gameState == GameStates.MENU)
                gc.setFill(Color.LIGHTGRAY);
            else if (GameStates.gameState == GameStates.GAME_OVER)
                gc.setFill(Color.rgb(128, 128, 128));
            else
                gc.setFill(Color.rgb(224, 224, 224));
        }
        else {
            if (GameStates.gameState == GameStates.MENU)
                gc.setFill(Color.rgb(245, 245, 220));
            else if (GameStates.gameState == GameStates.GAME_OVER)
                gc.setFill(Color.LIGHTGRAY);
            else
                gc.setFill(Color.rgb(242, 244, 250));
        }
        gc.fillRoundRect(x, y, width, height, 14, 14);
    }

    /**
     * Draws the text on the button.
     * @param gc The graphics context to render on.
     */
    private void drawText(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        double textWidth = new Text(text).getBoundsInLocal().getWidth();
        double textHeight = new Text(text).getBoundsInLocal().getHeight();
        gc.fillText(text, x + (width - textWidth) / 2, y + (height + textHeight) / 2);
    }

//    Getters and setters
    public Rectangle getBounds() {
        return bounds;
    }

    public void setButtonHovered(boolean isButtonHovered) {
        this.isButtonHovered = isButtonHovered;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public boolean isButtonHovered() {
        return isButtonHovered;
    }
}
