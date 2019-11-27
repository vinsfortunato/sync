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

package net.touchmania.game.resource.xml.parsers.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import net.touchmania.game.resource.Style;
import net.touchmania.game.resource.xml.parsers.XmlLayoutParser;
import net.touchmania.game.resource.xml.resolvers.XmlAlignResolver;
import net.touchmania.game.util.xml.XmlParseException;
import net.touchmania.game.util.xml.XmlParser;

import static net.touchmania.game.resource.xml.resolvers.XmlAlignResolver.GLOBAL_ALIGN_RESOLVER;

public class XmlTableParser extends XmlWidgetGroupParser<Table> {
    public XmlTableParser(XmlLayoutParser layoutParser) {
        super(layoutParser);
    }

    @Override
    protected void parseChildren(Table table, XmlParser.Element element) throws XmlParseException {
        for (int i = 0; i < element.getChildCount(); i++) {
            if (i > 0) {
                //Go to the next row
                table.row();
            }
            parseRow(table, element.getChild(i));
        }
    }

    /**
     * Parses a table row from the given xml element.
     *
     * @param table the result table.
     * @param element the row xml element.
     * @throws XmlParseException if the cell cannot be parsed correctly.
     */
    protected void parseRow(Table table, XmlParser.Element element) throws XmlParseException {
        if (!element.getName().equals("Row")) {
            throw new XmlParseException(
                    String.format("Unexpected child element '%s'! Expected it to be named 'Row'!", element.getName()));
        }

        for (int i = 0; i < element.getChildCount(); i++) {
            parseCell(table.add(), element.getChild(i));
        }
    }

    /**
     * Parses a table cell from the given xml element.
     *
     * @param cell    the result cell.
     * @param element the cell xml element.
     * @throws XmlParseException if the cell cannot be parsed correctly.
     */
    protected void parseCell(Cell cell, XmlParser.Element element) throws XmlParseException {
        if (!element.getName().equals("Cell")) {
            throw new XmlParseException(
                    String.format("Unexpected child element '%s'! Expected it to be named 'Cell'!", element.getName()));
        }

        //Check data integrity
        if (element.getChildCount() > 1) {
            throw new XmlParseException("Table cell can only have one child!");
        }

        //Parse the Cell actor
        if (element.getChildCount() == 1) {
            XmlParser.Element child = element.getChild(0);
            XmlActorParser<?> parser = getLayoutParser().getActorElementParser(child.getName());
            cell.setActor(parser.parse(child));
        }

        //Parse the Cell style
        if (element.hasAttribute("style")) {
            /** TODO
            Style style = styleResolver().resolve(element.getAttribute("style"));
            for (String attributeName : style.getAttributeNames()) {
                //Don't parse overridden attributes
                if (!element.hasAttribute(attributeName)) {
                    parseCellAttributeOrThrow(cell, attributeName, style.getAttributeValue(attributeName));
                }
            }
             **/
        }

        //Parse the Cell attributes
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes().entries()) {
            String attributeName = attribute.key;
            //Ignore style attribute and parse the others
            if (!attributeName.equals("style")) {
                parseCellAttributeOrThrow(cell, attributeName, attribute.value);
            }
        }
    }

    @Override
    protected boolean parseAttribute(Table table, String name, String value) throws XmlParseException {
        if (super.parseAttribute(table, name, value)) {
            //Attribute already parsed
            return true;
        }

        switch (name) {
            case "align":      table.align(GLOBAL_ALIGN_RESOLVER.resolve(value));                               break;
            //case "background": table.background(drawableResolver().resolve(value));                             break; TODO
            case "padLeft":    table.padLeft(dimensionResolver().resolve(value).getValue());                    break;
            case "padRight":   table.padRight(dimensionResolver().resolve(value).getValue());                   break;
            case "padTop":     table.padTop(dimensionResolver().resolve(value).getValue());                     break;
            case "padBottom":  table.padBottom(dimensionResolver().resolve(value).getValue());                  break;
            case "pad":        table.pad(dimensionResolver().resolve(value).getValue());                        break;
            case "clip":       table.setClip(booleanResolver().resolve(value));                                 break;
            case "round":      table.setRound(booleanResolver().resolve(value));                                break;
            default: return false; //Unrecognised attribute
        }

        return true;
    }

    private void parseCellAttributeOrThrow(Cell cell, String name, String value) throws XmlParseException {
        if (!parseCellAttribute(cell, name, value)) {
            throw new XmlParseException(String.format("Unrecognised cell attribute with name '%s' and value '%s'!", name, value));
        }
    }

    /**
     * Parses the cell element's attribute with the given name and value.
     *
     * @param cell  the result cell.
     * @param name  the attribute name.
     * @param value the attribute value.
     * @return true if the attribute has been recognised and parsed, false if it has not been recognised.
     * @throws XmlParseException if the attribute has been recognised but cannot be parsed correctly.
     */
    protected boolean parseCellAttribute(Cell cell, String name, String value) throws XmlParseException {
        switch(name) {
            case "align":       cell.align(XmlAlignResolver.GLOBAL_ALIGN_RESOLVER.resolve(value));              break;
            case "colspan":     cell.colspan(integerResolver().resolve(value));                                 break;
            case "expandX":     if(booleanResolver().resolve(value)) cell.expandX();                            break;
            case "expandY":     if(booleanResolver().resolve(value)) cell.expandY();                            break;
            case "fillX":       if(booleanResolver().resolve(value)) cell.fillX();                              break;
            case "fillY":       if(booleanResolver().resolve(value)) cell.fillY();                              break;
            case "growX":       if(booleanResolver().resolve(value)) cell.growX();                              break;
            case "growY":       if(booleanResolver().resolve(value)) cell.growY();                              break;
            case "width":       cell.width(dimensionResolver().resolve(value).getValue());                      break;
            case "height":      cell.height(dimensionResolver().resolve(value).getValue());                     break;
            case "maxWidth":    cell.maxWidth(dimensionResolver().resolve(value).getValue());                   break;
            case "maxHeight":   cell.maxHeight(dimensionResolver().resolve(value).getValue());                  break;
            case "minWidth":    cell.minWidth(dimensionResolver().resolve(value).getValue());                   break;
            case "minHeight":   cell.minHeight(dimensionResolver().resolve(value).getValue());                  break;
            case "prefWidth":   cell.prefWidth(dimensionResolver().resolve(value).getValue());                  break;
            case "prefHeight":  cell.prefHeight(dimensionResolver().resolve(value).getValue());                 break;
            case "maxSize":     cell.maxSize(dimensionResolver().resolve(value).getValue());                    break;
            case "minSize":     cell.minSize(dimensionResolver().resolve(value).getValue());                    break;
            case "prefSize":    cell.prefSize(dimensionResolver().resolve(value).getValue());                   break;
            case "padLeft":     cell.padLeft(dimensionResolver().resolve(value).getValue());                    break;
            case "padRight":    cell.padRight(dimensionResolver().resolve(value).getValue());                   break;
            case "padTop":      cell.padTop(dimensionResolver().resolve(value).getValue());                     break;
            case "padBottom":   cell.padBottom(dimensionResolver().resolve(value).getValue());                  break;
            case "pad":         cell.pad(dimensionResolver().resolve(value).getValue());                        break;
            case "actorX":      cell.setActorX(dimensionResolver().resolve(value).getValue());                  break;
            case "actorY":      cell.setActorY(dimensionResolver().resolve(value).getValue());                  break;
            case "actorWidth":  cell.setActorWidth(dimensionResolver().resolve(value).getValue());              break;
            case "actorHeight": cell.setActorHeight(dimensionResolver().resolve(value).getValue());             break;
            case "size":        cell.size(dimensionResolver().resolve(value).getValue());                       break;
            case "space":       cell.space(dimensionResolver().resolve(value).getValue());                      break;
            case "spaceLeft":   cell.spaceLeft(dimensionResolver().resolve(value).getValue());                  break;
            case "spaceRight":  cell.spaceRight(dimensionResolver().resolve(value).getValue());                 break;
            case "spaceTop":    cell.spaceTop(dimensionResolver().resolve(value).getValue());                   break;
            case "spaceBottom": cell.spaceBottom(dimensionResolver().resolve(value).getValue());                break;
            case "uniformX":    cell.uniform(booleanResolver().resolve(value), cell.getUniformY());             break;
            case "uniformY":    cell.uniform(cell.getUniformX(), booleanResolver().resolve(value));             break;
            default: return false; //Unrecognised cell attribute
        }

        return true;
    }

    @Override
    protected Table createActor() {
        return new Table();
    }
}
