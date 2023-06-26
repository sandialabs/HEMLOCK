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

import gov.sandia.hemlock.evaluation.AccuracyTable;

import java.io.*;

import org.junit.*;

public class Ensembles extends ExperimentTest
{	
	@Test 
	public void hetero_top66_Voting() throws Exception
	{
		String inputFileName = "tests/experiments/Ensembles/hetero_top66_Voting";
		String outputFileName = "tests/experiments/results/Ensembles/Ensembles";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Ensembles/", 
			"Ensembles_hetero_top66_Voting", 
			"tests/correct-outputs/Ensembles_hetero_top66_Voting.20090701103928.xml");
	}
	
	@Test 
	public void hetero_top66_LinearRegression() throws Exception
	{
		String inputFileName = "tests/experiments/Ensembles/hetero_top66_LinearRegression";
		String outputFileName = "tests/experiments/results/Ensembles/Ensembles";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Ensembles/", 
			"Ensembles_hetero_top66_LinearRegression.", 
			"tests/correct-outputs/Ensembles_hetero_top66_LinearRegression.20090727133217.xml");
	}
	
	@Test 
	public void hetero_top66_SumRule() throws Exception
	{
		String inputFileName = "tests/experiments/Ensembles/hetero_top66_SumRule";
		String outputFileName = "tests/experiments/results/Ensembles/Ensembles";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Ensembles/", 
			"Ensembles_hetero_top66_SumRule", 
			"tests/correct-outputs/Ensembles_hetero_top66_SumRule.20090701103930.xml");
	}
	
	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/Ensembles", null, new File("."));
	}
	

}
