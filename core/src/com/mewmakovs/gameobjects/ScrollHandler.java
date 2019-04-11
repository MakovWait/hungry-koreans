package com.mewmakovs.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mewmakovs.hkHelpers.AssetLoader;


public class ScrollHandler {

    private ObjectComparator compare;
    private Player korean;
    private ScoreBar score;
    private final Array<Scrollable> objects;
    private Array<Scrollable> traps;
    private float gameHeight;
    private float numOfDogs;
    private float numOfGrass;

    private float runTime = 0;
    private float spawnTime;

    private int numOfObjects;

    public ScrollHandler(Player korean, float gameHeight, ScoreBar score) {
        compare = new ObjectComparator();
        objects = new Array<Scrollable>();
        traps = new Array<Scrollable>(5);

        this.gameHeight = gameHeight;

        this.korean = korean;
        this.score = score;

        numOfDogs = AssetLoader.getCurrentNumOfDogs();
        numOfGrass = AssetLoader.getCurrentNumOfGrass();

        numOfObjects = 29 + (int) numOfDogs + (int) numOfGrass;

        initObjects(korean);
    }

    public void update(float delta) {
        runTime += delta;
        for (int i = 0; i < objects.size; i++) {

            objects.get(i).update(delta);
            collisionDetector(i);

            if (objects.get(i).getX() < -1 * objects.get(i).getWidth() || objects.get(i).needToReset) {
                resetUnit(i);
                sort(i);
            }

            if (objects.get(i).needToSort) {
                sort(i);
            }
        }
    }

    private void collisionDetector(int i) {
        if (objects.get(i).getID() == 3 || objects.get(i).getID() == 4 || objects.get(i).getID() == 7) { //то бишь собаки!
            if (objects.get(i).getY() == korean.getY()) { //на одном уровне шо и кореец
                if (objects.get(i).getX() < korean.getX() + korean.getWidth() * 2f / 3f) { //в районе..
                    if (objects.get(i).getX() > korean.getX() + korean.getWidth() / 3f) {//..головы
                        if (!objects.get(i).isChanged) {
                            objects.get(i).isChanged();
                            score.addScore(objects.get(i).saturability);
                            korean.interaction(objects.get(i).getID());
                            if (objects.get(i).getID() == 3) {
                                objects.get(i).ID = 4;
                            }
                        }
                    }
                }
            }
        }

        if (objects.get(i).getID() == 11) {//камень
            if (//objects.get(i).getBotPointY() > korean.getBotPointY()) {
                    korean.getY() == objects.get(i).getY()){
                if (objects.get(i).getBody().overlaps(korean.getHitBox())) {
                    korean.interaction(objects.get(i).getID());
                }
            }
        }

        if(korean.ID ==5)

        {
            if (objects.get(i).getBody().overlaps(korean.getBody())) {
                if (objects.get(i).getID() != 5)
                    objects.get(i).boom();
            }
        }

    }

    public void restart() {
        Gdx.app.log("numOfGrass in memory", AssetLoader.getCurrentNumOfGrass() + "");
        Gdx.app.log("numOfGrass", numOfGrass + "");
        Gdx.app.log("numOfDogs in memory", AssetLoader.getCurrentNumOfDogs() + "");
        Gdx.app.log("numOfDogs", numOfDogs + "");

        for (int i = 0; i < objects.size; i++) {
            objects.get(i).restart();
            if (objects.get(i).getID() != 2) {
                afterResetButtonUpdate(i);
                resetUnit(i);
            }
        }
        /*numOfGrass = AssetLoader.getCurrentNumOfGrass();
        numOfDogs = AssetLoader.getCurrentNumOfDogs();*/
    }

    private void afterResetButtonUpdate(int i) {
        if (objects.get(i).getID() == 7) {
            Gdx.app.log("trava", "");
            if (numOfGrass > AssetLoader.getCurrentNumOfGrass()) {
                objects.removeIndex(i);
                numOfGrass--;
                Gdx.app.log("1", numOfGrass + "");
            }
        } else if (objects.get(i).getID() == 3) {
            if (numOfDogs > AssetLoader.getCurrentNumOfDogs()) {
                objects.removeIndex(i);
                Gdx.app.log("4", numOfDogs + "");
                numOfDogs--;
            } else if (numOfDogs < AssetLoader.getCurrentNumOfDogs()) {
                objects.add(new Food(0, 0, 24, 24, 3, 1));
                numOfDogs++;
                Gdx.app.log("3", numOfDogs + "");
            }
        }
        if (numOfGrass < AssetLoader.getCurrentNumOfGrass()) {
            objects.add(new Food(0, 0, 24, 24, 7, 1));
            Gdx.app.log("2", numOfGrass + "");
            numOfGrass++;
        }
    }

    private void sort(int i) {
        objects.sort(compare);
        objects.get(i).needToSort = false;
    }


    private void initObjects(Player korean) {
        objects.add(korean);

        for (int i = 1; i < numOfObjects; i++) {
            addUnit(i);
            resetUnit(i);
        }

        objects.sort(compare);
    }

    private void addUnit(int i) {
        if (i < 20) {
            objects.add(new Decoration(0, 0, 64, 64, 1, 1));//trees
        } else if (i < 25) {
            objects.add(new Decoration(0, 0, 32, 32, 0, 1));//bushes
        } else if (i < 30) {
            objects.add(new Trap(0, 0, 32, 32, 11, 1));//rock
            Gdx.app.log("trap", "");
            traps.add(objects.get(i));
        } else if (i < 30 + numOfDogs) {
            Gdx.app.log("dog", "");
            objects.add(new Food(0, 0, 24, 24, 3, 1));//dackel
        } else {
            Gdx.app.log("trava", "");
            objects.add(new Food(0, 0, 24, 24, 7, 1));//trava
        }
    }

    private void resetUnit(int i) {
        if (objects.get(i).getID() == 4) {
            objects.get(i).ID = 3;
        } else if (objects.get(i).getID() == 3 || objects.get(i).getID() == 7) { //паруске: если текущий объект обязан двигаться на определенной позиции.
            objects.get(i).setX(getRandomX(i));
            objects.get(i).setY(getCurrentRandomY());
            //Gdx.app.log("newY", objects.get(i).getY() + "");
        } else if (objects.get(i).getID() == 1) {
            objects.get(i).setX(getRandomX(i));
            objects.get(i).setY(getRandomY());
        } else if (objects.get(i).getID() == 11) {
            if(runTime - spawnTime >= getDeltaTrapSpawn()) { //(0.3f, 0.9f)
                objects.get(i).setX(255);
                objects.get(i).setY(getCurrentRandomY());
                spawnTime = runTime;
            }
        } else {
            objects.get(i).setX(getRandomX(i));
            objects.get(i).setY(getRandomY());
        }

        getCurrentArea(i);

        objects.get(i).HP = objects.get(i).getStartHP();
        objects.get(i).needToReset = false;
    }




    private void getCurrentArea(int i) {
        float k;
        k = objects.get(i).getY() / (gameHeight - 64) - 0.1f;

        objects.get(i).setWidth(objects.get(i).getStartWidth() * k);

        objects.get(i).setHeight(objects.get(i).getWidth());
    }

    private float getCurrentRandomY() {
        int road;
        road = MathUtils.random(0, 2);

        switch (road) {
            case 0:
                return korean.getTopBorder();
            case 1:
                return korean.getMidPointY();
            case 2:
                return korean.getBotBorder();
        }

        return 0;
    }

    private float getRandomY() {
        return MathUtils.random(40, gameHeight - 64);
    }

    private float getRandomX(int i) {
        return MathUtils.random(255 + i * 3, 500);
    }

    public Array<Scrollable> getObjects() {
        return objects;
    }

    private float getDeltaTrapSpawn(){ //метод возвращающий необходимое для спавна новых ловушек время
        float k = ScoreBar.speedBooster / -111;
        //Gdx.app.log("Delta Time",1.0f - (k * 0.7f) + "");
        return 1.0f - (k * 0.7f);
    }
}
