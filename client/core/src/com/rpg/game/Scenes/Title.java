package com.rpg.game.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.rpg.game.Server;
import com.rpg.game.elements.Player;

public class Title implements Screen  {

    private SpriteBatch batch;
    private Player player;
    private Timer.Task timerUpdatePositionPlayer;

    @Override
    public void show() {
        player = new Player(new Vector2((float)(Math.random() * 200), (float)(Math.random() * 200)), "" + (int)(Math.random() * 10000), true);
        batch = new SpriteBatch();
        Server.init();
        Server.joinPlayer(player);

        Timer.Task timerUpdatePositionPlayer = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Server.updatePlayerPosition();
            }
        }, 1, 0.05f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,  0.631f , 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        player.update();

        for(Player player : Server.getPlayers()) {
            player.draw(batch);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        timerUpdatePositionPlayer.cancel();
    }
}
