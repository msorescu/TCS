//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:40 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Reflection.BindingFlags;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.TypeSupport;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.FieldBase;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.StringTokenizerEx;
import Tcs.Mls.TCSStandardResultFields;

/**
* class CmaField for interaction with UI
* Really there ara columns of result in grid
*/
public class CmaField  extends FieldBase 
{
    private void initBlock() throws Exception {
        m_stdId = TCSStandardResultFields.CUSTOM_FIELD;
    }

    public static final String CMA_FIELD_CAPTION = "ColCaption";
    public static final String CMA_FIELD_DELIMITER = "Delimiter";
    public static final String CMA_FIELD_WIDTH = "ColWidth";
    public static final String CMA_FIELD_TRIMSPACES = "TrimSpaces";
    public static final String CMA_FIELD_ALIGN = "ColAlign";
    public static final String CMA_FIELD_TPOALIGN = "TPOColAlign";
    public static final String CMA_FIELD_CHARSTRANSF = "CharsTransf";
    public static final String CMA_FIELD_CHARSTRIP = "CharStrip";
    public static final String CMA_FIELD_COMBINE = "Combine";
    public static final String CMA_FIELD_SUBSTTABLE = "SubstTable";
    public static final String CMA_FIELD_PREFIX = "Prefix";
    public static final String CMA_FIELD_POSTFIX = "Postfix";
    public static final String CMA_FIELD_DATEFORMAT = "DateFormat";
    public static final String CMA_FIELD_DEFAULT = "Default";
    public static final String CMA_FIELD_UNITOFAREA = "UnitOfArea";
    public static final String CMA_FIELD_DISPLAYNAME = "DisplayName";
    public static final String CMA_FIELD_DISPLAYRULE = "DisplayRule";
    public static final String CMA_FIELD_RETSLONGNAME = "RETSLongName";
    public static final String CMA_FIELD_CATEGORY = "Category";
    public static final String CMA_FIELD_ORDERINCATEGORY = "OrderInCategory";
    public static final String CMA_FIELD_DISPLAYMASK = "DisplayFormatString";
    public static final String CMA_FIELD_NEWDISPLAYRULE = "AttributeExposure";
    //public const string CMA_FIELD_DISPLAYLISTASBULLETS = "DisplayListAsBullets";
    public static final String CMA_FIELD_DISPLAY_BEHAVIOR = "DisplayBehaviorBitmask";
    public static final String CMA_FIELD_DYNAMIC_NAME = "DynamicName";
    private static final String REC_FLDTYPE_TEXT = "S";
    private static final String REC_FLDTYPE_DATE = "D";
    private static final String REC_FLDTYPE_CURRENCY = "L";
    private static final String REC_FLDTYPE_NUMBER = "I";
    private static final String REC_FLDTYPE_BOOLEAN = "B";
    private static final String REC_FLDTYPE_DATETIME = "DT";
    private static final int ALIGN_LEFT = 0x00;
    private static final int ALIGN_CENTER = 0x01;
    private static final int ALIGN_RIGHT = 0x02;
    public static final int CMA_FLDTYPE_NUMBER = 1;
    public static final int CMA_FLDTYPE_TEXT = 2;
    public static final int CMA_FLDTYPE_DATE = 3;
    public static final int CMA_FLDTYPE_CURRENCY = 4;
    public static final int CMA_FLDTYPE_BOOLEAN = 5;
    public static final int CMA_FLDTYPE_RICHTEXT = 6;
    public static final int CMA_FLDTYPE_DATETIME = 7;
    private static final String COMBINE_MAX = "MAX";
    private static final String COMBINE_MIN = "MIN";
    private static final String COMBINE_ALL = "ALL";
    public static final String COMBINE_FNB = "FNB";
    private static final String COMBINE_SUM = "SUM";
    private static final String CMA_FLD_COLUMN_ALIGN_RIGHT = "Right";
    private static final String CMA_FLD_COLUMN_ALIGN_CENTER = "Center";
    public static final int CHAR_TRANSF_NOTHING = 0;
    public static final int CHAR_TRANSF_FIRST_CHAR_OF_WORD = 1;
    public static final int CHAR_TRANSF_FIRST_CHAR_OF_SENT = 2;
    public static final int CHAR_TRANSF_FIRST_CHAR_OF_UNKNOWN = 3;
    public String trimspaces;
    public String colalign;
    public String charstrip;
    public String combine;
    public String substtable;
    public String dateformat;
    public String prefix;
    public String postfix;
    public String cutby;
    public String takeafter;
    public String multrecs;
    public String tpocolalign;
    public String defaultvalue;
    public String unitofarea;
    public String displayname;
    public String retsLongName;
    public String displayrule;
    public String[] methodParams;
    public String category;
    public String orderincategory;
    public String displayformatstring;
    public String attributeexposure;
    public String displaybehaviorbitmask;
    public String dynamicname;
    private String m_SubstTableName = "";
    private String m_RecordPosition;
    private ArrayList m_substWith;
    private ArrayList m_substStrings;
    private ArrayList m_scriptStrings;
    private Hashtable m_hashTableReplaceDelimVal = new Hashtable();
    //UPGRADE_NOTE: The initialization of  'm_stdId' was moved to method 'InitBlock'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1005'"
    private int m_stdId;
    public int charstransf;
    public int inplength;
    public int numreps;
    private boolean m_Visible = false;
    private boolean m_CmaField = false;
    private int m_SubIndex1, m_SubIndex2;
    private int m_Rank;
    private int m_nRecordPosition;
    private String[] m_ContFieldNames = null;
    //private MLSEngine m_engine;
    public CmaField() throws Exception {
        initBlock();
        //m_engine = engine;
        m_RecordPosition = "";
        trimspaces = "";
        colalign = "";
        tpocolalign = "";
        charstrip = "";
        combine = "";
        cutby = "";
        takeafter = "";
        dateformat = "";
        prefix = "";
        postfix = "";
        substtable = "";
        multrecs = "";
        defaultvalue = "";
        unitofarea = "";
        displayname = "";
        retsLongName = "";
        displayrule = "";
        dynamicname = "";
        methodParams = null;
        m_Visible = false;
        charstransf = 0;
        inplength = 0;
        numreps = 0;
        m_SubIndex1 = m_SubIndex2 = -1;
        m_Rank = 0;
        m_nRecordPosition = -1;
        m_substWith = ArrayList.Synchronized(new ArrayList(10));
        m_substStrings = ArrayList.Synchronized(new ArrayList(10));
        m_scriptStrings = ArrayList.Synchronized(new ArrayList(10));
        m_ContFieldNames = null;
    }

    public void setSubstTableName(String _name) throws Exception {
        m_SubstTableName = _name;
    }

    public void setMethodParams(String[] _name) throws Exception {
        methodParams = _name;
    }

    public void setColumnWidth(String _value) throws Exception {
        new MLSUtil();
        if (MLSUtil.isNumber(_value) == true)
            super.setWidth(Integer.valueOf(_value));
        else
            super.setWidth(0); 
    }

    public void addSubstString(String ss) throws Exception {
        m_substStrings.add(MLSUtil.toStr(ss));
    }

    public void addSubstWith(String sw) throws Exception {
        m_substWith.add(MLSUtil.toStr(sw));
    }

    public void addScriptString(String ss) throws Exception {
        ss = StringSupport.Trim(ss);
        String[] aParams = new String[0];
        if (ss.startsWith("if"))
        {
            ss = StringSupport.Trim(ss.substring(ss.indexOf("\"")));
            aParams = MLSUtil.stringSplitEx("if," + ss,",");
        }
        else if (ss.startsWith("value"))
        {
            ss = StringSupport.Trim(ss.substring(ss.indexOf("=") + 1));
            aParams = new String[]{ "value", ss };
        }
        else //MLSUtil.stringSplitEx("value+" + ss,"+");
        if (ss.startsWith("else"))
        {
            aParams = new String[]{ "else" };
        }
        else if (ss.startsWith("endif"))
        {
            aParams = new String[]{ "endif" };
        }
            
        for (int i = 0;i < aParams.length;i++)
        {
            aParams[i] = MLSUtil.toStr(StringSupport.Trim(aParams[i].replace('"', ' ')));
        }
        m_scriptStrings.add(aParams);
    }

    public void addReplaceDelimVal(String value) throws Exception {
        value = MLSUtil.toStr(value);
        String[] lists = StringSupport.Split(value, '|');
        m_hashTableReplaceDelimVal.put(StringSupport.Trim(lists[0].toUpperCase()), StringSupport.Trim(lists[1]));
    }

    public boolean hasScripts() throws Exception {
        return m_scriptStrings.size() > 0;
    }

    public ArrayList getScripts() throws Exception {
        return m_scriptStrings;
    }

    public void setFieldType(String _type) throws Exception {
        if (MLSCmaFields.isStdField(this,TCSStandardResultFields.STDF_CMAIDENTIFIER))
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT);
        else if (_type.equals(net.toppro.components.mls.engine.CmaField.REC_FLDTYPE_CURRENCY))
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY);
        else if (_type.equals(net.toppro.components.mls.engine.CmaField.REC_FLDTYPE_DATE))
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE);
        else if (_type.equals(net.toppro.components.mls.engine.CmaField.REC_FLDTYPE_NUMBER))
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER);
        else if (_type.equals(net.toppro.components.mls.engine.CmaField.REC_FLDTYPE_DATETIME))
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME);
        else if (_type.equals(net.toppro.components.mls.engine.CmaField.REC_FLDTYPE_BOOLEAN))
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_BOOLEAN);
        else
            setType(net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT);      
    }

    public void setRecordPosition(int _value) throws Exception {
        m_nRecordPosition = _value;
    }

    public void setRecordPosition(String _value) throws Exception {
        m_RecordPosition = _value;
        int k, j;
        String str = "";
        if (m_RecordPosition.charAt(0) == '@')
        {
            // case #1
            m_RecordPosition = m_RecordPosition.Substring(1, (m_RecordPosition.Length)-(1));
            if ((j = m_RecordPosition.indexOf("{")) != -1)
            {
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                if ((k = SupportClass.getIndex(m_RecordPosition,"}",j + 1)) == -1)
                {
                    System.out.println("Wrong ofset position for " + _value);
                    return ;
                }
                 
                SupportClass.Tokenizer strToken = new SupportClass.Tokenizer(m_RecordPosition.Substring(j + 1, (k)-(j + 1)), ",");
                try
                {
                    m_SubIndex1 = Integer.valueOf((String)strToken.nextToken());
                    m_SubIndex2 = Integer.valueOf((String)strToken.nextToken());
                }
                catch (Exception e)
                {
                    m_SubIndex1 = m_SubIndex2 = -1;
                }

                m_RecordPosition = m_RecordPosition.Substring(0, (j)-(0));
            }
            else if ((j = m_RecordPosition.indexOf("[")) != -1)
            {
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                if ((k = SupportClass.getIndex(m_RecordPosition,"]",j + 1)) == -1)
                {
                    System.out.println("Wrong ofset position for " + _value);
                    return ;
                }
                 
                try
                {
                    m_Rank = int.Parse(m_RecordPosition.Substring(j + 1, (k)-(j + 1)));
                }
                catch (Exception e)
                {
                    m_Rank = 0;
                }

                m_RecordPosition = m_RecordPosition.Substring(0, (j)-(0));
            }
              
            if (MLSUtil.isNumber(m_RecordPosition))
            {
                try
                {
                    m_nRecordPosition = Integer.valueOf(m_RecordPosition) - 1;
                }
                catch (Exception e)
                {
                    m_nRecordPosition = -1;
                }
            
            }
             
        }
        else if (m_RecordPosition.charAt(0) == '\\')
        {
            // case 2 /Field1, Field2/
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            if ((j = SupportClass.getIndex(m_RecordPosition,"\\",1)) == -1)
            {
                System.out.println("Wrong ofset position for " + _value);
            }
             
            StringTokenizerEx strToken = new StringTokenizerEx(m_RecordPosition.Substring(1, (j)-(1)), ",");
            strToken.setMode(StringTokenizerEx.MODE_TAIL_NOT_INCLUDE);
            k = 0;
            m_ContFieldNames = new String[strToken.countTokens()];
            while (strToken.hasMoreElements())
            m_ContFieldNames[k++] = strToken.nextElement();
        }
          
    }

    public String getRecordPosition() throws Exception {
        return m_RecordPosition;
    }

    public int getRecordPositionInt() throws Exception {
        return m_nRecordPosition;
    }

    public int getSubIndex1() throws Exception {
        return m_SubIndex1;
    }

    public int getSubIndex2() throws Exception {
        return m_SubIndex2;
    }

    public int getRank() throws Exception {
        return m_Rank;
    }

    /**
    * @return  list of field's names
    */
    public String[] getContainedFields() throws Exception {
        return m_ContFieldNames;
    }

    public void setColumnCaption(String _value) throws Exception {
        super.setCaption(_value);
    }

    public String getColumnCaption() throws Exception {
        return super.getCaption();
    }

    public void setVisible(boolean _value) throws Exception {
        m_Visible = _value;
    }

    public boolean getVisible() throws Exception {
        return m_Visible;
    }

    public void setCmaField(boolean _value) throws Exception {
        m_CmaField = _value;
    }

    public boolean getCmaField() throws Exception {
        return m_CmaField;
    }

    public void setPropertyValue(String propName, String _value) throws Exception {
        try
        {
            //UPGRADE_TODO: The differences in the expected value  of parameters for method 'java.lang.Class.getField'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
            Field nameField = TypeSupport.GetField(this.getClass(),propName.toLowerCase(),BindingFlags.getInstance() | BindingFlags.getPublic() | BindingFlags.getStatic());
            Class typeField = nameField.getType();
            if (typeField == int.class)
            {
                new MLSUtil();
                if (MLSUtil.isNumber(_value) == true)
                    nameField.set(this, Integer.valueOf(_value));
                else
                    nameField.set(this, 0); 
            }
            else if (typeField == String.class)
                nameField.set(this, _value);
              
        }
        catch (Exception e)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            System.out.println(e.getClass().toString() + ": " + e.getMessage());
        }
    
    }

    public String getDateFormat() throws Exception {
        return dateformat;
    }

    public int getInpLength() throws Exception {
        return inplength;
    }

    public String getTakeAfter() throws Exception {
        return MLSUtil.toStr(takeafter).replace('~', ' ');
    }

    public int getNumReps() throws Exception {
        return numreps;
    }

    public String getCutBy() throws Exception {
        if (cutby.length() >= 1 && cutby.startsWith("`"))
            return "";
        else if (cutby.length() == 0)
            return "\r";
          
        return cutby;
    }

    //default cutby value
    public void setDelimiter(String _delimiter) throws Exception {
        delimiter = _delimiter.replace('~', ' ');
    }

    public String getDelimiter() throws Exception {
        if (delimiter.equals("\\"))
            return "\r";
         
        return delimiter;
    }

    public String getDisplayName() throws Exception {
        if (StringSupport.isNullOrEmpty(displayname))
        {
            return "";
        }
         
        return displayname;
    }

    public String getRetsLongName() throws Exception {
        if (StringSupport.isNullOrEmpty(retsLongName))
        {
            return "";
        }
         
        return retsLongName;
    }

    public String getDisplayRule() throws Exception {
        if (StringSupport.isNullOrEmpty(displayrule))
        {
            return "";
        }
         
        return displayrule;
    }

    public String getMultRecs() throws Exception {
        return multrecs;
    }

    public String getCombine() throws Exception {
        return combine;
    }

    public int getCharsTransf() throws Exception {
        return charstransf;
    }

    public String getPrefix() throws Exception {
        return prefix.replace('~', ' ');
    }

    public int getColAlign() throws Exception {
        if (tpocolalign.length() > 0)
        {
            if (tpocolalign.toUpperCase().equals(CMA_FLD_COLUMN_ALIGN_RIGHT.toUpperCase()) == true)
                return ALIGN_RIGHT;
            else if (tpocolalign.toUpperCase().equals(CMA_FLD_COLUMN_ALIGN_CENTER.toUpperCase()) == true)
                return ALIGN_CENTER;
            else
                return ALIGN_LEFT;  
        }
         
        int align = ALIGN_LEFT;
        switch(super.getFieldType())
        {
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY: 
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER: 
                align = ALIGN_RIGHT;
                break;
            case CMA_FLDTYPE_TEXT: 
            case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE: 
                break;
        
        }
        return align;
    }

    /*
    			if( colalign.equalsIgnoreCase( CMA_FLD_COLUMN_ALIGN_RIGHT ) == true)
    			return ALIGN_RIGHT;
    			else if( colalign.equalsIgnoreCase( CMA_FLD_COLUMN_ALIGN_CENTER ) == true)
    			return ALIGN_CENTER;
    			*/
    /**
    * Remove from string str characters which included in chatStr
    */
    private String charStrip(String str, String charStr) throws Exception {
        char[] szCharStr = MLSUtil.toStr(charStr).toCharArray();
        char[] szStr = str.toCharArray();
        if (szStr.length == 0 || szCharStr.length == 0)
            return str;
         
        StringBuilder sb = new StringBuilder();
        int j;
        boolean found = false;
        for (int i = 0;i < szStr.length;i++)
        {
            found = false;
            for (j = 0;j < szCharStr.length;j++)
                if (szStr[i] == szCharStr[j])
                {
                    found = true;
                    break;
                }
                 
            if (!found)
                sb.append(szStr[i]);
             
        }
        return sb.toString();
    }

    /**
    * @return  transformed string by following rules:
    * 
    *  @param type int
    * type = 0 - nothing to do,
    * type = 1 - set first letter of each word to uppercase,
    * type = 2 - set first letter of sentence to uppercase
    * type = 3 - set first letter after character '-'(minus) to uppercase
    */
    public static String charTransf(String str, int type, net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        char[] szStr = str.toCharArray();
        int i;
        boolean bFirstLetter = true;
        StringBuilder result = new StringBuilder();
        StringBuilder tempBuffer = new StringBuilder();
        switch(type)
        {
            case CHAR_TRANSF_NOTHING: 
                return str;
            case CHAR_TRANSF_FIRST_CHAR_OF_WORD: 
                for (i = 0;i < szStr.length;i++)
                {
                    if (MLSUtil.isEow(szStr[i]))
                    {
                        bFirstLetter = true;
                        result.append(Character.toLowerCase(szStr[i]));
                    }
                    else
                    {
                        if (bFirstLetter)
                        {
                            result.append(Character.toUpperCase(szStr[i]));
                            bFirstLetter = false;
                        }
                        else
                            result.append(Character.toLowerCase(szStr[i])); 
                    } 
                }
                //
                // TT#65322 - CharsTransf=1 incorrectly capitalizes first letter after an open bracket
                //
                if (result.toString().indexOf("(S)") != -1)
                {
                    String s = result.toString();
                    result = new StringBuilder(MLSUtil.replaceSubStr(s,"(S)","(s)"));
                }
                 
                break;
            case CHAR_TRANSF_FIRST_CHAR_OF_SENT: 
                if (!engine.getTpAppRequest().m_tcs.getIsDataAggListing())
                {
                    for (i = 0;i < szStr.length;i++)
                    {
                        if (MLSUtil.isEosn(szStr[i]))
                        {
                            result.append(Character.toLowerCase(szStr[i]));
                            bFirstLetter = true;
                        }
                        else
                        {
                            if (bFirstLetter)
                            {
                                result.append(Character.toUpperCase(szStr[i]));
                                if (MLSUtil.isEow(szStr[i]) == false)
                                    bFirstLetter = false;
                                 
                            }
                            else
                                result.append(Character.toLowerCase(szStr[i])); 
                        } 
                    }
                }
                else
                    return str; 
                break;
            case CHAR_TRANSF_FIRST_CHAR_OF_UNKNOWN: 
                if (str.indexOf("-") == -1)
                    return str;
                 
                bFirstLetter = true;
                for (i = 0;i < szStr.length;i++)
                {
                    if (bFirstLetter)
                    {
                        result.append(Character.toUpperCase(szStr[i]));
                        if (szStr[i] >= 32)
                            bFirstLetter = false;
                         
                    }
                    else
                    {
                        result.append(Character.toLowerCase(szStr[i]));
                        if (szStr[i] == '-')
                            bFirstLetter = true;
                         
                    } 
                }
                break;
        
        }
        return result.toString();
    }

    /**
    * @return  string without nonprintable characters
    */
    private String removeNonPrintable(String value_Renamed) throws Exception {
        if (value_Renamed.length() == 0)
            return value_Renamed;
         
        char[] szValue = value_Renamed.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0;i < szValue.length;i++)
        {
            if (szValue[i] >= ' ' || szValue[i] == 13 || szValue[i] == 10)
                result.append(szValue[i]);
             
        }
        return result.toString();
    }

    /**
    * @return  valid for iCMA feature string
    * sets result string as empty if value of feature include "N", "NO", "NOT", "NONE"
    */
    public static String getValidFeature(String value_Renamed) throws Exception {
        if (value_Renamed == null)
            return "";
         
        if (value_Renamed.length() == 0)
            return value_Renamed;
         
        String FEAT_DELIM = ":";
        int index;
        if ((index = value_Renamed.indexOf(FEAT_DELIM)) == -1)
            return value_Renamed;
         
        String[] N = new String[]{ "N", "NO", "NOT", "NONE" };
        char[] szValue = value_Renamed.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = index + 1;i < szValue.length;i++)
        {
            if (szValue[i] != ' ' && !Character.isLetter(szValue[i]))
                break;
             
            if (szValue[i] == ' ' && sb.length() != 0)
                break;
            else if (szValue[i] != ' ')
                sb.append(szValue[i]);
              
        }
        String str = sb.toString().toUpperCase();
        for (index = 0;index < N.length;index++)
            if (str.equals(N[index]))
                return "";
             
        return value_Renamed;
    }

    public String prepareValue2(String _value, DefParser _defParser, net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        int j;
        String tmpStr = "";
        try
        {
            _value = _value.replace('~', ' ');
            if (_value.length() > getInpLength())
            {
                if ((j = _value.indexOf(MLSUtil.toStr(getCutBy()).replace('~', ' '))) == -1 || getInpLength() < j)
                {
                    if (getMultRecs().toUpperCase().equals("Y".toUpperCase()) != true && getNumReps() <= 0 && m_RecordPosition.charAt(0) != '\\' && getFieldType() == CMA_FLDTYPE_TEXT)
                        //if(getInpLength() != 0 && _value.length() > getInpLength())
                        _value = _value.substring(0, (0) + ((getInpLength()) - (0)));
                     
                }
                else if (j != -1 && j < getInpLength() && getCutBy().length() != 0)
                    _value = MLSUtil.cutBy(_value,getCutBy());
                  
            }
            else
            {
                if (getCutBy().length() != 0 && !m_RecordPosition.startsWith("\\"))
                    _value = MLSUtil.cutBy(_value,getCutBy());
                 
            } 
            if (getTakeAfter().length() != 0 && (j = _value.indexOf(getTakeAfter())) != -1)
                _value = _value.Substring(j + 1, (_value.Length)-(j + 1));
             
            if (_value.length() > 0)
                _value = removeNonPrintable(_value);
             
            //
            if (m_SubstTableName != null && m_SubstTableName.length() != 0)
            {
                tmpStr = _defParser.getValue(m_SubstTableName,StringSupport.Trim(_value));
                if (tmpStr != null && tmpStr.length() != 0)
                    _value = tmpStr;
                 
            }
             
            // replace logic ...
            if (m_substWith.size() > 0)
            {
                _value = substituteString(_value,m_substStrings,m_substWith);
            }
             
            _value = MLSUtil.trimSpaces(_value,trimspaces);
            if (StringSupport.Trim(_value).length() != 0 && prefix.length() != 0)
            {
                _value = new StringBuilder(prefix.replace('~', ' ') + _value).toString();
            }
             
            if (StringSupport.Trim(_value).length() != 0 && postfix.length() != 0)
            {
                _value += postfix.replace('~', ' ');
            }
             
            if (_value.length() != 0 && charstrip.length() != 0)
                _value = charStrip(_value,charstrip);
             
            if (charstransf != CHAR_TRANSF_NOTHING)
                _value = charTransf(_value,charstransf,engine);
             
            if (super.getFieldType() == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY)
            {
                _value = charStrip(_value,"$,");
            }
             
            if (super.getFieldType() == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE)
            {
                if (dateformat.length() != 0)
                    _value = MLSUtil.formatDate(dateformat,MLSRecord.DATE_FORMAT,_value);
                else
                    _value = MLSUtil.formatDate("",MLSRecord.DATE_FORMAT,_value); 
            }
             
        }
        catch (Exception e)
        {
        }

        return _value;
    }

    public String prepareRawValueEx(String _value, DefParser defParser, net.toppro.components.mls.engine.MLSEngine getEngine) throws Exception {
        if (StringSupport.isNullOrEmpty(_value))
            return "";
         
        return _value;
    }

    //_value = _value.Replace('~', ' ');
    //System.String _cutBy = MLSUtil.toStr(getCutBy()).Replace('~', ' ');
    //System.String _takeAfter = getTakeAfter();
    //System.String tmpStr = "";
    //int index;
    //if (_cutBy.Length != 0)
    //    _value = MLSUtil.CutBy(_value, _cutBy);
    public String prepareValueEx(String _value, DefParser _defParser, net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        try
        {
            if (StringSupport.isNullOrEmpty(_value))
                return "";
             
            _value = _value.replace('~', ' ');
            String _cutBy = MLSUtil.toStr(getCutBy()).replace('~', ' ');
            String _takeAfter = getTakeAfter();
            String tmpStr = "";
            int index;
            if (_value.length() > inplength)
            {
                if ((index = _value.indexOf(_cutBy)) == -1 || inplength < index)
                {
                    if (multrecs.toUpperCase().equals("Y".toUpperCase()) != true && numreps <= 0 && m_RecordPosition.charAt(0) != '\\' && getFieldType() == CMA_FLDTYPE_TEXT)
                        _value = _value.Substring(0, (inplength)-(0));
                     
                }
                else if (index != -1 && index < inplength && _cutBy.length() != 0)
                    _value = MLSUtil.cutBy(_value,_cutBy);
                  
            }
            else if (_cutBy.length() != 0 && !m_RecordPosition.startsWith("\\"))
                _value = MLSUtil.cutBy(_value,_cutBy);
              
            if (_takeAfter.length() != 0 && (index = _value.indexOf(_takeAfter)) != -1)
                _value = _value.Substring(index + 1, (_value.Length)-(index + 1));
             
            if (_value.length() > 0)
                _value = removeNonPrintable(_value);
             
            _value = StringSupport.Trim(_value);
            if (!StringSupport.isNullOrEmpty(_value) && m_hashTableReplaceDelimVal.size() > 0)
            {
                //string delim = getDelimiter();
                //if (string.IsNullOrEmpty(delim))
                //    delim = ",";
                //string[] stringSeparators = new string[] { delim };
                String[] delimVal = StringSupport.Split(_value, ',');
                _value = "";
                for (int i = 0;i < delimVal.length;i++)
                {
                    delimVal[i] = StringSupport.Trim(delimVal[i]);
                    if (m_hashTableReplaceDelimVal.containsKey(delimVal[i].toUpperCase()))
                    {
                        delimVal[i] = (String)m_hashTableReplaceDelimVal.get(delimVal[i].toUpperCase());
                    }
                     
                    if (!StringSupport.isNullOrEmpty(delimVal[i]))
                        _value += delimVal[i] + ", ";
                     
                }
                if (!StringSupport.isNullOrEmpty(_value))
                {
                    _value = _value.substring(0, (0) + (_value.lastIndexOf(", ")));
                }
                 
            }
             
            if (m_SubstTableName != null && m_SubstTableName.length() != 0)
            {
                tmpStr = _defParser.getValue(m_SubstTableName,_value);
                if (tmpStr != null && tmpStr.length() != 0)
                    _value = MLSUtil.toStr(tmpStr.replace('~', ' '));
                 
            }
             
            // replace logic ...
            if (m_substWith.size() > 0)
            {
                _value = substituteString(_value,m_substStrings,m_substWith);
            }
             
            _value = MLSUtil.trimSpaces(_value,trimspaces);
            if (_value.length() != 0 && charstrip.length() != 0)
                _value = charStrip(_value,charstrip);
             
            //if( isStd ( TCSStandardResultFields.STDF_CMAIDENTIFIER  ) )
            //	System.out.println( "\nIdentifier\n" );
            if ((_value.length() != 0 || isStd(TCSStandardResultFields.STDF_CMAIDENTIFIER)) && prefix.length() != 0)
                _value = MLSUtil.toStr(prefix.replace('~', ' ')) + _value;
             
            if (_value.length() != 0 && postfix.length() != 0)
                _value += MLSUtil.toStr(postfix.replace('~', ' '));
             
            if (charstransf != CHAR_TRANSF_NOTHING)
                _value = charTransf(_value,charstransf,engine);
             
            if (super.getFieldType() == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY)
                _value = charStrip(_value,"$,");
             
            //if (base.getType() == CmaField.CMA_FLDTYPE_DATETIME)
            //{
            //if (dateformat.Length != 0)
            //{
            //    _value = MLSUtil.FormatDate(dateformat.Replace('~', ' '), MLSRecord.DATETIME_FORMAT, _value, m_engine.GetMLSResultsTimeZone(), m_engine.GetGMTTimeZone());
            //}
            //else
            //{
            //    _value = MLSUtil.FormatDate("", MLSRecord.DATETIME_FORMAT, _value.Trim().Replace("T", " "), m_engine.GetMLSResultsTimeZone(), m_engine.GetGMTTimeZone());
            //}
            //if (_value.IndexOf("00:00:00") > -1)
            //    _value = _value.Replace("00:00:00", "23:59:59");
            //}
            if (super.getFieldType() == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE)
            {
                if (dateformat.length() != 0)
                {
                    _value = MLSUtil.formatDate(dateformat.replace('~', ' '),MLSRecord.DATE_FORMAT,_value);
                }
                else
                    _value = MLSUtil.formatDate("",MLSRecord.DATE_FORMAT,_value); 
            }
             
            if (super.getFieldType() == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_TEXT || super.getFieldType() == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE)
            {
                _value = replaceEncodedXMLChar(_value);
            }
             
        }
        catch (Exception e)
        {
            _value = "";
        }

        return _value;
    }

    private String substituteString(String value_Renamed, ArrayList source, ArrayList target) throws Exception {
        if (value_Renamed.length() == 0)
            return "";
         
        if (source.size() == 0)
            return value_Renamed;
         
        if (target.size() == 0)
            return value_Renamed;
         
        String s = value_Renamed;
        for (int i = 0;i < source.size();i++)
        {
            s = s.replace((String)source.get(i), (String)target.get(i));
        }
        //System.String source = (System.String) m_source[i];
        //System.String target = (System.String) m_target[i];
        //if (target.Equals("`"))
        //    target = "";
        //int pos = 0;
        //while (pos != - 1)
        //{
        //    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //    pos = s.IndexOf(source, pos);
        //    if (pos != - 1)
        //    {
        //        s = s.Substring(0, (pos) - (0)) + target + s.Substring(pos + source.Length);
        //        pos = pos + target.Length;
        //    }
        //}
        if (s.startsWith(","))
            s = s.substring(1);
         
        if (s.endsWith(","))
            s = s.substring(0, (0) + ((s.length() - 1) - (0)));
         
        return s;
    }

    public String combineEx(String value_Renamed, String prev_value) throws Exception {
        value_Renamed = StringSupport.Trim(value_Renamed);
        //TmpStr2
        if (StringSupport.Trim(prev_value).length() == 0)
            prev_value = StringSupport.Trim(prev_value);
         
        long lTemp1 = 0;
        long lTemp2 = 0;
        long lMinVal = 32000;
        long lMaxVal = 0;
        if (combine.toUpperCase().equals(COMBINE_FNB.toUpperCase()) && value_Renamed.length() != 0)
            return prev_value + value_Renamed;
        else if (combine.toUpperCase().equals(COMBINE_ALL.toUpperCase()))
        {
            if (value_Renamed.length() != 0)
                return prev_value + value_Renamed;
             
        }
        else if (combine.toUpperCase().equals(COMBINE_SUM.toUpperCase()) == true)
        {
            if (value_Renamed.length() != 0)
            {
                try
                {
                    if (prev_value.length() > 0)
                    {
                        lTemp1 = Long.valueOf(prev_value);
                    }
                     
                }
                catch (Exception e)
                {
                    try
                    {
                        //UPGRADE_WARNING: Data types in Visual C# might be different.  Verify the accuracy of narrowing conversions. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1042'"
                        float f = float.Parse(prev_value);
                        lTemp1 = (long)f;
                    }
                    catch (Exception e1)
                    {
                        lTemp1 = 0;
                    }
                
                }

                try
                {
                    lTemp2 = Long.valueOf(value_Renamed);
                }
                catch (Exception e)
                {
                    try
                    {
                        //UPGRADE_WARNING: Data types in Visual C# might be different.  Verify the accuracy of narrowing conversions. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1042'"
                        float f = float.Parse(value_Renamed);
                        lTemp2 = (long)f;
                    }
                    catch (Exception e1)
                    {
                        lTemp2 = 0;
                    }
                
                }

                prev_value = Convert.ToString(lTemp1 + lTemp2);
                if (prev_value.toUpperCase().equals("0".toUpperCase()))
                    prev_value = "";
                 
            }
             
        }
        else if (combine.toUpperCase().equals(COMBINE_MIN.toUpperCase()) == true)
        {
            if (value_Renamed.length() != 0)
            {
                try
                {
                    lTemp2 = Long.valueOf(value_Renamed);
                }
                catch (Exception e)
                {
                    lTemp2 = 0;
                }

                if (lTemp2 < lMinVal)
                {
                    lMinVal = lTemp2;
                    prev_value = value_Renamed;
                }
                 
            }
             
        }
        else if (combine.toUpperCase().equals(COMBINE_MAX.toUpperCase()) == true)
        {
            if (value_Renamed.length() != 0)
            {
                try
                {
                    lTemp2 = Long.valueOf(value_Renamed);
                }
                catch (Exception e)
                {
                    lTemp2 = 0;
                }

                if (lTemp2 > lMaxVal)
                {
                    lMaxVal = lTemp2;
                    prev_value = value_Renamed;
                }
                 
            }
             
        }
             
        return prev_value;
    }

    public boolean isStd(int std_field) throws Exception {
        return m_stdId == std_field;
    }

    public int getStdId() throws Exception {
        return m_stdId;
    }

    public void setStdId(int id) throws Exception {
        m_stdId = id;
    }

    private String replaceEncodedXMLChar(String value_Renamed) throws Exception {
        String[] encodedXML = new String[]{ "amp", "lt", "gt", "quot", "apos" };
        int[] charEncode = new int[]{ 38, 60, 62, 34, 39 };
        StringBuilder result = new StringBuilder(value_Renamed.length());
        int i = 0, k = 0, phead = 0;
        while (i < value_Renamed.length())
        {
            if (value_Renamed.charAt(i) == '&')
            {
                if (phead < i)
                {
                    result.append(value_Renamed.Substring(phead, (i)-(phead)));
                    phead = i;
                }
                 
                for (k = 0;k < encodedXML.length;k++)
                {
                    if (i + 1 + encodedXML[k].length() < value_Renamed.length())
                        if (value_Renamed.charAt(i + 1 + encodedXML[k].length()) == ';' && encodedXML[k].toUpperCase().equals(value_Renamed.substring(i + 1, (i + 1) + ((i + 1 + encodedXML[k].length()) - (i + 1))).toUpperCase()))
                        {
                            char x = (char)charEncode[k];
                            result.append(x);
                            i = i + 1 + encodedXML[k].length();
                            phead = i + 1;
                            break;
                        }
                         
                     
                }
            }
             
            i++;
        }
        if (phead < i)
            result.append(value_Renamed.substring(phead, (phead) + ((i--) - (phead))));
         
        return result.toString();
    }

}


