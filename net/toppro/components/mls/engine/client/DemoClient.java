//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:33 PM
//

package net.toppro.components.mls.engine.client;

import java.util.Properties;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.client.STRINGS;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;

public class DemoClient  extends net.toppro.components.mls.engine.CommunicationClient 
{
    /**
    * 
    */
    private static final String DemoFloder = "\\TOPPDEMO";
    public DemoClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
    }

    public void initBoardSetup(BoardSetup setup) throws Exception {
        setup.addGroup(createIPSetupGroup());
        setup.addGroup(createSecFieldsSetupGroup());
    }

    /**
    * Launch process of downloading of data(records) from MLS Board. Initialises MLSRecords object of
    * MLSEngine.
    */
    public void runMainScript() throws Exception {
        Properties settings;
        String filePath = "";
        try
        {
            settings = (Properties)NONE.GetSection("GeneralConfiguration");
            filePath = (String)settings["DefFilePath"] + DemoFloder;
            MLSUtil.copyFile(getEngine(),filePath + "\\@@recs@@.dat",getEngine().getResultsFolder() + "@@recs@@.dat");
            getEngine().preparePrevMlsRecords();
        }
        catch (Exception exc)
        {
            throw getEngine().createException(exc,STRINGS.get_Renamed(STRINGS.ERR_DEMO_CLIENT_UNABLE_TO_GET_RECORDS));
        }
    
    }

    //getEngine().getEnvironment().downloadDemoData(getEngine().getResultsFolder());
    /**
    * Launch process of downloading of pictures  from MLS Board. Creates picture files in client folder.
    */
    public void runPicScript(MLSRecords records) throws Exception {
    }

    //NameValueCollection settings;
    //string filePath = "";
    //try
    //{
    //    settings = (NameValueCollection)ConfigurationManager.GetSection(
    //    "GeneralConfiguration"
    //    );
    //    filePath = (string)settings["DefFilePath"] + DemoFloder;
    //    MLSUtil.CopyFolder(new DirectoryInfo(filePath), new DirectoryInfo(getEngine().getResultsFolder()));
    //    //getEngine().getEnvironment().downloadDemoData(getEngine().getResultsFolder());
    //}
    //catch (System.Exception exc)
    //{
    //    throw getEngine().createException(exc, STRINGS.get_Renamed(STRINGS.ERR_DEMO_CLIENT_UNABLE_TO_GET_RECORDS));
    //}
    /**
    * Launch process of downloading of notes  from MLS Board.
    */
    public void runRemScript(MLSRecords records) throws Exception {
    }

    /**
    * Read raw data file and cut or remove garbage from.
    *  @param datFileName path to the data-file on local machine
    * 
    *  @return  good file's content
    */
    public String prepareDataFile(String datFileName) throws Exception {
        return "";
    }

}


//System.Text.StringBuilder BeginDataDelimiter = null;
//DefParser m_DefParser = getEngine().getDefParser();
//BeginDataDelimiter = new System.Text.StringBuilder(MLSUtil.toStr(m_DefParser.getValue(MLSEngine.SECTION_RCVDATA, MLSEngine.ATTRIBUTE_RECORDMARK)));
//if (BeginDataDelimiter.Length == 0 && m_DefParser.getValue(MLSEngine.SECTION_RCVDATA, MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING).Length != 0)
//    BeginDataDelimiter = new System.Text.StringBuilder(MLSUtil.toStr(m_DefParser.getValue(MLSEngine.SECTION_RCVDATA, MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING)));
/**
* /read file to stream
*/
//System.IO.FileStream stream;
//System.Text.StringBuilder file_context = new System.Text.StringBuilder();
//try
//{
//    //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
//    stream = new System.IO.FileStream(datFileName, System.IO.FileMode.Open, System.IO.FileAccess.Read);
//}
//catch (System.IO.FileNotFoundException fnfe)
//{
//    throw getEngine().createException(fnfe, STRINGS.get_Renamed(STRINGS.ERR_DATA_FILE_NOT_FOUND));
//}
//System.IO.StreamReader reader = new System.IO.StreamReader(stream, System.Text.Encoding.Default);
////UPGRADE_TODO: The differences in the expected value  of parameters for constructor 'java.io.BufferedReader.BufferedReader'  may cause compilation errors.  "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1092'"
//System.IO.StreamReader buffered_reader = new System.IO.StreamReader(reader.BaseStream, reader.CurrentEncoding);
//sbyte b;
//try
//{
//    while ((b = (sbyte) buffered_reader.Read()) != - 1)
//        if (b != 0)
//            file_context.Append((char) b);
//    buffered_reader.Close();
//    reader.Close();
//    stream.Close();
//}
//catch (System.IO.IOException ioe)
//{
//    throw getEngine().createException(ioe);
//}
//int i;
//if ((i = file_context.ToString().IndexOf("<title>")) != - 1)
//{
//    System.String error = file_context.ToString().Substring(i + new System.Text.StringBuilder("<title>").ToString().Length, (file_context.ToString().IndexOf("</title>")) - (i + new System.Text.StringBuilder("<title>").ToString().Length));
//    System.String fileMsg = file_context.ToString();
//    fileMsg = MLSUtil.replaceSubStr(fileMsg, "<hr>", "<hr><br>");
//    if (fileMsg.Length > 1024)
//        fileMsg = fileMsg.Substring(1024);
//    throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg, MLSException.FORMAT_HTML);
//}
/**
* /find begin of data
*/
//if (BeginDataDelimiter.Length != 0)
//{
//    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
//    i = SupportClass.getIndex(file_context.ToString(), BeginDataDelimiter.ToString(), 0);
//}
//else
//    i = 0;
//if (i == - 1)
//{
//    System.String fileMsg = file_context.ToString().Trim();
//    if (fileMsg.Length != 0)
//    {
//        fileMsg = MLSUtil.replaceSubStr(fileMsg, "\r\n", "<br>");
//        if (fileMsg.Length > 1024)
//            fileMsg = fileMsg.Substring(1024);
//        throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg, MLSException.FORMAT_HTML);
//    }
//    else
//        return "";
//}
//else
//{
//    return file_context.ToString().Substring(i, (file_context.Length) - (i));
//}