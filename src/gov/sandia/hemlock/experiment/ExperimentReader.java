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
 
package gov.sandia.hemlock.experiment;

import java.io.*;
import java.util.*;
import gov.sandia.hemlock.core.*;
import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.classification.parameters.*;
import gov.sandia.hemlock.classification.fusion.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import gov.sandia.hemlock.data.*;

/**
 * Reads an experiment file and records the details into an Experiment object.
 */
public class ExperimentReader
{
	private static DataSetInfo[] dataSetInfo;
	
	public static Experiment[] readExperimentFile(String fileName, DataSetInfo[] info) throws ParserConfigurationException, IOException, SAXException, Exception
	{
		dataSetInfo = info;
		
		File file = new File(fileName);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
	
		Element root = (Element)doc.getElementsByTagName("Experiments").item(0);
		//Read experiments
		Experiment[] expArr = readExperiments(root);

		//Read base classifiers
		Hashtable<String, BaseClassifierSet> baseParams = readBaseClassifierParameters(root);

		//link base classifiers to experiments
		linkEnsemblesToBaseClassifiers(expArr, baseParams);
		
		return expArr;
	}
	
	
	public static void linkEnsemblesToBaseClassifiers(Experiment[] experiments, Hashtable<String, BaseClassifierSet> baseParams)
	{
		for(int i = 0; i < experiments.length; i++)
		{
			experiments[i].baseClassiferSets = baseParams;
			if(experiments[i].modelParameters.isEnsemble())
			{
				EnsembleParameters params = (EnsembleParameters)experiments[i].modelParameters;
				String ID = params.getBaseClassifierSetID();
				params.setBaseModelParameters(baseParams.get(ID));
			}	
		}
	}
	
	public static Hashtable<String, BaseClassifierSet> readBaseClassifierParameters(Element root) throws IOException
	{
		Hashtable<String, BaseClassifierSet> setHash = new Hashtable<String, BaseClassifierSet>();
		NodeList nlBaseClassifiers = root.getElementsByTagName("BaseClassifiers");
		if(nlBaseClassifiers.getLength() > 0)
		{
			Element eBaseClassifiers = (Element)nlBaseClassifiers.item(0);
			NodeList setList = eBaseClassifiers.getElementsByTagName("Set");
			for(int i = 0; i < setList.getLength(); i++)
			{
				
				Element set = (Element) setList.item(i);
				//get set id
				String setID = set.getAttribute("ID");
				//get type
				String strSetType = set.getAttribute("Type");
				BaseClassifierSetType setType = BaseClassifierSetType.valueOf(strSetType);
				

				switch(setType)
				{
				case ModelParameters:
					//get serilialize
					boolean doSerialize = false;
					String savePath = "";
					try{
						savePath = set.getAttribute("Path");
						String strSerialize= set.getAttribute("Serialize");
						doSerialize = Boolean.parseBoolean(strSerialize);
					}catch(Exception e){}
					//read model parameters
					NodeList modelParamList = set.getElementsByTagName("ModelParameters");
					ArrayList<ModelParameters> params = new ArrayList<ModelParameters>();
					for(int j = 0; j < modelParamList.getLength(); j++)
					{
						params.add(createModelParameters((Element)modelParamList.item(j)));
					}
					ModelParameters[] arrParams = new ModelParameters[params.size()];
					params.toArray(arrParams);
					BaseClassifierSet bcs = new BaseClassifierSet(arrParams, doSerialize, savePath);
					setHash.put(setID, bcs);
					break;
				case LoadModels:
					String loadPath = set.getAttribute("Path");
					BaseClassifierSet bcsLoad = new BaseClassifierSet(loadPath);
					setHash.put(setID,bcsLoad);
					break;
				}
			}
		}
		
		return setHash;
	}
	
	public static Experiment[] readExperiments(Element root) throws Exception
	{
		ArrayList<Experiment> experiments = new ArrayList<Experiment>();
		NodeList nodes = root.getElementsByTagName("Experiment");
		for (int i = 0; i < nodes.getLength(); i++) 
		{
			Element element = (Element) nodes.item(i);
			experiments.add(createExperiment(element));	
		}

		Experiment[] expArr = new Experiment[experiments.size()];
		return experiments.toArray(expArr);
	}
	
	private static Experiment createExperiment(Element experimentNode) throws Exception
	{
		//Method
		Element eMethod = (Element)experimentNode.getElementsByTagName("Method").item(0);
		Element eMethodType = (Element)eMethod.getElementsByTagName("MethodType").item(0);
		CharacterData cdMethodType = (CharacterData)eMethodType.getFirstChild();
		String strMethodType = cdMethodType.getData();
		
		switch(ExperimentTypes.valueOf(strMethodType))
		{
		case KFoldCrossValidation:
			return readKFoldCrossValidation(experimentNode);
		case NoHoldOut:
			return readNoHoldOut(experimentNode);
		default:
			throw new Exception("Unknown experiment type.");
				
		}
	}
	
	public static Experiment readKFoldCrossValidation(Element experimentNode)
	{
		KFoldsExperiment experiment = new KFoldsExperiment(dataSetInfo);
		//Name
		experiment.name = experimentNode.getAttribute("ID").trim();
		//Method
		experiment.experimentType = ExperimentTypes.KFoldCrossValidation;
		//Get number of folds
		Element eMethod = (Element)experimentNode.getElementsByTagName("Method").item(0);
		Element eNumberOfFolds = (Element)eMethod.getElementsByTagName("NumberOfFolds").item(0);
		CharacterData cdNumberOfFolds = (CharacterData)eNumberOfFolds.getFirstChild();
		experiment.numberOfFolds = Integer.parseInt(cdNumberOfFolds.getData());
		//Get seed for creating folds
		try
		{
			Element eSeed = (Element)eMethod.getElementsByTagName("Seed").item(0);
			CharacterData cdSeed = (CharacterData)eSeed.getFirstChild();
			experiment.seed = Long.parseLong(cdSeed.getData());
			experiment.seedSet = true;
		} catch(Exception e){}
		//ModelParameters
		experiment.modelParameters = createModelParameters((Element)experimentNode.getElementsByTagName("ModelParameters").item(0));
		
		//Data
		experiment.dataQuery = createDataSetQuery((Element)experimentNode.getElementsByTagName("Data").item(0));
		
		//Metrics
		Element eMetrics= (Element)experimentNode.getElementsByTagName("Metrics").item(0);
		NodeList nlMetrics = eMetrics.getElementsByTagName("Metric");
		readEvaluationMeasureTypes(experiment, nlMetrics);
		
		return experiment;
	}
	
	public static Experiment readNoHoldOut(Element experimentNode)
	{
		NoHoldOutExperiment experiment = new NoHoldOutExperiment(dataSetInfo);
		//Name
		experiment.name = experimentNode.getAttribute("ID").trim();
		//Method
		experiment.experimentType = ExperimentTypes.NoHoldOut;
		
		//ModelParameters
		experiment.modelParameters = createModelParameters((Element)experimentNode.getElementsByTagName("ModelParameters").item(0));
		
		//Data
		experiment.dataQuery = createDataSetQuery((Element)experimentNode.getElementsByTagName("Data").item(0));
		
		//Metrics
		Element eMetrics= (Element)experimentNode.getElementsByTagName("Metrics").item(0);
		NodeList nlMetrics = eMetrics.getElementsByTagName("Metric");
		readEvaluationMeasureTypes(experiment, nlMetrics);
		
		return experiment;
	}
	
	private static void readEvaluationMeasureTypes(Experiment experiment, NodeList nlMeasures)
	{
		for(int i = 0; i < nlMeasures.getLength(); i++)
		{
			Element eMetric = (Element)nlMeasures.item(i);
			String strMetric = eMetric.getAttribute("type");
			if(strMetric.equalsIgnoreCase("Confusion Matrix"))
				experiment.computeConfusionMatrix = true;
			if(strMetric.equalsIgnoreCase("Accuracy"))
				experiment.computeAccuracy = true;
			if(strMetric.equalsIgnoreCase("ROCAnalysis"))
			{
				experiment.computeROCMetrics = true;
				experiment.rocPositiveClass = Integer.parseInt(eMetric.getAttribute("positiveClass"));
			}
			
			//Read diversity measurements
			if(strMetric.equalsIgnoreCase("Diversity_disagreement"))
				experiment.computeDiversity_disagreement = true;
			if(strMetric.equalsIgnoreCase("Diversity_correlation"))
				experiment.computeDiversity_correlation = true;
			if(strMetric.equalsIgnoreCase("Diversity_yuleQ"))
				experiment.computeDiversity_yuleQ = true;
			if(strMetric.equalsIgnoreCase("Diversity_doubleFault"))
				experiment.computeDiversity_doubleFault = true;
			if(strMetric.equalsIgnoreCase("Diversity_entropy"))
				experiment.computeDiversity_entropy = true;
			if(strMetric.equalsIgnoreCase("Diversity_generalDiversity"))
				experiment.computeDiversity_generalDiversity = true;
			if(strMetric.equalsIgnoreCase("Diversity_coincidentFailure"))
				experiment.computeDiversity_coincidentFailure = true;
			if(strMetric.equalsIgnoreCase("Diversity_difficulty"))
				experiment.computeDiversity_difficulty = true;	
		}
	}
	
	private static DataSetQuery createDataSetQuery(Element dsqNode)
	{
		//Method (Auto or Manual)
		Element eMethod = (Element)dsqNode.getElementsByTagName("Method").item(0);
		CharacterData cdMethod = (CharacterData)eMethod.getFirstChild();
		String strMethod= cdMethod.getData();
		
		DataSetQuery dsq;
		if(strMethod.equals("Auto"))
		{
			dsq = new DataSetQuery(true);  //true = automatic
			//Query
			Element queryNode = (Element)dsqNode.getElementsByTagName("Query").item(0);
			//NominalAttributes value=yes/no
			NodeList nlNominal = queryNode.getElementsByTagName("NominalAttributes");
			if(nlNominal.getLength() > 0)
			{
				Element eNominal = (Element)nlNominal.item(0);
				boolean valueNominal = eNominal.getAttribute("value").equals("yes");
				dsq.setNominalAttributes(valueNominal);
			}
			
			//ContinuousAttributes value=yes/no 
			NodeList nlContinuous = queryNode.getElementsByTagName("ContinuousAttributes");
			if(nlContinuous.getLength() > 0)
			{
				Element eContinuous = (Element)nlContinuous.item(0);
				boolean valueContinuous = eContinuous.getAttribute("value").equals("yes");
				dsq.setContinuousAttributes(valueContinuous);
			}
			
			//MissingValues value=yes/no
			NodeList nlMissing = queryNode.getElementsByTagName("MissingValues");
			if(nlMissing.getLength() > 0)
			{
				Element eMissing = (Element)nlMissing.item(0);
				boolean valueMissing = eMissing.getAttribute("value").equals("yes");
				dsq.setMissingValues(valueMissing);
			}
		}
		else
		{
			dsq = new DataSetQuery(false);  //false = manual
			//DataSet
			ArrayList<String> datasets = new ArrayList<String>();
			ArrayList<DataSetInfo> datasetPaths = new ArrayList<DataSetInfo>();
			Element dsNode = (Element)dsqNode.getElementsByTagName("DataSets").item(0);
			NodeList nodesDataSet = dsNode.getElementsByTagName("DataSet");
			for(int i=0; i < nodesDataSet.getLength(); i++)
			{
				Element eDataSet = (Element)nodesDataSet.item(i);
				//Name
				Element eName = (Element)eDataSet.getElementsByTagName("Name").item(0);
				CharacterData cdName = (CharacterData)eName.getFirstChild();
				String strName= cdName.getData();
				//Package or Path
				Element ePackage = (Element)eDataSet.getElementsByTagName("Repository").item(0);
				if(ePackage != null)
				{
					CharacterData cdPackage = (CharacterData)ePackage.getFirstChild();
					String strPackage = cdPackage.getData();
					datasets.add(strPackage.trim() + "." + strName.trim());
				}
				else
				{
					Element ePath = (Element)eDataSet.getElementsByTagName("Path").item(0);
					CharacterData cdPath = (CharacterData)ePath.getFirstChild();
					String strPath = cdPath.getData();
					datasetPaths.add(DataSetInfo.fromPath(strPath, strName, FileFormatType.ModifiedC45));
				}					
			}
			String[] arrDatasets = new String[datasets.size()];
			dsq.setDataSets(datasets.toArray(arrDatasets));
			DataSetInfo[] arrDatasetInfos = new DataSetInfo[datasetPaths.size()];
			dsq.setDataSetInfos(datasetPaths.toArray(arrDatasetInfos));
		}
		return dsq;
	}
	
	
	private static ModelParameters createModelParameters(Element mpNode)
	{
		//FrameworkType
		Element eFramework = (Element)mpNode.getElementsByTagName("FrameworkType").item(0);
		CharacterData cdFramework = (CharacterData)eFramework.getFirstChild();
		String strFramework= cdFramework.getData();
		FrameworkType framework = FrameworkType.valueOf(strFramework);
		
		//ModelType
		Element eModel = (Element)mpNode.getElementsByTagName("ModelType").item(0);
		CharacterData cdModel = (CharacterData)eModel.getFirstChild();
		String strModel= cdModel.getData();
		ModelType model = ModelType.valueOf(strModel);
		
		//Parameter name="k">1</Parameter>
		Hashtable<String,String> parameters = new Hashtable<String,String>();
		NodeList nodesParameters = mpNode.getElementsByTagName("Parameter");
		for(int i=0; i < nodesParameters.getLength(); i++)
		{
			Element eParameter = (Element)nodesParameters.item(i);
			String strParameterName = eParameter.getAttribute("name");
			CharacterData cdParameter = (CharacterData)eParameter.getFirstChild();
			String strParameterValue = cdParameter.getData();
			parameters.put(strParameterName, strParameterValue);
		}
		
		return ModelParameters.createModelParameters(framework, model, parameters);
	}
	
	
}	