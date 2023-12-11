package com.mygdx.game;

public enum ItemType {
    COINS(0),
    MEDKIT(1);

    int index;
    ItemType(int index) {
        this.index = index;
    }
}
