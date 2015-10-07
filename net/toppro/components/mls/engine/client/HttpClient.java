//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:35 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileNotFoundException;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.Web.HttpUtilSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.client.FTP;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSCmaNotes;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.TCSStandardResultFields;

/**
* This is the new implelemtation of HTTPClient. It follows the same logic
* as TOPPRODUCER 6i VB code.
* set some method and member to protected for RETS client.
*   Jack Jian
*/
public class HttpClient  extends net.toppro.components.mls.engine.MLSScriptClient 
{
    protected public boolean m_debug = true;
    protected public static final String HTTP_HEAD_LOCATION = "Location";
    protected public static final String HTTP_HEAD_CONTENTTYPE = "Content-Type";
    protected public static final String HTTP_HEAD_SETCOOKIE = "Set-Cookie";
    protected public static final String HTTP_HEAD_AUTHEN = "WWW-Authenticate";
    protected public String HTTP_PROTOCOL = "http://";
    protected public static final String HTTPS_PROTOCOL = "https://";
    protected public static final String FTP_PROTOCOL = "ftp://";
    protected public static final String FILENAME_NOTES = "@@Notes@@.dat";
    private static final String FILENAME_MLSNUM = "mls-num.txt";
    private static final int MAX_DOWNLOAD_SIZE = 50000000;
    protected public static final int MAX_PICTURES = 100;
    private static final String DEFAULT_PICTURE_EXTENSION = "jpg";
    protected public boolean m_writeFlag = false;
    protected public String m_cookie = "";
    protected public String m_CGIUsername = null;
    protected public String m_CGIPassword = null;
    protected public String m_dataFile = null;
    protected public String m_siteIPAddress = null;
    protected public StringBuilder m_connString = null;
    protected public HttpURLConnectionEx m_hc = null;
    protected public DefParser m_def = null;
    protected public FTP m_ftpClient = null;
    protected boolean m_useRelativePath = true;
    public HttpClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
        m_def = getEngine().getDefParser();
        String useRelativePath = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_USERELATIVEPATH);
        if (useRelativePath != null && useRelativePath.equals("0"))
            m_useRelativePath = false;
         
    }

    public void connect(String host, int port) throws Exception {
        m_hc = null;
        m_cookie = "";
        m_siteIPAddress = null;
        m_CGIUsername = null;
        m_CGIPassword = null;
        m_dataFile = null;
        m_connString = new StringBuilder();
        System.Net.ServicePointManager.DefaultConnectionLimit = 20;
    }

    /**
    * @return  combination of FLAG_PICSCRIPT_INSIDE_MAINSCRIPT and FLAG_REMSCRIPT_INSIDE_MAINSCRIPT
    */
    public int getFlags() throws Exception {
        return FLAG_REMSCRIPT_INSIDE_MAINSCRIPT;
    }

    // for GTE client
    protected public void runScript() throws Exception {
        try
        {
            connect(null,0);
        }
        catch (Exception e)
        {
                ;
        }

        switch(getCurrentScriptType())
        {
            case net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_MAIN: 
                super.runScript();
                break;
            case net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_PIC: 
            case net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_EXTRAVAR: 
                if (!updateClient())
                    throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S29));
                 
                if (this instanceof RETSClient)
                {
                    super.runScript();
                    break;
                }
                 
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

                    if (getCurrentScriptType() == net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_PIC && isDownPicFlag3())
                        break;
                     
                    //getEngine().setProgress( CurrentRec++, TotalRecs );
                    rec = getNextRecord();
                }
                if (exc != null)
                    throw exc;
                 
                break;
            case net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_REM: 
                if (isMultiConnection())
                {
                    super.runScript();
                }
                 
                break;
        
        }
    }

    public void write(String str) throws Exception {
        try
        {
            if (str.indexOf("\r") != -1)
                m_connString.append(str.substring(0, (0) + ((str.indexOf("\r")) - (0))));
            else
            {
                if (m_writeFlag == true)
                {
                    m_connString = new StringBuilder();
                    m_connString.append(str);
                    m_writeFlag = false;
                }
                else
                    m_connString.append(str); 
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
        if (m_ftpClient != null)
        {
            m_ftpClient.disconnect();
            m_ftpClient = null;
        }
         
        if (m_hc != null)
            m_hc.disconnect();
         
    }

    protected public void setTimeout(long timeout) throws Exception {
    }

    public void initBoardSetup(BoardSetup setup) throws Exception {
        setup.addGroup(createSecFieldsSetupGroup());
    }

    protected public void receive(String s) throws Exception {
        m_writeFlag = true;
        if (m_connString.toString().IndexOf(HTTPS_PROTOCOL, StringComparison.CurrentCultureIgnoreCase) > -1)
            HTTP_PROTOCOL = HTTPS_PROTOCOL;
         
        if (getCurrentScriptType() != SCRIPT_REM)
            downLoad();
        else
            downloadNotes(getEngine().getResultsFolder() + FILENAME_NOTES); 
    }

    protected public void downLoad() throws Exception {
        m_siteIPAddress = StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_IPADDRESS));
        if (m_siteIPAddress.length() == 0)
            m_siteIPAddress = getBaseURLFull(StringSupport.Trim(m_connString.toString()));
         
        m_CGIUsername = getCGIUserInfo(m_connString,true);
        m_CGIPassword = getCGIUserInfo(m_connString,false);
        m_cookie = "";
        if (getCurrentScriptType() == net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_MAIN)
            downloadMain();
        else if (getCurrentScriptType() == net.toppro.components.mls.engine.MLSScriptClient.SCRIPT_PIC)
            downloadPic();
          
    }

    protected public void downloadMain() throws Exception {
        m_dataFile = getEngine().getResultsFilename();
        String sUrl = StringSupport.Trim(m_connString.toString());
        fileDownload(m_dataFile,sUrl);
        String isDelLastDelimeter = StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DELASTDEL));
        if (isDelLastDelimeter != null && isDelLastDelimeter.toUpperCase().equals("1".toUpperCase()))
            removeLastDelimeter(m_dataFile,getRecordDelimeter());
         
    }

    protected public void downloadPic() throws Exception {
        String sUrl = StringSupport.Trim(m_connString.toString());
        String picPath = getEngine().getResultsFolder();
        String picFullName = null;
        if (isDownPicFlag3())
        {
            downloadHSPics(sUrl);
            if (getEngine().getMulitiPictures().isGetMultiPicture())
            {
                MLSRecords records = getEngine().getMLSRecords(MLSRecords.FILTER_FLAG_CHECKED | MLSRecords.FILTER_FLAG_ANY_TYPE | MLSRecords.FILTER_FLAG_MISSING_PICTURES,null);
                MLSRecord rec = records.getFirstRecord();
                while (rec != null)
                {
                    multiPicFileDownload(rec.getRecordID(),rec.getDedicatedPictureFileName());
                    rec = records.getNextRecord();
                }
            }
             
            return ;
        }
         
        int type = Integer.valueOf(getEngine().getClientType());
        try
        {
            picFullName = picPath + getCurrentRecordId() + ".jpg";
            switch(type)
            {
                case 2: 
                case 7: 
                    // re/explore, rapattoni, Homeseekers, Realty Plus, Solidearth, RealGo
                    if (isDownPicFlag1())
                        sUrl = getDownPicFlag1Url(sUrl);
                    else if (isDownPicFlag5())
                        sUrl = getDownPicFlag5Url(sUrl);
                    else if (isDownPicFlag6())
                        sUrl = getDownPicFlag6Url(sUrl);
                    else if (isDownPicFlag14())
                        sUrl = getDownPicFlag14Url(sUrl);
                        
                    if (!getEngine().getMulitiPictures().isGetMultiPicture())
                        fileDownload(picFullName,sUrl);
                    else
                        multiPicFileDownload(getCurrentRecordId(),sUrl); 
                    break;
                case 3: 
                case 5: 
                    // download pic as zip file, Commercial Software Inc/Midland
                    // GTE
                    downloadPicZIP(picFullName,sUrl);
                    break;
                case 6: 
                    // marketlinx
                    downloadPic6(picFullName,sUrl);
                    break;
            
            }
        }
        catch (Exception e)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            getEngine().writeLine(STRINGS.get_Renamed(STRINGS.ERROR_DOWNLOAD_PIC) + e.getMessage());
        }
        finally
        {
            // Don't create no-pic image
            if (!getEngine().getEnvironment().isPictureFileSupported("file:/" + picFullName))
            {
                if (type != 3 && type != 5)
                {
                    File f = new File(picFullName);
                    boolean tmpBool;
                    if (File.Exists(f.FullName))
                    {
                        File.Delete(f.FullName);
                        tmpBool = true;
                    }
                    else if (File.Exists(f.FullName))
                    {
                        File.Delete(f.FullName);
                        tmpBool = true;
                    }
                    else
                        tmpBool = false;  
                    boolean generatedAux3 = tmpBool;
                }
                 
            }
             
        }
    }

    //createDefaultNoPicFile(picFullName);
    protected public void downloadNotes(String notesFileName) throws Exception {
        String request = m_connString.toString() + getRecordIDList();
        fileDownload(notesFileName,request);
        addNotesToRecords();
    }

    private void downloadHSPics(String sUrl) throws Exception {
        String idListFileName = createIDListFile();
        if (idListFileName == null)
            return ;
         
        String exePath = getEngine().getBinFolder();
        String cmdLine = exePath + sUrl + getEngine().getResultsFolder() + " /I:" + idListFileName;
        getEngine().writeLine(cmdLine);
        try
        {
            System.Diagnostics.Process.GetCurrentProcess();
            System.Diagnostics.Process process = System.Diagnostics.Process.Start(cmdLine);
            process.WaitForExit();
            int generatedAux3 = process.ExitCode;
        }
        catch (Exception e)
        {
            getEngine().writeLine(cmdLine);
            throw getEngine().createException(e,"Cannot execute downloading...");
        }
        finally
        {
        }
    }

    protected public void fileDownload(String sSaveto, String sReq) throws Exception {
        if (sReq.startsWith(FTP_PROTOCOL))
        {
            try
            {
                connectFTP(sSaveto,sReq);
            }
            catch (Exception e)
            {
            }

            return ;
        }
         
        //getEngine().WriteLine("Retry to get FTP picutre");
        //try
        //{
        //    if (m_ftpClient != null)
        //    {
        //        try
        //        {
        //            m_ftpClient.logout();
        //            m_ftpClient = null;
        //        }
        //        catch (System.Exception e2)
        //        {
        //            m_ftpClient = null;
        //        }
        //    }
        //    connectFTP(sSaveto, sReq);
        //}
        //catch (System.Exception e1)
        //{
        //}
        m_hc = createHcFromReq(sReq);
        connectHTTP(sSaveto,sReq,m_hc,true);
        getEngine().getEnvironment().checkStop();
        if (m_hc.getHeaderField(HTTP_HEAD_LOCATION) != null && StringSupport.Trim(m_hc.getHeaderField(HTTP_HEAD_LOCATION)).length() != 0)
            httpGetDocument(sSaveto,sReq,m_hc);
        else if (m_hc.getHeaderField(HTTP_HEAD_AUTHEN) != null && StringSupport.Trim(m_hc.getHeaderField(HTTP_HEAD_AUTHEN)).length() != 0)
            httpMlsExplorer(sSaveto,sReq);
          
    }

    protected public byte[] connectHTTP(String sSaveto, String sReq, HttpURLConnectionEx hc, boolean isSetAuth) throws Exception {
        try
        {
            // TODO: we actually should not do this, but currently
            // a lot of def-files use spaces in URL construction. :(
            // This is something what needs to be fixed.
            sReq = MLSUtil.replaceSubStr(sReq," ","%20");
            String request2 = removeCgiFromReq(sReq);
            try
            {
                //debugLog(null, "Send URL");
                //debugLog(null, request2);
                //debugLog(null, "\r\n");
                hc.connect();
            }
            catch (IOException ioe)
            {
                throw getEngine().createException(STRINGS.get_Renamed(STRINGS.SCRIPT_CLIENT_CANNOT_CONNECT));
            }

            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            setHttpHeader(hc,isSetAuth);
            try
            {
                hc.sendGetRequest(request2, getEngine());
            }
            catch (Exception e)
            {
                throw getEngine().createException(e,STRINGS.get_Renamed(STRINGS.S7_0));
            }

            byte[] data = hc.getResponseTextAsBytes();
            environment.checkStop();
            if (sSaveto != null && sSaveto.length() != 0 && data != null)
                // Hot fix for Norton IS
                writeToFile(sSaveto,data);
             
            int StatusCode = hc.getStatusCode();
            if (StatusCode == 200)
            {
                if (sSaveto != null && sSaveto.length() != 0 && data == null)
                    //TT#68701
                    writeToFile(sSaveto,data);
                 
                return data;
            }
            else if (StatusCode == 302)
            {
                //sReq = MLSUtil.getSubString(sReq, "?", MLSUtil.STRING_LEFT, false);
                //sReq = sReq.Substring(0, (sReq.LastIndexOf("/")) - (0));
                String sLocation = hc.responsHeader.get_Renamed(HTTP_HEAD_LOCATION);
                //sReq += ("/" + sLocation.Trim());
                sReq = getLocation(StringSupport.Trim(sLocation));
                HttpURLConnectionEx hce = createHcFromReq(sReq);
                return connectHTTP(sSaveto,sReq,hce,isSetAuth);
            }
            else //Removed for TT#117734
            //else if (StatusCode == 482)
            // This is specaial for Portland Metropolitan Area AOR - TT#55455
            //	throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD, STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD));
            if (StatusCode == 403 || StatusCode == 503)
            {
                if (connectHttpDirectEx(sSaveto,sReq) != 200)
                    if (connectHttpDirectEx(sSaveto,sReq) != 200)
                        if (connectHttpDirectEx(sSaveto,sReq) != 200)
                            if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                    if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                        if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                            if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                                if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                                    if (connectHttpDirectEx(sSaveto,sReq) != 200)
                                                        return null;
                                                     
                                                 
                                             
                                         
                                     
                                 
                             
                         
                     
                 
                return null;
            }
            else
                return null;   
        }
        catch (MLSException exc)
        {
            throw exc;
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }
    
    }

    protected public void connectHttpDirect(String saveTo, String sReq) throws Exception {
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
            getEngine().writeLine(e.getMessage());
        }
    
    }

    protected public int connectHttpDirectEx(String saveTo, String sReq) throws Exception {
        debugLog(null,"###################### request string ##################x");
        debugLog(null,sReq);
        debugLog(null,"######################  request end   ###################");
        int status = 0;
        try
        {
            //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
            URI url = new URI(StringSupport.Trim(sReq));
            HttpURLConnection urlc = (HttpURLConnection)url.toURL().openConnection();
            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            byte[] b = new byte[1];
            HttpURLConnection hwr = (HttpURLConnection)urlc;
            status = ((int)(hwr.getResponseCode()));
            //UPGRADE_TODO: Class 'java.io.DataInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioDataInputStream'"
            System.IO.BinaryReader di = new System.IO.BinaryReader(hwr.getInputStream());
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
            getEngine().writeLine(e.getMessage());
        }

        return status;
    }

    protected public void httpMlsExplorer(String sSaveto, String sReq) throws Exception {
        try
        {
            m_cookie = "";
            if (m_hc.getHeaderField(HTTP_HEAD_SETCOOKIE) == null || StringSupport.Trim(m_hc.getHeaderField(HTTP_HEAD_SETCOOKIE)).length() == 0)
            {
                String sbaseCookieSite = StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_BASECOOKSITE));
                if (sbaseCookieSite.length() != 0)
                {
                    // TODO: we actually should not do this, but currently
                    // a lot of def-files use spaces in URL construction. :(
                    // This is something what needs to be fixed.
                    sbaseCookieSite = MLSUtil.replaceSubStr(sbaseCookieSite," ","%20");
                    String sbaseCookieSite2 = removeCgiFromReq(sbaseCookieSite);
                    //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
                    URI tempUrl = new URI(getBaseUrlOnly(sbaseCookieSite2));
                    HttpURLConnectionEx tempHc = new HttpURLConnectionEx(tempUrl);
                    tempHc.setUseRelativePath(m_useRelativePath);
                    connectHTTP("",sbaseCookieSite2,tempHc,false);
                    m_cookie = tempHc.getHeaderField(HTTP_HEAD_SETCOOKIE);
                }
                 
            }
            else
                m_cookie = m_hc.getHeaderField(HTTP_HEAD_SETCOOKIE); 
            setCgiInfo();
            if (m_cookie.length() != 0)
                m_cookie = getCookie(m_cookie);
             
            connectHTTP(sSaveto,sReq,m_hc,true);
            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            if (m_hc.getHeaderField(HTTP_HEAD_LOCATION) != null && StringSupport.Trim(m_hc.getHeaderField(HTTP_HEAD_LOCATION)).length() != 0)
            {
                String sUrl = getLocation(StringSupport.Trim(m_hc.getHeaderField(HTTP_HEAD_LOCATION)));
                HttpURLConnectionEx tempHc2 = createHcFromReq(sUrl);
                connectHTTP(sSaveto,sUrl,tempHc2,true);
            }
             
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }
    
    }

    protected public void httpGetDocument(String sSaveto, String sReq, HttpURLConnectionEx hc) throws Exception {
        String newLocationUrl = getLocation(StringSupport.Trim(hc.getHeaderField(HTTP_HEAD_LOCATION)));
        connectHttpDirect(sSaveto,newLocationUrl);
    }

    protected public void setHttpHeader(HttpURLConnectionEx hc, boolean isSetAuth) throws Exception {
        if (hc != null)
        {
            hc.removeRequestProperty();
            hc.setRequestProperty("Accept","*/*");
            hc.setRequestProperty("User-Agent","Top Producer/1.0");
            hc.setRequestProperty("Pragma","no-cache");
            hc.setRequestProperty("Content-Length","0");
            hc.setRequestProperty("Host",hc.getHost());
            if (!hc.isConnectionClose())
                hc.setRequestProperty("Connection","Keep-Alive");
            else
                hc.setRequestProperty("Connection","Close"); 
            if (m_cookie.length() != 0)
                hc.setRequestProperty("Cookie",m_cookie);
             
            if (m_CGIUsername != null && m_CGIPassword != null && isSetAuth)
                hc.setRequestProperty("Authorization",userNamePasswordBase64(m_CGIUsername,m_CGIPassword));
             
        }
         
    }

    protected public String removeCgiFromReq(String sReq) throws Exception {
        String s = sReq;
        if (s.indexOf("@") != -1)
        {
            s = sReq.Substring(sReq.indexOf("@") + 1, (sReq.Length)-(sReq.indexOf("@") + 1));
            s = HTTP_PROTOCOL + s;
        }
         
        return s;
    }

    protected public void removeLastDelimeter(String fileName, String delimeter) throws Exception {
    }

    protected public String getRecordDelimeter() throws Exception {
        String recordDelimiter = getEngine().getDefParser().getValue(getCommonSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDDELIMITER);
        if (recordDelimiter.length() != 0)
            recordDelimiter = MLSUtil.toStr(recordDelimiter);
         
        return recordDelimiter;
    }

    //UPGRADE_NOTE: Access modifiers of method 'prepareDataFile' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public String prepareDataFile(String filename) throws Exception {
        StringBuilder BeginDataDelimiter = null;
        String EndProcessingString = MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_ENDPROCESSINGSTRING));
        BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING)));
        if (BeginDataDelimiter.length() == 0)
            BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA_ALT,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING)));
         
        if (BeginDataDelimiter.length() == 0)
            BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK)));
         
        String fieldDelimiter = m_def.getValue(getCommonSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_FIELDDELIMITER);
        if (fieldDelimiter.length() != 0)
            fieldDelimiter = MLSUtil.toStr(fieldDelimiter);
        else
            fieldDelimiter = ","; 
        FileStreamSupport stream;
        String file_context = "";
        try
        {
            file_context = MLSUtil.readFile(filename);
        }
        catch (FileNotFoundException fnfe)
        {
            throw getEngine().createException(fnfe,STRINGS.get_Renamed(STRINGS.ERR_DATA_FILE_NOT_FOUND));
        }

        if (file_context.length() > MAX_DOWNLOAD_SIZE)
            throw getEngine().createException(MLSException.CODE_EXCEED_MAX_DOWNLOAD_SIZE,STRINGS.get_Renamed(STRINGS.ERR_EXCEED_MAX_DOWNLOAD_SIZE));
         
        int i;
        if ((i = file_context.indexOf("<title>")) != -1)
        {
            String error = file_context.substring(i + new StringBuilder("<title>").toString().length(), (i + new StringBuilder("<title>").toString().length()) + ((file_context.toString().indexOf("</title>")) - (i + new StringBuilder("<title>").toString().length())));
            String fileMsg = file_context.replace("<hr>", "<hr><br>");
            if (fileMsg.length() > 1024)
                fileMsg = fileMsg.substring(0, (0) + ((1024) - (0)));
             
            throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg,MLSException.FORMAT_HTML);
        }
         
        if ((i = file_context.indexOf(fieldDelimiter)) == -1)
        {
            if (file_context.length() > 1024)
                file_context = file_context.substring(0, (0) + ((1024) - (0)));
             
            throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + file_context,MLSException.FORMAT_HTML);
        }
         
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        i = SupportClass.getIndex(file_context,BeginDataDelimiter.toString(),0);
        if (i == -1)
        {
            String fileMsg = StringSupport.Trim(file_context);
            if (fileMsg.length() != 0)
            {
                fileMsg = fileMsg.replace("\r\n", "<br>");
                if (fileMsg.length() > 1024)
                    fileMsg = fileMsg.substring(0, (0) + ((1024) - (0)));
                 
                throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg,MLSException.FORMAT_HTML);
            }
            else
                return ""; 
        }
        else
        {
            String data = file_context.Substring(i + BeginDataDelimiter.toString().length(), (file_context.Length)-(i + BeginDataDelimiter.toString().length()));
            if (data.length() < 2000000 && StringSupport.Trim(data).length() == 0)
                return "";
            else
            {
                // If data is larger than 4MB, trim string will cause TCSExecutive crash.
                if (EndProcessingString.length() != 0 && (i = data.lastIndexOf(EndProcessingString)) != -1)
                {
                    data = data.Substring(0, (i)-(0));
                }
                 
                return data;
            } 
        } 
    }

    protected public void printHeadInfo(HttpURLConnectionEx hc) throws Exception {
        if (hc != null && m_debug)
        {
            getEngine().writeLine("<------begin http response header info-----");
            for (int i = 0;i < hc.getResponseHeaderCount();i++)
                getEngine().writeLine(hc.getHeaderFieldKey(i));
            getEngine().writeLine("------end of http response header info----->");
        }
         
    }

    protected public HttpURLConnectionEx createHcFromReq(String sReq) throws Exception {
        try
        {
            // TODO: we actually should not do this, but currently
            // a lot of def-files use spaces in URL construction. :(
            // This is something what needs to be fixed.
            sReq = MLSUtil.replaceSubStr(sReq," ","%20");
            String request2 = removeCgiFromReq(sReq);
            //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
            URI url = new URI(getBaseUrlOnly(request2));
            HttpURLConnectionEx hc = new HttpURLConnectionEx(url);
            hc.setUseRelativePath(m_useRelativePath);
            return hc;
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }
    
    }

    protected public void connectFTP(String sSaveTo, String sReq) throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        try
        {
            //bool retry = false;
            getEngine().writeLine("Get: " + sReq);
            sReq = removeCgiFromReq(sReq);
            int i = sReq.indexOf(FTP_PROTOCOL);
            i += new StringBuilder(FTP_PROTOCOL).toString().length();
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            String baseURL = sReq.substring(i, (i) + ((SupportClass.getIndex(sReq,"/",i)) - (i)));
            String port = "";
            if (baseURL.indexOf(":") > -1)
            {
                port = baseURL.Substring(baseURL.indexOf(":") + 1, (baseURL.Length)-(baseURL.indexOf(":") + 1));
                baseURL = baseURL.substring(0, (0) + ((baseURL.indexOf(":")) - (0)));
            }
             
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            sReq = sReq.Substring(SupportClass.getIndex(sReq,"/",i), (sReq.Length)-(SupportClass.getIndex(sReq,"/",i)));
            String working_dir = sReq.toString().substring(0, (0) + ((sReq.toString().lastIndexOf("/")) - (0)));
            String file_name = sReq.toString().substring(sReq.toString().lastIndexOf("/") + 1, (sReq.toString().lastIndexOf("/") + 1) + ((sReq.toString().length()) - (sReq.toString().lastIndexOf("/") + 1)));
            String picFullName = sSaveTo;
            if (m_ftpClient == null)
            {
                //if (port.Length > 0)
                //    m_ftpClient.connect(baseURL, System.Int32.Parse(port));
                //else
                //    m_ftpClient.connect(baseURL);
                String ftp_user_name = "";
                String ftp_user_pwd = "";
                BoardSetupField f = getEngine().getBoardSetup().getSecField("FTPNAME");
                if (f != null)
                    ftp_user_name = f.getValue();
                 
                f = getEngine().getBoardSetup().getSecField("FTPPWD");
                if (f != null)
                    ftp_user_pwd = f.getValue();
                 
                if (ftp_user_name.length() == 0)
                    ftp_user_name = "anonymous";
                 
                if (ftp_user_pwd.length() == 0)
                    ftp_user_pwd = "anonymous";
                 
                m_ftpClient = new FTP();
                m_ftpClient.connect(baseURL,ftp_user_name,ftp_user_pwd);
                m_ftpClient.changeDir(working_dir);
            }
             
            try
            {
                m_ftpClient.downloadFile(sReq,sSaveTo);
            }
            catch (IOException e)
            {
                getEngine().writeLine(e.getMessage());
            }

            //m_ftpClient.changeWorkingDirectory(working_dir);
            //getEngine().WriteLine("Connect to FTP server= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
            //long time3 = (System.DateTime.Now.Ticks - 621355968000000000) / 10000;
            //FtpBinaryReader in_Renamed = m_ftpClient.retrieve(file_name);
            //byte[] buffer = new byte[1024];
            //ByteBuffer readBuffer = new ByteBuffer();
            //int count = 0;
            //while ((count = SupportClass.ReadInput(in_Renamed.BaseStream, buffer, 0, buffer.Length)) != - 1)
            //{
            //    for (i = 0; i < count; i++)
            //        readBuffer.append(buffer[i]);
            //}
            //getEngine().WriteLine("Recieving Time = " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time3));
            //long time2 = (System.DateTime.Now.Ticks - 621355968000000000) / 10000;
            ////UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
            //System.IO.FileStream fo = new System.IO.FileStream(picFullName, System.IO.FileMode.Create);
            //SupportClass.WriteOutput(fo, readBuffer.get_Renamed());
            //fo.Close();
            //getEngine().WriteLine("Wrote to File Time = " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time2));
            //long time4 = (System.DateTime.Now.Ticks - 621355968000000000) / 10000;
            ////UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
            //in_Renamed.Close();
            //getEngine().WriteLine("Close Data socket = " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time4));
            getEngine().writeLine("Download picture through FTP clinet Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            getEngine().writeLine(exc.getMessage());
        }
    
    }

    //if (retry)
    //	throw getEngine().createException(60060);
    private String getLocation(String locationHeader) throws Exception {
        locationHeader = StringSupport.Trim(locationHeader);
        if (locationHeader.startsWith("/"))
        {
            // is a relative path
            if (m_siteIPAddress.indexOf("@") != -1)
            {
                String s = HTTP_PROTOCOL + m_siteIPAddress.substring(m_siteIPAddress.indexOf("@") + 1);
                s += locationHeader;
                return s;
            }
            else
                return m_siteIPAddress + locationHeader; 
        }
        else if (locationHeader.startsWith(".."))
        {
            int last = m_connString.toString().lastIndexOf("/");
            String parent = m_connString.toString().Substring(0, (last)-(0));
            last = parent.lastIndexOf("/");
            parent = parent.Substring(0, (last)-(0));
            locationHeader = locationHeader.substring(locationHeader.indexOf("/"));
            return parent + locationHeader;
        }
        else
        {
            try
            {
                //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
                URI url = new URI(locationHeader);
            }
            catch (Exception e)
            {
                String parent = "";
                int last = m_connString.toString().lastIndexOf("/");
                if (last != -1)
                    parent = m_connString.toString().Substring(0, (last)-(0));
                 
                return parent + "/" + locationHeader;
            }

            return locationHeader;
        }  
    }

    static protected public String getCookie(String cookie) throws Exception {
        if (StringSupport.Trim(cookie).length() == 0)
            return "";
         
        String str = StringSupport.Trim(cookie);
        int index = cookie.indexOf(";");
        if (index != -1)
            str = cookie.Substring(0, (index)-(0)).Trim();
         
        return str;
    }

    static protected public void writeToFile(String sSaveto, byte[] data) throws Exception {
        FileStreamSupport fo = null;
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
            fo = new FileStreamSupport(sSaveto, FileMode.Create);
            int i = data.length - 1;
            while (data[i] == 0)
            --i;
            fo.write(data,0,i + 1);
        }
        catch (Exception e)
        {
            SupportClass.WriteStackTrace(e, Console.Error);
        }
        finally
        {
            try
            {
                if (fo != null)
                    fo.close();
                 
            }
            catch (Exception e1)
            {
                    ;
            }
        
        }
    }

    static protected public String userNamePasswordBase64(String username, String password) throws Exception {
        return "Basic " + MLSUtil.base64Encode(username + ":" + password);
    }

    public String getBaseUrlOnly(String sUrl) throws Exception {
        String s = "";
        if (sUrl.indexOf("@") != -1)
        {
            s = sUrl.Substring(sUrl.indexOf("@") + 1, (sUrl.Length)-(sUrl.indexOf("@") + 1));
            s = s.substring(0, (0) + ((s.indexOf("/")) - (0)));
            s = HTTP_PROTOCOL + s;
        }
        else
        {
            s = sUrl.toString();
            if (sUrl.indexOf(HTTP_PROTOCOL) == -1)
                s = s.substring(0, (0) + ((s.indexOf("/")) - (0)));
            else
            {
                int i = sUrl.indexOf(HTTP_PROTOCOL);
                i += new StringBuilder(HTTP_PROTOCOL).toString().length();
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                if (SupportClass.getIndex(sUrl,"/",i + 1) != -1)
                {
                    // http://www.test.com/folder
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    s = sUrl.substring(i, (i) + ((SupportClass.getIndex(sUrl,"/",i)) - (i)));
                    s = HTTP_PROTOCOL + s;
                }
                else
                    s = sUrl; 
            } 
        } 
        return StringSupport.Trim(s);
    }

    // http://www.test.com
    public String getBaseURLFull(String sUrl) throws Exception {
        try
        {
            int i = sUrl.indexOf(HTTP_PROTOCOL);
            if (i == -1)
                return sUrl;
            else
            {
                String s = "";
                i += new StringBuilder(HTTP_PROTOCOL).toString().length();
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                s = sUrl.substring(i, (i) + ((SupportClass.getIndex(sUrl,"/",i)) - (i)));
                s = HTTP_PROTOCOL + s;
                return StringSupport.Trim(s);
            } 
        }
        catch (Exception e)
        {
            throw getEngine().createException(e,STRINGS.get_Renamed(STRINGS.ERR_ERROR_WRONGURL));
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

    protected public void setCgiInfo() throws Exception {
        BoardSetup setup = getEngine().getBoardSetup();
        if (m_CGIUsername == null || StringSupport.Trim(m_CGIUsername).length() == 0)
        {
            m_CGIUsername = setup.getSecField(0).getValue();
            m_CGIPassword = setup.getSecField(1).getValue();
        }
         
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
                getEngine().writeLine(log);
             
        }
         
    }

    protected public String getSafeUrl(String source) throws Exception {
        if (source != null && source.length() != 0)
        {
            String result = "";
            SupportClass.Tokenizer st = new SupportClass.Tokenizer(source,"\r");
            while (st.hasMoreTokens())
            {
                String token = (String)st.nextToken();
                token = token.replace('\r', ' ');
                token = token.replace('\n', ' ');
                token = StringSupport.Trim(token);
                if (token.length() != 0)
                    result += token;
                 
            }
            if (result != null)
                source = result;
             
        }
         
        return source;
    }

    private void downloadPicZIP(String picFilePath, String sUrl) throws Exception {
        try
        {
            sUrl += getCurrentRecordId();
            String basePicSite = StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_BASEPICSITE));
            HttpURLConnectionEx hc = createHcFromReq(sUrl);
            byte[] target = connectHTTP(null,sUrl,hc,true);
            if (target == null)
                return ;
             
            String sTarget = new String(target, EncodingSupport.GetEncoder("ASCII").getString());
            int end_index = -1;
            if ((end_index = sTarget.indexOf(".zip")) != -1)
            {
                sTarget = sTarget.substring(sTarget.indexOf("\n") + 1);
                sTarget = StringSupport.Trim((sTarget.replace('\"', ' ')));
                String picUrl = basePicSite + sTarget;
                //MLSUtil.unzipFromServer(picUrl, getEngine().getResultsFolder(), getCurrentRecordId() + ".jpg");
                connectHttpDirect(getEngine().getResultsFolder() + getCurrentRecordId() + ".zip",picUrl);
                MLSUtil.extractTo(getEngine().getResultsFolder() + getCurrentRecordId() + ".zip",getEngine().getResultsFolder());
            }
             
        }
        catch (Exception e)
        {
                ;
        }
    
    }

    private void downloadPic6(String picFilePath, String sUrl) throws Exception {
        try
        {
            HttpURLConnectionEx hc = createHcFromReq(sUrl);
            byte[] target = connectHTTP(null,sUrl,hc,true);
            if (target == null)
                return ;
             
            String sTarget = new String(target, EncodingSupport.GetEncoder("ASCII").getString());
            int end_index = -1;
            if ((end_index = sTarget.indexOf("END OF DATA")) != -1)
            {
                sTarget = getSafeUrl(sTarget.Substring(0, (end_index)-(0)));
                if (sTarget != null && StringSupport.Trim(sTarget).length() != 0 && !StringSupport.Trim(sTarget).startsWith("\\"))
                {
                    m_CGIUsername = getCGIUserInfo(new StringBuilder(sTarget),true);
                    m_CGIPassword = getCGIUserInfo(new StringBuilder(sTarget),false);
                    if (!getEngine().getMulitiPictures().isGetMultiPicture())
                        fileDownload(picFilePath,sTarget);
                    else
                        multiPicFileDownload(getCurrentRecordId(),sTarget); 
                }
                 
            }
             
        }
        catch (Exception e)
        {
                ;
        }
    
    }

    protected public void multiPicFileDownload(String recordId, String url) throws Exception {
        String maxPics = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_MPICMAX);
        if (maxPics == null || maxPics.length() == 0)
            maxPics = Integer.toString(MAX_PICTURES);
         
        int iMaxPics = Integer.valueOf(maxPics);
        String[] arrFiles = new String[iMaxPics];
        getEngine().getMulitiPictures().clearPictureData();
        for (int i = 0;i <= iMaxPics;i++)
        {
            String[] urlAndFilePath = getEngine().getMulitiPictures().getURLAndFilePath(url,getEngine().getResultsFolder() + recordId,i);
            boolean tmpBool = true;
            if (i < iMaxPics)
            {
                if (isDownPicFlag3())
                    urlAndFilePath[1] = urlAndFilePath[0];
                else
                    downloadMultiPics(urlAndFilePath[1],urlAndFilePath[0]); 
                if (File.Exists((new File(urlAndFilePath[1])).FullName))
                    tmpBool = true;
                else
                    tmpBool = File.Exists((new File(urlAndFilePath[1])).FullName); 
            }
             
            if (i < iMaxPics && tmpBool && getEngine().getEnvironment().isPictureFileSupported("file:/" + urlAndFilePath[1]))
            {
                if (getEngine().getMulitiPictures().isComparePictures())
                {
                    getEngine().getMulitiPictures().setCurrentPicture(getEngine().getEnvironment().getPictureData());
                    if (getEngine().getMulitiPictures().isSamePicture() && i > 0)
                    {
                        String[] arrDes = new String[i];
                        Array.Copy(arrFiles, 0, arrDes, 0, i);
                        MLSRecord record = getEngine().getMLSRecord(recordId);
                        record.setMultiPictureFileName(arrDes);
                        break;
                    }
                     
                }
                 
                arrFiles[i] = urlAndFilePath[1];
            }
            else
            {
                if (i > 0)
                {
                    String[] arrDes = new String[i];
                    Array.Copy(arrFiles, 0, arrDes, 0, i);
                    MLSRecord record = getEngine().getMLSRecord(recordId);
                    record.setMultiPictureFileName(arrDes);
                }
                 
                break;
            } 
        }
    }

    protected public void downloadMultiPics(String filePath, String url) throws Exception {
        fileDownload(filePath,url);
    }

    private void addNotesToRecords() throws Exception {
        if (!(isMultiConnection() && getRecordCount() != 0))
            return ;
         
        String sNotes = readNotesFromFile();
        if (sNotes.length() == 0)
            return ;
         
        MLSCmaNotes cmaNotes = new MLSCmaNotes(m_def);
        Environment environment = getEngine().getEnvironment();
        environment.checkStop();
        MLSRecord rec = getFirstRecord();
        while (rec != null)
        {
            String id = rec.getRecordID();
            SupportClass.Tokenizer stNotes = new SupportClass.Tokenizer(sNotes,"\n");
            String note = "";
            environment.checkStop();
            while (stNotes.hasMoreTokens())
            {
                String item = StringSupport.Trim(((String)stNotes.nextToken()));
                if (item.indexOf(id) != -1)
                {
                    String _note = cmaNotes.getFormatedNote(item) + " ";
                    note += _note;
                }
                 
            }
            rec.setStdFieldValue(TCSStandardResultFields.STDF_NOTES,note);
            rec = getNextRecord();
        }
    }

    private String readNotesFromFile() throws Exception {
        String content = "";
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            content = MLSUtil.readFile(getEngine().getResultsFolder() + FILENAME_NOTES);
        }
        catch (FileNotFoundException fnfe)
        {
            return null;
        }

        return content;
    }

    protected public String getRecordIDList() throws Exception {
        MLSRecords records = getEngine().getMLSRecords(MLSRecords.FILTER_ALL,null);
        String list = "";
        if (records.size() == 0)
            return list;
         
        MLSRecord rec = records.getFirstRecord();
        while (rec != null)
        {
            list += (rec.getRecordID() + ",");
            rec = records.getNextRecord();
        }
        return StringSupport.Trim(list).substring(0, (0) + ((StringSupport.Trim(list).length() - 1) - (0)));
    }

    protected public boolean isMultiConnection() throws Exception {
        return StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_MULTICONN)).toUpperCase().equals("1".toUpperCase());
    }

    //gte notes
    private boolean isDownPicFlag1() throws Exception {
        return StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICDWNFLAG)).toUpperCase().equals("1".toUpperCase());
    }

    //CTI / Navigator 2000 pic
    private boolean isDownPicFlag3() throws Exception {
        return StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICDWNFLAG)).toUpperCase().equals("3".toUpperCase());
    }

    //homeseekers pic
    private boolean isDownPicFlag6() throws Exception {
        return StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICDWNFLAG)).toUpperCase().equals("6".toUpperCase());
    }

    // Hawaii res
    private boolean isDownPicFlag5() throws Exception {
        return StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICDWNFLAG)).toUpperCase().equals("5".toUpperCase());
    }

    // Portland Metropolitan Area AOR
    private boolean isDownPicFlag14() throws Exception {
        return StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICDWNFLAG)).toUpperCase().equals("14".toUpperCase());
    }

    //TREB
    private String getDownPicFlag1Url(String source) throws Exception {
        //CTI / Navigator 2000 pic
        String id = getCurrentRecordId();
        if (id.length() < 2)
            return source;
         
        String part1 = id.substring(id.length() - 2, (id.length() - 2) + ((id.length() - 1) - (id.length() - 2)));
        String part2 = id.substring(id.length() - 1);
        String part3 = id + "a.jpg";
        return source + part1 + "/" + part2 + "/" + part3;
    }

    private String getDownPicFlag6Url(String source) throws Exception {
        try
        {
            // Hawaii res
            String sBaseImageURL = source.substring(0, (0) + ((source.lastIndexOf("/") + 1) - (0)));
            String sExtend = StringSupport.Trim(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICFILESEXT));
            if (sExtend == null || sExtend.length() == 0)
                sExtend = DEFAULT_PICTURE_EXTENSION;
             
            String id = getCurrentRecordId();
            while (id.length() < 5)
            id = "0" + id;
            String sLastTwoDigits = id.substring(id.length() - 2);
            String sImageRequest = sBaseImageURL + sLastTwoDigits.substring(0, (0) + ((1) - (0))) + "/" + sLastTwoDigits + "/" + id + "301." + sExtend;
            return sImageRequest;
        }
        catch (Exception e)
        {
            return null;
        }
    
    }

    private String getDownPicFlag5Url(String source) throws Exception {
        // Portland Metropolitan Area AOR
        String lowDir = "";
        //Lowest Directory
        String midDir = "";
        //Middle Directory
        String farLeftDir = "";
        //Highest Dirctory
        String result = "";
        try
        {
            String sBaseImageURL = source.substring(0, (0) + ((source.lastIndexOf("/") + 1) - (0)));
            String id = getCurrentRecordId();
            if (id.length() < 2)
                return "";
             
            if (id.length() < 5)
                while (id.length() < 7)
                //Make the MLS# 7 Digits in Length
                id = "0" + id;
             
            int idl = id.length();
            if (id.substring(idl - 4, (idl - 4) + ((idl - 3) - (idl - 4))).length() > 0)
            {
                lowDir = id.substring(idl - 4, (idl - 4) + ((idl - 3) - (idl - 4))) + "000";
                if (id.substring(idl - 5, (idl - 5) + ((idl - 4) - (idl - 5))).length() > 0)
                {
                    midDir = id.substring(idl - 5, (idl - 5) + ((idl - 4) - (idl - 5))) + "0000";
                    if (id.substring(0, (0) + ((idl - 5) - (0))).length() > 0)
                        farLeftDir = id.substring(0, (0) + ((idl - 5) - (0))) + "00000";
                     
                }
                 
            }
             
            while (farLeftDir.length() < 8)
            farLeftDir = "0" + farLeftDir;
            while (id.startsWith("0"))
            id = id.Substring(1, (id.Length)-(1));
            result = sBaseImageURL + farLeftDir + "/" + midDir + "/" + lowDir + "/" + id + "-1.jpg";
            return result;
        }
        catch (Exception e)
        {
            SupportClass.WriteStackTrace(e, Console.Error);
            return "";
        }
    
    }

    private String getDownPicFlag14Url(String source) throws Exception {
        // TREB
        String id = getCurrentRecordId();
        if (id.length() == 0)
            return source;
         
        int index = 0;
        if ((index = source.lastIndexOf("/")) != -1)
            source = source.substring(0, (0) + ((index + 1) - (0)));
         
        String last3digit = id.Substring(id.length() - 3, (id.Length)-(id.length() - 3));
        String filename = id + ".jpg";
        return source + last3digit + "/" + filename;
    }

    private String createIDListFile() throws Exception {
        try
        {
            String fileFullName = getEngine().getResultsFolder() + FILENAME_MLSNUM;
            File f = new File(fileFullName);
            boolean tmpBool;
            if (File.Exists(f.FullName))
                tmpBool = true;
            else
                tmpBool = File.Exists(f.FullName); 
            if (tmpBool)
            {
                boolean tmpBool2;
                if (File.Exists(f.FullName))
                {
                    File.Delete(f.FullName);
                    tmpBool2 = true;
                }
                else if (File.Exists(f.FullName))
                {
                    File.Delete(f.FullName);
                    tmpBool2 = true;
                }
                else
                    tmpBool2 = false;  
                boolean generatedAux = tmpBool2;
            }
             
            //UPGRADE_TODO: Class 'java.io.FileWriter' was converted to 'System.IO.StreamWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileWriter'"
            //UPGRADE_TODO: Constructor 'java.io.FileWriter.FileWriter' was converted to 'System.IO.StreamWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileWriterFileWriter_javalangString_boolean'"
            PrintWriter writer = new PrintWriter(fileFullName, true, Encoding.Default);
            MLSRecords records = getEngine().getMLSRecords(MLSRecords.FILTER_FLAG_CHECKED | MLSRecords.FILTER_FLAG_ANY_TYPE | MLSRecords.FILTER_FLAG_MISSING_PICTURES,null);
            MLSRecord rec = records.getFirstRecord();
            while (rec != null)
            {
                String id = rec.getRecordID();
                writer.print(id + "\r\n");
                rec = records.getNextRecord();
            }
            writer.Flush();
            writer.close();
            return fileFullName;
        }
        catch (Exception e)
        {
            SupportClass.WriteStackTrace(e, Console.Error);
            return null;
        }
    
    }

    private boolean updateClient() throws Exception {
        String helper_zip = m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TPONLINE,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HELPER_ZIP);
        if (helper_zip.length() == 0)
            return true;
         
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        if (engine.isTCSRequest())
            return true;
         
        engine.getEnvironment().setMessage(STRINGS.get_Renamed(STRINGS.S28));
        String dir = engine.install(helper_zip);
        return true;
    }

}


