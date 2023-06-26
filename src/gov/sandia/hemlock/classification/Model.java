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

package gov.sandia.hemlock.classification;

import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.data.IDataSet;
import java.io.Serializable;

/**
 * Interface that all classification models must implement.  All classification
 * models are expected to implement a minimal set of functionality that is
 * specified in this interface.  All models that implement this interface should
 * expect {@link #setData setData} to be called first, followed by 
 * {@link #buildModel buildModel}.  Only then, should the other methods work
 * correctly.
 *
 * @author Sean A. Gilpin
 */
public interface Model extends Serializable
{
	/**
	 * Perform all steps to build/train the model.  Assumes the data set has
	 * already been specified.
	 *
	 * @param modelParameters Specifies all information about which type of
	 *	model should be built.  
	 * @throws Exception
	 */
	public void buildModel(ModelParameters modelParameters) throws Exception;
	
	/**
	 * Predicts the class label for an instance. Assumes the model has
	 * already been built.
	 * 
	 * @param record The instance which the prediction is made for.
	 * @return The index of the predicted class label
	 * @throws Exception
	 */
	public double getTargetValue(double[] record) throws Exception;
	
	/**
	 * Predicts the class distribution given an instance. Assumes the model 
	 * has already been built.
	 * 
	 * @param record The given instance which the prediction is made for.
	 * @return Distribution represented as an array of probabilities, where
	 * 	the ith element is the probability of class i.
	 * @throws Exception
	 */
	public double[] getTargetDistribution(double[] record) throws Exception;
	
	/**
	 * Will report the learning algorithm that is used by this model.
	 * Assumes the model has been built.
	 *
	 * @return The learning algorithm that is used by this model.
	 */
	public ModelType getModelType();
	
	/**
	 * Associates this model with a set of data that will be used for
	 * training.
	 *
	 * @param data A dataset in the native format of the framework used
	 * 	to build this model.
	 */
	public void setData(IDataSet data);
}
