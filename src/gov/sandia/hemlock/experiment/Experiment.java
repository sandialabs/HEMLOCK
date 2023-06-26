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

package gov.sandia.hemlock.experiment;

import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.data.DataSetQuery;
import gov.sandia.hemlock.core.*;
import java.util.*;
import gov.sandia.hemlock.classification.fusion.*;

/*
 * Data structure used to record the details of an experiment that a user is interested in running.
 */
public class Experiment
{
	public String name;
	
	public ExperimentTypes experimentType;
	
	public ModelParameters modelParameters;
	
	public DataSetQuery dataQuery;
	
	public Hashtable<String, BaseClassifierSet> baseClassiferSets;
	
	public boolean computeConfusionMatrix = false;
	public boolean computeAccuracy = false;
	public boolean computeROCMetrics = false;
	public boolean computeDiversity_disagreement = false;
	public boolean computeDiversity_correlation = false;
	public boolean computeDiversity_yuleQ = false;
	public boolean computeDiversity_doubleFault = false;
	public boolean computeDiversity_entropy = false;
	public boolean computeDiversity_generalDiversity = false;
	public boolean computeDiversity_coincidentFailure = false;
	public boolean computeDiversity_difficulty = false;
	public int rocPositiveClass;
	
	public void runExperiment(String outputFileName) throws Exception
	{
		try
		{
			if(experimentType == ExperimentTypes.KFoldCrossValidation)
			{
				KFoldsExperiment kfe = (KFoldsExperiment)this;
				kfe.executeExperiment(outputFileName);
			}
			if(experimentType == ExperimentTypes.NoHoldOut)
			{
				NoHoldOutExperiment nhoe = (NoHoldOutExperiment)this;
				nhoe.executeExperiment(outputFileName);
			}
		
		}
		catch(FrameworkNotSupportedException e)
		{
			System.err.println(e.getError());
			System.err.println("See README file for instructions on setting up that framework.");
		}
	}
}
