package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.*;

public class GameScreen {
    private SpriteBatch batch;
    private Stage stage;
    private BitmapFont font24;
    private Landscape landscape;
    private ItemsEmitter itemsEmitter;
    private TextEmitter textEmitter;
    private Hero hero;

    private boolean paused;
    private float spawnTimer;

    private List<Creature> allCharacters;
    private List<Enemy> allEnemies;

    public List<Enemy> getAllEnemies() {
        return allEnemies;
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public Hero getHero() {
        return hero;
    }

    public TextEmitter getTextEmitter() {
        return textEmitter;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public void create() {
        landscape = new Landscape(new Texture("grass_64x64.jpg"));
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));

        allCharacters = new ArrayList<>();
        allEnemies = new ArrayList<>();
        itemsEmitter = new ItemsEmitter();
        textEmitter = new TextEmitter();

        hero = new Hero("knight_64x64.png", 128 ,128, 100.0f);
        hero.setGameScreen(this);
        allCharacters.addAll(Arrays.asList(
                hero,
                new Enemy("skeleton_64x64.png", 40.0f, this),
                new Enemy("skeleton_64x64.png", 40.0f, this),
                new Enemy("skeleton_64x64.png", 40.0f, this),
                new Enemy("skeleton_64x64.png", 40.0f, this),
                new Enemy("skeleton_64x64.png", 40.0f, this),
                new Enemy("skeleton_64x64.png", 40.0f, this)
        ));
        for (Creature character : allCharacters) {
            if (character instanceof Enemy) {
                allEnemies.add((Enemy) character);
            }
        }

        stage = new Stage();

        Skin skin = new Skin();
        skin.add("simpleButton", new Texture("simpleButton.png"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        Group menuGroup = new Group();
        menuGroup.addActor(pauseButton);
        menuGroup.addActor(exitButton);
        exitButton.setPosition(150, 0);
        menuGroup.setPosition(980, 680);

        stage.addActor(menuGroup);
        Gdx.input.setInputProcessor(stage);

    }

    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        landscape.render(batch);
        Collections.sort(allCharacters);
        for (int i = 0; i < allCharacters.size(); i++) {
            allCharacters.get(i).render(batch);
        }
        itemsEmitter.render(batch);
        textEmitter.render(batch, font24);
        hero.renderHUD(batch, font24);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        if (!paused) {
            spawnTimer += dt;
            if (spawnTimer > 3.0f) {
                Enemy monster = new Enemy("skeleton_64x64.png", 40.0f, this);
                allCharacters.add(monster);
                allEnemies.add(monster);
                spawnTimer = 0.0f;
            }
            for (int i = 0; i < allCharacters.size(); i++) {
                allCharacters.get(i).update(dt);
            }
            for (int i = 0; i < allEnemies.size(); i++) {
                Enemy currentsMonster = allEnemies.get(i);
                if (!currentsMonster.isAlive()) {
                    allEnemies.remove(currentsMonster);
                    allCharacters.remove(currentsMonster);
                    itemsEmitter.generateRandomItem(currentsMonster.getPosition().x, currentsMonster.getPosition().y, 5, 0.6f);
                    hero.getExp(currentsMonster);
                }
            }
            for (int i = 0; i < itemsEmitter.getItems().length; i++) {
                Item it = itemsEmitter.getItems()[i];
                if (it.isActive()) {
                    float dst = hero.getPosition().dst(it.getPosition());
                    if (dst < 24.0f) {
                        hero.useItem(it);
                    }
                }
            }
            textEmitter.update(dt);
            itemsEmitter.update(dt);
        }
        stage.act(dt);
    }
}
