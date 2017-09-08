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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruankotovich 
 * @github https://github.com/ruankotovich
 */
public class QueryEngine {

    private final JIPEngine jipEngine;
    private PrintWriter newKnowledgeWriter;
    private final File knowledgeFile;
    private final TreeMap<String, String> TRUE_STRING;
    private final TreeMap<String, String> FALSE_STRING;
        
    public QueryEngine(File knowledgeFile) {
        this.TRUE_STRING = new TreeMap<>();
        this.FALSE_STRING = new TreeMap<>();
        TRUE_STRING.put("${BOOL_VALUE}", "true");
        FALSE_STRING.put("${BOOL_VALUE}", "false");

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

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
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
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
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

    public String consultFirst(String query, String variable) {
        TreeMap<Integer, TreeMap<String, String>> result = consult(query);
        TreeMap<String, String> response = result.get(1);

        if (response != null) {
            return response.get(variable);
        }

        return null;
    }

    public TreeMap<Integer, TreeMap<String, String>> consult(String query) {
        TreeMap<Integer, TreeMap<String, String>> treeMap = new TreeMap<>();

        try {
            JIPTerm queryTerm = jipEngine.getTermParser().parseTerm(query);
            JIPQuery jipQuery = jipEngine.openSynchronousQuery(queryTerm);
            JIPTerm solution;
            int count = 0;
            while (jipQuery.hasMoreChoicePoints()) {
                solution = jipQuery.nextSolution();
                if (solution != null) {
                    if (solution.getVariables().length > 0) {
                        JIPVariable[] vars = solution.getVariables();
                        TreeMap<String, String> mapSet = new TreeMap<>();
                        treeMap.put(++count, mapSet);
                        for (JIPVariable var : vars) {
                            if (!var.isAnonymous()) {
                                mapSet.put(var.getName(), var.toString());
                            }
                        }
                    } else {
                        treeMap.put(0, TRUE_STRING);
                    }
                } else {
                    treeMap.put(0, FALSE_STRING);
                }
            }

        } catch (Exception ex) {
            System.err.println("Could not parse query.");
            ex.printStackTrace();
            return null;
        }
        return treeMap;
    }

}
