/*
 * Copyright 2019 Vincenzo Fortunato
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

package net.touchmania.game.ui.screen.play;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.touchmania.game.Game;
import net.touchmania.game.resource.ResourceProvider;
import net.touchmania.game.resource.lazy.Resource;
import net.touchmania.game.round.Round;
import net.touchmania.game.round.judge.Judgment;
import net.touchmania.game.round.judge.JudgmentClass;
import net.touchmania.game.round.judge.TapJudgement;

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
        Judgment lastJudgment = round.getJudge().getJudgmentKeeper().getLastJudgment();
        if(lastJudgment instanceof TapJudgement) {
            TapJudgement judgment = (TapJudgement) lastJudgment;

            Drawable drawable = getJudgmentDrawable( judgment.getJudgmentClass());
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
