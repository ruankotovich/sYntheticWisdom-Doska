/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.model;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author dmitry
 */
public class Question {

    private final String question;
    private final TreeMap<Character, String> options;
    private char answer;

    public Question(String question) {
        this.question = question;
        this.options = new TreeMap<>();
    }

    public boolean checkAnswer(char answer) {
        return answer == this.answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }

    public void addOption(char letter, String option) {
        if (options.size() < 5) {
            options.put(letter, option);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.question).append("\n\n");
        for (Map.Entry<Character, String> options : options.entrySet()) {
            builder.append(options.getKey()).append(") ").append(options.getValue()).append(checkAnswer(options.getKey()) ? "*\n" : "\n");
        }
        return builder.toString();
    }

    public String getQuestion() {
        return question;
    }

    public TreeMap<Character, String> getOptions() {
        return options;
    }

    public char getAnswer() {
        return answer;
    }

}
