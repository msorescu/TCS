//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:33 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.Net.IPEndPoint;
import CS2JNet.System.Net.Sockets.AddressFamily;
import CS2JNet.System.Net.Sockets.NetSocket;
import CS2JNet.System.Net.Sockets.ProtocolType;
import CS2JNet.System.Net.Sockets.SocketType;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

/* Copyright (c) 2005, J.P. Trosclair
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this list of conditions and 
 *		the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *		and the following disclaimer in the documentation and/or other materials provided with the 
 *		distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Based on FTPFactory.cs code, pretty much a complete re-write with FTPFactory.cs
 * as a reference.
 * 
 ***********************
 * Authors of this code:
 ***********************
 * J.P. Trosclair (jptrosclair@judelawfirm.com)
 * Filipe Madureira (filipe_madureira@hotmail.com) 
 * 
 *********************** 
 * FTPFactory.cs was written by Jaimon Mathew (jaimonmathew@rediffmail.com)
 * and modified by Dan Rolander (Dan.Rolander@marriott.com).
 *	http://www.csharphelp.com/archives/archive9.html
 ***********************
 * 
 * ** DO NOT ** contact the authors of FTPFactory.cs about problems with this code. It
 * is not their responsibility. Only contact people listed as authors of THIS CODE.
 * 
 *  Any bug fixes or additions to the code will be properly credited to the author.
 * 
 *  BUGS: There probably are plenty. If you fix one, please email me with info
 *   about the bug and the fix, code is welcome.
 * 
 * All calls to the ftplib functions should be:
 * 
 * try 
 * { 
 *		// ftplib function call
 * } 
 * catch(Exception ex) 
 * {
 *		// error handeler
 * }
 * 
 * If you add to the code please make use of OpenDataSocket(), CloseDataSocket(), and
 * ReadResponse() appropriately. See the comments above each for info about using them.
 * 
 * The Fail() function terminates the entire connection. Only call it on critical errors.
 * Non critical errors should NOT close the connection.
 * All errors should throw an exception of type Exception with the response string from
 * the server as the message.
 * 
 * See the simple ftp client for examples on using this class
 */
//#define FTP_DEBUG
public class FTP   
{
    public String server;
    public String user;
    public String pass;
    public int port;
    public int timeout;
    // timeout in miliseconds
    public long bytes_total;
    // upload/download info if the user wants it.
    public long file_size;
    // gets set when an upload or download takes place
    public String responseStr;
    // server response if the user wants it.
    public String messages;
    // server messages
    private NetSocket main_sock;
    private IPEndPoint main_ipEndPoint;
    private NetSocket data_sock;
    private IPEndPoint data_ipEndPoint;
    private FileStreamSupport file;
    private int response;
    private String bucket;
    public FTP() throws Exception {
        server = null;
        user = null;
        pass = null;
        port = 21;
        main_sock = null;
        main_ipEndPoint = null;
        data_sock = null;
        data_ipEndPoint = null;
        file = null;
        bucket = "";
        bytes_total = 0;
        timeout = 10000;
        messages = "";
    }

    public boolean isConnected() throws Exception {
        if (main_sock != null)
            return main_sock.Connected;
         
        return false;
    }

    private void fail() throws Exception {
        disconnect();
        throw new Exception(responseStr);
    }

    private void setBinaryMode(boolean mode) throws Exception {
        if (mode)
            sendCommand("TYPE I");
        else
            sendCommand("TYPE A"); 
        readResponse();
        if (response != 200)
            fail();
         
    }

    private void sendCommand(String command) throws Exception {
        byte[] cmd = EncodingSupport.GetEncoder("ASCII").GetBytes((command + "\r\n").toCharArray());
        main_sock.Send(cmd, cmd.length, 0);
    }

    private void fillBucket() throws Exception {
        byte[] bytes = new byte[512];
        long bytesgot;
        int seconds_passed = 0;
        while (main_sock.Available < 1)
        {
            Thread.sleep(50);
            seconds_passed += 50;
            // this code is just a fail safe option
            // so the code doesn't hang if there is
            // no data comming.
            if (seconds_passed > timeout)
            {
                disconnect();
                throw new Exception("Timed out waiting on server to respond.");
            }
             
        }
        while (main_sock.Available > 0)
        {
            bytesgot = main_sock.Receive(bytes, 512, 0);
            bucket += new String(bytes, 0, (int)bytesgot, EncodingSupport.GetEncoder("ASCII").getCharset());
        }
    }

    // this may not be needed, gives any more data that hasn't arrived
    // just yet a small chance to get there.
    //System.Threading.Thread.Sleep(100);
    private String getLineFromBucket() throws Exception {
        int i;
        String buf = "";
        if ((i = bucket.indexOf('\n')) < 0)
        {
            while (i < 0)
            {
                fillBucket();
                i = bucket.indexOf('\n');
            }
        }
         
        buf = bucket.substring(0, (0) + (i));
        bucket = bucket.substring(i + 1);
        return buf;
    }

    // Any time a command is sent, use ReadResponse() to get the response
    // from the server. The variable responseStr holds the entire string and
    // the variable response holds the response number.
    private void readResponse() throws Exception {
        String buf;
        messages = "";
        while (true)
        {
            //buf = GetLineFromBucket();
            buf = getLineFromBucket();
            // the server will respond with "000-Foo bar" on multi line responses
            // "000 Foo bar" would be the last line it sent for that response.
            // Better example:
            // "000-This is a multiline response"
            // "000-Foo bar"
            // "000 This is the end of the response"
            if (Pattern.Match(buf, "^[0-9]+ ").Success)
            {
                responseStr = buf;
                response = Integer.valueOf(buf.substring(0, (0) + (3)));
                break;
            }
            else
                messages += Pattern.compile("^[0-9]+-").matcher(buf).replaceAll("") + "\n"; 
        }
    }

    // if you add code that needs a data socket, i.e. a PASV command required,
    // call this function to do the dirty work. It sends the PASV command,
    // parses out the port and ip info and opens the appropriate data socket
    // for you. The socket variable is private Socket data_socket. Once you
    // are done with it, be sure to call CloseDataSocket()
    private void openDataSocket() throws Exception {
        String[] pasv;
        String server;
        int port;
        connect();
        sendCommand("PASV");
        readResponse();
        if (response != 227)
            fail();
         
        try
        {
            int i1, i2;
            i1 = responseStr.indexOf('(') + 1;
            i2 = responseStr.indexOf(')') - i1;
            pasv = StringSupport.Split(responseStr.substring(i1, (i1) + (i2)), ',');
        }
        catch (Exception __dummyCatchVar0)
        {
            disconnect();
            throw new Exception("Malformed PASV response: " + responseStr);
        }

        if (pasv.length < 6)
        {
            disconnect();
            throw new Exception("Malformed PASV response: " + responseStr);
        }
         
        server = String.Format("{0}.{1}.{2}.{3}", pasv[0], pasv[1], pasv[2], pasv[3]);
        port = (Integer.valueOf(pasv[4]) << 8) + Integer.valueOf(pasv[5]);
        try
        {
            if (data_sock != null)
            {
                if (data_sock.Connected)
                    data_sock.close();
                 
                data_sock = null;
            }
             
            if (data_ipEndPoint != null)
                data_ipEndPoint = null;
             
            data_sock = new NetSocket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            data_ipEndPoint = new IPEndPoint(Dns.GetHostAddresses(server)[0], port);
            data_sock.Connect(data_ipEndPoint);
        }
        catch (Exception ex)
        {
            throw new Exception("Failed to connect for data transfer: " + ex.getMessage());
        }
    
    }

    private void closeDataSocket() throws Exception {
        if (data_sock != null)
        {
            if (data_sock.Connected)
            {
                data_sock.close();
            }
             
            data_sock = null;
        }
         
        data_ipEndPoint = null;
    }

    public void disconnect() throws Exception {
        closeDataSocket();
        if (main_sock != null)
        {
            if (main_sock.Connected)
            {
                sendCommand("QUIT");
                main_sock.close();
            }
             
            main_sock = null;
        }
         
        if (file != null)
            file.close();
         
        main_ipEndPoint = null;
        file = null;
    }

    public void connect(String server, String user, String pass) throws Exception {
        this.server = server;
        this.user = user;
        this.pass = pass;
        connect();
    }

    public void connect() throws Exception {
        if (server == null)
            throw new Exception("No server has been set.");
         
        if (user == null)
            throw new Exception("No username has been set.");
         
        if (main_sock != null)
            if (main_sock.Connected)
                return ;
             
         
        main_sock = new NetSocket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        main_ipEndPoint = new IPEndPoint(Dns.GetHostAddresses(server)[0], port);
        try
        {
            main_sock.Connect(main_ipEndPoint);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }

        readResponse();
        if (response != 220)
            fail();
         
        sendCommand("USER " + user);
        readResponse();
        switch(response)
        {
            case 331: 
                if (pass == null)
                {
                    disconnect();
                    throw new Exception("No password has been set.");
                }
                 
                sendCommand("PASS " + pass);
                readResponse();
                if (response != 230)
                    fail();
                 
                break;
            case 230: 
                break;
        
        }
        setBinaryMode(true);
        return ;
    }

    public ArrayList list() throws Exception {
        byte[] bytes = new byte[512];
        String file_list = "";
        long bytesgot = 0;
        int seconds_passed = 0;
        ArrayList list = new ArrayList();
        connect();
        openDataSocket();
        sendCommand("LIST");
        readResponse();
        //FILIPE MADUREIRA.
        //Added response 125
        switch(response)
        {
            case 125: 
            case 150: 
                break;
            default: 
                closeDataSocket();
                throw new Exception(responseStr);
        
        }
        while (data_sock.Available < 1)
        {
            Thread.sleep(50);
            seconds_passed += 50;
            // this code is just a fail safe option
            // so the code doesn't hang if there is
            // no data comming.
            if (seconds_passed > (timeout / 10))
            {
                break;
            }
             
        }
        while (data_sock.Available > 0)
        {
            //CloseDataSocket();
            //throw new Exception("Timed out waiting on server to respond.");
            //FILIPE MADUREIRA.
            //If there are no files to list it gives timeout.
            //So I wait less time and if no data is received, means that there are no files
            //Maybe there are no files
            bytesgot = data_sock.Receive(bytes, bytes.length, 0);
            file_list += new String(bytes, 0, (int)bytesgot, EncodingSupport.GetEncoder("ASCII").getCharset());
        }
        //System.Threading.Thread.Sleep(100);
        closeDataSocket();
        readResponse();
        if (response != 226)
            throw new Exception(responseStr);
         
        for (String f : StringSupport.Split(file_list, '\n'))
        {
            if (f.length() > 0 && !Pattern.Match(f, "^total").Success)
                list.add(f.substring(0, (0) + (f.length() - 1)));
             
        }
        return list;
    }

    public ArrayList listFiles() throws Exception {
        ArrayList list = new ArrayList();
        for (Object __dummyForeachVar1 : list())
        {
            String f = (String)__dummyForeachVar1;
            //FILIPE MADUREIRA
            //In Windows servers it is identified by <DIR>
            if ((f.length() > 0))
            {
                if ((f.charAt(0) != 'd') && (f.toUpperCase().indexOf("<DIR>") < 0))
                    list.add(f);
                 
            }
             
        }
        return list;
    }

    public ArrayList listDirectories() throws Exception {
        ArrayList list = new ArrayList();
        for (Object __dummyForeachVar2 : list())
        {
            String f = (String)__dummyForeachVar2;
            //FILIPE MADUREIRA
            //In Windows servers it is identified by <DIR>
            if (f.length() > 0)
            {
                if ((f.charAt(0) == 'd') || (f.toUpperCase().indexOf("<DIR>") >= 0))
                    list.add(f);
                 
            }
             
        }
        return list;
    }

    public void changeDir(String path) throws Exception {
        connect();
        sendCommand("CWD " + path);
        readResponse();
        if (response != 250)
        {
            throw new Exception(responseStr);
        }
         
    }

    public void makeDir(String dir) throws Exception {
        connect();
        sendCommand("MKD " + dir);
        readResponse();
        switch(response)
        {
            case 257: 
            case 250: 
                break;
            default: 
                throw new Exception(responseStr);
        
        }
    }

    public void removeDir(String dir) throws Exception {
        connect();
        sendCommand("RMD " + dir);
        readResponse();
        if (response != 250)
        {
            throw new Exception(responseStr);
        }
         
    }

    public void removeFile(String filename) throws Exception {
        connect();
        sendCommand("DELE " + filename);
        readResponse();
        if (response != 250)
        {
            throw new Exception(responseStr);
        }
         
    }

    public long getFileSize(String filename) throws Exception {
        connect();
        sendCommand("SIZE " + filename);
        readResponse();
        if (response != 213)
        {
            throw new Exception(responseStr);
        }
         
        return Long.valueOf(responseStr.substring(4));
    }

    public void openUpload(String filename) throws Exception {
        openUpload(filename,filename,false);
    }

    public void openUpload(String filename, String remotefilename) throws Exception {
        openUpload(filename,remotefilename,false);
    }

    public void openUpload(String filename, boolean resume) throws Exception {
        openUpload(filename,filename,resume);
    }

    public void openUpload(String filename, String remote_filename, boolean resume) throws Exception {
        connect();
        setBinaryMode(true);
        openDataSocket();
        bytes_total = 0;
        try
        {
            file = new FileStreamSupport(filename, FileMode.Open);
        }
        catch (Exception ex)
        {
            file = null;
            throw new Exception(ex.getMessage());
        }

        file_size = file.Length;
        if (resume)
        {
            long size = getFileSize(remote_filename);
            sendCommand("REST " + size);
            readResponse();
            if (response == 350)
                file.Seek(size, SeekOrigin.Begin);
             
        }
         
        sendCommand("STOR " + remote_filename);
        readResponse();
        switch(response)
        {
            case 125: 
            case 150: 
                break;
            default: 
                file.close();
                file = null;
                throw new Exception(responseStr);
        
        }
        return ;
    }

    //break;
    public void openDownload(String filename) throws Exception {
        openDownload(filename,filename,false);
    }

    public void openDownload(String filename, String localFilePath, boolean resume) throws Exception {
        connect();
        //SetBinaryMode(true);
        bytes_total = 0;
        //try
        //{
        //    file_size = GetFileSize(filename);
        //}
        //catch
        //{
        //    file_size = 0;
        //}
        if (resume && (new File(localFilePath)).exists())
        {
            try
            {
                file = new FileStreamSupport(localFilePath, FileMode.Open);
            }
            catch (Exception ex)
            {
                file = null;
                throw new Exception(ex.getMessage());
            }

            sendCommand("REST " + file.Length);
            readResponse();
            if (response != 350)
                throw new Exception(responseStr);
             
            file.Seek(file.Length, SeekOrigin.Begin);
            bytes_total = file.Length;
        }
        else
        {
            try
            {
                file = new FileStreamSupport(localFilePath, FileMode.Create);
            }
            catch (Exception ex)
            {
                file = null;
                throw new Exception(ex.getMessage());
            }
        
        } 
        openDataSocket();
        sendCommand("RETR " + filename);
        readResponse();
        switch(response)
        {
            case 125: 
            case 150: 
                break;
            default: 
                file.close();
                file = null;
                throw new Exception(responseStr);
        
        }
        return ;
    }

    public int doUpload() throws Exception {
        byte[] bytes = new byte[512];
        long bytes_got;
        bytes_got = file.read(bytes,0,bytes.length);
        bytes_total += bytes_got;
        data_sock.Send(bytes, (int)bytes_got, 0);
        if (bytes_total == file_size)
        {
            file.close();
            file = null;
            closeDataSocket();
            readResponse();
            switch(response)
            {
                case 226: 
                case 250: 
                    break;
                default: 
                    throw new Exception(responseStr);
            
            }
            setBinaryMode(false);
        }
         
        //FILIPE MADUREIRA
        //To prevent DIVIDEBYZERO Exception on zero length files
        if (file_size == 0)
        {
            return 100;
        }
         
        return (int)((bytes_total * 100) / file_size);
    }

    // consider throwing an exception here so the calling code knows
    // there was an error
    public int doDownload() throws Exception {
        byte[] bytes = new byte[10240];
        long bytes_got;
        bytes_got = data_sock.Receive(bytes, bytes.length, 0);
        file.write(bytes,0,(int)bytes_got);
        bytes_total += bytes_got;
        if (bytes_got == 0)
        {
            closeDataSocket();
            file.close();
            file = null;
            readResponse();
            //if(response != 226 && response != 250)
            //	Fail();
            switch(response)
            {
                case 226: 
                case 250: 
                    break;
                default: 
                    throw new Exception(responseStr);
            
            }
            return 100;
        }
         
        return 1;
    }

    //break;
    //SetBinaryMode(false);
    //FILIPE MADUREIRA
    //To prevent DIVIDEBYZERO Exception on zero length files
    //if(file_size == 0)
    //{
    // consider throwing and exception here so the calling code knows
    // there was an error
    //}
    //(int)((bytes_total * 100) / file_size);
    public void downloadFile(String fileName, String saveTo) throws Exception {
        int perc = 0, old_perc = 0;
        openDownload(fileName,saveTo,false);
        while (true)
        {
            // the DoDownload() function returns the percentage complete
            // there are 2 public members bytes_total and file_size
            // that may be of interest to the developer. They are used by
            // the upload/download code though so DO NOT modify them. I know
            // it's sloppy to make them public rather than a 'get' function.
            perc = doDownload();
            //if (perc > old_perc)
            //{
            //    Console.Write("\rDownloading: {0}/{1} {2}%", bytes_total, file_size, perc);
            //    Console.Out.Flush();
            //}
            //old_perc = perc;
            if (perc == 100)
                break;
             
        }
    }

}


