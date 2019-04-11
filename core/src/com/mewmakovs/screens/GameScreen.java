package com.mewmakovs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.TimeUtils;
import com.mewmakovs.gameworld.GameRenderer;
import com.mewmakovs.gameworld.GameWorld;
import com.mewmakovs.hkHelpers.GestureHandler;

public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime = 0;
    public static float gameHeight;

    public GameScreen(){
     Gdx.app.log("GameScreen", "is created");
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 256;
        gameHeight = screenHeight / (screenWidth / gameWidth);
        Gdx.app.log("Height of the screen: ",gameHeight + "");
        int midPointY = (int) (gameHeight / 2);

        world = new GameWorld(midPointY, gameHeight);
        renderer = new GameRenderer(world, (int) gameHeight);

        Gdx.input.setInputProcessor(new GestureDetector(new GestureHandler(world)));
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

       //Gdx.app.log("FPS:", (1/delta) + ";");

        runTime += delta;

        world.update(delta);
        renderer.render(runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
