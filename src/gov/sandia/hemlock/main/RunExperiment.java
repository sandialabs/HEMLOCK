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

package gov.sandia.hemlock.main;
import gov.sandia.hemlock.data.DataSetInfo;
import gov.sandia.hemlock.data.DataSetInfoEvaluator;
import gov.sandia.hemlock.experiment.*;
import java.io.*;

/**
 * Command line interface for Hemlock.
 *
 * @author Sean A. Gilpin
 */
public class RunExperiment
{
	/**
	 * Executes experiments in an experiment file that is given as an
	 * argument and outputs the results to the path that is given as an
	 * argument.
	 *
	 * @param args Command line arguments.  First entry is the path to
	 * 	an experiment file, and the second entry is the path and
	 *	prefix for writing the results to.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{	
		//input experiment file name
		String inputFileName = args[0].trim();
		if(!isInputValid(inputFileName))
			System.err.println("Cannot open file " + inputFileName);
		
		//output path and file name prefix
		String outputFileName = args[1].trim();
		if(!isOutputValid(outputFileName))
			System.err.println("Cannot write to the specified "
				+ "location.  Make sure directory exists");
		
		DataSetInfo[] info = DataSetInfoEvaluator.getInfoForAllDataSets();
		Experiment[] experiments = ExperimentReader.readExperimentFile(inputFileName,info);
		
		for(int i = 0; i < experiments.length; i++)
		{
			experiments[i].runExperiment(outputFileName);
			experiments[i] = null; //Let experiment be garbage collected
		}
		
	}
	
	/**
	 * Checks to see if the path to the experiments file is valid by
	 * checking if it is possible to read from the path.
	 *
	 * @param inputFileName path to input experiment file.
	 * @return True if its possible to read from the path.
	 */
	public static boolean isInputValid(String inputFileName)
	{
		try
		{
			File f = new File(inputFileName);
			return f.canRead();
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Check to see if the output path is valid.  If the directory
	 * exists it is valid and if it doesn't, it is still valid
	 * if the directory can be created which it will do.
	 *
	 * @param outputFileName output path which the results will be 
	 * 	written too with prefix appended.
	 * @return True if the directory in the path exists or can be created.
	 */
	public static boolean isOutputValid(String outputFileName)
	{
		try
		{
			File f = new File(outputFileName);
			String s = f.getParent();
			File d = new File(s);
			if(d.exists())
				return true;
			else
				return d.mkdirs();
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
