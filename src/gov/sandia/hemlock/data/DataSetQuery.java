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
 * Used to specify properties for a set of data sets that a user would like to 
 * obtain.  Also has methods to actually find the data sets that satisfy a 
 * given DataSetQuery.
 *
 * @author Sean A. Gilpin
 */
public class DataSetQuery
{
	private boolean auto = true;

	private boolean continuousAttributes;
	private boolean isSetContinuousAttributes = false;

	private boolean nominalAttributes;
	private boolean isSetNominalAttributes = false;
	
	private boolean missingValues;
	private boolean isSetMissingValues = false;
	
	private String repositoryName;
	private boolean isSetRepositoryName = false;
	
	private String[] datasetNamesToAdd;
	private DataSetInfo[] datasetInfoToAdd;
	
	/**
	 * Constructor for DataSetQuery which specifies whether or not the 
	 * query will do a search or not
	 *
	 * @param automatic If set to true then a data set search will be done. 
	 */
	public DataSetQuery(boolean automatic)
	{
		this.auto = automatic;
	}
	
	/**
	 * Can use this to explicity set which data sets to use by specifying
	 * their names here.
	 *
	 * @param data An array of data set names whose corresponding data sets
	 *	will be returned when executing this query.
	 */
	public void setDataSets(String[] data)
	{
		datasetNamesToAdd = data;
	}
	
	/**
	 * If this is used in conjuction with a query that is not automatic,
	 * all of the data sets associated with the array of DataSetInfos
	 * provided as a parameter, will be added to the results of the query.
	 *
	 * @param data Array of DataSetInfo whose associated data sets
	 * 	are to beto added to results of query
	 */
	public void setDataSetInfos(DataSetInfo[] data)
	{
		this.datasetInfoToAdd = data;
	}

	/**
	 * Used to determine if an automatic search is supposed to be performed.
	 *
	 * @return True if an automatic search should be performed.
	 */
	public boolean isAuto()
	{
		return auto;
	}
	
	/** 
	 * Used to determine if the continuousAttributes meta feature should
	 *	be used to filter data sets to be included in the set. 
	 *
	 * @return True if the continuousAttributes meta feature should
	 *	be used to filter data.
	 */
	public boolean isSetContinuousAttributes()
	{
		return isSetContinuousAttributes;
	}
	
	/**
	 * Set to true if data sets with continuous features should
	 *	be included in the set or false otherwise 
	 *
	 * @param set If true then only data sets with continuous features
	 *	will be used.
	 */
	public void setContinuousAttributes(boolean set)
	{
		continuousAttributes = set;
		isSetContinuousAttributes = true;
	}
	
	/**
	 * Determine if data sets with continuous features will be
	 *	included in the set
	 *
	 * @return True  if data sets with continuous features will be included.
	 */
	public boolean getContinuousAttributes()
	{
		return continuousAttributes;
	}
	
	/** 
	 * Used to determine if the nominalAttributes meta feature should
	 *	be used to filter data sets to be included in the set. 
	 *
	 * @return True if the nominalAttributes meta feature should
	 *	be used to filter data.
	 */
	public boolean isSetNominalAttributes()
	{
		return isSetNominalAttributes;
	}
	
	/**
	 * Set to true if data sets with nominal features should
	 *	be included in the set or false otherwise 
	 *
	 * @param set If true then only data sets with nominal features
	 *	will be used.
	 */
	public void setNominalAttributes(boolean set)
	{
		nominalAttributes = set;
		isSetNominalAttributes = true;
	}
	
	/**
	 * Determine if data sets with nominal features will be
	 *	included in the set
	 *
	 * @return True  if data sets with nominal features will be included.
	 */
	public boolean getNominalAttributes()
	{
		return nominalAttributes;
	}
	
	/** 
	 * Used to determine if the missingValues meta feature should
	 *	be used to filter data sets to be included in the set. 
	 *
	 * @return True if the missingValues meta feature should
	 *	be used to filter data.
	 */
	public boolean isSetMissingValues()
	{
		return  isSetMissingValues;
	}
	
	/**
	 * Set to true if data sets with missing values should
	 *	be included in the set or false otherwise 
	 *
	 * @param set If true then only data sets with missing values
	 *	will be used.
	 */
	public void setMissingValues(boolean set)
	{
		missingValues = set;
		isSetMissingValues = true;
	}
	
	/**
	 * Determine if data sets with missing values will be
	 *	included in the set
	 *
	 * @return True if data sets with missing values will be included.
	 */
	public boolean getMissingValues()
	{
		return missingValues;
	}
	
	/**
	 * If the repository name is set then only that repository will be
	 * used to search for data sets.  This checks to see if it has been
	 * set.
	 *
	 * @return True if the repository name has been set.
	 */
	public boolean isSetRepositoryName()
	{
		return isSetRepositoryName;
	}
	
	/** 
	 * Specify the name of the repository that should be searched.  If this
	 * is set only this specific repository will be searched.  If it is
	 * not explicitly searched, then all repositories will be searched.
	 *
	 * @param repositoryName The name of the repository that should
	 * 	exclusively be used in any data set search.
	 */
	public void setRepositoryName(String repositoryName)
	{
		this.repositoryName = repositoryName;
		isSetRepositoryName = true;
	}
	
	
	/** 
	 * Get the name of the repository that has been specified for searching.
	 *
	 * @return The name of the repository that will be searched.
	 */
	public String getRepositoryName()
	{
		return repositoryName;
	}
	
	/** 
	 * Performs the actions necesary to execute this query and return a 
	 * list of data sets that this query matches.
	 *
	 * @param dataSetInfo Data set information loaded from repositories
	 *	where the data set query will look for data sets.
	 * @return An array of data sets that matched this query
	 * @throws Exception
	 */
	public DataSet[] getDataSets(DataSetInfo[] dataSetInfo) throws Exception
	{
		ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
		if(isAuto())
		{
			for(int i = 0; i < dataSetInfo.length; i++)
			{
				boolean importSet = true;
				if(isSetContinuousAttributes())
				{
					importSet &= (getContinuousAttributes() == (dataSetInfo[i].numberOfContinuous > 0));
				}
				if(isSetMissingValues())
				{
					importSet &= (getMissingValues() == dataSetInfo[i].hasUnknownValues);
				}
				if(isSetNominalAttributes())
				{
					importSet &= (getNominalAttributes() == (dataSetInfo[i].numberOfNominal > 0));
				}
				if(isSetRepositoryName())
				{
					importSet &= (getRepositoryName().equals(dataSetInfo[i].repositoryName));
				}
				
				if(importSet)
				{
					DataImporter importer = new DataImporter();
					dataSets.add(importer.importDataSet(dataSetInfo[i]));
				}
			}
		}
		else
		{
			for(int i = 0; i < dataSetInfo.length; i++)
			{
				for(int j = 0; j < datasetNamesToAdd.length; j++)
				{
					
					int sepIndex = datasetNamesToAdd[j].indexOf(".");
					String dataSetName = datasetNamesToAdd[j].substring(sepIndex + 1).trim();
					
					if(dataSetInfo[i].dataSetName.equalsIgnoreCase(dataSetName))
					{
						DataImporter importer = new DataImporter();
						dataSets.add(importer.importDataSet(dataSetInfo[i]));
					}
				}
			}
			for(int i = 0; i < datasetInfoToAdd.length; i++)
			{			
				DataImporter importer = new DataImporter();
				dataSets.add(importer.importDataSet(datasetInfoToAdd[i]));
			}
		}
	
		DataSet[] arrDataSets = new DataSet[dataSets.size()];
		return dataSets.toArray(arrDataSets);
	}
}
