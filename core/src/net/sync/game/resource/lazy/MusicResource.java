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

package net.sync.game.resource.lazy;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

//TODO
public class MusicResource implements Resource<Music> {

    public MusicResource(FileHandle handle) {

    }

    /**
     * Copy constructor
     * @param resource
     */
    public MusicResource(MusicResource resource) {

    }

    @Override
    public Music get() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void load() {

    }
}
