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

package gov.sandia.hemlock.classification.parameters;

import gov.sandia.hemlock.classification.ModelType;
import gov.sandia.hemlock.core.*;

import java.util.*;

/**
 * Base class for specifying parameters for a model to be constructed.  Is
 * not meant to every be instantiated.  Only subclasses of this class should
 * be instantiated.  Every derived class must always must specify a framework 
 * type and a model type.
 * 
 * @author Sean A. Gilpin
 */
public class ModelParameters
{
	/** The learning algorithm that should be used to build the model */
	public final ModelType modelType;
	/** The framework that should be used to build the model */
	public final FrameworkType frameworkType;
		
	protected boolean isEnsemble = false;
	
	/**
	 * This is the constructor for ModelParameters which is not meant to
	 * be called by any method other than a derived class constructor
	 *
	 * @param framework The framework that should be used to build the
	 *	model.
	 * @param modelType The type of model that should be built.
	 */
	protected ModelParameters(FrameworkType frameworkType, ModelType modelType)
	{
		this.modelType = modelType;
		this.frameworkType = frameworkType;
	}
	
	
	/**
	 * This is used to determine if it is safe to cast an object that
	 * is a ModelParameter into an {@link EnsembleParameters}.
	 * EnsembleParameter class provides more general functionality for
	 * ModelParameters that specify a model that is a classication 
	 * ensemble.
	 *
	 * @return True when the ModelParameter is an EnsembleParameter.
	 */
	public boolean isEnsemble()
	{
		return isEnsemble;
	}
	
	/**
	 * Used to instantiate ModelParameters derived classes.  Each value
	 * from the enum {@link ModelType} corresponds to a subclass of
	 * ModelParameters.  
	 *
	 * @param frameworkType The framework that should be used to build the
	 *	model.
	 * @param modelType The type of model that should be built.  This will
	 *	determine which subclass of ModelParameters is instantiated.
	 * @return The instantiated subclass of ModelParameters.
	 */
	public static ModelParameters createModelParameters(FrameworkType frameworkType, ModelType modelType, Hashtable<String,String> parameters)
	{
		switch(modelType)
		{
		case KNearestNeighbor:
			return new KNearestNeighborParameters(frameworkType, parameters);
		case NaiveBayesian:
			return new NaiveBayesianParameters(frameworkType, parameters);
		case RandomForest:
			return new RandomForestParameters(frameworkType, parameters);
		case RIPPER:
			return new RIPPERParameters(frameworkType, parameters);
		case SVM:
			return new SVMParameters(frameworkType, parameters);
		case RandomTree:
			return new RandomTreeParameters(frameworkType, parameters);
		case Voting:
			return new VotingParameters(frameworkType, parameters);
		case LinearRegression:
			return new LinearRegressionParameters(frameworkType, parameters);
		case SumRule:
			return new SumRuleParameters(frameworkType, parameters);
		default:
			return new ModelParameters(frameworkType, modelType);
		}
	}
}
