package cz.cvut.fel.pjv.semak_pjv;

import cz.cvut.fel.pjv.semak_pjv.handlers.BlockHandler;
import cz.cvut.fel.pjv.semak_pjv.inputs.KeyboardListener;
import cz.cvut.fel.pjv.semak_pjv.inputs.MyMouseListener;
import cz.cvut.fel.pjv.semak_pjv.main.GameStates;
import cz.cvut.fel.pjv.semak_pjv.main.GameWindow;
import cz.cvut.fel.pjv.semak_pjv.main.Render;
import cz.cvut.fel.pjv.semak_pjv.scenes.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Logger;

import static cz.cvut.fel.pjv.semak_pjv.main.GameStates.*;

/**
 * The main class responsible for managing the game window, rendering, updating, and input handling.
 */
public class TowerDefense extends Application {
    GameWindow gameWindow = new GameWindow();
    private long lastSecondTime;
    private int frames = 0;
    private long lastFrame;
    private long lastUpdate;
    private double timePerFrame;
    private double timePerUpdate;
    private Render render;
    private final Menu menu = new Menu(this);
    private final Playing playing = new Playing(this);
    private final Editing editing = new Editing(this);
    private final GameOver gameOver = new GameOver(this);
    private final GameWon gameWon = new GameWon(this);
    private final BlockHandler blockHandler = new BlockHandler();
    private static final Logger LOGGER = Logger.getLogger(TowerDefense.class.getName());

    /**
     * Initializes the game and starts the game loop.
     */
    @Override
    public void start(Stage stage) {
        render = new Render(this);

        Canvas canvas = new Canvas(gameWindow.getWindowWidth(), gameWindow.getWindowHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, gameWindow.getWindowWidth(), gameWindow.getWindowHeight());

        initInputListeners(scene);

        stage.setScene(scene);
        stage.setTitle("Game Window");
        stage.setResizable(false);
        stage.show();

        timePerFrame = 1_000_000_000.0 / 120.0;
        timePerUpdate = 1_000_000_000.0 / 60.0;

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                long now = System.nanoTime();

                // Render
                if (now - lastFrame >= timePerFrame) {
                    render.render(gc);
                    updateGame();
                    lastFrame = now;
                    frames++;
                }

                // Update
                if (now - lastUpdate >= timePerUpdate) {
                    gameWindow.update();
                    lastUpdate = now;
                }

                if (now - lastSecondTime >= 1_000_000_000) {
                    LOGGER.info("FPS: " + frames + " | UPS: " + gameWindow.getUpdates());
                    frames = 0;
                    gameWindow.resetUpdates();
                    lastSecondTime = now;
                }
            }
        };
        animationTimer.start();
    }

    /**
     * Updates the game state based on the current game state.
     * If the game state is PLAYING, updates the playing scene.
     * If the game state is EDITING, updates the editing scene.
     */
    private void updateGame() {
       if (GameStates.gameState == PLAYING)
            playing.update();
       else if (GameStates.gameState == EDITING)
            editing.update();
    }

    /**
     * Initializes input listeners for keyboard and mouse events.
     */
    private void initInputListeners(Scene scene) {
        KeyboardListener keyboardListener = new KeyboardListener(this);
        scene.setOnKeyPressed(keyboardListener);

        MyMouseListener myMouseListener = new MyMouseListener(this);
        scene.setOnMouseClicked(myMouseListener);
        scene.setOnMouseMoved(myMouseListener);
        scene.setOnMouseDragged(myMouseListener);
    }

//    Getters
    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Editing getEditing() {
        return editing;
    }

    public BlockHandler getBlockHandler() {
        return blockHandler;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public GameWon getGameWon() {
        return gameWon;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
