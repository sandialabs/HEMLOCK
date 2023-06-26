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
 
package gov.sandia.hemlock.evaluation;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

/**
 * Used to create a summary of results from many different experiments.  Will
 * look at all of the result files in a directory and compile the evaluations
 * so that they can easily be compared.
 *
 * @author Sean A. Gilpin
 */
public class AccuracyTable
{
	private Hashtable<String, Hashtable<String,String> > table;
	
	/**
	 * Default constructor.
	 *
	 */
	public AccuracyTable()
	{
		table = new Hashtable<String, Hashtable<String,String> >();
	}
	
	/**
	 * Reads all of the results in a directory, compiles the results, and
	 * stores the results in a hashtable
	 *
	 * @param folder The director to look for results files in.
	 * @throws Exception
	 */
	public void createTable(String folder) throws Exception
	{
		//Read every file in the folder
		File[] experiments = readFolder(folder);
		
		for(int i = 0; i < experiments.length; i++)
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(experiments[i]);
			Element root = (Element)doc.getElementsByTagName("Experiment").item(0);
			NodeList dataSets = root.getElementsByTagName("KFoldCrossValidation");
			
			//loop through "KFoldCrossValidation"s (datasets)
			for(int j = 0; j < dataSets.getLength(); j++)
			{
				//open summary
				Element set = (Element)dataSets.item(j);
				Element summary = (Element)set.getElementsByTagName("Summary").item(0);
				
				//read accuracy
				Element accuracy = (Element)summary.getElementsByTagName("averageAccuracy").item(0);
				CharacterData accData = (CharacterData)accuracy.getFirstChild();
				String strAcc = accData.getData();
				
				//get name of experiment
				Element experimentName = 
					(Element)summary.getElementsByTagName("experimentName").item(0);
				CharacterData expNameData = (CharacterData)experimentName.getFirstChild();
				String strExpName = expNameData.getData();
				
				//get name of data set
				Element dataSetName = (Element)summary.getElementsByTagName("dataSetName").item(0);
				CharacterData dataNameData = (CharacterData)dataSetName.getFirstChild();
				String strDataName = dataNameData.getData();
				
				addRecord(strExpName, strDataName, strAcc);
			}	
		}
	}

	/**
	 * Prepar to write the compiled results stored in the hashtable to a 
	 * file in csv format by first creating the contents that will be
	 * written to the file.
	 *
	 * @return The contents that should be written to a file.
	 */
	public String writeTableAsCSV()
	{
		StringBuilder csv = new StringBuilder();
		
		//print column headers
		List<String> experimentNames = getExperimentNames();
		csv.append("data set,");
		for(String experiment: experimentNames)
		{
			csv.append(experiment);
			csv.append(",");
		}
		csv.deleteCharAt(csv.length() - 1);
		csv.append("\n");
		
		//get list of all data sets
		List<String> keys = Collections.list(table.keys());
		Collections.sort(keys);
		
		for(String key : keys)
		{
			//print row header
			csv.append(key);
			csv.append(",");
			
			Hashtable<String, String> row = table.get(key);
			for(String experiment: experimentNames)
			{
				if(row.containsKey(experiment))
					csv.append(row.get(experiment));
				csv.append(",");
			}
			csv.deleteCharAt(csv.length() - 1);
			csv.append("\n");
		}
		
		return csv.toString();
	}
	
	/**
	 * Gets the names of all of the experiments that are in the 
	 * hashtable.
	 *
	 * @return A list of all experiment names.
	 */
	private List<String> getExperimentNames()
	{
		List<String> names = new ArrayList<String>();
		List<String> keys = Collections.list(table.keys());
		for(String key : keys)
		{
			List<String> experimentNames = Collections.list(table.get(key).keys());
			for(String expName : experimentNames)
			{
				if(!names.contains(expName))
					names.add(expName);
			}
		}
		Collections.sort(names);
		return names;
	}
	
	/**
	 * Adds a record to the hashtable.  The key for the hashtable will be
	 * the name of the data set. The value will be another hashtable that
	 * cotains experiment name and accuracy as the keys and values.
	 *
	 * @param experiment The name of the experiment.
	 * @param dataSet The name of the dataset.
	 * @param accuracy The accuracy that the experiment achieved.
	 */
	private void addRecord(String experiment, String dataSet, String accuracy)
	{
		Hashtable<String,String> row;
		if(table.containsKey(dataSet))
		{
			row = table.get(dataSet);
		}
		else
		{
			row = new Hashtable<String, String>();
			table.put(dataSet, row);
		}
		
		row.put(experiment, accuracy);
	}
	
	/**
	 * Gets a list of all the files in a directory.
	 *
	 * @param folder The name of the directory to find files in
	 * @return The list of the files in the directory.
	 */
	private File[] readFolder(String folder)
	{
		//get all normal visible files in directory
		File fFolder = new File(folder);
		return fFolder.listFiles(new FileFilter() {
	        public boolean accept(File file) {
	            return file.isFile() && !file.isHidden();
	        }
		});
	}

}
