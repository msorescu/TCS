//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:50 PM
//


import CS2JNet.JavaSupport.Unsupported;
import CS2JNet.System.ArgumentException;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;

/**
* Contains conversion support elements such as classes, interfaces and static methods.
*/
public class SupportClass   
{
    /**
    * Performs an unsigned bitwise right shift with the specified number
    * 
    *  @param number Number to operate on
    *  @param bits Ammount of bits to shift
    *  @return The resulting number from the shift operation
    */
    public static int uRShift(int number, int bits) throws Exception {
        if (number >= 0)
            return number >> bits;
        else
            return (number >> bits) + (2 << ~bits); 
    }

    /**
    * Performs an unsigned bitwise right shift with the specified number
    * 
    *  @param number Number to operate on
    *  @param bits Ammount of bits to shift
    *  @return The resulting number from the shift operation
    */
    public static int uRShift(int number, long bits) throws Exception {
        return uRShift(number,(int)bits);
    }

    /**
    * Performs an unsigned bitwise right shift with the specified number
    * 
    *  @param number Number to operate on
    *  @param bits Ammount of bits to shift
    *  @return The resulting number from the shift operation
    */
    public static long uRShift(long number, int bits) throws Exception {
        if (number >= 0)
            return number >> bits;
        else
            return (number >> bits) + (2L << ~bits); 
    }

    /**
    * Performs an unsigned bitwise right shift with the specified number
    * 
    *  @param number Number to operate on
    *  @param bits Ammount of bits to shift
    *  @return The resulting number from the shift operation
    */
    public static long uRShift(long number, long bits) throws Exception {
        return uRShift(number,(int)bits);
    }

    /*******************************/
    /**
    * This method returns the literal value received
    * 
    *  @param literal The literal to return
    *  @return The received value
    */
    public static long identity(long literal) throws Exception {
        return literal;
    }

    /**
    * This method returns the literal value received
    * 
    *  @param literal The literal to return
    *  @return The received value
    */
    //public static ulong Identity(ulong literal)
    //{
    //    return literal;
    //}
    /**
    * // 
    * // This method returns the literal value received
    * // 
    * // 
    *  @param literal The literal to return
    * // 
    *  @return The received value
    */
    //public static float Identity(float literal)
    //{
    //    return literal;
    //}
    /**
    * This method returns the literal value received
    * 
    *  @param literal The literal to return
    *  @return The received value
    */
    public static double identity(double literal) throws Exception {
        return literal;
    }

    /**
    * Wrapper of indexof function. This method does not throw exception.
    * 
    *  @param str String
    *  @param value search value
    *  @return The received value
    */
    public static int getIndex(String str, String value) throws Exception {
        try
        {
            return str.indexOf(value);
        }
        catch (Exception exc)
        {
            return -1;
        }
    
    }

    public static int getIndex(String str, char value) throws Exception {
        try
        {
            return str.indexOf(value);
        }
        catch (Exception exc)
        {
            return -1;
        }
    
    }

    /**
    * Wrapper of indexof function. This method does not throw exception.
    * 
    *  @param str String
    *  @param value search value
    *  @param startIndex Start index
    *  @return The received value
    */
    public static int getIndex(String str, String value, int startIndex) throws Exception {
        try
        {
            if (startIndex > str.length() - 1)
                return -1;
             
            return str.indexOf(value, startIndex);
        }
        catch (Exception exc)
        {
            return -1;
        }
    
    }

    public static int getIndex(String str, char value, int startIndex) throws Exception {
        try
        {
            if (startIndex > str.length() - 1)
                return -1;
             
            return str.indexOf(value, startIndex);
        }
        catch (Exception exc)
        {
            return -1;
        }
    
    }

    /*******************************/
    /**
    * Reads a number of characters from the current source Stream and writes the data to the target array at the specified index.
    *  @param sourceStream The source Stream to read from.
    *  @param target Contains the array of characteres read from the source Stream.
    *  @param start The starting index of the target array.
    *  @param count The maximum number of characters to read from the source Stream.
    *  @return The number of characters read. The number will be less than or equal to count depending on the data available in the source Stream. Returns -1 if the end of the stream is reached.
    */
    public static int readInput(InputStream sourceStream, byte[] target, int start, int count) throws Exception {
        // Returns 0 bytes if not enough space in target
        if (target.length == 0)
            return 0;
         
        byte[] receiver = new byte[target.length];
        int bytesRead = sourceStream.read(receiver,start,count);
        // Returns -1 if EOF
        if (bytesRead == 0)
            return -1;
         
        for (int i = start;i < start + bytesRead;i++)
            target[i] = receiver[i];
        return bytesRead;
    }

    /**
    * Reads a number of characters from the current source TextReader and writes the data to the target array at the specified index.
    *  @param sourceTextReader The source TextReader to read from
    *  @param target Contains the array of characteres read from the source TextReader.
    *  @param start The starting index of the target array.
    *  @param count The maximum number of characters to read from the source TextReader.
    *  @return The number of characters read. The number will be less than or equal to count depending on the data available in the source TextReader. Returns -1 if the end of the stream is reached.
    */
    //public static System.Int32 ReadInput(System.IO.TextReader sourceTextReader, sbyte[] target, int start, int count)
    //{
    //    // Returns 0 bytes if not enough space in target
    //    if (target.Length == 0) return 0;
    //    char[] charArray = new char[target.Length];
    //    int bytesRead = sourceTextReader.Read(charArray, start, count);
    //    // Returns -1 if EOF
    //    if (bytesRead == 0) return -1;
    //    for(int index=start; index<start+bytesRead; index++)
    //        target[index] = (sbyte)charArray[index];
    //    return bytesRead;
    //}
    /*******************************/
    /**
    * Converts an array of sbytes to an array of bytes
    * 
    *  @param sbyteArray The array of sbytes to be converted
    *  @return The new array of bytes
    */
    //public static byte[] ToByteArray(sbyte[] sbyteArray)
    //{
    //    byte[] byteArray = null;
    //    if (sbyteArray != null)
    //    {
    //        byteArray = new byte[sbyteArray.Length];
    //        for(int index=0; index < sbyteArray.Length; index++)
    //            byteArray[index] = (byte) sbyteArray[index];
    //    }
    //    return byteArray;
    //}
    /**
    * Converts a string to an array of bytes
    * 
    *  @param sourceString The string to be converted
    *  @return The new array of bytes
    */
    public static byte[] toByteArray(String sourceString) throws Exception {
        return System.Text.UTF8Encoding.UTF8.GetBytes(sourceString);
    }

    /**
    * Converts a array of object-type instances to a byte-type array.
    * 
    *  @param tempObjectArray Array to convert.
    *  @return An array of byte type elements.
    */
    public static byte[] toByteArray(Object[] tempObjectArray) throws Exception {
        byte[] byteArray = null;
        if (tempObjectArray != null)
        {
            byteArray = new byte[tempObjectArray.length];
            for (int index = 0;index < tempObjectArray.length;index++)
                byteArray[index] = (Byte)tempObjectArray[index];
        }
         
        return byteArray;
    }

    /*******************************/
    /**
    * This class manages different features for calendars.
    * The different calendars are internally managed using a hashtable structure.
    */
    /*******************************/
    /**
    * Provides support functions to create read-write random acces files and write functions
    */
    public static class RandomAccessFileSupport   
    {
        /**
        * Creates a new random acces stream with read-write or read rights
        * 
        *  @param fileName A relative or absolute path for the file to open
        *  @param mode Mode to open the file in
        *  @return The new System.IO.FileStream
        */
        public static FileStreamSupport createRandomAccessFile(String fileName, String mode) throws Exception {
            FileStreamSupport newFile = null;
            if (mode.compareTo("rw") == 0)
                newFile = new FileStreamSupport(fileName, FileMode.OpenOrCreate, FileAccess.ReadWrite);
            else if (mode.compareTo("r") == 0)
                newFile = new FileStreamSupport(fileName, FileMode.Open, FileAccess.Read);
            else
                throw new ArgumentException();  
            return newFile;
        }

        /**
        * Creates a new random acces stream with read-write or read rights
        * 
        *  @param fileName File infomation for the file to open
        *  @param mode Mode to open the file in
        *  @return The new System.IO.FileStream
        */
        public static FileStreamSupport createRandomAccessFile(File fileName, String mode) throws Exception {
            return CreateRandomAccessFile(fileName.FullName, mode);
        }

        /**
        * Writes the data to the specified file stream
        * 
        *  @param data Data to write
        *  @param fileStream File to write to
        */
        public static void writeBytes(String data, FileStreamSupport fileStream) throws Exception {
            int index = 0;
            int length = data.length();
            while (index < length)
            fileStream.WriteByte((byte)data.charAt(index++));
        }

        /**
        * Writes the received string to the file stream
        * 
        *  @param data String of information to write
        *  @param fileStream File to write to
        */
        public static void writeChars(String data, FileStreamSupport fileStream) throws Exception {
            writeBytes(data,fileStream);
        }
    
    }

    /**
    * Writes the received data to the file stream
    * 
    *  @param sByteArray Data to write
    *  @param fileStream File to write to
    */
    //public static void WriteRandomFile(sbyte[] sByteArray,System.IO.FileStream fileStream)
    //{
    //    byte[] byteArray = ToByteArray(sByteArray);
    //    fileStream.Write(byteArray, 0, byteArray.Length);
    //}
    /*******************************/
    /**
    * Try to skip bytes in the input stream and return the actual number of bytes skipped.
    * 
    *  @param stream Input stream that will be used to skip the bytes
    *  @param skipbytes Number of bytes to be skipped
    *  @return Actual number of bytes skipped
    */
    public static int skip(InputStream stream, int skipBytes) throws Exception {
        long oldPosition = Unsupported.throwUnsupported("stream.Position");
        long result = stream.Seek(skipBytes, System.IO.SeekOrigin.Current) - oldPosition;
        return (int)result;
    }

    /**
    * Skips a given number of characters into a given Stream.
    * 
    *  @param  stream The stream in which the skips are done.
    *  @param number The number of caracters to skip.
    *  @return The number of characters skipped.
    */
    public static long skip(BufferedReader stream, long number) throws Exception {
        long skippedBytes = 0;
        for (long index = 0;index < number;index++)
        {
            stream.Read();
            skippedBytes++;
        }
        return skippedBytes;
    }

    /**
    * Skips a given number of characters into a given StringReader.
    * 
    *  @param strReader The StringReader in which the skips are done.
    *  @param number The number of caracters to skip.
    *  @return The number of characters skipped.
    */
    public static long skip(StringReader strReader, long number) throws Exception {
        long skippedBytes = 0;
        for (long index = 0;index < number;index++)
        {
            strReader.Read();
            skippedBytes++;
        }
        return skippedBytes;
    }

    /*******************************/
    /**
    * Receives a byte array and returns it transformed in an sbyte array
    * 
    *  @param byteArray Byte array to process
    *  @return The transformed array
    */
    //public static sbyte[] ToSByteArray(byte[] byteArray)
    //{
    //    sbyte[] sbyteArray = null;
    //    if (byteArray != null)
    //    {
    //        sbyteArray = new sbyte[byteArray.Length];
    //        for(int index=0; index < byteArray.Length; index++)
    //            sbyteArray[index] = (sbyte) byteArray[index];
    //    }
    //    return sbyteArray;
    //}
    /*******************************/
    /**
    * Writes an object to the specified Stream
    * 
    *  @param stream The target Stream
    *  @param objectToSend The object to be sent
    */
    public static void serialize(InputStream stream, Object objectToSend) throws Exception {
        System.Runtime.Serialization.Formatters.Binary.BinaryFormatter formatter = new System.Runtime.Serialization.Formatters.Binary.BinaryFormatter();
        formatter.Serialize(stream, objectToSend);
    }

    /**
    * Writes an object to the specified BinaryWriter
    * 
    *  @param stream The target BinaryWriter
    *  @param objectToSend The object to be sent
    */
    public static void serialize(System.IO.BinaryWriter binaryWriter, Object objectToSend) throws Exception {
        System.Runtime.Serialization.Formatters.Binary.BinaryFormatter formatter = new System.Runtime.Serialization.Formatters.Binary.BinaryFormatter();
        formatter.Serialize(binaryWriter.BaseStream, objectToSend);
    }

    /*******************************/
    /**
    * Deserializes an object, or an entire graph of connected objects, and returns the object intance
    * 
    *  @param binaryReader Reader instance used to read the object
    *  @return The object instance
    */
    public static Object deserialize(System.IO.BinaryReader binaryReader) throws Exception {
        System.Runtime.Serialization.Formatters.Binary.BinaryFormatter formatter = new System.Runtime.Serialization.Formatters.Binary.BinaryFormatter();
        return formatter.Deserialize(binaryReader.BaseStream);
    }

    /*******************************/
    /**
    * Writes the exception stack trace to the received stream
    * 
    *  @param throwable Exception to obtain information from
    *  @param stream Output sream used to write to
    */
    public static void writeStackTrace(Exception throwable, PrintStream stream) throws Exception {
        stream.print(throwable.getStackTrace().toString());
        stream.Flush();
    }

    /*******************************/
    /**
    * Converts an array of sbytes to an array of chars
    * 
    *  @param sByteArray The array of sbytes to convert
    *  @return The new array of chars
    */
    //public static char[] ToCharArray(sbyte[] sByteArray)
    //{
    //    return System.Text.UTF8Encoding.UTF8.GetChars(ToByteArray(sByteArray));
    //}
    /**
    * Converts an array of bytes to an array of chars
    * 
    *  @param byteArray The array of bytes to convert
    *  @return The new array of chars
    */
    public static char[] toCharArray(byte[] byteArray) throws Exception {
        return System.Text.UTF8Encoding.UTF8.GetChars(byteArray);
    }

    /*******************************/
    /**
    * The class performs token processing in strings
    */
    public static class Tokenizer   implements IEnumerator
    {
        /**
        * Position over the string
        */
        private long currentPos = 0;
        /**
        * Include demiliters in the results.
        */
        private boolean includeDelims = false;
        private boolean isReachTheEnd = false;
        /**
        * Char representation of the String to tokenize.
        */
        private char[] chars = null;
        //The tokenizer uses the default delimiter set: the space character, the tab character, the newline character, and the carriage-return character and the form-feed character
        private String delimiters = " \t\n\r\f";
        /**
        * Initializes a new class instance with a specified string to process
        * 
        *  @param source String to tokenize
        */
        public Tokenizer(String source) throws Exception {
            this.chars = source.toCharArray();
        }

        /**
        * Initializes a new class instance with a specified string to process
        * and the specified token delimiters to use
        * 
        *  @param source String to tokenize
        *  @param delimiters String containing the delimiters
        */
        public Tokenizer(String source, String delimiters) throws Exception {
            this(source);
            this.delimiters = delimiters;
        }

        /**
        * Initializes a new class instance with a specified string to process, the specified token
        * delimiters to use, and whether the delimiters must be included in the results.
        * 
        *  @param source String to tokenize
        *  @param delimiters String containing the delimiters
        *  @param includeDelims Determines if delimiters are included in the results.
        */
        public Tokenizer(String source, String delimiters, boolean includeDelims) throws Exception {
            this(source, delimiters);
            this.includeDelims = includeDelims;
        }

        /**
        * Returns the next token from the token list
        * 
        *  @return The string value of the token
        */
        public String nextToken() throws Exception {
            return nextToken(this.delimiters);
        }

        /**
        * Returns the next token from the source string, using the provided
        * token delimiters
        * 
        *  @param delimiters String containing the delimiters to use
        *  @return The string value of the token
        */
        public String nextToken(String delimiters) throws Exception {
            //According to documentation, the usage of the received delimiters should be temporary (only for this call).
            //However, it seems it is not true, so the following line is necessary.
            this.delimiters = delimiters;
            //at the end
            if (this.currentPos == this.chars.length)
            {
                //throw new System.ArgumentOutOfRangeException();
                isReachTheEnd = true;
                return "";
            }
            else //if over a delimiter and delimiters must be returned
            if ((System.Array.IndexOf(delimiters.toCharArray(), chars[this.currentPos]) != -1) && this.includeDelims)
                return "" + this.chars[this.currentPos++];
            else
                return nextToken(delimiters.toCharArray());  
        }

        //need to get the token wo delimiters.
        //Returns the nextToken wo delimiters
        private String nextToken(char[] delimiters) throws Exception {
            String token = "";
            long pos = this.currentPos;
            while (System.Array.IndexOf(delimiters, this.chars[currentPos]) != -1)
            //skip possible delimiters
            //The last one is a delimiter (i.e there is no more tokens)
            if (++this.currentPos == this.chars.length)
            {
                this.currentPos = pos;
                isReachTheEnd = true;
                break;
            }
             
            //throw new System.ArgumentOutOfRangeException();
            if (isReachTheEnd == true)
                return "";
             
            while (System.Array.IndexOf(delimiters, this.chars[this.currentPos]) == -1)
            {
                //getting the token
                token += this.chars[this.currentPos];
                //the last one is not a delimiter
                if (++this.currentPos == this.chars.length)
                    break;
                 
            }
            return token;
        }

        /**
        * Determines if there are more tokens to return from the source string
        * 
        *  @return True or false, depending if there are more tokens
        */
        public boolean hasMoreTokens() throws Exception {
            //keeping the current pos
            long pos = this.currentPos;
            try
            {
                this.nextToken();
                if (isReachTheEnd)
                    return false;
                 
            }
            catch (System.ArgumentOutOfRangeException __dummyCatchVar0)
            {
                return false;
            }
            finally
            {
                this.currentPos = pos;
            }
            return true;
        }

        /**
        * Remaining tokens count
        */
        public int getCount() throws Exception {
            //keeping the current pos
            long pos = this.currentPos;
            int i = 0;
            try
            {
                while (true)
                {
                    this.nextToken();
                    if (isReachTheEnd)
                        break;
                     
                    i++;
                }
            }
            catch (System.ArgumentOutOfRangeException __dummyCatchVar1)
            {
                this.currentPos = pos;
                return i;
            }

            this.currentPos = pos;
            isReachTheEnd = false;
            return i;
        }

        /**
        * Performs the same action as NextToken.
        */
        public Object getCurrent() throws Exception {
            return (Object)this.nextToken();
        }

        /// <summary>
        //  Performs the same action as HasMoreTokens.
        /// </summary>
        /// <returns>True or false, depending if there are more tokens</returns>
        public boolean moveNext() throws Exception {
            return this.hasMoreTokens();
        }

        /**
        * Does nothing.
        */
        public void reset() throws Exception {
                ;
        }
    
    }

    /*******************************/
    /**
    * Write an array of bytes int the FileStream specified.
    * 
    *  @param FileStreamWrite FileStream that must be updated.
    *  @param Source Array of bytes that must be written in the FileStream.
    */
    public static void writeOutput(FileStreamSupport FileStreamWrite, byte[] Source) throws Exception {
        FileStreamWrite.write(Source,0,Source.length);
    }

    /*******************************/
    /**
    * Provides support for DateFormat
    */
    /*******************************/
    /**
    * Gets the DateTimeFormat instance and date instance to obtain the date with the format passed
    * 
    *  @param format The DateTimeFormat to obtain the time and date pattern
    *  @param date The date instance used to get the date
    *  @return A string representing the date with the time and date patterns
    */
    /*******************************/
    /**
    * Reverses string values.
    * 
    *  @param text The StringBuilder object containing the string to be reversed.
    *  @return The reversed string contained in a StringBuilder object.
    */
    public static StringBuilder reverseString(StringBuilder text) throws Exception {
        char[] tmpChar = text.toString().toCharArray();
        System.Array.Reverse(tmpChar);
        return new StringBuilder(new String(tmpChar));
    }

    /*******************************/
    /**
    * Checks if the giving File instance is a directory or file, and returns his Length
    * 
    *  @param file The File instance to check
    *  @return The length of the file
    */
    public static long fileLength(File file) throws Exception {
        if (file.Exists)
            return file.Length;
        else
            return 0; 
    }

    /*******************************/
    /**
    * SupportClass for the Stack class.
    */
    public static class StackSupport   
    {
        /**
        * Removes the element at the top of the stack and returns it.
        * 
        *  @param stack The stack where the element at the top will be returned and removed.
        *  @return The element at the top of the stack.
        */
        public static Object pop(ArrayList stack) throws Exception {
            Object obj = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);
            return obj;
        }
    
    }

}


/*******************************/
/**
* Gets the DateTimeFormat instance using the culture passed as parameter and sets the pattern to the time or date depending of the value
* 
*  @param dateStyle The desired date style.
*  @param timeStyle The desired time style
*  @param culture The CultureInfo instance used to obtain the DateTimeFormat
*  @return The DateTimeFomatInfo of the culture and with the desired date or time style
*/
/*******************************/
/*******************************/
/**
* Gives support functions to Http internet connections.
*/