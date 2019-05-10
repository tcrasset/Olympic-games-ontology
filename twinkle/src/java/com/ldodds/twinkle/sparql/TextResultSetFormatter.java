package com.ldodds.twinkle.sparql;

import java.io.*;
import javax.swing.JTextArea;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.resultset.ResultSetFormat;

/**
 * @author ccslrd
 */
public class TextResultSetFormatter implements com.ldodds.twinkle.sparql.ResultSetFormatter
{
    private JTextArea _text;
    
    public TextResultSetFormatter(JTextArea text)
    {
        _text = text;
    }
    
    public void display(Query query, ResultSet results)
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        
        ResultSetFormatter.output(bOut, results, ResultSetFormat.syntaxText);
        try
        {
            bOut.close();
        } catch (IOException ioe)
        {
            
        }
        try
        {
            _text.setText( bOut.toString("UTF-8") );
        } catch (UnsupportedEncodingException ue)
        {
            //will never happen
        } 
    }

}
