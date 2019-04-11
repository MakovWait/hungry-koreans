package com.mewmakovs.hkHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mewmakovs.gameobjects.Stats;
import sun.security.krb5.internal.PAData;

public class AssetLoader {
    public static Texture gameTexture, mainTexture, upGradeTexture;
    public static TextureRegion rTree, rBush, rTV, rVeganBar, rNeVeganBar, rUpGradeScreen, rPit, rRock, rVegans;
    public static TextureRegion[] regions, rsKoreanFrames, rsStartScreen, rsDackelFrames, rsMountains,
            rsKoreanFall, rsKoreanEat, rsDackelHalfFrames, rsGreenPeace, rsKoreanDeath;
    public static BitmapFont font, shadow;

    public static Animation aKoreanRun, aStartAnimation, aDackelRun, aDackelHalfRun, aKoreanFall, aKoreanEat, aGreenPeace, aKoreanDeath;

    public static Preferences preferences;

    final static String FONT_CHARS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
            + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            + "IQ"
            + "1234567890.,:;_¡!¿?\"'+-*/()[]={}<>";
    public static BitmapFont rusFont;
    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public static void load() {

        gameTexture = new Texture(Gdx.files.internal("gameTextureV1.0.png"));
        gameTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        mainTexture = new Texture(Gdx.files.internal("mainTexture.png"));
        mainTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        upGradeTexture = new Texture(Gdx.files.internal("upgradeMenu.png"));
        //upGradeTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Imperial Web.ttf"));

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = FONT_CHARS;
        parameter.size = 48;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        parameter.kerning = false;
        parameter.flip = true;
        rusFont = generator.generateFont(parameter);
        rusFont.getData().setScale(0.1f);
        rusFont.setColor(136f/255f, 153f/255f, 73f/255f, 1f);

        koreanLoad();
        dackelLoad();
        decorationsLoad();
        backgroundLoad();
        startScreenLoad();
        uiLoad();
        trapLoad();
        fontLoad();
        upGradeScreenLoad();
        preferencesLoad();
        regions = new TextureRegion[13];

        regions[0] = rBush;
        regions[1] = rTree;
        regions[12] = rPit;
    }

    private static void preferencesLoad() {
        preferences = Gdx.app.getPreferences("Hungry koreans");

        if (!preferences.contains("HighDistance")) {
            preferences.putFloat("HighDistance", 0);
        }
        if (!preferences.contains("CurrentExp")) {
            preferences.putFloat("CurrentExp", 0);
        }
        if (!preferences.contains("CurrentNeVeganSaturability")) {
            preferences.putFloat("CurrentNeVeganSaturability", 1);
        }
        if (!preferences.contains("CurrentVeganSaturability")) {
            preferences.putFloat("CurrentVeganSaturability", -1);
        }
        if (!preferences.contains("CurrentHP")) {
            preferences.putFloat("CurrentHP", 1);
        }
        if (!preferences.contains("CurrentNumOfDogs")) {
            preferences.putFloat("CurrentNumOfDogs", 5);
        }
        if (!preferences.contains("CurrentNumOfGrass")) {
            preferences.putFloat("CurrentNumOfGrass", 5);
        }
        if (!preferences.contains("CurrentScoreDown")) {
            preferences.putFloat("CurrentScoreDown", -2);
        }
        if (!preferences.contains("CurrentBottomScore")) {
            preferences.putFloat("CurrentBottomScore", 30);
        }
        if (!preferences.contains("CurrentLvl")) {
            preferences.putFloat("CurrentLvl", 0);
        }
        if (!preferences.contains("SkillPoint")) {
            preferences.putFloat("SkillPoint", 0);
        }
    }

    public static void setSkillPoint(float val) {
        if(val > 0) {
                preferences.putFloat("SkillPoint", val + getSkillPoint());
        } else if(val < 0){
            if(getSkillPoint() > 0){
                preferences.putFloat("SkillPoint", val + getSkillPoint());
            }
        }
        preferences.flush();
    }

    public static float getSkillPoint() {
        return preferences.getFloat("SkillPoint");
    }

    public static void setLvl(float val) {
        preferences.putFloat("CurrentLvl", val + getCurrentLvl());
        preferences.flush();
    }

    public static float getCurrentLvl() {
        return preferences.getFloat("CurrentLvl");
    }

    public static void setCurrentBottomScore(float val) {
        if(getCurrentBottomScore() < 50 || Stats.isNeedReset()) {
            AssetLoader.setSkillPoint(-1);
            preferences.putFloat("CurrentBottomScore", val + getCurrentBottomScore());
            preferences.flush();
        }
    }

    public static float getCurrentBottomScore() {
            return preferences.getFloat("CurrentBottomScore");
    }

    public static void setCurrentScoreDown(float val) {
        if(getCurrentScoreDown() < -0.5f || Stats.isNeedReset()) {
            AssetLoader.setSkillPoint(-1);
            preferences.putFloat("CurrentScoreDown", val + getCurrentScoreDown());
            preferences.flush();
        }
    }

    public static float getCurrentScoreDown() {
        return preferences.getFloat("CurrentScoreDown");
    }

    public static void setCurrentNumOfGrass(float val) {
        if(getCurrentNumOfGrass() > 0 || Stats.isNeedReset()) {
            AssetLoader.setSkillPoint(-1);
            preferences.putFloat("CurrentNumOfGrass", val + getCurrentNumOfGrass());
            preferences.flush();
        }
    }

    public static float getCurrentNumOfGrass() {
        return preferences.getFloat("CurrentNumOfGrass");
    }

    public static void setCurrentNumOfDogs(float val) {
        AssetLoader.setSkillPoint(-1);
        preferences.putFloat("CurrentNumOfDogs", val + getCurrentNumOfDogs());
        preferences.flush();
    }

    public static float getCurrentNumOfDogs() {
        return preferences.getFloat("CurrentNumOfDogs");
    }

    public static void setCurrentVeganSaturability(float val) {
        AssetLoader.setSkillPoint(-1);
        preferences.putFloat("CurrentVeganSaturability", val + getCurrentVeganSaturability());
        preferences.flush();
    }

    public static float getCurrentVeganSaturability() {
        return preferences.getFloat("CurrentVeganSaturability");
    }

    public static void setCurrentNeVeganSaturability(float val) {
        AssetLoader.setSkillPoint(-1);
        preferences.putFloat("CurrentNeVeganSaturability", val + getCurrentNeVeganSaturability());
        preferences.flush();
    }

    public static float getCurrentNeVeganSaturability() {
        return preferences.getFloat("CurrentNeVeganSaturability");
    }

    public static void setCurrentHP(float val) {
        AssetLoader.setSkillPoint(-1);
        preferences.putFloat("CurrentHP", val + getCurrentHP());
        preferences.flush();
    }

    public static float getCurrentHP() {
        return preferences.getFloat("CurrentHP");
    }


    public static void setCurrentExp(float val) {
        preferences.putFloat("CurrentExp", val + getCurrentExp());
        preferences.flush();
    }

    public static float getCurrentExp() {
        return preferences.getFloat("CurrentExp");
    }

    public static void setHighDistance(float val) {
        preferences.putFloat("HighDistance", val);
        preferences.flush();
    }

    public static float getHighDistance() {
        return preferences.getFloat("HighDistance");
    }

    private static void upGradeScreenLoad() {
        rUpGradeScreen = new TextureRegion(upGradeTexture, 0, 0, 256, 144);
        rUpGradeScreen.flip(false, true);
    }

    private static void trapLoad() {
        rPit = new TextureRegion(gameTexture, 192, 64, 64, 32);
        rPit.flip(false, true);

        rRock = new TextureRegion(gameTexture, 896, 64, 32, 32);
        rRock.flip(false, true);

        rVegans = new TextureRegion(gameTexture, 928, 64, 32, 32);
        rVegans.flip(false, true);

        rsGreenPeace = new TextureRegion[5];

        rsGreenPeace[0] = new TextureRegion(gameTexture, 576, 64, 64, 32);
        rsGreenPeace[0].flip(false, true);
        rsGreenPeace[1] = new TextureRegion(gameTexture, 640, 64, 64, 32);
        rsGreenPeace[1].flip(false, true);
        rsGreenPeace[2] = new TextureRegion(gameTexture, 704, 64, 64, 32);
        rsGreenPeace[2].flip(false, true);
        rsGreenPeace[3] = new TextureRegion(gameTexture, 768, 64, 64, 32);
        rsGreenPeace[3].flip(false, true);
        rsGreenPeace[4] = new TextureRegion(gameTexture, 832, 64, 64, 32);
        rsGreenPeace[4].flip(false, true);

        aGreenPeace = new Animation(0.15f, rsGreenPeace);
        aGreenPeace.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void koreanLoad() {
        rsKoreanFrames = new TextureRegion[5];

        rsKoreanFrames[0] = new TextureRegion(gameTexture, 0, 0, 64, 64);
        rsKoreanFrames[0].flip(false, true);
        rsKoreanFrames[1] = new TextureRegion(gameTexture, 64, 0, 64, 64);
        rsKoreanFrames[1].flip(false, true);
        rsKoreanFrames[2] = new TextureRegion(gameTexture, 128, 0, 64, 64);
        rsKoreanFrames[2].flip(false, true);
        rsKoreanFrames[3] = new TextureRegion(gameTexture, 192, 0, 64, 64);
        rsKoreanFrames[3].flip(false, true);
        rsKoreanFrames[4] = new TextureRegion(gameTexture, 256, 0, 64, 64);
        rsKoreanFrames[4].flip(false, true);


        aKoreanRun = new Animation(0.1f, rsKoreanFrames);
        aKoreanRun.setPlayMode(Animation.PlayMode.LOOP);

        rsKoreanEat = new TextureRegion[5];
        rsKoreanEat[0] = new TextureRegion(gameTexture, 320, 0, 64, 64);
        rsKoreanEat[0].flip(false, true);
        rsKoreanEat[1] = new TextureRegion(gameTexture, 384, 0, 64, 64);
        rsKoreanEat[1].flip(false, true);
        rsKoreanEat[2] = new TextureRegion(gameTexture, 448, 0, 64, 64);
        rsKoreanEat[2].flip(false, true);
        rsKoreanEat[3] = new TextureRegion(gameTexture, 512, 0, 64, 64);
        rsKoreanEat[3].flip(false, true);
        rsKoreanEat[4] = new TextureRegion(gameTexture, 576, 0, 64, 64);
        rsKoreanEat[4].flip(false, true);


        aKoreanEat = new Animation(0.1f, rsKoreanEat);
        aKoreanEat.setPlayMode(Animation.PlayMode.LOOP);

        rsKoreanDeath = new TextureRegion[6];
        rsKoreanDeath[0] = new TextureRegion(gameTexture, 640, 0, 64, 64);
        rsKoreanDeath[0].flip(false, true);
        rsKoreanDeath[1] = new TextureRegion(gameTexture, 704, 0, 64, 64);
        rsKoreanDeath[1].flip(false, true);
        rsKoreanDeath[2] = new TextureRegion(gameTexture, 768, 0, 64, 64);
        rsKoreanDeath[2].flip(false, true);
        rsKoreanDeath[3] = new TextureRegion(gameTexture, 832, 0, 64, 64);
        rsKoreanDeath[3].flip(false, true);
        rsKoreanDeath[4] = new TextureRegion(gameTexture, 896, 0, 64, 64);
        rsKoreanDeath[4].flip(false, true);
        rsKoreanDeath[5] = new TextureRegion(gameTexture, 960, 0, 64, 64);
        rsKoreanDeath[5].flip(false, true);

        aKoreanDeath = new Animation(0.07f, rsKoreanDeath);
        aKoreanDeath.setPlayMode(Animation.PlayMode.LOOP);

        rsKoreanFall = new TextureRegion[7];
        rsKoreanFall[0] = new TextureRegion(gameTexture, 576, 96, 64, 64);
        rsKoreanFall[0].flip(false, true);
        rsKoreanFall[1] = new TextureRegion(gameTexture, 640, 96, 64, 64);
        rsKoreanFall[1].flip(false, true);
        rsKoreanFall[2] = new TextureRegion(gameTexture, 704, 96, 64, 64);
        rsKoreanFall[2].flip(false, true);
        rsKoreanFall[3] = new TextureRegion(gameTexture, 768, 96, 64, 64);
        rsKoreanFall[3].flip(false, true);
        rsKoreanFall[4] = new TextureRegion(gameTexture, 832, 96, 64, 64);
        rsKoreanFall[4].flip(false, true);
        rsKoreanFall[5] = new TextureRegion(gameTexture, 896, 96, 64, 64);
        rsKoreanFall[5].flip(false, true);
        rsKoreanFall[6] = new TextureRegion(gameTexture, 960, 96, 64, 64);
        rsKoreanFall[6].flip(false, true);

        aKoreanFall = new Animation(0.07f, rsKoreanFall);
        aKoreanFall.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void dackelLoad() {
        rsDackelFrames = new TextureRegion[3];

        rsDackelFrames[0] = new TextureRegion(gameTexture, 0, 64, 32, 32);
        rsDackelFrames[0].flip(false, true);
        rsDackelFrames[1] = new TextureRegion(gameTexture, 32, 64, 32, 32);
        rsDackelFrames[1].flip(false, true);
        rsDackelFrames[2] = new TextureRegion(gameTexture, 64, 64, 32, 32);
        rsDackelFrames[2].flip(false, true);


        aDackelRun = new Animation(0.1f, rsDackelFrames);
        aDackelRun.setPlayMode(Animation.PlayMode.LOOP);

        rsDackelHalfFrames = new TextureRegion[3];
        rsDackelHalfFrames[0] = new TextureRegion(gameTexture, 96, 64, 32, 32);
        rsDackelHalfFrames[0].flip(false, true);
        rsDackelHalfFrames[1] = new TextureRegion(gameTexture, 128, 64, 32, 32);
        rsDackelHalfFrames[1].flip(false, true);
        rsDackelHalfFrames[2] = new TextureRegion(gameTexture, 160, 64, 32, 32);
        rsDackelHalfFrames[2].flip(false, true);

        aDackelHalfRun = new Animation(0.1f, rsDackelHalfFrames);
        aDackelHalfRun.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void decorationsLoad() {
        rBush = new TextureRegion(gameTexture, 64, 96, 64, 64);
        rBush.flip(false, true);

        rTree = new TextureRegion(gameTexture, 0, 96, 64, 62);
        rTree.flip(false, true);
    }

    private static void backgroundLoad() {
        rsMountains = new TextureRegion[2];

        rsMountains[0] = new TextureRegion(gameTexture, 384, 64, 128, 32);
        rsMountains[0].flip(false, true);
        rsMountains[1] = new TextureRegion(gameTexture, 256, 64, 128, 32);
        rsMountains[1].flip(false, true);
    }

    private static void startScreenLoad() {
        rsStartScreen = new TextureRegion[7];

        rsStartScreen[0] = new TextureRegion(mainTexture, 0, 0, 256, 144);
        rsStartScreen[0].flip(false, true);
        rsStartScreen[1] = new TextureRegion(mainTexture, 256, 0, 256, 144);
        rsStartScreen[1].flip(false, true);
        rsStartScreen[2] = new TextureRegion(mainTexture, 512, 0, 256, 144);
        rsStartScreen[2].flip(false, true);
        rsStartScreen[3] = new TextureRegion(mainTexture, 768, 0, 256, 144);
        rsStartScreen[3].flip(false, true);
        rsStartScreen[4] = new TextureRegion(mainTexture, 1024, 0, 256, 144);
        rsStartScreen[4].flip(false, true);
        rsStartScreen[5] = new TextureRegion(mainTexture, 1280, 0, 256, 144);
        rsStartScreen[5].flip(false, true);
        rsStartScreen[6] = new TextureRegion(mainTexture, 1536, 0, 256, 144);
        rsStartScreen[6].flip(false, true);


        aStartAnimation = new Animation(0.1f, rsStartScreen);
        aStartAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void uiLoad() {
        rVeganBar = new TextureRegion(gameTexture, 128, 96, 80, 24);
        rVeganBar.flip(true, true);

        rNeVeganBar = new TextureRegion(gameTexture, 208, 96, 80, 24);
        rNeVeganBar.flip(false, true);

        rTV = new TextureRegion(gameTexture, 0, 160, 127, 71);
        rTV.flip(false, true);
    }

    private static void fontLoad() {
        font = new BitmapFont(Gdx.files.internal("font/text.fnt"), true);
        font.getData().setScale(0.1f);
        shadow = new BitmapFont(Gdx.files.internal("font/shadow.fnt"), true);
        shadow.getData().setScale(0.1f);
    }

    public static void dispose() {
        generator.dispose();
        font.dispose();
        rusFont.dispose();
        shadow.dispose();
        gameTexture.dispose();
        mainTexture.dispose();
        upGradeTexture.dispose();
    }

}
