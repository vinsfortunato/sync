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

import net.touchmania.game.util.math.Graph2D;
import net.touchmania.game.util.math.Graph2DPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author Vincenzo Fortunato
 */
public class GraphVisualizer extends JFrame {
    private Graph2D graph;
    private float zoomX = 10.0f;
    private float zoomY = 10.0f;
    private boolean zoomModeX = false;
    private double originXScale = 0.5F;
    private double originYScale = 0.5F;
    private int mousePosX = 0;
    private int mousePosY = 0;

    public GraphVisualizer(Graph2D graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(1400, 800));
        setBackground(Color.BLACK);
        setContentPane(new GraphPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosX = e.getX();
                mousePosY = e.getY();
                repaint();
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("+")) {
                    if(zoomModeX) {
                        zoomX += 0.5F;
                    } else {
                        zoomY += 0.5F;
                    }
                    repaint();
                }
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("-")) {
                    if(zoomModeX) {
                        zoomX -= 0.5F;
                    } else {
                        zoomY -= 0.5F;
                    }
                    repaint();
                }
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("m")) {
                    zoomModeX = !zoomModeX;
                    repaint();
                }
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("a")) {
                    originXScale -= 0.02F;
                    repaint();
                }
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("d")) {
                    originXScale += 0.02F;
                    repaint();
                }
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("w")) {
                    originYScale -= 0.02F;
                    repaint();
                }
                if(String.valueOf(e.getKeyChar()).equalsIgnoreCase("s")) {
                    originYScale += 0.02F;
                    repaint();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        pack();

        setVisible(true);
    }
    public static void showGraph(final Graph2D graph) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphVisualizer(graph);
            }
        });
    }

    private class GraphPanel extends JPanel {

        public GraphPanel() {
            setBackground(Color.BLACK);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);

            //Draw axis
            int originX = (int) ((double) getWidth() * originXScale);
            int originY = (int) ((double) getHeight() * originYScale);
            g2.drawLine(0, originY, getWidth(), originY);
            g2.drawLine(originX, 0, originX, getHeight());

            double graphMouseX = (mousePosX - originX) / zoomX;
            double graphMouseY = graph.f(graphMouseX);

            //Draw info
            g2.drawString(String.format(Locale.ENGLISH, "[%.3f | %.3f]", graphMouseX, graphMouseY), mousePosX, mousePosY);
            g2.drawString("Zoom with key +/-. Change zoom mode with key M. Current zoom mode = " + (zoomModeX ? "x" : "y"), 0, 20);

            Collection<Graph2DPoint> points = graph.getPoints();

            g2.translate(originX, originY);

            //Draw graph
            Iterator<Graph2DPoint> it = points.iterator();
            Graph2DPoint leftPoint = null, rightPoint = null;
            while(it.hasNext()) {
                rightPoint = it.next();

                if(leftPoint != null) {
                    Double leftX = leftPoint.x;
                    Double rightX = rightPoint.x;
                    Double leftY = leftPoint.y + leftPoint.jump;
                    Double rightY = rightPoint.y;

                    leftX = leftX * zoomX;
                    rightX = rightX * zoomX;
                    leftY = -leftY * zoomY;
                    rightY = -rightY * zoomY;

                    g2.drawLine((int) (double) leftX, (int) (double)  leftY, (int) (double)  rightX, (int) (double)  rightY);
                }

                leftPoint = rightPoint;
            }

            //Draw selection box
            graphMouseX *= zoomX;
            graphMouseY = -graphMouseY * zoomY;
            g2.drawRect((int) graphMouseX - 2,(int) graphMouseY - 2, 4, 4);
        }
    }
}
