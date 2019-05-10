package com.ldodds.twinkle.ui.components;

import java.util.*;

import javax.swing.JTable;
import javax.swing.table.*;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * @author ccslrd
 * FIXME turn into JTable sub-class?
 */
public class JTableResultsSetFormatter
{
    public void display(QueryInterface queryFrame, ResultSet results)
    {
        final JTable table = queryFrame.getResultsTable();        
        DefaultTableModel tableModel = new DefaultTableModel(getData(results), getColumns(results));
        table.setModel(tableModel);
        table.repaint();
    }

    private Vector getData(ResultSet data)
    {
        Vector results = new Vector();
        for (; data.hasNext(); )
        {
            QuerySolution qs = data.nextSolution();
            results.add( getRowData(data, qs) );
        }
        return results;
    }
    
    private Vector getRowData(ResultSet results, QuerySolution qs)
    {
        Vector row = new Vector();
        for (Iterator iter = results.getResultVars().iterator() ; iter.hasNext() ; )
        {
            String var = (String)iter.next();
            row.add( getValueAsString(qs, var) );
        }
        return row;
    }
    
    private String getValueAsString(QuerySolution qs, String var)
    {
        RDFNode result = qs.get(var);
        if (result == null)
        {
            return "";
        }
        return result.toString();
    }
    private Vector getColumns(ResultSet results)
    {
        Vector cols = new Vector();
        cols.addAll(results.getResultVars());
        return cols;
    }
}
