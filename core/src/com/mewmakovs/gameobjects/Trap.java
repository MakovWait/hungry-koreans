package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;

public class Trap extends Scrollable{

    public Trap(float x, float y, float width, float height, int whatToDraw, float zoom) {
        super(x, y, width, height, zoom);
        speed = -10;
        ID = whatToDraw;
    }


    @Override
    public float getStartHP() {
        return 1;
    }

    @Override
    public void update(float delta) {
        move();
    }

    private void move(){
        setX(getX() - (getBotPointY() - speed - ScoreBar.speedBooster) * Gdx.graphics.getDeltaTime());
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
        setX(-20);
        needToReset = false;
    }

    @Override
    public void boom() {
        needToReset = true;
    }
}
