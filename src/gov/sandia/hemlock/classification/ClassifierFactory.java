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

import gov.sandia.hemlock.classification.parameters.ModelParameters;
import gov.sandia.hemlock.core.*;
import gov.sandia.hemlock.data.*;
import java.lang.reflect.*;

/**
 * Used to construct classification models using any of the interfaced model
 * building frameworks.  An instance of this class will be specific to a given
 * data set and model building framework.  The advantage of this approach is
 * the data sets are converted to the appropriate framework native data set 
 * types only once, and an instance of this type can then be repeatedly used to 
 * build any types of models that are supported by the given framework.
 *
 * @author Sean A. Gilpin
 */
public class ClassifierFactory
{
	private FrameworkType framework;
	private DataSet dataSet;
	private IDataSet wekaDataSet;
	private IDataSet cognitiveDataSet;
	private boolean isFoldSet = false;
	private int fold;
	
	private boolean wekaSupported = false;
	private boolean cognitiveSupported = false;
	
	/**
	 * Constructor to create an instance specific to a model building 
	 * framework and a data set.
	 *
	 * @param framework The model building framework which will create 
	 *	models
	 * @param data The training data set used to build models
	 * @throws FrameworkNotSupportedException When the given framework 
	 *	type is not properly configured to be used by Hemlock
	 */
	public ClassifierFactory(FrameworkType framework, DataSet data) 
		throws FrameworkNotSupportedException
	{
		this.framework = framework;
		this.dataSet = data;
		
		testFrameworkAvailability(framework);
	}
	
	/**
	 * Constructor to be used when subsampling methods such as k-fold cross
	 * validation are being used.  This is needed because some serialization
	 * methods occur while models are being built and the identification
	 * of the current subsample is needed for the path of the serialized 
	 * models.
	 *
	 * @param framework The model building framework which will create 
	 *	models
	 * @param data The training data set used to build models
	 * @param fold A number identifying which subsample is represented by
	 	data.
	 * @throws FrameworkNotSupportedException When the given framework 
	 *	type is not properly configured to be used by Hemlock
	 */
	public ClassifierFactory(FrameworkType framework, DataSet data, int fold) 
		throws FrameworkNotSupportedException
	{
		this.framework = framework;
		this.dataSet = data;
		this.isFoldSet = true;
		this.fold = fold;
		
		testFrameworkAvailability(framework);
	}
	
	/**
	 * The generic function to be used for creating a model as specified
	 * in modelParameters.  This method will create the framework specific 
	 * native data types if not already created and then call framework 
	 * specific model creation subroutines.
	 *
	 * @param modelParameters Specifies the parameters for a model to be
	 *	built.
	 * @return The resulting trained model
	 * @throws Exception
	 *
	 */
	public Model createModel(ModelParameters modelParameters) throws Exception
	{
		if(framework == FrameworkType.weka)
		{
			//first time model is created need to create converted data
			if(wekaDataSet == null)
			{
				wekaDataSet = getDataSet(framework, dataSet);
			}
			return createWekaModel(modelParameters);
		}
		else if(framework == FrameworkType.cog_foundry)
		{
			if(cognitiveDataSet == null)
			{
				cognitiveDataSet = getDataSet(framework, dataSet);
			}
			return createCognitiveModel(modelParameters);
		}
		else if(framework == FrameworkType.hemlock)
		{
			return createHemlockModel(modelParameters);
		}
		else
		{
			throw new Exception("that framework is not supported");
		}
	}
	
	/**
	 * Creates a model using the Hemlock framework.  The details of the
	 * types of models that Hemlock can create are detailed within this
	 * method.
	 *
	 * @param modelParameters The parameters that specify the model to be
	 *	built
	 * @return The resulting model created using Hemlock
	 * @throws Exception
	 */
	private Model createHemlockModel(ModelParameters modelParameters) throws Exception
	{
		switch(modelParameters.modelType)
		{
		case Voting:
			gov.sandia.hemlock.classification.fusion.Voting voter = new gov.sandia.hemlock.classification.fusion.Voting();
			voter.setData(dataSet);
			if(isFoldSet)
				voter.buildModel(modelParameters, this.fold);
			else
				voter.buildModel(modelParameters);
			return voter;
		case LinearRegression:
			gov.sandia.hemlock.classification.fusion.OrdinaryLeastSquares lr = new gov.sandia.hemlock.classification.fusion.OrdinaryLeastSquares();
			lr.setData(dataSet);
			if(isFoldSet)
				lr.buildModel(modelParameters, this.fold);
			else
				lr.buildModel(modelParameters);
			return lr;
		case SumRule:
			gov.sandia.hemlock.classification.fusion.SumRule sr = new gov.sandia.hemlock.classification.fusion.SumRule();
			sr.setData(dataSet);
			if(isFoldSet)
				sr.buildModel(modelParameters, this.fold);
			else
				sr.buildModel(modelParameters);
			return sr;
		default:
			throw new Exception ("that model is not supported");
		}
	}
	
	/**
	 * Creates a model using Cognitive Framework.  Cognitive Framework 
	 * integration has not been implemented yet, so this method is a
	 * placeholder for future use.
	 *
	 * @param modelParameters The parameters that specify the model to be
	 *	built
	 * @return The resulting model created using Hemlock
	 * @throws Exception
	 */
	private Model createCognitiveModel(ModelParameters modelParameters) throws Exception
	{
		Class cogModelClass;
		switch(modelParameters.modelType)
		{
			case DecisionTree:
				cogModelClass = loadClass("gov.sandia.hemlock.cognitiveFoundryInterface.DecisionTree");
				gov.sandia.hemlock.classification.Model tree = (Model)cogModelClass.newInstance();
				tree.setData(cognitiveDataSet);
				tree.buildModel(modelParameters);
				return tree;
			default:
				throw new Exception ("that model is not supported");
		}	
	}
	
	
	/**
	 * Creates a model using the Weka Framework.  The details of which 
	 * types of models are supported using weka can be seen within the
	 * details of this method.
	 *
	 * @param modelParameters The parameters that specify the model to be
	 *	built
	 * @return The resulting model created using Hemlock
	 * @throws Exception
	 */
	private Model createWekaModel(ModelParameters modelParameters) throws Exception
	{
		Class wekaModelClass;
		switch(modelParameters.modelType)
		{
			case RandomForest:
				wekaModelClass = loadClass("gov.sandia.hemlock.wekaInterface.RandomForest");
				gov.sandia.hemlock.classification.Model rf = (Model)wekaModelClass.newInstance();
				rf.setData(wekaDataSet);
				rf.buildModel(modelParameters);
				return rf;
			case NaiveBayesian:
				wekaModelClass = loadClass("gov.sandia.hemlock.wekaInterface.NaiveBayesian");
				gov.sandia.hemlock.classification.Model nb = (Model)wekaModelClass.newInstance();
				nb.setData(wekaDataSet);
				nb.buildModel(modelParameters);
				return nb;
			case KNearestNeighbor:
				wekaModelClass = loadClass("gov.sandia.hemlock.wekaInterface.IBk");
				gov.sandia.hemlock.classification.Model ibk = (Model)wekaModelClass.newInstance();
				ibk.setData(wekaDataSet);
				ibk.buildModel(modelParameters);
				return ibk;
			case RIPPER:
				wekaModelClass = loadClass("gov.sandia.hemlock.wekaInterface.RIPPER");
				gov.sandia.hemlock.classification.Model jrip = (Model)wekaModelClass.newInstance();
				jrip.setData(wekaDataSet);
				jrip.buildModel(modelParameters);
				return jrip;
			case SVM:
				wekaModelClass = loadClass("gov.sandia.hemlock.wekaInterface.SVM");
				gov.sandia.hemlock.classification.Model svm = (Model)wekaModelClass.newInstance();
				svm.setData(wekaDataSet);
				svm.buildModel(modelParameters);
				return svm;
			case RandomTree:
				wekaModelClass = loadClass("gov.sandia.hemlock.wekaInterface.RandomTree");
				gov.sandia.hemlock.classification.Model rt = (Model)wekaModelClass.newInstance();
				rt.setData(wekaDataSet);
				rt.buildModel(modelParameters);
				return rt;
			default:
				throw new ModelTypeNotSupportedException(modelParameters.modelType, modelParameters.frameworkType);
		}
	}
	
	/**
	 * This method is used to test if an external frameworks is
	 * properly configured for use with Hemlock.  If this test fails an
	 * exception,  {@link FrameworkNotSupportedException} will be thrown
	 * and the framework in question will not be able to be used to build
	 * models.
	 *
	 * @param framework Specifies the external framework whose availability
	 *	will be tested
	 * @throws  FrameworkNotSupportedException When the test fails and the
	 *	external framework is not available.
	 */
	private void testFrameworkAvailability(FrameworkType framework) throws FrameworkNotSupportedException
	{
		try
		{
			if(framework == FrameworkType.weka)
			{
				Class wekaLibraryTestClass = loadClass("gov.sandia.hemlock.wekaInterface.WekaLibraryTest");
				//default constructor will run all tests
				Object test = wekaLibraryTestClass.newInstance();
			}
			else if(framework == FrameworkType.cog_foundry)
			{
				Class cogFoundryTestClass = loadClass("gov.sandia.hemlock.cognitiveFoundryInterface.CognitiveLibraryTest");
				//default constructor will run all tests
				Object test = cogFoundryTestClass.newInstance();
			}
		}
		catch(ClassNotFoundException e)
		{
			throw new FrameworkNotSupportedException(framework.toString());
		}
		catch(InstantiationException e)
		{
			throw new FrameworkNotSupportedException(framework.toString());
		}
		catch(IllegalAccessException e)
		{
			throw new FrameworkNotSupportedException(framework.toString());
		}
	}

	/**
	 * Converts a {@link gov.sandia.hemlock.data.DataSet} into an external
	 * framework specific native data set.  Each external framework has its
	 * own internal data structures for representing data and the
	 * translation begins in this function.
	 * 
	 * @param frameworkType The external framework whose native data set
	 * 	we are interested in creating.
	 * @param data The Hemlock representation of the data set which needs
	 *	to be converted.
	 * @return A data set which can be used with an external framework
	 *	for building classification models.
	 */
	private IDataSet getDataSet(FrameworkType frameworkType, DataSet data) throws Exception
	{
		Class[] argsClass = new Class[] { IDataSet.class };
	    Object[] args = new Object[] { data };
		if(frameworkType == FrameworkType.weka)
		{
			Class wekaDataSetClass = loadClass("gov.sandia.hemlock.wekaInterface.WekaDataSet");
			Constructor constr = wekaDataSetClass.getConstructor(argsClass);
			return (IDataSet) constr.newInstance(args);
		}
		else if(frameworkType == FrameworkType.cog_foundry)
		{
			Class wekaDataSetClass = loadClass("gov.sandia.hemlock.cognitiveFoundryInterface.CognitiveDataSet");
			Constructor constr = wekaDataSetClass.getConstructor(argsClass);
			return (IDataSet) constr.newInstance(args);
		}
		else 
		{
			throw new Exception("Data set conversion not implemented for" + frameworkType.toString());
		}
		
	}
	
	/**
	 * A reflection tool used to obtain a class type given the fully
	 * qualified name of the class.  This class type can be used to 
	 * instantiate an object of the same type.  This can be useful because
	 * the class in question does not need to available at compile time.
	 *
	 * @param className The name of the class that is to be instantiated.
	 * @return A Class object which can be used to instantiate objects of 
	 *	the same type.
	 * @throws ClassNotFoundException When the class specified by className
	 * 	is not found in the classpath.
	 */
	
	private Class loadClass(String className) throws ClassNotFoundException
	{
	    Class theClass = null;
	    try 
	    {
	        theClass = Class.forName( className, true, Thread.currentThread().getContextClassLoader() );
	    }

	    catch (ClassNotFoundException e) 
	    {
	        theClass = Class.forName( className );
	    }
	    return theClass;
	}
}
