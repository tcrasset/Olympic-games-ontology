package com.ldodds.twinkle.ui.components;

import javax.swing.JPanel;

import com.hp.hpl.jena.assembler.Assembler;
import com.hp.hpl.jena.assembler.exceptions.AssemblerException;
import com.hp.hpl.jena.assembler.exceptions.CannotConstructException;
import com.hp.hpl.jena.assembler.exceptions.NoImplementationException;
import com.hp.hpl.jena.assembler.exceptions.PropertyRequiredException;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.resultset.DataException;

//FIXME better name
public class AssemblerQueryForm extends QueryForm
{
	private Resource reference;
	
	public static void add(String label, Resource reference) {
		QueryForm frame = new AssemblerQueryForm(label, reference);
		ComponentRegistry.registry.getMainWindow().getDesktop().add(frame);
	    ComponentRegistry.registry.setCurrentFrame(frame);			
	    frame.setVisible(true);
	    
	    try {
	    	frame.setSelected(true);
	    } catch (java.beans.PropertyVetoException ex) {}		
	}
	
	/**
	 * @param name title of the query form
	 * @param reference reference to the Assembler config that says 
	 * how to construct the Model
	 */
	public AssemblerQueryForm(String label, Resource reference) {
		super("Untitled", label);
		this.reference = reference;
	}
	
	@Override
	protected BasicQueryFormOptions createOptionsPanel() {
		//TEMPORARY
		options = new BasicQueryFormOptions();
		options.getDataURL().setEditable(false);
		options.getBaseURL().setEditable(false);
		return options;
	}

	public QueryExecution getQueryExecution(Query query) throws QueryException {
		
		Dataset dataSet = DatasetFactory.create( getModelToQuery() );
		return QueryExecutionFactory.create(query, dataSet);
	}

    private Model getModelToQuery() { 
    	try {
    		return Assembler.general.openModel(reference);
		} catch (NoImplementationException nie) {
			throw new DataException( "No assembler implementation", nie);
		} catch (PropertyRequiredException pre) {
			throw new DataException( "Resource incorrectly configured", pre);
		} catch (CannotConstructException e) {
			throw new DataException( "Cannot construct Model", e);
		} catch (AssemblerException e) {
			throw new DataException( "Unable to construct Model", e);
		}    	
    }	
}
