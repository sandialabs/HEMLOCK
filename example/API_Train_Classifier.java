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

 package example;

import gov.sandia.hemlock.core.*;
import gov.sandia.hemlock.data.*;
import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.classification.parameters.*;

public class API_Train_Classifier
{
	
	public static void main(String [] args) throws Exception
	{
		//Create DataSet by loading data from file
		DataImporter importer = new DataImporter();
		DataSet trainingSet = importer.importDataSet(
			DataSetInfo.fromPath("data/mixed_2", "mixed_2",
				FileFormatType.ModifiedC45));
		
		//Specify type of model and training parameters
		RandomTreeParameters params = 
			new RandomTreeParameters(FrameworkType.weka);
		params.setMinInstances(2);
		
		//Train classifier
		ClassifierFactory factory = 
			new ClassifierFactory(FrameworkType.weka, trainingSet);
		Model classifier = factory.createModel(params);
		
		//Make prediction for an instance in training set
		double[] instance = trainingSet.records.get(0);
		double labelIndex = classifier.getTargetValue(instance);
		String label = trainingSet.recordSchema.labels[(int)labelIndex];
		
		System.out.println(label);
	}	

}
