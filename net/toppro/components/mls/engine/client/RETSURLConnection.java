//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:39 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.NumberSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import java.net.URI;
import java.util.ArrayList;
import net.toppro.components.mls.engine.client.Digest;
import net.toppro.components.mls.engine.client.HttpHeader;
import net.toppro.components.mls.engine.client.HttpHeaderItem;
import net.toppro.components.mls.engine.client.HttpURLConnectionEx;
import net.toppro.components.mls.engine.client.QuotedPrintable;
import net.toppro.components.mls.engine.client.RETSClient;
import net.toppro.components.mls.engine.StringTokenizerEx;

public class RETSURLConnection  extends HttpURLConnectionEx 
{
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String NONCE_COUNT = "00000001";
    public static final String NONCE = "TOPPRODUCER";
    public static final String LOCATION = "TOPPRODUCER";
    private static final boolean bDebug = true;
    private String m_Authorization = "";
    private Digest m_Digest = null;
    private String m_sMethod;
    private String m_sHttpVersion;
    private ArrayList m_vCookies;
    public RETSURLConnection(URI url) throws Exception {
        super(url);
        m_vCookies = ArrayList.Synchronized(new ArrayList(10));
        m_sMethod = HTTP_METHOD_POST;
        m_sHttpVersion = RETSClient.HTTP_VERSION;
    }

    public void setHttpVersion(String version) throws Exception {
        m_sHttpVersion = version;
    }

    public void setMethod(String method_name) throws Exception {
        m_sMethod = method_name;
    }

    public String getMethod() throws Exception {
        return m_sMethod;
    }

    public void sendRequest(String uri, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        int retsWait = mlsEngine.getRETSWait();
        if (retsWait > 0)
        {
            Thread.sleep(retsWait);
            mlsEngine.writeLine("... wait " + retsWait + " millisecond\r\n");
        }
         
        //System.IO.StreamWriter temp_writer;
        //temp_writer = new System.IO.StreamWriter(GetRequestStream(), System.Text.Encoding.Default);
        //temp_writer.AutoFlush = false;
        //System.IO.StreamWriter pw = temp_writer;
        mlsEngine.writeLine("\r\n===>>>Send:\r\n",true);
        URI reqUri = new URI(uri);
        String request = "";
        if (!useRelativePath())
        {
            uri = reqUri.AbsoluteUri;
            if (StringSupport.Compare(m_sMethod, HTTP_METHOD_GET, true) != 0)
            {
                int pos = uri.indexOf('?');
                if (pos > -1)
                    uri = uri.substring(0, (0) + (pos));
                 
            }
             
        }
        else
        {
            if (StringSupport.Compare(m_sMethod, HTTP_METHOD_GET, true) == 0)
                uri = reqUri.PathAndQuery;
            else
                uri = reqUri.AbsolutePath; 
        } 
        String sHttpReq = m_sMethod + " " + uri + " HTTP/" + m_sHttpVersion + "\r\n";
        //mlsEngine.WriteLine(sHttpReq);
        //pw.Write(sHttpReq);
        String postString = reqUri.Query.Trim();
        if (!StringSupport.isNullOrEmpty(postString))
        {
            if (postString.startsWith("?"))
                postString = postString.substring(1);
             
        }
         
        if (StringSupport.Compare(m_sMethod, HTTP_METHOD_GET, true) == 0)
            postString = "";
         
        HttpHeaderItem reqHeadItem = requestHeader.getFirst();
        while (reqHeadItem != null)
        {
            if (StringSupport.Compare(reqHeadItem.getKeyName(), "Content-Length", true) == 0 && !StringSupport.isNullOrEmpty(postString))
                sHttpReq += reqHeadItem.getKeyName() + ": " + postString.length() + "\r\n" + "Content-Type: application/x-www-form-urlencoded\r\n";
            else
                sHttpReq += reqHeadItem.getKeyName() + ": " + StringSupport.Trim(reqHeadItem.getValue()) + "\r\n"; 
            reqHeadItem = requestHeader.getNext();
        }
        sHttpReq += "\r\n";
        if (StringSupport.Compare(m_sMethod, HTTP_METHOD_GET, true) != 0 && !StringSupport.isNullOrEmpty(postString))
            sHttpReq = sHttpReq + postString;
         
        mlsEngine.writeLine(sHttpReq,true);
        byte[] byteRequest = EncodingSupport.GetEncoder("ASCII").getBytes(sHttpReq);
        // try
        // {
        createSockConnection();
        m_stream = new NetworkStream(sock);
        if (reqUri.Scheme.IndexOf("https", StringComparison.CurrentCultureIgnoreCase) > -1)
        {
            m_stream = new SslStream(m_stream);
            ((SslStream)m_stream).AuthenticateAsClient(reqUri.Host);
        }
         
        //IPAddress ipAddress = Dns.GetHostAddresses(reqUri.Host)[0];
        //IPEndPoint ipLocalEndPoint = new IPEndPoint(ipAddress, reqUri.Port);
        m_stream.write(byteRequest,0,byteRequest.length);
        m_stream.Flush();
        //Parse feedback as it comes in.  First line is the status.
        Read(mlsEngine);
        if (ResponseText == null)
        {
            m_stream.close();
            sock.close();
            m_bSocketClosed = true;
            return ;
        }
         
        //}
        // catch (Exception exc)
        // { mlsEngine.WriteLine(exc.Message); }
        //Parse feedback as it comes in.  First line is the status.
        //string s = Encoding.ASCII.GetString(bb.get_Renamed());
        //mlsEngine.WriteLine(s);
        //StatusText = "";
        //ReasonLine = "";
        //ResponseHeaders = "";
        //ResponseText = new ByteBuffer();
        //int i,j,Code;
        ////String Code = "";
        ////UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //if ((j = s.IndexOf("\r\n", 0)) != - 1)
        //{
        //    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //    StatusText = s.Substring(0, (s.IndexOf("\r\n", 0)) - (0));
        //    Code = parseStatusCode();
        //    if (Code == 100)
        //    // New for RETS/1.5 Continue to get next status code
        //    {
        //        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //        if ((j + new System.Text.StringBuilder("\r\n").ToString().Length) < s.Length && (i = SupportClass.getIndex(s,"\r\n\r\n", s.IndexOf("\r\n", 0) + 1)) != - 1)
        //        {
        //            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //            s = s.Substring(SupportClass.getIndex(s,"\r\n\r\n", s.IndexOf("\r\n", 0) + 1) + 4, (s.Length - 1) - (SupportClass.getIndex(s,"\r\n\r\n", s.IndexOf("\r\n", 0) + 1) + 4));
        //            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //            StatusText = s.Substring(0, (s.IndexOf("\r\n", 0)) - (0));
        //        }
        //    }
        //}
        ////UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //if ((j + new System.Text.StringBuilder("\r\n").ToString().Length) < s.Length && (i = SupportClass.getIndex(s,"\r\n\r\n", s.IndexOf("\r\n", 0) + 1)) != - 1)
        //{
        //    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //    ResponseHeaders = s.Substring(s.IndexOf("\r\n", 0) + 2, (SupportClass.getIndex(s,"\r\n\r\n", s.IndexOf("\r\n", 0) + 1)) - (s.IndexOf("\r\n", 0) + 2));
        //}
        //for (i = s.IndexOf("\r\n\r\n", 0) + 4; i < bb.size(); i++)
        //    ResponseText.append(bb.get_Renamed(i));
        //Code = parseStatusCode();
        //if (sock.Connected)
        //    mlsEngine.WriteLine("Socket is still connected.");
        /**
        * /sock.Close();
        */
        //System.String sAuthenticate = responsHeader.get_Renamed(HttpHeader.HTTP_HEAD_AUTHEN);
        //responsHeader.clear();
        //if (sAuthenticate != null)
        //    responsHeader.add(HttpHeader.HTTP_HEAD_AUTHEN, sAuthenticate);
        //parseHeaders();
        String TransferEncoding = getTransferEncoding();
        if (TransferEncoding != null && TransferEncoding.toUpperCase().equals(TRANSFER_ENCODING_CHUNKED.toUpperCase()))
        {
            byte[] postbuffer = postChunkedProcessing(ResponseText.ToArray(), ResponseText.Length);
            ResponseText = new MemoryStream(postbuffer);
        }
         
        String ContentTransferEncoding = getContentTransferEncoding();
        if (ContentTransferEncoding != null)
        {
            if (ContentTransferEncoding.toUpperCase().equals(ENCODING_EIGHTBIT.toUpperCase()))
            {
            }
            else // do nothing
            if (ContentTransferEncoding.toUpperCase().equals(ENCODING_QUOTEDPRINTABLE.toUpperCase()))
            {
                //sbyte[] qpdbuffer = quotedPrintableDecode(ResponseText.get_Renamed(), ResponseText.size());
                ResponseText = new MemoryStream(EncodingSupport.GetEncoder("ASCII").GetBytes(QuotedPrintable.Decode(EncodingSupport.GetEncoder("ASCII").GetString(ResponseText.ToArray()))));
            }
            else //ResponseText = new ByteBuffer(qpdbuffer, qpdbuffer.Length);
            if (ContentTransferEncoding.toUpperCase().equals(ENCODING_BASE64.toUpperCase()))
            {
            }
               
        }
         
    }

    //byte[] b64buffer = MLSUtil.base64Decode( ResponseText.get() );
    //ResponseText = new ByteBuffer( b64buffer, b64buffer.length );
    //System.out.println("StatusText:" + StatusText);
    //System.out.println("ResponseHeaders:" + ResponseHeaders);
    //System.out.println("ResponseText:" + new String(ResponseText.get()));
    /*		}
    			catch(java.io.InterruptedIOException e)
    			{
    			throw getEngine().createException ( e, STRINGS.get ( STRINGS.S7_0 ) );
    			}*/
    //protected internal override void  parseHeaders()
    //{
    //    System.String sAuthenticate = responsHeader.get_Renamed(HttpHeader.HTTP_HEAD_AUTHEN);
    //    responsHeader.clear();
    //    if (sAuthenticate != null)
    //        responsHeader.add(HttpHeader.HTTP_HEAD_AUTHEN, sAuthenticate);
    //    MatchCollection matches = new Regex("[^\r\n]+").Matches(ResponseHeaders.TrimEnd('\r', '\n'));
    //    StatusText = matches[0].Value;
    //    for (int n = 1; n < matches.Count; n++)
    //    {
    //        string[] strItem = matches[n].Value.Split(new char[] { ':' },2);
    //        if (strItem.Length > 0)
    //        {
    //            if (!strItem[0].Trim().ToUpper().Equals("SET-COOKIE"))
    //                responsHeader.add(strItem[0].Trim(), strItem[1].Trim());
    //            else
    //            {
    //                string[] strCookies = strItem[1].Trim().Split( new char[]{';'});
    //                string strCookieName = "";
    //                for(int j = 0; j < strCookies.Length; j++)
    //                {
    //                    //m_vCookies.Insert(0, strCookies[j]);
    //                    bool found = false;
    //                    for (int l = 0; l < m_vCookies.Count; l++)
    //                    {
    //                        if (strCookies[j].ToUpper().CompareTo(((string)m_vCookies[l]).ToUpper()) == 0)
    //                        {
    //                            found = true;
    //                            break;
    //                        }
    //                    }
    //                    if(!found)
    //                        m_vCookies.Insert(0, strCookies[j]);
    //                }
    //            }
    //        }
    //    }
    //    if (responsHeader.isDigestAuthentication())
    //        responsHeader.parseDigest();
    //}
    protected public void parseHeaders() throws Exception {
        String sAuthenticate = responsHeader.get_Renamed(HttpHeader.HTTP_HEAD_AUTHEN);
        responsHeader.clear();
        if (sAuthenticate != null)
            responsHeader.add(HttpHeader.HTTP_HEAD_AUTHEN,sAuthenticate);
         
        int index = 0;
        String tmp = "";
        SupportClass.Tokenizer u = new SupportClass.Tokenizer(ResponseHeaders,"\r\n");
        String headerName = "";
        String headerValues = "";
        boolean multiline = false;
        StatusText = u.nextToken();
        while (u.hasMoreTokens())
        {
            String header = u.nextToken();
            StringTokenizerEx v = new StringTokenizerEx(header,": ");
            v.hasMoreElements();
            if (!multiline)
                headerName = v.nextElement();
            else
            {
                tmp = v.nextElement();
                // nextToken();
                if (tmp.charAt(0) == '\t' || tmp.charAt(0) == ' ')
                    headerValues += StringSupport.Trim(v.nextElement());
                else
                {
                    // nextToken();
                    responsHeader.add(headerName,headerValues);
                    headerName = "";
                    headerValues = "";
                    headerName = tmp;
                    multiline = false;
                } 
            } 
            if (!multiline && (index = header.indexOf(" ")) != -1)
            {
                headerValues = header.Substring(index, (header.Length)-(index));
                if (StringSupport.Trim(headerValues).endsWith(";"))
                {
                    headerValues = StringSupport.Trim(headerValues);
                    multiline = true;
                }
                 
            }
            else if (!StringSupport.Trim(headerValues).endsWith(";"))
                multiline = false;
              
            if (StringSupport.Trim(headerName).toUpperCase().equals("Set-Cookie".toUpperCase()))
            {
                boolean bFound = false;
                for (int l = 0;l < m_vCookies.size();l++)
                {
                    if (headerValues.toUpperCase().equals(((String)m_vCookies.get(l)).toUpperCase()))
                    {
                        bFound = true;
                        break;
                    }
                     
                }
                if (!bFound)
                    m_vCookies.add(0, headerValues);
                 
            }
             
            if (!multiline)
            {
                responsHeader.add(headerName,headerValues);
                headerName = "";
                headerValues = "";
            }
             
        }
        if (headerName.length() != 0 && headerValues.length() != 0)
            responsHeader.add(headerName,headerValues);
         
        if (responsHeader.isDigestAuthentication())
            responsHeader.parseDigest();
         
    }

    public void clearCookies() throws Exception {
        if (m_vCookies != null)
            m_vCookies.clear();
         
    }

    public ArrayList getCookies() throws Exception {
        return (ArrayList)m_vCookies.clone();
    }

    /**
    * Create message digest according to RFC2069
    */
    public String createDigestRFC2069(String sUsername, String sPassword, String sUri) throws Exception {
        //if( m_Authorization.length() != 0 )
        //	return m_Authorization;
        Digest digest = this.responsHeader.getDigest();
        if (digest == null || m_sMethod == null)
            return null;
         
        String sRealm = digest.m_sRealm;
        String sFlatInfo = sUsername + ":" + sRealm + ":" + sPassword;
        String sDigest = null;
        sDigest = createDigest(sFlatInfo);
        String sA2 = m_sMethod + ":" + sUri;
        String sTemp = createDigest(sA2);
        String sTmp = sDigest + ":" + digest.m_sNonce + ":" + sTemp;
        sDigest = createDigest(sTmp);
        sDigest = "username=\"" + sUsername + "\"," + "realm=\"" + digest.m_sRealm + "\"," + "nonce=\"" + digest.m_sNonce + "\"," + "opaque=\"" + digest.m_sOpaque + "\"," + "uri=\"" + sUri + "\"," + "response=\"" + sDigest + "\"" + "\r\n";
        m_Authorization = sDigest;
        return sDigest;
    }

    /**
    * Create message digest according to RFC2617 , but with GardenState's tricks
    */
    public String createDigestRFC2617_GS(String sUsername, String sPassword, String sUri, String RetsUAPwd) throws Exception {
        //		if( m_Authorization.length() != 0 )
        //			return m_Authorization;
        Digest digest = this.responsHeader.getDigest();
        if (digest == null || m_sMethod == null)
            return null;
         
        String sRealm = digest.m_sRealm;
        String sFlatInfo = sUsername + ":" + sRealm + ":" + sPassword;
        String sDigest = null;
        sDigest = createDigest(sFlatInfo);
        String sT1 = m_sMethod + ":" + sUri;
        String sE1 = createDigest(sT1);
        String sNonce = digest.m_sNonce;
        String sQop = digest.m_sQop;
        //String strCNonce = RETSClient.USER_AGENT + ":" + "9e7UdP(" + "::" +  sNonce;
        String strCNonce = RETSClient.USER_AGENT + ":" + RetsUAPwd + "::" + sNonce;
        strCNonce = createDigest(strCNonce);
        String sTmp = sDigest + ":" + sNonce + ":" + NONCE_COUNT + ":" + strCNonce + ":" + sQop + ":" + sE1;
        sDigest = createDigest(sTmp);
        sDigest = "username=\"" + sUsername + "\"," + "realm=\"" + digest.m_sRealm + "\"," + "nonce=\"" + digest.m_sNonce + "\"," + "opaque=\"" + digest.m_sOpaque + "\"," + "uri=\"" + sUri + "\"," + "qop=\"" + digest.m_sQop + "\"," + "nc=" + NONCE_COUNT + "," + "cnonce=\"" + strCNonce + "\"," + "response=\"" + sDigest + "\"" + "\r\n";
        m_Authorization = sDigest;
        return sDigest;
    }

    /**
    * Create message digest according to RFC2617
    */
    public String createDigestRFC2617(String sUsername, String sPassword, String sUri) throws Exception {
        Digest digest = this.responsHeader.getDigest();
        if (digest == null || m_sMethod == null)
            return null;
         
        String sRealm = digest.m_sRealm;
        String sFlatInfo = sUsername + ":" + sRealm + ":" + sPassword;
        String sDigest = null;
        sDigest = createDigest(sFlatInfo);
        String sT1 = m_sMethod + ":" + sUri;
        String sE1 = createDigest(sT1);
        String sNonce = digest.m_sNonce;
        String sQop = digest.m_sQop;
        String sTmp = sDigest + ":" + sNonce + ":" + NONCE_COUNT + ":" + NONCE + ":" + sQop + ":" + sE1;
        sDigest = createDigest(sTmp);
        sDigest = "username=\"" + sUsername + "\"," + "realm=\"" + digest.m_sRealm + "\"," + "nonce=\"" + digest.m_sNonce + "\"," + "opaque=\"" + digest.m_sOpaque + "\"," + "uri=\"" + sUri + "\"," + "qop=\"" + digest.m_sQop + "\"," + "nc=" + NONCE_COUNT + "," + "cnonce=\"" + NONCE + "\"," + "response=\"" + sDigest + "\"" + "\r\n";
        m_Authorization = sDigest;
        return sDigest;
    }

    public static String createDigest(String sFlatInfo) throws Exception {
        MD5 md = new MD5CryptoServiceProvider();
        byte[] bs = EncodingSupport.GetEncoder("UTF-8").getBytes(sFlatInfo);
        bs = md.ComputeHash(bs);
        StringBuilder s = new StringBuilder();
        for (byte b : bs)
        {
            s.append(NumberSupport.format(b, "x2").toLowerCase());
        }
        return s.toString();
    }

}


//Compute the hash value from the array of bytes.
//String digest = BitConverter.ToString(md.ComputeHash(Encoding.ASCII.GetBytes(sFlatInfo)));
//return digest;
//System.String sDigest = null;
//SupportClass.MessageDigestSupport md = SupportClass.MessageDigestSupport.GetInstance("MD5");
//md.Update(SupportClass.ToByteArray(sFlatInfo));
//sbyte[] bytesMD5Digest = md.DigestData();
//System.String sTmp = new string(SupportClass.ToCharArray(SupportClass.ToByteArray(bytesMD5Digest)));//bytesMD5Digest.ToString();
//sDigest = sTmp.Substring(sTmp.IndexOf("<") + 1, (sTmp.IndexOf(">")) - (sTmp.IndexOf("<") + 1));
//return sDigest;