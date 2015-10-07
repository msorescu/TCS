//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:43 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.STRINGS;

public class MLSPropertyFields   
{
    public static final String FIELD_PREFIX = "__CUST__";
    public static final int SEARCH_TYPE_BY_COMMON = 0;
    public static final int SEARCH_TYPE_BY_MLSNUM = 1;
    public static final int SEARCH_TYPE_BY_ADVANCED = 2;
    private ArrayList m_PropertyFields = ArrayList.Synchronized(new ArrayList(10));
    public int size() throws Exception {
        return m_PropertyFields.size();
    }

    public net.toppro.components.mls.engine.PropertyField[] toArray() throws Exception {
        net.toppro.components.mls.engine.PropertyField[] pf = new net.toppro.components.mls.engine.PropertyField[m_PropertyFields.size()];
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            pf[i] = f;
        }
        return pf;
    }

    public net.toppro.components.mls.engine.PropertyField[] getAdvancedSearchFields() throws Exception {
        int pf_actually_size = 0;
        net.toppro.components.mls.engine.PropertyField[] pf = new net.toppro.components.mls.engine.PropertyField[32];
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (!f.isMLSNumField())
            {
                if (pf_actually_size < pf.length)
                    pf[pf_actually_size++] = f;
                else
                {
                    net.toppro.components.mls.engine.PropertyField[] new_pf = new net.toppro.components.mls.engine.PropertyField[pf_actually_size * 2];
                    Array.Copy(pf, 0, new_pf, 0, pf_actually_size);
                    pf = new_pf;
                    pf[pf_actually_size++] = f;
                } 
            }
             
        }
        if (pf.length == 0)
            return null;
         
        net.toppro.components.mls.engine.PropertyField[] new_pf2 = new net.toppro.components.mls.engine.PropertyField[pf_actually_size];
        Array.Copy(pf, 0, new_pf2, 0, pf_actually_size);
        return new_pf2;
    }

    public net.toppro.components.mls.engine.PropertyField[] getRequredForMLSNumFields() throws Exception {
        int pf_actually_size = 0;
        net.toppro.components.mls.engine.PropertyField[] pf = new net.toppro.components.mls.engine.PropertyField[32];
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (f.isRequiredForMlsNum() || f.isMLSNumField())
            {
                if (pf_actually_size < pf.length)
                    pf[pf_actually_size++] = f;
                else
                {
                    net.toppro.components.mls.engine.PropertyField[] new_pf = new net.toppro.components.mls.engine.PropertyField[pf_actually_size * 2];
                    Array.Copy(pf, 0, new_pf, 0, pf_actually_size);
                    pf = new_pf;
                    pf[pf_actually_size++] = f;
                } 
            }
             
        }
        if (pf_actually_size == 0)
            return null;
         
        net.toppro.components.mls.engine.PropertyField[] new_pf2 = new net.toppro.components.mls.engine.PropertyField[pf_actually_size];
        Array.Copy(pf, 0, new_pf2, 0, pf_actually_size);
        return new_pf2;
    }

    public void setInUse(int mode) throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            f.setInUse(false);
            switch(mode)
            {
                case SEARCH_TYPE_BY_COMMON: 
                    f.setInUse(true);
                    break;
                case SEARCH_TYPE_BY_MLSNUM: 
                    if (f.isRequiredForMlsNum() || f.isMLSNumField())
                        f.setInUse(true);
                     
                    break;
                case SEARCH_TYPE_BY_ADVANCED: 
                    if (!f.isMLSNumField())
                        f.setInUse(true);
                     
                    break;
            
            }
        }
    }

    public boolean checkRequeredFields() throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (f.isRequired() && f.getValue().length() == 0)
                return false;
             
        }
        return true;
    }

    public net.toppro.components.mls.engine.PropertyField getField(String _name) throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField field = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (field.getName().toUpperCase().equals(_name.toUpperCase()))
                return field;
             
        }
        return null;
    }

    public net.toppro.components.mls.engine.PropertyField getField(int item) throws Exception {
        if (item >= 0 && item < m_PropertyFields.size())
            return (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(item);
         
        return null;
    }

    /**
    * This function should be called from the constructors of some communication clients
    * (see ASCIIClient)
    */
    public void addField(net.toppro.components.mls.engine.PropertyField field) throws Exception {
        m_PropertyFields.add(field);
    }

    public void init(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        DefParser parser = engine.getDefParser();
        String buffer = parser.getFieldsContent();
        int dataType = 2;
        boolean newLine = false;
        net.toppro.components.mls.engine.PropertyField f = null;
        String str = "";
        StringBuilder paramName = new StringBuilder();
        StringBuilder paramValue = new StringBuilder();
        int i = 0;
        int j;
        while (i < buffer.length())
        {
            char c = buffer.charAt(i);
            switch(dataType)
            {
                case 3: 
                    if (c != '\r' && c != ';')
                    {
                        paramValue.append(c);
                        i++;
                    }
                    else
                    {
                        if (newLine == true && c == ';')
                        {
                            int pos = i + 1;
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            i = SupportClass.getIndex(buffer,"\r\n",pos);
                            if (i == -1)
                            {
                                String s = buffer.substring(pos);
                                engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,s);
                                throw engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS),s);
                            }
                             
                            i += 2;
                            if (i > buffer.length())
                            {
                                String s = buffer.substring(pos);
                                engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,s);
                                throw engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS),s);
                            }
                             
                        }
                         
                        if (newLine && c == '\r')
                            return ;
                         
                        if (c == ';')
                        {
                            int pos = i + 1;
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            i = SupportClass.getIndex(buffer,"\r\n",pos);
                            if (i == -1)
                            {
                                String s = buffer.substring(pos);
                                engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,s);
                                throw engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS),s);
                            }
                             
                            if (i > buffer.length())
                                return ;
                             
                            i += 2;
                        }
                         
                        if (c == '\r')
                        {
                            int pos = i + 1;
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            i = SupportClass.getIndex(buffer,"\n",pos);
                            if (i == -1)
                            {
                                String s = buffer.substring(pos);
                                engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,s);
                                throw engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS),s);
                            }
                             
                            if (i > buffer.length())
                                return ;
                             
                            i++;
                        }
                         
                        newLine = true;
                        paramValue = new StringBuilder(StringSupport.Trim(paramValue.toString()));
                        f = new net.toppro.components.mls.engine.PropertyField();
                        if (paramValue.toString().indexOf(FIELD_PREFIX) == -1)
                            f.setPrefix(paramValue.toString());
                         
                        for (j = 0;j < net.toppro.components.mls.engine.PropertyField.PROPERTIES_NAMES.length;j++)
                        {
                            String prop = net.toppro.components.mls.engine.PropertyField.PROPERTIES_NAMES[j];
                            f.setPropertyValue(engine,prop,parser.getValue("Field_" + paramName.toString(),prop));
                        }
                        f.setName(paramName.toString());
                        f.setValue(paramValue.toString());
                        str = " ";
                        j = 1;
                        while (str.length() != 0)
                        {
                            str = parser.getValue("Field_" + paramName.toString(),"Value" + (j++));
                            if (str.length() != 0)
                                f.addInitValue(str);
                             
                        }
                        if (f.getCaption().length() == 0)
                            f.setCaption(paramName.toString());
                         
                        m_PropertyFields.add(f);
                        dataType = 2;
                        paramName = new StringBuilder();
                    } 
                    break;
                case 2: 
                    if (c != '=')
                    {
                        if (paramName.length() == 0 && c == ';')
                        {
                            int pos = i + 1;
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            i = SupportClass.getIndex(buffer,"\r\n",i + 1);
                            if (i == -1)
                            {
                                String s = buffer.substring(pos);
                                engine.notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_BAD_SECTION_SYNTAX,net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS,s);
                                throw engine.createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.COMMON_ERR_BAD_DEF_FILE_SYNTAX),net.toppro.components.mls.engine.MLSEngine.SECTION_FIELDS),s);
                            }
                             
                            i += 2;
                            if (i > buffer.length())
                                return ;
                             
                            break;
                        }
                         
                        if (paramName.length() == 0 && c == '\r')
                            return ;
                         
                        paramName.append(c);
                    }
                    else
                    {
                        paramName = new StringBuilder(StringSupport.Trim(paramName.toString()));
                        paramValue = new StringBuilder();
                        dataType = 3;
                        newLine = false;
                    } 
                    i++;
                    break;
            
            }
        }
    }

    //end of initPropetiesFields
    public void clearValues() throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
            ((net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i)).setValue("");
    }

    public void load(String fileName) throws Exception {
        if (fileName == null)
        {
            clearValues();
            return ;
        }
         
        File file = new File(fileName);
        boolean tmpBool;
        if (File.Exists(file.FullName))
            tmpBool = true;
        else
            tmpBool = File.Exists(file.FullName); 
        if (!tmpBool)
        {
            clearValues();
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
            for (int i = 0;i < m_PropertyFields.size();i++)
            {
                String value_Renamed = s.readLine();
                if (value_Renamed != null)
                    ((net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i)).setValue(value_Renamed);
                else
                    ((net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i)).setValue(""); 
            }
        }
        catch (Exception e)
        {
            System.out.println("Warning: unable to load property fields");
            SupportClass.WriteStackTrace(e, Console.Error);
            clearValues();
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

    public void save(String fileName) throws Exception {
        if (fileName == null)
            return ;
         
        FileStreamSupport f = null;
        PrintWriter s = null;
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
            f = new FileStreamSupport(fileName, FileMode.Create);
            s = new PrintWriter(f, Encoding.Default);
            String value_Renamed = "";
            for (int i = 0;i < m_PropertyFields.size();i++)
            {
                value_Renamed = ((net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i)).getValue();
                s.print(value_Renamed + "\r\n");
            }
        }
        catch (Exception e)
        {
            System.out.println("Warning: unable to save property fields");
            SupportClass.WriteStackTrace(e, Console.Error);
        }
        finally
        {
            if (s != null)
            {
                try
                {
                    //UPGRADE_NOTE: Exceptions thrown by the equivalent in .NET of method 'java.io.PrintWriter.close' may be different. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1099'"
                    s.close();
                }
                catch (Exception e2)
                {
                        ;
                }
            
            }
             
            if (f != null)
            {
                try
                {
                    f.close();
                }
                catch (Exception e3)
                {
                        ;
                }
            
            }
             
        }
    }

    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
    public void serialize(System.IO.BinaryWriter out_Renamed) throws Exception {
        if (m_PropertyFields == null)
            out_Renamed.Write(0);
         
        out_Renamed.Write(m_PropertyFields.size());
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            SupportClass.serialize(out_Renamed,m_PropertyFields.get(i));
        }
    }

    //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
    public void deserialize(System.IO.BinaryReader in_Renamed) throws Exception {
        int size = in_Renamed.Read();
        m_PropertyFields.clear();
        for (int i = 0;i < size;i++)
        {
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            m_PropertyFields.add((net.toppro.components.mls.engine.PropertyField)SupportClass.deserialize(in_Renamed));
        }
    }

}


