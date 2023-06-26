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
import java.io.*;

/**
 * Contains methods for serializing and loading models.  Using these methods
 * models can be saved for later use.
 *
 * @author Sean A. Gilpin
 */
public class ModelSerialization
{
	/**
	 * Save a trained model to a file.  This should be used with caution,
	 * with non-parametric models which must also save the data set used
	 * by the model as it may lead to a larger than desired file size.
	 *
	 * @param toSerialize The classification model which will be serialized.
	 * @param path The path, including file name, which the model will
	 *	be serialized to.
	 * @throws IOException If Hemlock is not able to write to path for any
	 *	reason.
	 */
	public static void serializeModel(Model toSerialize, String path) 
		throws IOException
	{	
	
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(toSerialize);
		out.close();
	}
	
	/**
	 * Load a previously serialized model for use of making further
	 * predictions.
	 *
	 * @param path The path, including file name, where the serialized
	 *	model will be found and loaded from.
	 * @return The classification model that is retrieved.
	 * @throws Exception
	 */
	public static Model loadModel(String path) throws Exception
	{
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream in = new ObjectInputStream(fis);
		Model loaded = (Model)in.readObject();
		in.close();
		return loaded;
	}
}
