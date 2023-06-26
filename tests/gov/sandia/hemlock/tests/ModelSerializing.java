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

package gov.sandia.hemlock.tests;

import gov.sandia.hemlock.data.*;
import gov.sandia.hemlock.classification.*;
import gov.sandia.hemlock.classification.parameters.*;
import gov.sandia.hemlock.core.*;
import org.junit.*;
import java.io.*;

public class ModelSerializing extends ExperimentTest
{
	@Test 
	public void canSaveAndLoad() throws Exception
	{
		DataSetInfo[] dsi = DataSetInfoEvaluator.getInfoForAllDataSets(); 
		DataImporter importer = new DataImporter();
		DataSet dataSet = importer.importDataSet(dsi[0]);
		 
		RandomTreeParameters params = new RandomTreeParameters(FrameworkType.weka);

		//Create model
		ClassifierFactory cf = new ClassifierFactory(params.frameworkType, dataSet);
		Model model = cf.createModel(params);
		
		//serialize model
		File directory = new File("tests/experiments/results/ModelSerialization/");
		directory.mkdirs();
		ModelSerialization.serializeModel(model,"tests/experiments/results/ModelSerialization/rt.model");
		
		//load model
		Model loaded = ModelSerialization.loadModel("tests/experiments/results/ModelSerialization/rt.model");
		
		
	}
	
	@Test
	public void serializeKFoldeBaseClassifiers() throws Exception
	{
		String inputFileName = "tests/experiments/ModelSerialization/kFoldSerializeBaseClassifiers";
		String outputFileName = "tests/experiments/results/ModelSerialization/ModelSerialization";
		runExperimentFile(inputFileName, outputFileName);
		
		/*isOutputFileCorrect("tests/experiments/results/ModelSerialization", 
			"ModelSerialization_hetero_top66_SumRuleSerialize.", 
			"tests/correct-outputs/ModelSerialization_hetero_top66_SumRuleSerialize.20090701121955.xml");
			*/
	}
	
	@Test
	public void loadKFoldeBaseClassifiers() throws Exception
	{
		String inputFileName = "tests/experiments/ModelSerialization/kFoldLoadBaseClassifiers";
		String outputFileName = "tests/experiments/results/ModelSerialization/ModelSerialization";
		runExperimentFile(inputFileName, outputFileName);
		
		/*isOutputFileCorrect("tests/experiments/results/ModelSerialization", 
			"ModelSerialization_hetero_top66_SumRuleLoad.", 
			"tests/correct-outputs/ModelSerialization_hetero_top66_SumRuleLoad.20090701121958.xml");
			*/
	}
	
	@Test
	public void serializeKFoldeBaseClassifiersBagging() throws Exception
	{
		String inputFileName = "tests/experiments/ModelSerialization/kFoldSerializeBaseClassifiersBagging";
		String outputFileName = "tests/experiments/results/ModelSerialization/ModelSerialization";
		runExperimentFile(inputFileName, outputFileName);
		
		/*isOutputFileCorrect("tests/experiments/results/ModelSerialization", 
			"ModelSerialization_hetero_top66_SumRuleSerialize.", 
			"tests/correct-outputs/ModelSerialization_hetero_top66_SumRuleSerialize.20090701121955.xml");
		*/
	}
	
	@Test
	public void loadKFoldeBaseClassifiersBagging() throws Exception
	{
		String inputFileName = "tests/experiments/ModelSerialization/kFoldLoadBaseClassifiersBagging";
		String outputFileName = "tests/experiments/results/ModelSerialization/ModelSerialization";
		runExperimentFile(inputFileName, outputFileName);
		
		/*isOutputFileCorrect("tests/experiments/results/ModelSerialization", 
			"ModelSerialization_hetero_top66_SumRuleLoad.", 
			"tests/correct-outputs/ModelSerialization_hetero_top66_SumRuleLoad.20090701121958.xml");
		*/
	}

	//TODO: Add test to compare results from serialize and load experiments
	//They should be the same.  I've done this manually to make sure it works.
	
	@BeforeClass
	public static void clean() throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		rt.exec("rm -rf tests/experiments/results/ModelSerialization", null, new File("."));
	}
}


