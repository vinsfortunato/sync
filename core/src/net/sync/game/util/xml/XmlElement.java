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

/**
 * An XML element.
 */
public interface XmlElement {

    /**
     * Gets the element's parent.
     * @return the element's parent or null if the element has no parent.
     */
    XmlElement getParent();

    /**
     * Gets the element's name.
     * @return the element's name.
     */
    String getName();

    /**
     * Gets the element's text.
     * @return the element's text.
     */
    String getText();

    /**
     * Gets the iterable containing element's children.
     * @return the element's children iterable.
     */
    Iterable<XmlElement> getChildren();

    /**
     * Gets the iterable containing element's children with the given name.
     * @param name the element name.
     * @return a iterable containing element's children with the given name.
     */
    Iterable<XmlElement> getChildren(String name);

    /**
     * Gets the child element at the given index.
     * @param index the child element index.
     * @return the child element at the given index, or null if there is no element at the given index.
     */
    XmlElement getChild(int index);

    /**
     * Gets the count of element's children.
     * @return the count of children.
     */
    int getChildCount();

    /**
     * Gets a iterable containing all element's attribute names.
     * @return the iterable with element's attribute names.
     */
    Iterable<String> getAttributeNames();

    /**
     * Gets the value of the attribute with the given name.
     * @param name the attribute name.
     * @return the attribute value or null if there is no attribute with the given name.
     */
    String getAttribute(String name);

    /**
     * Gets the value of the attribute with the given name. If the
     * attribute is not found returns the given default value.
     * @param name the attribute name.
     * @param defaultValue the default value to return if the attribute is not found.
     * @return the attribute value or the default value if there is no attribute with the given name.
     */
    String getAttribute(String name, String defaultValue);

    /**
     * Gets the count of element's attributes.
     * @return the count of attributes.
     */
    int getAttributeCount();

    /**
     * Checks if the element has an attribute with the given name.
     * @param name the attribute name.
     * @return true if the element has the attribute, false otherwise.
     */
    boolean hasAttribute(String name);
}
