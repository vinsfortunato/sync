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

package net.touchmania.game.util;

import com.badlogic.gdx.utils.ObjectSet;
import com.google.common.base.Preconditions;

public class Node<T> {
    private ObjectSet<Node<T>> children;
    private Node<T> parent;
    private T value;

    public Node() {
        this(null, null);
    }

    public Node(T value) {
        this(null, value);
    }

    public Node(Node<T> parent) {
        this(parent, null);
    }

    public Node(Node<T> parent, T value) {
        setValue(value);
        if(parent != null) {
            parent.addChild(this);
        }
    }

    /**
     * Adds the given node as a child. If the node already has a parent removes it from its parent.
     * @param node the node to add as a child.
     * @return true if the node wasn't already in the children set, false otherwise.
     */
    public boolean addChild(Node<T> node) {
        Preconditions.checkArgument(node != this, "Cannot set itself as child!");
        if(node.getParent() == this) {
            return false;
        }
        Preconditions.checkArgument(!hasAncestor(node), "Cannot add an ancestor as child!");
        Node<T> prevParent = node.setParent(this);
        if(prevParent != null) {
            prevParent.removeChild(node);
        }
        if(children == null) {
            children = new ObjectSet<>();
        }
        return children.add(node);
    }

    /**
     * Removes the given node from the children set.
     * @param node the node to remove from children.
     * @return true if the node has been removed, false if the node wasn't in the children set.
     */
    public boolean removeChild(Node<T> node) {
        if(children != null && children.remove(node)) {
            if(children.size == 0) {
                children = null;
            }
            if(node.getParent() == this) {
                node.setParent(null);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes this node from its parent.
     * @return true if the child has been removed from its parent, false if the node had no parent.
     */
    public boolean removeParent() {
        return parent != null && parent.removeChild(this);
    }

    /**
     * Sets the given node as parent.
     * @param node the node to set as parent.
     * @return previous parent.
     */
    private Node<T> setParent(Node<T> node) {
        Node<T> prev = parent;
        parent = node;
        return prev;
    }

    /**
     * Checks if this node has the given ancestor.
     * @param ancestor the ancestor.
     * @return true if the node has the given ancestor, false otherwise.
     */
    public boolean hasAncestor(Node<T> ancestor) {
        if(ancestor == this || parent == null) {
            return false;
        }
        if(parent == ancestor) {
            return true;
        }
        return parent.hasAncestor(ancestor);
    }

    /**
     * Sets node value.
     * @param value the value, can be null.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Gets node value.
     * @return the node value. Can be null.
     */
    public T getValue() {
        return value;
    }

    /**
     * Checks if the node is a root.
     * @return true if the node has no parent.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Checks if the node is a leaf.
     * @return true if the node has no children.
     */
    public boolean isLeaf() {
        return children == null || children.size == 0;
    }

    /**
     * Gets the node parent.
     * @return the node parent or null if the node has no parent (root).
     */
    public Node<T> getParent() {
        return parent;
    }

    /**
     * Gets node's children.
     * @return a set containing all the node children.
     */
    public ObjectSet<Node<T>> getChildren() {
        return children;
    }
}
