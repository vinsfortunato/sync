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

package net.sync.game.resource.xml.resolvers;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

public class XmlTextureWrapResolver implements XmlValueResolver<TextureWrap> {
    public static final XmlTextureWrapResolver GLOBAL_TEXTURE_WRAP_RESOLVER = new XmlTextureWrapResolver();

    @Override
    public TextureWrap resolve(String value) throws XmlParseException {
        //TODO
        switch(value.trim().toLowerCase()) {

        }

        throw new XmlParseException(String.format("Invalid wrap format for value '%s'!", value));
    }
}
