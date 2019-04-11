package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class BackgroundHandler {

    private final Array<Background> backgroundHandler;


    public BackgroundHandler() {
        backgroundHandler = new Array<Background>();
        addBackground();
    }


    private void addBackground() {
        backgroundHandler.add(new Background(0, 5, 128, 32, 0));
        backgroundHandler.add(new Background(128, 5, 128, 32, 0));
        backgroundHandler.add(new Background(256, 5, 128, 32, 0));

        backgroundHandler.add(new Background(0, 20, 128, 24, 1));
        backgroundHandler.add(new Background(128, 20, 128, 24, 1));
        backgroundHandler.add(new Background(256, 20, 128, 24, 1));
    }

    public void update() {
        move();
    }

    private void move() {
        for (int i = 0; i < backgroundHandler.size; i++) {
            if (backgroundHandler.get(i).getBody().x <= -1 * backgroundHandler.get(i).getBody().width) { // другими словами если текстура вышла из
                                                                                                        // поля зрения
                backgroundHandler.get(i).getBody().setX(backgroundHandler.get(i).getBody().x + 384);
            }

            if (backgroundHandler.get(i).getMountain() == 0) {
                backgroundHandler.get(i).getBody().x -= (backgroundHandler.get(i).getBody().y - 1) * Gdx.graphics.getDeltaTime();
            } else if(backgroundHandler.get(i).getMountain() == 1){
                backgroundHandler.get(i).getBody().x -= (backgroundHandler.get(i).getBody().y - 4 - ScoreBar.speedBooster) * Gdx.graphics.getDeltaTime();
            }
        }
    }

    public void restart(){
        for(int i = 0; i < backgroundHandler.size; i++){
            backgroundHandler.get(i).restart();
        }
    }

    public Array<Background> getBackgroundHandler() {
        return backgroundHandler;
    }
}
