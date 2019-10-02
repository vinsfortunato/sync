package net.touchmania.game.ui.play;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import net.touchmania.game.Game;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.Beatmap;
import net.touchmania.game.song.Timing;
import net.touchmania.game.song.note.*;

/**
 * Renders beatmap notes and receptors.
 */
public class BeatmapView extends Widget {
    private Round round;

    /* Note drawers */
    private TapNoteRenderer tapNoteRenderer = new TapNoteRenderer(this);
    private HoldNoteRenderer holdNoteRenderer = new HoldNoteRenderer(this);
    private RollNoteRenderer rollNoteRenderer = new RollNoteRenderer(this);
    private MineNoteRenderer mineNoteRenderer = new MineNoteRenderer(this);
    private LiftNoteRenderer liftNoteRenderer = new LiftNoteRenderer(this);
    private FakeNoteRenderer fakeNoteRenderer = new FakeNoteRenderer(this);

    private NotePanel[] panels = NotePanel.getModePanels(Game.instance().getSettings().getGameMode());

    private float posY = 0;

    public BeatmapView(Round round) {
        super();
        this.round = round;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Timing timing = getRound().getTiming();

        //Get current time and beat
        double time = getRound().getCurrentTime();
        double beat = timing.getBeatAt(time);

        //Draw panels
        for(NotePanel panel : panels) {
            drawNotePanel(batch, panel, beat, time);
        }

        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GREEN);
        renderer.rect(20, 20, getWidth() - 40, getHeight() - 40);
        renderer.setColor(Color.RED);
        renderer.rect(getWidth() / 2 - 40, posY, 80, 80);
        renderer.end();

        batch.begin();

        posY += 1.0f;

        if(posY > getHeight() - 80) {
            posY = 0.0f;
        }
    }

    private void drawNotePanel(Batch batch, NotePanel panel, double beat, double time) {
        Beatmap beatmap = getBeatmap();

        if(!beatmap.hasNotes(panel)) {
            //Nothing to draw
            return;
        }

        float viewX = 0; //TODO relative receptorX
        float viewY = 0; //TODO relative receptorY
        float viewW = getWidth();
        float viewH = getHeight();
        Note note;
        NoteRenderer renderer;

        //Get the render starting note
        note = findStartingNote(panel, beat, time, viewX, viewY, viewW, viewH);
        if(note == null) {
            //Nothing to draw
            return;
        }

        //Start by rendering the starting note, then render next notes until a note
        //outside the view is found or the end of the beatmap is reached.
        while(note != null && (renderer = getNoteRenderer(note)).isNoteInsideView(panel, note, beat, time, viewX, viewY, viewW, viewH)) {
            renderer.draw(batch, panel, note, beat, time);
            note = beatmap.higherNote(panel, note.getBeat());
        }
    }

    /**
     * Find the first note that appears inside the view for the given panel. Render will begin from this
     * note and will continue with following notes until reaching a note that renders outside the view.
     * @param panel the note panel
     * @param beat the current beat
     * @param time the current time
     * @return the render starting note, or null if there are no notes to render.
     */
    private Note findStartingNote(NotePanel panel, double beat, double time,
                                  float viewX, float viewY, float viewW, float viewH) {
        Beatmap beatmap = getBeatmap();
        Note note;
        NoteRenderer renderer;

        //Start by getting the closest floor note and finding the
        //first note that does not appear inside the view.
        note = beatmap.floorNote(panel, beat);

        while(note != null) {
            renderer = getNoteRenderer(note);
            if(renderer.isNoteInsideView(panel, note, beat, time, viewX, viewY, viewW, viewH)) {
                note = beatmap.lowerNote(panel, note.getBeat());
            } else {
                break;
            }
        }

        note = note != null ? beatmap.higherNote(panel, note.getBeat()) : beatmap.firstNote(panel);

        if(note != null) {
            renderer = getNoteRenderer(note);
            if(renderer.isNoteInsideView(panel, note, beat, time, viewX, viewY, viewW, viewH)) {
                return note;
            }
        }

        //There is no visible note
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
