//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:50 PM
//

package Tcs.Mls;

import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.ObjectSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Xml.XmlAttributeCollection;
import CS2JNet.System.Xml.XmlDocument;
import CS2JNet.System.Xml.XmlElement;
import CS2JNet.System.Xml.XmlNode;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import net.toppro.components.mls.engine.Log;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.MLSEnvironment;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class MLSConnector   
{
    public static int RUN_LOCAL = 0;
    private static long MAXIMUM_EXECUTIVE_TIME = 7200000;
    private static long MAXIMUM_EXECUTIVE_TIME_DATAAGG = 86400000;
    private String m_messageHeader;
    private String m_defPath;
    private String m_moduleName;
    private String m_defVersion;
    private String m_propertySubType;
    private String m_boardName;
    private String m_vendorName;
    private String m_stateName;
    private String m_propertyTypeTMK;
    public String getPropertyTypeTMK() throws Exception {
        return m_propertyTypeTMK;
    }

    public void setPropertyTypeTMK(String value) throws Exception {
        m_propertyTypeTMK = value;
    }

    private String m_moduleID;
    public String getModuleID() throws Exception {
        return m_moduleID;
    }

    public void setModuleID(String value) throws Exception {
        m_moduleID = value;
    }

    private int m_picFlag = -1;
    private String m_selectFields = null;
    private int m_isRETSClient = -1;
    private int m_returnNetworkPath = -1;
    private int m_returnDataAggListingsXml = -1;
    private String m_retsQueryParameter = null;
    private String m_message = "";
    public static final int OVERALL_PROGRESS_SEARCH_PREPARE_PICTURE = 3;
    public static final int OVERALL_PROGRESS_SEARCH_PREPARE = 2;
    public static final int OVERALL_PROGRESS_PICTURE = 1;
    public static final int OVERALL_PROGRESS_CHECKCREDENTIAL = 0;
    private static final int PROGRESS_NONE = -1;
    private static final int PROGRESS_SEARCH = 0;
    private static final int PROGRESS_PREPARE = 1;
    private static final int PROGRESS_PICTURE = 2;
    private int m_overAllProgress = OVERALL_PROGRESS_SEARCH_PREPARE;
    private int m_currentProgress = PROGRESS_NONE;
    public static final int PROGRESS_MAX_VALUE = 100;
    public static final int PROGRESS_START_VALUE = 10;
    public static final int PROGRESS_END_VALUE = 95;
    public static final int CLIENT_ENGINE_BOARDID = 999999;
    private static final String NODE_FUNCTION_NAME = ".//Function";
    private static final String NODE_LOGIN_INFO = ".//Login";
    private static final String ATTRIBUTE_USERAGENT = "UserAgent";
    private static final String ATTRIBUTE_RETSUAPWD = "RetsUAPwd";
    private static final String NODE_BOARD_INFO = ".//Board";
    private static final String NODE_SEARCH_FIELDS = ".//Search";
    private static final String NODE_STANDARD_SEARCH_FIELDS = "//StandardSearch";
    private static final String NODE_LISTINGS = ".//Listings";
    private static final String NODE_CALLBACK = ".//Callback";
    public static final String NODE_URL = ".//URL";
    public static final String NODE_DATA = ".//Data";
    private static final String ATTRIBUTE_PASSWORD = "password";
    private static final String ATTRIBUTE_BOARD_ID = "BoardId";
    private static final String ATTRIBUTE_LOG = "Log";
    private static final String ATTRIBUTE_MODULE_NAME = "ClassName";
    private static final String ATTRIBUTE_PICTURE_FLAG = "Picture";
    private static final String ATTRIBUTE_PROPERTY_TYPE = "ST_PType";
    private static final String ATTRIBUTE_RETURN_NETWORK_PATH = "ReturnNetworkPath";
    private static final String ATTRIBUTE_RETURN_DATAAGGLISTINGS_XML = "ReturnDataAggListingsXML";
    private static final String ATTRIBUTE_RETS_QUERY_PARAMETER = "RETSQueryParameter";
    private static final String ATTRIBUTE_SELECT_RESULTFIELDS = "SelectFields";
    public static final String NODE_CONNECTION_CONN = "connection_info.conn";
    public static final String NODE_CONNECTIONS = ".//connection_info";
    public static final String ATTRIBUTE_BOARD_STATUS = "board_status_id";
    public static final String ATTRIBUTE_IS_COMPATIBLE = "is_compatible";
    public static final String ATTRIBUTE_DEFINITION_FILE = "definition_file";
    public static final String ATTRIBUTE_CONNECTION_TYPE = "connection_type";
    public static final String ATTRIBUTE_CONNECTION_NAME = "connection_name";
    public static final String ATTRIBUTE_VERSION = "version";
    public static final String ATTRIBUTE_PROPERTY_SUB_TYPE = "property_sub_type";
    public static final String ATTRIBUTE_BOARD_NAME = "board_name";
    public static final String ATTRIBUTE_BOARD_ID_DB = "board_id";
    public static final String ATTRIBUTE_METADATA_VERSION = "MetadataVersion";
    public static final String ATTRIBUTE_MLS_METADATA_VERSION = "MLSRETSMetadataVer";
    public static final String ATTRIBUTE_VENDOR_NAME = "vendor_name";
    public static final String ATTRIBUTE_STATE_NAME = "state_name";
    public static final String ATTRIBUTE_HELP_URL = "url_location_path";
    public static final String ATTRIBUTE_MAX_LISTING_NUMBER = "MaxListingNumber";
    public static final String ATTRIBUTE_STATUS = "ST_Status";
    public static final String ATTRIBUTE_RECORD_COUNT = "Count";
    public static final String ATTRIBUTE_SEARCH_BY_MLSNUMBER = "SearchLayout";
    public static final String ATTRIBUTE_UPLOADID = "UploadID";
    public static final String ATTRIBUTE_TYPE = "type";
    public static final String ATTRIBUTE_RECORD_LIMIT = "RecordsLimit";
    public static final String ATTRIBUTE_CONDITIONCODE = "ST_ConditionCode";
    public static final String ATTRIBUTE_OVERRIDE_RECORD_LIMIT = "OverrideRecordsLimit";
    public static final String ATTRIBUTE_CLIENTNAME = "ClientName";
    public static final String ATTRIBUTE_MODULE_ID = "module_id";
    public static final String ATTRIBUTE_MODULE_NAME_DB = "module_name";
    public static final String ATTRIBUTE_DEF_PATH = "DefPath";
    public static final String ATTRIBUTE_BYPASS_AGENTROSTER_AUTH = "BypassARAuthentication";
    public static final String ATTRIBUTE_INCLUDE_TL_BLOB_FIELD = "IncludeRETSBlob";
    public static final String ATTRIBUTE_COMPACT_DATA_FORMAT = "CompactFormat";
    public static final String ATTRIBUTE_RESULT_FILE_PATH = "ResultFilePath";
    public static final String ATTRIBUTE_RETS_CLASS_NAME = "RETSClass";
    public static final String ATTRIBUTE_RETURNIDSONLY = "ReturnIDsOnly";
    public static final String AttributeReturnMlsNumberOnly = "ReturnMlsNumberOnly";
    public static final String ATTRIBUTE_DACOUNT = "DACount";
    public static final String ATTRIBUTE_DATASOURCEID = "DataSourceID";
    public static final String ATTRIBUTE_TMK_PROPERTY_TYPE = "type_description";
    public static final String AttributeResidentialClassOnly = "ResidentialClassOnly";
    public static final String AttributeBypassTimeZone = "BypassTimezone";
    public static final String NODE_TCSERVICE = "TCService";
    private static final String FAKE_DEF_LIST = "<connection_info board_status_id=\"1\" is_compatible=\"1\"><conn board_id=\"999999\" vendor_name=\"TCS\" state_name=\"NA\" board_name=\"Sample Board\" module_id=\"1847\" module_name=\"Sample module\" type=\"Internet\" url_location_path=\"http://www.topproducer.com/support/tc7i/default.asp?id=7itcindex\" version=\"0\" connection_id=\"{A3A85D3C-07E3-4188-975C-1DEF59D126EB}\" connection_type=\"Residential\" connection_name=\"sample.def\" is_login_required=\"1\" property_sub_type=\"\" type_description=\"\" definition_file=\"testre.def\" when_checked=\"5/27/2009 10:32:42 AM\" mls_search_order=\"1\"/><conn board_id=\"999999\" vendor_name=\"TCS\" state_name=\"NA\" board_name=\"Sample Board\" module_id=\"1847\" module_name=\"Sample module\" type=\"Internet\" url_location_path=\"http://www.topproducer.com/support/tc7i/default.asp?id=7itcindex\" version=\"0\" connection_id=\"{A3A85D3C-07E3-4188-975C-1DEF59D126EB}\" connection_type=\"Residential\" connection_name=\"sampleag.sql\" is_login_required=\"1\" property_sub_type=\"\" type_description=\"\" definition_file=\"sampleag.sql\" when_checked=\"5/27/2009 10:32:42 AM\" mls_search_order=\"2\"/><conn board_id=\"999999\" vendor_name=\"TCS\" state_name=\"NA\" board_name=\"Sample Board\" module_id=\"1847\" module_name=\"Sample module\" type=\"Internet\" url_location_path=\"http://www.topproducer.com/support/tc7i/default.asp?id=7itcindex\" version=\"0\" connection_id=\"{A3A85D3C-07E3-4188-975C-1DEF59D126EB}\" connection_type=\"Residential\" connection_name=\"sampleof.sql\" is_login_required=\"1\" property_sub_type=\"\" type_description=\"\" definition_file=\"sampleof.sql\" when_checked=\"5/27/2009 10:32:42 AM\" mls_search_order=\"3\"/><conn board_id=\"999999\" vendor_name=\"TCS\" state_name=\"NA\" board_name=\"Sample Board\" module_id=\"1847\" module_name=\"Sample module\" type=\"Internet\" url_location_path=\"http://www.topproducer.com/support/tc7i/default.asp?id=7itcindex\" version=\"0\" connection_id=\"{A3A85D3C-07E3-4188-975C-1DEF59D126EB}\" connection_type=\"Residential\" connection_name=\"sampleoh.sql\" is_login_required=\"1\" property_sub_type=\"\" type_description=\"\" definition_file=\"sampleoh.sql\" when_checked=\"5/27/2009 10:32:42 AM\" mls_search_order=\"4\"/><conn board_id=\"999999\" vendor_name=\"TCS\" state_name=\"NA\" board_name=\"Sample Board\" module_id=\"1847\" module_name=\"Sample module\" type=\"Internet\" url_location_path=\"http://www.topproducer.com/support/tc7i/default.asp?id=7itcindex\" version=\"0\" connection_id=\"{A3A85D3C-07E3-4188-975C-1DEF59D126EB}\" connection_type=\"Residential\" connection_name=\"sampleda.sql\" is_login_required=\"1\" property_sub_type=\"\" type_description=\"\" definition_file=\"sampleda.sql\" when_checked=\"5/27/2009 10:32:42 AM\" mls_search_order=\"5\"/></connection_info>";
    private static final String FAKE_DEF_LIST_FOR_TCSMETADATA = "<connection_info board_status_id=\"1\" is_compatible=\"1\"><conn board_id=\"999999\" vendor_name=\"TCS\" state_name=\"NA\" board_name=\"Sample Board\" module_id=\"1847\" module_name=\"Sample module\" type=\"Internet\" url_location_path=\"http://www.topproducer.com/support/tc7i/default.asp?id=7itcindex\" version=\"0\" connection_id=\"{A3A85D3C-07E3-4188-975C-1DEF59D126EB}\" connection_type=\"Residential\" connection_name=\"sample.def\" is_login_required=\"1\" property_sub_type=\"\" type_description=\"\" definition_file=\"{0}\" when_checked=\"5/27/2009 10:32:42 AM\" mls_search_order=\"1\"/></connection_info>";
    private static final int BOARD_STATUS_TEMP_UNAVAILABLE = 2;
    private static final int BOARD_STATUS_UNAVAILABLE = 3;
    private XmlElement m_inputXml = null;
    private static final String LOCAL_DEF_FILE = null;
    //"C:\\temp\\C2naflre.def"; //"C:\\MLSDEF\\FRMACARE.def";
    private net.toppro.components.mls.engine.MLSEngine m_engine = null;
    //private TCSBLLWrapper._CTCSBLL m_ctcsBll = null;
    private TCSBllWrapper m_ctcsBll = null;
    private DataJobsDal m_dataJobsDal = null;
    private Hashtable m_searchFields = Hashtable.Synchronized(new Hashtable());
    private Hashtable m_standardSearchFields = Hashtable.Synchronized(new Hashtable());
    private int m_currentSearchNumber = 1;
    private int m_overAllSearchNumber = 1;
    private String m_propertyTpye;
    private String m_searchStatus = "";
    private boolean m_searchByMLSNumber = false;
    private TPAppRequest m_requestClient = null;
    protected Log m_log;
    private String m_agentDefPath;
    private boolean m_needAgentOfficeSearch;
    private TCServer m_tcServer;
    private String m_searchTempFolder = "";
    private String m_resultFolder = "";
    private ArrayList m_retsRawDataFiles = new ArrayList();
    private int m_lastProgressCount = 0;
    private int m_isRemoveRoomDimFromFeatures = -1;
    private String m_defaultChunkingParameter = null;
    private HashMap<String,HashMap<String,String>> m_agentRoster;
    private int m_isUsingOldLogicForPublicStatus = -1;
    public ArrayList getRetsRawDataFiles() throws Exception {
        return m_retsRawDataFiles;
    }

    public void setRetsRawDataFiles(ArrayList value) throws Exception {
        m_retsRawDataFiles = value;
    }

    public boolean getNeedAgentOfficeSearch() throws Exception {
        return m_needAgentOfficeSearch;
    }

    public void setNeedAgentOfficeSearch(boolean value) throws Exception {
        m_needAgentOfficeSearch = value;
    }

    public String getAgentDefPath() throws Exception {
        return m_agentDefPath;
    }

    public void setAgentDefPath(String value) throws Exception {
        m_agentDefPath = value;
    }

    private String m_agentDefVersion;
    public String getAgentDefVersion() throws Exception {
        return m_agentDefVersion;
    }

    public void setAgentDefVersion(String value) throws Exception {
        m_agentDefVersion = value;
    }

    private String m_officeDefPath;
    public String getOfficeDefPath() throws Exception {
        return m_officeDefPath;
    }

    public void setOfficeDefPath(String value) throws Exception {
        m_officeDefPath = value;
    }

    private String m_officeDefVersion;
    public String getOfficeDefVersion() throws Exception {
        return m_officeDefVersion;
    }

    public void setOfficeDefVersion(String value) throws Exception {
        m_officeDefVersion = value;
    }

    private String m_retsClassName = null;
    public String getRetsClassName() throws Exception {
        return m_retsClassName;
    }

    public void setRetsClassName(String value) throws Exception {
        m_retsClassName = value;
    }

    private Boolean m_isDACount = null;
    private long m_startTime;
    public MLSConnector(String messageHeader, Log log, TCServer tcServer) throws Exception {
        m_messageHeader = messageHeader;
        m_startTime = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        m_log = log;
        m_tcServer = tcServer;
    }

    private void getTCSBllWrapper() throws Exception {
        if (m_ctcsBll == null)
            m_ctcsBll = new TCSBllWrapper();
         
    }

    public String getDataFromDB(String fieldName, String referenceID, String fieldType) throws Exception {
        if (m_dataJobsDal == null)
            m_dataJobsDal = new DataJobsDal();
         
        String result = "";
        try
        {
            int posFdName = fieldName.lastIndexOf('.') + 1;
            String fdName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            String __dummyScrutVar0 = fieldType;
            if (__dummyScrutVar0.equals("AG"))
            {
                result = m_dataJobsDal.TcsGetagentrosterinfobyfieldname(getModuleID(), referenceID, fdName);
            }
            else if (__dummyScrutVar0.equals("OF"))
            {
                result = m_dataJobsDal.TcsGetofficerosterinfobyfieldname(getModuleID(), referenceID, fdName);
            }
              
        }
        catch (Exception __dummyCatchVar0)
        {
            result = "";
        }

        return result;
    }

    public boolean isUsingOldLogicForPublicStatus() throws Exception {
        if (m_isUsingOldLogicForPublicStatus == -1)
        {
            try
            {
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ dbAccess = new DBAccess();
                m_isUsingOldLogicForPublicStatus = (int)dbAccess.ExecuteScalar("select count(*) from dbo.tcs_pending_status_new where module_id=" + getModuleID());
            }
            catch (Exception ex)
            {
                m_isUsingOldLogicForPublicStatus = 0;
            }
        
        }
         
        return m_isUsingOldLogicForPublicStatus == 0;
    }

    public HashSet<String> getDomFilteredFields() throws Exception {
        HashSet<String> hs = new HashSet<String>();
        try
        {
            DataJobsDal dal = new DataJobsDal();
            IDataReader dr = dal.GetDataReader("select distinct field_name from dbo.dom_filter_block_fields where module_id=" + getModuleID() + " and type in (1,3) and field_name not in (select field_name from dom_filter_block_fields where module_id=" + getModuleID() + " and type=2)");
            while (dr.Read())
            {
                hs.Add((String)dr[0]);
            }
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
        }
        finally
        {
            if (hs.Count == 0)
                hs.Add("N/A/NotAvailable");
             
        }
        return hs;
    }

    public net.toppro.components.mls.engine.MLSEngine createEngine(XmlNode ele, TPAppRequest tpAppRequest) throws Exception {
        try
        {
            //MLSUtil.printCurrentTime( "Create engine start" );
            setCurrentDEF(ele);
            //MLSUtil.printCurrentTime( "set current def done" );
            if (RUN_LOCAL == 0)
                m_engine = new net.toppro.components.mls.engine.MLSEngine(new MLSEnvironment(this), tpAppRequest);
            else if (RUN_LOCAL == CLIENT_ENGINE_BOARDID)
            {
                m_engine = new net.toppro.components.mls.engine.MLSEngine(new MLSEnvironment(this), Tcs.Mls.Util.loadFile(getDefPath()), tpAppRequest);
            }
            else
                m_engine = new net.toppro.components.mls.engine.MLSEngine(new MLSEnvironment(this), Tcs.Mls.Util.loadFile("c:\\temp\\" + RUN_LOCAL + ".def"), tpAppRequest);  
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return m_engine;
    }

    //MLSUtil.printCurrentTime( "Create engine done" );
    public net.toppro.components.mls.engine.MLSEngine createEngine(net.toppro.components.mls.engine.MLSEngine.EngineType type) throws Exception {
        return null;
    }

    //MLSEngine engine = null;
    //try
    //{
    //    if (RUN_LOCAL == 0)
    //    {
    //        if (type == MLSEngine.EngineType.AgentEngine)
    //        {
    //            if(string.IsNullOrEmpty(AgentDefPath))
    //                return null;
    //            m_defVersion = AgentDefVersion;
    //            engine = new MLSEngine(new MLSEnvironment(this), type, AgentDefPath, ); }
    //        else
    //        {
    //            if(string.IsNullOrEmpty(OfficeDefPath))
    //                return null;
    //            m_defVersion = OfficeDefVersion;
    //            engine = new MLSEngine(new MLSEnvironment(this), type, OfficeDefPath);
    //        }
    //    }
    //    else
    //    {
    //        string localPath = "";
    //        switch (type)
    //        {
    //            case MLSEngine.EngineType.AgentEngine:
    //                localPath = "c:\\temp\\" + RUN_LOCAL + "agent.sql";
    //                break;
    //            case MLSEngine.EngineType.OfficeEngine:
    //                localPath = "c:\\temp\\" + RUN_LOCAL + "office.sql";
    //                break;
    //            default:
    //                localPath = "c:\\temp\\" + RUN_LOCAL + ".def";
    //                break;
    //        }
    //        if(File.Exists(localPath))
    //            engine = new MLSEngine(new MLSEnvironment(this), Util.loadFile(localPath));
    //    }
    //    //MLSUtil.printCurrentTime( "Create engine done" );
    //}
    //catch (System.Exception e)
    //{
    //    throw TCSException.produceTCSException(e);
    //}
    public static net.toppro.components.mls.engine.MLSEngine createClientMLSEngine(String defPath) throws Exception {
        return new net.toppro.components.mls.engine.MLSEngine(defPath,null);
    }

    public net.toppro.components.mls.engine.MLSEngine getEngine() throws Exception {
        return m_engine;
    }

    public String getSystemPlusMetadataVersion() throws Exception {
        String version = "1" + StringSupport.PadLeft(net.toppro.components.mls.engine.MLSEngine.MLS_ENGINE_VERSION, 3, '0');
        version += "1" + StringSupport.PadLeft(getDefFileVersion(), 3, '0');
        return version;
    }

    public String getDefaultChunkingParameter() throws Exception {
        if (m_defaultChunkingParameter == null)
        {
            Properties settings = (Properties)NONE.GetSection("GeneralConfiguration");
            String val = (String)settings["DefaultAdvancedChunkingParameter"];
            if (StringSupport.isNullOrEmpty(val))
                val = "";
             
            m_defaultChunkingParameter = val;
        }
         
        return m_defaultChunkingParameter;
    }

    public boolean isRemoveRoomDimFromFeatures() throws Exception {
        if (m_isRemoveRoomDimFromFeatures == -1)
        {
            Properties settings = (Properties)NONE.GetSection("GeneralConfiguration");
            String val = (String)settings["RemoveRoomDimentionsFromFeatures"];
            if (!StringSupport.isNullOrEmpty(val) && val.equals("1"))
            {
                m_isRemoveRoomDimFromFeatures = 1;
            }
            else
            {
                m_isRemoveRoomDimFromFeatures = 0;
            } 
        }
         
        return m_isRemoveRoomDimFromFeatures == 1;
    }

    public void addRawDataFile(String path) throws Exception {
        getRetsRawDataFiles().add(path);
    }

    //public virtual bool isRETSClient()
    //{
    //    if (m_isRETSClient == - 1)
    //    {
    //        try
    //        {
    //            int clientType = System.Int32.Parse(m_engine.GetClientType());
    //            switch (clientType)
    //            {
    //                case MLSEngine.CLIENT_TYPE_RETS:
    //                case MLSEngine.CLIENT_TYPE_GARDENSTATE:
    //                case MLSEngine.CLIENT_TYPE_SAFEMLS:
    //                    m_isRETSClient = 1;
    //                    break;
    //                default:
    //                    m_isRETSClient = 0;
    //                    break;
    //            }
    //        }
    //        catch (System.Exception exc)
    //        {
    //            WriteLine("-----------------Can not get client type--------------");
    //            m_isRETSClient = 0;
    //        }
    //    }
    //    return m_isRETSClient == 1;
    //}
    public boolean isLogOnMode() throws Exception {
        String s = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_LOG);
        if (s == null || s.length() == 0 || !s.toUpperCase().equals("1".toUpperCase()))
            return false;
        else
            return true; 
    }

    public String getDefPath() throws Exception {
        String s = getAttribute(getInputXml(),NODE_BOARD_INFO,ATTRIBUTE_DEF_PATH);
        return s;
    }

    public String getMessageHeader() throws Exception {
        return m_messageHeader;
    }

    public String getLocalHomePath() throws Exception {
        return Tcs.Mls.Util.getHomePath();
    }

    public String getSearchTempFolder() throws Exception {
        if (m_searchTempFolder.length() > 0)
            return m_searchTempFolder;
         
        if (RUN_LOCAL == CLIENT_ENGINE_BOARDID)
        {
            m_searchTempFolder = (new File(getDefPath())).getParent();
            return m_searchTempFolder;
        }
         
        String messageHeader = m_messageHeader;
        if (m_messageHeader.startsWith("<TCService"))
            messageHeader = m_tcServer.getMessageHeaderGuid();
         
        String lastDigit = m_messageHeader.substring(m_messageHeader.length() - 3, (m_messageHeader.length() - 3) + (1));
        m_searchTempFolder = Tcs.Mls.Util.endSlash(Tcs.Mls.Util.getHomePath()) + "MLS\\" + DateTimeSupport.ToString(Calendar.getInstance().getTime(), "yyyyMMdd") + lastDigit + "\\" + messageHeader;
        return m_searchTempFolder;
    }

    public String getResultFolder() throws Exception {
        if (m_resultFolder.length() > 0)
            return m_resultFolder;
         
        if (RUN_LOCAL == CLIENT_ENGINE_BOARDID)
        {
            m_resultFolder = (new File(getDefPath())).getParent();
            return m_resultFolder;
        }
         
        String lastDigit = m_messageHeader.substring(m_messageHeader.length() - 3, (m_messageHeader.length() - 3) + (1));
        m_resultFolder = Tcs.Mls.Util.endSlash(Tcs.Mls.Util.getHomePath()) + "TCSResult\\" + DateTimeSupport.ToString(Calendar.getInstance().getTime(), "yyyyMMdd") + lastDigit;
        if (!(new File(m_resultFolder)).exists())
            (new File(m_resultFolder)).mkdirs();
         
        return m_resultFolder;
    }

    public String getBoardID() throws Exception {
        return getAttribute(getInputXml(),NODE_BOARD_INFO,ATTRIBUTE_BOARD_ID);
    }

    public String getMetadataVersion() throws Exception {
        return getAttribute(getInputXml(),NODE_BOARD_INFO,ATTRIBUTE_METADATA_VERSION);
    }

    public String getMLSMetadataVersion() throws Exception {
        return getAttribute(getInputXml(),NODE_BOARD_INFO,ATTRIBUTE_MLS_METADATA_VERSION);
    }

    private String getAttribute(XmlElement ele, String strNodeName, String strAttributeName) throws Exception {
        return getAttribute(ele,strNodeName,strAttributeName,"");
    }

    private String getAttribute(XmlElement ele, String strNodeName, String strAttributeName, String strDefaultValue) throws Exception {
        XmlNode xmlnode = ele.selectSingleNode(strNodeName);
        if (xmlnode.getAttributes().get(strAttributeName) == null)
            return strDefaultValue;
         
        if (xmlnode.getAttributes().get(strAttributeName).getValue().length() == 0)
            return strDefaultValue;
         
        return xmlnode.getAttributes().get(strAttributeName).getValue();
    }

    public XmlNode getCallback() throws Exception {
        return getInputXml().selectSingleNode(NODE_CALLBACK);
    }

    public String getPropertyType() throws Exception {
        if (m_propertyTpye != null)
            return m_propertyTpye;
         
        try
        {
            m_propertyTpye = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_PROPERTY_TYPE);
            if (m_propertyTpye == null)
                m_propertyTpye = "";
             
        }
        catch (Exception exc)
        {
        }

        return m_propertyTpye;
    }

    public String getResultFilePath() throws Exception {
        return getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RESULT_FILE_PATH);
    }

    public void setSearchByMLSNumber(boolean b) throws Exception {
        m_searchByMLSNumber = b;
    }

    public boolean getSearchByMLSNumber() throws Exception {
        return m_searchByMLSNumber;
    }

    public String getFunctionName() throws Exception {
        return getInputXml().selectSingleNode(NODE_FUNCTION_NAME).getInnerText();
    }

    //XmlNode node = m_inputXml.SelectSingleNode(".//Function");
    public String getAgentIDForCheckCredential() throws Exception {
        XmlNode node = getInputXml().selectSingleNode("AgentID");
        if (node != null)
            return node.getInnerText();
         
        return "";
    }

    public XmlNode getListingsNode() throws Exception {
        return getInputXml().selectSingleNode(NODE_LISTINGS);
    }

    public String getRETSUAPwd() throws Exception {
        XmlNode node = getInputXml().selectSingleNode(NODE_LOGIN_INFO + "/" + ATTRIBUTE_RETSUAPWD);
        if (node != null)
            return node.getInnerText();
        else
            return ""; 
    }

    public String getUserAgent() throws Exception {
        XmlNode node = getInputXml().selectSingleNode(NODE_LOGIN_INFO + "/" + ATTRIBUTE_USERAGENT);
        if (node != null)
            return node.getInnerText();
        else
            return ""; 
    }

    public int getInputPictureFlag() throws Exception {
        if (m_picFlag == -1)
        {
            try
            {
                String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_PICTURE_FLAG);
                if (flag == null || flag.length() == 0)
                    m_picFlag = 0;
                else
                    m_picFlag = Integer.valueOf(flag); 
            }
            catch (Exception exc)
            {
                writeLine("-------------------Can not get picture flag-------------------------");
                m_picFlag = 0;
            }
        
        }
         
        return m_picFlag;
    }

    public String getSelectFields() throws Exception {
        if (m_selectFields == null)
        {
            m_selectFields = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_SELECT_RESULTFIELDS);
            if (m_selectFields == null)
                m_selectFields = "";
             
        }
         
        return m_selectFields;
    }

    public boolean isReturnNetworkPath() throws Exception {
        if (m_returnNetworkPath == -1)
        {
            try
            {
                String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RETURN_NETWORK_PATH);
                if (flag == null || flag.length() == 0)
                    m_returnNetworkPath = 0;
                else
                    m_returnNetworkPath = Integer.valueOf(flag); 
            }
            catch (Exception exc)
            {
                writeLine("-------------------Can not get picture flag-------------------------" + exc.getMessage());
                m_returnNetworkPath = 0;
            }
        
        }
         
        return m_returnNetworkPath == 1;
    }

    public boolean isReturnDataAggListingsXml() throws Exception {
        if (m_returnDataAggListingsXml == -1)
        {
            try
            {
                String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RETURN_DATAAGGLISTINGS_XML);
                if (flag == null || flag.length() == 0)
                    m_returnDataAggListingsXml = 0;
                else
                    m_returnDataAggListingsXml = Integer.valueOf(flag); 
            }
            catch (Exception exc)
            {
                writeLine("-------------------Can not get picture flag-------------------------" + exc.getMessage());
                m_returnNetworkPath = 0;
            }
        
        }
         
        return m_returnDataAggListingsXml == 1;
    }

    public String getRETSQueryParameter() throws Exception {
        if (m_retsQueryParameter == null)
        {
            m_retsQueryParameter = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RETS_QUERY_PARAMETER);
            if (StringSupport.isNullOrEmpty(m_retsQueryParameter))
                m_retsQueryParameter = "";
             
        }
         
        return m_retsQueryParameter;
    }

    public int getFlags() throws Exception {
        int flag = 0;
        if (getInputPictureFlag() == TCServer.FLAG_MULTIPLE_PICTURE)
            flag = MLSEnvironment.FLAG_GET_MULTI_PICTURE;
         
        if (getDefFileVersion().toUpperCase().equals("0".toUpperCase()))
            flag = flag | MLSEnvironment.FLAG_NO_LOCAL_CACHE;
         
        if (isGetCount())
            flag = flag | MLSEnvironment.FLAG_GET_RECORD_COUNT;
         
        if (getFunctionName().toUpperCase().equals(TCServer.FUNCTION_CHECK_CREDENTIAL_NAME.toUpperCase()))
            flag = flag | MLSEnvironment.FLAG_CHECK_CREDENTIAL;
         
        return flag | MLSEnvironment.FLAG_INCLUDE_HIDDEN_FIELD | MLSEnvironment.FLAG_USE_CURRENT_THREAD_RECIEVE_DATA;
    }

    //if (getFunctionName().ToUpper().Equals(TCServer.FUNCTION_GET_DATA_AGG_LISTINGS.ToUpper()) || getFunctionName().ToUpper().Equals(TCServer.FUNCTION_GET_IDX_LISTINGS.ToUpper()))
    //    flag = flag | MLSEnvironment.FLAG_GET_ADDITIONAL_DATA;
    //|MLSEnvironment.FLAG_USE_OUTSIDE_HTTP_API ;
    public boolean isGetPictureRequest() throws Exception {
        return m_tcServer.isGetPicture();
    }

    public boolean isGetCount() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RECORD_COUNT);
        if (flag == null)
            flag = "";
         
        return flag.toUpperCase().equals("1".toUpperCase());
    }

    public boolean isBypassAgentRosterAuth() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_BYPASS_AGENTROSTER_AUTH);
        if (flag == null)
            flag = "";
         
        return StringSupport.Trim(flag).equals("1");
    }

    public boolean isIncludingTlBlobField() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_INCLUDE_TL_BLOB_FIELD);
        if (flag == null)
            flag = "";
         
        return StringSupport.Trim(flag).equals("1");
    }

    public boolean isUsingCompactFormat() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_COMPACT_DATA_FORMAT);
        if (flag == null)
            flag = "";
         
        return StringSupport.Trim(flag).equals("1");
    }

    public boolean isBypassTimezone() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,AttributeBypassTimeZone);
        if (flag == null)
            flag = "";
         
        return StringSupport.Trim(flag).equals("1");
    }

    public boolean isReturnIdsOnly() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RETURNIDSONLY);
        if (flag == null)
            flag = "";
         
        return StringSupport.Trim(flag).equals("1");
    }

    public boolean isResidentialClassOnly() throws Exception {
        String flag = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,AttributeResidentialClassOnly);
        if (flag == null)
            flag = "";
         
        return StringSupport.Trim(flag).equals("1");
    }

    public String[] getLoginInfoFromRequest() throws Exception {
        XmlNode loginInfo = getLoginInfo();
        if (loginInfo == null || loginInfo.getChildNodes().size() == 0)
            return null;
         
        String[] login = new String[2];
        try
        {
            login[0] = loginInfo.getChildNodes().get(0).getInnerText();
            login[1] = loginInfo.getChildNodes().get(1).getInnerText();
        }
        catch (Exception exc)
        {
            writeLine("Can't get login info.");
        }

        return login;
    }

    public boolean isSearchByMLSNumber() throws Exception {
        String arrSearchByMLS = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_SEARCH_BY_MLSNUMBER);
        if (arrSearchByMLS == null)
            return false;
         
        return ObjectSupport.Equals("MLS Number", StringComparison.CurrentCultureIgnoreCase) ? true : false;
    }

    //public String getRecordCountFlag() throws TCSException
    //{
    //	return flag;
    //}
    public int getMaxListingNumber() throws Exception {
        String num = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_MAX_LISTING_NUMBER);
        try
        {
            if (!StringSupport.isNullOrEmpty(num))
                return Integer.valueOf(num);
            else
                return -1; 
        }
        catch (Exception exc)
        {
            return -1;
        }
    
    }

    public String getConditionCode() throws Exception {
        String result = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_CONDITIONCODE);
        if (StringSupport.isNullOrEmpty(result))
            result = "";
         
        return result;
    }

    public String getDefFile() throws Exception {
        String result;
        if (RUN_LOCAL > 0)
            result = Tcs.Mls.Util.loadFile("c:\\temp\\" + RUN_LOCAL + ".def");
        else
            result = Tcs.Mls.Util.loadFile(m_defPath); 
        return result;
    }

    public String getModuleName() throws Exception {
        return m_moduleName;
    }

    public String getDefFileVersion() throws Exception {
        return m_defVersion;
    }

    public String getStateProvince() throws Exception {
        return m_stateName;
    }

    public String getPropertySubType() throws Exception {
        return m_propertySubType;
    }

    public String getBinFolder() throws Exception {
        return Tcs.Mls.Util.getBinFolder();
    }

    public void writeLine(String line) throws Exception {
        m_log.writeLine(line);
    }

    public void writeLine(String line, boolean isCompactCommunicationLog) throws Exception {
        m_log.writeLine(line,isCompactCommunicationLog);
    }

    public String[] getStatus() throws Exception {
        getSearchFields();
        String st = (String)m_searchFields.get(ATTRIBUTE_STATUS);
        //GetAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_STATUS);
        if (st == null)
            st = "";
         
        String[] status = new String[]{ "" };
        if (st.length() > 0)
            status = Tcs.Mls.Util.stringSplit(st,",");
         
        return status;
    }

    public String[] getStandardStatus() throws Exception {
        String st = getAttribute(getInputXml(),NODE_STANDARD_SEARCH_FIELDS,ATTRIBUTE_STATUS);
        if (st == null)
            st = "";
         
        String[] status = new String[]{ "" };
        if (st.length() > 0)
            status = Tcs.Mls.Util.stringSplit(st,",");
         
        return status;
    }

    public void setSearchFields(String fieldName, String value) throws Exception {
        getSearchFields();
        if (m_searchFields.containsKey(fieldName))
            m_searchFields.put(fieldName, value);
        else
            m_searchFields.put(fieldName, value); 
    }

    public Hashtable getSearchFields() throws Exception {
        if (m_searchFields.size() > 0)
            return m_searchFields;
         
        //XmlNode node = getInputXml().SelectSingleNode( NODE_SEARCH_FIELDS );
        XmlAttributeCollection att = getInputXml().selectSingleNode(NODE_SEARCH_FIELDS).getAttributes();
        String str = "";
        for (int i = 0;i < att.size();i++)
        {
            //if (att[i].Name.StartsWith("ST_"))
            str = att[i].Value.Trim();
            if (str == null)
                str = "";
             
            m_searchFields.put(att[i].Name, str);
        }
        return m_searchFields;
    }

    public Hashtable getStandardSearchFields() throws Exception {
        if (m_standardSearchFields.size() > 0)
            return m_standardSearchFields;
         
        //XmlNode node = getInputXml().SelectSingleNode(NODE_STANDARD_SEARCH_FIELDS);
        XmlAttributeCollection att = getInputXml().selectSingleNode(NODE_STANDARD_SEARCH_FIELDS).getAttributes();
        String str = "";
        for (int i = 0;i < att.size();i++)
        {
            if (String.Compare(att[i].Name, "Log", true) == 0 || String.Compare(att[i].Name, "Count", true) == 0 || String.Compare(att[i].Name, "RecordsLimit", true) == 0 || String.Compare(att[i].Name, "OverrideRecordsLimit", true) == 0)
            {
                throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NONMLS_SEARCH_PARA_IN_STANDARD_SEARCH),"",TCSException.NONMLS_SEARCH_PARA_IN_STANDARD_SEARCH);
            }
             
            str = att[i].Value.Trim();
            if (str == null)
                str = "";
             
            m_standardSearchFields.put(att[i].Name, str);
        }
        return m_standardSearchFields;
    }

    //}
    public String getUploadFolder() throws Exception {
        String path = Tcs.Mls.Util.endSlash(Tcs.Mls.Util.getUploadFolderPath());
        path += getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_UPLOADID);
        return Tcs.Mls.Util.endSlash(path);
    }

    public XmlNode getLoginInfo() throws Exception {
        XmlNode element = getInputXml().selectSingleNode(NODE_LOGIN_INFO);
        return element;
    }

    public String getConnectionName() throws Exception {
        String s = getAttribute(getInputXml(),NODE_BOARD_INFO,ATTRIBUTE_MODULE_NAME);
        if (s == null)
            s = "";
         
        return s;
    }

    public boolean isDACount() throws Exception {
        if (m_isDACount == null)
        {
            String s = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_DACOUNT);
            if (!StringSupport.isNullOrEmpty(s) && s.equals("1"))
                m_isDACount = true;
            else
                m_isDACount = false; 
        }
         
        return m_isDACount == true;
    }

    public String getDataSourceID() throws Exception {
        String dataSourceID = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_DATASOURCEID);
        if (StringSupport.isNullOrEmpty(dataSourceID))
            throw new TCSException(null,"Data source ID is not specified.","",TCSException.TCSEXCEPTION_SOURCE_APPLICATION,TCSException.INTERNAL_ERROR);
         
        return dataSourceID;
    }

    public HashMap<String,Integer> getXMARTCount() throws Exception {
        HashMap<String,Integer> daCount = new HashMap<String,Integer>();
        CategorizationDal dal = new CategorizationDal("DACount");
        String dataSourceID = getDataSourceID();
        IDataReader dr = dal.SpDATRPCountsByPropTypeSel(dataSourceID);
        try
        {
            {
                if (dr.Read())
                {
                    daCount.put("Residential", (int)dr["TotalSingleFamily"]);
                    daCount.put("Condo", (int)dr["TotalCondo"]);
                    daCount.put("Mobile", (int)dr["TotalMobileHome"]);
                    daCount.put("MultiFamily", (int)dr["TotalMultiFamily"]);
                    daCount.put("Farm", (int)dr["TotalFarm"]);
                    daCount.put("Land", (int)dr["TotalLand"]);
                    daCount.put("Rental", (int)dr["TotalRent"]);
                    daCount.put("Office", (int)dr["TotalActiveOffices"]);
                    daCount.put("Agent", (int)dr["TotalActiveAgents"]);
                }
                else
                    throw new TCSException(null,"Failed to retrieve xMART counts.","",TCSException.TCSEXCEPTION_SOURCE_APPLICATION,TCSException.INTERNAL_ERROR); 
            }
        }
        finally
        {
            if (dr != null)
                Disposable.mkDisposable(dr).dispose();
             
        }
        return daCount;
    }

    public String getRETSClassName() throws Exception {
        if (getRetsClassName() == null)
        {
            String s = getAttribute(getInputXml(),NODE_SEARCH_FIELDS,ATTRIBUTE_RETS_CLASS_NAME);
            if (StringSupport.isNullOrEmpty(s))
                s = "";
            else
            {
                String[] list = StringSupport.Split(s, ',');
                for (int i = 0;i < list.length;i++)
                {
                    if (!StringSupport.isNullOrEmpty(list[i]))
                    {
                        list[i] = StringSupport.Trim(list[i]);
                        if (StringSupport.isNullOrEmpty(getRetsClassName()))
                            setRetsClassName(getRetsClassName() + ("'" + list[i] + "'"));
                        else
                        {
                            setRetsClassName(getRetsClassName() + (",'" + list[i] + "'"));
                        } 
                    }
                     
                }
            } 
        }
         
        return getRetsClassName();
    }

    public XmlNode getDEFList(String compatabilityID) throws Exception {
        XmlDocument doc = new XmlDocument();
        //if (RUN_LOCAL > 0)
        //{
        //    doc.LoadXml(Util.getFileContent("c:\\temp\\getDEFList.xml"));
        //    return doc.SelectSingleNode(NODE_CONNECTIONS);
        //}
        String propertyType = "";
        String connectionName = "";
        if (!m_searchByMLSNumber)
        {
            propertyType = getPropertyType();
            connectionName = Tcs.Mls.Util.encodeEntity(getConnectionName());
        }
         
        String retsClassName = getRETSClassName();
        if (!StringSupport.isNullOrEmpty(retsClassName))
        {
            propertyType = "GetDataAggListing";
            connectionName = retsClassName;
        }
        else
        {
            if (!StringSupport.isNullOrEmpty(propertyType))
                propertyType = "'" + propertyType.replace(",", "','") + "'";
             
        } 
        String request = "<Input board_id=\"" + getBoardID() + "\" property_type=\"" + Tcs.Mls.Util.encodeEntity(propertyType) + "\" class_name=\"" + connectionName + "\" compatibility_id=\"" + compatabilityID + "\"/>";
        String result;
        try
        {
            writeLine("getDEFInfo() request");
            writeLine(request);
            if (RUN_LOCAL != CLIENT_ENGINE_BOARDID)
            {
                getTCSBllWrapper();
                result = m_ctcsBll.GetDEF(request);
            }
            else
                result = FAKE_DEF_LIST; 
            writeLine(result);
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_GET_DEF);
        }

        doc.loadXml(result);
        XmlNode ele = doc.selectSingleNode(NODE_CONNECTIONS);
        String status_id = ele.getAttributes().get(ATTRIBUTE_BOARD_STATUS).getValue();
        if (status_id != null)
        {
            switch(Integer.valueOf(status_id))
            {
                case BOARD_STATUS_TEMP_UNAVAILABLE: 
                    throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_TEMP_UNAVAILABLE) + getBoardID(),"",TCSException.BOARD_TEMP_UNAVAILABLE);
                case BOARD_STATUS_UNAVAILABLE: 
                    throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_UNAVAILABLE) + getBoardID(),"",TCSException.BOARD_UNAVAILABLE);
            
            }
        }
         
        boolean isCompatible = ele.getAttributes().get(ATTRIBUTE_IS_COMPATIBLE).getValue().equals("1");
        if (!isCompatible)
            throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.BOARD_NOT_COMPATIBLE),"",TCSException.BOARD_NOT_COMPATIBLE);
         
        if (StringSupport.isNullOrEmpty(getRETSClassName()) && ele.getChildNodes().size() == 0)
            throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_DEF_FILE_MATCH) + getPropertyType(),"",TCSException.NO_DEF_FILE_MATCH);
         
        if (!m_tcServer.getIsRealTime())
        {
            XmlNode eleAll = getMetaDataDEFList();
            for (int i = 0;i < eleAll.getChildNodes().size();i++)
            {
                if (eleAll.getChildNodes().get(i).getAttributes().get(ATTRIBUTE_DEFINITION_FILE).getValue().toLowerCase().endsWith("ag.sql"))
                {
                    setAgentDefPath(eleAll.getChildNodes().get(i).getAttributes().get(ATTRIBUTE_DEFINITION_FILE).getValue());
                    setAgentDefVersion(eleAll.getChildNodes().get(i).getAttributes().get(ATTRIBUTE_VERSION).getValue());
                }
                 
                if (eleAll.getChildNodes().get(i).getAttributes().get(ATTRIBUTE_DEFINITION_FILE).getValue().toLowerCase().endsWith("of.sql"))
                {
                    setOfficeDefPath(eleAll.getChildNodes().get(i).getAttributes().get(ATTRIBUTE_DEFINITION_FILE).getValue());
                    setOfficeDefVersion(eleAll.getChildNodes().get(i).getAttributes().get(ATTRIBUTE_VERSION).getValue());
                }
                 
            }
        }
         
        XmlNode element = getInputXml().selectSingleNode(NODE_TCSERVICE);
        String clientName = "";
        try
        {
            if (getInputXml().getAttributes().get(ATTRIBUTE_CLIENTNAME) != null)
                clientName = getInputXml().getAttributes().get(ATTRIBUTE_CLIENTNAME).getValue();
             
        }
        catch (Exception e)
        {
        }

        if (!StringSupport.isNullOrEmpty(clientName) && StringSupport.Compare(clientName, "NEX", true) == 0 && !StringSupport.isNullOrEmpty(getAgentDefPath()))
            setNeedAgentOfficeSearch(true);
        else
            setNeedAgentOfficeSearch(false); 
        return ele;
    }

    public String getClientName() throws Exception {
        String clientName = "";
        try
        {
            clientName = getInputXml().getAttributes().get(ATTRIBUTE_CLIENTNAME).getValue();
        }
        catch (Exception e)
        {
            writeLine(e.getMessage());
        }

        if (StringSupport.isNullOrEmpty(clientName))
            clientName = "";
         
        return clientName;
    }

    public XmlNode getMetaDataDEFList() throws Exception {
        XmlDocument doc = new XmlDocument();
        String request = "<Input board_id=\"" + getBoardID() + "\" property_type=\"\" class_name=\"\" compatibility_id=\"\"/>";
        String result;
        try
        {
            writeLine("getDEFInfo() request");
            writeLine(request);
            getTCSBllWrapper();
            result = m_ctcsBll.GetDEF(request);
            writeLine(result);
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_GET_DEF);
        }

        doc.loadXml(result);
        return doc.selectSingleNode(NODE_CONNECTIONS);
    }

    public void setCurrentDEF(XmlNode ele) throws Exception {
        m_defPath = ele.getAttributes().get(ATTRIBUTE_DEFINITION_FILE).getValue();
        m_moduleName = ele.getAttributes().get(ATTRIBUTE_CONNECTION_TYPE).getValue();
        m_defVersion = ele.getAttributes().get(ATTRIBUTE_VERSION).getValue();
        m_propertySubType = ele.getAttributes().get(ATTRIBUTE_PROPERTY_SUB_TYPE).getValue();
        m_boardName = ele.getAttributes().get(ATTRIBUTE_BOARD_NAME).getValue();
        m_vendorName = ele.getAttributes().get(ATTRIBUTE_VENDOR_NAME).getValue();
        m_stateName = ele.getAttributes().get(ATTRIBUTE_STATE_NAME).getValue();
        setModuleID(ele.getAttributes().get(ATTRIBUTE_MODULE_ID).getValue());
        m_propertyTypeTMK = ele.getAttributes().get(ATTRIBUTE_TMK_PROPERTY_TYPE).getValue();
    }

    private String getLookUpTable() throws Exception {
        return "";
    }

    private XmlElement getInputXml() throws Exception {
        if (m_inputXml == null)
        {
            try
            {
                String result = "";
                if (!m_tcServer.getIsRealTime())
                {
                    writeLine("Request: ----- getInputXml() ------ ");
                    writeLine("MessageHeader = " + getMessageHeader());
                    if (getMessageHeader().IndexOf(TCServer.FUNCTION_GET_META_DATA, StringComparison.CurrentCultureIgnoreCase) < 0 && getMessageHeader().IndexOf(TCServer.FUNCTION_GET_MAPPING_FIELDS, StringComparison.CurrentCultureIgnoreCase) < 0 && getMessageHeader().IndexOf(TCServer.FUNCTION_LOAD_AUTOCATEGORY_DATA, StringComparison.CurrentCultureIgnoreCase) < 0)
                    {
                        if (RUN_LOCAL == 0)
                        {
                            getTCSBllWrapper();
                            result = m_ctcsBll.GetMessageBody(getMessageHeader());
                        }
                        else if (RUN_LOCAL == CLIENT_ENGINE_BOARDID)
                        {
                            result = Tcs.Mls.Util.getFileContent(Tcs.Mls.Util.endSlash(getDefPath()) + RUN_LOCAL + ".xml");
                        }
                        else
                            result = Tcs.Mls.Util.getFileContent("c:\\temp\\inputListings" + RUN_LOCAL + ".xml");  
                    }
                    else
                        result = getMessageHeader(); 
                    writeLine("Response: ----- getInputXml() ------ ");
                    writeLine(result);
                }
                else
                    result = m_tcServer.getRequestXml(); 
                XmlDocument doc = new XmlDocument();
                doc.loadXml(result);
                m_inputXml = doc.getDocumentElement();
            }
            catch (Exception e)
            {
                throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_GET_MESSAGE_BODY);
            }
        
        }
         
        return m_inputXml;
    }

    public void saveTaskResult(String objectId, String result) throws Exception {
        if (m_tcServer.getIsRealTime())
        {
            if (MLSConnector.RUN_LOCAL == MLSConnector.CLIENT_ENGINE_BOARDID)
            {
                String defPath = (new File(getDefPath())).getParent();
                if (!StringSupport.isNullOrEmpty(defPath))
                {
                    String resultLocationPath = (new File(defPath, "tcslisting.xml")).toString();
                    File.WriteAllText(resultLocationPath, result);
                }
                 
            }
             
            return ;
        }
         
        String resultFilePath = StringSupport.TrimEnd(getResultFilePath(), new char[]{ '\\' });
        if (!StringSupport.isNullOrEmpty(resultFilePath))
        {
            try
            {
                new BufferedOutputStream(new FileOutputStream(resultFilePath + "\\TaskCompleted.tcs")).close();
            }
            catch (Exception ex)
            {
                TCSException tcsExc = TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.WRITE_RESULT_FLAG_FILE_FAILED) + " -- " + ex.getMessage(),"",TCSException.WRITE_RESULT_FLAG_FILE_FAILED);
                result = tcsExc.getOutputErrorXml();
            }
        
        }
         
        try
        {
            writeLine("Request: ----- saveTaskResult() ------ ");
            writeLine("objectId = " + objectId);
            writeLine("result = " + result);
            if (RUN_LOCAL == 0)
            {
                //if (result.EndsWith(".dbm"))
                //{
                //    m_ctcsBll.SaveTaskFileResult(m_messageHeader, objectId, result);
                //}
                //else
                //{
                getTCSBllWrapper();
                m_ctcsBll.SaveTaskResult(m_messageHeader, objectId, result);
            }
             
            //}
            writeLine("Response: ----- saveTaskResult() ------ ");
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_SAVE_TASK_RESULT);
        }
    
    }

    public void saveTaskFileResult(String objectId, String result) throws Exception {
        if (m_tcServer.getIsRealTime())
            return ;
         
        try
        {
            writeLine("Request: ----- SaveTaskFileResult() ------ ");
            writeLine("objectId = " + objectId);
            //WriteLine("result = " + result);
            if (RUN_LOCAL == 0)
            {
                //m_ctcsBll.SaveTaskFileResult(m_messageHeader, objectId, result);
                getTCSBllWrapper();
                m_ctcsBll.SaveTaskResult(m_messageHeader, objectId, result);
            }
            else
                //No longer copy files to DBMQ result folder to save filer space
                writeLine("result = " + result); 
            writeLine("Response: ----- SaveTaskFileResult() ------ ");
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_SAVE_TASK_RESULT);
        }
    
    }

    public void setCurrentSearchNumber(int value_Renamed) throws Exception {
        m_currentSearchNumber = value_Renamed;
    }

    public void setOverAllSearchNumber(int value_Renamed) throws Exception {
        m_overAllSearchNumber = value_Renamed;
    }

    public void updateTaskProgress(int value_Renamed) throws Exception {
        if (m_tcServer.getIsRealTime() || !StringSupport.isNullOrEmpty(m_tcServer.getModuleID()))
            return ;
         
        try
        {
            if (value_Renamed == 0)
                return ;
             
            if (m_message.toUpperCase().equals("Searching...".toUpperCase()))
                m_currentProgress = PROGRESS_SEARCH;
            else if (m_message.toUpperCase().equals("Preparing records ...".toUpperCase()))
                m_currentProgress = PROGRESS_PREPARE;
            else if (m_message.toUpperCase().equals("Downloading pictures...".toUpperCase()))
                m_currentProgress = PROGRESS_PICTURE;
            else
                m_currentProgress = PROGRESS_NONE;   
            switch(m_currentProgress)
            {
                case PROGRESS_SEARCH: 
                    value_Renamed = (value_Renamed) / m_overAllProgress;
                    break;
                case PROGRESS_PREPARE: 
                    if (value_Renamed == 50 || value_Renamed == 100)
                        value_Renamed = 100 / m_overAllProgress + (value_Renamed) / m_overAllProgress;
                    else
                        value_Renamed = 0; 
                    break;
                case PROGRESS_PICTURE: 
                    if (m_overAllProgress == OVERALL_PROGRESS_SEARCH_PREPARE_PICTURE)
                        value_Renamed = 200 / m_overAllProgress + (value_Renamed) / m_overAllProgress;
                     
                    break;
            
            }
            if (m_currentProgress != -1 && value_Renamed > 0 && value_Renamed < PROGRESS_MAX_VALUE + 1)
                value_Renamed = PROGRESS_START_VALUE + (value_Renamed * (PROGRESS_END_VALUE - PROGRESS_START_VALUE)) / PROGRESS_MAX_VALUE;
             
            if (value_Renamed > 0)
            {
                //if( updateProgress )
                value_Renamed = ((m_currentSearchNumber - 1) * 100 + value_Renamed) / m_overAllSearchNumber;
                //long time = System.currentTimeMillis();
                if (RUN_LOCAL == 0)
                {
                    getTCSBllWrapper();
                    if (value_Renamed != m_lastProgressCount)
                    {
                        writeLine("Request: ----- updateTaskProgress() ------ ");
                        writeLine("Value = : " + value_Renamed);
                        m_ctcsBll.UpdateTaskProgress(m_messageHeader, value_Renamed);
                    }
                     
                    m_lastProgressCount = value_Renamed;
                }
                 
            }
             
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_UPDATE_TASK_PROGRESS);
        }
    
    }

    //System.out.println("Response: ----- updateTask Progress() ------ ");
    public void setMessage(String message) throws Exception {
        m_message = message;
    }

    public void setOverallProgress(int value_Renamed) throws Exception {
        m_overAllProgress = value_Renamed;
    }

    public void updateTaskStatus(int value_Renamed) throws Exception {
        if (m_tcServer.getIsRealTime())
            return ;
         
        try
        {
            if (RUN_LOCAL == 0)
            {
                getTCSBllWrapper();
                m_ctcsBll.UpdateTaskStatus(m_messageHeader, value_Renamed);
            }
             
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_UPDATE_TASK_PROGRESS);
        }
    
    }

    public void addFilesToDelete(String path) throws Exception {
        try
        {
            if ((new File(path)).exists())
            {
                if (m_tcServer.getIsRealTime())
                    return ;
                 
                getTCSBllWrapper();
                m_ctcsBll.AddFilesToDelete(path);
            }
             
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_UPDATE_TASK_PROGRESS);
        }
    
    }

    public String getURLContent(String path, String method, String header, String httpVersion) throws Exception {
        return "";
    }

    //System.String result = "";
    //try
    //{
    //    result = m_ctcsBll.HTTPGetContent(path, method, header, httpVersion);
    //}
    //catch (System.Exception e)
    //{
    //    throw TCSException.produceTCSException(e, TCSException.BLL_ERROR_UPDATE_TASK_PROGRESS);
    //}
    public void checkStop() throws Exception {
        long elapsedTime = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - m_startTime;
        if (m_tcServer.getIsDataAggListing())
        {
            if (elapsedTime > MAXIMUM_EXECUTIVE_TIME_DATAAGG)
                throw TCSException.produceTCSException(TCSException.TCS_EXECUTIVE_TIMEOUT);
             
        }
        else
        {
            if (elapsedTime > MAXIMUM_EXECUTIVE_TIME)
                throw TCSException.produceTCSException(TCSException.TCS_EXECUTIVE_TIMEOUT);
             
        } 
    }

    public void setClient(TPAppRequest client) throws Exception {
        m_requestClient = client;
    }

    public void addMLSEngineNote(String note) throws Exception {
        m_requestClient.addXMLNote(TCSException.MLS_ENGINE_NOTE,note);
    }

    public void addXmlNote(int code, String note) throws Exception {
        if (m_requestClient != null)
            m_requestClient.addXMLNote(code,note);
         
    }

    public TCSBllWrapper getCtcsBll() throws Exception {
        getTCSBllWrapper();
        return m_ctcsBll;
    }

    public void closeCtcsBll() throws Exception {
        m_ctcsBll.Close();
        m_ctcsBll = null;
    }

}


