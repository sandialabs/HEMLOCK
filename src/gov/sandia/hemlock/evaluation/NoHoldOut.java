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
 * Method to evaluate a given set of model parameter choices using 
 * 100% of the data for training and testing.  This generally is not desirable
 * because the resulting evaluations will show better results than what
 * will be seen in general.  However it can be useful for determining the
 * best possible results that can be obtained with a particular dataset.
 *
 * @author Sean A. Gilpin
 */
public class NoHoldOut
{
	private DataSet dataSet;
	private RecordSchema schema;
	private DataSetInfo info;
	

	/**
	 * Constructor which associates this evaluation method with
	 * a set of data that models will be built and evaluated with
	 *
	 * @param labeled Data set models will be built and evaluated with.
	 */
	public NoHoldOut(DataSet labeled)
	{
		dataSet = labeled;
		schema = labeled.recordSchema;
		info = labeled.info;
	}

	/**
	 * Builds the model specified in the experiment using all of the data
	 * and then evaluates it using all of the same data.
	 *
	 * @param experiment Specifies model to be built
	 * @return The results of the evaluation
	 * @throws FrameworkNotSupportedException
	 * @throws Exception
	 */
	public ModelEvaluationResults runTest(Experiment experiment) throws FrameworkNotSupportedException, Exception
	{
		ModelParameters params = experiment.modelParameters;
		ModelEvaluationResults results;

		//Create model
		ClassifierFactory cf = new ClassifierFactory(params.frameworkType, dataSet);
		Model model = cf.createModel(params);
				
		//Calculate evaluation metrics
		ClassifiedDataSet cdata = new ClassifiedDataSet(dataSet, model);
		
		results = new ModelEvaluationResults(cdata, model, experiment);

		return results;
	}
	
}
