//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:49 PM
//

package net.toppro.components.mls.engine;

import java.util.ArrayList;

public class StringTokenizerEx   
{
    public static final int MODE_TAIL_INCLUDE = 0;
    public static final int MODE_HEAD_INCLUDE = 1;
    public static final int MODE_TAIL_NOT_INCLUDE = 2;
    private String m_Delimiter;
    private String m_Str;
    private String m_CurrentElement;
    private String[] m_Tokens = null;
    private int m_Position;
    private int m_Mode;
    private int m_CurrentIndex;
    public StringTokenizerEx(String _str, String _delimiter) throws Exception {
        m_Delimiter = _delimiter;
        m_CurrentElement = "";
        m_Str = _str;
        m_Position = 0;
        m_CurrentIndex = -1;
        setMode(MODE_TAIL_NOT_INCLUDE);
    }

    public void setMode(int _Mode) throws Exception {
        m_Mode = _Mode;
    }

    public int getMode() throws Exception {
        return m_Mode;
    }

    public String nextElement() throws Exception {
        return m_CurrentElement;
    }

    public boolean hasMoreElements() throws Exception {
        if (m_Str == null || m_Str.length() == 0)
            return false;
         
        if (m_Position == m_Str.length())
            return false;
         
        int result = -1;
        if (m_Tokens != null && m_CurrentIndex + 1 < m_Tokens.length)
        {
            m_CurrentIndex++;
            m_CurrentElement = m_Tokens[m_CurrentIndex];
            m_Position = 0;
            return true;
        }
        else if (m_Tokens != null)
        {
            m_CurrentElement = "";
            m_Position = m_Str.length();
            return false;
        }
          
        switch(m_Mode)
        {
            case MODE_TAIL_NOT_INCLUDE: 
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                result = SupportClass.getIndex(m_Str,m_Delimiter,m_Position);
                if (result == -1 && m_Position == 0)
                {
                    m_CurrentElement = m_Str;
                    m_Position = m_Str.length();
                    m_CurrentIndex++;
                    return true;
                }
                else if (result == -1 && m_Position != 0 && m_Position <= m_Str.length())
                {
                    m_CurrentElement = m_Str.Substring(m_Position, (m_Str.Length)-(m_Position));
                    m_Position = m_Str.length();
                    m_CurrentIndex++;
                    return true;
                }
                else //new add for the first record
                if (result == 0 && m_Position == 0)
                {
                    m_CurrentElement = "";
                    m_Position = result + m_Delimiter.length();
                    m_CurrentIndex++;
                    return true;
                }
                else if (result != -1)
                {
                    m_CurrentElement = m_Str.Substring(m_Position, (result)-(m_Position));
                    m_Position = result + m_Delimiter.length();
                    m_CurrentIndex++;
                    return true;
                }
                    
                return false;
            case MODE_HEAD_INCLUDE: 
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                result = SupportClass.getIndex(m_Str,m_Delimiter,m_Position);
                if (result == -1 && m_Position == 0)
                {
                    m_CurrentElement = "";
                    m_Position = m_Str.length();
                    return false;
                }
                else if (result == -1 && m_Position != 0)
                {
                    m_CurrentElement = m_Str.Substring(m_Position - m_Delimiter.length(), (m_Str.Length)-(m_Position - m_Delimiter.length()));
                    m_Position = m_Str.length();
                    m_CurrentIndex++;
                    return true;
                }
                else if (result != -1 && m_Position == 0)
                {
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    int p1 = SupportClass.getIndex(m_Str,m_Delimiter,result + 1);
                    if (p1 == -1)
                    {
                        m_CurrentElement = m_Str.Substring(result, (m_Str.Length)-(result));
                        m_Position = m_Str.length();
                    }
                    else
                    {
                        m_CurrentElement = m_Str.Substring(result, (p1)-(result));
                        m_Position = p1 + m_Delimiter.length();
                    } 
                    m_CurrentIndex++;
                    return true;
                }
                else if (result != -1 && m_Position != 0)
                {
                    m_CurrentElement = m_Str.Substring(m_Position - m_Delimiter.length(), (result)-(m_Position - m_Delimiter.length()));
                    m_Position = result + m_Delimiter.length();
                    m_CurrentIndex++;
                    return true;
                }
                    
                break;
            case MODE_TAIL_INCLUDE: 
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                result = SupportClass.getIndex(m_Str,m_Delimiter,m_Position);
                if (result == -1 && m_Position == 0)
                {
                    m_CurrentElement = m_Str;
                    m_Position = m_Str.length();
                    m_CurrentIndex++;
                    return true;
                }
                else if (result == -1 && m_Position != 0 && m_Position <= m_Str.length())
                {
                    m_CurrentElement = "";
                    m_CurrentElement = m_Str.Substring(m_Position, (m_Str.Length)-(m_Position));
                    // Fort McMurray REB board has records for this case
                    m_Position = m_Str.length();
                    return true;
                }
                else //new add for the first record
                if (result == 0 && m_Position == 0)
                {
                    String r1 = m_Str.substring(m_Delimiter.length());
                    int p1 = r1.indexOf(m_Delimiter);
                    if (p1 != -1)
                        m_CurrentElement = r1.Substring(0, (p1)-(0));
                    else
                        m_CurrentElement = r1; 
                    m_Position = result + 2 * m_Delimiter.length() + m_CurrentElement.length();
                    m_CurrentIndex++;
                    return true;
                }
                else if (result != -1)
                {
                    m_CurrentElement = m_Str.substring(m_Position, (m_Position) + ((result + m_Delimiter.length()) - (m_Position)));
                    m_Position = result + m_Delimiter.length();
                    m_CurrentIndex++;
                    return true;
                }
                    
                return false;
        
        }
        return false;
    }

    public int countTokens() throws Exception {
        String CurrentElement = m_CurrentElement;
        String Str = m_Str;
        int Position = m_Position;
        int CurrentIndex = m_CurrentIndex;
        ArrayList tokens = ArrayList.Synchronized(new ArrayList(10));
        String buffer = "";
        while (hasMoreElements())
        tokens.add(nextElement());
        if (tokens.size() > 0)
        {
            m_Tokens = new String[tokens.size()];
            for (int i = 0;i < tokens.size();i++)
                m_Tokens[i] = ((String)tokens.get(i));
        }
         
        m_CurrentElement = CurrentElement;
        m_Str = Str;
        m_Position = Position;
        m_CurrentIndex = CurrentIndex;
        return tokens.size();
    }

}


