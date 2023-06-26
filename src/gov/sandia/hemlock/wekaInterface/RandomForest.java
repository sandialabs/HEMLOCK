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

package gov.sandia.hemlock.wekaInterface;

import gov.sandia.hemlock.classification.Model;
import gov.sandia.hemlock.classification.ModelType;
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import weka.core.Instance;
import gov.sandia.hemlock.data.IDataSet;

/**
 * Wrapper for Random Forest algorithm.
 *
 * @author Sean A. Gilpin
 */
public class RandomForest implements Model
{
	public weka.classifiers.trees.RandomForest wekaRFModel;
	public WekaDataSet dataSet;
	
	/**
	 * Associates this model with a set of data that will be used for
	 * training.
	 *
	 * @param data A dataset in the native format of the framework used
	 * 	to build this model.
	 */
	public void setData(IDataSet data) 
	{
		this.dataSet = (WekaDataSet) data;
	}
	
	/**
	 * Perform all steps to build/train the model.  Assumes the data set has
	 * already been specified.
	 *
	 * @param modelParameters Specifies all information about which type of
	 *	model should be built.  
	 * @throws Exception
	 */
	public void buildModel(ModelParameters modelParameters) throws Exception
	{
		wekaRFModel = new weka.classifiers.trees.RandomForest();
		String[] options = WekaParameterTranslator.translate(modelParameters);
		wekaRFModel.setOptions(options);     // set the options
		wekaRFModel.buildClassifier(dataSet.records);   // build classifier
		//Cleanout training data
		this.dataSet = dataSet.getEmptyCopy();
	}
	
	/**
	 * Predicts the class label for an instance. Assumes the model has
	 * already been built.
	 * 
	 * @param record The instance which the prediction is made for.
	 * @return The index of the predicted class label
	 * @throws Exception
	 */
	public double getTargetValue(double[] record) throws Exception
	{
		//Our record values and weka record values are setup in same
		//way so this should work
		Instance instance = new Instance(1.0, record);
		instance.setDataset(dataSet.records);
		return wekaRFModel.classifyInstance(instance);
	}
	
	/**
	 * Predicts the class distribution given an instance. Assumes the model 
	 * has already been built.
	 * 
	 * @param record The given instance which the prediction is made for.
	 * @return Distribution represented as an array of probabilities, where
	 * 	the ith element is the probability of class i.
	 * @throws Exception
	 */
	public double[] getTargetDistribution(double[] record) throws Exception
	{
		//Our record values and weka record values are setup in same
		//way so this should work
		Instance instance = new Instance(1.0, record);
		instance.setDataset(dataSet.records);
		return wekaRFModel.distributionForInstance(instance);
	}
	
	/**
	 * Will report the learning algorithm that is used by this model.
	 * Assumes the model has been built.
	 *
	 * @return The learning algorithm that is used by this model.
	 */
	public ModelType getModelType()
	{
		return ModelType.RandomForest;
	}
}
