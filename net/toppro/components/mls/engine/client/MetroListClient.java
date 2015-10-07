//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:36 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.StringSupport;
import java.io.IOException;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.client.RETSURLConnection;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSUtil;

public class MetroListClient  extends RETSClient 
{
    public MetroListClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
    }

    /**
    * //  We override it because HTTP client requires rem script, while here it's not required.
    * // 
    *  @return  combination of FLAG_PICSCRIPT_INSIDE_MAINSCRIPT and FLAG_REMSCRIPT_INSIDE_MAINSCRIPT
    * //
    */
    //public override int getFlags()
    //{
    //    return 0;
    //}
    protected public void startRETSPicDownloading() throws Exception {
        MLSException exc = null;
        String picPath = getEngine().getResultsFolder();
        if (getCurrentScriptType() == SCRIPT_PIC)
        {
            if (retsLogin())
            {
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
                        try
                        {
                            m_sRequestURL = m_sGetObjectURL + ReqURL;
                            connectHTTP_ML(picPath + RecId + ".jpg",m_sRequestURL,m_retsConn,false);
                            getEngine().setProgress(++CountPics,TotalPics);
                        }
                        catch (MLSException mls_ex)
                        {
                            exc = mls_ex;
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

    private byte[] connectHTTP_ML(String sSaveto, String sReq, HttpURLConnectionEx hc, boolean isSetAuth) throws Exception {
        if (!(hc instanceof RETSURLConnection))
            return null;
         
        try
        {
            new MLSUtil();
            String request = MLSUtil.replaceSubStr(sReq," ","%20");
            String request2 = removeCgiFromReq(request);
            RETSURLConnection retsCon = (RETSURLConnection)hc;
            retsCon.setMethod(RETSURLConnection.HTTP_METHOD_GET);
            Environment environment = getEngine().getEnvironment();
            environment.checkStop();
            try
            {
                retsCon.connect();
            }
            catch (IOException ioe)
            {
                throw getEngine().createException(STRINGS.get_Renamed(STRINGS.SCRIPT_CLIENT_CANNOT_CONNECT));
            }

            setHttpHeader(retsCon,isSetAuth);
            retsCon.sendRequest(request2,getEngine());
            switch(retsCon.getStatusCode())
            {
                case 200: 
                    //HTTP successful
                    String sResponse = retsCon.getResponseTextAsText(1000);
                    String sReplyCode = MLSUtil.getPara(sResponse,"Code","=","\r");
                    if (sReplyCode == null || !sReplyCode.equals("20403"))
                    {
                        byte[] data = retsCon.getResponseTextAsBytes();
                        if (sSaveto != null && sSaveto.length() != 0)
                            writeToFile(sSaveto,data);
                         
                        return data;
                    }
                     
                    break;
                case 302: 
                    //Object moved
                    //Redirect to another URL to get result
                    sReq = MLSUtil.getSubString(sReq, "?", MLSUtil.STRING_LEFT, false);
                    sReq = sReq.substring(0, (0) + ((sReq.lastIndexOf("/")) - (0)));
                    String sLocation = retsCon.responsHeader.get_Renamed(HTTP_HEAD_LOCATION);
                    sReq += ("/" + StringSupport.Trim(sLocation));
                    connectHTTP(sSaveto,sReq,hc,false);
                    break;
                default: 
                    return null;
            
            }
            //HTTP failed
            // end of switch
            environment.checkStop();
        }
        catch (Exception e)
        {
            throw getEngine().createException(e);
        }

        return null;
    }

}


