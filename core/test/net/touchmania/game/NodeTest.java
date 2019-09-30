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

package net.touchmania.game;

import net.touchmania.game.util.Node;

public class NodeTest {

    public static void main(String[]args) {
        Node<String> root = new Node<>();

        Node<String> node1 = new Node<>("1");
        Node<String> node2 = new Node<>("2");
        Node<String> node3 = new Node<>("3");
        Node<String> node4 = new Node<>("4");
        Node<String> node5 = new Node<>("5");
        Node<String> node6 = new Node<>("6");
        Node<String> node7 = new Node<>("7");
        Node<String> node8 = new Node<>("8");
        Node<String> node9 = new Node<>("9");
        Node<String> node10 = new Node<>("10");

        root.addChild(node1);
        root.addChild(node2);
        root.addChild(node3);

        node1.addChild(node4);

        node4.addChild(node5);
        node4.addChild(node6);
        node4.addChild(node7);

        node2.addChild(node8);
        node3.addChild(node9);
        node3.addChild(node10);

        node6.addChild(node4);

        printChildren(root);
        printChildren(node4);
        printChildren(node2);
        printChildren(node3);
    }

    private static void printChildren(Node<String> node) {
        for(Node<String> child : node.getChildren()) {
            System.out.print(child.getValue());
            System.out.print(" ");
        }
        System.out.println();
    }
}
