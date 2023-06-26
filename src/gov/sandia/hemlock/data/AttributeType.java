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

package gov.sandia.hemlock.data;

/**
 * Types of features or attributes that Hemlock can accept as part of input 
 * instances.  Continuous attributes take any value that can be represented
 * as a double.  Discrete valued attributes can take any value from a set
 * of string labels.  Discrete valued attributes must have the set of labels
 * explicitly specified.
 *
 * @author Sean A. Gilpin
 */
public enum AttributeType
{
	Continuous, Discrete;
}
