package com.rpg.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.rpg.game.api.ColorSquare;

public class Player {

    private Texture texture;
    private Vector2 pos;
    private String name;

    public Player(Vector2 pos, String name, boolean setTexture) {
        if(setTexture) this.texture = new ColorSquare(1, 1, Color.GREEN).get();
        this.pos = pos;
        this.name = name;
    }

    public void update() {
        if(Gdx.input.isKeyPressed(Input.Keys.A)) pos.x = pos.x - 1;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) pos.x = pos.x + 1;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) pos.y = pos.y - 1;
        if(Gdx.input.isKeyPressed(Input.Keys.W)) pos.y = pos.y + 1;
    }

    public void draw(SpriteBatch batch) {
        if(texture != null) batch.draw(texture, pos.x, pos.y);
    }


    public void setTexture() {
        this.texture = new ColorSquare(1, 1, Color.GREEN).get();
    }
    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
