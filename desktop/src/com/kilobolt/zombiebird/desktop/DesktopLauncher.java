package com.kilobolt.ZombieBird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kilobolt.ZombieBird.ZBGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "ZombieBird";
		config.width = 272;
		config.height = 408;
		
		
		new LwjglApplication(new ZBGame(), config);
	}
}
