package com.rpg.game;

import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;

    private String name;

    public Player(String name, Vector2 position) {
        this.position = position;
        this.name = name;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 vector) {
        position = vector;
    }

    public String getName() {
        return name;
    }
}
