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
 * This is one of the test classes.  All of the methods annotated with @Test
 * will run as a test.  The method annotated with @BeforeClass, 
 * {@link #getDataSetInfo() getDataSetInfo()}, will run once before all of the 
 * tests.
 *
 */
public class Evaluation extends ExperimentTest
{

	@Test 
	public void testNoHoldOutSVM() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutSVM";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutSVM.", 
			"tests/correct-outputs/Evaluation_noHoldOutSVM.20090701113045.xml");
	}
	
	@Test 
	public void testNoHoldOutSVMROC() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutSVMROC";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutSVMROC.", 
			"tests/correct-outputs/Evaluation_noHoldOutSVMROC.20090727132006.xml");
	}
	
	@Test 
	public void testKFoldsWithKey() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/kFoldsWithKey";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_kFoldsWithKey.", 
			"tests/correct-outputs/Evaluation_kFoldsWithKey.20090701113046.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityDisagreement() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityDisagreement";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityDisagreement.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityDisagreement.20090727132429.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityCorrelation() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityCorrelation";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityCorrelation.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityCorrelation.20090727131507.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityYuleQ() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityYuleQ";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityYuleQ.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityYuleQ.20090727132712.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityDoubleFault() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityDoubleFault";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityDoubleFault.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityDoubleFault.20090727131508.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityEntropy() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityEntropy";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityEntropy.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityEntropy.20090727131144.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityGeneralDiversity() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityGeneralDiversity";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityGeneralDiversity.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityGeneralDiversity.20090727125029.xml");
		
	}
	
	@Test
	public void testNoHoldOutDiversityCoincidentFailure() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityCoincidentFailure";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityCoincidentFailure.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityCoincidentFailure.20090727130606.xml");
	}
	
	@Test
	public void testNoHoldOutDiversityDifficulty() throws Exception
	{
		String inputFileName = "tests/experiments/Evaluation/noHoldOutDiversityDifficulty";
		String outputFileName = "tests/experiments/results/Evaluation/Evaluation";
		runExperimentFile(inputFileName, outputFileName);
		isOutputFileCorrect("tests/experiments/results/Evaluation/", 
			"Evaluation_noHoldOutDiversityDifficulty.", 
			"tests/correct-outputs/Evaluation_noHoldOutDiversityDifficulty.20090727122912.xml");
		
	}
	
	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/Evaluation", null, new File("."));
	}
}
