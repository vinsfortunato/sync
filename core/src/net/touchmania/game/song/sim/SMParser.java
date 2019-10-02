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
import net.touchmania.game.song.DisplayBPM;
import net.touchmania.game.song.GrooveRadar;
import net.touchmania.game.song.TimingData;
import net.touchmania.game.song.note.AutoKeySoundNote;
import net.touchmania.game.song.note.FakeNote;
import net.touchmania.game.song.note.HoldNote;
import net.touchmania.game.song.note.LengthyNote;
import net.touchmania.game.song.note.LiftNote;
import net.touchmania.game.song.note.MineNote;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;
import net.touchmania.game.song.note.NoteResolution;
import net.touchmania.game.song.note.RollNote;
import net.touchmania.game.song.note.TapNote;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse SM SIMs - Old Stepmania format (.sm file extension).
 * @author flood2d
 */
public class SMParser extends TagSimParser {
    /** Pattern that matches the display bpm syntax **/
    private static Pattern DISPLAY_BPM_PATTERN = Pattern.compile("(\\*)|(\\d+)(\\s*:\\s*(\\d+))?");
    /** Pattern that matches the BPMs and stops syntax **/
    protected static Pattern TIMING_DATA_PATTERN = Pattern.compile("([+-]?\\d+(\\.\\d+)?)\\s*=\\s*(\\+?\\d+(\\.\\d+)?)");

    @Override
    protected DataSupplier prepareDataSupplier(String rawContent) throws SimParseException {
        return new SMDataSupplier(rawContent);
    }

    @Override
    public String parseTitle() throws SimParseException {
        String title = dataSupplier.getHeaderTagValue("TITLE");
        if(title == null || title.isEmpty()) {
            throw new SimParseException("Cannot parse song title!");
        }
        return title;
    }

    @Override
    public String parseSubtitle() {
        return dataSupplier.getHeaderTagValue("SUBTITLE");
    }

    @Override
    public String parseArtist() {
        return dataSupplier.getHeaderTagValue("ARTIST");
    }

    @Override
    public String parseGenre() {
        return dataSupplier.getHeaderTagValue("GENRE");
    }

    @Override
    public String parseCredit() {
        return dataSupplier.getHeaderTagValue("CREDIT");
    }

    @Override
    public String parseBannerPath() {
        return dataSupplier.getHeaderTagValue("BANNER");
    }

    @Override
    public String parseBackgroundPath() {
        return dataSupplier.getHeaderTagValue("BACKGROUND");
    }

    @Override
    public String parseLyricsPath() {
        return dataSupplier.getHeaderTagValue("LYRICSPATH");
    }

    @Override
    public String parseCdTitle() {
        return dataSupplier.getHeaderTagValue("CDTITLE");
    }

    @Override
    public String parseMusicPath() {
        return dataSupplier.getHeaderTagValue("MUSIC");
    }

    @Override
    public double parseOffset() {
        try {
            return SimParserUtils.parseDouble(dataSupplier.getHeaderTagValue("OFFSET"));
        } catch (SimParseException e) {
            return 0.0f;
        }
    }

    @Override
    public float parseSampleStart() {
        try {
            return SimParserUtils.parseFloat(dataSupplier.getHeaderTagValue("SAMPLESTART"));
        } catch (SimParseException e) {
            return -1.0f;
        }
    }

    @Override
    public float parseSampleLength() {
        try {
            return SimParserUtils.parseFloat(dataSupplier.getHeaderTagValue("SAMPLELENGTH"));
        } catch (SimParseException e) {
            return -1.0f;
        }
    }

    @Override
    public DisplayBPM parseDisplayBPM() {
        String value = dataSupplier.getHeaderTagValue("DISPLAYBPM");
        if(value != null) {
            Matcher matcher = DISPLAY_BPM_PATTERN.matcher(value);
            if(matcher.find()) {
                if(!matcher.group(1).isEmpty()) {
                    return new DisplayBPM.RandomDisplayBPM();
                }

                if(!matcher.group(4).isEmpty()) {
                    return new DisplayBPM.RangeDisplayBPM(
                            Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(4)));
                }

                return new DisplayBPM.StaticDisplayBPM(
                        Integer.parseInt(matcher.group(2)));
            }
        }
        return null;
    }

    @Override
    public boolean parseSelectable() {
        String value = dataSupplier.getHeaderTagValue("SELECTABLE");
        return value == null || !value.equalsIgnoreCase("NO");
    }

    @Override
    public TimingData parseTimingData() throws SimParseException {
        TimingData data = new TimingData();
        parseBpms(data);
        parseStops(data);
        return data;
    }

    private void parseStops(TimingData data) {
        try {
            String value = dataSupplier.getHeaderTagValue("STOPS");
            if(value != null) {
                Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
                while(matcher.find()) {
                    double beat = SimParserUtils.parseDouble(matcher.group(1));
                    double length = SimParserUtils.parseDouble(matcher.group(3)); //length in seconds
                    data.putStop(beat, length);
                }
            }
        } catch(SimParseException e) {
            data.stops = null; //Reset stop map
        }
    }

    private void parseBpms(TimingData data) throws SimParseException {
        try {
            String value = dataSupplier.getHeaderTagValue("BPMS");
            if(value != null) {
                Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
                while(matcher.find()) {
                    double beat = SimParserUtils.parseDouble(matcher.group(1));
                    double bpm = SimParserUtils.parseDouble(matcher.group(3));
                    data.putBpm(beat, bpm);
                }
            }
        } catch(SimParseException e) {
            //Keep already parsed bpm changes
        }

        if(data.bpms == null || data.bpms.isEmpty()) { //No bpm found
            throw new SimParseException("BPM tag is invalid or empty");
        }
    }

    @Override
    protected Chart parseChart(String chartRawData) throws SimParseException {
        String[] chartData = chartRawData.split("\\s*:\\s*");

        if(chartData.length != 6) {
            throw new SimParseException("Invalid chart data.");
        }

        ChartType type = parseChartType(chartData[0]);
        if(type != null) {
            Chart chart = new Chart();
            chart.type = type;
            chart.description = chartData[1];
            chart.difficultyClass = SimParserUtils.parseDifficultyClass(chartData[2]);
            chart.difficultyMeter = SimParserUtils.parseInt(chartData[3]);
            try {
                chart.grooveRadar = parseGrooveRadar(chartData[4]);
            } catch(SimParseException e) {
                chart.grooveRadar = null; //Ignore invalid groove radar.
            }
            return chart;
        }
        return null; //Unsupported game style.
    }

    private GrooveRadar parseGrooveRadar(String value) throws SimParseException {
        String[] grooveValues = value.split("\\s*,\\s*");

        if(grooveValues.length != 5) {
            throw new SimParseException("Groove radar cannot be parsed!");
        }

        GrooveRadar radar = new GrooveRadar();
        radar.air = SimParserUtils.parseFloat(grooveValues[0]);
        radar.chaos = SimParserUtils.parseFloat(grooveValues[1]);
        radar.freeze = SimParserUtils.parseFloat(grooveValues[2]);
        radar.stream = SimParserUtils.parseFloat(grooveValues[3]);
        radar.voltage = SimParserUtils.parseFloat(grooveValues[4]);
        return radar;
    }

    /**
     * @param value the raw chart type.
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

    @Override
    protected Beatmap parseBeatmap(String chartRawData) throws SimParseException {
        String[] chartData = chartRawData.split("\\s*:\\s*");
        ChartType chartType = parseChartType(chartData[0]);
        if(chartType != null) {
            switch(chartType) {
                case DANCE_SINGLE:
                    return new DanceSingleBeatmapParser(chartData[5]).parse();
                case PUMP_SINGLE:
                    return new PumpSingleBeatmapParser(chartData[5]).parse();
            }
        }
        throw new SimParseException("Chart type unsupported!");
    }

    /**
     * Split tags into header and chart tags. Chart tags have NOTES
     * as tag name, other tags must be considered header tags.
     */
    private static class SMDataSupplier implements DataSupplier {
        /** Contains header tags where key is the tag name and value is the tag value **/
        ObjectMap<String, String> headerTagsMap = new ObjectMap<>();
        /** Contains charts data as strings **/
        Array<String> chartRawDataArray = new Array<>();

        SMDataSupplier(String rawContent) throws SimParseException {
            //Split the content into tags
            Matcher matcher = TAG_PATTERN.matcher(rawContent);
            while(matcher.find()) {
                String tagName = matcher.group(1);
                String tagValue = matcher.group(2);

                if(tagName.equalsIgnoreCase("NOTES")) { //Chart tag
                    chartRawDataArray.add(tagValue);
                } else { //Header tag
                    headerTagsMap.put(tagName.toUpperCase(), tagValue);
                }
            }

            if(headerTagsMap.size == 0) {
                throw new SimParseException("Cannot parse sim file!");
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

    protected abstract static class BeatmapParser {
        protected Beatmap beatmap = new Beatmap();
        private String[] measures;

        BeatmapParser(String beatmapData) {
            beatmapData = beatmapData.replaceAll("\\s", ""); //Remove whitespaces
            measures = beatmapData.split(",");
        }

        Beatmap parse() throws SimParseException {
            for(int i = 0; i < measures.length; i++) { //Parse each measure separately
                String measure = measures[i];

                //Ensure measure validity
                if(measure.length() % getPanelsCount() != 0) {
                    throw new SimParseException("The value cannot be parsed");
                }

                //Measure note spacing resolution
                NoteResolution resolution = null;
                int notesInMeasure = measure.length() / getPanelsCount();
                for(NoteResolution res : NoteResolution.values()) {
                    if(res.notesInMeasure == notesInMeasure) {
                        resolution = res;
                        break;
                    }
                }
                if (resolution == null) {
                    throw new SimParseException("The value cannot be parsed!");
                }

                double currentBeat = i * 4.0D;
                //Read each measure line
                for(int l = 0; l < measure.length(); l+=getPanelsCount()) {
                    //Read each character of the current line and parse it
                    for(int m = 0; m < getPanelsCount(); m++) {
                        parseNote(getPanelFromIndex(m), currentBeat, measure.charAt(l));
                    }
                    currentBeat += resolution.noteDistance;
                }
            }
            return beatmap;
        }

        /**
         * Parse a note.
         * @param panel the note panel.
         * @param beat the beat at the given line.
         * @param c the character representing the note.
         * @throws SimParseException if the note cannot be parsed correctly.
         */
        public void parseNote(NotePanel panel, double beat, char c) throws SimParseException {
            switch(c) {
                case '1':
                    beatmap.putNote(panel, new TapNote(beat));
                    break;
                case '2':
                    beatmap.putNote(panel, new HoldNote(beat));
                    break;
                case '3':
                    Note lastNote = beatmap.lastNote(panel);
                    //Last note in the map must be a LengthyNote otherwise beatmap data is invalid.
                    if(lastNote instanceof LengthyNote) {
                        ((LengthyNote) lastNote).setLength(beat - lastNote.getBeat());
                    } else {
                        throw new SimParseException("Cannot parse LengthyNote length!");
                    }
                    break;
                case '4':
                    beatmap.putNote(panel, new RollNote(beat));
                    break;
                case 'M':
                    beatmap.putNote(panel, new MineNote(beat));
                    break;
                case 'K':
                    beatmap.putNote(panel, new AutoKeySoundNote(beat));
                    break;
                case 'L':
                    beatmap.putNote(panel, new LiftNote(beat));
                    break;
                case 'F':
                    beatmap.putNote(panel, new FakeNote(beat));
                    break;
            }
        }

        /**
         * @return the number of panels.
         */
        public abstract int getPanelsCount();

        /**
         * @param index the measure line index from left to right.
         * @return the note panel related to the given measure line index, or
         * null if there's no panel associated to the given index.
         */
        public abstract NotePanel getPanelFromIndex(int index);
    }

    protected static class DanceSingleBeatmapParser extends BeatmapParser {
        DanceSingleBeatmapParser(String beatmapData) {
            super(beatmapData);
        }

        @Override
        public int getPanelsCount() {
            return ChartType.DANCE_SINGLE.panels;
        }

        public NotePanel getPanelFromIndex(int index) {
            switch(index) {
                case 0:
                    return NotePanel.LEFT;
                case 1:
                    return NotePanel.DOWN;
                case 2:
                    return NotePanel.UP;
                case 3:
                    return NotePanel.RIGHT;
            }
            return null;
        }
    }

    protected static class PumpSingleBeatmapParser extends BeatmapParser {
        PumpSingleBeatmapParser(String beatmapData) {
            super(beatmapData);
        }

        @Override
        public int getPanelsCount() {
            return ChartType.PUMP_SINGLE.panels;
        }

        @Override
        public NotePanel getPanelFromIndex(int index) {
            switch(index) {
                case 0:
                    return NotePanel.LEFT_DOWN;
                case 1:
                    return NotePanel.LEFT_UP;
                case 2:
                    return NotePanel.CENTER;
                case 3:
                    return NotePanel.RIGHT_UP;
                case 4:
                    return NotePanel.RIGHT_DOWN;
            }
            return null;
        }
    }
}
