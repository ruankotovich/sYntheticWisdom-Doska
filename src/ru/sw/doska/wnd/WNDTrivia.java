/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.wnd;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import ru.sw.doska.controller.MaposoController;
import ru.sw.doska.model.QueryEngine;
import ru.sw.doska.model.Question;

/**
 *
 * @author dmitry
 */
public class WNDTrivia extends javax.swing.JPanel {

    /**
     * Creates new form WNDTrivia
     *
     * @param engine
     */
    private final MaposoController maposo;
    private final QueryEngine engine;
    private final WNDMainWindow caller;
    private final List<Question> questions;
    private final Random rand;
    private final String avaiableLC[];
    private final String avaiableUC[];
    private final HyperlinkListener listener;
    private JLabel jLbfeedback = null;
    private BufferedImage ok, wrong;
    private int timeInThread = 0;
    private final int totalQuestions;
    private int correctAnswers = 0;
    private Thread timeThread;
    private boolean isOn = true;

    public WNDTrivia(WNDMainWindow calling, QueryEngine engine, MaposoController controller) {
        initComponents();

        {
            timeThread = new Thread(() -> {
                while (isOn) {
                    ++timeInThread;

                    int minutes = timeInThread / 60;
                    int seconds = timeInThread % 60;

                    jLbtime.setText(String.format("%02dm%02ds", minutes, seconds));

                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.err.println("Sleep interrupted.");
                    }
                }
            });
        }

        {
            jBconfirmar.setVisible(false);
            jBdesistir.setVisible(false);
            jBpular.setVisible(false);
            jTBoption1.setVisible(false);
            jTBoption2.setVisible(false);
            jTBoption3.setVisible(false);
            jBvoltar.setVisible(false);
            jTBoption4.setVisible(false);
            jTBoption5.setVisible(false);
            jLbQuestion.setVisible(false);
            jLbTD.setVisible(false);
            jLbR.setVisible(false);
            jLbanswereds.setVisible(false);
            jLbtime.setVisible(false);
        }

        try {
            ok = ImageIO.read(getClass().getResource("/ru/sw/doska/gfx/ok.png"));
            wrong = ImageIO.read(getClass().getResource("/ru/sw/doska/gfx/wrong.png"));
        } catch (IOException ex) {
            Logger.getLogger(WNDTrivia.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.caller = calling;
        this.maposo = controller;
        this.engine = engine;

        avaiableLC = new String[]{"a", "e", "i", "o", "u"};
        avaiableUC = new String[]{"A", "E", "I", "O", "U"};
        this.questions = new ArrayList<>();
        this.rand = new Random();

        generateQuestions();

        listener = (HyperlinkEvent hlEvent) -> {
            if (hlEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                String[] linkShelf = hlEvent.getDescription().split(";");
                if (linkShelf.length > 0) {
                    switch (linkShelf[0]) {
                        case "@exit": {
                            WNDTrivia.this.dispose();
                        }
                        case "@cancel_exit": {
                            maposo.shut();
                        }

                    }
                }
            }
        };
        totalQuestions = questions.size();
    }

    private void removeFeedback() {
        if (jLbfeedback != null) {
            this.remove(jLbfeedback);
            jLbfeedback.setVisible(false);
        }
    }

    public void feedback(boolean value) {
        removeFeedback();
        jLbfeedback = new JLabel();
        jLbfeedback.setVisible(true);
        if (value) {
            maposo.sendMessage("<b>Boa! Correto :)</b>");
            jLbfeedback.setIcon(new ImageIcon(ok));
            jLbfeedback.setSize(ok.getWidth(), ok.getHeight());
        } else {
            maposo.sendMessage("<i>Que pena, não é isso</i> :/");
            jLbfeedback.setIcon(new ImageIcon(wrong));
            jLbfeedback.setSize(wrong.getWidth(), wrong.getHeight());
        }

        jLbfeedback.setLocation(jTBoption3.getWidth() / 2 + jTBoption3.getLocation().x - jLbfeedback.getWidth() / 2, jTBoption3.getHeight() / 2 + jTBoption3.getLocation().y - jLbfeedback.getHeight() / 2);

        jpIremel.add(jLbfeedback);
        jpIremel.setComponentZOrder(jLbfeedback, 0);
        jpIremel.repaint();

        jBconfirmar.setEnabled(false);
        jBpular.setEnabled(false);
        jTBoption1.setEnabled(false);
        jTBoption2.setEnabled(false);
        jTBoption3.setEnabled(false);
        jTBoption4.setEnabled(false);
        jTBoption5.setEnabled(false);

        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(WNDTrivia.class.getName()).log(Level.SEVERE, null, ex);
            }
            removeFeedback();
            this.repaint();

            jTBoption1.setEnabled(true);
            jTBoption2.setEnabled(true);
            jTBoption3.setEnabled(true);
            jTBoption4.setEnabled(true);
            jTBoption5.setEnabled(true);

            jBconfirmar.setEnabled(true);
            jBpular.setEnabled(true);

            maposo.shut();

            questions.remove(0);
            nextQuestion();

            if (value) {
                correctAnswers++;
            }

            jLbanswereds.setText(String.valueOf(questions.size()) + "/" + totalQuestions);
            optionGroup.clearSelection();
        }).start();

    }

    private void generateQuestions() {
        for (int i = 0; i < 3; i++) {
            generateQuestion(engine.consult("internal_CITIZEN_CALLED(CITY, CALLED)."), "Como são chamados os habitantes de <?> ? ", "CITY", "CALLED");
            generateQuestion(engine.consult("internal_CLIMATE_BY_STATE(STATE, CLIMATE)."), "Qual o clima predominante no estado do <?> ? ", "STATE", "CLIMATE");
            generateQuestion(engine.consult("internal_CAPITAL_OF(STATE, CITY)."), "Qual a capital do estado do <?> ?", "STATE", "CITY");
        }
        generateQuestion(engine.consult("internal_STATE_REGION(STATE, REGION)."), "Em qual região é localizada o estado do <?> ?", "STATE", "REGION");

        Collections.shuffle(questions);
    }

    public void start() {
        jLbanswereds.setText(String.valueOf(questions.size()) + "/" + totalQuestions);
        timeThread.start();
        jBconfirmar.setVisible(true);
        jBdesistir.setVisible(true);
        jBpular.setVisible(true);
        jTBoption1.setVisible(true);
        jTBoption2.setVisible(true);
        jTBoption3.setVisible(true);
        jTBoption4.setVisible(true);
        jTBoption5.setVisible(true);
        jLbQuestion.setVisible(true);
        jBcomecar.setVisible(false);
        jLbTD.setVisible(true);
        jLbR.setVisible(true);
        jLbanswereds.setVisible(true);
        jLbtime.setVisible(true);
        nextQuestion();
    }

    private void nextQuestion() {
        if (!questions.isEmpty()) {
            buildQuestion(questions.get(0));
        } else {
            end();
        }
    }

    private void end() {
        isOn = false;
        timeThread.interrupt();

        jTBoption1.setVisible(false);
        jTBoption2.setVisible(false);
        jTBoption3.setVisible(false);
        jTBoption4.setVisible(false);
        jTBoption5.setVisible(false);
        jLbQuestion.setVisible(false);

        jBdesistir.setVisible(false);;
        jBconfirmar.setVisible(false);
        jBpular.setVisible(false);

        jBvoltar.setVisible(true);

        maposo.sendMessage("<p align='center'>Você acertou " + correctAnswers + " de " + totalQuestions + " perguntas.<br>" + "\n"
                + "Seu tempo foi de " + jLbtime.getText() + "<br>"
                + (correctAnswers > 8 ? "Você foi excelente!" : (correctAnswers > 5 ? "Você foi bem." : (correctAnswers > 3 ? "Você precisa estudar um pouco mais." : "Você precisa estudar mais.")))
                + "</p>"
        );

    }

    private void buildQuestion(Question q) {
        this.jLbQuestion.setText(q.getQuestion());
        this.jTBoption1.setText(q.getOptions().get('A'));
        this.jTBoption2.setText(q.getOptions().get('B'));
        this.jTBoption3.setText(q.getOptions().get('C'));
        this.jTBoption4.setText(q.getOptions().get('D'));
        this.jTBoption5.setText(q.getOptions().get('E'));
    }

    private void generateQuestion(TreeMap<Integer, TreeMap<String, String>> informationCollection, String questionTitle, String objectNameOnMap, String answerNameOnMap) {

        List<Character> avaiableLetters = new ArrayList<>();
        Set<String> pastOptions = new TreeSet<>();

        generateLetters(avaiableLetters);

        int correctQuestionIndex = (int) informationCollection.keySet().toArray()[rand.nextInt(informationCollection.size())];
        TreeMap<String, String> collection = informationCollection.get(correctQuestionIndex);
        informationCollection.remove(correctQuestionIndex);

        String correctString = collection.get(answerNameOnMap).replace("'", "");
        pastOptions.add(correctString);
        Question question = new Question(questionTitle.replace("<?>", collection.get(objectNameOnMap)));
        question.addOption(avaiableLetters.get(0), correctString);
        question.setAnswer(avaiableLetters.get(0));
        avaiableLetters.remove(0);
        questions.add(question);

        for (int i = 0; i < 2; i++) {

            int currentPrimaryAnswerIndex = (int) informationCollection.keySet().toArray()[rand.nextInt(informationCollection.size())];
            TreeMap<String, String> insidePrimaryCollection = informationCollection.get(currentPrimaryAnswerIndex);
            informationCollection.remove(currentPrimaryAnswerIndex);

            int currentSecondaryDeployIndex = (int) informationCollection.keySet().toArray()[rand.nextInt(informationCollection.size())];
            TreeMap<String, String> insideSecondaryCollection = informationCollection.get(currentSecondaryDeployIndex);
            informationCollection.remove(currentSecondaryDeployIndex);

            String fAns = insidePrimaryCollection.get(answerNameOnMap).replace("'", "");
            String sAns = insideSecondaryCollection.get(answerNameOnMap).replace("'", "");

            if (pastOptions.contains(fAns)) {
                fAns = shuffleString(fAns);
            } else {
                pastOptions.add(fAns);
            }

            if (pastOptions.contains(sAns)) {
                sAns = shuffleString(sAns);
            } else {
                pastOptions.add(sAns);
            }

            question.addOption(avaiableLetters.get(0), fAns);
            avaiableLetters.remove(0);
            question.addOption(avaiableLetters.get(0), sAns);
            avaiableLetters.remove(0);
        }
    }

    @SuppressWarnings("empty-statement")
    private String getAvaiable(String excluding, boolean upper) {
        String toReturn;

        if (upper) {
            while ((toReturn = avaiableUC[rand.nextInt(avaiableUC.length)]).equals(excluding));
        } else {
            while ((toReturn = avaiableLC[rand.nextInt(avaiableLC.length)]).equals(excluding));
        }

        return toReturn;
    }

    private String shuffleString(String str) {

        int from = rand.nextInt(str.length());
        int to = from + rand.nextInt(str.length() - from);

        switch (rand.nextInt(9)) {
            case 0:
                return str.contains("a") ? str.replace("a", getAvaiable("a", false)) : shuffleString(str);
            case 1:
                return str.contains("e") ? str.replace("e", getAvaiable("e", false)) : shuffleString(str);
            case 2:
                return str.contains("i") ? str.replace("i", getAvaiable("u", false)) : shuffleString(str);
            case 3:
                return str.contains("o") ? str.replace("o", getAvaiable("o", false)) : shuffleString(str);
            case 4:
                return str.contains("u") ? str.replace("u", getAvaiable("a", false)) : shuffleString(str);
            case 5:
                return str.contains("A") ? str.replace("A", getAvaiable("A", true)) : shuffleString(str);
            case 6:
                return str.contains("E") ? str.replace("E", getAvaiable("E", true)) : shuffleString(str);
            case 7:
                return str.contains("I") ? str.replace("I", getAvaiable("I", true)) : shuffleString(str);
            case 8:
                return str.contains("O") ? str.replace("O", getAvaiable("O", true)) : shuffleString(str);
            case 9:
                return str.contains("U") ? str.replace("U", getAvaiable("U", true)) : shuffleString(str);
            default:
                return str;
        }

    }

    private void generateLetters(List<Character> list) {
        list.add('A');
        list.add('B');
        list.add('C');
        list.add('D');
        list.add('E');
        Collections.shuffle(list);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLbTD = new javax.swing.JLabel();
        jLbtime = new javax.swing.JLabel();
        jLbR = new javax.swing.JLabel();
        jLbanswereds = new javax.swing.JLabel();
        jBcomecar = new javax.swing.JButton();
        jBvoltar = new javax.swing.JButton();
        jpIremel = new javax.swing.JPanel();
        jLbQuestion = new javax.swing.JLabel();
        jTBoption2 = new javax.swing.JToggleButton();
        jBdesistir = new javax.swing.JButton();
        jTBoption3 = new javax.swing.JToggleButton();
        jTBoption4 = new javax.swing.JToggleButton();
        jTBoption5 = new javax.swing.JToggleButton();
        jTBoption1 = new javax.swing.JToggleButton();
        jBpular = new javax.swing.JButton();
        jBconfirmar = new javax.swing.JButton();

        setBackground(new java.awt.Color(254, 254, 254));
        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));
        jPanel1.setOpaque(false);

        jLbTD.setBackground(new java.awt.Color(1, 1, 1));
        jLbTD.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jLbTD.setForeground(new java.awt.Color(1, 1, 1));
        jLbTD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbTD.setText("Tempo Decorrido :");

        jLbtime.setBackground(new java.awt.Color(2, 96, 143));
        jLbtime.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jLbtime.setForeground(new java.awt.Color(254, 254, 254));
        jLbtime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbtime.setText("00m00s");
        jLbtime.setOpaque(true);

        jLbR.setBackground(new java.awt.Color(1, 1, 1));
        jLbR.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jLbR.setForeground(new java.awt.Color(1, 1, 1));
        jLbR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbR.setText("Questões Restantes:");

        jLbanswereds.setBackground(new java.awt.Color(2, 96, 143));
        jLbanswereds.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jLbanswereds.setForeground(new java.awt.Color(254, 254, 254));
        jLbanswereds.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbanswereds.setText("0/10");
        jLbanswereds.setOpaque(true);

        jBcomecar.setBackground(new java.awt.Color(0, 77, 116));
        jBcomecar.setFont(new java.awt.Font("Noto Sans", 0, 48)); // NOI18N
        jBcomecar.setForeground(new java.awt.Color(254, 254, 254));
        jBcomecar.setText("Começar");
        jBcomecar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBcomecar.setDefaultCapable(false);
        jBcomecar.setFocusPainted(false);
        jBcomecar.setFocusable(false);
        jBcomecar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBcomecarActionPerformed(evt);
            }
        });

        jBvoltar.setBackground(new java.awt.Color(77, 91, 1));
        jBvoltar.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jBvoltar.setForeground(new java.awt.Color(254, 254, 254));
        jBvoltar.setText("Voltar");
        jBvoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBvoltar.setDefaultCapable(false);
        jBvoltar.setFocusPainted(false);
        jBvoltar.setFocusable(false);
        jBvoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBvoltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLbtime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbTD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbanswereds, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbR, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBcomecar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBvoltar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLbTD, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbtime, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLbR, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLbanswereds, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBcomecar)
                .addGap(18, 18, 18)
                .addComponent(jBvoltar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpIremel.setMaximumSize(new java.awt.Dimension(729, 497));
        jpIremel.setMinimumSize(new java.awt.Dimension(729, 497));
        jpIremel.setOpaque(false);

        jLbQuestion.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        jLbQuestion.setForeground(new java.awt.Color(1, 1, 1));
        jLbQuestion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbQuestion.setText("1. Blablablablablabla?");

        optionGroup.add(jTBoption2);
        jTBoption2.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jTBoption2.setText("OPSAOAOAOAOA1OPSAOAOAOAOA1");
        jTBoption2.setActionCommand("B");
        jTBoption2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTBoption2.setFocusable(false);
        jTBoption2.setVerifyInputWhenFocusTarget(false);

        jBdesistir.setBackground(new java.awt.Color(123, 2, 12));
        jBdesistir.setFont(new java.awt.Font("Noto Sans", 0, 36)); // NOI18N
        jBdesistir.setForeground(new java.awt.Color(254, 254, 254));
        jBdesistir.setText("Desistir");
        jBdesistir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBdesistir.setFocusable(false);
        jBdesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBdesistirActionPerformed(evt);
            }
        });

        optionGroup.add(jTBoption3);
        jTBoption3.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jTBoption3.setText("OPSAOAOAOAOA1OPSAOAOAOAOA1OPSAOAOAOAOA1");
        jTBoption3.setActionCommand("C");
        jTBoption3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTBoption3.setFocusable(false);
        jTBoption3.setVerifyInputWhenFocusTarget(false);

        optionGroup.add(jTBoption4);
        jTBoption4.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jTBoption4.setText("OPSAOAOAOAOA1");
        jTBoption4.setActionCommand("D");
        jTBoption4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTBoption4.setFocusable(false);
        jTBoption4.setVerifyInputWhenFocusTarget(false);

        optionGroup.add(jTBoption5);
        jTBoption5.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jTBoption5.setText("OPSAOAOAOAOA1");
        jTBoption5.setActionCommand("E");
        jTBoption5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTBoption5.setFocusable(false);
        jTBoption5.setVerifyInputWhenFocusTarget(false);

        optionGroup.add(jTBoption1);
        jTBoption1.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
        jTBoption1.setText("OPSAOAOAOAOA1");
        jTBoption1.setActionCommand("A");
        jTBoption1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTBoption1.setFocusable(false);
        jTBoption1.setVerifyInputWhenFocusTarget(false);

        jBpular.setBackground(new java.awt.Color(173, 132, 1));
        jBpular.setFont(new java.awt.Font("Noto Sans", 0, 36)); // NOI18N
        jBpular.setForeground(new java.awt.Color(254, 254, 254));
        jBpular.setText("Pular");
        jBpular.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBpular.setFocusable(false);
        jBpular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBpularActionPerformed(evt);
            }
        });

        jBconfirmar.setBackground(new java.awt.Color(5, 90, 1));
        jBconfirmar.setFont(new java.awt.Font("Noto Sans", 0, 36)); // NOI18N
        jBconfirmar.setForeground(new java.awt.Color(254, 254, 254));
        jBconfirmar.setText("Confirmar");
        jBconfirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBconfirmar.setFocusable(false);
        jBconfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBconfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpIremelLayout = new javax.swing.GroupLayout(jpIremel);
        jpIremel.setLayout(jpIremelLayout);
        jpIremelLayout.setHorizontalGroup(
            jpIremelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIremelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpIremelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTBoption5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTBoption4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTBoption3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                    .addComponent(jTBoption1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLbQuestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTBoption2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpIremelLayout.createSequentialGroup()
                        .addComponent(jBconfirmar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBpular)
                        .addGap(18, 18, 18)
                        .addComponent(jBdesistir)))
                .addContainerGap())
        );
        jpIremelLayout.setVerticalGroup(
            jpIremelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpIremelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLbQuestion)
                .addGap(18, 18, 18)
                .addComponent(jTBoption1)
                .addGap(18, 18, 18)
                .addComponent(jTBoption2)
                .addGap(18, 18, 18)
                .addComponent(jTBoption3)
                .addGap(18, 18, 18)
                .addComponent(jTBoption4)
                .addGap(18, 18, 18)
                .addComponent(jTBoption5)
                .addGap(18, 18, 18)
                .addGroup(jpIremelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBconfirmar)
                    .addComponent(jBpular)
                    .addComponent(jBdesistir))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpIremel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpIremel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBdesistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBdesistirActionPerformed
        maposo.sendMessage("<p align='center'><b>Deseja mesmo sair? Irá perder seu progresso.</b><br><a href='@exit'>Sim</a><br><a href='@cancel_exit'>Não</a></p>", this.listener);
    }//GEN-LAST:event_jBdesistirActionPerformed

    private void jBcomecarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBcomecarActionPerformed
        maposo.shut();
        start();
    }//GEN-LAST:event_jBcomecarActionPerformed

    private void jBconfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBconfirmarActionPerformed
        if (optionGroup.getSelection() != null) {
            Character optionSelected = optionGroup.getSelection().getActionCommand().charAt(0);
            boolean isCorrect = questions.get(0).checkAnswer(optionSelected);
            feedback(isCorrect);
        }
    }//GEN-LAST:event_jBconfirmarActionPerformed

    private void jBvoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBvoltarActionPerformed
        WNDTrivia.this.dispose();
    }//GEN-LAST:event_jBvoltarActionPerformed

    private void jBpularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBpularActionPerformed
        Question q = questions.get(0);
        questions.remove(0);
        questions.add(q);
        nextQuestion();
    }//GEN-LAST:event_jBpularActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBcomecar;
    private javax.swing.JButton jBconfirmar;
    private javax.swing.JButton jBdesistir;
    private javax.swing.JButton jBpular;
    private javax.swing.JButton jBvoltar;
    private javax.swing.JLabel jLbQuestion;
    private javax.swing.JLabel jLbR;
    private javax.swing.JLabel jLbTD;
    private javax.swing.JLabel jLbanswereds;
    private javax.swing.JLabel jLbtime;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jTBoption1;
    private javax.swing.JToggleButton jTBoption2;
    private javax.swing.JToggleButton jTBoption3;
    private javax.swing.JToggleButton jTBoption4;
    private javax.swing.JToggleButton jTBoption5;
    private javax.swing.JPanel jpIremel;
    private javax.swing.ButtonGroup optionGroup;
    // End of variables declaration//GEN-END:variables

    private void dispose() {
        this.setVisible(false);
        caller.exitTrivia();
    }
}
