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

package net.touchmania.game.ui.xml;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import net.touchmania.game.ui.FontGenerator;

/**
 * Generates a font from its file by using FreeType font engine. Check
 * <a href="https://freetype.org/">FreeType</a> documentation for
 * more info (supported font formats, features etc..)
 */
public class XmlFontGenerator implements FontGenerator, Cloneable {
    /** The font file **/
    public final FileHandle file;
    /** Holds font generation parameters **/
    public final FreeTypeFontParameter parameter = new FreeTypeFontParameter();

    /**
     * Creates a generator from a font file.
     * @param file the font file.
     */
    public XmlFontGenerator(FileHandle file) {
        this.file = file;
    }

    @Override
    public BitmapFont generate() throws Exception{
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

    public XmlFontGenerator copy() {
        XmlFontGenerator copy = new XmlFontGenerator(file);
        copy.parameter.size = parameter.size;
        copy.parameter.mono = parameter.mono;
        copy.parameter.hinting = parameter.hinting;
        copy.parameter.color = new Color(parameter.color);
        copy.parameter.gamma = parameter.gamma;
        copy.parameter.renderCount = parameter.renderCount;
        copy.parameter.borderWidth = parameter.borderWidth;
        copy.parameter.borderColor = new Color(parameter.borderColor);
        copy.parameter.borderStraight = parameter.borderStraight;
        copy.parameter.borderGamma = parameter.borderGamma;
        copy.parameter.shadowOffsetX = parameter.shadowOffsetX;
        copy.parameter.shadowOffsetY = parameter.shadowOffsetY;
        copy.parameter.shadowColor = new Color(parameter.shadowColor);
        copy.parameter.spaceX = parameter.spaceX;
        copy.parameter.spaceY = parameter.spaceY;
        copy.parameter.characters = parameter.characters;
        copy.parameter.kerning = parameter.kerning;
        copy.parameter.packer = parameter.packer;
        copy.parameter.flip = parameter.flip;
        copy.parameter.genMipMaps = parameter.genMipMaps;
        copy.parameter.minFilter = parameter.minFilter;
        copy.parameter.magFilter = parameter.magFilter;
        copy.parameter.incremental = parameter.incremental;
        return copy;
    }
}
