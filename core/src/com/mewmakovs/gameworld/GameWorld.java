package com.mewmakovs.gameworld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mewmakovs.gameobjects.*;
import com.mewmakovs.hkHelpers.AssetLoader;

public class GameWorld {

    private Player korean;
    private ScrollHandler scrollHandler;
    private BackgroundHandler backgroundHandler;
    private GameState currentState;
    private static UpgradeState upgradeState;
    private float gameHeight;
    private ScoreBar scoreBar;

    public enum GameState {
        RUNNING, GAMEOVER, START_MENU, ENTER_GAME, UPGRADE_MENU
    }

    public static enum UpgradeState {
        FIRST_UPGRADE, SECOND_UPGRADE, THIRD_UPGRADE,
        FOURTH_UPGRADE, FIFTH_UPGRADE, SIXTH_UPGRADE,
        SEVENTH_UPGRADE
    }

    public GameWorld(int midPointY, float gameHeight) {
        scoreBar = new ScoreBar();
        this.gameHeight = gameHeight;
        korean = new Player(25, midPointY, 48, 48);
        scrollHandler = new ScrollHandler(getKorean(), gameHeight, scoreBar);
        backgroundHandler = new BackgroundHandler();
        currentState = GameState.START_MENU;
        upgradeState = UpgradeState.FIRST_UPGRADE;
    }


    public void update(float delta) {
        //Gdx.app.log("game state", getCurrentState() + " ");
        switch (currentState) {
            case RUNNING:
                updateRunning(delta);
                break;

            case GAMEOVER:
                updateRunning(delta);
                break;
        }
    }

    private void updateRunning(float delta) {
        scoreBar.update();
        if (scoreBar.getScore() < 0 || korean.getHP() <= 0) {
            scoreBar.setTheEnd(true);
            if (currentState != GameState.GAMEOVER) {
                GameRenderer.setKoreanMessage();
                GameRenderer.getRandomMessage();
                currentState = GameState.GAMEOVER;
            }
            if (scoreBar.getDistance() > AssetLoader.getHighDistance()) {
                AssetLoader.setHighDistance(scoreBar.getDistance());
                GameRenderer.setMessage(3);
            }
        }

        scrollHandler.update(delta);
        backgroundHandler.update();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void reStart() {
        currentState = GameState.RUNNING;
    }

    public void start() {
        currentState = GameState.RUNNING;
        scrollHandler.restart();
        backgroundHandler.restart();
        scoreBar.restart();
        AssetLoader.mainTexture.dispose();
    }

    public void upgradeMenu() {
        AssetLoader.mainTexture.dispose();
        currentState = GameState.UPGRADE_MENU;
        Gdx.app.log("state", currentState + " ");
    }

    public void startMenu() {

        if (scoreBar.getDistance() > AssetLoader.getHighDistance()) {
            AssetLoader.setHighDistance(scoreBar.getDistance());
        }
        AssetLoader.setCurrentExp(scoreBar.getDistance());
        scoreBar.resetDistance();

        Stats.update();
        Stats.load();
        AssetLoader.mainTexture.load(AssetLoader.mainTexture.getTextureData());
        currentState = GameState.START_MENU;
        Gdx.app.log("state", currentState + " ");
    }

    public void enterGame() {
        currentState = GameState.ENTER_GAME;
    }

    public ScoreBar getScoreBar() {
        return scoreBar;
    }

    public Player getKorean() {
        return korean;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public BackgroundHandler getBackgroundHandler() {
        return backgroundHandler;
    }

    public static void setUpgradeState(UpgradeState state) {
        upgradeState = state;
    }

    public static UpgradeState getUpgradeState() {
        return upgradeState;
    }

    public float getGameHeight() {
        return gameHeight;
    }
}
