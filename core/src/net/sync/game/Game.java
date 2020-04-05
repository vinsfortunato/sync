/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import net.sync.game.database.DatabaseManager;
import net.sync.game.player.PlayerManager;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.ThemeManager;
import net.sync.game.song.SongManager;
import net.sync.game.ui.ScreenManager;
import net.sync.game.ui.screen.play.GameScreen;
import net.sync.game.util.Disposer;
import net.sync.game.util.concurrent.ExecutorManager;

public class Game implements ApplicationListener {
	public static String INPUT_PATH;

	public String tempFile;

	private static Game instance;
	private Backend backend;
	private GameSettings settings;
	private AssetManager assets;
	private ScreenManager screens;
	private ThemeManager themes;
	private SongManager songs;
	private PlayerManager players;
	private ExecutorManager executors;
	private DatabaseManager database;
	private Disposer disposer;
	private FPSLogger fps;

	public Game(Backend backend) {
		instance = this;
		this.backend = backend;
	}

	@Override
	public void create () {
		this.backend.initBackend();

		this.fps = new FPSLogger();
		this.disposer = new Disposer();
		this.settings = new GameSettings();
		this.executors = new ExecutorManager();
		//if(Gdx.app.getType() != Application.ApplicationType.Android) { //TODO
			this.database = new DatabaseManager();
		//}
		this.assets = new AssetManager();
		this.themes = new ThemeManager();
		this.screens = new ScreenManager();
		this.songs = new SongManager();
		this.players = new PlayerManager();

		//TODO test
		this.songs.index(Gdx.files.absolute("E:/Games/StepMania 5/Songs"));

		//Show test screen TODO
		if(Gdx.app.getType() != Application.ApplicationType.Android)
			this.screens.show(GameScreen.instance());
	}

	@Override
	public void resize(int width, int height) {
		screens.resize(width, height);
	}

	@Override
	public void render () {
		//fps.log();

		//Update assets
		assets.update();

		//Perform rendering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screens.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume () {
		if(Gdx.app.getType() == Application.ApplicationType.Android) //TODO
			this.screens.show(GameScreen.instance());
	}

	@Override
	public void dispose () {
		disposer.dispose();
	}

	public AssetManager getAssets() {
		return assets;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public Backend getBackend() {
		return backend;
	}

	public ScreenManager getScreens() {
		return screens;
	}

	public ThemeManager getThemes() {
		return themes;
	}

	public SongManager getSongs() {
		return songs;
	}

	public PlayerManager getPlayers() {
		return players;
	}

	public ResourceProvider getResources() {
		//The theme is also the global resource provider
		return getThemes().getActiveTheme();
	}

	public ExecutorManager getExecutors() {
		return executors;
	}

	public DatabaseManager getDatabase() {
		return database;
	}

	public Disposer getDisposer() {
		return disposer;
	}

	public static Game instance() {
		return instance;
	}
}
