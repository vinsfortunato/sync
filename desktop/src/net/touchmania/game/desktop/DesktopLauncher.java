package net.touchmania.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.touchmania.game.Backend;
import net.touchmania.game.Game;
import net.touchmania.game.database.DatabaseHelper;
import net.touchmania.game.util.ui.DPI;

public class DesktopLauncher implements Backend {
	public static void main (String[] arg) {
		new DesktopLauncher().startGame();
	}

	private void startGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		config.foregroundFPS = 144;
		new LwjglApplication(new Game(this), config);
	}

	@Override
	public DatabaseHelper getDatabaseHelper() {
		return null;
	}

	@Override
	public DPI getDeviceDPI() {
		return DPI.LOW;
	}
}
