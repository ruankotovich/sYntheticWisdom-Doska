/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ruankotovich 
 * @github https://github.com/ruankotovich
 */
public class Query {

    public void main2(String args[]) {
        // New instance of prolog engine
        JIPEngine jip = new JIPEngine();

        JIPTerm queryTerm = null;

        // parse query
        try {
            // consult file
            //jip.consultStream(in, string);
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new FileReader("./relacoes.pl"));

            while (bufferedReader.ready()) {
                jip.assertz(jip.getTermParser().parseTerm(bufferedReader.readLine()));
            }

            queryTerm = jip.getTermParser().parseTerm("canHaveAWedding(Father, Child)");
        } catch (JIPSyntaxErrorException ex) {
            System.exit(0); // needed to close threads by AWT if shareware
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        // needed to close threads by AWT if shareware

        // open Query
        JIPQuery jipQuery = jip.openSynchronousQuery(queryTerm);
        JIPTerm solution;

        // Loop while there is another solution
        while (jipQuery.hasMoreChoicePoints()) {
            solution = jipQuery.nextSolution();
            if (solution != null) {
                if (solution.getVariables().length > 0) {
                    JIPVariable[] vars = solution.getVariables();
                    StringBuilder stringB = new StringBuilder();
                    for (JIPVariable var : vars) {
                        if (!var.isAnonymous()) {
                            stringB.append(var.getName()).append(" = ").append(var.toString(jip)).append("\n");
                        }
                    }
                    JOptionPane.showMessageDialog(null, stringB);

                }
            }
        }
    }
}
