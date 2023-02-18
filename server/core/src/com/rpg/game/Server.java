package com.rpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server extends ApplicationAdapter {

	private ServerSocket server;
	private int online;
	private List<Player> players;

	@Override
	public void create () {
		players = new ArrayList<>();

		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocketHints hints = new ServerSocketHints();
				hints.acceptTimeout = 0;
				server = Gdx.net.newServerSocket(Net.Protocol.TCP, "localhost", 1025, hints);

				while (true) {
					SocketHints socketHints = new SocketHints();
					Socket socket = server.accept(socketHints);

					if (socket != null) {
						try {
							try {
								byte[] read = new byte[1024];
								socket.getInputStream().read(read);

								String stringRead = new String(read).trim();
								System.out.println(stringRead);

								String[] args = smashOnArgs(stringRead);

								stringRead = "";

								if (args[0].equalsIgnoreCase("get_online")) {
									stringRead = "" + online;
								}

								if (args[0].equalsIgnoreCase("join_server")) {
									online++;
									String name = args[1];
									Vector2 position = new Vector2(Float.parseFloat(args[2]), Float.parseFloat(args[3]));
									players.add(new Player(name, position));
									// send pos all player
									stringRead = "" + online + " ";
									for (Player player : players) {
										stringRead += "" + player.getPosition().x + " " + player.getPosition().y + " ";
									}
								}

								if (args[0].equalsIgnoreCase("updatePlayerPosition") && args.length <= 4) {
									String name = args[1];
									Vector2 position = new Vector2(Float.parseFloat(args[2]), Float.parseFloat(args[3]));
									// send pos all player
									stringRead = "" + online + " ";
									for (Player player : players) {
										if (player.getName().equals(name)) player.setPosition(position);
										stringRead += "" + player.getPosition().x + " " + player.getPosition().y + " ";
									}
								}


								socket.getOutputStream().write(stringRead.getBytes());
								socket.dispose();
							} catch (IOException | NumberFormatException e) {
								throw new RuntimeException(e);
							}
						} catch (RuntimeException e2) {
							throw new RuntimeException(e2);
						}
					}
				}
			}
		});

		serverThread.start();

	}

	private String[] smashOnArgs(String text) {

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

	@Override
	public void render () {

	}
	
	@Override
	public void dispose () {
		Gdx.app.exit();
		server.dispose();
	}
}
