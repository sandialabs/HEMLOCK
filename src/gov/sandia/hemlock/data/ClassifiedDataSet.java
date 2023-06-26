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

package gov.sandia.hemlock.data;


import gov.sandia.hemlock.classification.Model;

import java.util.ArrayList;

/**
 * Data structure for a classified data set.  A classified data set is a set of 
 * labeled instances and the corresponding labels that some classification 
 * algorithm assigned to them.  A clasified data set can be used to evaluate 
 * the predictions of a model.
 *
 * @author Sean A. Gilpin
 */
public class ClassifiedDataSet
{
	/** The schema for the data in this data set */
	public final RecordSchema recordSchema;
	/** The instances in this data set along with true labels*/
	public final ArrayList<double[]> records;
	/** The predicted labels for this data set made by a model*/
	public double[] predictedLabels;
	/** The predicted class distribution for this data set made by a model*/
	public double[][] predictedDistributions;
	/** Information about this data set such as its name */
	public DataSetInfo info;
	
	
	/**
	 * Constructor that takes a labeled data set and a classification model,
	 * and uses the model to make predictions about the data set.  Those
	 * predictions are stored within the member variables of the new
	 * ClassifiedDataSet.
	 *
	 * @param data The data to make predictions for.
	 * @param classifier The model that will make th prediction
	 * @throws Exception
	 */
	public ClassifiedDataSet(DataSet data, Model classifier) throws Exception
	{
		//shallow copy records and schema from dataset
		recordSchema = data.recordSchema;
		records = data.records;
		info = data.info;
		
		//predict labels
		predictedLabels = new double[data.records.size()];
		predictedDistributions = new double[data.records.size()][];
		for(int i = 0; i < data.records.size(); i++)
		{
			predictedLabels[i] = classifier.getTargetValue(data.records.get(i));
			predictedDistributions[i] = classifier.getTargetDistribution(data.records.get(i));
		}
	}
}
