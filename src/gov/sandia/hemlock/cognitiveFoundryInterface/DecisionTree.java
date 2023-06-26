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
 
package gov.sandia.hemlock.cognitiveFoundryInterface;

import gov.sandia.cognition.math.matrix.mtj.*;
import gov.sandia.hemlock.classification.Model;
import gov.sandia.hemlock.classification.ModelType;
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.data.IDataSet;

/**
 * A wrapper for the Decision Tree algorithms provided by Cognitive Framework.
 */
public class DecisionTree implements Model
{
	public gov.sandia.cognition.learning.algorithm.tree.CategorizationTree<DenseVector,String> tree;
	public CognitiveDataSet dataSet;
	
	
	@Override
	public void setData(IDataSet data)
	{
		dataSet = (CognitiveDataSet)data;
	}

	@Override
	public void buildModel(ModelParameters modelParameters) throws Exception
	{
		gov.sandia.cognition.learning.algorithm.tree.CategorizationTreeLearner<DenseVector, String> learner;
		learner = new gov.sandia.cognition.learning.algorithm.tree.CategorizationTreeLearner<DenseVector, String>(new gov.sandia.cognition.learning.algorithm.tree.VectorThresholdInformationGainLearner<String>()); 
		tree = learner.learn(dataSet.instances);
	}

	@Override
	public double getTargetValue(double[] record) throws Exception
	{
		//Note this doesn't work correctly
		//strip class label from end of record
		DenseVector input = DenseVectorFactoryMTJ.INSTANCE.copyArray(java.util.Arrays.copyOf(record, record.length - 1));
		return dataSet.recordSchema.getTranslation(record.length-1, tree.evaluate(input));
	}
	
	@Override
	public double[] getTargetDistribution(double[] record) throws Exception
	{
		//This is just fill in.  Doesn't work.
		return new double[0];
	}
	
	public ModelType getModelType()
	{
		return ModelType.DecisionTree;
	}

}
