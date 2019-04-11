package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mewmakovs.gameworld.GameWorld;
import com.mewmakovs.hkHelpers.AssetLoader;

public class Stats {
    private static float VeganSaturability;
    private static float NeVeganSaturability;
    private static float HP;
    private static float numOfdogs;
    private static float numOfGrass;
    private static float ScoreDown;
    private static float bottomScore;
    private static float EXP;
    private static float SkillPoint;
    private static float Lvl;
    private static float HowMuchExp;
    private static boolean needUpgrade = false;
    private static boolean needReset = false;

    public static void load() {
        VeganSaturability = AssetLoader.getCurrentVeganSaturability();
        NeVeganSaturability = AssetLoader.getCurrentNeVeganSaturability();
        HP = AssetLoader.getCurrentHP();
        numOfdogs = AssetLoader.getCurrentNumOfDogs();
        numOfGrass = AssetLoader.getCurrentNumOfGrass();
        ScoreDown = AssetLoader.getCurrentScoreDown();
        bottomScore = AssetLoader.getCurrentBottomScore();
        EXP = AssetLoader.getCurrentExp();
        Lvl = AssetLoader.getCurrentLvl();
    }

    private static float currentHowMuchExp() {
        return 100f * (float)Math.pow(2f, AssetLoader.getCurrentLvl());
    }

    public static void update() {
        while (AssetLoader.getCurrentExp() / currentHowMuchExp() >= 1) {
            AssetLoader.setLvl(1);
            AssetLoader.setSkillPoint(1);
        }

        if(Stats.isNeedUpgrade()){
            upgrade();
            Stats.NeedUpgrade(false);
        }

        if(needReset){
            reset();
        }
        //AssetLoader.setLvl(100);
        /*if(AssetLoader.getCurrentHP() != 1)
        AssetLoader.setCurrentHP(1 - AssetLoader.getCurrentHP());*/
    }

    public static void upgrade(){
        switch (GameWorld.getUpgradeState()){
            case FIRST_UPGRADE:
                AssetLoader.setCurrentVeganSaturability(0.5f);
                break;
            case SECOND_UPGRADE:
                AssetLoader.setCurrentNeVeganSaturability(0.5f);
                break;
            case THIRD_UPGRADE:
                AssetLoader.setCurrentNumOfDogs(1);
                break;
            case FOURTH_UPGRADE:
                AssetLoader.setCurrentHP(1);
                break;

            case FIFTH_UPGRADE:
                AssetLoader.setCurrentNumOfGrass(-1);
                break;
            case SIXTH_UPGRADE:
                AssetLoader.setCurrentBottomScore(5);
                break;
            case SEVENTH_UPGRADE:
                AssetLoader.setCurrentScoreDown(0.5f);
                break;
            default:

                break;
        }
    }

    public static void reset(){
       AssetLoader.setCurrentVeganSaturability(- 1f - AssetLoader.getCurrentVeganSaturability());
       AssetLoader.setCurrentNeVeganSaturability(1f - AssetLoader.getCurrentNeVeganSaturability());
       AssetLoader.setCurrentNumOfDogs(5f -  AssetLoader.getCurrentNumOfDogs());
       AssetLoader.setCurrentHP(1f - AssetLoader.getCurrentHP());
       AssetLoader.setCurrentNumOfGrass(5f - AssetLoader.getCurrentNumOfGrass());
       AssetLoader.setCurrentBottomScore(30f - AssetLoader.getCurrentBottomScore());
       AssetLoader.setCurrentScoreDown(- 2f - AssetLoader.getCurrentScoreDown());

       //AssetLoader.setHighDistance(0);

       Gdx.app.log("reset", "confirmed");
       AssetLoader.setSkillPoint(AssetLoader.getCurrentLvl() - AssetLoader.getSkillPoint());
       NeedReset(false);
    }

    public static void NeedUpgrade(boolean state){
        if(AssetLoader.getSkillPoint() > 0){
            needUpgrade = state;
        } else {
            needUpgrade = false;
        }
    }

    public static boolean isNeedUpgrade() {
        return needUpgrade;
    }

    public static boolean NeedReset(boolean state){
        return needReset = state;
    }

    public static boolean isNeedReset(){
        return needReset;
    }
}
