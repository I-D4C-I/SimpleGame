package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Item {

    private Vector2 position;
    private Vector2 velocity;
    private ItemType itemType;
    private float time;
    private float timeMax;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Item() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.itemType = ItemType.COINS;
        this.timeMax = 5.0f;
        this.time = 0.0f;
        this.active = false;
    }

    public void setup(float x, float y, ItemType type) {
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
        this.itemType = type;
        this.time = 0.0f;
        this.active = true;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if (time > timeMax) {
            deactivate();
        }
    }
}
