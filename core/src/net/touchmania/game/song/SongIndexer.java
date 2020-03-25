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
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static net.touchmania.game.database.schema.tables.Songs.SONGS;

public class SongIndexer implements Callable<Integer> {
    /** The root directory where the all packs are located */
    private FileHandle rootDir;
    /** A map containing indexes. Key is the path of the song. The value is the hash of the sim file. */
    private Map<String, String> indexes;

    /** A DSL database context */
    private DSLContext context;

    public SongIndexer(FileHandle rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public Integer call() throws Exception {
        try {
            //Open the database
            context = Game.instance().getDatabase().openDatabase();

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
        } finally {
            context.close();
        }
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
        context.deleteFrom(SONGS)
                .where(SONGS.PACK.eq(pack).and(SONGS.DIRECTORY.eq(directory)))
                .execute();

        Gdx.app.log("Song Indexer", String.format("Remove song %s/%s", pack, directory));
    }

    private void addSong(String pack, String directory, String hash) {
        context.insertInto(SONGS, SONGS.PACK, SONGS.DIRECTORY, SONGS.HASH)
                .values(pack, directory, hash)
                .execute();

        Gdx.app.log("Song Indexer", String.format("Add song %s/%s with hash %s", pack, directory, hash));
    }

    private Map<String, String> queryIndexes() {
        Map<String, String> indexes = new HashMap<>();

        Result<Record3<String, String, String>> result = context
                .select(SONGS.PACK, SONGS.DIRECTORY, SONGS.HASH)
                .from(SONGS)
                .fetch();

        for(Record3<String, String, String> record : result) {
            String pack = record.get(SONGS.PACK);
            String directory = record.get(SONGS.DIRECTORY);
            String hash = record.get(SONGS.HASH);
            indexes.put(pack + "/" + directory, hash);
            Gdx.app.log("Song Indexer", String.format("Index %s/%s with hash %s", pack, directory, hash));
        }

        return indexes;
    }

}
