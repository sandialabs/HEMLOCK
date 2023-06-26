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

import java.io.File;
import java.io.PrintWriter;

import org.junit.*;

public class BaggingEnsembles extends ExperimentTest
{	
	@Test 
	public void hetero_top1_Voting_Bagging() throws Exception
	{
		String inputFileName = "tests/experiments/BaggingEnsembles/hetero_top1_Voting";
		String outputFileName = "tests/experiments/results/BaggingEnsembles/BaggingEnsembles";
		runExperimentFile(inputFileName, outputFileName);
		
		
		isOutputFileCorrect("tests/experiments/results/BaggingEnsembles/", 
			"BaggingEnsembles_hetero_top1_Voting.", 
			"tests/correct-outputs/BaggingEnsembles_hetero_top1_Voting.20090701093239.xml");
	}
	
	@Test 
	public void hetero_top1_SumRule_Bagging() throws Exception
	{
		String inputFileName = "tests/experiments/BaggingEnsembles/hetero_top1_SumRule";
		String outputFileName = "tests/experiments/results/BaggingEnsembles/BaggingEnsembles";
		runExperimentFile(inputFileName, outputFileName);
		
		isOutputFileCorrect("tests/experiments/results/BaggingEnsembles/", 
			"BaggingEnsembles_hetero_top1_SumRule.", 
			"tests/correct-outputs/BaggingEnsembles_hetero_top1_SumRule.20090701093240.xml");
	}
	
	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/BaggingEnsembles", null, new File("."));
	}
	
}
