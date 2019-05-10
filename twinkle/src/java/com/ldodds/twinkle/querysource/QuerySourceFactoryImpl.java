package com.ldodds.twinkle.querysource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hp.hpl.jena.assembler.AssemblerHelp;
import com.hp.hpl.jena.assembler.JA;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class QuerySourceFactoryImpl implements QuerySourceFactory
{
	private Model sources;
	
	public QuerySourceFactoryImpl(Model sources) {
		this.sources = sources;
	}

	public Set<Resource> getQuerySources() {
		return (Set<Resource>)AssemblerHelp.findAssemblerRoots(sources, JA.Model);
	}

	public Set<Resource> getMemoryQuerySources() {
		return (Set<Resource>)AssemblerHelp.findAssemblerRoots(sources, JA.MemoryModel);
	}

	public Set<Resource> getRDBQuerySources() {
		return (Set<Resource>)AssemblerHelp.findAssemblerRoots(sources, JA.RDBModel);
	}
	
	public Set<Resource> getInferencingSources() {
		return (Set<Resource>)AssemblerHelp.findAssemblerRoots(sources, JA.InfModel);
	}
	
	public Set<Resource> getEndpoints() {
		return (Set<Resource>)AssemblerHelp.findAssemblerRoots(sources, SOURCES.ENDPOINT);
	}
	
	public Set<String> getQuerySourceNames() {
		Set<Resource> sources = getQuerySources();
		Set<String> names = new HashSet<String>( sources.size() );
		
		for (Iterator<Resource> iter = sources.iterator(); iter.hasNext(); ) {
			names.add( QuerySourceUtils.getLabel(iter.next()) );
		}
		return names;
	}

	public Model getSources() {
		return sources;
	}
}
