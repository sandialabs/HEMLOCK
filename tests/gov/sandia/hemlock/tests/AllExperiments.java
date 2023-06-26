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

package gov.sandia.hemlock.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.*;

/**
 * 
 * This is the JUnit test suite class.  All of the classes in the 
 * SuiteClasses annotation will have their tests run.
 * 
 * @author Sean Gilpin
 * @author Danny Dunlavy
 *
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={WekaInterface.class, UseCaseEndUser.class, Metrics.class, 
	MetaExperiments.class, Evaluation.class, 
	Ensembles.class, BaggingEnsembles.class})
public class AllExperiments {

}