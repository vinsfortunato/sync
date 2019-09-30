package net.touchmania.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.touchmania.game.Backend;
import net.touchmania.game.Game;
import net.touchmania.game.database.DatabaseHelper;

public class DesktopLauncher implements Backend{
	public static void main (String[] arg) {
		new DesktopLauncher().startGame();
	}

	private void startGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(this), config);
	}

	@Override
	public DatabaseHelper getDatabaseHelper() {
		return null;
	}
}
