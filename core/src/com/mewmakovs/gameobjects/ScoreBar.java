package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mewmakovs.hkHelpers.AssetLoader;
import com.mewmakovs.screens.GameScreen;

public class ScoreBar {
    private float Distance;
    public static float speedBooster;
    private float Score;
    private float bottomScore;
    private float scoreDown;
    private float runTime;
    private float startTime;
    private static float distanceTimer;

    private boolean theEnd = false;
    private Vector2 position;
    private float scale = 0.1f;

    public ScoreBar() {
        distanceTimer = 0.6f;
        bottomScore = AssetLoader.getCurrentBottomScore();
        Score = bottomScore;
        scoreDown = AssetLoader.getCurrentScoreDown();
        Distance = 0;

        position = new Vector2(125, GameScreen.gameHeight - 20);

        runTime = 0;
        startTime = 0;
    }


    public void update() {
        runTime += Gdx.graphics.getDeltaTime();

        //Gdx.app.log("speed booster", speedBooster + "");

        if (runTime - startTime > distanceTimer) {
            startTime = runTime;

            if (!theEnd) {
                Distance++;
                /*if(Distance % 10 == 0) {
                    setSpeedBooster(1.5f);//было 3
                    setDistanceTimer(0.01f);
                    Gdx.app.log("speed booster", speedBooster + "");
                }*/
                setSpeedBooster(2f);//было 3
                setDistanceTimer(0.01f);
                //Gdx.app.log("speed booster", speedBooster + "");
            }

            if (Score >= 0) {
                Score += scoreDown;
            }
        }
        //Gdx.app.log("Distance", Distance + "");
    }

    public void restart() {
        bottomScore = AssetLoader.getCurrentBottomScore();
        scoreDown = AssetLoader.getCurrentScoreDown();
        Score = bottomScore;
        Distance = 0;
        setSpeedBooster(0);
        distanceTimer = 1.0f;

        position.x = 125;
        position.y = GameScreen.gameHeight - 20;
        scale = 0.1f;
        AssetLoader.font.getData().setScale(scale);
        runTime = 0;
        startTime = 0;
        theEnd = false;
    }

    public void move() {
        if (scale < 0.9f) {
            position.y -= 100 * Gdx.graphics.getDeltaTime();
            position.x -= 100 * Gdx.graphics.getDeltaTime();
            scale += 1 * Gdx.graphics.getDeltaTime();
            AssetLoader.font.getData().setScale(scale);
        }
    }

    public void addScore(float score) {
        if (Score < 50) {
            Score += score;
        }
    }

    private void setSpeedBooster(float booster) {
        if (booster == 0) {
            speedBooster = 0;
        }
        if (speedBooster > -111) {
            speedBooster -= booster;
        }
    }

    private void setDistanceTimer(float newTimer) {
            if (distanceTimer > 0.3f) {
                distanceTimer -= newTimer;
                Gdx.app.log("distance Timer", distanceTimer + "");
        }
    }

    public static float getDistanceTimer() {
        return distanceTimer;
    }

    public float getNeVeganScore() {
        return (Score / 100) * 27f;
    }

    public float getVeganScore() {
        return (1 - Score / 100) * 27f;
    }

    public float getScore() {
        return Score;
    }

    public float getDistance() {
        return Distance;
    }

    public void resetDistance() {
        Distance = 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public void setTheEnd(boolean state) {
        theEnd = state;
    }

    public boolean isEnd() {
        return theEnd;
    }
}
