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
import com.google.common.base.Preconditions;
import net.touchmania.game.Game;
import net.touchmania.game.song.sim.SimFormat;

import java.io.File;

public class SongManager {
    //Start indexing the given folder (the songs folder)
    public void index(FileHandle dir) {
        try {
            new SongIndexer(dir).call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Find a set of song matching the given params
    public void find(SongSearchParams params) {

    }

    //From preview to view state
    public void load(Song song) {

    }

    //From view to preview state
    public void unload(Song song) {

    }

    /**
     * Searches a sim file in the given song directory.
     * If there are multiple sim files picks the one that has the format with higher priority.
     * @param dir the directory to search.
     * @return the resulting sim file or null if there was no sim file in the given directory.
     */
    public static FileHandle searchSimFile(FileHandle dir) {
        Preconditions.checkArgument(dir.isDirectory(), String.format("%s is not a directory", dir));
        int resultPriority = -1;
        FileHandle simFile = null;

        //Search sim file and pick the one with higher priority
        for(FileHandle file : dir.list(File::isFile)) {
            SimFormat format = SimFormat.valueFromExtension(file.extension());
            if(format != null) { //Supported format
                int priority = Game.instance().getSettings().getSimFormatPriority(format);
                if(priority > resultPriority) { //Prefer higher priority formats
                    simFile = file;
                    resultPriority = priority;
                }
            }
        }
        return simFile;
    }
}
