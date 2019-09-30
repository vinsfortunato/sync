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
import com.badlogic.gdx.utils.Array;
import net.touchmania.game.song.sim.SimFormat;

/**
 * @author flood2d
 */
public class Song {
    /**
     * The directory that contains the song's sim file.
     */
    public FileHandle directory;
    /**
     * The song's sim file format.
     */
    public SimFormat simFormat;
    /**
     * Gets the sha256 hash for this song. The sha256 is the hash of the
     * sim file that contains song data. It's unique and it's used to
     * save a cached version of the song into the game database. If
     * the sim file changes the cached song will be removed from the
     * database and a new version of the song will be inserted into the
     * database. Scores related to the old version of the song will
     * not be related to the new version. Scores related to the old
     * version will not be deleted and can be restored if the old version
     * is reinserted into the database.
     */
    public String hash;
    public String group;
    public String title;
    public String subtitle;
    public String artist;
    public String genre;
    public String credit;
    public String bannerPath;
    public String backgroundPath;
    public String lyricsPath;
    public String cdTitle;
    public String musicPath;
    public double offset = 0.0f;
    public TimingData timingData = new TimingData();
    public float sampleStart = -1.0f;
    public float sampleLength = -1.0f;
    public DisplayBPM displayBPM;
    public boolean selectable = true;
    public Array<Chart> charts;
}