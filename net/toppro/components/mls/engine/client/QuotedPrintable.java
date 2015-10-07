//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:36 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.IO.FileNotFoundException;
import CS2JNet.System.Text.RegularExpressions.GroupCollection;
import CS2JNet.System.Text.RegularExpressions.Match;
import CS2JNet.System.Text.RegularExpressions.RegexOptions;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Pattern;

/**
* Robust and fast implementation of Quoted Printable
* Multipart Internet Mail Encoding (MIME) which encodes every
* character, not just "special characters" for transmission over
* SMTP.
* 
* More information on the quoted-printable encoding can be found
* here: http://www.freesoft.org/CIE/RFC/1521/6.htm
* 
* detailed in: RFC 1521
* 
* more info: http://www.freesoft.org/CIE/RFC/1521/6.htm
* 
* The QuotedPrintable class encodes and decodes strings and files
* that either were encoded or need encoded in the Quoted-Printable
* MIME encoding for Internet mail. The encoding methods of the class
* use pointers wherever possible to guarantee the fastest possible
* encoding times for any size file or string. The decoding methods
* use only the .NET framework classes.
* 
* The Quoted-Printable implementation
* is robust which means it encodes every character to ensure that the
* information is decoded properly regardless of machine or underlying
* operating system or protocol implementation. The decode can recognize
* robust encodings as well as minimal encodings that only encode special
* characters and any implementation in between. Internally, the
* class uses a regular expression replace pattern to decode a quoted-
* printable string or file.
* 
* This example shows how to quoted-printable encode an html file and then
* decode it.
* 
*  {@code 
* string encoded = QuotedPrintable.EncodeFile(
* @"C:\WEBS\wwwroot\index.html"
* );
* 
* string decoded = QuotedPrintable.Decode(encoded);
* 
* Console.WriteLine(decoded);
* }
*/
public class QuotedPrintable   
{
    private QuotedPrintable() throws Exception {
    }

    /**
    * Gets the maximum number of characters per quoted-printable
    * line as defined in the RFC minus 1 to allow for the =
    * character (soft line break).
    * 
    * (Soft Line Breaks): The Quoted-Printable encoding REQUIRES
    * that encoded lines be no more than 76 characters long. If
    * longer lines are to be encoded with the Quoted-Printable
    * encoding, 'soft' line breaks must be used. An equal sign
    * as the last character on a encoded line indicates such a
    * non-significant ('soft') line break in the encoded text.
    */
    public static final int RFC_1521_MAX_CHARS_PER_LINE = 75;
    /**
    * Encodes a very large string into the Quoted-Printable
    * encoding for transmission via SMTP
    * 
    *  @param toencode 
    * the very large string to encode
    * 
    *  @return The Quoted-Printable encoded string
    *  @throws ObjectDisposedException 
    * A problem occurred while attempting to read the encoded
    * string.
    * 
    *  @throws OutOfMemoryException 
    * There is insufficient memory to allocate a buffer for the
    * returned string.
    * 
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    *  @throws IOException 
    * An I/O error occurs, such as the stream being closed.
    * 
    *  @throws ArgumentOutOfRangeException 
    * The charsperline argument is less than or equal to 0.
    * 
    * This method encodes a large string into the quoted-printable
    * encoding and then properly formats it into lines of 76 characters
    * using the 
    *  {@link #FormatEncodedString}
    *  method.
    */
    //public static string Encode(string toencode)
    //{
    //    return Encode(toencode, RFC_1521_MAX_CHARS_PER_LINE);
    //}
    /**
    * Encodes a very large string into the Quoted-Printable
    * encoding for transmission via SMTP
    * 
    *  @param toencode 
    * the very large string to encode
    * 
    *  @param charsperline 
    * the number of chars per line after encoding
    * 
    *  @return The Quoted-Printable encoded string
    *  @throws ObjectDisposedException 
    * A problem occurred while attempting to read the encoded
    * string.
    * 
    *  @throws OutOfMemoryException 
    * There is insufficient memory to allocate a buffer for the
    * returned string.
    * 
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    *  @throws IOException 
    * An I/O error occurs, such as the stream being closed.
    * 
    *  @throws ArgumentOutOfRangeException 
    * The charsperline argument is less than or equal to 0.
    * 
    * This method encodes a large string into the quoted-printable
    * encoding and then properly formats it into lines of
    * charsperline characters using the 
    *  {@link #FormatEncodedString}
    * 
    * method.
    */
    //public static string Encode(string toencode, int charsperline)
    //{
    //    if (toencode == null)
    //        throw new ArgumentNullException();
    //    if (charsperline <= 0)
    //        throw new ArgumentOutOfRangeException();
    //    string line, encodedHtml = "";
    //    StringReader sr = new StringReader(toencode);
    //    try
    //    {
    //        while ((line = sr.ReadLine()) != null)
    //            encodedHtml += EncodeSmallLine(line);
    //        return FormatEncodedString(encodedHtml, charsperline);
    //    }
    //    finally
    //    {
    //        sr.Close();
    //        sr = null;
    //    }
    //}
    /**
    * Encodes a file's contents into a string using
    * the Quoted-Printable encoding.
    * 
    *  @param filepath 
    * The path to the file to encode.
    * 
    *  @return The Quoted-Printable encoded string
    *  @throws ObjectDisposedException 
    * A problem occurred while attempting to encode the
    * string.
    * 
    *  @throws OutOfMemoryException 
    * There is insufficient memory to allocate a buffer for the
    * returned string.
    * 
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    *  @throws IOException 
    * An I/O error occurs, such as the stream being closed.
    * 
    *  @throws FileNotFoundException 
    * The file was not found.
    * 
    *  @throws SecurityException 
    * The caller does not have the required permission to open
    * the file specified in filepath.
    * 
    *  @throws UnauthorizedAccessException 
    * filepath is read-only or a directory.
    * 
    * This method encodes a file's text into the quoted-printable
    * encoding and then properly formats it into lines of 76 characters
    * using the 
    *  {@link #FormatEncodedString}
    *  method.
    */
    //public static string EncodeFile(string filepath)
    //{
    //    return EncodeFile(filepath, RFC_1521_MAX_CHARS_PER_LINE);
    //}
    /**
    * Encodes a file's contents into a string using
    * the Quoted-Printable encoding.
    * 
    *  @param filepath 
    * The path to the file to encode.
    * 
    *  @param charsperline 
    * the number of chars per line after encoding
    * 
    *  @return The Quoted-Printable encoded string
    *  @throws ObjectDisposedException 
    * A problem occurred while attempting to encode the
    * string.
    * 
    *  @throws OutOfMemoryException 
    * There is insufficient memory to allocate a buffer for the
    * returned string.
    * 
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    *  @throws IOException 
    * An I/O error occurs, such as the stream being closed.
    * 
    *  @throws FileNotFoundException 
    * The file was not found.
    * 
    *  @throws SecurityException 
    * The caller does not have the required permission to open
    * the file specified in filepath.
    * 
    *  @throws UnauthorizedAccessException 
    * filepath is read-only or a directory.
    * 
    * This method encodes a file's text into the quoted-printable
    * encoding and then properly formats it into lines of
    * charsperline characters using the 
    *  {@link #FormatEncodedString}
    * 
    * method.
    */
    //public static string EncodeFile(string filepath, int charsperline)
    //{
    //    if (filepath == null)
    //        throw new ArgumentNullException();
    //    string encodedHtml = "", line;
    //    FileInfo f = new FileInfo(filepath);
    //    if (!f.Exists)
    //        throw new FileNotFoundException();
    //    StreamReader sr = f.OpenText();
    //    try
    //    {
    //        while ((line = sr.ReadLine()) != null)
    //            encodedHtml += EncodeSmallLine(line);
    //        return FormatEncodedString(encodedHtml, charsperline);
    //    }
    //    finally
    //    {
    //        sr.Close();
    //        sr = null;
    //        f = null;
    //    }
    //}
    /**
    * Encodes a small string into the Quoted-Printable encoding
    * for transmission via SMTP. The string is not split
    * into lines of X characters like the string that the
    * Encode method returns.
    * 
    *  @param s 
    * The string to encode.
    * 
    *  @return The Quoted-Printable encoded string
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    * This method encodes a small string into the quoted-printable
    * encoding. The resultant encoded string has NOT been separated
    * into lined results using the 
    *  {@link #FormatEncodedString}
    * 
    * method.
    */
    //public unsafe static string EncodeSmall(string s)
    //{
    //    if (s == null)
    //        throw new ArgumentNullException();
    //    string result = "";
    //    fixed (char* pChar = s)
    //    {
    //        char* pCurrent = pChar;
    //        do
    //        {
    //            int code = (*pCurrent);
    //            result += String.Format("={0}", code.ToString("X2"));
    //            pCurrent++;
    //        }
    //        while (*pCurrent != 0);
    //    }
    //    return result;
    //}
    /**
    * Encodes a small string with an appended newline into the
    * Quoted-Printable encoding for transmission via SMTP. The
    * string is not split into lines of X characters like the
    * string that the Encode or the EncodeFile methods return.
    * 
    *  @param s 
    * The string to encode.
    * 
    *  @return The Quoted-Printable encoded string
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    * This method encodes a small string into the quoted-printable
    * encoding. The resultant encoded string has NOT been separated
    * into lined results using the 
    *  {@link #FormatEncodedString}
    * 
    * method.
    */
    //public static string EncodeSmallLine(string s)
    //{
    //    if (s == null)
    //        throw new ArgumentNullException();
    //    return EncodeSmall(s + "\r\n");
    //}
    /**
    * Formats a quoted-printable string into lines equal to maxcharlen,
    * following all protocol rules such as byte stuffing. This method is
    * called automatically by the Encode method and the EncodeFile method.
    * 
    *  @param qpstr 
    * the quoted-printable encoded string.
    * 
    *  @param maxcharlen 
    * the number of chars per line after encoding.
    * 
    *  @return 
    * The properly formatted Quoted-Printable encoded string in lines of
    * 76 characters as defined by the RFC.
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    *  @throws IOException 
    * An I/O error occurs, such as the stream being closed.
    * 
    * Formats a quoted-printable encoded string into lines of
    * maxcharlen characters for transmission via SMTP.
    */
    //public unsafe static string FormatEncodedString(string qpstr, int maxcharlen)
    //{
    //    if (qpstr == null)
    //        throw new ArgumentNullException();
    //    string strout = "";
    //    StringWriter qpsw = new StringWriter();
    //    try
    //    {
    //        fixed (char* pChr = qpstr)
    //        {
    //            char* pCurrent = pChr;
    //            int i = 0;
    //            do
    //            {
    //                strout += pCurrent->ToString();
    //                i++;
    //                if (i == maxcharlen)
    //                {
    //                    qpsw.WriteLine("{0}=", strout);
    //                    qpsw.Flush();
    //                    i = 0;
    //                    strout = "";
    //                }
    //                pCurrent++;
    //            }
    //            while (*pCurrent != 0);
    //        }
    //        qpsw.WriteLine(strout);
    //        qpsw.Flush();
    //        return qpsw.ToString();
    //    }
    //    finally
    //    {
    //        qpsw.Close();
    //        qpsw = null;
    //    }
    //}
    static String hexDecoderEvaluator(Match m) throws Exception {
        String hex = GroupCollection.mk(m).get(2).getValue();
        int iHex = Convert.ToInt32(hex, 16);
        char c = (char)iHex;
        return String.valueOf(c);
    }

    static String hexDecoder(String line) throws Exception {
        if (line == null)
            throw new ArgumentNullException();
         
        //parse looking for =XX where XX is hexadecimal
        Pattern re = Pattern.compile("(\\=([0-9A-F][0-9A-F]))", Pattern.CASE_INSENSITIVE );
        return re.Replace(line, new MatchEvaluator(HexDecoderEvaluator));
    }

    /**
    * decodes an entire file's contents into plain text that
    * was encoded with quoted-printable.
    * 
    *  @param filepath 
    * The path to the quoted-printable encoded file to decode.
    * 
    *  @return The decoded string.
    *  @throws ObjectDisposedException 
    * A problem occurred while attempting to decode the
    * encoded string.
    * 
    *  @throws OutOfMemoryException 
    * There is insufficient memory to allocate a buffer for the
    * returned string.
    * 
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    *  @throws IOException 
    * An I/O error occurs, such as the stream being closed.
    * 
    *  @throws FileNotFoundException 
    * The file was not found.
    * 
    *  @throws SecurityException 
    * The caller does not have the required permission to open
    * the file specified in filepath.
    * 
    *  @throws UnauthorizedAccessException 
    * filepath is read-only or a directory.
    * 
    * Decodes a quoted-printable encoded file into a string
    * of unencoded text of any size.
    */
    public static String decodeFile(String filepath) throws Exception {
        if (filepath == null)
            throw new ArgumentNullException();
         
        String decodedHtml = "", line;
        File f = new File(filepath);
        if (!f.Exists)
            throw new FileNotFoundException();
         
        BufferedReader sr = f.OpenText();
        try
        {
            while ((line = sr.readLine()) != null)
            decodedHtml += decode(line);
            return decodedHtml;
        }
        finally
        {
            sr.close();
            sr = null;
            f = null;
        }
    }

    /**
    * Decodes a Quoted-Printable string of any size into
    * it's original text.
    * 
    *  @param encoded 
    * The encoded string to decode.
    * 
    *  @return The decoded string.
    *  @throws ArgumentNullException 
    * A string is passed in as a null reference.
    * 
    * Decodes a quoted-printable encoded string into a string
    * of unencoded text of any size.
    */
    public static String decode(String encoded) throws Exception {
        if (encoded == null)
            throw new ArgumentNullException();
         
        String line;
        StringWriter sw = new StringWriter();
        StringReader sr = new StringReader(encoded);
        try
        {
            while ((line = sr.readLine()) != null)
            {
                if (line.endsWith("="))
                    sw.print(hexDecoder(line.substring(0, (0) + (line.length() - 1))));
                else
                    sw.println(hexDecoder(line)); 
                sw.Flush();
            }
            return sw.toString();
        }
        finally
        {
            sw.close();
            sr.close();
            sw = null;
            sr = null;
        }
    }

}


