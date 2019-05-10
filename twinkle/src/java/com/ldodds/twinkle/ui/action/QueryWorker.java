package com.ldodds.twinkle.ui.action;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.swing.JTabbedPane;

import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.DataException;
import com.hp.hpl.jena.util.FileUtils;
import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.JTableResultsSetFormatter;
import com.ldodds.twinkle.ui.components.QueryInterface;

public class QueryWorker extends SwingWorker
{
	private QueryInterface queryInterface;
	private Query query;
	private QueryExecution queryExecution;
	
	public QueryWorker(QueryInterface queryInterface) {
		this.queryInterface = queryInterface;
		queryInterface.setSwingWorker(this);   		
	}

	@Override
	protected Object doInBackground() throws Exception {
		return doQuery();
	}
		
    private Object doQuery() {    	
        query = null;
        try {
        	query = queryInterface.getQuery();
        } catch (QueryException qe1) {
        	//FIXME add catch for DataException
        	ErrorInfo errorInfo = new ErrorInfo("Query Error", qe1.getMessage(), "", 
        			null, qe1, Level.SEVERE, null);
        	JXErrorPane.showDialog(
        			(java.awt.Component)ComponentRegistry.registry.getMainWindow(), errorInfo);        	
        	qe1.printStackTrace();
        	return null;
        }
        
        queryExecution = null;
        try {
        	queryExecution = queryInterface.getQueryExecution(query);
        } catch (DataException de) {
        	Throwable toReport = de.getCause() != null ? de.getCause() : de;
        	String msg = toReport.getMessage();
        	if ( msg != null && msg.indexOf("Interrupt detected") != -1 ) {
        		//then this exception thrown by XML Handler because we've
        		//cancelled, so ignore it
        		return null;
        	}
        	ErrorInfo errorInfo = new ErrorInfo("Data Error", toReport.getMessage(), "", 
        			null, toReport, Level.SEVERE, null);
        	JXErrorPane.showDialog(
        			(java.awt.Component)ComponentRegistry.registry.getMainWindow(), errorInfo);        	
        	toReport.printStackTrace();
        	return null;
        	
        } catch (QueryException qe2) {
        	//FIXME try and differentiate RDF errors from query errors
        	ErrorInfo errorInfo = new ErrorInfo("Query Execution Error", qe2.getMessage(), "", 
        			null, qe2, Level.SEVERE, null);
        	JXErrorPane.showDialog(
        			(java.awt.Component)ComponentRegistry.registry.getMainWindow(), errorInfo);        	
        	qe2.printStackTrace();
        	return null;
        }
        return execute(queryExecution);
    }
    
    private Object execute(QueryExecution queryExecution) 
    	throws QueryException
    {
        try
        {
        	if ( query.isSelectType() ) {
        		return queryExecution.execSelect();
        	}
        	else if ( query.isAskType() ) {
        		return Boolean.valueOf( queryExecution.execAsk() );
        	}
        	else if ( query.isConstructType() ) {
        		return queryExecution.execConstruct();
        	}
        	else {
        		return queryExecution.execDescribe();
        	}
        }
        catch (com.hp.hpl.jena.sparql.ARQInternalErrorException intEx)
        {
            //TEMPORARY... do we still need this?
            if (intEx.getCause() != null)
            {
                System.err.println("Cause:");
                intEx.getCause().printStackTrace(System.err);
                System.err.println();
            }
            intEx.printStackTrace(System.err);
            throw intEx;
        }
        catch (QueryException qEx)
        {
        	//FIXME error reporting
            qEx.printStackTrace();
            throw qEx;
        }
        catch (Exception ex) {
        	//FIXME error reporting        	
            ex.printStackTrace();
            throw new QueryException("Unexpected error executing query", ex);
        }
    }
	
	protected void done() {		
		try {
			if ( this.isCancelled() ) {
				return;
			}
			
			Object o = get();
			
			if (o == null) {
				//we've already shown error dialog
				return;
			}
        	if ( query.isSelectType() ) {
        		JTabbedPane panel = queryInterface.getTabbedPanel();
        		int index = panel.indexOfTab("table");
        		if (index != -1) {
        			panel.setSelectedIndex(index);        			
        		}
        		ResultSet results = (ResultSet)o;
    	        results = ResultSetFactory.makeRewindable(results);
    	        JTableResultsSetFormatter formatter = new JTableResultsSetFormatter();    	        
    	        formatter.display(queryInterface, results);    	        
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ResultSetFormatter.out(bOut, results);        
                queryInterface.getTextResultsBox().setText(bOut.toString());            	        
        	}
        	else if ( query.isAskType() ) {
        		Boolean result = (Boolean)o;
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ResultSetFormatter.out(bOut, result);        
                queryInterface.getTextResultsBox().setText(bOut.toString());        
        	}
        	else  {
        		Model results = (Model)o;
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                PrintWriter out = FileUtils.asPrintWriterUTF8(bOut);
                //TODO how do we choose output format?
                results.write(out, "RDF/XML", null);
                out.flush();
                out.close();        
                queryInterface.getTextResultsBox().setText(bOut.toString());
        	}			
		} catch (InterruptedException ie) {
			//cancelled?
			ie.printStackTrace();
		} catch (Exception e) {
        	ErrorInfo errorInfo = new ErrorInfo("Query Execution Error", e.getMessage(), "", 
        			null, e, Level.SEVERE, null);
        	JXErrorPane.showDialog(
        			(java.awt.Component)ComponentRegistry.registry.getMainWindow(), errorInfo);        	
			e.printStackTrace();
		} finally {
			queryInterface.setSwingWorker(null);
			if (queryExecution != null) {
				queryExecution.close();
			}
			queryInterface.setBusy(false);
		}
	}	
    
}
