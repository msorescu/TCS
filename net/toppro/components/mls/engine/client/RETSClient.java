//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:38 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import net.toppro.components.mls.engine.client.Digest;
import net.toppro.components.mls.engine.client.HttpClient;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.RETSURLConnection;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSUtil;

public class RETSClient  extends HttpClient 
{
    protected public static final String USER_AGENT = "Top Producer/1.0";
    protected public static final String UA_USER_PASSWORD = "";
    //"Richard";
    protected public static final String UA_USER_AGENT = "";
    //"Top Producer";
    protected public static final String RETS_VERSION = "RETS/1.0";
    protected public static final String HTTP_VERSION = "1.0";
    protected public static final String RECORD_COUNT = "&count=2";
    protected public static final String RETS_STATUS = "rets-status";
    protected public String m_sRequestURL;
    protected public String m_sURI;
    private String m_sMemberName;
    private String m_sUser;
    private String m_sBroker;
    private String m_sMetadataVersion;
    private String m_sMinMetadataVersion;
    private String m_sTimeoutSeconds;
    private String m_sLoginComplete;
    private String m_sUpdate;
    protected public String m_sBaseURL;
    protected public String m_sLoginURL;
    protected public String m_sLogoutURL;
    protected public String m_sActionURL;
    protected public String m_sMetaDataURL;
    protected public String m_sSearchURL;
    protected public String m_sGetObjectURL;
    private String m_sChangePasswdURL;
    protected public String m_sUsername;
    protected public String m_sPassword;
    protected public String m_sSearch;
    protected public String m_RetsUAPwd;
    protected public String m_RetsUAUserAgent;
    protected public String m_HttpUserAgent;
    protected public String m_HttpVersion;
    protected public String m_RetsVersion;
    protected public String m_AcceptType;
    protected public String m_ConnectionType;
    protected public String m_HttpMethod;
    protected public ArrayList m_vCookies;
    protected public RETSURLConnection m_retsConn;
    protected public boolean m_bCheckMetaDataVersion;
    protected boolean m_isNonceExpiredError = false;
    protected boolean m_searchSuccessOnce = false;
    private boolean m_isCheckAgentSearch = false;
    protected public String m_ContentTypeBoundry;
    protected boolean getIsCheckAgentSearch() throws Exception {
        return m_isCheckAgentSearch;
    }

    protected void setIsCheckAgentSearch(boolean value) throws Exception {
        m_isCheckAgentSearch = value;
    }

    protected public Hashtable m_SearchURLs = Hashtable.Synchronized(new Hashtable());
    public RETSClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
        m_bCheckMetaDataVersion = false;
        m_def = _Engine.getDefParser();
        m_HttpUserAgent = _Engine.getUserAgent();
        if (StringSupport.isNullOrEmpty(m_HttpUserAgent))
            m_HttpUserAgent = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HTTPAGENT);
         
        if (m_HttpUserAgent.length() == 0)
            m_HttpUserAgent = USER_AGENT;
         
        m_RetsUAUserAgent = _Engine.getUserAgent();
        if (StringSupport.isNullOrEmpty(m_RetsUAUserAgent))
            m_RetsUAUserAgent = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RETSUAAGENT);
         
        if (m_RetsUAUserAgent.length() == 0)
            m_RetsUAUserAgent = UA_USER_AGENT;
         
        m_RetsUAPwd = _Engine.getRETSUAPwd();
        if (StringSupport.isNullOrEmpty(m_RetsUAPwd))
        {
            m_RetsUAPwd = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RETSUAPWD);
            if (m_RetsUAPwd.length() != 0)
            {
                StringBuilder sb = new StringBuilder(m_RetsUAPwd);
                m_RetsUAPwd = SupportClass.reverseString(sb).toString();
                //m_RetsUAPwd = m_RetsUAPwd + "=";
                m_RetsUAPwd = MLSUtil.base64Decode(m_RetsUAPwd);
            }
             
        }
         
        if (m_RetsUAPwd.length() == 0)
            m_RetsUAPwd = UA_USER_PASSWORD;
         
        m_HttpVersion = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HTTPVERSION);
        if (m_HttpVersion.length() == 0)
            m_HttpVersion = HTTP_VERSION;
         
        m_RetsVersion = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RETSVERSION);
        if (m_RetsVersion.length() == 0)
            m_RetsVersion = RETS_VERSION;
         
        m_HttpMethod = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HTTPMETHOD);
        if (m_HttpMethod.length() == 0)
            m_HttpMethod = RETSURLConnection.HTTP_METHOD_GET;
        else
            m_HttpMethod = StringSupport.Trim(m_HttpMethod).toUpperCase(); 
        m_AcceptType = "*/*";
        m_ConnectionType = _Engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HTTPCONNECTION);
        if (StringSupport.isNullOrEmpty(m_ConnectionType))
            m_ConnectionType = "keep-alive";
         
    }

    protected public void runScript() throws Exception {
        if (getCurrentScriptType() != SCRIPT_PIC && getCurrentScriptType() != SCRIPT_EXTRAVAR)
        {
            super.runScript();
            return ;
        }
         
        m_SearchURLs.Clear();
        MLSRecord rec = getFirstRecord();
        MLSException exc = null;
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

            rec = getNextRecord();
        }
        if (exc != null)
            throw exc;
         
        if (getCurrentScriptType() == SCRIPT_PIC)
            startRETSPicDownloading();
        else if (getCurrentScriptType() == SCRIPT_EXTRAVAR)
        {
            startRETSExtraVarDownloading();
            addVarsToRecords();
        }
          
    }

    private void addVarsToRecords() throws Exception {
        if (!(getRecordCount() != 0))
            return ;
         
        Environment environment = getEngine().getEnvironment();
        environment.checkStop();
        MLSRecord rec = getFirstRecord();
        while (rec != null)
        {
            String id = rec.getRecordID();
            String filePath = getEngine().getResultsFolder() + id + ".dat";
            if ((new File(filePath)).exists())
            {
                environment.checkStop();
                if (getEngine().getSectionExtraVarScript().indexOf(',') == -1)
                {
                    String file_context = MLSUtil.readFile(filePath);
                    MLSCmaFields fields = getEngine().getCmaFields();
                    net.toppro.components.mls.engine.CmaField field = fields.getFieldByRecordPosition(String.format(StringSupport.CSFmtStrToJFmtStr("$"),getEngine().getSectionExtraVarScript()));
                    String fieldValue = (field.getStdId() == -1) ? rec.getFieldValue(field.name) : rec.getStdFieldValue(field.getStdId());
                    String StartProcessingString = MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA_EXTRA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING));
                    String EndProcessingString = MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA_EXTRA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_ENDPROCESSINGSTRING));
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    int i = SupportClass.getIndex(file_context,StartProcessingString,0);
                    int j = SupportClass.getIndex(file_context,EndProcessingString,0);
                    if (i == -1 || j == -1)
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
                            return ; 
                    }
                    else
                    {
                        boolean bFoundString = false;
                        SupportClass.Tokenizer stVars = new SupportClass.Tokenizer(file_context,MLSUtil.toStr(m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA_EXTRA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK)));
                        while (stVars.hasMoreTokens())
                        {
                            String item = StringSupport.Trim(((String)stVars.nextToken()));
                            if (item.indexOf(StartProcessingString) != -1)
                            {
                                bFoundString = true;
                                continue;
                            }
                             
                            if (item.indexOf(EndProcessingString) != -1 && bFoundString)
                            {
                                bFoundString = false;
                                break;
                            }
                             
                            if (bFoundString)
                            {
                                fieldValue += " " + StringSupport.Trim(item);
                            }
                             
                        }
                    } 
                    if (field.getStdId() == -1)
                        rec.setFieldValue(field.name,fieldValue);
                    else
                        rec.setStdFieldValue(field.getStdId(),fieldValue); 
                }
                else
                    getEngine().getCommunicationClient().prepareAdditionalMlsRecord(rec,filePath); 
            }
             
            rec = getNextRecord();
        }
    }

    protected public void startRETSExtraVarDownloading() throws Exception {
        MLSException exc = null;
        String exRemPath = getEngine().getResultsFolder();
        if (getCurrentScriptType() == SCRIPT_EXTRAVAR)
        {
            if (retsLogin())
            {
                if (getEngine().isCheckCredential())
                {
                    retsLogout();
                    return ;
                }
                 
                if (isActionUrl())
                {
                    if (!retsAction())
                    {
                        retsLogout();
                        return ;
                    }
                     
                }
                 
                try
                {
                    String RecId = "";
                    String ReqURL = "";
                    IEnumerator en = EnumeratorSupport.mk(m_SearchURLs.keySet().iterator());
                    int TotalExRemarks = m_SearchURLs.size();
                    int CountExRemarks = 0;
                    //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
                    int count = 0;
                    while (en.moveNext())
                    {
                        getEngine().getEnvironment().checkStop();
                        //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
                        RecId = ((String)en.getCurrent());
                        ReqURL = ((String)m_SearchURLs.get(RecId));
                        int retryCount = 0;
                        LableRetry:if (retryCount > 0)
                            getEngine().writeLine("Download Extra Remarks Failed. Retry " + retryCount + ": URL=" + ReqURL + RecId,true);
                         
                        try
                        {
                            if (ReqURL.indexOf("Query=") != -1)
                                retsSearch(ReqURL,exRemPath + RecId + ".dat");
                            else
                                retsGetObject(ReqURL,exRemPath + RecId + ".dat"); 
                            getEngine().setProgress(++CountExRemarks,TotalExRemarks);
                        }
                        catch (MLSException mls_ex)
                        {
                            retryCount++;
                            if (((MLSException)mls_ex).getCode() != MLSException.CODE_NO_RECORD_FOUND)
                            {
                                if (retryCount > 0 && retryCount < 4)
                                {
                                    retsLogout();
                                    objectLoginRetry();
                                    goto LableRetry
                                }
                                 
                            }
                             
                        }

                        count++;
                    }
                }
                catch (MLSException mls_ex)
                {
                    exc = mls_ex;
                }

                retsLogout();
            }
             
        }
         
        //if( retsLogin() )
        if (exc != null)
            throw exc;
         
    }

    protected public void startRETSPicDownloading() throws Exception {
        MLSException exc = null;
        String picPath = getEngine().getResultsFolder();
        if (getCurrentScriptType() == SCRIPT_PIC)
        {
            if (retsLogin())
            {
                if (getEngine().isCheckCredential())
                {
                    retsLogout();
                    return ;
                }
                 
                if (isActionUrl())
                {
                    if (!retsAction())
                    {
                        retsLogout();
                        return ;
                    }
                     
                }
                 
                try
                {
                    String RecId = "";
                    String ReqURL = "";
                    IEnumerator en = EnumeratorSupport.mk(m_SearchURLs.keySet().iterator());
                    int TotalPics = m_SearchURLs.size();
                    int CountPics = 0;
                    //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
                    int count = 0;
                    while (en.moveNext())
                    {
                        getEngine().getEnvironment().checkStop();
                        //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
                        RecId = ((String)en.getCurrent());
                        ReqURL = ((String)m_SearchURLs.get(RecId));
                        int retryCount = 0;
                        LableRetry:if (retryCount > 0)
                            getEngine().writeLine("Download Picture Failed. Retry " + retryCount + ": URL=" + ReqURL + RecId,true);
                         
                        try
                        {
                            if (!getEngine().getMulitiPictures().isGetMultiPicture())
                                retsGetObject(ReqURL,picPath + RecId + ".jpg");
                            else
                                multiPicFileDownload(RecId,ReqURL); 
                            getEngine().setProgress(++CountPics,TotalPics);
                        }
                        catch (MLSException mls_ex)
                        {
                            retryCount++;
                            if (((MLSException)mls_ex).getCode() != MLSException.CODE_NO_RECORD_FOUND)
                            {
                                if (retryCount > 0 && retryCount < 4)
                                {
                                    retsLogout();
                                    objectLoginRetry();
                                    goto LableRetry
                                }
                                 
                            }
                             
                        }

                        count++;
                    }
                }
                catch (MLSException mls_ex)
                {
                    exc = mls_ex;
                }

                retsLogout();
            }
             
        }
         
        //if( retsLogin() )
        if (exc != null)
            throw exc;
         
    }

    private void objectLoginRetry() throws Exception {
        int retrycount = 0;
        LabelRetry:if (retrycount >= 4)
        {
            return ;
        }
         
        retrycount++;
        try
        {
            if (retsLogin())
            {
                if (isActionUrl())
                {
                    if (!retsAction())
                    {
                        retsLogout();
                    }
                     
                }
                 
            }
             
        }
        catch (Exception exce)
        {
            getEngine().writeLine("Login Failed. Retry " + retrycount + ":" + exce.getMessage(),true);
            goto LabelRetry
        }
    
    }

    protected public void multiPicFileDownload(String recordId, String url) throws Exception {
        //Todo: Get flag from DEF file
        //bool supportGetAllPicsInOneRequest = true;//true;
        if (!getEngine().isGetAllPicsInOneRequest() || url.IndexOf("Location=1", StringComparison.CurrentCultureIgnoreCase) > -1)
        {
            super.multiPicFileDownload(recordId,url);
            return ;
        }
         
        //System.String maxPics = getEngine().getDefParser().getValue(MLSEngine.SECTION_PICTURES, MLSEngine.ATTRIBUTE_MPICMAX);
        //if (maxPics == null || maxPics.Length == 0)
        //    maxPics = System.Convert.ToString(MAX_PICTURES);
        //int iMaxPics = System.Int32.Parse(maxPics);
        //System.String[] arrFiles = new System.String[iMaxPics];
        //getEngine().getMulitiPictures().clearPictureData();
        int pos = url.lastIndexOf(':');
        String endString = "";
        if ((pos + 2) < url.length())
            endString = url.substring(pos + 2);
         
        url = url.substring(0, (0) + (pos + 1)) + "*" + endString;
        String filePath = getEngine().getResultsFolder() + recordId;
        byte[] _bytes = retsGetObjectAllPictures(url,filePath);
        //writeToFile(filePath, _bytes);
        int _index = 0;
        int _picStart = 0;
        int _picEnd = 0;
        boolean foundPicStart = false;
        boolean foundPicEnd = false;
        boolean foundPicStartLine = false;
        int picCount = 0;
        byte[] firstPicture = null;
        byte[] _bytesContentTypeBoundary = new byte[0];
        if (m_ContentTypeBoundry != null)
        {
            _bytesContentTypeBoundary = EncodingSupport.GetEncoder("UTF-8").getBytes(m_ContentTypeBoundry);
        }
         
        if (_bytes == null)
        {
            _bytes = new byte[0];
        }
         
        for (int i = 0;i < _bytes.length;i++)
        {
            if (i == 0 && _bytes[i] == (byte)'-' && _bytes[i + 1] == (byte)'-')
                foundPicStartLine = true;
             
            // if the current byte is the LF char
            if (_bytes[i] == (byte)'\n')
            {
                // try backing up and seeing if the one before was a CR char (the length of it will be our current position - where we started
                int length = i - _index;
                // if there is a non-zero length, and the char matches (back the length up one more so that we do not include the CR char
                if (_bytes[i - 1] == (byte)'\r')
                {
                    if (i < (_bytes.length - 2))
                    {
                        if (_bytes[i + 1] == (byte)'\r' && _bytes[i + 2] == (byte)'\n')
                        {
                            if (foundPicStartLine)
                            {
                                foundPicStart = true;
                                foundPicStartLine = false;
                                _picStart = i + 3;
                            }
                             
                        }
                         
                        if ((_bytes[i + 1] == (byte)'-') && (_bytes[i + 2] == (byte)'-'))
                        {
                            // If in middle of data and ContentType Boundary exists, then compare the first 4 byte values of boundary to 4 bytes following --
                            if ((i < (_bytes.length - 7)) && (_bytesContentTypeBoundary.length > 4))
                            {
                                if ((_bytes[i + 3] == _bytesContentTypeBoundary[0]) && (_bytes[i + 4] == _bytesContentTypeBoundary[1]) && (_bytes[i + 5] == _bytesContentTypeBoundary[2]) && (_bytes[i + 6] == _bytesContentTypeBoundary[3]))
                                {
                                    foundPicStartLine = true;
                                    if (foundPicStart)
                                    {
                                        foundPicEnd = true;
                                        _picEnd = i - 1;
                                    }
                                     
                                }
                                 
                            }
                            else
                            {
                                foundPicStartLine = true;
                                if (foundPicStart)
                                {
                                    foundPicEnd = true;
                                    _picEnd = i - 1;
                                }
                                 
                            } 
                        }
                         
                    }
                     
                    if ((foundPicStart && foundPicEnd))
                    {
                        if (_picEnd < _picStart)
                        {
                            int ih = 445;
                        }
                         
                    }
                     
                    if (foundPicStart && foundPicEnd)
                    {
                        picCount++;
                        byte[] tempBuffer = new byte[_picEnd - _picStart + 1];
                        if (tempBuffer.length > 0)
                        {
                            if (picCount == 1)
                            {
                                firstPicture = new byte[_picEnd - _picStart + 1];
                                Buffer.BlockCopy(_bytes, _picStart, firstPicture, 0, firstPicture.length);
                            }
                            else
                                Buffer.BlockCopy(_bytes, _picStart, tempBuffer, 0, tempBuffer.length); 
                        }
                         
                        if (picCount == 2)
                        {
                            if (byteArrayCompare(firstPicture,tempBuffer))
                            {
                                picCount--;
                                foundPicStart = foundPicEnd = false;
                                continue;
                            }
                             
                            firstPicture = null;
                        }
                         
                        writeToFile(filePath + "_" + picCount + ".jpg",picCount == 1 ? firstPicture : tempBuffer);
                        foundPicStart = foundPicEnd = false;
                    }
                     
                }
                 
            }
             
        }
        String[] arrDes = new String[picCount];
        for (int i = 0;i < arrDes.length;i++)
        {
            arrDes[i] = filePath + "_" + (i + 1) + ".jpg";
        }
        MLSRecord record = getEngine().getMLSRecord(recordId);
        record.setMultiPictureFileName(arrDes);
    }

    //for (int i = 0; i <= iMaxPics; i++)
    //{
    //    System.String[] urlAndFilePath = getEngine().getMulitiPictures().getURLAndFilePath(url, getEngine().getResultsFolder() + recordId, i);
    //    if (isDownPicFlag3())
    //        urlAndFilePath[1] = urlAndFilePath[0];
    //    else
    //        downloadMultiPics(urlAndFilePath[1], urlAndFilePath[0]);
    //    bool tmpBool;
    //    if (System.IO.File.Exists((new System.IO.FileInfo(urlAndFilePath[1])).FullName))
    //        tmpBool = true;
    //    else
    //        tmpBool = System.IO.Directory.Exists((new System.IO.FileInfo(urlAndFilePath[1])).FullName);
    //    if (i < iMaxPics && tmpBool && getEngine().getEnvironment().isPictureFileSupported("file:/" + urlAndFilePath[1]))
    //    {
    //        if (getEngine().getMulitiPictures().isComparePictures())
    //        {
    //            getEngine().getMulitiPictures().setCurrentPicture(getEngine().getEnvironment().getPictureData());
    //            if (getEngine().getMulitiPictures().isSamePicture() && i > 0)
    //            {
    //                System.String[] arrDes = new System.String[i];
    //                Array.Copy(arrFiles, 0, arrDes, 0, i);
    //                MLSRecord record = getEngine().getMLSRecord(recordId);
    //                record.setMultiPictureFileName(arrDes);
    //                break;
    //            }
    //        }
    //        arrFiles[i] = urlAndFilePath[1];
    //    }
    //    else
    //    {
    //        if (i > 0)
    //        {
    //            System.String[] arrDes = new System.String[i];
    //            Array.Copy(arrFiles, 0, arrDes, 0, i);
    //            MLSRecord record = getEngine().getMLSRecord(recordId);
    //            record.setMultiPictureFileName(arrDes);
    //        }
    //        break;
    //    }
    //}
    protected boolean byteArrayCompare(byte[] a1, byte[] a2) throws Exception {
        if (a1.length != a2.length)
            return false;
         
        for (int i = 0;i < a1.length;i++)
            if (a1[i] != a2[i])
                return false;
             
        return true;
    }

    protected public void downloadMultiPics(String filePath, String url) throws Exception {
        retsGetObject(url,filePath);
    }

    protected public void downLoad() throws Exception {
        parseURL(StringSupport.Trim(m_connString.toString()));
        if (getCurrentScriptType() == SCRIPT_MAIN)
            downloadMain();
        else if (getCurrentScriptType() == SCRIPT_PIC || getCurrentScriptType() == SCRIPT_EXTRAVAR)
            downloadPic();
          
    }

    protected public void downloadPic() throws Exception {
        MLSException exc = null;
        parseURL(m_connString.toString());
        m_SearchURLs.put(getCurrentRecordId(), m_sSearch);
        String picPath = getEngine().getResultsFolder();
    }

    protected public void fileDownload(String sSaveto, String sReq) throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        int retryCount = 0;
        boolean loggedIn = false;
        LableRetry:if (retryCount > 0)
            getEngine().writeLine("Search Failed. Retry " + retryCount + ": URL=" + m_sSearchURL + m_sSearch,true);
         
        try
        {
            loggedIn = false;
            if (retsLogin())
            {
                loggedIn = true;
                if (isActionUrl())
                {
                    if (!retsAction())
                    {
                        return ;
                    }
                     
                }
                 
                //retsLogout();
                if ((getEngine().getEngineFlags() & net.toppro.components.mls.engine.MLSEngine.MSFLAG_METADATA_ONLY) == net.toppro.components.mls.engine.MLSEngine.MSFLAG_METADATA_ONLY)
                {
                    getMetadata();
                    return ;
                }
                else if (m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_GETMETADATA).equals("1"))
                    getMetadata();
                  
                retsSearch(sSaveto);
                m_searchSuccessOnce = true;
            }
            else
            {
                writeToFile(sSaveto,null);
            } 
        }
        catch (Exception exc)
        {
            if (m_searchSuccessOnce && (!(exc instanceof MLSException) || (exc instanceof MLSException && ((MLSException)exc).getCode() != MLSException.CODE_NO_RECORD_FOUND)))
            {
                retryCount++;
                if (retryCount > 0 && retryCount < 4)
                {
                    //getEngine().WriteLine("Search Failed. Retry " + retryCount + ": URL=" + m_sSearchURL + m_sSearch, true);
                    Thread.sleep(1000);
                    goto LableRetry
                }
                else
                {
                    getEngine().writeLine("Retry Search Failed 3 Times. URL=" + m_sSearchURL + m_sSearch,true);
                    getEngine().getEnvironment().addXmlNote(60521,"Some sub-requests within the internal chunking logic have failed. The result data may not contain all records.");
                    throw getEngine().createException(exc);
                } 
            }
            else
                throw getEngine().createException(exc); 
        }
        finally
        {
            if (loggedIn)
                retsLogout();
             
        }
        getEngine().writeLine("\r\nGet Search result = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
    }

    private void getMetadata() throws Exception {
        String meta_xml = getEngine().getResultsFolder() + "metadata.xml";
        //m_sRequestURL = "";
        if (m_sMetaDataURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sMetaDataURL;
        else
            m_sRequestURL = m_sMetaDataURL; 
        m_sURI = getSubUrl(m_sMetaDataURL);
        //"?Type=METADATA-SYSTEM&ID=0&Format=COMPACT";
        String MetaType = "?Type=METADATA-SYSTEM&ID=*";
        if ((getEngine().getEngineFlags() & net.toppro.components.mls.engine.MLSEngine.MSFLAG_DATAAGG_METADATA_ONLY) == net.toppro.components.mls.engine.MLSEngine.MSFLAG_DATAAGG_METADATA_ONLY)
        {
            MetaType = "?Type=METADATA-SYSTEM&ID=*&Format=COMPACT";
            if (m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DATAAGGMETADATACONFIG).length() != 0)
            {
                MetaType = m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DATAAGGMETADATACONFIG);
            }
             
        }
        else
        {
            if (m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_GETMETADATATYPE).length() != 0)
            {
                MetaType = m_def.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_GETMETADATATYPE);
            }
             
        } 
        m_sRequestURL += MetaType;
        if (connectHTTP(meta_xml,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return ;
        else if (connectHTTP(meta_xml,m_sRequestURL,m_retsConn,false,m_HttpMethod) != null)
            return ;
        else if (connectHTTP(meta_xml,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return ;
           
    }

    /**
    * RETS Login transaction
    */
    protected public boolean retsLogin() throws Exception {
        m_sRequestURL = m_sBaseURL + m_sLoginURL;
        int iLen = m_sLoginURL.indexOf("?");
        if (iLen == -1)
            m_sURI = m_sLoginURL;
        else
            m_sURI = m_sLoginURL.substring(0, (0) + (iLen)); 
        m_vCookies = null;
        try
        {
            //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
            m_retsConn = new RETSURLConnection(new URI(m_sBaseURL));
            m_retsConn.setUseRelativePath(m_useRelativePath);
            m_retsConn.setHttpVersion(m_HttpVersion);
            return loginConnectHTTP(m_sRequestURL,m_retsConn);
        }
        catch (System.UriFormatException e)
        {
            throw getEngine().createException(e);
        }
    
    }

    protected public boolean isActionUrl() throws Exception {
        return m_sActionURL != null && StringSupport.Trim(m_sActionURL).length() != 0;
    }

    /**
    * RETS Action transaction
    */
    protected public boolean retsAction() throws Exception {
        if (getEngine().isSkipActionURL())
            return true;
         
        if (m_sActionURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sActionURL;
        else
            m_sRequestURL = m_sActionURL; 
        m_sURI = getSubUrl(m_sActionURL);
        // TODO: we actually should not do this, but currently
        // a lot of def-files use spaces in URL construction. :(
        // This is something what needs to be fixed.
        String request = MLSUtil.replaceSubStr(m_sRequestURL," ","%20");
        String request2 = removeCgiFromReq(request);
        boolean isSetAuth = true;
        int iStatus;
        String sResponse;
        String sMsg = "";
        // try send action url via GET method,
        // if it failed then try to send via POST
        RETSURLConnection retsCon = m_retsConn;
        retsCon.setMethod(RETSURLConnection.HTTP_METHOD_GET);
        Environment environment = getEngine().getEnvironment();
        environment.checkStop();
        for (int i = 0;i < 2;i++)
        {
            try
            {
                //m_retsConn.clearCookies(); // comment for Albuquerque NM
                setHttpHeader(retsCon,true);
                try
                {
                    retsCon.connect();
                }
                catch (IOException ioe)
                {
                    throw getEngine().createException(ioe,STRINGS.get_Renamed(STRINGS.SCRIPT_CLIENT_CANNOT_CONNECT));
                }

                try
                {
                    retsCon.sendRequest(request2,getEngine());
                }
                catch (IOException ioe2)
                {
                    throw getEngine().createException(ioe2,STRINGS.get_Renamed(STRINGS.S7_0));
                }

                iStatus = retsCon.getStatusCode();
                sResponse = retsCon.getResponseTextAsText();
                if (sResponse == null)
                    // Hot fix for Norton IS
                    sResponse = "";
                 
                sMsg = "";
                switch(iStatus)
                {
                    case 200: 
                        m_vCookies = retsCon.getCookies();
                        return true;
                    case 405: 
                        return retsAction();
                    case 401: 
                        //405 Method Not Allowed, change method, try again
                        if (retsCon.getMethod().equals(RETSURLConnection.HTTP_METHOD_GET))
                        {
                            retsCon.setMethod(RETSURLConnection.HTTP_METHOD_POST);
                            break;
                        }
                         
                        sMsg = getRETSErrorMsg(sResponse);
                        if (sMsg != null)
                            throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + "\n\n" + sMsg,"Reply code:" + "" + getRETSErrorCode(sResponse) + "\n\n" + "Error Message: " + sMsg);
                        else
                            throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD)); 
                    default: 
                        //goto default;
                        sMsg = getRETSErrorMsg(sResponse);
                        if (sMsg != null)
                            throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + "\n\n" + sMsg,"Reply code:" + "" + getRETSErrorCode(sResponse) + "\n\n" + "Error Message: " + sMsg);
                         
                        break;
                
                }
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
        retsCon.closeMemoryStream();
        environment.checkStop();
        return false;
    }

    /**
    * RETS Search transaction
    */
    protected public boolean retsSearch(String sSaveto) throws Exception {
        if (m_sSearchURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sSearchURL + m_sSearch;
        else
            m_sRequestURL = m_sSearchURL + m_sSearch; 
        if (getEngine().isGetRecordCount())
        {
            String strRecordCount = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_TCSRCOUNT);
            if (strRecordCount == null || strRecordCount.length() == 0)
                strRecordCount = RECORD_COUNT;
             
            m_sRequestURL = m_sRequestURL + strRecordCount;
        }
         
        m_sURI = getSubUrl(m_sSearchURL);
        setConnectionType("close");
        if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,false,m_HttpMethod) != null)
            return true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return true;
        else if (m_isNonceExpiredError)
        {
            if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
                return true;
            else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
                return true;
              
        }
             
        return false;
    }

    /**
    * RETS Search transaction
    */
    protected public boolean retsSearch(String sSearch, String sSaveto) throws Exception {
        if (m_sSearchURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sSearchURL + sSearch;
        else
            m_sRequestURL = m_sSearchURL + sSearch; 
        if (getEngine().isGetRecordCount())
        {
            String strRecordCount = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_TCSRCOUNT);
            if (strRecordCount == null || strRecordCount.length() == 0)
                strRecordCount = RECORD_COUNT;
             
            m_sRequestURL = m_sRequestURL + strRecordCount;
        }
         
        m_sURI = getSubUrl(m_sSearchURL);
        setConnectionType("close");
        if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,false,m_HttpMethod) != null)
            return true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
            return true;
        else if (m_isNonceExpiredError)
        {
            if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
                return true;
            else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
                return true;
              
        }
             
        return false;
    }

    protected public boolean retsGetObject(String request_url, String sSaveto) throws Exception {
        if (m_sGetObjectURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sGetObjectURL + request_url;
        else
            m_sRequestURL = m_sGetObjectURL + request_url; 
        m_sURI = getSubUrl(m_sGetObjectURL);
        //
        // Changes 4th parameter to false in connectHTTP, because we have an issue with Norton Firewall.
        // It rejects packet with authentication in GetObject
        //
        m_retsConn.setMethod(RETSURLConnection.HTTP_METHOD_GET);
        setAcceptType("image/jpeg");
        if (sSaveto.endsWith(".txt"))
            setAcceptType("text/plain");
         
        boolean bResult = false;
        if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,null) != null)
            bResult = true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,false,null) != null)
            bResult = true;
        else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,null) != null)
            bResult = true;
        else if (m_isNonceExpiredError)
        {
            if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
                return true;
            else if (connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod) != null)
                return true;
              
        }
            
        setAcceptType("*/*");
        return bResult;
    }

    protected public byte[] retsGetObjectAllPictures(String request_url, String sSaveto) throws Exception {
        if (m_sGetObjectURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sGetObjectURL + request_url;
        else
            m_sRequestURL = m_sGetObjectURL + request_url; 
        m_sURI = getSubUrl(m_sGetObjectURL);
        //
        // Changes 4th parameter to false in connectHTTP, because we have an issue with Norton Firewall.
        // It rejects packet with authentication in GetObject
        //
        m_retsConn.setMethod(RETSURLConnection.HTTP_METHOD_GET);
        setAcceptType("image/jpeg");
        boolean bResult = false;
        byte[] responseData;
        if ((responseData = connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,null,true)) != null)
            return responseData;
        else if ((responseData = connectHTTP(sSaveto,m_sRequestURL,m_retsConn,false,null,true)) != null)
            return responseData;
        else if ((responseData = connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,null,true)) != null)
            return responseData;
        else if (m_isNonceExpiredError)
        {
            if ((responseData = connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod,true)) != null)
                return responseData;
            else if ((responseData = connectHTTP(sSaveto,m_sRequestURL,m_retsConn,true,m_HttpMethod,true)) != null)
                return responseData;
              
        }
            
        setAcceptType("*/*");
        return responseData;
    }

    /**
    * RETS Logout transaction
    */
    protected public boolean retsLogout() throws Exception {
        setAcceptType("*/*");
        setConnectionType("close");
        if (m_sLogoutURL == null || m_sLogoutURL.length() == 0)
            return false;
         
        if (m_sLogoutURL.indexOf(HTTP_PROTOCOL) == -1)
            m_sRequestURL = m_sBaseURL + m_sLogoutURL;
        else
            m_sRequestURL = m_sLogoutURL; 
        m_sURI = getSubUrl(m_sLogoutURL);
        try
        {
            //m_retsConn.setMethod(RETSURLConnection.HTTP_METHOD_POST);
            if (connectHTTP(null,m_sRequestURL,m_retsConn,true,null) != null)
                return true;
             
        }
        catch (Exception __dummyCatchVar0)
        {
        }

        return false;
    }

    public void disconnect(Exception e) throws Exception {
        if (m_retsConn != null)
            m_retsConn.disconnect();
         
    }

    protected public void parseURL(String sReq) throws Exception {
        String sUrl = MLSUtil.getSubString(sReq, "\r", MLSUtil.STRING_LEFT, false);
        String sPara = MLSUtil.getSubString(sReq, "\r", MLSUtil.STRING_RIGHT, false);
        String sUserInfo = MLSUtil.getSubString(sPara, "\r", MLSUtil.STRING_LEFT, false);
        if (sUrl.StartsWith(HTTPS_PROTOCOL, StringComparison.CurrentCultureIgnoreCase))
            HTTP_PROTOCOL = HTTPS_PROTOCOL;
         
        m_sBaseURL = getBaseUrl(sUrl);
        m_sLoginURL = getSubUrl(sUrl);
        fillUserInfo(sUserInfo);
        m_sSearch = MLSUtil.getSubString(sPara, "\r", MLSUtil.STRING_RIGHT, false).Trim();
        addRETSQueryParameter();
        int iLen = m_sLoginURL.indexOf("?");
        if (iLen == -1)
            m_sURI = m_sLoginURL;
        else
            m_sURI = m_sLoginURL.Substring(0, (iLen)-(0)); 
    }

    protected void addRETSQueryParameter() throws Exception {
        String retsQueryParameter = getEngine().getRETSQueryParameter();
        boolean returnIDsOnly = getEngine().getTpAppRequest().getSelectPicFieldsOnly();
        Boolean isReturnMlsNumberOnlyRequest = getEngine().getTpAppRequest().getIsReturnMlsNumberOnlyRequest();
        if (!StringSupport.isNullOrEmpty(retsQueryParameter) && !getIsCheckAgentSearch())
        {
            int posEqual = retsQueryParameter.IndexOf("=", StringComparison.CurrentCultureIgnoreCase);
            String searchCode = retsQueryParameter.substring(0, (0) + (posEqual));
            if (m_sSearch.IndexOf("(" + searchCode + "=", StringComparison.CurrentCultureIgnoreCase) > -1)
            {
                throw getEngine().createException(MLSException.CODE_DUPLICATE_SEARCH_CODE_RETS_PARAMETER,"");
            }
             
            //Raise Error;
            int posQuery = m_sSearch.IndexOf("Query=", StringComparison.CurrentCultureIgnoreCase);
            int posLast = -1;
            if (posQuery > -1)
                posLast = m_sSearch.indexOf("&", posQuery);
             
            String searchURL = "(" + retsQueryParameter + ")";
            if (posQuery < 0)
                m_sSearch = m_sSearch + "&" + searchURL;
            else if (posLast > -1 && posLast > posQuery)
            {
                m_sSearch = m_sSearch.substring(0, (0) + (posLast)) + "," + searchURL + m_sSearch.substring(posLast, (posLast) + (m_sSearch.length() - posLast));
            }
            else
            {
                m_sSearch = m_sSearch + "," + searchURL;
            }  
        }
         
        if (!getIsCheckAgentSearch() && !getEngine().getIsDownloadIDs())
        {
            if (returnIDsOnly)
            {
                addSelectQueryClause(getEngine().getReturnIDsOnlyFieldList());
            }
            else if (isReturnMlsNumberOnlyRequest)
            {
                addSelectQueryClause(getEngine().getReturnMlsNumberOnlyFieldName());
            }
              
        }
         
        if (getEngine().isIncludingTlBlobField() && !getIsCheckAgentSearch())
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ posClass = m_sSearch.IndexOf("Class=", StringComparison.CurrentCultureIgnoreCase);
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ posClassEnd = m_sSearch.IndexOf('&', posClass);
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ className = m_sSearch.Substring(posClass + 6, posClassEnd - posClass - 6);
            getEngine().setPropertyClassNameFromMainScript(className);
        }
         
        if (getEngine().isUsingComaptFormat() && !getIsCheckAgentSearch())
        {
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ posFormat = m_sSearch.IndexOf("Format=", StringComparison.CurrentCultureIgnoreCase);
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ posFormatEnd = m_sSearch.IndexOf('&', posFormat);
            m_sSearch = m_sSearch.Substring(0, posFormat + 7) + "COMPACT" + m_sSearch.Substring(posFormatEnd, m_sSearch.length() - posFormatEnd);
        }
         
        if (!StringSupport.isNullOrEmpty(getEngine().getCurrentOpenHouseClass()) && !getIsCheckAgentSearch())
        {
            int pos = m_sSearch.IndexOf("Class=", StringComparison.CurrentCultureIgnoreCase);
            int pos1 = m_sSearch.indexOf('&', pos);
            m_sSearch = m_sSearch.substring(0, (0) + (pos + 6)) + getEngine().getCurrentOpenHouseClass() + m_sSearch.substring(pos1, (pos1) + (m_sSearch.length() - pos1));
        }
         
    }

    private void addSelectQueryClause(String fieldNames) throws Exception {
        if (!StringSupport.isNullOrEmpty(fieldNames))
        {
            int selectStart = m_sSearch.IndexOf("&Select=", StringComparison.CurrentCultureIgnoreCase);
            int selectEnd = 0;
            if (selectStart == -1)
            {
                selectStart = m_sSearch.IndexOf("Class=", StringComparison.CurrentCultureIgnoreCase);
                selectStart = m_sSearch.indexOf("&", selectStart);
                selectEnd = selectStart;
            }
            else
            {
                selectEnd = m_sSearch.indexOf("&", selectStart + 1);
            } 
            m_sSearch = m_sSearch.substring(0, (0) + (selectStart)) + "&Select=" + fieldNames + m_sSearch.substring(selectEnd);
        }
         
    }

    protected public void fillUserInfo(String sUserInfo) throws Exception {
        String sNamePara = MLSUtil.getSubString(sUserInfo, "&", MLSUtil.STRING_LEFT, false);
        m_sUsername = MLSUtil.getSubString(sNamePara, "=", MLSUtil.STRING_RIGHT, false);
        String sPwPara = MLSUtil.getSubString(sUserInfo, "&", MLSUtil.STRING_RIGHT, false);
        m_sPassword = MLSUtil.getSubString(sPwPara, "=", MLSUtil.STRING_RIGHT, false);
    }

    protected public String getBaseUrl(String sUrl) throws Exception {
        String sBase = "";
        if (sUrl != null && StringSupport.Trim(sUrl).length() != 0)
        {
            String s = MLSUtil.getSubString(sUrl, HTTP_PROTOCOL, MLSUtil.STRING_RIGHT, false);
            s = MLSUtil.getSubString(s, "/", MLSUtil.STRING_LEFT, false);
            sBase = HTTP_PROTOCOL + s;
        }
         
        return sBase;
    }

    protected public String getSubUrl(String sUrl) throws Exception {
        String sSub = "";
        if (sUrl != null && StringSupport.Trim(sUrl).length() != 0)
        {
            String s = MLSUtil.getSubString(sUrl, HTTP_PROTOCOL, MLSUtil.STRING_RIGHT, false);
            sSub = MLSUtil.getSubString(s, "/", MLSUtil.STRING_RIGHT, true);
        }
         
        return sSub;
    }

    public void write(String str) throws Exception {
        m_connString.append(str);
    }

    protected public boolean loginConnectHTTP(String sReq, HttpURLConnectionEx hc) throws Exception {
        try
        {
            // TODO: we actually should not do this, but currently
            // a lot of def-files use spaces in URL construction. :(
            // This is something what needs to be fixed.
            String request = MLSUtil.replaceSubStr(sReq," ","%20");
            String request2 = removeCgiFromReq(request);
            boolean isSetAuth = false;
            RETSURLConnection retsCon = (RETSURLConnection)hc;
            retsCon.setMethod(RETSURLConnection.HTTP_METHOD_POST);
            int iStatus = 0;
            int iConCnt = 0;
            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            int maxTry = 2;
            while (iStatus != 200 && iConCnt < maxTry)
            {
                iConCnt++;
                setHttpHeader(retsCon,isSetAuth);
                retsCon.closeMemoryStream();
                try
                {
                    retsCon.connect();
                }
                catch (IOException ioe)
                {
                    throw getEngine().createException(ioe,STRINGS.get_Renamed(STRINGS.SCRIPT_CLIENT_CANNOT_CONNECT));
                }

                try
                {
                    retsCon.sendRequest(request2,getEngine());
                }
                catch (IOException ioe2)
                {
                    throw getEngine().createException(ioe2,STRINGS.get_Renamed(STRINGS.S7_0));
                }

                iStatus = retsCon.getStatusCode();
                String sResponse = retsCon.getResponseTextAsText();
                if (sResponse == null)
                    // Hot fix for Norton IS
                    sResponse = "";
                 
                String sReplyCode = getReplyCode(sResponse);
                //MLSUtil.getPara(sResponse, "ReplyCode", "=", "\r");
                switch(iStatus)
                {
                    case 200: 
                        m_vCookies = retsCon.getCookies();
                        getParas(sResponse);
                        if (sReplyCode == null || !StringSupport.Trim(sReplyCode).equals("0"))
                        {
                            String sMsg = getRETSErrorMsg(sResponse);
                            if (sMsg != null)
                                throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.S32) + "\n\n" + sMsg,"Reply code:" + "" + getRETSErrorCode(sResponse) + "\n\n" + "Error Message: " + sMsg);
                            else
                                break; 
                        }
                        else
                            return true; 
                    case 401: 
                        //goto case 401;
                        if (!isSetAuth)
                        {
                            isSetAuth = true;
                            m_vCookies = retsCon.getCookies();
                        }
                        else if (retsCon != null && retsCon.getReasonLine().toUpperCase().equals(HttpURLConnectionEx.REASON_LINE_NONCE_EXPIRED.toUpperCase()))
                        {
                            setHttpHeader(retsCon,isSetAuth);
                            try
                            {
                                retsCon.connect();
                            }
                            catch (IOException ioe)
                            {
                                throw getEngine().createException(ioe,STRINGS.get_Renamed(STRINGS.SCRIPT_CLIENT_CANNOT_CONNECT));
                            }

                            try
                            {
                                retsCon.sendRequest(request2,getEngine());
                            }
                            catch (IOException ioe2)
                            {
                                throw getEngine().createException(ioe2,STRINGS.get_Renamed(STRINGS.S7_0));
                            }

                            sResponse = retsCon.getResponseTextAsText();
                            if (sResponse == null)
                                // Hot fix for Norton IS
                                sResponse = "";
                             
                            sReplyCode = getReplyCode(sResponse);
                            if ((iStatus = retsCon.getStatusCode()) == 200)
                            {
                                getParas(sResponse);
                                return true;
                            }
                            else if (iStatus == 401)
                            {
                                //ERROR_BAD_USERNAME_OR_PWD
                                maxTry = 4;
                                if (iConCnt < maxTry)
                                    continue;
                                else
                                {
                                    String strStatusText = retsCon.getReasonLine();
                                    throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD));
                                } 
                            }
                              
                        }
                        else
                        {
                            if (sReplyCode == null || !StringSupport.Trim(sReplyCode).equals("0"))
                            {
                                String sMsg = getRETSErrorMsg(sResponse);
                                if (sMsg != null)
                                    throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.S32) + "\n\n" + sMsg,"Reply code:" + "" + getRETSErrorCode(sResponse) + "\n\n" + "Error Message: " + sMsg);
                                else
                                    throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD)); 
                            }
                             
                        }  
                        break;
                    case 403: 
                        throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD));
                    case 503: 
                        sResponse = retsCon.getResponseTextAsText();
                        if (sResponse == null)
                            // Hot fix for Norton IS
                            sResponse = "";
                         
                        throw getEngine().createException(STRINGS.get_Renamed(STRINGS.ERROR_RETS_20050) + "<br><br>" + sResponse,MLSException.FORMAT_HTML);
                    default: 
                        break;
                
                }
            }
            environment.checkStop();
            retsCon.closeMemoryStream();
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }

        return false;
    }

    private String getRETSErrorMsg(String sResponse) throws Exception {
        int pos = sResponse.IndexOf("", StringComparison.CurrentCultureIgnoreCase);
        if (pos > -1)
            sResponse = sResponse.substring(pos, (pos) + (sResponse.length() - pos));
         
        String sErrCode = getReplyCode(sResponse);
        String sErrMsg = null;
        if (sErrCode != null && StringSupport.Trim(sErrCode).length() != 0 && MLSUtil.isNumber(StringSupport.Trim(sErrCode)))
        {
            int iErrCode = Integer.valueOf(StringSupport.Trim(sErrCode));
            String sErrText = getErrTextByCode(iErrCode);
            if (sErrText != null)
                sErrMsg = sErrText;
            else
            {
                sErrText = getReplyText(sResponse);
                String sErrDetails = null;
                if (sResponse != null && sResponse.indexOf("<RETS") != -1)
                {
                    String str1 = MLSUtil.getSubString(sResponse, ">", MLSUtil.STRING_RIGHT, false);
                    if (str1 != null && str1.indexOf("</RETS>") != -1)
                        sErrDetails = str1.substring(0, (0) + ((str1.indexOf("</RETS>")) - (0)));
                     
                }
                 
                if (sErrText != null)
                {
                    if (sErrDetails != null && sErrDetails.length() != 0)
                        sErrMsg = sErrText + "\n" + StringSupport.Trim(sErrDetails);
                    else
                        sErrMsg = sErrText; 
                }
                 
            } 
        }
         
        return sErrMsg;
    }

    private String getReplyText(String sResponse) throws Exception {
        if (StringSupport.isNullOrEmpty(sResponse))
            return "";
         
        try
        {
            int pos = sResponse.IndexOf(RETS_STATUS, StringComparison.CurrentCultureIgnoreCase);
            if (pos > -1)
                sResponse = sResponse.substring(pos, (pos) + (sResponse.length() - pos));
             
        }
        catch (Exception exc)
        {
            getEngine().writeLine(exc.getMessage());
        }

        String replyCode = MLSUtil.getPara(sResponse,"ReplyText","=","\r");
        return replyCode;
    }

    private String getReplyCode(String sResponse) throws Exception {
        if (StringSupport.isNullOrEmpty(sResponse))
            return "";
         
        try
        {
            int pos = sResponse.IndexOf(RETS_STATUS, StringComparison.CurrentCultureIgnoreCase);
            if (pos > -1)
                sResponse = sResponse.substring(pos, (pos) + (sResponse.length() - pos));
             
        }
        catch (Exception exc)
        {
            getEngine().writeLine(exc.getMessage());
        }

        String replyCode = MLSUtil.getPara(sResponse,"ReplyCode","=","\r");
        return replyCode;
    }

    private int getRETSErrorCode(String sResponse) throws Exception {
        String sErrCode = getReplyCode(sResponse);
        String sErrMsg = null;
        int iErrCode = 0;
        if (sErrCode != null && StringSupport.Trim(sErrCode).length() != 0 && MLSUtil.isNumber(StringSupport.Trim(sErrCode)))
        {
            try
            {
                iErrCode = Integer.valueOf(StringSupport.Trim(sErrCode));
            }
            catch (Exception ce)
            {
                iErrCode = 0;
            }
        
        }
         
        return iErrCode;
    }

    private String getErrTextByCode(int iErrCode) throws Exception {
        if (iErrCode == 0)
            return "Operation successful.";
         
        switch(iErrCode)
        {
            case 20003: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20003);
            case 20004: 
            case 20005: 
            case 20006: 
            case 20007: 
            case 20008: 
            case 20009: 
            case 200010: 
            case 200011: 
                return null;
            case 20012: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20012);
            case 20013: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20013);
            case 20014: 
            case 20015: 
            case 20016: 
            case 20017: 
            case 20018: 
            case 20019: 
                return null;
            case 20036: 
                return null;
            case 20050: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20050);
            case 20200: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20200);
            case 20201: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20201);
            case 20202: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20202);
            case 20203: 
            case 20206: 
                return null;
            case 20207: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20207);
            case 20208: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20208);
            case 20209: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20209);
            case 20210: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20210);
            case 20514: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20514);
            case 20400: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20400);
            case 20401: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20401);
            case 20402: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20402);
            case 20403: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20403);
            case 20406: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20406);
            case 20407: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20407);
            case 20408: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20408);
            case 20409: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20409);
            case 20410: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20410);
            case 20411: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20411);
            case 20412: 
                return STRINGS.get_Renamed(STRINGS.ERROR_RETS_20412);
            case 20413: 
                return null;
            default: 
                return null;
        
        }
    }

    //Login
    //Zero Balance
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Broker Code Required
    //Broker Code Invalid
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Reserved
    //Miscellaneous server login error
    //return STRINGS.get ( STRINGS.ERROR_RETS_20036 );
    //Server Temporarily Disabled
    //Search
    //Unknown Query Field
    //No Records Found
    //Invalid Select
    //Miscellaneous Search Error
    //Invalid Query Syntax
    //return STRINGS.get ( STRINGS.ERROR_RETS_20203 );
    //case 20206: //Invalid Query Syntax
    //	return STRINGS.get ( STRINGS.ERROR_RETS_20206 );
    //Unauthorized Query
    //Maximum Records Exceeded
    //Timeout
    //Too many outstanding queries
    //Requested DTD version unavailable
    //GetObject
    //Invalid Resource
    //Invalid Type
    //Invalid Identifier
    //No Object Found
    //Unsupported MIME type
    //Unauthorized Retrieval
    //Resource Unavailable
    //Object Unavailable
    //Request Too Large
    //Timeout
    //Too many outstanding requests
    //Miscellaneous error
    //return STRINGS.get ( STRINGS.ERROR_RETS_20413 );
    private String getRETSReplyText(String sResponse) throws Exception {
        String str = null;
        String sReplytext = MLSUtil.getPara(sResponse,"ReplyText","=","\r");
        return str;
    }

    private void getParas(String sResponse) throws Exception {
        m_sMemberName = MLSUtil.getPara(sResponse,"MemberName","=","\r");
        m_sUser = MLSUtil.getPara(sResponse,"User","=","\r");
        m_sBroker = MLSUtil.getPara(sResponse,"Broker","=","\r");
        m_sMetadataVersion = MLSUtil.getPara(sResponse,"MetadataVersion","=","\r");
        //if (!m_bCheckMetaDataVersion && m_sMetadataVersion != null)
        //{
        //    System.String defMetaDataVersion = "";
        //    defMetaDataVersion = getEngine().getDefParser().getValue(MLSEngine.SECTION_COMMON, MLSEngine.ATTRIBUTE_METADATAVERSION);
        //    if (defMetaDataVersion.Length != 0 && !defMetaDataVersion.ToUpper().Equals(m_sMetadataVersion.ToUpper()))
        //        getEngine().notifyTechSupport(MLSEngine.SUPPORT_CODE_METADATA_CHANGED, defMetaDataVersion, m_sMetadataVersion);
        //}
        m_sMinMetadataVersion = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"MinMetadataVersion","=","\r"));
        m_sTimeoutSeconds = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"TimeoutSeconds","=","\r"));
        m_sLoginComplete = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"ChangePassword","=","\r"));
        m_sUpdate = MLSUtil.getPara(sResponse,"Update","=","\r");
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsLogout").length() == 0)
            m_sLogoutURL = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nLogout","=","\r"));
        else
            m_sLogoutURL = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsLogout"); 
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsAction").length() == 0)
            m_sActionURL = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nAction","=","\r"));
        else
            m_sActionURL = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsAction"); 
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsSearch").length() == 0)
            m_sSearchURL = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nSearch","=","\r"));
        else
            m_sSearchURL = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsSearch"); 
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsGetObject").length() == 0)
            m_sGetObjectURL = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nGetObject","=","\r"));
        else
            m_sGetObjectURL = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsGetObject"); 
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsGetMetadata").length() == 0)
            m_sMetaDataURL = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nGetMetadata","=","\r"));
        else
            m_sMetaDataURL = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsGetMetadata"); 
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsChangePassword").length() == 0)
            m_sChangePasswdURL = MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nChangePassword","=","\r"));
        else
            m_sChangePasswdURL = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsChangePassword"); 
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsMetadataVersion").length() == 0)
            getEngine().setMlsMetadataVersion(MLSUtil.getCapbilityListString(MLSUtil.getPara(sResponse,"\nMetadataVersion","=","\r")));
        else
            getEngine().setMlsMetadataVersion(getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP,"RetsMetadataVersion")); 
    }

    protected public byte[] connectHTTP(String sSaveto, String sReq, HttpURLConnectionEx hc, boolean isSetAuth, String method) throws Exception {
        return connectHTTP(sSaveto,sReq,hc,isSetAuth,method,false);
    }

    protected public byte[] connectHTTP(String sSaveto, String sReq, HttpURLConnectionEx hc, boolean isSetAuth, String method, boolean isGetAllPictures) throws Exception {
        byte[] empty = new byte[0];
        if (!(hc instanceof RETSURLConnection))
            return null;
         
        m_isNonceExpiredError = false;
        try
        {
            // TODO: we actually should not do this, but currently
            // a lot of def-files use spaces in URL construction. :(
            // This is something what needs to be fixed.
            String request = MLSUtil.replaceSubStr(sReq," ","%20");
            String request2 = removeCgiFromReq(request);
            RETSURLConnection retsCon = (RETSURLConnection)hc;
            if (method != null && method.length() != 0)
                retsCon.setMethod(method);
             
            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            try
            {
                retsCon.connect();
            }
            catch (IOException ioe)
            {
                throw getEngine().createException(ioe,STRINGS.get_Renamed(STRINGS.SCRIPT_CLIENT_CANNOT_CONNECT));
            }

            m_vCookies = retsCon.getCookies();
            setHttpHeader(retsCon,isSetAuth);
            try
            {
                retsCon.sendRequest(request2,getEngine());
            }
            catch (IOException ioe2)
            {
                throw getEngine().createException(ioe2,STRINGS.get_Renamed(STRINGS.S7_0));
            }

            String sResponse = "";
            String sReplyCode = "";
            String location = retsCon.getHeaderField(HTTP_HEAD_LOCATION);
            String contentType = retsCon.getHeaderField(HTTP_HEAD_CONTENTTYPE);
            if (!StringSupport.isNullOrEmpty(location))
            {
                if (StringSupport.isNullOrEmpty(contentType))
                {
                    getLocationData(sSaveto,sReq,location,retsCon,isSetAuth,method);
                    return new byte[0];
                }
                else
                {
                    if (contentType.IndexOf("image", StringComparison.CurrentCultureIgnoreCase) > -1)
                    {
                        sResponse = retsCon.getResponseTextAsText(1000);
                        if (sResponse == null)
                        {
                            getLocationData(sSaveto,sReq,location,retsCon,isSetAuth,method);
                            return new byte[0];
                        }
                         
                    }
                    else
                    {
                        getLocationData(sSaveto,sReq,location,retsCon,isSetAuth,method);
                        return new byte[0];
                    } 
                } 
            }
             
            switch(retsCon.getStatusCode())
            {
                case 200: 
                    //HTTP successful
                    sResponse = retsCon.getResponseTextAsText(1000);
                    if (sResponse == null)
                        // Hot fix for Norton IS
                        sResponse = "";
                     
                    sReplyCode = getReplyCode(sResponse);
                    if (sReplyCode != null && StringSupport.Trim(sReplyCode).length() != 0)
                    {
                        if (!StringSupport.Trim(sReplyCode).equals("0"))
                        {
                            //RETS failed
                            if (StringSupport.Trim(sReplyCode).equals("20201"))
                            {
                                throw getEngine().createException(MLSException.CODE_NO_RECORD_FOUND,STRINGS.get_Renamed(STRINGS.S10));
                            }
                            else //retsLogout();
                            if (StringSupport.Trim(sReplyCode).equals("20208"))
                            {
                                //Maximum Records Exceeded, operation successful,
                                //write the received records to disk
                                byte[] data = retsCon.getResponseTextAsBytes();
                                if (sSaveto != null && sSaveto.length() != 0 && data != null)
                                    // Hot fix for Norton IS
                                    writeToFile(sSaveto,data);
                                 
                            }
                            else if (StringSupport.Trim(sReplyCode).equals("20400"))
                                return empty;
                            else //No object found
                            if (StringSupport.Trim(sReplyCode).equals("20402"))
                                return empty;
                            else //Invalid Id
                            if (StringSupport.Trim(sReplyCode).equals("20403"))
                                return empty;
                            else //No object found
                            if (StringSupport.Trim(sReplyCode).equals("20409"))
                                return empty;
                            else //Object Unavailable
                            if (StringSupport.Trim(sReplyCode).equals("20210"))
                                throw getEngine().createException(MLSException.CODE_QUERY_LIMIT_EXCEEDED,"");
                                   
                            String sMsg = getRETSErrorMsg(sResponse);
                            if (sMsg != null)
                            {
                                throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + "\n\n" + sMsg,"Reply code:" + "" + getRETSErrorCode(sResponse) + "\n\n" + "Error Message: " + sMsg);
                            }
                            else
                                return empty; 
                        }
                        else
                        {
                            //retsLogout();
                            //RETS successful
                            //Check if Location is in the header
                            byte[] data = retsCon.getResponseTextAsBytes();
                            int ContentLength = retsCon.contentLength();
                            if (sSaveto != null && sSaveto.length() != 0 && data != null)
                            {
                                // Hot fix for Norton IS
                                if (sSaveto.indexOf("jpg") != -1 && data.length < ContentLength)
                                    throw getEngine().createException(STRINGS.get_Renamed(STRINGS.ERR_WRONG_PICTURE_SIZE));
                                 
                                if (!isGetAllPictures || sSaveto.endsWith(".txt"))
                                    writeToFile(sSaveto,data);
                                 
                            }
                             
                            return data;
                        } 
                    }
                    else
                    {
                        try
                        {
                            String strContentType = retsCon.contentType();
                            String[] spiltContentType = StringSupport.Split(strContentType, ';');
                            if (spiltContentType.length > 1)
                            {
                                if (spiltContentType[1].contains("boundary"))
                                {
                                    String[] spiltBoundary = StringSupport.Split(spiltContentType[1], '=');
                                    m_ContentTypeBoundry = spiltBoundary[1];
                                    m_ContentTypeBoundry = StringSupport.Trim(m_ContentTypeBoundry, new char[] {'"'});
                                }
                                 
                            }
                             
                        }
                        catch (Exception __dummyCatchVar1)
                        {
                        }

                        int ContentLength = retsCon.contentLength();
                        //Data
                        byte[] data = retsCon.getResponseTextAsBytes();
                        if (sSaveto != null && sSaveto.length() != 0 && data != null)
                        {
                            if (sSaveto.indexOf("jpg") != -1 && data.length < ContentLength)
                                throw getEngine().createException(STRINGS.get_Renamed(STRINGS.ERR_WRONG_PICTURE_SIZE));
                             
                            if (!isGetAllPictures || sSaveto.endsWith(".txt"))
                                writeToFile(sSaveto,data);
                            else
                                return data; 
                        }
                        else
                        {
                            if (sSaveto != null && sSaveto.length() != 0)
                            {
                                data = new byte[0];
                                writeToFile(sSaveto,data);
                            }
                             
                        } 
                        if (ContentLength == 0)
                            return new byte[0];
                         
                        return data;
                    } 
                case 302: 
                    break;
                case 401: 
                    //goto case 302;
                    //Object moved
                    //string location = retsCon.getHeaderField(HTTP_HEAD_LOCATION);
                    //if (!string.IsNullOrEmpty(location))
                    //{
                    //    GetLocationData(sSaveto, sReq, location, retsCon, isSetAuth, method);
                    //    return new byte[0];
                    //}
                    //Redirect to another URL to get result
                    //sReq = MLSUtil.getSubString(sReq, "?", MLSUtil.STRING_LEFT, false);
                    //sReq = sReq.Substring(0, (sReq.LastIndexOf("/")) - (0));
                    //System.String sLocation = retsCon.responsHeader.get_Renamed(HTTP_HEAD_LOCATION);
                    //sReq += ("/" + sLocation.Trim());
                    //GetLocationData(sSaveto, sReq, sLocation, retsCon, false, null);
                    //return new byte[0];
                    //connectHTTP(sSaveto, sReq, hc, false, null);
                    //m_retsConn = retsCon;
                    sResponse = retsCon.getResponseTextAsText();
                    sReplyCode = getReplyCode(sResponse);
                    String sReplyText = getReplyText(sResponse);
                    if (StringSupport.isNullOrEmpty(sReplyText))
                        sReplyText = "";
                     
                    if (StringSupport.isNullOrEmpty(sReplyCode))
                        sReplyCode = "";
                     
                    if (StringSupport.Trim(sReplyCode).equals("400") && StringSupport.Trim(sReplyText).toLowerCase().contains("rets session is invalid"))
                    {
                        throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,sResponse);
                    }
                     
                    if (StringSupport.Trim(sReplyCode).equals("20203") && StringSupport.Trim(sReplyText).toLowerCase().contains("rets-session-id is invalid"))
                    {
                        throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,sResponse);
                    }
                     
                    if (retsCon != null && retsCon.getReasonLine().toUpperCase().equals(HttpURLConnectionEx.REASON_LINE_NONCE_EXPIRED.toUpperCase()))
                    {
                        m_isNonceExpiredError = true;
                    }
                     
                    return null;
                case 404: 
                    //retsLogout();
                    sResponse = retsCon.getResponseTextAsText();
                    if (sResponse == null)
                        // Hot fix for Norton IS
                        sResponse = "";
                     
                    sReplyCode = getReplyCode(sResponse);
                    if (sReplyCode != null && StringSupport.Trim(sReplyCode).length() != 0)
                    {
                        if (StringSupport.Trim(sReplyCode).equals("20400"))
                            return empty;
                        else //No object found
                        if (StringSupport.Trim(sReplyCode).equals("20402"))
                            return empty;
                        else //Invalid Id
                        if (StringSupport.Trim(sReplyCode).equals("20403"))
                            return empty;
                        else //No object found
                        if (StringSupport.Trim(sReplyCode).equals("20409"))
                            return empty;
                            
                    }
                     
                    throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + STRINGS.get_Renamed(STRINGS.ERR_HTTP_404));
                case 405: 
                    throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + STRINGS.get_Renamed(STRINGS.ERR_HTTP_405));
                default: 
                    //Object Unavailable
                    //retsLogout();
                    //HTTP failed
                    sResponse = retsCon.getResponseTextAsText();
                    if (sResponse == null)
                        // Hot fix for Norton IS
                        sResponse = "";
                     
                    if (!isSetAuth)
                        throw getEngine().createException(MLSException.CODE_NATIVE_MLS_ERROR,STRINGS.get_Renamed(STRINGS.S32) + sResponse,MLSException.FORMAT_HTML);
                     
                    break;
            
            }
            // end of switch
            retsCon.closeMemoryStream();
            environment.checkStop();
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }

        return null;
    }

    private void getLocationData(String saveTo, String sRequest, String locationHeader, HttpURLConnectionEx hc, boolean isSetAuth, String method) throws Exception {
        locationHeader = StringSupport.Trim(locationHeader);
        //if (locationHeader.IndexOf("http://", StringComparison.CurrentCultureIgnoreCase) > -1 ||
        //        locationHeader.IndexOf("https://", StringComparison.CurrentCultureIgnoreCase) > -1)
        //{
        //}
        if (locationHeader.startsWith("//"))
            locationHeader = "http:" + locationHeader;
         
        if (locationHeader.startsWith("/"))
        {
            // is a relative path
            locationHeader = m_sBaseURL + locationHeader;
        }
        else if (locationHeader.IndexOf("http://", StringComparison.CurrentCultureIgnoreCase) == -1 && locationHeader.IndexOf("https://", StringComparison.CurrentCultureIgnoreCase) == -1)
        {
            locationHeader = m_sBaseURL + "/" + locationHeader;
        }
          
        if (locationHeader.IndexOf(m_sBaseURL, StringComparison.CurrentCultureIgnoreCase) > -1)
        {
            if (connectHTTP(saveTo,locationHeader,hc,isSetAuth,method) != null)
                return ;
            else if (connectHTTP(saveTo,locationHeader,hc,!isSetAuth,method) != null)
                return ;
            else
                connectHttpDirectEx(saveTo,locationHeader);  
        }
        else
            connectHttpDirectEx(saveTo,locationHeader); 
    }

    protected public void setAcceptType(String type) throws Exception {
        m_AcceptType = type;
    }

    protected public void setConnectionType(String type) throws Exception {
        m_ConnectionType = type;
    }

    protected public void setHttpHeader(HttpURLConnectionEx hc, boolean isSetAuth) throws Exception {
        if (hc != null)
        {
            if (!(hc instanceof RETSURLConnection))
            {
                super.setHttpHeader(hc,isSetAuth);
                return ;
            }
             
            RETSURLConnection retsCon = (RETSURLConnection)hc;
            retsCon.removeRequestProperty();
            retsCon.setRequestProperty("Accept",m_AcceptType);
            retsCon.setRequestProperty("User-Agent",m_HttpUserAgent);
            retsCon.setRequestProperty("Host",retsCon.getHost());
            retsCon.setRequestProperty("Pragma","no-cache");
            retsCon.setRequestProperty("RETS-Version",m_RetsVersion);
            retsCon.setRequestProperty("Content-Length","0");
            if (hc.isConnectionClose())
                retsCon.setRequestProperty("Connection","close");
            else
                retsCon.setRequestProperty("Connection",m_ConnectionType); 
            String retsSessionID = "";
            if (m_vCookies != null && m_vCookies.size() != 0)
            {
                String sCookie = "";
                for (int i = 0;i < m_vCookies.size();i++)
                {
                    if (sCookie.length() != 0)
                    {
                        sCookie += ";";
                    }
                     
                    String cookie = getCookie((String)m_vCookies.get(i));
                    if (cookie.toLowerCase().contains("rets-session-id"))
                    {
                        if (cookie.indexOf("=") > -1)
                            retsSessionID = StringSupport.Trim(cookie.substring(cookie.indexOf("=") + 1));
                         
                    }
                     
                    sCookie += cookie;
                }
                if (sCookie.length() != 0)
                    retsCon.setRequestProperty("Cookie",sCookie);
                 
            }
             
            try
            {
                // System.out.println( "createDigest(\"Top Producer:Richard\")=" + RETSURLConnection.createDigest("Top Producer:Richard") );
                if (m_RetsUAUserAgent.length() > 0 && m_RetsUAPwd.length() > 0)
                {
                    String hash = m_RetsUAUserAgent + ":" + m_RetsUAPwd;
                    retsCon.setRequestProperty("RETS-UA-Authorization","Digest " + RETSURLConnection.createDigest(RETSURLConnection.createDigest(hash) + "::" + retsSessionID + ":" + m_RetsVersion));
                }
                 
            }
            catch (Exception ne)
            {
                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                getEngine().writeLine(ne.getMessage());
            }

            if (m_sUsername != null && m_sPassword != null && isSetAuth)
            {
                try
                {
                    if (retsCon.responsHeader.isDigestAuthentication())
                    {
                        Digest obj = retsCon.responsHeader.getDigest();
                        String sDigest = null;
                        if (obj.m_sQop != null && StringSupport.Trim(obj.m_sQop).length() != 0)
                            sDigest = retsCon.createDigestRFC2617(m_sUsername,m_sPassword,m_sURI);
                        else
                            sDigest = retsCon.createDigestRFC2069(m_sUsername,m_sPassword,m_sURI); 
                        if (sDigest != null)
                            retsCon.setRequestProperty("Authorization","Digest " + sDigest);
                         
                    }
                     
                    if (retsCon.responsHeader.isBasicAuthentication())
                    {
                        retsCon.setRequestProperty("Authorization","Basic " + MLSUtil.base64Encode(m_sUsername + ":" + m_sPassword));
                    }
                     
                }
                catch (Exception exc)
                {
                    throw getEngine().createException(exc);
                }
            
            }
             
        }
         
    }

    /**
    * Disabled encoding for username and password because username and password send via MD5
    */
    protected public String encodeParameter(String parameter, int parameterType) throws Exception {
        if (parameterType != PARAMETER_TYPE_SECURITY_FIELD)
            return super.encodeParameter(parameter,parameterType);
         
        return parameter;
    }

    /**
    * We override it because HTTP client requires rem script
    *  @return FLAG_REMSCRIPT_INSIDE_MAINSCRIPT
    */
    public int getFlags() throws Exception {
        return !getEngine().isReturnIdsOnly() ? FLAG_EXREMSCRIPT_INSIDE_MAINSCRIPT : 0;
    }

}


// end of class