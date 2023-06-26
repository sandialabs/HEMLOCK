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
import gov.sandia.hemlock.classification.fusion.*;

import java.util.*;

/**
 * This is a type of experiment that can be used to evaluate a set of model parameters.  Stratified
 * k-fold cross validation is used an the metrics for each fold and aggregated metrics are
 * calculated and reported.
 */
public class KFoldsExperiment extends Experiment
{
	protected DataSetInfo[] dataSetInfo;  // a list of all the dataSetInfo objects for all known data sets
	public int numberOfFolds;
	public long seed;
	public boolean seedSet = false;
	
	public KFoldsExperiment(DataSetInfo[] info)
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
			StratifiedCrossValidation crossVal;

			if(seedSet)
				crossVal = new StratifiedCrossValidation(numberOfFolds, dataSet[i], seed);
			else
				crossVal = new StratifiedCrossValidation(numberOfFolds, dataSet[i]);
			
			ModelEvaluationResults[] results = crossVal.runTest(this);
			Hashtable<String,String> summary = createSummary(results);
			writer.writeKFoldExperiment(results, summary);
		}
		writer.stopExperiment();
	}
	
	protected Hashtable<String,String> createSummary(ModelEvaluationResults[] results)
	{
		Hashtable<String,String> summary = new Hashtable<String,String>();
		//experimentName
		summary.put("experimentName", this.name);
		//modelType
		summary.put("modelType", this.modelParameters.modelType.toString());
		//dataSetName
		summary.put("dataSetName", results[0].dataSetInfo.dataSetName);
		//numberOfFolds
		summary.put("numberOfFolds", Integer.toString(results.length));
		//averageAccuracy
		double averageAccuracy = 0;
		for(int i = 0; i < results.length; i++)
		{
			averageAccuracy += results[i].accuracy;
		}
		averageAccuracy = averageAccuracy / results.length;
		summary.put("accuracy", Double.toString(averageAccuracy));
		//averageAUC
		if(this.computeROCMetrics)
		{
			double averageAUC = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageAUC += results[i].auc;
			}
			averageAUC = averageAUC / results.length;
			summary.put("AUC", Double.toString(averageAUC));
		}
		//diversity
		if(this.computeDiversity_disagreement)
		{
			double averageDiv_disagreement = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv_disagreement += results[i].diversity_disagreement;
			}
			averageDiv_disagreement /= results.length; 			
			summary.put("diversity_disagreement", Double.toString(averageDiv_disagreement));
		}
		if(this.computeDiversity_correlation)
		{
			double averageDiv_correlation = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv_correlation += results[i].diversity_correlation;
			}
			averageDiv_correlation /= results.length; 
			summary.put("diversity_correlation", Double.toString(averageDiv_correlation));
		}
		if(this.computeDiversity_yuleQ)
		{
			double averageDiv = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv += results[i].diversity_yuleQ;
			}
			averageDiv /= results.length; 
			summary.put("diversity_yuleQ", Double.toString(averageDiv));
		}
		if(this.computeDiversity_doubleFault)
		{
			double averageDiv = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv += results[i].diversity_doubleFault;
			}
			averageDiv /= results.length; 
			summary.put("diversity_doubleFault", Double.toString(averageDiv));
		}
		if(this.computeDiversity_entropy)
		{
			double averageDiv = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv += results[i].diversity_entropy;
			}
			averageDiv /= results.length; 
			summary.put("diversity_entropy", Double.toString(averageDiv));
		}
		if(this.computeDiversity_generalDiversity)
		{
			double averageDiv = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv += results[i].diversity_generalDiversity;
			}
			averageDiv /= results.length; 
			summary.put("diversity_generalDiversity", Double.toString(averageDiv));
		}
		if(this.computeDiversity_coincidentFailure)
		{
			double averageDiv = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv += results[i].diversity_coincidentFailure;
			}
			averageDiv /= results.length; 
			summary.put("diversity_coincidentFailure", Double.toString(averageDiv));
		}
		if(this.computeDiversity_difficulty)
		{
			double averageDiv = 0;
			for(int i =0 ; i < results.length; i++)
			{
				averageDiv += results[i].diversity_difficulty;
			}
			averageDiv /= results.length; 
			summary.put("diversity_difficulty", Double.toString(averageDiv));
		}
		
		
		return summary;
	}
	
}
