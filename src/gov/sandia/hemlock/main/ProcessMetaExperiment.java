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

import gov.sandia.hemlock.experiment.*;

/**
 * Command line tool for processing a meta file.
 *
 * @author Sean A. Gilpin
 */
public class ProcessMetaExperiment
{

	/**
	 * Process a meta experiment file as input and creates experiments
	 * in an experiment file as output.
	 *
	 * @param args Command line arguments.  First entry will be path
	 * 	of meta experiment file and second entry the path of the
	 *	output experiment file
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		String inputFileName = args[0].trim();
		String outputFileName = args[1].trim();
		
		MetaExperiment me = new MetaExperiment();
		me.createExperiments(inputFileName, outputFileName);
	}
}
