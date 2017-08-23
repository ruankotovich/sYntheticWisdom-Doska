/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ru.sw.doska.model.Balloon;

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
    private final double horMinimapRatio;
    private final double verMinimaoRatio;
    private static final int ABSTRACT_PIXEL_SIZE = 10;
    private final TreeMap<Integer, TreeMap<Integer, Object>> infosMap;

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
            int inferedHor = e.getPoint().x / ABSTRACT_PIXEL_SIZE;
            int inferedVer = e.getPoint().y / ABSTRACT_PIXEL_SIZE;

            if (e.getButton() == MouseEvent.BUTTON3) {
                Object newInfo = JOptionPane.showInputDialog("Que informação você quer inserir aqui?");
                putInfoOnMap(inferedHor, inferedVer, newInfo);
            } else {
                System.out.println("Searching info at [" + inferedHor + "," + inferedVer + "]");
                Object info = getInfoOnMap(inferedHor, inferedVer);

                for (Component comp : map.getComponents()) {
                    comp.setVisible(false);
                }

                map.removeAll();
                System.out.println(info);
                if (info != null) {

                    boolean horOffset_Container = (e.getPoint().x - scroll.getHorizontalScrollBar().getValue()) > (scroll.getWidth() / 2);
                    boolean verOffset_Container = (e.getPoint().y - scroll.getVerticalScrollBar().getValue()) > (scroll.getHeight() / 2);

                    if (horOffset_Container) {
                        if (verOffset_Container) {
                            map.add(new Balloon(Balloon.BalloonType.BOTTOM_RIGHT, e.getPoint(), info.toString()).getBalloon());
                        } else {
                            map.add(new Balloon(Balloon.BalloonType.TOP_RIGHT, e.getPoint(), info.toString()).getBalloon());
                        }
                    } else {
                        if (verOffset_Container) {
                            map.add(new Balloon(Balloon.BalloonType.BOTTOM_LEFT, e.getPoint(), info.toString()).getBalloon());
                        } else {
                            map.add(new Balloon(Balloon.BalloonType.TOP_LEFT, e.getPoint(), info.toString()).getBalloon());
                        }

                    }
                }
            }
            map.repaint();
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

    public void putInfoOnMap(int x, int y, Object info) {
        TreeMap<Integer, Object> secondLevelMap = this.infosMap.get(x);

        if (secondLevelMap == null) {
            this.infosMap.put(x, new TreeMap<>());
            secondLevelMap = this.infosMap.get(x);
        }
        System.out.println("A info was added on [" + x + "," + y + "]");
        secondLevelMap.put(y, info);

    }

    public Object getInfoOnMap(int x, int y) {
        TreeMap<Integer, Object> secondLevelMap = this.infosMap.get(x);

        if (secondLevelMap != null) {
            return secondLevelMap.get(y);
        }

        return null;
    }

    public MapController(JoystickController controllers[], JLabel map, JLabel minimap, JPanel minimapContainer, JScrollPane scroll) {
        this.controllers = controllers;

        this.map = map;
        this.minimap = minimap;
        this.map.addMouseListener(mapMouseListener);

        this.minimapContainer = minimapContainer;
        this.horMinimapRatio = minimap.getBounds().getWidth() / map.getBounds().getWidth();
        this.verMinimaoRatio = minimap.getBounds().getHeight() / map.getBounds().getHeight();

        this.scroll = scroll;

        this.infosMap = new TreeMap<>();

        for (JoystickController controller : controllers) {
            implementJoystick(controller);
        }
    }
}
