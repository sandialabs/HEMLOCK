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

/**
 * 
 * This is one of the test classes.  All of the methods annotated with @Test will run as a test.  The method annotated with @BeforeClass, {@link #getDataSetInfo() getDataSetInfo()}, will run once before all of the tests.
 *
 */
public class WekaInterface extends ExperimentTest
{

	@Test
	public void testKNearestNeighbor() throws Exception
	{
		String inputFileName = "tests/experiments/WekaInterface/kFoldNearestNeighbor";
		String outputFileName = "tests/experiments/results/WekaInterface/WekaInterface";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/WekaInterface/", 
			"WekaInterface_kFoldNearestNeighbor.", 
			"tests/correct-outputs/WekaInterface_kFoldNearestNeighbor.20090727143651.xml");
	}
	
	
	@Test 
	public void testRipper() throws Exception
	{
		String inputFileName = "tests/experiments/WekaInterface/kFoldRipper";
		String outputFileName = "tests/experiments/results/WekaInterface/WekaInterface";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/WekaInterface/", 
			"WekaInterface_kFoldRipper.", 
			"tests/correct-outputs/WekaInterface_kFoldRipper.20090727143651.xml");
	}

	@Test 
	public void testSVM() throws Exception
	{
		String inputFileName = "tests/experiments/WekaInterface/kFoldSVM";
		String outputFileName = "tests/experiments/results/WekaInterface/WekaInterface";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/WekaInterface/", 
			"WekaInterface_kFoldSVM.", 
			"tests/correct-outputs/WekaInterface_kFoldSVM.20090727143651.xml");
	}
	
	@Test 
	public void testNaiveBayesian() throws Exception
	{
		String inputFileName = "tests/experiments/WekaInterface/kFoldNaiveBayesian";
		String outputFileName = "tests/experiments/results/WekaInterface/WekaInterface";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/WekaInterface/", 
			"WekaInterface_kFoldNaiveBayesian.", 
			"tests/correct-outputs/WekaInterface_kFoldNaiveBayesian.20090729081335.xml");
	}
	

	
	@Test 
	public void testRandomTree() throws Exception
	{
		String inputFileName = "tests/experiments/WekaInterface/kFoldRandomTree";
		String outputFileName = "tests/experiments/results/WekaInterface/WekaInterface";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/WekaInterface/", 
			"WekaInterface_kFoldRandomTree.", 
			"tests/correct-outputs/WekaInterface_kFoldRandomTree.20090727143652.xml");
	}

	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/WekaInterface", null, new File("."));
	}
}



