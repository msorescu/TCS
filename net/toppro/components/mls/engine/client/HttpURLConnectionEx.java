//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:35 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.Net.IPAddress;
import CS2JNet.System.Net.IPEndPoint;
import CS2JNet.System.Net.Sockets.AddressFamily;
import CS2JNet.System.Net.Sockets.NetSocket;
import CS2JNet.System.Net.Sockets.ProtocolType;
import CS2JNet.System.Net.Sockets.SocketType;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import java.io.InputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import net.toppro.components.mls.engine.client.ByteBuffer;
import net.toppro.components.mls.engine.client.HttpHeader;
import net.toppro.components.mls.engine.client.HttpHeaderItem;
import net.toppro.components.mls.engine.MLSException;

/**
* Class HttpURLConnectionEx is extention of URLConnection.
* We need in this class, because we have MS bug when use Java Applet and URLConnection
*/
public class HttpURLConnectionEx   
{
    public static final String REASON_LINE_NONCE_EXPIRED = "Nonce Expired";
    public static final String ENCODING_BASE64 = "base64";
    public static final String ENCODING_UUENCODE = "uuencode";
    public static final String ENCODING_QUOTEDPRINTABLE = "quoted-printable";
    public static final String ENCODING_SEVENBIT = "7bit";
    public static final String ENCODING_EIGHTBIT = "8bit";
    public static final String ENCODING_BINARY = "binary";
    public static final String TRANSFER_ENCODING_CHUNKED = "chunked";
    public static final int DEFAULT_PORT = 80;
    public static final int FTP_PORT = 21;
    protected public String ReasonLine = "";
    //protected internal System.IO.Stream input = null;
    //protected internal System.IO.Stream output = null;
    protected public HttpHeader requestHeader = null;
    protected public HttpHeader responsHeader = null;
    protected public String StatusText;
    protected public MemoryStream ResponseText = new MemoryStream();
    protected public String ResponseHeaders;
    protected public InputStream m_stream;
    protected public NetSocket sock;
    protected public boolean m_bSocketClosed = true;
    protected public boolean m_bConnectionClose = false;
    public int Status = 0;
    protected URI url;
    protected public IPEndPoint m_ipLocalEndPoint;
    protected boolean m_useRelativePath = false;
    /**
    * Constructor with specific URL
    *  @param url URL for connect
    */
    public HttpURLConnectionEx(URI uri) throws Exception {
        url = uri;
        //input = null;
        //output = null;
        requestHeader = new HttpHeader();
        responsHeader = new HttpHeader();
        try
        {
            IPAddress ipAddress = Dns.GetHostAddresses(url.Host)[0];
            m_ipLocalEndPoint = new IPEndPoint(ipAddress, url.Port);
        }
        catch (Exception exc)
        {
            throw new MLSException(MLSException.CODE_TIMEOUT);
        }
    
    }

    /**
    * connect to server
    *  @throws IOException
    */
    //UPGRADE_NOTE: The equivalent of method 'java.net.URLConnection.connect' is not an override method. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1143'"
    public void connect() throws Exception {
        try
        {
            //UPGRADE_TODO: Method 'java.net.URL.getPort' was converted to 'System.Uri.Port' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javanetURLgetPort'"
            //UPGRADE_ISSUE: Field 'java.net.URLConnection.url' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javanetURLConnectionurl_f'"
            if (sock != null)
                return ;
             
            int port = url.Port;
            if (port == -1)
            {
                //UPGRADE_ISSUE: Field 'java.net.URLConnection.url' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javanetURLConnectionurl_f'"
                if (url.Scheme.equals("ftp"))
                    port = FTP_PORT;
                else
                    port = DEFAULT_PORT; 
            }
             
            //UPGRADE_ISSUE: Field 'java.net.URLConnection.url' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javanetURLConnectionurl_f'"
            sock = new NetSocket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            //IPAddress ipAddress = Dns.GetHostAddresses(url.Host)[0];
            //IPEndPoint ipLocalEndPoint = new IPEndPoint(ipAddress, url.Port);
            //sock.Connect(ipLocalEndPoint);
            //input = sock.GetStream();
            //output = sock.GetStream();
            //HaveResponse = true;
            m_bSocketClosed = false;
        }
        catch (Exception exc)
        {
            throw new MLSException(MLSException.CODE_TIMEOUT);
        }
    
    }

    protected void createSockConnection() throws Exception {
        try
        {
            if (!sock.Connected)
            {
                sock = new NetSocket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                sock.Connect(m_ipLocalEndPoint);
            }
             
        }
        catch (Exception exc)
        {
            throw new MLSException(MLSException.CODE_TIMEOUT);
        }
    
    }

    public void disconnect() throws Exception {
        if (!m_bSocketClosed)
        {
            try
            {
                m_stream.close();
                sock.close();
                m_bSocketClosed = true;
            }
            catch (Exception e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        
        }
         
        closeMemoryStream();
    }

    public void closeMemoryStream() throws Exception {
        try
        {
            if (ResponseText != null)
                ResponseText.Dispose();
             
        }
        catch (Exception e)
        {
        }
    
    }

    public boolean isSocketClosed() throws Exception {
        if (sock == null)
            return false;
         
        return m_bSocketClosed;
    }

    public boolean isConnectionClose() throws Exception {
        return m_bConnectionClose;
    }

    public void setConnectionClose(boolean value) throws Exception {
        m_bConnectionClose = value;
    }

    public void setUseRelativePath(boolean b) throws Exception {
        m_useRelativePath = b;
    }

    protected boolean useRelativePath() throws Exception {
        return m_useRelativePath;
    }

    public String getHost() throws Exception {
        return url.Host;
    }

    /**
    * Sends GET request to Http Server
    *  @param uri URL
    */
    public void sendGetRequest(String uri, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        StatusText = "";
        ResponseHeaders = "";
        ReasonLine = "";
        //ResponseText = new ByteBuffer();
        URI reqUri = new URI(uri);
        //System.IO.StreamWriter temp_writer;
        //temp_writer = new System.IO.StreamWriter(GetRequestStream(), System.Text.Encoding.Default);
        //temp_writer.AutoFlush = false;
        //System.IO.StreamWriter pw = temp_writer;
        String request = "";
        if (!useRelativePath())
            request = "GET " + reqUri.AbsoluteUri + " HTTP/1.0\r\n";
        else
            request = "GET " + reqUri.PathAndQuery + " HTTP/1.0\r\n"; 
        HttpHeaderItem reqHeadItem = requestHeader.getFirst();
        while (reqHeadItem != null)
        {
            request += reqHeadItem.getKeyName() + ": " + reqHeadItem.getValue() + "\r\n";
            reqHeadItem = requestHeader.getNext();
        }
        mlsEngine.writeLine(request);
        byte[] byteRequest = EncodingSupport.GetEncoder("ASCII").getBytes(request + "\r\n");
        //IPAddress ipAddress = Dns.GetHostAddresses(reqUri.Host)[0];
        //IPEndPoint ipLocalEndPoint = new IPEndPoint(ipAddress, reqUri.Port);
        createSockConnection();
        m_stream = new NetworkStream(sock);
        //if (uri.IndexOf("https",StringComparison.CurrentCultureIgnoreCase) > -1)
        if (reqUri.Scheme.IndexOf("https", StringComparison.CurrentCultureIgnoreCase) > -1)
        {
            m_stream = new SslStream(m_stream);
            ((SslStream)m_stream).AuthenticateAsClient(reqUri.Host);
        }
         
        //IPAddress ipAddress = Dns.GetHostAddresses(reqUri.Host)[0];
        //IPEndPoint ipLocalEndPoint = new IPEndPoint(ipAddress, reqUri.Port);
        m_stream.write(byteRequest,0,byteRequest.length);
        m_stream.Flush();
        //sock.Send(byteRequest,SocketFlags.None);
        int count = 0;
        int i, j;
        int Code;
        ByteBuffer bb = new ByteBuffer();
        sock.ReceiveTimeout = 300000;
        Read(mlsEngine);
        if (ResponseText == null)
        {
            m_stream.close();
            sock.close();
            m_bSocketClosed = true;
            return ;
        }
         
        //string s = System.Text.Encoding.ASCII.GetString(bb.get_Renamed());
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //if ((j = s.IndexOf("\r\n", 0)) != - 1)
        //{
        //    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //    StatusText = s.Substring(0, (s.IndexOf("\r\n", 0)) - (0));
        //    Code = parseStatusCode();
        //    if (Code == 100)
        //    // New for RETS/1.5 Continue to get next status code
        //    {
        //        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //        if ((j + new System.Text.StringBuilder("\r\n").ToString().Length) < s.Length && (i = s.IndexOf("\r\n\r\n", s.IndexOf("\r\n", 0) + 1)) != - 1)
        //        {
        //            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //            s = s.Substring(s.IndexOf("\r\n\r\n", s.IndexOf("\r\n", 0) + 1), (s.Length) - (s.IndexOf("\r\n\r\n", s.IndexOf("\r\n", 0) + 1)));
        //            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //            StatusText = s.Substring(0, (s.IndexOf("\r\n", 0)) - (0));
        //        }
        //    }
        //}
        ////UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //if ((j + new System.Text.StringBuilder("\r\n").ToString().Length) < s.Length && (i = s.IndexOf("\r\n\r\n", s.IndexOf("\r\n", 0) + 1)) != - 1)
        //{
        //    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //    ResponseHeaders = s.Substring(s.IndexOf("\r\n", 0) + 2, (s.IndexOf("\r\n\r\n", s.IndexOf("\r\n", 0) + 1)) - (s.IndexOf("\r\n", 0) + 2));
        //}
        ////UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        ////for (i = s.IndexOf("\r\n\r\n", 0) + 4; i < bb.size(); i++)
        ////    ResponseText.append(bb.get_Renamed(i));
        //for (i = s.IndexOf("\r\n\r\n", 0) + 4; i < bb.size(); i++)
        //    ResponseText.append(bb.get_Renamed(i));
        //Code = parseStatusCode();
        //sock.Close();
        //m_bSocketClosed = true;
        //parseHeaders();
        //The following logic is for Trend to get multiple pictures, if "nophoto.jpg"
        //found in header content-location, then stop getting the next picture.
        String content_location = getHeaderField("Content-Location");
        if (content_location != null && content_location.length() > 0)
            if (content_location.toLowerCase().indexOf("nophoto.jpg") > -1)
                ResponseText = null;
             
         
        String TransferEncoding = getTransferEncoding();
        if (TransferEncoding != null && TransferEncoding.length() != 0 && TransferEncoding.toUpperCase().equals(TRANSFER_ENCODING_CHUNKED.toUpperCase()))
        {
            byte[] postbuffer = postChunkedProcessing(ResponseText.ToArray(), ResponseText.Length);
            closeMemoryStream();
            ResponseText = new MemoryStream(postbuffer);
        }
         
        String ContentTransferEncoding = getContentTransferEncoding();
        if (ContentTransferEncoding != null && ContentTransferEncoding.length() != 0 && ContentTransferEncoding.toUpperCase().equals(ENCODING_EIGHTBIT.toUpperCase()))
        {
        }
         
    }

    //mlsEngine.WriteLine("StatusText:" + StatusText);
    //mlsEngine.WriteLine("ResponseHeaders:" + ResponseHeaders);
    //if(ResponseText != null)
    //    mlsEngine.WriteLine("ResponseText:" + System.Text.Encoding.ASCII.GetString(ResponseText.ToArray()));
    /**
    * Parses Status Code from server's response
    *  @return  Status Code
    */
    protected public int parseStatusCode() throws Exception {
        Status = 0;
        ReasonLine = "";
        if (StatusText.length() == 0)
            return 0;
         
        SupportClass.Tokenizer u = new SupportClass.Tokenizer(StatusText," ");
        if (u.hasMoreTokens())
            u.nextToken();
        else
        {
            Status = 0;
            ReasonLine = "";
            return 0;
        } 
        try
        {
            Status = Integer.valueOf(u.nextToken());
            while (u.hasMoreTokens())
            {
                if (ReasonLine.length() != 0)
                    ReasonLine += " ";
                 
                ReasonLine += u.nextToken();
            }
        }
        catch (Exception e)
        {
            Status = 0;
            ReasonLine = "";
        }

        return Status;
    }

    /**
    * Parses and Creates Http Headers from server's response
    */
    protected public void parseHeaders() throws Exception {
        responsHeader.clear();
        SupportClass.Tokenizer u = new SupportClass.Tokenizer(ResponseHeaders,"\r\n");
        String headerName = "";
        String headerValues = "";
        StatusText = u.nextToken();
        while (u.hasMoreTokens())
        {
            String header = u.nextToken();
            SupportClass.Tokenizer v = new SupportClass.Tokenizer(header,": ");
            headerName = v.nextToken();
            try
            {
                v.nextToken(" ");
                headerValues = v.nextToken("");
            }
            catch (Exception e)
            {
                headerValues = "";
            }

            responsHeader.add(headerName,headerValues);
        }
    }

    /**
    * Gets Response Context as String
    */
    public String getResponseTextAsText() throws Exception {
        if (ResponseText == null || ResponseText.Length == 0)
            return null;
         
        return EncodingSupport.GetEncoder("ASCII").GetString(ResponseText.ToArray());
    }

    // Hot fix for Norton IS
    public String getResponseTextAsText(int textLength) throws Exception {
        if (ResponseText == null || ResponseText.Length == 0)
            return null;
         
        // Hot fix for Norton IS
        if (ResponseText.Length < textLength)
            return EncodingSupport.GetEncoder("ASCII").GetString(ResponseText.ToArray());
        else
        {
            byte[] firstPartBytes = new byte[textLength];
            int read = ResponseText.Read(firstPartBytes, 0, textLength);
            return new String(firstPartBytes, 0, textLength, EncodingSupport.GetEncoder("ASCII").getCharset());
        } 
    }

    /**
    * Gets Response Context as bytes
    */
    public byte[] getResponseTextAsBytes() throws Exception {
        if (ResponseText == null || ResponseText.Length == 0)
            return null;
         
        return ResponseText.ToArray();
    }

    // Hot fix for Norton IS
    //return ResponseText.get();
    public String getReasonLine() throws Exception {
        return ReasonLine;
    }

    public String contentType() throws Exception {
        return getHeaderField("Content-Type");
    }

    public String getTransferEncoding() throws Exception {
        return getHeaderField("Transfer-Encoding");
    }

    public String getContentTransferEncoding() throws Exception {
        return getHeaderField("Content-transfer-encoding");
    }

    public int contentLength() throws Exception {
        int nContentLength = 0;
        String len = getHeaderField("Content-Length");
        if (len != null && len.length() != 0)
        {
            try
            {
                nContentLength = Integer.valueOf(StringSupport.Trim(len));
            }
            catch (Exception e)
            {
                nContentLength = 0;
            }
        
        }
         
        return nContentLength;
    }

    /**
    * Get Currectly Status Code
    */
    public int getStatusCode() throws Exception {
        return Status;
    }

    /**
    * Sets Property for Request
    *  @param p1 Propert's Name
    * 
    *  @param p2 Propert's Value
    */
    //UPGRADE_NOTE: The equivalent of method 'java.net.URLConnection.setRequestProperty' is not an override method. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1143'"
    public void setRequestProperty(String p1, String p2) throws Exception {
        requestHeader.add(p1,p2);
    }

    public void removeRequestProperty() throws Exception {
        requestHeader.clear();
    }

    /**
    * Deletes Request Property at special index
    *  @param index Index
    */
    public void removeRequestProperty(int index) throws Exception {
        requestHeader.removeItem(index);
    }

    /**
    * Gets Request Property at special index
    */
    //UPGRADE_NOTE: The equivalent of method 'java.net.URLConnection.getRequestProperty' is not an override method. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1143'"
    public String getRequestProperty(String p1) throws Exception {
        return requestHeader.get_Renamed(p1);
    }

    /**
    * Gets Header Property Value at index
    *  @param p1 Property Index
    */
    //UPGRADE_NOTE: The equivalent of method 'java.net.URLConnection.getHeaderFieldKey' is not an override method. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1143'"
    public String getHeaderFieldKey(int p1) throws Exception {
        return responsHeader.get_Renamed(p1);
    }

    /**
    * Gets Header Property Value at name
    *  @param p1 Property Name
    */
    //UPGRADE_NOTE: The equivalent of method 'java.net.URLConnection.getHeaderField' is not an override method. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1143'"
    public String getHeaderField(String p1) throws Exception {
        return responsHeader.get_Renamed(p1);
    }

    public int getResponseHeaderCount() throws Exception {
        return responsHeader.count();
    }

    /**
    * This method does a quoted-printable decoding of the given string
    * according to RFC-2045 (Section 6.7). Note: this method
    * expects the whole message in one chunk, not line by line.
    * 
    * 
    *  @param str the message
    * 
    *  @return  the decoded message
    * 
    *  @throws ParseException If a '=' is not followed by a valid
    * 2-digit hex number or '\r\n'.
    */
    //public static byte[] quotedPrintableDecode(byte[] buffer, int buff_len)
    //{
    //    if (buffer == null)
    //        return null;
    //    ByteBuffer res = new ByteBuffer();
    //    ByteBuffer src = new ByteBuffer(buffer, buff_len);
    //    //UPGRADE_ISSUE: Method 'java.lang.System.getProperty' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javalangSystem'"
    //    char[] nl = { '\r', '\n' };
    //    int last = 0, didx = 0;
    //    for ( int sidx = 0; sidx < buff_len; )
    //    {
    //        char ch = (char)src.get_Renamed(sidx++);
    //        if (ch == '=')
    //        {
    //            if (sidx >= buff_len - 1)
    //                return res.get_Renamed();
    //            if (src.get_Renamed(sidx) == '\n' || src.get_Renamed(sidx) == '\r')
    //            {
    //                // Rule #5
    //                sidx++;
    //                if (src.get_Renamed(sidx - 1) == '\r' && src.get_Renamed(sidx) == '\n')
    //                    sidx++;
    //            }
    //            // Rule #1
    //            else
    //            {
    //                char repl;
    //                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Character.digit' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
    //                int hi = System.Char.GetNumericValue((char)src.get_Renamed(sidx)), lo = System.Char.GetNumericValue((char)src.get_Renamed(sidx + 1));
    //                if ((hi | lo) < 0)
    //                    return res.get_Renamed();
    //                //throw new ParseException(new String(src, sidx-1, 3) + " is an invalid code");
    //                else
    //                {
    //                    repl = (char)(hi << 4 | lo);
    //                    sidx += 2;
    //                }
    //                res.append((sbyte)repl);
    //                didx++;
    //            }
    //            last = didx;
    //        }
    //        else if (ch == '\n' || ch == '\r')
    //        // Rule #4
    //        {
    //            if (ch == '\r' && sidx < buff_len && src.get_Renamed(sidx) == '\n')
    //                sidx++;
    //            for (int idx = 0; idx < nl.Length; idx++)
    //            {
    //                res.append((byte)nl[idx]);
    //                last++;
    //            }
    //            didx = last;
    //        }
    //        // Rule #1, #2
    //        else
    //        {
    //            res.append((byte)ch);
    //            didx++;
    //            if (ch != ' ' && ch != '\t')
    //                // Rule #3
    //                last = didx;
    //        }
    //    }
    //    return res.get_Renamed();
    //}
    /**
    * Convert chunked content to normal.
    */
    public static byte[] postChunkedProcessing(byte[] buffer, long buff_length) throws Exception {
        int chunk_size = -1;
        ByteBuffer bb = new ByteBuffer();
        ByteBuffer csbb = new ByteBuffer();
        String strHexChunkSize;
        int j = 0;
        char ch;
        for (int i = 0;i < buff_length;i++)
        {
            ch = (char)buffer[i];
            if (chunk_size == -1)
            {
                if (ch != '\r')
                    csbb.append(buffer[i]);
                else if (ch == '\r' && csbb.size() > 0)
                {
                    try
                    {
                        strHexChunkSize = new String(csbb.get_Renamed(), 0, csbb.size(), EncodingSupport.GetEncoder("ASCII").getCharset());
                        if (!strHexChunkSize.startsWith("0x"))
                            strHexChunkSize = "0x" + strHexChunkSize;
                         
                        //UPGRADE_TODO: Method 'java.lang.Integer.decode' was converted to 'System.Int32.Parse' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073'"
                        chunk_size = Convert.ToInt32(strHexChunkSize, 16);
                        csbb.size_Renamed_Field = 0;
                    }
                    catch (Exception e)
                    {
                        chunk_size = 0;
                    }

                    i++;
                }
                else // next byte is 0x13
                if (ch == '\r' && csbb.size() == 0)
                    i++;
                   
            }
            else if (chunk_size > 0)
            {
                bb.append(buffer[i]);
                chunk_size--;
                if (chunk_size == 0)
                    chunk_size--;
                 
            }
              
            if (chunk_size == 0)
                break;
             
        }
        return bb.get_Renamed();
    }

    public void read(net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        //ByteBuffer readBuffer = new ByteBuffer(;
        boolean closeSocket = false;
        closeMemoryStream();
        ResponseText = new MemoryStream();
        ResponseHeaders = "";
        long time1 = 0;
        boolean keepAlive = false;
        try
        {
            createSockConnection();
            //sock.SetSocketOption(SocketOptionLevel.Socket,SocketOptionName.Linger,SO_LINGER);
            sock.Blocking = true;
            //sock.LingerState = new LingerOption(true, 60);
            //sock.ExclusiveAddressUse = true;
            sock.NoDelay = true;
            sock.ReceiveBufferSize = 8192;
            sock.ReceiveTimeout = 300000;
            long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            boolean b = false;
            byte[] readBytes = new byte[1024];
            int sizeReceived = 0;
            String reData = "";
            String Header = "";
            WebHeaderCollection Headers = new WebHeaderCollection();
            boolean isChuncked = false;
            byte[] bytes = new byte[10];
            while (m_stream.read(bytes,0,1) > 0)
            {
                //mlsEngine.WriteLine("Header:" + Encoding.ASCII.GetString(bytes, 0, 1));
                ResponseHeaders += new String(bytes, 0, 1, EncodingSupport.GetEncoder("ASCII").getCharset());
                //ms.Write(bytes, 0, 1);
                if (bytes[0] == '\n' && ResponseHeaders.endsWith("\r\n\r\n"))
                    break;
                 
            }
            mlsEngine.writeLine("\r\n<<<===Receive:\r\n",true);
            mlsEngine.writeLine(ResponseHeaders,true);
            parseHeaders();
            parseStatusCode();
            int ContentLength = -1;
            keepAlive = responsHeader.get_Renamed("Connection") != null && StringSupport.equals(responsHeader.get_Renamed("Connection").toLowerCase(), "keep-alive");
            isChuncked = responsHeader.get_Renamed("TRANSFER-ENCODING") != null && StringSupport.equals(responsHeader.get_Renamed("TRANSFER-ENCODING"), "chunked");
            if (responsHeader.get_Renamed("Content-Length") != null)
                ContentLength = Integer.valueOf(responsHeader.get_Renamed("Content-Length"));
             
            keepAlive = keepAlive && ContentLength != -1;
            if (isChuncked && !isConnectionClose())
            {
                sock.Shutdown(SocketShutdown.Send);
                //closeSocket = true;
                setConnectionClose(true);
            }
             
            if (!keepAlive)
                setConnectionClose(true);
             
            int nTotalBytes = 0;
            if (!(ContentLength == 0))
            {
                while ((sizeReceived = m_stream.read(readBytes,0,readBytes.length)) > 0)
                {
                    nTotalBytes += sizeReceived;
                    ResponseText.Write(readBytes, 0, sizeReceived);
                    if (ContentLength > -1 && nTotalBytes >= ContentLength)
                        break;
                     
                }
            }
             
            //else
            //{
            //    string eof = Encoding.ASCII.GetString(readBytes, sizeReceived - 4, 4);
            //    if (eof.Equals("\r\n\r\n"))
            //        break;
            //}
            //}
            //if (sizeReceived < readBytes.Length)
            //    break;
            //time1 = (System.DateTime.Now.Ticks - 621355968000000000) / 10000;
            mlsEngine.writeLine("\r\nGetInputStream Time  = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        }
        catch (SocketException es)
        {
            mlsEngine.writeLine("\r\nSocket Exception Time  = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time1),true);
            //System.out.print( "java.io.InterruptedIOException " + e.getMessage() );
            mlsEngine.WriteLine(es.ErrorCode + es.Message + es.toString(), true);
            throw mlsEngine.getEnvironment().createException(MLSException.CODE_TIMEOUT);
        }
        catch (IOException e)
        {
            //System.out.print( "java.io.InterruptedIOException " + e.getMessage() );
            mlsEngine.writeLine(e.getMessage() + e.toString(),true);
            throw mlsEngine.getEnvironment().createException(MLSException.CODE_TIMEOUT);
        }
        catch (Exception e1)
        {
            //System.out.print( "java.Exception " + e1.getMessage() );
            //throw getEngine().createException ( e1, "" );
            mlsEngine.writeLine(e1.getMessage() + e1.toString(),true);
            throw mlsEngine.getEnvironment().createException(MLSException.CODE_TIMEOUT);
        }
        finally
        {
            if (!keepAlive)
                sock.close();
             
        }
        if (ResponseText.Length < 2000)
            mlsEngine.WriteLine(EncodingSupport.GetEncoder("ASCII").GetString(ResponseText.ToArray()), true);
        else
            mlsEngine.writeLine("\r\n...Received data has been saved to a file.",true); 
    }

}


