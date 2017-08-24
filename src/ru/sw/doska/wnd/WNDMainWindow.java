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

/**
 *
 * @author dmitry
 */
public class WNDMainWindow extends javax.swing.JFrame {

    private MapController mapMovementController;

    public WNDMainWindow() {
        initComponents();
        this.setLocationRelativeTo(null);
        initMinimap();
        initControllers();
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

    private void initControllers() {
        mapMovementController = new MapController(new JoystickController[]{
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
        jPanel1 = new javax.swing.JPanel();
        jPminimapAnchor = new javax.swing.JPanel();
        jLbMinimap = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPcontainer.setBackground(new java.awt.Color(254, 254, 254));
        jPcontainer.setPreferredSize(new java.awt.Dimension(600, 600));

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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPminimapAnchor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
        jPminimapAnchor.setOpaque(false);

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

        jPanel1.add(jPminimapAnchor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLbMinimap.setAlignmentY(0.0F);
        jLbMinimap.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanel1.add(jLbMinimap, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, -1, -1));

        javax.swing.GroupLayout jPcontainerLayout = new javax.swing.GroupLayout(jPcontainer);
        jPcontainer.setLayout(jPcontainerLayout);
        jPcontainerLayout.setHorizontalGroup(
            jPcontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPcontainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPmapContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPcontainerLayout.setVerticalGroup(
            jPcontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPcontainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPcontainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPmapContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPcontainer, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPcontainer, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            new WNDMainWindow().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLbMinimap;
    private javax.swing.JLabel jPMap;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPbottonMove;
    private javax.swing.JPanel jPcontainer;
    private javax.swing.JPanel jPleftMove;
    private javax.swing.JPanel jPmapContainer;
    private javax.swing.JPanel jPminimapAnchor;
    private javax.swing.JPanel jPnorthEast;
    private javax.swing.JPanel jPnorthWest;
    private javax.swing.JPanel jPrightMove;
    private javax.swing.JPanel jPsouthEast;
    private javax.swing.JPanel jPsouthWest;
    private javax.swing.JPanel jPtopMove;
    private javax.swing.JScrollPane jSPMapcontainer;
    // End of variables declaration//GEN-END:variables
}
