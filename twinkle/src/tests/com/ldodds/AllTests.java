package com.ldodds;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.ldodds");
		
		suite.addTest( com.ldodds.twinkle.querysource.AllTests.suite() );
		suite.addTest( com.ldodds.util.AllTests.suite() );
		//$JUnit-BEGIN$

		//$JUnit-END$
		return suite;
	}

}
