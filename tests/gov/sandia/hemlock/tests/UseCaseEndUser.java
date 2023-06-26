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

import java.io.*;
import java.util.*;
import org.junit.Test;
import org.junit.Assert;

//These tests are to be run from Hemlock base directory.
public class UseCaseEndUser
{
	@Test 
	public void runExperimentFile_DataRepository() throws Exception
	{

		Runtime rt = Runtime.getRuntime();
		rt.exec("ant");			//compile
		//rt.exec("ant", new String[]{"package"});		//package
		Process p0 = rt.exec("ant package");
		p0.waitFor();
		//copy package somewhere
		Process p1 = rt.exec("mv hemlock.zip ../");
		p1.waitFor();
		//extract
		Process p2 = rt.exec("unzip hemlock.zip", null, new File(".."));
		p2.waitFor();
		//copy weka
		Process p3a = rt.exec("mkdir tpl", null, new File("../hemlock")); 
		p3a.waitFor();
		Process p3 = rt.exec("cp tests/external-libraries/weka.jar ../hemlock/tpl/weka.jar");
		p3.waitFor();
		//run experiment
		Process p4 = rt.exec("runHemlock example/optimize_KNN example/out/test",
			null,
			new File("../hemlock/"));
		p4.waitFor();
		
		//compare output 
		File correct_Dir = new File("tests/correct-outputs/");
		File correct = correct_Dir.listFiles(new FilenameFilter() {public boolean accept(File f, String s) { return s.startsWith("test_optimize_KNN_0"); }})[0];
		
		File actual_Dir = new File("../hemlock/example/out/");
		File actual = actual_Dir.listFiles(new FilenameFilter() {public boolean accept(File f, String s) { return s.startsWith("test_optimize_KNN_0"); }})[0];
		
		FileReader fr_c = new FileReader(correct);
		ArrayList<Character> ar_c = new ArrayList<Character>();
		while(fr_c.ready())
			ar_c.add((char)fr_c.read());
		fr_c.close();
		
		FileReader fr_a = new FileReader(actual);
		ArrayList<Character> ar_a = new ArrayList<Character>();
		while(fr_a.ready())
			ar_a.add((char)fr_a.read());
		fr_a.close();
		
		Assert.assertArrayEquals(ar_c.toArray(), ar_a.toArray());

		//delete package and extracted package
		rt.exec("rm -rf hemlock", null, new File(".."));
		rt.exec("rm hemlock.zip", null, new File(".."));

	}
	

}
