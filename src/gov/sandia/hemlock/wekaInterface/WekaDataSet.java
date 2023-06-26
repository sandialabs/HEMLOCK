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

import gov.sandia.hemlock.data.*;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import java.io.*;

/**
 * Data structure for Weka's representations of instances and schemas.  
 * Contains algorithms for converting from Hemlock data set types.
 *
 * @author Sean A. Gilpin
 */
public class WekaDataSet implements IDataSet, Serializable
{
	/** Weka internal representation of the data set schema*/
	public FastVector recordSchema;
	/** Weka internal representation for the instances*/
	public Instances records;
	
	/**
	 * Constructor that creates a Weka data set by translating from a 
	 * Hemlock data set.
	 *
	 * @param DataSet The Hemlock data set that needs to be converted.
	 */
	public WekaDataSet(IDataSet dataSet)
	{
		DataSet data = (DataSet) dataSet;
		recordSchema = createSchema(data.recordSchema);
		records = createInstances(data);	
		records.setClassIndex(records.numAttributes() - 1);
	}
	
	/** 
	 * Gives a copy of a Weka data set that has only the schema with
	 * all of the instances removed.
	 *
	 * @return A copy of this data set with none of the instances but
	 * 	with the schema intact.
	 */
	public WekaDataSet getEmptyCopy()
	{
		WekaDataSet wds = new WekaDataSet();
		wds.recordSchema = recordSchema;
		wds.records = new Instances("", recordSchema, 0);
		wds.records.setClassIndex(wds.records.numAttributes() - 1);
		return wds;
	}
	
	/** 
	 * Created to enforce that empty constructor cannot be used.
	 *
	 */
	private WekaDataSet()
	{
	}
	
	/**
	 * Convert instances from Hemlock format into weka format.
	 *
	 * @param data The Hemlock data set with instances to be converted
	 *	to Weka format.
	 * @return A set of instances in Weka data format.
	 */
	private Instances createInstances(DataSet data)
	{
		//the schema must have been created first
		if(recordSchema != null)
		{
			//Create recordSet
			Instances records = new Instances("", recordSchema, data.records.size());
			for(int i = 0; i < data.records.size(); i++)
			{
				//Get old row
				double[] oldRow = data.records.get(i);
				//Create a row
				double[] newRow = new double[records.numAttributes()];
				for(int j = 0; j < recordSchema.size(); j++)
				{
					//For continuous values just assign the continuous value which
					//is what is stored in our universal recordSet
					
					//For discrete values look up the index value and store that
					//which is also how we do it in our dataSet so it should
					//be the same if we assigning attribute values in the weka schema
					//in the same order we did in our universal schema.
					
					newRow[j] = oldRow[j];			
				}
				//We set the weight to 1.0 so each record is equal
				records.add(new Instance(1.0, newRow));
			}
			return records;
		}
		else
		{
			System.err.print("Incorrect use of WekaDataSet.  Must use createSchema method before calling createInstance");
			return null;
		}
	}
	
	/** 
	 * Convert a Hemlock schema into a Weka schema.
	 *
	 * @param oldSchema The Hemlock schema to be converted into Weka format
	 * @return The schema in Weka format.
	 */
	private FastVector createSchema(RecordSchema oldSchema)
	{
		FastVector attributes = new FastVector();
		for(int i = 0; i < oldSchema.numAttributes + 1; i++)
		{
			if(i == oldSchema.numAttributes || oldSchema.attributeTypes[i] == AttributeType.Discrete)
			{
				//If we are dealing with a discrete attribute
				//or the class label which is always discrete
				String attributeValues[];
				if(i == oldSchema.numAttributes)
					attributeValues = oldSchema.labels;
				else
					attributeValues = oldSchema.attributeValues[i];
				
			    FastVector values = new FastVector();
			    for (int j = 0; j < attributeValues.length; j++)
			      values.addElement(attributeValues[j]);
			    
			    attributes.addElement(new Attribute("attribute" + i, values));
			}
			else
			{
				//Continuous values
			    attributes.addElement(new Attribute("attribute" + i));
			}   
		}
		
		return attributes;
	}
}
