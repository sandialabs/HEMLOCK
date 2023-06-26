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
import gov.sandia.hemlock.experiment.Experiment;
import gov.sandia.hemlock.data.*;

/**
 * Data structure to record measures related to the performance of a 
 * classification model.
 *
 * @author Sean A. Gilpin
 */
public class ModelEvaluationResults
{
	/** Stores the confusion matrix*/
	public ConfusionMatrix confusionMatrix;
	/** Stores the accuracy*/
	public double accuracy;
	
	/** Stores information about the data set the evaluation were made for*/
	public DataSetInfo dataSetInfo;
	/** The type of model that made the predictions that were evaluated*/
	public ModelType modelType;
	
	//ROC Graph Curve Points
	/** ROC plot x-axis coordinate*/
	public double[] rocXCoordinates;
	/** ROC plot y-axis coordinates*/
	public double[] rocYCoordinates;
	/** Area under curve of ROC plot*/
	public double auc;
	/** The positive class for the ROC evaluations */
	public int positiveClass;
	
	//Diversity
	/** Stores diversity measurements */
	public double diversity_disagreement;
	public double diversity_correlation;
	public double diversity_yuleQ;
	public double diversity_doubleFault;
	public double diversity_entropy;
	public double diversity_generalDiversity;
	public double diversity_coincidentFailure;
	public double diversity_difficulty;
	
	/** Indicators for which measurements have been calculated*/
	public boolean calculated_disagreement = false;
	public boolean calculated_correlation = false;
	public boolean calculated_ROCCurve = false;
	public boolean calculated_yuleQ = false;
	public boolean calculated_doubleFault = false;
	public boolean calculated_entropy = false;
	public boolean calculated_generalDiversity = false;
	public boolean calculated_coincidentFailure = false;
	public boolean calculated_difficulty = false;
	
	/**
	 * Evaluates the model and stores the evaluation measurement in this
	 * classes member variables.
	 *
	 * @param dataSet Contains predictions and true labels for all instances
	 *	to be used for evaluation.
	 * @param model The model which made the predictions
	 * @param experiment The experiment which these results are for.
	 * @throws Exception
	 */
	public ModelEvaluationResults(ClassifiedDataSet dataSet, Model model, Experiment experiment) throws Exception
	{
		confusionMatrix = new ConfusionMatrix(dataSet);
		accuracy = confusionMatrix.calculateAccuracy();
		dataSetInfo = dataSet.info;
		this.modelType = model.getModelType();
		if(experiment.computeROCMetrics)
		{
			//calculate roc graph points
			this.calculated_ROCCurve = true;
			this.positiveClass = experiment.rocPositiveClass;
			ROCGraph roc = new ROCGraph(dataSet, positiveClass);
			double[][] rocCoords = roc.calculateROCPoints();
			this.rocXCoordinates = rocCoords[0];
			this.rocYCoordinates = rocCoords[1];
			this.auc = roc.calculateAUC();
		}
		if(experiment.modelParameters.isEnsemble())
		{
			if(experiment.computeDiversity_disagreement ||	experiment.computeDiversity_correlation  ||
					experiment.computeDiversity_yuleQ  ||	experiment.computeDiversity_doubleFault || 
					experiment.computeDiversity_entropy  || experiment.computeDiversity_generalDiversity || 
					experiment.computeDiversity_coincidentFailure  || experiment.computeDiversity_difficulty)
			{
				//Precalculate predictions of all base classifier models
				EnsembleModel eModel = (EnsembleModel)model;
				DataSet labeled = new DataSet(dataSet.recordSchema, dataSet.info, dataSet.records);
				Model[] baseModels = eModel.getBaseClassifierModels();
				//Get classified data sets for each model
				ClassifiedDataSet[] cds = new ClassifiedDataSet[baseModels.length];
				for(int i = 0; i < baseModels.length; i++)
				{
					cds[i] = new ClassifiedDataSet(labeled, baseModels[i]);
				}
				
				//Now calculate diversity measures
				if(experiment.computeDiversity_disagreement)
				{
					this.diversity_disagreement = Diversity.disagreement(cds);
					calculated_disagreement = true;
				}
				if(experiment.computeDiversity_correlation)
				{
					this.diversity_correlation = Diversity.correlation(cds);
					calculated_correlation = true;
				}
				if(experiment.computeDiversity_yuleQ)
				{
					this.diversity_yuleQ = Diversity.yule_q(cds);
					calculated_yuleQ = true;
				}
				if(experiment.computeDiversity_doubleFault)
				{
					this.diversity_doubleFault = Diversity.double_fault(cds);
					calculated_doubleFault = true;
				}
				if(experiment.computeDiversity_entropy)
				{
					this.diversity_entropy = Diversity.entropy(cds);
					calculated_entropy = true;
				}
				if(experiment.computeDiversity_generalDiversity)
				{
					this.diversity_generalDiversity = Diversity.general_diversity(cds);
					calculated_generalDiversity = true;
				}
				if(experiment.computeDiversity_coincidentFailure)
				{
					this.diversity_coincidentFailure = Diversity.coincident_failure(cds);
					calculated_coincidentFailure = true;
				}
				if(experiment.computeDiversity_difficulty)
				{
					this.diversity_difficulty = Diversity.difficulty(cds);	
					calculated_difficulty = true;
				}
			}
		}	
	}
}
