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

package net.sync.game.song.sim;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import net.sync.game.song.DifficultyClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A base for all the parser that use #TAG:VALUE; syntax to represent song data (e.g. DWI, SM, SSC)
 * <p>
 *
 *     This format supports comments. Every character after the following sequence // and before the end of the line
 *     will be ignored. This parser implementation removes every comment from the raw data before parsing it.
 * </p>
 * @author Vincenzo Fortunato
 */
public abstract class TagSimParser implements SimParser {
    /**
     *  Tags can be split into two categories:
     *     <ul>
     *         <li>Header tags - contain song info and global timing data. There may be only one tag with a
     *         given name. Usually they are located at the beginning of the file (header) but this isn't mandatory. </li>
     *         <li>Chart tags - contain chart data and specific chart's timing data. There may be more than one
     *         tag with the same name</li>
     *     </ul>
     *     Each parser that parse formats with the #TAG:VALUE; syntax must
     *     provide an implementation of this interface.
     */
    public interface DataSupplier {
        /**
         * Returns the value of the header tag with the given name.
         * Header tags are unique. There may be only one tag with a given name.
         * @param tagName the name of a header tag.
         * @return
         */
        String getHeaderTagValue(String tagName);

        /**
         * @return a list of strings where each string contains
         * data of a single chart. Each format must parse it according to its
         * syntax.
         */
        List<String> getChartTagValues();
    }

    /** Pattern that matches the #TAG:VALUE; syntax **/
    public static Pattern TAG_PATTERN = Pattern.compile("#\\s*([^:]+?)\\s*:\\s*([^;]*?)\\s*;");

    protected DataSupplier dataSupplier = null;

    @Override
    public void init(SimFile simFile) throws SimParseException {
        Preconditions.checkNotNull(simFile);
        Preconditions.checkState(dataSupplier == null, "Sim parser already initialized!");

        //Get sim file content
        String content = simFile.getFile().readString(Charsets.UTF_8.name());
        //Remove comments from the content and prepare the data supplier
        dataSupplier = createDataSupplier(content.replaceAll("//.*", ""));
    }

    /**
     * Creates a data supplier that will be used by the parser.
     * @param rawContent the sim file raw content without comments.
     * @return a data supplier.
     * @throws SimParseException if the data supplier cannot be created correctly.
     */
    protected abstract DataSupplier createDataSupplier(String rawContent) throws SimParseException;

    /**
     * Creates a sim chart parser from the given raw chart raw content.
     * @param chartRawContent the chart raw content.
     * @return a sim chart parser.
     */
    protected abstract SimChartParser createChartParser(String chartRawContent);

    @Override
    public List<SimChartParser> getChartParsers() {
        List<String> chartTagValues = dataSupplier.getChartTagValues();
        List<SimChartParser> parsers = new ArrayList<>(chartTagValues.size());
        for(String chartTagValue : chartTagValues) {
            parsers.add(createChartParser(chartTagValue));
        }
        return parsers;
    }

    @Override
    public SimChartParser getChartParser(String hash) {
        for(SimChartParser chartParser : getChartParsers()) {
            if(hash.equals(chartParser.getHash())) {
                return chartParser;
            }
        }
        return null;
    }

    /**
     * Parse difficulty class.
     * @param value the value to parse
     * @return the difficulty class.
     * @throws SimParseException if the difficulty class cannot be recognised.
     */
    protected static DifficultyClass parseDifficultyClass(String value) throws SimParseException {
        if(value != null) {
            switch(value.toUpperCase()) {
                case "BEGINNER":
                    return DifficultyClass.BEGINNER;
                case "EASY":
                case "BASIC":
                case "LIGHT":
                    return DifficultyClass.EASY;
                case "MEDIUM":
                case "ANOTHER":
                case "TRICK":
                case "STANDARD":
                case "DIFFICULT":
                    return DifficultyClass.MEDIUM;
                case "HARD":
                case "SSR":
                case "MANIAC":
                case "HEAVY":
                    return DifficultyClass.HARD;
                case "SMANIAC":
                case "CHALLENGE":
                case "EXPERT":
                case "ONI":
                    return DifficultyClass.CHALLENGE;
                case "EDIT":
                    return DifficultyClass.EDIT;
                default:
                    throw new SimParseException("Unrecognised difficulty class!");
            }
        }
        return null;
    }
}
