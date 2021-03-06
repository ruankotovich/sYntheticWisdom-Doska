/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkListener;
import ru.sw.doska.model.Balloon;
import ru.sw.doska.model.QueryEngine;

/**
 *
 * @author ruankotovich 
 * @github https://github.com/ruankotovich
 */
public class MaposoController {

    public static enum MaposoMode {
        DEFAULT,
        TRIVIA;
    }

    private final QueryEngine qEngine;
    private final JLabel maposo;
    private final JPanel maposoContainer;
    private final Point mouthLocation;
    private final ImageIcon maposoShutted;
    private final ImageIcon maposoTalking;
    private MaposoMode mode;
    private Balloon previousBalloon = null;
    private Thread timeoutShutUP;
    private final Random randomSeed = new Random();
    private final String maposoSpeaks[] = new String[]{
        "<p align='center'>Se você quiser <b><font color='#007700'>calcular a distância<font color='#007700'></b> de maneira fácil, aperte <b>shift</b> e clique na primeira cidade com o <b>botão esquerdo do mouse</b>; o mesmo procedimento para a segunda cidade, mas com o <b>botão direito do mouse</b> :)</p>",
        "<p align='center'>Ao <b>passar o mouse</b> em uma cidade,  você verá mais informações sobre tal cidade tal como a <b><font color='#007700'>nomenclatura do povo local</font></b> e <b><font color='#007700'>informações espaciais</font></b> :)</p>",
        "<p align='center'>Ao <b>clicar</b> em um estado, você conhecerá a <b><font color='#007700'>capital daquele estado</font></b>, o <b><font color='#007700'>clima</font></b>, a <b><font color='#007700'>região</font></b> e até mesmo as <b><font color='#007700'>principais cidades daquele estado</font></b> :)</p>",
        "<p align='center'>Você pode se movimentar no mapa tanto arrastando o <b><font color='red'>bloquinho vermelho</font></b> do minimapa quanto posicionando o mouse nas <b>bordas do mapa</b> :)</p>",
        "<center>Você também pode adicionar/visualizar novas informações legais sobre as cidades, basta clicar em <b><font color='blue'>clique para saber mais</font></b> quando estiver visualizando as informações de uma cidade e em seguida <b><font color='blue'>clique para adicionar/ver informações</font></b> :)</center>"
    };
    private final String maposoSpeaksTrivia[] = new String[]{
        "<p align='center'>Sempre que pensar em desistir, desista da ideia :)</p>",
        "<p align='center'>Escolha uma das opções e clique em <b>\"CONFIRMAR\"</b> :)</p>"
    };

    private void talk() {
        this.maposo.setIcon(maposoTalking);
    }

    private void shutUp() {
        this.maposo.setIcon(maposoShutted);
    }
    
    public void shut(){
        shutUp();
        removeBalloon();
    }

    private void removeBalloon() {
        if (previousBalloon != null) {
            previousBalloon.getBalloon().setVisible(false);
            maposoContainer.remove(previousBalloon.getBalloon());
        }
    }

    private void addBalloon(String text, HyperlinkListener listener) {
        if (timeoutShutUP != null) {
            if (timeoutShutUP.isAlive()) {
                timeoutShutUP.interrupt();
                try {
                    timeoutShutUP.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MaposoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        removeBalloon();
        previousBalloon = new Balloon(Balloon.BalloonType.BOTTOM_LEFT, listener, mouthLocation, text);
        maposoContainer.add(previousBalloon.getBalloon());
        maposoContainer.setComponentZOrder(previousBalloon.getBalloon(), 0);
        maposoContainer.repaint();
        talk();
        timeoutShutUP = new Thread(() -> {
            try {
                Thread.sleep(text.length() * 45);
                removeBalloon();
                shutUp();
            } catch (Exception ex) {

            }
        });
        timeoutShutUP.start();
    }

    private void addBalloon(String text) {
        if (timeoutShutUP != null) {
            if (timeoutShutUP.isAlive()) {
                timeoutShutUP.interrupt();
                try {
                    timeoutShutUP.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MaposoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        removeBalloon();
        previousBalloon = new Balloon(Balloon.BalloonType.BOTTOM_LEFT, mouthLocation, text);
        maposoContainer.add(previousBalloon.getBalloon());
        maposoContainer.setComponentZOrder(previousBalloon.getBalloon(), 0);
        maposoContainer.repaint();
        talk();
        timeoutShutUP = new Thread(() -> {
            try {
                Thread.sleep(text.length() * 45);
                removeBalloon();
                shutUp();
            } catch (Exception ex) {

            }
        });
        timeoutShutUP.start();
    }
    private final MouseAdapter maposoAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (mode == MaposoMode.DEFAULT) {
                addBalloon(maposoSpeaks[randomSeed.nextInt(maposoSpeaks.length)]);
            } else {
                addBalloon(maposoSpeaksTrivia[randomSeed.nextInt(maposoSpeaksTrivia.length)]);
            }
        }
    };

    public MaposoController(QueryEngine qEngine, JLabel maposo, JPanel maposoContainer, MaposoMode mode) {
        this.qEngine = qEngine;
        this.mode = mode;
        this.maposo = maposo;
        this.maposoContainer = maposoContainer;

        this.maposoShutted = new ImageIcon(this.getClass().getResource("/ru/sw/doska/gfx/maposo_shutted.png"));
        this.maposoTalking = new ImageIcon(this.getClass().getResource("/ru/sw/doska/gfx/maposo_talking.png"));

        this.mouthLocation = new Point(maposo.getWidth() - 50, (maposo.getHeight() >> 1) + 35);
        this.maposo.addMouseListener(maposoAdapter);
    }

    public void sendMessage(String text) {
        addBalloon(text);
    }

    public void sendMessage(String text, HyperlinkListener listener) {
        addBalloon(text, listener);
    }

    private void triviaMessage() {
        addBalloon("<center>Bem-vindo à <b>Trivia</b> do Doska!<br><br>Responda as dez perguntas o <font color='red'>mais rápido possível</font><br>Clique em <b>\"Começar\"</b> para iniciar <br>Boa Sorte :)</center>");
    }

    public void welcomeMessage() {
        addBalloon("<center><br><b>Opa, eu sou o maposo!</b><br>Se prepare bastante e tente resolver uma trívia<br><br> Se tiver dúvidas sobre o uso, fale comigo. :)</center>");
    }

    public void setMode(MaposoMode mode) {
        this.mode = mode;

        if (this.mode == MaposoMode.TRIVIA) {
            triviaMessage();
        }
    }

}
