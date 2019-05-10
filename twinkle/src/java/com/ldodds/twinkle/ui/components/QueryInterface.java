package com.ldodds.twinkle.ui.components;

import java.io.File;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.jdesktop.swingworker.SwingWorker;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Basic methods for extracting the key query 
 * objects from a QueryFrame sub-class
 * 
 * @author ccslrd
 */
public interface QueryInterface {
	
	String getQueryString();
	void setQueryString(String query);
	Query getQuery() throws QueryException;
	
	JTextArea getTextResultsBox();
	JTable getResultsTable();
	
	QueryExecution getQueryExecution(Query query) throws QueryException;
	void setSwingWorker(SwingWorker worker);
	SwingWorker getSwingWorker();
	
	void setFile(File file);
	File getFile();
	
	JTabbedPane getTabbedPanel();
	void setBusy(boolean busy);
}
