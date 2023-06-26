/*
 * Copyright 2009 Sandia Corporation.  Under the terms of Contract 
 * DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government retains 
 * certain rights in this software.
 * Hemlock is distributed under a BSD License.  See LICENSE for details.
 *
 * Authors:             Sean Gilpin, Daniel Dunlavy
 * Company:             Sandia National Laboratories
 * Project:             HEMLOCK
 */

package gov.sandia.hemlock.tests;

import org.junit.*;
import java.io.*;

public class Metrics extends ExperimentTest
{
	@Test 
	public void testRandomTreeROC() throws Exception
	{
		String inputFileName = "tests/experiments/Metrics/kFoldRandomTreeROC";
		String outputFileName = "tests/experiments/results/Metrics/Metrics";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Metrics/", 
			"Metrics_kFoldRandomTreeROC.", 
			"tests/correct-outputs/Metrics_kFoldRandomTreeROC.20090727140119.xml");
	}
	
	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/Metrics", null, new File("."));
	}
}
