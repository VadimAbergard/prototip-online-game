package com.rpg.game.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameСollider {

    private Body body;

    public GameСollider(World world, BodyDef.BodyType type, Vector2 position, int width, int height, String tag) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        Body body = world.createBody(bodyDef);
        body.setTransform(position, 0);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        body.createFixture(poly, 0);
        body.getFixtureList().get(0).setUserData(tag);
        poly.dispose();

        this.body = body;
    }

    public Body get() {
        return body;
    }
}
