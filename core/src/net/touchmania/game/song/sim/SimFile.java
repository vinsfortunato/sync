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

package net.touchmania.game.song.sim;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import net.touchmania.game.Game;

import java.io.File;
import java.io.IOException;

/**
 * A file handle that represents a sim file and provides utility methods.
 */
public class SimFile extends FileHandle {
    private SimFormat format;

    public SimFile(FileHandle file) {
        this(file, SimFormat.valueFromExtension(file.extension()));
    }

    private SimFile(FileHandle file, SimFormat format) {
        super(file.file(), file.type());
        this.format = format;
    }

    public SimFormat getFormat() {
        return format;
    }

    /**
     * Compute the sim file hash.
     * @return the sim file hash.
     * @throws IOException if an IO exception occurs while calculating the hash.
     */
    public String computeHash() throws IOException {
        return Files.asByteSource(file()).hash(Hashing.sha256()).toString();
    }

    /**
     * Searches a sim file in the given song directory.
     * If there are multiple sim files picks the one that has the format with higher priority.
     * @param directory the directory to search.
     * @return the resulting sim file or null if there was no sim file in the given directory.
     */
    public static SimFile searchSimFile(FileHandle directory) {
        Preconditions.checkArgument(directory.isDirectory(), String.format("%s is not a directory", directory));
        int resultPriority = -1;
        SimFile simFile = null;

        //Search sim file and pick the one with higher priority
        for(FileHandle file : directory.list(File::isFile)) {
            SimFormat format = SimFormat.valueFromExtension(file.extension());
            if(format != null) { //Supported format
                //TODO remove Game.instance().. from here, maybe use a given interface that converts format to priority
                int priority = Game.instance().getSettings().getSimFormatPriority(format);
                if(priority > resultPriority) { //Prefer higher priority formats
                    simFile = new SimFile(file, format);
                    resultPriority = priority;
                }
            }
        }

        return simFile;
    }
}
