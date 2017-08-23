/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sw.doska.model;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dmitry
 */
public class QueryEngine {

    private final JIPEngine jipEngine;
    private PrintWriter newKnowledgeWriter;
    private final File knowledgeFile;
    private final Set<String> TRUE_STRING;
    private final Set<String> FALSE_STRING;

    public QueryEngine(File knowledgeFile) {
        this.TRUE_STRING = new TreeSet<>();
        this.FALSE_STRING = new TreeSet<>();
        TRUE_STRING.add("true");
        FALSE_STRING.add("false");

        this.jipEngine = new JIPEngine();
        this.knowledgeFile = knowledgeFile;

        File file = new File("~DoskaKnowledgeData.sw");

        if (file.exists()) {
            writeBackKnowledge(file);
        }

        try {
            newKnowledgeWriter = new PrintWriter(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

        addFileKnowledge(knowledgeFile);
    }

    private void writeBackKnowledge(File file) {

        try {

            if (!knowledgeFile.exists()) {
                knowledgeFile.createNewFile();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            PrintWriter printWriter = new PrintWriter(new FileWriter(knowledgeFile, true));

            while (bufferedReader.ready()) {
                printWriter.println(bufferedReader.readLine());
            }

            printWriter.close();
            bufferedReader.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean addKnowledge(String knowledge) {
        try {
            jipEngine.assertz(jipEngine.getTermParser().parseTerm(knowledge));
            newKnowledgeWriter.println(knowledge);
            newKnowledgeWriter.flush();
            return true;
        } catch (Exception ex) {
            System.err.println("Error adding knowledge \"" + knowledge + "\"");
        }
        return false;
    }

    public boolean addFileKnowledge(File file) {
        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    if (line.length() > 0) {
                        System.out.println("Adding \"" + line + "\"");
                        jipEngine.assertz(jipEngine.getTermParser().parseTerm(line));
                    }
                }
                bufferedReader.close();
                return true;
            } catch (Exception ex) {
                System.err.println("Could not add the knowledge file.");
            }
        }
        return false;
    }

    public TreeMap<String, Set<String>> consult(String query) {
        TreeMap<String, Set<String>> treeMap = new TreeMap<>();
        try {
            JIPTerm queryTerm = jipEngine.getTermParser().parseTerm(query);
            JIPQuery jipQuery = jipEngine.openSynchronousQuery(queryTerm);
            JIPTerm solution;

            while (jipQuery.hasMoreChoicePoints()) {
                solution = jipQuery.nextSolution();
                if (solution != null) {
                    if (solution.getVariables().length > 0) {
                        JIPVariable[] vars = solution.getVariables();
                        for (JIPVariable var : vars) {
                            if (!var.isAnonymous()) {

                                Set<String> mapSet = treeMap.get(var.getName());
                                if (mapSet != null) {
                                    mapSet.add(var.toString());
                                } else {
                                    Set<String> set = new TreeSet<>();
                                    set.add(var.toString());
                                    treeMap.put(var.getName(), set);
                                }

                            }
                        }
                    } else {
                        treeMap.put("${QUERY_RESULT}", TRUE_STRING);
                    }
                } else {
                    treeMap.put("${QUERY_RESULT}", FALSE_STRING);
                }
            }

        } catch (Exception ex) {
            System.err.println("Could not parse query.");
            return null;
        }
        return treeMap;
    }

}