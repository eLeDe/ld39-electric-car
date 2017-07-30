package gal.elede.ld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;

import java.util.ArrayList;
import java.util.Iterator;


public class Ld39 extends ApplicationAdapter {

    public static final int TITLE = 0;
    public static final int INSTRUCTIONS = 1;
    public static final int GAME = 2;
    public static final int GAME_OVER = 3;
    public static final int WIN = 4;

    private float delay = 0.5f;
    private static final float MAX_DELAY = 0.5f;
    private int status = TITLE;
    SpriteBatch batch;
    Sprite car;
    ArrayList<Sprite> batteries;
    Texture img;
    Boolean movement;
    int orientation = 0;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera cam;
    ShapeRenderer shapeRenderer;
    float power;
    float MAX_POWER = 10000;
    TiledMapTileLayer tiledMapTileLayer;
    float velocity = 5.0f;
    Sprite gameOver;
    Sprite gameOverSub;
    private Sprite title;
    private Sprite ludumdare;
    private Sprite chusta;
    private Sprite elede;
    private Sprite instructions;
    private Texture batteryTex;
    private Sprite win;
    private Music music;
    private Sound winSound;
    private Sound loseSound;
    private Sound pickBatterySound;
    private Sound engineSound;
    private Sound alertSound;


    public void addBattery(int x, int y) {
        Sprite battery = new Sprite(batteryTex);
        battery.setPosition(x*64, (119-y)*64);
        batteries.add(battery);
    }

    public void addAllBatteries() {
        addBattery(20, 102);
        addBattery(11, 94);
        addBattery(51, 99);
        addBattery(67, 108);
        addBattery(41, 88);
        addBattery(42, 88);
        addBattery(40, 89);
        addBattery(43, 89);
        addBattery(22, 71);
        addBattery(23, 87);
        addBattery(11, 40);
        addBattery(33, 48);
        addBattery(49, 65);

        addBattery(67, 81);
        addBattery(69, 81);
        addBattery(68, 82);
        addBattery(67, 83);
        addBattery(69, 83);
        addBattery(75, 87);
        addBattery(66, 22);
        addBattery(11, 21);
        addBattery(11, 23);
        addBattery(11, 26);
        addBattery(69, 13);
        addBattery(81, 36);
        addBattery(96, 38);

        addBattery(98, 73);
        addBattery(127, 108);
        addBattery(104, 88);
        addBattery(115, 89);
        addBattery(123, 85);
        addBattery(107, 31);
        addBattery(12, 90);
        addBattery(49, 38);
        addBattery(50, 66);
        addBattery(88, 18);
        addBattery(83, 108);

    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("car.png");
        car = new Sprite(img);

        batteries = new ArrayList<Sprite>();
        batteryTex = new Texture("battery.png");
        addAllBatteries();

        cam = new OrthographicCamera(640, 480);
        cam.setToOrtho(false);
        cam.update();
        tiledMap = new TmxMapLoader().load("ld39.tmx");
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);

        shapeRenderer = new ShapeRenderer();

        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

        gameOver = new Sprite(new Texture("game_over.png"));
        gameOverSub = new Sprite(new Texture("game_over_subtitle.png"));
        gameOverSub.setScale(gameOver.getWidth()/gameOverSub.getWidth());

        title = new Sprite(new Texture("title.png"));
        ludumdare = new Sprite(new Texture("ludumdare.png"));
        ludumdare.setScale(title.getWidth()/ludumdare.getWidth());
        chusta = new Sprite(new Texture("chusta.png"));
        elede = new Sprite(new Texture("elede.png"));
        elede.setScale(title.getWidth()/ludumdare.getWidth());


        instructions = new Sprite(new Texture("instructions.png"));
        instructions.setScale(title.getWidth()/instructions.getWidth());

        win = new Sprite(new Texture("win.png"));

        music = Gdx.audio.newMusic(Gdx.files.internal("background.ogg"));

        music.setLooping(true);
        music.play();

        winSound = Gdx.audio.newSound(Gdx.files.internal("win.wav"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("lose.wav"));
        pickBatterySound = Gdx.audio.newSound(Gdx.files.internal("get_battery.wav"));
        engineSound = Gdx.audio.newSound(Gdx.files.internal("engine.wav"));
        engineSound.loop(0.3f);
        engineSound.pause();
        alertSound = Gdx.audio.newSound(Gdx.files.internal("alert.wav"));
        alertSound.loop(0.5f);
        alertSound.pause();
    }

    private void goGameOver() {
        gameOver.setPosition(car.getX()-gameOver.getWidth()/2f, car.getY()+gameOver.getHeight()/2f);
        gameOverSub.setPosition(car.getX()-gameOverSub.getWidth()/2f, car.getY()+gameOverSub.getHeight()/2f - 200);
        removeAllBatteries();
    }

    @Override
    public void render() {

        switch (status) {
            case TITLE:
                goTitle();
                delay -= Gdx.graphics.getDeltaTime();
                System.out.println("Delay:" + delay);
                if (delay < 0 && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    delay = MAX_DELAY;
                    status = INSTRUCTIONS;
                }
                break;
            case INSTRUCTIONS:
                goInstructions();
                delay -= Gdx.graphics.getDeltaTime();
                System.out.println("Delay:" + delay);
                if (delay < 0 && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    delay = MAX_DELAY;
                    status = GAME;
                }
                break;
            case GAME:
                goGame(); break;
            case GAME_OVER:
                goGameOver();
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    status = TITLE;
                }
                break;
            case WIN:
                goWin();
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    status = TITLE;
                }
                break;
            default:
                goTitle();
        }

        // DRAW
        cam.position.x = car.getX();
        cam.position.y = car.getY();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        for (Sprite battery : batteries) {
            battery.draw(batch);
        }
        car.draw(batch);

        switch (status) {
            case TITLE:
                title.draw(batch);
                ludumdare.draw(batch);
                chusta.draw(batch);
                elede.draw(batch);
                break;
            case INSTRUCTIONS:
                instructions.draw(batch);
                break;
            case GAME:
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                gameOverSub.draw(batch);
                break;
            case WIN:
                win.draw(batch);
            default:
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, 20, cam.viewportHeight);
        if (power < MAX_POWER/5f) {
            shapeRenderer.setColor(Color.RED);
        } else {
            shapeRenderer.setColor(Color.BLUE);
        }
        shapeRenderer.rect(1, 0, 18, power * cam.viewportHeight / MAX_POWER);
        shapeRenderer.end();


    }


    private void goInstructions() {
        instructions.setPosition(car.getX()-instructions.getWidth()/2f, car.getY()+instructions.getHeight()/2f - 250);
    }



    private void restoreBatteries() {
        if (batteries.size() == 0) {
            addAllBatteries();
        }
    }


    private void goTitle() {
        title.setPosition(car.getX()-title.getWidth()/2f, car.getY()+title.getHeight()/2f);
        chusta.setPosition(title.getX() + 10, title.getY() - 100);
        ludumdare.setPosition(car.getX()-ludumdare.getWidth()/2f, car.getY()+ludumdare.getHeight()/2f - 250);
        elede.setPosition(car.getX()-elede.getWidth()/2f, car.getY()+elede.getHeight()/2f - 200);

        car.setPosition(11*64, 11*64);
        //car.setPosition(150*64, (119-107)*64);
        car.setRotation(270.0f);
        power = MAX_POWER;

        restoreBatteries();
    }



    private void removeAllBatteries() {
        batteries.clear();
    }

    private void goWin() {
        win.setPosition(car.getX()-win.getWidth()/2f, car.getY()+win.getHeight()/2f);
        removeAllBatteries();
    }

    private void goGame() {
        int tilePosX = (int) Math.floor((car.getX() + 32f) / 64f);
        int tilePosY = (int) Math.floor((car.getY() + 32f) / 64f);

        System.out.println("Car: (" + tilePosX + ", " + tilePosY + ") Battery: " +
                power + " Cell: " + tiledMapTileLayer.getCell(tilePosX, tilePosY).getTile().getId());


        if (tilePosX == 156 && (tilePosY == 119-108 || tilePosY == 119-107)) {
            status = WIN;
            winSound.play();
            engineSound.pause();
            alertSound.pause();
            return;
        }

        // MOVEMENT
        movement = true;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (tiledMapTileLayer.getCell(tilePosX, tilePosY).getTile().getId() != 2) {
                car.setY(car.getY() + velocity);
            } else {
                if (tiledMapTileLayer.getCell(tilePosX, tilePosY + 1).getTile().getId() != 2) {
                    car.setY(car.getY() + velocity);
                }
            }
            orientation = 0;
            car.setRotation(0.0f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (tiledMapTileLayer.getCell(tilePosX, tilePosY).getTile().getId() != 2) {
                car.setY(car.getY() - velocity);
            } else {
                if (tilePosY > 0 && tiledMapTileLayer.getCell(tilePosX, tilePosY - 1).getTile().getId() != 2) {
                    car.setY(car.getY() - velocity);
                }
            }
            orientation = 2;
            car.setRotation(180.0f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (tiledMapTileLayer.getCell(tilePosX, tilePosY).getTile().getId() != 2) {
                car.setX(car.getX() - velocity);
            } else {
                if (tilePosX > 0 && tiledMapTileLayer.getCell(tilePosX - 1, tilePosY).getTile().getId() != 2) {
                    car.setX(car.getX() - velocity);
                }
            }
            orientation = 3;
            car.setRotation(90.0f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (tiledMapTileLayer.getCell(tilePosX, tilePosY).getTile().getId() != 2) {
                car.setX(car.getX() + velocity);
            } else {
                if (tiledMapTileLayer.getCell(tilePosX + 1, tilePosY).getTile().getId() != 2) {
                    car.setX(car.getX() + velocity);
                }
            }
            orientation = 1;
            car.setRotation(270.0f);
        } else {
            movement = false;
            engineSound.pause();
        }
        if (movement) {
            power -= 10;
            alertSound.pause();
            engineSound.resume();

            if (power < 0) {
                status = GAME_OVER;
                loseSound.play();
                engineSound.pause();
            } else if (power < MAX_POWER/5.0f) {
                alertSound.resume();
            }
        }


        if (car.getX() < 0) {
            car.setX(0f);
        }

        if (car.getY() < 0) {
            car.setY(0f);
        }

        Iterator<Sprite> batteryIterator = batteries.iterator();
        while (batteryIterator.hasNext()) {
            Sprite battery = batteryIterator.next();
            if (battery.getBoundingRectangle().overlaps(car.getBoundingRectangle())) {
                System.out.println("Found battery");
                power += 5000;
                batteryIterator.remove();
                pickBatterySound.play();
            }
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        batteryTex.dispose();

        music.dispose();
        winSound.dispose();
        loseSound.dispose();
        pickBatterySound.dispose();
        engineSound.dispose();
        alertSound.dispose();
    }
}
