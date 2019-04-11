package com.mewmakovs.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Background {

    private Rectangle body;
    private int mountain;
    private Vector2 startPosition;

    protected Background (float x, float y, float width, float height, int mountain){
        body = new Rectangle(x, y, width, height);
        startPosition = new Vector2(x, y);

        this.mountain = mountain;
    }

    public void restart(){
        body.x = startPosition.x;
        body.y = startPosition.y;
    }

    public int getMountain(){
        return mountain;
    }

    public Rectangle getBody() {
        return body;
    }

}
