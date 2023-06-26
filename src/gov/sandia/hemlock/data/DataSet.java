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
import java.io.*;

/**
 * A set of labeled instances along with information about the original source
 * of the data, and a schema.  This is the data set that is used throughout
 * Hemlock, but other external framework specific types of data sets must also
 * be used when interfacing with those frameworks.
 *
 * @author Sean A. Gilpin
 */
public class DataSet implements IDataSet
{
	/** Information about the format of the instances */
	public final RecordSchema recordSchema;
	/** A set of instances */
	public final ArrayList<double[]> records;
	/** Information about the data set such as its source and name */
	public DataSetInfo info;
	
	/**
	 * Constructor that takes individual parts of a data set and puts them
	 * together in one DataSet.
	 *
	 * @param schema The data set schema to be used.
	 * @param info The information about the data set.
	 * @param record The instances to be put in this data set.
	 */
	public DataSet(RecordSchema schema, DataSetInfo info, ArrayList<double[]> records)
	{
		this.recordSchema = schema;
		this.records = records;
		this.info = info;
	}
	
	/**
	 * If this data set was created generatively, it can be saved to the 
	 * disk in Modified C45 format for later use using this method.  The
	 * path that is used to save the data set is in the DataSetInfo object
	 * associate with this data set.
	 *
	 * @throws FileNotFoundException
	 */
	public void serialize() throws FileNotFoundException
	{
		//make data directory
		File dataDirectory = new File(info.absolutePath);
		dataDirectory.mkdirs();
		
		//write data file
		String dataFileString = recordsToString();
		File dataFile = new File(info.absolutePath + "/" + info.dataSetName + ".data");
		FileOutputStream fos = new FileOutputStream(dataFile);
		PrintStream ps = new PrintStream(fos);
		ps.print(dataFileString);
		ps.close();
		
		//write names file
		String namesFileString = recordSchema.toString();
		File namesFile = new File(info.absolutePath + "/" + info.dataSetName + ".names");
		FileOutputStream fosNames = new FileOutputStream(namesFile);
		PrintStream psNames = new PrintStream(fosNames);
		psNames.print(namesFileString);
		psNames.close();
	}
	
	/**
	 * Used for saving this data set to disk.  Will conver all instances
	 * into the apropriate string values for writing in Modified C45 format.
	 *
	 * @return All of the instances in this data set as one large string
	 */
	public String recordsToString()
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < records.size(); i++)
		{
			double[] record = records.get(i);
			for(int j=0; j < recordSchema.numAttributes; j++)
			{
				
				if(recordSchema.attributeTypes[j] == AttributeType.Continuous)
					sb.append(record[j]);
				else
					sb.append((int)record[j]);
				sb.append(" ");
			}
			sb.append((int)record[recordSchema.numAttributes]); //write class label
			sb.append("\n");
		}
		return sb.toString();
	}
}
