package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.mewmakovs.gameworld.GameWorld;
import com.mewmakovs.hkHelpers.AssetLoader;
import com.mewmakovs.screens.GameScreen;


public class Player extends Scrollable {
    private float y;
    private float x;
    private float jump;
    private static float botBorder;
    private static float topBorder;
    private static float midPointY;
    private static final float ZOOM = 2f;
    private Food food;
    private float maxHP;
    private float runTime;
    private float startTime;
    float a;

    public Player(float x, float y, float width, float height) {
        super(x, y, width, height, 1);
        midPointY = y;
        //jump = midPointY / 3f;
        jump = 20;
        topBorder = midPointY - jump;
        botBorder = midPointY + jump; //бот ЭКРАНА!!!

        /*Gdx.app.log("midPoint", midPointY + "");
        Gdx.app.log("topBorder", topBorder + "");
        Gdx.app.log("botBorder", botBorder + "");*/

        restart();
        HP = AssetLoader.getCurrentHP();
        ID = 2;
    }

    public void update(float delta) {
        // body.x += 100 * Gdx.graphics.getDeltaTime();
        //Gdx.app.log("HP", HP + " ");
        updateHitbox();
        runTime += delta;

        if (ID == 6 || ID == 5 && getHP() > 0) {
            if (runTime - startTime > 1.5f) {
                ID = 2;
            }
        }
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
        setX(25);
        setY(midPointY);
        Gdx.app.log("korean.xy", getX() + "; " + getY() + "");

        setHeight(48 * (getY() / GameScreen.gameHeight) - 0.1f);
        setWidth(getHeight());

        ID = 2;
        HP = AssetLoader.getCurrentHP();
    }

    @Override
    public void boom() {

    }

    public void onSwipe(float swipeDirectionY, float swipeDirectionX) {
        move(swipeDirectionY, swipeDirectionX);
    }


    private void move(float swipeDirectionY, float swipeDirectionX) //Попробуем сделать солидно, чтоб не стыдно. Но завтра, правда?..
    {
        if (swipeDirectionY < 0) {
            if (getY() > topBorder) {
                setY(getY() - jump);
                setX(getX() + 10);
            }
        } else {
            if (getY() < botBorder) {
                setX(getX() - 10);
                setY(getY() + jump);
            }
        }

        setHeight(getStartHeight() * (getY() / GameScreen.gameHeight) - 0.1f);
        setWidth(getHeight());
        needToSort = true;
    }

    public void interaction(int enterID) {
        switch (enterID) {
            case 3:
                if (ID != 5) {
                    ID = 6;
                    startTime = runTime;
                }
                break;

            case 11://камень
                if (ID != 5) {
                    //Gdx.input.vibrate(1500);
                    HP--;
                    ID = 5;
                    startTime = runTime;
                }
                break;

            case 12://яма
                if (ID != 5) {
                    HP--;
                    ID = 5;
                    startTime = runTime;
                }
                break;
        }
    }

    @Override
    public float getStartHP() {
        return maxHP;
    }

    public float getHP() {
        return HP;
    }

    public float getX() {
        return super.getX();
    }

    public float getY() {
        return super.getY();
    }

    public float getWidth() {
        return super.getWidth();
    }

    public float getHeight() {
        return super.getHeight();
    }

    public float getBotBorder() {
        return botBorder;
    }

    public float getTopBorder() {
        return topBorder;
    }

    public float getMidPointY() {
        return midPointY;
    }
}
