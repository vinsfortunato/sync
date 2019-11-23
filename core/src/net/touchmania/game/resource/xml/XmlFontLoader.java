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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import net.touchmania.game.util.Loader;

/**
 * Generates a font from its file by using FreeType font engine. Check
 * <a href="https://freetype.org/">FreeType</a> documentation for
 * more info (supported font formats, features etc..)
 */
public class XmlFontLoader implements Loader<BitmapFont>, Cloneable {
    /** The font file **/
    public final FileHandle file;
    /** Holds font generation parameters **/
    public final FreeTypeFontParameter parameter = new FreeTypeFontParameter();

    /**
     * Creates a generator from a font file.
     * @param file the font file.
     */
    public XmlFontLoader(FileHandle file) {
        this.file = file;
    }

    public XmlFontLoader(XmlFontLoader loader) {
        this(loader.file);
        loader.parameter.size = parameter.size;
        loader.parameter.mono = parameter.mono;
        loader.parameter.hinting = parameter.hinting;
        loader.parameter.color = new Color(parameter.color);
        loader.parameter.gamma = parameter.gamma;
        loader.parameter.renderCount = parameter.renderCount;
        loader.parameter.borderWidth = parameter.borderWidth;
        loader.parameter.borderColor = new Color(parameter.borderColor);
        loader.parameter.borderStraight = parameter.borderStraight;
        loader.parameter.borderGamma = parameter.borderGamma;
        loader.parameter.shadowOffsetX = parameter.shadowOffsetX;
        loader.parameter.shadowOffsetY = parameter.shadowOffsetY;
        loader.parameter.shadowColor = new Color(parameter.shadowColor);
        loader.parameter.spaceX = parameter.spaceX;
        loader.parameter.spaceY = parameter.spaceY;
        loader.parameter.characters = parameter.characters;
        loader.parameter.kerning = parameter.kerning;
        loader.parameter.packer = parameter.packer;
        loader.parameter.flip = parameter.flip;
        loader.parameter.genMipMaps = parameter.genMipMaps;
        loader.parameter.minFilter = parameter.minFilter;
        loader.parameter.magFilter = parameter.magFilter;
        loader.parameter.incremental = parameter.incremental;
    }

    public BitmapFont load() throws Exception {
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(file);
            return generator.generateFont(parameter);
        } finally {
            if(generator != null) {
                try {
                    generator.dispose();
                } catch(Throwable e) {
                    //Ignore
                }
            }
        }
    }
}
