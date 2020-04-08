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

package net.sync.game.resource.xml2.parsers.actors;

import net.sync.game.resource.xml2.parsers.XmlLayoutParser;
import net.sync.game.ui.widgets.ScrollPane;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

public class XmlScrollPaneParser extends XmlWidgetGroupParser<ScrollPane> {
    public XmlScrollPaneParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected boolean parseAttribute(ScrollPane pane, String name, String value) throws XmlParseException {
        if(super.parseAttribute(pane, name, value)) {
            //Attribute already parsed
            return true;
        }

        switch(name) {
            case "cancelTouchFocus": pane.setCancelTouchFocus(booleanResolver().resolve(value));                break;
            case "clamp": pane.setClamp(booleanResolver().resolve(value));                                      break;
            case "fadeScrollBars": pane.setFadeScrollBars(booleanResolver().resolve(value));                    break;
            case "flickScroll": pane.setFlickScroll(booleanResolver().resolve(value));                          break;
            case "flickScrollTapSquareSize": pane.setFlickScrollTapSquareSize(floatResolver().resolve(value));  break;
            case "flingTime": pane.setFlingTime(floatResolver().resolve(value));                                break;
            case "forceScrollX": pane.setForceScroll(booleanResolver().resolve(value), pane.isForceScrollY());  break;
            case "forceScrollY": pane.setForceScroll(pane.isForceScrollX(), booleanResolver().resolve(value));  break;
            case "overScrollX": pane.setOverscroll(booleanResolver().resolve(value), pane.isOverscrollY());     break;
            case "overScrollY": pane.setOverscroll(pane.isOverscrollX(), booleanResolver().resolve(value));     break;
            case "hScrollBarOnBottom": pane.setScrollBarPositions(
                    booleanResolver().resolve(value), pane.isVerticalScrollBarOnRight());                       break;
            case "vScrollBarOnRight": pane.setScrollBarPositions(
                    pane.isHorizontalScrollBarOnBottom(), booleanResolver().resolve(value));                    break;
            case "scrollBarsOnTop": pane.setScrollbarsOnTop(booleanResolver().resolve(value));                  break;
            case "scrollDisabledX": pane.setScrollingDisabled(
                    booleanResolver().resolve(value), pane.isScrollingDisabledY());                             break;
            case "scrollDisabledY": pane.setScrollingDisabled(
                    pane.isScrollingDisabledX(), booleanResolver().resolve(value));                             break;
            case "scrollPercentX": pane.setScrollPercentX(floatResolver().resolve(value));                      break;
            case "scrollPercentY": pane.setScrollPercentY(floatResolver().resolve(value));                      break;
            case "smoothScrolling": pane.setSmoothScrolling(booleanResolver().resolve(value));                  break;
            case "scrollBarsFadeAlphaSeconds": pane.setupFadeScrollBars(
                    floatResolver().resolve(value), pane.getFadeDelaySeconds());                                break;
            case "scrollBarsFadeDelaySeconds": pane.setupFadeScrollBars(
                    pane.getFadeAlphaSeconds(), floatResolver().resolve(value));                                break;
            case "variableSizeKnobs": pane.setVariableSizeKnobs(booleanResolver().resolve(value));              break;
            case "velocityX": pane.setVelocityX(floatResolver().resolve(value));                                break;
            case "velocityY": pane.setVelocityY(floatResolver().resolve(value));                                break;
            case "overScrollDistance": pane.setupOverscroll(
                    dimensionResolver().resolve(value).getValue(),
                    pane.getOverScrollSpeedMin(),
                    pane.getOverScrollSpeedMax());                                                              break;
            case "overScrollSpeedMin": pane.setupOverscroll(
                    pane.getOverScrollDistance(),
                    floatResolver().resolve(value),
                    pane.getOverScrollSpeedMax());                                                              break;
            case "overScrollSpeedMax": pane.setupOverscroll(
                    pane.getOverScrollDistance(),
                    pane.getOverScrollSpeedMin(),
                    floatResolver().resolve(value));                                                            break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    @Override
    protected void parseChildren(ScrollPane pane, XmlParser.Element element) throws XmlParseException {
        if(element.getChildCount() != 1) {
            throw new XmlParseException("ScrollPane can only have one child!");
        }

        //Parse and set the scroll pane child.
        XmlParser.Element child = element.getChild(0);
        XmlActorParser<?> parser = getLayoutParser().getActorElementParser(child.getName());
        pane.setActor(parser.parse(child));
    }

    @Override
    protected ScrollPane createActor() {
        return new ScrollPane();
    }
}
