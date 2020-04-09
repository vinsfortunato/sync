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

import com.badlogic.gdx.utils.Align;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlAlignResolver implements XmlValueResolver<Integer> {
    private static final Pattern ALIGN_REGEX = Pattern.compile("(top|bottom)\\|(left|right)|(top|bottom|center|left|right)");

    @Override
    public Integer resolve(String value) {
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
        throw new XmlDeserializeException(String.format("Invalid align format for value '%s'!", value));
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
