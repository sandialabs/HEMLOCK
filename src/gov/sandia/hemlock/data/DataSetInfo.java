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

import java.io.*;

/**
 * Meta information about a data set.  Data set queries can use this data to 
 * find a set of data sets that have a given meta property.
 *
 * @author Sean A. Gilpin
 */
public class DataSetInfo
{
	/** The file format the original data set was in.*/
	public FileFormatType fileFormat;
	/** The path to the file data set was loaded from */
	public String absolutePath = "";
	/** The name of the data set */
	public String dataSetName = "";
	/** The name of the repository data set was loaded from */
	public String repositoryName = "";
	/** Whether or not data has unknown values in it */
	public boolean hasUnknownValues = false;
	/** The number of records in the data set */
	public int numberOfRecords = 0;
	/** The number of continuous attributes in the data set */
	public int numberOfContinuous = 0;
	/** The number of discrete attibutes in the data set */
	public int numberOfNominal = 0;
	/** The number of possible class values an instance in the data set can
		have */
	public int numberOfClasses = 0;	

	
	/**
	 * Searches an array to find the DataSetInfo with the given name. 
	 *
	 * @param infoArr Array to search through.
	 * @param dataSetName Name of data set who's DataSetInfo is to be found.
	 * @return The data set info that is found, or null if no data set
	 *	has that name.
	 */
	public static DataSetInfo getByName(DataSetInfo[] infoArr, String dataSetName)
	{
		for(int i = 0; i < infoArr.length; i++)
		{
			if(infoArr[i].dataSetName.equals(dataSetName))
				return infoArr[i];
		}
		return null;
	}
	
	/**
	 * Creates an empy DataSetInfo and adds information about the path and
	 * name of some data set.  The subsequent DataSetInfo can be used
	 * to load that data set from the disk.
	 *
	 * @param path Path to the data set.
	 * @param name Name to give to this data set.
	 * @param format The format the data set files will be in.
	 * @return The resulint DataSetInfo object.
	 */
	public static DataSetInfo fromPath(String path, String name, FileFormatType format)
	{
		DataSetInfo dsi = new DataSetInfo();
		File directory = new File(path);
		dsi.absolutePath = directory.getAbsolutePath();
		dsi.dataSetName = name;
		dsi.fileFormat = format;
		
		return dsi;
	}
}
