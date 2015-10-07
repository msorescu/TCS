//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:39 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.StringSupport;
import java.util.Hashtable;
import net.toppro.components.mls.engine.client.Digest;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.client.RETSURLConnection;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSUtil;

public class SafeMLSClient  extends RETSClient 
{
    private String m_strDynPin = "";
    public SafeMLSClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
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
    /**
    * RETS Login transaction
    */
    protected public boolean retsLogin() throws Exception {
        Hashtable t = this.getEngine().getAdditionalParams();
        if (t == null)
            throw getEngine().createException(MLSException.CODE_SAFEMLS_PIN_REQUEST);
        else if (t.size() == 1)
        {
            IEnumerator e = EnumeratorSupport.mk(t.values().iterator());
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            e.moveNext();
            m_strDynPin = ((String)e.getCurrent());
        }
          
        return super.retsLogin();
    }

    protected public void setHttpHeader(HttpURLConnectionEx hc, boolean isSetAuth) throws Exception {
        if (hc != null)
        {
            RETSURLConnection retsCon = (RETSURLConnection)hc;
            retsCon.removeRequestProperty();
            retsCon.setRequestProperty("Accept","*/*");
            retsCon.setRequestProperty("User-Agent",m_HttpUserAgent);
            retsCon.setRequestProperty("Host",retsCon.getHost());
            retsCon.setRequestProperty("Pragma","no-cache");
            retsCon.setRequestProperty("RETS-Version",RETS_VERSION);
            retsCon.setRequestProperty("Content-Length","0");
            try
            {
                String hash = m_RetsUAUserAgent + ":" + m_RetsUAPwd;
                retsCon.setRequestProperty("RETS-UA-Authorization","Digest " + RETSURLConnection.createDigest(RETSURLConnection.createDigest(hash) + ":::" + RETS_VERSION));
            }
            catch (Exception ne)
            {
                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                System.out.println(ne.getMessage());
            }

            if (m_vCookies != null && m_vCookies.size() != 0)
            {
                String sCookie = "";
                for (int i = 0;i < m_vCookies.size();i++)
                {
                    if (sCookie.length() != 0)
                        sCookie += ";";
                     
                    sCookie += getCookie((String)m_vCookies.get(i));
                }
                if (sCookie.length() != 0)
                    retsCon.setRequestProperty("Cookie",sCookie);
                 
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
                        retsCon.setRequestProperty("Authorization","Basic " + MLSUtil.base64Encode(m_sUsername + ":" + m_sPassword + m_strDynPin));
                    }
                     
                }
                catch (Exception exc)
                {
                    throw getEngine().createException(exc);
                }
            
            }
             
        }
         
    }

}


