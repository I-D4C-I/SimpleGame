package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    protected GameScreen gameScreen;
    protected Texture texture;
    protected Vector2 position;
    protected Vector2 direction;
    protected float speed;

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Entity(String texturePath, float x, float y, float speed) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.speed = speed;
    }

    public Entity(String texturePath, float speed, GameScreen screen){
        this.gameScreen = screen;
        this.position = new Vector2(MathUtils.random(0,1000.0f), MathUtils.random(0,700.0f));
        while (!gameScreen.getLandscape().isCellPassable(position))
            this.position = new Vector2(MathUtils.random(0,1000.0f), MathUtils.random(0,700.0f));
        this.texture = new Texture(texturePath);
        this.speed = speed;
    }

    public void render(SpriteBatch batch){
        float posX = position.x - (float) texture.getWidth() /2;
        float posY = position.y - (float) texture.getHeight() /2;
        batch.draw(texture,posX,posY);
    }

    public void update(float dt){

    }
}
