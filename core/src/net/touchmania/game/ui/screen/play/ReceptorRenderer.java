package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.Game;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.NotePanel;

public class ReceptorRenderer {
    private BeatmapView view;

    public ReceptorRenderer(BeatmapView view) {
        this.view = view;
    }

    /**
     * Draws a receptor to the batch. The batch is translated to the view position.
     * @param batch the batch.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     */
    public void draw(Batch batch, NotePanel panel, double beat, double time) {

    }

    /**
     * Gets receptor x position inside the view.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the x position of the receptor inside the view.
     */
    public float getReceptorX(NotePanel panel, double beat, double time) {
        return 0.0f;
    }

    /**
     * Gets receptor y position inside the view.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the y position of the receptor inside the view.
     */
    public float getReceptorY(NotePanel panel, double beat, double time) {
        return 0.0f;
    }

    /**
     * Gets the receptor scale x. Scale is applied to the note texture.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor scale x.
     */
    public float getReceptorScaleX(NotePanel panel, double beat, double time) {
        return 1.0f;
    }

    /**
     * Gets the receptor scale y. Scale is applied to the note texture.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor scale y.
     */
    public float getReceptorScaleY(NotePanel panel, double beat, double time) {
        return 1.0f;
    }

    /**
     * Gets the receptor rotation in degrees with the origin in the center of the receptor texture.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor rotation in degrees.
     */
    public float getReceptorRotation(NotePanel panel, double beat, double time) {
        return 0.0f;
    }

    /**
     * Gets the receptor opacity, a value from 0.0f to 1.0f (inclusive).
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor opacity. 0.0f if the note is invisible, 1.0f if the note is fully opaque.
     */
    public float getReceptorOpacity(NotePanel panel, double beat, double time) {
        return 1.0f;
    }

    /**
     * Checks if the given receptor is visible. If it isn't visible, the receptor will not
     * be drawn.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return true if the receptor is visible and must be drawn.
     */
    public boolean isReceptorVisible(NotePanel panel, double beat, double time) {
        return true;
    }

    /**
     * Gets the receptor drawable.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor drawable.
     */
    public Drawable getReceptorDrawable(NotePanel panel, double beat, double time) {
        return Game.instance().getResources().getDrawable("res_play_dance_receptor");
    }

    /**
     * Gets the round currently being played.
     * @return the current round.
     */
    public Round getRound() {
        return view.getRound();
    }
}
