package com.ldodds.twinkle.querysource;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.ldodds.sparql.querysource");
		//$JUnit-BEGIN$
		suite.addTestSuite(QuerySourceFactoryImplTest.class);
		//$JUnit-END$
		return suite;
	}

}
