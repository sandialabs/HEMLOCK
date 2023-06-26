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

import gov.sandia.hemlock.data.DataSetInfo;
import gov.sandia.hemlock.data.DataSetInfoEvaluator;
import gov.sandia.hemlock.experiment.Experiment;
import gov.sandia.hemlock.experiment.ExperimentReader;
import gov.sandia.hemlock.main.RunExperiment;
import java.io.*;
import java.util.*;
import org.junit.*;

public class ExperimentTest
{
	public static DataSetInfo[] info;

	@BeforeClass
	public static void getDataSetInfo() throws Exception
	{
		info = DataSetInfoEvaluator.getInfoForAllDataSets();
	}
	
	public void runExperimentFile(String inputFileName, String outputFileName) throws Exception
	{
		
		//Make sure output path exists
		//RunExperiment.isInputValid(inputFileName);
		RunExperiment.isOutputValid(outputFileName);
		
		Experiment[] experiments 
			= ExperimentReader.readExperimentFile(inputFileName, info);
		
		for(int i = 0; i < experiments.length; i++)
		{
			experiments[i].runExperiment(outputFileName);
		}
	}
	
	public void isOutputFileCorrect(String directory, final String prefix, String correctFile) throws Exception 
	{
		//compare output 
		File correct = new File(correctFile);
		
		File actual_Dir = new File(directory);
		File actual = actual_Dir.listFiles(new FilenameFilter() {public boolean accept(File f, String s) { return s.startsWith(prefix); }})[0];
		
		FileReader fr_c = new FileReader(correct);
		ArrayList<Character> ar_c = new ArrayList<Character>();
		while(fr_c.ready())
			ar_c.add((char)fr_c.read());
		fr_c.close();
		
		FileReader fr_a = new FileReader(actual);
		ArrayList<Character> ar_a = new ArrayList<Character>();
		while(fr_a.ready())
			ar_a.add((char)fr_a.read());
		fr_a.close();
		
		Assert.assertArrayEquals(ar_c.toArray(), ar_a.toArray());
	}
}
