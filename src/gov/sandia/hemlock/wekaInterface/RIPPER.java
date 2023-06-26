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
import weka.core.Instance;
import gov.sandia.hemlock.classification.ModelType;
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.data.IDataSet;

/**
 * Wrapper for Weka JRIPPER algorithm.
 *
 * @author Sean A. Gilpin
 */
public class RIPPER implements Model
{
	public weka.classifiers.rules.JRip wekaJRipModel;
	public WekaDataSet dataSet;
	
	/**
	 * Associates this model with a set of data that will be used for
	 * training.
	 *
	 * @param data A dataset in the native format of the framework used
	 * 	to build this model.
	 */
	@Override
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
	@Override
	public void buildModel(ModelParameters modelParameters) throws Exception
	{
		wekaJRipModel = new weka.classifiers.rules.JRip();
		wekaJRipModel.setOptions(WekaParameterTranslator.translate(modelParameters));
		wekaJRipModel.buildClassifier(dataSet.records);
		//Cleanout training data
		this.dataSet = dataSet.getEmptyCopy();
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
	@Override
	public double[] getTargetDistribution(double[] record) throws Exception
	{
		//Our record values and weka record values are setup in same
		//way so this should work
		Instance instance = new Instance(1.0, record);
		instance.setDataset(dataSet.records);
		return wekaJRipModel.distributionForInstance(instance);
	}
	
	 /**
	 * Predicts the class label for an instance. Assumes the model has
	 * already been built.
	 * 
	 * @param record The instance which the prediction is made for.
	 * @return The index of the predicted class label
	 * @throws Exception
	 */
	@Override
	public double getTargetValue(double[] record) throws Exception
	{
		//Our record values and weka record values are setup in same
		//way so this should work
		Instance instance = new Instance(1.0, record);
		instance.setDataset(dataSet.records);
		return wekaJRipModel.classifyInstance(instance);
	}
	
	/**
	 * Will report the learning algorithm that is used by this model.
	 * Assumes the model has been built.
	 *
	 * @return The learning algorithm that is used by this model.
	 */
	@Override
	public ModelType getModelType()
	{
		return ModelType.RIPPER;
	}
}
