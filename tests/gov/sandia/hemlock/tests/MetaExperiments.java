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
import gov.sandia.hemlock.experiment.MetaExperiment;
import java.io.*;
import org.junit.Test;

public class MetaExperiments extends ExperimentTest
{

	@Test
	public void processOptimizeRandomTree() throws Exception
	{
		MetaExperiment me = new MetaExperiment();
		me.createExperiments("tests/meta-experiments/optimize_RandomTree", 
				"tests/experiments/BaseClassifierParameterSweep/optimize_RandomTree");
	}
	
	@Test
	public void runOptimizeRandomTree() throws Exception
	{
		//delete all old files
		File folder = new File("tests/experiments/results/opt/randomtree/");
		File[] files = folder.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			files[i].delete();
		}
		
		//run experiment
		String inputFileName = "tests/experiments/BaseClassifierParameterSweep/optimize_RandomTree";
		String outputFileName = "tests/experiments/results/opt/randomtree/test";
		runExperimentFile(inputFileName, outputFileName);
	}
	
	@Test
	public void createRandomTreeAccuracyTable() throws Exception
	{
		AccuracyTable at = new AccuracyTable();
		at.createTable("tests/experiments/results/opt/randomtree/");
		String table = at.writeTableAsCSV();
		File oFile = new File("tests/experiments/results/opt/randomtree/randomtreeAccuracy.csv");
		PrintWriter pw = new PrintWriter(oFile);
		pw.print(table);
		pw.close();
	}

}
