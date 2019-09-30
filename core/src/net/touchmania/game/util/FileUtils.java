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

package net.touchmania.game.util;

import com.badlogic.gdx.files.FileHandle;

import java.io.FileNotFoundException;

public final class FileUtils {
    /**
     * Checks if the given file exists and throws and exception if
     * it doesn't exist.
     *
     * @param file the file to check.
     * @throws FileNotFoundException if the file doesn't exist.
     */
    public static void checkFilePresence(FileHandle file) throws FileNotFoundException {
        if(!file.exists()) {
            throw new FileNotFoundException(String.format("Required file '%s' not found!", file.path()));
        }
    }
}
