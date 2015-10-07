//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:43 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Text.StringBuilderSupport;
import net.toppro.components.mls.engine.MLSCommand;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.STRINGS;

/**
* class MlsException - special Exception for MLS Engine
*/
public class MLSException  extends Exception 
{
    /**
    * @return  error message. It can be null.
    */
    public String getMessage() throws Exception {
        return m_message;
    }

    /**
    * The format of the message is simple text.
    */
    public static final int FORMAT_TEXT = 0;
    /**
    * The format of the message is HTML.
    */
    public static final int FORMAT_HTML = 1;
    /**
    * Most of the MLS exceptions should be thrown with this code.
    * This usually means that the exception should simply be shown to the users.
    * No decision making is required.
    */
    public static final int CODE_GENERIC = 0;
    /**
    * The script for ambiance client is not installed.
    * We understand it's too specific, but currently there is no way to design it in
    * client independent way.
    */
    public static final int CODE_AMBIANCE_SCRIPT_NOT_INSTALLED = 1;
    /**
    * Operation timeout. An implementation of IEnvironment must throw an exceptions
    * with this code in case of timeouts. See progress control functions of IEnvironment.
    */
    public static final int CODE_TIMEOUT = 2;
    /**
    * Request for SafeMLS PIN number.
    * We understand it's too specific, but currently there is no way to design it in
    * client independent way.
    */
    public static final int CODE_SAFEMLS_PIN_REQUEST = 3;
    public static final int CODE_NATIVE_MLS_ERROR = 4;
    public static final int CODE_UNABLE_TO_CONNECT = 5;
    public static final int CODE_UNEXPECTED_SERVER_RESPONSE = 6;
    public static final int CODE_NO_RECORD_FOUND = 7;
    public static final int CODE_NO_USERNAME_PASSWORD = 8;
    public static final int CODE_INVALID_USERNAME_PASSWORD = 9;
    public static final int CODE_DEF_PARSING_ERROR = 10;
    public static final int CODE_EXCEED_MAX_DOWNLOAD_SIZE = 11;
    public static final int CODE_REMOTE_SERVER_DOWN = 12;
    public static final int CODE_QUERY_LIMIT_EXCEEDED = 20210;
    public static final int CODE_DUPLICATE_SEARCH_CODE_RETS_PARAMETER = 60384;
    private int m_code;
    private String m_message;
    private StringBuilder m_developerMessage = new StringBuilder();
    private int m_format;
    protected public MLSException(int code) throws Exception {
        m_code = code;
        switch(code)
        {
            case CODE_TIMEOUT: 
                m_message = STRINGS.get_Renamed(STRINGS.MLS_EXCEPTION_ERR_SCRIPT_TIMEOUT);
                break;
            default: 
                m_message = null;
                break;
        
        }
        addDeveloperMessage(readStackTrace(this));
    }

    /**
    * Sets exception with special message in special format.
    *  @param exc exception which caused throwing of this MLSException.
    * 
    *  @param message Error message
    * 
    *  @param developer_message An info that should be added to developer message.
    * 
    *  @param format format of the message. Currently: TEXT, or HTML.
    */
    protected public MLSException(Exception exc, String message, String developer_message, int format) throws Exception {
        m_message = message;
        if (exc instanceof MLSException)
            m_code = ((MLSException)exc).getCode();
        else
            m_code = MLSException.CODE_GENERIC; 
        m_format = format;
        if (developer_message != null)
            addDeveloperMessage(developer_message);
         
        //addDeveloperMessage ( readStackTrace ( this ) );
        if (exc != null)
            addDeveloperMessage(exc);
         
    }

    /**
    * Sets exception with special message in special format.
    *  @param exc exception which caused throwing of this MLSException.
    * 
    *  @param code Error code
    * 
    *  @param message Error message
    * 
    *  @param developer_message An info that should be added to developer message.
    * 
    *  @param format format of the message. Currently: TEXT, or HTML.
    */
    protected public MLSException(Exception exc, int code, String message, String developer_message, int format) throws Exception {
        m_message = message;
        m_code = code;
        m_format = format;
        if (developer_message != null)
            addDeveloperMessage(developer_message);
         
        addDeveloperMessage(readStackTrace(this));
        if (exc != null)
            addDeveloperMessage(exc);
         
    }

    /**
    * @return  exception code. One of the CODE_XXX constants specified above.
    */
    public int getCode() throws Exception {
        return m_code;
    }

    /**
    * Sets exception message only if exception's message is null (not initialized).
    *  @param message message to set.
    */
    public void setMessageIfMissing(String message) throws Exception {
        if (m_message == null)
            m_message = message;
         
    }

    /**
    * @return  developer message.
    */
    public String getDeveloperMessage() throws Exception {
        return m_developerMessage.toString();
    }

    /**
    * @return  message format. One of FORMAT_XXX constants.
    */
    public int getFormat() throws Exception {
        return m_format;
    }

    /**
    * If an exception occurs while script execution, MLSEngine calls this function
    * to provide the information about current script command.
    */
    public void setCurrentScriptCommand(MLSCommand command, int number) throws Exception {
        if (command != null)
        {
            m_developerMessage.append("\n-------------------------------------------------\n");
            m_developerMessage.append(number);
            m_developerMessage.append(": ");
            m_developerMessage.append(command.getName());
            m_developerMessage.append(' ');
            m_developerMessage.append(command.getParams());
            m_developerMessage.append("\n\n");
            m_developerMessage.append("\nParameters were parsed as:\n");
            for (int i = 0;i < command.getParamCount();i++)
            {
                m_developerMessage.append('"');
                try
                {
                    m_developerMessage.append(command.getStringParam(i));
                }
                catch (MLSException never)
                {
                }

                // Impossible program should never come to this point, because
                // getStringParam throws IMLSException only if i >= command.getParamCount()
                // which is a condition of the loop.
                m_developerMessage.append("\"\n");
            }
            m_developerMessage.append('\n');
        }
         
    }

    public void addDeveloperMessage(String message) throws Exception {
        if (m_developerMessage.length() > 0)
            m_developerMessage.append("\n-------------------------------------------------\n");
         
        m_developerMessage.append(message);
    }

    public void addDeveloperMessage(Exception exc) throws Exception {
        if (exc instanceof MLSException)
        {
            MLSException e = (MLSException)exc;
            setMessageIfMissing(e.m_message);
            addDeveloperMessage(e.m_developerMessage.toString());
        }
        else
            addDeveloperMessage(readStackTrace(exc)); 
    }

    /**
    * @param exc a source of the stack trace.
    * 
    *  @return  a string containing the stack trace for the exc.
    */
    //UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
    public static String readStackTrace(Exception exc) throws Exception {
        return exc.getStackTrace().toString();
    }

}


//System.IO.StringWriter sw = new System.IO.StringWriter();
//UPGRADE_ISSUE: Constructor 'java.io.PrintWriter.PrintWriter' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javaioPrintWriterPrintWriter_javaioWriter'"
//System.IO.StreamWriter pw = new System.IO.StreamWriter(;
//SupportClass.WriteStackTrace(exc, pw);
//return sw.ToString();