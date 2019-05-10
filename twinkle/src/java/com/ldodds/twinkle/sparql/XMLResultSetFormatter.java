package com.ldodds.twinkle.sparql;

import java.io.*;

import javax.swing.JTextArea;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

/**
 * @author ccslrd
 */
public class XMLResultSetFormatter implements com.ldodds.twinkle.sparql.ResultSetFormatter
{
    private JTextArea _text;
    
    public XMLResultSetFormatter(JTextArea text)
    {
        _text = text;
        
    }

    public void display(Query query, ResultSet results)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ResultSetFormatter.output(out, results, com.hp.hpl.jena.sparql.resultset.ResultSetFormat.syntaxXML);
        _text.setText( out.toString() );
    }

}
