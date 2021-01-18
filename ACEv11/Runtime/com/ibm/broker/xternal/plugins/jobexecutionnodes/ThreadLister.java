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

public class ThreadLister 
{
	 logData logger;
	
	public ThreadLister(logData l) 
	{
		logger = l;
		
	}
	
	public ThreadLister()
	{
	}
	
 void  printThreadInfo(Thread t) 
{
	    if (t == null)
	      return;
	   logger.logData_Debug("Thread Name is " + t.getName());	    
	    logger.logData_Debug("Thread Priority is " + t.getPriority());	 
	    if(t.isAlive())
	    {
	    	logger.logData_Debug("Thread Status is  :----  Alive");
	    }
	    else 
	    {
	    	logger.logData_Debug("Thread Status is  :----  Not Alive");
	    }
}

	  /** Display info about a thread group */
 void printGroupInfo(ThreadGroup g) 
{
	    if (g == null)
	      return;
	    
	    int numThreads = g.activeCount();
	    int numGroups = g.activeGroupCount();
	    Thread[] threads = new Thread[numThreads];
	    ThreadGroup[] groups = new ThreadGroup[numGroups];

	    g.enumerate(threads, false);
	    g.enumerate(groups, false);
	    logger.logData_Debug("----------------------------");
	    logger.logData_Debug("");
	 logger.logData_Debug( "Thread Group: " + g.getName()+ "  Max Priority: " + g.getMaxPriority() + (g.isDaemon() ? " Daemon" : ""));

	    for (int i = 0; i < numThreads; i++)
	      printThreadInfo(threads[i]);
	    for (int i = 0; i < numGroups; i++)
	      printGroupInfo(groups[i]);
}

	  /** Find the root thread group and list it recursively */
 void listAllThreads() 
 {
	    ThreadGroup currentThreadGroup;
	    ThreadGroup rootThreadGroup;
	    ThreadGroup parent;

	    // Get the current thread group
	    currentThreadGroup = Thread.currentThread().getThreadGroup();

	    // Now go find the root thread group
	    rootThreadGroup = currentThreadGroup;
	    parent = rootThreadGroup.getParent();
	    while (parent != null) 
	    {
	      rootThreadGroup = parent;
	      parent = parent.getParent();
	    }

	    printGroupInfo(rootThreadGroup);
  }

	  public static void main(String[] args)
	  {
	   // ThreadLister t = new ThreadLister();
	   // t.listAllThreads();
	  }
}
