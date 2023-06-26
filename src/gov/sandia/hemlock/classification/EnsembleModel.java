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

package gov.sandia.hemlock.classification;

import java.util.ArrayList;
import java.util.Random;

import gov.sandia.hemlock.classification.parameters.EnsembleParameters;
import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.core.FrameworkType;
import gov.sandia.hemlock.data.DataSet;
import gov.sandia.hemlock.data.IDataSet;
import gov.sandia.hemlock.classification.fusion.*;


/**
 * Super class for Hemlock implementations of models which are ensemble 
 * classifiers.  {@link gov.sandia.hemlock.classification.parameters.ModelParameters} 
 * that are also {@link gov.sandia.hemlock.classification.parameters.EnsembleParameters} 
 * and have {@link gov.sandia.hemlock.core.FrameworkType} Hemlock specified 
 * will end up being used to instantiate one of the sub classes of EnsembleModel.  
 * The EnsembleModel provides functionality that all ensemble classifiers will
 * need such as creating a set of base classifiers.
 *
 * @author Sean A. Gilpin
 */
public abstract class EnsembleModel implements Model
{

	protected DataSet dataSet;
	protected Model[] baseClassifierSet;
	protected DataSet[] outOfBagTestSets;
	protected Random generator;
	/** Ensemble parameters */
	public EnsembleParameters ensembleParams;
	
	/**
	 * Contains model building steps that all ensemble sub classes likely
	 * need to perform.  Building the set of base classifiers is done here. 
	 *
	 * @param modelParameters Specifies all information about which type of
	 *	model should be built      
	 * @throws Exception
	 */
	public void buildModel(ModelParameters modelParameters) throws Exception
	{
		ensembleParams = (EnsembleParameters)modelParameters;
		BaseClassifierSet baseSet = ensembleParams.getBaseModelParameters();
		if(ensembleParams.isSetSeed())
			generator = new Random(ensembleParams.getSeed());
		else
			generator = new Random();
		
		if(baseSet.type == BaseClassifierSetType.ModelParameters)
		{
			baseClassifierSet = baseSet.getModels(this);
		}
		else	//(baseSet.type == LoadModels)
		{
			baseClassifierSet = baseSet.loadModels(dataSet.info.dataSetName);			
		}
		
	}
	
	/**
	 * Used to build the model when subsampling is being used to train
	 * and evaluate the models, as is done in k-fold cross validation.
	 * This special attention given to the fold in this case is warranted 
	 * because of the way the base classifier serialization occurs as they 
	 * are being built, and each subsample will have its own set of base 
	 * classifiers.
	 *
	 * @param modelParameters Specifies all information about which type of
	 *	model should be built  
	 * @param fold The number of the fold being used to build this model
	 *	when subsampling methods such as k-fold cross validation are
	 *	being used.
	 * @throws Exception
	 */
	public void buildModel(ModelParameters modelParameters, int fold) throws Exception
	{
		ensembleParams = (EnsembleParameters)modelParameters;
		BaseClassifierSet baseSet = ensembleParams.getBaseModelParameters();
		if(ensembleParams.isSetSeed())
			generator = new Random(ensembleParams.getSeed());
		else
			generator = new Random();
		
		if(baseSet.type == BaseClassifierSetType.ModelParameters)
		{
			baseClassifierSet = baseSet.getModels(this, fold);
		}
		else	//(baseSet.type == LoadModels)
		{
			baseClassifierSet = baseSet.loadModels(dataSet.info.dataSetName, fold);			
		}
	}
	
	
	/**
	 * Used to create the complement of a set of instances specified by the
	 * parameter bag.  The complement will be all of the instances in 
	 * the dataSet member which are not in bag.
	 *
	 * @param bag A subsample of the data set specified in the dataSet
	 *	member variable.
	 * @return The DataSet which contains all of the instances not in bag.
	 */
	public DataSet generateOutOfBag(DataSet bag)
	{
		int numRecords = this.dataSet.records.size();
		ArrayList<double[]> oob = new ArrayList<double[]>();
		for(int i = 0; i < numRecords; i++)
		{
			if(!bag.records.contains(this.dataSet.records.get(i)))
				oob.add(this.dataSet.records.get(i));
		}
		
		return new DataSet(this.dataSet.recordSchema, this.dataSet.info, oob);
	}
	
	/**
	 * Creates a subsample (with replacement) of the instances in the member 
	 * dataSet.  The number of instances in the subsample will be the same
	 * as the number of instances in the original dataset.  It is very
	 * likely that many of the sampled instances will be duplicates.
	 *
	 * @return The DataSet which contains all of the sampled instances.
	 */
	public DataSet generateBag()
	{
		int numRecords = this.dataSet.records.size();
		ArrayList<double[]> bag = new ArrayList<double[]>();
		for(int i = 0; i < numRecords; i++)
		{
			//create a random number between 0 and number of records
			int index = generator.nextInt(numRecords);
			bag.add(this.dataSet.records.get(index));
		}
		
		return new DataSet(this.dataSet.recordSchema, this.dataSet.info, bag);
	}
	
	/**
	 * Specifies the data that will be used to train the ensemble
	 * model.  Some or part of this data will also be used to train the
	 * base classifiers.  This must be set before the model can be trained.
	 *
	 * @param data The DataSet which will be used to train this model.
	 */
	public void setData(IDataSet data)
	{
		dataSet = (DataSet)data;
	}
	
	/**
	 * Retrieves the data that this model will or has been trained with.
	 *
	 * @return The associated training DataSet.
	 */
	public DataSet getData()
	{
		return dataSet;
	}
	
	/**
	 * After the ensemble model has been trained there will be a set of base
	 * classifier models also trained which can be accessed using this
	 * method.
	 *
	 * @return An array of base classifer associate with this ensemble.
	 */
	public Model[] getBaseClassifierModels()
	{
		return baseClassifierSet;
	}
}
