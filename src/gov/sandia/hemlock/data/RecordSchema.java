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

import java.util.*;

/**
 * Schema for a set of instances describing the domain and range of the classification function
 * that will be estimated.
 *
 * @author Sean A. Gilpin
 */
public class RecordSchema 
{
	/** Possible class values */
	public String[] labels;
	/** The type of each feature in the instances */
	public AttributeType[] attributeTypes;
	/** The possible values for discrete features*/
	public String[][] attributeValues;
	/** The number of features in each instace */
	public int numAttributes;  //does not include label
	
	/**
	 * Deciphers the information about the features and records their
	 * values in the internal data structures.
	 *
	 * @param attributes List of line each of which corresponds to 
	 *	a specification for one feature
	 */
	public void createAttributes(ArrayList<String[]> attributes)
	{
		//Initialize arrays
		numAttributes = attributes.size();
		attributeTypes = new AttributeType[numAttributes];
		attributeValues = new String[numAttributes][];
		//Each record in array list will be either "continuous" 
		//	or "discrete val1 val2 ... valn"
		for(int i = 0; i < numAttributes; i++)
		{
			String[] curAtr = attributes.get(i);
			if(curAtr[0].equalsIgnoreCase("continuous"))
			{
				attributeTypes[i] = AttributeType.Continuous;
			}
			else
			{
				attributeTypes[i] = AttributeType.Discrete;
				
				//remove leading "discrete" from attribute values
				//and record the list of discrete values
				ArrayList<String> values = new ArrayList<String>(Arrays.asList(attributes.get(i)));
				values.remove(0);
				
				String[] valuesArr = new String[values.size()];
				values.toArray(valuesArr);
	
				attributeValues[i] = valuesArr;
			}
		}
	}
	
	/**
	 * Translates an instance from its original format into a numerical
	 * array with the values that are specified in RecordSchema object.
	 *
	 * @param record An instance in the original format.
	 * @return Instance in numerical format.
	 * @throws Exception If there are unexpected values in original 
	 *	instance.
	 */
	public double[] translateRecord(String[] record) throws Exception
	{
		if(record.length != (numAttributes + 1))
		{
			// Should have number of attributes plus one for the class label
			throw new Exception("records not same length as schema specifies.");
		}
		else
		{
			double[] values = new double[numAttributes + 1];
			for(int i = 0; i < record.length; i++)
			{
				values[i] = getTranslation(i, record[i]);
			}
			return values;
		}
	}
	
	/**
	 * Translates one feature from its original format to a numerical value.
	 *
	 * @param attribute The index of the feature in the RecordSchema.
	 * @param value The value of the feature.
	 * @return The translated value of the feature.
	 * @throws Exception If the original value is unexpected and cannot be
	 * 	translated.
	 */
	public double getTranslation(int attribute, String value) throws Exception
	{
		//Discrete values are translated to the index of their entry n attributeValues[attribute]
		//The class label is always discrete
		//Continuous values only need to be parsed into a double
		if(attribute == numAttributes || attributeTypes[attribute] == AttributeType.Discrete )
		{
			String[] values;
			if(attribute == numAttributes)	//Dealing with class label
				values = labels;
			else							//Or an attribute
				values = attributeValues[attribute];
			
			for(int i = 0; i < values.length; i++)
			{
				if(values[i].equals(value))
					return i;
			}
			
			throw new Exception("value " + value + " not found for attribute " + attribute);
		}
		else
		{
			return Double.parseDouble(value);
		}
	}

	/**
	 * Writes the schema into Modified C45 format so it can be subsequently
	 * written to the disk.
	 *
	 * @return The schema in Modified C45 format as a string.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		//write class label line
		for(int i = 0; i < labels.length; i++)
		{
			sb.append(labels[i]);
			sb.append(" ");
		}
		sb.append("\n\n");
		
		//write attribute lines
		for(int i = 0; i < numAttributes; i++)
		{
			if(attributeTypes[i] == AttributeType.Discrete)
			{
				sb.append("discrete ");
				for(int j = 0; j < attributeValues[i].length; j++)
				{
					sb.append(attributeValues[i][j]);
					sb.append(" ");
				}
			}
			else
			{
				sb.append("continuous");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
