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

package net.touchmania.game.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import net.touchmania.game.Game;
import net.touchmania.game.database.Cursor;
import net.touchmania.game.database.Database;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class SongIndexer implements Callable<Integer> {
    /** The root directory where the all packs are located */
    private FileHandle rootDir;
    /** A map containing indexes. Key is the path of the song. The value is the hash of the sim file. */
    private Map<String, String> indexes;

    public SongIndexer(FileHandle rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public Integer call() throws Exception {
        //Load the indexes map
        indexes = queryIndexes();

        if(!rootDir.isDirectory()) {
            throw new IOException(String.format("%s is not a directory", rootDir.path()));
        }

        for(FileHandle file : rootDir.list(File::isDirectory)) {
            indexPack(file);
        }

        //Remove all remaining indexed songs
        for(String key : indexes.keySet()) {
            String[] parts = key.split("/");
            String pack = parts[0];
            String directory = parts[1];
            removeSong(pack, directory);
        }

        return 0; //TODO temp
    }

    private void indexPack(FileHandle packDir) throws Exception {
        String pack = packDir.name(); //Packs are identified by their name

        for(FileHandle file : packDir.list(File::isDirectory)) {
            try {
                indexSong(pack, file);
            } catch(Exception e) {
                //TODO skip?
                e.printStackTrace();
            }
        }
    }

    private void indexSong(String pack, FileHandle songDir) throws Exception {
        FileHandle simFile = SongManager.searchSimFile(songDir);

        String directory = songDir.name();
        String key = pack + "/" + directory;
        String oldHash = indexes.remove(key);

        if(simFile != null) {
            String hash = computeSimFileHash(simFile);

            if(oldHash == null) {
                //Song is not indexed. Add song
                addSong(pack, directory, hash);
            } else if(!oldHash.equals(hash)) {
                //Song is indexed but the sim file has changed. Update song
                updateSong();
            }
        } else if(oldHash != null) {
            //No sim file found but the song is indexed. Remove song
            removeSong(pack, directory);
        }
    }

    private String computeSimFileHash(FileHandle simFile) throws Exception {
        return Files.asByteSource(simFile.file()).hash(Hashing.sha256()).toString();
    }

    private void updateSong() {
        Gdx.app.log("Song Indexer", "Song updated");
    }

    private void removeSong(String pack, String directory) {
        Database db = Game.instance().getBackend().getDatabaseHelper().getWritableDatabase();
        db.executeSQL(String.format(
                "DELETE FROM songs WHERE pack = '%s' AND directory = '%s';",
                sanitize(pack), sanitize(directory)));
        db.close();

        Gdx.app.log("Song Indexer", String.format("Remove song %s/%s", pack, directory));
    }

    private void addSong(String pack, String directory, String hash) {
        Database db = Game.instance().getBackend().getDatabaseHelper().getWritableDatabase();
        db.executeSQL(String.format(
                "INSERT INTO songs (pack, directory, hash) VALUES ('%s', '%s', '%s');",
                sanitize(pack), sanitize(directory), sanitize(hash)));
        db.close();

        Gdx.app.log("Song Indexer", String.format("Add song %s/%s with hash %s", pack, directory, hash));
    }

    private Map<String, String> queryIndexes() {
        Database db = Game.instance().getBackend().getDatabaseHelper().getReadableDatabase();
        Map<String, String> indexes = new HashMap<>();
        Cursor cursor = db.query("SELECT pack, directory, hash FROM songs");

        while(cursor.moveToNext()) {
            String pack = cursor.getString(0);
            String directory = cursor.getString(1);
            String hash = cursor.getString(2);
            indexes.put(pack + "/" + directory, hash);

            Gdx.app.log("Song Indexer", String.format("Index %s/%s with hash %s", pack, directory, hash));
        }
        db.close();
        return indexes;
    }

    private String sanitize(String value) {
        return value.contains("'") ? value.replaceAll("'", "''") : value;
    }
}
