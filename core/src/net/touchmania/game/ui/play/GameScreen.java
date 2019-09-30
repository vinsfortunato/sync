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

package net.touchmania.game.ui.play;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.touchmania.game.match.Match;
import net.touchmania.game.util.concurrent.DoneListener;
import net.touchmania.game.util.ui.LayoutScreen;

/**
 * @author flood2d
 */
public class GameScreen extends LayoutScreen{
    private BeatmapView notes;
    private Image background;
    private GameControls controls;

    private Match match;

    public GameScreen(String layoutId) {
        super(layoutId);
    }

    /**
    public GameScreen(net.touchmania.game.Game game, net.touchmania.game.match.Match match) {
       // super(game);
        this.match = match;
    }

     **/
    @Override
    public void prepare() {
        //loadAsset("texture/blue_background.jpg", Texture.class);
       // loadAsset(net.touchmania.game.Const.ATLAS_GAME, TextureAtlas.class);
    }

    @Override
    public void show(Stage stage) {
        /**
        Stage stage = getStage();
        Gdx.input.setInputProcessor(stage);

        notes = new BeatmapView(this);
        background = new Image(new TextureRegion(
                getAssets().get("texture/blue_background.jpg", Texture.class)));
        controls = new GameControls(this);

        this.match.getControls().addListener(notes.getState());
        this.match.getScoreKeeper().addListener(notes.getState());

        background.setDebug(true);
        notes.setDebug(true);
        controls.setDebug(true);

        Container<GameControls> controlContainer = new Container<GameControls>(controls);
        controlContainer.center();

        Table table = new Table();
        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.addActor(background);
        stack.addActor(notes);
        stack.add(controlContainer);
        table.addActor(stack);
        table.setFillParent(true);
        getStage().addActor(table);
         **/
    }


    @Override
    public void hide(DoneListener listener) {

    }

    public TextureAtlas getTextureAtlas() {
       // return getAssets().get(net.touchmania.game.Const.ATLAS_GAME, TextureAtlas.class);
        return null;
    }

    public Match getMatch() {
        return match;
    }
}
