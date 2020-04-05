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

package net.sync.game.ui.screen.play;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import net.sync.game.Game;
import net.sync.game.round.Round;
import net.sync.game.song.Beatmap;
import net.sync.game.song.Timing;
import net.sync.game.song.note.FakeNote;
import net.sync.game.song.note.HoldNote;
import net.sync.game.song.note.LiftNote;
import net.sync.game.song.note.MineNote;
import net.sync.game.song.note.Note;
import net.sync.game.song.note.NotePanel;
import net.sync.game.song.note.RollNote;
import net.sync.game.song.note.TapNote;

/**
 * Renders beatmap notes and receptors.
 */
public class BeatmapView extends Widget {
    private Round round;

    /* Receptor renderer */
    private ReceptorRenderer receptorRenderer = new ReceptorRenderer(this);

    /* Note Renderers */
    private TapNoteRenderer tapNoteRenderer = new TapNoteRenderer(this);
    private HoldNoteRenderer holdNoteRenderer = new HoldNoteRenderer(this);
    private RollNoteRenderer rollNoteRenderer = new RollNoteRenderer(this);
    private MineNoteRenderer mineNoteRenderer = new MineNoteRenderer(this);
    private LiftNoteRenderer liftNoteRenderer = new LiftNoteRenderer(this);
    private FakeNoteRenderer fakeNoteRenderer = new FakeNoteRenderer(this);

    private int[] panels = NotePanel.getModePanels(Game.instance().getSettings().getGameMode());

    public BeatmapView(Round round) {
        super();
        this.round = round;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Timing timing = getRound().getTiming();

        //Get current time and beat
        double time = getRound().getMusicPosition().getPosition();
        double beat = timing.getBeatAt(time);

        //Draw panels
        for(int panel : panels) {
            drawReceptor(batch, panel, beat, time);
            drawNotes(batch, panel, beat, time);
        }
    }

    private void drawReceptor(Batch batch, int panel, double beat, double time) {
        receptorRenderer.draw(batch, panel, beat, time);
    }

    private void drawNotes(Batch batch, int panel, double beat, double time) {
        Beatmap beatmap = getBeatmap();

        //Calculate view x, y, width and height to use for note rendering.
        //View position is relative to the receptor position.
        float receptorX = receptorRenderer.getReceptorX(panel, beat, time);
        float receptorY = receptorRenderer.getReceptorY(panel, beat, time);
        float viewW = getWidth();
        float viewH = getHeight();
        Note note;
        NoteRenderer renderer;

        //Get the render starting note
        note = findStartingNote(panel, beat, time, receptorX, receptorY, viewW, viewH);

        //Start by rendering the starting note, then render next notes until a note
        //outside the view is found or the end of the beatmap is reached.
        while(note != null && (renderer = getNoteRenderer(note)).isNoteInsideView(panel, note, beat, time, receptorX, receptorY, viewW, viewH)) {
            renderer.draw(batch, panel, note, beat, time, receptorX, receptorY);
            note = beatmap.higherNote(panel, note.getBeat());
        }
    }

    /**
     * Find the first note that appears inside the view for the given panel. Render will begin from this
     * note and will continue with following notes until reaching a note that renders outside the view.
     * @param panel the note panel
     * @param beat the current beat
     * @param time the current time
     * @param receptorX the receptor x position inside the view
     * @param receptorY the receptor y position inside the view
     * @param viewW the view width
     * @param viewH the view height
     * @return the render starting note, or null if there are no notes to render.
     */
    private Note findStartingNote(int panel, double beat, double time,
                                                          float receptorX, float receptorY, float viewW, float viewH) {
        Beatmap beatmap = getBeatmap();

        if(!beatmap.hasNotes(panel)) {
            //There is no note inside the beatmap for the given panel.
            return null;
        }

        Note note;
        NoteRenderer renderer;

        //Start by getting the closest floor note and find the
        //first note that does not appear inside the view.
        note = beatmap.floorNote(panel, beat);

        while(note != null) {
            renderer = getNoteRenderer(note);
            if(renderer.isNoteInsideView(panel, note, beat, time, receptorX, receptorY, viewW, viewH)) {
                note = beatmap.lowerNote(panel, note.getBeat());
            } else {
                break;
            }
        }

        note = note != null ? beatmap.higherNote(panel, note.getBeat()) : beatmap.firstNote(panel);

        if(note != null) {
            renderer = getNoteRenderer(note);
            if(renderer.isNoteInsideView(panel, note, beat, time, receptorX, receptorY, viewW, viewH)) {
                return note;
            }
        }

        //There is no note to render inside the view
        return null;
    }

    private NoteRenderer getNoteRenderer(Note note) {
        if(note instanceof TapNote)  return tapNoteRenderer;
        if(note instanceof HoldNote) return holdNoteRenderer;
        if(note instanceof RollNote) return rollNoteRenderer;
        if(note instanceof MineNote) return mineNoteRenderer;
        if(note instanceof LiftNote) return liftNoteRenderer;
        if(note instanceof FakeNote) return fakeNoteRenderer;
        return null;
    }

    public Beatmap getBeatmap() {
        return getRound().getChart().beatmap;
    }

    public Round getRound() {
        return round;
    }
}
