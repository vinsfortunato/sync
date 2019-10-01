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

import java.util.Map;
import java.util.TreeMap;

/**
 * Renders beatmap notes and receptors.
 */
public class BeatmapView extends Widget {
    private Beatmap beatmap;

    /* Note drawers */
    private TapNoteDrawer tapNoteDrawer = new TapNoteDrawer(this);
    private HoldNoteDrawer holdNoteDrawer = new HoldNoteDrawer(this);
    private RollNoteDrawer rollNoteDrawer = new RollNoteDrawer(this);
    private MineNoteDrawer mineNoteDrawer = new MineNoteDrawer(this);
    private LiftNoteDrawer liftNoteDrawer = new LiftNoteDrawer(this);
    private FakeNoteDrawer fakeNoteDrawer = new FakeNoteDrawer(this);

    private NotePanel[] panels = NotePanel.getModePanels(Game.instance().getSettings().getGameMode());

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

        /**
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GREEN);
        renderer.rect(20, 20, getWidth() - 40, getHeight() - 40);
        renderer.setColor(Color.RED);
        renderer.rect(getWidth() / 2 - 40, getHeight() / 2 - 40, 80, 80);
        renderer.end();

        batch.begin();
         **/
    }

    private void drawNotePanel(Batch batch, NotePanel panel, double beat, double time) {
        TreeMap<Double, Note> notes = beatmap.getNotes(panel);

        if(notes.isEmpty()) {
            //Nothing to draw
            return;
        }

        //Get floor note
        Map.Entry<Double, Note> entry = notes.floorEntry(beat);

        if(entry != null) {
            Note note = entry.getValue();

            if(note instanceof TapNote) {

            }

        }
    }

    private NoteDrawer getNoteDrawer(Note note) {
        if(note instanceof TapNote)  return tapNoteDrawer;
        if(note instanceof HoldNote) return holdNoteDrawer;
        if(note instanceof RollNote) return rollNoteDrawer;
        if(note instanceof MineNote) return mineNoteDrawer;
        if(note instanceof LiftNote) return liftNoteDrawer;
        if(note instanceof FakeNote) return fakeNoteDrawer;
        return null;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    public void setBeatmap(Beatmap beatmap) {
        this.beatmap = beatmap;
    }

    public Round getRound() {
        return null; //TODO
    }
}
