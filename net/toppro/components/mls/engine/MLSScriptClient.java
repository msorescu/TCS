//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:45 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.TimeSpan;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.DefString;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSCommand;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.PropertyFieldGroup;
import net.toppro.components.mls.engine.STRINGS;

public abstract class MLSScriptClient  extends net.toppro.components.mls.engine.CommunicationClient 
{
    protected public static final int NO_SCRIPT = -1;
    protected public static final int SCRIPT_MAIN = 0;
    protected public static final int SCRIPT_PIC = 1;
    protected public static final int SCRIPT_REM = 2;
    protected public static final int SCRIPT_EXTRAVAR = 3;
    //UPGRADE_NOTE: Final was removed from the declaration of 'SCRIPT_NAME'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final String[] SCRIPT_NAME = new String[]{ "MainScript", "PictureScript", "REMScript", "EXTRAVARScript" };
    protected public static final int PARAMETER_TYPE_SEARCH_FIELD = 0;
    protected public static final int PARAMETER_TYPE_SECURITY_FIELD = 1;
    protected public static final int PARAMETER_TYPE_CMA_FIELD = 2;
    protected public static final int PARAMETER_TYPE_DEF_FIELD = 3;
    protected public static final int PARAMETER_TYPE_VARIABLE = 4;
    protected public static final int PARAMETER_TYPE_TEST_VALUE = 5;
    private static final int FIELD_REPLACE_FLAG_NOT_EMPTY = 1;
    private static final int FIELD_REPLACE_FLAG_FORMATING = 2;
    private static final int FIELD_REPLACE_FLAG_ENCODING = 3;
    private static final int TRANSFIX_PADDING_MODE_LEFT = 1;
    private static final int TRANSFIX_PADDING_MODE_RIGHT = 2;
    private static final int DEFAULT_TIMEOUT = 5 * 60;
    // Scripts
    private ArrayList[] m_script = new ArrayList[]{ null, null, null, null };
    private ArrayList m_defStrings = ArrayList.Synchronized(new ArrayList(10));
    private int m_currentScriptType = NO_SCRIPT;
    private int m_currentCommandIndex = -1;
    private ArrayList m_currentCommands = null;
    private MLSRecords m_records = null;
    private Hashtable m_valueCursors = Hashtable.Synchronized(new Hashtable());
    private Hashtable m_variables = Hashtable.Synchronized(new Hashtable());
    // output formatting parameters (controlled by set TransFixlen operator)
    // it is used for replacing /.../ parameters in commands ifval, transmit, transval, transCMAVal.
    // formatting is also applyed to $RecId, but not to $Chr and %nn
    // See function formatString.
    private int m_outputLength = 0;
    // desired output length; <=0 turns formatting off.
    private char m_outputFillChar = ' ';
    // fill character. It is used to extend output string to m_outputLength
    private boolean m_outputRightJustification = false;
    // if true, fill character is added from the right side. If false - from the left.
    private boolean m_outputCut = false;
    // if true, and output length is greater than m_outputLength, the output string is cut. m_outputRightJustification is used to determine the side.
    /**
    * m_case_sensitive specifies, if string search operations in script commands are case case
    * sensitive, or not. Script command set "CaseSensitive=True|False" sets this variable.
    */
    private boolean m_case_sensitive = true;
    /// <summary> If m_paramEncoding is true - all the parameters in transval are encoded.
    /// <p>Encoding is a client specific process. For example, http clients replace all the characters
    /// except ASCII with their codes (space is replaced with %20, for instance), to be able to put the
    /// parameter to the URL.
    /// <p>See command set "ParamEncoding=[true|false]". This parameter works for all commands that
    /// allow replacement of the expression like \ParamName\ and $RecId.
    /// <p> By default m_paramEncoding is true for MainScript and RemScript, and false for PictureScript.
    /// The reason is because 6i doesn't have encoding at all, so many picture scripts rely on the
    /// fact that data is not encoded. Of course we can simply fix them by adding command
    /// set "ParamEncoding=false" at the beginning of the script, but the number of def-files is too big.
    /// We decided that it's quicker to set default param encoding to false in picture scripts.
    /// <p>Example for http:
    /// <br>Suppose we have a Field named Street. User inputs something like "Abbey Road"
    /// <br>transval "http://www.mlsboard.com/search.asp?Street="
    /// <br>---------------- trace:
    /// <br>http://www.mlsboard.com/search.asp?Street=
    /// <br>transval "\Street\"
    /// <br>---------------- trace:
    /// <br>Abbey%20Road
    /// <br>set "ParamEncoding=false"
    /// <br>transval "\Street\"
    /// <br>---------------- trace:
    /// <br>Abbey Road
    /// </summary>
    private boolean m_paramEncoding = true;
    //+-----------------------------------------------------------------------------------+
    //|                                    Constructor                                    |
    //+-----------------------------------------------------------------------------------+
    /**
    * @param engine MLS engine
    */
    protected public MLSScriptClient(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        super(engine);
    }

    //+-----------------------------------------------------------------------------------+
    //|    Functions which provide some info and control to derived implementations       |
    //+-----------------------------------------------------------------------------------+
    protected public int getCurrentScriptType() throws Exception {
        return m_currentScriptType;
    }

    protected public String getCurrentRecordId() throws Exception {
        MLSRecord rec = getCurrentRecord();
        if (rec != null)
            return rec.getRecordID();
         
        return null;
    }

    protected public MLSRecord getCurrentRecord() throws Exception {
        if (m_records == null)
            return getFirstRecord();
         
        return m_records.getCurrentRecord();
    }

    private MLSRecord updateProgress(MLSRecord rec, boolean back) throws Exception {
        if (this instanceof RETSClient)
            return rec;
         
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        if (rec != null)
        {
            if (back)
                engine.setOverallProgress(m_records.size() - m_records.index(),m_records.size());
            else
                engine.setOverallProgress(m_records.index(),m_records.size()); 
            engine.setProgress(0,1);
        }
        else
        {
            engine.setOverallProgress(0,1);
            engine.setProgress(1,1);
        } 
        return rec;
    }

    protected public MLSRecord getNextRecord() throws Exception {
        if (m_records == null)
            return getFirstRecord();
         
        return updateProgress(m_records.getNextRecord(),false);
    }

    protected public MLSRecord getFirstRecord() throws Exception {
        if (m_records == null)
            return updateProgress(null,false);
         
            ;
        return updateProgress(m_records.getFirstRecord(),false);
    }

    protected public MLSRecord getPrevRecord() throws Exception {
        if (m_records == null)
            return getLastRecord();
         
        return updateProgress(m_records.getPrevRecord(),true);
    }

    protected public MLSRecord getLastRecord() throws Exception {
        if (m_records == null)
            return updateProgress(null,true);
         
        return updateProgress(m_records.getLastRecord(),true);
    }

    protected public int getRecordCount() throws Exception {
        if (m_records == null)
            return 0;
         
        return m_records.size();
    }

    protected public boolean waitFor(String str) throws Exception {
        return waitFor(str,DEFAULT_TIMEOUT);
    }

    protected public int getReadStopIndex(String received, String the_end) throws Exception {
        int index;
        String received_for_compares;
        if (m_case_sensitive)
            received_for_compares = received;
        else
            received_for_compares = received.toLowerCase(); 
        for (int i = 0;i < m_defStrings.size();i++)
        {
            // Note that the priority of def-strings is higher than the priority of search-string.
            // This doesn't look logical, but some scripts are written assuming that def-strings have
            // higher priority...
            DefString def_string = (DefString)m_defStrings.get(i);
            String value_Renamed;
            if (m_case_sensitive)
                value_Renamed = def_string.getValue();
            else
                value_Renamed = def_string.getValue().toLowerCase(); 
            index = SupportClass.getIndex(received_for_compares,value_Renamed);
            if (index >= 0)
            {
                index += value_Renamed.length();
                if (index < received.length())
                    unread(received.substring(index));
                 
                switch(def_string.getAction())
                {
                    case DefString.ACTION_CONTINUE: 
                        m_currentCommandIndex--;
                        return index;
                    case DefString.ACTION_END: 
                        throw getEngine().createException(def_string.getMessage());
                    case DefString.ACTION_GOTO: 
                        goTo(def_string.getLabel());
                        return index;
                    case DefString.ACTION_IGNORE: 
                        return index;
                    case DefString.ACTION_STOP: 
                        m_currentCommandIndex = m_currentCommands.size();
                        return index;
                
                }
            }
             
        }
        if (!m_case_sensitive)
            the_end = the_end.toLowerCase();
         
        index = SupportClass.getIndex(received_for_compares,the_end);
        if (index >= 0)
        {
            index += the_end.length();
            if (index < received.length())
                unread(received.substring(index));
             
        }
         
        return index;
    }

    //+-----------------------------------------------------------------------------------+
    //|                 Abstract functions for actual client implementations              |
    //|              Script client will call them as necessary in the process of          |
    //|                                      executing script                             |
    //+-----------------------------------------------------------------------------------+
    /**
    * Connects to the server. It is garanteed that this function will always (and only once) be called
    * at the beginning of the script. Clients may use it for additional initialization before
    * executing script, even if they don't use permanent socket connection.
    * 
    *  @param host IPAddress of server
    * 
    *  @param port IPPort for connecting
    */
    //UPGRADE_NOTE: Access modifiers of method 'connect' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public abstract void connect(String host, int port) throws Exception ;

    /**
    * Disconnects from the server. It is garanteed that this function will always (and only once) be
    * called at the end of the script. Clients may use it to finalize their script job, even if they
    * don't use permanent socket connection. Note that this function is called even if an exception
    * is thrown and script is aborted.
    * 
    *  @param exc exception occured while running script. If no exception occured, this is null.
    */
    //UPGRADE_NOTE: Access modifiers of method 'disconnect' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public abstract void disconnect(Exception exc) throws Exception ;

    /**
    * Writes the string to an output stream.
    *  @param str string to write
    */
    //UPGRADE_NOTE: Access modifiers of method 'write' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public abstract void write(String str) throws Exception ;

    /**
    * Reads a string from input stream.
    *  @param len a requered number of characters.
    * 
    *  @return  read result. It should contain 'len' characters, or less if input stream doesnt have
    * enough data to get 'len' characters. The caller knows that if read(len).length() is less then
    * len, the end of stream is reached. read(len).length() can not be more than len by any sence.
    */
    //UPGRADE_NOTE: Access modifiers of method 'read' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public abstract String read(int len) throws Exception ;

    /// <summary> Returns the string back to the input stream. Returning string back means that next call of read
    /// will return it. The example:
    /// <p>
    /// String str = read ( 10 ); // suppose it returns "0123456789"<br>
    /// unread ( str.substring ( 4 ) );<br>
    /// str = read ( 6 ); // "456789" shall be returned
    /// </summary>
    /// <param name="str">string to put back.
    /// </param>
    protected public abstract void unread(String str) throws Exception ;

    /// <summary> This command is called when it is time for actual data download.
    /// Usually this should be the point of data file creation. But the model doesn't push the
    /// implementation to create file right here. For example http clients may create data file
    /// right int the transmition process (in the write function). It's really up to specific
    /// client, how it creates the file. The only rule is - the file must be created at the end of
    /// a script.
    /// <p>Although it is garanteed that this function will be called at least once per script. If
    /// run() doesn't find ReceiveUntil command in the script, it compulsory calls receive method
    /// before disconnectig.
    /// </summary>
    /// <param name="until">the string which signals the end of data. Use function getReadStopIndex to determine
    /// the end. Until can be null. This means that receive function was not in the script, and now it
    /// is called compulsory before disconnecting.
    /// </param>
    protected public abstract void receive(String until) throws Exception ;

    /**
    * The implementation must set socket timeout to 'timeout' when this function is called.
    *  @param timeout timeout to set.
    */
    protected public abstract void setTimeout(long timeout) throws Exception ;

    /**
    * Read raw data file and cut or remove garbage from. This comes from CommunicationClient.
    * Script client doesn't implement it, leaving it for derived implementations.
    * 
    *  @param datFileName path to the data-file on local machine
    * 
    *  @return  good file's content
    * See the function StartProcStr in XCOMPS.bas
    * It uses StartProcessingString in this function.
    */
    //UPGRADE_NOTE: Access modifiers of method 'prepareDataFile' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public abstract String prepareDataFile(String filename) throws Exception ;

    /**
    * This is for SCRIPTDLL command. Most of the clients don't need to do anything about it, so
    * it's implemented empty by default. But some clients (for example telnet Stellar) should
    * load dll and call some it's functions (they know which functions themselfes).
    * 
    *  @param path dll path.
    */
    protected public void callDLL(String path) throws Exception {
    }

    protected public void runScript() throws Exception {
        run();
    }

    /// <summary> This function is called by ScriptClient before inserting some external
    /// (like security field, search field, cma field or some field from def-file) into
    /// server request.
    /// <p>The inherited class may want to make some conversions of the parameter. For
    /// example, HTTPClient performs writes as URL construction. Users may enter some values
    /// containing incorrect URLs charactes - like spaces, '$', '%', '&' etc. They should
    /// be changed to correct URL form - %20, %24, %25, %26, etc. The HTTPClient
    /// implementation of this function should do it.
    /// <p>Default implementation does nothing - it simply returns parameter.
    /// <p>Note: only user parameters are encoded. The parameters that come from def-file
    /// must contain only legal values for this kind of client.
    /// </summary>
    /// <param name="parameter">user entered parameter to encode.
    /// </param>
    /// <param name="parameterType">one of the costants PARAMETER_TYPE_SEARCH_FIELD,
    /// PARAMETER_TYPE_SECURITY_FIELD, PARAMETER_TYPE_CMA_FIELD or PARAMETER_TYPE_DEF_FIELD
    /// </param>
    /// <returns> encoded parameter
    /// </returns>
    protected public String encodeParameter(String parameter, int parameterType) throws Exception {
        return parameter;
    }

    //+-----------------------------------------------------------------------------------+
    //|               Communication client abstract functions implementations             |
    //+-----------------------------------------------------------------------------------+
    /**
    * @return  always true, because most of the script clients require ip. If some doesn't it always
    * can overwrite this function, returning false.
    */
    public boolean isIpAddressRequired() throws Exception {
        return !getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_IPADDRESS_DISABLED).equals("1");
    }

    /**
    * 
    */
    public void runMainScript() throws Exception {
        if (!getEngine().getBoardSetup().checkSecValues())
            throw getEngine().createException(MLSException.CODE_NO_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_NO_LOGIN_INFO));
         
        if (m_script[SCRIPT_MAIN] == null)
            parseCommandSection(getEngine().getDefParser().getMainScriptContent(),SCRIPT_MAIN);
         
        m_currentScriptType = SCRIPT_MAIN;
        m_records = null;
        try
        {
            runScript();
        }
        finally
        {
            m_currentScriptType = NO_SCRIPT;
        }
    }

    /**
    * 
    */
    public void runPicScript(MLSRecords records) throws Exception {
        if (m_script[SCRIPT_PIC] == null)
        {
            if (!getEngine().getMulitiPictures().isGetMultiPicture() || getEngine().getDefParser().getMPicScriptContent().length() == 0)
                parseCommandSection(getEngine().getDefParser().getPicScriptContent(),SCRIPT_PIC);
            else
                parseCommandSection(getEngine().getDefParser().getMPicScriptContent(),SCRIPT_PIC); 
        }
         
        m_currentScriptType = SCRIPT_PIC;
        m_records = records;
        try
        {
            runScript();
        }
        finally
        {
            m_currentScriptType = NO_SCRIPT;
        }
    }

    /**
    * Launch process of downloading of notes  from MLS Board.
    */
    public void runRemScript(MLSRecords records) throws Exception {
        if (m_script[SCRIPT_REM] == null)
            parseCommandSection(getEngine().getDefParser().getRemScriptContent(),SCRIPT_REM);
         
        m_currentScriptType = SCRIPT_REM;
        m_records = records;
        try
        {
            runScript();
        }
        finally
        {
            m_currentScriptType = NO_SCRIPT;
        }
    }

    /**
    * Launch process of downloading of extra notes/remarks  from MLS Board.
    */
    public void runExRemScript(MLSRecords records) throws Exception {
        if (m_script[SCRIPT_EXTRAVAR] == null)
            parseCommandSection(getEngine().getDefParser().getExVarScriptContent(getEngine().getSectionExtraVarScript()),SCRIPT_EXTRAVAR);
         
        m_currentScriptType = SCRIPT_EXTRAVAR;
        m_records = records;
        try
        {
            runScript();
        }
        finally
        {
            m_script[SCRIPT_EXTRAVAR] = null;
            m_currentScriptType = NO_SCRIPT;
        }
    }

    //+-----------------------------------------------------------------------------------+
    //|                                Private section                                    |
    //+-----------------------------------------------------------------------------------+
    private static class LineBreaker   
    {
        private int m_position;
        private String m_string;
        public LineBreaker(String string_Renamed) throws Exception {
            if (string_Renamed != null)
                m_string = string_Renamed;
            else
                m_string = ""; 
            m_position = 0;
        }

        public String nextLine() throws Exception {
            String result = null;
            if (m_position < m_string.length())
            {
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                int i = SupportClass.getIndex(m_string,'\n',m_position);
                if (i >= 0)
                {
                    if (i > 0 && m_string.charAt(i - 1) == '\r')
                        result = m_string.substring(m_position, (m_position) + ((i - 1) - (m_position)));
                    else
                        result = m_string.Substring(m_position, (i)-(m_position)); 
                    m_position = i + 1;
                }
                else
                {
                    result = m_string.substring(m_position);
                    m_position = m_string.length();
                } 
            }
             
            return result;
        }
    
    }

    private static String removeComments(String line) throws Exception {
        boolean inside_quotes = false;
        int i;
        for (i = 0;i < line.length();i++)
        {
            char c = line.charAt(i);
            if (!inside_quotes)
            {
                if (c == '"')
                    inside_quotes = true;
                else if (c == ';')
                    break;
                  
            }
            else if (c == '"')
                inside_quotes = false;
              
        }
        if (i < line.length())
            line = line.Substring(0, (i)-(0));
         
        return StringSupport.Trim(line);
    }

    /**
    * Parses the script to the sequence of commands for later execution. A kind of "precompile" script.
    * The result is an initialization of appropriate m_script element (script_type).
    * 
    *  @param buffer buffer containing script in text form
    * 
    *  @param script_type script type: 	SCRIPT_MAIN, SCRIPT_PIC, or SCRIPT_REM.
    */
    private void parseCommandSection(String buffer, int script_type) throws Exception {
        m_script[script_type] = ArrayList.Synchronized(new ArrayList(10));
        try
        {
            ArrayList script = m_script[script_type];
            ArrayList if_stack = new ArrayList();
            net.toppro.components.mls.engine.MLSEngine engine = getEngine();
            LineBreaker breaker = new LineBreaker(buffer);
            for (String line = breaker.nextLine();line != null;line = breaker.nextLine())
            {
                line = removeComments(line);
                if (line.length() > 0)
                {
                    int i;
                    for (i = 0;i < line.length();i++)
                        if (!Character.isLetterOrDigit(line.charAt(i)))
                            break;
                         
                    String command = line.Substring(0, (i)-(0)).Trim();
                    String params_Renamed = StringSupport.Trim(line.substring(i));
                    MLSCommand cmd = new MLSCommand(engine,command,SCRIPT_NAME[script_type],script.size());
                    script.add(cmd);
                    cmd.setParams(params_Renamed,",:");
                    switch(cmd.getCommand())
                    {
                        case MLSCommand.CMD_IFHANG: 
                        case MLSCommand.CMD_IFINC: 
                        case MLSCommand.CMD_IFINFIELD: 
                        case MLSCommand.CMD_IFNINC: 
                        case MLSCommand.CMD_IFNVAL: 
                        case MLSCommand.CMD_IFVAL: 
                            if_stack.add(cmd);
                            break;
                        case MLSCommand.CMD_ELSE: 
                            if (!(if_stack.size() == 0))
                            {
                                MLSCommand c = (MLSCommand)SupportClass.StackSupport.pop(if_stack);
                                c.setFalseIndex(script.size() - 1);
                                if_stack.add(cmd);
                            }
                            else
                            {
                                getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_ELSE_WITHOUT_IF,SCRIPT_NAME[script_type]);
                                throw getEngine().createException(MLSException.CODE_DEF_PARSING_ERROR,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_ELSE_WITHOUT_IF));
                            } 
                            break;
                        case MLSCommand.CMD_ENDIV: 
                            if (!(if_stack.size() == 0))
                            {
                                MLSCommand c = (MLSCommand)SupportClass.StackSupport.pop(if_stack);
                                c.setFalseIndex(script.size() - 1);
                            }
                            else
                            {
                                getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_ENDIV_WITHOUT_IF,SCRIPT_NAME[script_type]);
                                throw getEngine().createException(MLSException.CODE_DEF_PARSING_ERROR,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_ENDIF_WITHOUT_IF));
                            } 
                            break;
                    
                    }
                }
                 
            }
            while (!(if_stack.size() == 0))
            {
                // Set all unclosed ifs to finish script on false
                MLSCommand c = (MLSCommand)SupportClass.StackSupport.pop(if_stack);
                String scommand = c.toString();
                c.setFalseIndex(script.size());
                getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_IF_WITHOUT_ENDIV,scommand,SCRIPT_NAME[script_type]);
                getEngine().writeLine("Warning: no endiv for " + scommand);
            }
        }
        catch (MLSException exc)
        {
            m_currentCommandIndex = m_script[script_type].size() - 1;
            m_currentCommands = m_script[script_type];
            exc.setCurrentScriptCommand(getCurrentScriptCommand(),getCurrentScriptComandNumber());
            m_currentCommandIndex = -1;
            m_currentCommands = m_script[script_type] = null;
            throw exc;
        }
        catch (Exception exc)
        {
            MLSException e = getEngine().createException(exc,MLSException.CODE_DEF_PARSING_ERROR,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_SCRIPT_PARSING));
            m_currentCommandIndex = m_script[script_type].size() - 1;
            m_currentCommands = m_script[script_type];
            e.setCurrentScriptCommand(getCurrentScriptCommand(),getCurrentScriptComandNumber());
            m_currentCommandIndex = -1;
            m_currentCommands = m_script[script_type] = null;
            throw e;
        }
    
    }

    /**
    * Parses and launch commands from section with script.
    *  @param script which script to execute. Eather SCRIPT_MAIN, SCRIPT_PIC, or SCRIPT_REM.
    */
    protected public void run() throws Exception {
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        Environment environment = engine.getEnvironment();
        engine.setMessage(STRINGS.get_Renamed(STRINGS.COMMON_EXECUTING_SCRIPT,m_currentScriptType));
        int maxCommandIndex = 0;
        // for progress bar. If we always undate it with m_currentCommandIndex - it sometimes "jumps" (in case of goto).
        m_currentCommands = m_script[m_currentScriptType];
        if (m_currentCommands.size() == 0)
        {
            // No commands - nothing to execute. No sence to make just connect/disconnect without any
            // functionality between them.
            // Although, may be empty script is just for auto-calling receiveUntil (see variable call_receive
            // later in this function). This is reasonable, but we don't support this situation.
            // So script must have at least one command to be executed.
            engine.setProgress(100,100);
            return ;
        }
         
        // clear def strings
        m_defStrings.clear();
        // clear all script variables
        m_variables.Clear();
        // set default section names for [Common] and [RcvData]
        // they may be changed later in the script by set command
        // here we restore them to defauts for the next run
        setCommonSectionName(net.toppro.components.mls.engine.MLSEngine.SECTION_COMMON);
        setRcvDataSectionName(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA);
        // Set output formatting parameters to initial values (no formatting).
        m_outputLength = 0;
        m_outputFillChar = ' ';
        m_outputRightJustification = false;
        m_outputCut = false;
        // Make sure receiveUntil function is called at least once per script
        boolean call_receive = true;
        // clear property field value cursors
        m_valueCursors.Clear();
        // Set compare operations to "case sensitive" by default
        m_case_sensitive = true;
        // Set parameters encoding to true for main and rem scripts, and to false for picture script.
        m_paramEncoding = (m_currentScriptType != SCRIPT_PIC);
        environment.checkStop();
        try
        {
            try
            {
                try
                {
                    connect(engine.getIPAddress(),engine.getPort());
                }
                catch (IOException exc)
                {
                    throw getEngine().createException(MLSException.CODE_UNABLE_TO_CONNECT,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_UNABLE_TO_CONNECT));
                }

                for (m_currentCommandIndex = 0;m_currentCommandIndex < m_currentCommands.size();m_currentCommandIndex++)
                {
                    //set commandName and parameter's Name for this command
                    MLSCommand command = (MLSCommand)m_currentCommands.get(m_currentCommandIndex);
                    String commandName = command.getName();
                    getEngine().writeLine(".....................................................");
                    getEngine().writeLine("\t" + (m_currentCommandIndex + 1) + ": " + command.getName() + " " + command.getParams());
                    // Update progress dialog
                    /*					if ( m_currentCommandIndex > maxCommandIndex )
                    						{
                    						maxCommandIndex = m_currentCommandIndex;
                    						engine.setProgress ( m_currentCommandIndex, m_currentCommands.size() * 2 );
                    						}
                    						else
                    						{*/
                    environment.checkStop();
                    //					}
                    //find current command and run it.
                    switch(command.getCommand())
                    {
                        case MLSCommand.CMD_ABORT: 
                            throw getEngine().createException(command.getStringParam(0));
                        case MLSCommand.CMD_DEFSTRINGS: 
                            defStrings(command.getStringParam(0));
                            break;
                        case MLSCommand.CMD_ENDIV: 
                            break;
                        case MLSCommand.CMD_GOTO: 
                            goTo(command.getStringParam(0));
                            break;
                        case MLSCommand.CMD_IFHANG: 
                            break;
                        case MLSCommand.CMD_IFINC: 
                        case MLSCommand.CMD_IFINFIELD: 
                            if (!ifinc(command.getStringParam(0)))
                                m_currentCommandIndex = command.getFalseIndex();
                             
                            break;
                        case MLSCommand.CMD_IFNINC: 
                            //if not include
                            if (ifinc(command.getStringParam(0)))
                                m_currentCommandIndex = command.getFalseIndex();
                             
                            break;
                        case MLSCommand.CMD_IFNVAL: 
                            //if not value
                            if (ifval(command.getStringParam(0)))
                                m_currentCommandIndex = command.getFalseIndex();
                             
                            break;
                        case MLSCommand.CMD_IFVAL: 
                            //if value
                            if (!ifval(command.getStringParam(0)))
                                m_currentCommandIndex = command.getFalseIndex();
                             
                            break;
                        case MLSCommand.CMD_ELSE: 
                            m_currentCommandIndex = command.getFalseIndex();
                            break;
                        case MLSCommand.CMD_LABEL: 
                            break;
                        case MLSCommand.CMD_LOGFILE: 
                            break;
                        case MLSCommand.CMD_ONERROR: 
                            break;
                        case MLSCommand.CMD_ONHANGUP: 
                            break;
                        case MLSCommand.CMD_PAUSE: 
                            pause(command.getStringParam(0),command.getIntParam(1));
                            break;
                        case MLSCommand.CMD_RECEIVEFILE: 
                            break;
                        case MLSCommand.CMD_RECEIVEUNTIL: 
                            // Make sure receiveUntil function is called at least once per script
                            call_receive = false;
                            if (command.hasParam(1))
                                receiveUntil(command.getStringParam(0),command.getIntParam(1));
                            else
                                receiveUntil(command.getStringParam(0),DEFAULT_TIMEOUT); 
                            break;
                        case MLSCommand.CMD_SET: 
                            set_Renamed(command.getStringParam(0));
                            break;
                        case MLSCommand.CMD_SAY: 
                            break;
                        case MLSCommand.CMD_SCRIPTDLL: 
                            callDLL(command.getStringParam(0));
                            break;
                        case MLSCommand.CMD_SETFIELD: 
                            setField(command.getStringParam(0));
                            break;
                        case MLSCommand.CMD_TRANSCMAVAL: 
                            if (!transCmaVal(command.getStringParam(0)))
                                call_receive = false;
                             
                            break;
                        case MLSCommand.CMD_TRANSMIT: 
                            // Defect #48830. If transCmaVal has nothing to transmit, there should not be receive
                            if (command.hasParam(1))
                                transmit(command.getStringParam(0),command.getIntParam(1));
                            else
                                transmit(command.getStringParam(0),DEFAULT_TIMEOUT); 
                            break;
                        case MLSCommand.CMD_TRANSVAL: 
                            //tramsmit value
                            transval(command.getStringParam(0));
                            break;
                        case MLSCommand.CMD_WAITFOR: 
                            {
                                String waitString = command.getStringParam(0);
                                if (command.hasParam(1))
                                {
                                    if (!waitFor(waitString,command.getIntParam(1)))
                                    {
                                        int i = 3;
                                        int timeout;
                                        do
                                        {
                                            if (!command.hasParam(i))
                                                throw getEngine().createException(MLSException.CODE_TIMEOUT);
                                             
                                            timeout = command.getIntParam(i);
                                            transmit(command.getStringParam(i - 1),timeout);
                                            i += 2;
                                        }
                                        while (!waitFor(waitString,timeout));
                                    }
                                     
                                }
                                else if (!waitFor(waitString))
                                    throw getEngine().createException(MLSException.CODE_TIMEOUT);
                                  
                            }
                            break;
                        default: 
                            engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_UNKNOWN_COMMAND,command.toString(),SCRIPT_NAME[m_currentScriptType]);
                            throw getEngine().createException(MLSException.CODE_DEF_PARSING_ERROR,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_UNKNOWN_COMMAND));
                    
                    }
                }
                m_currentCommandIndex = -1;
                m_currentCommands = null;
                // Make sure receiveUntil function is called at least once per script
                if (call_receive)
                    receiveUntil(null,DEFAULT_TIMEOUT);
                 
            }
            catch (MLSException exc)
            {
                try
                {
                    disconnect(exc);
                }
                catch (Exception e)
                {
                    SupportClass.WriteStackTrace(e, Console.Error);
                }

                exc.setCurrentScriptCommand(getCurrentScriptCommand(),getCurrentScriptComandNumber());
                throw exc;
            }
            catch (Exception exc)
            {
                try
                {
                    disconnect(exc);
                }
                catch (Exception e)
                {
                    SupportClass.WriteStackTrace(e, Console.Error);
                }

                MLSException mls_exc = getEngine().createException(exc,MLSException.CODE_DEF_PARSING_ERROR,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_UNKNOWN_ERROR));
                mls_exc.setCurrentScriptCommand(getCurrentScriptCommand(),getCurrentScriptComandNumber());
                throw mls_exc;
            }
            finally
            {
                m_currentCommandIndex = -1;
                m_currentCommands = null;
            }
            if (!((getCurrentScriptType() == SCRIPT_PIC || getCurrentScriptType() == SCRIPT_EXTRAVAR) && (this instanceof RETSClient || this instanceof HttpClient)))
                engine.setProgress(100,100);
             
            try
            {
                disconnect(null);
            }
            catch (MLSException exc)
            {
                throw exc;
            }
            catch (Exception exc)
            {
                throw getEngine().createException(exc,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_DISCONNECT));
            }
        
        }
        finally
        {
            m_valueCursors.Clear();
        }
    }

    /**
    * A wrapper around write function. Performs some error handling and log writing.
    *  @param str string to transmit.
    */
    private void doWrite(String str) throws Exception {
        getEngine().writeLine("....................... out .........................");
        getEngine().writeLine(str);
        try
        {
            write(str);
        }
        catch (IOException exc)
        {
            throw getEngine().createException(STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_TRANSMIT_IO_ERROR));
        }
    
    }

    private void throwBadCommandFormat(int errorCode, String userMessage) throws Exception {
        MLSCommand command = getCurrentScriptCommand();
        if (command != null)
        {
            getEngine().notifyTechSupport(errorCode,command.toString(),SCRIPT_NAME[m_currentScriptType]);
        }
         
        throw getEngine().createException(MLSException.CODE_DEF_PARSING_ERROR,userMessage);
    }

    /// <summary> Sets formatting parameters.</summary>
    /// <param name="value">a string containing "little language" for setting parameters:
    /// [<number>[!][,<padding-mode><pad-char>]]<br>
    /// Where:<br>
    /// number - desired string length.<br>
    /// ! - if set we have to cut the string if it's length is greater than number.<br>
    /// padding-mode - '<' or '>', which means left and right justification.<br>
    /// pad-char - character to use to extend string to the length 'number'.
    /// </param>
    private void setTransFixlen(String value_Renamed) throws Exception {
        value_Renamed = replaceEscapeChars(value_Renamed);
        value_Renamed = replaceChrFunction(value_Renamed);
        value_Renamed = replaceFieldValues(value_Renamed,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING,PARAMETER_TYPE_SEARCH_FIELD,null);
        m_outputLength = 0;
        m_outputFillChar = ' ';
        m_outputRightJustification = false;
        m_outputCut = false;
        if (value_Renamed.length() > 0)
        {
            net.toppro.components.mls.engine.MLSEngine engine = getEngine();
            int index = 0;
            char c = value_Renamed.charAt(index);
            if (c < '0' || c > '9')
                throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
             
            m_outputLength = c - '0';
            index++;
            while (index < value_Renamed.length())
            {
                c = value_Renamed.charAt(index);
                if (c < '0' || c > '9')
                    break;
                 
                m_outputLength = m_outputLength * 10 + c - '0';
                index++;
            }
            if (index < value_Renamed.length())
            {
                if (c == '!')
                {
                    m_outputCut = true;
                    index++;
                    if (index < value_Renamed.length())
                        c = value_Renamed.charAt(index);
                     
                }
                 
                if (index < value_Renamed.length())
                {
                    if (c != ',')
                        throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
                     
                    index++;
                    if (index >= value_Renamed.length())
                        throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
                     
                    c = value_Renamed.charAt(index);
                    if (c == '<')
                        m_outputRightJustification = false;
                    else if (c == '>')
                        m_outputRightJustification = true;
                    else
                        throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));  
                    index++;
                    if (index >= value_Renamed.length())
                        throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
                     
                    m_outputFillChar = value_Renamed.charAt(index);
                }
                 
            }
             
        }
         
    }

    //+-----------------------------------------------------------------------------------+
    //|                                Script commands                                    |
    //+-----------------------------------------------------------------------------------+
    /**
    * SCRIPT COMMAND: pause
    * Makes a pause in MLS Import process
    * 
    *  @param str - obsolette, TP6i used it.
    * 
    *  @param timeout value for timer
    */
    private void pause(String str, long timeout) throws Exception {
        try
        {
            //UPGRADE_TODO: Method 'java.lang.Thread.sleep' was converted to 'System.Threading.Thread.Sleep' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangThreadsleep_long'"
            Thread.Sleep(new TimeSpan((long)10000 * timeout * 1000));
        }
        catch (System.Threading.ThreadInterruptedException e)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            getEngine().writeLine("Warning: command 'pause " + timeout + "' was interrupted:" + e.toString());
        }
    
    }

    /**
    * SCRIPT COMMAND: transmit
    * Sends the string to the MLS Server, uses timer if it need.
    * 
    *  @param waitStr string, which we send
    * 
    *  @param timeout value for timer
    */
    protected public void transmit(String str, long timeout) throws Exception {
        str = replaceEscapeChars(str);
        str = replaceChrFunction(str);
        str = replaceFieldValues(str,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING | FIELD_REPLACE_FLAG_ENCODING,PARAMETER_TYPE_DEF_FIELD,null);
        timeout *= 1000;
        Environment environment = getEngine().getEnvironment();
        environment.setTimeout(timeout);
        try
        {
            setTimeout(timeout);
            doWrite(str);
        }
        catch (IOException exc)
        {
            throw getEngine().createException(exc,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_READ_IO));
        }
        finally
        {
            environment.resetTimeout();
        }
    }

    /**
    * SCRIPT COMMAND: transval
    * Send the value to the MLS Server. For example: value from the PropertyField.
    * 
    *  @param waitStr value to send
    */
    protected public void transval(String str) throws Exception {
        str = replaceEscapeChars(str);
        str = replaceChrFunction(str);
        str = replacePostfix(str);
        str = replaceRecordId(str,FIELD_REPLACE_FLAG_FORMATING | FIELD_REPLACE_FLAG_ENCODING);
        str = replaceFieldValues(str,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING | FIELD_REPLACE_FLAG_ENCODING,PARAMETER_TYPE_SEARCH_FIELD,null);
        doWrite(str);
    }

    private boolean transCmaVal(String str) throws Exception {
        str = replaceEscapeChars(str);
        str = replaceChrFunction(str);
        str = replaceFieldValues(str,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING | FIELD_REPLACE_FLAG_ENCODING,PARAMETER_TYPE_CMA_FIELD,null);
        if (str.length() <= 0)
            return false;
         
        doWrite(str);
        return true;
    }

    /**
    * SCRIPT COMMAND: ifval
    * Tests value. This value may value from PropertyField, may be value from the MLSPicRecords object.
    * If value not null returns true, else - false.
    * 
    *  @param param the value
    */
    private boolean ifval(String str) throws Exception {
        str = replaceEscapeChars(str);
        str = replaceChrFunction(str);
        str = replaceRecordId(str,0);
        str = StringSupport.Trim(replaceFieldValues(str,0,PARAMETER_TYPE_SEARCH_FIELD,null));
        return str.length() > 0;
    }

    /**
    * SCRIPT COMMAND: ifinc
    * Tests value. This value may value from PropertyField, may be value from the MLSPicRecords object.
    * If value include substring return true, else - false
    * 
    *  @param param the substring's value
    */
    private boolean ifinc(String str) throws Exception {
        int i = str.lastIndexOf(',');
        if (i < 0)
            throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
         
        String search_string = StringSupport.Trim(str.substring(i + 1));
        str = replaceEscapeChars(str.Substring(0, (i)-(0)));
        str = replaceChrFunction(str);
        str = replaceRecordId(str,0);
        str = StringSupport.Trim(replaceFieldValues(str,0,PARAMETER_TYPE_SEARCH_FIELD,null));
        if (!m_case_sensitive)
        {
            search_string = search_string.toLowerCase();
            str = str.toLowerCase();
        }
         
        return SupportClass.getIndex(str,search_string) >= 0;
    }

    /**
    * SCRIPT COMMAND: set
    * Sets value. This value may value for PropertyField, may be value for the MLSPicRecords object.
    * 
    *  @param param the value
    */
    private void set_Renamed(String param) throws Exception {
        int i = SupportClass.getIndex(param,'=');
        if (i < 0)
            throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
         
        String name = param.Substring(0, (i)-(0)).Trim().ToUpper();
        String value_Renamed = StringSupport.Trim(param.substring(i + 1));
        if (name.length() == 0)
            throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_BAD_PARAMETER,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_BAD_PARAMETER_FORMAT));
         
        if (name.equals("COMMONSECT"))
        {
            setCommonSectionName(value_Renamed);
        }
        else if (name.equals("RCVDATASECT"))
        {
            setRcvDataSectionName(value_Renamed);
        }
        else if (name.equals("TRANSFIXLEN"))
        {
            setTransFixlen(value_Renamed);
        }
        else if (name.equals("CASESENSITIVE"))
        {
            m_case_sensitive = value_Renamed.toUpperCase().equals("true".toUpperCase());
        }
        else if (name.equals("PARAMENCODING"))
        {
            m_paramEncoding = value_Renamed.toUpperCase().equals("true".toUpperCase());
        }
        else if (name.equals("LOGFILE") || name.equals("DLOADRES") || name.equals("PROGRESSMSG") || name.equals("DLDRECSCOUNTING") || name.equals("DLDRECSREGISTERING"))
        {
        }
        else
        {
            // We ignore these commands. They don't make sence for version 7
            value_Renamed = replaceEscapeChars(value_Renamed);
            value_Renamed = replaceChrFunction(value_Renamed);
            value_Renamed = replaceRecordId(value_Renamed,FIELD_REPLACE_FLAG_FORMATING);
            value_Renamed = replaceFieldValues(value_Renamed,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING,PARAMETER_TYPE_SEARCH_FIELD,null);
            if (name.charAt(name.length() - 1) == '+')
            {
                name = StringSupport.Trim(name.substring(0, (0) + ((name.length() - 1) - (0))));
                String str = (String)m_variables.get(name);
                if (str != null)
                    value_Renamed = str + value_Renamed;
                 
            }
             
            if (name.startsWith("PARAM_"))
            {
                getEngine().setEngineParameter(name,value_Renamed);
            }
            else
                m_variables.put(name, value_Renamed); 
        }      
    }

    /**
    * SCRIPT COMMAND: setField
    * Sets value. This value may value for PropertyField, may be value for the MLSPicRecords object.
    * 
    *  @param param the value
    */
    private void setField(String str) throws Exception {
        str = replaceRecordId(str,0);
        str = StringSupport.Trim(replaceFieldValues(str,0,PARAMETER_TYPE_SEARCH_FIELD,null));
    }

    private void goTo(String label) throws Exception {
        label = label.toUpperCase();
        for (int i = 0;i < m_currentCommands.size();i++)
        {
            MLSCommand cmd = (MLSCommand)m_currentCommands.get(i);
            if (cmd.getCommand() == MLSCommand.CMD_LABEL && cmd.getStringParam(0).toUpperCase().equals(label))
            {
                m_currentCommandIndex = i - 1;
                return ;
            }
             
        }
        throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_UNKNOWN_LABEL,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_UNKNOWN_LABEL));
    }

    /**
    * 
    */
    private void defStrings(String section) throws Exception {
        DefParser defParser = getEngine().getDefParser();
        m_defStrings.clear();
        String result = " ", s = "";
        int i = 1;
        DefString ds = null;
        while (result.length() != 0)
        {
            s = "String" + i;
            result = defParser.getValue(section,s);
            if (result.length() != 0)
            {
                ds = new DefString();
                ds.setValue(result);
                result = defParser.getValue(section,"MsgBox" + i);
                if (result.length() != 0)
                    ds.setMessage(result);
                 
                result = defParser.getValue(section,"Action" + i);
                if (result.length() != 0)
                    ds.setAction(result);
                 
                m_defStrings.add(ds);
            }
             
            i++;
        }
    }

    /**
    * @param timeout timeout in seconds.
    * 
    *  @return  true - if ok, false - if timeout is expired. Throws exception on all other errors.
    */
    private boolean waitFor(String str, long timeout) throws Exception {
        str = replaceEscapeChars(str);
        str = replaceChrFunction(str);
        str = replaceFieldValues(str,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING,PARAMETER_TYPE_DEF_FIELD,null);
        StringBuilder buffer = new StringBuilder();
        Environment environment = getEngine().getEnvironment();
        boolean result = true;
        timeout *= 1000;
        environment.setTimeout(timeout);
        try
        {
            setTimeout(timeout);
            while (true)
            {
                environment.checkStop();
                String portion = read(1024);
                if (portion.length() > 0)
                {
                    buffer.append(portion);
                    if (getReadStopIndex(buffer.toString(),str) >= 0)
                        break;
                     
                }
                 
            }
        }
        catch (MLSException exc)
        {
            if (exc.getCode() == MLSException.CODE_TIMEOUT)
                result = false;
            else
                throw exc; 
        }
        catch (IOException exc)
        {
            throw getEngine().createException(exc,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_READ_IO));
        }
        finally
        {
            environment.resetTimeout();
            getEngine().writeLine("....................... in ..........................");
            getEngine().writeLine(buffer.toString());
        }
        return result;
    }

    /**
    * SCRIPT COMMAND: ReceiveUntil
    * Receives data from MLS Board until EndOfData flag
    * 
    *  @param param addtional params for this command. For example: value for timer
    */
    protected public void receiveUntil(String str, int timeout) throws Exception {
        if (str != null)
        {
            str = replaceEscapeChars(str);
            str = replaceChrFunction(str);
            str = replaceFieldValues(str,FIELD_REPLACE_FLAG_NOT_EMPTY | FIELD_REPLACE_FLAG_FORMATING,PARAMETER_TYPE_DEF_FIELD,null);
        }
         
        Environment environment = getEngine().getEnvironment();
        timeout *= 1000;
        environment.setTimeout(timeout);
        try
        {
            setTimeout(timeout);
            receive(str);
        }
        catch (IOException exc)
        {
            throw getEngine().createException(exc,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_IO_DATA_RECEIVE));
        }
        finally
        {
            environment.resetTimeout();
        }
    }

    //+-----------------------------------------------------------------------------------+
    //|                  Utility functions for working with parameter strings             |
    //+-----------------------------------------------------------------------------------+
    /*
    		* Performs formatting using format parameters set by set TransFixlen command.
    		* @param str string to format.	
    		*/
    private String formatString(String str) throws Exception {
        if (m_outputLength > 0)
        {
            if (str.length() < m_outputLength)
            {
                StringBuilder buf = new StringBuilder();
                if (m_outputRightJustification)
                {
                    buf.append(str);
                    for (int i = str.length();i < m_outputLength;i++)
                        buf.append(m_outputFillChar);
                }
                else
                {
                    for (int i = str.length();i < m_outputLength;i++)
                        buf.append(m_outputFillChar);
                    buf.append(str);
                } 
                str = buf.toString();
            }
            else if (m_outputCut && str.length() > m_outputLength)
            {
                if (m_outputRightJustification)
                    str = str.Substring(0, (m_outputLength)-(0));
                else
                    str = str.substring(str.length() - m_outputLength); 
            }
              
        }
         
        return str;
    }

    /**
    * An array of escape characters. Some commands should replace them with the codes.
    * For example "aaa^Drrrr^M" should be converted to "aaa\04rrrr\015". An index of a letter
    * in this array is the code to replace the ^Letter sequence.
    * 
    *  @see MLSScriptClient.replaceEscapeChars
    */
    private static final String ESCAPE_CHARACTERS = "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_";
    /**
    * Replaces escape sequences in the form ^Letter with the codes. For example ^M is replaced
    * with 13 (\n), etc.
    * 
    *  @param str string containing escape sequences.
    * 
    *  @return  string with the replaces sequences
    * 
    *  @see MLSScriptClient.ESCAPE_CHARACTERS
    */
    private static String replaceEscapeChars(String str) throws Exception {
        StringBuilder result = new StringBuilder();
        String upper = str.toUpperCase();
        int beg = 0;
        for (int i = SupportClass.getIndex(str,'^');i >= 0;i = SupportClass.getIndex(str,'^',beg))
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            result.append(str.Substring(beg, (i)-(beg)));
            beg = i + 2;
            if (beg <= str.length())
            {
                char code = (char)ESCAPE_CHARACTERS.indexOf((char)upper.charAt(i + 1));
                if (code < 0)
                {
                    result.append('^');
                    result.append(str.charAt(i + 1));
                }
                else
                    result.append(code); 
            }
            else
                result.append('^'); 
        }
        if (beg != 0)
        {
            result.append(str.substring(beg));
            str = result.toString();
        }
         
        return str;
    }

    /**
    * Replaces the instances of $Chr('code') with the code. Code is a decimal integer.
    */
    private static String replaceChrFunction(String str) throws Exception {
        StringBuilder result = new StringBuilder();
        String upper = str.toUpperCase();
        int beg = 0;
        for (int i = SupportClass.getIndex(upper,"$CHR");i >= 0;i = SupportClass.getIndex(upper,"$CHR",beg))
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            result.append(str.Substring(beg, (i)-(beg)));
            char code = (char)(0);
            boolean calculating = false;
            for (beg = i + "$CHR".length();beg < str.length();beg++)
            {
                char c = upper.charAt(beg);
                if (!calculating)
                {
                    if (c == '(')
                    {
                        calculating = true;
                    }
                    else if (c != ' ' && c != '\t')
                    {
                        result.append(str.Substring(i, (beg)-(i)));
                        break;
                    }
                      
                }
                else if (c >= '0' && c <= '9')
                {
                    code = (char)(code * 10 + c - '0');
                }
                else if (c != ' ' && c != '\t')
                {
                    if (c == ')')
                        beg++;
                    else
                        result.append(str.Substring(i, (beg)-(i))); 
                    break;
                }
                   
            }
            if (beg >= str.length())
                result.append(str.substring(i));
            else
                result.append(code); 
        }
        if (beg != 0)
        {
            result.append(str.substring(beg));
            str = result.toString();
        }
         
        return str;
    }

    private String replacePostfix(String str) throws Exception {
        // find postfix
        StringBuilder postfix = null;
        StringBuilder buffer = new StringBuilder();
        int beg = 0;
        for (int i = SupportClass.getIndex(str,'{');i >= 0;i = SupportClass.getIndex(str,'{',beg + 1))
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            buffer.append(str.Substring(beg, (i)-(beg)));
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            beg = SupportClass.getIndex(str,'}',i + 1);
            if (beg < 0)
            {
                beg = i;
                break;
            }
            else
            {
                if (postfix == null)
                    postfix = new StringBuilder();
                 
                postfix.append(str.Substring(i + 1, (beg)-(i + 1)).Trim());
                beg++;
            } 
        }
        buffer.append(str.substring(beg));
        if (postfix != null)
            buffer.append(postfix.toString());
        else if (buffer[buffer.length() - 1] != '\r')
            buffer.append('\r');
          
        return buffer.toString();
    }

    private static final String RECID_START = "$RECID";
    private static final String RECID_CURRENT = "@";
    private static final String RECID_FIRST = "FIRST";
    private static final String RECID_NEXT = "NEXT";
    private static final String RECID_PREV = "PREV";
    private static final String RECID_LAST = "LAST";
    //UPGRADE_NOTE: Field 'EnclosingInstance' was added to class 'FieldValueCursor' to access its enclosing instance. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1019'"
    private static class FieldValueCursor   
    {
        private void initBlock(net.toppro.components.mls.engine.MLSScriptClient enclosingInstance) throws Exception {
            this.enclosingInstance = enclosingInstance;
        }

        private net.toppro.components.mls.engine.MLSScriptClient enclosingInstance;
        public net.toppro.components.mls.engine.MLSScriptClient getEnclosing_Instance() throws Exception {
            return enclosingInstance;
        }

        private net.toppro.components.mls.engine.PropertyField m_field;
        private int m_cursor = 0;
        private int m_size = -1;
        public FieldValueCursor(net.toppro.components.mls.engine.MLSScriptClient enclosingInstance, String field) throws Exception {
            initBlock(enclosingInstance);
            m_field = getEnclosing_Instance().getEngine().getPropertyFields().getField(field);
        }

        public int getSize() throws Exception {
            if (m_size < 0)
            {
                m_size = 0;
                while (m_field.getValue(m_size).length() > 0)
                m_size++;
            }
             
            return m_size;
        }

        public int current() throws Exception {
            return m_cursor;
        }

        public int next() throws Exception {
            if (m_cursor < getSize())
                m_cursor++;
             
            return m_cursor;
        }

        public int prev() throws Exception {
            if (m_cursor < getSize())
                m_cursor--;
             
            if (m_cursor < 0)
                m_cursor = getSize();
             
            return m_cursor;
        }

        public int first() throws Exception {
            m_cursor = 0;
            return m_cursor;
        }

        public int last() throws Exception {
            m_cursor = getSize() - 1;
            if (m_cursor < 0)
                m_cursor = getSize();
             
            return m_cursor;
        }
    
    }

    private FieldValueCursor getFieldValueCursor(String field) throws Exception {
        FieldValueCursor cursor = (FieldValueCursor)m_valueCursors.get(field);
        if (cursor == null)
        {
            cursor = new FieldValueCursor(this,field);
            m_valueCursors.put(field, cursor);
        }
         
        return cursor;
    }

    private String getSubstring(String value_Renamed, String delimiter, int index, boolean not_empty) throws Exception {
        int beg = 0;
        int end = -delimiter.length();
        String value_for_compares;
        if (!m_case_sensitive)
        {
            value_for_compares = value_Renamed.toLowerCase();
            delimiter = delimiter.toLowerCase();
        }
        else
            value_for_compares = value_Renamed; 
        while (index >= 0)
        {
            beg = end + delimiter.length();
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            end = SupportClass.getIndex(value_for_compares,delimiter,beg);
            index--;
            if (end < 0)
                break;
             
        }
        if (index >= 0)
        {
            if (not_empty)
                value_Renamed = StringSupport.Trim(value_Renamed.substring(beg));
            else
                value_Renamed = ""; 
        }
        else if (end < 0)
            value_Renamed = StringSupport.Trim(value_Renamed.substring(beg));
        else
            value_Renamed = value_Renamed.Substring(beg, (end)-(beg)).Trim();  
        return value_Renamed;
    }

    private static class Value   
    {
        public String field;
        public String ufield;
        public String value_Renamed = null;
        public String prefix = "";
        public String delimiter = ",";
        public Value(String fld) throws Exception {
            field = fld;
            ufield = fld.toUpperCase();
        }
    
    }

    private boolean getFieldValue(Value value_Renamed, int paramType, Hashtable testValues) throws Exception {
        switch(paramType)
        {
            case PARAMETER_TYPE_SEARCH_FIELD: 
                if (value_Renamed.ufield.startsWith("FIELDS."))
                {
                    String fieldName = value_Renamed.field.substring(SupportClass.getIndex(value_Renamed.field,'.') + 1);
                    PropertyFieldGroup group = getEngine().getCurrentPropertyFieldGroup();
                    net.toppro.components.mls.engine.PropertyField fld = group.getField(fieldName);
                    if (fld != null)
                    {
                        value_Renamed.delimiter = fld.getDelimiter();
                        value_Renamed.value_Renamed = fld.getValue();
                        value_Renamed.prefix = StringSupport.Trim(fld.getPrefix());
                    }
                     
                    return true;
                }
                else if (value_Renamed.ufield.startsWith("ENGINE."))
                {
                    String __dummyScrutVar4 = value_Renamed.ufield;
                    if (__dummyScrutVar4.equals("ENGINE.RECORDSLIMIT"))
                    {
                        value_Renamed.value_Renamed = String.valueOf(getEngine().getRecordsLimit());
                    }
                    else if (__dummyScrutVar4.equals("ENGINE.AGENTID"))
                    {
                        value_Renamed.value_Renamed = getEngine().getAgentID();
                    }
                    else if (__dummyScrutVar4.equals("ENGINE.OFFICEID"))
                    {
                        value_Renamed.value_Renamed = getEngine().getOfficeID();
                    }
                    else if (__dummyScrutVar4.equals("ENGINE.ISDOWNLOADIDS"))
                    {
                        value_Renamed.value_Renamed = getEngine().getIsDownloadIDs() ? "1" : "";
                    }
                        
                    return true;
                }
                else if (value_Renamed.ufield.startsWith("ENGINE_") || value_Renamed.ufield.startsWith("PARAM_"))
                {
                    value_Renamed.value_Renamed = getEngine().getEngineParameter(value_Renamed.ufield);
                }
                   
                break;
            case PARAMETER_TYPE_SECURITY_FIELD: 
                if (value_Renamed.ufield.startsWith("SECLIST."))
                {
                    BoardSetupField bsfield = getEngine().getBoardSetup().getSecField(value_Renamed.field.substring("SECLIST.".length()));
                    if (bsfield != null)
                        value_Renamed.value_Renamed = bsfield.getValue();
                     
                    return true;
                }
                 
                break;
            case PARAMETER_TYPE_CMA_FIELD: 
                {
                    MLSRecord record = getCurrentRecord();
                    if (record != null)
                        value_Renamed.value_Renamed = record.getFieldValue(value_Renamed.field);
                    else
                        throwBadCommandFormat(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_NO_CURRENT_RECORD,STRINGS.get_Renamed(STRINGS.MLS_SCRIPT_CLIENT_ERR_NO_CURRENT_RECORD)); 
                }
                break;
            case PARAMETER_TYPE_DEF_FIELD: 
                if (value_Renamed.ufield.startsWith("SECLIST."))
                    return false;
                 
                value_Renamed.value_Renamed = getEngine().getDefParser().getValue(value_Renamed.field);
                return value_Renamed.value_Renamed != null && value_Renamed.value_Renamed.length() > 0;
            case PARAMETER_TYPE_VARIABLE: 
                value_Renamed.value_Renamed = ((String)m_variables.get(value_Renamed.ufield));
                break;
            case PARAMETER_TYPE_TEST_VALUE: 
                {
                    TestField fld = (TestField)testValues.get(value_Renamed.field);
                    if (fld != null)
                    {
                        value_Renamed.value_Renamed = fld.value_Renamed;
                        value_Renamed.delimiter = fld.delimiter;
                        value_Renamed.prefix = fld.prefix;
                    }
                     
                }
                break;
        
        }
        return value_Renamed.value_Renamed != null && value_Renamed.value_Renamed.length() > 0;
    }

    private String replaceFieldValues(String str, int flags, int expectedParamType, Hashtable testValues) throws Exception {
        boolean not_empty = ((flags & FIELD_REPLACE_FLAG_NOT_EMPTY) != 0);
        boolean formating = ((flags & FIELD_REPLACE_FLAG_FORMATING) != 0);
        boolean encoding = m_paramEncoding && ((flags & FIELD_REPLACE_FLAG_ENCODING) != 0);
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        int beg = 0;
        StringBuilder buffer = new StringBuilder();
        for (int i = SupportClass.getIndex(str,'\\');i >= 0;i = SupportClass.getIndex(str,'\\',beg))
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            buffer.append(str.Substring(beg, (i)-(beg)));
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            beg = SupportClass.getIndex(str,'\\',i + 1);
            if (beg >= 0)
            {
                String field = str.Substring(i + 1, (beg)-(i + 1)).Trim();
                Value val = new Value(field);
                int paramType;
                // calculate value
                paramType = expectedParamType;
                if (!getFieldValue(val,paramType,testValues))
                {
                    switch(expectedParamType)
                    {
                        case PARAMETER_TYPE_SEARCH_FIELD: 
                            paramType = PARAMETER_TYPE_SECURITY_FIELD;
                            if (getFieldValue(val,paramType,testValues))
                                break;
                             
                            paramType = PARAMETER_TYPE_DEF_FIELD;
                            if (getFieldValue(val,paramType,testValues))
                                break;
                             
                            paramType = PARAMETER_TYPE_VARIABLE;
                            getFieldValue(val,paramType,testValues);
                            break;
                        case PARAMETER_TYPE_DEF_FIELD: 
                            paramType = PARAMETER_TYPE_SECURITY_FIELD;
                            if (getFieldValue(val,paramType,testValues))
                                break;
                             
                            paramType = PARAMETER_TYPE_SEARCH_FIELD;
                            if (getFieldValue(val,paramType,testValues))
                                break;
                             
                            paramType = PARAMETER_TYPE_VARIABLE;
                            getFieldValue(val,paramType,testValues);
                            break;
                    
                    }
                }
                 
                if (val.value_Renamed == null)
                    val.value_Renamed = "";
                 
                String value_Renamed = val.value_Renamed;
                String delimiter = val.delimiter;
                String prefix = val.prefix;
                int index = -1;
                // calculate index
                beg++;
                int param_name_end = beg;
                if (beg < str.length() && str.charAt(beg) == ',')
                {
                    beg++;
                    if (beg < str.length())
                    {
                        String rest = str.substring(beg).toUpperCase();
                        String field_name = field.substring(SupportClass.getIndex(field,'.') + 1);
                        if (rest.startsWith(RECID_CURRENT))
                        {
                            index = getFieldValueCursor(field_name).current();
                            beg += RECID_CURRENT.length();
                        }
                        else if (rest.startsWith(RECID_FIRST))
                        {
                            index = getFieldValueCursor(field_name).first();
                            beg += RECID_FIRST.length();
                        }
                        else if (rest.startsWith(RECID_NEXT))
                        {
                            index = getFieldValueCursor(field_name).next();
                            beg += RECID_NEXT.length();
                        }
                        else if (rest.startsWith(RECID_PREV))
                        {
                            index = getFieldValueCursor(field_name).prev();
                            beg += RECID_PREV.length();
                        }
                        else if (rest.startsWith(RECID_LAST))
                        {
                            index = getFieldValueCursor(field_name).last();
                            beg += RECID_LAST.length();
                        }
                        else
                        {
                            char c = str.charAt(beg);
                            if (c >= '0' && c <= '9')
                            {
                                index = c - '0';
                                for (beg++;beg < str.length();beg++)
                                {
                                    c = str.charAt(beg);
                                    if (c >= '0' && c <= '9')
                                        index = index * 10 + c - '0';
                                    else
                                        break; 
                                }
                                index--;
                            }
                             
                        }     
                    }
                     
                }
                 
                if (index < 0)
                    beg = param_name_end;
                 
                // calculate substring #index
                if (value_Renamed != null && index >= 0)
                    value_Renamed = getSubstring(value_Renamed,delimiter,index,not_empty);
                 
                // find additional substrings
                if (beg < str.length())
                {
                    for (char c = str.charAt(beg);c == ':';c = str.charAt(beg))
                    {
                        int ptr = beg + 1;
                        if (ptr >= str.length() || str.charAt(ptr++) != '(')
                            break;
                         
                        if (ptr >= str.length())
                            break;
                         
                        c = str.charAt(ptr++);
                        if (c == '\'')
                        {
                            StringBuilder delim = new StringBuilder();
                            while (ptr < str.length() && (c = str.charAt(ptr++)) != '\'')
                            delim.append(c);
                            if (delim.length() == 0)
                                break;
                             
                            if (ptr >= str.length() || str.charAt(ptr++) != ',')
                                break;
                             
                            if (ptr >= str.length())
                                break;
                             
                            c = str.charAt(ptr++);
                            if (c < '1' || c > '9')
                                break;
                             
                            index = c - '0';
                            if (ptr < str.length())
                                for (c = str.charAt(ptr++);c >= '0' && c <= '9' && ptr < str.length();c = str.charAt(ptr++))
                                    index = index * 10 + c - '0';
                             
                            if (c != ')')
                                break;
                             
                            value_Renamed = getSubstring(value_Renamed,delim.toString(),index - 1,not_empty);
                        }
                        else if (c >= '1' && c <= '9')
                        {
                            int idx = 0;
                            do
                            {
                                idx = idx * 10 + c - '0';
                                c = str.charAt(ptr++);
                            }
                            while (ptr < str.length() && c >= '0' && c <= '9');
                            int len;
                            if (c == ',')
                            {
                                if (ptr >= str.length())
                                    break;
                                 
                                c = str.charAt(ptr++);
                                if (c < '1' || c > '9')
                                    break;
                                 
                                len = 0;
                                do
                                {
                                    len = len * 10 + c - '0';
                                    c = str.charAt(ptr++);
                                }
                                while (ptr < str.length() && c >= '0' && c <= '9');
                                if (c != ')')
                                    break;
                                 
                            }
                            else if (c == ')')
                            {
                                len = value_Renamed.length();
                            }
                            else
                                break;  
                            idx--;
                            if (idx < value_Renamed.length())
                            {
                                if (idx + len < value_Renamed.length())
                                    value_Renamed = value_Renamed.substring(idx, (idx) + ((idx + len) - (idx)));
                                else
                                    value_Renamed = value_Renamed.substring(idx); 
                            }
                            else
                                value_Renamed = ""; 
                        }
                        else if (c == '-')
                        {
                            c = str.charAt(ptr++);
                            if (c < '1' || c > '9')
                                break;
                             
                            int idx = 0;
                            do
                            {
                                idx = idx * 10 + c - '0';
                                c = str.charAt(ptr++);
                            }
                            while (ptr < str.length() && c >= '0' && c <= '9');
                            int len;
                            if (c == ',')
                            {
                                if (ptr >= str.length())
                                    break;
                                 
                                c = str.charAt(ptr++);
                                if (c < '1' || c > '9')
                                    break;
                                 
                                len = 0;
                                do
                                {
                                    len = len * 10 + c - '0';
                                    c = str.charAt(ptr++);
                                }
                                while (ptr < str.length() && c >= '0' && c <= '9');
                                if (c != ')')
                                    break;
                                 
                            }
                            else if (c == ')')
                            {
                                len = value_Renamed.length();
                            }
                            else
                                break;  
                            idx = value_Renamed.length() - idx;
                            if (idx < 0)
                                idx = 0;
                             
                            if (idx < value_Renamed.length())
                            {
                                if (idx + len < value_Renamed.length())
                                    value_Renamed = value_Renamed.substring(idx, (idx) + ((idx + len) - (idx)));
                                else
                                    value_Renamed = value_Renamed.substring(idx); 
                            }
                            else
                                value_Renamed = ""; 
                        }
                        else
                            break;   
                        beg = ptr;
                        if (beg >= str.length())
                            break;
                         
                    }
                }
                 
                // add prefix
                if (value_Renamed != null && prefix != null && value_Renamed.length() > 0 && prefix.length() > 0)
                {
                    if (prefix.charAt(prefix.length() - 1) == ':')
                        value_Renamed = prefix + value_Renamed;
                    else
                        value_Renamed = prefix + ":" + value_Renamed; 
                }
                 
                if (value_Renamed != null)
                {
                    if (formating)
                        value_Renamed = formatString(value_Renamed);
                     
                    if (encoding)
                        value_Renamed = encodeParameter(value_Renamed,paramType);
                     
                    buffer.append(value_Renamed);
                }
                else
                    buffer.append(str.Substring(i, (beg)-(i))); 
            }
            else
            {
                beg = i;
                break;
            } 
        }
        if (beg != 0)
        {
            if (beg < str.length())
                buffer.append(str.substring(beg));
             
            str = buffer.toString();
        }
         
        return str;
    }

    private String replaceRecordId(String str, int flags) throws Exception {
        boolean formating = ((flags & FIELD_REPLACE_FLAG_FORMATING) != 0);
        boolean encoding = m_paramEncoding && ((flags & FIELD_REPLACE_FLAG_ENCODING) != 0);
        StringBuilder result = new StringBuilder();
        String upper = str.toUpperCase();
        int beg = 0;
        for (int i = SupportClass.getIndex(upper,RECID_START);i >= 0;i = SupportClass.getIndex(upper,RECID_START,beg))
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            result.append(str.Substring(beg, (i)-(beg)));
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            beg = SupportClass.getIndex(str,',',i + RECID_START.length());
            if (beg >= 0)
            {
                beg++;
                String rest = upper.substring(beg);
                if (rest.startsWith(RECID_CURRENT))
                {
                    MLSRecord rec = getCurrentRecord();
                    if (rec != null)
                    {
                        String value_Renamed = rec.getRecordID();
                        if (formating)
                            value_Renamed = formatString(value_Renamed);
                         
                        if (encoding)
                            value_Renamed = encodeParameter(value_Renamed,PARAMETER_TYPE_CMA_FIELD);
                         
                        result.append(value_Renamed);
                    }
                     
                    beg += RECID_CURRENT.length();
                }
                else if (rest.startsWith(RECID_FIRST))
                {
                    MLSRecord rec = getFirstRecord();
                    if (rec != null)
                    {
                        String value_Renamed = rec.getRecordID();
                        if (formating)
                            value_Renamed = formatString(value_Renamed);
                         
                        if (encoding)
                            value_Renamed = encodeParameter(value_Renamed,PARAMETER_TYPE_CMA_FIELD);
                         
                        result.append(value_Renamed);
                    }
                     
                    beg += RECID_FIRST.length();
                }
                else if (rest.startsWith(RECID_NEXT))
                {
                    MLSRecord rec = getNextRecord();
                    if (rec != null)
                    {
                        String value_Renamed = rec.getRecordID();
                        if (formating)
                            value_Renamed = formatString(value_Renamed);
                         
                        if (encoding)
                            value_Renamed = encodeParameter(value_Renamed,PARAMETER_TYPE_CMA_FIELD);
                         
                        result.append(value_Renamed);
                    }
                     
                    beg += RECID_NEXT.length();
                }
                else if (rest.startsWith(RECID_PREV))
                {
                    MLSRecord rec = getPrevRecord();
                    if (rec != null)
                    {
                        String value_Renamed = rec.getRecordID();
                        if (formating)
                            value_Renamed = formatString(value_Renamed);
                         
                        if (encoding)
                            value_Renamed = encodeParameter(value_Renamed,PARAMETER_TYPE_CMA_FIELD);
                         
                        result.append(value_Renamed);
                    }
                     
                    beg += RECID_PREV.length();
                }
                else if (rest.startsWith(RECID_LAST))
                {
                    MLSRecord rec = getLastRecord();
                    if (rec != null)
                    {
                        String value_Renamed = rec.getRecordID();
                        if (formating)
                            value_Renamed = formatString(value_Renamed);
                         
                        if (encoding)
                            value_Renamed = encodeParameter(value_Renamed,PARAMETER_TYPE_CMA_FIELD);
                         
                        result.append(value_Renamed);
                    }
                     
                    beg += RECID_LAST.length();
                }
                else
                {
                    result.append(str.Substring(i, (beg)-(i)));
                }     
            }
            else
            {
                result.append(str.substring(i, (i) + ((i + RECID_START.length()) - (i))));
                beg = i + RECID_START.length();
            } 
        }
        if (beg != 0)
        {
            result.append(str.substring(beg));
            str = result.toString();
        }
         
        return str;
    }

    //+-----------------------------------------------------------------------------------+
    //|                               Debuging functions                                  |
    //+-----------------------------------------------------------------------------------+
    public MLSCommand getCurrentScriptCommand() throws Exception {
        if (m_currentCommandIndex >= 0 && m_currentCommandIndex < m_currentCommands.size())
            return (MLSCommand)m_currentCommands.get(m_currentCommandIndex);
         
        return null;
    }

    public int getCurrentScriptComandNumber() throws Exception {
        return m_currentCommandIndex;
    }

    //+-----------------------------------------------------------------------------------+
    //|								  Serialization										  |
    //+-----------------------------------------------------------------------------------+
    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
    public void save(System.IO.BinaryWriter out_Renamed) throws Exception {
        try
        {
            int count = 0;
            if (m_script[SCRIPT_MAIN] != null)
                count++;
             
            if (m_script[SCRIPT_PIC] != null)
                count++;
             
            if (m_script[SCRIPT_REM] != null)
                count++;
             
            if (m_script[SCRIPT_EXTRAVAR] != null)
                count++;
             
            out_Renamed.Write(count);
            if (m_script[SCRIPT_MAIN] != null)
            {
                out_Renamed.Write(SCRIPT_MAIN);
                out_Renamed.Write(m_script[SCRIPT_MAIN].size());
                for (int i = 0;i < m_script[SCRIPT_MAIN].size();i++)
                {
                    //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                    SupportClass.serialize(out_Renamed,(MLSCommand)m_script[SCRIPT_MAIN].get(i));
                }
            }
             
            if (m_script[SCRIPT_PIC] != null)
            {
                out_Renamed.Write(SCRIPT_PIC);
                out_Renamed.Write(m_script[SCRIPT_PIC].size());
                for (int i = 0;i < m_script[SCRIPT_PIC].size();i++)
                {
                    //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                    SupportClass.serialize(out_Renamed,(MLSCommand)m_script[SCRIPT_PIC].get(i));
                }
            }
             
            if (m_script[SCRIPT_REM] != null)
            {
                out_Renamed.Write(SCRIPT_REM);
                out_Renamed.Write(m_script[SCRIPT_REM].size());
                for (int i = 0;i < m_script[SCRIPT_REM].size();i++)
                {
                    //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                    SupportClass.serialize(out_Renamed,(MLSCommand)m_script[SCRIPT_REM].get(i));
                }
            }
             
            if (m_script[SCRIPT_EXTRAVAR] != null)
            {
                out_Renamed.Write(SCRIPT_EXTRAVAR);
                out_Renamed.Write(m_script[SCRIPT_EXTRAVAR].size());
                for (int i = 0;i < m_script[SCRIPT_EXTRAVAR].size();i++)
                {
                    //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                    SupportClass.serialize(out_Renamed,(MLSCommand)m_script[SCRIPT_EXTRAVAR].get(i));
                }
            }
             
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }
    
    }

    /**
    * 
    */
    //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
    public void load(System.IO.BinaryReader in_Renamed) throws Exception {
        try
        {
            int count = in_Renamed.Read();
            int script = -1;
            for (int j = 0;j < count;j++)
            {
                script = in_Renamed.Read();
                if (m_script[script] != null)
                    m_script[script].clear();
                 
                int size = in_Renamed.Read();
                for (int i = 0;i < size;i++)
                {
                    //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    m_script[script].add((MLSCommand)SupportClass.deserialize(in_Renamed));
                }
            }
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }
    
    }

    private static class TestField   
    {
        public String value_Renamed;
        public String delimiter;
        public String prefix = null;
    }

}


