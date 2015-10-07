//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:37 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSUtil;

public class REInfoLinkRETSClient  extends RETSClient 
{
    private String m_sAgentURL = "";
    private boolean m_isBypassCheckAgent = false;
    public REInfoLinkRETSClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
        m_isBypassCheckAgent = _Engine.isBypassAgentRosterAuth();
    }

    protected public void startRETSPicDownloading() throws Exception {
        MLSException exc = null;
        String picPath = getEngine().getResultsFolder();
        if (getCurrentScriptType() == SCRIPT_PIC)
        {
            if (retsLogin())
            {
                if (isActionUrl())
                {
                    if (!retsAction())
                    {
                        retsLogout();
                        return ;
                    }
                     
                }
                 
                if (!m_isBypassCheckAgent)
                    checkAgent(picPath + "Agent");
                 
                try
                {
                    String RecId = "";
                    String ReqURL = "";
                    IEnumerator en = EnumeratorSupport.mk(m_SearchURLs.keySet().iterator());
                    int TotalPics = m_SearchURLs.size();
                    int CountPics = 0;
                    while (en.moveNext())
                    {
                        //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
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
                            //exc = mls_ex;
                            retryCount++;
                            if (((MLSException)mls_ex).getCode() != MLSException.CODE_NO_RECORD_FOUND)
                            {
                                if (retryCount > 0 && retryCount < 4)
                                {
                                    retsLogout();
                                    pictureLoginRetry();
                                    goto LableRetry
                                }
                                 
                            }
                             
                        }
                    
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

    private void pictureLoginRetry() throws Exception {
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

    /**
    * RETS Search transaction
    */
    protected public boolean retsSearch(String sSaveto) throws Exception {
        if (!m_isBypassCheckAgent)
            checkAgent(sSaveto);
         
        return super.retsSearch(sSaveto);
    }

    protected public boolean checkAgent(String sSaveto) throws Exception {
        String searchURL = m_sSearch;
        m_sSearch = m_sAgentURL;
        boolean isSearchByRecordCount = getEngine().isGetRecordCount();
        getEngine().setSearchByRecordCount(true);
        String countFilePath = sSaveto + ".count";
        setIsCheckAgentSearch(true);
        try
        {
            super.retsSearch(countFilePath);
            String content = MLSUtil.readFile(countFilePath);
            int n = 0;
            try
            {
                n = Integer.valueOf(Tcs.Mls.Util.getRecordCount(content));
            }
            catch (Exception exc)
            {
            }

            if (n < 1)
                throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD));
             
        }
        catch (Exception exc)
        {
            throw getEngine().createException(MLSException.CODE_INVALID_USERNAME_PASSWORD,STRINGS.get_Renamed(STRINGS.ERROR_BAD_USERNAME_OR_PWD));
        }
        finally
        {
            getEngine().setSearchByRecordCount(isSearchByRecordCount);
            setIsCheckAgentSearch(false);
        }
        m_sSearch = searchURL;
        return true;
    }

    protected public void parseURL(String sReq) throws Exception {
        String sUrl = MLSUtil.getSubString(sReq, "\r", MLSUtil.STRING_LEFT, false);
        String sPara = MLSUtil.getSubString(sReq, "\r", MLSUtil.STRING_RIGHT, false);
        String sUserInfo = MLSUtil.getSubString(sPara, "\r", MLSUtil.STRING_LEFT, false);
        m_sBaseURL = getBaseUrl(sUrl);
        m_sLoginURL = getSubUrl(sUrl);
        fillUserInfo(sUserInfo);
        String sParaAll = MLSUtil.getSubString(sPara, "\r", MLSUtil.STRING_RIGHT, false).Trim();
        m_sAgentURL = MLSUtil.getSubString(sParaAll, "\r", MLSUtil.STRING_LEFT, false).Trim();
        m_sSearch = MLSUtil.getSubString(sParaAll, "\r", MLSUtil.STRING_RIGHT, false).Trim();
        addRETSQueryParameter();
        int iLen = m_sLoginURL.indexOf("?");
        if (iLen == -1)
            m_sURI = m_sLoginURL;
        else
            m_sURI = m_sLoginURL.Substring(0, (iLen)-(0)); 
    }

    protected public void fillUserInfo(String sUserInfo) throws Exception {
        super.fillUserInfo(sUserInfo);
        if (m_isBypassCheckAgent)
        {
            String[] loginInfo = getEngine().getLoginInfoFromRequest();
            if (loginInfo != null)
            {
                m_sUsername = loginInfo[0];
                m_sPassword = loginInfo[1];
            }
             
        }
        else
        {
            m_sUsername = decodePassword(m_sUsername);
            m_sPassword = decodePassword(m_sPassword);
        } 
    }

    private String decodePassword(String password) throws Exception {
        String result = password;
        try
        {
            StringBuilder sb = new StringBuilder(password);
            password = SupportClass.reverseString(sb).toString();
            //password = password + "=";
            result = MLSUtil.base64Decode(password);
        }
        catch (Exception exc)
        {
            getEngine().writeLine(exc.getMessage());
        }

        return result;
    }

}


