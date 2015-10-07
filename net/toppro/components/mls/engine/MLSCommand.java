//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:42 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import java.util.ArrayList;
import net.toppro.components.mls.engine.STRINGS;

/**
* class Command - for script language
* each line is Command in MainScript and PicScript sections in DEF-file
*/
public class MLSCommand  extends Object 
{
    public static final int CMD_UNKNOWN = 0;
    public static final int CMD_ABORT = 1;
    public static final int CMD_DEFSTRINGS = 2;
    public static final int CMD_ENDIV = 3;
    public static final int CMD_GOTO = 4;
    public static final int CMD_IFHANG = 5;
    public static final int CMD_IFINC = 6;
    public static final int CMD_IFINFIELD = 7;
    public static final int CMD_IFNINC = 8;
    public static final int CMD_IFNVAL = 9;
    public static final int CMD_IFVAL = 10;
    public static final int CMD_LABEL = 11;
    public static final int CMD_LOGFILE = 12;
    public static final int CMD_ONERROR = 13;
    public static final int CMD_ONHANGUP = 14;
    public static final int CMD_PAUSE = 15;
    public static final int CMD_RECEIVEFILE = 16;
    public static final int CMD_RECEIVEUNTIL = 17;
    public static final int CMD_SET = 18;
    public static final int CMD_SAY = 19;
    public static final int CMD_SCRIPTDLL = 20;
    public static final int CMD_SETFIELD = 21;
    public static final int CMD_TRANSCMAVAL = 22;
    public static final int CMD_TRANSMIT = 23;
    public static final int CMD_TRANSVAL = 24;
    public static final int CMD_WAITFOR = 25;
    public static final int CMD_ELSE = 26;
    private String m_name;
    private int m_falseIndex = 0;
    private String[] m_params = null;
    private String m_allParams = null;
    private int m_command;
    private net.toppro.components.mls.engine.MLSEngine m_engine;
    private String m_scriptName;
    private int m_index;
    public MLSCommand(net.toppro.components.mls.engine.MLSEngine engine, String name, String scriptName, int index) throws Exception {
        m_name = name;
        m_engine = engine;
        m_index = index;
        m_scriptName = scriptName;
        name = name.toUpperCase();
        if (name.equals("ABORT"))
            m_command = CMD_ABORT;
        else if (name.equals("DEFSTRINGS"))
            m_command = CMD_DEFSTRINGS;
        else if (name.equals("ENDIV"))
            m_command = CMD_ENDIV;
        else if (name.equals("GOTO"))
            m_command = CMD_GOTO;
        else if (name.equals("IFHANG"))
            m_command = CMD_IFHANG;
        else if (name.equals("IFINC"))
            m_command = CMD_IFINC;
        else if (name.equals("IFINFIELD"))
            m_command = CMD_IFINFIELD;
        else if (name.equals("IFNINC"))
            m_command = CMD_IFNINC;
        else if (name.equals("IFNVAL"))
            m_command = CMD_IFNVAL;
        else if (name.equals("IFVAL"))
            m_command = CMD_IFVAL;
        else if (name.equals("LABEL"))
            m_command = CMD_LABEL;
        else if (name.equals("LOGFILE"))
            m_command = CMD_LOGFILE;
        else if (name.equals("ONERROR"))
            m_command = CMD_ONERROR;
        else if (name.equals("ONHANGUP"))
            m_command = CMD_ONHANGUP;
        else if (name.equals("PAUSE"))
            m_command = CMD_PAUSE;
        else if (name.equals("RECEIVEFILE"))
            m_command = CMD_RECEIVEFILE;
        else if (name.equals("RECEIVEUNTIL"))
            m_command = CMD_RECEIVEUNTIL;
        else if (name.equals("SET"))
            m_command = CMD_SET;
        else if (name.equals("SAY"))
            m_command = CMD_SAY;
        else if (name.equals("SCRIPTDLL"))
            m_command = CMD_SCRIPTDLL;
        else if (name.equals("SETFIELD"))
            m_command = CMD_SETFIELD;
        else if (name.equals("TRANSCMAVAL"))
            m_command = CMD_TRANSCMAVAL;
        else if (name.equals("TRANSMIT"))
            m_command = CMD_TRANSMIT;
        else if (name.equals("TRANSVAL"))
            m_command = CMD_TRANSVAL;
        else if (name.equals("WAITFOR"))
            m_command = CMD_WAITFOR;
        else if (name.equals("ELSE"))
            m_command = CMD_ELSE;
        else
            m_command = CMD_UNKNOWN;                          
    }

    public String getName() throws Exception {
        return m_name;
    }

    public int getCommand() throws Exception {
        return m_command;
    }

    /**
    * @return  command index in the script.
    */
    public int getIndex() throws Exception {
        return m_index;
    }

    public String getStringParam(int i) throws Exception {
        if (!hasParam(i))
        {
            m_engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_MISSING_PARAMETER,"" + (i + 1),toString(),m_scriptName);
            throw m_engine.createException(STRINGS.get_Renamed(STRINGS.MLS_COMMAND_ERR_NO_PARAM));
        }
         
        return m_params[i];
    }

    public boolean hasParam(int i) throws Exception {
        return m_params != null && i >= 0 && i < m_params.length;
    }

    public int getIntParam(int i) throws Exception {
        String param = getStringParam(i);
        int result;
        try
        {
            result = Integer.valueOf(param);
        }
        catch (Exception exc)
        {
            m_engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_SCRIPT_EXPECTING_INTEGER,"" + (i + 1),toString(),m_scriptName);
            throw m_engine.createException(STRINGS.get_Renamed(STRINGS.MLS_COMMAND_ERR_EXPECTING_INT_PARAM));
        }

        return result;
    }

    public int getParamCount() throws Exception {
        if (m_params == null)
            return 0;
         
        return m_params.length;
    }

    public String getParams() throws Exception {
        return m_allParams;
    }

    public int getFalseIndex() throws Exception {
        return m_falseIndex;
    }

    public void setFalseIndex(int falseIndex) throws Exception {
        m_falseIndex = falseIndex;
    }

    private static String prepareParam(String param) throws Exception {
        param = StringSupport.Trim(param);
        int p = SupportClass.getIndex(param,'"');
        if (p >= 0)
        {
            p++;
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            int p1 = SupportClass.getIndex(param,'"',p);
            if (p1 >= 0)
                param = param.Substring(p, (p1)-(p));
            else
                param = param.substring(p); 
        }
         
        return param;
    }

    public void setParams(String param, String delimiter_chars) throws Exception {
        m_allParams = param;
        // remove quotes
        ArrayList vector = ArrayList.Synchronized(new ArrayList(10));
        int beg = 0;
        boolean inside_quotes = false;
        for (int i = 0;i < param.length();i++)
        {
            char c = param.charAt(i);
            if (c == '"')
                inside_quotes = !inside_quotes;
            else if (!inside_quotes && SupportClass.getIndex(delimiter_chars,(char)c) >= 0)
            {
                vector.add(prepareParam(param.Substring(beg, (i)-(beg))));
                beg = i + 1;
            }
              
        }
        vector.add(prepareParam(param.substring(beg)));
        m_params = new String[vector.size()];
        for (int i = 0;i < m_params.length;i++)
            m_params[i] = ((String)vector.get(i));
    }

    public String toString() {
        try
        {
            if (m_allParams == null)
                return m_name;
             
            return m_name + " " + m_allParams;
        }
        catch (RuntimeException __dummyCatchVar0)
        {
            throw __dummyCatchVar0;
        }
        catch (Exception __dummyCatchVar0)
        {
            throw new RuntimeException(__dummyCatchVar0);
        }
    
    }

}


