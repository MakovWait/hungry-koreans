package com.mewmakovs.hkHelpers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.mewmakovs.game.MyGame;
import com.mewmakovs.gameobjects.Player;
import com.mewmakovs.gameobjects.ScoreBar;
import com.mewmakovs.gameobjects.Stats;
import com.mewmakovs.gameworld.GameWorld;
import com.mewmakovs.screens.GameScreen;

public class GestureHandler implements GestureDetector.GestureListener {

    private GameWorld world;
    private Player korean;
    private int click = 0;

    public GestureHandler(GameWorld world) {
        this.world = world;
        this.korean = world.getKorean();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (world.getCurrentState() == GameWorld.GameState.START_MENU) {
            if (tapOnTv(x, y)) {
                world.enterGame();
            }
            if (tapOnBox(x, y)) {
               world.upgradeMenu();

            }
        } else if (world.getCurrentState() == GameWorld.GameState.GAMEOVER) {
                click++;
                if(click >= 2) {
                    click = 0;
                    world.startMenu();
                }
        } else if(world.getCurrentState() == GameWorld.GameState.UPGRADE_MENU){
            if(tapOnFirstUpgrade(x, y)){
                Gdx.app.log("First upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.FIRST_UPGRADE);
            }
            if(tapOnSecondUpgrade(x, y)){
                Gdx.app.log("Second upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.SECOND_UPGRADE);
            }
            if(tapOnThirdUpgrade(x, y)){
                Gdx.app.log("Third upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.THIRD_UPGRADE);
            }
            if(tapOnFourthUpgrade(x, y)){
                Gdx.app.log("Fourth upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.FOURTH_UPGRADE);
            }
            if(tapOnFifthUpgrade(x, y)){
                Gdx.app.log("Fifth upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.FIFTH_UPGRADE);
            }
            if(tapOnSixthUpgrade(x, y)){
                Gdx.app.log("Sixth upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.SIXTH_UPGRADE);
            }
            if(tapOnSeventhUpgrade(x, y)){
                Gdx.app.log("Seventh upgrade", "was tapped");
                world.setUpgradeState(GameWorld.UpgradeState.SEVENTH_UPGRADE);
            }
            if(tapOnUpgradeButton(x, y)){
                Gdx.app.log("Upgrade Button", "was tapped");
                Stats.NeedUpgrade(true);
            }
            if(tapOnResetButton(x, y)){
                Gdx.app.log("Reset Button", "was tapped");
                Stats.NeedReset(true);
            }
            if(tapOnBackButton(x,y)){
                Gdx.app.log("Back Button", "was tapped");
                world.startMenu();
            }

        }

        Gdx.app.log("tap", "x: " + getCurrentX(x) + "; y: " + getCurrentY(y));

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        Gdx.app.log("tap", "da");
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        if (world.getCurrentState() == GameWorld.GameState.RUNNING) {
            korean.onSwipe(velocityY, velocityX);
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    private float getCurrentX(float x) { //масштабируем координаты тапов под значения игрового мира
        float k;
        k = Gdx.graphics.getWidth() / x;

        return 256 / k;
    }

    private float getCurrentY(float y) {
        float k;
        k = Gdx.graphics.getHeight() / y;

        return (world.getGameHeight() / k);
    }

    private boolean tapOnTv(float x, float y) {
        if ((int) getCurrentX(x) < 220 && (int) getCurrentY(y) < 101
                && (int) getCurrentX(x) > 137 && (int) getCurrentY(y) > 59) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnBox(float x, float y) {
        if ((int) getCurrentX(x) < 177 && (int) getCurrentY(y) < Gdx.graphics.getHeight()
                && (int) getCurrentX(x) > 120 && (int) getCurrentY(y) > 128) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnFirstUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 20 && (int) getCurrentY(y) > 27
                && (int) getCurrentX(x) < 60 && (int) getCurrentY(y) < 74) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnSecondUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 61 && (int) getCurrentY(y) > 26
                && (int) getCurrentX(x) < 100 && (int) getCurrentY(y) < 74) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnThirdUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 30 && (int) getCurrentY(y) > 76
                && (int) getCurrentX(x) < 42 && (int) getCurrentY(y) < 101) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnFourthUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 45 && (int) getCurrentY(y) > 79
                && (int) getCurrentX(x) < 80 && (int) getCurrentY(y) < 102) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnFifthUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 83 && (int) getCurrentY(y) > 76
                && (int) getCurrentX(x) < 94 && (int) getCurrentY(y) < 101) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnSixthUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 35 && (int) getCurrentY(y) > 106
                && (int) getCurrentX(x) < 53 && (int) getCurrentY(y) < 127) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnSeventhUpgrade(float x, float y) {
        if ((int) getCurrentX(x) > 68 && (int) getCurrentY(y) > 116
                && (int) getCurrentX(x) < 87 && (int) getCurrentY(y) < 126) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnUpgradeButton(float x, float y) {
        if ((int) getCurrentX(x) > 146 && (int) getCurrentY(y) > 91
                && (int) getCurrentX(x) < 200 && (int) getCurrentY(y) < 111) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnBackButton(float x, float y) {
        if ((int) getCurrentX(x) > 193 && (int) getCurrentY(y) > 128) {
            return true;
        } else {
            return false;
        }
    }

    private boolean tapOnResetButton(float x, float y){
        if ((int) getCurrentX(x) > 106 && (int) getCurrentY(y) > 127
                && (int) getCurrentX(x) <  168 && (int) getCurrentY(y) < 145) {
            return true;
        } else {
            return false;
        }
    }
}
