package com.ldodds.twinkle.querysource;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class QuerySourceUtils
{
	/**
	 * @param resource a resource
	 * @return the label for the resource
	 */
	public static String getLabel(Resource resource) {
		if ( resource.hasProperty(RDFS.label) ) {
			return resource.getProperty(RDFS.label).getObject().toString();
		} else {
			return resource.getURI();
		}		
	}
}
