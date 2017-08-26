/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author dmitry
 */
public class HyperlinkController implements HyperlinkListener {

    MapController mapController;

    public HyperlinkController(MapController mapController) {
        this.mapController = mapController;
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        mapController.notifyListener(e);
    }

}
