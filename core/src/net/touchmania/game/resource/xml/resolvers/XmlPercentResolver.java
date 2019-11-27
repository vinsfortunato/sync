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

import net.touchmania.game.util.math.MathUtils;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.util.xml.XmlParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class XmlPercentResolver extends XmlReferenceValueResolver<Float> {
    private static final Pattern PERCENT_REGEX = Pattern.compile("(?:100(?:\\.0+)?|[1-9]?\\d(?:[.,]\\d+)?)\\s*%");

    @Override
    protected String getResourceTypeName() {
        return "percent";
    }

    @Override
    public Float resolveValue(String value) throws XmlParseException {
        if(value == null) {
            throw new XmlParseException("Value cannot be null");
        }

        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

        if(value.isEmpty()) {
            //Consider empty values as 0%
            return 0.0f;
        }

        Matcher matcher = PERCENT_REGEX.matcher(value);
        if(matcher.matches()) {
            value = value.replaceAll("\\s*%", "");
            return (float) (Double.parseDouble(value) / 100D);
        }

        try {
            //Parse value as percent float
            float percent = Float.parseFloat(value);
            if(percent < 0) {
                throw new XmlParseException("Percentage cannot be negative!");
            }
            //Return clamped percentage
            return MathUtils.clamp(0.0f, 1.0f, percent);
        } catch (Exception e) {
            throw new XmlParseException("Invalid percentage format for value '%s'!", e);
        }
    }

    public static XmlPercentResolver from(final ResourceProvider provider) {
        return new XmlPercentResolver() {
            @Override
            public Float resolveReference(String resourceId) throws XmlParseException {
                return provider.getPercent(resourceId);
            }
        };
    }
}
