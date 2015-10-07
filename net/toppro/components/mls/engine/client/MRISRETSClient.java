//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:36 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.ObjectSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import net.toppro.components.mls.engine.client.ByteBuffer;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.client.RETSURLConnection;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSUtil;

public class MRISRETSClient  extends RETSClient 
{
    private static final String MainPicType = "";
    public MRISRETSClient(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        super(engine);
    }

    /**
    * RETS GetObject transaction
    */
    protected public boolean retsGetObject(String requestUrl, String sSaveto) throws Exception {
        boolean bResult = false;
        try
        {
            m_retsConn.setMethod(RETSURLConnection.HTTP_METHOD_GET);
            //setAcceptType( "image/jpeg" );
            String[] picList = pictureSearch(requestUrl);
            Boolean bFound = false;
            String mainPicUrl = "";
            String picType = getEngine().getMRISPrimaryPicType();
            if (!StringSupport.isNullOrEmpty(picType))
            {
                boolean matchFound = false;
                for (String pic : picList)
                {
                    //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                    if (matchFound)
                    {
                        break;
                    }
                     
                    String[] splitpicList = pic.split(StringSupport.charAltsToRegex(new char[]{ '\t' }));
                    for (int p = 0;p < splitpicList.length;p++)
                    {
                        if (ObjectSupport.Equals(picType, StringComparison.CurrentCultureIgnoreCase) == true)
                        {
                            matchFound = true;
                            bFound = true;
                            mainPicUrl = pic;
                            break;
                        }
                         
                    }
                }
            }
             
            if (picList.length > 0 && !bFound)
                mainPicUrl = picList[0];
             
            mainPicUrl = mainPicUrl.Substring(mainPicUrl.IndexOf("\t", System.StringComparison.Ordinal)).Trim();
            if (mainPicUrl.length() > 0)
            {
                connectHttpDirect(sSaveto,mainPicUrl);
                bResult = true;
            }
             
        }
        catch (Exception exc)
        {
            System.out.println(exc.getMessage());
        }

        return bResult;
    }

    protected public void multiPicFileDownload(String recordId, String url) throws Exception {
        try
        {
            String picType = getEngine().getMRISPrimaryPicType();
            m_retsConn.setMethod(RETSURLConnection.HTTP_METHOD_GET);
            String[] picList = pictureSearch(url);
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ arrFiles = new String[picList.length];
            if (!StringSupport.isNullOrEmpty(picType))
            {
                boolean matchFound = false;
                for (int j = 0;j < picList.length;j++)
                {
                    if (matchFound)
                    {
                        break;
                    }
                     
                    String[] splitpicList = picList[j].split(StringSupport.charAltsToRegex(new char[]{ '\t' }));
                    for (int p = 0;p < splitpicList.length;p++)
                    {
                        if (ObjectSupport.Equals(picType, StringComparison.CurrentCultureIgnoreCase) == true)
                        {
                            String temp = picList[j];
                            for (int n = j;n > 0;n--)
                            {
                                picList[n] = picList[n - 1];
                            }
                            picList[0] = temp;
                            matchFound = true;
                            break;
                        }
                         
                    }
                }
            }
             
            for (Int32 i = 0;i < picList.length;i++)
            {
                arrFiles[i] = getEngine().getResultsFolder() + recordId + "_" + Integer.toString(i + 1) + ".jpg";
                picList[i] = picList[i].Substring(picList[i].IndexOf("\t", System.StringComparison.Ordinal)).Trim();
                connectHttpDirect(arrFiles[i], picList[i]);
            }
            MLSRecord record = getEngine().getMLSRecord(recordId);
            record.setMultiPictureFileName(arrFiles);
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            System.out.println(exc.getMessage());
        }
    
    }

    protected public String[] pictureSearch(String url) throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ picList = new String[0];
        ByteBuffer readBuffer = new ByteBuffer();
        if (url.IndexOf(HTTP_PROTOCOL, System.StringComparison.Ordinal) == -1)
        {
            if (m_sSearchURL.IndexOf(HTTP_PROTOCOL, System.StringComparison.Ordinal) == -1)
                url = m_sBaseURL + m_sSearchURL + url;
            else
                url = m_sSearchURL + url; 
        }
         
        m_sURI = getSubUrl(m_sSearchURL);
        Int32 i = 0;
        byte[] buffer;
        do
        {
            buffer = connectHTTP(null,url,m_retsConn,true,null);
            i++;
        }
        while (buffer == null && i < 3);
        if (buffer != null)
        {
            for (i = 0;i < buffer.length;i++)
                readBuffer.append(buffer[i]);
        }
         
        String pics = new String(readBuffer.get_Renamed(), EncodingSupport.GetEncoder("ASCII").getString());
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ posBegin = pics.IndexOf("<DATA>", System.StringComparison.Ordinal);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ posEnd = pics.LastIndexOf("</DATA>", System.StringComparison.Ordinal);
        if (posBegin > -1 && posEnd > -1)
        {
            pics = pics.Substring(posBegin, (posEnd + 7) - (posBegin));
            picList = MLSUtil.stringSplitEx(pics,"</DATA>");
            for (i = 0;i < picList.Length;i++)
                picList[i] = MLSUtil.replaceSubStr(picList[i], "<DATA>", "").Trim();
        }
         
        return picList;
    }

}


