package com.ldodds.util;

import junit.framework.TestCase;
import java.io.File;
import javax.swing.filechooser.*;

public class FileFiltersTest extends TestCase
{
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FileFiltersTest.class);
	}

	public FileFiltersTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'com.ldodds.util.FileFilters.getExtension(File)'
	 */
	public void testGetExtension() {
		File file = new File("foo.rq");
		assertEquals("rq", FileFilters.getExtension(file) );
		assertEquals(null, FileFilters.getExtension(null) );
		assertEquals(null, FileFilters.getExtension( new File("foo") ) );
		assertEquals("ttl", FileFilters.getExtension( new File("test.ttl") ) );
	}

	/*
	 * Test method for 'com.ldodds.util.FileFilters.getFileFilter(String, String)'
	 */
	public void testGetFileFilter() {
		FileFilter filter = FileFilters.getFileFilter("rq", "SPARQL Query");
		
		assertEquals( "SPARQL Query", filter.getDescription() );
		
		//accept dir
		assertTrue( filter.accept( File.listRoots()[0] ) );
		assertTrue( filter.accept( new File("test.rq") ) );
		assertTrue( !filter.accept( new File("test.txt") ) );
		
	}

}
