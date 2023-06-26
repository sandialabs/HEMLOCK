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

import gov.sandia.hemlock.data.ClassifiedDataSet;
import java.lang.StringBuilder;

/**
 * Used to create a confusion matrix from a ClassifiedDataSet.  If d is the the
 * number of classes then the confusion matrix will be d by d.  Also contains
 * methods to calculate measures that can be derived from a confusion matrix.
 *
 * @author Sean A. Gilpin
 */
public class ConfusionMatrix
{
	/** The confusion matrix that will be produced */
	public final int[][] matrix;
	/** The total number of instances used to make confusion matrix*/
	public final int total;
	
	
	/**
	 * Constructor which will create the confusion matrix given a 
	 * ClassifiedDataSet which contains predictions along with true class
	 * labels
	 *
	 * @param data ClassifiedDataSet which contains predictions and true
	 *	class labels.
	 */
	public ConfusionMatrix(ClassifiedDataSet data)
	{
		total = data.records.size();
		int[][] matrix = initMatrix(data.recordSchema.labels.length);
		for(int i = 0; i < total; i++)
		{
			int correctLabel = (int)data.records.get(i)[data.recordSchema.numAttributes];
			int predictedLabel = (int)data.predictedLabels[i];
			matrix[correctLabel][predictedLabel]++;
		}
		this.matrix = matrix;
	}
	
	/**
	 * Initialize the confusion matrix arrays and set values to all zeroes
	 *
	 * @param size The dimension of the confusion matrix to be created.
	 * @return The array which will be used to store confusion matrix.
	 */
	private int[][] initMatrix(int size)
	{
		//instantiate array and initialize values to all zero
		int[][] matrix = new int[size][size];
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				matrix[i][j] = 0;
			}
		}
		return matrix;
	}
	
	/**
	 * Use the confusion matrix to calculate the accuracy of the
	 * prediction model.  This corresponds to the sum of the diagonal
	 * entries over the total number of predictions.
	 *
	 * @return The accuracy of the predictions.
	 */
	public double calculateAccuracy()
	{
		int totalCorrect = 0;
		for(int i=0; i < matrix.length; i++)
		{
			totalCorrect += matrix[i][i];
		}
		
		return (double)totalCorrect / total;
	}
	
	/**
	 * Creates a string representation of the matrix so that it can be
	 * output to experiment result files.
	 *
	 * @return The string representation of the file.
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				sb.append(matrix[i][j]);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);  //remove the last comma
			sb.append(";");
		}
		sb.deleteCharAt(sb.length()-1);  //remove the last semicolon
		sb.append("]");
		
		return sb.toString();
	}
}
