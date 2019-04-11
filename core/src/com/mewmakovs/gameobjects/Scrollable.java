package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public abstract class Scrollable {
    private Rectangle body;
    private Rectangle hitBox;
    private  float startHeight;
    private float startWidth;
    private float startX;
    private float startY;
    protected boolean isChanged = false;
    protected boolean needToReset = false;
    protected  boolean needToSort = false;
    protected float HP;
    protected float speed;
    protected int ID;
    protected boolean death = false;
    protected float saturability;

    protected Scrollable(float x, float y, float width, float height, float zoom) {
        body = new Rectangle(x, y, width, height);
        hitBox = new Rectangle(x + 5, y + 5, width - 10, height - 10);

        startHeight = height / zoom;
        startWidth = width / zoom;
        startX = x;
        startY = y;
    }

    public float getSaturability(){
        return saturability;
    }

    public void setY(float y) {
        this.body.y = y;
    }

    public void setX(float x) {
        this.body.x = x;
    }

    public void setWidth(float width) {
        this.body.width = width;
    }

    public void setHeight(float height) {
        this.body.height = height;
    }

    public boolean setIsChanged(boolean isChanged) {
        return this.isChanged = isChanged;
    }

    public float getX() {
        return body.x;
    }

    public float getStartX() {
        return startX;
    }

    public float getY() {
        return body.y;
    }

    public float getStartY() {
        return startY;
    }

    public float getWidth() {
        return body.width;
    }

    public float getStartWidth() {
        return startWidth;
    }

    public float getHeight() {
        return body.height;
    }

    public float getStartHeight() {
        return startHeight;
    }

    public float getBotPointX() {
        return body.x + body.width / 2;
    }

    public float getBotPointY() {
        return body.y + body.height;
    }

    public boolean isDeath() {
        return death;
    }

    public abstract float getStartHP();

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void updateHitbox(){
        hitBox.x = getX() + 5;
        hitBox.y = getY() + 5;
        hitBox.width = getWidth() - 10;
        hitBox.height = getHeight() - 10;
    }

    public void setHP(int damage){
        HP += damage;
    }

    public Rectangle getBody(){
        return body;
    }
    public abstract void update(float delta);

    public abstract void isChanged();

    public abstract int getID();

    public abstract void restart();

    public abstract void boom();
}
