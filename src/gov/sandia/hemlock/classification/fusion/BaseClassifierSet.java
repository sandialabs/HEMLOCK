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

package gov.sandia.hemlock.classification.fusion;

import gov.sandia.hemlock.classification.parameters.*;
import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.core.*;
import gov.sandia.hemlock.data.*;
import java.io.*;

/**
 * All information needed to specify a set of base classifiers for use with
 * an ensemble classifier.  A set of base classifiers is a set of classification
 * models whose predictions will be combined by an ensemble classifier.
 *
 * @author Sean A. Gilpin
 */
public class BaseClassifierSet
{
	/** The method for specifying the set of base classifiers*/
	public BaseClassifierSetType type;
	/** When type==ModelParameters then each element of this array will
		correspond to a model in the resulting base classifier set.*/
	public ModelParameters[] params;
	/** When true will specify that the created set of base classifiers
		should be serialized */
	public boolean serialize = false;
	/** The path that a set of base classifiers should be loaded from or
		serialized to.*/
	public String path;
	
	/**
	 * Constructor for the case when the BaseClassifierType is 
	 * ModelParameter.
	 * 
	 * @param params A list of ModelParameters which will subsequently
	 *	be used to create a set of classification models.
	 */
	public BaseClassifierSet(ModelParameters[] params)
	{
		this.type = BaseClassifierSetType.ModelParameters;
		this.params = params;
	}
	
	/**
	 * Constructor for the case when BaseClassifierSetType is ModelParameter
	 * that also allows to specify whether or not to serialize and the
	 * serialization path.
	 *
	 * @param params A list of ModelParameters which will subsequently
	 *	be used to create a set of classification models.
	 * @param serialize If true, when the base classifier set models are 
	 * 	created, the entire set should be serialized.
	 * @param path If the base classifier set is serialized, this is the
	 *	path.
	 */
	public BaseClassifierSet(ModelParameters[] params,  boolean serialize, String path)
	{
		this(params);
		this.serialize = serialize;
		this.path = path;
	}
	
	/**
	 * Construcotr for the case when BaseClassifierSetType is LoadModels
	 *
	 * @param path The path where the set of serialized base classifiers 
	 * 	should be located
	 */
	public BaseClassifierSet(String path)
	{
		this.type = BaseClassifierSetType.LoadModels;
		this.path = path;
	}
		
	
	/**
	 * When using subsampling evaluation methods such as k-fold cross 
	 * validation, this method will serialize a set of models for future
	 * use by other ensemble classifier models.  This is meant
	 * for serializing sets of base classifiers.  Each subsample will have 
	 * its own set of base classifiers so it is necesary to take extra
	 * measures when serializing when using subsampling evaluation methods.
	 *
	 * @param models A set of models to be serialized.
	 * @param dataSetName The name of the data set this set of models is
	 * 	specific to.
	 * @param fold The identification of the subsample which the models
	 *	have been built with.
	 * @throws IOException If there is a problem writing to the path.
	 */
	public void serializeModels(Model[] models, String dataSetName, int fold) throws IOException
	{
		String serializePath = this.path + "/" + dataSetName + "/" + fold + "/";
		File directory = new File(serializePath);
		directory.mkdirs();
		
		for(int i=0; i< models.length; i++)
			ModelSerialization.serializeModel(models[i], serializePath + i + ".model");
		
	}
	
	/**
	 * Given an array of models, this method will serialized them using
	 * the appropriate paths and format to allow them to be loaded for 
	 * future use with possibly other types of ensembles.  This is meant
	 * for serializing sets of base classifiers
	 *
	 * @param models A set of models to be serialized.
	 * @param dataSetName The name of the data set this set of models is
	 * 	specific to.
	 * @throws IOException If there is a problem writing to the path.
	 */
	public void serializeModels(Model[] models, String dataSetName) throws IOException
	{
		String serializePath = this.path + "/" + dataSetName + "/";
		File directory = new File(serializePath);
		directory.mkdirs();
		
		for(int i=0; i< models.length; i++)
			ModelSerialization.serializeModel(models[i], serializePath + i + ".model");
		
	}
	
	/**
	 * 
	 *
	 */
	public Model[] loadModels(String dataSetName, int fold) throws Exception
	{
		String fullPath = this.path + "/" + dataSetName + "/" + fold;
		File folder = new File(fullPath);
		String[] modelFileNames = folder.list();
		Model[] models = new Model[modelFileNames.length];
		for(int i=0; i < modelFileNames.length; i++)
		{
			models[i]= ModelSerialization.loadModel(fullPath + "/" + modelFileNames[i]);
		}
		
		return models;
		
	}
	
	/**
	 * Loads a set of previously serialized models created using the given
	 * dataSetName in the appropriate path.  These models are intended for
	 * use as a set of base classifiers.
	 *
	 * @param dataSetName The name of the data set for which the models
	 *	to be loaded were trained with.
	 * @return Model[] A set of models which were loaded.
	 * @throws Exception
	 */
	public Model[] loadModels(String dataSetName) throws Exception
	{
		String fullPath = this.path + "/" + dataSetName;
		File folder = new File(fullPath);
		String[] modelFileNames = folder.list();
		Model[] models = new Model[modelFileNames.length];
		for(int i=0; i < modelFileNames.length; i++)
		{
			models[i]= ModelSerialization.loadModel(fullPath + "/" + modelFileNames[i]);
		}
		
		return models;
		
	}
	
	/**
	 * Creates a set of base classifiers as specified in the model 
	 * parameters associated with an instance of this type.  Will also
	 * serialize all of the produced models when an instance of this
	 * type has been specified to do so.
	 *
	 * @param eModel The ensemble model which may have special instructions
	 *	for producing the models, such as whether or not to use bagging.
	 * @return An array of Models which are produced by this method.
	 * @throws Exception
	 */
	public Model[] getModels(EnsembleModel eModel) throws Exception
	{
		Model[] models = new Model[this.params.length];
		
		ClassifierFactory wekaFactory = null;
		for(int i = 0; i < models.length; i++)
		{
			if(params[i % params.length].frameworkType == FrameworkType.weka)
			{
				if(eModel.ensembleParams.getEnsembleGeneration() == EnsembleParameters.EnsembleGenerationType.Bagging)
				{
					//generate bootstrapped data set
					DataSet bag = eModel.generateBag();
					//outOfBagTestSets[i] = generateOutOfBag(bag); 
					//create new wekaFactory
					wekaFactory = new ClassifierFactory(FrameworkType.weka, bag);
				}
				else if(wekaFactory == null)
				{
					wekaFactory = new ClassifierFactory(FrameworkType.weka, eModel.getData());
				}
				models[i] = wekaFactory.createModel(params[i % params.length]);
			}
			else
			{
				throw new Exception("Need to add support for base classifiers in other framework");
			}
		}
		
		if(serialize)
			this.serializeModels(models, eModel.getData().info.dataSetName);
		
		return models;
	}
	
	/**
	 * When using a subsampling evaluation method such as k-fold cross
	 * validation, creates a set of base classifiers as specified in the model 
	 * parameters associated with an instance of this type.  Will also
	 * serialize all of the produced models when an instance of this
	 * type has been specified to do so.
	 *
	 * @param eModel The ensemble model which may have special instructions
	 *	for producing the models, such as whether or not to use bagging.
	 * @param fold The identification for the subsample used to build
	 *	the models.  Only needed when serializing the models.
	 * @return An array of Models which are produced by this method.
	 * @throws Exception
	 */
	public Model[] getModels(EnsembleModel eModel, int fold) throws Exception
	{
		Model[] models = new Model[this.params.length];
		
		ClassifierFactory wekaFactory = null;
		for(int i = 0; i < models.length; i++)
		{
			if(params[i % params.length].frameworkType == FrameworkType.weka)
			{
				if(eModel.ensembleParams.getEnsembleGeneration() == EnsembleParameters.EnsembleGenerationType.Bagging)
				{
					//generate bootstrapped data set
					DataSet bag = eModel.generateBag();
					//outOfBagTestSets[i] = generateOutOfBag(bag); 
					//create new wekaFactory
					wekaFactory = new ClassifierFactory(FrameworkType.weka, bag);
				}
				else if(wekaFactory == null)
				{
					wekaFactory = new ClassifierFactory(FrameworkType.weka, eModel.getData());
				}
				models[i] = wekaFactory.createModel(params[i % params.length]);
			}
			else
			{
				throw new Exception("Need to add support for base classifiers in other framework");
			}
		}
		
		if(serialize)
			this.serializeModels(models, eModel.getData().info.dataSetName, fold);
		
		return models;
	}
	
}

