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

package net.touchmania.game.util.ui.xml.resolvers;

import com.badlogic.gdx.graphics.Color;
import net.touchmania.game.util.ui.ResourceProvider;
import net.touchmania.game.util.xml.XmlParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class XmlColorResolver extends XmlReferenceValueResolver<Color> {
    private static final Pattern HEX_REGEX = Pattern.compile("#((?:[0-9a-fA-F]{2}){3,4})|#([0-9a-fA-F]{3,4})");
    private static final Pattern RGB_REGEX = Pattern.compile("rgb\\((\\d{1,3}(?:,\\d{1,3}){2})\\)");
    private static final Pattern RGBA_REGEX = Pattern.compile("rgba\\((\\d{1,3}(?:,\\d{1,3}){2},(?:[01]?\\.\\d+))\\)");

    @Override
    protected String getResourceTypeName() {
        return "color";
    }

    @Override
    public Color resolveValue(String value) throws XmlParseException {
        if(value == null || value.isEmpty()) {
            throw new XmlParseException("Invalid color value! Value cannot be null or empty!");
        }

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
            String params[] = matcher.group(1).split(",");
            return new Color(
                    Integer.parseInt(params[0]) / 255f,
                    Integer.parseInt(params[1]) / 255f,
                    Integer.parseInt(params[2]) / 255f,
                    1.0f);
        }

        //Try to match rgba function
        matcher = RGBA_REGEX.matcher(value.trim());
        if(matcher.find()) {
            String params[] = matcher.group(1).split(",");
            return new Color(
                    Integer.parseInt(params[0]) / 255f,
                    Integer.parseInt(params[1]) / 255f,
                    Integer.parseInt(params[2]) / 255f,
                    Float.parseFloat(params[3]));
        }

        throw new XmlParseException("Unrecognised color format!");
    }

    public static XmlColorResolver from(final ResourceProvider provider) {
        return new XmlColorResolver() {
            @Override
            public Color resolveReference(String resourceId) throws XmlParseException {
                return provider.getColor(resourceId);
            }
        };
    }
}
