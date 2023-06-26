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

import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.data.*;
import Jama.*;

/**
 * Contains methods for measuring the diversity in a set of base classifiers.
 * Definitions for the contained diversity measures came from the following
 * reference:
 * 
 * Kuncheva, L. I. 
 * Combining Pattern Classifiers: Methods and Algorithms 
 * Wiley-Interscience, 2004
 *
 * @author Sean A. Gilpin
 */
public class Diversity
{
	
	/**
	 * Calculates the average pairwise disagreement.  The predictions for
	 * every combination of two base classifiers are compared and the number
	 * of times they disagree about the target class variable is counted.
	 * The average is found by dividing the number of disagreements by the
	 * total number of comparisons.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double disagreement(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;
		
		//TODO: Assert that L >= 2
		//Do pairwise comparison of all models predictions
		long totalDisagree = 0;
		for(int i=0; i < L - 1; i++)
		{
			for(int j=i+1; j < L; j++)
			{
				long disagree = 0;
				for(int k=0; k<N; k++)
				{
					if(cds[i].predictedLabels[k] != cds[j].predictedLabels[k])
					{
						disagree++;
					}
				}
				totalDisagree += disagree;
			}
		}
		//take average over all pairs and all instances.
		long total_comparisons = N*(((L)*(L-1))/2);
		return ((double)totalDisagree)/total_comparisons;
	}
	
	/**
	 * Calculates the average pairwise Yule Q statistic.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double yule_q(ClassifiedDataSet[] cds) throws Exception
	{
		
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;
		
		int classIndex = cds[0].recordSchema.numAttributes;

		double totalCor = 0;
		for(int i=0; i < L - 1; i++)
		{
			for(int j=i+1; j < L; j++)
			{
				double a,b,c,d; //confusion matrix values
				a=b=c=d=0;
				for(int k=0; k < N; k++)
				{
					if(cds[i].predictedLabels[k] == cds[j].predictedLabels[k])
					{
						if(cds[i].predictedLabels[k] == cds[i].records.get(k)[classIndex])
							a++; //They are both correct
						else
							d++; //They are both incorrect.
					}
					else
					{
						if(cds[i].predictedLabels[k] == cds[i].records.get(k)[classIndex])
							b++; //classifier i right, j wrong
						else
							c++; //classifier j wrong, i right
					}
				}
				//normalize confusion matrix values, so they become probabilities
				a = a / N; b = b / N;
				c = c / N; d = d / N;
				
				if(a*d+b*c == 0)
					totalCor += 1;
				else
					totalCor += (a*d-b*c)/(a*d+b*c);
			}
		}
		
		//Average results over all pairs
		return totalCor / (double)((L*(L-1))/2.0);
	}
	
	/**
	 * Calculates the average pairwise correlation.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double correlation(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;
		
		int classIndex = cds[0].recordSchema.numAttributes;
		
		//matrix of base classifier oracle values for all instances
		double[][] x = new double[N][L];
		for(int i=0; i<L; i++)
		{
			double count = 0;
			for(int j=0; j<N; j++)
			{
				if(cds[i].predictedLabels[j] == cds[i].records.get(j)[classIndex])
				{
					x[j][i] = 1;
					count++;
				}
				else
				{
					x[j][i] = 0; 
				}
			}
			for(int j=0; j<N; j++) //Center data such that mean is zero
				x[j][i] =- count/N;
		}
		Matrix m_x = new Matrix(x);
		Matrix m_covariance = m_x.transpose().times(m_x);
		
		double total_corr = 0;
		for(int i=0; i<L-1; i++)
		{
			for(int j=i+1; j<L; j++)
			{
				total_corr += m_covariance.get(i, j)/
					Math.sqrt(m_covariance.get(i,i)*m_covariance.get(j,j));				
			}
		}
		
		return total_corr/((L*(L-1))/2);
	}
	
	/**
	 * Calculates the average pairwise double fault proportion.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double double_fault(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;
		
		int classIndex = cds[0].recordSchema.numAttributes;

		double totalD = 0;
		for(int i=0; i < L - 1; i++)
		{
			for(int j=i+1; j < L; j++)
			{
				for(int k=0; k<N; k++)
				{
					if(cds[i].predictedLabels[k] == cds[j].predictedLabels[k])
					{
						if(cds[i].predictedLabels[k] != cds[i].records.get(k)[classIndex])
							totalD++; //They are both incorrect						
					}

				}
			}
		}
		
		//Average results over all pairs and all instances
		return totalD / (double)(N*(L*(L-1))/2);
	}
	
	/**
	 * Calculates the entropy diversity measurement which is a
	 * non-pairwise diversity measurement.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double entropy(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;

		int classIndex = cds[0].recordSchema.numAttributes;
		double totalE = 0;
		
		for(int i=0; i<N; i++)
		{
			double trueClass = cds[0].records.get(i)[classIndex];
			double e = 0;
			for(int j=0; j < L; j++)
			{
				if(cds[j].predictedLabels[i] == trueClass)
				{
					e++;
				}
			}
			e = Math.min(e, L - e);
			totalE += e;
		}
		
		return totalE / (double)((N*(L-1))/2);
	}
	
	/**
	 * Calculates the general diversity (GD) measurement which is a
	 * non-pairwise diversity measurement.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double general_diversity(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;

		int classIndex = cds[0].recordSchema.numAttributes;
		
		//Calculate number of times classifier fails for each instance.
		int[] fail_count = new int[N];
		for(int i=0; i<N; i++)
			fail_count[i]=0;
		for(int i=0; i < L; i++)
		{
			for(int j=0; j< N; j++)
			{
				//If this is true then the prediction doesn't match the true class
				if(cds[i].records.get(j)[classIndex] != cds[i].predictedLabels[j])
					fail_count[j]++;
			}
		}
		
		//Calculate probability that random instance mispredicted i times
		double[] pi = new double[L + 1];
		for(int i=0; i<N; i++)
		{
			pi[fail_count[i]]++;
		}
		for(int i=0; i<(L+1); i++)
			pi[i] = pi[i]/N; //normalize to create probabilities.
		
		//Now calculate GD 
		double p_1 = 0;
		double p_2 = 0;
		for(int i = 1; i<=L; i++)
		{
			p_1 += (i / (float)L)*pi[i];
			p_2 += (i / (float)L)*((i-1) / (float)(L-1))*pi[i];
		}
		
		return 1 - (p_2/p_1);
	}
	
	/**
	 * Calculates the coincident failure diversity (CFD) measurement which is a
	 * non-pairwise diversity measurement.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double coincident_failure(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;

		int classIndex = cds[0].recordSchema.numAttributes;
		
		//Calculate number of times classifier fails for each instance.
		int[] fail_count = new int[N];
		for(int i=0; i<N; i++)
			fail_count[i]=0;
		for(int i=0; i < L; i++)
		{
			for(int j=0; j< N; j++)
			{
				if(cds[i].records.get(j)[classIndex] != cds[i].predictedLabels[j])
					fail_count[j]++;
			}
		}
		
		//Calculate probability that random instance mispredicted i times
		double[] pi = new double[L + 1];
		for(int i=0; i<N; i++)
		{
			pi[fail_count[i]]++;
		}
		for(int i=0; i<(L+1); i++)
			pi[i] = pi[i]/N;
		
		if(pi[0] == 1)	//All classifiers are always right
			return 0;
		else
		{
			double sum = 0;
			for(int i = 1; i<=L; i++)
			{
				sum += ((float)(L - i) / (float)(L -1))*pi[i];
			}
			return sum / (1.0 - pi[0]);
		}	
	}
	
	/**
	 * Calculates the measure of difficulty diversity measurement which is a
	 * non-pairwise diversity measurement.
	 * 
	 * @param ClassifiedDataSet An array of predictions made by each base
	 * 	classifier, along with the known true labels.
	 * @throws Exception.
	 */
	public static double difficulty(ClassifiedDataSet[] cds) throws Exception
	{
		//Number of instances 
		int N = cds[0].predictedLabels.length;
		//Number of base classifiers
		int L = cds.length;

		int classIndex = cds[0].recordSchema.numAttributes;
		
		//Calculate number of times classifier is correct for each instance.
		int[] correct_count = new int[N];
		for(int i=0; i<N; i++)
			correct_count[i]=0;
		for(int i=0; i < L; i++)
		{
			for(int j=0; j< N; j++)
			{
				if(cds[i].records.get(j)[classIndex] == cds[i].predictedLabels[j])
					correct_count[j]++;
			}
		}
		
		//Calculate probability that random instance correctly predicted i times
		double[] pi = new double[L + 1];
		for(int i=0; i<N; i++)
		{
			pi[correct_count[i]]++;
		}
		for(int i=0; i<(L+1); i++)
			pi[i] = pi[i]/N;
		
		//Get mean
		double mean = 0;
		for(int i=0; i<(L+1); i++)
			mean += (i/(double)L)*pi[i];
		
		//calculate sample variance of random variable whose value is
		//the proportion of base classifiers that correctly predict
		//the label of a randomly selected instance.
		double theta = 0;
		for(int i=0; i<N; i++)
			theta += Math.pow(correct_count[i]/(double)L - mean, 2)*pi[correct_count[i]];

		return theta;
	}
	
}

