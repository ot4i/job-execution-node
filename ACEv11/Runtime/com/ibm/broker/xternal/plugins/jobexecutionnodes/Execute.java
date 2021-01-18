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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Execute extends Thread {
	private InputStream output;
	private int num;
	private InputStream error;
	private String command;
	// private String type;
	private String result = "";
	private String error1 = "";
	private String resultFile = "";
	private String jobType;
	private logData logger;
	private Process toBeExecuted;
	private boolean sourceIsMessage;
	private boolean successful;
	private PropertyData props;
	private static final String className = "Execute";

	/* This method writes data to a file. */
	private void writeToFile(String filename, String data) {
		try {
			FileOutputStream out = new FileOutputStream(filename, true);
			// Connect print stream to the output stream
			PrintStream p = new PrintStream(out);
			p.println(data);
			p.close();
		} catch (Exception e) {
			System.out.println("file name was " + filename);
			System.err.println("Error writing to file");
			e.printStackTrace();
		}
	}

	public Execute(String jobType, String command, boolean sourceismg,
			String fileName, PropertyData pdata, logData ld, int n) {
		this.setName(command);
		this.jobType = jobType;
		this.command = command;
		this.resultFile = fileName;
		this.sourceIsMessage = sourceismg;
		this.num = n;
		props = pdata;
		this.logger = ld;
	}

	public void run() {
		String s = "";
		String methodName = "run";
		logger.logData_Debug(" { " + className + "." + methodName);
		try {
			String osname = System.getProperty("os.name");
			if ((jobType).toUpperCase().equals("BATCH")) {
				if (!command.toUpperCase().endsWith(".BAT")) {
					throw new NotABatchFileException("Not A Valid BatchFile");
				} else if (osname.toUpperCase().indexOf("WINDOWS") != -1)
					toBeExecuted = Runtime.getRuntime().exec(
							"cmd /c " + command);
				else {
					throw new OsNotWindowsException("Os is not Windows");

				}
			} else if ((jobType).toUpperCase().equals("SHELL")) {
				if (osname.toUpperCase().indexOf("WINDOWS") != -1) {
					throw new OsIsWindowsException("Os is not Unix");

				} else
					toBeExecuted = Runtime.getRuntime().exec("sh " + command);
			} else {
				toBeExecuted = Runtime.getRuntime().exec(command);
			}
			output = toBeExecuted.getInputStream();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(output));

			while ((s = in.readLine()) != null) {

				this.result = this.result + s + "\n";
				if (!(resultFile.equals("NONE")))
					writeToFile(resultFile, s);
			}
			error = toBeExecuted.getErrorStream();
			BufferedReader in1 = new BufferedReader(
					new InputStreamReader(error));
			while ((s = in1.readLine()) != null) {
				this.error1 = this.error1 + s + "\n";
				if (!(resultFile.equals("NONE")))
					writeToFile(resultFile, s);
			}
			int exitVal = toBeExecuted.waitFor();
			if (exitVal == 0)
				successful = true;
			else
				successful = false;
			logger.logData_Debug(" } " + className + "." + methodName);

			in.close();
			in1.close();
		} catch (IOException ioe) {
			OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,
					"INTERNAL_UNKNOWN_ERROR", null);
			logger.logData_Error("Some unknows Error occured while executing "
					+ command);
			ioe.printStackTrace();
		} catch (InterruptedException e) {
			// Shralog unknown here
			OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,
					"INTERNAL_UNKNOWN_ERROR", null);
			logger.logData_Error("Some unknows Error occured while executing "
					+ command);
			e.printStackTrace();
		} catch (NotABatchFileException e) {
			// TODO Auto-generated catch block
			OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,
					"NOT_BAT", null);
			logger.logData_Error("The file " + command
					+ " is not a valid Batch File");
			e.printStackTrace();
		} catch (OsNotWindowsException e) {
			// TODO Auto-generated catch block
			OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,
					"NOT_WINDOWS", null);
			logger.logData_Error("The file to be executed is a Batch File but the operating System is not windows");
			e.printStackTrace();
		} catch (OsIsWindowsException e) {
			// TODO Auto-generated catch block
			OSLogData.syslog(className, "Run", OSLogData.SYSLOG_ERROR,
					"NOT_LINUX", null);
			logger.logData_Error("The operating sysetm is windows so shellscript "
					+ command + " cant be executed");
			e.printStackTrace();
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	void destroyoutThread() {
		result = "";
	}

	Process returnprocess() {
		return toBeExecuted;
	}

	PropertyData getProps() {
		return props;
	}

	int getNum() {
		return num;
	}

	public String getCommand() {
		return command;

	}

	public String getJobType() {
		return jobType;
	}

	public boolean sourceIsMessage() {
		return sourceIsMessage;
	}

	public String getError() {
		return error1;
	}

}

class PropertyData {
	String messageType;
	String messageSet;
	String messageFormat;
	int encoding;
	int ccsid;
	byte[] message;

	public PropertyData(String messageType, String messageSet,
			String messageFormat, int encoding, int ccsid, byte[] msg) {
		this.messageType = messageType;
		this.messageSet = messageSet;
		this.messageFormat = messageFormat;
		this.encoding = encoding;
		this.ccsid = ccsid;
		this.message = msg;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getMessageFormat() {
		return messageFormat;
	}

	public String getMessageSet() {
		return messageFormat;
	}

	public int getEncoding() {
		return encoding;
	}

	public int getCCSID() {
		return ccsid;
	}

	public byte[] getMessage() {
		return message;
	}

}
