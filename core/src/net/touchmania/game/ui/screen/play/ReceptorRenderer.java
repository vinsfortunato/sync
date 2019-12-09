package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import net.touchmania.game.Game;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.resource.lazy.Resource;
import net.touchmania.game.round.PanelState;
import net.touchmania.game.round.Round;
import net.touchmania.game.song.note.NotePanel;
import net.touchmania.game.util.math.MathUtils;

public class ReceptorRenderer {
    private BeatmapView view;

    /* Resources */
    private Resource<Drawable> receptorDrawable;
    private Resource<Drawable> outlineDrawable;

    public ReceptorRenderer(BeatmapView view) {
        this.view = view;

        //Prepare resources
        ResourceProvider resources = Game.instance().getResources();
        (receptorDrawable = resources.getDrawable("play_dance_receptor"        )).load();
        (outlineDrawable  = resources.getDrawable("play_dance_receptor_outline")).load();
    }

    /**
     * Draws a receptor to the batch. The batch is translated to the view position.
     * @param batch the batch.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     */
    public void draw(Batch batch, int panel, double beat, double time) {
        if(isReceptorVisible(panel, beat, time))
            drawReceptor(batch, panel, beat, time);
        if(isOutlineVisible(panel, beat, time))
            drawOutline(batch, panel, beat, time);
    }

    private void drawReceptor(Batch batch, int panel, double beat, double time) {
        Drawable drawable = getReceptorDrawable(panel, beat, time);
        if(drawable == null) {
            return; //TODO
        }

        float width = drawable.getMinWidth();
        float height = drawable.getMinHeight();
        float x = getReceptorX(panel, beat, time);
        float y = getReceptorY(panel, beat, time);
        float originX = width / 2.0f;
        float originY = height / 2.0f;
        float rotation = getReceptorRotation(panel, beat, time);
        float scaleX = getReceptorScaleX(panel, beat, time);
        float scaleY = getReceptorScaleY(panel, beat, time);
        float opacity = getReceptorOpacity(panel, beat, time);

        //Set opacity
        Color color = new Color(batch.getColor());
        color.a = opacity;
        batch.setColor(color);

        //Draw
        if(drawable instanceof TransformDrawable) {
            TransformDrawable transformDrawable = (TransformDrawable) drawable;
            transformDrawable.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        } else {
            drawable.draw(batch, x, y, width * scaleX, height * scaleY);
        }
    }

    private void drawOutline(Batch batch, int panel, double beat, double time) {
        Drawable drawable = getOutlineDrawable(panel, beat, time);
        if(drawable == null) {
            return; //TODO
        }

        float width = drawable.getMinWidth();
        float height = drawable.getMinHeight();
        float x = getOutlineX(panel, beat, time);
        float y = getOutlineY(panel, beat, time);
        float originX = width / 2.0f;
        float originY = height / 2.0f;
        float rotation = getOutlineRotation(panel, beat, time);
        float scaleX = getOutlineScaleX(panel, beat, time);
        float scaleY = getOutlineScaleY(panel, beat, time);
        float opacity = getOutlineOpacity(panel, beat, time);

        //Set opacity
        Color color = new Color(batch.getColor());
        color.a = opacity;
        batch.setColor(color);

        //Draw
        if(drawable instanceof TransformDrawable) {
            TransformDrawable transformDrawable = (TransformDrawable) drawable;
            transformDrawable.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        } else {
            drawable.draw(batch, x, y, width * scaleX, height * scaleY);
        }
    }

    /**
     * Gets receptor x position inside the view.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the x position of the receptor inside the view.
     */
    public float getReceptorX(int panel, double beat, double time) {
        Drawable drawable = getReceptorDrawable(panel, beat, time);
        if(drawable == null) return 0.0f;

        float width = drawable.getMinWidth();
        switch(panel) {
            case NotePanel.DOWN:
                return width;
            case NotePanel.UP:
                return width * 2.0f;
            case NotePanel.RIGHT:
                return width * 3.0f;
        }
        return 0.0f;
    }

    /**
     * Gets receptor y position inside the view.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the y position of the receptor inside the view.
     */
    public float getReceptorY(int panel, double beat, double time) {
        boolean desktop = Gdx.app.getType() == Application.ApplicationType.Desktop;
        return (desktop ? 1080 : 1920) - 256 - 64;
    }

    /**
     * Gets the receptor scale x. Scale is applied to the note texture.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor scale x.
     */
    public float getReceptorScaleX(int panel, double beat, double time) {
        return 1.0f;
    }

    /**
     * Gets the receptor scale y. Scale is applied to the note texture.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor scale y.
     */
    public float getReceptorScaleY(int panel, double beat, double time) {
        return 1.0f;
    }

    /**
     * Gets the receptor rotation in degrees with the origin in the center of the receptor texture.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor rotation in degrees.
     */
    public float getReceptorRotation(int panel, double beat, double time) {
        switch(panel) {
            case NotePanel.RIGHT:
                return 90.0f;
            case NotePanel.UP:
                return 180.0f;
            case NotePanel.LEFT:
                return 270.0f;
        }
        return 0.0f;
    }

    /**
     * Gets the receptor opacity, a value from 0.0f to 1.0f (inclusive).
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor opacity. 0.0f if the note is invisible, 1.0f if the note is fully opaque.
     */
    public float getReceptorOpacity(int panel, double beat, double time) {
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
    public boolean isReceptorVisible(int panel, double beat, double time) {
        return true;
    }

    public float getOutlineX(int panel, double beat, double time) {
        return getReceptorX(panel, beat, time);
    }

    public float getOutlineY(int panel, double beat, double time) {
        return getReceptorY(panel, beat, time);
    }

    public float getOutlineScaleX(int panel, double beat, double time) {
        float scaleX = getReceptorScaleX(panel, beat, time);
        float scaleFactor = 0.35f;
        PanelState state = getRound().getPanelState();
        if(!state.isPressedAt(panel, time)) {
            double fadeTime = 0.250D; //250ms
            double lastTimeReleased = state.getLastTimeReleasedAt(panel, time);
            double progress = MathUtils.clamp(0.0D, 1.0D, Math.abs((time - lastTimeReleased) / fadeTime));
            return scaleX + (float) (progress * scaleFactor * scaleX);
        }
        return scaleX;
    }

    public float getOutlineScaleY(int panel, double beat, double time) {
        float scaleY = getReceptorScaleY(panel, beat, time);
        float scaleFactor = 0.35f;
        PanelState state = getRound().getPanelState();
        if(!state.isPressedAt(panel, time)) {
            double fadeTime = 0.250D; //250ms
            double lastTimeReleased = state.getLastTimeReleasedAt(panel, time);
            double progress = MathUtils.clamp(0.0D, 1.0D, Math.abs((time - lastTimeReleased) / fadeTime));
            return scaleY + (float) (progress * scaleFactor* scaleY);
        }
        return scaleY;
    }

    public float getOutlineRotation(int panel, double beat, double time) {
        return getReceptorRotation(panel, beat, time);
    }

    public float getOutlineOpacity(int panel, double beat, double time) {
        PanelState state = getRound().getPanelState();
        if(!state.isPressedAt(panel, time)) {
            double fadeTime = 0.250D; //250ms
            double lastTimeReleased = state.getLastTimeReleasedAt(panel, time);
            double progress = MathUtils.clamp(0.0D, 1.0D, Math.abs((time - lastTimeReleased) / fadeTime));
            return (float) (1.0D - progress);
        }
        return 1.0f;
    }

    public boolean isOutlineVisible(int panel, double beat, double time) {
        PanelState state = getRound().getPanelState();
        if(!state.isPressedAt(panel, time)) {
            double fadeTime = 0.250D; //250ms
            double lastTimeReleased = state.getLastTimeReleasedAt(panel, time);
            return Math.abs(time - lastTimeReleased) < fadeTime;
        }
        return true;
    }

    /**
     * Gets the receptor drawable.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor drawable.
     */
    public Drawable getReceptorDrawable(int panel, double beat, double time) {
        return receptorDrawable != null ? receptorDrawable.get() : null;
    }

    /**
     * Gets the receptor outline drawable.
     * @param panel the note panel.
     * @param beat the current beat.
     * @param time the current time relative to the start of the music track.
     * @return the receptor outline drawable.
     */
    public Drawable getOutlineDrawable(int panel, double beat, double time) {
        return outlineDrawable != null ? outlineDrawable.get() : null;
    }

    /**
     * Gets the round currently being played.
     * @return the current round.
     */
    public Round getRound() {
        return view.getRound();
    }
}
