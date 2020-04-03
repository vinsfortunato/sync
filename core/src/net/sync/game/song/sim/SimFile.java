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

package net.sync.game.song.sim;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.io.File;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A file handle that represents a sim file and provides utility methods.
 */
public class SimFile {
    private FileHandle file;
    private SimFormat format;

    public SimFile(FileHandle file) {
        checkArgument(!file.isDirectory(), "Sim file cannot be a directory");
        this.file = file;
        this.format = SimFormat.valueFromExtension(file.extension());
    }

    public SimFormat getFormat() {
        return format;
    }

    public FileHandle getFile() {
        return file;
    }

    /**
     * Compute the sim file hash.
     * @return the sim file hash.
     */
    public String computeHash() {
        Hasher hasher = Hashing.sha256().newHasher();
        hasher.putBytes(file.readBytes());
        return hasher.hash().toString();
    }

    /**
     * Searches a sim file in the given song directory.
     * If there are multiple sim files picks the one that has the format with higher priority.
     * @param directory the directory to search.
     * @return the resulting sim file or null if there was no sim file in the given directory.
     */
    public static SimFile searchSimFile(FileHandle directory, Function<SimFormat, Integer> priorityProvider) {
        Preconditions.checkArgument(directory.isDirectory(), String.format("%s is not a directory", directory));
        int resultPriority = -1;
        SimFile simFile = null;

        //Search sim file and pick the one with higher priority
        for (FileHandle file : directory.list(File::isFile)) {
            SimFormat format = SimFormat.valueFromExtension(file.extension());
            if (format != null) { //Supported format
                int priority = priorityProvider.apply(format);
                if (priority > resultPriority) { //Prefer higher priority formats
                    simFile = new SimFile(file);
                    resultPriority = priority;
                }
            }
        }

        return simFile;
    }
}