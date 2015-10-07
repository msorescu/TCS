//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;


/**
* class DefString provides information from DefString's sections
*/
public class DefString   
{
    /**
    * Action command. Terminate script processing, do NOT make grid file afterwords.
    */
    public static final int ACTION_END = 0;
    /**
    * Action command. Terminate script processing, DO make grid file afterwords.
    */
    public static final int ACTION_STOP = 1;
    /**
    * Action command. Keep script file processing from the NEXT line.
    */
    public static final int ACTION_IGNORE = 2;
    /**
    * Action command. Keep script file processing from the CURRENT line
    */
    public static final int ACTION_CONTINUE = 3;
    /**
    * Action command. use name of label to GOTO.
    */
    public static final int ACTION_GOTO = 4;
    /* Name of DefString */
    private String value_Renamed = "";
    /**
    * The label to go, if action is ACTION_GOTO.
    */
    private String m_label = "";
    /**
    * An action to execute, if def string was found in the server responce.
    * Possible values are ACTION_UNKNOWN, ACTION_END, ACTION_STOP, ACTION_IGNORE, ACTION_CONTINUE.
    */
    private int m_action = ACTION_END;
    /* Message, which descript this action*/
    private String message = "";
    public DefString() throws Exception {
    }

    public void setValue(String _value) throws Exception {
        value_Renamed = _value;
    }

    public void setAction(String action) throws Exception {
        String uaction = action.toUpperCase();
        if (uaction.equals("END"))
        {
            m_label = "";
            m_action = ACTION_END;
        }
        else if (uaction.equals("STOP"))
        {
            m_label = "";
            m_action = ACTION_STOP;
        }
        else if (uaction.equals("IGNORE") || uaction.equals("__RETURN__"))
        {
            m_label = "";
            m_action = ACTION_IGNORE;
        }
        else if (uaction.equals("CONTINUE"))
        {
            m_label = "";
            m_action = ACTION_CONTINUE;
        }
        else
        {
            int i = action.indexOf('"');
            if (i >= 0)
            {
                i++;
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                int j = SupportClass.getIndex(action,'"',i);
                if (j >= 0)
                    m_label = action.Substring(i, (j)-(i));
                else
                    m_label = action.substring(i); 
            }
            else
                m_label = action; 
            m_action = ACTION_GOTO;
        }    
    }

    public void setMessage(String _message) throws Exception {
        message = _message;
    }

    public String getValue() throws Exception {
        return value_Renamed;
    }

    public int getAction() throws Exception {
        return m_action;
    }

    public String getLabel() throws Exception {
        return m_label;
    }

    public String getMessage() throws Exception {
        return message;
    }

}


