/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.model;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 *
 * @author dmitry
 */
public class Balloon {

    public static enum BalloonType {
        BOTTOM_LEFT("/ru/sw/doska/gfx/balloons/bottom_left.png"),
        BOTTOM_RIGHT("/ru/sw/doska/gfx/balloons/bottom_right.png"),
        TOP_RIGHT("/ru/sw/doska/gfx/balloons/top_right.png"),
        TOP_LEFT("/ru/sw/doska/gfx/balloons/top_left.png");

        private BufferedImage imageIcon;
        private final Point textPosition;

        private BalloonType(String imageIcon) {
            try {
                this.imageIcon = ImageIO.read(getClass().getResource(imageIcon));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (this.imageIcon.getRGB(0, 0) == Color.BLACK.getRGB() || this.imageIcon.getRGB(this.imageIcon.getWidth() - 1, 0) == Color.BLACK.getRGB()) {
                this.textPosition = new Point(3, this.imageIcon.getHeight() / 2);
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

    }
    
    private final BalloonType type;
    private final JLabel contentLabel;
    
}
