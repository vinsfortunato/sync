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

package net.touchmania.game.resource.xml;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.touchmania.game.util.Loader;

public class XmlMusicLoader implements Loader<Music>, Cloneable {
    /** The sound file **/
    public final FileHandle file;

    /**
     * Creates a loader from a sound file.
     * @param file the sound file.
     */
    public XmlMusicLoader(FileHandle file) {
        this.file = file;
    }

    /**
     * Copy constructor.
     * @param loader the loader to copy.
     */
    public XmlMusicLoader(XmlMusicLoader loader) {
        this(loader.file);
    }

    public Music load() throws Exception {
        try {
            return Gdx.audio.newMusic(file);
        } catch(GdxRuntimeException e) {
            throw new Exception("Music cannot be loaded correctly!", e);
        }
    }
}
