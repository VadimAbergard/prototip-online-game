package com.rpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Timer;
import com.rpg.game.elements.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    private static Socket socket;
    private static List<Player> playerList;
    private static int playerNumInList;
    private static int online;
    private static boolean playerJoin;
    public static void init() {
        playerList = new ArrayList<>();
    }

    public static List<Player> getPlayers() {
        return playerList;
    }

    public static void joinPlayer(final Player player) {
        System.out.println("joinPlayer");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connect();
                    socket.getOutputStream().write(("join_server " + player.getName() + " " + player.getPos().x + " " + player.getPos().y).getBytes());

                    byte[] read = new byte[1024];
                    socket.getInputStream().read(read);

                    String stringRead = new String(read).trim(); // out from server
                    System.out.println("out from server: " + stringRead);
                    String[] args = smashOnArgs(stringRead);

                    online = Integer.parseInt(args[0]);
                    addPlayersInList(online);


                    playerList.add(player);
                    playerNumInList = playerList.size() - 1;

                    int playerNow = 0;
                    for (int i = 0; i < playerList.size(); i += 2) {
                        playerList.get(playerNow++).setPos(new Vector2(Float.parseFloat(args[i]), Float.parseFloat(args[i + 1])));
                    }

                    socket.getOutputStream().write(stringRead.getBytes());

                    playerJoin = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void updatePlayerPosition() {
        System.out.println("updatePlayerPosition");
        if(!playerJoin) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connect();
                    System.out.println(playerNumInList);
                    Player player = playerList.get(playerNumInList);

                    socket.getOutputStream().write(("updatePlayerPosition " + player.getName() + " " + player.getPos().x + " " + player.getPos().y).getBytes());

                    byte[] read = new byte[1024];
                    socket.getInputStream().read(read);

                    String stringRead = new String(read).trim(); // out from server
                    System.out.println("out from server: " + stringRead);
                    String[] args = smashOnArgs(stringRead);

                    System.out.println("online - " + args[0]);
                    System.out.println("players - " + playerList.size());
                    int _online = Integer.parseInt(args[0]);
                    if(online != _online) {
                        System.out.println("----------------------------------------------------------------------" + (_online - online));
                        addPlayersInList(_online - online + 1);
                        online = _online;
                    }

                    int playerNow = 0;
                    for (int i = 1;i < playerList.size() + 2; i += 2) {
                        if(playerNow != playerNumInList)
                            playerList.get(playerNow).setPos(new Vector2(Float.parseFloat(args[i]), Float.parseFloat(args[i + 1])));
                        playerNow++;
                    }

                    socket.getOutputStream().write(stringRead.getBytes());
                } catch (IOException | NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static void addPlayersInList(int countOnline) {
        for(int i = 0;i < countOnline - 1;i++) {
            final Player player = new Player(new Vector2(0 ,0), "name", false);
            playerList.add(player);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    player.setTexture();
                }
            }, 1);
        }
    }


    private static String[] smashOnArgs(String text) {
        char[] chars = text.toCharArray();

        int countArgs = 1;
        for (char _char : chars) {
            if(_char == ' ') countArgs++;
        }
        String[] args = new String[countArgs];
        Arrays.fill(args, "");

        int arg = 0;
        for (int i = 0; i < chars.length; i++) {
            try {
                if (chars[i] != ' ') args[arg] += "" + chars[i];
                else {
                    arg++;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                args[arg] += "" + chars[i];
            }
        }

        return args;
    }

    private static void connect() {
        SocketHints hints = new SocketHints();
        hints.connectTimeout = 15000;
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "localhost", 1025, hints);
        System.out.println("start server init, server null = " + (socket == null));
    }
}
