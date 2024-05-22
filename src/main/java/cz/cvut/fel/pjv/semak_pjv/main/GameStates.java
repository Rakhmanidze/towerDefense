package cz.cvut.fel.pjv.semak_pjv.main;

/**
 * Enum representing different states of the game, such as playing, in menu, editing, game over, and game won.
 */
public enum GameStates {
    PLAYING, MENU, EDITING, GAME_OVER, GAME_WON;

    /**
     * Static variable representing the current state of the game.
     * By default, the game starts in the MENU state.
     */
    public static GameStates gameState = MENU;

    public static void setGameState(GameStates gameState) {
        GameStates.gameState = gameState;
    }
}
