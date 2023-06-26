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

package gov.sandia.hemlock.classification.fusion;

import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.classification.parameters.EnsembleParameters;
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.data.*;
import gov.sandia.hemlock.core.*;

/**
 * This is an ensemble classification model that uses the predictions of a set 
 * of base classifiers as votes in order to make its own predictions.  Plurality 
 * voting is used where only the target label of base classifiers are considered 
 * as votes. 
 *
 * @author Sean A. Gilpin
 */
public class Voting extends EnsembleModel
{
	
	/**
	 * Makes a prediction for the class of an instance by letting each
	 * of the base classifiers predict the class of an instance and 
	 * choosing the class that is predicted most by the base classifer 
	 * models.
	 *
	 * @param record The instance for which the prediction will be made
	 * @return The value of the class that is predicted
	 * @throws Exception
	 */
	public double getTargetValue(double[] record) throws Exception
	{
		double[] votes = getTargetDistribution(record);
		double max = Double.NEGATIVE_INFINITY;
		int maxIndex = 0;
		
		for(int i = 0; i < votes.length; i++)
		{
			if(votes[i] > max)
			{
				max = votes[i];
				maxIndex = i;
			}
		}
		
		return maxIndex;
	}
	
	/**
	 * Predicts the class distribution given an instance by counting the
	 * predictions of each base classifier for each possible class value.
	 * Each of these counts is put into a vector where each element of the
	 * vector corresponds to a the number of predictions for a single class
	 * by all of the base classifiers for the given instance.  That vector
	 * is then normalized and used as the prediction for the class 
	 * distribution.
	 *
	 * @param record The instance for which the prediction will be made.
	 * @return The class distribution prediciton which is made by this 
	 *	model.
	 * @throws Exception
	 */
	public double[] getTargetDistribution(double[] record) throws Exception
	{
		double[] votes = new double[dataSet.recordSchema.labels.length];
		for(int i = 0; i < baseClassifierSet.length; i++)
		{
			int label = (int)baseClassifierSet[i].getTargetValue(record);
			votes[label]++;
		}
		
		//calculate 1-norm
		double norm = 0;
		for(int i = 0; i < dataSet.recordSchema.labels.length; i++)
			norm += votes[i];
		//set 1-norm of distribution to 1
		for(int i = 0; i < dataSet.recordSchema.labels.length; i++)
			votes[i] = votes[i]/norm;
		return votes;
	}
	
	/**
	 * Used to identify the type of this model.
	 *
	 * @return Will return ModelType.Voting which identifies the type of 
	 * 	this model.
	 */
	public ModelType getModelType()
	{
		return ModelType.Voting;
	}
}
