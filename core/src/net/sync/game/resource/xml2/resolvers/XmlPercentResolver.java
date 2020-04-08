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

package net.sync.game.resource.xml2.resolvers;

import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.xml2.XmlReferenceNotFoundException;
import net.sync.game.util.math.MathUtils;
import net.sync.game.util.xml.XmlParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class XmlPercentResolver extends XmlReferenceResolver<Float> {
    private static final Pattern PERCENT_REGEX = Pattern.compile("(?:100(?:\\.0+)?|[1-9]?\\d(?:[.,]\\d+)?)\\s*%");

    @Override
    protected String getResourceTypeName() {
        return "percent";
    }

    @Override
    public Float resolveValue(String value) throws XmlParseException {
        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

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
            public Float resolveReference(String resourceId) throws XmlReferenceNotFoundException {
                Float percent = provider.getPercent(resourceId);

                if(percent == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return percent;
            }
        };
    }
}
