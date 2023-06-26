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

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.w3c.dom.*;

public class MetaExperiment
{
	private String[] parameterNames;
	private String[][] parameterValues;
	private int numValues[];
	private int combinations = 1;
	
	private Document doc;
	
	public void createExperiments(String metaExperimentFile, String outputFile) throws Exception
	{
		//read file
		File file = new File(metaExperimentFile);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		doc = builder.parse(file);
		Element root = (Element)doc.getElementsByTagName("Experiments").item(0);
		Element experiment = (Element)root.getElementsByTagName("Experiment").item(0);
		Element modelParameters = 
			(Element)experiment.getElementsByTagName("ModelParameters").item(0);
		NodeList parameters = modelParameters.getElementsByTagName("Parameter");
		
		//generate model for parameters
		parameterValues = new String[parameters.getLength()][];
		parameterNames = new String[parameters.getLength()];
		numValues = new int[parameters.getLength()];
		for(int i = parameterValues.length-1; i >= 0 ; i--)
		{
			parameterNames[i] = getParamName((Element)parameters.item(i));
			parameterValues[i] = getParamValues((Element)parameters.item(i));
			numValues[i] = parameterValues[i].length;
			combinations *= numValues[i];
			modelParameters.removeChild(parameters.item(i));
		}
		
		
		//replicate experiment and insert parameters
		for(int i = 0; i < combinations; i++)
		{
			Element newExperiment = (Element)experiment.cloneNode(true);
			insertParameters(newExperiment, i);
			root.appendChild(newExperiment);
		}
		root.removeChild(experiment);
		
		writeToFile(outputFile);
	}
	
	private void writeToFile(String fileName) throws Exception
	{
		File outputFile = new File(fileName);
		StreamResult result = new StreamResult(outputFile);
		DOMSource source = new DOMSource(doc);
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);
	}
	
	
	private void insertParameters(Element experiment, int repNum)
	{	
		//rename experiment
		String expName = experiment.getAttribute("ID");
		experiment.setAttribute("ID", expName + "_" + repNum);
		
		//calculate indexes
		int soFar = 1;
		int[] indexes = new int[numValues.length];
		for(int i = 0; i < numValues.length; i++)
		{
			indexes[i] = (repNum % (numValues[i]*soFar))/soFar;
			repNum = repNum - indexes[i]*soFar;
			soFar = soFar * numValues[i];
		}
		
		//create parameters for set of indexes
		Element[] params = createSetOfParameters(indexes);
			
		//insert parameters
		Element modelParameters = 
			(Element)experiment.getElementsByTagName("ModelParameters").item(0);
		for(int i = 0; i < params.length; i++)
			modelParameters.appendChild(params[i]);
		
	}
	
	private Element[] createSetOfParameters(int[] indexes)
	{
		Element[] params = new Element[parameterNames.length];
		for(int i = 0; i < parameterNames.length; i++)
		{
			Element p = doc.createElement("Parameter");
			p.setAttribute("name", parameterNames[i]);
			p.appendChild(doc.createTextNode(parameterValues[i][indexes[i]]));
			params[i] = p;
		}
		
		return params;
	}
	
	private String getParamName(Element param)
	{
		return param.getAttribute("name");
	}
	
	private String[] getParamValues(Element param)
	{
		NodeList nlRange = param.getElementsByTagName("Range");
		NodeList nlValues = param.getElementsByTagName("Values");

		String[] values;
		if(nlRange.getLength() > 0)
		{
			values = getParamRangeValues(nlRange);
		}
		else if(nlValues.getLength() > 0)
		{
			values = getParamSpecifiedValues(nlValues);
		}
		else
		{
			values = getParamValue(param);
		}
		
		return values;
	}
	
	private String[] getParamSpecifiedValues(NodeList nlValues)
	{
		String[] values;

		Element eValues = (Element)nlValues.item(0);
		NodeList nlValue = eValues.getElementsByTagName("Value");
		values = new String[nlValue.getLength()];
		for(int i = 0; i < nlValue.getLength(); i++)
		{
			Element eValue = (Element)nlValue.item(i);
			CharacterData cdValue = (CharacterData)eValue.getFirstChild();
			values[i] = cdValue.getData();
		}
	
		return values;
	}
	
	private String[] getParamRangeValues(NodeList nlRange)
	{
		String[] values;

		Element range = (Element)nlRange.item(0);
		Element eBegin = (Element)range.getElementsByTagName("Begin").item(0);
		CharacterData cdBegin = (CharacterData)eBegin.getChildNodes().item(0);
		double begin = Double.parseDouble(cdBegin.getData());
		
		Element eEnd = (Element)range.getElementsByTagName("End").item(0);
		CharacterData cdEnd = (CharacterData)eEnd.getChildNodes().item(0);
		double end = Double.parseDouble(cdEnd.getData());
		
		Element eIncrement = (Element)range.getElementsByTagName("Increment").item(0);
		CharacterData cdIncrement = (CharacterData)eIncrement.getChildNodes().item(0);
		double increment = Double.parseDouble(cdIncrement.getData());
		
		int length = (int)( 1 + (end-begin)/increment);
		values = new String[length];
		for(int i=0; (i*increment + begin) <= end; i++ )
			values[i] = Double.toString(i*increment + begin);
		
		return values;	
	}
	
	private String[] getParamValue(Element param)
	{
		String[] values = new String[1];
		CharacterData cd = (CharacterData)param.getFirstChild();
		values[0] = cd.getData();
		return values;
	}
}
