/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.wnd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import ru.sw.doska.controller.JoystickController;
import ru.sw.doska.controller.MapController;
import ru.sw.doska.controller.MaposoController;
import ru.sw.doska.model.QueryEngine;

/**
 *
 * @author ruankotovich 
 * @github https://github.com/ruankotovich
 */
public class WNDMainWindow extends javax.swing.JFrame {

    private MapController mapMovementController;
    private MaposoController maposoController;
    private final int anchorHor, anchorVer;
    private final QueryEngine engine;

    public WNDMainWindow(QueryEngine engine) {
        initComponents();

        this.setVisible(false);

        try {
            this.setIconImage(ImageIO.read(this.getClass().getResource("/ru/sw/doska/gfx/flag-brasil.png")));
        } catch (IOException ex) {
            Logger.getLogger(WNDMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        anchorVer = jPminimapAnchor.getHeight() >> 1;
        anchorHor = jPminimapAnchor.getWidth() >> 1;
        this.setLocationRelativeTo(null);
        initMinimap();
        initControllers(engine);
        initComboboxCities();
        this.maposoController.welcomeMessage();
        this.setVisible(true);
        this.engine = mapMovementController.getEngine();
    }

    private void initComboboxCities() {
        for (String city : mapMovementController.getCitiesWithDistance()) {
            jCBcity1.addItem(city);
            jCBcity2.addItem(city);
        }
    }

    private void initMinimap() {
        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResourceAsStream("/ru/sw/doska/gfx/brasil-politico.jpg"));
            jLbMinimap.setIcon(new ImageIcon(bufferedImage.getScaledInstance(jLbMinimap.getWidth(), jLbMinimap.getWidth() * (bufferedImage.getWidth() / bufferedImage.getHeight()), BufferedImage.TYPE_INT_ARGB)));
            double horSupressor = jSPMapcontainer.getBounds().getWidth() / bufferedImage.getWidth();
            double verSupressor = jSPMapcontainer.getBounds().getHeight() / bufferedImage.getHeight();
            jPminimapAnchor.setPreferredSize(new Dimension((int) (jLbMinimap.getWidth() * horSupressor), (int) (jLbMinimap.getHeight() * verSupressor)));
        } catch (IOException ex) {
            Logger.getLogger(WNDMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initControllers(QueryEngine engine) {
        mapMovementController = new MapController(this, engine, new JoystickController[]{
            new JoystickController(jPtopMove, JoystickController.JoystickButton.NORTH, () -> {
                jSPMapcontainer.getVerticalScrollBar().setValue(jSPMapcontainer.getVerticalScrollBar().getValue() - 1);
                return null;
            }),
            new JoystickController(jPbottonMove, JoystickController.JoystickButton.SOUTH, () -> {
                jSPMapcontainer.getVerticalScrollBar().setValue(jSPMapcontainer.getVerticalScrollBar().getValue() + 1);
                return null;
            }),
            new JoystickController(jPrightMove, JoystickController.JoystickButton.EAST, () -> {
                jSPMapcontainer.getHorizontalScrollBar().setValue(jSPMapcontainer.getHorizontalScrollBar().getValue() + 1);
                return null;
            }),
            new JoystickController(jPleftMove, JoystickController.JoystickButton.WEST, () -> {
                jSPMapcontainer.getHorizontalScrollBar().setValue(jSPMapcontainer.getHorizontalScrollBar().getValue() - 1);
                return null;
            }), new JoystickController(jPnorthEast, JoystickController.JoystickButton.NORTHEAST, () -> {
                jSPMapcontainer.getVerticalScrollBar().setValue(jSPMapcontainer.getVerticalScrollBar().getValue() - 1);
                jSPMapcontainer.getHorizontalScrollBar().setValue(jSPMapcontainer.getHorizontalScrollBar().getValue() + 1);
                return null;
            }), new JoystickController(jPnorthWest, JoystickController.JoystickButton.NORTH, () -> {
                jSPMapcontainer.getVerticalScrollBar().setValue(jSPMapcontainer.getVerticalScrollBar().getValue() - 1);
                jSPMapcontainer.getHorizontalScrollBar().setValue(jSPMapcontainer.getHorizontalScrollBar().getValue() - 1);
                return null;
            }), new JoystickController(jPsouthEast, JoystickController.JoystickButton.SOUTHEAST, () -> {
                jSPMapcontainer.getHorizontalScrollBar().setValue(jSPMapcontainer.getHorizontalScrollBar().getValue() + 1);
                jSPMapcontainer.getVerticalScrollBar().setValue(jSPMapcontainer.getVerticalScrollBar().getValue() + 1);
                return null;
            }), new JoystickController(jPsouthWest, JoystickController.JoystickButton.SOUTHWEST, () -> {
                jSPMapcontainer.getHorizontalScrollBar().setValue(jSPMapcontainer.getHorizontalScrollBar().getValue() - 1);
                jSPMapcontainer.getVerticalScrollBar().setValue(jSPMapcontainer.getVerticalScrollBar().getValue() + 1);
                return null;
            })
        }, jPMap, jLbMinimap, jPminimapAnchor, jSPMapcontainer);
        maposoController = new MaposoController(mapMovementController.getEngine(), jLbmaposo, jPmaposo, MaposoController.MaposoMode.DEFAULT);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPcontainer = new javax.swing.JPanel();
        jPmapContainer = new javax.swing.JPanel();
        jPsouthEast = new javax.swing.JPanel();
        jPsouthWest = new javax.swing.JPanel();
        jPnorthEast = new javax.swing.JPanel();
        jPnorthWest = new javax.swing.JPanel();
        jPrightMove = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPtopMove = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPleftMove = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPbottonMove = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSPMapcontainer = new javax.swing.JScrollPane();
        jPMap = new javax.swing.JLabel();
        jPminimapContainer = new javax.swing.JPanel();
        jPminimapAnchor = new javax.swing.JPanel();
        jLbMinimap = new javax.swing.JLabel();
        jPdistancecalcContainer = new javax.swing.JPanel();
        jPdistanceContainer = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCBcity1 = new javax.swing.JComboBox<>();
        jTdistancia = new javax.swing.JLabel();
        jCBcity2 = new javax.swing.JComboBox<>();
        jPmaposo = new javax.swing.JPanel();
        jLbTrivia = new javax.swing.JLabel();
        jLbmaposo = new javax.swing.JLabel();
        jLbBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Synthetic Wisdom :: Doska");
        setResizable(false);

        jPcontainer.setBackground(new java.awt.Color(254, 254, 254));
        jPcontainer.setPreferredSize(new java.awt.Dimension(600, 600));
        jPcontainer.setLayout(null);

        jPmapContainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPsouthEast.setBackground(new Color(0,0,0,50));
        jPsouthEast.setCursor(new java.awt.Cursor(java.awt.Cursor.SE_RESIZE_CURSOR));

        javax.swing.GroupLayout jPsouthEastLayout = new javax.swing.GroupLayout(jPsouthEast);
        jPsouthEast.setLayout(jPsouthEastLayout);
        jPsouthEastLayout.setHorizontalGroup(
            jPsouthEastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPsouthEastLayout.setVerticalGroup(
            jPsouthEastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPmapContainer.add(jPsouthEast, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 570, 40, 40));

        jPsouthWest.setBackground(new Color(0,0,0,50));
        jPsouthWest.setCursor(new java.awt.Cursor(java.awt.Cursor.SW_RESIZE_CURSOR));

        javax.swing.GroupLayout jPsouthWestLayout = new javax.swing.GroupLayout(jPsouthWest);
        jPsouthWest.setLayout(jPsouthWestLayout);
        jPsouthWestLayout.setHorizontalGroup(
            jPsouthWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPsouthWestLayout.setVerticalGroup(
            jPsouthWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPmapContainer.add(jPsouthWest, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 570, 40, 40));

        jPnorthEast.setBackground(new Color(0,0,0,50));
        jPnorthEast.setCursor(new java.awt.Cursor(java.awt.Cursor.NE_RESIZE_CURSOR));

        javax.swing.GroupLayout jPnorthEastLayout = new javax.swing.GroupLayout(jPnorthEast);
        jPnorthEast.setLayout(jPnorthEastLayout);
        jPnorthEastLayout.setHorizontalGroup(
            jPnorthEastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPnorthEastLayout.setVerticalGroup(
            jPnorthEastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPmapContainer.add(jPnorthEast, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 0, 40, 40));

        jPnorthWest.setBackground(new Color(0,0,0,50));
        jPnorthWest.setCursor(new java.awt.Cursor(java.awt.Cursor.NW_RESIZE_CURSOR));

        javax.swing.GroupLayout jPnorthWestLayout = new javax.swing.GroupLayout(jPnorthWest);
        jPnorthWest.setLayout(jPnorthWestLayout);
        jPnorthWestLayout.setHorizontalGroup(
            jPnorthWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPnorthWestLayout.setVerticalGroup(
            jPnorthWestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPmapContainer.add(jPnorthWest, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jPrightMove.setBackground(new Color(0,0,0,50));
        jPrightMove.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jPrightMove.setVerifyInputWhenFocusTarget(false);

        jLabel4.setBackground(new java.awt.Color(56, 38, 40));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/arrows/east.png"))); // NOI18N

        javax.swing.GroupLayout jPrightMoveLayout = new javax.swing.GroupLayout(jPrightMove);
        jPrightMove.setLayout(jPrightMoveLayout);
        jPrightMoveLayout.setHorizontalGroup(
            jPrightMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPrightMoveLayout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(jLabel4))
        );
        jPrightMoveLayout.setVerticalGroup(
            jPrightMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );

        jPmapContainer.add(jPrightMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 0, 40, 610));

        jPtopMove.setBackground(new Color(0,0,0,50));
        jPtopMove.setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        jPtopMove.setVerifyInputWhenFocusTarget(false);

        jLabel3.setBackground(new java.awt.Color(56, 38, 40));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/arrows/north.png"))); // NOI18N

        javax.swing.GroupLayout jPtopMoveLayout = new javax.swing.GroupLayout(jPtopMove);
        jPtopMove.setLayout(jPtopMoveLayout);
        jPtopMoveLayout.setHorizontalGroup(
            jPtopMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        jPtopMoveLayout.setVerticalGroup(
            jPtopMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPtopMoveLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPmapContainer.add(jPtopMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 40));

        jPleftMove.setBackground(new Color(0,0,0,50));
        jPleftMove.setCursor(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));
        jPleftMove.setVerifyInputWhenFocusTarget(false);

        jLabel2.setBackground(new java.awt.Color(56, 38, 40));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/arrows/west.png"))); // NOI18N

        javax.swing.GroupLayout jPleftMoveLayout = new javax.swing.GroupLayout(jPleftMove);
        jPleftMove.setLayout(jPleftMoveLayout);
        jPleftMoveLayout.setHorizontalGroup(
            jPleftMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPleftMoveLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 8, Short.MAX_VALUE))
        );
        jPleftMoveLayout.setVerticalGroup(
            jPleftMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );

        jPmapContainer.add(jPleftMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 610));

        jPbottonMove.setBackground(new Color(0,0,0,50));
        jPbottonMove.setCursor(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR));
        jPbottonMove.setVerifyInputWhenFocusTarget(false);

        jLabel1.setBackground(new java.awt.Color(56, 38, 40));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/arrows/south.png"))); // NOI18N

        javax.swing.GroupLayout jPbottonMoveLayout = new javax.swing.GroupLayout(jPbottonMove);
        jPbottonMove.setLayout(jPbottonMoveLayout);
        jPbottonMoveLayout.setHorizontalGroup(
            jPbottonMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        jPbottonMoveLayout.setVerticalGroup(
            jPbottonMoveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPbottonMoveLayout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        jPmapContainer.add(jPbottonMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 570, 610, 40));

        jSPMapcontainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jSPMapcontainer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/brasil-politico.png"))); // NOI18N
        jPMap.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jSPMapcontainer.setViewportView(jPMap);

        jPmapContainer.add(jSPMapcontainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 610));

        jPcontainer.add(jPmapContainer);
        jPmapContainer.setBounds(626, 12, 610, 610);

        jPminimapContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPminimapContainer.setPreferredSize(new java.awt.Dimension(300, 300));
        jPminimapContainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPminimapAnchor.setBackground(new Color(255, 0,0,50));
        jPminimapAnchor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
        jPminimapAnchor.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jPminimapAnchor.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPminimapAnchorMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout jPminimapAnchorLayout = new javax.swing.GroupLayout(jPminimapAnchor);
        jPminimapAnchor.setLayout(jPminimapAnchorLayout);
        jPminimapAnchorLayout.setHorizontalGroup(
            jPminimapAnchorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPminimapAnchorLayout.setVerticalGroup(
            jPminimapAnchorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPminimapContainer.add(jPminimapAnchor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLbMinimap.setAlignmentY(0.0F);
        jLbMinimap.setPreferredSize(new java.awt.Dimension(300, 300));
        jPminimapContainer.add(jLbMinimap, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, -1, -1));

        jPcontainer.add(jPminimapContainer);
        jPminimapContainer.setBounds(320, 12, 300, 300);

        jPdistancecalcContainer.setBackground(new java.awt.Color(254, 254, 254));
        jPdistancecalcContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cálculo de Distância", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(1, 1, 1))); // NOI18N
        jPdistancecalcContainer.setOpaque(false);

        jPdistanceContainer.setBackground(new java.awt.Color(254, 254, 254));
        jPdistanceContainer.setOpaque(false);

        jLabel5.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(1, 1, 1));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("<>");

        jCBcity1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        jCBcity1.setBorder(new javax.swing.border.MatteBorder(null));
        jCBcity1.setFocusable(false);
        jCBcity1.setLightWeightPopupEnabled(false);
        jCBcity1.setOpaque(false);
        jCBcity1.setRequestFocusEnabled(false);
        jCBcity1.setVerifyInputWhenFocusTarget(false);
        jCBcity1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBcity1ActionPerformed(evt);
            }
        });

        jTdistancia.setBackground(new java.awt.Color(254, 254, 254));
        jTdistancia.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        jTdistancia.setForeground(new java.awt.Color(1, 6, 156));
        jTdistancia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jCBcity2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        jCBcity2.setBorder(new javax.swing.border.MatteBorder(null));
        jCBcity2.setFocusable(false);
        jCBcity2.setLightWeightPopupEnabled(false);
        jCBcity2.setOpaque(false);
        jCBcity2.setRequestFocusEnabled(false);
        jCBcity2.setVerifyInputWhenFocusTarget(false);
        jCBcity2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBcity2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPdistanceContainerLayout = new javax.swing.GroupLayout(jPdistanceContainer);
        jPdistanceContainer.setLayout(jPdistanceContainerLayout);
        jPdistanceContainerLayout.setHorizontalGroup(
            jPdistanceContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPdistanceContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPdistanceContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTdistancia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBcity1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 266, Short.MAX_VALUE)
                    .addComponent(jCBcity2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPdistanceContainerLayout.setVerticalGroup(
            jPdistanceContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPdistanceContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBcity1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBcity2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTdistancia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPdistancecalcContainerLayout = new javax.swing.GroupLayout(jPdistancecalcContainer);
        jPdistancecalcContainer.setLayout(jPdistancecalcContainerLayout);
        jPdistancecalcContainerLayout.setHorizontalGroup(
            jPdistancecalcContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPdistanceContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPdistancecalcContainerLayout.setVerticalGroup(
            jPdistancecalcContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPdistanceContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPcontainer.add(jPdistancecalcContainer);
        jPdistancecalcContainer.setBounds(12, 12, 302, 300);

        jPmaposo.setOpaque(false);

        jLbTrivia.setFont(new java.awt.Font("Noto Sans", 1, 48)); // NOI18N
        jLbTrivia.setForeground(new java.awt.Color(2, 18, 110));
        jLbTrivia.setText("Iniciar Trivia");
        jLbTrivia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLbTrivia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLbTriviaMouseClicked(evt);
            }
        });

        jLbmaposo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/maposo_shutted.png"))); // NOI18N
        jLbmaposo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPmaposoLayout = new javax.swing.GroupLayout(jPmaposo);
        jPmaposo.setLayout(jPmaposoLayout);
        jPmaposoLayout.setHorizontalGroup(
            jPmaposoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPmaposoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLbmaposo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addComponent(jLbTrivia)
                .addContainerGap())
        );
        jPmaposoLayout.setVerticalGroup(
            jPmaposoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPmaposoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPmaposoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLbTrivia)
                    .addComponent(jLbmaposo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPcontainer.add(jPmaposo);
        jPmaposo.setBounds(10, 330, 600, 300);

        jLbBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/sw/doska/gfx/background.jpg"))); // NOI18N
        jPcontainer.add(jLbBackground);
        jLbBackground.setBounds(0, 0, 1250, 640);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPcontainer, javax.swing.GroupLayout.DEFAULT_SIZE, 1250, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPcontainer, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPminimapAnchorMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPminimapAnchorMouseDragged
        evt.translatePoint(evt.getComponent().getLocation().x - anchorHor, evt.getComponent().getLocation().y - anchorVer);
        jPminimapAnchor.setLocation(
                (evt.getX() >= 0 && evt.getX() <= jLbMinimap.getWidth() - jPminimapAnchor.getWidth()) ? evt.getX() : jPminimapAnchor.getLocation().x,
                (evt.getY() >= 0 && evt.getY() <= jLbMinimap.getHeight() - jPminimapAnchor.getHeight()) ? evt.getY() : jPminimapAnchor.getLocation().y);
        mapMovementController.translateMap();
    }//GEN-LAST:event_jPminimapAnchorMouseDragged

    private void jCBcity1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBcity1ActionPerformed
        if (jCBcity1.getSelectedIndex() != jCBcity2.getSelectedIndex() && jCBcity1.getSelectedIndex() * jCBcity2.getSelectedIndex() > 0) {
            jTdistancia.setText(String.format("%.2f", mapMovementController.calculateDistanceBetween(jCBcity1.getSelectedItem().toString(), jCBcity2.getSelectedItem().toString())) + " Km");
            jPdistanceContainer.repaint();
        }
    }//GEN-LAST:event_jCBcity1ActionPerformed

    private void jCBcity2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBcity2ActionPerformed
        if (jCBcity1.getSelectedIndex() != jCBcity2.getSelectedIndex() && jCBcity1.getSelectedIndex() * jCBcity2.getSelectedIndex() > 0) {
            jTdistancia.setText(String.format("%.2f", mapMovementController.calculateDistanceBetween(jCBcity1.getSelectedItem().toString(), jCBcity2.getSelectedItem().toString())) + " Km");
            jPdistanceContainer.repaint();
        }
    }//GEN-LAST:event_jCBcity2ActionPerformed

    private void jLbTriviaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLbTriviaMouseClicked
        jPminimapContainer.setVisible(false);
        jPmapContainer.setVisible(false);
        jPdistancecalcContainer.setVisible(false);
        jLbTrivia.setVisible(false);

        WNDTrivia trivia = new WNDTrivia(this, engine, maposoController);
        trivia.setBounds(0, 0, this.getWidth(), this.getHeight());
        trivia.setVisible(true);

        jLbBackground.add(trivia);
        maposoController.setMode(MaposoController.MaposoMode.TRIVIA);
    }//GEN-LAST:event_jLbTriviaMouseClicked

    public void exitTrivia() {
        jPminimapContainer.setVisible(true);
        jPmapContainer.setVisible(true);
        jPdistancecalcContainer.setVisible(true);
        jLbTrivia.setVisible(true);
        jLbBackground.removeAll();
        maposoController.setMode(MaposoController.MaposoMode.DEFAULT);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WNDMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WNDMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WNDMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WNDMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new WNDMainWindow(null).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> jCBcity1;
    public javax.swing.JComboBox<String> jCBcity2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLbBackground;
    private javax.swing.JLabel jLbMinimap;
    private javax.swing.JLabel jLbTrivia;
    private javax.swing.JLabel jLbmaposo;
    private javax.swing.JLabel jPMap;
    private javax.swing.JPanel jPbottonMove;
    private javax.swing.JPanel jPcontainer;
    private javax.swing.JPanel jPdistanceContainer;
    private javax.swing.JPanel jPdistancecalcContainer;
    private javax.swing.JPanel jPleftMove;
    private javax.swing.JPanel jPmapContainer;
    private javax.swing.JPanel jPmaposo;
    private javax.swing.JPanel jPminimapAnchor;
    private javax.swing.JPanel jPminimapContainer;
    private javax.swing.JPanel jPnorthEast;
    private javax.swing.JPanel jPnorthWest;
    private javax.swing.JPanel jPrightMove;
    private javax.swing.JPanel jPsouthEast;
    private javax.swing.JPanel jPsouthWest;
    private javax.swing.JPanel jPtopMove;
    private javax.swing.JScrollPane jSPMapcontainer;
    private javax.swing.JLabel jTdistancia;
    // End of variables declaration//GEN-END:variables
}
