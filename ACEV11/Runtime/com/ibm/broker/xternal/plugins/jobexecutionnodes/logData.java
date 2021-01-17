/*
Copyright 2015 IBM Corporation 
Author: Amar Shah
 
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the MIT License
  which accompanies this distribution, and is available at
  http://opensource.org/licenses/MIT
 
  Contributors:
      Amar Shah - initial implementation 
*/
package com.ibm.broker.xternal.plugins.jobexecutionnodes;

import java.io.*;
import java.util.Calendar;

public class logData
{
 //private String className = "logData";
 public static final int LOGDATA_LEVEL_NONE=0; /*no trace file created*/
 public static final int LOGDATA_LEVEL_ERROR=1; /*trace logs only errors*/
 public static final int LOGDATA_LEVEL_INFO=2; /*trace logs errors and some basic info of broker transaction*/
 public static final int LOGDATA_LEVEL_DEBUG=3; /*trace logs error,info,debug*/

 private static int instancesCnt = 0;/*to check the number if instances of the object*/
 private int logLevel; /*indicates NONE,INFO,ERROR,DEBUG*/
 private BufferedWriter logFileWriter = null;

 public static logData ld = null;
 private static boolean initialized = false;
 
 private logData()
 {
   super();
   setlogDataLevel("NONE");
   try
   {
   	String osname = System.getProperty("os.name");
   	if (osname.toUpperCase().indexOf("WINDOWS") != -1){
   		logFileWriter = new BufferedWriter(new FileWriter("JobExecutionNodeLogData.txt",true));
   	}else{
   		logFileWriter = new BufferedWriter(new FileWriter("/tmp/JobExecutionNodeLogData.txt",true));
   	}
   }
   catch(IOException ioe) 
   {
	logData_Error(" IOException occurred while initializing the logFile ",ioe);
   }
 }

/*Return the one and only one instance of the file logger*/
 public static logData getInstance()
 {
 	instancesCnt++;
	if(!initialized)
	 	{
			ld = new logData(); //Initialize the object for user trace
			initialized = true;
		}
	return ld;
 }
 
/*Close the instance of the file logger*/
 public void closeInstance() 
 {
 	instancesCnt--;
 	if(instancesCnt == 0)
 	{
 		try
    	{
    		logFileWriter.close();	
    		initialized = false;
    	}
   		catch(IOException ioe) 
	   {
		logData_Error(" IOException occurred while closing the logFile ",ioe);
   	}
   }
 }
 /*Return the logLevel*/
 public int getlogDataLevel()
 {
   return logLevel;
 }
 /*set the logLevel*/
 public void setlogDataLevel(String Level)
 {
	try
	{ 	
   		logLevel = getlogDataLevelValue(Level);
   }catch(Exception e)
	{
   }
 }
/*Various logging methos...INFO, ERROR, DEBUG..under normal situations*/ 
public void logData_Info(Object message) 
 {
 	if (logLevel >= LOGDATA_LEVEL_INFO)
   		logDataPrint(message,null);
 }
 
 public void logData_Debug(Object message) 
 {
 	if (logLevel >= LOGDATA_LEVEL_DEBUG)
  		logDataPrint(message,null);
 }
 
 public void logData_Error(Object message)
 {
 	if (logLevel >= LOGDATA_LEVEL_ERROR)
  		logDataPrint(message,null);
 }
 /*Various logging methods...INFO, ERROR, DEBUG under exception situations*/ 
 public void logData_Info(Object message, Throwable exception) 
 {
 	if (logLevel >= LOGDATA_LEVEL_INFO)
   		logDataPrint(message,exception);
 }
 
 public void logData_Debug(Object message, Throwable exception)
 {
 	if (logLevel >= LOGDATA_LEVEL_DEBUG)
  		logDataPrint(message,exception);
 }
 
 public void logData_Error(Object message, Throwable exception) 
 {
 	if (logLevel >= LOGDATA_LEVEL_ERROR)
  		logDataPrint(message,exception);
 }

 /*This is the actual method that writes into the file */
 private void logDataPrint(Object message, Throwable exception)
 {
  /*This shd. write into file...*/
  if (logLevel == LOGDATA_LEVEL_NONE)  
  	return;

   	StringBuffer bf = new StringBuffer();
   	String threadTime = getThreadTime();
	bf.append("[ "+getlogDataLevelInfo(logLevel)+" ]");
	bf.append(" "+threadTime+" ");
	if (message != null) 
		bf.append(message.toString()+" ");
	if (exception != null)
		bf.append(getExceptionString(exception));  	
	if(logFileWriter != null)
	{
	  try
	  {
		logFileWriter.write(bf.toString(),0,bf.length());
		logFileWriter.newLine();
		logFileWriter.flush();
	
	  }
	  catch(IOException ioe)
	  {
		  System.err.println();
	  }
	  catch(Exception e)
	  {
		  System.err.println();
	  }
	}
	else { 
		System.err.println();
	}
 }
/*get the name and time of the thead for logging*/

 private synchronized String getThreadTime() 
 {
   return 	"Thread : "+Thread.currentThread().getName()+" "+((Calendar.getInstance()).getTime()).toString();
 }
 /*Extract the Exception into a string format*/
 public String getExceptionString(Throwable excp)
 {
	

	StringWriter sw = new StringWriter();
	
	PrintWriter pw = new PrintWriter(sw);
	
	excp.printStackTrace(pw);
	
	return sw.getBuffer().toString();
 }
 /*Receive logLevel in String format*/
 private String getlogDataLevelInfo(int level)
 {
   String s = "";
   switch (level)
   {
    case LOGDATA_LEVEL_NONE:   
    				s = "NONE";
				break;
    case LOGDATA_LEVEL_INFO:   
    				s = "INFO ";
				break;
    case LOGDATA_LEVEL_DEBUG:   
    				s = "DEBUG";
				break;
    case LOGDATA_LEVEL_ERROR:   
    				s = "ERROR";
				break;
   }
   return s;  
 }
 /*Receive logLEvel in int format*/
 private int getlogDataLevelValue(String level)
 {
 	int logDataLevel = 0;
   	
	if(level.equals("DEBUG")) 
		logDataLevel = LOGDATA_LEVEL_DEBUG;
	else if(level.equals("INFO")) 
		logDataLevel = LOGDATA_LEVEL_INFO;
	else if(level.equals("ERROR"))
		logDataLevel = LOGDATA_LEVEL_ERROR;
	else logDataLevel = LOGDATA_LEVEL_NONE;
	
	return logDataLevel;
 }

}
