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
import com.google.common.io.Files;
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
        simFile = SongManager.searchSimFile(songDir);

        if(simFile == null) {
            //Sim file not found
            throw new FileNotFoundException("There's no supported sim file in the given directory!");
        }

        if(simFile.length() > MAX_FILE_LENGTH) {
            //Sim file exceeds max file length
            throw new IOException("Sim file size exceeds the maximum allowed file size.");
        }

        simFormat = SimFormat.valueFromExtension(simFile.extension());
        SimParser parser = simFormat.newParser();

        //Initialize the parser
        parser.init(Files.asCharSource(simFile.file(), Charsets.UTF_8).read());

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
        song.sampleStart = parser.parseSampleStart();
        song.sampleLength = parser.parseSampleLength();
        song.displayBPM = parser.parseDisplayBPM();
        song.selectable = parser.parseSelectable();
        song.timingData = parser.parseTimingData();
        song.charts = parser.parseCharts();
        for(Chart chart : song.charts) {
            chart.song = song;
        }
        //TODO song.hash = calculateSongHash(rawContent); //Calculate at the end if the parse is successful
        return song;
    }
}
