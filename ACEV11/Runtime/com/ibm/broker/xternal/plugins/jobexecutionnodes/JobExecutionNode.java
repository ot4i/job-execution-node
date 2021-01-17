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


import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ibm.broker.plugin.MbBrokerException;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbInputTerminal;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbNode;
import com.ibm.broker.plugin.MbNodeInterface;
import com.ibm.broker.plugin.MbOutputTerminal;

public class JobExecutionNode extends MbNode implements MbNodeInterface 
{
	private String className = "JobExecutionNode";
	private MbOutputTerminal outTerminal = null;
	private MbOutputTerminal failureTerminal = null;
	private MbInputTerminal inputTerminal = null;
	private static final String OUTPUT_TERMINAL = "out";
	private static final String FAILURE_TERMINAL = "failure";
	private static final String INPUT_TERMINAL = "in";
	
	/* Define the properties for the node. */
	private String jobType = "COMMANDLINE";
	static String jobLocation = "";
	private int logLevel=0;/* Define the properties for 'Tracing' */
	private String resultFile = "";
	private String commandSource = "MESSAGE";
	private String propagateResult = "NO";
	static String resultToAdd = "";
	static String outputDefaultFile = "";
	static boolean isDefaultoutFileNull = true;
	
	// locally used variables
	private String outputResults = "";
	private String errorResults = "";
	private boolean initialized = false; 
	Vector jobExecutionThreads;
	static int count = 0;
	private logData logger = null; /* to log the information into a file */
	ThreadLister ll = null;

	public JobExecutionNode() throws MbException 
	{
		super();
		logger = logData.getInstance();
		setInputTerminal(createInputTerminal(INPUT_TERMINAL));
		setOutputTerminal(createOutputTerminal(OUTPUT_TERMINAL));
		setFailureTerminal(createOutputTerminal(FAILURE_TERMINAL));
		jobExecutionThreads = new Vector();

	}
	
	public void evaluate(MbMessageAssembly inassembly, MbInputTerminal arg1) throws MbException 
	{
		String methodName = "evaluate";
		setLogLevel(getLogLevel());
		logger.logData_Debug(" { " + className + "." + methodName);
		logger.logData_Debug("ONE MORE MESSAGE RECEIVED");
		MbMessage msg = null;
		try 
		{
			msg = new MbMessage(inassembly.getMessage());
			if (getCommandSource().trim().toUpperCase().equals("MESSAGE")){ 
				if(!executeCommandfromMessage(msg)){
					throw new commandTagNotInMessage("Message Doesn't contain any command for execution");
				}
			}
			else {
				executeCommandFromProperty(msg);
			}
			Vector completedJobs = checkAllThreads();
			propogateCompletedJobs(inassembly, completedJobs);
			deleteCompletedJobs(completedJobs);
		} catch (IOException e) 
		{	OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,"INTERNAL_UNKNOWN_ERROR", null);
		logger.logData_Error("Some unknows Error occured ");		
			e.printStackTrace();
		} catch (InterruptedException e) 
		{	OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,"INTERNAL_UNKNOWN_ERROR", null);
		logger.logData_Error("Some unknows Error occured ");		
			e.printStackTrace();
		} catch (commandTagNotInMessage e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    getOutputTerminal().propagate(inassembly);
		
		} 
		finally
		{
			printAllThreadsStatus();
		    if(msg != null)
		        msg.clearMessage();
		    logger.logData_Debug(" } " + className + "." + methodName);
		}
	}
	
	/*Returns a vector containing indices of threads which have been executed
	 * If suppose we have 10 jobs out of which 3,6and 8 are completed then this
	 * function will return 3,6,8
	 */
	private Vector checkAllThreads() 
	{
		String methodName = "checkAllThreads";
		logger.logData_Debug(" { " + className + "." + methodName);
	//	int num = 0;
		/* this vector contains indices of all the jobs which have been completed */
		Vector completedjobsIndices = new Vector();
		for (int i = 0; i < jobExecutionThreads.size(); i++) 
		{
			//Checks if the ith Job in execution has been completed
			if (!(((Execute) jobExecutionThreads.elementAt(i)).isAlive())) 
			{
				completedjobsIndices.addElement(Integer.toString(i));
			}
		}
		logger.logData_Debug(" } " + className + "." + methodName);
		return completedjobsIndices;
	}
	
	PropertyData returnpros(MbMessage msg) throws MbException{
		String methodName = "returnpros";
		logger.logData_Debug(" { " + className + "." + methodName);
		/*We get parameters which are required to store message in byte[] format as below*/
		MbElement rootElement = msg.getRootElement();
		MbElement messageType = rootElement.getFirstElementByPath("Properties/MessageType");
		String messagetype = messageType.getValue().toString();
		MbElement messageFormat = rootElement.getFirstElementByPath("Properties/MessageFormat");
		String messageformat = messageFormat.getValue().toString();
		MbElement messageSet = rootElement.getFirstElementByPath("Properties/MessageSet");
		String messageset = messageSet.getValue().toString();
		MbElement Encoing = rootElement.getFirstElementByPath("Properties/Encoding");
		int incoding = Integer.parseInt(Encoing.getValue().toString());
		MbElement Ccsid = rootElement.getFirstElementByPath("Properties/CodedCharSetId");
		int ccsid = Integer.parseInt(Ccsid.getValue().toString());
				
		/* message is converted to byte[] as below*/
		byte[] msgbytes = rootElement.getLastChild().toBitstream(messagetype,messageset, messageformat, incoding, ccsid, 0);
		PropertyData pdata = new PropertyData(messagetype, messageset,messageformat, incoding, ccsid,msgbytes);
		logger.logData_Debug(" } " + className + "." + methodName);
		return pdata;
	}
	
	/* This starts execution of Jobs which are inputted from message */
	boolean executeCommandfromMessage(MbMessage msg) throws IOException, InterruptedException, MbException 
	{
		String methodName = "executeCommandfromMessage";
		logger.logData_Debug(" { " + className + "." + methodName);
	
		PropertyData pdata = returnpros(msg);

		logger.logData_Debug("message was  " + new String(pdata.getMessage()));
		List commands = (List) msg.evaluateXPath("/message/command");
        List resultFilename =(List) msg.evaluateXPath("/message/filename");
        
		if(commands==null || commands.size() == 0){
			logger.logData_Debug("No Command found in message");
			logger.logData_Debug(" } " + className + "." + methodName);
			return false;
		}
		
		logger.logData_Debug("Command found in message");
		
		if(resultFilename!=null && resultFilename.size() > 0){
			String command = ((MbElement)commands.get(0)).getValueAsString();
			String fileName = ((MbElement)resultFilename.get(0)).getValueAsString();
			
			logger.logData_Debug("We found a command in message"+ command);
			logger.logData_Debug("We found a filename in message"+ fileName);
			
		    if (fileName!=null)
		        setResultFile(fileName);				
		    else 
		        setResultFile(outputDefaultFile);
			
		    setJobLocation(command);
		    logger.logData_Debug("Job Executer is about to be called for message commands");
		    
			/* A new object for current job is created and added to the jobExecutionThreads here */
			
			Execute currentJob = new Execute(jobType, jobLocation,true, getResultFile(), pdata, logger, count++);
			currentJob.start();
			//experimental
			//if(orderIsToBeEnsured)
			String propMsg = getPropagateResult();
			if(propMsg.equals("YES")){
				currentJob.join();
			}
			jobExecutionThreads.addElement(currentJob);
			
			logger.logData_Debug("New thread for command " + getJobLocation());
			logger.logData_Debug("Current Size of vector is   "	+ jobExecutionThreads.size());		    
		}
		
		
		
		for (int index = 0; index < commands.size(); index++) 
		{
			MbElement cmd = (MbElement) commands.get(index);
			String command = cmd.getValueAsString();
			MbElement fileElement = cmd.getLastChild();
			if(fileElement != null)
			{
				String tagName = null;
				if(resultFilename!=null && resultFilename.size() > 0)
					tagName =((MbElement)resultFilename.get(0)).getValueAsString(); 
				//if filename is specified with command then send output to it else to default file
				    if (tagName.toUpperCase().equals("FILENAME"))
				        setResultFile(fileElement.getValue().toString());				
				    else 
				        setResultFile(outputDefaultFile);
			}
			setJobLocation(command);
			logger.logData_Debug("Job Executer is about to be called for message commands");
			/* A new object for current job is created and added to the jobExecutionThreads here */
			
			Execute currentJob = new Execute(jobType, jobLocation,true, getResultFile(), pdata, logger, count++);
			currentJob.start();
			
			String propMsg = getPropagateResult();
			if(propMsg.equals("YES")){
			currentJob.join();
			}
			jobExecutionThreads.addElement(currentJob);
			
			logger.logData_Debug("New thread for command " + getJobLocation());
			logger.logData_Debug("Current Size of vector is   "	+ jobExecutionThreads.size());
		
		}
		logger.logData_Debug(" } " + className + "." + methodName);
		return true;
	}
	
	/*
	 * This starts execution of Jobs which are inputted from Node property
	 *  
	 */
	void executeCommandFromProperty(MbMessage msg) throws IOException,InterruptedException, MbException 
	{
		String methodName = "executeCommandFromProperty";
		logger.logData_Debug(" { " + className + "." + methodName);
		
		String commandlist = getJobLocation();
		logger.logData_Debug("New  command " + commandlist);
		/* More than one commands are specified in Node property separated by and as below we parse them one by one	 */
		StringTokenizer st = new StringTokenizer(commandlist, ",");
		int totalCommands = st.countTokens();
		String commandArray[] = new String[totalCommands];
		logger.logData_Debug("New  command " + commandArray[0]);
		int l = 0;
		while (st.hasMoreTokens()) 
		{
			commandArray[l] = st.nextToken().trim();
			l++;
		}
		
		for (int m = 0; m < commandArray.length; m++) 
		{
			
			PropertyData pdata = returnpros(msg);
			Execute currentJob = new Execute(jobType,commandArray[m], false, getResultFile(), pdata, logger, count++);
			currentJob.start();
			String propMsg = getPropagateResult();
			if(propMsg.equals("YES")){
			currentJob.join();
			}
			jobExecutionThreads.addElement(currentJob);
			
			logger.logData_Debug("New thread for command " + getJobLocation());
			logger.logData_Debug("Current Size of vector is   "	+ jobExecutionThreads.size());
		}
		logger.logData_Debug(" } " + className + "." + methodName);
	}
	
	/* This propagates Message for each all the Jobs which have been completed */
	
	void propogateCompletedJobs(MbMessageAssembly inassembly, Vector v)	throws MbException 
	{
		String methodName = "propogateCompletedJobs";
		
		logger.logData_Debug(" { " + className + "." + methodName);
		String propMsg = getPropagateResult();
		if(propMsg.equals("YES")){
			
		for (int i = 0; i < v.size(); i++) 
		{
			/*id the is the index of the element in jobExecutionThreads which has been completed*/
			int id = Integer.parseInt(v.elementAt(i).toString());
			Execute tmp = (Execute) jobExecutionThreads.elementAt(id);
			logger.logData_Debug("Currently JobNumber " + tmp.getNum()	+ "is being propogated ");
			
			MbMessage mg1 = new MbMessage(inassembly.getMessage());
			MbElement msgBody = mg1.getRootElement().getLastChild();
			msgBody.detach();
			msgBody = mg1.getRootElement().createElementAsLastChildFromBitstream(tmp.getProps().getMessage(), "XML",tmp.getProps().getMessageType(), 
			tmp.getProps().getMessageSet(), tmp.getProps().getMessageFormat(), tmp.getProps().getEncoding(),
			tmp.getProps().getCCSID(), 0);
	
			MbElement rootElement1 = mg1.getRootElement();
			/* If the command was input from message then we dont have to create new tags for that command so we edit MbMessafe as below */
			
			if (tmp.sourceIsMessage())
			{
				/*If command has to be taken from input messsage instead of node properties*/
				List commands = (List) mg1.evaluateXPath("/message/command");
				for (int index = 0; index < commands.size(); index++) 
				{
					MbElement cmd = (MbElement) commands.get(index);
					String command = cmd.getValue().toString();
					String tagName = cmd.getName().toString();
					
					if (tagName.toUpperCase().equals("COMMAND")	&& command.equals(tmp.getCommand())) 
					{
						/* We Rename command tag to result tag and them make command as an attribute to this tag */
						cmd.setName("Result");
						cmd.setValue("");
						// create tags for command jobType, errors
						cmd.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE, "command", command);
						MbElement jobTag = cmd.createElementAsLastChild(MbElement.TYPE_NAME, "JobType", tmp.getJobType());
						MbElement outputTag = cmd.createElementAsLastChild(MbElement.TYPE_NAME, "output", 
					tmp.getResult());
						MbElement errorTag = cmd.createElementAsLastChild(MbElement.TYPE_NAME, "error", tmp.getError());
					}
				}
			}
			/*If the command was made input from Node property then we need to create a new tags for propogating it which is done as below */
			else 
			{
				MbElement resultTag1 = rootElement1.getLastChild().getFirstChild().createElementAsLastChild(MbElement.TYPE_NAME, "Result", "");
				resultTag1.createElementAsFirstChild(MbElement.TYPE_NAME_VALUE,	"command", tmp.getCommand());
				MbElement jobTag = resultTag1.createElementAsLastChild(MbElement.TYPE_NAME, "JobType", tmp.getJobType());
				MbElement outputTag = resultTag1.createElementAsLastChild(MbElement.TYPE_NAME, "output", tmp.getResult());
				MbElement errorTag = resultTag1.createElementAsLastChild(MbElement.TYPE_NAME, "error", tmp.getError());
			}
			
			MbMessageAssembly outassembly = new MbMessageAssembly(inassembly,mg1);
			logger.logData_Debug("Message for Job Number " + tmp.getNum()+ "  is  propagating to output terminal ");
			if (getOutputTerminal().isAttached()) 
			{
				try 
				{
					getOutputTerminal().propagate(outassembly);
				} catch (MbException mbex) 
				{
					logger.logData_Error("\tMbException occurred ", mbex);
					parseBrokerException(mbex, 0);
					throw mbex;
				}
				/* Catch the MbBrokerException and log it to the file and rethrows the original exception. */
				catch (MbBrokerException mbbrke) {
					logger.logData_Error("\tMbBrokerException occurred ", mbbrke);
					/* Some Error using broker so we sent the message to failure terminal */
				
					if (failureTerminal.isAttached()) {
						
						failureTerminal.propagate(inassembly);
						logger.logData_Info("  Message propagated to failure terminal ");
						logger.logData_Debug(" }JobExecuteNode::run ");
					}
					throw mbbrke;
				} catch (Exception ex) 
				{
					logger.logData_Error("\tException occurred ", ex);
				} finally 
				{
					destroyMessageAssembly(outassembly);
				}
				logger.logData_Info("  Message propagated to output terminal successfully");
			} else 
			{
				logger.logData_Error("  ERROR ... OUT TERMINAL NOT CONNECTED :-( ");
				try {
					throw new OutPutTerminalNotConnected("out put terminal Not connected");
				} catch (OutPutTerminalNotConnected e) {
					// TODO Auto-generated catch block
					OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,"OUT_NOTCONNECTED", null);
					logger.logData_Error("Output Terminal is not connected");
					e.printStackTrace();
				}
			}
			((Execute) jobExecutionThreads.elementAt(id)).destroyoutThread();
		}
		}//if progResult = YES
		else {
			MbMessage mg1 = new MbMessage(inassembly.getMessage());
			MbMessageAssembly outassembly = new MbMessageAssembly(inassembly,mg1);
			
			if (getOutputTerminal().isAttached()) 
			{
				try 
				{
					getOutputTerminal().propagate(outassembly);
				} catch (MbException mbex) 
				{
					logger.logData_Error("\tMbException occurred ", mbex);
					parseBrokerException(mbex, 0);
					throw mbex;
				}
			}
		}
		logger.logData_Debug(" } " + className + "." + methodName);
	}
	
	/* In this function we delete all the Jobs which have been completed from JobExecutionThreads */
	void deleteCompletedJobs(Vector v) 
	{
		String methodName = "deleteCompletedJobs";
		logger.logData_Debug(" { " + className + "." + methodName);
		int len = v.size();
		for (int i = 0; i < len; i++) 
		{
			int id = Integer.parseInt(v.elementAt(i).toString());
			jobExecutionThreads.removeElementAt(id - i);
		}
	logger.logData_Debug("Details of threads in current Execution Group is as below");
	ll.printGroupInfo(Thread.currentThread().getThreadGroup());
		logger.logData_Debug(" } " + className + "." + methodName);
	
	}
		void printAllThreadsStatus() 
	{
		String methodName = "printAllThreadsStatus";
		logger.logData_Debug(" { " + className + "." + methodName);
		
		for (int index = 0; index < jobExecutionThreads.size(); index++) {
			Execute p = (Execute) jobExecutionThreads.elementAt(index);
			logger.logData_Debug("The thread Index is " + index);
			logger.logData_Debug("The thread was started for comman " + p.getCommand());
			if (p.isAlive()) {
				logger.logData_Debug("Execution Status :---         Completed");
			} else
				logger.logData_Debug("Execution Status :---        In Process");
		}
		logger.logData_Debug(" { " + className + "." + methodName);
	}

	private void destroyMessageAssembly(MbMessageAssembly assembly) 
	{
		String methodName = "destroyMessageAssembly";
		logger.logData_Debug(" { " + className + "." + methodName);
		try 
		{
			if (assembly.getMessage() != null)//checck for if message is enpty or not first
				assembly.getMessage().clearMessage();
		} catch (MbException mbe) 
		{
			logger.logData_Error("  MbException occurred ", mbe);
			OSLogData.syslog(className, "destroyMessageAssembly=", OSLogData.SYSLOG_WARN, "WMB_WARN", logger.getExceptionString(mbe));
		}
		logger.logData_Debug(" } " + className + "." + methodName);
	}
	
	public static String getNodeName() 
	{
		return "JobExecutionNode";
	}
	
	/*
	 * This method is called by the broker when stop command is issued on the
	 * flow by Execution Group.
	 */
	public void onDelete() 
	{
		logger.logData_Debug(" { " + className + ".onDelete ");
		for (int i = 0; i < jobExecutionThreads.size(); i++) 
		{
			Execute toBeTerminated = ((Execute) jobExecutionThreads.elementAt(i));
			toBeTerminated.returnprocess().destroy();
		//	OSLogData.syslog(className, "onDelete", OSLogData.SYSLOG_ERROR,"JOB_TERMINATED", toBeTerminated.getCommand());
			
		}
		logger.logData_Debug(" } " + className + ".onDelete ");
		OSLogData.syslog(className, "onDelete", OSLogData.SYSLOG_INFO,"NODE_DELETED", getNodeName());
		logger.closeInstance();
	}
	
	private void parseBrokerException(MbException mbex, int pLevel) 
	{
		String methodName = "parseBrokerException";
		logger.logData_Debug(" { " + className + "." + methodName);
		if (mbex != null) 
		{
			logger.logData_Error("\t parseStackErrorLevel : " + pLevel);
			logger.logData_Error("\t Exception Trace Text : " + mbex.getTraceText());
			
			/* Now traverse the hierarchy */
			MbException e[] = mbex.getNestedExceptions();
			int size = e.length;
			for (int i = 0; i < size; i++) 
			{
				parseBrokerException(e[i], pLevel + 1);
			}
		}
		logger.logData_Debug(" } " + className + "." + methodName);
	}
	
	public String getJobType() 
	{
		logger.logData_Debug("  " + className + ".getJobtype returns " + jobType);
		return jobType;
	}
	
	/* for node properties attribute 'jobtype' used by broker */
	public void setJobType(String jtype) 
	{
		jobType = jtype;
		logger.logData_Debug("  " + className + ".setJobType sets " + jtype);
	}
	
	public String getJobLocation() 
	{
		logger.logData_Debug("  " + className + ".getJobLocation returns "	+ jobLocation);
		// does the logger really wrk as the setloglevel is random call
		return jobLocation;
	}
	
	/* for node properties attribute 'jobLocation' used by broker */
	public void setJobLocation(String jloc) 
	{
		jobLocation = jloc;
		logger.logData_Debug("  " + className + ".setJoblocation sets " + jloc);
	}
	
	public String getResultFile() 
	{
		logger.logData_Debug("  " + className + ".getResultFile returns "	+ resultFile);
		return resultFile;
	}
	
	/* for node properties attribute 'resultFile' used by broker */
	public void setResultFile(String file) 
	{
		resultFile = file;
		if (isDefaultoutFileNull) 
		{
			outputDefaultFile = file;
			isDefaultoutFileNull = false;
		}
		logger.logData_Debug("  " + className + ".setResultFile sets " + file);
	}
	
	public String getCommandSource() 
	{
		logger.logData_Debug("  " + className + ".getcommandSource returns "+ commandSource);
		return commandSource;
	}
	
	/* for node properties attribute 'commandSource' used by broker */
	public void setCommandSource(String source) 
	{
		commandSource = source;
		logger.logData_Debug("  " + className + ".setCommandSource sets " + source);
	}
	public String getPropagateResult() 
	{
		logger.logData_Debug("  " + className + ".getPropagateResult returns "+ propagateResult);
		return propagateResult;
	}
	
	/* for node properties attribute 'commandSource' used by broker */
	public void setPropagateResult(String propResult) 
	{
		
		propagateResult = propResult;
		logger.logData_Debug("  " + className + ".setpropagateResult sets " + propResult);
	}
	public String getResultToAdd() 
	{
		logger.logData_Debug("  " + className + ".getResultToAdd returns "
				+ resultFile);
		return resultToAdd;
	}
	
	/* for node properties attribute 'commandSource' used by broker */
	public void setResultToAdd(String resultTobeAdded) 
	{
		resultToAdd = resultTobeAdded;
		logger.logData_Debug("  " + className + ".setResultToAdd sets "	+ resultToAdd);
	}
	
	/* for node properties attribute 'logLevel' */
	public String getLogLevel() 
	{
		String level = "NONE";
		
		switch (logLevel) 
		{
			case logData.LOGDATA_LEVEL_NONE:	level = "NONE";		break;
			case logData.LOGDATA_LEVEL_DEBUG:	level = "DEBUG";	break;
			case logData.LOGDATA_LEVEL_INFO:	level = "INFO";		break;
			case logData.LOGDATA_LEVEL_ERROR:	level = "ERROR";	break;
		}
		logger.logData_Debug("  " + className + ".getLogLevel returns " + level);
		return level;
	}
	
	/* for node properties attribute 'logLevel' used by broker */
	public void setLogLevel(String level) 
	{
		if (level == null){
			
		    level = "NONE";
			return;
		}
		
		if (level.equals("NONE"))
			logLevel = logData.LOGDATA_LEVEL_NONE;
		else if (level.equals("DEBUG"))
			logLevel = logData.LOGDATA_LEVEL_DEBUG;
		else if (level.equals("INFO"))
			logLevel = logData.LOGDATA_LEVEL_INFO;
		else if (level.equals("ERROR"))
			logLevel = logData.LOGDATA_LEVEL_ERROR;
		
		logger.setlogDataLevel(getLogLevel());
		
		logger.logData_Debug("  " + className + ".setLogLevel sets " + level);
		ll = new ThreadLister(logger);
		/* get the logLevel from the node properties */
		OSLogData.syslog(className, "setLogLevel", OSLogData.SYSLOG_INFO,"NODE_INITIALIZED", getNodeName());
	}
	
	void setFailureTerminal(MbOutputTerminal failTerm) 
	{
		failureTerminal = failTerm;
	}
	
	MbOutputTerminal getFailureTerminal() 
	{
		return failureTerminal;
	}
	
	void setOutputTerminal(MbOutputTerminal outTerm) 
	{
		outTerminal = outTerm;
	}
	
	MbOutputTerminal getOutputTerminal() 
	{
		return outTerminal;
	}
	
	void setInputTerminal(MbInputTerminal inTerm) 
	{
		inputTerminal = inTerm;
	}
	
	MbInputTerminal getInputTerminal() 
	{
		return inputTerminal;
	}
}
