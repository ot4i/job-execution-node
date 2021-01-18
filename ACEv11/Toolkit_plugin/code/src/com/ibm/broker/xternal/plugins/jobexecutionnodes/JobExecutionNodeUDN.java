package com.ibm.broker.xternal.plugins.jobexecutionnodes;

import com.ibm.broker.config.appdev.InputTerminal;
import com.ibm.broker.config.appdev.Node;
import com.ibm.broker.config.appdev.NodeProperty;
import com.ibm.broker.config.appdev.OutputTerminal;

/*** 
 * <p>  <I>JobExecutionNodeUDN</I> instance</p>
 * <p></p>
 */
public class JobExecutionNodeUDN extends Node {

	private static final long serialVersionUID = 1L;

	// Node constants
	protected final static String NODE_TYPE_NAME = "com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecutionNode";
	protected final static String NODE_GRAPHIC_16 = "platform:/plugin/com.ibm.broker.xternal.plugins.jobexecutionnodes/icons/full/obj16/com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution.gif";
	protected final static String NODE_GRAPHIC_32 = "platform:/plugin/com.ibm.broker.xternal.plugins.jobexecutionnodes/icons/full/obj30/com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution.gif";

	protected final static String PROPERTY_JOBTYPE = "jobType";
	protected final static String PROPERTY_JOBLOCATION = "jobLocation";
	protected final static String PROPERTY_COMMANDSOURCE = "commandSource";
	protected final static String PROPERTY_RESULTFILE = "resultFile";
	protected final static String PROPERTY_LOGLEVEL = "logLevel";
	protected final static String PROPERTY_PROPAGATERESULT = "propagateResult";


	/**
	 * <I>ENUM_JOBEXECUTION_LOGLEVEL</I>
	 * <pre>
	 * ENUM_JOBEXECUTION_LOGLEVEL.NONE = NONE
	 * ENUM_JOBEXECUTION_LOGLEVEL.INFO = INFO
	 * ENUM_JOBEXECUTION_LOGLEVEL.ERROR = ERROR
	 * ENUM_JOBEXECUTION_LOGLEVEL.DEBUG = DEBUG
	 * </pre>
	 */
	public static class ENUM_JOBEXECUTION_LOGLEVEL {
		private String value;

		public static final ENUM_JOBEXECUTION_LOGLEVEL NONE = new ENUM_JOBEXECUTION_LOGLEVEL("NONE");
		public static final ENUM_JOBEXECUTION_LOGLEVEL INFO = new ENUM_JOBEXECUTION_LOGLEVEL("INFO");
		public static final ENUM_JOBEXECUTION_LOGLEVEL ERROR = new ENUM_JOBEXECUTION_LOGLEVEL("ERROR");
		public static final ENUM_JOBEXECUTION_LOGLEVEL DEBUG = new ENUM_JOBEXECUTION_LOGLEVEL("DEBUG");

		protected ENUM_JOBEXECUTION_LOGLEVEL(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_JOBEXECUTION_LOGLEVEL getEnumFromString(String enumValue) {
			ENUM_JOBEXECUTION_LOGLEVEL enumConst = ENUM_JOBEXECUTION_LOGLEVEL.NONE;
			if (ENUM_JOBEXECUTION_LOGLEVEL.INFO.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_LOGLEVEL.INFO;
			if (ENUM_JOBEXECUTION_LOGLEVEL.ERROR.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_LOGLEVEL.ERROR;
			if (ENUM_JOBEXECUTION_LOGLEVEL.DEBUG.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_LOGLEVEL.DEBUG;
			return enumConst;
		}

		public static String[] values = new String[]{ "NONE", "INFO", "ERROR", "DEBUG" };

	}

	/**
	 * <I>ENUM_JOBEXECUTION_COMMANDSOURCE</I>
	 * <pre>
	 * ENUM_JOBEXECUTION_COMMANDSOURCE.MESSAGE = MESSAGE
	 * ENUM_JOBEXECUTION_COMMANDSOURCE.PROPERTY = PROPERTY
	 * </pre>
	 */
	public static class ENUM_JOBEXECUTION_COMMANDSOURCE {
		private String value;

		public static final ENUM_JOBEXECUTION_COMMANDSOURCE MESSAGE = new ENUM_JOBEXECUTION_COMMANDSOURCE("MESSAGE");
		public static final ENUM_JOBEXECUTION_COMMANDSOURCE PROPERTY = new ENUM_JOBEXECUTION_COMMANDSOURCE("PROPERTY");

		protected ENUM_JOBEXECUTION_COMMANDSOURCE(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_JOBEXECUTION_COMMANDSOURCE getEnumFromString(String enumValue) {
			ENUM_JOBEXECUTION_COMMANDSOURCE enumConst = ENUM_JOBEXECUTION_COMMANDSOURCE.MESSAGE;
			if (ENUM_JOBEXECUTION_COMMANDSOURCE.PROPERTY.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_COMMANDSOURCE.PROPERTY;
			return enumConst;
		}

		public static String[] values = new String[]{ "MESSAGE", "PROPERTY" };

	}

	/**
	 * <I>ENUM_JOBEXECUTION_JOBTYPE</I>
	 * <pre>
	 * ENUM_JOBEXECUTION_JOBTYPE.COMMANDLINE = COMMANDLINE
	 * ENUM_JOBEXECUTION_JOBTYPE.BATCH = BATCH
	 * ENUM_JOBEXECUTION_JOBTYPE.SHELL = SHELL
	 * </pre>
	 */
	public static class ENUM_JOBEXECUTION_JOBTYPE {
		private String value;

		public static final ENUM_JOBEXECUTION_JOBTYPE COMMANDLINE = new ENUM_JOBEXECUTION_JOBTYPE("COMMANDLINE");
		public static final ENUM_JOBEXECUTION_JOBTYPE BATCH = new ENUM_JOBEXECUTION_JOBTYPE("BATCH");
		public static final ENUM_JOBEXECUTION_JOBTYPE SHELL = new ENUM_JOBEXECUTION_JOBTYPE("SHELL");

		protected ENUM_JOBEXECUTION_JOBTYPE(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_JOBEXECUTION_JOBTYPE getEnumFromString(String enumValue) {
			ENUM_JOBEXECUTION_JOBTYPE enumConst = ENUM_JOBEXECUTION_JOBTYPE.COMMANDLINE;
			if (ENUM_JOBEXECUTION_JOBTYPE.BATCH.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_JOBTYPE.BATCH;
			if (ENUM_JOBEXECUTION_JOBTYPE.SHELL.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_JOBTYPE.SHELL;
			return enumConst;
		}

		public static String[] values = new String[]{ "COMMANDLINE", "BATCH", "SHELL" };

	}

	/**
	 * <I>ENUM_JOBEXECUTION_PROPAGATERESULT</I>
	 * <pre>
	 * ENUM_JOBEXECUTION_PROPAGATERESULT.NO = NO
	 * ENUM_JOBEXECUTION_PROPAGATERESULT.YES = YES
	 * </pre>
	 */
	public static class ENUM_JOBEXECUTION_PROPAGATERESULT {
		private String value;

		public static final ENUM_JOBEXECUTION_PROPAGATERESULT NO = new ENUM_JOBEXECUTION_PROPAGATERESULT("NO");
		public static final ENUM_JOBEXECUTION_PROPAGATERESULT YES = new ENUM_JOBEXECUTION_PROPAGATERESULT("YES");

		protected ENUM_JOBEXECUTION_PROPAGATERESULT(String value) {
			this.value = value;
		}
		public String toString() {
			return value;
		}

		protected static ENUM_JOBEXECUTION_PROPAGATERESULT getEnumFromString(String enumValue) {
			ENUM_JOBEXECUTION_PROPAGATERESULT enumConst = ENUM_JOBEXECUTION_PROPAGATERESULT.NO;
			if (ENUM_JOBEXECUTION_PROPAGATERESULT.YES.value.equals(enumValue)) enumConst = ENUM_JOBEXECUTION_PROPAGATERESULT.YES;
			return enumConst;
		}

		public static String[] values = new String[]{ "NO", "YES" };

	}
	protected NodeProperty[] getNodeProperties() {
		return new NodeProperty[] {
			new NodeProperty(JobExecutionNodeUDN.PROPERTY_JOBTYPE,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.ENUMERATION, "COMMANDLINE", ENUM_JOBEXECUTION_JOBTYPE.class,"","",	"com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution",	"com.ibm.broker.xternal.plugins.jobexecutionnodes"),
			new NodeProperty(JobExecutionNodeUDN.PROPERTY_JOBLOCATION,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.STRING, "","","",	"com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution",	"com.ibm.broker.xternal.plugins.jobexecutionnodes"),
			new NodeProperty(JobExecutionNodeUDN.PROPERTY_COMMANDSOURCE,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.ENUMERATION, "MESSAGE", ENUM_JOBEXECUTION_COMMANDSOURCE.class,"","",	"com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution",	"com.ibm.broker.xternal.plugins.jobexecutionnodes"),
			new NodeProperty(JobExecutionNodeUDN.PROPERTY_RESULTFILE,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution",	"com.ibm.broker.xternal.plugins.jobexecutionnodes"),
			new NodeProperty(JobExecutionNodeUDN.PROPERTY_LOGLEVEL,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.ENUMERATION, "NONE", ENUM_JOBEXECUTION_LOGLEVEL.class,"","",	"com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution",	"com.ibm.broker.xternal.plugins.jobexecutionnodes"),
			new NodeProperty(JobExecutionNodeUDN.PROPERTY_PROPAGATERESULT,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.ENUMERATION, "NO", ENUM_JOBEXECUTION_PROPAGATERESULT.class,"","",	"com/ibm/broker/xternal/plugins/jobexecutionnodes/JobExecution",	"com.ibm.broker.xternal.plugins.jobexecutionnodes")
		};
	}

	public JobExecutionNodeUDN() {
	}

	public final InputTerminal INPUT_TERMINAL_IN = new InputTerminal(this,"InTerminal.in");
	@Override
	public InputTerminal[] getInputTerminals() {
		return new InputTerminal[] {
			INPUT_TERMINAL_IN
	};
	}

	public final OutputTerminal OUTPUT_TERMINAL_FAILURE = new OutputTerminal(this,"OutTerminal.failure");
	public final OutputTerminal OUTPUT_TERMINAL_OUT = new OutputTerminal(this,"OutTerminal.out");
	@Override
	public OutputTerminal[] getOutputTerminals() {
		return new OutputTerminal[] {
			OUTPUT_TERMINAL_FAILURE,
			OUTPUT_TERMINAL_OUT
		};
	}

	@Override
	public String getTypeName() {
		return NODE_TYPE_NAME;
	}

	protected String getGraphic16() {
		return NODE_GRAPHIC_16;
	}

	protected String getGraphic32() {
		return NODE_GRAPHIC_32;
	}

	/**
	 * Set the <I>JobExecutionNodeUDN</I> "<I>jobType</I>" property
	 * 
	 * @param value ENUM_JOBEXECUTION_JOBTYPE ; the value to set the property "<I>jobType</I>"
	 */
	public JobExecutionNodeUDN setJobType(ENUM_JOBEXECUTION_JOBTYPE value) {
		setProperty(JobExecutionNodeUDN.PROPERTY_JOBTYPE, value.toString());
		return this;
	}

	/**
	 * Get the <I>JobExecutionNodeUDN</I> "<I>jobType</I>" property
	 * 
	 * @return ENUM_JOBEXECUTION_JOBTYPE; the value of the property "<I>jobType</I>"
	 */
	public ENUM_JOBEXECUTION_JOBTYPE getJobType() {
		ENUM_JOBEXECUTION_JOBTYPE value = ENUM_JOBEXECUTION_JOBTYPE.getEnumFromString((String)getPropertyValue(JobExecutionNodeUDN.PROPERTY_JOBTYPE));
		return value;
	}

	/**
	 * Set the <I>JobExecutionNodeUDN</I> "<I>jobLocation</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>jobLocation</I>"
	 */
	public JobExecutionNodeUDN setJobLocation(String value) {
		setProperty(JobExecutionNodeUDN.PROPERTY_JOBLOCATION, value);
		return this;
	}

	/**
	 * Get the <I>JobExecutionNodeUDN</I> "<I>jobLocation</I>" property
	 * 
	 * @return String; the value of the property "<I>jobLocation</I>"
	 */
	public String getJobLocation() {
		return (String)getPropertyValue(JobExecutionNodeUDN.PROPERTY_JOBLOCATION);
	}

	/**
	 * Set the <I>JobExecutionNodeUDN</I> "<I>commandSource</I>" property
	 * 
	 * @param value ENUM_JOBEXECUTION_COMMANDSOURCE ; the value to set the property "<I>commandSource</I>"
	 */
	public JobExecutionNodeUDN setCommandSource(ENUM_JOBEXECUTION_COMMANDSOURCE value) {
		setProperty(JobExecutionNodeUDN.PROPERTY_COMMANDSOURCE, value.toString());
		return this;
	}

	/**
	 * Get the <I>JobExecutionNodeUDN</I> "<I>commandSource</I>" property
	 * 
	 * @return ENUM_JOBEXECUTION_COMMANDSOURCE; the value of the property "<I>commandSource</I>"
	 */
	public ENUM_JOBEXECUTION_COMMANDSOURCE getCommandSource() {
		ENUM_JOBEXECUTION_COMMANDSOURCE value = ENUM_JOBEXECUTION_COMMANDSOURCE.getEnumFromString((String)getPropertyValue(JobExecutionNodeUDN.PROPERTY_COMMANDSOURCE));
		return value;
	}

	/**
	 * Set the <I>JobExecutionNodeUDN</I> "<I>resultFile</I>" property
	 * 
	 * @param value String ; the value to set the property "<I>resultFile</I>"
	 */
	public JobExecutionNodeUDN setResultFile(String value) {
		setProperty(JobExecutionNodeUDN.PROPERTY_RESULTFILE, value);
		return this;
	}

	/**
	 * Get the <I>JobExecutionNodeUDN</I> "<I>resultFile</I>" property
	 * 
	 * @return String; the value of the property "<I>resultFile</I>"
	 */
	public String getResultFile() {
		return (String)getPropertyValue(JobExecutionNodeUDN.PROPERTY_RESULTFILE);
	}

	/**
	 * Set the <I>JobExecutionNodeUDN</I> "<I>logLevel</I>" property
	 * 
	 * @param value ENUM_JOBEXECUTION_LOGLEVEL ; the value to set the property "<I>logLevel</I>"
	 */
	public JobExecutionNodeUDN setLogLevel(ENUM_JOBEXECUTION_LOGLEVEL value) {
		setProperty(JobExecutionNodeUDN.PROPERTY_LOGLEVEL, value.toString());
		return this;
	}

	/**
	 * Get the <I>JobExecutionNodeUDN</I> "<I>logLevel</I>" property
	 * 
	 * @return ENUM_JOBEXECUTION_LOGLEVEL; the value of the property "<I>logLevel</I>"
	 */
	public ENUM_JOBEXECUTION_LOGLEVEL getLogLevel() {
		ENUM_JOBEXECUTION_LOGLEVEL value = ENUM_JOBEXECUTION_LOGLEVEL.getEnumFromString((String)getPropertyValue(JobExecutionNodeUDN.PROPERTY_LOGLEVEL));
		return value;
	}

	/**
	 * Set the <I>JobExecutionNodeUDN</I> "<I>propagateResult</I>" property
	 * 
	 * @param value ENUM_JOBEXECUTION_PROPAGATERESULT ; the value to set the property "<I>propagateResult</I>"
	 */
	public JobExecutionNodeUDN setPropagateResult(ENUM_JOBEXECUTION_PROPAGATERESULT value) {
		setProperty(JobExecutionNodeUDN.PROPERTY_PROPAGATERESULT, value.toString());
		return this;
	}

	/**
	 * Get the <I>JobExecutionNodeUDN</I> "<I>propagateResult</I>" property
	 * 
	 * @return ENUM_JOBEXECUTION_PROPAGATERESULT; the value of the property "<I>propagateResult</I>"
	 */
	public ENUM_JOBEXECUTION_PROPAGATERESULT getPropagateResult() {
		ENUM_JOBEXECUTION_PROPAGATERESULT value = ENUM_JOBEXECUTION_PROPAGATERESULT.getEnumFromString((String)getPropertyValue(JobExecutionNodeUDN.PROPERTY_PROPAGATERESULT));
		return value;
	}

	public String getNodeName() {
		String retVal = super.getNodeName();
		if ((retVal==null) || retVal.equals(""))
			retVal = "JobExecution";
		return retVal;
	};
}
