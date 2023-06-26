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

package gov.sandia.hemlock.evaluation;

import gov.sandia.hemlock.data.*;
import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.classification.parameters.*;
import gov.sandia.hemlock.core.*;
import gov.sandia.hemlock.experiment.*;
import java.util.*;
import java.io.*;

/**
 * Method to evaluate a given set of model parameter choices using K-fold cross
 * validation where each of the folds is forced to have the same class 
 * distribution as the entire training set.  The subsamples are generated
 * randomly but constrained in such a way to enforce that each subsample 
 * receives the same number of instances from each class (plus or minus one).
 *
 * @author Sean A. Gilpin
 */
public class StratifiedCrossValidation
{
	private int numFolds;
	private long seed;
	private boolean seedSet = false;
	private ArrayList<double[]>[] folds;
	private DataSet dataSet;
	private RecordSchema schema;
	private DataSetInfo info;

	/**
	 * Constructor that specifies the number of subsamples to create and
	 * the data set from which to create those subsamples.
	 *
	 * @param kfolds Number of sumsamples to create
	 * @param labeled The data set to create subsamples from
	 */
	public StratifiedCrossValidation(int kfolds, DataSet labeled)
	{
		numFolds = kfolds;
		dataSet = labeled;
		schema = labeled.recordSchema;
		info = labeled.info;
		folds = splitRecords(labeled);
	}
	
	/**
	 * Constructor that allows user to specify the seed used in random
	 * number generator used in subsampling method.  
	 *
	 * @param kfolds Number of sumsamples to create
	 * @param labeled The data set to create subsamples from
	 * @param seed Seed used for random numbers while creating subsamples.
	 */
	public StratifiedCrossValidation(int kfolds, DataSet labeled, long seed) 
	{
		this.seed = seed;
		this.seedSet = true;
		numFolds = kfolds;
		dataSet = labeled;
		schema = labeled.recordSchema;
		info = labeled.info;
		folds = splitRecords(labeled);
	}
	
	/**
	 * Evaluates a set model parameters by one by one setting aside one
	 * of the subsamples, training the model on the rest of the subsamples,
	 * and calculating the evaluation metrics using the held out subsample.
	 * Each such trained model will have its evaluation metrics stored
	 * and an array of results will be returned.
	 *
	 * @param experiment The model parameters and evaluation measurement
	 *	will be specified here.
	 * @return An array of results, one for each model that is built.
	 * @throws FrameworkNotSupportedException
	 * @throws Exception
	 */
	public ModelEvaluationResults[] runTest(Experiment experiment) throws FrameworkNotSupportedException, Exception
	{
		ModelParameters params = experiment.modelParameters;
		ModelEvaluationResults[] results = new ModelEvaluationResults[numFolds];
		for(int i=0; i < numFolds; i++)
		{
			//Create model
			ClassifierFactory cf = new ClassifierFactory(params.frameworkType, createTrainingDataSet(i), i);
			Model model = cf.createModel(params);
			
			DataSet testFold = createTestDataSet(i);
			
			//Calculate evaluation metrics
			ClassifiedDataSet cdata = new ClassifiedDataSet(testFold, model);
			results[i] = new ModelEvaluationResults(cdata, model, experiment);
			
		}
		
		return results;
	}
	
	/**
	 * Subsampling method.  The subsamples will be partions of the original 
	 * set of instances, where partitions will be of equal size (or as close
	 * as possible).  Subsamples will have same class distribution (or as
	 * close as possible).
	 *
	 * @param training Instances to be partioned
	 * @return List of subsamples of instances
	 */
	private ArrayList<double[]>[] splitRecords(DataSet training)
	{
		ArrayList<double[]> records = new ArrayList<double[]>(training.records);
		int numClasses = schema.labels.length;
		ArrayList<double[]>[] bins = new ArrayList[numClasses];
		for(int i =0; i < numClasses; i++)
			bins[i] = new ArrayList<double[]>();
		
		Random generator;
		if(seedSet)
			generator = new Random(seed);
		else
			generator = new Random();
		
		int numRecords = records.size();
		for(int i = 0; i < numRecords; i++)
		{
			//create a random number between 1 and remaining number of records
			int index = generator.nextInt(numRecords) % records.size();
			//find that record, and put in bin corresponding to class
			int classIndex = (int)records.get(index)[schema.numAttributes];
			bins[classIndex].add(records.get(index));
			records.remove(index);
		}
		
		//Assign records to folds
		ArrayList<double[]>[] subsets = new ArrayList[numFolds];
		for(int i=0; i < numFolds; i++)
			subsets[i] = new ArrayList<double[]>();
			
		for(int i = 0; i < numClasses; i++)
		{
			for(int j = 0; j < bins[i].size(); j++)
			{
				subsets[j % numFolds].add(bins[i].get(j));
			}
		}

		return subsets;
	}
	
	/**
	 * Turn one of the subsamples into a DataSet so it can be used
	 * for evaluation.
	 *
	 * @param foldK the identification of the subsample to use.
	 * @return The created dataset using only on subsample.
	 */
	private DataSet createTestDataSet(int foldk)
	{
		return new DataSet(schema, info, folds[foldk]);	
	}
	
	/** 
	 * Create a data set using all but one of the subsamples which will
	 * be used to train a model.
	 *
	 * @param foldk The identification of the subsample not to include
	 * 	 in the data set.
	 * @return The data set that will be used for training.
	 */
	private DataSet createTrainingDataSet(int foldk)
	{
		ArrayList<double[]> records = new ArrayList<double[]>();
		for(int i = 0; i < numFolds; i++)
		{
			if(i != foldk)
			{
				records.addAll(folds[i]);
			}
		}
		return new DataSet(schema, info, records);
	}
}
