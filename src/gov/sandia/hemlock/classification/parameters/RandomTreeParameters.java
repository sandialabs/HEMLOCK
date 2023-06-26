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

import java.util.Hashtable;

import gov.sandia.hemlock.classification.ModelType;
import gov.sandia.hemlock.core.*;

/**
 * Specifies parameters for building a Random Tree classifier.
 *  
 * @author Sean A. Gilpin
 */

public class RandomTreeParameters extends ModelParameters
{
	private int numFeatures;
	private boolean isNumFeaturesSet = false;
	private int maxDepth;
	private boolean isMaxDepthSet = false;
	private int minInstances;
	private boolean isMinInstancesSet = false;
	private int seed;
	private boolean isSeedSet = false;
	
	
	/**
	 * Constructor for creating parameters instance, where none of the
	 * parameters will be set besides the mandatory modelType and 
	 * frameworkType.
	 *
	 * @param frameworkType The framework that should be used to build the
	 *	model.
	 */
	public RandomTreeParameters(FrameworkType frameworkType)
	{
		super(frameworkType, ModelType.RandomTree);
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
	public RandomTreeParameters(FrameworkType frameworkType, Hashtable<String,String> parameters)
	{
		this(frameworkType);
		
		if(parameters.containsKey("numFeatures;"))
		{
			this.setNumFeatures((int)Double.parseDouble(parameters.get("numFeatures;")));
		}
		if(parameters.containsKey("maxDepth"))
		{
			this.setMaxDepth((int)Double.parseDouble(parameters.get("maxDepth")));
		}
		if(parameters.containsKey("minInstance"))
		{
			this.setMinInstances((int)Double.parseDouble(parameters.get("minInstance")));
		}
		if(parameters.containsKey("seed"))
		{
			this.setSeed((int)Double.parseDouble(parameters.get("seed")));
		}
	}
	
	/**
	 * Specify the number of features that should be randomly chosen before
	 * selecting the best feature for splitting on, while training a 
	 * random tree.
	 *
	 * @param numFeatures The number of features that will be radomly
	 * 	selected.
	 */
	public void setNumFeatures(int numFeatures)
	{
		this.numFeatures = numFeatures;
		isNumFeaturesSet = true;
	}
	
	/**
	 * Get the value that has been set for the number of features parameter.
	 *
	 * @return The number of features that will be randomly chosen while
	 *	building a random tree.
	 */
	public int getNumFeatures()
	{
		return numFeatures;
	}
	
	/**
	 * Check to see if the number of features parameter has been explicitly
	 * set.
	 *
	 * @return True if the number of features parameter has been explicitly
	 * set.
	 */
	public boolean isSetNumFeatures()
	{
		return isNumFeaturesSet;
	}
	
	/**
	 * Specify the maximum depth of a tree.
	 *
	 * @param maxDepth The maximum depth that the tree will have.
	 */
	public void setMaxDepth(int maxDepth)
	{
		this.maxDepth = maxDepth;
		isMaxDepthSet = true;
	}
	
	/**
	 * Get the value of the maximum depth parameter.
	 *
	 * @return The maximum depth of the tree to be trained.
	 */
	public int getMaxDepth()
	{
		return maxDepth;
	}
	
	/**
	 * Check if the maximum depth parameter has been explicitly set.
	 *
	 * @return true if the maximum depth parameter has been explicty set.
	 */
	public boolean isSetMaxDepth()
	{
		return isMaxDepthSet;
	}
	
	/**
	 * Specify the minimum number of instances per node while training.
	 *
	 * @param minInstances The minimum number of instances per node.
	 */
	public void setMinInstances(int minInstances)
	{
		this.minInstances = minInstances;
		isMinInstancesSet = true;
	}
	
	/**
	 * Get the value of the minimum number of instances parameter.
	 *
	 * @return The minimum number of instances parameter.
	 */
	public int getMinInstances()
	{
		return minInstances;
	}
	
	/**
	 * Check if the minimum number of instances parameter has been
	 * explicitly set.
	 *
	 * @return True if the minimum number of instances parameter has been
	 * 	explicitly set.
	 */
	public boolean isSetMinInstances()
	{
		return isMinInstancesSet;
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
