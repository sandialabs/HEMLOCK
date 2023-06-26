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

package gov.sandia.hemlock.core;


/**
 * This is the exception that should be thrown any time a resource is requested 
 * from an external framework that is not available to Hemlock at that time.
 *
 * @author Sean A. Gilpin
 */
public class FrameworkNotSupportedException extends Exception
{
	private String frameworkName;
	
	/**
	 * Constructor for the exception which requires the name of the 
	 * framework that was requested.
	 *
	 * @param frameworkName The name of the framework that was requested
	 *	but is not available.
	 */
	public FrameworkNotSupportedException(String frameworkName)
	{
		super();
		this.frameworkName = frameworkName;
	}
	
	/**
	 * Creates user readable error message corresponding to this exception.
	 *
	 * @return The user readable error message.
	 */
	public String getError()
	{
		return "The framework " + frameworkName 
			+ " is not supported without further configuration.";
	}
}
