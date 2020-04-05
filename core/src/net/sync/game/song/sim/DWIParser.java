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

import com.badlogic.gdx.utils.IntSet;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import net.sync.game.song.Beatmap;
import net.sync.game.song.ChartType;
import net.sync.game.song.DifficultyClass;
import net.sync.game.song.DisplayBPM;
import net.sync.game.song.TimingData;
import net.sync.game.song.note.HoldNote;
import net.sync.game.song.note.Note;
import net.sync.game.song.note.NotePanel;
import net.sync.game.song.note.NoteResolution;
import net.sync.game.song.note.TapNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse DWI SIMs - Dance With Intensity (.dwi file extension).
 * @author Vincenzo Fortunato
 */
public class DWIParser extends TagSimParser {
    /** Pattern that matches the display bpm syntax **/
    private static Pattern DISPLAY_BPM_PATTERN = Pattern.compile("(\\*)|(?:(\\d+)(?:\\.\\d+)?)(?:\\s*:\\s*(?:(\\d+)(?:\\.\\d+)?))?");
    /** Pattern that matches the BPMs and stops syntax **/
    private static Pattern BPM_STOP_PATTERN = Pattern.compile("([+-]?\\d+(\\.\\d+)?)\\s*=\\s*(\\+?\\d+(\\.\\d+)?)");

    @Override
    protected DataSupplier createDataSupplier(String rawContent) throws SimParseException {
        return new DWIDataSupplier(rawContent);
    }

    @Override
    protected SimChartParser createChartParser(String chartRawContent) {
        return new DWIChartParser(chartRawContent);
    }

    @Override
    public String parseTitle() throws SimParseException {
        return dataSupplier.getHeaderTagValue("TITLE");
    }

    @Override
    public String parseSubtitle() throws SimParseException {
        throw new SimParseException("This format doesn't support this property.");
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
    public String parseCredit() throws SimParseException{
        throw new SimParseException("This format doesn't support this property.");
    }

    @Override
    public String parseBannerPath() throws SimParseException {
        throw new SimParseException("This format doesn't support this property.");
    }

    @Override
    public String parseBackgroundPath() throws SimParseException {
        throw new SimParseException("This format doesn't support this property.");
    }

    @Override
    public String parseLyricsPath() throws SimParseException {
        throw new SimParseException("This format doesn't support this property.");
    }

    @Override
    public String parseAlbum() {
        return dataSupplier.getHeaderTagValue("CDTITLE");
    }

    @Override
    public String parseMusicPath() {
        return dataSupplier.getHeaderTagValue("FILE");
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
        return -1;
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
        return -1;
    }

    @Override
    public boolean parseSelectable() throws SimParseException{
        throw new SimParseException("This format doesn't support this property");
    }

    public TimingData parseGlobalTimingData() throws SimParseException {
        TimingData data = new TimingData();
        parseGlobalOffset(data);
        parseGlobalBpms(data);
        parseGlobalStops(data);
        return data;
    }

    private void parseGlobalOffset(TimingData data) throws SimParseException {
        String value = dataSupplier.getHeaderTagValue("GAP");
        if(value != null) {
            try {
                data.offset = Double.parseDouble(value);
            } catch(NumberFormatException e) {
                throw new SimParseException("Cannot parse offset tag value: " + value, e);
            }
        }
    }

    private void parseGlobalBpms(TimingData data) throws SimParseException {
        //Parse initial bpm value.
        String value = dataSupplier.getHeaderTagValue("BPM");
        if(value == null) {
            throw new SimParseException("BPM tag not found!");
        }
        try {
            data.bpms = null; //Reset
            data.putBpm(0.0D, Double.parseDouble(value));
        } catch(NumberFormatException e) {
            throw new SimParseException("Cannot parse bpm tag value: " + value);
        }

        //Parse bpm changes.
        value = dataSupplier.getHeaderTagValue("CHANGEBPM");
        if(value != null) {
            Matcher matcher = BPM_STOP_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = Double.parseDouble(matcher.group(1));
                double bpm = Double.parseDouble(matcher.group(3));
                data.putBpm(beat, bpm);
            }
        }
    }

    private void parseGlobalStops(TimingData data) {
        String value = dataSupplier.getHeaderTagValue("FREEZE");
        if(value != null) {
            data.stops = null; //Reset
            Matcher matcher = BPM_STOP_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = Double.parseDouble(matcher.group(1));
                double length = Double.parseDouble(matcher.group(3));
                data.putStop(beat, length);
            }
        }
    }

    /**
     * Split tags into header and chart tags. Chart tags has chart
     * style as tag name, other tags must be considered header tags.
     */
    private static class DWIDataSupplier implements DataSupplier {
        /** Contains header tags where key is the tag name and value is the tag value **/
        private Map<String, String> headerTagsMap = new HashMap<>();
        /** Contains charts data as string that are a result of the concatenation
         * of tag name and tag value separated by a colon (tagname:tagvalue).
         * In DWI format chart tag names is the style.**/
        private List<String> chartTagsValues = new ArrayList<>();

        DWIDataSupplier(String rawContent) throws SimParseException {
            //Split the content into tags
            Matcher matcher = TAG_PATTERN.matcher(rawContent);
            while(matcher.find()) {
                String tagName = matcher.group(1);
                String tagValue = matcher.group(2);

                if(isChartTag(tagName)) {
                    chartTagsValues.add(tagName + ":" + tagValue);
                } else {
                    headerTagsMap.put(tagName.toUpperCase(), tagValue);
                }
            }
            if(headerTagsMap.isEmpty()){
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

        private boolean isChartTag(String tagName) {
            switch(tagName.toUpperCase()) {
                case "SINGLE":
                case "DOUBLE":
                case "COUPLE":
                case "SOLO":
                    return true;
            }
            return false;
        }
    }

    protected class DWIChartParser implements SimChartParser {
        private String[] chartData;
        private String hash;

        public DWIChartParser(String chartRawData) {
            //Compute the chart raw data hash
            Hasher hasher = Hashing.sha256().newHasher();
            hasher.putBytes(chartRawData.getBytes());
            hash = hasher.hash().toString();

            chartData = chartRawData.split("\\s*:\\s*");
        }

        @Override
        public void init() throws SimParseException {
            if(chartData.length < 4) { //Must be 4 (or 5 if double mode).
                throw new SimParseException("Invalid chart data.");
            }
        }

        @Override
        public ChartType parseChartType() throws SimParseException {
            String value = chartData[0];
            if(value != null) {
                switch(value.toUpperCase()) {
                    case "SINGLE":
                        return ChartType.DANCE_SINGLE;
                    default:
                        throw new SimParseException("Unrecognised/Unsupported chart type: " + value);
                }
            }
            return null;

        }

        @Override
        public DifficultyClass parseDifficultyClass() throws SimParseException {
            return DWIParser.parseDifficultyClass(chartData[1]);
        }

        @Override
        public TimingData parseTimingData() throws SimParseException {
            return parseGlobalTimingData();
        }

        @Override
        public Beatmap parseBeatmap() throws SimParseException {
            return new BeatmapParser(chartData[3]).parse();
        }

        @Override
        public DisplayBPM parseDisplayBPM() throws SimParseException {
            String value = dataSupplier.getHeaderTagValue("DISPLAYBPM");
            if(value != null) {
                Matcher matcher = DISPLAY_BPM_PATTERN.matcher(value);
                if(matcher.matches()) {
                    if(matcher.group(1) != null) {
                        return new DisplayBPM.RandomDisplayBPM();
                    }
                    if(matcher.group(3) != null) {
                        return new DisplayBPM.RangeDisplayBPM(
                                Integer.parseInt(matcher.group(3)),
                                Integer.parseInt(matcher.group(2)));
                    }
                    return new DisplayBPM.StaticDisplayBPM(
                            Integer.parseInt(matcher.group(2)));
                } else {
                    throw new SimParseException("Invalid display BPM tag value: " + value);
                }
            }
            return null;
        }

        @Override
        public int parseDifficultyMeter() throws SimParseException {
            String value = chartData[2];
            try {
                return Integer.parseInt(value);
            } catch(NumberFormatException e) {
                throw new SimParseException("Cannot parse difficulty meter value: " + value, e);
            }
        }

        @Override
        public String parseName() throws SimParseException {
            throw new SimParseException("This format doesn't support this property.");
        }

        @Override
        public String parseDescription() throws SimParseException {
            throw new SimParseException("This format doesn't support this property.");
        }

        @Override
        public String parseCredit() throws SimParseException {
            throw new SimParseException("This format doesn't support this property.");
        }

        @Override
        public String getHash() {
            return hash;
        }
    }

    private static class BeatmapParser {
        private Beatmap beatmap = new Beatmap();
        private String beatmapData;
        private NoteResolution resolution = NoteResolution.NOTE_8TH; //Default resolution
        private double currentBeat = 0.0D;
        private boolean combining = false;
        private boolean holdFlag = false;
        /** Contains currently hold panels **/
        private IntSet holdingPanelSet = new IntSet();
        /** Contains combined panels **/
        private IntSet combinePanelSet = new IntSet();

        BeatmapParser(String beatmapData) {
            this.beatmapData = beatmapData.replaceAll("\\s", ""); //Remove whitespaces
        }

        Beatmap parse() throws SimParseException {
            for(int i = 0; i < beatmapData.length(); i++) {
                parseDataCharacter(beatmapData.charAt(i));
            }
            return beatmap;
        }

        /**
         * @param dataChar the beatmap data character to parse.
         * @throws SimParseException if the beatmap data is invalid.
         */
        private void parseDataCharacter(char dataChar) throws SimParseException {
            switch(dataChar) {
                case '(':
                    setNoteResolution(NoteResolution.NOTE_16TH);
                    break;
                case '[':
                    setNoteResolution(NoteResolution.NOTE_24TH);
                    break;
                case '{':
                    setNoteResolution(NoteResolution.NOTE_64TH);
                    break;
                case '`':
                    setNoteResolution(NoteResolution.NOTE_192ND);
                    break;
                case ')':
                case ']':
                case '}':
                case '\'':
                    setNoteResolution(NoteResolution.NOTE_8TH); //Reset default resolution
                    break;
                case '<':
                    setCombining(true); //Enable note characters combine mode to get more than 2 panels together.
                    break;
                case '>':
                    setCombining(false); //Disable note characters combine mode
                    break;
                case '!':
                    if(combining || holdFlag) {
                        //Next character after ! must be a note character or a combination
                        //hold flat ! cant be inside combination brackets < >
                        throw new SimParseException("Invalid beatmap data! Redundant hold flag or invalid flag location");
                    }
                    holdFlag = true; //Mark next character as hold head.
                    break;
                default:
                    //It is a note character.
                    int[] panels = parseNoteCharacter(dataChar);
                    if(combining) {
                        combinePanelSet.addAll(panels);
                    } else if(holdFlag) {
                        holdPanels(panels);
                    } else {
                        putPanels(panels);
                    }
                    break;
            }
        }

        private void putPanels(int ... panels) throws SimParseException {
            for(int panel : panels) {
                if(holdingPanelSet.remove(panel)) {
                    //Parsing hold tail
                    Note lastNote = beatmap.lastNote(panel);
                    if(lastNote instanceof TapNote) {
                        //Calculate hold note length
                        double length = currentBeat - lastNote.getBeat();
                        //Replace previous tap note with hold note
                        beatmap.putNote(panel, new HoldNote(lastNote.getBeat(), length));
                    } else {
                        throw new SimParseException("Invalid beatmap data! Expecting tap note as last map value!");
                    }
                } else {
                    beatmap.putNote(panel, new TapNote(currentBeat));
                }
            }

            currentBeat += resolution.noteDistance;
        }

        private void holdPanels(int ... panels) throws SimParseException{
            if(panels.length == 0) {
                throw new SimParseException("Invalid beatmap data! No panel specified for hold note.");
            }
            holdingPanelSet.addAll(panels);
            holdFlag = false; //Panels marked as hold
        }

        private void setNoteResolution(NoteResolution resolution) throws SimParseException {
            if(combining || holdFlag) {
                throw new SimParseException("Invalid beatmap data! Current state doesn't allow resolution changes.");
            }
            this.resolution = resolution;
        }

        private void setCombining(boolean combining) throws SimParseException {
            if(this.combining == combining) { //Unclosed/Unopened combinations
                throw new SimParseException("Invalid beatmap data. Unclosed/Unopened combinations!");
            }
            this.combining = combining;
            if(!combining && combinePanelSet.size > 0) {
                //Combine panels
                int[] panels = new int[combinePanelSet.size];
                int i = 0;
                IntSet.IntSetIterator it = combinePanelSet.iterator();
                while(it.hasNext) {
                    panels[i] = it.next();
                    i++;
                }
                combinePanelSet.clear();
                if(holdFlag) {
                    holdPanels(panels);
                } else {
                    putPanels(panels);
                }
            }
        }

        private int[] parseNoteCharacter(char noteChar) throws SimParseException {
            switch(noteChar) {
                case '1': return new int[] {NotePanel.LEFT, NotePanel.DOWN};
                case '2': return new int[] {NotePanel.DOWN};
                case '3': return new int[] {NotePanel.DOWN, NotePanel.RIGHT};
                case '4': return new int[] {NotePanel.LEFT};
                case '6': return new int[] {NotePanel.RIGHT};
                case '7': return new int[] {NotePanel.UP, NotePanel.LEFT};
                case '8': return new int[] {NotePanel.UP};
                case '9': return new int[] {NotePanel.UP, NotePanel.RIGHT};
                case 'A': return new int[] {NotePanel.UP, NotePanel.DOWN};
                case 'B': return new int[] {NotePanel.LEFT, NotePanel.RIGHT};
                case '0': return new int[0];
            }
            throw new SimParseException("Invalid beatmap data! Unrecognised data character!");
        }
    }
}
