/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import ru.sw.doska.model.Balloon;
import ru.sw.doska.model.QueryEngine;
import ru.sw.doska.wnd.WNDMainWindow;

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
    private QueryEngine qEngine;
    private final TreeMap<Integer, TreeMap<Integer, Object>> infosMap;
    private BufferedImage mapImageBuffer;
    private PrintWriter mapImageWriter;
    private static final double M_PI = 3.1415926535897932384626433832795;
    private static final double EARTH_RADIUS_KM = 6371.0;
    private final List<String> citiesWithDistance;
    private final HyperlinkController hlController;
    private final WNDMainWindow caller;

    MouseAdapter mapMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int inferedHor = e.getPoint().x / ABSTRACT_PIXEL_SIZE;
            int inferedVer = e.getPoint().y / ABSTRACT_PIXEL_SIZE;

//            if (e.getButton() == MouseEvent.BUTTON3 && e.isControlDown()) {
//                Object newInfo = JOptionPane.showInputDialog("Inline Information");
//                putInfoOnMap(inferedHor, inferedVer, newInfo);
//                MapController.this.qEngine.addKnowledge("internal_MAP_LOCATION('" + newInfo.toString() + "'," + inferedHor + "," + inferedVer + ").");
//            } else {
            Object info = getInfoOnMap(inferedHor, inferedVer);

            if (info != null) {
                String cityName = info.toString();
                String citizenCalled = qEngine.consultFirst("internal_CITIZEN_CALLED('" + cityName + "', X).", "X");

                StringBuilder content = new StringBuilder();
                content.append("<center><b>").append(cityName.replace("'", "")).append("</b></b>");
                if (citizenCalled != null) {
                    content.append("<br>Os habitantes são chamados de <br>").append("<b><font color='blue'>").append(citizenCalled.replace("'", "")).append("</font></b></center>");
                    System.out.println(citizenCalled);
                }
                String linkRelf = e.getX() + "," + e.getY() + ";" + cityName;
                content.append("<br><center><a href=\"@city;").append(linkRelf).append("\">Clique para saber mais</a></center>");

                addBalloon(e.getPoint(), content.toString(), true);

                if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()) {
                    caller.jCBcity1.setSelectedItem(cityName);
                } else if (e.getButton() == MouseEvent.BUTTON3 && e.isShiftDown()) {
                    caller.jCBcity2.setSelectedItem(cityName);
                }

            } else {
                Color queryColor = new Color(mapImageBuffer.getRGB(e.getX(), e.getY()));
                String color = String.format("#%02x%02x%02x", queryColor.getRed(), queryColor.getGreen(), queryColor.getBlue());
                String response = qEngine.consultFirst("internal_STATE_COLOR(X,'" + color + "').", "X");
                String responseCapital = qEngine.consultFirst("internal_CAPITAL_OF(" + response + ",X).", "X");
                String stateClimate = qEngine.consultFirst("internal_CLIMATE_BY_STATE(" + response + ", X).", "X");
                String coreCities = qEngine.consultFirst("internal_CITIES_BY_STATE(" + response + ", X).", "X");
                if (response != null) {
                    StringBuilder content = new StringBuilder();
                    content.append("<center><b>").append(response.replace("'", "")).append("</b>")
                            .append("<br>Capital : ").append("<font color='red'>").append(responseCapital.replace("'", "")).append("</font>")
                            .append("<br>Clima : ").append("<font color='red'>").append(stateClimate.replace("'", "")).append("</font>")
                            .append("<br><br><a href=\"@state;").append(e.getX()).append(",").append(e.getY()).append(";").append(response.replace("'", "")).append("\"> Clique aqui para ver as principais cidades</a>")
                            .append("</center>");
                    addBalloon(e.getPoint(), content.toString(), true);
                }

            }
//            }
            map.repaint();
        }
    };

    private void implementJoystick(JoystickController controller) {
        MouseAdapter mouseListener = new MouseAdapter() {

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
    }

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

    private double deg2rad(double deg) {
        return (deg * M_PI / 180.0);
    }

    private double haversine_distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double lat1 = deg2rad(latitude1);
        double lon1 = deg2rad(longitude1);
        double lat2 = deg2rad(latitude2);
        double lon2 = deg2rad(longitude2);

        double d_lat = Math.abs(lat1 - lat2);
        double d_lon = Math.abs(lon1 - lon2);

        double a = Math.pow(Math.sin(d_lat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(d_lon / 2), 2);

        double d_sigma = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS_KM * d_sigma;
    }

    public double calculateDistanceBetween(String city1, String city2) {
        TreeMap<Integer, TreeMap<String, String>> resCity1 = qEngine
                .consult("internal_CITY_POINT('" + city1 + "',X,Y).");

        TreeMap<Integer, TreeMap<String, String>> resCity2 = qEngine
                .consult("internal_CITY_POINT('" + city2 + "',X,Y).");
        double lat1, lat2, lng1, lng2;

        if (resCity1.get(1) != null) {
            lat1 = Double.parseDouble(resCity1.get(1).get("X"));
            lng1 = Double.parseDouble(resCity1.get(1).get("Y"));
        } else {
            return -1.0;
        }

        if (resCity2.get(1) != null) {
            lat2 = Double.parseDouble(resCity2.get(1).get("X"));
            lng2 = Double.parseDouble(resCity2.get(1).get("Y"));
        } else {
            return -2.0;
        }

        return haversine_distance(lat1, lng1, lat2, lng2);
    }

    public void translateMap() {
        scroll.getHorizontalScrollBar().setValue(
                (int) (this.minimapContainer.getLocation().getX() / horMinimapRatio)
        );

        scroll.getVerticalScrollBar().setValue(
                (int) (this.minimapContainer.getLocation().getY() / verMinimaoRatio)
        );
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

    public void addBalloon(Point e, String information, boolean hideOthers) {
        boolean horOffset_Container = (e.x - scroll.getHorizontalScrollBar().getValue()) > (scroll.getWidth() / 2);
        boolean verOffset_Container = (e.y - scroll.getVerticalScrollBar().getValue()) > (scroll.getHeight() / 2);

        if (hideOthers) {
            for (Component comp : map.getComponents()) {
                comp.setVisible(false);
            }

            map.removeAll();
        }

        if (horOffset_Container) {
            if (verOffset_Container) {
                map.add(new Balloon(Balloon.BalloonType.BOTTOM_RIGHT, this.hlController, e, information).getBalloon());
            } else {
                map.add(new Balloon(Balloon.BalloonType.TOP_RIGHT, this.hlController, e, information).getBalloon());
            }
        } else {
            if (verOffset_Container) {
                map.add(new Balloon(Balloon.BalloonType.BOTTOM_LEFT, this.hlController, e, information).getBalloon());
            } else {
                map.add(new Balloon(Balloon.BalloonType.TOP_LEFT, this.hlController, e, information).getBalloon());
            }
        }
        map.repaint();
    }

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
        TreeMap<Integer, TreeMap<String, String>> queryResults = this.qEngine.consult("internal_MAP_LOCATION(M, X, Y).");

        if (queryResults.get(0) != null) {
            return;
        }

        for (Iterator<Map.Entry<Integer, TreeMap<String, String>>> it = queryResults.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, TreeMap<String, String>> result = it.next();
            putInfoOnMap(Integer.parseInt(result.getValue().get("X")), Integer.parseInt(result.getValue().get("Y")), result.getValue().get("M").replace("'", ""));
        }
    }

    protected void notifyListener(HyperlinkEvent hlEvent) {
        if (hlEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            String[] linkShelf = hlEvent.getDescription().split(";");
            if (linkShelf.length > 0) {
                switch (linkShelf[0]) {
                    case "@city": {
                        String furtherInfo = qEngine.consultFirst("internal_FURTHER_INFORMATIONS('" + linkShelf[2] + "',X).", "X");
                        String[] strPoint = linkShelf[1].split(",");
                        addBalloon(new Point(Integer.parseInt(strPoint[0]), Integer.parseInt(strPoint[1])), "<meta charset=\"UTF-8\"><center>" + furtherInfo.replace("'", "") + "</center>", true);
                    }
                    break;

                    case "@state": {
                        String furtherInfo = qEngine.consultFirst("internal_CITIES_BY_STATE('" + linkShelf[2] + "',X).", "X");
                        String[] strPoint = linkShelf[1].split(",");
                        addBalloon(new Point(Integer.parseInt(strPoint[0]), Integer.parseInt(strPoint[1])), "<meta charset=\"UTF-8\"><center>" + "<b>Principais cidades do estado " + linkShelf[2] + " :</b><br>" + furtherInfo.replace("'.'('", "").replace("',", " ,").replace(",[]", "").replace(")", "") + "</center>", true);
                    }
                    break;
                }
            }
        }
    }

    private void initCitiesWithDistance() {
        TreeMap<Integer, TreeMap<String, String>> cwDistance = qEngine.consult("internal_CITY_POINT(CITY, X, Y).");
        for (Map.Entry<Integer, TreeMap<String, String>> varMap : cwDistance.entrySet()) {
            this.citiesWithDistance.add(varMap.getValue().get("CITY").replace("'", ""));
        }
    }

    public List<String> getCitiesWithDistance() {
        return citiesWithDistance;
    }

    public MapController(WNDMainWindow toControl, QueryEngine engine, JoystickController controllers[], JLabel map, JLabel minimap, JPanel minimapContainer, JScrollPane scroll) {
        if (engine != null) {
            qEngine = engine;
        } else {
            qEngine = new QueryEngine(new File("Knowledge.sw"));
        }

        this.controllers = controllers;

        this.map = map;
        this.minimap = minimap;
        this.map.addMouseListener(mapMouseListener);

        this.caller = toControl;
        this.hlController = new HyperlinkController(this);
        this.minimapContainer = minimapContainer;
        this.horMinimapRatio = minimap.getBounds().getWidth() / map.getBounds().getWidth();
        this.verMinimaoRatio = minimap.getBounds().getHeight() / map.getBounds().getHeight();
        this.citiesWithDistance = new ArrayList<>();
        this.scroll = scroll;
        this.infosMap = new TreeMap<>();

        try {
            this.mapImageBuffer = ImageIO.read(this.getClass().getResource("/ru/sw/doska/gfx/brasil-politico-spectrum.png"));
            this.mapImageWriter = new PrintWriter(new File("~colormap.out"));
        } catch (IOException ex) {
            System.err.println("Cannot parse map image");
            Logger
                    .getLogger(MapController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        for (JoystickController controller : controllers) {
            implementJoystick(controller);
        }
        initInternalMapLocation();
        initCitiesWithDistance();
    }

    public QueryEngine getEngine() {
        return qEngine;
    }

}
