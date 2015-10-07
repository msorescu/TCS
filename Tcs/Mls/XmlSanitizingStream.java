//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:34:03 PM
//

package Tcs.Mls;

import CS2JNet.System.LCC.Disposable;
import java.io.BufferedReader;
import java.io.InputStream;
import Tcs.Mls.XmlSanitizingStream;

public class XmlSanitizingStream  extends BufferedReader 
{
    // Pass 'true' to automatically detect encoding using BOMs.
    // BOMs: http://en.wikipedia.org/wiki/Byte-order_mark
    public XmlSanitizingStream(InputStream streamToSanitize) throws Exception {
        super(streamToSanitize, true);
    }

    public XmlSanitizingStream(String fileName) throws Exception {
        super(fileName);
    }

    /**
    * Whether a given character is allowed by XML 1.0.
    */
    public static boolean isLegalXmlChar(int character) throws Exception {
        return (character == 0x9 || character == 0xA || character == 0xD || (character >= 0x20 && character <= 0xD7FF) || (character >= 0xE000 && character <= 0xFFFD) || (character >= 0x10000 && character <= 0x10FFFF));
    }

    /* == '\t' == 9   */
    /* == '\n' == 10  */
    /* == '\r' == 13  */
    private static final int EOF = -1;
    public int read() throws Exception {
        // Read each char, skipping ones XML has prohibited
        int nextCharacter;
        do
        {
            // Read a character
            if ((nextCharacter = super.Read()) == EOF)
            {
                break;
            }
             
        }
        while (!XmlSanitizingStream.isLegalXmlChar(nextCharacter));
        return nextCharacter;
    }

    // If the char denotes end of file, stop
    // Skip char if it's illegal, and try the next
    public int peek() throws Exception {
        // Return next legal XML char w/o reading it
        int nextCharacter;
        do
        {
            // See what the next character is
            nextCharacter = super.Peek();
        }
        while (!XmlSanitizingStream.isLegalXmlChar(nextCharacter) && (nextCharacter = super.Read()) != EOF);
        return nextCharacter;
    }

}


// If it's illegal, skip over
// and try the next.