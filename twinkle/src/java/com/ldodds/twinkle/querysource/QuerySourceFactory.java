package com.ldodds.twinkle.querysource;

import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public interface QuerySourceFactory
{
	/**
	 * @return a Set of Resources
	 */
	Set<Resource> getQuerySources();

	Set<Resource> getMemoryQuerySources();
	
	Set<Resource> getInferencingSources();
	
	Set<Resource> getRDBQuerySources();
	
	Set<Resource> getEndpoints();
	
	/**
	 * @return a Set of data source names
	 */
	Set<String> getQuerySourceNames();
	
	
	/**
	 * @return the Model holding the underlying query sources
	 */
	Model getSources();
}
