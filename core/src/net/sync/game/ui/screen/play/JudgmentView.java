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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.Game;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.round.Round;
import net.sync.game.round.judge.Judgment;
import net.sync.game.round.judge.JudgmentClass;
import net.sync.game.round.judge.TailJudgment;
import net.sync.game.round.judge.TapJudgment;

public class JudgmentView extends Widget {
    private Round round;

    private Resource<Drawable> marvelousDrawable;
    private Resource<Drawable> perfectDrawable;
    private Resource<Drawable> greatDrawable;
    private Resource<Drawable> goodDrawable;
    private Resource<Drawable> booDrawable;
    private Resource<Drawable> missDrawable;
    private Resource<Drawable> okDrawable;
    private Resource<Drawable> ngDrawable;

    public JudgmentView(Round round) {
        super();
        this.round = round;

        //Init resources
        ResourceProvider resources = Game.instance().getResources();
        (marvelousDrawable = resources.getDrawable("play_dance_judgment_marvelous")).load();
        (perfectDrawable = resources.getDrawable("play_dance_judgment_perfect")).load();
        (greatDrawable = resources.getDrawable("play_dance_judgment_great")).load();
        (goodDrawable = resources.getDrawable("play_dance_judgment_good")).load();
        (booDrawable = resources.getDrawable("play_dance_judgment_boo")).load();
        (missDrawable = resources.getDrawable("play_dance_judgment_miss")).load();
        (okDrawable = resources.getDrawable("play_dance_judgment_ok")).load();
        (ngDrawable = resources.getDrawable("play_dance_judgment_ng")).load();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //Draw judgment
        Judgment lastJudgment = round.getJudge().getLastJudgment();

        JudgmentClass c = null;
        if(lastJudgment instanceof TapJudgment) {
            TapJudgment judgment = (TapJudgment) lastJudgment;
            c = judgment.getJudgmentClass();
        } else if(lastJudgment instanceof TailJudgment) {
            TailJudgment judgment = (TailJudgment) lastJudgment;
            c = judgment.getJudgmentClass();
        }

        if(c != null) {
            Drawable drawable = getJudgmentDrawable(c);
            if(drawable == null) return; //TODO

            //Set opacity
            Color color = new Color(batch.getColor());
            color.a = 1.0f;
            batch.setColor(color);

            //Draw
            float x = getWidth() / 2 - drawable.getMinWidth() / 2;
            float y = getHeight() / 2 - drawable.getMinHeight() / 2;
            float w = drawable.getMinWidth();
            float h = drawable.getMinHeight();
            drawable.draw(batch, x, y, w, h);
        }
    }

    private Drawable getJudgmentDrawable(JudgmentClass judgmentClass) {
        switch(judgmentClass) {
            case MARVELOUS: return marvelousDrawable.get();
            case PERFECT:   return perfectDrawable.get();
            case GREAT:     return greatDrawable.get();
            case GOOD:      return goodDrawable.get();
            case BOO:       return booDrawable.get();
            case MISS:      return missDrawable.get();
            case OK:        return okDrawable.get();
            case NG:        return ngDrawable.get();
        }
        return null;
    }
}
