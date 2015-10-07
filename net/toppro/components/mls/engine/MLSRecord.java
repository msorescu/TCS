//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:44 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DoubleSupport;
import CS2JNet.System.ObjectSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.TimeSpan;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.IPropertyFieldValidator;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.STRINGS;
import Tcs.Mls.TCSStandardResultFields;

public class MLSRecord   
{
    public static final String DATE_FORMAT = "MM/DD/YYYY";
    public static final String DATETIME_FORMAT = "MM/dd/yyyyTHH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ACTIVE = 1;
    public static final int TYPE_SOLD = 2;
    public static final int TYPE_PENDING = 3;
    public static final int TYPE_EXPIRED = 4;
    public static final int PICTURE_NOT_DOWNLOADED = 0;
    public static final int PICTURE_DOWNLOADED = 1;
    public static final int PICTURE_DOWNLOAD_FAILED = 2;
    public static final int PICTURE_DOWNLOADED_FINALY = 3;
    public static final int PICTURE_DOWNLOAD_FAILED_FINALY = 4;
    private static final String VALIDATION_FAILED = "VALIDATION_FAILED";
    public static String[] STANDARD_UNITS_FIELDS = new String[]{ "ActualRent", "FurnishedYN", "NumberBaths", "NumberBeds", "NumberUnits", "TotalRent" };
    private String[] m_data;
    private String[] m_validatedData;
    private String[] m_rawData;
    private MLSCmaFields m_fields;
    private int m_type;
    private int m_publicType;
    private boolean m_checked;
    private int m_picture_download_state;
    private String m_picture_filename;
    private String[] m_multi_picture_filename;
    private IPropertyFieldValidator m_validator;
    private String m_featureNotesPositions = null;
    private net.toppro.components.mls.engine.MLSEngine m_engine;
    private Pattern m_regexSplit = Pattern.compile(" +");
    private Pattern m_regexIsNumber = Pattern.compile("^\\d*,?\\d*.?\\d*$");
    private HashMap<String,String> m_statesProvinces = null;
    /**
    * @param fields fields description from the MLSEngine
    * 
    *  @param data an array containing field values.
    */
    public MLSRecord(MLSCmaFields fields, String[] data) throws Exception {
        m_fields = fields;
        m_data = data;
        m_validatedData = new String[TCSStandardResultFields.STANDARD_FIELDS_COUNT];
        m_engine = fields.getEngine();
        DefParser parser = m_engine.getDefParser();
        // Init type
        m_type = initType(m_engine);
        m_publicType = initPublicType(m_engine);
        // Init checked flag
        m_checked = false;
        // Init picture filename
        if (parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICDWNFLAG).equals("3"))
            m_picture_filename = m_engine.getResultsFolder() + getRecordID() + ".1.jpg";
        else
            m_picture_filename = m_engine.getResultsFolder() + getRecordID() + ".jpg"; 
        // Init 'picture downloaded' flag
        setPictureDownloadState(PICTURE_NOT_DOWNLOADED);
    }

    public void setValidator(IPropertyFieldValidator validator) throws Exception {
        if (validator != m_validator)
        {
            m_validator = validator;
            for (int i = 0;i < TCSStandardResultFields.STANDARD_FIELDS_COUNT;i++)
                m_validatedData[i] = null;
        }
         
    }

    /**
    * It is used in MLSRecords for serialization.
    *  @return  string array containing all field values.
    */
    public String[] getData() throws Exception {
        return m_data;
    }

    /**
    * @param index index of the field in the fields array.
    * 
    *  @return  field name
    */
    public String getFieldName(int index) throws Exception {
        net.toppro.components.mls.engine.CmaField field = m_fields.getField(index);
        if (field != null)
            return field.getName();
         
        return null;
    }

    /**
    * @param fieldName field name.
    * 
    *  @return  field index in the fields array.
    */
    public int getFieldIndex(String fieldName) throws Exception {
        return m_fields.getFieldIndex(fieldName);
    }

    public net.toppro.components.mls.engine.CmaField getStdField(int stdField) throws Exception {
        return m_fields.getStdField(stdField);
    }

    public int getStdFieldIndex(int stdField) throws Exception {
        return m_fields.getStdFieldIndex(stdField);
    }

    /**
    * Sets field value.
    *  @param index field index in fields array
    * 
    *  @param value field value.
    * 
    *  @return  true is field index is correct and value was successfuly set.
    */
    public boolean setFieldValue(int index, String value_Renamed) throws Exception {
        net.toppro.components.mls.engine.CmaField field = m_fields.getField(index);
        if (field != null)
        {
            int id = field.getStdId();
            if (id != TCSStandardResultFields.CUSTOM_FIELD)
                m_validatedData[id] = null;
             
            m_data[index] = value_Renamed;
            return true;
        }
         
        return false;
    }

    /**
    * Sets field value.
    *  @param fieldName field name.
    * 
    *  @param value field value.
    * 
    *  @return  true is field with this name exists and value was successfuly set.
    */
    public boolean setFieldValue(String fieldName, String value_Renamed) throws Exception {
        return setFieldValue(getFieldIndex(fieldName),value_Renamed);
    }

    public void addRawData(String[] data) throws Exception {
        m_rawData = data;
    }

    public String getFieldRawValue(int index) throws Exception {
        CmaField field = m_fields.getField(index);
        if (field != null)
        {
            if (index >= m_rawData.length)
                return null;
             
            return m_rawData[index];
        }
         
        return null;
    }

    /**
    * field index in the fields array. 
    *  @return  field value, or null if index is out of range.
    */
    public String getFieldValue(int index) throws Exception {
        String value_Renamed;
        net.toppro.components.mls.engine.CmaField field = m_fields.getField(index);
        if (field != null)
        {
            if (index >= m_data.length)
                return null;
             
            value_Renamed = m_data[index];
            switch(field.getFieldType())
            {
                case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_BOOLEAN: 
                    //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Boolean.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                    String prefix = field.getPrefix();
                    if (!StringSupport.isNullOrEmpty(prefix) && value_Renamed.startsWith(prefix))
                    {
                        String boolValue = value_Renamed.replace(prefix, "");
                        if (boolValue.equals("1") || boolValue.toUpperCase().equals(String.valueOf(true).toUpperCase()) || boolValue.toLowerCase().equals("y") || boolValue.toLowerCase().equals("yes"))
                        {
                            value_Renamed = prefix + "Y";
                        }
                        else
                        {
                            value_Renamed = "";
                        } 
                    }
                    else
                    {
                        if (value_Renamed.equals("1") || value_Renamed.toUpperCase().equals(String.valueOf(true).toUpperCase()) || value_Renamed.toLowerCase().equals("y") || value_Renamed.toLowerCase().equals("yes"))
                        {
                            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Boolean.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                            value_Renamed = "Y";
                        }
                        else
                        {
                            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Boolean.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                            value_Renamed = "";
                        } 
                    } 
                    break;
                case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME: 
                    if (!String.IsNullOrWhiteSpace(value_Renamed))
                    {
                        String dateformat = field.getDateFormat();
                        if (dateformat.length() != 0)
                        {
                            value_Renamed = MLSUtil.FormatDate(dateformat.replace('~', ' '), MLSRecord.DATETIME_FORMAT, value_Renamed, m_engine.getMLSResultsTimeZone(), m_engine.getGMTTimeZone());
                        }
                        else
                        {
                            value_Renamed = MLSUtil.FormatDate("", MLSRecord.DATETIME_FORMAT, StringSupport.Trim(value_Renamed).replace("T", " "), m_engine.getMLSResultsTimeZone(), m_engine.getGMTTimeZone());
                        } 
                        if (value_Renamed.indexOf("00:00:00") > -1)
                            value_Renamed = value_Renamed.replace("00:00:00", "23:59:59");
                         
                    }
                     
                    break;
            
            }
            if (field.hasScripts() && value_Renamed != null)
            {
                int currentYear = Calendar.getInstance().getTime().Year;
                try
                {
                    value_Renamed = processScripts(value_Renamed,field);
                    if (field.isStd(TCSStandardResultFields.STDF_CMAAGE))
                    {
                        if ((field.caption.toLowerCase().indexOf("yr") != -1 || field.caption.toLowerCase().indexOf("year") != -1) && field.caption.toLowerCase().indexOf("age") == -1 && value_Renamed.length() == 2)
                        {
                            try
                            {
                                if (currentYear - 1999 >= ((int)((Integer.valueOf(value_Renamed.toString())))))
                                    value_Renamed = "20" + value_Renamed;
                                else
                                    value_Renamed = "19" + value_Renamed; 
                            }
                            catch (Exception e)
                            {
                            }
                        
                        }
                         
                    }
                     
                }
                catch (Exception exc)
                {
                }
            
            }
             
            //m_engine.WriteLine("Exception on running result field scripts.");
            if (field.methodParams != null)
            {
                String[] paramsValues = new String[field.methodParams.length];
                for (int i = 0;i < paramsValues.length;i++)
                {
                    if (i == 0)
                        paramsValues[i] = value_Renamed;
                    else
                        paramsValues[i] = getResultFieldParam("\\" + field.methodParams[i] + "\\",field); 
                }
                value_Renamed = m_engine.callScriptMethod(field.name,paramsValues);
            }
             
            return value_Renamed;
        }
         
        return null;
    }

    private String processScripts(String value, net.toppro.components.mls.engine.CmaField field) throws Exception {
        ArrayList vScripts = field.getScripts();
        boolean bGoNext = false;
        for (int i = 0;i < vScripts.size();i++)
        {
            String[] aParams = (String[])vScripts.get(i);
            if (ObjectSupport.Equals("if", StringComparison.CurrentCultureIgnoreCase))
            {
                if (ObjectSupport.Equals("Equals", StringComparison.CurrentCultureIgnoreCase))
                    bGoNext = ObjectSupport.Equals(getResultFieldParam(aParams[3],field), StringComparison.CurrentCultureIgnoreCase);
                else if (ObjectSupport.Equals("StartWith", StringComparison.CurrentCultureIgnoreCase))
                    bGoNext = getResultFieldParam(aParams[1],field).toLowerCase().startsWith(getResultFieldParam(aParams[3],field).toLowerCase());
                else if (ObjectSupport.Equals("EndWith", StringComparison.CurrentCultureIgnoreCase))
                    bGoNext = getResultFieldParam(aParams[1],field).toLowerCase().endsWith(getResultFieldParam(aParams[3],field).toLowerCase());
                else if (ObjectSupport.Equals("Contains", StringComparison.CurrentCultureIgnoreCase))
                    bGoNext = getResultFieldParam(aParams[1],field).toLowerCase().contains(getResultFieldParam(aParams[3],field).toLowerCase());
                    
            }
            else //else if (aParams[2].Equals(">", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult = 0;
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[1], field), out outResult);
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = (int)outResult > (int)outResult1;
            //}
            //else if (aParams[2].Equals("<", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult = 0;
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[1], field), out outResult);
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = (int)outResult < (int)outResult1;
            //}
            //else if (aParams[2].Equals(">=", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult = 0;
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[1], field), out outResult);
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = (int)outResult >= (int)outResult1;
            //}
            //else if (aParams[2].Equals("<=", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult = 0;
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[1], field), out outResult);
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = (int)outResult <= (int)outResult1;
            //}
            //else if (aParams[2].Equals("Len=", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = getResultFieldParam(aParams[1], field).Length == (int)outResult1;
            //}
            //else if (aParams[2].Equals("Len>", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = getResultFieldParam(aParams[1], field).Length > (int)outResult1;
            //}
            //else if (aParams[2].Equals("Len<", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = getResultFieldParam(aParams[1], field).Length < (int)outResult1;
            //}
            //else if (aParams[2].Equals("Len>=", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = getResultFieldParam(aParams[1], field).Length >= (int)outResult1;
            //}
            //else if (aParams[2].Equals("Len<=", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    Double outResult1 = 0;
            //    Double.TryParse(getResultFieldParam(aParams[3], field), out outResult1);
            //    bGoNext = getResultFieldParam(aParams[1], field).Length <= (int)outResult1;
            //}
            //else if (aParams[2].Equals("IsEmpty", StringComparison.CurrentCultureIgnoreCase))
            //{
            //    bGoNext = string.IsNullOrEmpty(getResultFieldParam(aParams[1], field));
            //}
            if (ObjectSupport.Equals("value", StringComparison.CurrentCultureIgnoreCase) && bGoNext)
            {
                value = "";
                for (int j = 1;j < aParams.length;j++)
                {
                    value = value + getResultFieldParam(aParams[j],field);
                }
            }
            else if (ObjectSupport.Equals("else", StringComparison.CurrentCultureIgnoreCase))
            {
                bGoNext = !bGoNext;
            }
               
        }
        return value;
    }

    private String getResultFieldParam(String param, net.toppro.components.mls.engine.CmaField sourcefield) throws Exception {
        String value = "";
        if (param.startsWith("\\"))
        {
            // For the case of "/status(1, 3)/"
            param = StringSupport.Trim(param.replace('\\', ' '));
            if (param.startsWith("Engine_") || param.startsWith("Param_"))
            {
                value = m_engine.getEngineParameter(param);
                return value;
            }
             
            int pos1 = param.indexOf("(");
            int pos2 = 0;
            int pos3 = 0;
            int startIndex = 0;
            int length = 0;
            if (pos1 > -1)
            {
                pos2 = param.indexOf(")");
                pos3 = param.indexOf(",");
                startIndex = Integer.valueOf(StringSupport.Trim(param.substring(pos1 + 1, (pos1 + 1) + (pos3 - pos1 - 1))));
                length = Integer.valueOf(StringSupport.Trim(param.substring(pos3 + 1, (pos3 + 1) + (pos2 - pos3 - 1))));
                param = param.substring(0, (0) + (pos1));
            }
             
            int index = getFieldIndex(param);
            net.toppro.components.mls.engine.CmaField field = m_fields.getField(index);
            if (field != null)
            {
                if (index >= m_data.length)
                    return "";
                 
                value = m_data[index];
            }
             
            if (value != null && value.length() > 0 && pos1 > -1)
            {
                if (value.length() > startIndex)
                {
                    value = value.substring(startIndex - 1, (startIndex - 1) + (length));
                }
                 
            }
             
        }
        else
            value = param; 
        return value;
    }

    /**
    * @param fieldName field name.
    * 
    *  @return  field value, or null if fieldName doesn't exist.
    */
    public String getFieldValue(String fieldName) throws Exception {
        return getFieldValue(getFieldIndex(fieldName));
    }

    public String getTCSStdFieldValue(int stdField, String value_Renamed) throws Exception {
        String result = "";
        switch(stdField)
        {
            case TCSStandardResultFields.STDF_CMABATHROOMSNORM: 
                result = validateSTDFTBath(value_Renamed);
                break;
            case TCSStandardResultFields.STDF_CMAAGENORM: 
                result = validateSTDFYearBuilt(value_Renamed);
                break;
            case TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                result = validateSTDFSQFT(value_Renamed);
                break;
            case TCSStandardResultFields.STDF_CMALOTSIZENORM: 
                net.toppro.components.mls.engine.CmaField field = getStdField(TCSStandardResultFields.STDF_CMALOTSIZE);
                if (field == null)
                    return "";
                 
                result = validateSTDFLotSize(value_Renamed,field.unitofarea);
                break;
        
        }
        return result;
    }

    public String getTCSStdFieldValue(int stdField, boolean validate) throws Exception {
        String result = getStdFieldValue(stdField,validate);
        net.toppro.components.mls.engine.CmaField field = m_fields.getStdField(stdField);
        if (result.length() == 0 || field == null)
            return result;
         
        String fieldName = field.getName().toUpperCase();
        if (fieldName.startsWith("STDF"))
        {
            if (m_featureNotesPositions == null)
            {
                String cont_fields = "";
                net.toppro.components.mls.engine.CmaField fnField = m_fields.getStdField(TCSStandardResultFields.STDF_CMAFEATURE);
                if (fnField != null)
                    cont_fields = fnField.getRecordPosition().toUpperCase();
                 
                fnField = m_fields.getStdField(TCSStandardResultFields.STDF_NOTES);
                if (fnField != null)
                    cont_fields += fnField.getRecordPosition().toUpperCase();
                 
                m_featureNotesPositions = cont_fields;
            }
             
            int index = m_featureNotesPositions.indexOf(fieldName);
            if (index > -1)
            {
                //Remove prefix
                String prefix = StringSupport.Trim(field.getPrefix());
                if (prefix.length() > 0)
                {
                    index = result.toLowerCase().indexOf(prefix.toLowerCase());
                    if (index > -1)
                        result = StringSupport.Trim(result.substring(index + prefix.length()));
                     
                }
                 
            }
             
        }
         
        switch(stdField)
        {
            case TCSStandardResultFields.STDF_STDFWATERFRONTYN: 
                result = validateWaterFrountYN(result);
                break;
            case TCSStandardResultFields.STDF_CMABEDROOMS: 
                try
                {
                    //Yes, we are using the Bathroom normalization rules for Bedrooms
                    result = validateSTDFTBath(result);
                }
                catch (Exception __dummyCatchVar0)
                {
                }

                break;
        
        }
        return result;
    }

    private String validateWaterFrountYN(String value_Renamed) throws Exception {
        if (value_Renamed != null)
        {
            value_Renamed = StringSupport.Trim(value_Renamed.toUpperCase());
            if (value_Renamed.equals("Y") || value_Renamed.equals("N"))
                return value_Renamed;
             
        }
         
        return "";
    }

    /**
    * @param stdField standard field id. One of the MLSCmaField.STDF_xxx constants.
    * 
    *  @return  field value. The return value can not be null. If the value is missing
    * - empty string is returned.
    */
    public String getStdFieldValue(int stdField, boolean bValidate) throws Exception {
        String str;
        if (bValidate)
        {
            str = m_validatedData[stdField];
            if ((Object)str == (Object)VALIDATION_FAILED)
            {
                str = "";
            }
            else if (str == null)
            {
                str = getFieldValue(m_fields.getStdFieldIndex(stdField));
                if (str == null)
                {
                    str = "";
                    if (stdField == TCSStandardResultFields.STDF_TPOSTATE)
                        str = getState();
                     
                    m_validatedData[stdField] = str;
                }
                else if (str.length() > 0)
                {
                    str = validate(str,m_fields.getStdField(stdField));
                    if (str == null)
                    {
                        str = "";
                        m_validatedData[stdField] = VALIDATION_FAILED;
                    }
                    else
                        m_validatedData[stdField] = str; 
                }
                else
                    m_validatedData[stdField] = str;  
                if (stdField == TCSStandardResultFields.STDF_CMALISTINGDATE && str.length() == 0)
                {
                    str = validate(str,m_fields.getStdField(stdField));
                    if (str == null)
                    {
                        str = "";
                        m_validatedData[stdField] = VALIDATION_FAILED;
                    }
                    else
                        m_validatedData[stdField] = str; 
                }
                 
            }
              
        }
        else
        {
            str = getFieldValue(m_fields.getStdFieldIndex(stdField));
            if (str == null)
                str = "";
             
        } 
        return str;
    }

    public String getStdFieldValue(int stdField) throws Exception {
        return getStdFieldValue(stdField,true);
    }

    public int getStdFieldIntValue(int stdField, boolean validate) throws Exception {
        int result = 0;
        String value_Renamed = getStdFieldValue(stdField,validate);
        if (value_Renamed.length() > 0)
        {
            try
            {
                result = Integer.valueOf(value_Renamed);
            }
            catch (Exception exc)
            {
            }
        
        }
         
        return result;
    }

    public int getStdFieldIntValue(int stdField) throws Exception {
        return getStdFieldIntValue(stdField,true);
    }

    public double getStdFieldDoubleValue(int stdField, boolean validate) throws Exception {
        double result = 0;
        String value_Renamed = getStdFieldValue(stdField,validate);
        if (value_Renamed.length() > 0)
        {
            try
            {
                result = Double.valueOf(value_Renamed);
            }
            catch (Exception exc)
            {
            }
        
        }
         
        return result;
    }

    public double getStdFieldDoubleValue(int stdField) throws Exception {
        return getStdFieldDoubleValue(stdField,true);
    }

    public Date getStdFieldDateValue(int stdField, boolean validate) throws Exception {
        return MLSUtil.getDate(getStdFieldValue(stdField,validate));
    }

    public Date getStdFieldDateValue(int stdField) throws Exception {
        return getStdFieldDateValue(stdField,true);
    }

    /**
    * @param stdField one of the rooms standard fields: STDF_LIVING_ROOM, STDF_DINING_ROOM, STDF_KITCHEN,
    * STDF_FAM_ROOM, STDF_MASTERBED, STDF_2ND_BED, STDF_3RD_BED, STDF_4TH_BED, STDF_5TH_BED, STDF_OFFICE,
    * STDF_DEN, STDF_GREAT_ROOM, STDF_LIBRARY, STDF_LAUNDRY, STDF_WORKSHOP, STDF_ROOM1, STDF_ROOM2, STDF_ROOM3,
    * STDF_ROOM4, STDF_ROOM5, STDF_ROOM6, STDF_ROOM7, STDF_ROOM8, STDF_ROOM9, and STDF_ROOM10.
    * 
    *  @param dimension. Which dimension is required: 0 - x; 1 - y. 3,4 - some boards provide 4 dimensions.
    * 
    *  @return  room dimension. It is guaranteed that this value can be converted to float/double.
    */
    public String getRoomDimension(int stdField, int dimension) throws Exception {
        String value_Renamed = getStdFieldValue(stdField);
        value_Renamed = value_Renamed.substring(value_Renamed.indexOf(',') + 1);
        int beg = 0;
        int end;
        for (int i = 0;i < dimension;i++)
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            end = SupportClass.getIndex(value_Renamed,'x',beg);
            if (end < 0)
                return "";
             
            beg = end + 1;
        }
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        end = SupportClass.getIndex(value_Renamed,'x',beg);
        if (end > 0)
            value_Renamed = value_Renamed.Substring(beg, (end)-(beg));
        else
            value_Renamed = value_Renamed.substring(beg); 
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        if (SupportClass.getIndex(value_Renamed,'x',0) == 0 && dimension == 0)
            return "0";
         
        return value_Renamed;
    }

    /**
    * @param stdField one of the custom rooms standard fields: STDF_ROOM1, STDF_ROOM2, STDF_ROOM3,
    * STDF_ROOM4, STDF_ROOM5, STDF_ROOM6, STDF_ROOM7, STDF_ROOM8, STDF_ROOM9, and STDF_ROOM10.
    * Sending standard rooms (STDF_LIVING_ROOM, STDF_DINING_ROOM, STDF_KITCHEN,
    * STDF_FAM_ROOM, STDF_MASTERBED, STDF_2ND_BED, STDF_3RD_BED, STDF_4TH_BED, STDF_5TH_BED, STDF_OFFICE,
    * STDF_DEN, STDF_GREAT_ROOM, STDF_LIBRARY, STDF_LAUNDRY, STDF_WORKSHOP) is also ok - the function returns
    * empty string as their description.
    * 
    *  @return  room description.
    */
    public String getRoomDescription(int stdField) throws Exception {
        String value_Renamed = getStdFieldValue(stdField);
        int i = value_Renamed.indexOf(',');
        if (i >= 0)
            return value_Renamed.Substring(0, (i)-(0));
         
        return "";
    }

    /**
    * @param stdField standard field id. One of the MLSCmaField.STDF_xxx constants.
    * 
    *  @param value field value.
    * 
    *  @return  true on success; false if this standard field doesn't exist in the fields
    * definitions for this board.
    */
    public boolean setStdFieldValue(int stdField, String value_Renamed) throws Exception {
        return setFieldValue(m_fields.getStdFieldIndex(stdField),value_Renamed);
    }

    public String getRecordID() throws Exception {
        return getStdFieldValue(TCSStandardResultFields.STDF_RECORDID,false);
    }

    private String validate(String value_Renamed, net.toppro.components.mls.engine.CmaField field) throws Exception {
        if (field != null)
        {
            switch(field.getStdId())
            {
                case TCSStandardResultFields.STDF_TPOZIP: 
                    if (StringSupport.Trim(value_Renamed).length() > 0)
                        value_Renamed = validateZipCode(value_Renamed);
                     
                    break;
                case TCSStandardResultFields.STDF_CMALISTINGDATE: 
                    // goto case TCSStandardResultFields.STDF_CMALISTINGDATE;
                    if (value_Renamed.length() == 0)
                        value_Renamed = calculateListingDate();
                     
                    break;
                case TCSStandardResultFields.STDF_CMAAGE: 
                    //Fix defect #67871, if age or year build = 0, year build will be the current year.
                    //if ( value.equals ( "0" ) ) // Sregei: Actually this should be moved to outside validations. But at the moment I wrote it they were not ready yet - I just don't want to loose the code.
                    //	value = "";
                    //else
                    value_Renamed = validateYearBuilt(value_Renamed,0);
                    break;
                case TCSStandardResultFields.STDF_TPOCITY: 
                    value_Renamed = StringSupport.Trim(net.toppro.components.mls.engine.CmaField.charTransf(value_Renamed,net.toppro.components.mls.engine.CmaField.CHAR_TRANSF_FIRST_CHAR_OF_WORD,m_engine).replace('/', ' '));
                    break;
                case TCSStandardResultFields.STDF_TPOSTATE: 
                case TCSStandardResultFields.STDF_AG_STATE: 
                case TCSStandardResultFields.STDF_OF_STATE: 
                    if (value_Renamed.length() == 0)
                        value_Renamed = getState();
                     
                    if (value_Renamed.length() == 2)
                        // this is state code
                        value_Renamed = value_Renamed.toUpperCase();
                    else // this is state name
                    if (value_Renamed.length() > 2)
                    {
                        value_Renamed = value_Renamed.toUpperCase();
                        if (m_statesProvinces == null)
                            m_statesProvinces = Tcs.Mls.Util.loadStatesAndProvinces();
                         
                        if (m_statesProvinces.containsKey(value_Renamed))
                            value_Renamed = m_statesProvinces.get(value_Renamed);
                        else
                            value_Renamed = ""; 
                    }
                    else
                    {
                        //CmaField.charTransf(value_Renamed, CmaField.CHAR_TRANSF_FIRST_CHAR_OF_WORD);
                        value_Renamed = "";
                    }  
                    value_Renamed = StringSupport.Trim(value_Renamed.replace('/', ' '));
                    if (!StringSupport.isNullOrEmpty(value_Renamed))
                    {
                        char[] charValue = value_Renamed.toCharArray();
                        if (charValue.length != 2)
                            value_Renamed = "";
                        else
                        {
                            for (int i = 0;i < charValue.length;i++)
                            {
                                if (Character.isDigit(charValue[i]))
                                {
                                    value_Renamed = "";
                                    break;
                                }
                                 
                            }
                        } 
                    }
                     
                    break;
                case TCSStandardResultFields.STDF_STDFSALEORLEASE: 
                    if (!StringSupport.isNullOrEmpty(value_Renamed))
                    {
                        if (!(StringSupport.Compare(value_Renamed, "Sale", true) == 0 || StringSupport.Compare(value_Renamed, "Lease", true) == 0))
                            value_Renamed = "";
                         
                    }
                     
                    break;
                case TCSStandardResultFields.STDF_CMALISTINGPRICE: 
                case TCSStandardResultFields.STDF_CMASALEPRICE: 
                case TCSStandardResultFields.STDF_CMATAXAMOUNTNORM: 
                case TCSStandardResultFields.STDF_CMAASSESSMENTNORM: 
                    value_Renamed = StringSupport.Trim(value_Renamed);
                    if (value_Renamed.length() > 0)
                    {
                        try
                        {
                            //UPGRADE_ISSUE: Class 'java.text.DecimalFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextDecimalFormat'"
                            //UPGRADE_ISSUE: Constructor 'java.text.DecimalFormat.DecimalFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextDecimalFormat'"
                            if (MLSUtil.isCurrency(value_Renamed))
                                value_Renamed = DoubleSupport.ToString(Double.valueOf(value_Renamed), "f");
                            else
                                value_Renamed = null; 
                        }
                        catch (Exception e)
                        {
                            //UPGRADE_TODO: Method 'java.text.Format.format' was converted to 'System.String.Format' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javatextFormatformat_javalangObject'"
                            //value_Renamed = System.String.Format(df2, System.Double.Parse(value_Renamed)); //(Double.valueOf(value) + "";//.toString();
                            value_Renamed = null;
                        }
                    
                    }
                     
                    break;
                case TCSStandardResultFields.STDF_CMADAYSONMARKET: 
                    value_Renamed = StringSupport.Trim(value_Renamed);
                    if (value_Renamed.length() > 0)
                    {
                        try
                        {
                            value_Renamed = String.valueOf(Short.valueOf(value_Renamed));
                        }
                        catch (Exception e)
                        {
                            value_Renamed = null;
                        }
                    
                    }
                     
                    break;
            
            }
            //case TCSStandardResultFields.STDF_NOTES:
            //    {
            //        System.Text.StringBuilder buf = null;
            //        for (int i = 0; i < TCSStandardResultFields.STANDARD_FIELDS_COUNT; i++)
            //        {
            //            if (i != TCSStandardResultFields.STDF_NOTES)
            //            {
            //                System.String str = m_validatedData[i];
            //                System.String pureValue = getStdFieldValue(i, false);
            //                if (str == null)
            //                {
            //                    if (pureValue.Length > 0)
            //                    {
            //                        str = validate(pureValue, getStdField(i));
            //                        if (str == null)
            //                            str = VALIDATION_FAILED;
            //                        m_validatedData[i] = str;
            //                    }
            //                    else
            //                        str = "";
            //                }
            //                if ((System.Object)str == (System.Object)VALIDATION_FAILED)
            //                {
            //                    if (buf == null)
            //                        buf = new System.Text.StringBuilder(value_Renamed);
            //                    buf.Append('\n');
            //                    buf.Append(STRINGS.getArray(STRINGS.MLS_RECORD_NOTES_PREFIXES)[i]);
            //                    buf.Append(pureValue);
            //                }
            //            }
            //        }
            //        if (buf != null)
            //            value_Renamed = buf.ToString();
            //    }
            //    break;
            if (m_validator != null)
                value_Renamed = m_validator.validateField(value_Renamed,field);
             
        }
         
        return value_Renamed;
    }

    public static String validateZipCode(String value_Renamed) throws Exception {
        String _value = value_Renamed;
        StringBuilder sb = new StringBuilder();
        char[] ch = value_Renamed.toCharArray();
        for (int i = 0;i < ch.length;i++)
        {
            if (Character.isLetterOrDigit(ch[i]))
                sb.append(Convert.ToString(ch[i]));
             
        }
        value_Renamed = sb.toString();
        if (MLSUtil.isNumber(value_Renamed))
        {
            if (value_Renamed.length() > 4)
                value_Renamed = value_Renamed.substring(0, (0) + ((5) - (0)));
            else if (value_Renamed.length() < 5)
                value_Renamed = _value;
              
        }
        else
        {
            if (value_Renamed.length() > 5)
            {
                value_Renamed = value_Renamed.substring(0, (0) + ((6) - (0)));
                value_Renamed = value_Renamed.substring(0, (0) + ((3) - (0))) + " " + value_Renamed.substring(3, (3) + ((6) - (3)));
            }
            else
            {
                //value = _value;
                value_Renamed = "";
            } 
        } 
        return value_Renamed;
    }

    private String calculateListingDate() throws Exception {
        String date = "";
        //if (m_type != TYPE_ACTIVE && m_type != TYPE_SOLD )
        //Only caculate listing date for Active Listing // Now for sold listing too.
        //    return date;
        String strDOM = getStdFieldValue(TCSStandardResultFields.STDF_CMADAYSONMARKET,false);
        if (strDOM.length() > 0)
        {
            try
            {
                int DOM = Integer.valueOf(strDOM);
                Date saleDate = new Date();
                switch(m_type)
                {
                    case TYPE_ACTIVE: 
                    case TYPE_PENDING: 
                        saleDate = Calendar.getInstance().getTime();
                        break;
                    case TYPE_EXPIRED: 
                        String exDate = getStdFieldValue(TCSStandardResultFields.STDF_STDFEXPIREDDATE,false);
                        if (StringSupport.isNullOrEmpty(exDate))
                            saleDate = Calendar.getInstance().getTime();
                        else
                            saleDate = MLSUtil.getDate(getStdFieldValue(TCSStandardResultFields.STDF_STDFEXPIREDDATE,false)); 
                        break;
                    case TYPE_SOLD: 
                        saleDate = MLSUtil.getDate(getStdFieldValue(TCSStandardResultFields.STDF_CMASALEDATE,false));
                        break;
                    default: 
                        return "";
                
                }
                date = MLSUtil.calculateListingDate(saleDate,DOM);
            }
            catch (Exception exc)
            {
                //UPGRADE_TODO: The 'System.DateTime' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
                //if (bSaleDateIsNull)
                //{
                //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
                //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
                //SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                //System.Globalization.Calendar list = new System.Globalization.GregorianCalendar();
                //System.Globalization.Calendar sale = new System.Globalization.GregorianCalendar();
                //System.Globalization.Calendar list1 = new System.Globalization.GregorianCalendar();
                //UPGRADE_TODO: The differences in the format  of parameters for method 'java.util.Calendar.setTime'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
                //UPGRADE_TODO: Constructor 'java.util.Date.Date' was converted to 'System.DateTime.DateTime' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilDateDate_long'"
                //UPGRADE_TODO: Method 'java.util.Date.getTime' was converted to 'System.DateTime.Ticks' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilDategetTime'"
                //SupportClass.CalendarManager.manager.SetDateTime(list, new System.DateTime(saleDate.Ticks - DOM * 86400000L));
                //UPGRADE_TODO: The differences in the format  of parameters for method 'java.util.Calendar.setTime'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
                //SupportClass.CalendarManager.manager.SetDateTime(sale, saleDate);
                //UPGRADE_TODO: The differences in the format  of parameters for method 'java.util.Calendar.setTime'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
                //UPGRADE_TODO: Constructor 'java.util.Date.Date' was converted to 'System.DateTime.DateTime' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilDateDate_long'"
                //UPGRADE_TODO: Method 'java.util.Date.getTime' was converted to 'System.DateTime.Ticks' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilDategetTime'"
                //SupportClass.CalendarManager.manager.SetDateTime(list1, new System.DateTime(saleDate.Ticks - DOM * 86400000L));
                //UPGRADE_ISSUE: Method 'java.util.Calendar.add' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javautilCalendaradd_int_int'"
                //list1.add(SupportClass.CalendarManager.DATE, DOM);
                //int i;
                //while ((i = compare(list1, sale)) != 0)
                //{
                //    if (i < 0)
                //    {
                //        //UPGRADE_ISSUE: Method 'java.util.Calendar.add' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javautilCalendaradd_int_int'"
                //        list.add(SupportClass.CalendarManager.DATE, 1);
                //        //UPGRADE_ISSUE: Method 'java.util.Calendar.add' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javautilCalendaradd_int_int'"
                //        list1.add(SupportClass.CalendarManager.DATE, 1);
                //    }
                //    else
                //    {
                //        //UPGRADE_ISSUE: Method 'java.util.Calendar.add' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javautilCalendaradd_int_int'"
                //        list.add(SupportClass.CalendarManager.DATE, - 1);
                //        //UPGRADE_ISSUE: Method 'java.util.Calendar.add' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javautilCalendaradd_int_int'"
                //        list1.add(SupportClass.CalendarManager.DATE, - 1);
                //    }
                //}
                ////UPGRADE_TODO: The equivalent in .NET for method 'java.util.Calendar.getTime' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                //date = SupportClass.FormatDateTime(dateFormat, SupportClass.CalendarManager.manager.GetDateTime(list));
                //}
                System.out.println("Failed to calculate listing date");
                SupportClass.WriteStackTrace(exc, Console.Error);
            }
        
        }
         
        return date;
    }

    private int initPublicType(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        DefParser parser = engine.getDefParser();
        String typeFieldName = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTFIELD);
        if (typeFieldName != null && typeFieldName.length() > 0)
        {
            String typeFieldValue = getFieldValue(typeFieldName);
            if (typeFieldValue != null && typeFieldValue.length() > 0)
            {
                String[][] typeStrings = engine.getPublicTypeStrings();
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                        if (typeStrings[i][j].equals(typeFieldValue))
                            return i + 1;
                         
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                        if (typeStrings[i][j].toUpperCase().equals(typeFieldValue.toUpperCase()))
                            return i + 1;
                         
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                    {
                        if (typeStrings[i][j].indexOf(typeFieldValue) >= 0)
                            return i + 1;
                        else if (typeFieldValue.indexOf(typeStrings[i][j]) >= 0)
                            return i + 1;
                          
                    }
                typeFieldValue = typeFieldValue.toLowerCase();
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                    {
                        if (typeStrings[i][j].toLowerCase().indexOf(typeFieldValue) >= 0)
                            return i + 1;
                        else if (typeFieldValue.indexOf(typeStrings[i][j].toLowerCase()) >= 0)
                            return i + 1;
                          
                    }
            }
             
        }
         
        return TYPE_UNKNOWN;
    }

    private int initType(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        DefParser parser = engine.getDefParser();
        String typeFieldName = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTFIELD);
        if (typeFieldName != null && typeFieldName.length() > 0)
        {
            String typeFieldValue = getFieldValue(typeFieldName);
            if (typeFieldValue != null && typeFieldValue.length() > 0)
            {
                String[][] typeStrings = engine.getTypeStrings();
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                        if (typeStrings[i][j].equals(typeFieldValue))
                            return i + 1;
                         
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                        if (typeStrings[i][j].toUpperCase().equals(typeFieldValue.toUpperCase()))
                            return i + 1;
                         
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                    {
                        if (typeStrings[i][j].indexOf(typeFieldValue) >= 0)
                            return i + 1;
                        else if (typeFieldValue.indexOf(typeStrings[i][j]) >= 0)
                            return i + 1;
                          
                    }
                typeFieldValue = typeFieldValue.toLowerCase();
                for (int i = 0;i < typeStrings.length;i++)
                    for (int j = 0;j < typeStrings[i].length;j++)
                    {
                        if (typeStrings[i][j].toLowerCase().indexOf(typeFieldValue) >= 0)
                            return i + 1;
                        else if (typeFieldValue.indexOf(typeStrings[i][j].toLowerCase()) >= 0)
                            return i + 1;
                          
                    }
            }
             
        }
         
        return TYPE_UNKNOWN;
    }

    public int getMLSRecordType() throws Exception {
        return m_type;
    }

    public int getPublicType() throws Exception {
        return m_publicType;
    }

    public boolean isChecked() throws Exception {
        return m_checked;
    }

    public void setChecked(boolean checked_Renamed) throws Exception {
        m_checked = checked_Renamed;
    }

    public void check() throws Exception {
        m_checked = true;
    }

    public void uncheck() throws Exception {
        m_checked = false;
    }

    public int getPictureDownloadState() throws Exception {
        return m_picture_download_state;
    }

    public void setPictureDownloadState(int state) throws Exception {
        boolean tmpBool = false;
        try
        {
            if (File.Exists((new File(m_picture_filename)).FullName))
                tmpBool = true;
            else
                tmpBool = File.Exists((new File(m_picture_filename)).FullName); 
        }
        catch (Exception exc)
        {
        }

        boolean has_picture = tmpBool;
        switch(state)
        {
            case PICTURE_DOWNLOADED: 
            case PICTURE_DOWNLOAD_FAILED: 
                if (has_picture)
                    m_picture_download_state = PICTURE_DOWNLOADED;
                else
                    m_picture_download_state = PICTURE_DOWNLOAD_FAILED; 
                break;
            case PICTURE_DOWNLOADED_FINALY: 
            case PICTURE_DOWNLOAD_FAILED_FINALY: 
                if (has_picture)
                    m_picture_download_state = PICTURE_DOWNLOADED_FINALY;
                else
                    m_picture_download_state = PICTURE_DOWNLOAD_FAILED_FINALY; 
                break;
            case PICTURE_NOT_DOWNLOADED: 
            default: 
                if (has_picture)
                    m_picture_download_state = PICTURE_DOWNLOADED;
                else
                    m_picture_download_state = PICTURE_NOT_DOWNLOADED; 
                break;
        
        }
    }

    public boolean hasPicture() throws Exception {
        return m_picture_download_state == PICTURE_DOWNLOADED || m_picture_download_state == PICTURE_DOWNLOADED_FINALY;
    }

    //Fix TT#67988 Get picture button at least was clicked once.
    public boolean hasTriedDownloadPicture() throws Exception {
        return m_picture_download_state != PICTURE_NOT_DOWNLOADED;
    }

    public boolean canPictureBeDownloaded() throws Exception {
        return m_picture_download_state != PICTURE_DOWNLOADED_FINALY && m_picture_download_state != PICTURE_DOWNLOAD_FAILED_FINALY;
    }

    public String getPictureFileName() throws Exception {
        switch(m_picture_download_state)
        {
            case PICTURE_DOWNLOADED: 
            case PICTURE_DOWNLOADED_FINALY: 
                return m_picture_filename;
            case PICTURE_DOWNLOAD_FAILED: 
            case PICTURE_DOWNLOAD_FAILED_FINALY: 
                return m_fields.getEngine().getNoPictureFilename();
        
        }
        return null;
    }

    public String[] getMultiPictureFileName() throws Exception {
        return m_multi_picture_filename;
    }

    public void setMultiPictureFileName(String[] array) throws Exception {
        m_multi_picture_filename = array;
        if (m_multi_picture_filename != null && m_multi_picture_filename.length > 0)
            m_picture_filename = m_multi_picture_filename[0];
        else
        {
            File file = new File(m_picture_filename);
            boolean tmpBool;
            if (File.Exists(file.FullName))
                tmpBool = true;
            else
                tmpBool = File.Exists(file.FullName); 
            if (tmpBool)
            {
                boolean tmpBool2;
                if (File.Exists(file.FullName))
                {
                    File.Delete(file.FullName);
                    tmpBool2 = true;
                }
                else if (File.Exists(file.FullName))
                {
                    File.Delete(file.FullName);
                    tmpBool2 = true;
                }
                else
                    tmpBool2 = false;  
                boolean generatedAux = tmpBool2;
            }
             
        } 
    }

    public String getDedicatedPictureFileName() throws Exception {
        return m_picture_filename;
    }

    //private static int compare(System.Globalization.Calendar c1, System.Globalization.Calendar c2)
    //{
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    int i1 = SupportClass.CalendarManager.manager.Get(c1, SupportClass.CalendarManager.YEAR);
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    int i2 = SupportClass.CalendarManager.manager.Get(c2, SupportClass.CalendarManager.YEAR);
    //    if (i1 < i2)
    //        return -1;
    //    else if (i1 > i2)
    //        return 1;
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    i1 = SupportClass.CalendarManager.manager.Get(c1, SupportClass.CalendarManager.DAY_OF_YEAR);
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    i2 = SupportClass.CalendarManager.manager.Get(c2, SupportClass.CalendarManager.DAY_OF_YEAR);
    //    if (i1 < i2)
    //        return -1;
    //    else if (i1 > i2)
    //        return 1;
    //    return 0;
    //}
    //UPGRADE_NOTE: ref keyword was added to struct-type parameters. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1303'"
    public static int getDaysOnMarket(Date list_date, Date sale_date) throws Exception {
        //UPGRADE_TODO: Method 'java.util.Date.getTime' was converted to 'System.DateTime.Ticks' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilDategetTime'"
        TimeSpan diff = (new TimeSpan(Math.abs(sale_date.getTime() - list_date.getTime())));
        return diff.Days;
    }

    public MLSCmaFields getFields() throws Exception {
        return m_fields;
    }

    /**
    * Returns array of separated values for export to listings.
    */
    public String[] getFeaturesList() throws Exception {
        return getSTDFFieldList(TCSStandardResultFields.STDF_CMAFEATURE,null,false);
    }

    public String getNotes() throws Exception {
        String result = "";
        String[] notes = getSTDFFieldList(TCSStandardResultFields.STDF_NOTES,new int[]{ TCSStandardResultFields.STDF_STDFVIEWS },true);
        if (notes != null && notes.length > 0)
        {
            for (int i = 0;i < notes.length;i++)
            {
                result = result + notes[i] + (i == notes.length - 1 ? "" : ", ");
            }
        }
         
        return result;
    }

    public String getCMAArea() throws Exception {
        String result = "";
        String[] area = getSTDFFieldList(TCSStandardResultFields.STDF_CMAAREA,new int[]{ TCSStandardResultFields.STDF_STDFVIEWS },true);
        if (area != null && area.length > 0)
        {
            for (int i = 0;i < area.length;i++)
            {
                result = result + area[i] + (i == area.length - 1 ? "" : ", ");
            }
        }
         
        if (result.length() > 0)
        {
            net.toppro.components.mls.engine.CmaField f = getStdField(TCSStandardResultFields.STDF_CMAAREA);
            if (f != null)
                result = f.getPrefix() + net.toppro.components.mls.engine.CmaField.charTransf(result,f.getCharsTransf(),m_engine);
             
        }
         
        return result;
    }

    public String[] getViewsList() throws Exception {
        net.toppro.components.mls.engine.CmaField field = getFields().getStdField(TCSStandardResultFields.STDF_STDFVIEWS);
        if (field == null)
            return null;
         
        String delimiter = field.getDelimiter();
        if (delimiter == null || delimiter.length() == 0)
            delimiter = ",";
         
        String value_Renamed = getStdFieldValue(TCSStandardResultFields.STDF_STDFVIEWS);
        int pos = value_Renamed.indexOf(": ");
        if (pos > -1 && pos + 1 < value_Renamed.length())
            value_Renamed = value_Renamed.substring(pos + 1);
         
        SupportClass.Tokenizer views = new SupportClass.Tokenizer(value_Renamed,delimiter);
        ArrayList result = ArrayList.Synchronized(new ArrayList(10));
        String view = null;
        while (views.hasMoreTokens())
        {
            view = ((String)views.nextToken());
            if (view == null)
                continue;
             
            view = Tcs.Mls.Util.filterJunkValue(StringSupport.Trim(view));
            if (!StringSupport.isNullOrEmpty(view))
                result.add(view);
             
        }
        if (result.size() == 0)
            return null;
         
        String[] result_values = new String[result.size()];
        for (int i = 0;i < result.size();i++)
            result_values[i] = ((String)result.get(i));
        return result_values;
    }

    public String getUnits(boolean isNonStandard) throws Exception {
        if (m_fields.UnitsList == null || m_fields.UnitsList.size() == 0)
            return "";
         
        String actualRent = "";
        String furnishedYN = "";
        String numberBaths = "";
        String numberBeds = "";
        String numberUnits = "";
        String totalRent = "";
        int fieldIndex = -1;
        net.toppro.components.mls.engine.CmaField fieldActualRent = null;
        net.toppro.components.mls.engine.CmaField fieldFurnishedYN = null;
        net.toppro.components.mls.engine.CmaField fieldNumberBaths = null;
        net.toppro.components.mls.engine.CmaField fieldNumberBeds = null;
        net.toppro.components.mls.engine.CmaField fieldNumberUnits = null;
        net.toppro.components.mls.engine.CmaField fieldTotalRent = null;
        StringBuilder resultBuffer = new StringBuilder();
        for (int i = 1;i < m_fields.UnitsList.size() + 1;i++)
        {
            fieldIndex = m_fields.getFieldIndex("Un" + i + "ActualRent");
            actualRent = Tcs.Mls.Util.filterJunkValue(getFieldValue(fieldIndex));
            fieldIndex = m_fields.getFieldIndex("Un" + i + "FurnishedYN");
            furnishedYN = Tcs.Mls.Util.filterJunkValue(getFieldValue(fieldIndex));
            fieldIndex = m_fields.getFieldIndex("Un" + i + "NumberBaths");
            numberBaths = Tcs.Mls.Util.filterJunkValue(getFieldValue(fieldIndex));
            fieldIndex = m_fields.getFieldIndex("Un" + i + "NumberBeds");
            numberBeds = Tcs.Mls.Util.filterJunkValue(getFieldValue(fieldIndex));
            fieldIndex = m_fields.getFieldIndex("Un" + i + "NumberUnits");
            numberUnits = Tcs.Mls.Util.filterJunkValue(getFieldValue(fieldIndex));
            fieldIndex = m_fields.getFieldIndex("Un" + i + "TotalRent");
            totalRent = Tcs.Mls.Util.filterJunkValue(getFieldValue(fieldIndex));
            StringBuilder sb = new StringBuilder();
            if (!isNonStandard)
            {
                if (!StringSupport.isNullOrEmpty(actualRent))
                    sb.append("<UnitActualRent>" + Tcs.Mls.Util.convertStringToXML(actualRent) + "</UnitActualRent>");
                 
                if (!StringSupport.isNullOrEmpty(furnishedYN))
                    sb.append("<UnitFurnishedYN>" + Tcs.Mls.Util.convertStringToXML(furnishedYN) + "</UnitFurnishedYN>");
                 
                if (!StringSupport.isNullOrEmpty(numberBaths))
                    sb.append("<UnitNumberBaths>" + Tcs.Mls.Util.convertStringToXML(numberBaths) + "</UnitNumberBaths>");
                 
                if (!StringSupport.isNullOrEmpty(numberBeds))
                    sb.append("<UnitNumberBeds>" + Tcs.Mls.Util.convertStringToXML(numberBeds) + "</UnitNumberBeds>");
                 
                if (!StringSupport.isNullOrEmpty(numberUnits))
                    sb.append("<UnitNumberUnits>" + Tcs.Mls.Util.convertStringToXML(numberUnits) + "</UnitNumberUnits>");
                 
                if (!StringSupport.isNullOrEmpty(totalRent))
                    sb.append("<UnitTotalRent>" + Tcs.Mls.Util.convertStringToXML(totalRent) + "</UnitTotalRent>");
                 
            }
            else
            {
                if (!StringSupport.isNullOrEmpty(actualRent))
                    sb.append("<MultiUnitInfo_Un" + i + "ActualRent>" + Tcs.Mls.Util.convertStringToXML(actualRent) + "</MultiUnitInfo_Un" + i + "ActualRent>");
                 
                if (!StringSupport.isNullOrEmpty(furnishedYN))
                    sb.append("<MultiUnitInfo_Un" + i + "FurnishedYN>" + Tcs.Mls.Util.convertStringToXML(furnishedYN) + "</MultiUnitInfo_Un" + i + "FurnishedYN>");
                 
                if (!StringSupport.isNullOrEmpty(numberBaths))
                    sb.append("<MultiUnitInfo_Un" + i + "NumberBaths>" + Tcs.Mls.Util.convertStringToXML(numberBaths) + "</MultiUnitInfo_Un" + i + "NumberBaths>");
                 
                if (!StringSupport.isNullOrEmpty(numberBeds))
                    sb.append("<MultiUnitInfo_Un" + i + "NumberBeds>" + Tcs.Mls.Util.convertStringToXML(numberBeds) + "</MultiUnitInfo_Un" + i + "NumberBeds>");
                 
                if (!StringSupport.isNullOrEmpty(numberUnits))
                    sb.append("<MultiUnitInfo_Un" + i + "NumberUnits>" + Tcs.Mls.Util.convertStringToXML(numberUnits) + "</MultiUnitInfo_Un" + i + "NumberUnits>");
                 
                if (!StringSupport.isNullOrEmpty(totalRent))
                    sb.append("<MultiUnitInfo_Un" + i + "TotalRent>" + Tcs.Mls.Util.convertStringToXML(totalRent) + "</MultiUnitInfo_Un" + i + "TotalRent>");
                 
            } 
            if (sb.length() > 0)
            {
                if (!isNonStandard)
                {
                    resultBuffer.append("<Unit>");
                    resultBuffer.append(sb.toString());
                    resultBuffer.append("</Unit>");
                }
                else
                    resultBuffer.append(sb.toString()); 
            }
             
        }
        if (resultBuffer.length() > 0)
        {
            if (!isNonStandard)
            {
                resultBuffer.append("</Units>");
                resultBuffer.insert(0, "<Units>");
            }
             
        }
         
        return resultBuffer.toString();
    }

    public String getRoomDimensions(boolean isDataAggListing, boolean isCyclicFormat, boolean isDisplayOnly) throws Exception {
        String _DEFAULT_VALUE_DELIMITER_ = ",";
        net.toppro.components.mls.engine.CmaField cma_field = m_fields.getStdField(TCSStandardResultFields.STDF_STDFROOMDIM);
        StringBuilder resultBuffer = new StringBuilder();
        if (cma_field == null)
            return null;
         
        String cont_fields = cma_field.getRecordPosition();
        if (cont_fields == null || cont_fields.length() == 0)
            return null;
         
        int index;
        String field_name;
        String value_Renamed;
        String subvalue;
        String subvalues = "";
        String oldPrefix = "";
        String prefix = "";
        index = cont_fields.indexOf("\\", 1);
        String[] field_names = cont_fields.Substring(1, (index)-(1)).ToUpper().Split(',');
        String value_delimiter = _DEFAULT_VALUE_DELIMITER_;
        net.toppro.components.mls.engine.CmaField field = null;
        String dimension_fields = "";
        for (int n = 0;n < field_names.length;n++)
        {
            value_delimiter = _DEFAULT_VALUE_DELIMITER_;
            field_name = field_names[n];
            int fieldIndex = m_fields.getFieldIndex(field_name);
            field = m_fields.getField(fieldIndex);
            if (field == null)
                continue;
             
            if (field.getDelimiter().length() != 0)
                value_delimiter = field.getDelimiter();
             
            value_Renamed = getFieldValue(fieldIndex);
            value_Renamed = net.toppro.components.mls.engine.CmaField.getValidFeature(value_Renamed);
            prefix = "";
            if (field.prefix.length() != 0 && value_Renamed.length() != 0)
            {
                prefix = field.getPrefix();
                //prefix = MLSUtil.toStr(field.prefix);
                if (prefix.length() < value_Renamed.length())
                    value_Renamed = value_Renamed.Substring(prefix.length(), (value_Renamed.Length)-(prefix.length()));
                else
                    value_Renamed = ""; 
            }
             
            if (value_Renamed.length() != 0)
            {
                int pos = value_Renamed.indexOf(":");
                if (pos > -1)
                {
                    if (StringSupport.isNullOrEmpty(prefix))
                    {
                        if (pos > 0)
                            prefix = value_Renamed.substring(0, (0) + (pos));
                         
                    }
                     
                    value_Renamed = value_Renamed.substring(pos + 1);
                }
                 
                if (!StringSupport.isNullOrEmpty(prefix) && prefix.indexOf(":") > -1)
                    prefix = StringSupport.Trim(prefix.substring(0, (0) + (prefix.indexOf(":"))));
                 
                String[] subValues = StringSupport.Split(value_Renamed.toLowerCase(), 'x');
                if (subValues.length > 1)
                {
                    subValues[0] = Tcs.Mls.Util.filterJunkValueForRoomDimensions(StringSupport.Trim(subValues[0]));
                    subValues[1] = Tcs.Mls.Util.filterJunkValueForRoomDimensions(StringSupport.Trim(subValues[1]));
                }
                 
                if (!StringSupport.isNullOrEmpty(field.displayname))
                    prefix = field.displayname;
                 
                if (!StringSupport.isNullOrEmpty(field.dynamicname))
                {
                    String dname = getFieldValue(m_fields.getFieldIndex(field.dynamicname));
                    if (!StringSupport.isNullOrEmpty(dname))
                        prefix = dname;
                     
                }
                 
                try
                {
                    if (!StringSupport.isNullOrEmpty(prefix) && subValues.length > 1 && !StringSupport.isNullOrEmpty(subValues[0]) && !StringSupport.isNullOrEmpty(subValues[1]))
                    {
                        if (!isDataAggListing)
                        {
                            if (!isDisplayOnly)
                                resultBuffer.append("<Room Name=\"" + Tcs.Mls.Util.convertStringToXML(prefix) + "\" Width=\"" + Tcs.Mls.Util.convertStringToXML(subValues[0]) + "\" Length=\"" + Tcs.Mls.Util.convertStringToXML(subValues[1]) + "\"/>\r\n");
                            else
                                resultBuffer.append("<Feature Value=\"" + Tcs.Mls.Util.convertStringToXML(prefix) + ": " + Tcs.Mls.Util.convertStringToXML(subValues[0]) + " x " + Tcs.Mls.Util.convertStringToXML(subValues[1]) + "\"/>\r\n"); 
                        }
                        else
                        {
                            resultBuffer.append("<Room>");
                            resultBuffer.append("<RoomDimensions1>" + Tcs.Mls.Util.convertStringToXML(subValues[0]) + "</RoomDimensions1>");
                            resultBuffer.append("<RoomDimensions2>" + Tcs.Mls.Util.convertStringToXML(subValues[1]) + "</RoomDimensions2>");
                            //if (!string.IsNullOrEmpty(feature))
                            //    resultBuffer.Append("<RoomFeatures>" + "" + "</RoomFeatures>");
                            resultBuffer.append("<RoomType>" + Tcs.Mls.Util.convertStringToXML(prefix) + "</RoomType>");
                            resultBuffer.append("</Room>");
                        } 
                    }
                     
                }
                catch (Exception __dummyCatchVar1)
                {
                }
            
            }
             
        }
        if (resultBuffer.length() > 0)
        {
            if (isDataAggListing)
            {
                resultBuffer.append("</Rooms>");
                resultBuffer.insert(0, "<Rooms>");
            }
             
        }
         
        return resultBuffer.toString();
    }

    public int[] getFeatureFieldList(int[] excludeFeild) throws Exception {
        //long time1 = (System.DateTime.Now.Ticks - 621355968000000000);
        String _DEFAULT_VALUE_DELIMITER_ = ",";
        net.toppro.components.mls.engine.CmaField cma_field = m_fields.getStdField(TCSStandardResultFields.STDF_CMAFEATURE);
        if (cma_field == null)
            return null;
         
        String cont_fields = cma_field.getRecordPosition();
        if (cont_fields == null || cont_fields.length() == 0)
            return null;
         
        int index;
        String field_name;
        String value_Renamed;
        String subvalue;
        String subvalues = "";
        String oldPrefix = "";
        String prefix = "";
        CSList<Integer> result = new CSList<Integer>();
        //System.String[] result_values = null;
        // m_engine.WriteLine("11111= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time1));
        //long time2 = (System.DateTime.Now.Ticks - 621355968000000000);
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        if ((index = SupportClass.getIndex(cont_fields,"\\",1)) == -1)
        {
            StringBuilder sb = new StringBuilder();
            //char[] array = getStdFieldValue(stdfFeild).ToCharArray();
            result.add(m_fields.getFieldIndex(cma_field.name));
            return ((int[]) result.toArray());
        }
         
        // m_engine.WriteLine("22222= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time2));
        // long time3 = (System.DateTime.Now.Ticks - 621355968000000000);
        String[] field_names = cont_fields.Substring(1, (index)-(1)).ToUpper().Split(',');
        String value_delimiter = _DEFAULT_VALUE_DELIMITER_;
        net.toppro.components.mls.engine.CmaField field = null;
        String dimension_fields = "";
        String[] excludeFields = new String[2];
        if (excludeFeild != null)
        {
            excludeFields = new String[excludeFeild.length];
            for (int i = 0;i < excludeFeild.length;i++)
            {
                net.toppro.components.mls.engine.CmaField stdf_field = m_fields.getStdField(excludeFeild[i]);
                if (stdf_field != null)
                    excludeFields[i] = stdf_field.getRecordPosition().toUpperCase();
                 
                if (excludeFields[i] == null)
                    excludeFields[i] = "";
                 
            }
        }
         
        for (int n = 0;n < field_names.length;n++)
        {
            //m_engine.WriteLine("33333= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time3));
            //long time4 = (System.DateTime.Now.Ticks - 621355968000000000);
            //long time11 = (System.DateTime.Now.Ticks - 621355968000000000);
            value_delimiter = _DEFAULT_VALUE_DELIMITER_;
            field_name = field_names[n];
            int fieldIndex = m_fields.getFieldIndex(field_name);
            field = m_fields.getField(fieldIndex);
            if (field == null)
                continue;
             
            //m_engine.WriteLine(field_name + "a1= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time11));
            if (field_name.startsWith("STDF"))
                continue;
            else if (excludeFeild != null)
            {
                boolean bFoundExcField = false;
                for (int i = 0;i < excludeFields.length;i++)
                {
                    if (excludeFields[i].length() > 0 && excludeFields[i].indexOf(field_name) > -1)
                    {
                        bFoundExcField = true;
                        break;
                    }
                     
                }
                if (bFoundExcField)
                    continue;
                 
            }
              
            result.add(fieldIndex);
        }
        return ((int[]) result.toArray());
    }

    public String[] getSTDFFieldList(int stdfFeild, int[] excludeFeild, boolean bMultiValues) throws Exception {
        //long time1 = (System.DateTime.Now.Ticks - 621355968000000000);
        String _DEFAULT_VALUE_DELIMITER_ = ",";
        net.toppro.components.mls.engine.CmaField cma_field = m_fields.getStdField(stdfFeild);
        if (cma_field == null)
            return null;
         
        String cont_fields = cma_field.getRecordPosition();
        if (cont_fields == null || cont_fields.length() == 0)
            return null;
         
        int index;
        String field_name;
        String value_Renamed;
        String subvalue;
        String subvalues = "";
        String oldPrefix = "";
        String prefix = "";
        ArrayList result = new ArrayList();
        String[] result_values = null;
        // m_engine.WriteLine("11111= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time1));
        //long time2 = (System.DateTime.Now.Ticks - 621355968000000000);
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        if ((index = SupportClass.getIndex(cont_fields,"\\",1)) == -1)
        {
            StringBuilder sb = new StringBuilder();
            //char[] array = getStdFieldValue(stdfFeild).ToCharArray();
            result_values = new String[]{ getStdFieldValue(stdfFeild) };
            return result_values;
        }
         
        // m_engine.WriteLine("22222= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time2));
        // long time3 = (System.DateTime.Now.Ticks - 621355968000000000);
        String[] field_names = cont_fields.Substring(1, (index)-(1)).ToUpper().Split(',');
        String value_delimiter = _DEFAULT_VALUE_DELIMITER_;
        net.toppro.components.mls.engine.CmaField field = null;
        String dimension_fields = "";
        String[] excludeFields = new String[2];
        if (excludeFeild != null)
        {
            excludeFields = new String[excludeFeild.length];
            for (int i = 0;i < excludeFeild.length;i++)
            {
                net.toppro.components.mls.engine.CmaField stdf_field = m_fields.getStdField(excludeFeild[i]);
                if (stdf_field != null)
                    excludeFields[i] = stdf_field.getRecordPosition().toUpperCase();
                 
                if (excludeFields[i] == null)
                    excludeFields[i] = "";
                 
            }
        }
         
        for (int n = 0;n < field_names.length;n++)
        {
            //m_engine.WriteLine("33333= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time3));
            //long time4 = (System.DateTime.Now.Ticks - 621355968000000000);
            //long time11 = (System.DateTime.Now.Ticks - 621355968000000000);
            value_delimiter = _DEFAULT_VALUE_DELIMITER_;
            field_name = field_names[n];
            int fieldIndex = m_fields.getFieldIndex(field_name);
            field = m_fields.getField(fieldIndex);
            if (field == null)
                continue;
            else //m_engine.WriteLine(field_name + "a1= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time11));
            //if (field_name.StartsWith("STDF"))
            //    continue;
            if (excludeFeild != null)
            {
                boolean bFoundExcField = false;
                for (int i = 0;i < excludeFields.length;i++)
                {
                    if (excludeFields[i].length() > 0 && excludeFields[i].indexOf(field_name) > -1)
                    {
                        bFoundExcField = true;
                        break;
                    }
                     
                }
                if (bFoundExcField)
                    continue;
                 
            }
              
            if (field.getDelimiter().length() != 0)
                value_delimiter = field.getDelimiter();
             
            //value_delimiter = MLSUtil.toStr(field.getDelimiter());
            //long time122 = (System.DateTime.Now.Ticks - 621355968000000000);
            value_Renamed = getFieldValue(fieldIndex);
            value_Renamed = net.toppro.components.mls.engine.CmaField.getValidFeature(value_Renamed);
            //m_engine.WriteLine("a122= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time122));
            //long time12 = (System.DateTime.Now.Ticks - 621355968000000000);
            prefix = "";
            if (field.prefix.length() != 0 && value_Renamed.length() != 0)
            {
                prefix = field.getPrefix();
                //prefix = MLSUtil.toStr(field.prefix);
                if (prefix.length() < value_Renamed.length())
                    value_Renamed = value_Renamed.Substring(prefix.length(), (value_Renamed.Length)-(prefix.length()));
                else
                    value_Renamed = ""; 
            }
             
            // m_engine.WriteLine("a2= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time12));
            //long time13 = (System.DateTime.Now.Ticks - 621355968000000000);
            if (value_Renamed.length() != 0)
            {
                if (!bMultiValues)
                {
                    String[] field_values = value_Renamed.split(StringSupport.charAltsToRegex(value_delimiter.toCharArray()));
                    for (int j = 0;j < field_values.length;j++)
                    {
                        subvalue = field_values[j];
                        if (field.getCharsTransf() == 0)
                            subvalue = net.toppro.components.mls.engine.CmaField.charTransf(subvalue,net.toppro.components.mls.engine.CmaField.CHAR_TRANSF_FIRST_CHAR_OF_WORD,m_engine);
                         
                        if (StringSupport.Trim(subvalue).length() != 0)
                            result.add(prefix + subvalue);
                         
                    }
                }
                else
                    result.add(prefix + net.toppro.components.mls.engine.CmaField.charTransf(value_Renamed,cma_field.getCharsTransf(),m_engine)); 
            }
             
        }
        //m_engine.WriteLine("a3= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time13));
        //m_engine.WriteLine("44444= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time4));
        if (result.size() == 0)
            return null;
         
        //long time5 = (System.DateTime.Now.Ticks - 621355968000000000);
        result_values = new String[result.size()];
        for (int i = 0;i < result.size();i++)
            result_values[i] = (String)result.get(i);
        return result_values;
    }

    //m_engine.WriteLine("55555= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time5));
    public String[] getFeatureNoRoomDimension(boolean bMultiValues) throws Exception {
        //long time1 = (System.DateTime.Now.Ticks - 621355968000000000);
        String _DEFAULT_VALUE_DELIMITER_ = ",";
        net.toppro.components.mls.engine.CmaField cma_field = m_fields.getStdField(TCSStandardResultFields.STDF_CMAFEATURE);
        if (cma_field == null)
            return null;
         
        // stdf_field = m_fields.getStdField(TCSStandardResultFields.STDF_STDFROOMDIM);
        //if (stdf_field == null)
        //    return null;
        String cont_fields = cma_field.getRecordPosition();
        if (cont_fields == null || cont_fields.length() == 0)
            return null;
         
        String excludeField = "";
        if (cont_fields.IndexOf("STDFRoomDim", 0, StringComparison.CurrentCultureIgnoreCase) < 0)
        {
            net.toppro.components.mls.engine.CmaField stdf_field = m_fields.getStdField(TCSStandardResultFields.STDF_STDFROOMDIM);
            if (stdf_field != null)
            {
                excludeField = stdf_field.getRecordPosition().toUpperCase();
                excludeField = excludeField.replace('\\', ',');
            }
             
        }
         
        int index;
        String field_name;
        String value_Renamed;
        String subvalue;
        String subvalues = "";
        String oldPrefix = "";
        String prefix = "";
        ArrayList result = new ArrayList();
        String[] result_values = null;
        // m_engine.WriteLine("11111= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time1));
        //long time2 = (System.DateTime.Now.Ticks - 621355968000000000);
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        if ((index = SupportClass.getIndex(cont_fields,"\\",1)) == -1)
        {
            StringBuilder sb = new StringBuilder();
            char[] array = getStdFieldValue(TCSStandardResultFields.STDF_CMAFEATURE).toCharArray();
            result_values = new String[]{ getStdFieldValue(TCSStandardResultFields.STDF_CMAFEATURE) };
            return result_values;
        }
         
        // m_engine.WriteLine("22222= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time2));
        // long time3 = (System.DateTime.Now.Ticks - 621355968000000000);
        String[] field_names = cont_fields.Substring(1, (index)-(1)).ToUpper().Split(',');
        String value_delimiter = _DEFAULT_VALUE_DELIMITER_;
        net.toppro.components.mls.engine.CmaField field = null;
        String dimension_fields = "";
        String[] excludeFields = new String[2];
        for (int n = 0;n < field_names.length;n++)
        {
            //m_engine.WriteLine("33333= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time3));
            //long time4 = (System.DateTime.Now.Ticks - 621355968000000000);
            //long time11 = (System.DateTime.Now.Ticks - 621355968000000000);
            value_delimiter = _DEFAULT_VALUE_DELIMITER_;
            field_name = field_names[n];
            int fieldIndex = m_fields.getFieldIndex(field_name);
            field = m_fields.getField(fieldIndex);
            if (field == null)
                continue;
             
            //m_engine.WriteLine(field_name + "a1= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time11));
            if (StringSupport.Compare(field_name, "STDFRoomDim", true) == 0)
                continue;
            else if (!StringSupport.isNullOrEmpty(excludeField))
            {
                if (excludeField.indexOf(field_name + ",") > -1)
                    continue;
                 
            }
              
            if (field.getDelimiter().length() != 0)
                value_delimiter = field.getDelimiter();
             
            //value_delimiter = MLSUtil.toStr(field.getDelimiter());
            //long time122 = (System.DateTime.Now.Ticks - 621355968000000000);
            value_Renamed = getFieldValue(fieldIndex);
            value_Renamed = net.toppro.components.mls.engine.CmaField.getValidFeature(value_Renamed);
            //m_engine.WriteLine("a122= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time122));
            //long time12 = (System.DateTime.Now.Ticks - 621355968000000000);
            prefix = "";
            if (field.prefix.length() != 0 && value_Renamed.length() != 0)
            {
                prefix = field.getPrefix();
                //prefix = MLSUtil.toStr(field.prefix);
                if (prefix.length() < value_Renamed.length() && value_Renamed.IndexOf(prefix, 0, StringComparison.CurrentCultureIgnoreCase) > -1)
                    value_Renamed = value_Renamed.Substring(prefix.length(), (value_Renamed.Length)-(prefix.length()));
                else
                    value_Renamed = ""; 
            }
             
            // m_engine.WriteLine("a2= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time12));
            //long time13 = (System.DateTime.Now.Ticks - 621355968000000000);
            if (value_Renamed.length() != 0)
            {
                if (!bMultiValues)
                {
                    String[] field_values = value_Renamed.split(StringSupport.charAltsToRegex(value_delimiter.toCharArray()));
                    for (int j = 0;j < field_values.length;j++)
                    {
                        subvalue = field_values[j];
                        if (field.getCharsTransf() == 0)
                            subvalue = net.toppro.components.mls.engine.CmaField.charTransf(subvalue,net.toppro.components.mls.engine.CmaField.CHAR_TRANSF_FIRST_CHAR_OF_WORD,m_engine);
                         
                        if (StringSupport.Trim(subvalue).length() != 0)
                            result.add(prefix + subvalue);
                         
                    }
                }
                else
                    result.add(prefix + net.toppro.components.mls.engine.CmaField.charTransf(value_Renamed,cma_field.getCharsTransf(),m_engine)); 
            }
             
        }
        //m_engine.WriteLine("a3= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time13));
        //m_engine.WriteLine("44444= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time4));
        if (result.size() == 0)
            return null;
         
        //long time5 = (System.DateTime.Now.Ticks - 621355968000000000);
        result_values = new String[result.size()];
        for (int i = 0;i < result.size();i++)
            result_values[i] = (String)result.get(i);
        return result_values;
    }

    //m_engine.WriteLine("55555= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time5));
    private static String validateYearBuilt(String s, int type) throws Exception {
        s = MLSUtil.removeSpace(s);
        if (s.length() == 0)
            return s;
         
        String str = "1234567890.-+";
        boolean isDigit = false;
        boolean isNotValid = false;
        String tmp = "", tmp1 = "", tmp2 = "", tmp3 = "";
        long ltmp = 0;
        for (int i = 0;i < s.length();i++)
            if (str.indexOf((char)s.charAt(i)) == -1)
                isNotValid = true;
             
        if (!isNotValid)
        {
            try
            {
                if (s.indexOf("-") > 0 || s.indexOf("+") > 0)
                {
                    tmp1 = s.substring(0, (0) + ((4) - (0)));
                    if (String.valueOf(Integer.valueOf(tmp1)).length() == 4 && (long)Integer.valueOf(tmp1) > 0 && tmp1.indexOf(".") == -1)
                    {
                        tmp2 = s.substring(4);
                        tmp3 = s.substring(5);
                        String str1 = "-+";
                        for (int i = 0;i < tmp2.length();i++)
                            if (str1.indexOf((char)tmp2.charAt(i)) == -1)
                                isDigit = true;
                             
                        if (isDigit)
                        {
                            if ((tmp3.length() == 2 || tmp3.length() == 4) && tmp3.indexOf("+") == -1 && tmp3.indexOf("-") == -1)
                                isNotValid = false;
                            else
                                isNotValid = true; 
                        }
                         
                    }
                    else
                        isNotValid = true; 
                }
                else
                {
                    //UPGRADE_TODO: Method 'java.lang.Math.round' was converted to 'System.Math.Round' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangMathround_double'"
                    ltmp = (long)Math.round(Double.valueOf(s));
                    tmp = String.valueOf(ltmp);
                    //System.Globalization.Calendar calendar = new System.Globalization.GregorianCalendar();
                    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
                    int iYear = Calendar.getInstance().getTime().Year;
                    //int iYear = SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.YEAR);
                    if (tmp.length() > 0 && tmp.length() < 4)
                        s = Integer.toString(iYear - ltmp);
                    else if (tmp.length() == 4)
                        s = tmp;
                    else
                        isNotValid = true;  
                } 
            }
            catch (Exception e)
            {
                isNotValid = true;
            }
        
        }
         
        if (type == 0)
        {
            if (isNotValid)
                return null;
            else
                return s; 
        }
        else
        {
            if (isNotValid)
                return s;
            else
                return null; 
        } 
    }

    public static String validateSTDFYearBuilt(String sVal) throws Exception {
        sVal = MLSUtil.removeSpace(sVal);
        if (sVal.length() == 0)
            return sVal;
         
        //System.Globalization.Calendar calendar = new System.Globalization.GregorianCalendar();
        //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
        int iYear = Calendar.getInstance().getTime().Year;
        if (sVal.toUpperCase().equals("new".toUpperCase()))
            return Integer.toString(iYear);
         
        String str = "1234567890.-";
        boolean isDigit = false;
        boolean isNotValid = false;
        String tmp = "", tmp1 = "", tmp2 = "", tmp3 = "";
        long ltmp = 0;
        StringBuilder sb = new StringBuilder(sVal.length());
        for (int i = 0;i < sVal.length();i++)
            if (str.indexOf((char)sVal.charAt(i)) > -1)
                sb.append(sVal.charAt(i));
             
        sVal = sb.toString();
        int pos = sVal.indexOf("-");
        if (pos == 4 && (sVal.length() == 7 || sVal.length() == 9))
            sVal = sVal.substring(0, (0) + (pos));
        else if (pos > 0)
            sVal = StringSupport.Trim(sVal.replace('-', ' '));
          
        try
        {
            //UPGRADE_TODO: Method 'java.lang.Math.round' was converted to 'System.Math.Round' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangMathround_double'"
            ltmp = (long)Math.round(Double.valueOf(sVal));
            tmp = String.valueOf(ltmp);
            if (tmp.length() > 0 && tmp.length() < 4)
                sVal = (iYear - ltmp) + "";
            else if (tmp.length() == 4)
                sVal = tmp;
              
            if (sVal.length() == 4 && (Integer.valueOf(sVal) < 1492 || Integer.valueOf(sVal) > (iYear + 10)))
                sVal = "";
             
            if (sVal.length() > 4)
                sVal = "";
             
        }
        catch (Exception e)
        {
            sVal = "";
        }

        return sVal;
    }

    private static String validateRoom(String str) throws Exception {
        str = StringSupport.Trim(str);
        if (str.length() > 0)
        {
            char[] array = str.toCharArray();
            StringBuilder buffer = new StringBuilder(array.length);
            int numberParsingState = 0;
            // 0 - before parsing number; 1 - before comma(.); 2 - afrer comma(.); 3 - parsing is finised
            boolean bxIsFirstLetter = false;
            for (int i = 0;i < array.length;i++)
            {
                switch(array[i])
                {
                    case '0': 
                    case '1': 
                    case '2': 
                    case '3': 
                    case '4': 
                    case '5': 
                    case '6': 
                    case '7': 
                    case '8': 
                    case '9': 
                        switch(numberParsingState)
                        {
                            case 0: 
                                buffer.append(array[i]);
                                numberParsingState = 1;
                                break;
                            case 1: 
                            case 2: 
                                buffer.append(array[i]);
                                break;
                            case 3: 
                                return null;
                        
                        }
                        break;
                    case '.': 
                        switch(numberParsingState)
                        {
                            case 1: 
                                buffer.append('.');
                                numberParsingState = 2;
                                break;
                            case 2: 
                                numberParsingState = 3;
                                break;
                        
                        }
                        break;
                    case 'x': 
                    case 'X': 
                        if (numberParsingState == 0 && bxIsFirstLetter)
                            return null;
                         
                        buffer.append('x');
                        numberParsingState = 0;
                        bxIsFirstLetter = true;
                        break;
                    default: 
                        switch(numberParsingState)
                        {
                            case 1: 
                            case 2: 
                                numberParsingState = 3;
                                break;
                        
                        }
                        break;
                
                }
            }
            int length;
            for (length = buffer.length();length > 0;length--)
                if (buffer[length - 1] != 'x')
                    break;
                 
            if (length > 0)
            {
                StringBuilderSupport.setLength(buffer, length);
                str = buffer.toString();
            }
            else
                str = null; 
        }
         
        return str;
    }

    private static String validateCustomRoom(String str) throws Exception {
        String description;
        String dimentions;
        int i = str.indexOf(',');
        if (i >= 0)
        {
            dimentions = validateRoom(str.substring(i + 1));
            description = str.Substring(0, (i)-(0)).Trim();
        }
        else
        {
            dimentions = validateRoom(str);
            description = "";
        } 
        if (description.length() == 0)
            description = STRINGS.get_Renamed(STRINGS.MLS_RECORD_DEFAULT_CUSTOM_ROOM_NAME);
         
        if (dimentions != null)
            str = description + "," + dimentions;
        else
            str = null; 
        return str;
    }

    public static String validateRooms(String sVal, String sDlm, String sCaption) throws Exception {
        char[] c = new char[]{ (char)(189), (char)(190), (char)(188) };
        //Special characters "½", "¾", "¼"
        boolean bFHBath = sCaption.indexOf(" / ") != -1 ? true : false;
        String[] source = new String[]{ Convert.ToString(c[0]), " 1/2", "-1/2", Convert.ToString(c[1]), " 3/4", "-3/4", Convert.ToString(c[2]), " 1/4", "-1/4" };
        String[] target = new String[]{ ".5", ".5", ".5", ".75", ".75", ".75", ".25", ".25", ".25" };
        String s = sVal;
        String str = new StringBuilder("1234567890.-/\\ ").toString();
        str += Convert.ToString(c[0]);
        str += Convert.ToString(c[1]);
        str += Convert.ToString(c[2]);
        for (int i = 0;i < s.length();i++)
            if (str.indexOf((char)s.charAt(i)) == -1)
                return null;
             
        int pos;
        boolean bMFamily = false;
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        pos = SupportClass.getIndex(sVal,"/",0);
        if (pos != -1)
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            pos = SupportClass.getIndex(sVal,"/",pos + 1);
            if (pos != -1)
                bMFamily = true;
             
        }
         
        if (bMFamily)
        {
            if (sVal.startsWith("/") && sVal.length() > 1)
                sVal = sVal.substring(1);
             
            if (sVal.endsWith("/") && sVal.length() > 1)
                sVal = sVal.substring(0, (0) + ((sVal.length() - 1) - (0)));
             
        }
         
        double nRooms = 0;
        boolean bOneUnit = false;
        SupportClass.Tokenizer strToken = null;
        if (sDlm.length() > 0)
            strToken = new SupportClass.Tokenizer(sVal,sDlm);
        else
            strToken = new SupportClass.Tokenizer(sVal,"/"); 
        if (strToken.getCount() < 3 && sDlm.length() == 0 && !bFHBath && !bMFamily)
        {
            bOneUnit = true;
            strToken = new SupportClass.Tokenizer(sVal,sVal);
        }
         
        while (strToken.hasMoreTokens() || bOneUnit)
        {
            double nRoom = 0;
            String tmpStr = "";
            if (!bOneUnit)
                tmpStr = new StringBuilder((String)strToken.nextToken()).toString();
            else
                tmpStr = sVal; 
            for (int i = 0;i < source.length;i++)
            {
                pos = tmpStr.indexOf(source[i]);
                if (pos > 0)
                {
                    if (Character.isDigit(tmpStr.charAt(pos - 1)))
                        tmpStr = MLSUtil.replaceSubStr(tmpStr,source[i],target[i]);
                     
                }
                 
            }
            tmpStr = StringSupport.Trim(MLSUtil.replaceSubStr(tmpStr,source[0],source[1]));
            tmpStr = StringSupport.Trim(MLSUtil.replaceSubStr(tmpStr,source[3],source[4]));
            tmpStr = StringSupport.Trim(MLSUtil.replaceSubStr(tmpStr,source[6],source[7]));
            if (tmpStr.equals("1/2"))
                tmpStr = "0.5";
             
            if ((Object)tmpStr == (Object)"1/4")
                tmpStr.equals("0.25");
             
            if (tmpStr.equals("3/4"))
                tmpStr = "0.75";
             
            try
            {
                nRoom = Double.valueOf(tmpStr);
            }
            catch (Exception e)
            {
                nRoom = 0;
            }

            nRooms = nRooms + nRoom;
            bOneUnit = false;
        }
        return (nRooms == 0) ? null : MLSUtil.replaceSubStr(Convert.ToString(nRooms), ".0", " ").Trim();
    }

    public static String validateSTDFTBath(String sVal) throws Exception {
        char[] c = new char[]{ (char)(189), (char)(190), (char)(188) };
        //Special characters "½", "¾", "¼"
        String[] source = new String[]{ String.valueOf(c[0]), " 1/2", "-1/2", String.valueOf(c[1]), " 3/4", "-3/4", String.valueOf(c[2]), " 1/4", "-1/4" };
        String[] target = new String[]{ ".5", ".5", ".5", ".75", ".75", ".75", ".25", ".25", ".25" };
        String s = sVal;
        String str = "1234567890.-/ ";
        str += String.valueOf(c[0]);
        str += String.valueOf(c[1]);
        str += String.valueOf(c[2]);
        int pos;
        char[] charValue = s.toCharArray();
        for (int i = 0;i < charValue.length;i++)
            if (str.indexOf(charValue[i]) == -1)
                return "";
             
        for (int i = 0;i < source.length;i++)
        {
            pos = sVal.indexOf(source[i]);
            if (pos > 0)
            {
                if (Character.isDigit(sVal.charAt(pos - 1)))
                    sVal = sVal.replace(source[i], target[i]);
                 
            }
             
        }
        boolean bMFamily = false;
        if (sVal.startsWith("/") && sVal.length() > 1)
            sVal = sVal.substring(1);
         
        if (sVal.endsWith("/") && sVal.length() > 1)
            sVal = sVal.substring(0, (0) + ((sVal.length() - 1) - (0)));
         
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        pos = SupportClass.getIndex(sVal,"/",0);
        if (pos != -1)
            bMFamily = true;
         
        double nRooms = 0;
        boolean bOneUnit = false;
        String[] strToken = StringSupport.Split(sVal, '/');
        for (int j = 0;j < strToken.length;j++)
        {
            double nRoom = 0;
            String tmpStr = strToken[j];
            if (tmpStr.length() == 0)
                continue;
             
            try
            {
                nRoom = Double.valueOf(tmpStr);
                //UPGRADE_TODO: Method 'java.lang.Math.round' was converted to 'System.Math.Round' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangMathround_double'"
                double roundValue = Math.round(nRoom - 0.499);
                double decimalValue = nRoom - roundValue;
                if (decimalValue == 0.25)
                    nRoom = roundValue + 0;
                else if (decimalValue == 0.5)
                    nRoom = roundValue + 1;
                else if (decimalValue == 0.75)
                    nRoom = roundValue + 1;
                else if (decimalValue > 0)
                    nRoom = roundValue + decimalValue * 10;
                    
            }
            catch (Exception e)
            {
                nRoom = 0;
            }

            nRooms += nRoom;
            bOneUnit = false;
        }
        return (nRooms == 0) ? "" : String.valueOf(nRooms);
    }

    public static String validateSQFT(String sVal, String sDlm) throws Exception {
        String str = "1234567890.-/\\ ";
        double nSqfts = 0;
        for (int i = 0;i < sVal.length();i++)
            if (str.indexOf((char)sVal.charAt(i)) == -1)
                return null;
             
        SupportClass.Tokenizer strToken = null;
        if (sDlm.length() > 0)
            strToken = new SupportClass.Tokenizer(sVal,sDlm);
        else
            strToken = new SupportClass.Tokenizer(sVal,"/"); 
        while (strToken.hasMoreTokens())
        {
            double nSqft = 0;
            String tmpStr = "";
            tmpStr = new StringBuilder((String)strToken.nextToken()).toString();
            try
            {
                nSqft = Double.valueOf(tmpStr);
            }
            catch (Exception e)
            {
                nSqft = 0;
            }

            nSqfts = nSqfts + nSqft;
        }
        return (nSqfts == 0) ? null : MLSUtil.replaceSubStr(Convert.ToString(nSqfts), ".0", " ").Trim();
    }

    public static String validateSTDFSQFT(String sVal) throws Exception {
        String str = "1234567890.-/ ";
        double nSqfts = 0;
        StringBuilder sb = new StringBuilder(sVal.length());
        for (int i = 0;i < sVal.length();i++)
            if (str.indexOf((char)sVal.charAt(i)) > -1)
                sb.append(sVal.charAt(i));
             
        sVal = sb.toString();
        if (sVal.length() == 0)
            return "";
         
        String[] strToken = StringSupport.Split(sVal, '/');
        for (int j = 0;j < strToken.length;j++)
        {
            double nSqft = 0;
            String tmpStr = "";
            tmpStr = strToken[j];
            boolean skip = false;
            try
            {
                int pos = sVal.indexOf("-");
                if (pos > -1)
                {
                    nSqft = (Double.valueOf(StringSupport.Trim(tmpStr.substring(0, (0) + (pos)))) + Double.valueOf(StringSupport.Trim(tmpStr.substring(pos + 1)))) / 2;
                    skip = true;
                }
                 
            }
            catch (Exception e)
            {
                tmpStr = StringSupport.Trim(tmpStr.replace('-', ' '));
            }

            try
            {
                if (!skip)
                    nSqft = Double.valueOf(tmpStr);
                 
            }
            catch (Exception e)
            {
                nSqft = 0;
            }

            nSqfts = nSqfts + nSqft;
        }
        //UPGRADE_TODO: Method 'java.lang.Math.round' was converted to 'System.Math.Round' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangMathround_double'"
        nSqfts = (long)Math.round(nSqfts);
        return (nSqfts == 0) ? "" : String.valueOf(nSqfts);
    }

    private String validateSTDFLotSize(String value, String unitOfArea) throws Exception {
        if (unitOfArea.length() == 0)
            return "";
         
        value = StringSupport.Trim(value);
        if (value.length() == 0)
            return "";
         
        String result = "";
        double size = 0;
        boolean isFound = false;
        try
        {
            String[] values = m_regexSplit.Split(value);
            for (int i = 0;i < values.length;i++)
            {
                if (m_regexIsNumber.matcher(values[i]).matches())
                {
                    size = Double.valueOf(values[i]);
                    isFound = true;
                    break;
                }
                 
            }
        }
        catch (Exception exc)
        {
            return "";
        }

        if (!isFound)
            return "";
         
        String __dummyScrutVar11 = unitOfArea;
        if (__dummyScrutVar11.equals("A"))
        {
        }
        else if (__dummyScrutVar11.equals("SF"))
        {
            size /= 43560;
        }
        else if (__dummyScrutVar11.equals("SM"))
        {
            size /= 4046.85642;
        }
        else
        {
            return "";
        }   
        result = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0.#########}"),size);
        return result;
    }

    public String validateSTDFStories(String value) throws Exception {
        value = StringSupport.Trim(value).toLowerCase();
        if (value.length() == 0)
            return "";
         
        double stories = 0;
        boolean bFound = false;
        try
        {
            if (m_regexIsNumber.matcher(value).matches())
                stories = Double.valueOf(value);
            else
            {
                String[] values = m_regexSplit.Split(value);
                for (int i = 0;i < values.length;i++)
                {
                    String __dummyScrutVar12 = values[i];
                    if (__dummyScrutVar12.equals("one"))
                    {
                        stories = 1;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("two"))
                    {
                        stories = 2;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("three"))
                    {
                        stories = 3;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("four"))
                    {
                        stories = 4;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("five"))
                    {
                        stories = 5;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("six"))
                    {
                        stories = 6;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("seven"))
                    {
                        stories = 7;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("eight"))
                    {
                        stories = 8;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("nine"))
                    {
                        stories = 9;
                        bFound = true;
                    }
                    else if (__dummyScrutVar12.equals("ten"))
                    {
                        stories = 10;
                        bFound = true;
                    }
                    else
                    {
                        if (values[i].startsWith("tri"))
                        {
                            stories = 3;
                            bFound = true;
                        }
                        else if (m_regexIsNumber.matcher(values[i]).matches())
                        {
                            stories = Double.valueOf(values[i]);
                            bFound = true;
                        }
                          
                    }          
                    if (bFound)
                        break;
                     
                }
                if (!bFound)
                    return "";
                 
            } 
        }
        catch (Exception exc)
        {
            return "";
        }

        return String.format(StringSupport.CSFmtStrToJFmtStr("{0:0.0}"),stories);
            ;
    }

    public String validateSTDFPropertySubtype(String value, String stdptype, boolean isMapped) throws Exception {
        String valuenorm = "";
        try
        {
            //Automatic values based on standard PType
            if (!isMapped)
            {
                if (stdptype.contains("Condo"))
                {
                    valuenorm = "condo";
                }
                 
                if (stdptype.contains("Town-Home"))
                {
                    valuenorm = "townhouse";
                }
                 
                if (stdptype.contains("Co-Op"))
                {
                    valuenorm = "co-op";
                }
                 
                return valuenorm;
            }
             
            //Override with manual mapping
            value = StringSupport.Trim(value).toLowerCase();
            if (value.length() == 0)
                return "";
             
            if ((stdptype.contains("Condo")) || (stdptype.contains("Town-Home")) || (stdptype.contains("Co-Op")))
            {
                String __dummyScrutVar13 = value;
                if (__dummyScrutVar13.equals("condo") || __dummyScrutVar13.equals("co-op") || __dummyScrutVar13.equals("cond-op") || __dummyScrutVar13.equals("townhouse"))
                {
                    valuenorm = value;
                }
                else
                {
                    valuenorm = "";
                } 
            }
            else
            {
                valuenorm = "";
            } 
        }
        catch (Exception exc)
        {
            return "";
        }

        return valuenorm;
    }

    public String validateSTDFTax(String value) throws Exception {
        value = StringSupport.Trim(value);
        if (value.length() == 0)
            return "";
         
        String result = "";
        double amount = 0;
        boolean isFound = false;
        try
        {
            String[] values = m_regexSplit.Split(value);
            for (int i = 0;i < values.length;i++)
            {
                if (m_regexIsNumber.matcher(values[i]).matches())
                {
                    amount = Double.valueOf(values[i]);
                    isFound = true;
                    break;
                }
                 
            }
        }
        catch (Exception exc)
        {
            return "";
        }

        if (!isFound)
            return "";
         
        result = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0}"),amount);
        return result;
    }

    private String getState() throws Exception {
        String value_Renamed = "";
        try
        {
            value_Renamed = m_fields.getEngine().getEnvironment().getStateProvince();
        }
        catch (Exception exc)
        {
            System.out.println("Can't get MLS board's state");
        }

        return value_Renamed;
    }

    public int compareRecords(MLSRecord NextRec, int[] ComparFields) throws Exception {
        return 1;
    }

}


