//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:34 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.StringSupport;
import net.toppro.components.mls.engine.client.Digest;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.client.RETSURLConnection;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSUtil;

/**
* This is the implelemtation of MRISClient.   Ilya Solnyshkin
*/
public class GardenStateClient  extends RETSClient 
{
    public GardenStateClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
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
            endString = url.substring(pos + 1);
         
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
                if (i > 0 && _bytes[i - 1] == (byte)'\r')
                {
                    if (i < (_bytes.length - 2))
                    {
                        if (foundPicStartLine)
                        {
                            foundPicStart = true;
                            foundPicStartLine = false;
                            _picStart = i + 1;
                        }
                        else if (_bytes[i + 1] == (byte)'O' && _bytes[i + 2] == (byte)'b')
                        {
                            foundPicStartLine = true;
                        }
                        else if (i > 16 && (_bytes[i - 16] == (byte)'-' && _bytes[i - 17] == (byte)'-'))
                        {
                            if (foundPicStart)
                            {
                                foundPicEnd = true;
                                _picEnd = i - 18;
                            }
                             
                        }
                           
                    }
                    else
                    {
                        if (i > 18 && ((_bytes[i - 18] == (byte)'-' && _bytes[i - 19] == (byte)'-')))
                        {
                            if (foundPicStart)
                            {
                                foundPicEnd = true;
                                _picEnd = i - 20;
                            }
                             
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

    protected public void setHttpHeader(HttpURLConnectionEx hc, boolean isSetAuth) throws Exception {
        if (hc != null)
        {
            RETSURLConnection retsCon = (RETSURLConnection)hc;
            retsCon.removeRequestProperty();
            retsCon.setRequestProperty("Accept","*/*");
            retsCon.setRequestProperty("User-Agent",m_HttpUserAgent);
            retsCon.setRequestProperty("Host",retsCon.getHost());
            retsCon.setRequestProperty("Pragma","no-cache");
            retsCon.setRequestProperty("RETS-Version","RETS/1.0");
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
                            sDigest = retsCon.createDigestRFC2617_GS(m_sUsername,m_sPassword,m_sURI,m_RetsUAPwd);
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

}


