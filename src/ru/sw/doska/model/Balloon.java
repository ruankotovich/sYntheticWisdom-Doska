/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author ruankotovich 
 * @github https://github.com/ruankotovich
 */
public class Balloon {

    public static enum BalloonType {
        BOTTOM_LEFT("/ru/sw/doska/gfx/balloons/bottom_left.png", false, true),
        BOTTOM_RIGHT("/ru/sw/doska/gfx/balloons/bottom_right.png", false, false),
        TOP_RIGHT("/ru/sw/doska/gfx/balloons/top_right.png", true, false),
        TOP_LEFT("/ru/sw/doska/gfx/balloons/top_left.png", true, true);

        private BufferedImage imageIcon;
        private final Point textPosition;
        private final boolean lockedHor;
        private final boolean lockedVer;

        private BalloonType(String imageIcon, boolean hasLockVer, boolean hasLockHor) {
            try {
                this.imageIcon = ImageIO.read(Balloon.class.getResource(imageIcon));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            this.lockedHor = hasLockHor;
            this.lockedVer = hasLockVer;

            if (this.imageIcon.getRGB(0, 0) == Color.BLACK.getRGB() || this.imageIcon.getRGB(this.imageIcon.getWidth() - 1, 0) == Color.BLACK.getRGB()) {
                this.textPosition = new Point(3, (int) ((double) this.imageIcon.getHeight() / 2.5));
            } else {
                this.textPosition = new Point(3, 3);
            }

        }

        public BufferedImage getImageIcon() {
            return imageIcon;
        }

        public Point getTextPosition() {
            return textPosition;
        }

        public Dimension getDimension() {
            return new Dimension(this.imageIcon.getWidth(), this.imageIcon.getHeight());
        }

        public boolean isLockedHor() {
            return lockedHor;
        }

        public boolean isLockedVer() {
            return lockedVer;
        }

    }

    public JLabel getBalloon() {
        return balloonLabel;
    }

    private final BalloonType type;
    private final JEditorPane content;
    private final JLabel balloonLabel;

    public Balloon(BalloonType type, Point location, String text) {
        this.type = type;

        this.balloonLabel = new JLabel(new ImageIcon(type.getImageIcon()));
        this.balloonLabel.setSize(type.getDimension());
        this.balloonLabel.setPreferredSize(type.getDimension());

        this.balloonLabel.setLocation(location.x - (type.isLockedHor() ? 0 : type.getDimension().width), location.y - (type.isLockedVer() ? 0 : type.getDimension().height));

        this.content = new JEditorPane();
        this.content.setContentType("text/html");
        this.content.setText(text);
        this.content.setSize(type.getDimension().width, (type.getDimension().height / 2) + 30);
        this.content.setLocation(type.getTextPosition());
        this.content.setEditable(false);
        this.content.setOpaque(false);
        this.content.setBackground(new Color(0, 0, 0, 0));
        this.content.setBorder(null);

        this.balloonLabel.add(content);

        this.content.setVisible(true);
        this.balloonLabel.setVisible(true);

    }

    public Balloon(BalloonType type, HyperlinkListener listener, Point location, String text) {
        this(type, location, text);
        this.content.addHyperlinkListener(listener);
    }

}
