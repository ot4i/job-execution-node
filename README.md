# job-execution-node
JobExecution Node is a custom node developed using Java plugin node API and hence it can be used on any distributed platform supported by IBM Integration Bus v10.
The JobExecution Node allows users to run operating system commands , windows batch files,unix shell scripts (henceforth referred as job) from within the message flow. 
Jobs can also be scheduled for specific time and interval using Timer nodes.


<b>Configuring the Node</b>

The JobExecutionNode has following properties to specify the jobs to be executed.  

Job Type : From the pull down List type of Job can be selected. In this version there are
three job types that are supported .
1) BATCH - The job to be executed is a windows batch (.bat) file.
2) SHELL - The job to be executed is a unix shell (.sh) script.
3) COMMANDLINE – The job to be executed is a single command directly to be executed
from command prompt (either windows or unix).


<b>Job Location</b> : This property specifies the Job to be executed. System commands, Unix shell
scripts or windows batch files that have to be executed are entered here. If you need to execute
Windows Batch Files or Unix Shell scripts, enter the absolute path. If you need to execute more
than one command, you can specify them as comma separated.

<b>Command Source</b> : This property specifies whether the jobs to be executed would be
sourced from the incoming message or from the node property.
The two options on the drop down list are –
1) MESSAGE - The job will be taken from input message
2) PROPERTY - The job will be taken from node property ‘Job Location’.

When the command source is selected as MESSAGE, then the input message must have
following elements in order in the incoming XML message into the JobExecutionNode.
<message><command> “job to be executed”</command></message>

<b>Example</b> :

Windows :
`<message><command>cmd /c dir c:\Work</command></message>`

Unix :
`<message><command>ls –l /home</command></message>`

<b>Results File </b>: User can specify the file where the result of the job to be stored. By default it
is blank which means that result will not be stored in the text file on the hard drive.

However the result will be available in the JobExecutionNode if the propagateResult property
on the node is set to YES.
If user intends to store the result in a file, an absolute path of the file can be specified

<b>Node Trace Level </b>: Choose the appropriate level to trace the node. By default this
is “NONE”.  
The node appends the information into a single log file, irrespective of number of instances of the
flows that use this node. The log file “JobeExecutionNodeLogData.txt” can be located in \<IIBinstall-
dir\>\bin on windows and on Unix machines it can be located in the /tmp directory. The
Node Tracing value set here will have the following significance.

NONE: No logging is done.
ERROR: Log only if an error occurs in the node during message processing.
INFO : Log basic node operations and ERROR(s) if any.
DEBUG : Log very detailed information of the node, including every function it enters and exists along with ERROR(s).

<b>Propagate Result </b>: This property decides whether the output of the job execution will be
propagated to the out terminal along with the original message. The default is NO which means
job will be fired/scheduled and the message will be propagated to out terminal as-is.

YES : Message will be held in the Job Execution Node and will be propagated after execution of Job is complete and result appended to the message.

NO : Job will be executed and message immediately propagates to out terminal.
 



# Installing the Plug-in Node in IIB v10

<b>Runtime :</b>

Download the code and build it in Toolkit  as a Java project and export it as a jar file - JobExecutionNode.jar

Place this jar file to `<IIB-Install–Directory>/server/jplugin`.

Restart the Integration Node.

<b>Toolkit:</b>

Copy ‘ JobExecutionNode_toolkit_plugin.jar’ file that is provided in this github project (IIBv10\\Toolkit_plugin) to `<IIB-Install-Directory>/tools/plugins` directory.

Restart the toolkit with “–clean” option

# Installing the Plug-in Node in ACE V11

<b>Runtime :</b>

Download the code from ACEV11/Runtime folder and build it in Toolkit  as a Java project and export it as a jar file - JobExecutionNode.jar

Place this jar file to `<ACE-Install–Directory>/server/jplugin`.

Restart the Integration Node.

<b>Toolkit:</b>

Copy ‘ JobExecutionNode_toolkit_plugin.jar’ file that is provided in this github project (ACEV11\\Toolkit_plugin) to `<ACE-Install-Directory>/tools/plugins` directory.

Restart the Integration Toolkit with “–clean” option
