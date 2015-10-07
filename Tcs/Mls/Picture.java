//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:51 PM
//

package Tcs.Mls;

import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.LCC.Disposable;
import java.io.File;
import java.io.InputStream;
import java.net.URI;

/**
* This class is a storage for information about image name, type, location(URL)
* //////////////////////////////////////////////////////////
*/
public class Picture   
{
    /**
    * //////////////////////////////////////////////////////////
    */
    //Image types
    public static final int GIF = 0;
    public static final int JPG = 1;
    public static final int PNG = 2;
    public static final int IPX = 3;
    //UPGRADE_NOTE: Final was removed from the declaration of 'NUM_TYPES '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    protected public static final int NUM_TYPES = IPX + 1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'UNKNOWN_TYPE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final int UNKNOWN_TYPE = IPX + 1;
    protected public static final int TYPE_NOT_INITIALIZED = -1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'SIGNATURE'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final byte[][] SIGNATURE = new byte[][]{ new byte[]{ 71, 73, 70 }, new byte[]{ 255, 216 }, new byte[]{ 137, 80, 78, 71, 13, 10, 26, 10 }, null };
    //UPGRADE_NOTE: Final was removed from the declaration of 'm_strTypeNames'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    protected public static final String[] m_strTypeNames = new String[]{ "GIF", "JPG", "PNG", "IPX", "UnknownExtension" };
    protected public static final int BUFSIZE = 0x10000;
    protected public String m_strName = "";
    protected public int m_nType = TYPE_NOT_INITIALIZED;
    protected public byte[] m_binaryData = null;
    protected public String m_strURL = null;
    /**
    * @param strURL URL of picture if it exists somewhere as a natural (binary) file
    * 
    *  @param componentForWaitCursor component that will have its cursor changed to hourglass when downloading data
    */
    //--------------------------------------------------
    public Picture(String strURL) throws Exception {
        if (strURL != null)
        {
            try
            {
                //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
                URI objURL = new URI(strURL);
                m_strURL = strURL;
            }
            catch (Exception e)
            {
                throw new Throwable("\n\nERROR: invalid URL '" + strURL + "' passed to Picture constructor.\n");
            }
        
        }
        else
            throw new System.NullReferenceException("\n\nERROR: <null> passed as URL to Picture constructor.\n"); 
    }

    /**
    * Get raw (binary) image data.
    */
    //--------------------------------------------------
    public byte[] getBinaryData() throws Exception {
        //--------------------------------------------------
        if (m_binaryData != null)
            return m_binaryData;
         
        if (m_strURL != null)
            downloadBinaryData();
         
        return m_binaryData;
    }

    //--------------------------------------------------
    private static boolean checkSignature(byte[] data, byte[] signature) throws Exception {
        //--------------------------------------------------
        if (signature == null)
            return false;
         
        for (int i = 0;i < signature.length;i++)
            if (i >= data.length || data[i] != signature[i])
                return false;
             
        return true;
    }

    //--------------------------------------------------
    private int figureOutType() throws Exception {
        //--------------------------------------------------
        byte[] data = getBinaryData();
        if (data == null)
            return UNKNOWN_TYPE;
         
        for (int i = 0;i < NUM_TYPES;i++)
            if (checkSignature(data,SIGNATURE[i]))
                return i;
             
        Console.Error.WriteLine("\n\nWARNING: Unknown picture type.\n");
        return UNKNOWN_TYPE;
    }

    /**
    * returns type ID: one-of Picture.GIF, Picture.JPG, Picture.PNG
    */
    //--------------------------------------------------
    public int getType() throws Exception {
        //--------------------------------------------------
        if (m_nType == TYPE_NOT_INITIALIZED)
            m_nType = figureOutType();
         
        return m_nType;
    }

    /**
    * returns type name: one-of "GIF", "JPG", "PNG", "IPX"
    */
    //--------------------------------------------------
    public String getTypeName() throws Exception {
        return m_strTypeNames[getType()];
    }

    //--------------------------------------------------
    /**
    * type one-of:"GIF","JPG","PNG"
    */
    //--------------------------------------------------
    public String getName() throws Exception {
        return m_strName;
    }

    //--------------------------------------------------
    /**
    * guess what...
    */
    //--------------------------------------------------
    public String getURL() throws Exception {
        return m_strURL;
    }

    //--------------------------------------------------
    private void downloadBinaryData() throws Exception {
        //--------------------------------------------------
        if (m_strURL == null)
            return ;
         
        InputStream in_Renamed = null;
        try
        {
            //System.Net.HttpWebRequest conn = null;
            // Local File
            String sFile = m_strURL;
            if (m_strURL.startsWith("file:/"))
            {
                sFile = m_strURL.substring("file:/".length());
            }
             
            if ((new File(sFile)).exists())
            {
                // REVIEW: Alexandre Kozlov: Hm how stupid it was to assume path separator characters, where these are defined by constants
                //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
                in_Renamed = new FileStreamSupport(sFile.replace('/', File.separatorChar), FileMode.Open, FileAccess.Read);
                //BinaryReader br = new BinaryReader(in_Renamed);
                m_binaryData = readFully(in_Renamed);
            }
            else
                m_binaryData = null; 
        }
        catch (Exception e)
        {
            //UPGRADE_TODO: Method 'java.io.PrintStream.println' was converted to 'System.Console.Error.WriteLine' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioPrintStreamprintln_javalangObject'"
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            Console.Error.WriteLine(e);
            return ;
        }
        finally
        {
            try
            {
                if (in_Renamed != null)
                    in_Renamed.close();
                 
            }
            catch (Exception exc)
            {
                //UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
                SupportClass.WriteStackTrace(exc, Console.Error);
            }
        
        }
    }

    //end downloadBinaryData()
    /**
    * Reads data from a stream until the end is reached. The
    * data is returned as a byte array. An IOException is
    * thrown if any of the underlying IO calls fail.
    * 
    *  @param stream The stream to read data from
    */
    //public static byte[] ReadFully(Stream stream)
    //{
    //    byte[] buffer = new byte[5000];
    //    using (MemoryStream ms = new MemoryStream())
    //    {
    //        int read = stream.Read(buffer, 0, buffer.Length);
    //        ms.Write(buffer, 0, read);
    //        return ms.ToArray();
    //    }
    //}
    public static byte[] readFully(InputStream stream) throws Exception {
        byte[] buffer = new byte[32768];
        MemoryStream ms = new MemoryStream();
        try
        {
            {
                while (true)
                {
                    int read = stream.read(buffer,0,buffer.length);
                    if (read <= 0)
                        return ms.ToArray();
                     
                    ms.Write(buffer, 0, read);
                }
            }
        }
        finally
        {
            if (ms != null)
                Disposable.mkDisposable(ms).dispose();
             
        }
    }

}


//end public class Picture