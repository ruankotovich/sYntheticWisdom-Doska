/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 *
 * @author dmitry
 */
public class JoystickController {

    public static enum JoystickButton {
        NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTWEST, SOUTHEAST, SOUTHWEST
    }

    public static interface JoystickAction {

        public void action();
    }

    private final JComponent component;
    private final JoystickButton button;
    private final JoystickAction action;
    private final Rectangle st_rectangle;

    public JoystickController(JComponent component, JoystickButton button, Callable<Void> action) {
        this.component = component;
        this.button = button;

        this.action = () -> {
            try {
                action.call();
            } catch (Exception ex) {
                Logger.getLogger(JoystickController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };

        st_rectangle = new Rectangle(0, 0, 1, 1);
    }

    public boolean intersects(Point p) {
        st_rectangle.setLocation(p);
        return component.getBounds().intersects(st_rectangle);
    }

    public JComponent getComponent() {
        return component;
    }

    public JoystickButton getButton() {
        return button;
    }

    public JoystickAction getAction() {
        return action;
    }

}
