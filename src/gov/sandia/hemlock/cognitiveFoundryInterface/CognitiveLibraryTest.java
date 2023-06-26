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

package gov.sandia.hemlock.cognitiveFoundryInterface;

/**
 * Used to check if the Cognitive Framework classes can be loaded for use in Hemlock.
 */
public class CognitiveLibraryTest
{
	public CognitiveLibraryTest() throws ClassNotFoundException
	{
		testAll();
	}
	
	public void testAll() throws ClassNotFoundException
	{
		//See if you can access all of the cognitive foundry classes you might need
		throw new ClassNotFoundException();
	}
}
