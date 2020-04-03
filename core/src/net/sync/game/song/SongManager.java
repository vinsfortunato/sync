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
import net.sync.game.Game;
import org.jooq.DSLContext;

import java.io.File;

public class SongManager {
    //Start indexing the given folder (the songs folder)
    public void index(FileHandle dir) {
        //TODO Temp test
        long millis = System.currentTimeMillis();
        for(FileHandle f : dir.list(File::isDirectory)) {
            String pack = f.name();
            try(DSLContext database = Game.instance().getDatabase().getDSL()) {
                database.transaction(configuration -> {
                    for(FileHandle songDir : f.list(File::isDirectory)) {
                        SongIndexer indexer = new SongIndexer(pack, songDir, configuration);
                        try {
                            indexer.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        System.out.println(System.currentTimeMillis() - millis);
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


}
