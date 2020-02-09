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
import java.util.*;

/*
 * This class provide a consistent way of accessing translated data.
 */

public class ResourceManager

{
   static ResourceBundle oRB = null;
   private static logData ld =  null;

 /*
 * Constructor retrieves the resource bundle.
 */
  static 
  {
    oRB = ResourceBundle.getBundle("com.ibm.broker.xternal.plugins.jobexecutionnodes.Syslog");
    ld = logData.getInstance();
  }

/*
 * Return the translated string corresponding to the key.
 */
  static String getString(String key)
  {
    try
    {
		String keyMessage = oRB.getString(key);
		ld.logData_Debug(" ResourceManager :-> The messageString for the messageKey "+key+" is "+keyMessage);
      return keyMessage;
    }
    catch (MissingResourceException e)
    {
     	ld.logData_Error(" ResourceManage :-> **** Missing Resource **** [ "+key+" ]",e);
      return "Couldnot Locate Resource Key [ " + key + " ]";
    }
  }
}
