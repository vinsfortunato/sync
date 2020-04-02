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

import com.badlogic.gdx.graphics.Pixmap.Format;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

public class XmlPixmapFormatResolver implements XmlValueResolver<Format> {
    public static final XmlPixmapFormatResolver GLOBAL_PIXMAP_FORMAT_RESOLVER = new XmlPixmapFormatResolver();

    @Override
    public Format resolve(String value) throws XmlParseException {
        //TODO
        switch(value.trim().toLowerCase()) {

        }

        throw new XmlParseException(String.format("Invalid pixmap format for value '%s'!", value));
    }
}
