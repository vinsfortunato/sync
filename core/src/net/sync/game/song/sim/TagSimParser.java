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
