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

import com.badlogic.gdx.graphics.Texture;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlValueResolver;

public class XmlTextureFilterResolver implements XmlValueResolver<Texture.TextureFilter> {
    @Override
    public Texture.TextureFilter resolve(String value) {
        switch(value.trim().toLowerCase()) {
            case "nearest": return Texture.TextureFilter.Nearest;
            case "linear": return Texture.TextureFilter.Linear;
            case "mipmap": return Texture.TextureFilter.MipMap;
            case "mipmapnearestnearest":
            case "mipmap_nearest_nearest": return Texture.TextureFilter.MipMapNearestNearest;
            case "mipmapnearestlinear":
            case "mipmap_nearest_linear": return Texture.TextureFilter.MipMapNearestLinear;
            case "mipmaplinearlinear":
            case "mipmap_linear_linear": return Texture.TextureFilter.MipMapLinearLinear;
            case "mipmaplinearnearest":
            case "mipmap_linear_nearest": return Texture.TextureFilter.MipMapLinearNearest;
        }
        throw new XmlDeserializeException(String.format("Invalid filter format for value '%s'!", value));
    }
}
