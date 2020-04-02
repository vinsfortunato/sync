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

package net.sync.game.resource.xml.resolvers;

import com.badlogic.gdx.graphics.Texture;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

public class XmlTextureFilterResolver implements XmlValueResolver<Texture.TextureFilter> {
    public static final XmlTextureFilterResolver GLOBAL_TEXTURE_FILTER_RESOLVER = new XmlTextureFilterResolver();

    @Override
    public Texture.TextureFilter resolve(String value) throws XmlParseException {
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

        throw new XmlParseException(String.format("Invalid filter format for value '%s'!", value));
    }
}
