package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Creature extends Entity implements Comparable<Creature>{

    protected Texture textureHP;
    protected float maxHP, currentHP;
    protected float damageEffectTimer;
    protected float attackTimer;
    protected Vector2 temp;


    public Creature(String texturePath, float x, float y, float speed) {
        super(texturePath, x, y, speed);
        setTextureHP();
        setMaxHP(100.0f);
        direction = new Vector2(0,0);
        temp = new Vector2(0,0);
    }

    public Creature(String texturePath, float speed, GameScreen screen) {
        super(texturePath, speed, screen);
        setTextureHP();
        setMaxHP(100.0f);
        direction = new Vector2(0,0);
        temp = new Vector2(0,0);
    }

    public void setTextureHP() {
        this.textureHP = new Texture("bar.png");
    }

    public void setMaxHP(float maxHP) {
        this.maxHP = maxHP;
        currentHP = maxHP;
    }

    public void moveForward(float dt){
        if(gameScreen.getLandscape().isCellPassable(temp.set(position).mulAdd(direction, speed * dt)))
            position.set(temp);
        else if(gameScreen.getLandscape().isCellPassable(temp.set(position).mulAdd(direction,speed*dt).set(temp.x,position.y)))
            position.set(temp);
        else  if(gameScreen.getLandscape().isCellPassable(temp.set(position).mulAdd(direction,speed*dt).set(position.x, temp.y)))
            position.set(temp);
    }

    public boolean isAlive(){
        return  currentHP > 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(damageEffectTimer > 0)
            batch.setColor(1,1-damageEffectTimer,1-damageEffectTimer,1);
        super.render(batch);
        batch.setColor(0,0,0,1);
        batch.draw(textureHP, position.x-52, position.y+38, maxHP+4, textureHP.getHeight()+4);
        batch.setColor(1,0,0,1);
        batch.draw(textureHP, position.x-50, position.y+40, 0,0,currentHP, textureHP.getHeight(), 1,1,0,0,0,textureHP.getWidth(),textureHP.getHeight(),false,false);
        batch.setColor(1,1,1,1);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        damageEffectTimer -= dt;
        if(damageEffectTimer < 0.0f)
            damageEffectTimer = 0.0f;
    }

    public void takeDamage(float amount){
        currentHP-=amount;
        damageEffectTimer += 0.5f;
        if(damageEffectTimer > 1.0f){
            damageEffectTimer = 1.0f;
        }
    }

    @Override
    public int compareTo(Creature o) {
        return (int) (this.position.y - o.getPosition().y);
    }
}
