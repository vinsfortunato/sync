package net.touchmania.game.desktop;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.touchmania.game.Backend;
import net.touchmania.game.Game;
import net.touchmania.game.util.ui.DPI;

public class DesktopLauncher implements Backend {
	public static void main (String[] arg) {
		if(arg.length > 0) {
			Game.INPUT_PATH = arg[0];
		}
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
	public void initBackend() {}

	@Override
	public DPI getDeviceDPI() {
		return DPI.LOW;
	}

	public double getDuration(Music music) {
		return 0.0f;
	}

	@Override
	public String getDatabaseUrl() {
		return "jdbc:sqlite:touchmania.sqlite";
	}
}
