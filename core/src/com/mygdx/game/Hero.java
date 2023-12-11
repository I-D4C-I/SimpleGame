package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Hero extends Creature {

    private final Weapon weapon;
    private int coins;
    private int level;
    private int exp;
    private int expTo;

    private StringBuilder stringHelper;


    public Hero(String texturePath, float x, float y, float speed) {
        super(texturePath, x, y, speed);
        stringHelper = new StringBuilder();
        level = 1;
        coins = 0;
        exp = 0;
        this.weapon = new Weapon("Spear", 80.0f, 1.0f, 4.0f);
        setNewExpTo();
    }

    public void setNewExpTo(){
        expTo = level*100 + level*15;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        stringHelper.setLength(0);
        stringHelper.append("Level: ").append(level).append('\n')
                .append("Exp: ").append(exp).append(" / ").append(expTo).append('\n')
                .append("Coins: ").append(coins);
        font24.draw(batch, stringHelper, 20, 700);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        Enemy nearestEnemy = null;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < gameScreen.getAllEnemies().size(); i++) {
            float dst = gameScreen.getAllEnemies().get(i).getPosition().dst(this.position);
            if (dst < minDist) {
                minDist = dst;
                nearestEnemy = gameScreen.getAllEnemies().get(i);
            }
        }

        if (nearestEnemy != null && minDist < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                nearestEnemy.takeDamage(weapon.getDamage());
            }
        }

        direction.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.y = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = -1.0f;
        }

        moveForward(dt);
    }

    public void getExp(Enemy monster) {
        exp += (int) (monster.maxHP * 5);
        if (exp >= expTo) {
            level++;
            exp = 0;
            maxHP += 10;
            currentHP = maxHP;
            setNewExpTo();
        }
    }

    public void useItem(Item item) {
        switch (item.getItemType()) {
            case COINS:
                int amount = MathUtils.random(3, 5);
                coins += amount;
                stringHelper.setLength(0);
                stringHelper.append("coins ").append("+").append(amount);
                gameScreen.getTextEmitter().setup(item.getPosition().x, item.getPosition().y, stringHelper);
                break;
            case MEDKIT:
                currentHP += 5;
                stringHelper.setLength(0);
                stringHelper.append("hp ").append("+5");
                gameScreen.getTextEmitter().setup(item.getPosition().x, item.getPosition().y, stringHelper);
                if (currentHP > maxHP) {
                    currentHP = maxHP;
                }
                break;
        }
        item.deactivate();
    }
}
