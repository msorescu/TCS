//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:42 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.MLSUtil;

public class MLSCmaNotes   
{
    public static final String SECTION_MLSREMARKS = "MLS_Remarks";
    public static final String KEY_INCLUDEMARKS = "IncludeMark";
    public static final String KEY_LINEEND = "LineTerminator";
    public static final String KEY_CHARTRANSF = "CharsTransf";
    public static final String KEY_CHARSTRIP = "CharStrip";
    public static final String KEY_TRIMSPACES = "TrimSpaces";
    public static final String KEY_FIELDCOL = "FieldColumn";
    private DefParser m_parser;
    private String m_includeMark;
    private String m_lineTerminator;
    private String m_charsTransf;
    private String m_charStrip;
    private String m_trimSpaces;
    private int m_fieldColumn;
    public MLSCmaNotes(DefParser parser) throws Exception {
        m_parser = parser;
        m_includeMark = m_parser.getValue(SECTION_MLSREMARKS,KEY_INCLUDEMARKS);
        m_lineTerminator = m_parser.getValue(SECTION_MLSREMARKS,KEY_LINEEND);
        m_charsTransf = m_parser.getValue(SECTION_MLSREMARKS,KEY_CHARTRANSF);
        m_charStrip = m_parser.getValue(SECTION_MLSREMARKS,KEY_CHARSTRIP);
        m_trimSpaces = m_parser.getValue(SECTION_MLSREMARKS,KEY_TRIMSPACES);
        m_fieldColumn = Integer.valueOf(m_parser.getValue(SECTION_MLSREMARKS,KEY_FIELDCOL));
    }

    public String getFormatedNote(String source) throws Exception {
        String result = getNotesPart(source,m_includeMark,m_fieldColumn);
        if (result != null && result.length() > 2)
        {
            if (result.startsWith("\"") && result.endsWith("\""))
                result = result.substring(1, (1) + ((result.length() - 1) - (1)));
             
        }
         
        if (m_trimSpaces != null && m_trimSpaces.length() != 0)
            result = MLSUtil.trimSpaces(result,m_trimSpaces);
         
        if (m_charStrip != null && m_charStrip.length() != 0)
            result = charStrip(result,m_charStrip);
         
        if (m_charsTransf != null && m_charsTransf.length() != 0)
        {
            int type = Integer.valueOf(m_charsTransf);
            result = charTransf(result,type);
        }
         
        return result;
    }

    private String getNotesPart(String item, String includeMark, int colIndex) throws Exception {
        String result = "";
        SupportClass.Tokenizer stMark = new SupportClass.Tokenizer(includeMark,",");
        while (stMark.hasMoreTokens())
        {
            String el = (String)stMark.nextToken();
            if (el != null && StringSupport.Trim(el).length() != 0 && item.indexOf(StringSupport.Trim(el)) != -1)
            {
                result = getNotesPartByIndex(item,"\t",colIndex);
                break;
            }
             
        }
        return StringSupport.Trim(result);
    }

    private String getNotesPartByIndex(String item, String delimeter, int index) throws Exception {
        int i = 0;
        String result = "";
        SupportClass.Tokenizer stItem = new SupportClass.Tokenizer(item,delimeter);
        while (stItem.hasMoreTokens())
        {
            i++;
            if (i == index)
            {
                result = ((String)stItem.nextToken());
                break;
            }
            else
                stItem.nextToken(); 
        }
        return result;
    }

    public String charTransf(String str, int type) throws Exception {
        StringBuilder s = new StringBuilder();
        String s1 = "";
        StringBuilder s2 = new StringBuilder();
        SupportClass.Tokenizer tmpStr = null;
        int i = 0;
        char c;
        boolean first = true;
        switch(type)
        {
            case 0: 
                return str;
            case 1: 
                for (i = 0;i < str.length();i++)
                {
                    c = str.charAt(i);
                    if (c == ' ' || c == '.' || c == ',' || c == ':' || c == ';' || c == '/')
                    {
                        first = true;
                        s.append(c);
                    }
                    else
                    {
                        s2.append(c);
                        if (first == true)
                            s.append(s2.toString().toUpperCase());
                        else
                            s.append(s2.toString().toLowerCase()); 
                        StringBuilderSupport.setLength(s2, 0);
                        first = false;
                    } 
                }
                break;
            case 2: 
                tmpStr = new SupportClass.Tokenizer(str,".");
                while (tmpStr.hasMoreTokens())
                {
                    if (s.length() != 0)
                        s.append(" ");
                     
                    s1 = new StringBuilder((String)tmpStr.nextToken()).toString();
                    s.append(s1.substring(0, (0) + ((1) - (0))).toUpperCase());
                    if (s1.length() > 1)
                        s.append(s1.Substring(1, (s1.Length)-(1)).ToLower());
                     
                }
                break;
            case 3: 
                tmpStr = new SupportClass.Tokenizer(str,"-");
                while (tmpStr.hasMoreTokens())
                {
                    if (s.length() != 0)
                        s.append("-");
                     
                    s1 = new StringBuilder((String)tmpStr.nextToken()).toString();
                    if (s1.equals(str))
                        return str;
                     
                    i = 0;
                    while (i < s1.length() && s1.charAt(i) < 32)
                    s.append(s1.charAt(i++));
                    if (i >= s1.length() && tmpStr.hasMoreTokens() == false)
                        break;
                    else if (i >= s1.length() && tmpStr.hasMoreTokens() == true)
                        continue;
                      
                    StringBuilderSupport.setLength(s2, 0);
                    s2.append(s1.charAt(i++));
                    s.append(s2.toString().toUpperCase());
                    if (s1.length() > i)
                        s.append(s1.Substring(i, (s1.Length)-(i)));
                     
                }
                break;
        
        }
        return s.toString();
    }

    private String charStrip(String str, String charStr) throws Exception {
        String s = str;
        new MLSUtil();
        charStr = MLSUtil.toStr(charStr);
        StringBuilder sb = new StringBuilder();
        int j;
        for (int i = 0;i < charStr.length();i++)
        {
            for (j = 0;j < s.length();j++)
            {
                if (s.charAt(j) != charStr.charAt(i))
                    sb.append(s.charAt(j));
                 
            }
            s = sb.toString();
            sb = new StringBuilder();
        }
        return s;
    }

}


