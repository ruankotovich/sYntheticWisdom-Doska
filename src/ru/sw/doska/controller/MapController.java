/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dmitry
 */
public class MapController {

    private Point movementPointer = null;
    private boolean detectTrigger = false;
    private JoystickController controllers[] = null;
    private final JLabel map, minimap;
    private final JPanel minimapContainer;
    private final JScrollPane scroll;
    private Thread currentThread = null;
    private double horMinimapRatio, verMinimaoRatio;

    public synchronized Point getMovementPointer() {
        return movementPointer;
    }

    public synchronized void setMovementPointer(Point movementPointer) {
        this.movementPointer = movementPointer;
    }

    public synchronized void setDetectTrigger(boolean detectTrigger) {
        this.detectTrigger = detectTrigger;
    }

    public synchronized boolean triggerIsOn() {
        return detectTrigger;
    }

    private void implementJoystick(JoystickController controller) {
        MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!triggerIsOn()) {
                    setDetectTrigger(true);
                    threadFactory(controller);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setDetectTrigger(false);
                if (currentThread != null) {
                    try {
                        currentThread.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MapController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        controller.getComponent().addMouseListener(mouseListener);
        controller.getComponent().addMouseMotionListener(mouseMotionListener);
    }

    private void threadFactory(JoystickController controller) {

        if (currentThread != null) {
            currentThread.interrupt();
        }

        currentThread = new Thread(() -> {
            while (triggerIsOn()) {
                try {
                    controller.getAction().action();
                    this.minimapContainer.setLocation((int) (scroll.getHorizontalScrollBar().getValue() * horMinimapRatio), (int) (scroll.getVerticalScrollBar().getValue() * verMinimaoRatio));
                    Thread.sleep(10);
                } catch (Exception ex) {
                }
            }
        });
        currentThread.start();
    }

    MouseListener mapMouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getPoint());
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    public MapController(JoystickController controllers[], JLabel map, JLabel minimap, JPanel minimapContainer, JScrollPane scroll) {
        this.controllers = controllers;

        this.map = map;
        this.minimap = minimap;
        this.map.addMouseListener(mapMouseListener);

        this.minimapContainer = minimapContainer;
        this.horMinimapRatio = minimap.getBounds().getWidth() / map.getBounds().getWidth();
        this.verMinimaoRatio = minimap.getBounds().getHeight() / map.getBounds().getHeight();

        this.scroll = scroll;

        for (JoystickController controller : controllers) {
            implementJoystick(controller);
        }
    }
}
