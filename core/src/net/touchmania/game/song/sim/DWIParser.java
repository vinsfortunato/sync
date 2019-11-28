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
import com.badlogic.gdx.utils.ObjectSet;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Chart;
import net.touchmania.game.song.ChartType;
import net.touchmania.game.song.DisplayBPM;
import net.touchmania.game.song.TimingData;
import net.touchmania.game.song.note.HoldNote;
import net.touchmania.game.song.note.Note;
import net.touchmania.game.song.note.NotePanel;
import net.touchmania.game.song.note.NoteResolution;
import net.touchmania.game.song.note.TapNote;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse DWI SIMs - Dance With Intensity (.dwi file extension).
 * @author flood2d
 */
public class DWIParser extends TagSimParser {
    /** Pattern that matches the display bpm syntax **/
    private static Pattern DISPLAY_BPM_PATTERN = Pattern.compile("(\\*)|(?:(\\d+)(?:\\.\\d+)?)(?:\\s*:\\s*(?:(\\d+)(?:\\.\\d+)?))?");
    /** Pattern that matches the BPMs and stops syntax **/
    private static Pattern BPM_STOP_PATTERN = Pattern.compile("([+-]?\\d+(\\.\\d+)?)\\s*=\\s*(\\+?\\d+(\\.\\d+)?)");

    @Override
    protected DataSupplier prepareDataSupplier(String rawContent) throws SimParseException {
        return new DWIDataSupplier(rawContent);
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
        return null; //No matching tag
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
        return null; //No matching tag
    }

    @Override
    public String parseBannerPath() {
        return null; //No matching tag
    }

    @Override
    public String parseBackgroundPath() {
        return null; //No matching tag
    }

    @Override
    public String parseLyricsPath() {
        return null; //No matching tag
    }

    @Override
    public String parseCdTitle() {
        return dataSupplier.getHeaderTagValue("CDTITLE");
    }

    @Override
    public String parseMusicPath() {
        return dataSupplier.getHeaderTagValue("FILE");
    }

    @Override
    public double parseOffset() {
        try {
            return SimParserUtils.parseDouble(dataSupplier.getHeaderTagValue("GAP"));
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
            }
        }
        return null;
    }

    @Override
    public boolean parseSelectable() {
        return true; //No matching tag, always selectable.
    }

    @Override
    public TimingData parseTimingData() throws SimParseException {
        TimingData data = new TimingData();
        parseBpms(data);
        parseStops(data);
        return data;
    }

    private void parseBpms(TimingData data) throws SimParseException {
        //Parse initial bpm value.
        String value = dataSupplier.getHeaderTagValue("BPM");
        if(value == null) {
            throw new SimParseException("BPM tag not found!");
        }
        data.putBpm(0.0D, SimParserUtils.parseDouble(value));

        //Parse bpm changes.
        value = dataSupplier.getHeaderTagValue("CHANGEBPM");
        if(value != null) {
            Matcher matcher = BPM_STOP_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = SimParserUtils.parseDouble(matcher.group(1));
                double bpm = SimParserUtils.parseDouble(matcher.group(3));
                data.putBpm(beat, bpm);
            }
        }
    }

    private void parseStops(TimingData data) {
        try {
            String value = dataSupplier.getHeaderTagValue("FREEZE");
            if(value != null) {
                Matcher matcher = BPM_STOP_PATTERN.matcher(value);
                while(matcher.find()) {
                    double beat = SimParserUtils.parseDouble(matcher.group(1));
                    double length = SimParserUtils.parseDouble(matcher.group(3));
                    data.putStop(beat, length);
                }
            }
        } catch(SimParseException e) {
            data.stops = null; //Reset stop map
        }
    }

    @Override
    protected Chart parseChart(String chartRawData) throws SimParseException {
        String[] chartData = chartRawData.split("\\s*:\\s*");
        if(chartData.length < 4) { //Must be 4 (or 5 if double mode).
            throw new SimParseException("Invalid chart data.");
        }
        ChartType type = parseChartType(chartData[0]);
        if(type != null) { //Chart is supported
            Chart chart = new Chart();
            chart.type = type;
            chart.difficultyClass = SimParserUtils.parseDifficultyClass(chartData[1]);
            chart.difficultyMeter = SimParserUtils.parseInt(chartData[2]);
            return chart;
        }
        return null;
    }

    @Override
    protected Beatmap parseBeatmap(String chartRawData) throws SimParseException {
        return new BeatmapParser(chartRawData.split("\\s*:\\s*")[3]).parse();
    }

    /**
     * Parse the chart type from the chart style. DWI supports only dance game type. If
     * the chart style is unrecognised or unsupported null will be returned.
     * @param chartStyle the chart style.
     * @return the parsed chart type, null if the chart style is unsupported or unrecognised.
     */
    private ChartType parseChartType(String chartStyle) {
        switch(chartStyle.toUpperCase()) {
            case "SINGLE":
                return ChartType.DANCE_SINGLE;
        }
        return null;
    }

    /**
     * Split tags into header and chart tags. Chart tags has chart
     * style as tag name, other tags must be considered header tags.
     */
    private static class DWIDataSupplier implements DataSupplier {
        /** Contains header tags where key is the tag name and value is the tag value **/
        private ObjectMap<String, String> headerTagsMap = new ObjectMap<>();
        /** Contains charts data as string that are a result of the concatenation
         * of tag name and tag value separated by a colon (tagname:tagvalue).
         * In DWI format chart tag names is the style.**/
        private Array<String> chartRawDataArray = new Array<>();

        DWIDataSupplier(String rawContent) throws SimParseException {
            //Split the content into tags
            Matcher matcher = TAG_PATTERN.matcher(rawContent);
            while(matcher.find()) {
                String tagName = matcher.group(1);
                String tagValue = matcher.group(2);

                if(isChartTag(tagName)) {
                    chartRawDataArray.add(tagName + ":" + tagValue);
                } else {
                    headerTagsMap.put(tagName.toUpperCase(), tagValue);
                }
            }
            if(headerTagsMap.size == 0){
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

        private boolean isChartTag(String tagName) {
            return tagName.equalsIgnoreCase("SINGLE")
                    || tagName.equalsIgnoreCase("DOUBLE")
                    || tagName.equalsIgnoreCase("COUPLE")
                    || tagName.equalsIgnoreCase("SOLO");
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
        private ObjectSet<NotePanel> holdingPanelSet = new ObjectSet<>();
        /** Contains combined panels **/
        private ObjectSet<NotePanel> combinePanelSet = new ObjectSet<>();

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
                    NotePanel[] panels = parseNoteCharacter(dataChar);
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

        private void putPanels(NotePanel... panels) throws SimParseException {
            for(NotePanel panel : panels) {
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

        private void holdPanels(NotePanel... panels) throws SimParseException{
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
                NotePanel[] panels = new NotePanel[combinePanelSet.size];
                int i = 0;
                for(NotePanel panel : combinePanelSet) {
                    panels[i] = panel;
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

        private NotePanel[] parseNoteCharacter(char noteChar) throws SimParseException {
            switch(noteChar) {
                case '1': return new NotePanel[] {NotePanel.LEFT, NotePanel.DOWN};
                case '2': return new NotePanel[] {NotePanel.DOWN};
                case '3': return new NotePanel[] {NotePanel.DOWN, NotePanel.RIGHT};
                case '4': return new NotePanel[] {NotePanel.LEFT};
                case '6': return new NotePanel[] {NotePanel.RIGHT};
                case '7': return new NotePanel[] {NotePanel.UP, NotePanel.LEFT};
                case '8': return new NotePanel[] {NotePanel.UP};
                case '9': return new NotePanel[] {NotePanel.UP, NotePanel.RIGHT};
                case 'A': return new NotePanel[] {NotePanel.UP, NotePanel.DOWN};
                case 'B': return new NotePanel[] {NotePanel.LEFT, NotePanel.RIGHT};
                case '0': return new NotePanel[0];
            }
            throw new SimParseException("Invalid beatmap data! Unrecognised data character!");
        }
    }
}
