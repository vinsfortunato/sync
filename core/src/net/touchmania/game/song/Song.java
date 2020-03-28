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
import net.touchmania.game.song.sim.SimFile;

import java.util.List;

/**
 * @author Vincenzo Fortunato
 */
public class Song {
    /**
     * The directory that contains the song's sim file.
     */
    public FileHandle directory;

    /**
     * The song's sim file.
     */
    public SimFile simFile;

    public String pack;    //TODO
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
    public TimingData timingData = new TimingData();
    public float sampleStart = -1.0f;
    public float sampleLength = -1.0f;
    public DisplayBPM displayBPM;
    public boolean selectable = true;
    public List<Chart> charts;
}