//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:43 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.JavaSupport.language.RefSupport;
import CS2JNet.System.DoubleSupport;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupGroup;
import net.toppro.components.mls.engine.ClientFactory;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.IPropertyFieldValidator;
import net.toppro.components.mls.engine.Log;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSRecordSet;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.MultiPictures;
import net.toppro.components.mls.engine.PropertyFieldGroup;
import net.toppro.components.mls.engine.PropertyFieldGroups;
import net.toppro.components.mls.engine.ScriptEngine;
import net.toppro.components.mls.engine.STRINGS;
import net.toppro.components.mls.engine.StringTokenizerEx;
import Tcs.Mls.MLSEnvironment;
import Tcs.Mls.TCSException;
import Tcs.Mls.TimeZoneInfoEx;
import Tcs.Mls.TPAppRequest;

public class MLSEngine   
{
    public static final String MLS_ENGINE_VERSION = "122";
    //+-----------------------------------------------------------------------------------+
    //|                                   Client types                                    |
    //+-----------------------------------------------------------------------------------+
    public static final int CLIENT_TYPE_UNKNOWN = -1;
    public static final int CLIENT_TYPE_HTTP_2 = 2;
    public static final int CLIENT_TYPE_HTTP_3 = 3;
    public static final int CLIENT_TYPE_TELNET = 4;
    public static final int CLIENT_TYPE_HTTP_5 = 5;
    public static final int CLIENT_TYPE_HTTP_6 = 6;
    public static final int CLIENT_TYPE_HTTP_7 = 7;
    public static final int CLIENT_TYPE_REINFOLINK = 8;
    public static final int CLIENT_TYPE_RETS = 9;
    public static final int CLIENT_TYPE_MRIS = 10;
    public static final int CLIENT_TYPE_GARDENSTATE = 11;
    public static final int CLIENT_TYPE_SAFEMLS = 12;
    public static final int CLIENT_TYPE_MRISRETS = 13;
    public static final int CLIENT_TYPE_REInfoLinkRETS = 14;
    public static final int CLIENT_TYPE_DEMO = 999;
    public static final int DEFAULT_MAX_RECORDS_LIMIT = 300;
    public static final boolean DEFAULT_OVERRIDE_ALLOWED = true;
    /**
    * Section [Fields]. This basically defines the mapping between any MLS board and TP.
    */
    public static final String SECTION_FIELDS = "Fields";
    /**
    * Section [FieldGroup]. Enumerates all the search screen that we need.
    */
    public static final String SECTION_FIELDGROUPS = "FieldGroups";
    /**
    * Section [Fields_]. Enumerates all the search fields for group.
    */
    public static final String SECTION_FIELDS_ = "Fields_";
    /**
    * Section [Field_]. Prefix for Field_BlaBla section.
    */
    public static final String SECTION_FIELD_ = "Field_";
    /**
    * Section [MainScript]. This section contains communication script to get MLS records.
    *  @see MLSScriptClient.
    */
    public static final String SECTION_MAINSCRIPT = "MainScript";
    /**
    * Section [PicScript]. This section contains communication script to get pictures.
    *  @see MLSScriptClient.
    */
    public static final String SECTION_PICSCRIPT = "PicScript";
    /**
    * Section [PicScript]. This section contains communication script to get pictures.
    *  @see MLSScriptClient.
    */
    public static final String SECTION_MPICSCRIPT = "PicScript1";
    /**
    * Section [RemScript]. This section contains communication script to get Notes/Remarks.
    */
    public static final String SECTION_REMSCRIPT = "RemScript";
    public static final String SECTION_RESULTSCRIPT = "ResultScript";
    /**
    * Section [MLSRecords] contains the description of MLS record fields.
    */
    public static final String SECTION_MLSRECORDS = "MLSRecords";
    /**
    * Section [MLSRecordsEx] contains the description of MLS record fields in new format(HeaderInfo).
    */
    private static final String SECTION_MLSRECORDSEX = "MLSRecordsEx";
    /**
    * Section [SecList] contains the list of security values used for login to MLS server.
    */
    public static final String SECTION_SECLIST = "SecList";
    /**
    * Section [Common] contains basic info about the board, like board name, vendor name, version,
    * client type, etc.
    */
    public static final String SECTION_COMMON = "Common";
    /**
    * Section [TcpIp] contains ip info - address and port.
    */
    public static final String SECTION_TCPIP = "TcpIp";
    /**
    * Section [Sorting] contains info allowing to filter recordset using listing type (current listing,
    * recent sale, pending sale, or expired listing).
    */
    public static final String SECTION_SORTING = "Sorting";
    public static final String SECTION_PUBLIC_SORTING = "SortingPLSNew";
    //"SortingPublicListingStatus";
    public static final String SECTION_PUBLIC_SORTING_Old_LOGIC = "SortingPublicListingStatus";
    /**
    * Section [Pictures] contains info about pictures format, record field corresponding to them, path,
    * etc...
    */
    public static final String SECTION_PICTURES = "Pictures";
    /**
    * Section [RcvData] contains the description of parsing attributes.
    */
    public static final String SECTION_RCVDATA = "RcvData";
    /**
    * Section [RcvDataAlt] contains the description of parsing attributes.
    */
    public static final String SECTION_RCVDATA_ALT = "RcvData_Alt";
    /**
    * Section [RcvDataExtra] contains the description of parsing attributes.
    */
    public static final String SECTION_RCVDATA_EXTRA = "RcvData_Extra";
    /**
    * Section [TPOnline] contains the specific data for iCMA.
    */
    public static final String SECTION_TPONLINE = "TPOnline";
    /**
    * Section [AutoSearch] contains mapping for auto search.
    */
    public static final String SECTION_AUTOSEARCH = "Standard_Search";
    /**
    * Section [TCS_Error] contains mapping for TCS error.
    */
    public static final String SECTION_TCSERROR = "TCS_Error";
    /**
    * Section [Fields_TCS] contains search fields which need hide from 6i and 7i.
    */
    public static final String SECTION_FIELD_TCS = "Fields_TCS";
    public static final String SECTION_LTS_TYPE = "LTSPType";
    public static final String SECTION_LTS_TYPE_RESULT = "LTSPTypeResults";
    public static final String SECTION_CHUNKING = "Advanced_Chunking";
    /**
    * Log mode. There are no any logging in runMainScript and runPictureScript functions
    * when this mode is set.
    */
    public static final int LOG_NO = 0;
    /**
    * Log mode. runMainScript and runPictureScript simply use application log (System.out)
    * when this mode is set.
    */
    public static final int LOG_APP_LOG = 1;
    /**
    * Log mode. runMainScript and runPictureScript write log to communication.log
    * file in the board results directory when this flag is set.
    */
    public static final int LOG_COMM_LOG = 2;
    public static final String ATTRIBUTE_IPADDRESS_DISABLED = "IPAddressDisabled";
    public static final String ATTRIBUTE_IPADDRESS = "IPAddress";
    public static final String ATTRIBUTE_DELASTDEL = "TcpIpStripLastRecDelimiter";
    private static final String ATTRIBUTE_IPPORT = "IPPort";
    public static final String ATTRIBUTE_TCPIP = "TcpIp";
    private static final String ATTRIBUTE_NAME_OF_ORIGIN = "NameOfOrigin";
    private static final String ATTRIBUTE_BOARDNAME = "BoardName";
    private static final String ATTRIBUTE_BOARDTYPE = "BoardType";
    private static final String ATTRIBUTE_VERSION = "Version";
    private static final String ATTRIBUTE_DATASOURCE = "DataSource";
    public static final String ATTRIBUTE_SORTFIELD = "SortField";
    public static final String ATTRIBUTE_PICDWNFLAG = "PicDwnFlag";
    public static final String ATTRIBUTE_DATAFILENAME = "DataFileName";
    public static final String ATTRIBUTE_PICIPADDRESS = "PicIpAddress";
    public static final String ATTRIBUTE_PICIPPORT = "PicIpPort";
    public static final String ATTRIBUTE_PICPREFIX = "PicPrefix";
    public static final String ATTRIBUTE_PICFILESEXT = "PicFilesExt";
    public static final String ATTRIBUTE_PICFILERULER = "PicFileRuler";
    public static final String ATTRIBUTE_PICRELFLDNAME = "PicRelFldName";
    public static final String ATTRIBUTE_RECORDDELIMITER = "RecordDelimiter";
    public static final String ATTRIBUTE_RECORDMARK = "RecordMark";
    public static final String ATTRIBUTE_STARTPROCESSINGSTRING = "StartProcessingString";
    public static final String ATTRIBUTE_ENDPROCESSINGSTRING = "EndProcessingString";
    public static final String ATTRIBUTE_DELLASTRECSTRING = "DelLastRecString";
    public static final String ATTRIBUTE_STARTHEADERSTRING = "StartHeaderString";
    public static final String ATTRIBUTE_ENDHEADERSTRING = "EndHeaderString";
    public static final String ATTRIBUTE_BASECOOKSITE = "BaseCookieSite";
    public static final String ATTRIBUTE_BASEPICSITE = "BasePicSite";
    public static final String ATTRIBUTE_MULTICONN = "MultipleConnection";
    public static final String ATTRIBUTE_FIELDDELIMITER = "FieldDelimiter";
    public static final String ATTRIBUTE_PICIMPFLAG = "PicImpFlag";
    public static final String ATTRIBUTE_PICMAXDOWNLOAD = "PicMaxDownload";
    public static final String ATTRIBUTE_DATA_IMPORT = "DataImport";
    //
    public static final String ATTRIBUTE_DATA_FLAG = "DataFlag";
    //
    public static final String ATTRIBUTE_HELPER_ZIP = "HelperZip";
    //
    public static final String ATTRIBUTE_AMBIANCE_SETUP_FILE = "InstCSSFile";
    public static final String ATTRIBUTE_TPONLINE_NOTE = "TPONote";
    public static final String ATTRIBUTE_DEFAULT = "Default";
    public static final String ATTRIBUTE_VALUE = "Value";
    public static final String ATTRIBUTE_MLSRECORDSEX = "MLSRecordsEx";
    public static final String ATTRIBUTE_HEADERINFOFILE = "HeaderInfoFile";
    public static final String ATTRIBUTE_HEADERDELIMITER = "HeaderDelimiter";
    public static final String ATTRIBUTE_METADATAVERSION = "MetaDataVersion";
    public static final String ATTRIBUTE_GETMETADATA = "GetMetaData";
    public static final String ATTRIBUTE_GETMETADATATYPE = "GetMetaDataType";
    public static final String ATTRIBUTE_RETSUAPWD = "RetsUAPwd";
    public static final String ATTRIBUTE_RETSUAAGENT = "RetsUAUserAgent";
    public static final String ATTRIBUTE_HTTPAGENT = "HttpUserAgent";
    public static final String ATTRIBUTE_HTTPVERSION = "HttpVersion";
    public static final String ATTRIBUTE_DELIMITER = "Delimiter";
    public static final String ATTRIBUTE_RETSVERSION = "RetsVersion";
    public static final String ATTRIBUTE_PICZIP = "PicZip";
    public static final String ATTRIBUTE_MPICFORMAT = "MpicFormat";
    public static final String ATTRIBUTE_DATEFORMAT = "DateFormat";
    public static final String ATTRIBUTE_STATUSDATE = "ST_StatusDate";
    public static final String ATTRIBUTE_SEARCHPRICE = "ST_SearchPrice";
    public static final String ATTRIBUTE_MLSNUM = "ST_MLSNo";
    public static final String ATTRIBUTE_MPICCOMPARE = "MPicCompare";
    public static final String ATTRIBUTE_MAXIMUMLISINGNUM = "MaximumParseListings";
    public static final String ATTRIBUTE_MPICMAX = "MpicMax";
    public static final String ATTRIBUTE_SETUP_LOCAL = "LoginParaSavedLocal";
    //Add Aug 8,2005
    public static final String ATTRIBUTE_DISCLAIMER = "Disclaimer";
    //Add 08222005
    public static final String ATTRIBUTE_TCSRCOUNT = "TCSRecCount";
    //Add 08222005
    public static final String ATTRIBUTE_TCSERRORSTRING = "String";
    public static final String ATTRIBUTE_TCSERRORCODE = "Code";
    public static final String ATTRIBUTE_TPOPASSPROMPT = "TPOPassPrompt";
    public static final String ATTRIBUTE_LABELPASS = "Label_PASS";
    public static final String ATTRIBUTE_USERELATIVEPATH = "UseRelativePath";
    public static final String ATTRIBUTE_IMPORTDATAUNICODE = "ImportDataUnicode";
    public static final String ATTRIBUTE_MAXRECORDSLIMIT = "MaxRecordsLimit";
    public static final String ATTRIBUTE_OVERRIDEALLOWED = "OverrideRecLimitAllowed";
    public static final String ATTRIBUTE_DAYSINCHUNK = "DaysInChunk";
    public static final String ATTRIBUTE_MAXAGENTOFFICEID = "MaxAgentOfficeID";
    public static final String ATTRIBUTE_MAXDEMORECORDS8I = "MaxDemoRecords8i";
    public static final String ATTRIBUTE_MRISPRIMARYPICTYPE = "MRISPrimaryPicType";
    public static final String ATTRIBUTE_MASKPASSWORD = "MaskPassword";
    public static final String ATTRIBUTE_HTTPMETHOD = "HttpMethod";
    public static final String ATTRIBUTE_HTTPCONNECTION = "HttpConnection";
    public static final String ATTRIBUTE_LASTMODIFIDATE = "ST_LastMod";
    public static final String ATTRIBUTE_PICLASTMODIFIDATE = "ST_PicMod";
    public static final String ATTRIBUTE_CONDITIONCODE = "ST_ConditionCode";
    public static final String ATTRIBUTE_MLSRESULTSTIMEZONE = "MLSResultsTimeZone";
    public static final String ATTRIBUTE_MLSSEARCHTIMEZONE = "MLSSearchTimeZone";
    public static final String ATTRIBUTE_STANDARDTIMEZONE = "StandardTimeZone";
    public static final String ATTRIBUTE_DEFNOTAVAILABLETO = "DEFNotAvailableTo";
    public static final String ATTRIBUTE_DATAAGGMETADATACONFIG = "DataAggMetadataConfig";
    public static final String ATTRIBUTE_TCPIP_PICTURE = "TCPIP_PIC";
    public static final String ATTRIBUTE_RETRYAUTHERRORS = "RetryFailedRequests";
    public static final String ATTRIBUTE_SKIPACTIONURL = "SkipGetActionURL";
    public static final String ATTRIBUTE_RETRYAUTHERRORSWAIT = "RetryFailedRequestsWait";
    public static final String ATTRIBUTE_PROPERTYRESOURCE = "PropertyResource";
    public static final String ATTRIBUTE_AGENTRESOURCE = "AgentResource";
    public static final String ATTRIBUTE_OFFICERESOURCE = "OfficeResource";
    public static final String ATTRIBUTE_OPENHOUSERESOURCE = "OpenHouseResource";
    public static final String ATTRIBUTE_RETSWAIT = "RETSWait";
    public static final String ATTRIBUTE_GETALLPICTURESINONEREQUEST = "GetAllPicturesInOneRequest";
    public static final String ATTRIBUTE_MAXRECORDSIDSLIMIT = "MaxRecordsIDsLimit";
    public static final String ATTRIBUTE_SEARCHIDSLIMIT = "SearchIDsLimit";
    public static final String ATTRIBUTE_BOARDSEARCHTYPE = "SearchType";
    public static final String ATTRIBUTE_OPENHOUSECLASSES = "OpenHouseClasses";
    /**
    * Main script flag. Forces main script to add data to previous search results rather than
    * overwriting them.
    */
    public static final int MSFLAG_ADD_TO_PREV = 0x0001;
    /**
    * Main script flag. Forces main script not to execute picture script even if CommunicationClient
    * requires it. If MSFLAG_RUN_PIC_SCRIPT is set at the same time - MSFLAG_NO_PIC_SCRIPT
    * wins.
    */
    public static final int MSFLAG_NO_PIC_SCRIPT = 0x0002;
    /**
    * Main script flag. Forces main script to execute picture script even if CommunicationClient
    * does not require it. If MSFLAG_NO_PIC_SCRIPT is set at the same time - MSFLAG_NO_PIC_SCRIPT
    * wins.
    */
    public static final int MSFLAG_RUN_PIC_SCRIPT = 0x0004;
    /**
    * Main script flag. Forces main script not to execute rem script (downloading notes) even if
    * CommunicationClient requires it. If MSFLAG_RUN_REM_SCRIPT is set at the same time -
    * MSFLAG_NO_REM_SCRIPT wins.
    */
    public static final int MSFLAG_NO_REM_SCRIPT = 0x0008;
    /**
    * Main script flag. Forces main script to execute rem script (downloading notes) even if
    * CommunicationClient does not require it. If MSFLAG_NO_REM_SCRIPT is set at the same time -
    * MSFLAG_NO_REM_SCRIPT wins.
    */
    public static final int MSFLAG_RUN_REM_SCRIPT = 0x0010;
    public static final int MSFLAG_METADATA_ONLY = 0x0020;
    public static final int MSFLAG_DATAAGG_METADATA_ONLY = 0x0040;
    public static final int MSFLAG_NO_EXREM_SCRIPT = 0x0007;
    public static final int MSFLAG_RUN_EXREM_SCRIPT = 0x0009;
    //UPGRADE_NOTE: Final was removed from the declaration of 'ATTRIBUTE_SORTING_TYPE'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] ATTRIBUTE_SORTING_TYPE = new String[]{ "Active", "Sold", "Pending", "Expired" };
    public static final String[] ATTRIBUTE_PUBLIC_SORTING_TYPE = new String[]{ "Active", "Sold", "OffMarketOrOther" };
    public static final int DEFAULT_PORT_NUMBER = 80;
    // when this variable is null no_picture filename is not used.
    // Sergei: I just didn't want to remove no_picture logic from the code.
    // I have a feeling, we'll need it some day.
    // To enable no_picture images, just specify filename here. Everything will
    // start working automatically, including UI.
    // !!!!!
    // See comments for Environment.getServerRoot() - this part should be redesigned.
    // !!!!!
    private static final String NO_PICTURE_FILENAME = null;
    //"nophoto.jpg";
    private static final String SERVER_MLS_FOLDER = "Mls";
    private static final String MLS_FOLDER = "MLS";
    private static final String RESULTS_FOLDER = "results";
    private static final String CACHE_FOLDER = "cache";
    public static final String RECORDS_FILE = "@@recs@@.dat";
    public static final String RESULTS_FILE = "@@grid@@.dat";
    public static final String FIELDS_FILE = "@@fields@@.dat";
    private static final String BIN_FOLDER = "bin";
    public static final String ENGINE_FILE = "@@engine@@.dat";
    private static final String INSTALL_EXE_FILE = "icmainstall.exe";
    private static final String INSTALL_LOG_FILE = "icmainstall.log";
    public static final String IMPORT_PICTURE_CHECKBOX = "@@importpicturecheckbox@@:";
    private static final String DemoFloder = "\\TOPPDEMO\\";
    private static final String DefaultTimeZone = "(GMT) Greenwich Mean Time";
    private TimeZoneInfoEx m_mlsSearchTimeZone = null;
    private TimeZoneInfoEx m_mlsResultsTimeZone = null;
    private TimeZoneInfoEx m_GMTTimeZone = null;
    private boolean m_isSearchTimeZoneExist = false;
    private boolean m_isResultTimeZoneExist = false;
    private DefParser m_DefParser;
    // Parsed DEF-file
    private DefParser m_LookUpTable = null;
    // Parsed LookUp Table
    private MLSCmaFields m_CmaFields;
    // CmaFields from [MLSRecords]-section
    private MLSCmaFields m_CmaFieldsEx;
    // CmaFields from [MLSRecordsEx]-section
    private BoardSetup m_boardSetup;
    // Passwords
    private MLSRecordSet m_MlsRecordset;
    // Our Records - result
    private Environment m_environment;
    private net.toppro.components.mls.engine.CommunicationClient m_commClient;
    // Current Client for board
    private PropertyFieldGroups m_PropertyFieldGroups = null;
    private MultiPictures m_multiPictures = null;
    private Log m_log = new Log();
    private int m_logMode = LOG_NO;
    private String m_moduleResultsFolder = null;
    private String m_strFieldsFile = "";
    private Hashtable m_Params = null;
    // This is ok that they are static. These variavbles should have the same value for all
    // instances of TPOnline, so it's not necessary to put them into the Framework.
    private static String m_mls_folder = null;
    private static String m_bin_folder = null;
    private static String m_noPictureFilename = null;
    private static boolean m_noPictureFilenameInitialized = false;
    private static final String SERVER_BIN_PATH = "MLS/";
    private static final int OVERAL_PROGRESS_MAX_VALUE = 10000;
    private int m_progressStartValue = 0;
    private int m_progressFinishValue = OVERAL_PROGRESS_MAX_VALUE;
    private static final int DEFAULT_DAYS_IN_CHUNK = 5;
    /// <summary> These strings help to identify type of the result record (current listing, expired listing,
    /// recent sale, or pending). See functions MLSEngine.getTypeStrings() and MLSRecord.initType()
    /// to understand the usage. Note that they are initialized in MLSEngine.parseDefFile().
    /// <p>We cache these strings to avoid parsing each time the record is created.
    /// </summary>
    private String[][] m_typeStrings = null;
    private String[][] m_publicTypeStrings = null;
    //For the feature of remember the user setting of Import Picture Check box
    private static boolean m_importPictureChecked = false;
    //Used for TCS only to put results in different folder for each status
    private String m_currentSearchStatus;
    private String m_retryNumber = "";
    private int m_recordsLimit = DEFAULT_MAX_RECORDS_LIMIT;
    private String m_agentID = "";
    private String m_officeID = "";
    private boolean m_isRecordCount = false;
    public enum EngineType
    {
        NormalEngine,
        AgentEngine,
        OfficeEngine
    }
    private net.toppro.components.mls.engine.MLSEngine.EngineType m_engineType = net.toppro.components.mls.engine.MLSEngine.EngineType.NormalEngine;
    private int m_maxAgentOfficeID = 0;
    private int m_engineFlags = 0x0000;
    private String m_mlsMetadataVersion = "";
    private String m_clientType = "";
    private int m_retryAuthErrors = -1;
    private int m_retryAuthErrorsWait = -1;
    private int m_skipActionURL = -1;
    private int m_retsWait = -1;
    private int m_getAllPicsInOneRequest = -1;
    private String m_searchType = null;
    private String m_openhouseClasses = null;
    private String m_returnIDsOnlyFieldList = null;
    public String getReturnIDsOnlyFieldList() throws Exception {
        if (m_returnIDsOnlyFieldList == null)
        {
            m_returnIDsOnlyFieldList = getCmaFields().getFieldNamesForReturnIDsOnly();
        }
         
        return m_returnIDsOnlyFieldList;
    }

    public String getReturnMlsNumberOnlyFieldName() throws Exception {
        return getCmaFields().getFieldNameForReturnMlsNumberOnly();
    }

    private String m_currentOpenHouseClass = "";
    public String getCurrentOpenHouseClass() throws Exception {
        return m_currentOpenHouseClass;
    }

    public void setCurrentOpenHouseClass(String value) throws Exception {
        m_currentOpenHouseClass = value;
    }

    public String getSearchType() throws Exception {
        if (m_searchType == null)
        {
            m_searchType = StringSupport.Trim(m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_BOARDSEARCHTYPE));
        }
         
        return m_searchType;
    }

    private String __PropertyClassNameFromMainScript;
    public String getPropertyClassNameFromMainScript() {
        return __PropertyClassNameFromMainScript;
    }

    public void setPropertyClassNameFromMainScript(String value) {
        __PropertyClassNameFromMainScript = value;
    }

    private int m_maxRecordsIDsLimit = -1;
    public int getMaxRecordsIDsLimit() throws Exception {
        if (m_maxRecordsIDsLimit == -1)
        {
            String s = StringSupport.Trim(m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MAXRECORDSIDSLIMIT));
            m_maxRecordsIDsLimit = 0;
            if (!StringSupport.isNullOrEmpty(s))
            {
                try
                {
                    m_maxRecordsIDsLimit = Integer.valueOf(s);
                }
                catch (Exception e)
                {
                }
            
            }
             
        }
         
        return m_maxRecordsIDsLimit;
    }

    private int m_searchIDsLimit = -1;
    public int getSearchIDsLimit() throws Exception {
        if (m_searchIDsLimit == -1)
        {
            String s = StringSupport.Trim(m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_SEARCHIDSLIMIT));
            m_searchIDsLimit = 0;
            if (!StringSupport.isNullOrEmpty(s))
            {
                try
                {
                    m_searchIDsLimit = Integer.valueOf(s);
                }
                catch (Exception e)
                {
                }
            
            }
             
            if (m_searchIDsLimit == 0)
                m_searchIDsLimit = 300;
             
        }
         
        return m_searchIDsLimit;
    }

    private int m_maxRecordsLimit = -1;
    private boolean m_isDownloadIDs = false;
    public boolean getIsDownloadIDs() throws Exception {
        return m_isDownloadIDs;
    }

    public void setIsDownloadIDs(boolean value) throws Exception {
        m_isDownloadIDs = value;
    }

    public String getMlsMetadataVersion() throws Exception {
        return m_mlsMetadataVersion;
    }

    public void setMlsMetadataVersion(String value) throws Exception {
        m_mlsMetadataVersion = value;
    }

    public int getEngineFlags() throws Exception {
        return m_engineFlags;
    }

    public void setEngineFlags(int value) throws Exception {
        m_engineFlags = value;
    }

    private ScriptEngine m_scriptEngine = null;
    private Object m_scriptEngineAssembly = null;
    private TPAppRequest m_tpAppRequest = null;
    public TPAppRequest getTpAppRequest() throws Exception {
        return m_tpAppRequest;
    }

    public void setTpAppRequest(TPAppRequest value) throws Exception {
        m_tpAppRequest = value;
    }

    private HashMap<String,String> m_paramDict = new HashMap<String,String>();
    private String m_SectionExtraVarScript = "";
    public String getSectionExtraVarScript() throws Exception {
        return m_SectionExtraVarScript;
    }

    public void setSectionExtraVarScript(String value) throws Exception {
        m_SectionExtraVarScript = value;
    }

    private String m_RETSUAPwd = "";
    public String getRETSUAPwd() throws Exception {
        return m_RETSUAPwd;
    }

    public void setRETSUAPwd(String value) throws Exception {
        m_RETSUAPwd = value;
    }

    private String m_UserAgent = "";
    public String getUserAgent() throws Exception {
        return m_UserAgent;
    }

    public void setUserAgent(String value) throws Exception {
        m_UserAgent = value;
    }

    private int m_BypassAgentRosterAuth = -1;
    public int getBypassAgentRosterAuth() throws Exception {
        return m_BypassAgentRosterAuth;
    }

    public void setBypassAgentRosterAuth(int value) throws Exception {
        m_BypassAgentRosterAuth = value;
    }

    private int m_IncludingTlBlobField = -1;
    public int getIncludingTlBlobField() throws Exception {
        return m_IncludingTlBlobField;
    }

    public void setIncludingTlBlobField(int value) throws Exception {
        m_IncludingTlBlobField = value;
    }

    private int m_UseCompactFormat = -1;
    public int getUseCompactFormat() throws Exception {
        return m_UseCompactFormat;
    }

    public void setUseCompactFormat(int value) throws Exception {
        m_UseCompactFormat = value;
    }

    private HashMap<String,Integer> m_FieldListForRetsBlob = null;
    public MLSEngine(Environment environment, TPAppRequest tpAppRequest) throws Exception {
        m_environment = environment;
        m_tpAppRequest = tpAppRequest;
        boolean cache = (environment.getFlags() & Environment.FLAG_NO_LOCAL_CACHE) == 0;
        if (cache)
        {
            boolean ok = false;
            String cacheFilename = getCacheFileName();
            File file = new File(cacheFilename);
            try
            {
                //if ( file.exists() )
                //
                load(cacheFilename);
                ok = true;
            }
            catch (Exception e)
            {
                writeLine("Warning: Failed to load MLS engine from local cache");
            }

            //}
            if (!ok)
            {
                parseDefFile(environment.downloadDefFile());
                try
                {
                    save(cacheFilename);
                }
                catch (Exception exc)
                {
                    writeLine("Warning: Failed to save MLS engine to local cache");
                }
            
            }
             
        }
        else
            parseDefFile(environment.downloadDefFile()); 
    }

    public MLSEngine(Environment environment, net.toppro.components.mls.engine.MLSEngine.EngineType engineType, String defPath, TPAppRequest tpAppRequest) throws Exception {
        m_environment = environment;
        m_tpAppRequest = tpAppRequest;
        m_engineType = engineType;
        boolean cache = (environment.getFlags() & Environment.FLAG_NO_LOCAL_CACHE) == 0;
        if (cache)
        {
            boolean ok = false;
            String cacheFilename = getCacheFileName();
            File file = new File(cacheFilename);
            try
            {
                //if ( file.exists() )
                //
                load(cacheFilename);
                ok = true;
            }
            catch (Exception e)
            {
                writeLine("Warning: Failed to load MLS engine from local cache");
            }

            //}
            if (!ok)
            {
                parseDefFile(MLSUtil.readFile2String(defPath).toString());
                try
                {
                    save(cacheFilename);
                }
                catch (Exception exc)
                {
                    writeLine("Warning: Failed to save MLS engine to local cache");
                }
            
            }
             
        }
        else
            parseDefFile(MLSUtil.readFile2String(defPath).toString()); 
    }

    public MLSEngine(Environment environment, String def_file, TPAppRequest tpAppRequest) throws Exception {
        m_environment = environment;
        m_tpAppRequest = tpAppRequest;
        parseDefFile(def_file);
        if ((environment.getFlags() & (Environment.FLAG_NO_LOCAL_CACHE | Environment.FLAG_CACHE_ON_EXPLICIT_DEF_FILE_SET)) == Environment.FLAG_CACHE_ON_EXPLICIT_DEF_FILE_SET)
        {
            try
            {
                save(getCacheFileName());
            }
            catch (Exception exc)
            {
                writeLine("Warning: Failed to save MLS engine to local cache");
            }
        
        }
         
    }

    public MLSEngine(String def_file, TPAppRequest tpAppRequest) throws Exception {
        m_environment = new MLSEnvironment(null);
        m_tpAppRequest = tpAppRequest;
        parseDefFile(def_file);
    }

    /**
    * Environment object is an implementation of Environment object.
    * MLSEngine uses this interface to communicate with the program. This makes the
    * dependency between MLSEngine and a rest of an application weaker.
    * Application must implement Environment to create an instance of MLSEngine.
    * 
    *  @return  the environment object.
    */
    public Environment getEnvironment() throws Exception {
        return m_environment;
    }

    public net.toppro.components.mls.engine.CommunicationClient getCommunicationClient() throws Exception {
        return m_commClient;
    }

    public int getRecordsLimit() throws Exception {
        return m_recordsLimit;
    }

    public void setRecordsLimit(int value) throws Exception {
        m_recordsLimit = value;
    }

    public String getAgentID() throws Exception {
        return m_agentID;
    }

    public void setAgentID(String value) throws Exception {
        m_agentID = value;
    }

    public String getOfficeID() throws Exception {
        return m_officeID;
    }

    public void setOfficeID(String value) throws Exception {
        m_officeID = value;
    }

    public int getMaxAgentOfficeID() throws Exception {
        if (m_maxAgentOfficeID > 0)
            return m_maxAgentOfficeID;
         
        String s = StringSupport.Trim(m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MAXAGENTOFFICEID));
        m_maxAgentOfficeID = 20;
        if (!StringSupport.isNullOrEmpty(s))
        {
            try
            {
                m_maxAgentOfficeID = Integer.valueOf(s);
            }
            catch (Exception e)
            {
            }
        
        }
         
        return m_maxAgentOfficeID;
    }

    public String getMRISPrimaryPicType() throws Exception {
        String s = StringSupport.Trim(m_DefParser.getValue(SECTION_PICTURES,ATTRIBUTE_MRISPRIMARYPICTYPE));
        if (StringSupport.isNullOrEmpty(s))
        {
            s = "";
        }
         
        return s;
    }

    public int getMaxDemoRecords() throws Exception {
        String s = StringSupport.Trim(m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MAXDEMORECORDS8I));
        int max = 50;
        if (!StringSupport.isNullOrEmpty(s))
        {
            try
            {
                max = Integer.valueOf(s);
            }
            catch (Exception e)
            {
            }
        
        }
         
        return max;
    }

    public HashMap<String,String[]> getResultScriptMethodList() throws Exception {
        if (m_scriptEngine == null)
            createScriptEngine();
         
        if (m_scriptEngine == null)
            return null;
         
        return m_scriptEngine.getMethodInfo(m_scriptEngineAssembly);
    }

    public String callScriptMethod(String methodName, String[] methodParams) throws Exception {
        if (m_scriptEngine == null)
            createScriptEngine();
         
        Object result = m_scriptEngine.callMethod(m_scriptEngineAssembly,methodName,methodParams);
        return (String)result;
    }

    public String getEngineParameter(String fieldName) throws Exception {
        String result = "";
        fieldName = fieldName.toUpperCase();
        if (fieldName.startsWith("ENGINE_"))
        {
            String __dummyScrutVar0 = fieldName;
            if (__dummyScrutVar0.equals("ENGINE_CONDITIONCODE"))
            {
                result = m_tpAppRequest.m_connector.getConditionCode();
            }
             
        }
        else if (fieldName.startsWith("PARAM_"))
        {
            if (m_paramDict.containsKey(fieldName))
                result = m_paramDict.get(fieldName);
             
        }
          
        return result;
    }

    public void setEngineParameter(String fieldName, String value) throws Exception {
        fieldName = fieldName.toUpperCase();
        if (fieldName.startsWith("PARAM_"))
        {
            if (m_paramDict.containsKey(fieldName))
            {
                m_paramDict.put(fieldName, value);
            }
            else
            {
                m_paramDict.put(fieldName, value);
            } 
        }
         
    }

    public void addParsingRawDataTime(long parsingTime) throws Exception {
        m_tpAppRequest.setParsingRawDataTime(m_tpAppRequest.getParsingRawDataTime() + parsingTime);
    }

    private void createScriptEngine() throws Exception {
        if (m_scriptEngine == null)
        {
            String content = m_DefParser.getResultScriptContent();
            if (!StringSupport.isNullOrEmpty(content))
            {
                m_scriptEngine = new ScriptEngine(this);
                String cacheFolder = "";
                String version = "0";
                try
                {
                    cacheFolder = getCacheScriptEngineFileName();
                    version = m_environment.getDefFileVersion();
                }
                catch (Exception e)
                {
                }

                boolean success = false;
                try
                {
                    success = m_scriptEngine.compileAssembly(m_DefParser.getResultScriptContent(),cacheFolder,makeSafeFolderName(version));
                }
                catch (Exception exc)
                {
                    version = "0";
                    success = m_scriptEngine.compileAssembly(m_DefParser.getResultScriptContent(),cacheFolder,makeSafeFolderName(version));
                    writeLine(exc.getMessage());
                }

                if (!success)
                    throw new MLSException(null,MLSException.CODE_DEF_PARSING_ERROR,m_scriptEngine.cErrorMsg,"",MLSException.FORMAT_TEXT);
                 
            }
            else
                return ; 
            m_scriptEngineAssembly = m_scriptEngine.createInstance();
        }
         
    }

    public void writeLine(String line) throws Exception {
        if (getLogMode() == LOG_COMM_LOG)
            m_log.writeLine(line);
        else
            m_environment.writeLine(line); 
    }

    public void writeLine(String line, boolean isCompactCommnicationLog) throws Exception {
        if (getLogMode() == LOG_COMM_LOG)
            m_log.writeLine(line);
        else
            m_environment.writeLine(line,isCompactCommnicationLog); 
    }

    public void writeLine() throws Exception {
    }

    /**
    * @return  the LookUp Table
    */
    public DefParser getLookUpTable() throws Exception {
        try
        {
            if (m_LookUpTable == null)
            {
                m_LookUpTable = new DefParser();
                m_LookUpTable.parseBuffer(m_environment.downloadLookupTable());
            }
             
        }
        catch (Exception e)
        {
            m_LookUpTable = null;
            writeLine("MLSEngine: cannot load Lookup Table");
        }

        return m_LookUpTable;
    }

    /**
    * @return  DEF-Parser. DEF-Parser is an object containing binary (parsed) data from the def-file.
    */
    public DefParser getDefParser() throws Exception {
        return m_DefParser;
    }

    /**
    * @return  Object containing resulting recordset columns.
    */
    public MLSCmaFields getCmaFields() throws Exception {
        if (m_CmaFieldsEx != null && m_CmaFieldsEx.size() != 0)
            return m_CmaFieldsEx;
         
        return m_CmaFields;
    }

    /**
    * 
    */
    public boolean isUseMLSRecordsEx() throws Exception {
        if (m_CmaFieldsEx != null && m_CmaFieldsEx.size() != 0)
            return true;
         
        return false;
    }

    public String getOpenHouseClasses() throws Exception {
        if (m_openhouseClasses == null)
        {
            m_openhouseClasses = m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_OPENHOUSECLASSES);
        }
         
        return m_openhouseClasses;
    }

    public boolean isSkipActionURL() throws Exception {
        if (m_skipActionURL == -1)
        {
            if (m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_SKIPACTIONURL).equals("1"))
                m_skipActionURL = 1;
            else
                m_skipActionURL = 0; 
        }
         
        return m_skipActionURL == 1;
    }

    public int getRETSWait() throws Exception {
        if (m_retsWait == -1)
        {
            String res = m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_RETSWAIT);
            if (StringSupport.isNullOrEmpty(res))
                m_retsWait = 0;
            else
            {
                RefSupport<Integer> refVar___0 = new RefSupport<Integer>();
                DoubleSupport.tryParse(res, refVar___0);
                m_retsWait = refVar___0.getValue();
            } 
        }
         
        return m_retsWait;
    }

    public boolean isGetAllPicsInOneRequest() throws Exception {
        //return false;
        if (m_getAllPicsInOneRequest == -1)
        {
            if (m_DefParser.getValue(SECTION_PICTURES,ATTRIBUTE_GETALLPICTURESINONEREQUEST).equals("0"))
                m_getAllPicsInOneRequest = 0;
            else
                m_getAllPicsInOneRequest = 1; 
        }
         
        return m_getAllPicsInOneRequest == 1;
    }

    public int getRetryAuthErrorsWait() throws Exception {
        if (m_retryAuthErrorsWait == -1)
        {
            String retry = m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_RETRYAUTHERRORSWAIT);
            if (StringSupport.isNullOrEmpty(retry))
                m_retryAuthErrorsWait = 0;
            else
            {
                int result;
                RefSupport<Integer> refVar___1 = new RefSupport<Integer>();
                DoubleSupport.tryParse(retry, refVar___1);
                result = refVar___1.getValue();
                m_retryAuthErrorsWait = result;
            } 
        }
         
        return m_retryAuthErrorsWait;
    }

    public int getRetryAuthErrorsNumber() throws Exception {
        if (m_retryAuthErrors == -1)
        {
            String retry = m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_RETRYAUTHERRORS);
            if (StringSupport.isNullOrEmpty(retry))
                m_retryAuthErrors = 0;
            else
            {
                int result;
                RefSupport<Integer> refVar___2 = new RefSupport<Integer>();
                DoubleSupport.tryParse(retry, refVar___2);
                result = refVar___2.getValue();
                m_retryAuthErrors = result;
            } 
        }
         
        return m_retryAuthErrors;
    }

    /**
    * @return  Object containing resulting recordset columns in new format(with header info).
    * 
    *  @return  object containing security values description.
    */
    public BoardSetup getBoardSetup() throws Exception {
        return m_boardSetup;
    }

    /**
    * @return  object containing property search fields.
    */
    public PropertyFieldGroup getPropertyFields() throws Exception {
        return getCurrentPropertyFieldGroup();
    }

    public void addPropertyField(net.toppro.components.mls.engine.PropertyField field) throws Exception {
        PropertyFieldGroup group = getCurrentPropertyFieldGroup();
        if (group == null)
        {
            group = m_PropertyFieldGroups.createEmptyGroup();
            m_PropertyFieldGroups.setCurrentGroup(0);
            m_PropertyFieldGroups.setDefaulGroupIndex(0);
        }
         
        group.addField(field);
    }

    /**
    * @return  def-file version.
    */
    public String getDefVersion() throws Exception {
        if (m_DefParser == null)
            return null;
         
        return StringSupport.Trim(m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_VERSION));
    }

    public String getRETSUAPwd() throws Exception {
        if (StringSupport.isNullOrEmpty(getRETSUAPwd()))
            setRETSUAPwd(m_environment.getRETSUAPwd());
         
        return getRETSUAPwd();
    }

    public String getUserAgent() throws Exception {
        if (StringSupport.isNullOrEmpty(getUserAgent()))
            setUserAgent(m_environment.getUserAgent());
         
        return getUserAgent();
    }

    /**
    * @return  MLS records iterator.
    */
    public MLSRecords getMLSRecords(int filter, IPropertyFieldValidator validator) throws Exception {
        return new MLSRecords(m_MlsRecordset,filter,validator);
    }

    /**
    * @return  MLS records iterator.
    */
    public MLSRecords getMLSRecords(int filter) throws Exception {
        return new MLSRecords(m_MlsRecordset,filter,null);
    }

    /**
    * @param id record id (MLS number)
    * 
    *  @return  MLS record with the specified id (MLS number), or null if not found.
    */
    public MLSRecord getMLSRecord(String id) throws Exception {
        return m_MlsRecordset.getRecord(id);
    }

    /**
    * @return  MLS record count.
    */
    public int getMLSRecordCount() throws Exception {
        return m_MlsRecordset.getRecordCount();
    }

    /**
    * Adds new record to MLS recordset.
    *  @param record new record.
    */
    public void addMLSRecord(MLSRecord record) throws Exception {
        m_MlsRecordset.addRecord(record);
    }

    /**
    * @return  true if the file with the previous search results exists.
    */
    public boolean hasPrevResults() throws Exception {
        boolean result = false;
        try
        {
            boolean tmpBool;
            if (File.Exists((new File(getRecordsFilename())).FullName))
                tmpBool = true;
            else
                tmpBool = File.Exists((new File(getRecordsFilename())).FullName); 
            result = tmpBool;
        }
        catch (Exception e)
        {
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            writeLine(e.getMessage());
        }

        return result;
    }

    /**
    * @return  Port number. This is applicable only for the boards which use tcp/ip.
    */
    public int getPort() throws Exception {
        int port = DEFAULT_PORT_NUMBER;
        if (m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_IPADDRESS_DISABLED).equals("1"))
        {
            String str = m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_IPPORT);
            if (str.length() == 0)
            {
                port = DEFAULT_PORT_NUMBER;
            }
            else
            {
                try
                {
                    port = Integer.valueOf(str);
                }
                catch (Exception e)
                {
                    notifyTechSupport(SUPPORT_CODE_BAD_PORT_VALUE,str);
                    throw createException(e,STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_GET_PORT));
                }
            
            } 
        }
        else
        {
            try
            {
                port = Integer.valueOf(m_boardSetup.getIPPort());
            }
            catch (Exception e)
            {
                throw createException(e,STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_GET_PORT));
            }
        
        } 
        if (port < 0 || port >= 65535)
        {
            notifyTechSupport(SUPPORT_CODE_PORT_VALUE_TOO_BIG,"" + port);
            throw createException(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_PORT_RANGE));
        }
         
        return port;
    }

    /**
    * @return  IP address. This is applicable only for the boards which use tcp/ip.
    */
    public String getIPAddress() throws Exception {
        if (m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_IPADDRESS_DISABLED).equals("1"))
            return m_DefParser.getValue(SECTION_TCPIP,ATTRIBUTE_IPADDRESS);
         
        return m_boardSetup.getIPAddress();
    }

    /**
    * @return  max count of download pictures for selecting int the grid
    */
    public int getPictureMaxDownload() throws Exception {
        int MaxDownload = 0;
        String PictureMaxDownload = m_DefParser.getValue(SECTION_PICTURES,ATTRIBUTE_PICMAXDOWNLOAD);
        if (PictureMaxDownload.length() == 0)
            return MaxDownload;
         
        try
        {
            MaxDownload = Integer.valueOf(PictureMaxDownload);
        }
        catch (Exception e)
        {
            MaxDownload = 0;
        }

        return MaxDownload;
    }

    private boolean isBypassTimeZone() throws Exception {
        if (m_tpAppRequest == null)
            return false;
        else
            return m_tpAppRequest.m_connector.isBypassTimezone(); 
    }

    public boolean isReturnIdsOnly() throws Exception {
        if (m_tpAppRequest == null)
            return false;
        else
            return m_tpAppRequest.m_connector.isReturnIdsOnly(); 
    }

    public TimeZoneInfoEx getMLSSearchTimeZone() throws Exception {
        if (m_isSearchTimeZoneExist)
            return m_mlsSearchTimeZone;
         
        String timeZone = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MLSSEARCHTIMEZONE);
        if (StringSupport.isNullOrEmpty(timeZone) || StringSupport.Compare(timeZone, "z", true) == 0 || isBypassTimeZone())
            m_mlsSearchTimeZone = getGMTTimeZone();
        else
            m_mlsSearchTimeZone = TimeZoneInfoEx.findTimeZone(timeZone); 
        m_isSearchTimeZoneExist = true;
        return m_mlsSearchTimeZone;
    }

    public TimeZoneInfoEx getMLSResultsTimeZone() throws Exception {
        if (m_isResultTimeZoneExist)
            return m_mlsResultsTimeZone;
         
        String timeZone = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MLSRESULTSTIMEZONE);
        if (StringSupport.isNullOrEmpty(timeZone) || StringSupport.Compare(timeZone, "z", true) == 0 || isBypassTimeZone())
            m_mlsResultsTimeZone = getGMTTimeZone();
        else
            m_mlsResultsTimeZone = TimeZoneInfoEx.findTimeZone(timeZone); 
        m_isResultTimeZoneExist = true;
        return m_mlsResultsTimeZone;
    }

    public String getTimeZoneDisplayName(TimeZoneInfoEx timezone) throws Exception {
        if (timezone == null)
            return DefaultTimeZone;
        else
            return timezone.getDisplayName(); 
    }

    public TimeZoneInfoEx getGMTTimeZone() throws Exception {
        return null;
    }

    //if (m_GMTTimeZone != null)
    //    return m_GMTTimeZone;
    //string timeZone = m_DefParser.getValue(SECTION_COMMON, ATTRIBUTE_STANDARDTIMEZONE);
    //if (string.IsNullOrEmpty(timeZone))
    //    timeZone = DefaultTimeZone;
    //m_GMTTimeZone = TimeZoneInfoEx.FindTimeZone(timeZone);
    //m_GMTTimeZone.IsStandardTime = true;
    //return m_GMTTimeZone;
    /**
    * @return  date format form DEF
    */
    public String getDateFormat() throws Exception {
        String str = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_DATEFORMAT);
        if (str.length() == 0)
            str = "MM/DD/YYYY";
         
        return str;
    }

    public boolean isSupportOpenHouseMLSNoSearch() throws Exception {
        String str = m_DefParser.getValue(SECTION_AUTOSEARCH,ATTRIBUTE_MLSNUM);
        return str.length() > 0;
    }

    /**
    * @return  support search price flag form DEF
    */
    public boolean isSupportSearchPrice() throws Exception {
        String str = m_DefParser.getValue(SECTION_AUTOSEARCH,ATTRIBUTE_SEARCHPRICE);
        return str.length() > 0;
    }

    /**
    * @return  date format form DEF
    */
    public boolean isSupportStatusDate() throws Exception {
        String str = m_DefParser.getValue(SECTION_AUTOSEARCH,ATTRIBUTE_STATUSDATE);
        return str.length() > 0;
    }

    public boolean isSupportLastModifiedDate() throws Exception {
        String str = m_DefParser.getValue(SECTION_AUTOSEARCH,ATTRIBUTE_LASTMODIFIDATE);
        return str.length() > 0;
    }

    public boolean isSupportPicLastModifiedDate() throws Exception {
        String str = m_DefParser.getValue(SECTION_AUTOSEARCH,ATTRIBUTE_PICLASTMODIFIDATE);
        return str.length() > 0;
    }

    /**
    * @return  date format form DEF
    */
    public boolean isAddHiddenSearchFields() throws Exception {
        return (getEnvironment().getFlags() & Environment.FLAG_INCLUDE_HIDDEN_FIELD) != 0;
    }

    public boolean isTCSRequest() throws Exception {
        return (getEnvironment().getFlags() & Environment.FLAG_TCS_SERVER) != 0;
    }

    public int getRecordCount() throws Exception {
        return 0;
    }

    public boolean isRETSClient() throws Exception {
        String clientType = getClientType();
        boolean b = false;
        try
        {
            switch(Integer.valueOf(clientType))
            {
                case CLIENT_TYPE_RETS: 
                case CLIENT_TYPE_GARDENSTATE: 
                case CLIENT_TYPE_SAFEMLS: 
                case CLIENT_TYPE_MRISRETS: 
                case CLIENT_TYPE_REInfoLinkRETS: 
                    b = true;
                    break;
            
            }
        }
        catch (Exception exc)
        {
        }

        return b;
    }

    public boolean isFileImport() throws Exception {
        String dataSource = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_DATASOURCE);
        boolean b = false;
        switch(ClientFactory.getDataSource(dataSource))
        {
            case ClientFactory.DATA_SOURCE_ASCII: 
            case ClientFactory.DATA_SOURCE_WYLDFYRE: 
                b = true;
                break;
        
        }
        return b;
    }

    public boolean isMrisClient() throws Exception {
        String clientType = getClientType();
        boolean b = false;
        try
        {
            switch(Integer.valueOf(clientType))
            {
                case CLIENT_TYPE_MRIS: 
                    b = true;
                    break;
            
            }
        }
        catch (Exception exc)
        {
        }

        return b;
    }

    public boolean isDemoClient() throws Exception {
        String clientType = getClientType();
        boolean b = false;
        try
        {
            switch(Integer.valueOf(clientType))
            {
                case CLIENT_TYPE_DEMO: 
                    b = true;
                    break;
            
            }
        }
        catch (Exception exc)
        {
        }

        return b;
    }

    public boolean isSafeMLS() throws Exception {
        String clientType = getClientType();
        if (clientType == null || clientType.length() == 0)
            return false;
         
        boolean b = false;
        try
        {
            switch(Integer.valueOf(clientType))
            {
                case CLIENT_TYPE_SAFEMLS: 
                    b = true;
                    break;
            
            }
        }
        catch (Exception exc)
        {
            return false;
        }

        return b;
    }

    public boolean isSavePassLocal() throws Exception {
        String type = m_DefParser.getValue(SECTION_SECLIST,ATTRIBUTE_SETUP_LOCAL);
        boolean b = false;
        String __dummyScrutVar6 = type;
        if (__dummyScrutVar6.equals("1"))
        {
            b = true;
        }
         
        return b;
    }

    public int getMaxLitingsNumber() throws Exception {
        String value_Renamed = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MAXIMUMLISINGNUM);
        int count = 0;
        try
        {
            if (!StringSupport.isNullOrEmpty(value_Renamed))
                count = Integer.valueOf(value_Renamed);
             
        }
        catch (Exception __dummyCatchVar0)
        {
        }

        return count;
    }

    public boolean isGetRecordCount() throws Exception {
        return ((m_environment.getFlags() & Environment.FLAG_GET_RECORD_COUNT) != 0) || m_isRecordCount;
    }

    //public virtual bool isGetAdditionalData()
    //{
    //    return ((m_environment.getFlags() & Environment.FLAG_GET_ADDITIONAL_DATA) != 0);
    //}
    public void setSearchByRecordCount(boolean value) throws Exception {
        m_isRecordCount = value;
    }

    public boolean isCheckCredential() throws Exception {
        return (m_environment.getFlags() & Environment.FLAG_CHECK_CREDENTIAL) != 0;
    }

    public boolean isBypassAgentRosterAuth() throws Exception {
        if (getBypassAgentRosterAuth() == 1)
            return true;
         
        return (m_environment.isBypassAgentRosterAuth());
    }

    public boolean isIncludingTlBlobField() throws Exception {
        if (m_tpAppRequest == null)
            return false;
         
        if (getIncludingTlBlobField() == -1)
            setIncludingTlBlobField(m_tpAppRequest.m_connector.isIncludingTlBlobField() ? 1 : 0);
         
        return getIncludingTlBlobField() == 1;
    }

    public boolean isUsingComaptFormat() throws Exception {
        if (m_tpAppRequest == null)
            return false;
         
        if (getUseCompactFormat() == -1)
            setUseCompactFormat(m_tpAppRequest.m_connector.isUsingCompactFormat() ? 1 : 0);
         
        return getUseCompactFormat() == 1;
    }

    public boolean isGetPictureRequest() throws Exception {
        return (m_environment.isGetPictureRequest());
    }

    public String[] getLoginInfoFromRequest() throws Exception {
        return (m_environment.getLoginInfoFromRequest());
    }

    public int getMaxRecordsLimit() throws Exception {
        if (m_maxRecordsLimit == -1)
        {
            m_maxRecordsLimit = DEFAULT_MAX_RECORDS_LIMIT;
            String value = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_MAXRECORDSLIMIT);
            if (!StringSupport.isNullOrEmpty(value))
            {
                if (getMaxLitingsNumber() > 0)
                    throw TCSException.produceTCSException(TCSException.PARSINGLIMIT_MAXRECLIMIT_BOTH_USED);
                 
                try
                {
                    m_maxRecordsLimit = Integer.valueOf(value);
                }
                catch (Exception exc)
                {
                    writeLine("Error on read DEFAULT_MAX_RECORDS_LIMIT from DEF file." + value);
                }
            
            }
             
        }
         
        if (getTpAppRequest() != null)
        {
            if (getIsDownloadIDs() || getTpAppRequest().getSelectPicFieldsOnly())
            {
                if (getMaxRecordsIDsLimit() > 0)
                    return getMaxRecordsIDsLimit();
                 
            }
             
        }
         
        return m_maxRecordsLimit;
    }

    public HashMap<String,Integer> getFieldListForRetsBlob() throws Exception {
        if (m_FieldListForRetsBlob == null)
        {
            m_FieldListForRetsBlob = new HashMap<String,Integer>();
            for (int i = 0;i < m_CmaFieldsEx.size();i++)
            {
                String systemName = m_CmaFieldsEx.getFieldPositions()[i];
                if (!StringSupport.isNullOrEmpty(systemName) && systemName.startsWith("@"))
                {
                    /* [UNSUPPORTED] 'var' as type is unsupported "var" */ fieldNameForBlob = systemName.Remove(0, 1).Replace("\"", "");
                    if (!m_FieldListForRetsBlob.ContainsKey(fieldNameForBlob))
                    {
                        m_FieldListForRetsBlob.Add(fieldNameForBlob, i);
                    }
                     
                }
                 
            }
        }
         
        return m_FieldListForRetsBlob;
    }

    public int getDaysInChunk() throws Exception {
        int result = DEFAULT_DAYS_IN_CHUNK;
        String value = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_DAYSINCHUNK);
        if (!StringSupport.isNullOrEmpty(value))
        {
            try
            {
                result = Integer.valueOf(value);
            }
            catch (Exception exc)
            {
                writeLine("Error on read ATTRIBUTE_DAYSINCHUNK from DEF file." + value);
            }
        
        }
         
        return result;
    }

    public boolean isOverrideAllowed() throws Exception {
        boolean result = DEFAULT_OVERRIDE_ALLOWED;
        String value = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_OVERRIDEALLOWED);
        if (!StringSupport.isNullOrEmpty(value))
        {
            result = value.equals("1");
        }
         
        return result;
    }

    /**
    * Removes illegal for folder name characters from the source string.
    *  @param source entire string.
    * 
    *  @return  The entire string with all illegal characters replaced with spaces.
    */
    public static String makeSafeFolderName(String source) throws Exception {
        source = source.replace('/', ' ');
        source = source.replace('\\', ' ');
        source = source.replace(':', ' ');
        source = source.replace('*', ' ');
        source = source.replace('?', ' ');
        source = source.replace('"', ' ');
        source = source.replace('>', ' ');
        source = source.replace('<', ' ');
        source = source.replace('|', ' ');
        return source;
    }

    private String getPrivateFolderName() throws Exception {
        String result = "";
        switch(m_engineType)
        {
            case AgentEngine: 
                result = m_environment.getBoardId() + "agent";
                break;
            case OfficeEngine: 
                result = m_environment.getBoardId() + "office";
                break;
            default: 
                result = m_environment.getBoardId() + "_" + Tcs.Mls.Util.convertStringToXMLNode(m_environment.getModuleName());
                break;
        
        }
        return result;
    }

    /**
    * @return  the results folder for this MLS board. All results file must be created only inside this
    * folder and nowhere else.
    */
    public String getResultsFolder() throws Exception {
        if (m_moduleResultsFolder == null)
        {
            String resultFolder = m_environment.getResultFolder();
            if (resultFolder == null || resultFolder.startsWith("<TCService"))
                resultFolder = RESULTS_FOLDER;
             
            String openHouseProperty = getCurrentOpenHouseClass();
            String privateFolder = m_currentSearchStatus == null ? getPrivateFolderName() : (getPrivateFolderName() + (String)(StringSupport.isNullOrEmpty(getCurrentOpenHouseClass()) ? "" : "_" + getCurrentOpenHouseClass()) + "_" + m_currentSearchStatus);
            if (!StringSupport.isNullOrEmpty(m_retryNumber))
                privateFolder = privateFolder + "_" + m_retryNumber;
             
            m_moduleResultsFolder = constructPath(m_environment.getSearchTempFolder(),privateFolder);
        }
         
        return m_moduleResultsFolder;
    }

    public void setCurrentSearchStatus(String status) throws Exception {
        m_currentSearchStatus = status;
        m_moduleResultsFolder = null;
    }

    public void setRetryNumber(String retryNumber) throws Exception {
        m_retryNumber = retryNumber;
    }

    /**
    * Sets file name for fields cache file
    */
    public void setPropertyFieldsFilename(String str) throws Exception {
        m_strFieldsFile = str;
    }

    /**
    * @return  propertyField values filename
    */
    private String getPropertyFieldsFilename() throws Exception {
        if (m_strFieldsFile != null && m_strFieldsFile.length() != 0)
            return getResultsFolder() + m_strFieldsFile;
         
        return getResultsFolder() + FIELDS_FILE;
    }

    /**
    * initializes engine with search params
    */
    public void setPropertyFields(Hashtable params_Renamed) throws Exception {
        if (m_PropertyFieldGroups != null)
            m_PropertyFieldGroups.load(params_Renamed,this);
         
    }

    public void loadPropertyFields() throws Exception {
        if (m_PropertyFieldGroups != null && m_strFieldsFile != null)
            m_PropertyFieldGroups.load(getPropertyFieldsFilename(),this);
         
    }

    /**
    * @return  search results filename.
    */
    public String getResultsFilename() throws Exception {
        return getResultsFolder() + RESULTS_FILE;
    }

    /**
    * @return  search results filename.
    */
    public String getRecordsFilename() throws Exception {
        return getResultsFolder() + RECORDS_FILE;
    }

    /**
    * @return  the folder for binary files
    */
    public String getBinFolder() throws Exception {
        if (m_bin_folder == null)
        {
            if (isTCSRequest())
            {
                m_bin_folder = m_environment.getBinFolder();
                if (!m_bin_folder.endsWith("\\"))
                    m_bin_folder += "\\";
                 
            }
            else
                m_bin_folder = constructPath(getMLSFolder() + BIN_FOLDER,getPrivateFolderName()); 
        }
         
        return m_bin_folder;
    }

    /**
    * @return  the folder for local def-file caching.
    */
    public String getCacheFileName() throws Exception {
        return constructPath(getMLSFolder(),CACHE_FOLDER) + getPrivateFolderName() + "-" + MLS_ENGINE_VERSION + "-" + makeSafeFolderName(m_environment.getDefFileVersion()) + ".tmp";
    }

    public String getCacheScriptEngineFileName() throws Exception {
        return constructPath(getMLSFolder(),CACHE_FOLDER) + getPrivateFolderName() + "-" + MLS_ENGINE_VERSION + "-" + makeSafeFolderName(m_environment.getDefFileVersion()) + ".dll";
    }

    public String getDisclaimer() throws Exception {
        String s = m_DefParser.getValue(SECTION_TPONLINE,ATTRIBUTE_DISCLAIMER);
        if (s == null)
            return "";
         
        return s;
    }

    /// <summary> Installs files from zip package to the MLS binary directory.
    /// <p>Important: Package must contain file install.inf containing just one string:<br>
    /// %path%/=*
    /// <br>
    /// </summary>
    /// <param name="zip_url">URL of the zip file. URL is treaded as relative if it doesn't
    /// contain protocol specifier. For example "www.toppro.com/aaa.zip" is not correct url,
    /// the function will try to add base url to it.
    /// "aaa.zip" is good - it is relative and base url will be added.
    /// "http://www.toppro.com/aaa.zip" and "file://c:\aaa.zip" are correct too. The function
    /// will find the protocol specifier "http:", or "file:", so it will tread it as absolute url.
    /// </param>
    /// <returns> Full path of the directory where files were installed.
    /// </returns>
    public String install(String zip_file) throws Exception {
        String dir = getBinFolder();
        int i = zip_file.indexOf(':');
        URI serverBase;
        String fileName;
        try
        {
            if (i < 0)
            {
                //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
                serverBase = new URI(m_environment.getServerRoot(), "/");
                fileName = SERVER_BIN_PATH + zip_file;
            }
            else
            {
                int j = zip_file.lastIndexOf('\\');
                if (j >= i)
                    i = j;
                 
                j = zip_file.lastIndexOf('/');
                if (j >= i)
                    i = j;
                 
                //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
                serverBase = new URI(zip_file.Substring(0, (i)-(0)));
                fileName = zip_file.substring(i + 1);
            } 
        }
        catch (Exception exc)
        {
            throw createException(exc,MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_DOWNLOADING_ZIP),zip_file));
        }

        if (!m_environment.installZipFile(serverBase,fileName,dir))
            throw createException(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_DOWNLOADING_ZIP),zip_file));
         
        File f = new File(dir + INSTALL_EXE_FILE);
        boolean tmpBool;
        if (File.Exists(f.FullName))
            tmpBool = true;
        else
            tmpBool = File.Exists(f.FullName); 
        if (tmpBool)
        {
            try
            {
                System.Diagnostics.Process.GetCurrentProcess();
                System.Diagnostics.Process install_process = System.Diagnostics.Process.Start(dir + INSTALL_EXE_FILE);
                install_process.WaitForExit();
                if (install_process.ExitCode != 0)
                {
                    File logFile = new File(dir + INSTALL_LOG_FILE);
                    boolean tmpBool2;
                    if (File.Exists(logFile.FullName))
                        tmpBool2 = true;
                    else
                        tmpBool2 = File.Exists(logFile.FullName); 
                    if (tmpBool2)
                    {
                        StringBuilder fc = MLSUtil.readFile2String(dir + INSTALL_LOG_FILE);
                        throw createException(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_UNABLE_TO_INSTALL_ZIP),fc.toString());
                    }
                    else
                    {
                        throw createException(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_UNABLE_TO_INSTALL_ZIP));
                    } 
                }
                 
            }
            catch (Exception e)
            {
                throw createException(e,STRINGS.get_Renamed(STRINGS.MLS_ENGINE_ERR_UNABLE_TO_INSTALL_ZIP));
            }
        
        }
         
        return dir;
    }

    /**
    * @return  the parent folder for all mls files and folders.
    *  have to be changed to private. See our todo list.
    */
    private String getMLSFolder() throws Exception {
        if (m_mls_folder == null)
            m_mls_folder = constructPath(m_environment.getLocalHomePath(),MLS_FOLDER);
         
        return m_mls_folder;
    }

    /**
    * ask Jack, what is it for???
    */
    public String getPicDownLoadFlag() throws Exception {
        String flag = "";
        flag = m_DefParser.getValue(SECTION_PICTURES,ATTRIBUTE_PICDWNFLAG);
        return flag;
    }

    /**
    * @return  Default ToopTip for this MLS Board
    */
    public String getDefaultToolTip() throws Exception {
        return m_commClient.getDefaultToolTip();
    }

    /**
    * This is used by BoardSetup only for lazy initialization of BoardSetup.m_notes
    *  @return  Setup notes for this MLS board.
    */
    public String getSetupNotes() throws Exception {
        String[] badPhrases = new String[]{ " tab", " modem", "make CMA", " Tab", "you must have access to the Internet", "It is suggested", "Top Producer will ask you", "C:\\TPOFFICE", "Photo Setup" };
        String str = "";
        String result = " ";
        String s = "";
        int i = 1, j;
        int k;
        boolean haveBadPhrase = false;
        while (result.length() != 0)
        {
            s = "Note";
            s += String.valueOf((((int)(i))));
            result = m_DefParser.getValue("Common",s);
            if (result.length() != 0)
            {
                if (i != 1)
                    str += " ";
                 
                str += result;
            }
             
            i++;
        }
        result = " ";
        i = 1;
        if ((m_DefParser.getValue(SECTION_TPONLINE,ATTRIBUTE_TPONLINE_NOTE + "1")).length() != 0)
        {
            str = "";
            while (result.length() != 0)
            {
                s = ATTRIBUTE_TPONLINE_NOTE;
                s += String.valueOf((((int)(i))));
                result = m_DefParser.getValue(SECTION_TPONLINE,s);
                if (result.length() != 0)
                {
                    str += " ";
                    str += result;
                }
                 
                i++;
            }
        }
         
        //return str;
        //System.String TopConnector = "top connector";
        //System.String TopConnectorVersion = " 7i";
        //System.String TopConnector7i = TopConnector + TopConnectorVersion;
        //System.String str_in_lowcase = str.ToLower();
        //k = 0;
        ////UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //while (SupportClass.getIndex(str_in_lowcase, TopConnector, k) != -1)
        //{
        //    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
        //    if ((j = SupportClass.getIndex(str_in_lowcase, TopConnector7i, k)) == -1 && (i = SupportClass.getIndex(str_in_lowcase, TopConnector, k)) != -1)
        //    {
        //        str = str.Substring(0, (i) - (0)) + TopConnector.ToUpper() + TopConnectorVersion + str.Substring(i + TopConnector.Length, (str.Length) - (i + TopConnector.Length));
        //        k = i + 1;
        //        str_in_lowcase = str.ToLower();
        //    }
        //    else if (j != - 1)
        //        break;
        //}
        haveBadPhrase = false;
        i = 0;
        StringTokenizerEx strToken = new StringTokenizerEx(str,". ");
        str = "";
        while (strToken.hasMoreElements())
        {
            // Delete sentence that contains the keyword
            haveBadPhrase = false;
            s = new StringBuilder((String)strToken.nextElement()).toString();
            for (k = 0;k < badPhrases.length;k++)
                if (s.indexOf(badPhrases[k]) != -1)
                {
                    haveBadPhrase = true;
                    break;
                }
                 
            if (haveBadPhrase == false)
                for (k = 0;k < badPhrases.length;k++)
                    if (s.indexOf(badPhrases[k].toUpperCase()) != -1)
                    {
                        haveBadPhrase = true;
                        break;
                    }
                     
             
            if (haveBadPhrase == false)
            {
                if (!s.endsWith("."))
                    str += (s + ". ");
                else
                    str += s; 
                i++;
            }
             
        }
        String[] badFilters = new String[]{ "Passwords section", "LOGON SETUP Screen", "LOGON SETUP", "PROGRA~1", ". To setup" };
        String[] goodFilters = new String[]{ "Login information section", "Login setup", "Login setup", "PROGRAM FILES", ". To set up" };
        for (i = 0;i < badFilters.length;i++)
        {
            if (str.indexOf(badFilters[i]) != -1)
                str = MLSUtil.replaceSubStr(str,badFilters[i],goodFilters[i]);
             
        }
        String[] oldPhrases = new String[]{ "Usually" };
        String[] addBeforOldPhrases = new String[]{ "If different from data file location, select MLS pictures folder. " };
        for (i = 0;i < oldPhrases.length;i++)
        {
            if ((k = str.indexOf(oldPhrases[i])) != -1)
            {
                if (k == 0)
                    str = addBeforOldPhrases[i] + str;
                else
                    str = str.substring(0, (0) + ((k - 1) - (0))) + addBeforOldPhrases[i] + str.Substring(k, (str.Length)-(k)); 
            }
             
        }
        return str;
    }

    public MultiPictures getMulitiPictures() throws Exception {
        if (m_multiPictures == null)
            m_multiPictures = new MultiPictures(this);
         
        return m_multiPictures;
    }

    /**
    * Parses Def file and prepare property fields
    *  @param buffer def-file content.
    */
    private void parseDefFile(String buff) throws Exception {
        StringBuilder fullContents = new StringBuilder();
        StringBuilder MainScriptContents = new StringBuilder();
        StringBuilder FieldsContents = new StringBuilder();
        StringBuilder SecFieldsContents = new StringBuilder();
        StringBuilder PicScriptContents = new StringBuilder();
        StringBuilder MPicScriptContents = new StringBuilder();
        StringBuilder MlsRecordsContents = new StringBuilder();
        StringBuilder MlsRecordsExContents = new StringBuilder();
        StringBuilder RemScriptContents = new StringBuilder();
        StringBuilder ExVarScriptContents = new StringBuilder();
        StringBuilder ResultScriptContents = new StringBuilder();
        boolean bMainScriptFlag = false;
        boolean bPicScriptFlag = false;
        boolean bMPicScriptFlag = false;
        boolean bFieldFlag = false;
        boolean bMlsRecordsFlag = false;
        boolean bMlsRecordsExFlag = false;
        boolean bSecFlag = false;
        boolean bRemScriptFlag = false;
        boolean bExVarScriptFlag = false;
        boolean bResultScriptFlag = false;
        String buffer = "";
        StringTokenizerEx st = new StringTokenizerEx(buff,"\n");
        while (st.hasMoreElements())
        {
            buffer = StringSupport.Trim(((String)st.nextElement()));
            if (bFieldFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bFieldFlag = false;
             
            if (bMainScriptFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bMainScriptFlag = false;
             
            if (bMlsRecordsFlag == true && buffer.length() == 0)
                bMlsRecordsFlag = false;
             
            if (bMlsRecordsExFlag == true && buffer.length() == 0)
                bMlsRecordsExFlag = false;
             
            if (bPicScriptFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bPicScriptFlag = false;
             
            if (bMPicScriptFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bMPicScriptFlag = false;
             
            if (bSecFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bSecFlag = false;
             
            if (bRemScriptFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bRemScriptFlag = false;
             
            if (bExVarScriptFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bExVarScriptFlag = false;
             
            if (bResultScriptFlag == true && buffer.length() != 0 && buffer.charAt(0) == '[')
                bResultScriptFlag = false;
             
            if (bMainScriptFlag == false && bPicScriptFlag == false && bRemScriptFlag == false && bExVarScriptFlag == false && bResultScriptFlag == false)
                fullContents.append(buffer + "\r\n");
             
            if (bMainScriptFlag == true)
                MainScriptContents.append(buffer + "\r\n");
             
            if (bFieldFlag == true)
                FieldsContents.append(buffer + "\r\n");
             
            if (bSecFlag == true)
                SecFieldsContents.append(buffer + "\r\n");
             
            if (bPicScriptFlag == true)
            {
                PicScriptContents.append(buffer + "\r\n");
                if (buffer.equals("\r\n"))
                    bPicScriptFlag = false;
                 
            }
             
            if (bMPicScriptFlag == true)
            {
                MPicScriptContents.append(buffer + "\r\n");
                if (buffer.equals("\r\n"))
                    bMPicScriptFlag = false;
                 
            }
             
            if (bRemScriptFlag == true)
            {
                RemScriptContents.append(buffer + "\r\n");
                if (buffer.equals("\r\n"))
                    bRemScriptFlag = false;
                 
            }
             
            if (bExVarScriptFlag == true)
            {
                ExVarScriptContents.append(buffer + "\r\n");
                if (buffer.equals("\r\n"))
                    bExVarScriptFlag = false;
                 
            }
             
            if (bResultScriptFlag == true)
            {
                if (buffer.indexOf("#End Script") < 0)
                    ResultScriptContents.append(buffer + "\r\n");
                 
                if (buffer.indexOf("#End Script") > -1)
                    bRemScriptFlag = false;
                 
            }
             
            if (bMlsRecordsFlag == true)
                MlsRecordsContents.append(buffer + "\r\n");
             
            if (bMlsRecordsExFlag == true)
                MlsRecordsExContents.append(buffer + "\r\n");
             
            if (buffer.toUpperCase().equals(("[" + SECTION_FIELDS + "]").toUpperCase()) == true)
                bFieldFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_MLSRECORDS + "]").toUpperCase()) == true)
                bMlsRecordsFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_MLSRECORDSEX + "]").toUpperCase()) == true)
                bMlsRecordsExFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_SECLIST + "]").toUpperCase()) == true)
                bSecFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_MAINSCRIPT + "]").toUpperCase()) == true)
                bMainScriptFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_PICSCRIPT + "]").toUpperCase()) == true)
                bPicScriptFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_MPICSCRIPT + "]").toUpperCase()) == true)
                bMPicScriptFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_REMSCRIPT + "]").toUpperCase()) == true)
                bRemScriptFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + ((getSectionExtraVarScript().indexOf(",") != -1) ? StringSupport.Split(getSectionExtraVarScript(), ',')[0] : getSectionExtraVarScript()) + "]").toUpperCase()) == true)
                bExVarScriptFlag = true;
             
            if (buffer.toUpperCase().equals(("[" + SECTION_RESULTSCRIPT + "]").toUpperCase()) == true)
                bResultScriptFlag = true;
             
        }
        if (!StringSupport.isNullOrEmpty(getSectionExtraVarScript()) && (m_DefParser != null))
        {
            m_DefParser.setExVarScriptContent(getSectionExtraVarScript(),ExVarScriptContents.toString());
            return ;
        }
         
        //
        m_DefParser = new DefParser();
        m_DefParser.parseBuffer(fullContents.toString());
        m_DefParser.setMainScriptContent(MainScriptContents.toString());
        m_DefParser.setPicScriptContent(PicScriptContents.toString());
        m_DefParser.setMPicScriptContent(MPicScriptContents.toString());
        m_DefParser.setRemScriptContent(RemScriptContents.toString());
        m_DefParser.setFieldsContent(FieldsContents.toString());
        m_DefParser.setSecFieldsContent(SecFieldsContents.toString());
        m_DefParser.setResultScriptContent(ResultScriptContents.toString());
        m_DefParser.setMlsRecordsContent(MlsRecordsContents.toString());
        m_DefParser.setMlsRecordsExContent(MlsRecordsExContents.toString());
        m_PropertyFieldGroups = new PropertyFieldGroups();
        m_boardSetup = new BoardSetup(this);
        m_CmaFields = new MLSCmaFields(this);
        if (MlsRecordsExContents.length() > 0)
            m_CmaFieldsEx = new MLSCmaFields(this);
         
        m_commClient = ClientFactory.createClient(this,getClientType(),m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_DATASOURCE),m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_NAME_OF_ORIGIN));
        m_commClient.initBoardSetup(m_boardSetup);
        m_commClient.initPropertyFields(m_PropertyFieldGroups);
        if (getCurrentPropertyFieldGroup() == null)
        {
            m_PropertyFieldGroups.createEmptyGroup();
            m_PropertyFieldGroups.setCurrentGroup(0);
            m_PropertyFieldGroups.setDefaulGroupIndex(0);
        }
         
        m_CmaFields.initCmaFields(MlsRecordsContents.toString());
        if (MlsRecordsExContents.length() > 0 && m_CmaFieldsEx != null)
        {
            m_CmaFieldsEx.initCmaFields(MlsRecordsExContents.toString());
            m_CmaFieldsEx.initFeatureGroups();
        }
         
        m_MlsRecordset = new MLSRecordSet();
        if (MlsRecordsExContents.length() > 0 && m_CmaFieldsEx != null && !isReturnIdsOnly())
        {
            //&& isGetAdditionalData())
            Hashtable tempHT = new Hashtable();
            for (String fieldPos : m_CmaFieldsEx.getFieldPositions())
            {
                if (fieldPos.indexOf("$") != -1)
                {
                    String valExtraVar = fieldPos.replace("$", "");
                    String keyExtraVar = ((valExtraVar.indexOf(",") != -1) ? StringSupport.Split(valExtraVar, ',')[0] : valExtraVar);
                    if (!tempHT.containsKey(keyExtraVar))
                    {
                        tempHT.put(keyExtraVar, "");
                        setSectionExtraVarScript(valExtraVar);
                        parseDefFile(buff);
                    }
                     
                }
                 
            }
        }
         
    }

    // Initialize m_typeStrings. See their definition for details.
    //m_typeStrings = new System.String[ATTRIBUTE_SORTING_TYPE.Length][];
    //m_publicTypeStrings = new string[ATTRIBUTE_PUBLIC_SORTING_TYPE.Length][];
    //for (int i = 0; i < ATTRIBUTE_SORTING_TYPE.Length; i++)
    //{
    //    System.String typeString = m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[i]);
    //    SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString, ",");
    //    System.Collections.ArrayList vector = System.Collections.ArrayList.Synchronized(new System.Collections.ArrayList(10));
    //    while (tokenizer.HasMoreTokens())
    //        vector.Add(tokenizer.NextToken());
    //    m_typeStrings[i] = new System.String[vector.Count];
    //    for (int j = 0; j < vector.Count; j++)
    //        m_typeStrings[i][j] = ((System.String) vector[j]);
    //}
    //for (int i = 0; i < ATTRIBUTE_PUBLIC_SORTING_TYPE.Length; i++)
    //{
    //    System.String typeString = m_DefParser.getValue(SECTION_PUBLIC_SORTING, ATTRIBUTE_PUBLIC_SORTING_TYPE[i]);
    //    if (string.IsNullOrEmpty(typeString))
    //    {
    //        if (i == 0 || i == 1)
    //            typeString = m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[i]);
    //        else
    //        {
    //            typeString = m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[2]);
    //            if (!string.IsNullOrEmpty(m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[3])))
    //                typeString = typeString + "," + m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[3]);
    //        }
    //    }
    //    SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString, ",");
    //    System.Collections.ArrayList vector = System.Collections.ArrayList.Synchronized(new System.Collections.ArrayList(10));
    //    while (tokenizer.HasMoreTokens())
    //        vector.Add(tokenizer.NextToken());
    //    m_publicTypeStrings[i] = new System.String[vector.Count];
    //    for (int j = 0; j < vector.Count; j++)
    //        m_publicTypeStrings[i][j] = ((System.String)vector[j]);
    //}
    /**
    * Loads previous search results into MLSRecords object.
    */
    public void preparePrevMlsRecords() throws Exception {
        if (hasPrevResults())
            m_MlsRecordset.load(this);
        else
            m_MlsRecordset.clear(); 
    }

    public void getDemoRecordSet() throws Exception {
        Properties settings;
        String filePath = "";
        try
        {
            settings = (Properties)NONE.GetSection("GeneralConfiguration");
            filePath = (String)settings["DefFilePath"] + DemoFloder;
            MLSUtil.copyFile(this,filePath + RECORDS_FILE,getResultsFolder() + RECORDS_FILE);
            preparePrevMlsRecords();
        }
        catch (Exception exc)
        {
        }
    
    }

    //getEngine().getEnvironment().downloadDemoData(getEngine().getResultsFolder());
    //throw getEngine().createException(exc, STRINGS.get_Renamed(STRINGS.ERR_DEMO_CLIENT_UNABLE_TO_GET_RECORDS));
    private void setMLSRecordsPictureState(MLSRecords records, int state) throws Exception {
        if (records != null)
        {
            MLSRecord rec = records.getFirstRecord();
            while (rec != null)
            {
                rec.setPictureDownloadState(state);
                rec = records.getNextRecord();
            }
        }
        else
        {
            MLSRecord[] recs = m_MlsRecordset.getArray();
            for (int i = 0;i < recs.length;i++)
                recs[i].setPictureDownloadState(state);
        } 
    }

    /**
    * Sets additional params for engine.
    *  @param t - Hashtable, where keys are Names of Param, values = values of param
    */
    public void setAdditionalParams(Hashtable t) throws Exception {
        m_Params = t;
    }

    public void addAdditionalParam(String name, String value_Renamed) throws Exception {
        if (m_Params == null)
            m_Params = Hashtable.Synchronized(new Hashtable());
         
        m_Params.put(name, value_Renamed);
    }

    /**
    * @return  additional params
    */
    public Hashtable getAdditionalParams() throws Exception {
        return m_Params;
    }

    /**
    * Runs PicScript for client
    */
    public void runPicScript(MLSRecords records) throws Exception {
        setOverallProgress(0,1);
        setProgress(0,100);
        setMessage(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_WAITING_FOR_PREV_SCRIPT));
        //lock (this)
        //{
        if (m_log != null)
            m_log.start();
         
        if (getLogMode() != LOG_COMM_LOG)
            writeLine("Working Folder -- " + getResultsFolder(),true);
         
        try
        {
            writeLine();
            writeLine("+-----------------------------------------------------------------------------------+");
            writeLine("|                       Downloading pictures (picture script)                       |");
            writeLine("+-----------------------------------------------------------------------------------+");
            writeLine();
            m_environment.setMessage(STRINGS.get_Renamed(STRINGS.COMMON_EXECUTING_SCRIPT,1));
            if (m_commClient != null && (m_commClient.getFlags() & net.toppro.components.mls.engine.CommunicationClient.FLAG_PICSCRIPT_INSIDE_MAINSCRIPT) == 0)
            {
                try
                {
                    m_commClient.runPicScript(records);
                }
                finally
                {
                    // update 'picture downloaded' flag for all checked records
                    // Sergei: think about moving this functionality to CommunicationClient
                    // refresh must be done in 'finally' block, because some pictures could be
                    // downloaded successfuly even if one of them failed
                    setMLSRecordsPictureState(records,MLSRecord.PICTURE_DOWNLOADED);
                }
                m_environment.checkStop();
            }
             
        }
        finally
        {
            //m_MlsRecordset.save(this);
            if (m_log != null)
                m_log.stop();
             
        }
    }

    //}
    /**
    * Runs RemScript for client
    */
    public void runExRemScript(MLSRecords records) throws Exception {
        setOverallProgress(0,1);
        setProgress(0,100);
        setMessage(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_WAITING_FOR_PREV_SCRIPT));
        //lock (this)
        //{
        if (m_log != null)
            m_log.start();
         
        if (getLogMode() != LOG_COMM_LOG)
            writeLine("Working Folder -- " + getResultsFolder(),true);
         
        try
        {
            writeLine();
            writeLine("+-----------------------------------------------------------------------------------+");
            writeLine("|                       Downloading notes (rem script)                       |");
            writeLine("+-----------------------------------------------------------------------------------+");
            writeLine();
            m_environment.setMessage(STRINGS.get_Renamed(STRINGS.COMMON_EXECUTING_SCRIPT,1));
            if (m_commClient != null && (m_commClient.getFlags() & net.toppro.components.mls.engine.CommunicationClient.FLAG_EXREMSCRIPT_INSIDE_MAINSCRIPT) == 0)
            {
                try
                {
                    m_commClient.runExRemScript(records);
                }
                finally
                {
                }
                // update 'notes downloaded' flag for all checked records
                m_environment.checkStop();
            }
             
        }
        finally
        {
            //m_MlsRecordset.save(this);
            if (m_log != null)
                m_log.stop();
             
        }
    }

    //}
    /**
    * Runs MainScript for client
    *  @param flags main script flags - an 'or' combination of MSFLAG_xxx constants.
    */
    public void runMainScript(int flags) throws Exception {
        setEngineFlags(flags);
        setOverallProgress(0,1);
        setProgress(0,100);
        setMessage(STRINGS.get_Renamed(STRINGS.MLS_ENGINE_WAITING_FOR_PREV_SCRIPT));
        if (!StringSupport.isNullOrEmpty(m_currentSearchStatus) && m_currentSearchStatus.length() == 1)
            setCurrentSearchStatus(m_currentSearchStatus + (Environment.TickCount & Integer.MAX_VALUE).toString());
         
        //lock (this)
        //{
        if (m_log != null)
            m_log.start();
         
        if (getLogMode() != LOG_COMM_LOG)
            writeLine("Working Folder -- " + getResultsFolder(),true);
         
        try
        {
            writeLine();
            writeLine("+-----------------------------------------------------------------------------------+");
            writeLine("|                        Downloading records (main script)                          |");
            writeLine("+-----------------------------------------------------------------------------------+");
            writeLine();
            m_environment.setMessage(STRINGS.get_Renamed(STRINGS.COMMON_EXECUTING_SCRIPT,0));
            if (m_commClient != null)
            {
                //if (m_PropertyFieldGroups != null && m_strFieldsFile != null)
                //    m_PropertyFieldGroups.save(getPropertyFieldsFilename(), this);
                if ((flags & MSFLAG_ADD_TO_PREV) == MSFLAG_ADD_TO_PREV)
                    preparePrevMlsRecords();
                else
                    m_MlsRecordset.clear(); 
                String filename = getResultsFilename();
                m_environment.checkStop();
                m_commClient.runMainScript();
                m_environment.checkStop();
                if ((flags & MSFLAG_METADATA_ONLY) != MSFLAG_METADATA_ONLY)
                {
                    m_environment.writeLine("Filename:" + filename);
                    m_commClient.prepareMlsRecords(filename);
                    m_environment.checkStop();
                    if (m_MlsRecordset.getRecordCount() != 0)
                    {
                        int clientFlags = m_commClient.getFlags();
                        if ((flags & MSFLAG_NO_REM_SCRIPT) != MSFLAG_NO_REM_SCRIPT)
                        {
                            if ((flags & MSFLAG_RUN_REM_SCRIPT) == MSFLAG_RUN_REM_SCRIPT || (clientFlags & net.toppro.components.mls.engine.CommunicationClient.FLAG_REMSCRIPT_INSIDE_MAINSCRIPT) == net.toppro.components.mls.engine.CommunicationClient.FLAG_REMSCRIPT_INSIDE_MAINSCRIPT)
                            {
                                m_commClient.runRemScript(new MLSRecords(m_MlsRecordset,MLSRecords.FILTER_ALL,null));
                            }
                             
                        }
                         
                        m_environment.checkStop();
                        if ((flags & MSFLAG_NO_PIC_SCRIPT) != MSFLAG_NO_PIC_SCRIPT)
                        {
                            if ((flags & MSFLAG_RUN_PIC_SCRIPT) == MSFLAG_RUN_PIC_SCRIPT || (clientFlags & net.toppro.components.mls.engine.CommunicationClient.FLAG_PICSCRIPT_INSIDE_MAINSCRIPT) == net.toppro.components.mls.engine.CommunicationClient.FLAG_PICSCRIPT_INSIDE_MAINSCRIPT)
                            {
                                MLSRecords records = new MLSRecords(m_MlsRecordset,MLSRecords.FILTER_MISSING_PICTURES,null);
                                try
                                {
                                    m_commClient.runPicScript(records);
                                }
                                finally
                                {
                                    // update 'picture downloaded' flag for all checked records
                                    // Sergei: think about moving this functionality to CommunicationClient
                                    // Refresh must be done in 'finally' block, because some pictures could be
                                    // downloaded successfuly even if one of them failed
                                    // null as a first parameter means that we need to update all records
                                    // rather than used in picture script. Because records with pictures (they
                                    // are not in the recordset) were created with MLSRecord.PICTURE_DOWNLOADED
                                    // status, which should be changed to MLSRecord.PICTURE_DOWNLOADED_FINALY
                                    // for MSFLAG_RUN_PIC_SCRIPT kind of clients.
                                    setMLSRecordsPictureState(null,MLSRecord.PICTURE_DOWNLOADED_FINALY);
                                }
                                m_environment.checkStop();
                            }
                             
                        }
                         
                        //m_MlsRecordset.save(this);
                        m_environment.checkStop();
                        if ((flags & MSFLAG_NO_EXREM_SCRIPT) != MSFLAG_NO_EXREM_SCRIPT)
                        {
                            if ((flags & MSFLAG_RUN_EXREM_SCRIPT) == MSFLAG_RUN_EXREM_SCRIPT || (clientFlags & net.toppro.components.mls.engine.CommunicationClient.FLAG_EXREMSCRIPT_INSIDE_MAINSCRIPT) == net.toppro.components.mls.engine.CommunicationClient.FLAG_EXREMSCRIPT_INSIDE_MAINSCRIPT)
                            {
                                MLSRecords records = new MLSRecords(m_MlsRecordset,MLSRecords.FILTER_ALL,null);
                                MLSCmaFields fields = getCmaFields();
                                Hashtable tempHT = new Hashtable();
                                for (String fieldPos : fields.getFieldPositions())
                                {
                                    if (fieldPos.indexOf("$") != -1)
                                    {
                                        try
                                        {
                                            String valExtraVar = fieldPos.replace("$", "");
                                            String keyExtraVar = ((valExtraVar.indexOf(",") != -1) ? StringSupport.Split(valExtraVar, ',')[0] : valExtraVar);
                                            if (!tempHT.containsKey(keyExtraVar))
                                            {
                                                tempHT.put(keyExtraVar, "");
                                                setSectionExtraVarScript(valExtraVar);
                                                m_commClient.runExRemScript(records);
                                            }
                                             
                                        }
                                        catch (Exception ex)
                                        {
                                            continue;
                                        }
                                        finally
                                        {
                                        }
                                    }
                                     
                                }
                                //ignore
                                m_environment.checkStop();
                            }
                             
                        }
                         
                    }
                     
                }
                 
            }
             
        }
        finally
        {
            if (m_log != null)
                m_log.stop();
             
        }
    }

    //}
    public String getNoPictureFilename() throws Exception {
        return m_noPictureFilename;
    }

    //if (!m_noPictureFilenameInitialized)
    //{
    //    m_noPictureFilenameInitialized = true;
    //    if (NO_PICTURE_FILENAME != null)
    //    {
    //        // Note possible synchronization problems with the different instances of IE...
    //        m_noPictureFilename = constructPath(getMLSFolder(), RESULTS_FOLDER) + NO_PICTURE_FILENAME;
    //        bool tmpBool;
    //        if (System.IO.File.Exists((new System.IO.FileInfo(m_noPictureFilename)).FullName))
    //            tmpBool = true;
    //        else
    //            tmpBool = System.IO.Directory.Exists((new System.IO.FileInfo(m_noPictureFilename)).FullName);
    //        if (!tmpBool)
    //        {
    //            System.IO.Stream in_Renamed = null;
    //            System.IO.BufferedStream bis = null;
    //            System.IO.FileStream fos = null;
    //            try
    //            {
    //                //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
    //                System.Uri url = new System.Uri(m_environment.getServerRoot(), SERVER_MLS_FOLDER + "/" + NO_PICTURE_FILENAME);
    //                System.Net.HttpWebRequest conn = (System.Net.HttpWebRequest) System.Net.WebRequest.Create(url);
    //                //UPGRADE_ISSUE: Method 'java.net.URLConnection.setUseCaches' was not converted. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1000_javanetURLConnectionsetUseCaches_boolean'"
    //                conn.setUseCaches(true);
    //                in_Renamed = conn is net.toppro.components.mls.engine.client.HttpURLConnectionEx?((net.toppro.components.mls.engine.client.HttpURLConnectionEx) conn).getInputStream():conn.GetResponse().GetResponseStream();
    //                bis = new System.IO.BufferedStream(in_Renamed);
    //                //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
    //                fos = new System.IO.FileStream(m_noPictureFilename, System.IO.FileMode.Create);
    //                sbyte[] buffer = new sbyte[1024];
    //                while (true)
    //                {
    //                    long available;
    //                    //UPGRADE_WARNING: Method java.io.BufferedInputStream.available was converted to 'System.IO.BufferedStream.Length' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
    //                    available = bis.Length - bis.Position;
    //                    int len = (int) available;
    //                    if (len <= 0)
    //                        len = 1;
    //                    else if (len > 1024)
    //                        len = 1024;
    //                    len = in_Renamed is net.sf.jazzlib.ZipFile.PartialInputStream?((net.sf.jazzlib.ZipFile.PartialInputStream) in_Renamed).read(buffer, 0, len):SupportClass.ReadInput(in_Renamed, buffer, 0, len);
    //                    if (len < 0)
    //                        break; //EOF
    //                    fos.Write(SupportClass.ToByteArray(buffer), 0, len);
    //                }
    //            }
    //            catch (System.Exception exc)
    //            {
    //                SupportClass.WriteStackTrace(exc, Console.Error);
    //                m_noPictureFilename = null;
    //            }
    //            finally
    //            {
    //                if (fos != null)
    //                {
    //                    try
    //                    {
    //                        fos.Close();
    //                    }
    //                    catch (System.Exception exc)
    //                    {
    //                    }
    //                }
    //                if (bis != null)
    //                {
    //                    try
    //                    {
    //                        //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
    //                        bis.Close();
    //                    }
    //                    catch (System.Exception exc)
    //                    {
    //                    }
    //                }
    //                if (in_Renamed != null)
    //                {
    //                    try
    //                    {
    //                        in_Renamed.Close();
    //                    }
    //                    catch (System.Exception exc)
    //                    {
    //                    }
    //                }
    //            }
    //        }
    //    }
    //}
    public String[][] getTypeStrings() throws Exception {
        if (m_typeStrings == null)
        {
            m_typeStrings = new String[ATTRIBUTE_SORTING_TYPE.length];
            for (int i = 0;i < ATTRIBUTE_SORTING_TYPE.length;i++)
            {
                String typeString = m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[i]);
                SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString,",");
                ArrayList vector = ArrayList.Synchronized(new ArrayList(10));
                while (tokenizer.hasMoreTokens())
                vector.add(tokenizer.nextToken());
                m_typeStrings[i] = new String[vector.size()];
                for (int j = 0;j < vector.size();j++)
                    m_typeStrings[i][j] = ((String)vector.get(j));
            }
        }
         
        return m_typeStrings;
    }

    /**
    * To be removed in a future release
    * 
    *  @return
    */
    public String[][] getPublicTypeStringsOldLogic() throws Exception {
        if (m_publicTypeStrings == null)
        {
            m_publicTypeStrings = new String[ATTRIBUTE_PUBLIC_SORTING_TYPE.length];
            for (int i = 0;i < ATTRIBUTE_PUBLIC_SORTING_TYPE.length;i++)
            {
                String typeString = m_DefParser.getValue(SECTION_PUBLIC_SORTING_Old_LOGIC,ATTRIBUTE_PUBLIC_SORTING_TYPE[i]);
                if (StringSupport.isNullOrEmpty(typeString))
                {
                    if (i == 0 || i == 1)
                        typeString = m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[i]);
                    else
                    {
                        typeString = m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[2]);
                        if (!StringSupport.isNullOrEmpty(m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[3])))
                            typeString = typeString + "," + m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[3]);
                         
                    } 
                }
                 
                SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString,",");
                ArrayList vector = ArrayList.Synchronized(new ArrayList(10));
                while (tokenizer.hasMoreTokens())
                vector.add(tokenizer.nextToken());
                m_publicTypeStrings[i] = new String[vector.size()];
                for (int j = 0;j < vector.size();j++)
                    m_publicTypeStrings[i][j] = ((String)vector.get(j));
            }
        }
         
        return m_publicTypeStrings;
    }

    public String[][] getPublicTypeStrings() throws Exception {
        if (m_publicTypeStrings == null)
        {
            //if (m_tpAppRequest.m_connector.IsUsingOldLogicForPublicStatus())
            //{
            //    return getPublicTypeStringsOldLogic();
            //}
            m_publicTypeStrings = new String[ATTRIBUTE_PUBLIC_SORTING_TYPE.length];
            for (int i = 0;i < ATTRIBUTE_PUBLIC_SORTING_TYPE.length;i++)
            {
                String typeString = m_DefParser.getValue(SECTION_PUBLIC_SORTING,ATTRIBUTE_PUBLIC_SORTING_TYPE[i]);
                if (StringSupport.isNullOrEmpty(typeString))
                {
                    if (i == 1)
                        typeString = m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[1]);
                    else if (i == 2)
                        typeString = m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[3]);
                    else
                    {
                        typeString = m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[0]);
                        if (!StringSupport.isNullOrEmpty(m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[2])))
                            typeString = typeString + "," + m_DefParser.getValue(SECTION_SORTING,ATTRIBUTE_SORTING_TYPE[2]);
                         
                    }  
                }
                 
                SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString,",");
                ArrayList vector = ArrayList.Synchronized(new ArrayList(10));
                while (tokenizer.hasMoreTokens())
                vector.add(tokenizer.nextToken());
                m_publicTypeStrings[i] = new String[vector.size()];
                for (int j = 0;j < vector.size();j++)
                    m_publicTypeStrings[i][j] = ((String)vector.get(j));
            }
        }
         
        return m_publicTypeStrings;
    }

    public String getClientType() throws Exception {
        if (StringSupport.isNullOrEmpty(m_clientType))
        {
            m_clientType = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_TCPIP);
            String picClientType = m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_TCPIP_PICTURE);
            if (!StringSupport.isNullOrEmpty(picClientType) && (isGetPictureRequest() || isCheckCredential()))
                m_clientType = picClientType;
             
            m_clientType = StringSupport.Trim(m_clientType);
        }
         
        return m_clientType;
    }

    public String getRETSQueryParameter() throws Exception {
        return m_environment.getRETSQueryParameter();
    }

    //+-----------------------------------------------------------------------------------+
    //|								  Serialization										  |
    //+-----------------------------------------------------------------------------------+
    public void save(String filename) throws Exception {
        InputStream out_Renamed = null;
        InputStream outBuffer = null;
        //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
        System.IO.BinaryWriter objectOut = null;
        try
        {
            if (filename == null)
                return ;
             
            //UPGRADE_TODO: Constructor 'java.io.FileOutputStream.FileOutputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileOutputStreamFileOutputStream_javalangString'"
            out_Renamed = new FileStreamSupport(filename, FileMode.Create);
            outBuffer = new System.IO.BufferedStream(out_Renamed);
            //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
            objectOut = new System.IO.BinaryWriter(outBuffer);
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            SupportClass.serialize(objectOut,m_DefParser);
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            SupportClass.serialize(objectOut,m_PropertyFieldGroups);
            if (!StringSupport.isNullOrEmpty(m_DefParser.getMlsRecordsContent()))
                m_CmaFields.save(objectOut);
             
            if (!StringSupport.isNullOrEmpty(m_DefParser.getMlsRecordsExContent()))
                m_CmaFieldsEx.save(objectOut);
             
            BoardSetupGroup boardGroup = m_boardSetup.getGroup(0);
            SupportClass.serialize(objectOut,boardGroup);
            //SupportClass.Serialize(objectOut, m_boardSetup);//m_boardSetup.save(objectOut);
            m_commClient.save(objectOut);
        }
        catch (IOException exc)
        {
            throw createException(exc);
        }
        finally
        {
            //SupportClass.Serialize(objectOut, m_typeStrings);
            //SupportClass.Serialize(objectOut, m_publicTypeStrings);
            //objectOut.Write(ATTRIBUTE_SORTING_TYPE.Length);
            //for (int i = 0; i < ATTRIBUTE_SORTING_TYPE.Length; i++)
            //{
            //    objectOut.Write(m_typeStrings[i].Length);
            //    for (int j = 0; j < m_typeStrings[i].Length; j++)
            //    {
            //        //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            //        SupportClass.Serialize(objectOut, m_typeStrings[i][j]);
            //    }
            //}
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            //SupportClass.Serialize(objectOut, getResultsFolder());
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            //SupportClass.Serialize(objectOut, m_mls_folder);
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            //SupportClass.Serialize(objectOut, m_bin_folder==null?"":m_bin_folder);
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            // SupportClass.Serialize(objectOut, m_noPictureFilename == null ? "" : m_noPictureFilename);
            //objectOut.Write(m_noPictureFilenameInitialized);
            if (objectOut != null)
                try
                {
                    objectOut.Close();
                }
                catch (IOException e1)
                {
                }
            
             
            if (outBuffer != null)
                try
                {
                    outBuffer.close();
                }
                catch (IOException e2)
                {
                }
            
             
            if (out_Renamed != null)
                try
                {
                    out_Renamed.close();
                }
                catch (IOException e3)
                {
                }
            
             
        }
    }

    /**
    * 
    */
    public void load(String filename) throws Exception {
        InputStream in_Renamed = null;
        InputStream inBuffer = null;
        //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
        System.IO.BinaryReader objectIn = null;
        try
        {
            if (filename == null)
                return ;
             
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            in_Renamed = new FileStreamSupport(filename, FileMode.Open, FileAccess.Read);
            inBuffer = new System.IO.BufferedStream(in_Renamed);
            //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
            objectIn = new System.IO.BinaryReader(inBuffer);
            if (m_DefParser == null)
                m_DefParser = new DefParser();
             
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            m_DefParser = (DefParser)SupportClass.deserialize(objectIn);
            System.out.println("DEF Parser loaded");
            if (m_PropertyFieldGroups == null)
                m_PropertyFieldGroups = new PropertyFieldGroups();
             
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            m_PropertyFieldGroups = (PropertyFieldGroups)SupportClass.deserialize(objectIn);
            if (m_CmaFields == null)
            {
                m_CmaFields = new MLSCmaFields(this);
                if (!StringSupport.isNullOrEmpty(m_DefParser.getMlsRecordsContent()))
                    m_CmaFields.load(objectIn);
                 
            }
             
            if (m_CmaFieldsEx == null)
                m_CmaFieldsEx = new MLSCmaFields(this);
             
            if (!StringSupport.isNullOrEmpty(m_DefParser.getMlsRecordsExContent()))
                m_CmaFieldsEx.load(objectIn);
             
            if (m_CmaFieldsEx != null)
                m_CmaFieldsEx.initFeatureGroups();
             
            if (m_boardSetup == null)
                m_boardSetup = new BoardSetup(this);
             
            BoardSetupGroup boardGroup = (BoardSetupGroup)SupportClass.deserialize(objectIn);
            m_boardSetup.addGroup(boardGroup);
            m_commClient = ClientFactory.createClient(this,getClientType(),m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_DATASOURCE),m_DefParser.getValue(SECTION_COMMON,ATTRIBUTE_NAME_OF_ORIGIN));
            m_commClient.load(objectIn);
            //m_typeStrings = (string[][])SupportClass.Deserialize(objectIn);
            //m_publicTypeStrings = (string[][])SupportClass.Deserialize(objectIn);
            //m_typeStrings = new System.String[ATTRIBUTE_SORTING_TYPE.Length][];
            //for (int i = 0; i < ATTRIBUTE_SORTING_TYPE.Length; i++)
            //{
            //    System.String typeString = m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[i]);
            //    SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString, ",");
            //    System.Collections.ArrayList vector = System.Collections.ArrayList.Synchronized(new System.Collections.ArrayList(10));
            //    while (tokenizer.HasMoreTokens())
            //        vector.Add(tokenizer.NextToken());
            //    m_typeStrings[i] = new System.String[vector.Count];
            //    for (int j = 0; j < vector.Count; j++)
            //        m_typeStrings[i][j] = ((System.String)vector[j]);
            //}
            //m_publicTypeStrings = new System.String[ATTRIBUTE_PUBLIC_SORTING_TYPE.Length][];
            //for (int i = 0; i < ATTRIBUTE_PUBLIC_SORTING_TYPE.Length; i++)
            //{
            //    System.String typeString = m_DefParser.getValue(SECTION_PUBLIC_SORTING, ATTRIBUTE_PUBLIC_SORTING_TYPE[i]);
            //    if (string.IsNullOrEmpty(typeString))
            //    {
            //        if (i == 0 || i == 1)
            //            typeString = m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[i]);
            //        else
            //        {
            //            typeString = m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[2]);
            //            if (!string.IsNullOrEmpty(m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[3])))
            //                typeString = typeString + "," + m_DefParser.getValue(SECTION_SORTING, ATTRIBUTE_SORTING_TYPE[3]);
            //        }
            //    }
            //    SupportClass.Tokenizer tokenizer = new SupportClass.Tokenizer(typeString, ",");
            //    System.Collections.ArrayList vector = System.Collections.ArrayList.Synchronized(new System.Collections.ArrayList(10));
            //    while (tokenizer.HasMoreTokens())
            //        vector.Add(tokenizer.NextToken());
            //    m_publicTypeStrings[i] = new System.String[vector.Count];
            //    for (int j = 0; j < vector.Count; j++)
            //        m_publicTypeStrings[i][j] = ((System.String)vector[j]);
            //}
            // m_typeStrings = (System.String[][])SupportClass.Deserialize(objectIn);
            m_MlsRecordset = new MLSRecordSet();
        }
        catch (Exception exc)
        {
            //int len = objectIn.Read();
            //m_typeStrings = new System.String[len][];
            //for (int i = 0; i < len; i++)
            //{
            //    int array_len = objectIn.Read();
            //    m_typeStrings[i] = new System.String[array_len];
            //    for (int j = 0; j < array_len; j++)
            //    {
            //        //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            //        m_typeStrings[i][j] = ((System.String) SupportClass.Deserialize(objectIn));
            //    }
            //}
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            //SupportClass.Deserialize(objectIn); // previously we read m_moduleResultsFolder here, but now the implementation is changed to initialize it on demart (see function getResultsFolder). We left it in the file to avoid changing format.
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            //m_mls_folder = ((System.String) SupportClass.Deserialize(objectIn));
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            //m_bin_folder = ((System.String) SupportClass.Deserialize(objectIn));
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            //m_noPictureFilename = ((System.String) SupportClass.Deserialize(objectIn));
            //m_noPictureFilenameInitialized = objectIn.ReadBoolean();
            System.out.println(exc.getMessage());
            SupportClass.WriteStackTrace(exc, Console.Error);
            throw createException(exc);
        }
        finally
        {
            if (objectIn != null)
                try
                {
                    objectIn.Close();
                }
                catch (IOException e1)
                {
                }
            
             
            if (inBuffer != null)
                try
                {
                    inBuffer.close();
                }
                catch (IOException e2)
                {
                }
            
             
            if (in_Renamed != null)
                try
                {
                    in_Renamed.close();
                }
                catch (IOException e3)
                {
                }
            
             
        }
    }

    /**
    * Gets sql-file name for Ambiance System.
    * Actually, this is sql-script file
    */
    public String getAmbianceSetupFile() throws Exception {
        if (m_commClient != null)
            return m_DefParser.getValue(SECTION_TPONLINE,ATTRIBUTE_AMBIANCE_SETUP_FILE);
         
        return "";
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
        else //	return null;
        if (!File.Exists(file.FullName))
            return null;
          
        String result = file.toString();
        //UPGRADE_TODO: Method 'java.lang.System.getProperty' was converted to 'System.IO.Path.DirectorySeparatorChar.ToString' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javalangSystemgetProperty_javalangString'"
        String separator = String.valueOf(File.separatorChar);
        if (result.length() > 0 && !result.endsWith(separator))
            result += separator;
         
        return result;
    }

    //+-----------------------------------------------------------------------------------+
    //|                             MLS department notifications                          |
    //+-----------------------------------------------------------------------------------+
    private String m_NotificationBuffer = "";
    private boolean m_bBufferedNotification = false;
    public static final int SUPPORT_CODE_BAD_PORT_VALUE = 1;
    // param1 - port value from def-file
    public static final int SUPPORT_CODE_PORT_VALUE_TOO_BIG = 2;
    // param1 - port value from def-file
    public static final int SUPPORT_CODE_UNKNOWN_CONNECTION_TYPE = 3;
    // no params
    public static final int SUPPORT_CODE_BAD_SECTION_SYNTAX = 4;
    // param1 - section name; param2 - peace of section containing bad syntax
    public static final int SUPPORT_CODE_SCRIPT_ELSE_WITHOUT_IF = 5;
    // param1 - script name
    public static final int SUPPORT_CODE_SCRIPT_ENDIV_WITHOUT_IF = 6;
    // param1 - script name
    public static final int SUPPORT_CODE_SCRIPT_IF_WITHOUT_ENDIV = 7;
    // param1 - unclosed if-command; param2 - script name
    public static final int SUPPORT_CODE_SCRIPT_MISSING_PARAMETER = 8;
    // param1 - parameter number; param2 - script command; param3 - script name
    public static final int SUPPORT_CODE_SCRIPT_EXPECTING_INTEGER = 9;
    // param1 - parameter number; param2 - script command; param3 - script name
    public static final int SUPPORT_CODE_SCRIPT_UNKNOWN_COMMAND = 10;
    // param1 - unknown script command; param2 - script name
    public static final int SUPPORT_CODE_SCRIPT_BAD_PARAMETER = 11;
    // param1 - script command; param2 - script name
    public static final int SUPPORT_CODE_SCRIPT_UNKNOWN_LABEL = 12;
    // param1 - script goto command; param2 - script name
    public static final int SUPPORT_CODE_SCRIPT_NO_CURRENT_RECORD = 13;
    // param1 - script command; param2 - script name
    public static final int SUPPORT_CODE_HEADER_BAD_INDEX = 14;
    // param1 - field name; param2 - field index in the received data
    public static final int SUPPORT_CODE_HEADER_LOOKUP = 15;
    // param1 - field name; param2 - old header name; param3 - new header name (found in lookup table)
    public static final int SUPPORT_CODE_HEADER_NOT_FOUND = 16;
    // param1 - field name; param2 - header name
    public static final int SUPPORT_CODE_DOWNLOAD_LT = 17;
    // no params
    public static final int SUPPORT_CODE_METADATA_CHANGED = 18;
    // param1 - previous version; param2 - current version
    //UPGRADE_NOTE: Final was removed from the declaration of 'SUPPORT_MESSAGE '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    private static final String[] SUPPORT_MESSAGE = new String[]{ "'%0%' - invalid value for port number. Section '" + SECTION_TCPIP + "', attribute '" + ATTRIBUTE_IPPORT + "'.", "'%0%' - wrong port number (should be between 0 and 65535). Section '" + SECTION_TCPIP + "', attribute '" + ATTRIBUTE_IPPORT + "'.", "Unable to create communication client.", "Bad syntax in section %0%: '%1%'.", "'else' without 'if' in the %0%", "'endiv' without 'if' in the %0%", "Command '%0%' doesn't have the corresponding 'endiv' in the %1%", "Required parameter #%0% is missing from the script command '%1%' in the %2%.", "Parameter #%0% must be integer in the %2%’s command '%1%'.", "Unknown command '%0%' in the %1%", "Bad parameter format in %1%’s command: '%0%'", "Unknown label in GoTo statement: '%0%' in the %1%", "No current record for the command: '%0%' in the %1%", "Can't find field '%0%' in the received data. The data doesn't have a field with index %1%.", "The header name '%1%' is incorrect for the field '%0%'. After searching in lookup table, we have found that the correct header name is '%2%'.", "Can't find field '%0%' in the received data. The data doesn't have a header '%1%'. We also have failed to find suitable name in the lookup table.", "Unable to download MLS LookupTable", "Warning: RETS MetaData Version has been changed from '%0%' to '%1%'" };
    private void notifyTechSupport(String message) throws Exception {
    }

    //byte[] data = null;
    //System.IO.FileStream fis = null;
    //System.IO.BufferedStream bis = null;
    //try
    //{
    //    System.IO.FileInfo file = new System.IO.FileInfo(getResultsFilename());
    //    data = new byte[(int) SupportClass.FileLength(file)];
    //    //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javaioFile'"
    //    fis = new System.IO.FileStream(file.FullName, System.IO.FileMode.Open, System.IO.FileAccess.Read);
    //    bis = new System.IO.BufferedStream(fis);
    //    SupportClass.ReadInput(bis, data, 0, data.Length);
    //}
    //catch (System.Exception exc)
    //{
    //    // We should not allow exceptions to be thrown.
    //    // If we are unable to contact MLS department - it's bad, but not that bad to break
    //    // the normal flow.
    //    WriteLine("Unable read data file for tech support message.");
    //    data = null;
    //}
    //try
    //{
    //    m_environment.notifyTechSupport(message, data);
    //}
    //catch (System.Exception exc)
    //{
    //    // We should not allow exceptions to be thrown.
    //    // If we are unable to contact MLS department - it's bad, but not that bad to break
    //    // the normal flow.
    //    WriteLine("Unable to notify tech support about the def-file problem: " + message);
    //}
    private void notifyTechSupport(String message, int errorCode) throws Exception {
    }

    //sbyte[] data = null;
    //switch (errorCode)
    //{
    //    case SUPPORT_CODE_HEADER_BAD_INDEX:
    //    case SUPPORT_CODE_HEADER_LOOKUP:
    //    case SUPPORT_CODE_HEADER_NOT_FOUND:
    //    {
    //        System.IO.FileStream fis = null;
    //        System.IO.BufferedStream bis = null;
    //        try
    //        {
    //            System.IO.FileInfo file = new System.IO.FileInfo(getResultsFilename());
    //            data = new sbyte[(int) SupportClass.FileLength(file)];
    //            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javaioFile'"
    //            fis = new System.IO.FileStream(file.FullName, System.IO.FileMode.Open, System.IO.FileAccess.Read);
    //            bis = new System.IO.BufferedStream(fis);
    //            SupportClass.ReadInput(bis, data, 0, data.Length);
    //        }
    //        catch (System.Exception exc)
    //        {
    //            // We should not allow exceptions to be thrown.
    //            // If we are unable to contact MLS department - it's bad, but not that bad to break
    //            // the normal flow.
    //            WriteLine("Unable read data file for tech support message.");
    //            data = null;
    //        }
    //        break;
    //    }
    //    }
    //try
    //{
    //    if (m_bBufferedNotification)
    //    {
    //        m_NotificationBuffer += "Error #";
    //        m_NotificationBuffer += errorCode;
    //        m_NotificationBuffer += " ";
    //        m_NotificationBuffer += message;
    //        m_NotificationBuffer += "\r\n";
    //    }
    //    else
    //        m_environment.notifyTechSupport("Error #" + errorCode + " " + message, data);
    //}
    //catch (System.Exception exc)
    //{
    //    // We should not allow exceptions to be thrown.
    //    // If we are unable to contact MLS department - it's bad, but not that bad to break
    //    // the normal flow.
    //    WriteLine("Unable to notify tech support about the def-file problem: " + message);
    //}
    public void enableBufferedNotification() throws Exception {
        m_NotificationBuffer = "";
        m_bBufferedNotification = true;
    }

    public void disableBufferedNotification() throws Exception {
        if (m_NotificationBuffer.length() > 0)
            notifyTechSupport(m_NotificationBuffer);
         
        m_NotificationBuffer = "";
        m_bBufferedNotification = false;
    }

    /**
    * Sends a notification about the errors in def-files to MLS department.
    *  @param errorCode one of the MLSEngine.SUPPORT_MESSAGE_XXX constants.
    */
    public void notifyTechSupport(int errorCode) throws Exception {
        notifyTechSupport(SUPPORT_MESSAGE[errorCode - 1],errorCode);
    }

    /**
    * Sends a notification about the errors in def-files to MLS department.
    *  @param errorCode one of the MLSEngine.SUPPORT_MESSAGE_XXX constants.
    * 
    *  @param param1 a string parameter to replace token '%0%' in support message.
    */
    public void notifyTechSupport(int errorCode, String param1) throws Exception {
        notifyTechSupport(MLSUtil.formatString(SUPPORT_MESSAGE[errorCode - 1],param1),errorCode);
    }

    /**
    * Sends a notification about the errors in def-files to MLS department.
    *  @param errorCode one of the MLSEngine.SUPPORT_MESSAGE_XXX constants.
    * 
    *  @param param1 a string parameter to replace token '%0%' in support message.
    * 
    *  @param param2 a string parameter to replace token '%1%' in support message.
    */
    public void notifyTechSupport(int errorCode, String param1, String param2) throws Exception {
        notifyTechSupport(MLSUtil.formatString(SUPPORT_MESSAGE[errorCode - 1],param1,param2),errorCode);
    }

    /**
    * Sends a notification about the errors in def-files to MLS department.
    *  @param errorCode one of the MLSEngine.SUPPORT_MESSAGE_XXX constants.
    * 
    *  @param param1 a string parameter to replace token '%0%' in support message.
    * 
    *  @param param2 a string parameter to replace token '%1%' in support message.
    * 
    *  @param param3 a string parameter to replace token '%2%' in support message.
    */
    public void notifyTechSupport(int errorCode, String param1, String param2, String param3) throws Exception {
        notifyTechSupport(MLSUtil.formatString(SUPPORT_MESSAGE[errorCode - 1],param1,param2,param3),errorCode);
    }

    //+-----------------------------------------------------------------------------------+
    //|                                     Exceptions                                    |
    //+-----------------------------------------------------------------------------------+
    public MLSException createException(int code) throws Exception {
        return m_environment.createException(code);
    }

    /**
    * Sets exception with special message in special format.
    *  @param exc exception which caused throwing of this MLSException.
    * 
    *  @param message Error message
    * 
    *  @param developer_message An info that should be added to developer message.
    * 
    *  @param format format of the message. Currently: TEXT, or HTML.
    */
    public MLSException createException(Exception exc, String message, String developer_message, int format) throws Exception {
        return m_environment.createException(exc,message,developer_message,format);
    }

    public MLSException createException(Exception exc, int code, String message, String developer_message, int format) throws Exception {
        return m_environment.createException(exc,code,message,developer_message,format);
    }

    /**
    * Sets exception with special message in special format.
    *  @param message Error message
    * 
    *  @param developer_message An info that should be added to developer message.
    * 
    *  @param format format of the message. Currently: TEXT, or HTML.
    */
    public MLSException createException(String message, String developer_message, int format) throws Exception {
        return m_environment.createException(null,message,developer_message,format);
    }

    public MLSException createException(String message, int code, String developer_message, int format) throws Exception {
        return m_environment.createException(null,code,message,developer_message,format);
    }

    /**
    * Sets exception with special message
    *  @param message Error message
    * 
    *  @param developer_message An info that should be added to developer message.
    */
    public MLSException createException(String message, String developer_message) throws Exception {
        return m_environment.createException(null,message,developer_message,MLSException.FORMAT_TEXT);
    }

    public MLSException createException(int code, String message, String developer_message) throws Exception {
        return m_environment.createException(null,code,message,developer_message,MLSException.FORMAT_TEXT);
    }

    /**
    * Sets exception with special message
    *  @param exc exception which caused throwing of this MLSException.
    * 
    *  @param message Error message
    * 
    *  @param developer_message An info that should be added to developer message.
    */
    public MLSException createException(Exception exc, String message, String developer_message) throws Exception {
        return m_environment.createException(exc,message,developer_message,MLSException.FORMAT_TEXT);
    }

    public MLSException createException(Exception exc, int code, String message, String developer_message) throws Exception {
        return m_environment.createException(exc,code,message,developer_message,MLSException.FORMAT_TEXT);
    }

    /**
    * Sets exception with special message in special format.
    *  @param exc exception which caused throwing of this MLSException.
    * 
    *  @param message Error message
    * 
    *  @param format format of the message. Currently: TEXT, or HTML.
    */
    public MLSException createException(Exception exc, String message, int format) throws Exception {
        return m_environment.createException(exc,message,null,format);
    }

    public MLSException createException(Exception exc, int code, String message, int format) throws Exception {
        return m_environment.createException(exc,code,message,null,format);
    }

    /**
    * Sets exception with special message in special format.
    *  @param message Error message
    * 
    *  @param format format of the message. Currently: TEXT, or HTML.
    */
    public MLSException createException(String message, int format) throws Exception {
        return m_environment.createException(null,message,null,format);
    }

    public MLSException createException(int code, String message, int format) throws Exception {
        return m_environment.createException(null,code,message,null,format);
    }

    /**
    * Sets exception with special message
    *  @param message Error message
    */
    public MLSException createException(String message) throws Exception {
        return m_environment.createException(null,message,null,MLSException.FORMAT_TEXT);
    }

    public MLSException createException(int code, String message) throws Exception {
        return m_environment.createException(null,code,message,null,MLSException.FORMAT_TEXT);
    }

    /**
    * Sets exception with special message
    *  @param exc exception which caused throwing of this MLSException.
    * 
    *  @param message Error message
    */
    public MLSException createException(Exception exc, String message) throws Exception {
        return m_environment.createException(exc,message,null,MLSException.FORMAT_TEXT);
    }

    public MLSException createException(Exception exc, int code, String message) throws Exception {
        return m_environment.createException(exc,code,message,null,MLSException.FORMAT_TEXT);
    }

    /**
    * @param exc exception which caused throwing of this MLSException.
    */
    public MLSException createException(Exception exc) throws Exception {
        return m_environment.createException(exc,null,null,MLSException.FORMAT_TEXT);
    }

    public MLSException createException(Exception exc, int code) throws Exception {
        return m_environment.createException(exc,code,null,null,MLSException.FORMAT_TEXT);
    }

    //+-----------------------------------------------------------------------------------+
    //|                                      Logging                                      |
    //+-----------------------------------------------------------------------------------+
    /// <summary> Sets communication log mode. This is all about receiving data. The parts
    /// outside the functions runMainScript and runPictureScript always use standard
    /// application log.
    /// </summary>
    /// <param name="mode">eather LOG_NO, LOG_APP_LOG, LOG_COMM_LOG.
    /// <br>LOG_NO turns logging off (default).
    /// <br>LOG_APP_LOG communication modules write to a standard application log
    /// (System.out)
    /// <br>LOG_COMM_LOG communication modules write to the file communication.log
    /// in the results folder of this board.
    /// </param>
    public void setLogMode(int mode) throws Exception {
        if (mode != m_logMode)
        {
            if (m_log != null)
                m_log.close();
             
            switch(mode)
            {
                case LOG_APP_LOG: 
                    m_log = null;
                    m_logMode = LOG_APP_LOG;
                    break;
                case LOG_COMM_LOG: 
                    m_log = new Log(getResultsFolder() + "communication.log");
                    m_logMode = LOG_COMM_LOG;
                    break;
                case LOG_NO: 
                default: 
                    m_log = new Log();
                    m_logMode = LOG_NO;
                    break;
            
            }
        }
         
    }

    /// <returns> eather LOG_NO, LOG_APP_LOG, LOG_COMM_LOG.
    /// <br>LOG_NO turns logging off (default).
    /// <br>LOG_APP_LOG communication modules write to a standard application log
    /// (System.out)
    /// <br>LOG_COMM_LOG communication modules write to the file communication.log
    /// in the results folder of this board.
    /// </returns>
    public int getLogMode() throws Exception {
        return m_logMode;
    }

    /**
    * ///////////////////////////////////////////////////////////////////////////
    */
    public PropertyFieldGroup[] getPropertyFieldGroups() throws Exception {
        if (m_PropertyFieldGroups == null || m_PropertyFieldGroups.size() == 0)
            return null;
         
        return m_PropertyFieldGroups.toArray();
    }

    /**
    * 
    */
    public PropertyFieldGroup getPropertyFieldGroup(int index) throws Exception {
        if (m_PropertyFieldGroups == null)
            return null;
         
        return m_PropertyFieldGroups.getGroup(index);
    }

    /**
    * 
    */
    public PropertyFieldGroup getCurrentPropertyFieldGroup() throws Exception {
        if (m_PropertyFieldGroups == null)
            return null;
         
        return m_PropertyFieldGroups.getCurrectGroup();
    }

    /**
    * 
    */
    public int getCurrentGroupIndex() throws Exception {
        return m_PropertyFieldGroups.getCurrentGroupIndex();
    }

    /**
    * @return s the index of the default group. Default group is set by def-file.
    * If def file doesn't specify default group, it is set to "Advanced". If "Advanced" group
    * doesn't exist, it is 0.
    */
    public int getDefaultGroupIndex() throws Exception {
        return m_PropertyFieldGroups.getDefaulGroupIndex();
    }

    /**
    * set up current group by index
    */
    public void setCurrentGroup(int index) throws Exception {
        m_PropertyFieldGroups.setCurrentGroup(index);
    }

    /**
    * Set current group by name. 
    *  @param name group name
    * 
    *  @return  true on success; false if group with this name doesn't exist.
    */
    public boolean setCurrentGroup(String name) throws Exception {
        int index = m_PropertyFieldGroups.getGroupIndex(name);
        if (index != -1)
        {
            m_PropertyFieldGroups.setCurrentGroup(index);
            return true;
        }
         
        return false;
    }

    /**
    * load and save the setting of Import Picture check box for Broker MLS import
    */
    public boolean isImportPictureChecked() throws Exception {
        return m_importPictureChecked;
    }

    /**
    * load and save the setting of Import Picture check box for Broker MLS import
    */
    public void setImportPictureCheckBox(boolean value_Renamed) throws Exception {
        m_importPictureChecked = value_Renamed;
    }

    /// <summary> setOverallProgress limits the progress bar in some range. All calls of setProgress
    /// chenge progress bar value in this range - not in the whole progress bar range.
    /// <p>Note that it doesn't set progress bar position. You should call setProgress for that.
    /// <p>The step for overal progress should always be one.
    /// Which means that this function must be called maxValue+1 times from 0 to maxValue in the
    /// order.
    /// <br>The right way:
    /// <br>setOverallProgress ( 0, 3 );
    /// <br>...
    /// <br>setOverallProgress ( 1, 3 );
    /// <br>...
    /// <br>setOverallProgress ( 2, 3 );
    /// <br>...
    /// <br>setOverallProgress ( 3, 3 );
    /// <br>
    /// <br>Wrong way:
    /// <br>setOverallProgress ( 0, 3 );
    /// <br>...
    /// <br>setOverallProgress ( 2, 3 ); // wrong should always be increased by 1.
    /// <br>...
    /// <br>setOverallProgress ( 1, 3 ); // wrong should always go in increment order.
    /// <br>...
    /// <br>setOverallProgress ( 3, 3 ); // wrong should always be increased by 1.
    /// </summary>
    /// <param name="value">overal progress value. Must be >= 0 and <= maxValue
    /// </param>
    /// <param name="maxValue">maximum overal value. Must be > 0
    /// </param>
    public void setOverallProgress(int value_Renamed, int maxValue) throws Exception {
        m_progressStartValue = (value_Renamed * OVERAL_PROGRESS_MAX_VALUE) / maxValue;
        m_progressFinishValue = ((value_Renamed + 1) * OVERAL_PROGRESS_MAX_VALUE) / maxValue;
    }

    public void setProgress(int value_Renamed, int maxValue) throws Exception {
        m_environment.setProgress(m_progressStartValue + ((m_progressFinishValue - m_progressStartValue) * value_Renamed) / maxValue,OVERAL_PROGRESS_MAX_VALUE);
    }

    public void setMessage(String message) throws Exception {
        m_environment.setMessage(message);
    }

}


//+-----------------------------------------------------------------------------------+
//|                               Debuging functions                                  |
//+-----------------------------------------------------------------------------------+
//public virtual void  _debug_prepareMlsRecords(System.String filename)
//{
//    if (m_commClient != null)
//        m_commClient.prepareMlsRecords(filename);
//}
/*	private void finalizeMainScript () throws MLSException
		{
		if ( m_commClient != null )
		{
		m_commClient.finalizeMainScript ();
		if ( m_log != null )
		m_log.stop();
		}
		}
		
		private void endMainScript () throws MLSException
		{
		if ( m_commClient != null )
		{
		try
		{
		m_commClient.endMainScript ();
		
		m_environment.checkStop ();
		m_commClient.prepareMlsRecords ( filename );
		
		m_environment.checkStop ();
		if ( m_MlsRecordset.getRecordCount() != 0 )
		{
		int clientFlags = m_commClient.getFlags();
		
		if ( ( flags & MSFLAG_NO_REM_SCRIPT ) != MSFLAG_NO_REM_SCRIPT )
		{
		if (
		( flags & MSFLAG_RUN_REM_SCRIPT ) == MSFLAG_RUN_REM_SCRIPT ||
		( clientFlags & CommunicationClient.FLAG_REMSCRIPT_INSIDE_MAINSCRIPT ) == CommunicationClient.FLAG_REMSCRIPT_INSIDE_MAINSCRIPT )
		{
		m_commClient.runRemScript ( new MLSRecords ( m_MlsRecordset, MLSRecords.FILTER_ALL, null ) );
		}
		}
		
		m_environment.checkStop ();
		if ( ( flags & MSFLAG_NO_PIC_SCRIPT ) != MSFLAG_NO_PIC_SCRIPT )
		{
		if (
		( flags & MSFLAG_RUN_PIC_SCRIPT ) == MSFLAG_RUN_PIC_SCRIPT ||
		( clientFlags & CommunicationClient.FLAG_PICSCRIPT_INSIDE_MAINSCRIPT ) == CommunicationClient.FLAG_PICSCRIPT_INSIDE_MAINSCRIPT )
		{
		MLSRecords records = new MLSRecords ( m_MlsRecordset, MLSRecords.FILTER_MISSING_PICTURES, null );
		try
		{
		m_commClient.runPicScript ( records );
		}
		finally
		{
		// update 'picture downloaded' flag for all checked records
		// Sergei: think about moving this functionality to CommunicationClient
		// Refresh must be done in 'finally' block, because some pictures could be
		// downloaded successfuly even if one of them failed
		setMLSRecordsPictureState ( records, MLSRecord.PICTURE_DOWNLOADED_FINALY );
		}
		m_environment.checkStop ();
		}
		}
		
		m_MlsRecordset.save ( this );
		}
		}
		finally
		{
		finalizeMainScript ();
		}
		}
		}
		
		public MLSCommand startMainScript () throws MLSException
		{
		if ( m_commClient != null )
		{
		MLSCommand command = null;
		
		setOverallProgress ( 0, 1 );
		setProgress ( 0, 100 );
		setMessage ( STRINGS.get ( STRINGS.MLS_ENGINE_WAITING_FOR_PREV_SCRIPT ) );
		
		if ( m_log != null )
		m_log.start();
		
		try
		{
		System.out.println ();
		System.out.println ( "+-----------------------------------------------------------------------------------+" );
		System.out.println ( "|                        Downloading records (main script)                          |" );
		System.out.println ( "+-----------------------------------------------------------------------------------+" );
		System.out.println ();
		
		m_environment.setMessage ( STRINGS.get ( STRINGS.COMMON_EXECUTING_SCRIPT, 0 ) );
		if ( m_PropertyFieldGroups != null )
		m_PropertyFieldGroups.save ( getPropertyFieldsFilename() );
		
		if ( ( flags & MSFLAG_ADD_TO_PREV ) == MSFLAG_ADD_TO_PREV )
		preparePrevMlsRecords ();
		else
		m_MlsRecordset.clear ();
		
		String filename = getResultsFilename();
		m_environment.checkStop ();
		
		command = m_commClient.startMainScript ();
		}
		catch ( MLSException exc )
		{
		finalizeMainScript ();
		throw exc;
		}
		catch ( Exception e )
		{
		finalizeMainScript ();
		throw createException ( e );
		}
		
		if ( command == null )
		endMainScript ();
		}
		return command;
		}
		
		public MLSCommand stepMainScript ()
		{
		MLSCommand command = null;
		try
		{
		command = m_commClient.stepMainScript ();
		}
		catch ( MLSException exc )
		{
		finalizeMainScript ();
		throw exc;
		}
		catch ( Exception e )
		{
		finalizeMainScript ();
		throw createException ( e );
		}
		if ( command == null )
		endMainScript ();
		return command;
		}
		
		public MLSCommand initPicScript ( MLSRecords records )
		{
		setOverallProgress ( 0, 1 );
		setProgress ( 0, 100 );
		setMessage ( STRINGS.get ( STRINGS.MLS_ENGINE_WAITING_FOR_PREV_SCRIPT ) );
		
		synchronized ( this )
		{
		if ( m_log != null )
		m_log.start();
		try
		{
		System.out.println ();
		System.out.println ( "+-----------------------------------------------------------------------------------+" );
		System.out.println ( "|                       Downloading pictures (picture script)                       |" );
		System.out.println ( "+-----------------------------------------------------------------------------------+" );
		System.out.println ();
		
		m_environment.setMessage ( STRINGS.get ( STRINGS.COMMON_EXECUTING_SCRIPT, 1 ) );
		if (
		m_commClient != null &&
		( m_commClient.getFlags() & m_commClient.FLAG_PICSCRIPT_INSIDE_MAINSCRIPT ) == 0 )
		{
		try
		{
		m_commClient.runPicScript ( records );
		}
		finally
		{
		// update 'picture downloaded' flag for all checked records
		// Sergei: think about moving this functionality to CommunicationClient
		
		// refresh must be done in 'finally' block, because some pictures could be
		// downloaded successfuly even if one of them failed
		setMLSRecordsPictureState ( records, MLSRecord.PICTURE_DOWNLOADED );
		//MLSRecords.FILTER_FLAG_ANY_TYPE | MLSRecords.FILTER_FLAG_CHECKED | MLSRecords.FILTER_FLAG_MISSING_PICTURES,
		//MLSRecord.PICTURE_DOWNLOADED );
		}
		m_environment.checkStop ();
		
		m_MlsRecordset.save ( this );
		}
		}
		finally
		{
		if ( m_log != null )
		m_log.stop();
		}
		}
		}
		
		public MLSCommand initRemScript ( MLSRecords records )
		{
		}
		
		public MLSCommand nextCommand ()
		{
		return m_commClient.nextCommand ();
		}
		
		public MLSCommand getCurrentCommand ()
		{
		}
		
		public String getVariableValue ( String varName )
		{
		}*/