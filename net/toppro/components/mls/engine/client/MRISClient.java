//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:36 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileNotFoundException;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.Web.HttpUtilSupport;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.FileFilter;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.PropertyFieldGroups;

/**
* This is the implelemtation of MRISClient.   Ilya Solnyshkin
*/
public class MRISClient  extends net.toppro.components.mls.engine.MLSScriptClient 
{
    /**
    * 
    */
    private static final String IMPORT_DATA_TEXT = "ImportData.txt";
    private static final String IMPORT_DATA_ZIP = "ImportData.zip";
    public static final String FIELDNAME_FILESELECT = "FileSelect";
    private static final String SELECT_DATA_FILE = "Select MLS data file";
    private static final String HTTP_PROTOCOL = "http";
    public String getSourceFileName() throws Exception {
        String filePath = getEngine().getEnvironment().getUploadFolder() + IMPORT_DATA_TEXT;
        if ((new File(filePath)).exists())
            return filePath;
        else
            return getEngine().getEnvironment().getUploadFolder() + IMPORT_DATA_ZIP; 
    }

    public String getDefaultToolTip() throws Exception {
        return getEngine().getBoardSetup().getNotes();
    }

    protected public boolean m_debug = true;
    private boolean m_bWriteFlag = false;
    protected public DefParser m_DefParser = null;
    private StringBuilder m_strConnectionString = null;
    private String m_strCGIUserName = null;
    private String m_strCGIPassword = null;
    private String m_strSiteIPAddress = null;
    public MRISClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
        m_DefParser = getEngine().getDefParser();
    }

    /**
    * This function is called by MLSEngine right after client constructor. Client must implement
    * it to initialize search fields (we call them property fields).
    * 
    *  @param fields object containing property fields info. Implementation should use
    * MLSPropertyFields.addField, or MLSPropertyFields.initPropetiesFields functions to
    * initialize it.
    * 
    *  @see MLSPropertyFields, PropertyField
    */
    public void initPropertyFields(PropertyFieldGroups groups) throws Exception {
        net.toppro.components.mls.engine.PropertyField f = null;
        //f = new PropertyField();
        //f.setName("Label1");
        //f.setControlType(PropertyField.CONTROL_TYPE_LABELPANEL);
        //getEngine().addPropertyField(f);
        f = new net.toppro.components.mls.engine.PropertyField();
        f.setName(FIELDNAME_FILESELECT);
        f.setControlType(net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_FILESELECT_PANEL);
        f.setCaption(SELECT_DATA_FILE);
        f.setRequired(true);
        f.setDelimiter("\r");
        f.setLayout(75);
        getEngine().addPropertyField(f);
    }

    public void connect(String host, int port) throws Exception {
        m_strConnectionString = new StringBuilder();
    }

    public int getFlags() throws Exception {
        return FLAG_PICSCRIPT_INSIDE_MAINSCRIPT;
    }

    protected public void runScript() throws Exception {
        try
        {
            connect(null,0);
        }
        catch (Exception e)
        {
                ;
        }

        net.toppro.components.mls.engine.client.MRISClient.APPLY __dummyScrutVar0 = getCurrentScriptType();
        if (__dummyScrutVar0.equals(net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_MAIN))
        {
            downloadMain();
        }
        else if (__dummyScrutVar0.equals(net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_PIC))
        {
            MLSRecord rec = getFirstRecord();
            MLSException exc = null;
            int TotalRecs = getRecordCount();
            int CurrentRec = 0;
            while (rec != null)
            {
                getEngine().getEnvironment().checkStop();
                try
                {
                    super.runScript();
                }
                catch (MLSException e)
                {
                    exc = e;
                }

                getEngine().setProgress(CurrentRec++, TotalRecs);
                rec = getNextRecord();
            }
            if (exc != null)
                throw exc;
             
        }
          
    }

    /**
    * Launch process of downloading of data(records) from MLS Board.
    */
    public void downloadMain() throws Exception {
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        try
        {
            String commandName = "";
            String paramValue = "";
            int i = 0, j = 0, result = 0;
            StringBuilder Str = null;
            StringBuilder TimeOut = null;
            String DataFileName = engine.getResultsFilename();
            //if (!engine.getPropertyFields().CheckRequeredFields())
            //{
            //    throw engine.createException(STRINGS.get_Renamed(STRINGS.S31));
            //}
            String tmpDataFileName = getSourceFileName();
                ;
            File f = new File(tmpDataFileName);
            boolean tmpBool;
            if (File.Exists(f.FullName))
                tmpBool = true;
            else
                tmpBool = File.Exists(f.FullName); 
            if (!tmpBool)
            {
                throw engine.createException(STRINGS.get_Renamed(STRINGS.S36));
            }
             
            String[] files = new String[0];
            FileFilter remove_filter = null;
            if (tmpDataFileName.indexOf(".zip") != -1)
            {
                String DataFileFromDef = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DATAFILENAME);
                if (DataFileFromDef.indexOf("*") >= 0 || DataFileFromDef.indexOf("?") >= 0)
                {
                    remove_filter = new FileFilter(getEngine().getResultsFolder(), FileFilter.FLAG_FIND_FILES);
                    files = remove_filter.getFiles(DataFileFromDef);
                    for (i = files.length - 1;i >= 0;i--)
                    {
                        if (!files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RESULTS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.FIELDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RECORDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.ENGINE_FILE.toUpperCase()))
                        {
                            File rf = new File(getEngine().getResultsFolder() + files[i]);
                            boolean tmpBool2;
                            if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool2 = true;
                            }
                            else if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool2 = true;
                            }
                            else
                                tmpBool2 = false;  
                            boolean generatedAux = tmpBool2;
                        }
                         
                    }
                }
                 
                String HeaderFile = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HEADERINFOFILE);
                if (HeaderFile.indexOf("*") >= 0 || HeaderFile.indexOf("?") >= 0)
                {
                    files = remove_filter.getFiles(HeaderFile);
                    for (i = files.length - 1;i >= 0;i--)
                    {
                        if (!files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RESULTS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.FIELDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RECORDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.ENGINE_FILE.toUpperCase()))
                        {
                            File rf = new File(getEngine().getResultsFolder() + files[i]);
                            boolean tmpBool3;
                            if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool3 = true;
                            }
                            else if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool3 = true;
                            }
                            else
                                tmpBool3 = false;  
                            boolean generatedAux2 = tmpBool3;
                        }
                         
                    }
                }
                 
                MLSUtil.extractTo(tmpDataFileName, getEngine().getResultsFolder());
                tmpDataFileName = DataFileFromDef;
                if (tmpDataFileName.length() != 0)
                {
                    DataFileName = getEngine().getResultsFolder() + tmpDataFileName;
                    if (tmpDataFileName.indexOf("*") >= 0 || tmpDataFileName.indexOf("?") >= 0)
                    {
                        FileFilter filter = new FileFilter(getEngine().getResultsFolder(), FileFilter.FLAG_FIND_FILES);
                        files = filter.getFiles(tmpDataFileName);
                        if (filter.getSize() > 1)
                        {
                            j = 0;
                            for (i = files.length - 1;i >= 0;i--)
                            {
                                if (files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RESULTS_FILE.toUpperCase()) || files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.FIELDS_FILE.toUpperCase()) || files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RECORDS_FILE.toUpperCase()) || files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.ENGINE_FILE.toUpperCase()))
                                    j++;
                                else
                                    DataFileName = getEngine().getResultsFolder() + files[i]; 
                            }
                            if (files.length - j > 1)
                                throw engine.createException(STRINGS.get_Renamed(STRINGS.ERR_BAD_RESULTS_FORMAT));
                             
                        }
                        else
                            DataFileName = filter.getFirstMatch(tmpDataFileName); 
                    }
                     
                    File tmpFile = new File(DataFileName);
                    boolean tmpBool4;
                    if (File.Exists(tmpFile.FullName))
                        tmpBool4 = true;
                    else
                        tmpBool4 = File.Exists(tmpFile.FullName); 
                    if (!tmpBool4)
                        throw engine.createException(STRINGS.get_Renamed(STRINGS.ERR_BAD_RESULTS_FORMAT));
                     
                    //tmpFile.renameTo(new File(getEngine().getResultsFilename()));
                    DataFileName = getEngine().getResultsFolder();
                    MLSUtil.CopyFile(engine, tmpFile.FullName, DataFileName);
                }
                 
            }
            else
                MLSUtil.copyFile(engine,tmpDataFileName,DataFileName); 
        }
        catch (MLSException exc)
        {
            throw exc;
        }
        catch (Exception exc)
        {
            throw engine.createException(exc);
        }
    
    }

    public void write(String buffer) throws Exception {
        try
        {
            if (buffer.indexOf("\r") != -1)
                m_strConnectionString.append(buffer.substring(0, (0) + ((buffer.indexOf("\r")) - (0))));
            else
            {
                if (m_bWriteFlag)
                {
                    m_strConnectionString = new StringBuilder();
                    m_strConnectionString.append(buffer);
                    m_bWriteFlag = false;
                }
                else
                    m_strConnectionString.append(buffer); 
            } 
        }
        catch (Exception e)
        {
            SupportClass.WriteStackTrace(e, Console.Error);
        }
    
    }

    protected public String encodeParameter(String parameter, int parameterType) throws Exception {
        if (parameterType != PARAMETER_TYPE_SECURITY_FIELD)
        {
            parameter = MLSUtil.replaceSubStr(HttpUtilSupport.UrlEncode(parameter),"+","%20");
        }
         
        return parameter;
    }

    // we don't use pure URLEncoder.encode because some servers don't support + as %20(space)
    // Sergei 13 November 2003: We (me and Ilya) dont know what the next line is for. We agreed to
    // comment it and see what happens. If you find that enough time have passed and nobody reports a
    // problems - just remove it and these comments.
    // parameter = MLSUtil.replaceSubStr ( request_url, "%3A", ":" );
    public String read(int len) throws Exception {
        return null;
    }

    protected public void unread(String str) throws Exception {
    }

    public void disconnect(Exception e) throws Exception {
    }

    public void initBoardSetup(BoardSetup setup) throws Exception {
    }

    protected public void setTimeout(long timeout) throws Exception {
    }

    protected public void receive(String s) throws Exception {
        m_bWriteFlag = true;
        if (getCurrentScriptType() == SCRIPT_PIC)
            download();
         
    }

    protected public void download() throws Exception {
        m_strSiteIPAddress = m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_IPADDRESS).Trim();
        if (m_strSiteIPAddress.length() != 0)
            m_strSiteIPAddress = getBaseURLFull(StringSupport.Trim(m_strConnectionString.toString()));
         
        m_strCGIUserName = getCGIUserInfo(m_strConnectionString,true);
        m_strCGIPassword = getCGIUserInfo(m_strConnectionString,false);
        if (getCurrentScriptType() == net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_PIC)
            downloadPic();
         
    }

    protected public void downloadPic() throws Exception {
        String strUrl = StringSupport.Trim(m_strConnectionString.toString());
        String strPicPath = getEngine().getResultsFolder();
        String strPicFullName = null;
        int type = int.Parse(getEngine().GetClientType());
        try
        {
            strPicFullName = strPicPath + getCurrentRecordId() + ".jpg";
            fileDownload(strPicFullName,strUrl);
        }
        catch (Exception e)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            System.out.println(STRINGS.get_Renamed(STRINGS.ERROR_DOWNLOAD_PIC) + e.getMessage());
        }
    
    }

    protected public void fileDownload(String strDestinationPath, String strWebUrl) throws Exception {
        connectHttpDirect(strDestinationPath,strWebUrl);
    }

    private void connectHttpDirect(String saveTo, String sReq) throws Exception {
        debugLog(null,"###################### request string ###################");
        debugLog(null,sReq);
        debugLog(null,"######################  request end   ###################");
        debugLog(null,"###################### request string ###################");
        debugLog(null,sReq);
        debugLog(null,"######################  request end   ###################");
        try
        {
            //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
            URI url = new URI(StringSupport.Trim(sReq));
            HttpURLConnection urlc = (HttpURLConnection)url.toURL().openConnection();
            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            byte[] b = new byte[1];
            //UPGRADE_TODO: Class 'java.io.DataInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioDataInputStream'"
            System.IO.BinaryReader di = new System.IO.BinaryReader(urlc.getInputStream());
            //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
            FileStreamSupport fo = new FileStreamSupport(saveTo, FileMode.Create);
            while (-1 != SupportClass.ReadInput(di.BaseStream, b, 0, 1))
            {
                fo.write(b,0,1);
                environment.checkStop();
            }
            //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
            di.Close();
            fo.close();
        }
        catch (Exception e)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            System.out.println(e.getMessage());
        }
    
    }

    //UPGRADE_NOTE: Access modifiers of method 'prepareDataFile' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public String prepareDataFile(String datFileName) throws Exception {
        StringBuilder BeginDataDelimiter = null;
        boolean bCutStartProcessingString = false;
        String EndProcessingString = MLSUtil.toStr(m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_ENDPROCESSINGSTRING));
        BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING)));
        if (BeginDataDelimiter.length() == 0 && m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK).Length != 0)
            BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK)));
        else
            bCutStartProcessingString = true; 
        //read file to stream
        FileStreamSupport stream;
        StringBuilder file_context = new StringBuilder();
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            stream = new FileStreamSupport(datFileName, FileMode.Open, FileAccess.Read);
        }
        catch (FileNotFoundException fnfe)
        {
            throw getEngine().createException(fnfe, STRINGS.get_Renamed(STRINGS.ERR_DATA_FILE_NOT_FOUND));
        }

        System.IO.BufferedStream reader = null;
        try
        {
            int b;
            reader = new System.IO.BufferedStream(stream);
            while ((b = reader.ReadByte()) > -1)
            //if(b != 0) // iLYA: I commented because WyldFyre create file this 00 inside of data
            file_context.append((char)b);
        }
        catch (Exception ioe)
        {
            throw getEngine().createException(ioe);
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
                    reader.Close();
                }
                 
            }
            catch (Exception exc)
            {
                //UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
                SupportClass.WriteStackTrace(exc, Console.Error);
            }

            try
            {
                if (stream != null)
                    stream.close();
                 
            }
            catch (Exception exc)
            {
                //UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
                SupportClass.WriteStackTrace(exc, Console.Error);
            }
        
        }
        int i, j = 0;
        if ((i = file_context.toString().indexOf("<title>")) != -1)
        {
            String error = file_context.toString().substring(i + new StringBuilder("<title>").toString().length(), (i + new StringBuilder("<title>").toString().length()) + ((file_context.toString().indexOf("</title>")) - (i + new StringBuilder("<title>").toString().length())));
            String fileMsg = file_context.toString();
            fileMsg = MLSUtil.replaceSubStr(fileMsg,"<hr>","<hr><br>");
            if (fileMsg.length() > 1024)
                fileMsg = fileMsg.substring(1024);
             
            throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg, MLSException.FORMAT_HTML);
        }
         
        //find begin of data
        if (BeginDataDelimiter.length() != 0)
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            i = file_context.toString().indexOf(BeginDataDelimiter.toString(), 0);
        }
        else
            i = 0; 
        if (i == -1)
        {
            String fileMsg = StringSupport.Trim(file_context.toString());
            if (fileMsg.length() != 0)
            {
                fileMsg = MLSUtil.replaceSubStr(fileMsg,"\r\n","<br>");
                if (fileMsg.length() > 1024)
                    fileMsg = fileMsg.substring(1024);
                 
                throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg, MLSException.FORMAT_HTML);
            }
            else
                return ""; 
        }
        else
        {
            String data = "";
            if (bCutStartProcessingString)
                data = file_context.toString().Substring(i + BeginDataDelimiter.toString().length(), (file_context.Length)-(i + BeginDataDelimiter.toString().length()));
            else
                data = file_context.toString(); 
            if (EndProcessingString.length() != 0 && (j = data.lastIndexOf(EndProcessingString)) != -1)
                return data.Substring(0, (j)-(0));
             
            return data;
        } 
    }

    protected public void printHeadInfo(HttpURLConnectionEx hc) throws Exception {
        if (hc != null && m_debug)
        {
            System.out.println("<------begin http response header info-----");
            for (int i = 0;i < hc.getResponseHeaderCount();i++)
                System.out.println(hc.getHeaderFieldKey(i));
            System.out.println("------end of http response header info----->");
        }
         
    }

    public String getBaseURLFull(String sUrl) throws Exception {
        try
        {
            int i;
            String s = "";
            if ((i = sUrl.indexOf(HTTP_PROTOCOL)) == -1)
                return sUrl;
            else
            {
                i += new StringBuilder(HTTP_PROTOCOL).toString().length();
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                s = sUrl.substring(i, (i) + ((sUrl.indexOf("/", i)) - (i)));
                s = HTTP_PROTOCOL + s;
                return StringSupport.Trim(s);
            } 
        }
        catch (Exception e)
        {
            throw getEngine().createException(e, STRINGS.get_Renamed(STRINGS.ERR_ERROR_WRONGURL));
        }
    
    }

    protected public String getCGIUserInfo(StringBuilder sURL, boolean bGetUserName) throws Exception {
        if (sURL == null)
            return null;
         
        String item = "";
        String source = sURL.toString();
        int i1 = sURL.toString().indexOf("//");
        int i2 = sURL.toString().indexOf("@");
        if (i1 > 0 && i2 > 0)
        {
            item = source.Substring(i1 + 2, (i2)-(i1 + 2));
            if (item.indexOf(":") > 0)
            {
                if (bGetUserName)
                    item = item.substring(0, (0) + ((item.indexOf(":")) - (0)));
                else
                    item = item.Substring(item.indexOf(":") + 1, (item.Length)-(item.indexOf(":") + 1)); 
                return item;
            }
            else
                return null; 
        }
        else
            return null; 
    }

    protected public void debugLog(String method, String content) throws Exception {
        String log = "";
        if (m_debug)
        {
            if (method != null && StringSupport.Trim(method).length() != 0)
                log += ("Method name: " + method + " ");
             
            if (content != null && StringSupport.Trim(content).length() != 0)
                log += content;
             
            if (log.length() != 0)
                System.out.println(log);
             
        }
         
    }

}


