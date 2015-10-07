//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:39 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileNotFoundException;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import java.io.BufferedReader;
import java.io.IOException;
import net.toppro.components.mls.engine.client.ASCIIClient;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.PropertyFieldGroups;

/**
* WyldFyre client.
*/
public class WyldFyreClient  extends ASCIIClient 
{
    private static final String ATTRIBUTE_WYLDFYRE_MLS_NAME = "WyldFyreMLSName";
    private static final String ATTRIBUTE_WYLDFYRE_CLASS = "WyldFyreClass";
    private static final String ATTRIBUTE_WYLDFYRE_ALLFIELDS = "WyldFyreAllFields";
    private String[] m_run_path = null;
    private MLSException m_init_error = null;
    public WyldFyreClient(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        super(engine);
    }

    public void initPropertyFields(PropertyFieldGroups groups) throws Exception {
    }

    public String getDefaultToolTip() throws Exception {
        return getEngine().getEnvironment().formatBrands(STRINGS.get_Renamed(STRINGS.WYLDFYRE_DEFAULT_TOOLTIP));
    }

    /**
    * 
    */
    public String getSourceFolderName() throws Exception {
        //read file to stream
        FileStreamSupport stream;
        StringBuilder file_context = new StringBuilder();
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            stream = new FileStreamSupport(getSourceFileName(), FileMode.Open, FileAccess.Read);
        }
        catch (FileNotFoundException fnfe)
        {
            throw getEngine().createException(fnfe, STRINGS.get_Renamed(STRINGS.ERR_DATA_FILE_NOT_FOUND));
        }

        BufferedReader reader = new BufferedReader(stream, Encoding.Default);
        //UPGRADE_TODO: The differences in the expected value  of parameters for constructor 'java.io.BufferedReader.BufferedReader'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
        BufferedReader buffered_reader = new BufferedReader(reader.BaseStream, reader.CurrentEncoding);
        sbyte b;
        try
        {
            while ((b = (sbyte)buffered_reader.Read()) != -1)
            if (b != 0)
                file_context.append((char)b);
             
            buffered_reader.close();
            reader.close();
            stream.close();
        }
        catch (IOException ioe)
        {
            throw getEngine().createException(ioe, STRINGS.get_Renamed(STRINGS.S21));
        }

        int i = file_context.toString().indexOf("PicPath=");
        if (i < 0)
            throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S21));
         
        i += "PicPath=".length();
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        int j = file_context.toString().indexOf('\r', i);
        if (j < 0)
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            j = file_context.toString().indexOf('\n', i);
            if (j < 0)
                return file_context.toString().substring(i);
             
        }
         
        return file_context.toString().Substring(i, (j)-(i));
    }

    /**
    * 
    */
    public String getSourceFileName() throws Exception {
        return getEngine().getResultsFilename();
    }

    public void runMainScript() throws Exception {
        if (m_init_error != null)
            throw m_init_error;
         
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        Environment environment = getEngine().getEnvironment();
        if (m_run_path == null)
        {
            environment.setMessage(STRINGS.get_Renamed(STRINGS.WYLDFYRE_DOWNLOADING));
            try
            {
                DefParser parser = engine.getDefParser();
                String helper_zip = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TPONLINE,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HELPER_ZIP);
                if (helper_zip.length() == 0)
                {
                    System.out.println("Empty HelperZip arrtibute ");
                }
                 
                String dir = engine.install(helper_zip);
                String mls = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TPONLINE,ATTRIBUTE_WYLDFYRE_MLS_NAME);
                if (mls == null)
                    mls = "";
                else
                    mls = StringSupport.Trim(mls); 
                String type = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TPONLINE,ATTRIBUTE_WYLDFYRE_CLASS);
                if (type == null)
                    type = "";
                else
                    type = StringSupport.Trim(type); 
                String allFieldsString = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TPONLINE,ATTRIBUTE_WYLDFYRE_ALLFIELDS);
                boolean allFields;
                if (allFieldsString != null)
                {
                    allFieldsString = allFieldsString.toLowerCase();
                    allFields = allFieldsString.equals("true") || allFieldsString.equals("1") || allFieldsString.equals("yes") || allFieldsString.equals("on");
                }
                else
                    allFields = false; 
                if (mls.length() > 0)
                {
                    if (type.length() > 0)
                    {
                        if (allFields)
                        {
                            m_run_path = new String[]{ dir + "WFImport.exe", "/log:\"" + getSourceFileName() + "\"", "/mls:\"" + mls + "\"", "/type:" + type, "/allfields" };
                        }
                        else
                        {
                            m_run_path = new String[]{ dir + "WFImport.exe", "/log:\"" + getSourceFileName() + "\"", "/mls:\"" + mls + "\"", "/type:" + type };
                        } 
                    }
                    else if (allFields)
                    {
                        m_run_path = new String[]{ dir + "WFImport.exe", "/log:\"" + getSourceFileName() + "\"", "/mls:\"" + mls + "\"", "/allfields" };
                    }
                    else
                    {
                        m_run_path = new String[]{ dir + "WFImport.exe", "/log:\"" + getSourceFileName() + "\"", "/mls:\"" + mls + "\"" };
                    }  
                }
                else if (allFields)
                {
                    m_run_path = new String[]{ dir + "WFImport.exe", "/log:\"" + getSourceFileName() + "\"", "/allfields" };
                }
                else
                {
                    m_run_path = new String[]{ dir + "WFImport.exe", "/log:\"" + getSourceFileName() + "\"" };
                }  
            }
            catch (MLSException exc)
            {
                m_init_error = exc;
                throw exc;
            }
        
        }
         
        try
        {
            environment.setMessage(STRINGS.get_Renamed(STRINGS.WYLDFYRE_RUNNING));
        }
        catch (MLSException exc)
        {
            throw exc;
        }
        catch (Exception exc)
        {
            throw engine.createException(exc,STRINGS.get_Renamed(STRINGS.ERR_WYLD_FYRE_SEARCH_CANCELED));
        }
    
    }

}


//System.Diagnostics.Process process = SupportClass.ExecSupport(m_run_path);
//process.WaitForExit();
//if (process.ExitCode != 0)
//    throw engine.createException(STRINGS.get_Renamed(STRINGS.ERR_WYLD_FYRE_SEARCH_CANCELED));