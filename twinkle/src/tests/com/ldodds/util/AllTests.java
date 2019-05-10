package com.ldodds.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.ldodds.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(FileFiltersTest.class);
		//$JUnit-END$
		return suite;
	}

}
