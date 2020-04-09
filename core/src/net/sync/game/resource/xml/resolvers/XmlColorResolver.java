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

import com.badlogic.gdx.graphics.Color;
import net.sync.game.util.xml.XmlDeserializeException;
import net.sync.game.util.xml.XmlValueResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlColorResolver implements XmlValueResolver<Color> {
    private static final Pattern HEX_REGEX = Pattern.compile("#((?:[0-9a-fA-F]{2}){3,4})|#([0-9a-fA-F]{3,4})");
    private static final Pattern RGB_REGEX = Pattern.compile("rgb\\((\\d{1,3}(?:,\\d{1,3}){2})\\)");
    private static final Pattern RGBA_REGEX = Pattern.compile("rgba\\((\\d{1,3}(?:,\\d{1,3}){2},(?:[01]?\\.\\d+))\\)");

    @Override
    public Color resolve(String value) {
        //Prepare the value for parsing by removing all whitespaces
        value = value.replaceAll("\\s", "");

        //Try to match hex format
        Matcher matcher = HEX_REGEX.matcher(value);
        if(matcher.find()) {
            String hexValue;
            if(matcher.group(1) == null) {
                //Expand three/four digits hex code by doubling each digit
                hexValue = matcher.group(2);
                StringBuilder hexBuilder = new StringBuilder();
                for(int i = 0; i < hexValue.length() * 2; i++) {
                    hexBuilder.append(hexValue.charAt(i / 2));
                }
                hexValue = hexBuilder.toString();
            } else {
                hexValue = matcher.group(1);
            }

            return Color.valueOf(hexValue);
        }

        //Try to match rgb function
        matcher = RGB_REGEX.matcher(value.trim());
        if(matcher.find()) {
            String[] params = matcher.group(1).split(",");
            return new Color(
                    Integer.parseInt(params[0]) / 255f,
                    Integer.parseInt(params[1]) / 255f,
                    Integer.parseInt(params[2]) / 255f,
                    1.0f);
        }

        //Try to match rgba function
        matcher = RGBA_REGEX.matcher(value.trim());
        if(matcher.find()) {
            String[] params = matcher.group(1).split(",");
            return new Color(
                    Integer.parseInt(params[0]) / 255f,
                    Integer.parseInt(params[1]) / 255f,
                    Integer.parseInt(params[2]) / 255f,
                    Float.parseFloat(params[3]));
        }

        throw new XmlDeserializeException("Unrecognised color format!");
    }
}
