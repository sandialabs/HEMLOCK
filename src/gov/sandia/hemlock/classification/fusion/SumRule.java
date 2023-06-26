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

/**
 * An ensemble classification model that combines the predictions of a set of
 * base classifiers by adding together their predicted class distributions 
 * and normalizing the results to produce its own prediction for the class
 * distribution.
 *
 * @author Sean A. Gilpin
 */
public class SumRule extends EnsembleModel
{	
	/**
	 * Predicts the class for an instance by first predicting the class
	 * distribution and, then picking the class that has the highest
	 * probability.
	 *
	 * @param record The instance for which a prediction is to be made.
	 * @return The value of the class that is predicted by this model.
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
	 * Predicts the class distribution given an instance by summing the 
	 * class distribution predictions for each of the base classifier models
	 * and normalizing the result.
	 *
	 * @param record The instance for which a prediction is to be made.
	 * @return The value of the class that is predicted by this model.
	 * @throws Exception
	 */
	public double[] getTargetDistribution(double[] record) throws Exception
	{
		double[] sumDist = new double[dataSet.recordSchema.labels.length];
		for(int i = 0; i < baseClassifierSet.length; i++)
		{
			double[] dist = baseClassifierSet[i].getTargetDistribution(record);
			for(int j = 0; j < dataSet.recordSchema.labels.length; j++)
				sumDist[j] += dist[j];
		}
		//calculate 1-norm
		double norm = 0;
		for(int i = 0; i < dataSet.recordSchema.labels.length; i++)
			norm += sumDist[i];
		//set 1-norm of distribution to 1
		for(int i = 0; i < dataSet.recordSchema.labels.length; i++)
			sumDist[i] = sumDist[i]/norm;
		
		return sumDist;
	}
	
	
	/**
	 * Used to identify the type of this Model
	 * 
	 * @return Returns ModelType.SumRule which is the type of this model.
	 */
	public ModelType getModelType()
	{
		return ModelType.SumRule;
	}
}
