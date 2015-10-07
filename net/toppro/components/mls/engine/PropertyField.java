//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Reflection.BindingFlags;
import CS2JNet.System.StringSupport;
import CS2JNet.System.TypeSupport;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.STRINGS;

public class PropertyField  extends Object 
{
    public PropertyField() throws Exception {
        initBlock();
    }

    private void initBlock() throws Exception {
        m_info = new FieldInfo();
    }

    //public const int CONTROL_TYPE_NUMBER = 0;
    public static final int CONTROL_TYPE_TEXTBOX = 1;
    public static final int CONTROL_TYPE_PICKLIST_TYPEABLE = 2;
    public static final int CONTROL_TYPE_PICKLIST = 3;
    public static final int CONTROL_TYPE_CHECKBOX = 4;
    public static final int CONTROL_TYPE_LABELPANEL = 5;
    public static final int CONTROL_TYPE_FILESELECT_PANEL = 6;
    public static final int CONTROL_TYPE_FOLDERSELECT_PANEL = 7;
    public static final int CONTROL_TYPE_PICTUREFILESELECT_PANEL = 8;
    public static final int CONTROL_TYPE_CALENDAR = 9;
    public static final int CONTROL_TYPE_NUMBER = 10;
    public static final String PROPERTY_CAPTION = "Caption";
    public static final String PROPERTY_HELPPANEL = "HelpPanel";
    public static final String PROPERTY_DELIMITER = "Delimiter";
    public static final String PROPERTY_LEFT = "Left";
    public static final String PROPERTY_MAXLENGTH = "MaxLength";
    public static final String PROPERTY_TOP = "Top";
    public static final String PROPERTY_CONTROLTYPE = "ControlType";
    public static final String PROPERTY_WIDTH = "Width";
    public static final String PROPERTY_REQUIRED = "Required";
    public static final String PROPERTY_FIELDTYPE = "FieldType";
    public static final String PROPERTY_REQUIREDFORMLSNUM = "ReqForMLSNum";
    public static final String PROPERTY_CHARSEXCLUDED = "CharsExcluded";
    public static final String PROPERTY_SDATEFORMAT = "SDateFormat";
    public static final String PROPERTY_FORMAT = "Format";
    //UPGRADE_NOTE: Final was removed from the declaration of 'PROPERTIES_NAMES '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] PROPERTIES_NAMES = new String[]{ PROPERTY_CAPTION, PROPERTY_HELPPANEL, PROPERTY_DELIMITER, PROPERTY_LEFT, PROPERTY_MAXLENGTH, PROPERTY_TOP, PROPERTY_CONTROLTYPE, PROPERTY_WIDTH, PROPERTY_REQUIRED, PROPERTY_FIELDTYPE, PROPERTY_REQUIREDFORMLSNUM, PROPERTY_CHARSEXCLUDED, PROPERTY_SDATEFORMAT, PROPERTY_FORMAT };
    //UPGRADE_NOTE: Final was removed from the declaration of 'MLS_NUMBER_NAMES'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] MLS_NUMBER_NAMES = new String[]{ "MLSNumbers", "MLS", "MLSNos", "MLSNo", "MLS_Number", "MLSNumber", "IndvMLS", "MLS_Numbers", "List_Number", "List_Numbers", "ClientID" };
    private static final int SPACES_IN_A_ROW = 3;
    private static final int MATCH_NONE = 0;
    private static final int MATCH_ALL = 1;
    private static final int MATCH_FIRST = 2;
    private static final int MATCH_SECOND = 3;
    private static final int MATCH_CONTAIN_STRING = 0;
    private static final int MATCH_EQUAL_STRING = 1;
    private static final int MATCH_NUMBER = 2;
    //********************************************************************
    // These fields are initialized using reflection interface           *
    // don't change their names                                          *
    // see function setPropertyValue                                     *
    //********************************************************************
    private static class FieldInfo  extends Object 
    {
        public String caption = "";
        public String helppanel = null;
        public String delimiter = "";
        public int left = 0;
        public int maxlength = 0;
        public int top = 0;
        public int controltype = 1;
        public int width = 0;
        public boolean required = false;
        public int fieldtype = 0;
        public boolean reqformlsnum = false;
        public String charsexcluded = "";
        public String sdateformat = "";
        public String format = "";
    }

    //********************************************************************
    private ArrayList m_initValues = null;
    private boolean m_iuInitValuesReplaceNeeded = false;
    // this variable just optimizes the replacement of ui init-values with the real values
    private ArrayList m_parsed_value = ArrayList.Synchronized(new ArrayList(10));
    private String m_value = "";
    private int m_layout = 0;
    private String m_prefix = "";
    private String m_name = "";
    /**
    * This flag works for searching mode: by MLS Number or Advanced
    */
    private boolean m_InUse = true;
    //UPGRADE_NOTE: The initialization of  'm_info' was moved to method 'InitBlock'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1005'"
    private FieldInfo m_info;
    public int getControlType() throws Exception {
        if (m_info.controltype == 0)
            return 1;
         
        return m_info.controltype;
    }

    public void setControlType(int type) throws Exception {
        m_info.controltype = type;
    }

    public String getDateFormat() throws Exception {
        return m_info.sdateformat;
    }

    public String getFormat() throws Exception {
        return m_info.format;
    }

    public boolean isRequired() throws Exception {
        return m_info.required;
    }

    public void setRequired(boolean b) throws Exception {
        m_info.required = b;
    }

    public void setInUse(boolean b) throws Exception {
        m_InUse = b;
    }

    public boolean isRequiredForMlsNum() throws Exception {
        return m_info.reqformlsnum;
    }

    public void setRequiredForMlsNum(boolean b) throws Exception {
        m_info.reqformlsnum = b;
    }

    public void setLayout(int _layout) throws Exception {
        m_layout = _layout;
    }

    public int getLayout() throws Exception {
        return m_layout;
    }

    public void setPrefix(String _prefix) throws Exception {
        m_prefix = _prefix;
    }

    public String getPrefix() throws Exception {
        return m_prefix;
    }

    public String getTip() throws Exception {
        return m_info.helppanel;
    }

    //		if ( m_info.helppanel == null )
    //			return
    public void setTip(String tip) throws Exception {
        m_info.helppanel = tip;
    }

    public String getName() throws Exception {
        return m_name;
    }

    public void setName(String name) throws Exception {
        m_name = name;
    }

    public void setPropertyValue(net.toppro.components.mls.engine.MLSEngine engine, String propName, String _value) throws Exception {
        try
        {
            //UPGRADE_TODO: The differences in the expected value  of parameters for method 'java.lang.Class.getField'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
            Field nameField = TypeSupport.GetField(m_info.getClass(),propName.toLowerCase(),BindingFlags.getInstance() | BindingFlags.getPublic() | BindingFlags.getStatic());
            Class typeField = nameField.getType();
            if (typeField == int.class)
            {
                if (MLSUtil.isNumber(_value))
                    nameField.set(m_info, Integer.valueOf(_value));
                else
                    nameField.set(m_info, 0); 
            }
            else if (typeField == boolean.class)
            {
                String[] boolValues = new String[]{ "y", "yes", "true" };
                boolean found = false;
                for (int i = 0;i < boolValues.length;i++)
                    if (_value.toUpperCase().equals(boolValues[i].toUpperCase()))
                    {
                        found = true;
                        break;
                    }
                     
                nameField.set(m_info, found);
            }
            else
                nameField.set(m_info, _value);  
        }
        catch (Exception e)
        {
            throw engine.createException(e,MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.PROPERTY_FIELD_ERR_UNABLE_TO_SET_VALUE),propName,_value));
        }
    
    }

    public int getLeft() throws Exception {
        return m_info.left;
    }

    public int getMaxLength() throws Exception {
        return m_info.maxlength;
    }

    public int getTop() throws Exception {
        return m_info.top;
    }

    private static boolean hasSpacesInARow(String str) throws Exception {
        int spaces_in_a_row = 0;
        for (int i = 0;i < str.length();i++)
        {
            switch(str.charAt(i))
            {
                case ' ': 
                case '\t': 
                    spaces_in_a_row++;
                    if (spaces_in_a_row >= SPACES_IN_A_ROW)
                        return true;
                     
                    break;
                default: 
                    spaces_in_a_row = 0;
                    break;
            
            }
        }
        return false;
    }

    //UPGRADE_NOTE: Field 'EnclosingInstance' was added to class 'InitValue' to access its enclosing instance. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1019'"
    private static class InitValue  extends Object 
    {
        private void initBlock(net.toppro.components.mls.engine.PropertyField enclosingInstance) throws Exception {
            this.enclosingInstance = enclosingInstance;
        }

        private net.toppro.components.mls.engine.PropertyField enclosingInstance;
        public net.toppro.components.mls.engine.PropertyField getEnclosing_Instance() throws Exception {
            return enclosingInstance;
        }

        private String m_value;
        private String m_ui_value;
        public String getValue() throws Exception {
            return m_value;
        }

        public String getUIValue() throws Exception {
            return m_ui_value;
        }

        public InitValue(net.toppro.components.mls.engine.PropertyField enclosingInstance, String value_Renamed) throws Exception {
            initBlock(enclosingInstance);
            if (net.toppro.components.mls.engine.PropertyField.hasSpacesInARow(value_Renamed))
            {
                String delim = getEnclosing_Instance().getDelimiter();
                int index = value_Renamed.indexOf(delim);
                if (index >= 0)
                {
                    m_ui_value = value_Renamed.Substring(0, (index)-(0)).Trim();
                    getEnclosing_Instance().m_iuInitValuesReplaceNeeded = true;
                    StringBuilder buf = new StringBuilder(m_ui_value);
                    int beg = index + delim.length();
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    index = SupportClass.getIndex(value_Renamed,delim,beg);
                    while (index >= 0)
                    {
                        buf.append(delim);
                        buf.append(value_Renamed.Substring(beg, (index)-(beg)).Trim());
                        beg = index + delim.length();
                        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                        index = SupportClass.getIndex(value_Renamed,delim,beg);
                    }
                    buf.append(delim);
                    buf.append(StringSupport.Trim(value_Renamed.substring(beg)));
                    value_Renamed = buf.toString();
                }
                else
                {
                    value_Renamed = StringSupport.Trim(value_Renamed);
                    m_ui_value = value_Renamed;
                } 
            }
            else
            {
                value_Renamed = StringSupport.Trim(value_Renamed);
                m_ui_value = value_Renamed;
            } 
            m_value = value_Renamed;
        }
    
    }

    public String getInitValue(int i) throws Exception {
        if (m_initValues != null && i >= 0 && i < m_initValues.size())
            return ((InitValue)m_initValues.get(i)).getValue();
         
        return "";
    }

    public String getUIInitValue(int i) throws Exception {
        if (m_initValues != null && i >= 0 && i < m_initValues.size())
            return ((InitValue)m_initValues.get(i)).getUIValue();
         
        return "";
    }

    public int getInitValueCount() throws Exception {
        if (m_initValues != null)
            return m_initValues.size();
         
        return 0;
    }

    public void addInitValue(String _value) throws Exception {
        if (_value != null && _value.length() > 0)
        {
            if (m_initValues == null)
                m_initValues = ArrayList.Synchronized(new ArrayList(10));
             
            m_initValues.add(new InitValue(this,_value));
        }
         
    }

    public String getDelimiter() throws Exception {
        //fix defect #46911, according to 6i code, defulat delimiter is "," Kevin Y
        if (m_info.delimiter.length() <= 0)
            return ",";
         
        return m_info.delimiter;
    }

    public String getInitDelimiter() throws Exception {
        return m_info.delimiter;
    }

    public void setDelimiter(String str) throws Exception {
        m_info.delimiter = str;
    }

    public boolean hasDelimiter() throws Exception {
        return m_info.delimiter.length() > 0;
    }

    public String getCaption() throws Exception {
        return m_info.caption;
    }

    public void setCaption(String str) throws Exception {
        m_info.caption = str;
    }

    public String getCharsExcluded() throws Exception {
        return m_info.charsexcluded;
    }

    public String getValue() throws Exception {
        if (!m_InUse)
            return "";
         
        return m_value;
    }

    public void setValue(String value_Renamed) throws Exception {
        // Checkbox returns false or true, though script expects the value to be empty on false (ifval)
        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Boolean.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
        if (m_info.controltype == CONTROL_TYPE_CHECKBOX && value_Renamed.toUpperCase().equals(String.valueOf(false).toUpperCase()))
        {
            value_Renamed = "";
        }
         
        String delim = getDelimiter();
        SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(value_Renamed,delim);
        StringBuilder buf = new StringBuilder();
        m_parsed_value.clear();
        if (tokenizer.hasMoreTokens())
        {
            String str = StringSupport.Trim(tokenizer.nextToken());
            m_parsed_value.add(str);
            buf.append(str);
            while (tokenizer.hasMoreTokens())
            {
                str = StringSupport.Trim(tokenizer.nextToken());
                m_parsed_value.add(str);
                buf.append(delim);
                buf.append(str);
            }
            if (value_Renamed.endsWith(delim))
            {
                //
                // Issue of TT#50494, when value ends with delimiter. It means that we have
                // next token, but this token is empty. Example: 150000-
                //
                str = "";
                m_parsed_value.add(str);
                buf.append(delim);
                buf.append(str);
            }
             
        }
         
        m_value = buf.toString();
    }

    public String getValue(int index) throws Exception {
        if (!m_InUse)
            return "";
         
        if (m_parsed_value != null && index >= 0 && index < m_parsed_value.size())
            return (String)m_parsed_value.get(index);
         
        return "";
    }

    public String getUIValue() throws Exception {
        String value_Renamed = m_value;
        if (m_iuInitValuesReplaceNeeded)
        {
            for (int i = 0;i < m_initValues.size();i++)
            {
                InitValue iv = (InitValue)m_initValues.get(i);
                String u = iv.getUIValue();
                String v = iv.getValue();
                int index = value_Renamed.indexOf(v);
                while (index >= 0)
                {
                    value_Renamed = value_Renamed.Substring(0, (index)-(0)) + u + value_Renamed.substring(index + v.length());
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    index = SupportClass.getIndex(value_Renamed,v,index + u.length());
                }
            }
        }
         
        return value_Renamed;
    }

    public void setUIValue(String value_Renamed) throws Exception {
        if (m_iuInitValuesReplaceNeeded)
        {
            String delimiter = getDelimiter();
            SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(value_Renamed,delimiter);
            if (tokenizer.hasMoreTokens())
            {
                StringBuilder buffer = new StringBuilder(value_Renamed.length());
                while (true)
                {
                    int i;
                    String str = tokenizer.nextToken();
                    for (i = m_initValues.size() - 1;i >= 0;i--)
                    {
                        InitValue iv = (InitValue)m_initValues.get(i);
                        if (str.equals(iv.getUIValue()))
                        {
                            buffer.append(iv.getValue());
                            break;
                        }
                         
                    }
                    if (i < 0)
                        buffer.append(str);
                     
                    if (tokenizer.hasMoreTokens())
                        buffer.append(delimiter);
                    else
                        break; 
                }
                value_Renamed = buffer.toString();
            }
             
        }
         
        setValue(value_Renamed);
    }

    public boolean isMLSNumField() throws Exception {
        for (int i = 0;i < MLS_NUMBER_NAMES.length;i++)
            if (getName().toUpperCase().equals(MLS_NUMBER_NAMES[i].toUpperCase()))
                return true;
             
        return false;
    }

    public String getLookupCode(String value_Renamed) throws Exception {
        if (getFormat().length() == 0 || (getControlType() != 2 && getControlType() != 3) || StringSupport.isNullOrEmpty(value_Renamed))
            return value_Renamed;
         
        int pos = 0;
        String format = StringSupport.Trim(getFormat().toUpperCase());
        int match_type = MATCH_EQUAL_STRING;
        pos = format.indexOf(":");
        if (pos > 0)
        {
            String s = format.substring(pos + 1);
            switch(s.charAt(0))
            {
                case 'C': 
                    match_type = MATCH_CONTAIN_STRING;
                    break;
                case 'E': 
                    match_type = MATCH_EQUAL_STRING;
                    break;
                case 'N': 
                    match_type = MATCH_NUMBER;
                    break;
                default: 
                    match_type = MATCH_EQUAL_STRING;
                    break;
            
            }
            format = format.Substring(0, (pos)-(0)).Trim();
        }
         
        String formatDelimiter = "";
        if (format.length() > 2)
            formatDelimiter = format.substring(1, (1) + ((format.length() - 1) - (1)));
         
        pos = format.toUpperCase().indexOf("L");
        int match_position = MATCH_SECOND;
        if (format.length() == 0 || pos == -1)
            match_position = MATCH_NONE;
         
        if (format.toUpperCase().equals("L".toUpperCase()) || format.toUpperCase().equals("S".toUpperCase()))
            match_position = MATCH_ALL;
        else if (pos == 0)
            match_position = MATCH_FIRST;
        else
            match_position = MATCH_SECOND;  
        String result = "";
        String initValue = "";
        String[] values = StringSupport.Split(value_Renamed, ',');
        for (int n = 0;n < values.length;n++)
        {
            for (int i = 0;i < getInitValueCount();i++)
            {
                initValue = getInitValue(i);
                if (compareString(initValue,values[n],match_type,match_position,formatDelimiter))
                {
                    if (result.indexOf(initValue + getDelimiter()) < 0)
                        result = result + initValue + getDelimiter();
                     
                    if (getControlType() == 3)
                        break;
                     
                }
                 
            }
            if (getControlType() == 3)
                break;
             
        }
        if (result.endsWith(getDelimiter()))
            result = result.substring(0, (0) + ((result.length() - 1) - (0)));
         
        return result;
    }

    private boolean compareString(String initValue, String value_Renamed, int match_type, int match_position, String formatDelimiter) throws Exception {
        try
        {
            initValue = StringSupport.Trim(initValue);
            //removeSpace( s1 );
            value_Renamed = StringSupport.Trim(value_Renamed);
            //removeSpace( s2 );
            boolean result = false;
            int pos = initValue.indexOf(formatDelimiter);
            switch(match_position)
            {
                case MATCH_ALL: 
                    break;
                case MATCH_FIRST: 
                    initValue = initValue.Substring(0, (pos)-(0));
                    break;
                case MATCH_SECOND: 
                    initValue = initValue.substring(pos + formatDelimiter.length());
                    break;
                case MATCH_NONE: 
                    break;
            
            }
            if (match_type == MATCH_NUMBER)
            {
                double startValue = 0;
                double endValue = 0;
                double initStartValue = 0;
                double initEndValue = 0;
                double initUpperValue = 0;
                double initLowerValue = 0;
                try
                {
                    initValue = initValue.replace('+', '>');
                    if (initValue.indexOf("<") > -1 || initValue.indexOf(">") > -1)
                    {
                        pos = initValue.indexOf("<");
                        if (initValue.indexOf("<") > -1)
                            initLowerValue = Double.valueOf(StringSupport.Trim(initValue.replace('<', ' ')));
                        else
                            initUpperValue = Double.valueOf(StringSupport.Trim(initValue.replace('>', ' '))); 
                    }
                    else
                    {
                        pos = initValue.indexOf("-");
                        if (pos > -1)
                        {
                            initStartValue = double.Parse(initValue.Substring(0, (pos)-(0)).Trim());
                            initEndValue = Double.valueOf(StringSupport.Trim(initValue.substring(pos + 1)));
                        }
                        else
                            initStartValue = Double.valueOf(initValue); 
                    } 
                    pos = value_Renamed.indexOf("-");
                    if (pos > -1)
                    {
                        startValue = double.Parse(value_Renamed.Substring(0, (pos)-(0)).Trim());
                        endValue = Double.valueOf(StringSupport.Trim(value_Renamed.substring(pos + 1)));
                    }
                    else
                        startValue = Double.valueOf(value_Renamed); 
                    if (initUpperValue > 0 || initLowerValue > 0)
                    {
                        if (initUpperValue > 0)
                            if (endValue == 0)
                                result = startValue > initUpperValue;
                            else
                                result = endValue > initUpperValue; 
                        else
                            result = startValue < initLowerValue; 
                    }
                    else if (endValue == 0 && initEndValue == 0)
                        result = startValue == initStartValue;
                    else if (endValue == 0 && initEndValue > 0)
                        result = !(startValue < initStartValue || startValue > initEndValue);
                    else if (endValue > 0 && initEndValue == 0)
                        result = !(initStartValue < startValue || initStartValue > endValue);
                    else if (endValue > 0 && initEndValue > 0)
                        result = !((startValue < initStartValue && endValue < initStartValue) || (startValue > initEndValue && endValue > initEndValue));
                    else
                        result = true;     
                }
                catch (Exception e)
                {
                }

                return result;
            }
            else
            {
                if (match_type == MATCH_EQUAL_STRING)
                    result = initValue.toUpperCase().equals(value_Renamed.toUpperCase());
                else
                    result = initValue.IndexOf(value_Renamed, StringComparison.CurrentCultureIgnoreCase) > -1; 
            } 
            return result;
        }
        catch (Exception e)
        {
            return false;
        }
    
    }

}


