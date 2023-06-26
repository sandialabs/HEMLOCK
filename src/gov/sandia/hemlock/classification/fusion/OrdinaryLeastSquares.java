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
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.data.DataSet;
import Jama.*;
import java.io.*;

/**
 * An ensemble classification model which combines the predictions of a set
 * of base classifier by treating their predictions as a new training set
 * and using ordinary least squares to create a vector of weights, with each of
 * the weights corresponding to one of the base classifier models.  A large 
 * weight leads to the corresponding base classifier having more influence in
 * future predicitons.  
 *
 * Each combination of original training instances with all possible class 
 * values is used to create a training instance in the new feature space.
 * The vector of base classifier probabilities for class i is used for the
 * features and 0 or 1 is used for the dependent variable depending on whether
 * class i is the true class for the original instance.
 *
 * @author Sean A. Gilpin
 */
public class OrdinaryLeastSquares extends EnsembleModel
{
	private double[] linearWeights;

	/**
	 * Trains the OLS ensemble model by creating the set of base classifiers
	 * and training a set of weights.
	 *
	 * @param modelParameters Will contain information about the base
	 *	classifiers to be used by this ensemble.
	 * @throws Exception
	 */
	@Override
	public void buildModel(ModelParameters modelParameters) throws Exception
	{
		super.buildModel(modelParameters);
		linearWeights = getLinearWeights();
	}
	
	/**
	 * Trains the OLS ensemble model, when a subsample evaluation method is
	 * being used such as k-fold cross validation.  The model is built by
	 * creating the set of base classifiers and training a set of weights.
	 *
	 * @param modelParameters Will contain information about the base
	 *	classifiers to be used by this ensemble.
	 * @param fold The identification for the subsample being used to build
	 *	this particular model.
	 * @throws Exception
	 */
	@Override
	public void buildModel(ModelParameters modelParameters, int fold) throws Exception
	{
		super.buildModel(modelParameters, fold);
		linearWeights = getLinearWeights();
	}
	
	/**
	 * Creates the weights needed for this model by solving an ordinary 
	 * least squares problem.
	 *
	 * @return An array representing the weights vector
	 * @throws Exception
	 */
	private double[] getLinearWeights() throws Exception
	{
		Matrix inputs = createInputMatrix();
		Matrix trueDist = createTrueDistributionVector();
		
		/* //Debug
		PrintWriter pw1 = new PrintWriter("matrix.txt");
		PrintWriter pw2 = new PrintWriter("dependent.txt");
		inputs.print(pw1,32,16);
		trueDist.print(pw2,32,16);
		pw1.flush();
		pw2.flush();
		pw1.close();
		pw2.close();
		//End Debug */
		
		//The following method is used instead of Matrix.solve,
		//which would perform least squares, to deal with case when
		//(transpose(X)X) is not invertible.
		Matrix pseudoInverse = inputs.inverse();
		Matrix weights = pseudoInverse.times(trueDist);
		double[] arrWeights = weights.getColumnPackedCopy();

		return arrWeights;
	}
	
	/**
	 * Creates the design matrix for use in the OLS problem to be
	 * solved when training this ensemble model.  Each row in this matrix
	 * will be a vector of predicted probabilities for a given class for
	 * a given training instance.  Each scalar from the vector will
	 * correspond to the prediction from one of the base classifiers.
	 *
	 * @return Matrix The design matrix needed for the OLS problem.
	 * @throws Exception
	 */
	private Matrix createInputMatrix() throws Exception
	{
		int numClasses = this.dataSet.recordSchema.labels.length;
		int numInstances = this.dataSet.records.size();
		int numBaseClassifiers = this.baseClassifierSet.length;
		double[][] inputs = new double[numClasses*numInstances][numBaseClassifiers+1];
		
		for(int i = 0; i < numBaseClassifiers; i++)
		{
			Model bModel = baseClassifierSet[i];
			for(int j = 0; j < numInstances; j++)
			{
				double[] instance = dataSet.records.get(j);
				double[] dist = bModel.getTargetDistribution(instance);
				for(int k = 0; k < numClasses; k++)
				{
					inputs[j*numClasses + k][i] = dist[k];
				}
			}
		}
		//Set constant term
		for(int i=0; i< numInstances*numClasses; i++)
			inputs[i][numBaseClassifiers] = 1;
		
		return new Matrix(inputs);
	}
	
	/**
	 * A column vector with the number of elements equal to the number of 
	 * instances times the number of classes.  Each true class label is
	 * converted into a true class distribution and then stacked on
	 * top of each other.  This vector will be used as the dependent
	 * variable vector in the OLS problem needed for training
	 *
	 * @return Matrix The column vector representing the dependent variable.
	 */
	public Matrix createTrueDistributionVector()
	{
		int numClasses = this.dataSet.recordSchema.labels.length;
		int numInstances = this.dataSet.records.size();
		double[] trueDist = new double[numClasses*numInstances];
		
		for(int i = 0; i < numInstances; i++)
		{
			//get true class for target
			double[] instance = dataSet.records.get(i);
			double trueClass = instance[dataSet.recordSchema.numAttributes];
			for(int j = 0; j < numClasses; j++)
			{
				trueDist[i*numClasses + j] = (j==trueClass) ? 1 : 0;
			}
		}
		
		return new Matrix(trueDist, numClasses*numInstances);
	}

	/**
	 * Predicts the class distribution given an instance by taking a
	 * weighed average of the predicted class distributions of the base
	 * classifiers
	 *
	 * @param record Instance for which prediction is to be made.
	 * @return The class distribution predictions made.
	 * @throws Exception.
	 */
	@Override
	public double[] getTargetDistribution(double[] record) throws Exception
	{
		int numClasses = dataSet.recordSchema.labels.length;
		double[] finalDist = new double[numClasses];
		for(int i = 0; i < baseClassifierSet.length; i++)
		{
			double[] bDist = baseClassifierSet[i].getTargetDistribution(record);
			for(int j = 0; j < numClasses; j++)
			{
				finalDist[j] += linearWeights[i] * bDist[j];
				
			}
		}
		
		//Add constant term weight
		for(int j = 0; j < numClasses; j++)
			finalDist[j] += linearWeights[linearWeights.length-1];
		
		
		Matrix m = new Matrix(finalDist, numClasses);
		Matrix normalized = m.times(1/m.norm1());
		return normalized.getColumnPackedCopy();
	}
	
	/**
	 * Predicts the class of an instance by taking a weighed average of the
	 * predicted class distributions of the base classifiers and then 
	 * returning the value of the class that has the highest probability.
	 *
	 * @param record Instance for which prediction is to be made.
	 * @return The value of the class which is predicted.
	 * @throws Exception.
	 */
	@Override
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
	 * Used to identify the type of this model.
	 *
	 * @return Will return ModelType.LinearRegression, which is this 
	 *	model's type.
	 */
	@Override
	public ModelType getModelType()
	{
		return ModelType.LinearRegression;
	}


}
