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

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import net.sync.game.song.Beatmap;
import net.sync.game.song.note.RollNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse SM SIMs - Old Stepmania format (.sm file extension).
 * @author Vincenzo Fortunato
 */
public class SMParser extends TagSimParser {
    /** Pattern that matches the display bpm syntax **/
    private static Pattern DISPLAY_BPM_PATTERN = Pattern.compile("(\\*)|(?:(\\d+)(?:\\.\\d+)?)(?:\\s*:\\s*(?:(\\d+)(?:\\.\\d+)?))?");
    /** Pattern that matches the BPMs and stops syntax **/
    protected static Pattern TIMING_DATA_PATTERN = Pattern.compile("([+-]?\\d+(\\.\\d+)?)\\s*=\\s*(\\+?\\d+(\\.\\d+)?)");

    @Override
    protected DataSupplier createDataSupplier(String rawContent) throws SimParseException {
        return new SMDataSupplier(rawContent);
    }

    @Override
    protected net.sync.game.song.sim.SimChartParser createChartParser(String chartRawContent) {
        return new SMChartParser(chartRawContent);
    }

    @Override
    public String parseTitle() throws SimParseException {
        return dataSupplier.getHeaderTagValue("TITLE");
    }

    @Override
    public String parseSubtitle() throws SimParseException {
        return dataSupplier.getHeaderTagValue("SUBTITLE");
    }

    @Override
    public String parseArtist() throws SimParseException {
        return dataSupplier.getHeaderTagValue("ARTIST");
    }

    @Override
    public String parseGenre() throws SimParseException {
        return dataSupplier.getHeaderTagValue("GENRE");
    }

    @Override
    public String parseCredit() throws SimParseException {
        return dataSupplier.getHeaderTagValue("CREDIT");
    }

    @Override
    public String parseBannerPath() throws SimParseException {
        return dataSupplier.getHeaderTagValue("BANNER");
    }

    @Override
    public String parseBackgroundPath() throws SimParseException {
        return dataSupplier.getHeaderTagValue("BACKGROUND");
    }

    @Override
    public String parseLyricsPath() throws SimParseException {
        return dataSupplier.getHeaderTagValue("LYRICSPATH");
    }

    @Override
    public String parseAlbum() throws SimParseException {
        return dataSupplier.getHeaderTagValue("CDTITLE");
    }

    @Override
    public String parseMusicPath() throws SimParseException {
        return dataSupplier.getHeaderTagValue("MUSIC");
    }

    @Override
    public float parseSampleStart() throws SimParseException {
        String value = dataSupplier.getHeaderTagValue("SAMPLESTART");
        if(value != null) {
            try {
                return Float.parseFloat(value);
            } catch(NumberFormatException e) {
                throw new SimParseException("Cannot parse sample start value: " + value, e);
            }
        }
        return -1.0f;
    }

    @Override
    public float parseSampleLength() throws SimParseException {
        String value = dataSupplier.getHeaderTagValue("SAMPLELENGTH");
        if(value != null) {
            try {
                return Float.parseFloat(value);
            } catch(NumberFormatException e) {
                throw new SimParseException("Cannot parse sample length value: " + value, e);
            }
        }
        return -1.0f;
    }

    @Override
    public boolean parseSelectable() throws SimParseException {
        String value = dataSupplier.getHeaderTagValue("SELECTABLE");
        if(value != null) {
            switch (value.toLowerCase()) {
                case "yes":
                case "true":
                    return true;
                case "no":
                case "false":
                    return false;
                default:
                    throw new SimParseException("Cannot parse selectable value: " + value);
            }
        }
        return true;
    }

    protected net.sync.game.song.DisplayBPM parseGlobalDisplayBPM() throws SimParseException {
        String value = dataSupplier.getHeaderTagValue("DISPLAYBPM");
        if(value != null) {
            Matcher matcher = DISPLAY_BPM_PATTERN.matcher(value);
            if(matcher.matches()) {
                if(matcher.group(1) != null) {
                    return new net.sync.game.song.DisplayBPM.RandomDisplayBPM();
                }

                if(matcher.group(3) != null) {
                    return new net.sync.game.song.DisplayBPM.RangeDisplayBPM(
                            Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(2)));
                }

                return new net.sync.game.song.DisplayBPM.StaticDisplayBPM(
                        Integer.parseInt(matcher.group(2)));
            } else {
                throw new SimParseException("Invalid display BPM tag value: " + value);
            }
        }
        return null;
    }

    protected net.sync.game.song.TimingData parseGlobalTimingData() throws SimParseException {
        net.sync.game.song.TimingData data = new net.sync.game.song.TimingData();
        parseOffset(data, dataSupplier.getHeaderTagValue("OFFSET"));
        parseBpms(data, dataSupplier.getHeaderTagValue("BPMS"));
        parseStops(data, dataSupplier.getHeaderTagValue("STOPS"));
        return data;
    }

    protected void parseOffset(net.sync.game.song.TimingData data, String value) throws SimParseException {
        if(value != null) {
            try {
                data.offset = Double.parseDouble(dataSupplier.getHeaderTagValue("OFFSET"));
            } catch(NumberFormatException e) {
                throw new SimParseException("Cannot parse offset tag value: " + value, e);
            }
        }
    }

    protected void parseBpms(net.sync.game.song.TimingData data, String value) throws SimParseException {
        if(value != null) {
            data.bpms = null; //Reset first
            Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = Double.parseDouble(matcher.group(1));
                double bpm = Double.parseDouble(matcher.group(3));
                data.putBpm(beat, bpm);
            }
        }
    }

    protected void parseStops(net.sync.game.song.TimingData data, String value) throws SimParseException {
        if(value != null) {
            data.stops = null; //Reset first
            Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = Double.parseDouble(matcher.group(1));
                double length = Double.parseDouble(matcher.group(3)); //length in seconds
                data.putStop(beat, length);
            }
        }
    }

    /**
     * Split tags into header and chart tags. Chart tags have NOTES
     * as tag name, other tags must be considered header tags.
     */
    private static class SMDataSupplier implements DataSupplier {
        /** Contains header tags where key is the tag name and value is the tag value **/
        private Map<String, String> headerTagsMap = new HashMap<>();
        /** Contains charts data as strings **/
        private List<String> chartTagsValues = new ArrayList<>();

        SMDataSupplier(String rawContent) throws SimParseException {
            //Split the content into tags
            Matcher matcher = TAG_PATTERN.matcher(rawContent);
            while(matcher.find()) {
                String tagName = matcher.group(1);
                String tagValue = matcher.group(2);

                if(tagName.equalsIgnoreCase("NOTES")) { //Chart tag
                    chartTagsValues.add(tagValue);
                } else { //Header tag
                    headerTagsMap.put(tagName.toUpperCase(), tagValue);
                }
            }

            if(headerTagsMap.isEmpty()) {
                throw new SimParseException("Cannot parse sim file!");
            }
        }

        @Override
        public String getHeaderTagValue(String tagName) {
            return headerTagsMap.get(tagName);
        }

        @Override
        public List<String> getChartTagValues() {
            return chartTagsValues;
        }

    }

    protected class SMChartParser implements SimChartParser {
        private String[] chartData;
        private String hash;

        public SMChartParser(String chartRawData) {
            //Compute the chart raw data hash
            Hasher hasher = Hashing.sha256().newHasher();
            hasher.putBytes(chartRawData.getBytes());
            hash = hasher.hash().toString();

            chartData = chartRawData.split("\\s*:\\s*");
        }

        @Override
        public void init() throws SimParseException {
            if(chartData.length != 6) {
                throw new SimParseException("Invalid chart data.");
            }
        }

        @Override
        public net.sync.game.song.ChartType parseChartType() throws SimParseException {
            String value = chartData[0];
            if(value != null) {
                switch(value.toLowerCase()) {
                    case "dance-single":
                        return net.sync.game.song.ChartType.DANCE_SINGLE;
                    case "pump-single":
                        return net.sync.game.song.ChartType.PUMP_SINGLE;
                    default:
                        throw new SimParseException("Unrecognised/Unsupported chart type: " + value);
                }
            }
            return null;
        }

        @Override
        public net.sync.game.song.DifficultyClass parseDifficultyClass() throws SimParseException {
            return SMParser.parseDifficultyClass(chartData[2]);
        }

        @Override
        public net.sync.game.song.TimingData parseTimingData() throws SimParseException {
            return parseGlobalTimingData();
        }

        @Override
        public net.sync.game.song.Beatmap parseBeatmap() throws SimParseException {
            net.sync.game.song.ChartType type = parseChartType();
            if(type != null) {
                switch(type) {
                    case DANCE_SINGLE:
                        return new DanceSingleBeatmapParser(chartData[5]).parse();
                    case PUMP_SINGLE:
                        return new PumpSingleBeatmapParser(chartData[5]).parse();
                    default:
                        throw new SimParseException("Unrecognised/Unsupported chart type: " + type);
                }
            }
            return null;
        }

        @Override
        public net.sync.game.song.DisplayBPM parseDisplayBPM() throws SimParseException {
            String value = dataSupplier.getHeaderTagValue("DISPLAYBPM");
            if(value != null) {
                Matcher matcher = DISPLAY_BPM_PATTERN.matcher(value);
                if(matcher.matches()) {
                    if(matcher.group(1) != null) {
                        return new net.sync.game.song.DisplayBPM.RandomDisplayBPM();
                    }
                    if(matcher.group(3) != null) {
                        return new net.sync.game.song.DisplayBPM.RangeDisplayBPM(
                                Integer.parseInt(matcher.group(3)),
                                Integer.parseInt(matcher.group(2)));
                    }
                    return new net.sync.game.song.DisplayBPM.StaticDisplayBPM(
                            Integer.parseInt(matcher.group(2)));
                } else {
                    throw new SimParseException("Invalid display BPM tag value: " + value);
                }
            }
            return null;
        }

        @Override
        public int parseDifficultyMeter() throws SimParseException {
            String value = chartData[3];
            try {
                return Integer.parseInt(value);
            } catch(NumberFormatException e) {
                throw new SimParseException("Cannot parse difficulty meter value: " + value, e);
            }
        }

        @Override
        public String parseName() throws SimParseException {
            throw new SimParseException("This format doesn't support this property");
        }

        @Override
        public String parseDescription() throws SimParseException {
            return chartData[1];
        }

        @Override
        public String parseCredit() throws SimParseException {
            throw new SimParseException("This format doesn't support this property");
        }

        @Override
        public String getHash() {
            return hash;
        }
    }

    protected abstract static class BeatmapParser {
        protected net.sync.game.song.Beatmap beatmap = new net.sync.game.song.Beatmap();
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
                net.sync.game.song.note.NoteResolution resolution = null;
                int notesInMeasure = measure.length() / getPanelsCount();
                for(net.sync.game.song.note.NoteResolution res : net.sync.game.song.note.NoteResolution.values()) {
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
                        int panel = getPanelFromIndex(m);
                        if(panel == -1) {
                            throw new SimParseException("Invalid note panel!");
                        }
                        parseNote(panel, currentBeat, measure.charAt(l+m));
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
        public void parseNote(int panel, double beat, char c) throws SimParseException {
            switch(c) {
                case '1':
                    beatmap.putNote(panel, new net.sync.game.song.note.TapNote(beat));
                    break;
                case '2':
                    beatmap.putNote(panel, new net.sync.game.song.note.HoldNote(beat));
                    break;
                case '3':
                    net.sync.game.song.note.Note lastNote = beatmap.lastNote(panel);
                    //Last note in the map must be a HoldNote or RollNote otherwise beatmap data is invalid.
                    if(lastNote instanceof net.sync.game.song.note.HoldNote) {
                        //Replace note with the properly sized one
                        beatmap.putNote(panel, new net.sync.game.song.note.HoldNote(lastNote.getBeat(), beat - lastNote.getBeat()));
                    } else if(lastNote instanceof net.sync.game.song.note.RollNote) {
                        //Replace note with the properly sized one
                        beatmap.putNote(panel, new net.sync.game.song.note.RollNote(lastNote.getBeat(), beat - lastNote.getBeat()));
                    } else {
                        throw new SimParseException("Cannot parse LengthyNote length!");
                    }
                    break;
                case '4':
                    beatmap.putNote(panel, new RollNote(beat));
                    break;
                case 'M':
                    beatmap.putNote(panel, new net.sync.game.song.note.MineNote(beat));
                    break;
                case 'K':
                    beatmap.putNote(panel, new net.sync.game.song.note.AutoKeySoundNote(beat));
                    break;
                case 'L':
                    beatmap.putNote(panel, new net.sync.game.song.note.LiftNote(beat));
                    break;
                case 'F':
                    beatmap.putNote(panel, new net.sync.game.song.note.FakeNote(beat));
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
        public abstract int getPanelFromIndex(int index);
    }

    protected static class DanceSingleBeatmapParser extends BeatmapParser {
        DanceSingleBeatmapParser(String beatmapData) {
            super(beatmapData);
        }

        @Override
        public int getPanelsCount() {
            return net.sync.game.song.ChartType.DANCE_SINGLE.panels;
        }

        public int getPanelFromIndex(int index) {
            switch(index) {
                case 0:
                    return net.sync.game.song.note.NotePanel.LEFT;
                case 1:
                    return net.sync.game.song.note.NotePanel.DOWN;
                case 2:
                    return net.sync.game.song.note.NotePanel.UP;
                case 3:
                    return net.sync.game.song.note.NotePanel.RIGHT;
            }
            return -1;
        }
    }

    protected static class PumpSingleBeatmapParser extends BeatmapParser {
        PumpSingleBeatmapParser(String beatmapData) {
            super(beatmapData);
        }

        @Override
        public int getPanelsCount() {
            return net.sync.game.song.ChartType.PUMP_SINGLE.panels;
        }

        @Override
        public int getPanelFromIndex(int index) {
            switch(index) {
                case 0:
                    return net.sync.game.song.note.NotePanel.LEFT_DOWN;
                case 1:
                    return net.sync.game.song.note.NotePanel.LEFT_UP;
                case 2:
                    return net.sync.game.song.note.NotePanel.CENTER;
                case 3:
                    return net.sync.game.song.note.NotePanel.RIGHT_UP;
                case 4:
                    return net.sync.game.song.note.NotePanel.RIGHT_DOWN;
            }
            return -1;
        }
    }
}
