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

package net.touchmania.game.song.sim;

import com.badlogic.gdx.utils.Array;
import com.google.common.base.Preconditions;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Chart;

import java.util.regex.Pattern;

/**
 * A base for all the parser that use #TAG:VALUE; syntax to represent song data (e.g. DWI, SM, SSC)
 * <p>
 *
 *     This format supports comments. Every character after the following sequence // and before the end of the line
 *     will be ignored. This parser implementation removes every comment from the raw data before parsing it.
 * </p>
 * @author flood2d
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
         * @return an {@link Array<String>} of string where each string contains
         * data of a single chart. Each format must parse it according to its
         * syntax.
         */
        Array<String> getChartsRawDataArray();
    }

    /** Pattern that matches the #TAG:VALUE; syntax **/
    public static Pattern TAG_PATTERN = Pattern.compile("#\\s*([^:]+?)\\s*:\\s*([^;]*?)\\s*;");

    public DataSupplier dataSupplier = null;

    /**
     * Prepare a data supplier that will be used by the parser.
     * @param rawContent the sim file raw content without comments.
     * @return a data supplier.
     * @throws SimParseException if the data supplier cannot be prepared correctly.
     */
    protected abstract DataSupplier prepareDataSupplier(String rawContent) throws SimParseException;

    /**
     * Parse a chart without the beatmap from raw data.
     * @param chartRawData the chart raw data.
     * @return a supported chart, null otherwise.
     * @throws SimParseException if the chart cannot be parsed correctly
     */
    protected abstract Chart parseChart(String chartRawData) throws SimParseException;

    /**
     * Parse a beatmap from raw chart data.
     * @param chartRawData the chart raw data.
     * @return a beatmap object, never null.
     * @throws SimParseException if the beatmap cannot be parsed correctly.
     */
    protected abstract Beatmap parseBeatmap(String chartRawData) throws SimParseException;

    @Override
    public void init(String rawContent) throws SimParseException {
        Preconditions.checkNotNull(rawContent);

        //Remove comments from the content and prepare the data supplier
        dataSupplier = prepareDataSupplier(rawContent.replaceAll( "//.*", ""));
    }

    @Override
    public Array<Chart> parseCharts() {
        Array<Chart> charts = new Array<>();
        for(String chartRawData : dataSupplier.getChartsRawDataArray()) {
            try {
                charts.add(parseChart(chartRawData));
            } catch (SimParseException e) {
                //Ignore invalid charts
            }
        }
        return charts;
    }

    @Override
    public Beatmap parseBeatmap(Chart chart) throws SimParseException {
        Preconditions.checkArgument(chart.type != null, "Unsupported chart type!");

        //Find the chart
        for(String chartRawData : dataSupplier.getChartsRawDataArray()) {
            Chart c;
            try {
                c = parseChart(chartRawData);
            } catch(SimParseException e) {
                continue; //Ignore invalid charts and go check the next one
            }
            if(c != null && c.equals(chart)) { //Chart found
                return parseBeatmap(chartRawData);
            }
        }
        return null; //No matching chart found
    }
}
