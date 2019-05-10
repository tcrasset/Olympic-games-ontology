package com.ldodds.twinkle.ui.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;


import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.DataException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.assembler.*;

public class BasicQueryForm extends QueryForm
{
	public static void add() {
		QueryForm frame = new BasicQueryForm();
		ComponentRegistry.registry.getMainWindow().getDesktop().add(frame);
	    ComponentRegistry.registry.setCurrentFrame(frame);			
	    frame.setVisible(true);
	    
	    try {
	    	frame.setSelected(true);
	    } catch (java.beans.PropertyVetoException ex) {}		
	}
	
//	private BasicQueryFormOptions options;
	
	public BasicQueryForm() {
		super();		
	}

	public BasicQueryForm(String name) {
		super(name);		
	}

	@Override
	protected BasicQueryFormOptions createOptionsPanel() {
		options = new BasicQueryFormOptions();
		return options;
	}
        
	public QueryExecution getQueryExecution(Query query) throws QueryException {
		
		String backgroundGraph = options.getDataURL().getText();
		
		if ( !query.hasDatasetDescription() && "".equals(backgroundGraph) ) {
			throw new DataException("No dataset in query");
		}
		
		if ( ! query.hasDatasetDescription() ) {
	        List<String> dataUrls = new ArrayList<String>();		
	        dataUrls.add( backgroundGraph );
	        //FIXME this may trigger HTTP gets, so should be in SwingWorker
	        Dataset dataSet = DatasetFactory.create(dataUrls, Collections.EMPTY_LIST, FileManager.get(), 
	        		options.getBaseURL().getText() ) ;
	        return QueryExecutionFactory.create(query, dataSet);
		}
		System.err.println("Query has data set");
        return QueryExecutionFactory.create(query);
	}

}
