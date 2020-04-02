/*
 * Copyright 2020 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sync.game.desktop;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.sync.game.Backend;
import net.sync.game.Game;
import net.sync.game.util.ui.DPI;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

public class DesktopLauncher implements Backend {
	private SQLiteDataSource dataSource;

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
	public DataSource getDatabaseDataSource() {
		if(dataSource == null) {
			dataSource = new SQLiteDataSource();
			dataSource.setUrl("jdbc:sqlite:sync.db");
		}
		return dataSource;
	}
}
