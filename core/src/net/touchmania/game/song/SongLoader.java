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

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Charsets;
import net.touchmania.game.Game;
import net.touchmania.game.song.sim.SimFile;
import net.touchmania.game.song.sim.SimParser;
import net.touchmania.game.util.concurrent.Task;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SongLoader extends Task<Song> {
    /** The sim file max allowed file length in bytes **/
    private static final long MAX_FILE_LENGTH = 10 * 1024 * 1024; //10 megabytes
    private SimFile simFile;

    /**
     * Creates a song loader from a given directory.
     * @param directory the song directory.
     */
    public SongLoader(FileHandle directory) {
        this(SimFile.searchSimFile(directory, format -> Game.instance().getSettings().getSimFormatPriority(format)));
    }

    /**
     * Creates a song loader from a given sim file.
     * @param simFile the song sim file.
     */
    public SongLoader(SimFile simFile) {
        this.simFile = simFile;
    }

    @Override
    protected Song call() throws Exception {
        if(simFile == null) {
            //Sim file not found
            throw new FileNotFoundException("There's no supported sim file in the given directory!");
        }

        if(simFile.getFile().length() > MAX_FILE_LENGTH) {
            //Sim file exceeds max file length
            throw new IOException("Sim file size exceeds the maximum allowed file size.");
        }

        SimParser parser = simFile.getFormat().newParser();

        //Initialize the parser
        parser.init(simFile.getFile().readString(Charsets.UTF_8.name()));

        //Parse song
        Song song = new Song();
        song.directory = simFile.getFile().parent();
        song.simFile = simFile;
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
        return song;
    }
}
