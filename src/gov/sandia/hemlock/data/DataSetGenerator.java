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
 * Generates a data set.  Must specify the number of classes, the number of 
 * continuous versus nominal features, the number of instances to create, and 
 * the means and std for each class feature combination.  When setting the mean
 * and std arrays, the first index represents the class and the second index 
 * represents the feature.  All of the continuous features come after the 
 * nominal features.
 *
 * @author Sean A. Gilpin
 */
public class DataSetGenerator
{
	
	public int numClasses;
	public int numNominalFeatures;
	public int numContinuousFeatures;
	public int numInstances;
	
	public int[] nominalFeaturesNumValues;
	
	public double[][] mean;
	public double[][] std;
	
	public String dataSetName;
	public String repositoryPath;
	
	/**
	 * Create a data set using the parameters that are set by initilizing
	 * the class member variables.
	 *
	 * @return The DataSet that is generated.
	 */
	public DataSet generateDataSet()
	{
		//Create records
		Random r = new Random();
		int numFeatures = numContinuousFeatures + numNominalFeatures;
		double[][] records = new double[numInstances][numFeatures + 1];
		//Assign a class to each record.
		for(int i=0; i < numInstances; i++)
		{
			records[i][numFeatures] = r.nextInt(numClasses);
		}
		
		for(int i=0; i < numInstances; i++)
		{
			int classIndex = (int)records[i][numFeatures];
			for(int j=0; j < numFeatures; j++)
			{
				double value = r.nextGaussian()*std[classIndex][j] + mean[classIndex][j];
				records[i][j] = value;
				if(j < numNominalFeatures)
				{
					if(records[i][j] < 0)
						records[i][j] = 0;
					if(records[i][j] > nominalFeaturesNumValues[j]-1)
						records[i][j] = nominalFeaturesNumValues[j]-1;
					records[i][j] = Math.round(records[i][j]);
				}
			}
		}
		ArrayList<double[]> alRecords = new ArrayList<double[]>();
		alRecords.addAll(Arrays.asList(records));
		
		//Create schema and meta information
		RecordSchema schema = createSchema();		
		DataSetInfo info = createDataSetInfo();
		
		return new DataSet(schema, info, alRecords);
	}
	
	/**
	 * Record the information about the data set being generated.  This
	 * information is obtained from the member variables.
	 * 
	 * @return The DataSetInfo that has all information about this data set.
	 */
	public DataSetInfo createDataSetInfo()
	{
		DataSetInfo info = new DataSetInfo();
		info.absolutePath = this.repositoryPath + "/" + this.dataSetName;
		info.dataSetName = this.dataSetName;
		info.fileFormat = FileFormatType.ModifiedC45;
		info.hasUnknownValues = false;
		info.numberOfClasses = this.numClasses;
		info.numberOfClasses = this.numContinuousFeatures;
		info.numberOfNominal = this.numNominalFeatures;
		info.numberOfRecords = this.numInstances;
		
		return info;
	}
	
	
	/**
	 * Creates the schema information for the data set being generated.
	 *
	 * @return The RecordSchema that is created for the data set created.
	 */
	public RecordSchema createSchema()
	{
		//create attribute types and attribute values
		int numFeatures = numContinuousFeatures + numNominalFeatures;
		AttributeType[] aTypes = new AttributeType[numFeatures];
		String[][] aValues = new String[aTypes.length][];
		for(int i=0; i < aTypes.length; i++)
		{
			aTypes[i] = (i < numNominalFeatures) ? AttributeType.Discrete: AttributeType.Continuous;
			if(i < numNominalFeatures)
			{
				String[] possibleValues = new String[nominalFeaturesNumValues[i]];
				for(int j = 0; j < nominalFeaturesNumValues[i]; j++)
					possibleValues[j] = Integer.toString(j);
				aValues[i] = possibleValues;
			}
		}
		
		//create class information
		String[] labels = new String[numClasses];
		for(int i=0; i < numClasses; i++)
		{
			labels[i] = Integer.toString(i);
		}
			
		RecordSchema schema = new RecordSchema();
		schema.attributeTypes = aTypes;
		schema.attributeValues = aValues;
		schema.labels = labels;
		schema.numAttributes = numFeatures;
		
		return schema;
	}
	
	/**
	 * When the data is generated using the command line tool, this method
	 * will be used to parse the command line arguments. 
	 *
	 * @param args The command line arguments
	 * @return The path where data sets will be written to.
	 */
	public static String parseArgs(String[] args)
	{
		if(args.length!=1)
		{
			System.out.println("The first argument must be "
				+ "directory path where datasets will be "
				+ "written to.");
			System.exit(1);
		}
		
		return args[0];
	}
	
	/**
	 * Method to allow command line usage of data generating tools.  Will
	 * generate three data sets that are already setup to be created and
	 * write the data sets to the path supplied in the command line
	 * arguments.
	 *
	 * @param args The command line arguments which will contain a path.
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		String path = args[0];
		
		DataSetGenerator dsg1 = new DataSetGenerator();
		dsg1.dataSetName = "continuous_2";
		dsg1.repositoryPath = path;
		dsg1.numClasses = 2;
		dsg1.numContinuousFeatures = 10;
		dsg1.numNominalFeatures = 0;
		dsg1.numInstances = 50;
		dsg1.mean = new double[2][10];
		dsg1.mean[0] = new double[]{0,1,2,3,4,5,6,7,8,9};
		dsg1.mean[1] = new double[]{2,3,4,5,6,7,8,9,10,11};
		dsg1.std = new double[2][10];
		dsg1.std[0] = new double[]{2,2,2,2,2,2,2,2,2,2};
		dsg1.std[1] = new double[]{2,2,2,2,2,2,2,2,2,2};
		dsg1.nominalFeaturesNumValues = new int[dsg1.numContinuousFeatures + dsg1.numNominalFeatures];
		
		DataSet ds1 = dsg1.generateDataSet();
		ds1.serialize();
		
		DataSetGenerator dsg2 = new DataSetGenerator();
		dsg2.dataSetName = "discrete_2";
		dsg2.repositoryPath = path;
		dsg2.numClasses = 2;
		dsg2.numContinuousFeatures = 0;
		dsg2.numNominalFeatures = 10;
		dsg2.numInstances = 50;
		dsg2.mean = new double[2][10];
		dsg2.mean[0] = new double[]{0,1,2,3,4,5,6,7,8,9};
		dsg2.mean[1] = new double[]{2,3,4,5,6,7,8,9,10,11};
		dsg2.std = new double[2][10];
		dsg2.std[0] = new double[]{2,2,2,2,2,2,2,2,2,2};
		dsg2.std[1] = new double[]{2,2,2,2,2,2,2,2,2,2};
		dsg2.nominalFeaturesNumValues = new int[]{10,10,10,10,10,10,10,10,10,10};
		
		DataSet ds2 = dsg2.generateDataSet();
		ds2.serialize();
		
		DataSetGenerator dsg3 = new DataSetGenerator();
		dsg3.dataSetName = "mixed_2";
		dsg3.repositoryPath = path;
		dsg3.numClasses = 2;
		dsg3.numContinuousFeatures = 5;
		dsg3.numNominalFeatures = 5;
		dsg3.numInstances = 50;
		dsg3.mean = new double[2][10];
		dsg3.mean[0] = new double[]{2,3,4,5,6,0,1,7,8,9};
		dsg3.mean[1] = new double[]{3,4,5,6,7,1,2,8,9,10};
		dsg3.std = new double[2][10];
		dsg3.std[0] = new double[]{2,2,2,2,2,2,2,2,2,2};
		dsg3.std[1] = new double[]{2,2,2,2,2,2,2,2,2,2};
		dsg3.nominalFeaturesNumValues = new int[]{10,10,10,10,10,0,0,0,0,0};
		
		DataSet ds3 = dsg3.generateDataSet();
		ds3.serialize();
		
		//Can be used to build 3 class data set
		//Will break tests that check known correct output
		/*
		DataSetGenerator dsg4 = new DataSetGenerator();
		dsg4.dataSetName = "mixed_3";
		dsg4.repositoryPath = path;
		dsg4.numClasses = 3;
		dsg4.numContinuousFeatures = 5;
		dsg4.numNominalFeatures = 5;
		dsg4.numInstances = 100;
		dsg4.mean = new double[3][10];
		dsg4.mean[0] = new double[]{2,3,4,5,6,0,1,7,8,9};
		dsg4.mean[1] = new double[]{3,4,5,6,7,1,2,8,9,10};
		dsg4.mean[2] = new double[]{3,3,5,5,7,0,2,6,9,8};
		dsg4.std = new double[3][10];
		dsg4.std[0] = new double[]{2,2,3,2,2,2,2,2,2,2};
		dsg4.std[1] = new double[]{2,2,2,2,2,2,2,2,3,2};
		dsg4.std[2] = new double[]{2,2,2,2,2,3,2,2,2,2};
		dsg4.nominalFeaturesNumValues = new int[]{10,10,10,10,10,0,0,0,0,0};
		
		DataSet ds4 = dsg4.generateDataSet();
		ds4.serialize();
		*/
		
		
	}
	
}
