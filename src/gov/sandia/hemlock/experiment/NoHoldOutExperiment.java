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
import gov.sandia.hemlock.data.*;
import gov.sandia.hemlock.evaluation.*;

import java.util.*;

/**
 * This is a type of experiment that can be used to evaluate a set of model 
 * parameters.  The model will be train and tested on 100% of the data.
 */
public class NoHoldOutExperiment extends Experiment
{
	protected DataSetInfo[] dataSetInfo;  // a list of all the dataSetInfo objects for all known data sets
	public Hashtable<String, ModelParameters[]> baseClassiferSets;
	
	public NoHoldOutExperiment(DataSetInfo[] info)
	{
		this.dataSetInfo = info;
	}
	
	public void executeExperiment(String outputFileName) throws Exception
	{
		ExperimentWriter writer = new ExperimentWriter(name, outputFileName);
		writer.startExperiment();
		DataSet[] dataSet = dataQuery.getDataSets(dataSetInfo);
		for(int i = 0; i < dataSet.length; i++)
		{
			NoHoldOut nho= new NoHoldOut(dataSet[i]);
			ModelEvaluationResults results = nho.runTest(this);
			Hashtable<String,String> summary = createSummary(results);
			writer.writeNoHoldOutExperiment(summary);
		}
		writer.stopExperiment();
	}
	
	protected Hashtable<String,String> createSummary(ModelEvaluationResults results)
	{
		Hashtable<String,String> summary = new Hashtable<String,String>();
		//experimentName
		summary.put("experimentName", this.name);
		//modelType
		summary.put("modelType", this.modelParameters.modelType.toString());
		//dataSetName
		summary.put("dataSetName", results.dataSetInfo.dataSetName);
		//accuracy
		summary.put("accuracy", Double.toString(results.accuracy));
		//aUC
		if(this.computeROCMetrics)
			summary.put("AUC", Double.toString(results.auc));
		
		//Diversity
		if(this.computeDiversity_disagreement)
			summary.put("diversity_disagreement", Double.toString(results.diversity_disagreement));
		if(this.computeDiversity_correlation)
			summary.put("diversity_correlation", Double.toString(results.diversity_correlation));
		if(this.computeDiversity_yuleQ)
			summary.put("diversity_yuleQ", Double.toString(results.diversity_yuleQ));
		if(this.computeDiversity_doubleFault)
			summary.put("diversity_doubleFault", Double.toString(results.diversity_doubleFault));
		if(this.computeDiversity_entropy)
			summary.put("diversity_entropy", Double.toString(results.diversity_entropy));
		if(this.computeDiversity_generalDiversity)
			summary.put("diversity_generalDiversity", Double.toString(results.diversity_generalDiversity));
		if(this.computeDiversity_coincidentFailure)
			summary.put("diversity_coincidentFailure", Double.toString(results.diversity_coincidentFailure));
		if(this.computeDiversity_difficulty)
			summary.put("diversity_difficulty", Double.toString(results.diversity_difficulty));

		return summary;
	}
	
}
