package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Decoration extends Scrollable {

    public Decoration(float x, float y, float width, float height, int numOfImage, float zoom) {
        super(x, y, width, height, zoom);

        ID = numOfImage;
        speed = -10;
    }

    @Override
    public void update(float delta) {
        //Gdx.app.log("update", "called");
        move();
    }

    @Override
    public void isChanged() {

    }

    @Override
    public int getID() {

        return ID;
    }

    @Override
    public void restart() {
        setX(getStartX());
        setY(getStartY());
        needToReset = false;

    }

    @Override
    public void boom() {
        setY(getY() + MathUtils.random(-1000, 1000) * Gdx.graphics.getDeltaTime());
        setX(getX() + MathUtils.random(-1000, 1000) * Gdx.graphics.getDeltaTime());
    }

    private void move() {
        setX(getX() - (getBotPointY() - speed - ScoreBar.speedBooster) * Gdx.graphics.getDeltaTime());
    }

    @Override
    public float getStartHP() {
        return 1;
    }
}
