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
import java.util.*;


/**
 * Used to read labeled data from a file and create the appropriate Hemlock 
 * data structures such as a data set and a schema.
 *
 * @author Sean A. Gilpin
 */
public class DataImporter
{
	/** When a data set is first loaded it is stored here, so that
		subsequent attempts to load the data set are more efficient.*/
	public static Hashtable<String, DataSet> cachedDataSets;
	
	/**
	 * Load a data set either from the cache, or from the disk.
	 *
	 * @param info Contains information about the data set to be loaded.
	 */
	public DataSet importDataSet(DataSetInfo info) throws Exception
	{
		if(cachedDataSets == null)
			cachedDataSets = new Hashtable<String, DataSet>();
		
		if(info.fileFormat == FileFormatType.ModifiedC45)
		{
			String fileName = info.absolutePath + "/" + info.dataSetName;
			if(cachedDataSets.containsKey(fileName))
				return cachedDataSets.get(fileName);
			
			DataSet data =  importC45Modified(fileName + ".data", fileName + ".names", info );
			data.info = info;
			cachedDataSets.put(fileName, data);
			return data;
		}
		throw new Exception("unsupported file type");
	}
	
	
	/**
	 * Load a data set that is in C45 Modified format from the disk.
	 *
	 * @param dataFileName Path to file that contains instances and labels.
	 * @param namesFileName Path to file that contains schema for data.
	 * @return The DataSet that is imported from the disk.
	 * @throws Exception
	 */
	private DataSet importC45Modified(String dataFileName, String namesFileName, DataSetInfo info) throws Exception
	{
		RecordSchema schema = createSchemaC45Modified(namesFileName);
		ArrayList<double[]> records = createRecordSetC45(dataFileName, schema); 
		
		DataSet ds = new DataSet(schema, info, records);

		return ds;
	}
	
	/**
	 * Creates a list of instances to be included in the data set when
	 * the data files are in Modified C45 format.
	 *
	 * @param dataFileName Path to file that contains instances and labels.
	 * @param schema Contains schema information about format of instances.
	 * @return The list of instances read from disk.
	 * @throws Exception
	 */
	private ArrayList<double[]> createRecordSetC45(String dataFileName, RecordSchema schema) throws Exception
	{
		//Modified C45 and C45 are the same for reading data file
		//Each line in file is a record and values are separated by a space
		ArrayList<String> dataFile = readFile(dataFileName);
		ArrayList<double[]> recordSet = new ArrayList<double[]>();
		for(int i = 0; i < dataFile.size(); i++)
		{
			String line = dataFile.get(i);
			line = line.trim();
			if(line.equals(""))
				continue;
			String[] record = line.split(" ");
			double[] translated = schema.translateRecord(record);
			recordSet.add(translated);
		}
		return recordSet;
	}
	
	/**
	 * Reads a schema file in Modified C45 format and uses the information
	 * to create a {@link RecordSchema} object.
	 *
	 * @param namesFileName Path to file that contains schema.
	 * @return RecordSchema that is created.
	 * @return IOException
	 */
	private RecordSchema createSchemaC45Modified(String namesFileName) throws IOException
	{
		RecordSchema newSchema = new RecordSchema();
		ArrayList<String> namesFile = readFile(namesFileName);
		
		//First line will have class labels separated by spaces
		String labels = namesFile.get(0);
		newSchema.labels = labels.split(" ");
		
		//Remaining lines will each define an attribute
		ArrayList<String[]> attributes = new ArrayList<String[]>();
		for(int i = 1; i < namesFile.size(); i++)
		{
			String line = namesFile.get(i).trim();
			if(line.equals(""))
			{
				//blank line so skip
				continue;
			}
			attributes.add(line.split(" "));
		}
		newSchema.createAttributes(attributes);
		
		return newSchema;
	}
	
	/**
	 * Utility function to hide details of reading file.  Will add each
	 * line from file to an ordered list.
	 *
	 * @param fileName File to be read.
	 * @return A list of lines ordered as they were in the file
	 * @return IOException
	 */
	private ArrayList<String> readFile(String fileName) throws IOException
	{
		ArrayList<String> contents = new ArrayList<String>();
		
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String str;
		while ((str = in.readLine()) != null) 
		{
			contents.add(str);
		}
		in.close();

		
		return contents;
	}
}
