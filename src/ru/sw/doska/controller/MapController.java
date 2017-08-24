/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ru.sw.doska.model.Balloon;
import ru.sw.doska.model.QueryEngine;

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
    private static final int ABSTRACT_PIXEL_SIZE = 8;
    private final QueryEngine qEngine = new QueryEngine(new File("Knowledge.sw"));
    private final TreeMap<Integer, TreeMap<Integer, Object>> infosMap;
    private BufferedImage mapImageBuffer;
    private PrintWriter mapImageWriter;

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

    public void translateMap() {
        scroll.getHorizontalScrollBar().setValue(
                (int) (this.minimapContainer.getLocation().getX() / horMinimapRatio)
        );

        scroll.getVerticalScrollBar().setValue(
                (int) (this.minimapContainer.getLocation().getY() / verMinimaoRatio)
        );
//        this.minimapContainer.setLocation((int) (scroll.getHorizontalScrollBar().getValue() * horMinimapRatio), (int) (scroll.getVerticalScrollBar().getValue() * verMinimaoRatio));
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

            if (e.getButton() == MouseEvent.BUTTON3 && e.isControlDown()) {
                Object newInfo = JOptionPane.showInputDialog("Inline Information");
                putInfoOnMap(inferedHor, inferedVer, newInfo);
                MapController.this.qEngine.addKnowledge("internal_MAP_LOCATION('" + newInfo.toString() + "'," + inferedHor + "," + inferedVer + ").");
            } else if (e.getButton() == MouseEvent.BUTTON3 && e.isShiftDown()) {
                String state = JOptionPane.showInputDialog("Inline State Name");
                Color pixColor = new Color(mapImageBuffer.getRGB(e.getX(), e.getY()));
                String color = String.format("#%02x%02x%02x", pixColor.getRed(), pixColor.getGreen(), pixColor.getBlue());
                mapImageWriter.println("internal_STATE_COLOR('" + state + "','" + color + "').");
                mapImageWriter.flush();
            } else {
                Object info = getInfoOnMap(inferedHor, inferedVer);

                for (Component comp : map.getComponents()) {
                    comp.setVisible(false);
                }

                map.removeAll();
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

    private void initInternalMapLocation() {
        //putInfoOnMap(inferedHor, inferedVer, newInfo);
        TreeMap<Integer, TreeMap<String, String>> queryResults = this.qEngine.consult("internal_MAP_LOCATION(M, X, Y).");

        if (queryResults.get(0) != null) {
            return;
        }

        for (Iterator<Map.Entry<Integer, TreeMap<String, String>>> it = queryResults.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, TreeMap<String, String>> result = it.next();
            putInfoOnMap(Integer.parseInt(result.getValue().get("X")), Integer.parseInt(result.getValue().get("Y")), result.getValue().get("M").replace("'", ""));
        }
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

        try {
            this.mapImageBuffer = ImageIO.read(this.getClass().getResource("/ru/sw/doska/gfx/brasil-politico.png"));
            this.mapImageWriter = new PrintWriter(new File("~colormap.out"));
        } catch (IOException ex) {
            System.err.println("Cannot parse map image");
            Logger.getLogger(MapController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (JoystickController controller : controllers) {
            implementJoystick(controller);
        }

        initInternalMapLocation();
    }
}
