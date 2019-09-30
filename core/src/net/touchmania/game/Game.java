/*
 * Copyright 2018 Vincenzo Fortunato
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

package net.touchmania.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import net.touchmania.game.song.SongManager;
import net.touchmania.game.util.Disposer;
import net.touchmania.game.util.concurrent.ExecutorManager;
import net.touchmania.game.util.ui.ScreenManager;
import net.touchmania.game.util.ui.test.TestScreen;

public class Game implements ApplicationListener {
	private static Game instance;
	private Backend backend;
	private GameSettings settings;
	private AssetManager assets;
	private ScreenManager screens;
	private SongManager songs;
	private ExecutorManager executors;
	private Disposer disposer;
	private Music music;
	private FPSLogger fps;

	public Game(Backend backend) {
		instance = this;
		this.backend = backend;
	}

	@Override
	public void create () {
		this.fps = new FPSLogger();
		this.disposer = new Disposer();
		this.settings = new GameSettings();
		this.executors = new ExecutorManager();
		this.assets = new AssetManager();
		this.screens = new ScreenManager();
		this.songs = new SongManager();

		//Show test screen
		this.screens.show(TestScreen.instance());
	}

	@Override
	public void resize(int width, int height) {
		screens.resize(width, height);
	}

	@Override
	public void render () {
		assets.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screens.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume () {

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

	public SongManager getSongs() {
		return songs;
	}

	public ExecutorManager getExecutors() {
		return executors;
	}

	public Disposer getDisposer() {
		return disposer;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	public Music getMusic() {
		return music;
	}

	public static Game instance() {
		return instance;
	}
}
