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

package gov.sandia.hemlock.wekaInterface;

/**
 * Used to check if the Weka classes can be loaded for use in Hemlock.
 *
 * @author Sean A. Gilpin
 */
public class WekaLibraryTest
{
	
	/**
	 * Constructor that runs all tests to check the availability of
	 * optional Weka classes.  If these tests fail an exception is
	 * throws indicating that Weka is not configured to be used
	 * as a library for creating classifiers.
	 *
	 * @throws ClassNotFoundException If the classes used for interfacing
	 *	with Weka are not present.
	 */
	public WekaLibraryTest() throws ClassNotFoundException
	{
		testAll();
	}
	
	/**
	 * Test if classes used for building classifiers using Weka can
	 * be loaded.  If not the Weka is not configured to be used
	 * with Hemlock as a classifier training library.
	 *
	 * @throws ClassNotFoundException If the classes used for interfacing
	 *	with Weka are not present.
	 */
	public void testAll() throws ClassNotFoundException
	{
		//See if you can access all of the weka classes you might need
		Class.forName("weka.core.Instance");
		Class.forName("weka.core.FastVector");
		Class.forName("weka.classifiers.trees.RandomForest");
		Class.forName("weka.classifiers.bayes.NaiveBayes");
	}
}
