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

package net.sync.game.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidMusic;
import net.sync.game.Backend;
import net.sync.game.Game;
import net.sync.game.util.ui.DPI;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import javax.sql.DataSource;
import java.io.File;

public class AndroidLauncher extends AndroidApplication implements Backend {
	private net.sync.game.Game game;
	private SQLiteDataSource dataSource;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x03);
			}
		}

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		game = new Game(this);

		initialize(game, config);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
			i.addCategory(Intent.CATEGORY_DEFAULT);
			startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case 9999:
				if(resultCode == RESULT_OK) {
					Uri treeUri = data.getData();
					File file = new File(treeUri.getPath());

					System.out.println(file.getPath());
					game.tempFile = file.getPath().split(":")[1];
				}
				break;
		}
	}

	@Override
	public void initBackend() {}

	@Override
	public DPI getDeviceDPI() {
		return DPI.closest(getResources().getDisplayMetrics().densityDpi);
	}

	@Override
	public double getDuration(Music music) {
		return ((AndroidMusic) music).getDuration();
	}

	@Override
	public DataSource getDatabaseDataSource() {
		SQLiteJDBCLoader loader;

		if(dataSource == null) {
			dataSource = new SQLiteDataSource();
			dataSource.setUrl("jdbc:sqlite:" + "/data/data/" + getPackageName() + "/sync.db");
		}
		return dataSource;
	}
}
