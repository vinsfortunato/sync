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

package net.sync.game.util.xml;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;

import java.util.Collections;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of {@link XmlElement} that wraps a {@link XmlReader.Element} from libGDX.
 */
public class GdxXmlElement implements XmlElement {
    private XmlReader.Element element;

    public GdxXmlElement(XmlReader.Element element) {
        this.element = checkNotNull(element);
    }

    @Override
    public XmlElement getParent() {
        XmlReader.Element parent = element.getParent();
        return parent != null ? new GdxXmlElement(parent) : null;
    }

    @Override
    public String getName() {
        return element.getName();
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public Iterable<XmlElement> getChildren() {
        return () -> new Iterator<XmlElement>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < element.getChildCount();
            }

            @Override
            public XmlElement next() {
                return new GdxXmlElement(element.getChild(index++));
            }
        };
    }

    @Override
    public Iterable<XmlElement> getChildren(String name) {
        final Iterator<XmlReader.Element> it = element.getChildrenByName(name).iterator();
        return () -> new Iterator<XmlElement> () {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public XmlElement next() {
                return new GdxXmlElement(it.next());
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    @Override
    public XmlElement getChild(int index) {
        XmlReader.Element child = element.getChild(index);
        return child != null ? new GdxXmlElement(child) : null;
    }

    @Override
    public int getChildCount() {
        return element.getChildCount();
    }

    @Override
    public Iterable<String> getAttributeNames() {
        ObjectMap<String, String> attributes = element.getAttributes();
        return attributes != null ? attributes.keys() : Collections::emptyIterator;
    }

    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    @Override
    public String getAttribute(String name, String defaultValue) {
        return element.getAttribute(name, defaultValue);
    }

    @Override
    public int getAttributeCount() {
        ObjectMap<String, String> attributes = element.getAttributes();
        return attributes != null ? attributes.size : 0;
    }

    @Override
    public boolean hasAttribute(String name) {
        return element.hasAttribute(name);
    }
}
