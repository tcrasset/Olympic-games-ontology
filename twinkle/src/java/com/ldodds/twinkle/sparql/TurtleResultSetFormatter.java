package com.ldodds.twinkle.sparql;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.swing.JTextArea;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.sparql.resultset.ResultsFormat;

/**
 * @author ccslrd
 */
public class TurtleResultSetFormatter implements com.ldodds.twinkle.sparql.ResultSetFormatter
{
    private JTextArea _text;
    
    public TurtleResultSetFormatter(JTextArea text)
    {
        _text = text;
        
    }

    public void display(Query query, ResultSet results)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ResultSetFormatter.output(out, results, com.hp.hpl.jena.sparql.resultset.ResultSetFormat.syntaxRDF_TURTLE);
        _text.setText( out.toString() );
    }

}
