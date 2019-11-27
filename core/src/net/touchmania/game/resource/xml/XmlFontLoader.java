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
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import net.touchmania.game.Game;
import net.touchmania.game.util.Loader;

import java.util.Objects;

/**
 * Generates a font from its file by using FreeType font engine. Check
 * <a href="https://freetype.org/">FreeType</a> documentation for
 * more info (supported font formats, features etc..)
 */
public class XmlFontLoader implements Loader<BitmapFont>, Cloneable {
    private XmlTheme theme;

    /** The font file **/
    public final FileHandle file;
    /** Holds font generation parameters **/
    public final FreeTypeFontParameter parameter = new FreeTypeFontParameter();

    /**
     * Creates a generator from a font file.
     * @param file the font file.
     */
    public XmlFontLoader(XmlTheme theme, FileHandle file) {
        this.theme = theme;
        this.file = file;
    }

    public XmlFontLoader(XmlFontLoader loader) {
        this(loader.theme, loader.file);
        parameter.size = loader.parameter.size;
        parameter.mono = loader.parameter.mono;
        parameter.hinting = loader.parameter.hinting;
        parameter.color = loader.parameter.color != null ? new Color(loader.parameter.color) : null;
        parameter.gamma = loader.parameter.gamma;
        parameter.renderCount = loader.parameter.renderCount;
        parameter.borderWidth = loader.parameter.borderWidth;
        parameter.borderColor = loader.parameter.borderColor != null ? new Color(loader.parameter.borderColor) : null;
        parameter.borderStraight = loader.parameter.borderStraight;
        parameter.borderGamma = loader.parameter.borderGamma;
        parameter.shadowOffsetX = loader.parameter.shadowOffsetX;
        parameter.shadowOffsetY = loader.parameter.shadowOffsetY;
        parameter.shadowColor = loader.parameter.shadowColor != null ? new Color(loader.parameter.shadowColor) : null;
        parameter.spaceX = loader.parameter.spaceX;
        parameter.spaceY = loader.parameter.spaceY;
        parameter.characters = loader.parameter.characters;
        parameter.kerning = loader.parameter.kerning;
        parameter.flip = loader.parameter.flip;
        parameter.genMipMaps = loader.parameter.genMipMaps;
        parameter.minFilter = loader.parameter.minFilter;
        parameter.magFilter = loader.parameter.magFilter;
        parameter.incremental = loader.parameter.incremental;
    }

    public BitmapFont load() throws Exception {
        //Generate hash from params
        HashFunction hf = Hashing.murmur3_128();
        HashCode hc = hf.newHasher()
                .putString(file.path(), Charsets.UTF_8)
                .putInt(parameter.size)
                .putBoolean(parameter.mono)
                .putInt(Objects.hashCode(parameter.hinting))
                .putInt(parameter.color != null ? parameter.color.toIntBits() : 0)
                .putFloat(parameter.gamma)
                .putInt(parameter.renderCount)
                .putFloat(parameter.borderWidth)
                .putInt(parameter.borderColor != null ? parameter.borderColor.toIntBits() : 0)
                .putBoolean(parameter.borderStraight)
                .putFloat(parameter.borderGamma)
                .putFloat(parameter.shadowOffsetX)
                .putFloat(parameter.shadowOffsetY)
                .putInt(parameter.shadowColor != null ? parameter.shadowColor.toIntBits() : 0)
                .putFloat(parameter.spaceX)
                .putFloat(parameter.spaceY)
                .putString(parameter.characters, Charsets.UTF_8)
                .putBoolean(parameter.kerning)
                .putBoolean(parameter.flip)
                .putBoolean(parameter.genMipMaps)
                .putInt(Objects.hashCode(parameter.minFilter))
                .putInt(Objects.hashCode(parameter.magFilter))
                .putBoolean(parameter.incremental).hash();

        return theme.load(hc.asLong(), BitmapFont.class, () -> {
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
        });
    }
}
