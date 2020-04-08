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
import net.sync.game.util.xml.XmlParseException;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class XmlDurationResolver extends XmlReferenceResolver<Long> {
    private static final Pattern TIME_REGEX = Pattern.compile("(?:\\d+:)?(?:\\d{1,2}:)?(?:\\d{1,2}(?:\\.\\d+)?)");

    @Override
    protected String getResourceTypeName() {
        return "duration";
    }

    @Override
    public Long resolveValue(String value) throws XmlParseException {
        //Prepare the value for parsing by removing leading/trailing spaces
        value = value.trim();

        //Check if duration is expressed in the HH:MM:SS.MILLIS format
        Matcher matcher = TIME_REGEX.matcher(value);
        if(matcher.matches()) {
            long duration = 0L;

            //Split value into hours, minutes and seconds
            String parts[] = value.split(":");

            //Parse seconds part
            String seconds = parts[parts.length - 1];
            if(seconds.contains(".")) {
                String ssParts[] = seconds.split("\\.");
                duration += TimeUnit.SECONDS.toMillis(Long.parseLong(ssParts[0]));
                duration += Long.parseLong(ssParts[1]);
            } else {
                duration += TimeUnit.SECONDS.toMillis(Long.parseLong(seconds));
            }

            //Parse minutes part if available
            if(parts.length > 1) {
                duration += TimeUnit.MINUTES.toMillis(Long.parseLong(parts[parts.length - 2]));
            }

            //Parse hours part if available
            if(parts.length > 2) {
                duration += TimeUnit.HOURS.toMillis(Long.parseLong(parts[parts.length - 3]));
            }

            return duration;
        }

        try {
            //Parse value as milliseconds long
            long duration = Long.parseLong(value);
            if(duration < 0) {
                throw new XmlParseException("Duration cannot be negative|");
            }
            return duration;
        } catch(Exception e) {
            throw new XmlParseException("Invalid duration format for value '%s'!", e);
        }
    }

    public static XmlDurationResolver from(final ResourceProvider provider) {
        return new XmlDurationResolver() {
            @Override
            public Long resolveReference(String resourceId) throws XmlReferenceNotFoundException {
                Long duration = provider.getDuration(resourceId);

                if(duration == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return duration;
            }
        };
    }
}
