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
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.ChartType;
import net.touchmania.game.song.TimingData;

import java.util.regex.Matcher;

/**
 * Parse SSC SIMs - Stepmania format (.ssc file extension).
 * @author Vincenzo Fortunato
 */
public class SSCParser extends SMParser {
    @Override
    protected TagSimParser.DataSupplier prepareDataSupplier(String rawContent) throws SimParseException {
        return new SSCDataSupplier(rawContent);
    }

    @Override
    public TimingData parseTimingData() throws SimParseException {
        //Parse offset, bpms, stops, delays and warps
        TimingData data = super.parseTimingData();
        parseDelays(data);
        parseWarps(data);
        return data;
    }

    private void parseDelays(TimingData data) {
        try {
            String value = dataSupplier.getHeaderTagValue("DELAYS");
            if(value != null) {
                Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
                while(matcher.find()) {
                    double beat = SimParserUtils.parseDouble(matcher.group(1));
                    double length = SimParserUtils.parseDouble(matcher.group(3)); //length in seconds
                    data.putDelay(beat, length);
                }
            }
        } catch(SimParseException e) {
            data.delays = null; //Reset stop map
        }
    }

    private void parseWarps(TimingData data) {
        try {
            String value = dataSupplier.getHeaderTagValue("WARPS");
            if(value != null) {
                Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
                while(matcher.find()) {
                    double beat = SimParserUtils.parseDouble(matcher.group(1));
                    double length = SimParserUtils.parseDouble(matcher.group(3)); //length in beats
                    data.putWarp(beat, length);
                }
            }
        } catch(SimParseException e) {
            data.warps = null; //Reset stop map
        }
    }

    @Override
    protected Chart parseChart(String chartRawData) throws SimParseException {
        Chart chart = new Chart();
        Matcher matcher = TagSimParser.TAG_PATTERN.matcher(chartRawData);
        while(matcher.find()) {
            String chartTagName = matcher.group(1);
            String chartTagValue = matcher.group(2);
            switch (chartTagName.toUpperCase()) {
                case "CHARTNAME":
                    chart.name = chartTagValue;
                    break;
                case "STEPSTYPE":
                    chart.type = parseChartType(chartTagValue);
                    if (chart.type == null) { //Unsupported/Unrecognised chart type
                        return null;
                    }
                    break;
                case "DESCRIPTION":
                    chart.description = chartTagValue;
                    break;
                case "CHARTSTYLE":
                    //TODO handle this tag
                    break;
                case "DIFFICULTY":
                    chart.difficultyClass = SimParserUtils.parseDifficultyClass(chartTagValue);
                    break;
                case "METER":
                    chart.difficultyMeter = SimParserUtils.parseInt(chartTagValue);
                    break;
                case "RADARVALUES":
                    //TODO handle this tag
                    break;
                case "CREDIT":
                    chart.credit = chartTagValue;
                    break;

                //TODO add timing tags http://ec2.stepmania.com/wiki/The_.SSC_file_format
            }
        }

        return chart.type == null ? null : chart; //Returns only supported charts
    }

    @Override
    protected Beatmap parseBeatmap(String chartRawData) throws SimParseException {
        Matcher matcher = TagSimParser.TAG_PATTERN.matcher(chartRawData);
        ChartType chartType = null;
        String beatmapData = null;
        while(matcher.find()) {
            String chartTagName = matcher.group(1);
            String chartTagValue = matcher.group(2);
            if(chartTagName.equalsIgnoreCase("NOTES")) {
                beatmapData = chartTagValue;
            } else if(chartTagName.equalsIgnoreCase("STEPSTYPE")) {
                chartType = parseChartType(chartTagValue);
            }
            if(chartType != null && beatmapData != null) {
                break; //Skip other tags
            }
        }
        if(chartType != null) {
            switch(chartType) {
                case DANCE_SINGLE:
                    return new DanceSingleBeatmapParser(beatmapData).parse();
                case PUMP_SINGLE:
                    return new PumpSingleBeatmapParser(beatmapData).parse();
            }
        }
        throw new SimParseException("Chart type unsupported!");
    }

    /**
     * @param value the raw chart type value.
     * @return the chart type or null if unsupported/unrecognised
     */
    private ChartType parseChartType(String value) {
        switch(value.toLowerCase()) {
            case "dance-single":
                return ChartType.DANCE_SINGLE;
            case "pump-single":
                return ChartType.PUMP_SINGLE;
            default:
                return null; //Unsupported game style.
        }
    }

    private static class SSCDataSupplier implements TagSimParser.DataSupplier {
        /** Contains header tags where key is the tag name and value is the tag value **/
        ObjectMap<String, String> headerTagsMap = new ObjectMap<>();
        /** Contains charts data as strings **/
        Array<String> chartRawDataArray = new Array<>();

        SSCDataSupplier(String rawContent) throws SimParseException {
            boolean parsingHeader = true; //Go false when the chart data begins
            Matcher matcher = TagSimParser.TAG_PATTERN.matcher(rawContent);
            StringBuilder strBuilder = new StringBuilder();
            while(matcher.find()) {
                String tagName = matcher.group(1);
                String tagValue = matcher.group(2);

                if(tagName.equalsIgnoreCase("NOTEDATA")) {
                    if(!parsingHeader) { //Go to the next chart
                        chartRawDataArray.add(strBuilder.toString());
                        strBuilder = new StringBuilder(); //Reset to hold next chart data
                    } else {
                        parsingHeader = false; //Stop parsing header
                        if(headerTagsMap.size == 0) { //Header tags map cannot be empty at this point
                            throw new SimParseException("Cannot parse sim file!");
                        }
                    }
                } else if(parsingHeader) { //Header tag
                    headerTagsMap.put(tagName.toUpperCase(), tagValue);
                } else { //Chart data tag
                    strBuilder.append(matcher.group());
                }
            }
            if(!parsingHeader && strBuilder.length() > 0) { //Save last parsed chart
                chartRawDataArray.add(strBuilder.toString());
            }
        }

        @Override
        public String getHeaderTagValue(String tagName) {
            return headerTagsMap.get(tagName);
        }

        @Override
        public Array<String> getChartsRawDataArray() {
            return chartRawDataArray;
        }
    }
}
