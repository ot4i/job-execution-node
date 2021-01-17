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

class NotABatchFileException extends Exception
{
	NotABatchFileException()
	{
		super();
	}
	NotABatchFileException(String msg)
	{
		super(msg);
	}
}

class OsNotWindowsException extends Exception
{
	OsNotWindowsException()
	{
		super();
	}
	OsNotWindowsException(String msg)
	{
		super(msg);
	}
}

class OsIsWindowsException extends Exception 
{
	OsIsWindowsException()
	{
		super();
	}
	OsIsWindowsException(String msg)
	{
		super(msg);
	}
}

class OutPutTerminalNotConnected extends Exception 
{
	OutPutTerminalNotConnected()
	{
	 super();	
	}
	OutPutTerminalNotConnected(String msg)
	{
		super(msg);
	}
}

class commandTagNotInMessage extends Exception{
	commandTagNotInMessage(){
		
	}
	commandTagNotInMessage(String msg){
		super(msg);
	}
}
