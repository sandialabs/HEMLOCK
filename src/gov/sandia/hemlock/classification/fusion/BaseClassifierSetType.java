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

package gov.sandia.hemlock.classification.fusion;

/**
 * Enumerates the methods for specifying a base classifier set.  Currently
 * there are two methods.  ModelParameters corresponds to specifying a set
 * of ModelParameters which will be used to build a set of base classifiers.
 * LoadModels correspsonds to loading a set of previously serialized base 
 * classifiers 
 *
 * @author Sean A. Gilpin
 */
public enum BaseClassifierSetType
{
	ModelParameters, LoadModels;
}
