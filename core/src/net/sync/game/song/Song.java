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

package net.sync.game.song;

import com.badlogic.gdx.files.FileHandle;
import net.sync.game.song.sim.SimFile;

import java.util.List;

/**
 * Represents a song parsed from a SIM file.
 */
public class Song {
    /** The directory that contains the song's sim file */
    public FileHandle directory;
    /** The song's sim file */
    public SimFile simFile;
    /** The song's pack */
    public String pack;
    /** The song's title */
    public String title;
    /** The song's subtitle */
    public String subtitle;
    /** The song's artist */
    public String artist;
    /** The song's genre */
    public String genre;
    /** The song's credit and acknowledgement */
    public String credit;
    /** The path to the banner relative to the sim file */
    public String bannerPath;
    /** The path to the background relative to the sim file */
    public String backgroundPath;
    /** The path to the lyrics relative to the sim file */
    public String lyricsPath;
    /** The title of the album the song belongs to */
    public String album;
    /** The path to the music track relative to the sim file */
    public String musicPath;
    /** The music sample start offset, negative if not set */
    public float sampleStart = -1.0f;
    /** The music sample length, negative if not set */
    public float sampleLength = -1.0f;
    /** True if the song is selectable during song selection, false otherwise */
    public boolean selectable = true;
    /** The list of charts */
    public List<Chart> charts;
}