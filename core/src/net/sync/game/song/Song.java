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