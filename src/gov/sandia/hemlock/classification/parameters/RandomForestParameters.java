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
 * Specifies parameters for building a Random Forest classifier.
 *  
 * @author Sean A. Gilpin
 */

public class RandomForestParameters extends ModelParameters
{
	private int numberOfFeatures;
	private boolean isNumberOfFeaturesSet = false;
	private int numberOfTrees;
	private boolean isNumberOfTreesSet = false;
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
	public RandomForestParameters(FrameworkType frameworkType)
	{
		super(frameworkType, ModelType.RandomForest);
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
	public RandomForestParameters(FrameworkType frameworkType, Hashtable<String,String> parameters) 
	{
		this(frameworkType);
		

		//numFeatures
		if(parameters.containsKey("numFeatures"))
		{
			this.setNumberOfFeatures((int)Double.parseDouble(parameters.get("numFeatures")));
		}
		
		//numTrees
		if(parameters.containsKey("numTrees"))
		{
			this.setNumberOfTrees((int)Double.parseDouble(parameters.get("numTrees")));
		}
		
		//seed
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
	 * @param numberOfFeatures The number of features that will be radomly
	 * 	selected.
	 */
	public void setNumberOfFeatures(int numberOfFeatures)
	{
		this.numberOfFeatures = numberOfFeatures;
		isNumberOfFeaturesSet = true;
	}
	
	/**
	 * Get the value that has been set for the number of features parameter.
	 *
	 * @return The number of features that will be randomly chosen while
	 *	building a random tree.
	 */
	public int getNumberOfFeatures()
	{
		return numberOfFeatures;
	}
	
	/**
	 * Check to see if the number of features parameter has been explicitly
	 * set.
	 *
	 * @return True if the number of features parameter has been explicitly
	 * set.
	 */
	public boolean isSetNumberOfFeatures()
	{
		return isNumberOfFeaturesSet;
	}
	
	/**
	 * Specify the number of trees that should be "grown" in this forest.
	 *
	 * @param numberOfTrees The number of trees to be added to the forest.
	 */
	public void setNumberOfTrees(int numberOfTrees)
	{
		this.numberOfTrees = numberOfTrees;
		isNumberOfTreesSet = true;
	}
	
	/**
	 * Get the value of the number of trees parameter.
	 *
	 * @return The number of trees to be added to the forest.
	 */
	public int getNumberOfTrees()
	{
		return numberOfTrees;
	}
	
	/**
	 * Check whether the number of trees parameter has been explicity set
	 *
	 * @return True if the number of trees parameter has been explicitly
	 * 	set.
	 */
	public boolean isSetNumberOfTrees()
	{
		return isNumberOfTreesSet;
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
