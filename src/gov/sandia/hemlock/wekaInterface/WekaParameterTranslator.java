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

import java.util.*;
import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.classification.parameters.KNearestNeighborParameters;
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.classification.parameters.NaiveBayesianParameters;
import gov.sandia.hemlock.classification.parameters.RIPPERParameters;
import gov.sandia.hemlock.classification.parameters.RandomForestParameters;
import gov.sandia.hemlock.classification.parameters.RandomTreeParameters;
import gov.sandia.hemlock.classification.parameters.SVMParameters;

/**
 * Translates between model parameters specified in Hemlock, to the associated 
 * parameter strings used to specify models in weka.  All of the parameters
 * for specifying training options are specified in an array of string when \
 * using Weka.  To see the details of how Weka interprets these strings,  see 
 * the details in the specific model's class in the weka.classifiers sub 
 * package.  The general format is a parameter option in one element of the 
 * array followed by the parameter value in the next array element.
 *
 * @author Sean A. Gilpin
 */
public class WekaParameterTranslator
{

	/** 
	 * Translates any model parameter into the apropriate Weka training
	 * parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 * @throws ModelTypeNotSupportedException If the type of model
	 *	specified in the Hemlock parameters are not supported for
	 *	use with Weka.
	 */
	public static String[] translate(ModelParameters params) throws ModelTypeNotSupportedException
	{
		switch(params.modelType)
		{
		case NaiveBayesian:
			return translateNaiveBayesian((NaiveBayesianParameters) params);
		case RandomForest:
			return translateRandomForest((RandomForestParameters) params);
		case KNearestNeighbor:
			return translateKNearestNeighbor((KNearestNeighborParameters)params);
		case RIPPER:
			return translateJRip((RIPPERParameters)params);
		case SVM:
			return translateSVM((SVMParameters)params);
		case RandomTree:
			return translateRandomTree((RandomTreeParameters)params);
		default:
			throw new ModelTypeNotSupportedException(params.modelType, params.frameworkType);
		}
	}
	
	/** 
	 * Translates a random tree parameter into the apropriate Weka training
	 * parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 */
	private static String[] translateRandomTree(RandomTreeParameters params)
	{
		ArrayList<String> wekaParams = new ArrayList<String>();
		
		if(params.isSetNumFeatures())
		{
			wekaParams.add("-K");
			wekaParams.add(Integer.toString(params.getNumFeatures()));
		}
		if(params.isSetMaxDepth())
		{
			wekaParams.add("-depth");
			wekaParams.add(Integer.toString(params.getMaxDepth()));
		}
		if(params.isSetMinInstances())
		{
			wekaParams.add("-M");
			wekaParams.add(Integer.toString(params.getMinInstances()));
		}
		if(params.isSetSeed())
		{
			wekaParams.add("-S");
			wekaParams.add(Integer.toString(params.getSeed()));
		}
		
		String[] stringParams = new String[wekaParams.size()];
		return wekaParams.toArray(stringParams);
	}
	
	/** 
	 * Translates an SVM parameter into the apropriate Weka training
	 * parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 */
	private static String[] translateSVM(SVMParameters params)
	{
		ArrayList<String> wekaParams = new ArrayList<String>();
		
		if(params.isSetBuildLogisticModels())
		{
			if(params.getBuildLogisticModels())
			{
				wekaParams.add("-M");
			}
		}
		if(params.isSetComplexity())
		{
			wekaParams.add("-C");
			wekaParams.add(Double.toString(params.getComplexity()));
		}
		if(params.isSetEpsilon())
		{
			wekaParams.add("-P");
			wekaParams.add(Double.toString(params.getEpsilon()));
		}
		if(params.isSetSeed())
		{
			wekaParams.add("-W");
			wekaParams.add(Integer.toString(params.getSeed()));
		}
		
		String[] stringParams = new String[wekaParams.size()];
		return wekaParams.toArray(stringParams);
	}
	
	/** 
	 * Translates a RIPPER model parameter into the apropriate Weka training
	 * parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 */
	private static String[] translateJRip(RIPPERParameters params)
	{
		ArrayList<String> wekaParams = new ArrayList<String>();
		if(params.isSetCheckErrorRate())
		{
			//-E if false no parameter
			if(!params.getCheckErrorRate())
			{
				wekaParams.add("-E");
			}
		}
		if(params.isSetMinNumber())
		{
			wekaParams.add("-N");
			wekaParams.add(Integer.toString(params.getMinNumber()));
		}
		if(params.isSetOptimizations())
		{
			wekaParams.add("-O");
			wekaParams.add(Integer.toString(params.getOptimizations()));
		}
		if(params.isSetSeed())
		{
			wekaParams.add("-S");
			wekaParams.add(Integer.toString(params.getSeed()));
		}
		if(params.isSetUsePruning())
		{
			//-P if false no parameter
			if(!params.getUsePruning())
			{
				wekaParams.add("-P");
			}
		}
		String[] stringParams = new String[wekaParams.size()];
		return wekaParams.toArray(stringParams);
		
	}
	
	/** 
	 * Translates a Random Forest model parameter into the apropriate Weka 
	 * training parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 */
	private static String[] translateRandomForest(RandomForestParameters params)
	{
		ArrayList<String> wekaParams = new ArrayList<String>();
		if(params.isSetNumberOfFeatures())
		{
			wekaParams.add("-K");
			wekaParams.add(Integer.toString(params.getNumberOfFeatures()));
		}
		if(params.isSetNumberOfTrees())
		{
			wekaParams.add("-I");
			wekaParams.add(Integer.toString(params.getNumberOfTrees()));
		}
		if(params.isSetSeed())
		{
			wekaParams.add("-S");
			wekaParams.add(Integer.toString(params.getSeed()));
		}
		String[] stringParams = new String[wekaParams.size()];
		return wekaParams.toArray(stringParams);
	}
	
	/** 
	 * Translates a k-Nearest Neighbor model parameter into the apropriate 
	 * Weka training parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 */
	private static  String[] translateKNearestNeighbor(KNearestNeighborParameters params)
	{
		ArrayList<String> wekaParams = new ArrayList<String>();
		if(params.isSetNumberOfNeighbors())
		{
			wekaParams.add("-K");
			wekaParams.add(Integer.toString(params.getNumberOfNeighbors()));
		}
		String[] stringParams = new String[wekaParams.size()];
		return wekaParams.toArray(stringParams);
	}
	
	/** 
	 * Translates a naive bayesion model parameter into the apropriate Weka 
	 * training parameters string array.
	 *
	 * @param params The training parameters in Hemlock format.
	 * @return The parameter string arrray to specify Weka training 
	 * parameters 
	 */
	private static String[] translateNaiveBayesian(NaiveBayesianParameters params)
	{
		return new String[0];
	}
}
