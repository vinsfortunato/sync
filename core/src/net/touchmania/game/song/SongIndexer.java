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
import net.touchmania.game.Game;
import net.touchmania.game.song.sim.SimFile;
import net.touchmania.game.util.concurrent.Task;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;

import static com.google.common.base.Preconditions.checkArgument;
import static net.touchmania.game.database.schema.tables.Songs.SONGS;

/**
 * A task that searches for a song in a given directory and adds, deletes or updates it inside the index. These
 * operation are performed under the following conditions:
 * <ul>
 *     <li>Add song: add the song to the index if a sim file exists in the given directory and it wasn't
 *     already present in the index</li>
 *     <li>Remove song: remove the song from the index (if present) if there is no sim file in the given
 *     directory </li>
 *     <li>Update song: update the song in the index if the sim file has changed. If updating the song is not
 *     feasible remove the song from the index and add the new version of the song </li>
 * </ul>
 * The result of the task is a boolean that is true when a song has been found inside the given directory.
 */
public class SongIndexer extends Task<Boolean> {
    private String pack;
    private FileHandle directory;

    public SongIndexer(String pack, FileHandle directory) {
        checkArgument(directory.isDirectory(), "directory parameter must be a directory!");
        this.pack = pack;
        this.directory = directory;
    }

    @Override
    protected Boolean call() throws Exception {
        //Check if there is a song inside the given folder and delete, update or add it.
        //Will return true if a song exists in the given directory, false otherwise.
        try (DSLContext database = Game.instance().getDatabase().getDSL()) {
            SimFile simFile = SimFile.searchSimFile(directory, format -> Game.instance().getSettings().getSimFormatPriority(format));

            if(simFile != null) {
                //The song directory contains a sim file
                String cachedHash = getCachedHash(database);
                String hash = simFile.computeHash();

                if(cachedHash == null) {
                    //Song is not in the index. Add the song to the index
                    addSong(database, hash);
                } else if(!cachedHash.equals(hash)) {
                    //Song is already in the index but the sim file has changed. Update if possible
                    updateSong(database, cachedHash);
                }
                return true;
            } else {
                //The song directory doesn't contain a sim file. If there is a song in the index at this directory
                //it should be removed from the index because the sim file isn't available anymore.
                removeSong(database);
                return false;
            }
        }
    }

    /**
     * Adds the song to the index.
     * @param database the database context.
     * @param hash the sim file hash.
     */
    private void addSong(DSLContext database, String hash) {
        database.insertInto(SONGS, SONGS.PACK, SONGS.DIRECTORY, SONGS.HASH)
                .values(pack, directory.name(), hash)
                .execute();

        Gdx.app.log("Song Indexer", String.format("Add song %s/%s with hash %s", pack, directory, hash));
    }

    /**
     * Removes the song from the index if present.
     * @param database the database context.
     */
    private void removeSong(DSLContext database) {
        database.deleteFrom(SONGS)
                .where(SONGS.PACK.eq(pack).and(SONGS.DIRECTORY.eq(directory.name())))
                .execute();

        Gdx.app.log("Song Indexer", String.format("Remove song %s/%s", pack, directory));
    }

    /**
     * Attempts to update the song in the index. If updating the song is not feasible
     * deletes it and adds a new song.
     * @param database the database context.
     * @param hash the sim file hash.
     */
    private void updateSong(DSLContext database ,String hash) {
        //TODO

        Gdx.app.log("Song Indexer", "Song updated");
    }

    /**
     * Gets the hash of the sim file that generated the cached version of the song into the index.
     * @param database the database context.
     * @return the hash of the cached song's sim file, or null if the song is not cached
     */
    private String getCachedHash(DSLContext database) {
        Result<Record1<String>> result = database
                .select(SONGS.HASH)
                .from(SONGS)
                .where(SONGS.PACK.eq(pack).and(SONGS.DIRECTORY.eq(directory.name())))
                .fetch();

        return result.isNotEmpty() ? result.get(0).get(SONGS.HASH) : null;
    }
}
