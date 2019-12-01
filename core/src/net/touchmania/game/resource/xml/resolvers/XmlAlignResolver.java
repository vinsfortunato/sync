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

package net.touchmania.game.resource.xml.resolvers;

import com.badlogic.gdx.utils.Align;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlValueResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlAlignResolver implements XmlValueResolver<Integer> {
    public static final XmlAlignResolver GLOBAL_ALIGN_RESOLVER = new XmlAlignResolver();

    private static final Pattern ALIGN_REGEX = Pattern.compile("(top|bottom)\\|(left|right)|(top|bottom|center|left|right)");

    @Override
    public Integer resolve(String value) throws XmlParseException {
        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

        Matcher matcher = ALIGN_REGEX.matcher(value);
        if(matcher.matches()) {
            if(matcher.group(3) == null) {
                return getAlignFromValue(matcher.group(1)) | getAlignFromValue(matcher.group(2));
            } else {
                return getAlignFromValue(matcher.group(3));
            }
        }

        throw new XmlParseException(String.format("Invalid align format for value '%s'!", value));
    }

    private int getAlignFromValue(String value) {
        switch(value) {
            case "top": return Align.top;
            case "bottom": return Align.bottom;
            case "left": return Align.left;
            case "right": return Align.right;
            default: return Align.center;
        }
    }
}
