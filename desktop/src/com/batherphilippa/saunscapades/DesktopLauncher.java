package com.batherphilippa.saunscapades;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * DesktopLauncher - el punto de entrada del juego de escritorio.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("shaun-scapades");
		new Lwjgl3Application(new ShaunScapades(), config);
	}
}
