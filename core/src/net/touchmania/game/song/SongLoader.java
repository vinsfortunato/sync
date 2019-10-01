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

package net.touchmania.game.song;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import net.touchmania.game.Game;
import net.touchmania.game.song.sim.SimFormat;
import net.touchmania.game.song.sim.SimParseException;
import net.touchmania.game.song.sim.SimParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * A task that load a song from a sim file by searching for
 * it in a given directory.
 */
public class SongLoader implements Callable<Song> {
    /** The sim file max allowed file length in bytes **/
    private static final long MAX_FILE_LENGTH = 10 * 1024 * 1024; //10 megabytes
    private final FileHandle songDir;
    private FileHandle simFile;
    private SimFormat simFormat;

    public SongLoader(FileHandle songDir) {
        Preconditions.checkArgument(songDir.isDirectory(), "The given file handle is not a directory!");
        this.songDir = songDir;
    }

    @Override
    public Song call() throws IOException, SimParseException {
        //Find the sim file to parse
        searchSimFile();

        if(simFile.length() > MAX_FILE_LENGTH) {
            throw new IOException("Sim file size exceeds the maximum allowed file size.");
        }

        SimParser parser = simFormat.newParser();
        Preconditions.checkNotNull(parser);

        //Read the file content and store it into a string
        String rawContent = Files.toString(simFile.file(), Charsets.UTF_8);

        //Initialize the parser
        parser.init(rawContent);

        //Parse song
        Song song = new Song();
        song.directory = songDir;
        song.simFile = simFile;
        song.simFormat = simFormat;
        song.title = parser.parseTitle();
        song.subtitle = parser.parseSubtitle();
        song.artist = parser.parseArtist();
        song.genre = parser.parseGenre();
        song.credit = parser.parseCredit();
        song.bannerPath = parser.parseBannerPath();
        song.backgroundPath = parser.parseBackgroundPath();
        song.lyricsPath = parser.parseLyricsPath();
        song.cdTitle = parser.parseCdTitle();
        song.musicPath = parser.parseMusicPath();
        song.offset = parser.parseOffset();
        song.sampleStart = parser.parseSampleStart();
        song.sampleLength = parser.parseSampleLength();
        song.displayBPM = parser.parseDisplayBPM();
        song.selectable = parser.parseSelectable();
        song.timingData = parser.parseTimingData();
        song.charts = parser.parseCharts();
        song.hash = calculateSongHash(rawContent); //Calculate at the end if the parse is successful
        return song;
    }

    /**
     * Calculate the song sha256 hash by encoding the sim file content.
     *
     * @param rawContent the content of the song's sim file.
     * @return the song hash.
     */
    private String calculateSongHash(String rawContent) {
        Hasher hasher = Hashing.sha256().newHasher();
        hasher.putString(rawContent, Charsets.UTF_8);
        return hasher.hash().toString();
    }

    /**
     * Search the sim file to parse in the given song directory.
     * If there are multiple sim files of different formats is picked
     * the one that has the format with higher priority.
     *
     * @throws FileNotFoundException if there's no supported sim file
     * in the given song directory.
     */
    private void searchSimFile() throws FileNotFoundException {
        int resultPriority = -1;

        //Search sim file and pick the one with higher priority
        for(FileHandle child : songDir.list()) {
            if(child.isDirectory()) {
                continue; //Skip directory handles
            }

            SimFormat format = SimFormat.valueFromExtension(child.extension());
            if(format != null) { //Supported format
                int priority = Game.instance().getSettings().getSimFormatPriority(format);
                if(priority > resultPriority) { //Prefer higher priority formats
                    simFile = child;
                    simFormat = format;
                    resultPriority = priority;
                }
            }
        }

        //Throw an exception if the given directory doesn't contain a sim file
        if(simFile == null) {
            throw new FileNotFoundException("There's no supported sim file in the given directory!");
        }
    }
}
