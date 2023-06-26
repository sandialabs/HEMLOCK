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
 
package gov.sandia.hemlock.cognitiveFoundryInterface;

import gov.sandia.cognition.learning.data.InputOutputPair;
import gov.sandia.cognition.math.matrix.mtj.DenseVector;
import gov.sandia.cognition.math.matrix.mtj.DenseVectorFactoryMTJ;
import gov.sandia.hemlock.data.DataSet;
import gov.sandia.hemlock.data.RecordSchema;
import gov.sandia.hemlock.data.IDataSet;



import java.util.*;

/*
 * Data types used by Cognitive Framework to represent instances, and algorithms to convert from the 
 * Hemlock data types.
 */
public class CognitiveDataSet implements IDataSet
{
	public ArrayList<InputOutputPair<DenseVector, String>> instances;
	public RecordSchema recordSchema;

	public CognitiveDataSet(IDataSet dataSet)
	{
		DataSet data = (DataSet)dataSet;
		instances = createInstances(data);
		recordSchema = data.recordSchema;
	}
	
	private ArrayList<InputOutputPair<DenseVector, String>> createInstances(DataSet data)
	{
		
		ArrayList<InputOutputPair<DenseVector, String>> instances = new ArrayList<InputOutputPair<DenseVector, String>>();
		//loop through records
		for(int i=0; i < data.records.size(); i++)
		{
			double[] record = data.records.get(i);
			double labelValue = record[0];
			String output = data.recordSchema.labels[(int)labelValue];
			
			//remove the class label 
			DenseVector input = DenseVectorFactoryMTJ.INSTANCE.copyArray(Arrays.copyOf(record, record.length-1));
			instances.add(new InputOutputPair<DenseVector, String>(input, output));
		}
		return instances;
	}
}
