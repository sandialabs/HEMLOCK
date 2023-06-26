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
import gov.sandia.hemlock.core.FrameworkType;

/**
 * Specifies parameters for building a K-Nearest Neighbor classifier.
 *  
 * @author Sean A. Gilpin
 */
public class KNearestNeighborParameters extends ModelParameters
{
	private int numberOfNeighbors;
	private boolean isNumberOfNeighborsSet = false;
	
	/**
	 * Constructor for creating parameters instance, where none of the
	 * parameters will be set besides the mandatory modelType and 
	 * frameworkType.
	 *
	 * @param frameworkType The framework that should be used to build the
	 *	model.
	 */
	public KNearestNeighborParameters(FrameworkType frameworkType)
	{
		super(frameworkType, ModelType.KNearestNeighbor);
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
	public KNearestNeighborParameters(FrameworkType frameworkType, Hashtable<String, String> parameters)
	{
		this(frameworkType);
		//numberOfNeighbors
		if(parameters.containsKey("numberOfNeighbors"))
		{
			setNumberOfNeighbors((int)Double.parseDouble(parameters.get("numberOfNeighbors")));
		}
	}
	
	/**
	 * Set the number of neighbors that should be considered by the KNN
	 * algorithm.
	 *
	 * @param k the number of neighbors to consider by KNN.
	 */
	public void setNumberOfNeighbors(int k)
	{
		numberOfNeighbors = k;
		isNumberOfNeighborsSet = true;
	}
	
	/**
	 * Return the number of neighbors that should be considered by the KNN
	 * algorithm that is currently set.
	 */
	public int getNumberOfNeighbors()
	{
		return numberOfNeighbors;
	}
	
	/**
	 * Check whether the parameter numberOfNeighbors has been set by the
	 * user.
	 *
	 * @return True if it has been set by the user.
	 */
	public boolean isSetNumberOfNeighbors()
	{
		return isNumberOfNeighborsSet;
	}
}
