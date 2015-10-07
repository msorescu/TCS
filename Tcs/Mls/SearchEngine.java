//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:34:04 PM
//

package Tcs.Mls;

import CS2JNet.System.EnumSupport;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.StringSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.SearchEngine;
import Tcs.Mls.TCServer;

//using System.Runtime.InteropServices;
/**
* This class is designed to be packaged with a COM DLL output format.
* The class has no standard entry points, other than the constructor.
* Public methods will be exposed as methods on the default COM interface.
*/
public class SearchEngine   
{
    private static String MLS_METADATA_ENGINE_VERSION = "10";
    private static int BOARD_ID = 4302;
    //4907;//5644;// 5524;//5482;//5870;//5482;//5870;
    private static boolean isGetMetadata = false;
    private boolean _isDebug = false;
    private int _boardID = 5482;
    private static final String RequestProgressInputXml = "<Input RequestId=\"{0}\" StatusId=\"{1}\"/>";
    public enum RequestLogProgressStatus
    {
        __dummyEnum__0,
        __dummyEnum__1,
        Started,
        Finished
    }
    public boolean getIsDebug() throws Exception {
        return _isDebug;
    }

    public void setIsDebug(boolean value) throws Exception {
        _isDebug = value;
    }

    public int getBoardID() throws Exception {
        return _boardID;
    }

    public void setBoardID(int value) throws Exception {
        _boardID = value;
    }

    public String runRequest(String messageHeader) throws Exception {
        return runRequest(messageHeader,false,"",0);
    }

    public String runClientRequest(String message) throws Exception {
        Properties settings;
        TCServer tcServer = null;
        String messageId = "200";
        //try {
        //    requestLog.AddRequestProgress(string.Format(CultureInfo.CurrentCulture, RequestProgressInputXml, messageId, RequestLogProgressStatus.Started.ToString("D")));
        //}
        //catch(Exception exce)
        //{
        //}
        settings = (Properties)NONE.GetSection("GeneralConfiguration");
        Log m_log = null;
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        try
        {
            m_log = new Log(Path.GetDirectoryName(MLSUtil.getPara(message, MLSConnector.ATTRIBUTE_DEF_PATH, "=", "/")) + "\\communication.log");
            m_log.start();
            if (m_log == null)
                m_log = new Log();
             
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Starts                                        |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //SimpleDateFormat TIME = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
            //m_log.WriteLine("Current Time = " + SupportClass.FormatDateTime(System.Globalization.DateTimeFormatInfo.GetInstance(, System.DateTime.Now));
            m_log.WriteLine(message);
            MLSConnector.RUN_LOCAL = getBoardID();
            tcServer = new TCServer("200@1", m_log);
            tcServer.setIsRealTime(true);
            tcServer.setRequestXml(message);
            tcServer.runServer();
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            m_log.WriteLine(exc.getMessage());
        }

        m_log.WriteLine("Total Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        m_log.WriteLine();
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine("|                        TCSExecutive Ends                                          |");
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine();
        if (m_log != null)
            m_log.stop();
         
        return "Done";
    }

    public String runRequest(String messageHeader, boolean isRealTime, String message, long requestId) throws Exception {
        Properties settings;
        TCServer tcServer = null;
        RequestLog requestLog = new RequestLog();
        String messageId = "";
        if (isRealTime)
        {
            if (requestId == 0)
                messageId = Tcs.Mls.Util.getNewGuid();
            else
                messageId = String.valueOf(requestId); 
            messageHeader = messageId;
        }
        else
            messageId = Tcs.Mls.Util.getMessageId(messageHeader); 
        //try {
        //    requestLog.AddRequestProgress(string.Format(CultureInfo.CurrentCulture, RequestProgressInputXml, messageId, RequestLogProgressStatus.Started.ToString("D")));
        //}
        //catch(Exception exce)
        //{
        //}
        settings = (Properties)NONE.GetSection("GeneralConfiguration");
        Tcs.Mls.Util.setHomePath((String)settings["Path"]);
        Tcs.Mls.Util.setPictureURL((String)settings["PicURL"]);
        Tcs.Mls.Util.setLogOn(((String)settings["Log"]).equals("1") ? true : false);
        if (getIsDebug())
            Tcs.Mls.Util.setLogOn(true);
         
        Log m_log = null;
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        try
        {
            //if (args.Length > 1)
            //{
            //Util.setConfigString(args[1]);
            //Util.setConfigString(contructionString);
            if (Tcs.Mls.Util.isLogModeOn())
            {
                //m_log = new Log(Util.getLogFilePath(args[0]));
                m_log = new Log(Tcs.Mls.Util.getLogFilePath(messageHeader));
                m_log.start();
            }
            else
            {
                String compactCommunicationLogOn = (String)settings["CommunicationLogOn"];
                boolean isCompactCommunicationLogOn = false;
                if (!StringSupport.isNullOrEmpty(compactCommunicationLogOn))
                {
                    if (compactCommunicationLogOn.equals("1"))
                        isCompactCommunicationLogOn = true;
                     
                }
                 
                //isCompactCommunicationLogOn = true; //Todo: Get this flag from configuration file or db
                if (isCompactCommunicationLogOn)
                {
                    m_log = new Log(Tcs.Mls.Util.getCommunicationLogFolder(messageHeader) + messageHeader + ".log");
                    m_log.IsCompactCommunicationLogOn = true;
                    m_log.start();
                }
                 
            } 
            if (m_log == null)
                m_log = new Log();
             
            try
            {
                if (!isRealTime)
                    requestLog.AddRequestProgress(String.Format(Locale.CurrentCulture, RequestProgressInputXml, messageId, EnumSupport.toString(RequestLogProgressStatus.Started, "D")));
                 
            }
            catch (Exception exce)
            {
                m_log.WriteLine(String.format(StringSupport.CSFmtStrToJFmtStr("Error on start progress log. MessageId: {0} " + exce.getMessage() + exce.getStackTrace().toString()),messageId));
            }

            //}
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Starts   ( C# version )   1.1.1.21             |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //SimpleDateFormat TIME = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
            //m_log.WriteLine("Current Time = " + SupportClass.FormatDateTime(System.Globalization.DateTimeFormatInfo.GetInstance(, System.DateTime.Now));
            m_log.WriteLine("args[0]" + messageHeader);
            //args[0]);
            // m_log.WriteLine("args[1]" + contructionString);//args[1]);
            //TCServer tcServer = new TCServer(args[0]);
            if (getIsDebug())
                MLSConnector.RUN_LOCAL = getBoardID();
             
            tcServer = new TCServer(messageHeader, m_log);
            tcServer.setIsRealTime(isRealTime);
            tcServer.setRequestXml(message);
            tcServer.setRequestId(messageId);
            tcServer.runServer();
            if (tcServer.getErrorCode() > 0 && !isRealTime)
                requestLog.AddErrorLog(messageId, tcServer.getErrorCode(), tcServer.getErrorMessage());
             
            m_log.WriteLine("Total Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Ends                                          |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            if (m_log != null)
            {
                m_log.stop();
                m_log = null;
            }
             
            if (isRealTime)
                return tcServer.getResult();
             
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            m_log.WriteLine(exc.getMessage());
        }
        finally
        {
            try
            {
                if (tcServer.getRefreshMetadata())
                    getMetadata(tcServer.getMetadataRequest());
                 
            }
            catch (Exception __dummyCatchVar0)
            {
            }

            tcServer = null;
        }
        try
        {
            if (!isRealTime)
                requestLog.AddRequestProgress(String.Format(Locale.CurrentCulture, RequestProgressInputXml, messageId, EnumSupport.toString(RequestLogProgressStatus.Finished, "D")));
             
        }
        catch (Exception exce)
        {
        }

        if (isRealTime)
            return tcServer.getResult();
         
        return "Done";
    }

    public String getMetadataEngineVersion() throws Exception {
        return MLS_METADATA_ENGINE_VERSION;
    }

    public String getTCSSystemVersion() throws Exception {
        return net.toppro.components.mls.engine.MLSEngine.MLS_ENGINE_VERSION;
    }

    public String getMetadata(String xmlString) throws Exception {
        Log m_log = null;
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        try
        {
            //if (args.Length > 1)
            //{
            //Util.setConfigString(args[1]);
            //Util.setConfigString(contructionString);
            //Util.LogOn = true;
            if (Tcs.Mls.Util.isLogModeOn())
            {
                //m_log = new Log(Util.getLogFilePath(args[0]));
                m_log = new Log("c:\\temp\\" + String.valueOf((new Random()).nextInt(5000)) + ".log");
                m_log.start();
            }
             
            if (m_log == null)
                m_log = new Log();
             
            //}
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Starts   ( C# version )   1.1.1.21             |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //SimpleDateFormat TIME = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
            //m_log.WriteLine("Current Time = " + SupportClass.FormatDateTime(System.Globalization.DateTimeFormatInfo.GetInstance(, System.DateTime.Now));
            m_log.WriteLine("args[0]" + xmlString);
            //args[0]);
            // m_log.WriteLine("args[1]" + contructionString);//args[1]);
            //TCServer tcServer = new TCServer(args[0]);
            if (getIsDebug())
                MLSConnector.RUN_LOCAL = getBoardID();
             
            TCServer tcServer = new TCServer(xmlString, m_log);
            tcServer.runServer();
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            m_log.WriteLine(exc.getMessage());
        }

        m_log.WriteLine("Total Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        m_log.WriteLine();
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine("|                        TCSExecutive Ends                                          |");
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine();
        if (m_log != null)
            m_log.stop();
         
        return "Done";
    }

    public String loadAutoCategoryData(String moduleID) throws Exception {
        Log m_log = null;
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        MLSBoard mlsBoard = new MLSBoard();
        String boardID = mlsBoard.GetBoardID(moduleID);
        if (getIsDebug())
            MLSConnector.RUN_LOCAL = Integer.valueOf(boardID);
         
        DBAccess dba = new DBAccess();
        String dataSourceID = "";
        IDataReader dr = dba.GetDataReader("select data_source_id from dbo.tcs_module_id_to_data_source_id where module_id=" + moduleID);
        try
        {
            {
                if (dr.Read())
                    dataSourceID = dr["data_source_id"].toString();
                 
            }
        }
        finally
        {
            if (dr != null)
                Disposable.mkDisposable(dr).dispose();
             
        }
        CategorizationDal cd = new CategorizationDal("DACount");
        String userName = "";
        String password = "";
        String userAgentPassword = "";
        String userAgent = "";
        try
        {
            IDataReader dr = cd.SpDARETSConnectionInfoSel(dataSourceID);
            try
            {
                {
                    if (dr.Read())
                    {
                        userName = dr["RETSUserName"].toString();
                        password = dr["RETSPassword"].toString();
                        userAgent = dr["RETSUserAgent"].toString();
                        userAgentPassword = dr["RETSUserAgentPassword"].toString();
                    }
                     
                }
            }
            finally
            {
                if (dr != null)
                    Disposable.mkDisposable(dr).dispose();
                 
            }
        }
        catch (Exception excep)
        {
            userName = "";
        }

        String request = "<TCService><Function>LoadAutoCategoryData</Function><Login><password>{0}</password><password>{1}</password><UserAgent>{2}</UserAgent><RetsUAPwd>{3}</RetsUAPwd></Login><Board BoardId=\"" + boardID + "\"/><Search Type=\"METADATA-Board\" BypassARAuthentication=\"1\" Format=\"XML\" Recursive=\"1\"/></TCService>";
        request = String.Format(request, userName, password, userAgent, userAgentPassword);
        try
        {
            //if (args.Length > 1)
            //{
            //Util.setConfigString(args[1]);
            //Util.setConfigString(contructionString);
            //Util.LogOn = true;
            Properties settings;
            settings = (Properties)NONE.GetSection("GeneralConfiguration");
            Tcs.Mls.Util.setHomePath((String)settings["Path"]);
            if (Tcs.Mls.Util.isLogModeOn())
            {
                //m_log = new Log(Util.getLogFilePath(args[0]));
                m_log = new Log("c:\\temp\\" + String.valueOf((new Random()).nextInt(5000)) + ".log");
                m_log.start();
            }
             
            if (m_log == null)
                m_log = new Log();
             
            //}
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Starts   ( C# version )   1.1.1.21             |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //SimpleDateFormat TIME = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
            //m_log.WriteLine("Current Time = " + SupportClass.FormatDateTime(System.Globalization.DateTimeFormatInfo.GetInstance(, System.DateTime.Now));
            m_log.WriteLine("args[0]" + moduleID);
            //args[0]);
            // m_log.WriteLine("args[1]" + contructionString);//args[1]);
            //TCServer tcServer = new TCServer(args[0]);
            //if (IsDebug)
            //    MLSConnector.RUN_LOCAL = BoardID;
            TCServer tcServer = new TCServer(request, m_log);
            tcServer.setModuleID(moduleID);
            tcServer.setMessageHeaderGuid(Tcs.Mls.Util.getNewGuid());
            //tcServer.IsRealTime = true;
            tcServer.runServer();
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            m_log.WriteLine(exc.getMessage());
        }

        m_log.WriteLine("Total Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        m_log.WriteLine();
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine("|                        TCSExecutive Ends                                          |");
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine();
        if (m_log != null)
            m_log.stop();
         
        return "Done";
    }

    public String getMlsFieldsCriteria(String xmlString) throws Exception {
        Log m_log = null;
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        String result = "";
        try
        {
            if (Tcs.Mls.Util.isLogModeOn() || getIsDebug())
            {
                m_log = new Log("c:\\temp\\" + String.valueOf((new Random()).nextInt(5000)) + ".log");
                m_log.start();
            }
             
            if (m_log == null)
                m_log = new Log();
             
            //}
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Starts   ( C# version )   1.1.1.21             |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            m_log.WriteLine("args[0]" + xmlString);
            //args[0]);
            if (getIsDebug())
                MLSConnector.RUN_LOCAL = getBoardID();
             
            TCServer tcServer = new TCServer(xmlString, m_log);
            tcServer.runServer();
            result = tcServer.getResult();
            m_log.WriteLine(result);
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            m_log.WriteLine(exc.getMessage());
        }

        m_log.WriteLine("Total Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        m_log.WriteLine();
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine("|                        TCSExecutive Ends                                          |");
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine();
        if (m_log != null)
            m_log.stop();
         
        return result;
    }

    public static void Main(String[] args) throws Exception {
        Properties settings;
        settings = (Properties)NONE.GetSection("GeneralConfiguration");
        Tcs.Mls.Util.setHomePath((String)settings["Path"]);
        Tcs.Mls.Util.setPictureURL((String)settings["PicURL"]);
        Tcs.Mls.Util.setLogOn(((String)settings["Log"]).equals("1") ? true : false);
        Log m_log = null;
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        try
        {
            if (args.length > 1)
            {
                Tcs.Mls.Util.setConfigString(args[1]);
                if (Tcs.Mls.Util.isLogModeOn())
                {
                    m_log = new Log(Tcs.Mls.Util.getLogFilePath(args[0]));
                    m_log.start();
                }
                 
            }
             
            if (m_log == null)
                m_log = new Log();
             
            m_log.WriteLine();
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine("|                        TCSExecutive Starts  ( C# )                                |");
            m_log.WriteLine("+-----------------------------------------------------------------------------------+");
            m_log.WriteLine();
            //UPGRADE_ISSUE: Class 'java.text.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //UPGRADE_ISSUE: Constructor 'java.text.SimpleDateFormat.SimpleDateFormat' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javatextSimpleDateFormat'"
            //SimpleDateFormat TIME = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
            //m_log.WriteLine("Current Time = " + SupportClass.FormatDateTime(System.Globalization.DateTimeFormatInfo.GetInstance(, System.DateTime.Now));
            m_log.WriteLine("args[0]" + args[0]);
            m_log.WriteLine("args[1]" + args[1]);
            String messageHeader = args[0];
            MLSConnector.RUN_LOCAL = BOARD_ID;
            if (isGetMetadata)
            {
                messageHeader = "<TCService><Function>GetMetadata</Function><Board BoardId=\"" + BOARD_ID + "\"/><Search Type=\"METADATA-Board\" Format=\"XML\" Recursive=\"1\"/></TCService>";
                SearchEngine ctcs = new SearchEngine();
                ctcs.getMetadata(messageHeader);
            }
            else
            {
                TCServer tcServer = new TCServer(args[0], m_log);
                tcServer.runServer();
            } 
        }
        catch (Exception exc)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            m_log.WriteLine(exc.getMessage());
        }

        m_log.WriteLine("Total Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
        m_log.WriteLine();
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine("|                        TCSExecutive Ends                                          |");
        m_log.WriteLine("+-----------------------------------------------------------------------------------+");
        m_log.WriteLine();
        if (m_log != null)
            m_log.stop();
         
    }

}


