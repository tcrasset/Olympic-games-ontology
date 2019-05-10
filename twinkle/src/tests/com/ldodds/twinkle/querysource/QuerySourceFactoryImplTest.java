package com.ldodds.twinkle.querysource;

import java.io.InputStream;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.ldodds.twinkle.querysource.QuerySourceFactory;
import com.ldodds.twinkle.querysource.QuerySourceFactoryImpl;

import junit.framework.TestCase;

public class QuerySourceFactoryImplTest extends TestCase
{
	private Model sources;
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(QuerySourceFactoryImplTest.class);
	}

	public QuerySourceFactoryImplTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		InputStream in = getClass().getResourceAsStream("datasource.n3");
		sources = ModelFactory.createDefaultModel();
		sources.read(in, "", "N3");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetQuerySources() throws Exception {
		QuerySourceFactory factory = new QuerySourceFactoryImpl(sources);
		Set<Resource> found = factory.getQuerySources();
		assertNotNull(found);
		assertEquals(3, found.size() );		
	}
	
	public void testGetQuerySourceNames() throws Exception {
		QuerySourceFactory factory = new QuerySourceFactoryImpl(sources);
		Set<String> found = factory.getQuerySourceNames();
		assertNotNull(found);
		assertEquals(3, found.size() );		
		assertTrue( found.contains("Memory Model") );
		assertTrue( found.contains("Memory Model with Remote") );
		assertTrue( found.contains("Database Model") );
	}
	
	public void testGetSources() throws Exception {
		QuerySourceFactory factory = new QuerySourceFactoryImpl(sources);		
		Model other = factory.getSources();
		assertNotNull(other);
		assertEquals(sources, other);
	}
}
