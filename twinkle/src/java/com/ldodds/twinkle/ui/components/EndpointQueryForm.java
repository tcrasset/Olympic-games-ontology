package com.ldodds.twinkle.ui.components;

import javax.swing.JPanel;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.ldodds.twinkle.querysource.SOURCES;

public class EndpointQueryForm extends QueryForm
{
	public static void add(String label, Resource reference) {
		QueryForm frame = new EndpointQueryForm(label, reference);
		ComponentRegistry.registry.getMainWindow().getDesktop().add(frame);
	    ComponentRegistry.registry.setCurrentFrame(frame);			
	    frame.setVisible(true);
	    
	    try {
	    	frame.setSelected(true);
	    } catch (java.beans.PropertyVetoException ex) {}		
	}
	
	private Resource reference;
	
	public EndpointQueryForm(String label, Resource reference) {
		super("Untitled", label);
		this.reference = reference;
	}

	@Override
	protected BasicQueryFormOptions createOptionsPanel() {
		//TEMPORARY
		options = new BasicQueryFormOptions();
		options.getBaseURL().setEditable(false);
		return options;
	}

	public QueryExecution getQueryExecution(Query query) throws QueryException {
		
		//if user specified a dataset, then use that
		if ( query.hasDatasetDescription() ) {
			return QueryExecutionFactory.sparqlService(reference.getURI(), query);
		}

		//if they've edited the box, then use that.
		String dataUrl = options.getDataURL().getText();
		if ( dataUrl != null ) {
			QueryExecutionFactory.sparqlService(reference.getURI(), query, dataUrl);
		}
		//if there's not a default graph in the config, then just send the query
		if ( !reference.hasProperty( SOURCES.DEFAULT_GRAPH ) ) {
			return QueryExecutionFactory.sparqlService(reference.getURI(), query);
		}
		//use the configured default
		String defaultGraph = reference.getProperty(SOURCES.DEFAULT_GRAPH).getObject().toString();
		return QueryExecutionFactory.sparqlService(reference.getURI(), query, defaultGraph);
	}

}
