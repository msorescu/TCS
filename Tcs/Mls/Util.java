//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:34:03 PM
//

package Tcs.Mls;

import CS2JNet.JavaSupport.language.RefSupport;
import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.IO.TextReaderSupport;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.EncodingSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import Tcs.Mls.TCSException;
import Tcs.Mls.XmlSanitizingStream;

public class Util   
{
    private static String m_configString = "";
    private static boolean m_logModeOn = false;
    private static String m_homePath = "c:\\TCSFiles";
    private static final String LOG_FOLDER = "TCSLog";
    private static final String UPLOAD_FOLDER = "UPLOAD";
    private static final String RESULT_FOLDER = "Result";
    private static final String METADATA_FOLDER = "Metadata";
    private static String m_picURL = "";
    private static boolean m_useHTTPAPI = false;
    private static String m_binFolder = "c:\\tcs\\bin";
    public static String getHomePath() throws Exception {
        return m_homePath;
    }

    public static void setHomePath(String value) throws Exception {
        m_homePath = value;
    }

    public static boolean getLogOn() throws Exception {
        return m_logModeOn;
    }

    public static void setLogOn(boolean value) throws Exception {
        m_logModeOn = value;
    }

    public static String getPictureURL() throws Exception {
        return m_picURL;
    }

    public static void setPictureURL(String value) throws Exception {
        m_picURL = value;
    }

    public static String getNewGuid() throws Exception {
        return UUID.randomUUID().toString();
    }

    public static HashMap<String,String> loadStatesAndProvinces() throws Exception {
        HashMap<String,String> sp = new HashMap<String,String>();
        sp.put("ALABAMA", "AL");
        sp.put("ALASKA", "AK");
        sp.put("AMERICAN SAMOA", "AS");
        sp.put("ARIZONA", "AZ");
        sp.put("ARKANSAS", "AR");
        sp.put("CALIFORNIA", "CA");
        sp.put("COLORADO", "CO");
        sp.put("CONNECTICUT", "CT");
        sp.put("District of Columbia", "DC");
        sp.put("DELAWARE", "DE");
        sp.put("FLORIDA", "FL");
        sp.put("FEDERATED STATES OF MICRONESIA", "FM");
        sp.put("GEORGIA", "GA");
        sp.put("GUAM", "GU");
        sp.put("HAWAII", "HI");
        sp.put("IDAHO", "ID");
        sp.put("ILLINOIS", "IL");
        sp.put("INDIANA", "IN");
        sp.put("IOWA", "IA");
        sp.put("KANSAS", "KS");
        sp.put("KENTUCKY", "KY");
        sp.put("LOUISIANA", "LA");
        sp.put("MAINE", "ME");
        sp.put("MARYLAND", "MD");
        sp.put("MASSACHUSETTS", "MA");
        sp.put("MARSHALL ISLANDS", "MH");
        sp.put("MICHIGAN", "MI");
        sp.put("MINNESOTA", "MN");
        sp.put("MISSISSIPPI", "MS");
        sp.put("MISSOURI", "MO");
        sp.put("MONTANA", "MT");
        sp.put("NEBRASKA", "NE");
        sp.put("NEVADA", "NV");
        sp.put("NEW HAMPSHIRE", "NH");
        sp.put("NEW JERSEY", "NJ");
        sp.put("NEW MEXICO", "NM");
        sp.put("NEW YORK", "NY");
        sp.put("NORTH CAROLINA", "NC");
        sp.put("NORTH DAKOTA", "ND");
        sp.put("NORTHERN MARIANA ISLANDS", "MP");
        sp.put("OHIO", "OH");
        sp.put("OKLAHOMA", "OK");
        sp.put("OREGON", "OR");
        sp.put("PENNSYLVANIA", "PA");
        sp.put("PALAU", "PW");
        sp.put("PUERTO RICO", "PR");
        sp.put("RHODE ISLAND", "RI");
        sp.put("SOUTH CAROLINA", "SC");
        sp.put("SOUTH DAKOTA", "SD");
        sp.put("TENNESSEE", "TN");
        sp.put("TEXAS", "TX");
        sp.put("UTAH", "UT");
        sp.put("VERMONT", "VT)");
        sp.put("VIRGINIA", "VA");
        sp.put("VIRGIN ISLANDS", "VI");
        sp.put("WASHINGTON", "WA");
        sp.put("WEST VIRGINIA", "WV");
        sp.put("WISCONSIN", "WI");
        sp.put("WYOMING", "WY");
        sp.put("ALBERTA", "AB");
        sp.put("BRITISH COLUMBIA", "BC");
        sp.put("MANITOBA", "MB");
        sp.put("NEW BRUNSWICK", "NB");
        sp.put("NEWFOUNDLAND AND LABRADOR", "NL");
        sp.put("NORTHWEST TERRITORIES", "NT");
        sp.put("NOVA SCOTIA", "NS");
        sp.put("NUNAVUT", "NU");
        sp.put("ONTARIO", "ON");
        sp.put("PRINCE EDWARD ISLAND", "PE");
        sp.put("QUEBEC", "QC");
        sp.put("SASKATCHEWAN", "SK");
        sp.put("YUKON", "YT");
        return sp;
    }

    public static String loadFile(String filename) throws Exception {
        FileStreamSupport stream = null;
        BufferedReader reader = null;
        BufferedReader buffered_reader = null;
        StringBuilder def_file = new StringBuilder();
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            stream = new FileStreamSupport(filename, FileMode.Open, FileAccess.Read);
            reader = new BufferedReader(stream, Encoding.Default);
            //UPGRADE_TODO: The differences in the expected value  of parameters for constructor 'java.io.BufferedReader.BufferedReader'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
            buffered_reader = new BufferedReader(reader.BaseStream, reader.CurrentEncoding);
            String buf;
            while ((buf = buffered_reader.readLine()) != null)
            {
                def_file.append(buf);
                def_file.append("\r\n");
            }
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
                ;
        }
        finally
        {
            try
            {
                if (buffered_reader != null)
                    buffered_reader.close();
                 
            }
            catch (Exception e)
            {
                throw TCSException.produceTCSException(e);
                    ;
            }

            try
            {
                if (reader != null)
                    reader.close();
                 
            }
            catch (Exception e)
            {
                throw TCSException.produceTCSException(e);
                    ;
            }

            try
            {
                if (stream != null)
                    stream.close();
                 
            }
            catch (Exception e)
            {
                throw TCSException.produceTCSException(e);
                    ;
            }
        
        }
        return def_file.toString();
    }

    public static String loadXmlFile(String filename) throws Exception {
        String xml;
        try
        {
            XmlSanitizingStream sanitizingReader = new XmlSanitizingStream(filename);
            try
            {
                {
                    xml = TextReaderSupport.readToEnd(sanitizingReader);
                }
            }
            finally
            {
                if (sanitizingReader != null)
                    Disposable.mkDisposable(sanitizingReader).dispose();
                 
            }
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
                ;
        }

        return xml;
    }

    public static void writeFile(String filePath, String content) throws Exception {
        FileStreamSupport fw = null;
        try
        {
            fw = new FileStreamSupport(filePath, FileMode.Create, FileAccess.Write);
            byte[] bc = EncodingSupport.GetEncoder("ASCII").getBytes(content);
            fw.write(bc,0,bc.length);
        }
        catch (Exception exc)
        {
            throw new IOException("Cannot write to file - " + filePath + "\r\n" + exc.getMessage());
        }
        finally
        {
            fw.close();
        }
        return ;
    }

    public static String getFileContent(String fileName) throws Exception {
        StringBuilder fileContent = new StringBuilder();
        try
        {
            System.IO.BufferedStream bReader = null;
            int b;
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            bReader = new System.IO.BufferedStream(new FileStreamSupport(fileName, FileMode.Open, FileAccess.Read));
            while ((b = bReader.ReadByte()) > -1)
            fileContent.append((char)b);
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
                ;
        }

        return fileContent.toString();
    }

    public static void setConfigString(String str) throws Exception {
        m_configString = str;
        String s = MLSUtil.getPara(str, "Log", "=", ";");
        if (s != null && s.toUpperCase().equals("1".toUpperCase()))
            m_logModeOn = true;
         
        s = MLSUtil.getPara(str, "Path", "=", ";");
        if (s != null && s.length() > 0)
            m_homePath = s;
         
        s = MLSUtil.getPara(str, "PicUrl", "=", ";");
        if (s != null && s.length() > 0)
            m_picURL = s;
         
        s = MLSUtil.getPara(str, "HTTPAPI", "=", ";");
        if (s != null && s.toUpperCase().equals("1".toUpperCase()))
            m_useHTTPAPI = true;
         
        s = MLSUtil.getPara(str, "BinFolder", "=", ";");
        if (s != null && s.length() > 0)
            m_binFolder = s;
         
    }

    public static String getRecordCount(String str) throws Exception {
        String s = MLSUtil.getPara(str, "Records", "=", "/");
        s = removeChar(s,'"');
        s = removeChar(s,'/');
        return StringSupport.Trim(s);
    }

    public static boolean isLogModeOn() throws Exception {
        return m_logModeOn;
    }

    public static boolean useHTTPAPI() throws Exception {
        return m_useHTTPAPI;
    }

    public static String getHomePath() throws Exception {
        return m_homePath;
    }

    public static String getBinFolder() throws Exception {
        return m_binFolder;
    }

    public static String getPicUrl() throws Exception {
        return endBackSlash(m_picURL);
    }

    public static String getLogFilePath(String fileName) throws Exception {
        String s = "";
        s = constructPath(m_homePath,LOG_FOLDER) + StringSupport.Trim(fileName) + ".txt";
        return s;
    }

    public static String getResultFolder() throws Exception {
        return constructPath(m_homePath,RESULT_FOLDER);
    }

    public static String getMetadataFolder() throws Exception {
        return constructPath(endSlash(m_homePath) + "MLS",METADATA_FOLDER);
    }

    public static String base64Encode(String data) throws Exception {
        try
        {
            byte[] encData_byte = new byte[data.length()];
            encData_byte = EncodingSupport.GetEncoder("UTF-8").getBytes(data);
            String encodedData = Base64.encodeBase64String(encData_byte);
            return encodedData;
        }
        catch (Exception e)
        {
            throw new Exception("Error in base64Encode" + e.getMessage());
        }
    
    }

    public static String base64Decode(String data) throws Exception {
        try
        {
            System.Text.UTF8Encoding encoder = new System.Text.UTF8Encoding();
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

    public static String getUploadFolderPath() throws Exception {
        String s = "";
        s = constructPath(m_homePath,UPLOAD_FOLDER);
        return s;
    }

    public static String getSearchTempFolder(String messageHeader) throws Exception {
        return endSlash(m_homePath) + "MLS\\" + messageHeader;
    }

    public static String getCommunicationLogFolder(String messageHeader) throws Exception {
        String lastDigit = messageHeader.substring(messageHeader.length() - 3, (messageHeader.length() - 3) + (1));
        String folderPath = Tcs.Mls.Util.endSlash(Tcs.Mls.Util.getHomePath()) + "CommunicationLog\\" + DateTimeSupport.ToString(Calendar.getInstance().getTime(), "yyyyMMdd") + lastDigit + "\\";
        if (!(new File(folderPath)).exists())
            (new File(folderPath)).mkdirs();
         
        return folderPath;
    }

    /// <summary> This function creates custom path using parent and chlid path.
    /// If constructed path doesn't exist - it tryes to create it.
    /// </summary>
    /// <returns> Constructed path or null, if failed to create.
    /// <p>It is garanteed that this path always ends with '\'. So it's not necessary
    /// to check it while construction of subdirectories pathes. It's always ok to write something like:<br>
    /// String subpath = constructPath("a","b") + "mydir";
    /// </returns>
    private static String constructPath(String parent, String child) throws Exception {
        File file = new File(parent + "\\" + child);
        boolean tmpBool;
        if (File.Exists(file.FullName))
            tmpBool = true;
        else
            tmpBool = File.Exists(file.FullName); 
        if (!tmpBool)
        {
            //UPGRADE_TODO: Method 'java.io.File.mkdirs' was converted to 'System.IO.Directory.CreateDirectory' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilemkdirs'"
            File.CreateDirectory(file.FullName);
        }
        else if (!File.Exists(file.FullName))
            return null;
          
        String result = file.toString();
        //UPGRADE_TODO: Method 'java.lang.System.getProperty' was converted to 'System.IO.Path.DirectorySeparatorChar.ToString' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangSystemgetProperty_javalangString'"
        String separator = String.valueOf(File.separatorChar);
        if (result.length() > 0 && !result.endsWith(separator))
            result += separator;
         
        return result;
    }

    public static String endSlash(String s) throws Exception {
        if (!s.endsWith("\\"))
            s = s + "\\";
         
        return s;
    }

    public static String endBackSlash(String s) throws Exception {
        if (!s.endsWith("/"))
            s = s + "/";
         
        return s;
    }

    public static boolean isValidTCSDateFormat(String dateString) throws Exception {
        return Pattern.compile("^([01]\\d)/([0-3]\\d)/(\\d{4})$").matcher(dateString).matches();
    }

    public static String[] stringSplit(String input, String delimiter) throws Exception {
        return input.split(StringSupport.charAltsToRegex(delimiter.toCharArray()));
    }

    //SupportClass.Tokenizer st = new SupportClass.Tokenizer(input, delimiter);
    //System.Collections.ArrayList tokens = System.Collections.ArrayList.Synchronized(new System.Collections.ArrayList(10));
    //while (st.HasMoreTokens())
    //{
    //    tokens.Add(st.NextToken());
    //}
    //System.String[] output = new System.String[tokens.Count];
    //System.Collections.IEnumerator outputTokens = tokens.GetEnumerator();
    //int i = 0;
    ////UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
    //while (outputTokens.MoveNext())
    //{
    //    //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
    //    output[i++] = ((System.String) outputTokens.Current);
    //}
    //return output;
    public static String removeSpace(String _value) throws Exception {
        return removeChar(_value,' ');
    }

    public static String removeChar(String _value, char _char) throws Exception {
        if (_value == null || _value.length() == 0)
            return _value;
         
        StringBuilder sb = new StringBuilder(_value.length());
        char[] value_Renamed = _value.toCharArray();
        for (int i = 0;i < value_Renamed.length;i++)
            if (value_Renamed[i] != _char)
                sb.append(value_Renamed[i]);
             
        return sb.toString();
    }

    public static String encodeEntity(String string_Renamed) throws Exception {
        StringBuilder buffer = new StringBuilder();
        int count = string_Renamed.length();
        for (int i = 0;i < count;i++)
        {
            char c = string_Renamed.charAt(i);
            switch(c)
            {
                case '&': 
                    buffer.append("&amp;");
                    break;
                case '<': 
                    buffer.append("&lt;");
                    break;
                case '>': 
                    buffer.append("&gt;");
                    break;
                case '\'': 
                    buffer.append("&apos;");
                    break;
                case '"': 
                    buffer.append("&quot;");
                    break;
                default: 
                    buffer.append(c);
                    break;
            
            }
        }
        return buffer.toString();
    }

    /**
    * Encodes a string to be represented as a string literal. The format
    * is essentially a JSON string.
    * 
    * The string returned includes outer quotes
    * Example Output: "Hello \"Rick\"!\r\nRock on"
    * 
    *  @param s 
    *  @return
    */
    public static String encodeJsString(String s) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (Character c : s.toCharArray())
        {
            switch(c)
            {
                case '\"': 
                    sb.append("\\\"");
                    break;
                case '\\': 
                    sb.append("\\\\");
                    break;
                case '\b': 
                    sb.append("\\b");
                    break;
                case '\f': 
                    sb.append("\\f");
                    break;
                case '\n': 
                    sb.append("\\n");
                    break;
                case '\r': 
                    sb.append("\\r");
                    break;
                case '\t': 
                    sb.append("\\t");
                    break;
                default: 
                    Int32 i = (int)c;
                    if (i < 32 || i > 127)
                    {
                        sb.append(String.format(StringSupport.CSFmtStrToJFmtStr("\\u{0:X04}"),i));
                    }
                    else
                    {
                        sb.append(c);
                    } 
                    break;
            
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    public static String arrayToString(String[] a, String separator) throws Exception {
        StringBuilder result = new StringBuilder();
        if (a.length > 0)
        {
            result.append(a[0]);
            for (int i = 1;i < a.length;i++)
            {
                result.append(separator);
                result.append(a[i]);
            }
        }
         
        return result.toString();
    }

    public static String replaceString(String sSource, String sSearch, String sReplace) throws Exception {
        if (sSource != null && sSearch != null && sReplace != null)
        {
            int nStart = -1;
            int nPosition = 0;
            int nLength = sSearch.length();
            while ((nStart = SupportClass.getIndex(sSource,sSearch,nPosition)) != -1)
            {
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                sSource = (new StringBuilder(sSource.Substring(0, (nStart)-(0)))).append(sReplace).append(sSource.substring(nStart + nLength)).toString();
                nPosition = nStart + sReplace.length();
            }
        }
         
        return sSource;
    }

    public static String convertStringToXML(String inString) throws Exception {
        return encodeEntity(inString);
    }

    public static String convertStringToXMLNode(String inString) throws Exception {
        StringBuilder buffer = new StringBuilder();
        char[] chars = inString.toCharArray();
        for (int i = 0;i < chars.length;i++)
        {
            if (Character.isLetterOrDigit(chars[i]))
                buffer.append(chars[i]);
             
        }
        String result = buffer.toString();
        if (!StringSupport.isNullOrEmpty(result) && char.IsDigit(result.substring(0, (0) + (1)), 0))
            result = result.substring(1) + result.substring(0, (0) + (1));
         
        return result;
    }

    public static String getMessageId(String input) throws Exception {
        if (input == null)
            throw new ArgumentNullException();
         
        int index = input.indexOf("@");
        if (index > -1)
            return input.substring(0, (0) + (index));
         
        return "";
    }

    public static String getRandomDate(Date startDate, Date endDate) throws Exception {
        try
        {
            byte[] b = new byte[1];
            System.Security.Cryptography.RandomNumberGenerator.Create().GetNonZeroBytes(b);
            int days = (new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).Days;
            Random random = new Random((int)b[0]);
            days = random.Next(0, days);
            startDate = DateTimeSupport.add(startDate,Calendar.DAY_OF_YEAR,days);
            return DateTimeSupport.ToString(startDate, "MM/dd/yyyy");
        }
        catch (Exception exc)
        {
        }

        return "";
    }

    public static String getRandomDate(String startDate, int days) throws Exception {
        try
        {
            Date dlDate = Date.ParseExact(startDate, "MM/dd/yyyy", null);
            int diffDays = Math.Abs((new TimeSpan(Math.abs(dlDate.getTime() - Calendar.getInstance().getTime().getTime()))).Days);
            if (diffDays < days)
                days = diffDays;
             
            byte[] b = new byte[1];
            System.Security.Cryptography.RandomNumberGenerator.Create().GetNonZeroBytes(b);
            Random random = new Random((int)b[0]);
            days = random.Next(0, days + 1);
            dlDate = DateTimeSupport.add(dlDate,Calendar.DAY_OF_YEAR,days);
            return DateTimeSupport.ToString(dlDate, "MM/dd/yyyy");
        }
        catch (Exception exc)
        {
        }

        return "";
    }

    public static String getRandomNumber(String numberRange) throws Exception {
        try
        {
            byte[] b = new byte[1];
            System.Security.Cryptography.RandomNumberGenerator.Create().GetNonZeroBytes(b);
            String[] nums = StringSupport.Split(numberRange, '-');
            int min = Integer.valueOf(nums[0]);
            int max = Integer.valueOf(nums[1]);
            Random random = new Random((int)b[0]);
            return random.Next(min, max + 1).toString();
        }
        catch (Exception exc)
        {
        }

        return "";
    }

    public static int[] mergeArray(int[] DestinationArray, int[] SourceArray) throws Exception {
        int iSourceIndex;
        int iDestinationLength = DestinationArray.length;
        if (SourceArray.length > 0)
        {
            RefSupport<int[]> refVar___0 = new RefSupport<int[]>(DestinationArray);
            Array.Resize(refVar___0, DestinationArray.length + SourceArray.length);
            DestinationArray = refVar___0.getValue();
        }
         
        for (iSourceIndex = 0;iSourceIndex < SourceArray.length;iSourceIndex++)
        {
            DestinationArray[iDestinationLength + iSourceIndex] = SourceArray[iSourceIndex];
        }
        return DestinationArray;
    }

    public static String filterJunkValueForRoomDimensions(String val) throws Exception {
        if (val == null)
            return "";
         
        //Any combination of only 0 and decimals
        if (Pattern.compile("^0+\\.?0* *(x|X) *0+\\.?0*[^1-9]*$").matcher(val).matches())
            return "";
         
        return filterJunkValue(val);
    }

    //(Regex.IsMatch(val, @"0+ *x *0*[^1-9]"))
    public static String filterJunkValue(String val) throws Exception {
        if (val == null)
            return "";
         
        //Any combination of only 0 and decimals
        if (Pattern.compile("^(?:0+\\.?0*|0*\\.?0+)$").matcher(val).matches())
            return "";
         
        //Other junks;
        CSList<String> junks = new CSList<String>();
        if (junks.contains(val.toLowerCase()))
            return "";
        else
            return val; 
    }

}


