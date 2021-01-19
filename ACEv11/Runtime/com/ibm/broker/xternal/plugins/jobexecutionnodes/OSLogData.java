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
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbService;


public class OSLogData
{
	  private static String className = "OSLogData";
      public static final     int   SYSLOG_INFO       = 0 ;
      public static final     int   SYSLOG_WARN       = 1 ;
      public static final     int   SYSLOG_ERROR      = 2 ;
      private static logData ld =  null;
      static final String resource = "com.ibm.broker.xternal.plugins.jobexecutionnodes.Syslog";
      static
      {
      	ld = logData.getInstance();//Initialize the logger only once.      	
      }

      /*
       * Write an message to the operating system log (Unix - syslog / Windows - Eventlog)
       * Object object: The object that caused the log write
       * String method: The method that caused the log write
       * String key: Key to the cache file
       * int level: Level of log, either info, warn or error
       * String text: message inserts.
       */
      public static void syslog(String objectClassName, String method,int level, String key, String insert )
      {

      	String methodName = "syslog";
            try 
	   		 {
                  switch (level)
                  {
			        case SYSLOG_INFO :
		        		ld.logData_Debug(" "+className+"."+methodName+" logging INFOrmation into OS LOG with key "+key);
		        		MbService.logInformation(objectClassName,method,resource,key,insert,null);
			            		break;
                    case SYSLOG_WARN :
   		        		ld.logData_Debug(" "+className+"."+methodName+" logging WARNing into OS LOG with key "+key);
   		        		MbService.logWarning(objectClassName,method,resource,key,insert,null);
   		        		break;
                    case SYSLOG_ERROR :
   		        		ld.logData_Debug(" "+className+"."+methodName+" logging ERROR into OS LOG with key "+key);
   		        		MbService.logError(objectClassName,method,resource,key,insert,null);
                        break;
                    default :
   		        		ld.logData_Debug(" "+className+"."+methodName+" YOU SHOULD NEVER SEE THIS MESSAGE.");
                         break;
                  }
            }
			catch(MbException mbe)
			{
				ld.logData_Error(" "+className+"."+methodName+" Unexpected MbException while logging into OS Log with "+key,mbe);	
			}
            catch (Exception e) 
            { 
       			ld.logData_Error(" "+className+"."+methodName+" Unexpected Exception while logging into OS Log with "+key,e);
 	    	}
      }
}