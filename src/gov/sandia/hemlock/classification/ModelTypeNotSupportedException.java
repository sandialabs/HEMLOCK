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

package gov.sandia.hemlock.classification;

import gov.sandia.hemlock.core.*; 

/**
 * Exception that is thrown when a given framework does not support the 
 * specified ModelType.  Not all interfaced frameworks necisarily support all
 * of the types of types of models specified in {@link ModelType}.  If such an
 * unsupported combination of ModelType and 
 * {@link gov.sandia.hemlock.core.FrameworkType} is attempted, this exception
 * should be thrown.  For details of which combinations are supported, see the 
 * details within the source of {@link ClassifierFactory}
 *
 * @author Sean A. Gilpin
 */
public class ModelTypeNotSupportedException extends Exception
{
	/** The type of model that was attempted to be built*/
	public String modelType;
	/** The model building framework that does not support modelType */
	public String frameworkType;
	
	/**
	 * Constructor which creates the exception when modelType and
	 * frameworkType are incompatible.
	 *
	 * @param modelType The type of model that was attempted to be built
	 * @param frameworkType The model building framework that does not 
	 * 	support modelType
	 */
	public ModelTypeNotSupportedException(ModelType modelType, FrameworkType frameworkType)
	{
		super();
		this.modelType = modelType.toString();
		this.frameworkType = frameworkType.toString();
	}
	
	/**
	 * Creates the appropriate error message.
	 *
	 * @return The error message the user will recieve.
	 */
	public String getError()
	{
		return "HEMLOCK is not cofigured to use " + modelType + "with the framework " + frameworkType + ".";
	}
}
