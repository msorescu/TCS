//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.StringSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import net.toppro.components.mls.engine.StringTokenizerEx;

/**
* class DefParser a like Xml Parser, but DefParser works with DEF-files.
* Builds tree of Section and attributes
*/
public class DefParser  extends Object 
{
    private static final boolean DEBUG = false;
    public Hashtable m_hTree = Hashtable.Synchronized(new Hashtable());
    public ArrayList m_TreeCopy = null;
    private String m_MainScriptContent = null;
    private String m_FieldsContent = null;
    private String m_SecFieldsContent = null;
    private String m_PicScriptContent = null;
    private String m_MPicScriptContent = null;
    private String m_MlsRecordsContent = null;
    private String m_MlsRecordsExContent = null;
    private String m_RemScriptContent = null;
    private String m_ResultScriptContent = null;
    private ArrayList m_categorizationGroups = new ArrayList();
    private Hashtable m_hExVarScriptContent = new Hashtable();
    public Object[] getCategorizationGroups() throws Exception {
        return m_categorizationGroups.ToArray();
    }

    /**
    * 
    */
    public DefParser() throws Exception {
        m_MainScriptContent = new StringBuilder().toString();
        m_FieldsContent = new StringBuilder().toString();
        m_SecFieldsContent = new StringBuilder().toString();
        m_PicScriptContent = new StringBuilder().toString();
        m_MPicScriptContent = new StringBuilder().toString();
        m_RemScriptContent = new StringBuilder().toString();
        m_MlsRecordsExContent = new StringBuilder().toString();
        m_MlsRecordsContent = new StringBuilder().toString();
        m_TreeCopy = ArrayList.Synchronized(new ArrayList(10));
    }

    /**
    * 
    */
    public void setMlsRecordsExContent(String content) throws Exception {
        m_MlsRecordsExContent = content;
    }

    public String getMlsRecordsExContent() throws Exception {
        return m_MlsRecordsExContent;
    }

    public void setMlsRecordsContent(String content) throws Exception {
        m_MlsRecordsContent = content;
    }

    public String getMlsRecordsContent() throws Exception {
        return m_MlsRecordsContent;
    }

    public void setRemScriptContent(String content) throws Exception {
        m_RemScriptContent = content;
    }

    public void setResultScriptContent(String content) throws Exception {
        m_ResultScriptContent = content;
    }

    /**
    * 
    */
    public String getRemScriptContent() throws Exception {
        return m_RemScriptContent;
    }

    /**
    * @param sectionExtraVarScript 
    *  @return
    */
    public String getExVarScriptContent(String sectionExtraVarScript) throws Exception {
        return (m_hExVarScriptContent.containsKey(sectionExtraVarScript)) ? m_hExVarScriptContent.get(sectionExtraVarScript).toString() : null;
    }

    /**
    * @param sectionExtraVarScript 
    *  @param content
    */
    public void setExVarScriptContent(String sectionExtraVarScript, String content) throws Exception {
        if (!m_hExVarScriptContent.containsKey(sectionExtraVarScript))
            m_hExVarScriptContent.put(sectionExtraVarScript, content);
         
    }

    public String getResultScriptContent() throws Exception {
        return m_ResultScriptContent;
    }

    /**
    * 
    */
    public void setPicScriptContent(String content) throws Exception {
        m_PicScriptContent = content;
    }

    /**
    * 
    */
    public String getPicScriptContent() throws Exception {
        return m_PicScriptContent;
    }

    /**
    * 
    */
    public void setMPicScriptContent(String content) throws Exception {
        m_MPicScriptContent = content;
    }

    /**
    * 
    */
    public String getMPicScriptContent() throws Exception {
        return m_MPicScriptContent;
    }

    /**
    * 
    */
    public void setMainScriptContent(String content) throws Exception {
        m_MainScriptContent = content;
    }

    /**
    * 
    */
    public String getMainScriptContent() throws Exception {
        return m_MainScriptContent;
    }

    /**
    * 
    */
    public void setFieldsContent(String content) throws Exception {
        m_FieldsContent = content;
    }

    /**
    * 
    */
    public String getFieldsContent() throws Exception {
        return m_FieldsContent;
    }

    /**
    * 
    */
    public void setSecFieldsContent(String content) throws Exception {
        m_SecFieldsContent = content;
    }

    /**
    * 
    */
    public String getSecFieldsContent() throws Exception {
        return m_SecFieldsContent;
    }

    /**
    * /////////////////////////////////////////////////////////////////////////////////////
    */
    public void parseBuffer(String buffer) throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        StringTokenizerEx strToken = new StringTokenizerEx(buffer,"\n");
        String line = "";
        int position1 = 0;
        int position2 = 0;
        String SectionName = "";
        String Attribute = "";
        String Value = "";
        while (strToken.hasMoreElements())
        {
            line = StringSupport.Trim(((String)strToken.nextElement()));
            if (line.length() == 0)
                SectionName = "";
             
            if (line.startsWith(";"))
                line = "";
            else if ((position1 = line.indexOf(";")) > 0)
            {
                line = line.Substring(0, (position1)-(0)).Trim();
            }
            else if (position1 == 0)
                line = "";
               
            if (line.startsWith("[") && (position2 = line.indexOf("]")) > 0)
            {
                SectionName = line.Substring(1, (position2)-(1)).Trim();
                if (SectionName.StartsWith("ResultFieldGroup_", StringComparison.CurrentCultureIgnoreCase))
                {
                    m_categorizationGroups.add(SectionName.substring(SectionName.indexOf("_") + 1));
                }
                 
                SectionName = SectionName.toUpperCase();
            }
            else
            {
                if (SectionName.length() != 0)
                {
                    if ((position1 = line.indexOf("=")) > 0)
                    {
                        Attribute = line.Substring(0, (position1)-(0)).Trim().ToUpper();
                        Value = line.Substring(position1 + 1, (line.Length)-(position1 + 1)).Trim();
                        if (Value.startsWith("\"") && Value.endsWith("\"") && Value.length() > 1)
                            Value = Value.substring(1, (1) + ((Value.length() - 1) - (1)));
                         
                        m_hTree.put(SectionName + "." + Attribute, Value);
                        m_TreeCopy.add(SectionName + "." + Attribute);
                    }
                    else
                    {
                        Attribute = StringSupport.Trim(line).toUpperCase();
                        if (Attribute.length() != 0)
                        {
                            m_hTree.put(SectionName + "." + Attribute, "");
                            m_TreeCopy.add(SectionName + "." + Attribute);
                        }
                         
                    } 
                }
                 
            } 
        }
    }

    //if (DEBUG)
    //    System.Console.Out.WriteLine("Clear time for parseBuffer is = " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
    //end parseBuffer
    public String getValue(String sectionAndField) throws Exception {
        String value_Renamed = (String)m_hTree.get(sectionAndField.toUpperCase());
        if (value_Renamed == null)
            return "";
         
        return value_Renamed;
    }

    public String getValue(String sectionName, String paramName) throws Exception {
        return getValue(sectionName + "." + paramName);
    }

    public String getMappedValue(String mappingSection, String mappedSectionName, String paramName, String realSectionPrefix) throws Exception {
        String RealSection = getValue(mappingSection,mappedSectionName);
        if (realSectionPrefix != null && realSectionPrefix.length() != 0)
            RealSection = realSectionPrefix + RealSection;
         
        if (RealSection != null && RealSection.length() != 0)
            return getValue(RealSection,paramName);
         
        return "";
    }

    public String[] getAttributiesFor(String sectionName) throws Exception {
        ArrayList v = ArrayList.Synchronized(new ArrayList(10));
        sectionName = sectionName.toUpperCase() + ".";
        String keyName = "";
        boolean bFound = false;
        for (int i = 0;i < m_TreeCopy.size();i++)
        {
            keyName = ((String)m_TreeCopy.get(i));
            if (keyName.startsWith(sectionName))
            {
                bFound = true;
                v.add(keyName.Substring(sectionName.length(), (keyName.Length)-(sectionName.length())));
            }
            else if (bFound)
                break;
              
        }
        String[] array = null;
        if (v.size() > 0)
        {
            array = new String[v.size()];
            for (int i = 0;i < v.size();i++)
                array[i] = ((String)v.get(i));
        }
         
        return array;
    }

    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
    public void save(System.IO.BinaryWriter out_Renamed) throws Exception {
        String key = "";
        ArrayList array = ArrayList.Synchronized(new ArrayList(10));
        IEnumerator enumA = EnumeratorSupport.mk(m_hTree.keySet().iterator());
        while (enumA.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            key = ((String)enumA.getCurrent());
            array.add(key);
            array.add((String)m_hTree.get(key));
        }
        //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
        SupportClass.serialize(out_Renamed,array);
        SupportClass.serialize(out_Renamed,m_categorizationGroups);
    }

    //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
    public void load(System.IO.BinaryReader in_Renamed) throws Exception {
        m_hTree.Clear();
        //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        ArrayList array = (ArrayList)SupportClass.deserialize(in_Renamed);
        for (int i = 1;i < array.size();i += 2)
            m_hTree.put((String)array.get(i - 1), (String)array.get(i));
        SupportClass.deserialize(in_Renamed);
    }

}


//end of DefParser