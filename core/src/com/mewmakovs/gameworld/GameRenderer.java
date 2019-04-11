package com.mewmakovs.gameworld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mewmakovs.gameobjects.*;
import com.mewmakovs.hkHelpers.AssetLoader;
import com.badlogic.gdx.utils.Array;


public class GameRenderer {

    private GameWorld world;

    private OrthographicCamera cam;
    private SpriteBatch batch;
    private ScrollHandler scrollHandler;
    private ScoreBar scoreBar;
    private Array<Scrollable> objects;
    private Array<Background> backgroundHandler;
    private float gameHeight;
    private ShapeRenderer shapeRenderer;
    private ShaderProgram black;
    private ShaderProgram shake;
    private ShaderProgram shakeAndWhite;
    private static int message;
    private static int koreanMessage;


    private float x = 256; //временно для пиздеца

    public GameRenderer(GameWorld world, int gameHeight) {
        this.world = world;
        scrollHandler = this.world.getScrollHandler();
        backgroundHandler = this.world.getBackgroundHandler().getBackgroundHandler();
        this.objects = scrollHandler.getObjects();
        this.scoreBar = this.world.getScoreBar();
        this.gameHeight = gameHeight;

        ShaderProgram.pedantic = false;
        black = new ShaderProgram(Gdx.files.internal("shaders/black&white/vertex.glsl"),
                Gdx.files.internal("shaders/black&white/fragment.glsl"));
        shake = new ShaderProgram(Gdx.files.internal("shaders/shake/vertex.glsl"),
                Gdx.files.internal("shaders/shake/fragment.glsl"));

        shakeAndWhite = new ShaderProgram(Gdx.files.internal("shaders/shake&white/vertex.glsl"),
                Gdx.files.internal("shaders/shake&white/fragment.glsl"));

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 256, gameHeight);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }


    public void render(float runTime) {
        if (world.getCurrentState() == GameWorld.GameState.GAMEOVER || world.getCurrentState() == GameWorld.GameState.RUNNING
                && scoreBar.getScore() < 5) {
            Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 0);
        } else {
            Gdx.gl.glClearColor(0.8f, 0.8f, 0.6f, 1);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        shake.begin();
        shake.setUniformf("u_distortion", MathUtils.random(3), MathUtils.random(3), 0f, 0f);
        shake.end();

        shakeAndWhite.begin();
        shakeAndWhite.setUniformf("u_distortion", MathUtils.random(3), MathUtils.random(3), 0f, 0f);
        shakeAndWhite.end();

        batch.begin();
        switch (world.getCurrentState()) {
            case START_MENU:
                startMenuScreen(runTime);
                break;

            case ENTER_GAME:
                enterRunningScreen(runTime);
                break;

            case RUNNING:
                runningScreen(runTime);
                UI();
                break;

            case GAMEOVER:
                runningScreen(runTime);
                gameOverScreen();
                break;

            case UPGRADE_MENU:
                upgradeScreen();
                break;
        }

        batch.end();


        if (world.getCurrentState() == GameWorld.GameState.RUNNING) {
            rectDraw();
        }

        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    private void startMenuScreen(float runTime) {
        batch.setShader(null);
        batch.draw((TextureRegion) AssetLoader.aStartAnimation.getKeyFrame(runTime),
                0, 0, 256, gameHeight);
    }

    private void enterRunningScreen(float runTime) { //ааа потрясающий зум!!!
        cam.position.x += 1;
        cam.zoom -= 1 * Gdx.graphics.getDeltaTime();
        if (cam.zoom < 0) {
            cam.zoom = 1;
            cam.position.x = 128;
            world.start();
        }
        startMenuScreen(runTime);

        AssetLoader.rusFont.getData().setScale(0.02f);
        AssetLoader.rusFont.draw(batch, "Хуй", 177, 77);
    }

    private void runningScreen(float runTime) {
        setShaders();
        backgroundDraw();
        objectsDraw(runTime);
        batch.setShader(null);
    }


    private void UI() {
        batch.draw(AssetLoader.rTV,
                0, 0, 256, gameHeight);
        batch.draw(AssetLoader.rNeVeganBar,
                36, 5, 40, 12);
        batch.draw(AssetLoader.rVeganBar,
                180, 5, 40, 12);

        AssetLoader.shadow.draw(batch, "" + world.getScoreBar().getDistance() + "m", world.getScoreBar().getPosition().x - 1,
                world.getScoreBar().getPosition().y);
        AssetLoader.font.draw(batch, "" + world.getScoreBar().getDistance() + "m", world.getScoreBar().getPosition().x,
                world.getScoreBar().getPosition().y);

        AssetLoader.shadow.draw(batch, "HP: " + world.getKorean().getHP(),
                105, 7);
        AssetLoader.font.draw(batch, "HP: " + world.getKorean().getHP(),
                106, 7);

        AssetLoader.rusFont.getData().setScale(0.1f);
        AssetLoader.rusFont.draw(batch, "ТО ШО НЕ ДОЛЖНО " +
                        "\n" + "РАВНЯТЬСЯ НУЛЮ: " + world.getScoreBar().getScore() + "%", 15,
                gameHeight - 25);

        AssetLoader.shadow.draw(batch, "FPS: " + (int) (1 / Gdx.graphics.getDeltaTime()) + "", 200, gameHeight - 20);
        AssetLoader.font.draw(batch, "FPS: " + (int) (1 / Gdx.graphics.getDeltaTime()) + "", 201, gameHeight - 20);


        //опять же временная залупа
        if(world.getScoreBar().getDistance() > 60 && world.getScoreBar().getDistance() < 64){
            if(x > -400) {
                AssetLoader.rusFont.getData().setScale(0.7f);
                AssetLoader.rusFont.draw(batch, "ЧЕ С ГЛАЗАМИ", x -= 500 * Gdx.graphics.getDeltaTime(), 20);
                Gdx.app.log("x", x + "");
            } else {
                x = 256;
            }
        }

        if(world.getScoreBar().getDistance() > 100) {
            if(x > -256) {
                AssetLoader.rusFont.getData().setScale(0.7f);
                AssetLoader.rusFont.draw(batch, "ПИЗДЕЦ", x -= 500 * Gdx.graphics.getDeltaTime(), 20);
            }
        }
    }

    private void gameOverScreen() {
        batch.enableBlending();

        world.getScoreBar().move();
        AssetLoader.font.getData().setScale(world.getScoreBar().getScale());

        AssetLoader.shadow.draw(batch, "" + world.getScoreBar().getDistance() + "m", world.getScoreBar().getPosition().x - 1,
                world.getScoreBar().getPosition().y);
        AssetLoader.font.draw(batch, "" + world.getScoreBar().getDistance() + "m", world.getScoreBar().getPosition().x,
                world.getScoreBar().getPosition().y);

        afterDeathMessage();

        batch.draw(AssetLoader.rTV,
                0, 0, 256, gameHeight);
    }

    private void afterDeathMessage() {
        AssetLoader.rusFont.getData().setScale(0.1f);

        if(world.getScoreBar().getDistance() > 80) {
            AssetLoader.rusFont.draw(batch, "НЕ СМОТРИ НА ПОТОЛОК", 10, 20);
        }

        if(message == 3){
            AssetLoader.rusFont.draw(batch, "У КОГО-ТО НОВЫЙ РЕКОРД!!" +
                    "\n" + "И КОМУ-ТО ПОРА ПОВТОРЯТЬ" +
                    "\n" + "МАТЕМАТИКУ", 10, gameHeight - 40);
            return;
        } else if (world.getScoreBar().getDistance() == 47) {
            AssetLoader.rusFont.draw(batch, "СЛОВНО ТВОИХ ХРОМОСОМ", 70, gameHeight - 20);
            return;
        }


        if (koreanMessage <= 4) {
            if (world.getKorean().getHP() > 0) {
                AssetLoader.rusFont.draw(batch, "ДА, КАМНЕЙ НЕ БЫЛО. ПРОСТО ТЕЛО ТЕПЕРЬ ПРИНАДЛЕЖИТ ВЕГАНУ", 10, gameHeight - 25);
                return;
            } else if (world.getKorean().getHP() == 0) {
                switch (message) {
                    case 0:
                        AssetLoader.rusFont.draw(batch, "ЖЕРНАК, СТАРАЙСЯ ЛУЧШЕ, ХИТБОКСЫ ТЕПЕРЬ \n ПРАВДА ЧЕСТНЫЕ!!!", 10, gameHeight - 25);
                        break;
                    case 1:
                        AssetLoader.rusFont.draw(batch, "МАНСИТЬ СЛОЖНО, НО ВОЗМОЖНО", 10, gameHeight - 25);
                        break;
                }
            }
            return;
        } else if (world.getScoreBar().getDistance() < 10) {
            switch (message) {
                case 0:
                    AssetLoader.rusFont.draw(batch, "СЧЕТ КАК КВИНТЭССЕНЦИЯ " +
                            "\n" + "ТВОЕГО РАЗМЕРА(ЕСЕСНА В СМ) ", 10, gameHeight - 25);
                    break;
                case 1:
                    AssetLoader.rusFont.draw(batch, "КАК БУДТО ПРОШЕЛ ТЕСТ НА IQ", 10, gameHeight - 20);
                    break;
            }
        } else if (world.getScoreBar().getDistance() <= 22 && world.getScoreBar().getDistance() >= 16) {
            AssetLoader.rusFont.draw(batch, "НЕТ, НЕ САНТИМЕТРОВ И НЕТ, НЕ В ШТАНАХ", 10, gameHeight - 20);

        } else if (world.getScoreBar().getDistance() > 22) {
            switch (message) {
                case 0:
                    AssetLoader.rusFont.draw(batch, "УЖЕ ЛУЧШЕ", 10, gameHeight - 25);
                    break;
                case 1:
                    AssetLoader.rusFont.draw(batch, "ЕЩЕ НЕМНОГО И У ФАЛЕЙЧИКА " +
                            "\n" + "ПОЯВИТСЯ СТИМУЛ", 10, gameHeight - 25);
                    break;
            }
        }
    }

    public static int getRandomMessage() {
        Gdx.app.log("mess", message + "");
        return message = MathUtils.random(0, 1);
    }

    public static void setMessage(int state){
        message = state;
    }

    public static void setKoreanMessage(){
        koreanMessage = MathUtils.random(0, 10);
        Gdx.app.log("koreanMess", koreanMessage + "");

    }

    private void backgroundDraw() {
        for (int j = 0; j < backgroundHandler.size; j++) {
            batch.draw(AssetLoader.rsMountains[backgroundHandler.get(j).getMountain()],
                    backgroundHandler.get(j).getBody().getX(), backgroundHandler.get(j).getBody().getY(),
                    backgroundHandler.get(j).getBody().getWidth(), backgroundHandler.get(j).getBody().getHeight());
        }
    }

    private void objectsDraw(float runTime) {
        for (int i = 0; i < objects.size; i++) {
            switch (objects.get(i).getID()) {
                case 0:
                    batch.draw(AssetLoader.regions[0],
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                case 1:
                    batch.draw(AssetLoader.regions[1],
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                case 2:
                    AssetLoader.aKoreanRun.setFrameDuration(ScoreBar.getDistanceTimer() / 10.f);
                    batch.draw((TextureRegion) AssetLoader.aKoreanRun.getKeyFrame(runTime),
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());

                    break;
                case 3:
                    batch.draw((TextureRegion) AssetLoader.aDackelRun.getKeyFrame(runTime),
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;

                case 4:
                    if (!objects.get(i).isDeath()) {
                        batch.draw((TextureRegion) AssetLoader.aDackelHalfRun.getKeyFrame(runTime),
                                objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    } else {
                        if (!scoreBar.isEnd()) {
                            AssetLoader.shadow.draw(batch, objects.get(i).getSaturability() + "",
                                    objects.get(i).getX(), objects.get(i).getY());
                            AssetLoader.font.draw(batch, objects.get(i).getSaturability() + "",
                                    objects.get(i).getX() + 1, objects.get(i).getY());
                        }
                    }
                    break;

                case 7:
                    if (!objects.get(i).isDeath()) {
                        batch.draw(AssetLoader.rVegans,
                                objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    } else {
                        if (!scoreBar.isEnd()) {
                            AssetLoader.shadow.draw(batch, objects.get(i).getSaturability() + "",
                                    objects.get(i).getX(), objects.get(i).getY());
                            AssetLoader.font.draw(batch, objects.get(i).getSaturability() + "",
                                    objects.get(i).getX() + 1, objects.get(i).getY());
                        }
                    }
                    break;

                case 5:
                    batch.draw((TextureRegion) AssetLoader.aKoreanFall.getKeyFrame(runTime),
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                case 6:
                    AssetLoader.aKoreanEat.setFrameDuration(ScoreBar.getDistanceTimer() / 10.f);
                    batch.draw((TextureRegion) AssetLoader.aKoreanEat.getKeyFrame(runTime),
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                case 10:
                    batch.draw((TextureRegion) AssetLoader.aGreenPeace.getKeyFrame(runTime),
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                case 11:
                    batch.draw(AssetLoader.rRock,
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                case 12:
                    batch.draw(AssetLoader.regions[12],
                            objects.get(i).getX(), objects.get(i).getY(), objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;
                default:
                    break;
            }
        }
    }

    private void rectDraw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, 1);
        shapeRenderer.setColor(0.12f, 0.203f, 0.003f, 1);

        shapeRenderer.rect(48.5f, 7.5f, scoreBar.getNeVeganScore(), 4.5f);
        shapeRenderer.rect(208f, 7.5f, -scoreBar.getVeganScore(), 4.5f);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        /*for (int i = 0; i < objects.size; i++) {
            switch (objects.get(i).getID()) {
                case 2:
                    shapeRenderer.rect(objects.get(i).getX() + 5, objects.get(i).getY() + 5,
                            objects.get(i).getWidth() - 10, objects.get(i).getHeight() - 10);
                    break;

                case 12:
                    shapeRenderer.rect(objects.get(i).getX(), objects.get(i).getY(),
                            objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;

                case 11:
                    shapeRenderer.rect(objects.get(i).getX(), objects.get(i).getY(),
                            objects.get(i).getWidth(), objects.get(i).getHeight());
                    break;

                default:
                    break;
            }
        }*/

        shapeRenderer.end();
    }

    private void upgradeScreen() {
        Stats.update();
        batch.draw(AssetLoader.rUpGradeScreen,
                0, 0, 256, gameHeight);

        AssetLoader.font.getData().setScale(0.1f);
        AssetLoader.rusFont.getData().setScale(0.1f);


        AssetLoader.rusFont.draw(batch, "УРОВЕНЬ " + AssetLoader.getCurrentLvl(), 8, 8);


        AssetLoader.rusFont.draw(batch, "ОПЫТ: " + AssetLoader.getCurrentExp() + " / " + 100 * Math.pow(2, AssetLoader.getCurrentLvl()),
                8, 15);


        AssetLoader.rusFont.draw(batch, "ОЧКОВ УЛУЧШЕНИЯ ДОСТУПНО: " + AssetLoader.getSkillPoint(),
                115, 8);

        AssetLoader.rusFont.draw(batch, "ЛУЧШИЙ ЗАБЕГ " +
                        "\n " + "В МЕТРАХ: " + AssetLoader.getHighDistance(),
                15, gameHeight - 15);

        switch (world.getUpgradeState()) {
            case FIRST_UPGRADE:
                AssetLoader.rusFont.draw(batch, "УЛУЧШЕННОЕ ПИЩЕВАРЕНИЕ 1: " +
                                "\n " + "УМЕНЬШАЕТ ВРЕД ОТ ПОГЛОЩЕНИЯ" +
                                "\n " + "ОДНОЙ ЕДЕНИЦЫ ТРАВЫ " +
                                "\n " + "СЕЙЧАС: " + AssetLoader.getCurrentVeganSaturability(),
                        114, 38);

                break;
            case SECOND_UPGRADE:
                AssetLoader.rusFont.draw(batch, "УЛУЧШЕННОЕ ПИЩЕВАРЕНИЕ 2: " +
                                "\n " + "УВЕЛИЧИВАЕТ КОЛИЧЕСТВО ОЧКОВ" +
                                "\n " + "ПОЛУЧАЕМОЕ ЗА ОДНУ " +
                                "\n " + "ПОЛОВИНУ СОБАКИ" +
                                "\n " + "СЕЙЧАС: " + AssetLoader.getCurrentNeVeganSaturability(),
                        114, 38);

                break;
            case THIRD_UPGRADE:
                AssetLoader.rusFont.draw(batch, "ЧИСЛО СОБАК НА ПОЛЕ. " +
                                "\n" + "ЧИСЛО СОБАК НА ПОЛЕ " +
                                "\n " + "ДА, ОПИСАНИЕ РАВНО НАЗВАНИЮ" +
                                "\n " + "СЕЙЧАС: " + AssetLoader.getCurrentNumOfDogs(),
                        116, 38);

                break;
            case FOURTH_UPGRADE:
                AssetLoader.rusFont.draw(batch, "КАМНЕСТОЙКОСТЬ." +
                                "\n " + "ПОВЫШЕНИЕ КАМНЕСТОЙКОСТИ" +
                                "\n " + "СПОСОБСТВУЕТ СТАНОВЛЕНИЮ" +
                                "\n " + "НОВЫХ РЕКОРДОВ" +
                                "\n " + "СЕЙЧАС: " + AssetLoader.getCurrentHP() +
                                "\n " + "(НЕ РЕКОММЕНДУЕТСЯ ДЛЯ " +
                                "\n " + "УЛУЧШЕНИЯ ХАРДКОРЩИКАМ" +
                                "\n " + "(ЖЕРНАКАМ))",
                        116, 38);


                break;

            case FIFTH_UPGRADE:
                AssetLoader.rusFont.draw(batch, "ЧИСЛО ТРАВЫ НА ПОЛЕ. " +
                                "\n" + "ВСЕ ПРОСТО. ЕСЛИ НЕ ПОНЯТНО - " +
                                "\n " + "МОЖНО ЗАГУГЛИТЬ" +
                                "\n " + "СЕЙЧАС: " + AssetLoader.getCurrentNumOfGrass(),
                        116, 38);

                break;
            case SIXTH_UPGRADE:
                AssetLoader.rusFont.draw(batch, "ВЕРХНИЙ ПРЕДЕЛ КОНТРОЛЯ." +
                                "\n " + "УВЕЛИЧИВАЕТ ПЕРВОНАЧАЛЬНОЕ" +
                                "\n " + "КОЛИЧЕСТВО КОНТРОЛЯ НАД" +
                                "\n " + "ТЕЛОМ У НЕВЕГАНА" +
                                "\n " + "СЕЙЧАС: " + AssetLoader.getCurrentBottomScore(),
                        116, 38);

                break;
            case SEVENTH_UPGRADE:

                AssetLoader.rusFont.draw(batch, "ЗАМЕДЛЕНИЕ МЕТАБОЛИЗМА. " +
                                "\n " + "(С КАЖДЫМ РАЗОМ УЛУЧШАЯ ЭТОТ " +
                                "\n " + "НАВЫК, ВОЗГЛАСОВ ВИДА " +
                                "\n " + "<<ДА ЧТО Я ЖЕ ПРЯМО" +
                                "\n " + " БЕЖАЛ!!!>> БУДЕТ МЕНЬШЕ)" +
                                "\n " + "ЗАМЕДЛЯЕТ ПОТЕРЮ КОНТРОЛЯ" +
                                "\n " + "ЗА КАЖДЫЙ МЕТР ПРОБЕГА." +
                                "\n " + "СЕЙЧАС:" + AssetLoader.getCurrentScoreDown(),
                        116, 38);

                break;
            default:
                break;
        }
    }

    private boolean setBlackShader() {
        if (world.getCurrentState() == GameWorld.GameState.GAMEOVER || world.getCurrentState() == GameWorld.GameState.RUNNING
                && scoreBar.getScore() < 5) {
            return true;
        } else {
            return false;
        }
    }

    private boolean setShakeShader() {
        if (world.getCurrentState() == GameWorld.GameState.GAMEOVER || world.getCurrentState() == GameWorld.GameState.RUNNING
                && world.getKorean().getID() == 5) {
            return true;
        } else {
            return false;
        }
    }

    private void setShaders() {
        if (setBlackShader() && setShakeShader()) {
            batch.setShader(shakeAndWhite);
        } else {
            if (setBlackShader()) {
                batch.setShader(black);
            }
            if (setShakeShader()) {
                batch.setShader(shake);
            }
        }
    }
}