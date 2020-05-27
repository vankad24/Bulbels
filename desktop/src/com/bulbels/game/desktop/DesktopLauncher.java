package com.bulbels.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bulbels.game.Bulbels;
import com.bulbels.game.utils.AndroidHelper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="Game";
		config.width=720;
		config.height=800;
		config.resizable= false;
		config.addIcon("icon_128.png", Files.FileType.Internal);
		config.addIcon("icon_32.png", Files.FileType.Internal);
		config.addIcon("icon_16.png", Files.FileType.Internal);
//		config.foregroundFPS = 60;

		new LwjglApplication(new Bulbels(null), config);
	}
}
