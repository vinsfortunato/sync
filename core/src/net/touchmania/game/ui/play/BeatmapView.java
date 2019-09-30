package net.touchmania.game.ui.play;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import net.touchmania.game.round.Round;

/**
 * Renders beatmap notes and receptors.
 */
public class BeatmapView extends Widget {

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

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
    }

    public Round getRound() {
        return null; //TODO
    }
}
