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

import java.io.*;

import org.junit.*;

public class Data extends ExperimentTest
{	
	@Test 
	public void auto() throws Exception
	{
		String inputFileName = "tests/experiments/Data/auto";
		String outputFileName = "tests/experiments/results/Data/Data";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Data/", 
			"Data_auto.", 
			"tests/correct-outputs/Data_auto.20090701095330.xml");
	}
	
	@Test 
	public void autoWithRepositoryName() throws Exception
	{
		String inputFileName = "tests/experiments/Data/autoWithRepositoryName";
		String outputFileName = "tests/experiments/results/Data/Data";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Data/", 
			"Data_autoWithRepositoryName.", 
			"tests/correct-outputs/Data_autoWithRepositoryName.20090701095330.xml");
	}
	
	@Test 
	public void manual_From_Repository() throws Exception
	{
		String inputFileName = "tests/experiments/Data/manual_From_Repository";
		String outputFileName = "tests/experiments/results/Data/Data";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Data/", 
			"Data_manual_From_Repository.", 
			"tests/correct-outputs/Data_manual_From_Repository.20090701095330.xml");
	}
	
	@Test 
	public void manual_From_Path() throws Exception
	{
		String inputFileName = "tests/experiments/Data/manual_From_Path";
		String outputFileName = "tests/experiments/results/Data/Data";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Data/", 
			"Data_manual_From_Path.", 
			"tests/correct-outputs/Data_manual_From_Path.20090701095330.xml");
	}
	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/Data", null, new File("."));
	}
	
}
