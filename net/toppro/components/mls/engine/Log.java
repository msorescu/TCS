//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Log   
{
    private InputStream m_stream;
    //UPGRADE_ISSUE: Class hierarchy differences between 'java.io.PrintStream' and 'System.IO.StreamWriter' may cause compilation errors. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1186'"
    private PrintWriter m_out;
    //UPGRADE_ISSUE: Class hierarchy differences between 'java.io.PrintStream' and 'System.IO.StreamWriter' may cause compilation errors. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1186'"
    private PrintWriter m_systemOut;
    //UPGRADE_ISSUE: Class hierarchy differences between 'java.io.PrintStream' and 'System.IO.StreamWriter' may cause compilation errors. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1186'"
    private PrintWriter m_systemErr;
    private boolean m_isCompactCommunicationLogOn = false;
    public boolean getIsCompactCommunicationLogOn() throws Exception {
        return m_isCompactCommunicationLogOn;
    }

    public void setIsCompactCommunicationLogOn(boolean value) throws Exception {
        m_isCompactCommunicationLogOn = value;
    }

    public Log(String filename) throws Exception {
        synchronized (this)
        {
            {
                try
                {
                    //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
                    m_out = new PrintWriter(new FileWriter(filename), true);
                    m_out.AutoFlush = true;
                }
                catch (IOException exc)
                {
                    System.out.println("Unable to open log-file: " + filename);
                    SupportClass.WriteStackTrace(exc, Console.Error);
                    m_stream = null;
                    m_out = null;
                    m_systemOut = null;
                    m_systemErr = null;
                }
            
            }
        }
    }

    public Log() throws Exception {
    }

    public void writeLine(String line) throws Exception {
        if (m_out != null)
        {
            if (!getIsCompactCommunicationLogOn())
            {
                m_out.println(line);
            }
             
        }
         
    }

    public void write(String line) throws Exception {
        if (m_out != null)
        {
            if (!getIsCompactCommunicationLogOn())
            {
                m_out.print(line);
            }
             
        }
         
    }

    public void writeLine(String line, boolean isCompactCommunicationLog) throws Exception {
        if (m_out != null)
        {
            if (getIsCompactCommunicationLogOn())
            {
                if (isCompactCommunicationLog)
                    m_out.println(line);
                 
            }
            else
            {
                m_out.println(line);
            } 
        }
         
    }

    public void writeLine() throws Exception {
    }

    public void close() throws Exception {
        synchronized (this)
        {
            {
                try
                {
                    if (m_out != null)
                    {
                        m_out.close();
                        m_out = null;
                    }
                     
                }
                catch (Exception exc)
                {
                    System.out.println("Failed to close the log-file");
                    SupportClass.WriteStackTrace(exc, Console.Error);
                }
            
            }
        }
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        close();
    }

}


