//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:46 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.JavaSupport.io.FilterOnlyDirs;
import CS2JNet.JavaSupport.io.FilterOnlyFiles;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.IO.StreamReader;
import CS2JNet.System.IO.TextReaderSupport;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import net.toppro.components.mls.engine.STRINGS;
import net.toppro.components.mls.engine.StringTokenizerEx;
import org.apache.commons.codec.binary.Base64;

//using net.sf.jazzlib;
public class MLSUtil   
{
    private static final String STANDARD_DATE_FORMAT_STRING = "MM/dd/yyyy";
    //UPGRADE_NOTE: Final was removed from the declaration of 'STANDARD_DATE_FORMAT '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
    //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
    //private static readonly SimpleDateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat(STANDARD_DATE_FORMAT_STRING);
    //UPGRADE_NOTE: Final was removed from the declaration of 'alphabet '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    public static int DATE_LAST_FORMAT_INDEX = -1;
    //UPGRADE_NOTE: Final was removed from the declaration of 'fmts '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
    //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
    //internal static readonly SimpleDateFormat[] fmts = new SimpleDateFormat[]{new SimpleDateFormat("M/d/yy"), new SimpleDateFormat("M/d/yyyy"), new SimpleDateFormat("MM/dd/yy"), new SimpleDateFormat("dd/MM/yy"), STANDARD_DATE_FORMAT, new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("yyyy/MM/dd"), new SimpleDateFormat("MM-dd-yy"), new SimpleDateFormat("dd-MM-yy"), new SimpleDateFormat("MM-dd-yyyy"), new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("MM.dd.yy"), new SimpleDateFormat("dd.MM.yy"), new SimpleDateFormat("MM.dd.yyyy"), new SimpleDateFormat("yyyy.MM.dd"), new SimpleDateFormat("dd.MM.yyyy"), new SimpleDateFormat("d M yy"), new SimpleDateFormat("MM dd yy"), new SimpleDateFormat("dd MM yy"), new SimpleDateFormat("MMddyy"), new SimpleDateFormat("ddMMyy"), new SimpleDateFormat("M/dd/yy"), new SimpleDateFormat("dd/M/yy"), new SimpleDateFormat("M/dd/yyyy"), new SimpleDateFormat("dd/M/yyyy"), new SimpleDateFormat("M-dd-yy"), new SimpleDateFormat("dd-M-yy"), new SimpleDateFormat("M-dd-yyyy"), new SimpleDateFormat("dd-M-yyyy"), new SimpleDateFormat("M.d.yy"), new SimpleDateFormat("M.d.yyyy"), new SimpleDateFormat("M.dd.yy"), new SimpleDateFormat("dd.M.yy"), new SimpleDateFormat("M.dd.yyyy"), new SimpleDateFormat("dd.M.yyyy"), new SimpleDateFormat("dd M yy"), new SimpleDateFormat("M dd yy"), new SimpleDateFormat("Mddyy"), new SimpleDateFormat("ddMyy"), new SimpleDateFormat("MMM/dd/yy"), new SimpleDateFormat("dd/MMM/yy"), new SimpleDateFormat("MMM/dd/yyyy"), new SimpleDateFormat("dd/MMM/yyyy"), new SimpleDateFormat("MMM-dd-yy"), new SimpleDateFormat("dd-MMM-yy"), new SimpleDateFormat("MMM-dd-yyyy"), new SimpleDateFormat("dd-MMM-yyyy"), new SimpleDateFormat("MMMddyyyy"), new SimpleDateFormat("MMMdyyyy"), new SimpleDateFormat("MMM.dd.yy"), new SimpleDateFormat("dd.MMM.yy"), new SimpleDateFormat("MMM.dd.yyyy"), new SimpleDateFormat("dd.MMM.yyyy"), new SimpleDateFormat("MMM dd yy"), new SimpleDateFormat("dd MMM yy"), new SimpleDateFormat("MMMddyy"), new
    //	SimpleDateFormat("ddMMMyy"), new SimpleDateFormat("MM/d/yy"), new SimpleDateFormat("d/MM/yy"), new SimpleDateFormat("MM/d/yyyy"), new SimpleDateFormat("d/MM/yyyy"), new SimpleDateFormat("MM-d-yy"), new SimpleDateFormat("d-MM-yy"), new SimpleDateFormat("MM-d-yyyy"), new SimpleDateFormat("d-MM-yyyy"), new SimpleDateFormat("MM.d.yy"), new SimpleDateFormat("d.MM.yy"), new SimpleDateFormat("MM.d.yyyy"), new SimpleDateFormat("d.MM.yyyy"), new SimpleDateFormat("MM d yy"), new SimpleDateFormat("d MM yy"), new SimpleDateFormat("MMdyy"), new SimpleDateFormat("dMMyy"), new SimpleDateFormat("MMM/d/yy"), new SimpleDateFormat("d/MMM/yy"), new SimpleDateFormat("MMM/d/yyyy"), new SimpleDateFormat("d/MMM/yyyy"), new SimpleDateFormat("MMM-d-yy"), new SimpleDateFormat("d-MMM-yy"), new SimpleDateFormat("MMM-d-yyyy"), new SimpleDateFormat("d-MMM-yyyy"), new SimpleDateFormat("MMM.d.yy"), new SimpleDateFormat("d.MMM.yy"), new SimpleDateFormat("MMM.d.yyyy"), new SimpleDateFormat("d.MMM.yyyy"), new SimpleDateFormat("MMM d yy"), new SimpleDateFormat("d MMM yy"), new SimpleDateFormat("MMM d yyyy"), new SimpleDateFormat("d MMM yyyy"), new SimpleDateFormat("MMMdyy"), new SimpleDateFormat("dMMMyy"), new SimpleDateFormat("MM/dd/yyyy hh:mm:ss"), new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"), new SimpleDateFormat("MM/dd/yy hh:mm:ss"), new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")};
    //UPGRADE_NOTE: Final was removed from the declaration of 'DATE_SHORT_MONTHS '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] DATE_SHORT_MONTHS = new String[]{ "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
    public static final short STRING_LEFT = 0;
    public static final short STRING_RIGHT = 1;
    public static String getSubString(String sMain, String sDelimit, int iDirect, boolean bInclude) throws Exception {
        String sSub = sMain;
        if (sMain != null)
        {
            int iPos = sMain.indexOf(sDelimit);
            if (iPos >= 0)
            {
                switch(iDirect)
                {
                    case STRING_LEFT: 
                        if (bInclude)
                            iPos += sDelimit.length();
                         
                        sSub = sMain.Substring(0, (iPos)-(0));
                        break;
                    case STRING_RIGHT: 
                        if (!bInclude)
                            iPos += sDelimit.length();
                         
                        sSub = sMain.substring(iPos);
                        break;
                
                }
            }
             
        }
         
        return sSub;
    }

    private static void copyInputStream(InputStream in_Renamed, InputStream out_Renamed) throws Exception {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = SupportClass.readInput(in_Renamed,buffer,0,buffer.length)) >= 0)
        out_Renamed.write(buffer,0,len);
        in_Renamed.close();
        out_Renamed.close();
    }

    private static String correctFilename(String _filename) throws Exception {
        StringBuilder buf = new StringBuilder(_filename.length());
        char[] filename = new char[0];
        filename = _filename.toCharArray();
        // give us more speed
        char c;
        for (int i = 0;i < filename.length;i++)
        {
            c = filename[i];
            switch(c)
            {
                case '\\': 
                case '/': 
                case ':': 
                case '*': 
                case '?': 
                case '"': 
                case '>': 
                case '<': 
                case '|': 
                    break;
                default: 
                    buf.append(c);
                    break;
            
            }
        }
        return buf.toString();
    }

    //public static bool extractTo(System.String zipPath, System.String extractToDir)
    //{
    //    try
    //    {
    //        ZipFile zipFile = new ZipFile(zipPath);
    //        System.Collections.IEnumerator entries;
    //        entries = zipFile.entries();
    //        System.String zipFileName = "";
    //        //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
    //        while (entries.MoveNext())
    //        {
    //            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
    //            ZipEntry entry = (ZipEntry) entries.Current;
    //            if (entry.isDirectory())
    //            {
    //                //UPGRADE_TODO: Method 'java.io.File.mkdir' was converted to 'System.IO.Directory.CreateDirectory' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilemkdir'"
    //                System.IO.Directory.CreateDirectory((new System.IO.FileInfo(extractToDir + entry.getName())).FullName);
    //                continue;
    //            }
    //            System.Console.Out.WriteLine("Extracting file: " + entry.getName());
    //            zipFileName = correctFilename(entry.getName());
    //            if (entry.getName().IndexOf("/") == - 1)
    //            {
    //                //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
    //                copyInputStream(zipFile.getInputStream(entry), new System.IO.BufferedStream(new System.IO.FileStream(extractToDir + zipFileName, System.IO.FileMode.Create)));
    //            }
    //            else
    //            {
    //                zipFileName = zipFileName.Substring(zipFileName.LastIndexOf("/") + 1, (zipFileName.Length) - (zipFileName.LastIndexOf("/") + 1));
    //                //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
    //                copyInputStream(zipFile.getInputStream(entry), new System.IO.BufferedStream(new System.IO.FileStream(extractToDir + zipFileName, System.IO.FileMode.Create)));
    //            }
    //        }
    //        zipFile.close();
    //        return true;
    //    }
    //    catch (System.IO.IOException ioe)
    //    {
    //        System.Console.Error.WriteLine("Unhandled exception:");
    //        SupportClass.WriteStackTrace(ioe, Console.Error);
    //        return false;
    //    }
    //}
    public static boolean isNumber(String _value) throws Exception {
        if (_value == null || _value.length() == 0)
            return false;
         
        int startPosition = 0;
        char[] value_Renamed = _value.toCharArray();
        if (value_Renamed[0] == '-' && value_Renamed.length > 1)
            startPosition = 1;
         
        for (int i = startPosition;i < value_Renamed.length;i++)
            if (Character.isDigit(value_Renamed[i]) == false)
                return false;
             
        return true;
    }

    public static boolean isDate(String _value) throws Exception {
        if (_value == null || _value.length() == 0)
            return false;
         
        int i;
        char[] value_Renamed = _value.toCharArray();
        for (i = 0;i < value_Renamed.length;i++)
        {
            if (value_Renamed[i] != '/' && value_Renamed[i] != '.' && value_Renamed[i] != '-')
                if (Character.isDigit(value_Renamed[i]) == false)
                    return false;
                 
             
        }
        return true;
    }

    public static boolean isBoolean(String _value) throws Exception {
        if (_value == null || _value.length() == 0)
            return false;
         
        if (_value.equals("true") || _value.equals("false"))
            return true;
         
        return false;
    }

    public static boolean isCurrency(String _value) throws Exception {
        _value = StringSupport.Trim(_value);
        if (_value == null || _value.length() == 0)
            return false;
         
        char[] value_Renamed = _value.toCharArray();
        for (int i = 0;i < value_Renamed.length;i++)
            if (Character.isDigit(value_Renamed[i]) == false && value_Renamed[i] != '.' && value_Renamed[i] != ',' && value_Renamed[i] != '-')
                return false;
             
        return true;
    }

    public static String prepareCurrency(String _value) throws Exception {
        if (_value == null || _value.length() == 0)
            return _value;
         
        StringBuilder sb = new StringBuilder(_value.length());
        char[] value_Renamed = _value.toCharArray();
        for (int i = 0;i < value_Renamed.length;i++)
        {
            if (value_Renamed[i] == '\'' || value_Renamed[i] == '/')
                value_Renamed[i] = '.';
            else if (value_Renamed[i] == ',')
                value_Renamed[i] = ' ';
              
            if (value_Renamed[i] >= '0' && value_Renamed[i] <= '9')
                sb.append(value_Renamed[i]);
            else if (value_Renamed[i] == '.' || value_Renamed[i] == ',' || value_Renamed[i] == '-')
                sb.append(value_Renamed[i]);
              
        }
        return sb.toString();
    }

    public static String removeSpace(String _value) throws Exception {
        if (_value == null || _value.length() == 0)
            return _value;
         
        return _value.replace(" ", "");
    }

    //System.Text.StringBuilder sb = new System.Text.StringBuilder(_value.Length);
    //char[] value_Renamed = _value.ToCharArray();
    //for (int i = 0; i < value_Renamed.Length; i++)
    //    if (value_Renamed[i] != ' ')
    //        sb.Append(value_Renamed[i]);
    //return sb.ToString();
    public static String replaceSubStr(String _value, String substStr, String substWith) throws Exception {
        if (_value == null || _value.length() == 0 || substStr == null || substStr.length() == 0)
            return _value;
         
        if (substWith == null)
            substWith = "";
         
        return _value.replace(substStr, substWith);
    }

    //System.Text.StringBuilder sb = new System.Text.StringBuilder(_value.Length);
    //char[] value_Renamed = _value.ToCharArray();
    //char[] substStrValue = substStr.ToCharArray();
    //int substStrLen = substStr.Length;
    //int substStrIndex = 0;
    //int j, k = - 1;
    //for (int i = 0; i < value_Renamed.Length; i++)
    //{
    //    if (value_Renamed[i] == substStrValue[substStrIndex])
    //    {
    //        if (k == - 1)
    //            k = i;
    //        substStrIndex++;
    //        if (substStrIndex == substStrLen)
    //        {
    //            sb.Append(substWith);
    //            k = - 1;
    //            substStrIndex = 0;
    //        }
    //    }
    //    else
    //    {
    //        if (k != - 1)
    //        {
    //            for (j = k; j <= i; j++)
    //                sb.Append(value_Renamed[j]);
    //            substStrIndex = 0;
    //            k = - 1;
    //        }
    //        else
    //            sb.Append(value_Renamed[i]);
    //    }
    //}
    //if (k != - 1)
    //    for (j = k; j < value_Renamed.Length; j++)
    //        sb.Append(value_Renamed[j]);
    //return sb.ToString();
    public static String cutBy(String _value, String cutByStr) throws Exception {
        if (_value == null || _value.length() == 0)
            return _value;
         
        if (cutByStr == null || cutByStr.length() == 0)
            return _value;
         
        int i;
        cutByStr = cutByStr.replace('~', ' ');
        if ((i = _value.indexOf(cutByStr)) != -1)
            return _value.Substring(0, (i)-(0));
         
        return _value;
    }

    public static String toStr(String _value) throws Exception {
        if (_value == null || _value.length() == 0)
            return _value;
         
        StringBuilder sb = new StringBuilder(_value.length());
        StringBuilder digit = new StringBuilder();
        char[] value_Renamed = _value.toCharArray();
        boolean bAscii = false;
        int iVar = 0;
        for (int i = 0;i < value_Renamed.length;i++)
        {
            if (value_Renamed[i] == '~')
                value_Renamed[i] = ' ';
             
            if (value_Renamed[i] == '\\')
            {
                if (digit.length() != 0)
                {
                    if (isNumber(digit.toString()))
                        sb.append((char)Integer.valueOf(digit.toString()));
                    else
                        sb.append(digit.toString()); 
                    digit = new StringBuilder();
                }
                 
                if ((i > 0 && value_Renamed[i - 1] == '\\') || i == (value_Renamed.length - 1))
                    sb.append(value_Renamed[i]);
                else
                    bAscii = true; 
            }
            else if (value_Renamed[i] == '$' && i + 5 < value_Renamed.length && value_Renamed[i + 1] == 'C' && value_Renamed[i + 2] == 'h' && value_Renamed[i + 3] == 'r' && value_Renamed[i + 4] == '(')
            {
                // $Chr()
                if (digit.length() != 0)
                {
                    if (isNumber(digit.toString()))
                        //&& iVar.parseInt(digit.toString()) != 0 ) // because we can have /0
                        sb.append((char)Integer.valueOf(digit.toString()));
                    else
                        sb.append(digit.toString()); 
                    StringBuilderSupport.setLength(digit, 0);
                }
                 
                i += 5;
                while (i < value_Renamed.length && value_Renamed[i] != ')')
                {
                    if (value_Renamed[i] == '~')
                        value_Renamed[i] = ' ';
                     
                    digit.append(value_Renamed[i]);
                    i++;
                }
                if (isNumber(digit.toString()))
                    sb.append((char)Integer.valueOf(digit.toString()));
                else
                    sb.append(digit.toString()); 
                StringBuilderSupport.setLength(digit, 0);
                bAscii = false;
            }
            else
            {
                if (bAscii && Character.isDigit(value_Renamed[i]) == true)
                    digit.append(value_Renamed[i]);
                else if (bAscii && digit.length() == 0)
                {
                    sb.append(value_Renamed[i - 1]);
                    sb.append(value_Renamed[i]);
                    bAscii = false;
                }
                else if (bAscii && digit.length() != 0)
                {
                    if (isNumber(digit.toString()))
                        sb.append((char)Integer.valueOf(digit.toString()));
                    else
                        sb.append(digit.toString()); 
                    sb.append(value_Renamed[i]);
                    StringBuilderSupport.setLength(digit, 0);
                    bAscii = false;
                }
                else
                {
                    if (digit.length() != 0)
                    {
                        if (isNumber(digit.toString()))
                            sb.append((char)Integer.valueOf(digit.toString()));
                        else
                            sb.append(digit.toString()); 
                        StringBuilderSupport.setLength(digit, 0);
                        bAscii = false;
                    }
                     
                    sb.append(value_Renamed[i]);
                }   
            }  
        }
        // end of for(...
        if (digit.length() != 0)
        {
            if (isNumber(digit.toString()))
                //&& iVar.parseInt(digit.toString()) != 0 )
                sb.append((char)Integer.valueOf(digit.toString()));
            else
                sb.append(digit.toString()); 
            StringBuilderSupport.setLength(digit, 0);
        }
         
        return sb.toString();
    }

    public static void copyFile(net.toppro.components.mls.engine.MLSEngine engine, String fromFileName, String toFileName) throws Exception {
        System.IO.BufferedStream fis = null;
        System.IO.BufferedStream fos = null;
        if (!fromFileName.toUpperCase().equals(toFileName.toUpperCase()))
        {
            try
            {
                //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
                fis = new System.IO.BufferedStream(new FileStreamSupport(fromFileName, FileMode.Open, FileAccess.Read));
                //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
                fos = new System.IO.BufferedStream(new FileStreamSupport(toFileName, FileMode.Create));
                byte[] buf = new byte[8192];
                int n;
                while ((n = SupportClass.ReadInput(fis, buf, 0, buf.length)) >= 0)
                fos.Write(buf, 0, n);
            }
            catch (Exception exc)
            {
                throw engine.createException(exc,formatString(STRINGS.get_Renamed(STRINGS.MLS_UTIL_UNABLE_TO_COPY_FILE),fromFileName,toFileName));
            }
            finally
            {
                if (fis != null)
                    try
                    {
                        //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
                        fis.Close();
                    }
                    catch (Exception exc)
                    {
                    }
                
                 
                if (fos != null)
                    try
                    {
                        fos.Close();
                    }
                    catch (Exception exc)
                    {
                    }
                
                 
            }
        }
         
    }

    public static String compressSpaces(String sInp) throws Exception {
        StringBuilder s = new StringBuilder();
        boolean bWasSpc = false;
        boolean bTakeSym = true;
        for (int i = 0;i < sInp.length();i++)
        {
            bTakeSym = true;
            if (sInp.charAt(i) == ' ')
            {
                if (bWasSpc)
                    bTakeSym = false;
                 
                bWasSpc = true;
            }
            else
                bWasSpc = false; 
            if (bTakeSym)
                s.append(sInp.charAt(i));
             
        }
        return StringSupport.Trim(s.toString());
    }

    public static String trimSpaces(String source, String flag) throws Exception {
        String result = "";
        if (flag.toUpperCase().equals("T".toUpperCase()))
            result = StringSupport.Trim(source);
        else if (flag.toUpperCase().equals("A".toUpperCase()))
            result = removeSpace(source);
        else if (flag.toUpperCase().equals("N".toUpperCase()))
            result = source;
        else if (flag.toUpperCase().equals("C".toUpperCase()))
            result = compressSpaces(source);
        else
            result = StringSupport.Trim(source);    
        return result;
    }

    /**
    * use "begin" and "end"  delimiter to extract from str the value information
    */
    private static int getPositionValue(String str, String begin, String end) throws Exception {
        return (Integer.valueOf(getKeyValue(str,begin,end)));
    }

    /**
    * the data format looks like "keyword," {x1, x2}
    * here extract x1
    */
    public static int getLineNumber(String str) throws Exception {
        String value_Renamed = str.Substring(str.indexOf("{"), (str.Length)-(str.indexOf("{")));
        return getPositionValue(value_Renamed,"{",",");
    }

    /**
    * the data format looks like "keyword," {x1, x2}
    * here extract x2
    */
    public static int getOffset(String str) throws Exception {
        String value_Renamed = str.Substring(str.indexOf("{"), (str.Length)-(str.indexOf("{")));
        return getPositionValue(value_Renamed,",","}");
    }

    public static String toDestLine(String record, String str1, int lineNumber) throws Exception {
        String initStr = getKeyValue(str1,"\"","\"");
        String dest = new StringBuilder().toString();
        if (lineNumber >= 0)
        {
            dest = record.substring(record.indexOf(initStr) + initStr.length() + 1);
            SupportClass.Tokenizer destLine = new SupportClass.Tokenizer(dest,"\r");
            while (destLine.hasMoreTokens() && lineNumber-- != 0)
            destLine.nextToken();
            return destLine.nextToken();
        }
        else
        {
            int p = 0;
            int p1 = 0;
            dest = record.substring(0, (0) + ((record.indexOf(initStr)) - (0)));
            while (true)
            {
                p = dest.lastIndexOf("\r");
                //UPGRADE_WARNING: Method 'java.lang.String.lastIndexOf' was converted to 'System.String.LastIndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                p1 = dest.lastIndexOf("\r",p - 1);
                if (p1 == -1)
                    p1 = 0;
                 
                if (p > 0 && p1 >= 0 && p > p1)
                    dest = dest.Substring(p1, (p)-(p1));
                else
                    break; 
                if (++lineNumber == 0)
                    break;
                 
            }
            return dest;
        } 
    }

    private static String getKeyValue(String str, String begin, String end) throws Exception {
        String line;
        int beginIndex = str.indexOf(begin);
        int endIndex = str.indexOf(end);
        if (beginIndex < 0 || endIndex < 0)
            return "0";
         
        if (begin.equals(end))
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            line = str.substring(str.indexOf(begin) + 1, (str.indexOf(begin) + 1) + ((SupportClass.getIndex(str,end,2)) - (str.indexOf(begin) + 1)));
        }
        else
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            line = str.substring(str.indexOf(begin) + 1, (str.indexOf(begin) + 1) + ((SupportClass.getIndex(str,end,1)) - (str.indexOf(begin) + 1)));
        } 
        return StringSupport.Trim(line);
    }

    /// <summary> Get parameters from a string.
    ///
    /// </summary>
    /// <sParas:>  the string contains parameters </sParas:>
    /// <sParaName:>  the parameter's name </sParaName:>
    /// <sAsign:>  the character divide the parameter name and value, normally it is  "=" </sAsign:>
    /// <sDelim:>  the delimiter to seperate the name/value pair in sParas  </sDelim:>
    public static String getPara(String sParas, String sParaName, String sAsign, String sDelim) throws Exception {
        if (sParas == null || sParaName == null || sAsign == null || sDelim == null)
            return null;
         
        String strLCsParas = sParas.toLowerCase();
        String strLCssParaName = sParaName.toLowerCase();
        int ParaNameIndex = strLCsParas.indexOf(strLCssParaName);
        if (ParaNameIndex == -1)
            return null;
         
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        int AsignIndex = SupportClass.getIndex(strLCsParas,sAsign,ParaNameIndex + strLCssParaName.length());
        if (AsignIndex == -1)
            return null;
         
        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        int DelimIndex = SupportClass.getIndex(strLCsParas,sDelim,AsignIndex + sAsign.length());
        if (DelimIndex == -1)
            DelimIndex = sParas.length() - 1;
         
        String strResult = sParas.Substring(AsignIndex + sAsign.length(), (DelimIndex)-(AsignIndex + sAsign.length())).Trim();
        if (strResult.indexOf('"') != -1)
            strResult = deleteQuote(strResult);
         
        return strResult;
    }

    public static String getCapbilityListString(String value) throws Exception {
        if (StringSupport.isNullOrEmpty(value))
            return "";
         
        int lfPos = value.indexOf("\n");
        if (lfPos > -1)
            value = value.substring(0, (0) + (lfPos));
         
        return value;
    }

    /**
    * Remove the quote character(") from a string.
    */
    public static String deleteQuote(String str) throws Exception {
        if (str == null || str.indexOf('"') == -1)
            return null;
         
        String sTmp = str.Substring(str.indexOf('"') + 1, (str.Length)-(str.indexOf('"') + 1));
        if (sTmp.indexOf('"') != -1)
            sTmp = sTmp.substring(0, (0) + ((sTmp.indexOf('"')) - (0)));
         
        return sTmp;
    }

    public static boolean isAlpha(char c) throws Exception {
        if (c <= ' ' || c > 'z')
            return false;
        else
            return true; 
    }

    public static boolean isEosn(char c) throws Exception {
        if (c == '!' || c == '?' || c == '.')
            return true;
        else
            return false; 
    }

    public static boolean isEow(char c) throws Exception {
        String s = " !()-=+[]{};:\"<>?,./\\|\n\r\t";
        if (s.indexOf((char)c) != -1)
            return true;
        else
            return false; 
    }

    //UPGRADE_NOTE: ref keyword was added to struct-type parameters. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1303'"
    public static String calculateListingDate(Date objDate, int iDom) throws Exception {
        //UPGRADE_TODO: The 'System.DateTime' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
        //if (objDate == null)
        //    return null;
        if (iDom > 0)
            iDom = iDom * (-1);
         
        objDate = DateTimeSupport.add(objDate,Calendar.DAY_OF_YEAR,iDom);
        return DateTimeSupport.ToString(objDate, STANDARD_DATE_FORMAT_STRING);
    }

    //System.Globalization.Calendar c1 = new System.Globalization.GregorianCalendar();
    ////UPGRADE_TODO: The differences in the format  of parameters for method 'java.util.Calendar.setTime'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
    //SupportClass.CalendarManager.manager.SetDateTime(c1, objDate);
    ////UPGRADE_TODO: Field 'java.util.Calendar.HOUR' was converted to 'SupportClass.CalendarManager.HOUR' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarHOUR_f'"
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.HOUR, 0);
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.MINUTE, 0);
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.SECOND, 0);
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.MILLISECOND, 0);
    ////UPGRADE_ISSUE: Method 'java.util.Calendar.add' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javautilCalendaradd_int_int'"
    //c1.AddDays(SupportClass.CalendarManager.DATE, iDom);
    ////UPGRADE_TODO: The equivalent in .NET for method 'java.util.Calendar.getTime' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
    //return SupportClass.FormatDateTime(STANDARD_DATE_FORMAT, SupportClass.CalendarManager.manager.GetDateTime(c1));
    public static Date getDate(String sDate) throws Exception {
        Date date = new Date();
        try
        {
            date = DateTimeSupport.parse(sDate);
        }
        catch (Exception exc)
        {
        }

        return date;
    }

    //UPGRADE_TODO: The 'System.DateTime' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
    public static Date getCurDate() throws Exception {
        //UPGRADE_TODO: The 'System.DateTime' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    //System.Globalization.Calendar c1 = new System.Globalization.GregorianCalendar();
    //System.String sCurDate = getCurTimeDate();
    ////UPGRADE_TODO: The differences in the format  of parameters for method 'java.util.Calendar.setTime'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
    //SupportClass.CalendarManager.manager.SetDateTime(c1, getDate(sCurDate));
    ////UPGRADE_TODO: Field 'java.util.Calendar.HOUR' was converted to 'SupportClass.CalendarManager.HOUR' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarHOUR_f'"
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.HOUR, 0);
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.MINUTE, 0);
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.SECOND, 0);
    //SupportClass.CalendarManager.manager.Set(c1, SupportClass.CalendarManager.MILLISECOND, 0);
    ////UPGRADE_TODO: The equivalent in .NET for method 'java.util.Calendar.getTime' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
    //date = SupportClass.CalendarManager.manager.GetDateTime(c1);
    //return date;
    //public static System.String getCurTimeDate()
    //{
    //    System.Globalization.Calendar calendar = new System.Globalization.GregorianCalendar();
    //    System.Text.StringBuilder strb = new System.Text.StringBuilder(20);
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    strb.Append(SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.MONTH) + 1);
    //    strb.Append("/");
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    strb.Append(SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.DAY_OF_MONTH));
    //    strb.Append("/");
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    strb.Append(SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.YEAR));
    //    strb.Append(" ");
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    strb.Append(SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.HOUR_OF_DAY));
    //    strb.Append(":");
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    strb.Append(SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.MINUTE));
    //    strb.Append(":");
    //    //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
    //    strb.Append(SupportClass.CalendarManager.manager.Get(calendar, SupportClass.CalendarManager.SECOND));
    //    return strb.ToString();
    //}
    public static int stringDefFind(String str, char find, int index) throws Exception {
        int length = str.length();
        boolean inQuotes = false;
        for (int i = index;i < length;i++)
        {
            char c = str.charAt(i);
            if (c == '"')
                inQuotes = !inQuotes;
            else if (!inQuotes && c == find)
                return i;
              
        }
        return -1;
    }

    public static int stringDefFind(String str, char find) throws Exception {
        return stringDefFind(str,find,0);
    }

    public static String formatDate(String oldFormatDate, String newFormatDate, String inDate) throws Exception {
        if (inDate == null || inDate.length() <= 0)
            return "";
         
        inDate = StringSupport.Trim(inDate);
        oldFormatDate = oldFormatDate.replace('D', 'd').replace('Y', 'y');
        newFormatDate = newFormatDate.replace('D', 'd').replace('Y', 'y');
        Date pDate;
        try
        {
            if (oldFormatDate != null && oldFormatDate.length() > 0)
                pDate = Date.ParseExact(inDate, oldFormatDate, null);
            else
            {
                inDate = inDate.replace("T", " ");
                pDate = DateTimeSupport.parse(inDate);
            } 
            return DateTimeSupport.ToString(pDate, newFormatDate);
        }
        catch (Exception e)
        {
            //, fmt);
            inDate = "";
        }

        return inDate;
    }

    public static String formatDate(String oldFormatDate, String newFormatDate, String inDate, TimeZoneInfoEx sourceTimeZone, TimeZoneInfoEx desTimeZone) throws Exception {
        if (inDate == null || inDate.length() <= 0)
            return "";
         
        inDate = StringSupport.Trim(inDate);
        oldFormatDate = oldFormatDate.replace('D', 'd').replace('Y', 'y');
        newFormatDate = newFormatDate.replace('D', 'd').replace('Y', 'y');
        Date pDate;
        try
        {
            if (oldFormatDate != null && oldFormatDate.length() > 0)
                pDate = Date.ParseExact(inDate, oldFormatDate, null);
            else
            {
                inDate = inDate.replace("T", " ");
                pDate = DateTimeSupport.parse(inDate);
            } 
            if (sourceTimeZone == null && desTimeZone != null)
                pDate = TimeZoneInfoEx.ConvertUtcToTimeZone(pDate, desTimeZone);
            else if (sourceTimeZone != null && desTimeZone == null)
                pDate = TimeZoneInfoEx.ConvertTimeZoneToUtc(pDate, sourceTimeZone);
              
            return DateTimeSupport.ToString(pDate, newFormatDate);
        }
        catch (Exception e)
        {
            inDate = "";
        }

        return inDate;
    }

    public static Nullable<Date> parseDateTime(String oldFormatDate, String inDate) throws Exception {
        if (inDate == null || inDate.length() <= 0)
            return null;
         
        Nullable<Date> pDate = new Date();
        inDate = StringSupport.Trim(inDate);
        inDate = inDate.replace("T", " ");
        oldFormatDate = oldFormatDate.replace('D', 'd').replace('Y', 'y');
        try
        {
            // parse the incoming date
            if (oldFormatDate != null && oldFormatDate.length() > 0)
                pDate = Date.ParseExact(inDate, oldFormatDate, null);
            else
                pDate = DateTimeSupport.parse(inDate); 
            return pDate;
        }
        catch (Exception e)
        {
            //, fmt);
            // convert the parsed date back to a string
            //convDate = SupportClass.FormatDateTime(fmt, pDate).Trim();
            pDate = null;
        }

        return pDate;
    }

    public static StringBuilder readFile2String(String filename) throws Exception {
        FileStreamSupport stream;
        StringBuilder file_context = new StringBuilder();
        //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
        stream = new FileStreamSupport(filename, FileMode.Open, FileAccess.Read);
        System.IO.BufferedStream reader = null;
        int b;
        reader = new System.IO.BufferedStream(stream);
        while ((b = reader.ReadByte()) > -1)
        if (b != 0)
            file_context.append((char)b);
         
        if (reader != null)
        {
            //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
            reader.Close();
        }
         
        if (stream != null)
            stream.close();
         
        return file_context;
    }

    //public static bool unzipFromServer(System.String sourcePath, System.String targetPath, System.String fileName)
    //{
    //    int sChunk = 8192;
    //    ZipInputStream zipin;
    //    bool bUpdateSucceeded = false;
    //    try
    //    {
    //        //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
    //        System.Uri url = new System.Uri(sourcePath);
    //        System.IO.Stream in_Renamed = System.Net.WebRequest.Create(url).GetResponse().GetResponseStream();
    //        zipin = new ZipInputStream(in_Renamed);
    //    }
    //    catch (System.IO.IOException e)
    //    {
    //        SupportClass.WriteStackTrace(e, Console.Error);
    //        return false;
    //    }
    //    sbyte[] buffer = new sbyte[sChunk];
    //    try
    //    {
    //        ZipEntry ze;
    //        System.String zipEntryName;
    //        bool bFound = false;
    //        while ((ze = zipin.getNextEntry()) != null)
    //        {
    //            zipEntryName = ze.getName();
    //            if (zipEntryName.LastIndexOf("/") != - 1)
    //                zipEntryName = zipEntryName.Substring(zipEntryName.LastIndexOf("/") + 1);
    //            if (zipEntryName.ToUpper().Equals(fileName.ToUpper()))
    //            {
    //                bFound = true;
    //                break;
    //            }
    //        }
    //        if (!bFound)
    //        {
    //            throw new System.Exception("the requested component wasn't found in the archive");
    //        }
    //        long size = 0;
    //        //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
    //        System.IO.FileStream out_Renamed = new System.IO.FileStream(targetPath + fileName + ".tmp", System.IO.FileMode.Create);
    //        int length;
    //        while ((length = zipin.read(buffer, 0, sChunk)) != - 1)
    //        {
    //            out_Renamed.Write(SupportClass.ToByteArray(buffer), 0, length);
    //            size += length;
    //        }
    //        out_Renamed.Close();
    //        System.IO.FileInfo f1 = new System.IO.FileInfo(targetPath + fileName);
    //        long size2 = SupportClass.FileLength(f1);
    //        System.IO.FileInfo f2 = new System.IO.FileInfo(targetPath + fileName + ".tmp");
    //        bool b1 = false;
    //        bool b2 = false;
    //        bool tmpBool;
    //        if (System.IO.File.Exists(f1.FullName))
    //            tmpBool = true;
    //        else
    //            tmpBool = System.IO.Directory.Exists(f1.FullName);
    //        if (tmpBool)
    //        {
    //            bool tmpBool2;
    //            if (System.IO.File.Exists(f1.FullName))
    //            {
    //                System.IO.File.Delete(f1.FullName);
    //                tmpBool2 = true;
    //            }
    //            else if (System.IO.Directory.Exists(f1.FullName))
    //            {
    //                System.IO.Directory.Delete(f1.FullName);
    //                tmpBool2 = true;
    //            }
    //            else
    //                tmpBool2 = false;
    //            b1 = tmpBool2;
    //        }
    //        else
    //            b1 = true;
    //        //UPGRADE_TODO: Method 'java.io.File.renameTo' was converted to ' System.IO.FileInfo.MoveTo' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilerenameTo_javaioFile'"
    //        b2 = f2.MoveTo(f1.FullName);
    //        if (!b2)
    //        {
    //            bool tmpBool3;
    //            if (System.IO.File.Exists(f2.FullName))
    //            {
    //                System.IO.File.Delete(f2.FullName);
    //                tmpBool3 = true;
    //            }
    //            else if (System.IO.Directory.Exists(f2.FullName))
    //            {
    //                System.IO.Directory.Delete(f2.FullName);
    //                tmpBool3 = true;
    //            }
    //            else
    //                tmpBool3 = false;
    //            bool generatedAux = tmpBool3;
    //        } //if the rename failed, need to delete the temp file
    //        if (size != size2)
    //        {
    //            //the target systems needs to be updated
    //            bUpdateSucceeded = (b1 && b2);
    //        }
    //        else
    //        {
    //            if (!(b1 && b2))
    //                System.Console.Out.WriteLine(fileName + " has the same size and it's locked");
    //            bUpdateSucceeded = true;
    //        }
    //    }
    //    catch (System.IO.IOException e)
    //    {
    //        System.Console.Out.WriteLine("Couldn't decompress the archive");
    //        return false;
    //    }
    //    try
    //    {
    //        //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
    //        zipin.Close();
    //    }
    //    catch (System.IO.IOException e)
    //    {
    //        System.Console.Out.WriteLine("Couldn't close the archive");
    //        return false;
    //    }
    //    return bUpdateSucceeded;
    //}
    /**
    * Replaces %0% with the param1; %1% with the param2; %2% with the param3.
    *  @return  the string with the the replacements done.
    * 
    *  @param param1 param for replacement of %0%.
    * 
    *  @param param2 param for replacement of %1%.
    * 
    *  @param param3 param for replacement of %2%.
    */
    public static String formatString(String str, String param1, String param2, String param3) throws Exception {
        str = replaceSubStr(str,"%0%",param1);
        str = replaceSubStr(str,"%1%",param2);
        return replaceSubStr(str,"%2%",param3);
    }

    /**
    * Replaces %0% with the param1; %1% with the param2.
    *  @return  the string with the the replacements done.
    * 
    *  @param param1 param for replacement of %0%.
    * 
    *  @param param2 param for replacement of %1%.
    */
    public static String formatString(String str, String param1, String param2) throws Exception {
        str = replaceSubStr(str,"%0%",param1);
        return replaceSubStr(str,"%1%",param2);
    }

    /**
    * Replaces %0% with the param1.
    *  @return  the string with the the replacements done.
    * 
    *  @param param1 param for replacement of %0%.
    */
    public static String formatString(String str, String param1) throws Exception {
        return replaceSubStr(str,"%0%",param1);
    }

    public static String base64Decode(String data) throws Exception {
        try
        {
            if (data == null || data.length() == 0)
                return "";
             
            if ((data.length() % 4) == 1)
                data += "===";
            else if ((data.length() % 4) == 2)
                data += "==";
            else if ((data.length() % 4) == 3)
                data += "=";
               
            System.Text.ASCIIEncoding encoder = new System.Text.ASCIIEncoding();
            System.Text.Decoder utf8Decode = encoder.GetDecoder();
            byte[] todecode_byte = Base64.decodeBase64(data);
            int charCount = utf8Decode.GetCharCount(todecode_byte, 0, todecode_byte.length);
            char[] decoded_char = new char[charCount];
            utf8Decode.GetChars(todecode_byte, 0, todecode_byte.length, decoded_char, 0);
            String result = new String(decoded_char);
            return result;
        }
        catch (Exception e)
        {
            throw new Exception("Error in base64Decode" + e.getMessage());
        }
    
    }

    //static public sbyte[] base64Decode(char[] data)
    //{
    //    sbyte[] codes = new sbyte[256];
    //    for (int i = 0; i < 256; i++)
    //        codes[i] = - 1;
    //    for (int i = 'A'; i <= 'Z'; i++)
    //        codes[i] = (sbyte) (i - 'A');
    //    for (int i = 'a'; i <= 'z'; i++)
    //        codes[i] = (sbyte) (26 + i - 'a');
    //    for (int i = '0'; i <= '9'; i++)
    //        codes[i] = (sbyte) (52 + i - '0');
    //    codes['+'] = 62;
    //    codes['/'] = 63;
    //    // as our input could contain non-BASE64 data (newlines,
    //    // whitespace of any sort, whatever) we must first adjust
    //    // our count of USABLE data so that...
    //    // (a) we don't misallocate the output array, and
    //    // (b) think that we miscalculated our data length
    //    //     just because of extraneous throw-away junk
    //    int tempLen = data.Length;
    //    for (int ix = 0; ix < data.Length; ix++)
    //    {
    //        if ((data[ix] > 255) || codes[data[ix]] < 0)
    //            --tempLen; // ignore non-valid chars and padding
    //    }
    //    // calculate required length:
    //    //  -- 3 bytes for every 4 valid base64 chars
    //    //  -- plus 2 bytes if there are 3 extra base64 chars,
    //    //     or plus 1 byte if there are 2 extra.
    //    int len = (tempLen / 4) * 3;
    //    if ((tempLen % 4) == 3)
    //        len += 2;
    //    if ((tempLen % 4) == 2)
    //        len += 1;
    //    sbyte[] out_Renamed = new sbyte[len];
    //    int shift = 0; // # of excess bits stored in accum
    //    int accum = 0; // excess bits
    //    int index = 0;
    //    // we now go through the entire array (NOT using the 'tempLen' value)
    //    for (int ix = 0; ix < data.Length; ix++)
    //    {
    //        int value_Renamed = (data[ix] > 255)?- 1:codes[data[ix]];
    //        if (value_Renamed >= 0)
    //        // skip over non-code
    //        {
    //            accum <<= 6; // bits shift up by 6 each time thru
    //            shift += 6; // loop, with new bits being put in
    //            accum |= value_Renamed; // at the bottom.
    //            if (shift >= 8)
    //            // whenever there are 8 or more shifted in,
    //            {
    //                shift -= 8; // write them out (from the top, leaving any
    //                out_Renamed[index++] = (sbyte) ((accum >> shift) & 0xff);
    //            }
    //        }
    //        // we will also have skipped processing a padding null byte ('=') here;
    //        // these are used ONLY for padding to an even length and do not legally
    //        // occur as encoded data. for this reason we can ignore the fact that
    //        // no index++ operation occurs in that special case: the out[] array is
    //        // initialized to all-zero bytes to start with and that works to our
    //        // advantage in this combination.
    //    }
    //    // if there is STILL something wrong we just have to throw up now!
    //    if (index != out_Renamed.Length)
    //    {
    //        throw new System.ApplicationException("Miscalculated data length (wrote " + index + " instead of " + out_Renamed.Length + ")");
    //    }
    //    return out_Renamed;
    //}
    public static String base64Encode(String data) throws Exception {
        try
        {
            byte[] encData_byte = new byte[data.length()];
            encData_byte = EncodingSupport.GetEncoder("ASCII").getBytes(data);
            String encodedData = Base64.encodeBase64String(encData_byte);
            return encodedData;
        }
        catch (Exception e)
        {
            throw new Exception("Error in base64Encode " + e.getMessage());
        }
    
    }

    /**
    * Utility routine to perform an efficient in-place sort of the
    * specified String array.
    * Perform a heapsort (Numerical Recipes in C, 1988).
    */
    public static void sortStringsArray(String[] strings) throws Exception {
        String testString;
        int n = strings.length;
        if (n <= 1)
            return ;
         
        for (int i, j, k = (n >> 1), ir = (n - 1);;)
        {
            // Nothing to sort
            // Index k will be decremented from initial value to 0 during
            // "hiring" (heap creation) phase. Once it reaches 0, index
            // ir is decremented from its initial value to 0 during
            // "retirement and promotion" phase.
            if (k > 0)
            {
                // Still in hiring phase
                testString = strings[--k];
            }
            else
            {
                // In retirement and promotion phase
                testString = strings[ir];
                // Save end of array
                strings[ir] = strings[0];
                // Retire top of heap to end of array
                if (--ir <= 0)
                {
                    // Done with last promotion
                    strings[0] = testString;
                    break;
                }
                 
            } 
            for (i = k, j = (k << 1) + 1;j <= ir;)
            {
                // Restore end of array
                // All done. Exit.
                // Sift down testString to its proper level
                if ((j < ir) && (String.CompareOrdinal(strings[j].toUpperCase(), strings[j + 1].toUpperCase()) < 0))
                    j++;
                 
                if ((String.CompareOrdinal(strings[j].toUpperCase(), testString.toUpperCase()) < 0))
                    break;
                 
                strings[i] = strings[j];
                i = j;
                j += j + 1;
            }
            strings[i] = testString;
        }
    }

    public static String[] stringSplit(String input, String delimiter) throws Exception {
        SupportClass.Tokenizer st = new SupportClass.Tokenizer(input,delimiter);
        ArrayList tokens = ArrayList.Synchronized(new ArrayList(10));
        while (st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        String[] output = new String[tokens.size()];
        IEnumerator outputTokens = EnumeratorSupport.mk(tokens.iterator());
        int i = 0;
        while (outputTokens.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            output[i++] = ((String)outputTokens.getCurrent());
        }
        return output;
    }

    public static String[] stringSplitEx(String input, String delimiter) throws Exception {
        StringTokenizerEx st = new StringTokenizerEx(input,delimiter);
        ArrayList tokens = ArrayList.Synchronized(new ArrayList(10));
        while (st.hasMoreElements())
        {
            tokens.add(st.nextElement());
        }
        String[] output = new String[tokens.size()];
        IEnumerator outputTokens = EnumeratorSupport.mk(tokens.iterator());
        int i = 0;
        while (outputTokens.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            output[i++] = ((String)outputTokens.getCurrent());
        }
        return output;
    }

    public static void printCurrentTime(String title) throws Exception {
    }

    //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
    //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
    //SimpleDateFormat TIME = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
    //System.Console.Out.Write(title + " : " + SupportClass.FormatDateTime(TIME, System.DateTime.Now) + "\r\n");
    public static String readFile(String path) throws Exception {
        BufferedReader sr = new BufferedReader(StreamReader.make(new BufferedInputStream(new FileInputStream(path))));
        try
        {
            {
                return TextReaderSupport.readToEnd(sr);
            }
        }
        finally
        {
            if (sr != null)
                Disposable.mkDisposable(sr).dispose();
             
        }
    }

    public static boolean extractTo(String zipPath, String extractToDir) throws Exception {
        FastZip fz = new FastZip();
        fz.ExtractZip(zipPath, extractToDir, "");
        return true;
    }

    public static void copyFolder(File source, File target) throws Exception {
        // Check if the target directory exists, if not, create it.
        if (File.Exists(target.FullName) == false)
        {
            File.CreateDirectory(target.FullName);
        }
         
        for (File fi : source.listFiles(new FilterOnlyFiles()))
        {
            // Copy each file into it’s new directory.
            fi.CopyTo((new File(target.toString(), fi.getName())).toString(), true);
        }
        for (Object __dummyForeachVar1 : source.listFiles(new FilterOnlyDirs()))
        {
            // Copy each subdirectory using recursion.
            File diSourceSubDir = (File)__dummyForeachVar1;
            File nextTargetSubDir = target.CreateSubdirectory(diSourceSubDir.getName());
            copyFolder(diSourceSubDir,nextTargetSubDir);
        }
    }

}


