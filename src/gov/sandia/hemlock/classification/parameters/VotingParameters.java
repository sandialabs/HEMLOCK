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
 * Specifies parameters for building a Voting Ensemble classifier.
 *  
 * @author Sean A. Gilpin
 */

public class VotingParameters extends EnsembleParameters
{
	
	/**
	 * Constructor for creating parameters instance, where none of the
	 * parameters will be set besides the mandatory modelType and 
	 * frameworkType.
	 *
	 * @param frameworkType The framework that should be used to build the
	 *	model.
	 */
	public VotingParameters(FrameworkType frameworkType)
	{
		super(frameworkType, ModelType.Voting);
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
	public VotingParameters(FrameworkType frameworkType, Hashtable<String,String> parameters)
	{
		this(frameworkType);
		
		setEnsembleParameters(parameters);	
	}
	
	
}
