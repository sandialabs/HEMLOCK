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

import gov.sandia.hemlock.evaluation.ModelEvaluationResults;
import gov.sandia.hemlock.data.*;
import javax.xml.stream.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Used to write the results of an experiment.
 */
public class ExperimentWriter
{
	private String fileName;
	private XMLStreamWriter writer;
	
	private boolean experimentStarted = false;
	
	public ExperimentWriter(String experimentName, String fileName)
	{
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		this.fileName = fileName + "_" + experimentName + "." + format.format(now) + ".xml";
	}
	
	public void startExperiment() throws XMLStreamException, IOException
	{
		XMLOutputFactory of = XMLOutputFactory.newInstance();
		File outputFile = new File(fileName);
		this.writer = of.createXMLStreamWriter(new FileWriter(outputFile));
		writer.writeStartDocument();
		writer.writeStartElement("Experiment");
		
		experimentStarted = true;
	}
	
	public void writeKFoldExperiment(ModelEvaluationResults[] results, Hashtable<String,String> summary) throws XMLStreamException
	{
		writer.writeStartElement("KFoldCrossValidation");
		for(int i = 0; i < results.length; i++)
		{
			writeModelEvaluationResults(results[i]);
		}
		
		//also write summary of k-fold experiment here
		writer.writeStartElement("Summary");
		//experimentName
		writer.writeStartElement("experimentName");
		writer.writeCharacters(summary.get("experimentName"));
		writer.writeEndElement();
		//modelType
		writer.writeStartElement("modelType");
		writer.writeCharacters(summary.get("modelType"));
		writer.writeEndElement();
		//dataSetName
		writer.writeStartElement("dataSetName");
		writer.writeCharacters(summary.get("dataSetName"));
		writer.writeEndElement();
		//numberOfFolds
		writer.writeStartElement("numberOfFolds");
		writer.writeCharacters(summary.get("numberOfFolds"));
		writer.writeEndElement();

		writeEvaluationMeasures(summary);

		writer.writeEndElement();  //End Summary
		
		writer.writeEndElement();  //End KFoldCrossValidation
		
	}
	
	public void writeNoHoldOutExperiment(Hashtable<String,String> summary) throws XMLStreamException
	{
		writer.writeStartElement("NoHoldOut");
		
		//also write summary of k-fold experiment here
		writer.writeStartElement("Summary");
		//experimentName
		writer.writeStartElement("experimentName");
		writer.writeCharacters(summary.get("experimentName"));
		writer.writeEndElement();
		//modelType
		writer.writeStartElement("modelType");
		writer.writeCharacters(summary.get("modelType"));
		writer.writeEndElement();
		//dataSetName
		writer.writeStartElement("dataSetName");
		writer.writeCharacters(summary.get("dataSetName"));
		writer.writeEndElement();
		
		writeEvaluationMeasures(summary);
		
		writer.writeEndElement();  //End Summary
		
		writer.writeEndElement();  //End KFoldCrossValidation
		
	}
	
	private void writeEvaluationMeasures(Hashtable<String,String> summary) throws XMLStreamException
	{
		//averageAccuracy
		if(summary.containsKey("accuracy"))
		{
			writer.writeStartElement("accuracy");
			writer.writeCharacters(summary.get("accuracy"));
			writer.writeEndElement();
		}
		//averageAuc
		if(summary.containsKey("AUC"))
		{
			writer.writeStartElement("AUC");
			writer.writeCharacters(summary.get("AUC"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_disagreement"))
		{
			writer.writeStartElement("diversity_disagreement");
			writer.writeCharacters(summary.get("diversity_disagreement"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_correlation"))
		{
			writer.writeStartElement("diversity_correlation");
			writer.writeCharacters(summary.get("diversity_correlation"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_yuleQ"))
		{
			writer.writeStartElement("diversity_yuleQ");
			writer.writeCharacters(summary.get("diversity_yuleQ"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_doubleFault"))
		{
			writer.writeStartElement("diversity_doubleFault");
			writer.writeCharacters(summary.get("diversity_doubleFault"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_entropy"))
		{
			writer.writeStartElement("diversity_entropy");
			writer.writeCharacters(summary.get("diversity_entropy"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_generalDiversity"))
		{
			writer.writeStartElement("diversity_generalDiversity");
			writer.writeCharacters(summary.get("diversity_generalDiversity"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_coincidentFailure"))
		{
			writer.writeStartElement("diversity_coincidentFailure");
			writer.writeCharacters(summary.get("diversity_coincidentFailure"));
			writer.writeEndElement();
		}
		if(summary.containsKey("diversity_difficulty"))
		{
			writer.writeStartElement("diversity_difficulty");
			writer.writeCharacters(summary.get("diversity_difficulty"));
			writer.writeEndElement();
		}
		
	}
	
	private void writeModelEvaluationResults(ModelEvaluationResults results) throws XMLStreamException
	{
		if(experimentStarted)
		{
			writer.writeStartElement("ModelEvaluationResults");
			//print data set name
			writer.writeStartElement("DataSet");
			writer.writeCharacters(results.dataSetInfo.dataSetName);
			writer.writeEndElement();
			//print model type and parameters
			writer.writeStartElement("ModelType");
			writer.writeCharacters(results.modelType.name());
			writer.writeEndElement();
			//print accuracy
			writer.writeStartElement("Accuracy");
			writer.writeCharacters(Double.toString(results.accuracy));
			writer.writeEndElement();
			//print confusion matrix
			writer.writeStartElement("ConfusionMatrix");
			writer.writeCharacters(results.confusionMatrix.toString());
			writer.writeEndElement();
			//print ROC Curve
			if(results.calculated_ROCCurve)
			{
				writer.writeStartElement("ROCCurve");
				writer.writeStartElement("XCoords");
				writer.writeCharacters(Arrays.toString(results.rocXCoordinates));
				writer.writeEndElement();
				writer.writeStartElement("YCoords");
				writer.writeCharacters(Arrays.toString(results.rocYCoordinates));
				writer.writeEndElement();
				writer.writeEndElement();
				writer.writeStartElement("AUC");
				String arrString = Double.toString(results.auc);
				writer.writeCharacters(arrString.replace(" ", ","));
				writer.writeEndElement();
			}
			//print diversity measurements
			if(results.calculated_disagreement)
			{
				writer.writeStartElement("diversity_disagreement");
				writer.writeCharacters(Double.toString(results.diversity_disagreement));
				writer.writeEndElement();
			}
			if(results.calculated_correlation)
			{
				writer.writeStartElement("diversity_correlation");
				writer.writeCharacters(Double.toString(results.diversity_correlation));
				writer.writeEndElement();
			}
			if(results.calculated_yuleQ)
			{
				writer.writeStartElement("diversity_yuleQ");
				writer.writeCharacters(Double.toString(results.diversity_yuleQ));
				writer.writeEndElement();
			}
			if(results.calculated_doubleFault)
			{
				writer.writeStartElement("diversity_doubleFault");
				writer.writeCharacters(Double.toString(results.diversity_doubleFault));
				writer.writeEndElement();
			}
			if(results.calculated_entropy)
			{
				writer.writeStartElement("diversity_entropy");
				writer.writeCharacters(Double.toString(results.diversity_entropy));
				writer.writeEndElement();
			}
			if(results.calculated_generalDiversity)
			{
				writer.writeStartElement("diversity_generalDiversity");
				writer.writeCharacters(Double.toString(results.diversity_generalDiversity));
				writer.writeEndElement();
			}
			if(results.calculated_coincidentFailure)
			{
				writer.writeStartElement("diversity_coincidentFailure");
				writer.writeCharacters(Double.toString(results.diversity_coincidentFailure));
				writer.writeEndElement();
			}
			if(results.calculated_difficulty)
			{
				writer.writeStartElement("diversity_difficulty");
				writer.writeCharacters(Double.toString(results.diversity_difficulty));
				writer.writeEndElement();
			}
			
			
			//End the the ModelEvaluationResults
			writer.writeEndElement();	
		}
	}
	
	public void stopExperiment() throws XMLStreamException
	{
		writer.writeEndElement();
		writer.writeEndDocument();
		writer.close();
		
		experimentStarted = false;
	}
}
