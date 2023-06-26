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

import gov.sandia.hemlock.data.*;
import java.util.*;

/**
 * Algorithms to calculate a Receiver Operator Curve (ROC) graph and the 
 * Area Under the Curve (AUC) measurement for a given model.
 *
 * @author Sean A. Gilpin 
 */
public class ROCGraph
{
	
	private DataPair[] twoClassData;
	private int totalP;
	private int totalN;
	private int total;
	
	/**
	 *_Prepares the predictions for ROC analysis by reducing the problem
	 * to a two class problem and sorting the predictions. 
	 *
	 * @param data The predictions along with the true class labels
	 * @param positiveClass The class to be used in one against all
	 *	comparison
	 */
	public ROCGraph(ClassifiedDataSet data, int positiveClass)
	{
		//Reduce into two class problem
		this.twoClassData = reduceDataSet(data, positiveClass);
	
		//Sort and store data based on the value of positive class in distribution
		sortDataSet(twoClassData);	
		
		//Count total P and total N
		this.totalP = countPositives(twoClassData); 
		this.total = twoClassData.length;
		this.totalN = total-totalP;
	}
	
	/**
	 * Calculates the Area Under the Curve (AUC) model evaluation
	 * measurement.
	 *
	 * @return The AUC measurement value.
	 */
	public double calculateAUC()
	{
		double FP = 0;
		double TP = 0;
		double FPPrev = 0;
		double TPPrev = 0;
		double fPrev = Double.NEGATIVE_INFINITY;
		double area = 0;
		
		//totalP and totalN must be greater than 0, otherwise use these values
		if(totalP==0)
		{
			return 0;
		}
		if(totalN==0)
		{
			return 1;
		}
		
		for(int i = 0; i < total; i++)
		{
			if(twoClassData[i].probability != fPrev)
			{
				double h = FP - FPPrev;
				double trapArea = (TP + TPPrev)*h/2;
				area += trapArea;
				
				fPrev = twoClassData[i].probability;
				FPPrev = FP;
				TPPrev = TP;
			}
			
			if(twoClassData[i].membership)
			{
				TP++;
			}
			else
			{
				FP++;
			}
		}
		//push what should be (1,1) onto points
		double h = FP - FPPrev;
		double trapArea = (TP + TPPrev)*h/2;
		area += trapArea;
		area = area / (totalP*totalN);
		
		return area;
	}
	
	/**
	 * Calculate the ROC plot, which shows the interaction between
	 * specificity and sensitivity in the predictions made by a model.
	 *
	 * @return Will return a 2xn array where n is the number of predictions.
	 *	The first row will correspond to the x-axis components of the
	 *	plotted points.
	 */
	public double[][] calculateROCPoints()
	{
		double[] xCoords = new double[total];  //enough room for maximum number of poitns
		double[] yCoords = new double[total];
		double FP = 0;
		double TP = 0;
		double fPrev = Double.NEGATIVE_INFINITY;
		int numPoints = 0;
		//totalP and totalN must be greater than 0, otherwise use these curves
		if(totalP==0)
		{
			double[][] points = new double[2][];
			points[0] = new double[]{0.0,1.0,1.0};
			points[1] = new double[]{0.0,0.0,1.0};
			return points;
		}
		if(totalN==0)
		{
			double[][] points = new double[2][];
			points[0] = new double[]{0.0,0.0,1.0};
			points[1] = new double[]{0.0,1.0,1.0};
			return points;
		}
		
		for(int i = 0; i < total; i++)
		{
			if(twoClassData[i].probability != fPrev)
			{
				xCoords[numPoints] = FP/totalN;
				yCoords[numPoints] = TP/totalP;
				numPoints++;
				fPrev = twoClassData[i].probability;
			}
			
			if(twoClassData[i].membership)
			{
				TP++;
			}
			else
			{
				FP++;
			}
		}
		//push what should be (1,1) onto points
		xCoords[numPoints] = FP/totalN;
		yCoords[numPoints] = TP/totalP;
		numPoints++;
		
		//reduce size of arrays to just fit the calculated points
		double[][] points = new double[2][];
		points[0] = Arrays.copyOf(xCoords, numPoints);
		points[1] = Arrays.copyOf(yCoords, numPoints);
		return points;
	}

	/**
	 * Count the number of instances that correspond to the chosen
	 * positive class.
	 *
	 * @param data A list of instances.
	 * @return The number of instances in the positive class.
	 */
	private int countPositives(DataPair[] data)
	{
		int count = 0;
		for(int i = 0; i < data.length; i++)
		{
			if(data[i].membership)
				count++;
		}
		return count;
	}
	
	/**
	 * Sort the instances based on the prediction probability that they
	 * belong to the postivie class.  Higher probabilities will come
	 * first.
	 *
	 * @param A list of instances combined with their predicted probability
	 * 	of belonging to the positive class.
	 */
	private void sortDataSet(DataPair[] data)
	{
		DataPairComparator comparator = new DataPairComparator();
		Arrays.sort(data, comparator);
	}
	
	/**
	 * Creates the DataPair array which helps in dealing with the instances
	 * as if they were a two class data set.
	 *
	 * @param data Instances with predictions and true labels
	 * @param positiveClass The class label that will be compared against
	 *	the others in an all against one manner.
	 * @return A list of instances transformed to two class problem.
	 */
	private DataPair[] reduceDataSet(ClassifiedDataSet data, int positiveClass)
	{
		int classIndex = data.recordSchema.numAttributes;
		int numRecords = data.records.size();
		
		DataPair[] tcData = new DataPair[data.records.size()];
		for(int i = 0; i < numRecords; i++)
		{
			boolean membership = (data.records.get(i)[classIndex] == positiveClass);
			double probability = data.predictedDistributions[i][positiveClass];
			tcData[i] = new DataPair(membership, probability);
		}
		return tcData;
	}
	
	/**
	 * Used to represent an instance with prediction as a two class
	 * instance and prediction.
	 */
	private class DataPair
	{
		public boolean membership;
		public double probability;
		
		public DataPair(boolean membership, double probability)
		{
			this.membership = membership;
			this.probability = probability;
		}
	}
	
	/**
	 * Used to be able to sort the instances in their two class format.
	 */
	private class DataPairComparator implements Comparator<DataPair>
	{
		public int compare(DataPair dp1, DataPair dp2)
		{
			//sort in descending order
			//default sort is ascending order, so swithc
			//the parameters
			return Double.compare(dp2.probability, dp1.probability);
		}
	}
}
