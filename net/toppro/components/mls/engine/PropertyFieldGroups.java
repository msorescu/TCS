//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.PropertyFieldGroup;
import net.toppro.components.mls.engine.PropertyFieldGroups;

public class PropertyFieldGroups  extends Object 
{
    private int m_DefaultGroupIndex = -1;
    private int m_CurrentGroupIndex = -1;
    private DefParser m_DefParser = null;
    private ArrayList m_PropertyFieldGroups = ArrayList.Synchronized(new ArrayList(10));
    private static final String GROUP_NAME_EMPTY = "Empty";
    private static final String DEFAULT_CAPTION = "Full";
    private static final String ADVANCED_CAPTION = "Advanced search";
    private static final String MLSNUMBER_CAPTION = "MLS Number";
    public static final String GROUP_NAME_MLSNUMBER = "MLSNumber";
    public static final String GROUP_NAME_ADVANCED = "Advanced";
    private static final String STANDARD_DATETIMEFORMAT = "MM/dd/yyyyTHH:mm:ss";
    //"MM/DD/YYYY";
    private static final String STANDARD_DATETIMEFORMAT_MILLISECONDS = "MM/dd/yyyyTHH:mm:ss.fff";
    //"MM/DD/YYYY";
    private static final String STANDARD_DATEFORMAT = "MM/dd/yyyy";
    /**
    * @return  array of groups
    */
    public PropertyFieldGroup[] toArray() throws Exception {
        PropertyFieldGroup[] pgfs = new PropertyFieldGroup[m_PropertyFieldGroups.size()];
        for (int i = 0;i < m_PropertyFieldGroups.size();i++)
        {
            PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
            pgfs[i] = pgf;
        }
        return pgfs;
    }

    /**
    * @return  index of group by group name
    * 
    *  @param name - name of group
    */
    public int getGroupIndex(String name) throws Exception {
        for (int i = 0;i < m_PropertyFieldGroups.size();i++)
        {
            PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
            if (pgf.getName().toUpperCase().equals(name.toUpperCase()))
                return i;
             
        }
        return -1;
    }

    /**
    * @return  object PropertyFieldGroup by index
    * 
    *  @param index - index of group in array
    */
    public PropertyFieldGroup getGroup(int index) throws Exception {
        if (index < 0 || index >= m_PropertyFieldGroups.size())
            return null;
         
        return (PropertyFieldGroup)m_PropertyFieldGroups.get(index);
    }

    /**
    * @return  count of groups
    */
    public int size() throws Exception {
        return m_PropertyFieldGroups.size();
    }

    /**
    * @return  created empty group
    */
    public PropertyFieldGroup createEmptyGroup() throws Exception {
        PropertyFieldGroup pfg = new PropertyFieldGroup();
        pfg.setName(GROUP_NAME_EMPTY);
        m_PropertyFieldGroups.add(pfg);
        return (PropertyFieldGroup)m_PropertyFieldGroups.get(m_PropertyFieldGroups.size() - 1);
    }

    /**
    * Initialize all groups for current DEF-file
    */
    public void init(net.toppro.components.mls.engine.MLSEngine _engine) throws Exception {
        DefParser m_DefParser = _engine.getDefParser();
        if (m_DefParser == null)
            return ;
         
        String[] Groups = m_DefParser.getAttributiesFor(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDGROUPS);
        if (Groups == null)
        {
            createMLSNumberGroup(_engine);
            createAdvancedGroup(_engine);
            m_DefaultGroupIndex = 1;
            m_CurrentGroupIndex = 0;
            return ;
        }
         
        String Caption;
        int i, j, index;
        boolean bIncludeFields = false;
        boolean bFound = false;
        for (i = 0;i < Groups.length;i++)
        {
            Caption = m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDGROUPS,Groups[i]);
            if (!Groups[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DEFAULT.toUpperCase()))
            {
                PropertyFieldGroup pfg = new PropertyFieldGroup();
                pfg.init(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS_ + Groups[i],Caption,_engine);
                pfg.setName(Groups[i]);
                m_PropertyFieldGroups.add(pfg);
            }
            else
            {
                bFound = false;
                index = -1;
                for (j = 0;j < m_PropertyFieldGroups.size();j++)
                    if (((PropertyFieldGroup)m_PropertyFieldGroups.get(j)).getName().toUpperCase().equals(Caption.toUpperCase()))
                    {
                        bFound = true;
                        index = j;
                    }
                     
                if (!bFound)
                {
                    PropertyFieldGroup pfg = new PropertyFieldGroup();
                    if (!Caption.toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS.toUpperCase()))
                    {
                        pfg.init(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS_ + Caption,Caption,_engine);
                        pfg.setName(Caption);
                    }
                    else
                    {
                        pfg.init(Caption,Caption,_engine);
                        pfg.setName(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS);
                    } 
                    pfg.setDefault(true);
                    m_PropertyFieldGroups.add(pfg);
                    m_DefaultGroupIndex = m_PropertyFieldGroups.size() - 1;
                    m_CurrentGroupIndex = m_PropertyFieldGroups.size() - 1;
                }
                else
                {
                    ((PropertyFieldGroup)m_PropertyFieldGroups.get(index)).setDefault(true);
                    m_DefaultGroupIndex = index;
                    m_CurrentGroupIndex = index;
                } 
            } 
        }
        if (m_DefaultGroupIndex == -1)
        {
            bFound = false;
            for (j = 0;j < m_PropertyFieldGroups.size();j++)
                if (((PropertyFieldGroup)m_PropertyFieldGroups.get(j)).getName().toUpperCase().equals(GROUP_NAME_ADVANCED.toUpperCase()))
                {
                    bFound = true;
                    m_DefaultGroupIndex = j;
                    m_CurrentGroupIndex = j;
                    ((PropertyFieldGroup)m_PropertyFieldGroups.get(j)).setDefault(true);
                    break;
                }
                 
            if (!bFound && m_PropertyFieldGroups.size() > 0)
            {
                ((PropertyFieldGroup)m_PropertyFieldGroups.get(0)).setDefault(true);
                m_DefaultGroupIndex = 0;
                m_CurrentGroupIndex = 0;
            }
             
        }
         
    }

    /**
    * @return  index of default group
    */
    public int getDefaulGroupIndex() throws Exception {
        return m_DefaultGroupIndex;
    }

    /**
    * Set group as default
    *  @param int index - index of group in array
    */
    public void setDefaulGroupIndex(int index) throws Exception {
        m_DefaultGroupIndex = index;
    }

    /**
    * @return  index of current group, group which is active for this moment.
    */
    public int getCurrentGroupIndex() throws Exception {
        return m_CurrentGroupIndex;
    }

    /**
    * Set current group
    *  @param int index - group's index in array
    */
    public void setCurrentGroup(int index) throws Exception {
        m_CurrentGroupIndex = index;
    }

    /**
    * @return  object PropertyFieldGroup which is current for this moment
    */
    public PropertyFieldGroup getCurrectGroup() throws Exception {
        return getGroup(m_CurrentGroupIndex);
    }

    /**
    * Creates default Advanced Group
    */
    public void createAdvancedGroup(net.toppro.components.mls.engine.MLSEngine _engine) throws Exception {
        PropertyFieldGroup pfg = new PropertyFieldGroup();
        pfg.init(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,ADVANCED_CAPTION,_engine);
        net.toppro.components.mls.engine.PropertyField[] all_fields = pfg.toArray();
        for (int i = 0;i < all_fields.length;i++)
        {
            net.toppro.components.mls.engine.PropertyField f = all_fields[i];
            if (f.isMLSNumField())
                pfg.removeField(f.getName());
             
        }
        pfg.setName(PropertyFieldGroups.GROUP_NAME_ADVANCED);
        pfg.setDefault(true);
        if (pfg.size() > 0)
            m_PropertyFieldGroups.add(pfg);
         
    }

    /**
    * Creates default MLSNumber Group
    */
    public void createMLSNumberGroup(net.toppro.components.mls.engine.MLSEngine _engine) throws Exception {
        PropertyFieldGroup pfg = new PropertyFieldGroup();
        pfg.init(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,MLSNUMBER_CAPTION,_engine);
        net.toppro.components.mls.engine.PropertyField[] all_fields = pfg.toArray();
        for (int i = 0;i < all_fields.length;i++)
        {
            net.toppro.components.mls.engine.PropertyField f = all_fields[i];
            if (!f.isRequiredForMlsNum() && !f.isMLSNumField())
                pfg.removeField(f.getName());
             
        }
        pfg.setName(GROUP_NAME_MLSNUMBER);
        if (pfg.size() > 0)
            m_PropertyFieldGroups.add(pfg);
         
    }

    /**
    * 
    */
    public void load(Hashtable t, net.toppro.components.mls.engine.MLSEngine _engine) throws Exception {
        int i, j = 0, k;
        boolean bFound = false;
        IEnumerator _names;
        String hashFieldName = "";
        String FieldName, FieldName2;
        for (i = 0;i < m_PropertyFieldGroups.size();i++)
        {
            PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
            pgf.clearValues();
            net.toppro.components.mls.engine.PropertyField[] _PropertyFields = pgf.toArray();
            _names = EnumeratorSupport.mk(t.keySet().iterator());
            while (_names.moveNext())
            {
                //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
                //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
                hashFieldName = ((String)_names.getCurrent());
                bFound = false;
                for (j = 0;j < _PropertyFields.length;j++)
                {
                    //iLYA[15 Feb, 2005]: I put this block in comments for future purposes.
                    FieldName = _PropertyFields[j].getName();
                    if (StringSupport.Compare(hashFieldName, FieldName, true) == 0)
                    {
                        bFound = true;
                        break;
                    }
                     
                }
                if (!bFound)
                {
                    FieldName2 = _engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,hashFieldName);
                    bFound = false;
                    if (FieldName2 != null && FieldName2.length() != 0)
                        for (j = 0;j < _PropertyFields.length;j++)
                        {
                            FieldName = _PropertyFields[j].getName();
                            if (StringSupport.Compare(FieldName2, FieldName, true) == 0)
                            {
                                bFound = true;
                                break;
                            }
                             
                        }
                     
                }
                 
                if (!bFound)
                    continue;
                 
                try
                {
                    String hashFieldValue = (String)t.get(hashFieldName);
                    boolean isDate = false;
                    if (_PropertyFields[j].getControlType() == 9)
                        isDate = true;
                     
                    if (hashFieldName.startsWith("ST_") && StringSupport.Compare(hashFieldName, "ST_Status", true) != 0 && StringSupport.Compare(hashFieldName, "ST_Ptype", true) != 0)
                        hashFieldValue = _PropertyFields[j].getLookupCode(hashFieldValue);
                     
                    if (isDate || (hashFieldName.startsWith("ST_") && (hashFieldName.toUpperCase().contains("DATE") || hashFieldName.toUpperCase().contains("LASTMOD") || hashFieldName.toUpperCase().contains("PICMOD"))))
                        hashFieldValue = getDateFormat(_PropertyFields[j],hashFieldValue,_engine);
                     
                    _PropertyFields[j].setValue(hashFieldValue);
                }
                catch (Exception e1)
                {
                    SupportClass.WriteStackTrace(e1, Console.Error);
                }
            
            }
        }
    }

    //while( _names.hasMoreElements() )
    //for( i = 0;
    private String getDateFormat(net.toppro.components.mls.engine.PropertyField pf, String value_Renamed, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        String date = "";
        boolean isDateTime = false;
        try
        {
            String dateFormat = "";
            String inDateFormat = STANDARD_DATEFORMAT;
            if (value_Renamed.indexOf('T') > -1)
            {
                inDateFormat = STANDARD_DATETIMEFORMAT;
                isDateTime = true;
            }
             
            String dateDelimeter = "";
            if (pf != null)
            {
                dateFormat = pf.getDateFormat();
                dateDelimeter = pf.getDelimiter();
            }
             
            if (dateFormat == null || dateFormat.length() == 0)
            {
                dateFormat = STANDARD_DATEFORMAT;
            }
             
            if (dateFormat.IndexOf("f", StringComparison.InvariantCultureIgnoreCase) > -1)
            {
                //fractionFormat = dateFormat.Substring(dateFormat.IndexOf("f", StringComparison.InvariantCultureIgnoreCase) - 1);
                inDateFormat = STANDARD_DATETIMEFORMAT_MILLISECONDS;
            }
             
            // inDateFormat + fractionFormat;
            if (dateDelimeter == null || dateDelimeter.length() == 0)
                dateDelimeter = "-";
             
            String[] dates = Tcs.Mls.Util.stringSplit(value_Renamed,"-");
            for (int i = 0;i < dates.length;i++)
            {
                if (i == 0)
                {
                    if (!isDateTime)
                    {
                        date = MLSUtil.formatDate(inDateFormat,dateFormat,dates[i]);
                    }
                    else
                    {
                        if (inDateFormat.length() > dates[i].length())
                            date = MLSUtil.FormatDate(inDateFormat, dateFormat, dates[i] + ".000", mlsEngine.getGMTTimeZone(), mlsEngine.getMLSSearchTimeZone());
                        else
                            date = MLSUtil.FormatDate(inDateFormat, dateFormat, dates[i], mlsEngine.getGMTTimeZone(), mlsEngine.getMLSSearchTimeZone()); 
                    } 
                }
                else if (!isDateTime)
                {
                    date = date + dateDelimeter + MLSUtil.formatDate(inDateFormat,dateFormat,dates[i]);
                }
                else
                {
                    if (inDateFormat.length() > dates[i].length())
                        date = date + dateDelimeter + MLSUtil.FormatDate(inDateFormat, dateFormat, dates[i] + ".999", mlsEngine.getGMTTimeZone(), mlsEngine.getMLSSearchTimeZone());
                    else
                        date = date + dateDelimeter + MLSUtil.FormatDate(inDateFormat, dateFormat, dates[i], mlsEngine.getGMTTimeZone(), mlsEngine.getMLSSearchTimeZone()); 
                }  
            }
            if (date == null)
                return value_Renamed;
             
        }
        catch (Exception exc)
        {
        }

        return date;
    }

    /**
    * 
    */
    public void load(String fileName, net.toppro.components.mls.engine.MLSEngine _engine) throws Exception {
        int i, j;
        if (fileName == null)
        {
            for (i = 0;i < m_PropertyFieldGroups.size();i++)
            {
                PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
                pgf.clearValues();
            }
            return ;
        }
         
        _engine.setImportPictureCheckBox(false);
        File file = new File(fileName);
        boolean tmpBool;
        if (File.Exists(file.FullName))
            tmpBool = true;
        else
            tmpBool = File.Exists(file.FullName); 
        if (!tmpBool)
        {
            for (i = 0;i < m_PropertyFieldGroups.size();i++)
            {
                PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
                pgf.clearValues();
            }
            return ;
        }
         
        FileStreamSupport f = null;
        BufferedReader r = null;
        BufferedReader s = null;
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            f = new FileStreamSupport(fileName, FileMode.Open, FileAccess.Read);
            r = new BufferedReader(f, Encoding.Default);
            //UPGRADE_TODO: The differences in the expected value  of parameters for constructor 'java.io.BufferedReader.BufferedReader'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
            s = new BufferedReader(r.BaseStream, r.CurrentEncoding);
            for (i = 0;i < m_PropertyFieldGroups.size();i++)
            {
                PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
                net.toppro.components.mls.engine.PropertyField[] _PropertyFields = pgf.toArray();
                for (j = 0;j < _PropertyFields.length;j++)
                {
                    String value_Renamed = s.readLine();
                    if (value_Renamed != null && value_Renamed.indexOf(net.toppro.components.mls.engine.MLSEngine.IMPORT_PICTURE_CHECKBOX) >= 0)
                        break;
                     
                    if (value_Renamed != null)
                        _PropertyFields[j].setValue(value_Renamed);
                    else
                        _PropertyFields[j].setValue(""); 
                }
            }
            String inputLine = "";
            String cb = "0";
            int index = -1;
            while ((inputLine = s.readLine()) != null)
            {
                index = inputLine.indexOf(net.toppro.components.mls.engine.MLSEngine.IMPORT_PICTURE_CHECKBOX);
                if (index >= 0)
                {
                    cb = inputLine.substring(index + net.toppro.components.mls.engine.MLSEngine.IMPORT_PICTURE_CHECKBOX.length());
                    _engine.setImportPictureCheckBox(cb.toUpperCase().equals("1".toUpperCase()) ? true : false);
                    break;
                }
                 
            }
        }
        catch (Exception e)
        {
            System.out.println("Warning: unable to load property fields");
            SupportClass.WriteStackTrace(e, Console.Error);
            for (i = 0;i < m_PropertyFieldGroups.size();i++)
            {
                PropertyFieldGroup pgf = (PropertyFieldGroup)m_PropertyFieldGroups.get(i);
                pgf.clearValues();
            }
        }
        finally
        {
            if (s != null)
            {
                try
                {
                    s.close();
                }
                catch (Exception e)
                {
                }
            
            }
             
            if (r != null)
            {
                try
                {
                    r.close();
                }
                catch (Exception e)
                {
                }
            
            }
             
            if (f != null)
            {
                try
                {
                    f.close();
                }
                catch (Exception e)
                {
                }
            
            }
             
        }
    }

}


//internal virtual void  save(System.String fileName, MLSEngine _engine)
//{
//    if (fileName == null)
//        return ;
//    System.IO.FileStream f = null;
//    System.IO.StreamWriter s = null;
//    int j, i;
//    try
//    {
//        //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
//        f = new System.IO.FileStream(fileName, System.IO.FileMode.Create);
//        s = new System.IO.StreamWriter(f, System.Text.Encoding.Default);
//        System.String value_Renamed = "";
//        for (i = 0; i < m_PropertyFieldGroups.Count; i++)
//        {
//            PropertyFieldGroup pgf = (PropertyFieldGroup) m_PropertyFieldGroups[i];
//            PropertyField[] _PropertyFields = pgf.toArray();
//            for (j = 0; j < _PropertyFields.Length; j++)
//            {
//                value_Renamed = _PropertyFields[j].getValue();
//                s.Write(value_Renamed + "\r\n");
//            }
//        }
//        s.Write("\r\n" + "\r\n" + net.toppro.components.mls.engine.MLSEngine.IMPORT_PICTURE_CHECKBOX + (_engine.IsImportPictureChecked()?"1":"0"));
//    }
//    catch (System.Exception e)
//    {
//        System.Console.Out.WriteLine("Warning: unable to save property fields");
//        SupportClass.WriteStackTrace(e, Console.Error);
//    }
//    finally
//    {
//        if (s != null)
//        {
//            try
//            {
//                //UPGRADE_NOTE: Exceptions thrown by the equivalent in .NET of method 'java.io.PrintWriter.close' may be different. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1099'"
//                s.Close();
//            }
//            catch (System.Exception e2)
//            {
//                ;
//            }
//        }
//        if (f != null)
//        {
//            try
//            {
//                f.Close();
//            }
//            catch (System.Exception e3)
//            {
//                ;
//            }
//        }
//    }
//}