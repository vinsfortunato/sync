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

package net.sync.game.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.sync.game.song.sim.SimFile;
import net.sync.game.util.concurrent.Task;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static net.sync.game.Game.settings;
import static net.sync.game.database.schema.tables.Charts.CHARTS;
import static net.sync.game.database.schema.tables.Songs.SONGS;

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
    private Configuration config;

    public SongIndexer(String pack, FileHandle directory, Configuration config) {
        checkArgument(directory.isDirectory(), "directory parameter must be a directory!");
        this.pack = pack;
        this.directory = directory;
        this.config = config;
    }

    @Override
    protected Boolean call() throws Exception {
        //Check if there is a song inside the given folder and delete, update or add it.
        //Will return true if a song exists in the given directory, false otherwise.
        DSLContext database = DSL.using(config);
        SimFile simFile = SimFile.searchSimFile(directory, format -> settings().getSimFormatPriority(format));

        if(simFile != null) {
            //The song directory contains a sim file
            String cachedHash = getCachedHash(database);
            String hash = simFile.computeHash();

            if(cachedHash == null) {
                //Song is not in the index. Add the song to the index
                addSong(database, simFile, hash);
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

    /**
     * Adds the song to the index.
     * @param database the database context.
     * @param hash the sim file hash.
     */
    private void addSong(DSLContext database, SimFile simFile, String hash) throws Exception {
        //Load the song
        Song song = new SongLoader(pack, simFile).call();

        //Insert the song into the database and get the song id
        int songId = database.insertInto(SONGS)
                .set(SONGS.PACK, song.pack)
                .set(SONGS.DIRECTORY, song.directory.name())
                .set(SONGS.HASH, hash)
                .set(SONGS.SIM_PATH, simFile.getFile().name())
                .set(SONGS.FORMAT, simFile.getFormat().name())
                .set(SONGS.TITLE, song.title)
                .set(SONGS.SUBTITLE, song.subtitle)
                .set(SONGS.ARTIST, song.artist)
                .set(SONGS.GENRE, song.genre)
                .set(SONGS.BANNER_PATH, song.bannerPath)
                .set(SONGS.BACKGROUND_PATH, song.backgroundPath)
                .set(SONGS.ALBUM, song.album)
                .set(SONGS.MUSIC_PATH, song.musicPath)
                .set(SONGS.SAMPLE_START, song.sampleStart)
                .set(SONGS.SAMPLE_LENGTH, song.sampleLength)
                .returning(SONGS.ID)
                .fetchOne()
                .getValue(SONGS.ID);

        //Insert each chart into the database
        for(Chart chart : song.charts)
            database.insertInto(CHARTS)
                    .set(CHARTS.ID, UUID.randomUUID().toString()) //TODO compute unique
                    .set(CHARTS.SONG_ID, songId)
                    .set(CHARTS.HASH, chart.hash)
                    .set(CHARTS.DIFFICULTY_CLASS, chart.difficultyClass.name())
                    .set(CHARTS.DIFFICULTY_METER, chart.difficultyMeter)
                    //TODO.set(CHARTS.DISPLAY_BPM, chart.displayBPM)
                    .set(CHARTS.NAME, chart.name)
                    .set(CHARTS.DESCRIPTION, chart.description)
                    .set(CHARTS.CREDIT, chart.credit)
                    .execute();

        Gdx.app.log("Song Indexer", String.format("Add song %s/%s with hash %s", pack, directory.name(), hash));
    }

    /**
     * Removes the song from the index if present.
     * @param database the database context.
     */
    private void removeSong(DSLContext database) {
        database.deleteFrom(SONGS)
                .where(SONGS.PACK.eq(pack).and(SONGS.DIRECTORY.eq(directory.name())))
                .execute();

        Gdx.app.log("Song Indexer", String.format("Remove song %s/%s", pack, directory.name()));
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
        return database
                .select(SONGS.HASH)
                .from(SONGS)
                .where(SONGS.PACK.eq(pack).and(SONGS.DIRECTORY.eq(directory.name())))
                .fetchOne(SONGS.HASH);
    }
}
