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
import gov.sandia.hemlock.core.FrameworkType;
import java.util.*;
/**
 * Specifies parameters for building a Support Vector Machine classifier.
 *  
 * @author Sean A. Gilpin
 */

public class SVMParameters extends ModelParameters
{
	
	private boolean buildLogisticModels;
	private boolean isBuildLogisticModelsSet;
	private double complexity;
	private boolean isComplexitySet;
	private double epsilon;
	private boolean isEpsilonSet;
	private int seed;
	private boolean isSeedSet;
	
	//Some parameters not added:
	//kernel
	//turnChecksOff
	//filterType
	//toleranceParameter

	
	/**
	 * Constructor for creating parameters instance, where none of the
	 * parameters will be set besides the mandatory modelType and 
	 * frameworkType.
	 *
	 * @param frameworkType The framework that should be used to build the
	 *	model.
	 */
	public SVMParameters(FrameworkType frameworkType)
	{
		super(frameworkType, ModelType.SVM);
	}
	
	/**
	 * Constructor for creating parameter instance while simultaneously
	 * setting all of the parameters that are found in the parameters
	 * hashtable. 
	 *
	 * @param frameworkType The framework that should be used to build the
	 *	model.
	 * @param parameters A hashtable with parameter name/value pairs that 
	 * 	will be used to initiate the values of this instance.
	 */
	public SVMParameters(FrameworkType frameworkType, Hashtable<String,String> parameters) 
	{
		this(frameworkType);
		
		//buildLogisticModels
		if(parameters.containsKey("buildLogisticModels"))
		{
			this.setBuildLogisticModels(Boolean.parseBoolean(parameters.get("buildLogisticModels")));
		}
		//complexity
		if(parameters.containsKey("complexity"))
		{
			this.setComplexity(Double.parseDouble(parameters.get("complexity")));
		}
		//epsilon
		if(parameters.containsKey("epsilon"))
		{
			this.setEpsilon(Double.parseDouble(parameters.get("epsilon")));
		}
		//seed
		if(parameters.containsKey("seed"))
		{
			this.setSeed((int)Double.parseDouble(parameters.get("seed")));
		}
	}
	
	/**
	 * Specify whether or not a logistic model should be trained.
	 *
	 * @param buildLogisticModels Whether or not a logistic model should
	 *	be trained.
	 */
	public void setBuildLogisticModels(boolean buildLogisticModels)
	{
		this.buildLogisticModels = buildLogisticModels;
		isBuildLogisticModelsSet = true;
	}
	
	/**
	 * Get value of the build logistic model parameter.
	 *
	 * @return The value of the build logisitic model parameter.
	 */
	public boolean getBuildLogisticModels()
	{
		return buildLogisticModels;
	}
	
	/**
	 * Check if the build logistic model parameter has been explicitly set.
	 *
	 * @return True if the logistic model parameter has been explicitly set.
	 */
	public boolean isSetBuildLogisticModels()
	{
		return isBuildLogisticModelsSet;
	}
	
	/**
	 * Specify the complexity parameter for training.
	 *
	 * @param complexity The complexity to use while training.
	 */
	public void setComplexity(double complexity)
	{
		this.complexity = complexity;
		isComplexitySet = true;
	}
	
	/**
	 * Get the value of the complexity parameter.
	 *
	 * @return The complexity parameter.
	 */
	public double getComplexity()
	{
		return complexity;
	}
	
	/**
	 * Check to see if the complexity parameter has been explicity set.
	 *
	 * @return True if the complexity parameter has been explicitly set.
	 */
	public boolean isSetComplexity()
	{
		return isComplexitySet;
	}
	
	/**
	 * Specify the value of epsilon, the round off error.
	 *
	 * @param epsilon The minimum precision that will be used for training.
	 */
	public void setEpsilon(double epsilon)
	{
		this.epsilon = epsilon;
		isEpsilonSet = true;
	}
	
	/**
	 * Get the value of the epsilon parameter.
	 *
	 * @return the value of the epsilon parameter.
	 */
	public double getEpsilon()
	{
		return epsilon;
	}
	
	/**
	 * Check if the epsilon parameter has been explicitly set.
	 *
	 * @return True if the epsilon parameter has been explicitly set.
	 */
	public boolean isSetEpsilon()
	{
		return isEpsilonSet;
	}
	
	/**
	 * Set the value of the seed used to generate random numbers in this
	 * learning algorithm.
	 * 
	 * @param seed The seed for the random number generator.
	 */
	public void setSeed(int seed)
	{
		this.seed = seed;
		isSeedSet = true;
	}
	
	/**
	 * If the seed has been set to some value, this will return the value
	 * of the seed.
	 *
	 * @return The value of the seed that has been set.
	 */
	public int getSeed()
	{
		return seed;
	}
	
	/**
	 * Checks to see if the seed has been set.  If it has not been set 
	 * then a specific seed will not be used explicitly for the random
	 * number generator.  One will implictly be created at random.
	 *
	 * @return True if the value of the seed has been set.
	 */
	public boolean isSetSeed()
	{
		return isSeedSet;
	}
	
	
}
