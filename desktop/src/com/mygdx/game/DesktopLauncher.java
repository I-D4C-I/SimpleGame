package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static final int WINDOW_WIDTH = 1280;
	public static final int WINDOW_HEIGHT = 720;

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(WINDOW_WIDTH,WINDOW_HEIGHT);
		config.setForegroundFPS(30);
		config.setTitle("SimpleRpg");
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
