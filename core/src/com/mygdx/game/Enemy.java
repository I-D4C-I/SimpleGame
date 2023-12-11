package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class Enemy extends Creature {

    private final Weapon weapon;
    private final float activityRadius;
    private float moveTimer;

    public Enemy(String texturePath, float speed, GameScreen screen) {
        super(texturePath, speed, screen);
        activityRadius = 200.0f;
        weapon = new Weapon("Rusty Sword", 30.0f,0.4f, 4.0f);
        setMaxHP(40.0f);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        float dst = gameScreen.getHero().getPosition().dst(this.position);
        if (dst < activityRadius) {
            direction.set(gameScreen.getHero().getPosition()).sub(this.position).nor();
        } else {
            moveTimer -= dt;
            if (moveTimer < 0.0f) {
                moveTimer = MathUtils.random(1.0f, 4.0f);
                direction.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
                direction.nor();
            }
        }

        moveForward(dt);

        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                gameScreen.getHero().takeDamage(weapon.getDamage());
            }
        }
    }
}
