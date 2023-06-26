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
 * Specifies parameters for building a RIPPER classifier.
 *  
 * @author Sean A. Gilpin
 */

public class RIPPERParameters extends ModelParameters
{
	private boolean checkErrorRate;
	private boolean isCheckErrorRateSet = false;
	private int minNumber;
	private boolean isMinNumberSet = false;
	private int optimizations;
	private boolean isOptimizationsSet = false;
	private boolean usePruning;
	private boolean isUsePruningSet = false;
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
	public RIPPERParameters(FrameworkType frameworkType)
	{
		super(frameworkType, ModelType.RIPPER);
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
	public RIPPERParameters(FrameworkType frameworkType, Hashtable<String,String> parameters)
	{
		this(frameworkType);
		
		if(parameters.containsKey("checkErrorRate"))
		{
			this.setCheckErrorRate(Boolean.parseBoolean(parameters.get("checkErrorRate")));
		}
		if(parameters.containsKey("minNumber"))
		{
			this.setMinNumber((int) Double.parseDouble(parameters.get("minNumber")));
		}
		if(parameters.containsKey("optimizations"))
		{
			this.setOptimizations((int) Double.parseDouble(parameters.get("optimizations")));
		}
		if(parameters.containsKey("usePruning"))
		{
			this.setUsePruning(Boolean.parseBoolean(parameters.get("usePruning")));
		}
		if(parameters.containsKey("seed"))
		{
			this.setSeed((int) Double.parseDouble(parameters.get("seed")));
		}
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
	
	
	/**
	 * Specify if pruning should be used when training the model.
	 *
	 * @param usePruning Specifies if pruning should be used
	 */
	public void setUsePruning(boolean usePruning)
	{
		this.usePruning = usePruning;
		isUsePruningSet = true;
	}
	
	/**
	 * Retrieve the parameter regarding whether pruning should be used
	 *
	 * @return True if pruning is set to be used.
	 */
	public boolean getUsePruning()
	{
		return usePruning;
	}
	
	/**
	 * Check if the pruning parameters has been set.
	 *
	 * @return True if it has been explicitly set.
	 */
	public boolean isSetUsePruning()
	{
		return isUsePruningSet;
	}
	
	/**
	 * Specify the number of optimization rounds to be used when training.
	 *
	 * @param optimizations The number of optimizations to be used.
	 */
	public void setOptimizations(int optimizations)
	{
		this.optimizations = optimizations;
		isOptimizationsSet = true;
	}
	
	
	/**
	 * Retrieve the number of optimizations to be used when training.
	 *
	 * @return The number of optimizations to be used
	 */
	public int getOptimizations()
	{
		return optimizations;
	}
	
	/**
	 * Check if the optimization paramater has been explicitly set.
	 *
	 * @return True if the optimization parameter has been explicitly set.
	 */
	public boolean isSetOptimizations()
	{
		return isOptimizationsSet;
	}
	
	/**
	 * Specify the minimum number of instances per rule while training.
	 *
	 * @param minNumber The minimum number of instances per rule.
	 */
	public void setMinNumber(int minNumber)
	{
		this.minNumber = minNumber;
		isMinNumberSet = true;
	}
	
	/**
	 * Get the value of the minimum number of instances parameter.
	 *
	 * @return The minimum number of instances parameter.
	 */
	public int getMinNumber()
	{
		return minNumber;
	}
	
	/**
	 * Check if the minimum number of instances parameter has been
	 * explicitly set.
	 *
	 * @return True if the minimum number of instances parameter has been
	 * 	explicitly set.
	 */
	public boolean isSetMinNumber()
	{
		return isMinNumberSet;
	}
	
	/**
	 * Specify whether or not the error rate should be used in the 
	 * stopping criterion.
	 *
	 * @param checkErrorRate Value that error rate parameter will take.
	 */
	public void setCheckErrorRate(boolean checkErrorRate)
	{
		this.checkErrorRate = checkErrorRate;
		isCheckErrorRateSet = true;
	}
	
	/**
	 * Get the value of ther error rate parameter.
	 *
	 * @return True if the error rate will be used in the stopping 
	 *	criterion.
	 */
	public boolean getCheckErrorRate()
	{
		return checkErrorRate;
	}
	
	/**
	 * Check if the error rate parameter has been explicity set.
	 *
	 * @return True if the error rate parameter has been explicity set.
	 */
	public boolean isSetCheckErrorRate()
	{
		return isCheckErrorRateSet;
	}
	
}
