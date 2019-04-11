package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mewmakovs.hkHelpers.AssetLoader;


public class Food extends Scrollable {

    private int maxHP;
    private int startID;

    public Food(float x, float y, float width, float height, int whatToDraw, float zoom) {
        super(x, y, width, height, zoom);
        ID = whatToDraw;
        startID = ID;
        if (ID == 3) { //dackel
            maxHP = 2;
            HP = maxHP;
            speed = MathUtils.random(20, 35);
            saturability = AssetLoader.getCurrentNeVeganSaturability();
        } else if (ID == 7) { //trava
            maxHP = 2;
            HP = maxHP;
            saturability = AssetLoader.getCurrentVeganSaturability();
            speed = -10;
        }
    }

    @Override
    public void update(float delta) {
        if (HP == maxHP) {
            move(speed);
        } else if (HP == 0) {
            death = true;
            death();
        } else {
            if (isChanged) {
                punch();
            } else {
                move(speed);
            }
        }
    }

    private void move(float speed) {
        setX(getX() - (getBotPointY() - speed - ScoreBar.speedBooster) * Gdx.graphics.getDeltaTime());
    }

    private void punch() {
        if(ID == 4) {
            setX(getX() + 250 * Gdx.graphics.getDeltaTime());

            if (getX() > 80) {
                isChanged = false;
            }
        } else {
            death = true;
            death();
        }
    }

    private void death() {
        float k;
        setY(getY() - 60 * Gdx.graphics.getDeltaTime());
        setX(getX() + 80 * Gdx.graphics.getDeltaTime());

        k = getY() / 86 - 0.1f;
        setWidth(getStartWidth() * k);
        setHeight(getWidth());

        if (getY() < 0) {
            needToReset = true;
            isChanged = false;
            death = false;
            ID = startID;
        }
    }

    @Override
    public void isChanged() {
        HP--;
        isChanged = true;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void restart() {
        isChanged = false;
        death = false;
        setX(getStartX());
        setY(getStartY());
        ID = startID;
        if(ID == 3){
            saturability = AssetLoader.getCurrentNeVeganSaturability();
        } else if(ID == 7){
            saturability = AssetLoader.getCurrentVeganSaturability();
        }
    }

    @Override
    public void boom() {

    }

    @Override
    public float getStartHP() {
        return maxHP;
    }
}
