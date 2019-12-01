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

import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.resource.xml.exception.XmlReferenceNotFoundException;
import net.touchmania.game.util.xml.XmlParseException;

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
