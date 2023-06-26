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

package gov.sandia.hemlock.classification.parameters;

import gov.sandia.hemlock.classification.ModelType;
import gov.sandia.hemlock.classification.fusion.*;
import gov.sandia.hemlock.core.FrameworkType;
import java.util.Hashtable;

/** 
 * Used to specify parameters that are generally relevenat to ensemble models. 
 * All ensembles need methods for manipulation of base classifier sets which 
 * this class provides among other things.
 *
 * @author Sean A. Gilpin
 */
public class EnsembleParameters extends ModelParameters
{
	protected BaseClassifierSet baseModelParams;
	protected String baseClassifierSetID;
	protected EnsembleGenerationType ensembleGeneration = EnsembleGenerationType.SameTrainingSet;
	protected int numberOfBaseClassifiers = 0;
	protected long seed;
	protected boolean seedSet=false;
	
	/**
	 * Calls the inhereted ModelParameters constructor and then specifies
	 * that this model parameters is used to build an ensemble by setting
	 * ModelParameters.isEnsemble to true;
	 *
	 * @param frameworkType The framework which should be used to build
	 * 	the model.
	 * @param modelType The type of model that will be specified by these
	 * 	parameters.
	 */
	protected EnsembleParameters(FrameworkType frameworkType, ModelType modelType)
	{
		super(frameworkType, modelType);
		this.isEnsemble = true;
	}
	
	/**
	 * Given a hashtable of parameter names paired with their values,
	 * assigns the associated parameters their respected values within
	 * this instance.  When experiment files are read, the parameters in
	 * the experiment files are converted to ModelParameter objects using 
	 * these hashtables in this way.
	 *
	 * @param parameters The hashtable full of parameter name/value pairs.
	 */
	public void setEnsembleParameters(Hashtable<String,String> parameters)
	{
		if(parameters.containsKey("BaseClassifierSetID"))
		{
			this.setBaseClassifierSetID(parameters.get("BaseClassifierSetID"));
		}
		if(parameters.containsKey("EnsembleGeneration"))
		{
			this.setEnsembleGeneration(
					EnsembleGenerationType.valueOf(parameters.get("EnsembleGeneration")));
		}
		if(parameters.containsKey("NumberOfBaseClassifiers"))
		{
			this.setNumberOfBaseClassifiers(Integer.parseInt(parameters.get("NumberOfBaseClassifiers")));
		}
		if(parameters.containsKey("seed"))
		{
			this.setSeed(Long.parseLong(parameters.get("seed")));
		}
	}
	
	public String getBaseClassifierSetID()
	{
		return baseClassifierSetID;
	}
	
	public void setBaseClassifierSetID(String ID)
	{
		baseClassifierSetID = ID;
	}
	
	public void setBaseModelParameters(BaseClassifierSet baseParams)
	{
		baseModelParams = baseParams;
	}
	
	public BaseClassifierSet getBaseModelParameters()
	{
		return baseModelParams;
	}
	
	public void setEnsembleGeneration(EnsembleGenerationType eg)
	{
		this.ensembleGeneration = eg;
	}
	
	public EnsembleGenerationType getEnsembleGeneration()
	{
		return this.ensembleGeneration;
	}
	
	public void setNumberOfBaseClassifiers(int numModels)
	{
		this.numberOfBaseClassifiers = numModels;
	}
	
	public int getNumberOfBaseClassifiers()
	{
		return this.numberOfBaseClassifiers;
	}
	
	public void setSeed(long seed)
	{
		this.seed = seed;
		this.seedSet = true;
	}
	public long getSeed()
	{
		return this.seed;
	}
	public boolean isSetSeed()
	{
		return this.seedSet;
	}
	
	public enum EnsembleGenerationType
	{
		Bagging, SameTrainingSet;
	}
}
