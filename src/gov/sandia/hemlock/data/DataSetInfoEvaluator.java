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

import gov.sandia.hemlock.data.DataSetInfo;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Used for gathering information about a data set or a repository of data sets.
 * This information will be stored as meta data associated with the data set.
 * This will allow experiments to use data that has specific qualities that
 * may be of interest.
 *
 * @author Sean A. Gilpin
 */
public class DataSetInfoEvaluator
{
	/**
	 * Looks into the Hemlock configuration file and gets a list of 
	 * all repositories and creates evaluates all of the data sets
	 * in those repositories.
	 *
	 * @return The evaluated information about every data set in a
	 *	registered repository.
	 * @throws Exception
	 */
	public static DataSetInfo[] getInfoForAllDataSets() throws Exception
	{
		ArrayList<DataSetInfo> info = new ArrayList<DataSetInfo>();
		//get list of repositories
		HashMap<String, File> repositories = getDataRepositories();
		for(String name: repositories.keySet())
		{
			File reposDirectory = repositories.get(name);
			String[] dataSetNames = reposDirectory.list();
			for(int i = 0; i < dataSetNames.length; i++)
			{
				File curDirectory = new File(reposDirectory.getPath()+ "/" + dataSetNames[i]); 
				if(curDirectory.isDirectory() && !curDirectory.isHidden())
				{
					DataSetInfo curInfo = processDataSet(dataSetNames[i], curDirectory);
					curInfo.fileFormat = FileFormatType.ModifiedC45;
					curInfo.repositoryName = name;
					info.add(curInfo);
				}
			}
		}
		DataSetInfo[] arrInfo = new DataSetInfo[info.size()];
		return info.toArray(arrInfo);
	}
	
	/**
	 * Reads the Hemlock configuration file to get the list of repositories
	 * and their associate paths.
	 *
	 * @return A map of repository name/path values.
	 * @throws Exception
	 */
	private static HashMap<String, File> getDataRepositories() throws Exception
	{
		File file = new File(gov.sandia.hemlock.core.Constants.configurationFilePath);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		Element root = (Element)doc.getElementsByTagName("config").item(0);
		Element datasets = (Element)root.getElementsByTagName("DataSets").item(0);
		NodeList directories = datasets.getElementsByTagName("Repository");
		
		HashMap<String, File> listOfRepositories = new HashMap<String, File>();
		for(int i = 0; i < directories.getLength(); i++)
		{
			Element eDirectory = (Element)directories.item(i);
			//name
			String name = eDirectory.getAttribute("name");
			//file
			CharacterData cdModel = (CharacterData)eDirectory.getFirstChild();
			String strDirectory = cdModel.getData();
			listOfRepositories.put(name, new File(strDirectory));
		}

		return listOfRepositories;
	}
	
	/** 
	 * Evaluate one data set given by a path.
	 *
	 * @param dataSetName The name of the data set to be evaluated
	 * @param directory The path to the data set to be evaluated.
	 * @return The information about the data set that is evaluated
	 * @throws Exception
	 */
	private static DataSetInfo processDataSet(String dataSetName, File directory) throws Exception
	{
		DataSetInfo info = new DataSetInfo();
		info.dataSetName = dataSetName;
		info.absolutePath = directory.getPath();
			
		processDataFile(info);
		processNamesFile(info);
			
		return info;
	}
	
	/**
	 * Reads all of the data for a data set and evaluates it, storing the
	 * findings in the DataSetInfo object.
	 *
	 * @param info Will specify which data set to load and will be location
	 *	to store evaluation.
	 * @throws Exception
	 */
	private static void processDataFile(DataSetInfo info) throws Exception
	{
		//data file information
		String dataFileName =  info.absolutePath + "/" + info.dataSetName + ".data";
		BufferedReader dataReader = new BufferedReader(new FileReader(dataFileName));
		String line;
		while ((line = dataReader.readLine()) != null)
		{
			//see if it has unknown values
			if(!info.hasUnknownValues && line.contains("?"))
			{
				info.hasUnknownValues = true;
			}
			//count number of records
			info.numberOfRecords++;
		}
		dataReader.close();
	}
	
	/**
	 * Reads the schema for a data set and evaluates it, storing the
	 * findings in the DataSetInfo object.
	 *
	 * @param info Will specify which data set schema to load and will be 
	 *	location to store evaluation.
	 * @throws Exception
	 */
	private static void processNamesFile(DataSetInfo info) throws Exception
	{
		//names file information
		String namesFileName = info.absolutePath + "/" + info.dataSetName + ".names";
		BufferedReader namesReader = new BufferedReader(new FileReader(namesFileName));
		boolean firstLine = true;
		String line;
		while ((line = namesReader.readLine()) != null)
		{
			String[] tokens = line.split(" ");
			
			if(firstLine)
			{
				//number of class labels
				info.numberOfClasses = tokens.length;
				firstLine = false;
				continue;
			}
			
			if(tokens.length == 0)
				continue;  //blank line
			
			if(tokens[0].equalsIgnoreCase("continuous"))
			{
				//count number of continuous attributes
				info.numberOfContinuous++;
			}
			else if(tokens[0].equalsIgnoreCase("discrete"))
			{
				//count number of nominal attributes
				info.numberOfNominal++;
			}
		}
		namesReader.close();
	}
	
	/**
	 * Command line tool for outputing evaluation information for data sets
	 * to a csv file.
	 *
	 * @param args first command line argument is the output file path which
	 * 	must include file name
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		DataSetInfo[] info = getInfoForAllDataSets();
		
		//print out results
		printInformation(info,args[0]);
	}
	
	/**
	 * Writes one line of csv file which will have all information for
	 * one data set.  
	 *
	 * @param info Information about data set to be written to line.
	 * @param outputFilePath Location to write one line of csv file.
	 * @throws Exception
	 */
	private static void printInformation(DataSetInfo[] info, String outputFilePath) throws Exception
	{
		//open output file
		FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
		PrintStream fileOut = new PrintStream(fileOutputStream);
		//print header
		fileOut.println("data set name,has unknown values,number of records,number of classes,number continuous features, number nominal features");
		//print one line for each DataSetInfo object
		for(int i=0; i < info.length; i++)
		{
			fileOut.print(info[i].dataSetName);
			fileOut.print(",");
			fileOut.print(info[i].hasUnknownValues);
			fileOut.print(",");
			fileOut.print(info[i].numberOfRecords);
			fileOut.print(",");
			fileOut.print(info[i].numberOfClasses);
			fileOut.print(",");
			fileOut.print(info[i].numberOfContinuous);
			fileOut.print(",");
			fileOut.print(info[i].numberOfNominal);
			fileOut.print("\n");
		}
		//close file
		fileOutputStream.close();
	}
}
