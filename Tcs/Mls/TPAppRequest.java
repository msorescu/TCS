//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:34:03 PM
//

package Tcs.Mls;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.JavaSupport.language.RefSupport;
import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DateTZ;
import CS2JNet.System.DoubleSupport;
import CS2JNet.System.StringSplitOptions;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.Xml.XmlNode;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.TimeZone;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.GetAgentRoster;
import Tcs.Mls.Request.GetComparableListings;
import Tcs.Mls.Request.GetDataAggListings;
import Tcs.Mls.Request.GetIDXListings;
import Tcs.Mls.Request.GetMLSPlusStandardListings;
import Tcs.Mls.Request.GetOfficeRoster;
import Tcs.Mls.Request.GetOpenHouse;
import Tcs.Mls.Request.GetPictures;
import Tcs.Mls.Request.GetRecordCount;
import Tcs.Mls.Request.GetRecordCountAgentRoster;
import Tcs.Mls.Request.GetRecordCountDataagg;
import Tcs.Mls.Request.GetRecordCountOfficeRoster;
import Tcs.Mls.Request.GetRecordCountOpenHouse;
import Tcs.Mls.Request.GetRecordCountPlus;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;

public class TPAppRequest   
{
    public static final String ATTRIBUTE_FORMAT = "Format";
    protected public static final String STANDARD_DATEFORMAT = "MM/dd/yyyy";
    protected public static final String STANDARD_DATETIMEFORMAT = "MM/dd/yyyyTHH:mm:ss";
    protected public static final String STANDARD_DATETIMEFORMAT_START = "MM/dd/yyyyTHH:mm:00";
    protected public static final String STANDARD_DATETIMEFORMAT_START_SECOND = "MM/dd/yyyyTHH:mm:ss";
    protected public static final String STANDARD_DATETIMEFORMAT_END = "MM/dd/yyyyTHH:mm:59";
    protected public static final String STANDARD_DATETIMEFORMAT_END_SECOND = "MM/dd/yyyyTHH:mm:ss";
    protected public static final String STANDARD_DATETIMEFORMAT_START_MILLISECOND = "MM/dd/yyyyTHH:mm:ss.fff";
    protected public static final String STANDARD_DATETIMEFORMAT_END_MILLISECOND = "MM/dd/yyyyTHH:mm:ss.fff";
    private static final String AttributeReturnMlsNumberOnly = "ReturnMlsNumberOnly";
    //UPGRADE_NOTE: Final was removed from the declaration of 'STANDERD_STATUS'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public String[] STANDERD_STATUS = new String[]{ "A", "S", "P", "E" };
    protected static final String STANDERD_PROPERTY_TYPE = "Single-family,Condo,Town-Home,Co-Op,Multifamily,Manufactured/Mobile Home,Land,Lot,Farm or Ranch,Recreational,Commercial,Investment,Industrial,Villa,";
    protected static final String CHUNKING_DELIMITER = ":|";
    protected static final int BUFFER_MAX_SIZE = 40000;
    protected static final int MAX_MLS_NO_SEARCH = 100;
    protected static final int MATCH_NONE = 0;
    protected static final int MATCH_ALL = 1;
    protected static final int MATCH_FIRST = 2;
    protected static final int MATCH_SECOND = 3;
    public static final int FLAG_NONE_PICTURE = 0;
    public static final int FLAG_ONE_PICTURE = 1;
    public static final int FLAG_MULTIPLE_PICTURE = 2;
    protected static final int MLS = 0;
    public static final int STANDARD = 1;
    private boolean __IsReturnMlsNumberOnlyRequest;
    public boolean getIsReturnMlsNumberOnlyRequest() {
        return __IsReturnMlsNumberOnlyRequest;
    }

    public void setIsReturnMlsNumberOnlyRequest(boolean value) {
        __IsReturnMlsNumberOnlyRequest = value;
    }

    protected public static final String FAKE_RECORDID = "000000";
    protected public static final int INTERNAL_FIELD_NOT_IN_DEF = 99999;
    protected public static final String[] STD_SEARCH_FIELD = new String[]{ "ST_MLSNo", "ST_PType", "ST_PType", "ST_Status", "ST_Zip", "ST_Beds", "ST_Fbaths", "ST_SqFt", "ST_Area", "ST_ListDate", "ST_ListPr", "ST_SaleDate", "ST_SalePrice", "ST_SearchPrice", "ST_StatusDate", "ST_Lat", "ST_Long", "ST_ListAgentID", "ST_ListBrokerID", "ST_TBaths", "ST_Subdivision", "ST_SaleOrLease", "ST_PendingDate", "ST_ExpiredDate", "ST_InactiveDate", "ST_PostalFSA", "ST_LastMod", "ST_PicMod", "ST_PublicListingStatus", "ST_ConditionCode", "ST_SearchDate" };
    protected public static final int ST_MLSNUMBER = 0;
    protected public static final int ST_PROPERTYTYPE = 1;
    protected public static final int ST_PROPERTYSUBTYPE = 2;
    protected public static final int ST_STATUS = 3;
    protected public static final int ST_ZIP = 4;
    protected public static final int ST_BEDS = 5;
    protected public static final int ST_FULLBATHS = 6;
    protected public static final int ST_SQFT = 7;
    protected public static final int ST_AREA = 8;
    protected public static final int ST_LISTDATE = 9;
    protected public static final int ST_LISTPRICE = 10;
    protected public static final int ST_SALEDATE = 11;
    protected public static final int ST_SALEPRICE = 12;
    protected public static final int ST_SEARCHPRICE = 13;
    protected public static final int ST_STATUSDATE = 14;
    protected public static final int ST_LAT = 15;
    protected public static final int ST_LONG = 16;
    protected public static final int ST_AGENTID = 17;
    protected public static final int ST_OFFICEID = 18;
    protected public static final int ST_TOTALBATHS = 19;
    protected public static final int ST_SUBDIVISION = 20;
    protected public static final int ST_SALEORLEASE = 21;
    protected public static final int ST_PENDINGDATE = 22;
    protected public static final int ST_EXPIREDDATE = 23;
    protected public static final int ST_INACTIVEDATE = 24;
    protected public static final int ST_PostalFSA = 25;
    protected public static final int ST_LastModified = 26;
    protected public static final int ST_PicModified = 27;
    protected public static final int ST_PublicListingStatus = 28;
    protected public static final int ST_ConditionCode = 29;
    protected public static final int ST_SearchDate = 30;
    protected public static final int ST_FIELDS_COUNT = 31;
    protected public static int[] m_resultField = new int[]{ Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPENORM, Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPEMLS, Tcs.Mls.TCSStandardResultFields.STDF_DEFTYPE_NODEFNAME, Tcs.Mls.TCSStandardResultFields.STDF_RECORDID, Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIER, Tcs.Mls.TCSStandardResultFields.STDF_CMAHOUSENO, Tcs.Mls.TCSStandardResultFields.STDF_CMASTREETNAME, Tcs.Mls.TCSStandardResultFields.STDF_CMASUITENO, Tcs.Mls.TCSStandardResultFields.STDF_TPOCITY, Tcs.Mls.TCSStandardResultFields.STDF_TPOSTATE, Tcs.Mls.TCSStandardResultFields.STDF_TPOZIP, Tcs.Mls.TCSStandardResultFields.STDF_STDFNUMUNITS, Tcs.Mls.TCSStandardResultFields.STDF_CMABEDROOMS, Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMS, Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM, Tcs.Mls.TCSStandardResultFields.STDF_STDFFULLBATHROOMS, Tcs.Mls.TCSStandardResultFields.STDF_STDFTHREEQBATHROOMS, Tcs.Mls.TCSStandardResultFields.STDF_STDFHALFBATHROOMS, Tcs.Mls.TCSStandardResultFields.STDF_STDFQBATHROOMS, Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZE, Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM, Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET, Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIES, Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIESNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMAHOUSESTYLE, Tcs.Mls.TCSStandardResultFields.STDF_CMAGARAGE, Tcs.Mls.TCSStandardResultFields.STDF_CMAAREA, Tcs.Mls.TCSStandardResultFields.STDF_STDFSUBDIVISION, Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE, Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM, Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNT, Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNTNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMATAXYEAR, Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENT, Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENTNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGDATE, Tcs.Mls.TCSStandardResultFields.STDF_CMADAYSONMARKET, Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGPRICE, Tcs.Mls.TCSStandardResultFields.STDF_CMASALEDATE, Tcs.Mls.TCSStandardResultFields.STDF_CMASALEPRICE, Tcs.Mls.TCSStandardResultFields.STDF_STDFSTATUSDATE, Tcs.Mls.TCSStandardResultFields.STDF_STDFLAT, Tcs.Mls.TCSStandardResultFields.STDF_STDFLONG, Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE, Tcs.Mls.TCSStandardResultFields.STDF_NOTES, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTID, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTBROKERID, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICENAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTFNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTLNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFAGENTREMARKS, Tcs.Mls.TCSStandardResultFields.STDF_STDFVIRTUALTOURURL, Tcs.Mls.TCSStandardResultFields.STDF_STDFELEMSCHOOL, Tcs.Mls.TCSStandardResultFields.STDF_STDFMIDDLESCHOOL, Tcs.Mls.TCSStandardResultFields.STDF_STDFHIGHSCHOOL, Tcs.Mls.TCSStandardResultFields.STDF_STDFVIEWS, Tcs.Mls.TCSStandardResultFields.STDF_STDFWATERFRONTYN, Tcs.Mls.TCSStandardResultFields.STDF_STDFWATERFRONTDESC, Tcs.Mls.TCSStandardResultFields.STDF_STDFLOTDESC, Tcs.Mls.TCSStandardResultFields.STDF_STDFDIRECTIONS, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTPHONE, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTEMAIL, Tcs.Mls.TCSStandardResultFields.STDF_PICID, Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD, Tcs.Mls.TCSStandardResultFields.STDF_STDFSEARCHPRICE, Tcs.Mls.TCSStandardResultFields.STDF_STDFSCHOOLDISTRICT, Tcs.Mls.TCSStandardResultFields.STDF_STDFSALEORLEASE, Tcs.Mls.TCSStandardResultFields.STDF_STDFPENDINGDATE, Tcs.Mls.TCSStandardResultFields.STDF_STDFEXPIREDDATE, Tcs.Mls.TCSStandardResultFields.STDF_STDFINACTIVEDATE, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTID, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERBROKERID, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYEROFFICENAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTFNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTLNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTPHONE, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTEMAIL, Tcs.Mls.TCSStandardResultFields.STDF_STDFAPNNUMBER, Tcs.Mls.TCSStandardResultFields.STDF_STDFCOUNTY, Tcs.Mls.TCSStandardResultFields.STDF_STDFZONING, Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDSHORTSALE, Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDAUCTION, Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDFORECLOSEDREO, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOAGENTFIRSTNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOAGENTLASTNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOAGENTCONTACTPHONE, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOAGENTASSOCIATION, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOAGENTEMAIL, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOAGENTFIRSTNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOAGENTLASTNAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOAGENTCONTACTPHONE, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOAGENTASSOCIATION, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOAGENTEMAIL, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOOFFICENAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOOFFICENAME, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGAGENTDESIGNATION, Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTINGCOAGENTDESIGNATION, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTDESIGNATION, Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERCOAGENTDESIGNATION };
    //TCSStandardResultFields.STDF_STDFRENTINCOME,
    //TCSStandardResultFields.STDF_STDFOWNERFNAME,
    //TCSStandardResultFields.STDF_STDFOWNERLNAME,
    //protected internal int[] SearchIDFields = new int[]
    //{
    //    TCSStandardResultFields.STDF_STDFBUYERAGENTID,
    //    TCSStandardResultFields.STDF_STDFLISTAGENTID,
    //    TCSStandardResultFields.STDF_STDFBUYERBROKERID,
    //    TCSStandardResultFields.STDF_STDFLISTBROKERID
    //};
    //UPGRADE_NOTE: Final was removed from the declaration of 'RETS_ERROR_CODES'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    protected public static final String[] RETS_ERROR_CODES = new String[]{ "20200", "20202", "20203", "20206", "20207", "20208", "20209", "20210", "20514", "20400", "20401", "20402", "20406", "20407", "20408", "20409", "20410", "20411", "20412", "20413" };
    protected public TCServer m_tcs = null;
    protected public XmlNode m_defList;
    protected public XmlNode m_propertyDefList;
    protected public MLSConnector m_connector = null;
    protected public net.toppro.components.mls.engine.MLSEngine m_engine;
    protected public net.toppro.components.mls.engine.MLSEngine m_agentEngine;
    protected public net.toppro.components.mls.engine.MLSEngine m_officeEngine;
    protected public MLSRecords m_records = null;
    protected public StringBuilder m_resultBuffer = new StringBuilder(65536);
    protected public StringBuilder m_resultNote = new StringBuilder();
    protected public String m_status = "A";
    protected public Hashtable m_searchFields = Hashtable.Synchronized(new Hashtable());
    protected Hashtable m_standardSearchFields = new Hashtable();
    protected public boolean m_isSearchOneDef = false;
    protected public int m_noteId = 1;
    protected public boolean m_isAddWarningNote = false;
    protected public int m_warningNoteId = 0;
    protected public boolean m_isSearchByMLSNumber = false;
    protected String[] m_defStatus = new String[4];
    protected int m_currentSearch = 1;
    public int getCurrentSearchNumber() throws Exception {
        return m_currentSearch;
    }

    public void setCurrentSearchNumber(int value) throws Exception {
        m_currentSearch = value;
    }

    public String m_mlsNumber;
    protected String m_dateFormat;
    protected static final int MAXIMUM_RESUTL_SIZE = 3000000;
    protected public boolean m_requestSucess = false;
    protected String[] m_dataStandardNote = new String[Tcs.Mls.TCSStandardResultFields.STANDARD_FIELDS_COUNT];
    protected int m_overrideLimitCount = 0;
    protected int m_overrideLimit = -1;
    protected boolean m_isOverrideLimit = false;
    protected HashMap<String,MLSRecord> m_agentRecords = new HashMap<String,MLSRecord>();
    protected HashMap<String,MLSRecord> m_officeRecords = new HashMap<String,MLSRecord>();
    protected StringDictionary m_agentID = new StringDictionary();
    protected StringDictionary m_officeID = new StringDictionary();
    protected BitArray m_searchID = new BitArray(4);
    protected PrintWriter m_resultStream;
    protected boolean m_useStreamWriter = false;
    private ArrayList m_resultPath = new ArrayList();
    private HashMap<String,String> m_propertyTypeHashTable = new HashMap<String,String>();
    protected boolean m_includeFeature = true;
    protected boolean m_includeRoomDimention = true;
    protected boolean m_includeView = true;
    protected Hashtable m_uniqueIDList = new Hashtable();
    private long m_generateOutPutXmlTime = 0;
    protected long getGenerateOutPutXmlTime() throws Exception {
        return m_generateOutPutXmlTime;
    }

    protected void setGenerateOutPutXmlTime(long value) throws Exception {
        m_generateOutPutXmlTime = value;
    }

    private long m_parsingRawDataTime = 0;
    public long getParsingRawDataTime() throws Exception {
        return m_parsingRawDataTime;
    }

    public void setParsingRawDataTime(long value) throws Exception {
        m_parsingRawDataTime = value;
    }

    public static class ChunkingParameter   
    {
        public ChunkingParameter() {
        }

        public String DataType;
        public String MinInterVal;
        public String StandardName;
        public String MinValue;
        public String MaxValue;
        public boolean IsSearchByMilliSecond;
    }

    protected CSList<ChunkingParameter> m_chunkingParameter = null;
    protected ArrayList getResultPath() throws Exception {
        return m_resultPath;
    }

    protected void setResultPath(ArrayList value) throws Exception {
        m_resultPath = value;
    }

    protected static final int MaxResultListingNumber = 10000;
    private int m_resultListingCount = 0;
    protected int getResultListingCount() throws Exception {
        return m_resultListingCount;
    }

    protected void setResultListingCount(int value) throws Exception {
        m_resultListingCount = value;
    }

    private int m_resultListingSize = 0;
    protected int getResultListingSize() throws Exception {
        return m_resultListingSize;
    }

    protected void setResultListingSize(int value) throws Exception {
        m_resultListingSize = value;
    }

    protected public enum ChunkSearchType
    {
        Date,
        Number,
        String
    }
    protected public enum NormFields
    {
        STDFPROPERTYTYPE,
        CMAIDENTIFIER,
        CMABATHROOMS,
        CMALOTSIZE,
        CMASQUAREFEET,
        CMASTORIES,
        CMAAGE,
        CMATAXAMOUNT,
        CMAASSESSMENT
    }
    protected int[] NormDecoupleData = new int[]{ Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPENORM, Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM, Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIESNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM, Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNTNORM, Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENTNORM };
    HashMap<String,String> m_ltsTypeMapping = null;
    protected HashSet<String> StandardPropertyTypeSet = new HashSet<String>{ "Single-family", "Condo", "Town-Home", "Co-Op", "Multifamily", "Manufactured/Mobile", "Land", "Lot", "Farm or Ranch", "Recreational", "Commercial", "Investment", "Industrial", "Villa" };
    protected boolean m_selectPicFieldsOnly = false;
    public boolean getSelectPicFieldsOnly() throws Exception {
        return m_selectPicFieldsOnly;
    }

    public void setSelectPicFieldsOnly(boolean value) throws Exception {
        m_selectPicFieldsOnly = value;
    }

    private int[] m_featureFieldListNoRoomDimensionAndViews = null;
    protected int[] getFeatureFieldListNoRoomDimensionAndViews() throws Exception {
        return m_featureFieldListNoRoomDimensionAndViews;
    }

    protected void setFeatureFieldListNoRoomDimensionAndViews(int[] value) throws Exception {
        m_featureFieldListNoRoomDimensionAndViews = value;
    }

    public TPAppRequest(TCServer tcs) throws Exception {
        m_tcs = tcs;
        m_connector = m_tcs.getConnector();
        m_propertyTypeHashTable.put("SFR", "Single-family");
        m_propertyTypeHashTable.put("CON", "Condo");
        m_propertyTypeHashTable.put("TWN", "Town-Home");
        m_propertyTypeHashTable.put("COP", "Co-Op");
        m_propertyTypeHashTable.put("MFM", "Multifamily");
        m_propertyTypeHashTable.put("MOB", "Manufactured/Mobile");
        m_propertyTypeHashTable.put("LND", "Land");
        m_propertyTypeHashTable.put("LOT", "Lot");
        m_propertyTypeHashTable.put("FRM", "Farm or Ranch");
        m_propertyTypeHashTable.put("REC", "Recreational");
        m_propertyTypeHashTable.put("COM", "Commercial");
        m_propertyTypeHashTable.put("INV", "Investment");
        m_propertyTypeHashTable.put("IND", "Industrial");
        m_propertyTypeHashTable.put("VIL", "Villa");
    }

    public String getResult() throws Exception {
        return m_resultBuffer.toString();
    }

    public void run() throws Exception {
        long time;
        try
        {
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            checkSearchFields();
            m_connector.writeLine("Check Search Fields Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
            m_defList = m_connector.getDEFList(getCompatibilityID());
            String retsClass = m_connector.getRETSClassName();
            if (!StringSupport.isNullOrEmpty(retsClass))
            {
                String[] classNameCount = StringSupport.Split(retsClass, ',');
                if (classNameCount.length != m_defList.getChildNodes().size())
                    throw TCSException.produceTCSException(TCSException.RETS_CLASS_NOT_EXISTS);
                 
            }
             
            Boolean isResidentialClassOnly = m_connector.isResidentialClassOnly();
            for (int i = 0;i < m_defList.getChildNodes().size();i++)
            {
                String defPath = m_defList.getChildNodes().get(i).getAttributes().get(MLSConnector.ATTRIBUTE_DEFINITION_FILE).getValue().toLowerCase();
                if (isResidentialClassOnly)
                {
                    if (!(defPath.endsWith("re.def") || defPath.endsWith("al.def") || defPath.endsWith("cp.def")) && !defPath.endsWith(".sql"))
                    {
                        m_defList.removeChild(m_defList.getChildNodes().get(i));
                        i--;
                    }
                     
                }
                 
                if (this instanceof GetOfficeRoster || this instanceof GetRecordCountOfficeRoster)
                {
                    if (!defPath.endsWith("of.sql"))
                    {
                        m_defList.removeChild(m_defList.getChildNodes().get(i));
                        i--;
                    }
                     
                }
                else if (this instanceof GetOpenHouse || this instanceof GetRecordCountOpenHouse)
                {
                    if (!defPath.endsWith("oh.sql"))
                    {
                        m_defList.removeChild(m_defList.getChildNodes().get(i));
                        i--;
                    }
                     
                }
                else if (this instanceof GetAgentRoster || this instanceof GetRecordCountAgentRoster)
                {
                    if (!defPath.endsWith("ag.sql"))
                    {
                        m_defList.removeChild(m_defList.getChildNodes().get(i));
                        i--;
                    }
                     
                }
                else if (defPath.endsWith("da.sql"))
                {
                    if (!(this instanceof GetDataAggListings || this instanceof GetIDXListings))
                    {
                        m_defList.removeChild(m_defList.getChildNodes().get(i));
                        i--;
                    }
                     
                }
                else if (defPath.endsWith(".sql"))
                {
                    m_defList.removeChild(m_defList.getChildNodes().get(i));
                    i--;
                }
                     
            }
            if (this instanceof GetAgentRoster || this instanceof GetRecordCountAgentRoster)
            {
                if (m_defList.getChildNodes().size() == 0)
                {
                    if (this instanceof GetOpenHouse || this instanceof GetRecordCountOpenHouse)
                        throw TCSException.produceTCSException(TCSException.OPENHOUSE_DEF_NOT_EXIST);
                    else
                        throw TCSException.produceTCSException(TCSException.AGENT_OFFICE_ROSTER_DEF_NOT_EXIST); 
                }
                 
            }
             
            if (isResidentialClassOnly)
            {
                if (m_defList.getChildNodes().size() == 0)
                {
                    throw TCSException.produceTCSException(TCSException.NoMappingForResidentailClassFound);
                }
                 
            }
             
            addXMLHeader();
            for (int i = 0;i < m_defList.getChildNodes().size();i++)
            {
                time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                m_engine = m_connector.createEngine(m_defList.getChildNodes().get(i),this);
                if (!StringSupport.isNullOrEmpty(retsClass) && !m_engine.isRETSClient())
                    throw TCSException.produceTCSException(TCSException.UNABLE_TO_USE_RETS_CLASS_NON_RETS);
                 
                if (m_tcs.getTotalRetriedNumber() > 0)
                    m_engine.setRetryNumber(String.valueOf(m_tcs.getTotalRetriedNumber()));
                else
                    m_engine.setRetryNumber(""); 
                String defNotAvailableTo = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_COMMON,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DEFNOTAVAILABLETO);
                boolean isNotAvailableTo = false;
                if (!StringSupport.isNullOrEmpty(defNotAvailableTo))
                {
                    String[] notAvailables = StringSupport.Split(defNotAvailableTo, ',');
                    for (int x = 0;x < notAvailables.length;x++)
                    {
                        if (String.Compare(StringSupport.Trim(notAvailables[x]), StringSupport.Trim(m_connector.getClientName()), StringComparison.CurrentCultureIgnoreCase) == 0)
                        {
                            isNotAvailableTo = true;
                            break;
                        }
                         
                    }
                }
                 
                //Todo, add a warning message;
                if (i == 0)
                    if (!checkMLSCredential())
                        throw TCSException.produceTCSException(TCSException.MINIMUM_MLS_CREDENTIAL_NOT_SUPPLIED);
                     
                 
                setLoginInfo();
                if (isNotAvailableTo)
                {
                    if (!StringSupport.isNullOrEmpty(retsClass))
                        throw TCSException.produceTCSException(TCSException.RETS_CLASS_NOT_AVAILABLE);
                     
                    continue;
                }
                 
                m_connector.writeLine("Create Engine Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                getResult();
                m_connector.writeLine("GetResult Time= " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                validateOutputResult();
                if (m_ltsTypeMapping != null)
                {
                    m_ltsTypeMapping.clear();
                    m_ltsTypeMapping = null;
                }
                 
                if (isSearchOneDef() || (m_isSearchByMLSNumber && m_mlsNumber.length() == 0) || MLSConnector.RUN_LOCAL > 0)
                    break;
                 
            }
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            addNoRecordFoundNote();
            addXMLFooter();
            saveResult();
            if (MLSConnector.RUN_LOCAL == MLSConnector.CLIENT_ENGINE_BOARDID)
                return ;
             
            m_connector.setMessage("");
            m_connector.updateTaskProgress(MLSConnector.PROGRESS_MAX_VALUE);
            m_connector.writeLine("Save Result Time= " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
            m_connector.writeLine("Total record count=" + getResultListingCount());
        }
        catch (Exception exc)
        {
            TCSException e = TCSException.produceTCSException(exc);
            if (e.getErrorCode() != 70001)
            {
                if (m_engine != null && m_engine.getRetryAuthErrorsNumber() > 0)
                {
                    m_tcs.setRetryNumber(m_engine.getRetryAuthErrorsNumber());
                    m_tcs.setRetryWait(m_engine.getRetryAuthErrorsWait());
                    throw e;
                }
                 
                e.setErrorMessage(exc.getMessage() + exc.getStackTrace().toString());
                String outErrXml = e.getOutputErrorXml();
                m_tcs.setErrorCode(e.getErrorCode());
                m_tcs.setErrorMessage(e.getOutputErrorXml());
                m_connector.writeLine(e.getErrorMessage());
                m_connector.saveTaskResult("",outErrXml);
                m_resultBuffer.delete(0, (0)+(m_resultBuffer.length()));
                m_resultBuffer.append(outErrXml);
            }
            else
            {
                addXMLFooter();
                saveResult();
                m_connector.setMessage("");
                m_connector.updateTaskProgress(MLSConnector.PROGRESS_MAX_VALUE);
            } 
        }
        finally
        {
            if (m_useStreamWriter && m_resultStream != null)
                m_resultStream.close();
             
        }
    }

    protected public boolean checkMLSCredential() throws Exception {
        boolean bResult = true;
        String[] attributeList = m_engine.getDefParser().getAttributiesFor(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST);
        int j = 0;
        if (attributeList != null && attributeList.length > 0)
        {
            String value_Renamed = "";
            for (int i = 0;i < attributeList.length;i++)
            {
                value_Renamed = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,attributeList[i]);
                if (value_Renamed.indexOf("__PASS") > -1)
                    j++;
                 
            }
        }
         
        if (j == 0)
            return true;
         
        XmlNode loginInfo = m_connector.getLoginInfo();
        if (loginInfo == null || loginInfo.getChildNodes().size() == 0)
            return false;
         
        String[] passwords = new String[loginInfo.getChildNodes().size()];
        for (int i = 0;i < loginInfo.getChildNodes().size();i++)
        {
            XmlNode node = loginInfo.getChildNodes().get(i);
            int password_num = 0;
            if (node.getInnerXml().length() > 0)
                j--;
            else
            {
                if (j > 0)
                    bResult = false;
                 
                break;
            } 
        }
        if (j > 0)
            bResult = false;
         
        return bResult;
    }

    protected public boolean isSearchOneDef() throws Exception {
        return m_isSearchOneDef;
    }

    //|| m_isSearchByMLSNumber;
    protected public void checkMLSRequiredSearchFeilds() throws Exception {
        String s = m_engine.getCurrentPropertyFieldGroup().checkRequiredFields();
        if (s.length() > 0)
            throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED) + s,"",TCSException.MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED);
         
    }

    protected public void checkSearchFields() throws Exception {
        String[] reqField = getRequiredField();
        Hashtable ht = m_connector.getSearchFields();
        String pType = (String)ht.get(STD_SEARCH_FIELD[ST_PROPERTYTYPE]);
        if (!StringSupport.isNullOrEmpty(pType))
        {
            String[] values = Tcs.Mls.Util.stringSplit(pType,",");
            boolean bFound = false;
            for (int i = 0;i < values.length;i++)
            {
                if (!StandardPropertyTypeSet.Contains(StringSupport.Trim(values[i])))
                {
                    throw TCSException.produceTCSException(TCSException.INVALID_PROPERTY_TYPE);
                }
                 
            }
        }
         
        checkReturnMlsNumberOnlyFlag(ht);
        String mlsNum = (String)ht.get(STD_SEARCH_FIELD[ST_MLSNUMBER]);
        m_isSearchByMLSNumber = (mlsNum != null && mlsNum.length() > 0);
        if (m_isSearchByMLSNumber)
        {
            m_mlsNumber = mlsNum;
            m_connector.setSearchByMLSNumber(true);
            String[] mlsNumArray = StringSupport.Split(mlsNum, ',');
            if (mlsNumArray.length > MAX_MLS_NO_SEARCH)
                throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.EXCEED_MAX_MLSNO_SEARCH_NUMBER),"",TCSException.EXCEED_MAX_MLSNO_SEARCH_NUMBER);
             
            return ;
        }
         
        String value_Renamed = "";
        for (int i = 0;i < reqField.length;i++)
        {
            value_Renamed = ((String)ht.get(reqField[i]));
            if (value_Renamed == null || value_Renamed.length() == 0)
                throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED) + reqField[i],"",TCSException.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED);
             
        }
    }

    //String field = "";
    //Enumeration names;
    //names = ht.keys();
    //while( names.hasMoreElements() )
    //{
    //	field = (String)names.nextElement();
    //	value = (String)ht.get( field );
    //value = validateSearchParams( field, value );
    //if( value == null )
    //	throw TCSException.produceTCSException( STRINGS.get( STRINGS.INVALID_SEARCH_FIELD_FORMAT ) + field, "", TCSException.INVALID_SEARCH_FIELD_FORMAT );
    //}
    protected void checkReturnMlsNumberOnlyFlag(Hashtable htSearchFields) throws Exception {
        if (!htSearchFields.containsKey(AttributeReturnMlsNumberOnly))
            return ;
         
        String returnMlsNumberOnly = (String)htSearchFields.get(AttributeReturnMlsNumberOnly);
        if (!StringSupport.isNullOrEmpty(returnMlsNumberOnly) && returnMlsNumberOnly.equals("1"))
            setIsReturnMlsNumberOnlyRequest(true);
         
    }

    protected public String[] getRequiredField() throws Exception {
        return null;
    }

    protected public void getListings() throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        setFeatureFieldListNoRoomDimensionAndViews(null);
        String[] status = m_connector.getStatus();
        String defStatus = "";
        m_connector.setOverAllSearchNumber(status.length * m_defList.getChildNodes().size());
        m_engine.setCurrentGroup(m_isSearchByMLSNumber ? 0 : 1);
        if (this instanceof GetAgentRoster || this instanceof GetRecordCountAgentRoster)
        {
            status = new String[]{ "A" };
            m_engine.setCurrentGroup(m_engine.getPropertyFieldGroups().length > 1 ? 1 : 0);
        }
         
        int mlsSearchNotSuppliedCount = status.length;
        for (int i = 0;i < status.length;i++)
        {
            m_chunkingParameter = null;
            m_connector.setCurrentSearchNumber(m_currentSearch++);
            if (m_isSearchByMLSNumber)
            {
                if (!isDEFSupportedSearchField(STD_SEARCH_FIELD[ST_MLSNUMBER]))
                    throw TCSException.produceTCSException(MLSUtil.formatString(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD),STD_SEARCH_FIELD[ST_MLSNUMBER],m_connector.getModuleName()),"",TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
                 
            }
            else
            {
                String defValue = "";
                String[] reqField = getRequiredField();
                for (int j = 0;j < reqField.length;j++)
                {
                    if (!reqField[j].toUpperCase().equals(STD_SEARCH_FIELD[ST_PROPERTYTYPE].toUpperCase()))
                    {
                        defValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,reqField[j]);
                        if (defValue == null || defValue.length() == 0)
                            throw TCSException.produceTCSException(MLSUtil.formatString(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD),reqField[j],m_connector.getModuleName()),"",TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
                         
                    }
                     
                }
                Hashtable ht = m_connector.getSearchFields();
                String id = (String)ht.get(STD_SEARCH_FIELD[ST_AGENTID]);
                if (id != null && id.length() > 0)
                    if (!isDEFSupportedSearchField(STD_SEARCH_FIELD[ST_AGENTID]))
                        throw TCSException.produceTCSException(MLSUtil.formatString(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD),STD_SEARCH_FIELD[ST_AGENTID],m_connector.getModuleName()),"",TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
                     
                 
                id = ((String)ht.get(STD_SEARCH_FIELD[ST_OFFICEID]));
                if (id != null && id.length() > 0)
                    if (!isDEFSupportedSearchField(STD_SEARCH_FIELD[ST_OFFICEID]))
                        TCSException.produceTCSException(MLSUtil.formatString(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD),STD_SEARCH_FIELD[ST_OFFICEID],m_connector.getModuleName()),"",TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
                     
                 
            } 
            setStatus(status[i]);
            setLogMode();
            try
            {
                processSearchFields();
                //if (m_defStatus[i] == null || m_defStatus[i].Length == 0)
                //    m_defStatus[i] = getSTStatus(status[i]);
                setSearchPropertyField(getSTStatus(status[i]));
                checkMLSRequiredSearchFeilds();
                if (m_tcs.isGetPicture())
                    m_connector.setOverallProgress(MLSConnector.OVERALL_PROGRESS_SEARCH_PREPARE_PICTURE);
                 
                m_connector.updateTaskProgress(MLSConnector.PROGRESS_START_VALUE);
                m_connector.writeLine("Get Listings before Run Main Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                m_engine.setIsDownloadIDs(false);
                if (getIsOverrideLimit() && !m_isSearchByMLSNumber)
                {
                    if (StringSupport.equals(m_engine.getSearchType(), "1") || StringSupport.equals(m_engine.getSearchType(), "2"))
                    {
                        boolean downloadIDs = true;
                        if (StringSupport.equals(m_engine.getSearchType(), "2"))
                        {
                            int count = getRecordCountForOverrideLimt();
                            if (count <= m_engine.getMaxRecordsLimit())
                            {
                                downloadIDs = false;
                                m_engine.setCurrentSearchStatus(m_status + (Environment.TickCount & Integer.MAX_VALUE).toString());
                                if (!m_engine.isDemoClient())
                                    m_engine.runMainScript(0);
                                else
                                    m_engine.getDemoRecordSet(); 
                                getOutputXML();
                            }
                             
                        }
                         
                        if (downloadIDs)
                        {
                            m_uniqueIDList = new Hashtable();
                            m_engine.setIsDownloadIDs(true);
                            m_overrideLimitCount = 1;
                            m_chunkingParameter = getChunkingParameterInDef();
                            setRecordLimit();
                            if (m_engine.isRETSClient() && m_chunkingParameter != null && m_chunkingParameter.size() > 0)
                                getListingForOverrideLimtEx(getSearchParameter(m_chunkingParameter.get(0)));
                            else
                                getListingForOverrideLimt(getSearchDate()); 
                            m_engine.setIsDownloadIDs(false);
                            if (m_uniqueIDList.size() > 0)
                            {
                                downloadDataByIDList();
                                m_uniqueIDList.Clear();
                            }
                             
                        }
                         
                    }
                    else
                    {
                        m_overrideLimitCount = 1;
                        m_chunkingParameter = getChunkingParameterInDef();
                        if (m_engine.isRETSClient() && m_chunkingParameter != null && m_chunkingParameter.size() > 0)
                            getListingForOverrideLimtEx(getSearchParameter(m_chunkingParameter.get(0)));
                        else
                            getListingForOverrideLimt(getSearchDate()); 
                    } 
                }
                else
                {
                    if (StringSupport.equals(m_engine.getSearchType(), "1"))
                        m_engine.setIsDownloadIDs(true);
                     
                    if (!m_engine.isDemoClient())
                        m_engine.runMainScript(0);
                    else
                        m_engine.getDemoRecordSet(); 
                    if (StringSupport.equals(m_engine.getSearchType(), "1"))
                    {
                        getOutputXML();
                        m_engine.setIsDownloadIDs(false);
                        if (m_uniqueIDList.size() > 0)
                        {
                            downloadDataByIDList();
                            m_uniqueIDList.Clear();
                        }
                         
                    }
                    else
                    {
                        MLSCmaFields fields = m_engine.getCmaFields();
                        m_records = m_engine.getMLSRecords(0x0FFF);
                        MLSRecord rec = m_records.getFirstRecord();
                        if (rec == null && !(this instanceof GetRecordCount || this instanceof GetRecordCountDataagg || this instanceof GetRecordCountPlus || this instanceof GetRecordCountAgentRoster || this instanceof GetRecordCountOpenHouse))
                            throw TCSException.produceTCSException(TCSException.MLS_ERROR_NO_RECORD_FOUND);
                         
                        int n = m_connector.getMaxListingNumber();
                        //Get Agent and Office info
                        if (!m_engine.isDemoClient())
                        {
                            while (rec != null && n != 0)
                            {
                                if (m_isSearchByMLSNumber)
                                    removeMLSNumber(rec.getRecordID());
                                 
                                rec.setChecked(true);
                                rec = m_records.getNextRecord();
                                n--;
                            }
                        }
                         
                        m_connector.writeLine("Run Main Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                        try
                        {
                            if (m_tcs.isGetPicture())
                            {
                                time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                                m_engine.runPicScript(m_engine.getMLSRecords(MLSRecords.FILTER_CHECKED));
                                m_connector.writeLine("\r\nRun Picture Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                            }
                             
                        }
                        catch (Exception e)
                        {
                            TCSException exc = TCSException.produceTCSException(e);
                            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                            addXMLNote(exc.getErrorCode(),exc.getMessage());
                        }

                        //if (MLSConnector.RUN_LOCAL == MLSConnector.CLIENT_ENGINE_BOARDID)
                        //    return;
                        m_connector.writeLine("Start generating Output XML ");
                        time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                        getOutputXML();
                        setGenerateOutPutXmlTime(getGenerateOutPutXmlTime() + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                        m_connector.writeLine("\r\nGet output xml time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                    } 
                } 
                m_connector.writeLine(m_connector.getConnectionName() + m_status);
                m_connector.writeLine("Parsing Raw data time = " + getParsingRawDataTime());
                m_connector.writeLine("Generating Output Xml time = " + getGenerateOutPutXmlTime());
                m_connector.writeLine("Transforming from Raw data to Xml time = = " + (getParsingRawDataTime() + getGenerateOutPutXmlTime()));
                m_requestSucess = true;
            }
            catch (Exception e)
            {
                int code;
                TCSException tcsExc = null;
                if (!m_engine.isRETSClient() && e instanceof MLSException)
                {
                    MLSException mlsExc = (MLSException)e;
                    code = mlsExc.getCode();
                    if (code == 4 || code == 7)
                    {
                        int tcsCode = getTCSErrorCode(mlsExc.getMessage());
                        if (tcsCode > 0 && tcsCode != code)
                        {
                            if (tcsCode == TCSException.INTERNAL_ERROR)
                                tcsExc = TCSException.produceTCSException(TCSException.getTCSMessage(tcsCode) + " " + mlsExc.getMessage(),"",tcsCode);
                            else
                                tcsExc = TCSException.produceTCSException(tcsCode); 
                        }
                         
                    }
                     
                }
                 
                TCSException exc = TCSException.produceTCSException(tcsExc == null ? e : tcsExc);
                //String outErrXml = exc.getOutputErrorXml();
                code = exc.getErrorCode();
                if (isRETSError(exc.getDeveloperMessage()))
                {
                    throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MISCELLANEOUS_MLS_ERROR) + exc.getMessage(),"",TCSException.MISCELLANEOUS_MLS_ERROR);
                }
                 
                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                if ((code < TCSException.INTERNAL_ERROR + 1) || code > 70000)
                    throw exc;
                 
                switch(code)
                {
                    case TCSException.MLS_ERROR_INVALID_USERNAME_PASSWORD: 
                    case TCSException.TCS_EXECUTIVE_TIMEOUT: 
                    case TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD: 
                    case TCSException.MLS_ERROR_TIMEOUT: 
                    case TCSException.EXCEED_MAX_DOWNLOAD_SIZE: 
                    case TCSException.RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT: 
                    case TCSException.BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED: 
                    case TCSException.NOT_SURPPORT_POSTALFSA_FIELD: 
                    case TCSException.EXCEED_MAX_LISITNG_NUMBER: 
                    case TCSException.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH: 
                    case TCSException.BOTH_LASTMODIFIED_PICMODIFIED_EMPTY: 
                    case TCSException.EXCEED_MAX_MLSNO_SEARCH_NUMBER: 
                    case TCSException.CHUNKING_FIELD_NOTDEFINED: 
                    case TCSException.BOTH_STATUS_PUBLICSTATUS_USED: 
                    case TCSException.OFFMARKET_STATUS_NOT_DEFINED_INDEF: 
                    case TCSException.CHUNKING_FAILED: 
                    case TCSException.BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED: 
                    case TCSException.DUPLICATE_SEARCH_CODE_RETS_PARAMETER: 
                    case TCSException.OpenHouse_NotAvailableTo_MlsNumberSearch: 
                        throw exc;
                    case TCSException.MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED: 
                        //case TCSException.CONDITIONCODE_NOT_IMPLEMENTED_INDEF:
                        mlsSearchNotSuppliedCount--;
                        if (mlsSearchNotSuppliedCount == 0)
                            throw exc;
                         
                        break;
                    default: 
                        m_requestSucess = true;
                        break;
                
                }
                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                String errorMsg = exc.getMessage();
                if (code == TCSException.MLS_ERROR_NO_RECORD_FOUND)
                    errorMsg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND);
                 
                addXMLNote(exc.getErrorCode(),Tcs.Mls.Util.convertStringToXML(errorMsg));
            }

            //checkResultSize();
            if (m_isSearchByMLSNumber)
                break;
             
        }
    }

    protected void downloadDataByIDList() throws Exception {
        m_engine.setIsDownloadIDs(false);
        IDictionaryEnumerator en = EnumeratorSupport.mk(m_uniqueIDList.iterator());
        int limit = m_engine.getSearchIDsLimit();
        String id = "";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (en.MoveNext())
        {
            i++;
            id = en.Key.toString();
            sb.append(id).append(",");
            if (i == limit)
            {
                runMainscriptToDownloadByIDs(sb);
                sb = new StringBuilder();
                i = 0;
            }
             
        }
        if (sb.length() > 0)
        {
            runMainscriptToDownloadByIDs(sb);
        }
         
    }

    //Last chunk
    protected void runMainscriptToDownloadByIDs(StringBuilder sbIDs) throws Exception {
        String ids = sbIDs.delete(sbIDs.length() - 1, (sbIDs.length() - 1)+(1)).toString();
        Hashtable ht = new Hashtable();
        ht.put("ReferencedSearchKey", ids);
        m_engine.setPropertyFields(ht);
        int retry = 3;
        LableRetry:try
        {
            m_engine.setCurrentSearchStatus(m_status + (Environment.TickCount & Integer.MAX_VALUE).toString());
            m_engine.runMainScript(0);
        }
        catch (Exception __dummyCatchVar0)
        {
            if (retry > 0)
            {
                retry--;
                m_connector.writeLine("One chunk failed. Retry...");
                goto LableRetry
            }
             
        }

        getOutputXML();
    }

    protected void addIDsForDownloadIDOnlySearch() throws Exception {
        m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        MLSRecord rec = m_records.getFirstRecord();
        String id = "";
        while (rec != null)
        {
            id = rec.getFieldValue("ReferencingKey");
            if (!StringSupport.isNullOrEmpty(id))
                m_uniqueIDList.put(id, null);
             
            rec = m_records.getNextRecord();
        }
    }

    //protected void SetAgentOfficeID()
    //{
    //bool needAgentOfficeInfo = m_connector.NeedAgentOfficeSearch;
    //if (!needAgentOfficeInfo)
    //    return;
    //MLSRecords records = m_engine.getMLSRecords(0x0FFF);
    //MLSRecord rec = records.getFirstRecord();
    //if (m_agentEngine == null)
    //{
    //    m_agentEngine = m_connector.createEngine(MLSEngine.EngineType.AgentEngine);
    //    if (m_agentEngine != null)
    //    {
    //        if (m_agentEngine.getCmaFields().getStdField(SearchIDFields[0]) != null)
    //            m_searchID[0] = true;
    //        if (m_agentEngine.getCmaFields().getStdField(SearchIDFields[1]) != null)
    //            m_searchID[1] = true;
    //    }
    //}
    //if (m_officeEngine == null)
    //    m_officeEngine = m_connector.createEngine(MLSEngine.EngineType.OfficeEngine);
    //if (m_officeEngine != null)
    //{
    //    if (m_officeEngine.getCmaFields().getStdField(SearchIDFields[2]) != null)
    //        m_searchID[2] = true;
    //    if (m_officeEngine.getCmaFields().getStdField(SearchIDFields[3]) != null)
    //        m_searchID[3] = true;
    //}
    //else if (m_agentEngine != null)
    //{
    //    if (m_agentEngine.getCmaFields().getStdField(SearchIDFields[2]) != null)
    //        m_searchID[2] = true;
    //    if (m_agentEngine.getCmaFields().getStdField(SearchIDFields[3]) != null)
    //        m_searchID[3] = true;
    //}
    //while (rec != null)
    //{
    //    for (int m = 0; m < 2; m++)
    //    {
    //        if (!m_searchID[m])
    //            continue;
    //        string id = rec.getFieldValue(m_engine.getCmaFields().getStdFieldIndex(SearchIDFields[m]));
    //        if (!string.IsNullOrEmpty(id) && !m_agentID.ContainsKey(id))
    //        {
    //            m_agentID.Add(id, "0");
    //        }
    //    }
    //    for (int x = 2; x < SearchIDFields.Length; x++)
    //    {
    //        if (!m_searchID[x])
    //            continue;
    //        string id = rec.getFieldValue(m_engine.getCmaFields().getStdFieldIndex(SearchIDFields[x]));
    //        if (!string.IsNullOrEmpty(id) && !m_officeID.ContainsKey(id))
    //        {
    //            m_officeID.Add(id, "0");
    //        }
    //    }
    //    rec = records.getNextRecord();
    //}
    /**
    * /Search Agent and Office info
    */
    //if (m_agentEngine != null)
    //    SearchAgentOfficeInfo(m_agentID, m_agentEngine, MLSEngine.EngineType.AgentEngine);
    //if (m_officeEngine != null)
    //    SearchAgentOfficeInfo(m_officeID, m_officeEngine, MLSEngine.EngineType.OfficeEngine);
    //}
    //private void SearchAgentOfficeInfo(StringDictionary sd, MLSEngine engine, MLSEngine.EngineType type)
    //{
    //    int i = 0;
    //    int n = 1;
    //    string ids = "";
    //    int total = sd.Count;
    //    int x = 1;
    //    bool isAgent = type == MLSEngine.EngineType.AgentEngine;
    //    List<string> list = new List<string>();
    //    foreach (DictionaryEntry de in sd)
    //    {
    //        if (n > total)
    //            break;
    //        if (de.Value.Equals("0"))
    //        {
    //            if (string.IsNullOrEmpty(ids))
    //                ids += de.Key.ToString();
    //            else
    //                ids += "," + de.Key.ToString();
    //            list.Add(de.Key.ToString());
    //            i++;
    //        }
    //        if (((i / engine.MaxAgentOfficeID > 0 && i % engine.MaxAgentOfficeID == 0) || n == total) && !string.IsNullOrEmpty(ids))
    //        {
    //            if (isAgent)
    //            {
    //                m_agentEngine.AgentID = ids;
    //                engine.setCurrentSearchStatus(m_status + "ag" + x);
    //            }
    //            else
    //            {
    //                m_officeEngine.OfficeID = ids;
    //                engine.setCurrentSearchStatus(m_status + "of" + x);
    //            }
    //            x++;
    //            try
    //            {
    //                setLoginInfo(engine);
    //                setLogMode(engine);
    //                engine.runMainScript(0);
    //                MLSRecords recs = engine.getMLSRecords(0x0FFF);
    //                MLSRecord rec = recs.getFirstRecord();
    //                while (rec != null)
    //                {
    //                    if (isAgent)
    //                    {
    //                        if (!m_agentRecords.ContainsKey(rec.getRecordID()))
    //                            m_agentRecords.Add(rec.getRecordID(), rec);
    //                    }
    //                    else
    //                    {
    //                        if (!m_officeRecords.ContainsKey(rec.getRecordID()))
    //                            m_officeRecords.Add(rec.getRecordID(), rec);
    //                    }
    //                    rec = recs.getNextRecord();
    //                }
    //            }
    //            catch (Exception e)
    //            {
    //                //Add error to note
    //            }
    //            ids = "";
    //        }
    //        n++;
    //    }
    //    for (int j = 0; j < list.Count; j++)
    //    {
    //        sd[list[j]] = "1";
    //    }
    //    return;
    //}
    protected String[] getSearchDate() throws Exception {
        String keyDate, valDate;
        String[] searchDate = new String[2];
        if (m_searchFields.containsKey(STD_SEARCH_FIELD[ST_STATUSDATE]))
        {
            keyDate = STD_SEARCH_FIELD[ST_STATUSDATE];
            valDate = m_searchFields.get(STD_SEARCH_FIELD[ST_STATUSDATE]).toString();
            if (valDate.length() > 0)
            {
                searchDate[0] = keyDate;
                searchDate[1] = valDate;
            }
             
        }
         
        if (m_searchFields.containsKey(STD_SEARCH_FIELD[ST_LISTDATE]))
        {
            keyDate = STD_SEARCH_FIELD[ST_LISTDATE];
            valDate = m_searchFields.get(STD_SEARCH_FIELD[ST_LISTDATE]).toString();
            if (valDate.length() > 0)
            {
                searchDate[0] = keyDate;
                searchDate[1] = valDate;
            }
             
        }
         
        if (m_searchFields.containsKey(STD_SEARCH_FIELD[ST_SALEDATE]))
        {
            keyDate = STD_SEARCH_FIELD[ST_SALEDATE];
            valDate = m_searchFields.get(STD_SEARCH_FIELD[ST_SALEDATE]).toString();
            if (valDate.length() > 0)
            {
                searchDate[0] = keyDate;
                searchDate[1] = valDate;
            }
             
        }
         
        if (m_searchFields.containsKey(STD_SEARCH_FIELD[ST_LastModified]))
        {
            keyDate = STD_SEARCH_FIELD[ST_LastModified];
            valDate = m_searchFields.get(STD_SEARCH_FIELD[ST_LastModified]).toString();
            if (valDate.length() > 0)
            {
                searchDate[0] = keyDate;
                searchDate[1] = valDate;
            }
             
        }
         
        if (m_searchFields.containsKey(STD_SEARCH_FIELD[ST_PicModified]))
        {
            keyDate = STD_SEARCH_FIELD[ST_PicModified];
            valDate = m_searchFields.get(STD_SEARCH_FIELD[ST_PicModified]).toString();
            if (valDate.length() > 0)
            {
                searchDate[0] = keyDate;
                searchDate[1] = valDate;
            }
             
        }
         
        if (m_searchFields.containsKey(STD_SEARCH_FIELD[ST_SearchDate]))
        {
            keyDate = STD_SEARCH_FIELD[ST_SearchDate];
            valDate = m_searchFields.get(STD_SEARCH_FIELD[ST_SearchDate]).toString();
            if (valDate.length() > 0)
            {
                searchDate[0] = keyDate;
                searchDate[1] = valDate;
            }
             
        }
         
        return searchDate;
    }

    protected void getListingForOverrideLimt(String[] searchDate) throws Exception {
        if (m_overrideLimitCount > getOverrideRecordLimit())
            return ;
         
        String field = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,searchDate[0]);
        net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(field);
        if (pf == null)
        {
            String errMsg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DEFNOTHAVEDATEFIELD);
            String __dummyScrutVar1 = searchDate[0];
            if (__dummyScrutVar1.equals("ST_ListDate"))
            {
                errMsg = String.format(StringSupport.CSFmtStrToJFmtStr(errMsg),"listing");
            }
            else if (__dummyScrutVar1.equals("ST_SaleDate"))
            {
                errMsg = String.format(StringSupport.CSFmtStrToJFmtStr(errMsg),"sold");
            }
            else if (__dummyScrutVar1.equals("ST_StatusDate"))
            {
                errMsg = String.format(StringSupport.CSFmtStrToJFmtStr(errMsg),"status");
            }
            else if (__dummyScrutVar1.equals("ST_LastMod"))
            {
                errMsg = String.format(StringSupport.CSFmtStrToJFmtStr(errMsg),"Last modified");
            }
            else if (__dummyScrutVar1.equals("ST_PicMod"))
            {
                errMsg = String.format(StringSupport.CSFmtStrToJFmtStr(errMsg),"Picture last modified");
            }
                 
            throw TCSException.produceTCSException(errMsg,"",60200);
        }
         
        boolean isSearchByMilliSeconds = false;
        String dateFormat = pf.getDateFormat();
        if (!StringSupport.isNullOrEmpty(dateFormat) && dateFormat.IndexOf("f", StringComparison.InvariantCultureIgnoreCase) > -1)
            isSearchByMilliSeconds = true;
         
        //string[] mlsDate = new string[] { searchDate[0], searchDate[0]getDateFormat(pf, searchDate[1]) };
        m_searchFields.put(searchDate[0], searchDate[1]);
        //m_connector.WriteLine("GoHere:" + searchDate[1]);
        m_engine.setPropertyFields(m_searchFields);
        int recCount = getRecordCountForOverrideLimt();
        //if (recCount > MaxResultListingNumber && GetOverrideRecordLimit() > MaxResultListingNumber)
        //    throw TCSException.produceTCSException(TCSException.EXCEED_MAX_LISITNG_NUMBER);
        if (recCount > 0)
        {
            if (((recCount > m_engine.getMaxRecordsLimit()) || (recCount == m_engine.getMaxRecordsLimit() && !m_engine.isRETSClient())) && !isInOneDay(searchDate[1],isSearchByMilliSeconds))
            {
                int n = 0;
                if (m_engine.isRETSClient())
                    n = recCount / m_engine.getMaxRecordsLimit() + 1;
                else
                    n = countDays(searchDate[1]) / m_engine.getDaysInChunk() + 1; 
                String[] splitedSearchDate = splitSearchDate(searchDate[1],n,isSearchByMilliSeconds);
                for (int i = splitedSearchDate.length - 1;i > -1;i--)
                {
                    if (!StringSupport.isNullOrEmpty(splitedSearchDate[i]))
                    {
                        try
                        {
                            //m_connector.WriteLine("Chunking Interval:" + splitedSearchDate[i]);
                            if (m_overrideLimitCount > getOverrideRecordLimit())
                                break;
                             
                            getListingForOverrideLimt(new String[]{ searchDate[0], splitedSearchDate[i] });
                        }
                        catch (TCSException texc)
                        {
                            if (texc.getErrorCode() == TCSException.EXCEED_MAX_LISITNG_NUMBER || texc.getErrorCode() == TCSException.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH)
                                throw texc;
                             
                        }
                        catch (Exception exc)
                        {
                        }
                    
                    }
                     
                }
            }
            else
            {
                //throw exc;
                if (m_engine.isRETSClient())
                {
                    m_engine.setCurrentSearchStatus(m_status + (Environment.TickCount & Integer.MAX_VALUE).toString());
                    setLogMode();
                    m_engine.runMainScript(0);
                }
                 
                //SetAgentOfficeID();
                getOutputXML();
            } 
        }
         
    }

    protected void getListingForOverrideLimtEx(String[] searchParameters) throws Exception {
        if (m_overrideLimitCount > getOverrideRecordLimit())
            return ;
         
        String field = searchParameters[searchParameters.length - 1].substring(0, (0) + (searchParameters[searchParameters.length - 1].indexOf(CHUNKING_DELIMITER)));
        String defField = field.toUpperCase();
        if (field.startsWith("ST"))
        {
            defField = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,defField);
        }
         
        net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(defField);
        if (pf == null)
        {
            String errMsg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.CHUNKING_FIELD_NOTDEFINED);
            throw TCSException.produceTCSException(errMsg + " " + field,"",TCSException.CHUNKING_FIELD_NOTDEFINED);
        }
         
        Hashtable cloneSearchField = (Hashtable)m_searchFields.clone();
        for (int i = 0;i < searchParameters.length;i++)
        {
            int pos = searchParameters[i].indexOf(CHUNKING_DELIMITER);
            String searchField = searchParameters[i].substring(0, (0) + (pos));
            cloneSearchField.put(searchField, searchParameters[i].substring(pos + 2));
        }
        m_engine.setPropertyFields(cloneSearchField);
        int recCount = getRecordCountForOverrideLimt();
        if (recCount > 0)
        {
            if (recCount > m_engine.getMaxRecordsLimit())
            {
                boolean useNextChunkingKey = false;
                if (isNotAbleToSplit(searchParameters[searchParameters.length - 1]))
                    useNextChunkingKey = true;
                 
                int n = recCount / m_engine.getMaxRecordsLimit() + 1;
                String[] splitedSearchParameter = splitSearchParameter(searchParameters[searchParameters.length - 1],n,useNextChunkingKey);
                for (int i = splitedSearchParameter.length - 1;i > -1;i--)
                {
                    if (!StringSupport.isNullOrEmpty(splitedSearchParameter[i]))
                    {
                        try
                        {
                            //int retryCount = 0;
                            //LableRetry:
                            //m_connector.WriteLine("Chunking Interval:" + splitedSearchParameter[i]);
                            if (m_overrideLimitCount > getOverrideRecordLimit())
                                break;
                             
                            if (!useNextChunkingKey)
                            {
                                searchParameters[searchParameters.length - 1] = splitedSearchParameter[i];
                                getListingForOverrideLimtEx(searchParameters);
                            }
                            else
                            {
                                String[] newSearchParmeters = new String[searchParameters.length + 1];
                                Array.Copy(searchParameters, newSearchParmeters, searchParameters.length);
                                newSearchParmeters[newSearchParmeters.length - 1] = splitedSearchParameter[i];
                                getListingForOverrideLimtEx(newSearchParmeters);
                            } 
                        }
                        catch (TCSException texc)
                        {
                            if (texc.getErrorCode() != TCSException.MLS_ERROR_NO_RECORD_FOUND)
                                throw TCSException.produceTCSException(TCSException.CHUNKING_FAILED);
                             
                        }
                        catch (Exception exc)
                        {
                            throw TCSException.produceTCSException(TCSException.CHUNKING_FAILED);
                        }
                    
                    }
                     
                }
            }
            else
            {
                //if (texc.getErrorCode() == TCSException.EXCEED_MAX_LISITNG_NUMBER || texc.getErrorCode() == TCSException.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH || texc.getErrorCode() == TCSException.CHUNKING_FIELD_NOTDEFINED)
                //    throw texc;
                //retryCount++;
                //retryCount++;
                //throw exc;
                //if (retryCount > 0 && retryCount < 4)
                //    goto LableRetry;
                m_engine.setCurrentSearchStatus(m_status + (Environment.TickCount & Integer.MAX_VALUE).toString());
                setLogMode();
                m_engine.runMainScript(0);
                //SetAgentOfficeID();
                getOutputXML();
            } 
        }
         
    }

    private boolean isNotAbleToSplit(String searchParameter) throws Exception {
        int next = 0;
        RefSupport<Integer> refVar___0 = new RefSupport<Integer>();
        ChunkingParameter currentChunkingParameter = getCurrentChunkingParameter(searchParameter,refVar___0);
        next = refVar___0.getValue();
        int pos = searchParameter.indexOf(CHUNKING_DELIMITER);
        String value = searchParameter.substring(pos + 2);
        long interval = 0;
        String minValue = "";
        String maxValue = "";
        if (value.indexOf("-") > -1)
        {
            minValue = value.substring(0, (0) + (value.indexOf("-")));
            maxValue = value.substring(value.indexOf("-") + 1);
        }
         
        String __dummyScrutVar2 = currentChunkingParameter.DataType;
        if (__dummyScrutVar2.equals("DATE") || __dummyScrutVar2.equals("DATETIME"))
        {
            String startDate = minValue.replace('T', ' ');
            Date sDate = DateTimeSupport.parse(startDate);
            String endDate = maxValue.replace('T', ' ');
            Date eDate = DateTimeSupport.parse(endDate);
            interval = (long)((new TimeSpan(Math.abs(eDate.getTime() - sDate.getTime()))).TotalMilliseconds);
            if (interval > Long.valueOf(currentChunkingParameter.MinInterVal))
                return false;
            else
                return true; 
        }
        else if (__dummyScrutVar2.equals("NUMBER"))
        {
            long intMin = Long.valueOf(minValue);
            long intMax = Long.valueOf(maxValue);
            interval = intMax - intMin;
            if (interval > Long.valueOf(currentChunkingParameter.MinInterVal))
                return false;
            else
                return true; 
        }
        else if (__dummyScrutVar2.equals("STARTSWITH"))
        {
            String[] letters = StringSupport.Split(searchParameter, ',');
            if (letters.length == 1)
                return true;
             
        }
           
        return false;
    }

    private boolean isInOneDay(String searchDate, boolean isSearchByMilliSeconds) throws Exception {
        String dateFormat = STANDARD_DATEFORMAT;
        if (searchDate.indexOf('T') > -1)
        {
            dateFormat = STANDARD_DATETIMEFORMAT;
        }
         
        String searchStart = StringSupport.Split(searchDate, '-')[0];
        String searchEnd = StringSupport.Split(searchDate, '-')[1];
        if (isSearchByMilliSeconds)
        {
            dateFormat = STANDARD_DATETIMEFORMAT_START_MILLISECOND;
            if (searchStart.length() < STANDARD_DATETIMEFORMAT_START_MILLISECOND.length())
            {
                searchStart = searchStart + ".000";
                searchEnd = searchEnd + ".999";
            }
             
        }
         
        Date startDate = Date.ParseExact(searchStart, dateFormat, null);
        Date endDate = Date.ParseExact(searchEnd, dateFormat, null);
        if (searchDate.indexOf('T') > -1)
        {
            if (isSearchByMilliSeconds)
            {
                if ((long)((new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).TotalMilliseconds) < 1)
                {
                    throw TCSException.produceTCSException(TCSException.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH);
                }
                else
                    return false; 
            }
            else
            {
                if ((long)(new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).getTotalSeconds() < 1)
                {
                    throw TCSException.produceTCSException(TCSException.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH);
                }
                else
                    return false; 
            } 
        }
         
        if (dateDiffInDays(startDate,endDate) == 0)
            return true;
        else
            return false; 
    }

    private int countSplitNumber(String searchParameter, net.toppro.components.mls.engine.MLSEngine mlsEngine) throws Exception {
        return 0;
    }

    //Todo: count splits for non rest client.
    private int countDays(String searchDate) throws Exception {
        Date startDate = DateTimeSupport.parse(StringSupport.Split(searchDate, '-')[0]);
        Date endDate = DateTimeSupport.parse(StringSupport.Split(searchDate, '-')[1]);
        return dateDiffInDays(startDate,endDate);
    }

    public CSList<ChunkingParameter> getChunkingParameterInDef() throws Exception {
        if (m_chunkingParameter != null)
            return m_chunkingParameter;
         
        m_chunkingParameter = new CSList<ChunkingParameter>();
        for (int i = 1;i < 21;i++)
        {
            String value = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_CHUNKING,"VALUE" + i);
            if (!StringSupport.isNullOrEmpty(value))
            {
                if (StringSupport.Compare(value, "none", true) == 0)
                    return m_chunkingParameter;
                 
                ChunkingParameter chp = parseChunkingParameter(value);
                m_chunkingParameter.add(chp);
            }
            else
            {
                break;
            } 
        }
        if (m_chunkingParameter != null && m_chunkingParameter.size() == 0)
        {
            //Use default chuncking logic
            if (!(this instanceof GetAgentRoster) && !(this instanceof GetOfficeRoster) && m_engine.isRETSClient())
            {
                try
                {
                    String defaultParameter = m_connector.getDefaultChunkingParameter();
                    if (!StringSupport.isNullOrEmpty(defaultParameter))
                    {
                        String[] values = StringSupport.Split(defaultParameter, '|');
                        for (int i = 0;i < values.length;i++)
                        {
                            ChunkingParameter chp = parseChunkingParameter(values[i]);
                            //"Field:ST_ListPr,Type:Number,From:1,To:2147483647,Min:1");
                            m_chunkingParameter.add(chp);
                        }
                    }
                     
                }
                catch (Exception __dummyCatchVar1)
                {
                }
            
            }
             
        }
         
        return m_chunkingParameter;
    }

    //chp = ParseChunkingParameter("Field:ST_ListDate,Type:Date,From:01/01/1900,To:Now");
    //m_chunkingParameter.Add(chp);
    private ChunkingParameter parseChunkingParameter(String parameter) throws Exception {
        ChunkingParameter chunkingParameter = new ChunkingParameter();
        try
        {
            String[] paras = StringSupport.Split(parameter, ',');
            String dateFormat = STANDARD_DATEFORMAT;
            for (int i = 0;i < paras.length;i++)
            {
                String field = StringSupport.Trim(paras[i].substring(0, (0) + (paras[i].indexOf(":"))).toUpperCase());
                String __dummyScrutVar3 = field;
                if (__dummyScrutVar3.equals("FIELD"))
                {
                    chunkingParameter.StandardName = StringSupport.Trim(paras[i].substring(paras[i].indexOf(":") + 1));
                    if (chunkingParameter.StandardName.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUSDATE].toUpperCase()))
                    {
                        if (!m_engine.isSupportStatusDate())
                        {
                            if (StringSupport.Compare("S", m_status, true) == 0)
                                chunkingParameter.StandardName = STD_SEARCH_FIELD[ST_SALEDATE];
                            else
                                chunkingParameter.StandardName = STD_SEARCH_FIELD[ST_LISTDATE]; 
                        }
                         
                    }
                     
                    if (chunkingParameter.StandardName.toUpperCase().equals(STD_SEARCH_FIELD[ST_SEARCHPRICE].toUpperCase()))
                    {
                        if (!m_engine.isSupportSearchPrice())
                        {
                            if (StringSupport.Compare("S", m_status, true) == 0)
                                chunkingParameter.StandardName = STD_SEARCH_FIELD[ST_SALEPRICE];
                            else
                                chunkingParameter.StandardName = STD_SEARCH_FIELD[ST_LISTPRICE]; 
                        }
                         
                    }
                     
                }
                else if (__dummyScrutVar3.equals("FROMTO"))
                {
                    chunkingParameter.MinValue = StringSupport.Trim(paras[i].substring(paras[i].indexOf(":") + 1));
                }
                else if (__dummyScrutVar3.equals("FROM"))
                {
                    chunkingParameter.MinValue = StringSupport.Trim(paras[i].substring(paras[i].indexOf(":") + 1));
                    if (chunkingParameter.DataType.IndexOf("date", StringComparison.InvariantCultureIgnoreCase) > -1)
                    {
                        if (StringSupport.Compare(chunkingParameter.MinValue, "Now", true) == 0)
                        {
                            chunkingParameter.MinValue = DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), dateFormat);
                        }
                         
                        if (chunkingParameter.MinValue.toUpperCase().endsWith("Y"))
                        {
                            int years = Integer.valueOf(chunkingParameter.MinValue.toUpperCase().replace("Y", ""));
                            chunkingParameter.MinValue = Calendar.getInstance().getTime().AddYears(-years).ToUniversalTime().ToString(dateFormat);
                        }
                         
                    }
                     
                }
                else if (__dummyScrutVar3.equals("TO"))
                {
                    chunkingParameter.MaxValue = StringSupport.Trim(paras[i].substring(paras[i].indexOf(":") + 1));
                    if (chunkingParameter.DataType.IndexOf("date", StringComparison.InvariantCultureIgnoreCase) > -1)
                    {
                        if (StringSupport.Compare(chunkingParameter.MaxValue, "Now", true) == 0)
                        {
                            chunkingParameter.MaxValue = DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), dateFormat);
                        }
                         
                        if (chunkingParameter.MaxValue.toUpperCase().endsWith("Y"))
                        {
                            int years = Integer.valueOf(chunkingParameter.MaxValue.toUpperCase().replace("Y", ""));
                            chunkingParameter.MaxValue = Calendar.getInstance().getTime().AddYears(years).ToUniversalTime().ToString(dateFormat);
                        }
                         
                    }
                     
                }
                else if (__dummyScrutVar3.equals("TYPE"))
                {
                    chunkingParameter.DataType = StringSupport.Trim(paras[i].substring(paras[i].indexOf(":") + 1).toUpperCase());
                    if (StringSupport.Compare(chunkingParameter.DataType, "DateTime", true) == 0)
                    {
                        String dtField = chunkingParameter.StandardName;
                        //if(chunkingParameter.StandardName.StartsWith("ST_"))
                        //   dtField = m_engine.getDefParser().getValue(MLSEngine.SECTION_AUTOSEARCH, chunkingParameter.StandardName);
                        //PropertyField pf = m_engine.getPropertyFields().getField(dtField);
                        dateFormat = STANDARD_DATETIMEFORMAT;
                        //if(pf != null)
                        //    dateFormat = pf.getDateFormat();
                        if (!StringSupport.isNullOrEmpty(dateFormat) && dateFormat.IndexOf("f", StringComparison.InvariantCultureIgnoreCase) > -1)
                            chunkingParameter.IsSearchByMilliSecond = true;
                        else
                            chunkingParameter.IsSearchByMilliSecond = false; 
                    }
                     
                }
                else if (__dummyScrutVar3.equals("MIN"))
                {
                    chunkingParameter.MinInterVal = StringSupport.Trim(paras[i].substring(paras[i].indexOf(":") + 1));
                    if (chunkingParameter.MinInterVal == null)
                        chunkingParameter.MinInterVal = "1";
                     
                }
                      
            }
            if (StringSupport.isNullOrEmpty(chunkingParameter.StandardName) || StringSupport.isNullOrEmpty(chunkingParameter.DataType))
                throw new Exception("Error on Parsing Advanced Chunking Parameters.");
             
            if (StringSupport.Compare(chunkingParameter.DataType, "Number", true) == 0)
            {
                if (StringSupport.isNullOrEmpty(chunkingParameter.MinValue) || StringSupport.isNullOrEmpty(chunkingParameter.MaxValue) || StringSupport.isNullOrEmpty(chunkingParameter.MinInterVal))
                    throw new Exception("Error on Parsing Advanced Chunking Parameters.");
                 
            }
             
            if (StringSupport.Compare(chunkingParameter.DataType, "Date", true) == 0 || StringSupport.Compare(chunkingParameter.DataType, "DateTime", true) == 0)
            {
                if (StringSupport.isNullOrEmpty(chunkingParameter.MinValue))
                {
                    chunkingParameter.MinValue = Calendar.getInstance().getTime().AddYears(-10).ToUniversalTime().ToString(dateFormat);
                    chunkingParameter.MaxValue = DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), dateFormat);
                }
                 
                if (!StringSupport.isNullOrEmpty(chunkingParameter.MinInterVal))
                    Integer.valueOf(chunkingParameter.MinInterVal);
                else
                {
                    if (StringSupport.Compare(chunkingParameter.DataType, "Date", true) == 0)
                    {
                        chunkingParameter.MinInterVal = "0";
                    }
                    else
                    {
                        if (chunkingParameter.IsSearchByMilliSecond)
                            chunkingParameter.MinInterVal = "0";
                        else
                            chunkingParameter.MinInterVal = "0"; 
                    } 
                } 
            }
             
            if (StringSupport.Compare(chunkingParameter.DataType, "StartsWith", true) == 0)
            {
                if (!StringSupport.isNullOrEmpty(chunkingParameter.MaxValue))
                {
                    if (chunkingParameter.MinValue.length() == 1 && chunkingParameter.MaxValue.length() == 1)
                        chunkingParameter.MinValue = chunkingParameter.MinValue + "-" + chunkingParameter.MaxValue;
                    else
                        throw new Exception("Error on Parsing Advanced Chunking Parameters."); 
                }
                 
                if (StringSupport.isNullOrEmpty(chunkingParameter.MinValue))
                    chunkingParameter.MinValue = "A-Z";
                 
                chunkingParameter.MinValue.replace(" ", "");
                String[] startsWith = StringSupport.Split(chunkingParameter.MinValue, ']');
                String letters = "";
                for (int i = 0;i < startsWith.length;i++)
                {
                    String str = startsWith[i].replace("[", "").replace("]", "");
                    if (StringSupport.isNullOrEmpty(str))
                        continue;
                     
                    if (str.indexOf("-") > -1)
                    {
                        if (str.length() != 3)
                            throw new Exception("Error on Parsing Advanced Chunking Parameters.");
                         
                        if (Character.isLetter(str.charAt(0)))
                        {
                            int start = str.charAt(0);
                            int end = str.charAt(2);
                            if (start < end)
                            {
                                for (int m = start;m <= end;m++)
                                {
                                    if (m > 90 && m < 97)
                                        continue;
                                     
                                    letters = letters + String.valueOf(((char)m)) + "*,";
                                }
                            }
                            else
                            {
                                for (int m = start;m <= 122;m++)
                                {
                                    if (m > 90 && m < 97)
                                        continue;
                                     
                                    letters = letters + String.valueOf(((char)m)) + "*,";
                                }
                                for (int m = 65;m <= end;m++)
                                {
                                    if (m > 90 && m < 97)
                                        continue;
                                     
                                    letters = letters + String.valueOf(((char)m)) + "*,";
                                }
                            } 
                        }
                        else if (Character.isDigit(str.charAt(0)) && Character.isDigit(str.charAt(2)))
                        {
                            int start = str.charAt(0);
                            int end = str.charAt(2);
                            for (int m = start;m <= end;m++)
                            {
                                letters = letters + String.valueOf(((char)m)) + "*,";
                            }
                        }
                        else
                        {
                            throw new Exception("Error on Parsing Advanced Chunking Parameters.");
                        }  
                    }
                    else
                    {
                        for (int j = 0;j < str.length();j++)
                        {
                            letters = letters + str.charAt(j) + "*,";
                        }
                    } 
                }
                chunkingParameter.MinValue = letters;
                if (chunkingParameter.MinValue.endsWith("*,"))
                {
                    chunkingParameter.MinValue = chunkingParameter.MinValue.substring(0, (0) + (chunkingParameter.MinValue.length() - 1));
                }
                 
            }
             
        }
        catch (Exception exc)
        {
            throw new Exception("Error on Parsing Advanced Chunking Parameters.");
        }

        return chunkingParameter;
    }

    private ChunkingParameter getCurrentChunkingParameter(String searchParameter, RefSupport<Integer> nextChunkingParameterIndex) throws Exception {
        nextChunkingParameterIndex.setValue(0);
        String currentSearchFeild = searchParameter.substring(0, (0) + (searchParameter.indexOf(CHUNKING_DELIMITER)));
        Integer currentChunkingIndex = 0;
        for (Integer i = 0;i < m_chunkingParameter.size();i++)
        {
            ChunkingParameter cp = m_chunkingParameter.get(i);
            if (StringSupport.Compare(cp.StandardName, currentSearchFeild, true) == 0)
            {
                currentChunkingIndex = i;
                break;
            }
             
        }
        nextChunkingParameterIndex.setValue(currentChunkingIndex + 1);
        return m_chunkingParameter.get(currentChunkingIndex);
    }

    protected String[] getSearchParameter(ChunkingParameter cp) throws Exception {
        String searchParameter = "";
        String currentSearchFeild = cp.StandardName;
        searchParameter = (String)m_searchFields.get(currentSearchFeild);
        if (StringSupport.isNullOrEmpty(searchParameter))
        {
            String __dummyScrutVar4 = cp.DataType;
            if (__dummyScrutVar4.equals("DATE") || __dummyScrutVar4.equals("DATETIME"))
            {
                if (StringSupport.isNullOrEmpty(cp.MinValue))
                    searchParameter = Calendar.getInstance().getTime().AddYears(-10).ToUniversalTime().ToString("MM/dd/yyyyTHH:mm:ss") + "-" + DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), "MM/dd/yyyyTHH:mm:ss");
                else
                    searchParameter = cp.MinValue + "-" + cp.MaxValue; 
            }
            else if (__dummyScrutVar4.equals("NUMBER"))
            {
                searchParameter = cp.MinValue + "-" + cp.MaxValue;
            }
            else if (__dummyScrutVar4.equals("STARTSWITH"))
            {
                searchParameter = cp.MinValue;
            }
               
        }
         
        return new String[]{ currentSearchFeild + CHUNKING_DELIMITER + searchParameter };
    }

    private String[] splitSearchParameter(String searchParameter, int num, boolean useNextChunkingKey) throws Exception {
        String[] splitedSearchParameter = null;
        m_chunkingParameter = getChunkingParameterInDef();
        String field = "";
        int next = 0;
        RefSupport<Integer> refVar___1 = new RefSupport<Integer>();
        ChunkingParameter currentChunkingParameter = getCurrentChunkingParameter(searchParameter,refVar___1);
        next = refVar___1.getValue();
        if (useNextChunkingKey)
        {
            if (next > (m_chunkingParameter.size() - 1))
                throw TCSException.produceTCSException(TCSException.LESS_THAN_ONE_SECOND_TRUNCKING_SEARCH);
             
            currentChunkingParameter = m_chunkingParameter.get(next);
        }
         
        String currentSearchFeild = currentChunkingParameter.StandardName;
        if (useNextChunkingKey)
        {
            searchParameter = (String)m_searchFields.get(currentSearchFeild);
            if (StringSupport.isNullOrEmpty(searchParameter))
                searchParameter = getSearchParameter(currentChunkingParameter)[0];
             
        }
         
        int pos = searchParameter.indexOf(CHUNKING_DELIMITER);
        String value = searchParameter;
        if (pos > -1)
            value = searchParameter.substring(pos + 2);
         
        String __dummyScrutVar5 = currentChunkingParameter.DataType;
        if (__dummyScrutVar5.equals("DATE") || __dummyScrutVar5.equals("DATETIME"))
        {
            splitedSearchParameter = splitSearchByDate(value,num,currentChunkingParameter);
        }
        else if (__dummyScrutVar5.equals("NUMBER"))
        {
            splitedSearchParameter = splitSearchByNumber(value,num,currentChunkingParameter);
        }
        else if (__dummyScrutVar5.equals("STARTSWITH"))
        {
            splitedSearchParameter = splitSearchByAlpha(value,num,currentChunkingParameter);
        }
           
        for (int i = 0;i < splitedSearchParameter.length;i++)
        {
            if (!StringSupport.isNullOrEmpty(splitedSearchParameter[i]))
                splitedSearchParameter[i] = currentChunkingParameter.StandardName + CHUNKING_DELIMITER + splitedSearchParameter[i];
             
        }
        return splitedSearchParameter;
    }

    private String[] splitSearchByDate(String searchParameter, int num, ChunkingParameter chunkingParameter) throws Exception {
        String[] result = splitSearchDate(searchParameter,num,chunkingParameter.IsSearchByMilliSecond);
        return result;
    }

    private String[] splitSearchByNumber(String searchParameter, int num, ChunkingParameter chunkingParameter) throws Exception {
        long start = Long.valueOf(searchParameter.substring(0, (0) + (searchParameter.indexOf("-"))));
        long end = Long.valueOf(searchParameter.substring(searchParameter.indexOf("-") + 1));
        long increment = 0;
        if (num == 1)
            num++;
         
        if (num > 0)
            increment = (end - start) / num;
         
        if (increment == 0)
            increment = 1;
         
        //int min = int.Parse(chunkingParameter.MinInterVal);
        //if (increment < min)
        //{
        //    increment = min;
        //}
        String[] splitedParameter = new String[num];
        for (int i = 0;i < num;i++)
        {
            splitedParameter[i] = String.valueOf(start) + "-";
            if ((end - start - increment) > 0)
            {
                start = start + increment;
                splitedParameter[i] = splitedParameter[i] + start;
            }
            else
            {
                splitedParameter[i] = splitedParameter[i] + String.valueOf(end);
                break;
            } 
            start++;
        }
        return splitedParameter;
    }

    private String[] splitSearchByAlpha(String searchParameter, int num, ChunkingParameter chunkingParameter) throws Exception {
        String[] letters = StringSupport.Split(searchParameter, ',');
        int increment = 0;
        if (num == 1)
            num++;
         
        if (num > 0)
            increment = letters.length / num;
         
        if (increment == 0)
            increment = 1;
         
        if (increment == 1)
            num = letters.length;
        else if (letters.length % increment > 0)
            num = letters.length / increment + 1;
        else
            num = letters.length / increment;  
        String[] splitedParameter = new String[num];
        try
        {
            for (int i = 0;i < num;i++)
            {
                for (int m = i * increment;m < (i + 1) * increment;m++)
                {
                    if (m > letters.length - 1)
                        break;
                     
                    splitedParameter[i] = splitedParameter[i] + letters[m] + ",";
                }
                if (!StringSupport.isNullOrEmpty(splitedParameter[i]) && splitedParameter[i].endsWith(","))
                    splitedParameter[i] = splitedParameter[i].Remove(splitedParameter[i].length() - 1);
                 
            }
        }
        catch (Exception e)
        {
            m_connector.writeLine("Split by letters failed" + e.getMessage());
        }

        return splitedParameter;
    }

    private String[] splitSearchDate(String searchDate, int num, boolean isSearchByMilliSeconds) throws Exception {
        Date startDate;
        Date endDate;
        String dateFormat = "";
        boolean isSearchByTime = false;
        if (searchDate.indexOf('T') > -1)
        {
            dateFormat = STANDARD_DATETIMEFORMAT;
            isSearchByTime = true;
        }
        else
        {
            dateFormat = STANDARD_DATEFORMAT;
            isSearchByTime = false;
        } 
        String searchStart = StringSupport.Split(searchDate, '-')[0];
        String searchEnd = StringSupport.Split(searchDate, '-')[1];
        if (isSearchByMilliSeconds)
        {
            dateFormat = STANDARD_DATETIMEFORMAT_START_MILLISECOND;
            if (searchStart.length() < STANDARD_DATETIMEFORMAT_START_MILLISECOND.length())
            {
                searchStart = searchStart + ".000";
                searchEnd = searchEnd + ".999";
            }
             
        }
         
        startDate = Date.ParseExact(searchStart, dateFormat, null);
        endDate = Date.ParseExact(searchEnd, dateFormat, null);
        long interval = endDate.Ticks - startDate.Ticks;
        int incrementdays = 0;
        long incrementTicks = 0;
        if (num == 1)
            num++;
         
        Date date1800 = Date.ParseExact("01/01/1800", "dd/MM/yyyy", null);
        Date date1950 = Date.ParseExact("01/01/1950", "dd/MM/yyyy", null);
        boolean is1800Date = (date1800.Ticks - startDate.Ticks) >= -864000000000L && (endDate.Ticks - date1950.Ticks) >= 864000000000L;
        if (num > 0)
        {
            if (is1800Date)
                incrementdays = dateDiffInDays(date1950,endDate) / (num - 1);
            else
                incrementdays = dateDiffInDays(startDate,endDate) / num; 
        }
         
        if (num > 0)
            incrementTicks = interval / num;
         
        String[] splitedSearchDate = new String[num];
        if (!isSearchByTime)
        {
            for (int i = 0;i < num;i++)
            {
                splitedSearchDate[i] = DateTimeSupport.ToString(startDate, dateFormat) + "-";
                if (dateDiffInDays(DateTimeSupport.add(startDate,Calendar.DAY_OF_YEAR,incrementdays),endDate) > 0)
                {
                    //Todo: date Boarder check
                    if (i == 0 && is1800Date)
                        startDate = startDate.AddYears(150);
                    else
                        startDate = DateTimeSupport.add(startDate,Calendar.DAY_OF_YEAR,incrementdays); 
                    splitedSearchDate[i] = splitedSearchDate[i] + DateTimeSupport.ToString(startDate, dateFormat);
                }
                else
                {
                    splitedSearchDate[i] = splitedSearchDate[i] + DateTimeSupport.ToString(endDate, dateFormat);
                    break;
                } 
                startDate = DateTimeSupport.add(startDate,Calendar.DAY_OF_YEAR,1);
            }
        }
        else
        {
            //Time
            boolean splitBySeconds = false;
            boolean splitByMilliseconds = false;
            if ((long)(new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).TotalMinutes < 1)
                splitBySeconds = true;
             
            if (isSearchByMilliSeconds && (long)(new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).getTotalSeconds() < 1)
            {
                splitByMilliseconds = true;
                splitBySeconds = false;
            }
             
            //m_connector.WriteLine("TimeDateCaculate:" + endDate.Subtract(startDate).TotalMinutes.ToString());
            //m_connector.WriteLine("TimeDateCaculate1:" + endDate.Subtract(startDate).TotalSeconds.ToString());
            //m_connector.WriteLine("TimeDateCaculate2:" + ((int)endDate.Subtract(startDate).TotalMinutes).ToString());
            //m_connector.WriteLine("TimeDateCaculate3:" + ((int)endDate.Subtract(startDate).TotalSeconds).ToString());
            String dateFormatStart = STANDARD_DATETIMEFORMAT_START;
            String dateFormatEnd = STANDARD_DATETIMEFORMAT_END;
            if (splitBySeconds)
            {
                dateFormatStart = STANDARD_DATETIMEFORMAT_START_SECOND;
                dateFormatEnd = STANDARD_DATETIMEFORMAT_END_SECOND;
            }
            else if (splitByMilliseconds)
            {
                dateFormatStart = STANDARD_DATETIMEFORMAT_START_MILLISECOND;
                dateFormatEnd = STANDARD_DATETIMEFORMAT_END_MILLISECOND;
            }
              
            for (int i = 0;i < num;i++)
            {
                splitedSearchDate[i] = DateTimeSupport.ToString(startDate, dateFormatStart) + "-";
                if ((endDate.Ticks - startDate.Ticks) > incrementTicks)
                {
                    //Todo: date Boarder check
                    startDate = new Date(startDate.Ticks + incrementTicks);
                    splitedSearchDate[i] = splitedSearchDate[i] + DateTimeSupport.ToString(startDate, dateFormatEnd);
                }
                else
                {
                    // *** strip time
                    //time = time.AddMinutes(time.Minute * -1);
                    //time = time.AddSeconds(time.Second * -1);
                    //time = time.AddMilliseconds(time.Millisecond * -1);
                    splitedSearchDate[i] = splitedSearchDate[i] + DateTimeSupport.ToString(endDate, dateFormatEnd);
                    if (splitBySeconds)
                    {
                        endDate = endDate.AddMilliseconds(endDate.Millisecond * -1);
                        startDate = startDate.AddMilliseconds(startDate.Millisecond * -1);
                        if ((long)Math.round((new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).getTotalSeconds()) < 0)
                            splitedSearchDate[i] = null;
                         
                    }
                    else if (splitByMilliseconds)
                    {
                        if ((long)Math.Round((new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).TotalMilliseconds) < 0)
                            splitedSearchDate[i] = null;
                         
                    }
                    else
                    {
                        endDate = endDate.AddMilliseconds(endDate.Millisecond * -1);
                        startDate = startDate.AddMilliseconds(startDate.Millisecond * -1);
                        endDate = endDate.AddSeconds(endDate.Second * -1);
                        startDate = startDate.AddSeconds(startDate.Second * -1);
                        if ((long)Math.Round((new TimeSpan(Math.abs(endDate.getTime() - startDate.getTime()))).TotalMinutes) < 0)
                            splitedSearchDate[i] = null;
                         
                    }  
                    break;
                } 
                if (splitBySeconds)
                    startDate = DateTimeSupport.add(startDate,Calendar.SECOND,1);
                else if (splitByMilliseconds)
                    startDate = DateTimeSupport.add(startDate,Calendar.MILLISECOND,1);
                else
                {
                    startDate = DateTimeSupport.add(startDate,Calendar.MINUTE,1);
                }  
            }
        } 
        return splitedSearchDate;
    }

    private int dateDiffInDays(Date fromDate, Date toDate) throws Exception {
        return (new TimeSpan(Math.abs(toDate.getTime() - fromDate.getTime()))).Days;
    }

    private int getRecordCountForOverrideLimt() throws Exception {
        int n = 0;
        try
        {
            if (m_engine.isRETSClient())
            {
                m_engine.setSearchByRecordCount(true);
            }
             
            m_engine.setCurrentSearchStatus(m_status + (Environment.TickCount & Integer.MAX_VALUE).toString());
            setLogMode();
            m_engine.runMainScript(0);
            if (m_engine.isRETSClient())
            {
                try
                {
                    String content = Tcs.Mls.Util.getFileContent(m_engine.getResultsFilename());
                    n = Integer.valueOf(Tcs.Mls.Util.getRecordCount(content));
                }
                catch (Exception exc)
                {
                }
            
            }
            else
            {
                n = m_engine.getMLSRecordCount();
            } 
        }
        catch (MLSException texc)
        {
            //SetAgentOfficeID();
            //m_engine.setSearchByRecordCount(false);
            if (texc.getCode() != MLSException.CODE_NO_RECORD_FOUND)
                throw texc;
             
        }
        catch (Exception exc)
        {
            throw exc;
        }
        finally
        {
            if (m_engine.isRETSClient())
            {
                m_engine.setSearchByRecordCount(false);
            }
             
        }
        return n;
    }

    protected int getTCSErrorCode(String msg) throws Exception {
        if (msg == null || msg.length() == 0)
            return 0;
        else
            msg = msg.toLowerCase(); 
        String tcsErrMsg = "";
        int tcsErrCode = 0;
        for (int i = 1;i < 100;i++)
        {
            tcsErrMsg = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCSERROR,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_TCSERRORSTRING + i);
            if (tcsErrMsg == null || tcsErrMsg.length() == 0)
                break;
             
            if (msg.contains(tcsErrMsg.toLowerCase()))
            {
                tcsErrCode = Integer.valueOf(m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCSERROR,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_TCSERRORCODE + i));
                break;
            }
             
        }
        return tcsErrCode;
    }

    protected public boolean isRETSError(String msg) throws Exception {
        if (msg != null && msg.length() > 0 && m_engine.isRETSClient())
            for (int i = 0;i < RETS_ERROR_CODES.length;i++)
            {
                if (msg.indexOf(RETS_ERROR_CODES[i]) > -1)
                    return true;
                 
            }
         
        return false;
    }

    protected public void addXMLHeader() throws Exception {
        m_resultBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n<Listings>\r\n");
    }

    //if (m_resultBuffer.Length > 20000000)
    //{
    //    if (m_requestSucess)
    //        m_tcs.ResultHeader = "<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n";
    //    else
    //        m_tcs.ResultHeader = "<TCResult ReplyCode=\"1\" ReplyText=\"Failure\">\r\n";
    //    m_tcs.ResultHeader += "<Listings>\r\n";
    //}
    //else
    //{
    //    m_resultBuffer.Insert(0, "<Listings>\r\n");
    //    if (m_requestSucess)
    //        m_resultBuffer.Insert(0, "<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n");
    //    else
    //        m_resultBuffer.Insert(0, "<TCResult ReplyCode=\"1\" ReplyText=\"Failure\">\r\n");
    //}
    protected public void addXMLFooter() throws Exception {
        String footer = "</Listings>\r\n<Notes>\r\n" + m_resultNote.toString() + "</Notes>\r\n</TCResult>\r\n";
        m_resultBuffer.append("</Listings>\r\n");
        addNotes();
        m_resultBuffer.append("</TCResult>\r\n");
    }

    protected void addNoRecordFoundNote() throws Exception {
        if (getResultListingCount() == 0 && !(this instanceof GetRecordCount || this instanceof GetRecordCountDataagg || this instanceof GetRecordCountPlus || this instanceof GetRecordCountAgentRoster))
        {
            m_resultNote.append("<Note ID=\"" + m_noteId + "\" ReplyCode=\"" + TCSException.MLS_ERROR_NO_RECORD_FOUND + "\" " + "ReplyText=\"" + TCSException.getTCSMessage(TCSException.MLS_ERROR_NO_RECORD_FOUND) + "\" Comment=\"" + "" + "\"/>\r\n");
            m_noteId++;
        }
         
    }

    protected public void addNotes() throws Exception {
        String footer = "<Notes>\r\n" + m_resultNote.toString() + "</Notes>\r\n";
        m_resultBuffer.append(footer);
    }

    protected public void addXMLNote(int code, String text) throws Exception {
        if (code == TCSException.MLS_ERROR_NO_RECORD_FOUND)
            return ;
         
        String comment = "";
        text = Tcs.Mls.Util.convertStringToXML(text);
        try
        {
            String moduleName = Tcs.Mls.Util.convertStringToXML(m_engine.getEnvironment().getModuleName());
            if (m_engine != null)
                if (!m_isSearchByMLSNumber)
                    comment = "Module name:" + moduleName + "; Status:" + m_status;
                else
                    comment = "Module name:" + moduleName + "; MLS Number:" + m_mlsNumber; 
             
        }
        catch (Exception e)
        {
        }

        m_resultNote.append("<Note ID=\"" + m_noteId + "\" ReplyCode=\"" + code + "\" " + "ReplyText=\"" + text + "\" Comment=\"" + comment + "\"/>\r\n");
        m_noteId++;
    }

    protected public void addMetadataXMLNote(int code, String text) throws Exception {
        String comment = "";
        m_resultNote.append("<RETSMetadataNote ID=\"" + "1" + "\" ReplyCode=\"" + code + "\" " + "ReplyText=\"" + text + "\" Comment=\"" + comment + "\"/>\r\n");
    }

    public int[] getResultField() throws Exception {
        return m_resultField;
    }

    protected public void getOutputXML() throws Exception {
        m_connector.writeLine("GetOutPutXML");
        if (m_engine.getIsDownloadIDs())
        {
            addIDsForDownloadIDOnlySearch();
            return ;
        }
         
        //System.Text.StringBuilder sb = new System.Text.StringBuilder();
        boolean isDemo = m_engine.isDemoClient();
        boolean foundDemoListing = false;
        MLSCmaFields fields = null;
        fields = m_engine.getCmaFields();
        checkNormFieldDecoupleDataFlag(fields);
        if (!isDemo || this instanceof GetPictures || m_isSearchByMLSNumber)
            m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        else
        {
            String __dummyScrutVar6 = m_status;
            if (__dummyScrutVar6.equals("A"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ACTIVE);
            }
            else if (__dummyScrutVar6.equals("S"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_SOLD);
            }
            else if (__dummyScrutVar6.equals("P"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_PENDING);
            }
            else if (__dummyScrutVar6.equals("E"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_EXPIRED);
            }
                
        } 
        if (getIsReturnMlsNumberOnlyRequest())
        {
            getOutputXmlForReturnMlsNumberOnlyRequeset(m_records,fields);
            return ;
        }
         
        MLSRecord rec = m_records.getFirstRecord();
        String fieldValue = "";
        String fieldName = "";
        boolean bAddField = true;
        int recordCount = 0;
        //MLSRecord buyerAgentRecord = null;
        //MLSRecord listAgentRecord = null;
        //MLSRecord buyerOfficeRecord = null;
        //MLSRecord listOfficeRecord = null;
        //string buyerAgentID = "";
        //string listAgentID = "";
        //string buyerOfficeID = "";
        //string listOfficeID = "";
        //bool needAgentOfficeInfo = false;
        boolean isReturnNetworkPath = m_connector.isReturnNetworkPath();
        //needAgentOfficeInfo = m_connector.NeedAgentOfficeSearch;
        String zip = (String)m_connector.getSearchFields().get(STD_SEARCH_FIELD[ST_ZIP]);
        int zipCount = 1;
        String[] zips = null;
        if (!StringSupport.isNullOrEmpty(zip))
        {
            zips = StringSupport.Split(zip, ',');
            zipCount = zips.length;
        }
         
        while (rec != null)
        {
            m_resultBuffer.append("<Listing");
            if (!(this instanceof GetPictures))
            {
                String[] fl = new String[]{ "" };
                String dataStandardNoteId = "";
                String demoListDate = "";
                String saleDate = "";
                String listDate = "";
                String statusDate = "";
                String listPrice = "";
                String salePrice = "";
                String searchPrice = "";
                String bathRooms = "";
                String sqft = "";
                boolean goNextRec = false;
                for (int i = 0;i < getResultField().length;i++)
                {
                    if (goNextRec)
                        break;
                     
                    fieldValue = "";
                    fieldName = Tcs.Mls.TCSStandardResultFields.getXmlName(getResultField()[i]);
                    //]field.getName();
                    bAddField = true;
                    String mlsValue = "";
                    if (!isDemo)
                    {
                        switch(getResultField()[i])
                        {
                            case Tcs.Mls.TCSStandardResultFields.STDF_RECORDID: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (fieldValue.startsWith("TP#"))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPEMLS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIER: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIES: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNT: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENT: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],false);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPENORM: 
                                fieldValue = m_connector.getPropertyType();
                                if (!StringSupport.isNullOrEmpty(fieldValue))
                                {
                                    if (fieldValue.indexOf(',') > -1)
                                    {
                                        fieldValue = m_connector.getPropertyTypeTMK();
                                    }
                                     
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMAIDENTIFIER).ordinal()] == -10)
                                    fieldValue = getStandardStatus(rec.getMLSRecordType());
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    if (!fieldValue.equals("A") && !fieldValue.equals("S") && !fieldValue.equals("P") && !fieldValue.equals("E"))
                                        fieldValue = "";
                                     
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_DEFTYPE_NODEFNAME: 
                                fieldValue = m_connector.getModuleName();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFVIEWS: 
                                bAddField = false;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_NOTES: 
                                fieldValue = rec.getNotes();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFNUMUNITS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMABEDROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFFULLBATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFHALFBATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFTHREEQBATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFQBATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGPRICE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASALEPRICE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                fieldValue = convertToInt(fieldValue);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMABATHROOMS).ordinal()] == -10)
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMS,false);
                                    fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM,mlsValue);
                                    if (!mlsValue.toUpperCase().equals(fieldValue.toUpperCase()))
                                        dataStandardNoteId = (dataStandardNoteId.length() == 0 ? "" : (dataStandardNoteId + ",")) + addResultValidationNote(getResultField()[i]);
                                     
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    fieldValue = convertToInt(fieldValue);
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMASQUAREFEET).ordinal()] == -10)
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET,false);
                                    fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM,mlsValue);
                                    if (!mlsValue.toUpperCase().equals(fieldValue.toUpperCase()))
                                        dataStandardNoteId = (dataStandardNoteId.length() == 0 ? "" : (dataStandardNoteId + ",")) + addResultValidationNote(getResultField()[i]);
                                     
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    fieldValue = convertToInt(fieldValue);
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMAAGE).ordinal()] == -10)
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE,false);
                                    fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM,mlsValue);
                                    if (!mlsValue.toUpperCase().equals(fieldValue.toUpperCase()))
                                        dataStandardNoteId = (dataStandardNoteId.length() == 0 ? "" : (dataStandardNoteId + ",")) + addResultValidationNote(getResultField()[i]);
                                     
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    fieldValue = convertToInt(fieldValue);
                                    if (fieldValue.length() != 4)
                                        fieldValue = "";
                                     
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMALOTSIZE).ordinal()] == -10)
                                {
                                    try
                                    {
                                        mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZE,false);
                                        fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM,mlsValue);
                                    }
                                    catch (Exception e)
                                    {
                                        fieldValue = "";
                                    }
                                
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    double Num;
                                    boolean boolVar___0 = !DoubleSupport.tryParse(fieldValue, refVar___2);
                                    if (boolVar___0)
                                    {
                                        RefSupport<Double> refVar___2 = new RefSupport<Double>();
                                        fieldValue = "";
                                        Num = refVar___2.getValue();
                                    }
                                     
                                    if (!StringSupport.isNullOrEmpty(fieldValue))
                                        fieldValue = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0.#########}"),Num);
                                     
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIESNORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMASTORIES).ordinal()] == -10)
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIES,false);
                                    fieldValue = rec.validateSTDFStories(mlsValue);
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    double Num;
                                    boolean boolVar___1 = !DoubleSupport.tryParse(fieldValue, refVar___3);
                                    if (boolVar___1)
                                    {
                                        RefSupport<Double> refVar___3 = new RefSupport<Double>();
                                        fieldValue = "";
                                        Num = refVar___3.getValue();
                                    }
                                    else
                                        fieldValue = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0.0}"),Num); 
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNTNORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMATAXAMOUNT).ordinal()] == -10)
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNT,false);
                                    fieldValue = rec.validateSTDFTax(mlsValue);
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    fieldValue = convertToInt(fieldValue);
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENTNORM: 
                                if (NormDecoupleData[((Enum)NormFields.CMAASSESSMENT).ordinal()] == -10)
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENT,false);
                                    fieldValue = rec.validateSTDFTax(mlsValue);
                                }
                                else
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    fieldValue = convertToInt(fieldValue);
                                } 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDFORECLOSEDREO: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDAUCTION: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDSHORTSALE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (!StringSupport.isNullOrEmpty(fieldValue))
                                {
                                    fieldValue = StringSupport.Trim(fieldValue);
                                    if (!(fieldValue.equals("Y") || fieldValue.equals("N")))
                                        fieldValue = "";
                                     
                                }
                                else
                                    fieldValue = ""; 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAAREA: 
                                fieldValue = rec.getCMAArea();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTID: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTFNAME: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTLNAME: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTEMAIL: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTPHONE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTID: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTFNAME: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTLNAME: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTPHONE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTEMAIL: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERBROKERID: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYEROFFICENAME: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTBROKERID: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICENAME: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGDATE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                listDate = fieldValue;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASALEDATE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                saleDate = fieldValue;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFSTATUSDATE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],false);
                                if (StringSupport.isNullOrEmpty(fieldValue))
                                {
                                    switch(rec.getMLSRecordType())
                                    {
                                        case MLSRecord.TYPE_ACTIVE: 
                                        case MLSRecord.TYPE_PENDING: 
                                            fieldValue = listDate;
                                            break;
                                        case MLSRecord.TYPE_SOLD: 
                                            fieldValue = saleDate;
                                            break;
                                        case MLSRecord.TYPE_EXPIRED: 
                                            fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_STDFEXPIREDDATE,true);
                                            if (StringSupport.isNullOrEmpty(fieldValue))
                                                fieldValue = listDate;
                                             
                                            break;
                                        default: 
                                            fieldValue = "";
                                            break;
                                    
                                    }
                                }
                                 
                                break;
                            default: 
                                // STANDERD_STATUS[0];
                                //case TCSStandardResultFields.STDF_STDFLAT:
                                //case TCSStandardResultFields.STDF_STDFLONG:
                                //    fieldValue = rec.getTCSStdFieldValue(getResultField()[i], true);
                                //    break;
                                //CmaField field = fields.getStdField( getResultField()[i] );
                                //if( field != null )
                                //long time = (System.DateTime.Now.Ticks - 621355968000000000);
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                break;
                        
                        }
                    }
                    else
                    {
                        //else
                        //	if( getResultField()[i] == TCSStandardResultFields.STDF_TPOSTATE )
                        //		fieldValue = m_connector.getStateProvince();
                        //if(((System.DateTime.Now.Ticks - 621355968000000000) - time)>0)
                        //{
                        //    m_connector.WriteLine(getResultField()[i] + "= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time));
                        //    fieldValue = rec.getTCSStdFieldValue(getResultField()[i], false);
                        //    m_connector.WriteLine("fieldName = " + fieldName + " Field MLS value = " + fieldValue);
                        //}
                        // logic for Demo client
                        switch(getResultField()[i])
                        {
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPEMLS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIER: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIES: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNT: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENT: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],false);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_RECORDID: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (m_isSearchByMLSNumber)
                                {
                                    if (m_mlsNumber.IndexOf(fieldValue, 0, StringComparison.CurrentCultureIgnoreCase) < 0)
                                    {
                                        goNextRec = true;
                                        continue;
                                    }
                                    else
                                        foundDemoListing = true; 
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPENORM: 
                                fieldValue = m_connector.getPropertyType();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM: 
                                fieldValue = getStandardStatus(rec.getMLSRecordType());
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_DEFTYPE_NODEFNAME: 
                                fieldValue = m_connector.getModuleName();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFVIEWS: 
                                bAddField = false;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_NOTES: 
                                fieldValue = rec.getNotes();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM: 
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMS,false);
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM,mlsValue);
                                if (!isMatchSearchCriteria(ST_TOTALBATHS,fieldValue))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET,false);
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM,mlsValue);
                                if (!isMatchSearchCriteria(ST_SQFT,fieldValue))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                if (StringSupport.isNullOrEmpty(fieldValue))
                                    fieldValue = sqft;
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET: 
                                fieldValue = rec.getStdFieldValue(getResultField()[i],false);
                                if (StringSupport.isNullOrEmpty(fieldValue))
                                    fieldValue = sqft = ((Integer.valueOf(Tcs.Mls.Util.getRandomNumber("1000-10000")) / 10) * 10) + "";
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM: 
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE,false);
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM,mlsValue);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMABEDROOMS: 
                                try
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    if (fieldValue != null && fieldValue.length() > 0)
                                        fieldValue = Integer.valueOf(fieldValue) + "";
                                     
                                }
                                catch (Exception e)
                                {
                                    fieldValue = "";
                                }

                                if (!isMatchSearchCriteria(ST_BEDS,fieldValue))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFFULLBATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFHALFBATHROOMS: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFQBATHROOMS: 
                                try
                                {
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                    if (fieldValue != null && fieldValue.length() > 0)
                                        fieldValue = Integer.valueOf(fieldValue) + "";
                                     
                                }
                                catch (Exception e)
                                {
                                    fieldValue = "";
                                }

                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM: 
                                try
                                {
                                    mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZE,false);
                                    fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM,mlsValue);
                                }
                                catch (Exception e)
                                {
                                    fieldValue = "";
                                }

                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGPRICE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (!isMatchSearchCriteria(ST_LISTPRICE,fieldValue))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASALEPRICE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (!isMatchSearchCriteria(ST_SALEPRICE,fieldValue))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFSEARCHPRICE: 
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (!isMatchSearchCriteria(ST_SEARCHPRICE,fieldValue))
                                {
                                    goNextRec = true;
                                    continue;
                                }
                                 
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIESNORM: 
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIES,false);
                                fieldValue = rec.validateSTDFStories(mlsValue);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNTNORM: 
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNT,false);
                                fieldValue = rec.validateSTDFTax(mlsValue);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENTNORM: 
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENT,false);
                                fieldValue = rec.validateSTDFTax(mlsValue);
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMAAREA: 
                                fieldValue = rec.getCMAArea();
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGDATE: 
                                String searchListDate = (String)m_connector.getSearchFields().get(STD_SEARCH_FIELD[ST_LISTDATE]);
                                String searchStatusDate = (String)m_connector.getSearchFields().get(STD_SEARCH_FIELD[ST_STATUSDATE]);
                                String searchSaleDate = (String)m_connector.getSearchFields().get(STD_SEARCH_FIELD[ST_SALEDATE]);
                                String __dummyScrutVar10 = m_status;
                                if (__dummyScrutVar10.equals("A") || __dummyScrutVar10.equals("E") || __dummyScrutVar10.equals("P"))
                                {
                                    searchSaleDate = "";
                                }
                                 
                                if (!StringSupport.isNullOrEmpty(searchStatusDate))
                                {
                                    statusDate = getDemoDate(searchStatusDate);
                                    String __dummyScrutVar11 = getStandardStatus(rec.getMLSRecordType());
                                    if (__dummyScrutVar11.equals("A"))
                                    {
                                        demoListDate = statusDate;
                                        saleDate = "";
                                    }
                                    else if (__dummyScrutVar11.equals("E") || __dummyScrutVar11.equals("P"))
                                    {
                                        demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(statusDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                        saleDate = "";
                                    }
                                    else if (__dummyScrutVar11.equals("S"))
                                    {
                                        saleDate = statusDate;
                                        demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(statusDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                    }
                                       
                                }
                                else if (!StringSupport.isNullOrEmpty(searchSaleDate))
                                {
                                    saleDate = getDemoDate(searchSaleDate);
                                    demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(saleDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                    statusDate = saleDate;
                                }
                                else
                                {
                                    demoListDate = getDemoDate(searchListDate);
                                    String __dummyScrutVar12 = getStandardStatus(rec.getMLSRecordType());
                                    if (__dummyScrutVar12.equals("A"))
                                    {
                                        statusDate = demoListDate;
                                        saleDate = "";
                                    }
                                    else if (__dummyScrutVar12.equals("E") || __dummyScrutVar12.equals("P"))
                                    {
                                        statusDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.parse(demoListDate), "MM/dd/yyyy"),60);
                                        saleDate = "";
                                    }
                                    else if (__dummyScrutVar12.equals("S"))
                                    {
                                        saleDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.parse(demoListDate), "MM/dd/yyyy"),60);
                                        statusDate = saleDate;
                                    }
                                       
                                }  
                                fieldValue = demoListDate;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_CMASALEDATE: 
                                fieldValue = saleDate;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFSTATUSDATE: 
                                fieldValue = statusDate;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD: 
                                fieldValue = statusDate;
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFPENDINGDATE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFINACTIVEDATE: 
                            case Tcs.Mls.TCSStandardResultFields.STDF_STDFEXPIREDDATE: 
                                fieldValue = "";
                                break;
                            case Tcs.Mls.TCSStandardResultFields.STDF_TPOZIP: 
                                if (zips == null)
                                    fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                else
                                {
                                    Random random = new Random();
                                    int pos = random.Next(0, zips.length);
                                    if (pos > (zips.length - 1))
                                        pos = zips.length - 1;
                                     
                                    fieldValue = zips[pos];
                                } 
                                break;
                            default: 
                                // case TCSStandardResultFields.STDF_STDFLAT:
                                // case TCSStandardResultFields.STDF_STDFLONG:
                                // fieldValue = rec.getTCSStdFieldValue(getResultField()[i], true);
                                //     break;
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                break;
                        
                        }
                    } 
                    if (bAddField)
                        m_resultBuffer.append(" " + fieldName + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\"");
                     
                }
                if (goNextRec)
                {
                    int pos = m_resultBuffer.toString().lastIndexOf("<Listing");
                    m_resultBuffer.delete(pos, (pos)+(m_resultBuffer.length() - pos));
                    rec = m_records.getNextRecord();
                    continue;
                }
                 
                if (isDemo)
                    foundDemoListing = true;
                 
                String footNote = "";
                if (m_isAddWarningNote)
                {
                    if (dataStandardNoteId.length() > 0)
                        footNote = m_warningNoteId + "," + dataStandardNoteId;
                    else
                        footNote = m_warningNoteId + ""; 
                }
                else if (dataStandardNoteId.length() > 0)
                    footNote = dataStandardNoteId;
                  
                m_resultBuffer.append(" NoteID=\"").append(footNote + "\"");
                m_resultBuffer.append(">\r\n");
                //Add Features, Room Dimensions, Views
                String prefix = "";
                if (m_includeFeature)
                {
                    m_resultBuffer.append("<Features>\r\n");
                    try
                    {
                        //long time2 = (System.DateTime.Now.Ticks - 621355968000000000);
                        fl = rec.getSTDFFieldList(Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE,new int[]{ Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM, Tcs.Mls.TCSStandardResultFields.STDF_STDFVIEWS },true);
                        for (int j = 0;j < fl.length;j++)
                        {
                            // m_connector.WriteLine("Get feature= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time2));
                            if (fl[j] != null && fl[j].length() > 0 && fl[j].indexOf(":") > -1)
                            {
                                prefix = fl[j].substring(0, (0) + ((fl[j].indexOf(":"))));
                                if (StringSupport.isNullOrEmpty(prefix))
                                    continue;
                                 
                                m_resultBuffer.append("<FeatureCategory Name=\"" + Tcs.Mls.Util.convertStringToXML(prefix) + "\">\r\n");
                                String[] subValues = StringSupport.Split(fl[j].substring(fl[j].indexOf(":") + 1), ',');
                                for (int n = 0;n < subValues.length;n++)
                                {
                                    m_resultBuffer.append("<Feature Name=\"" + Tcs.Mls.Util.convertStringToXML(StringSupport.Trim(subValues[n])) + "\"/>\r\n");
                                }
                                m_resultBuffer.append("</FeatureCategory>\r\n");
                            }
                             
                        }
                    }
                    catch (Exception e)
                    {
                        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                        m_connector.writeLine("Error on retrieving features" + e.getMessage());
                    }

                    m_resultBuffer.append("</Features>\r\n");
                }
                 
                if (m_includeRoomDimention)
                {
                    m_resultBuffer.append("<RoomDimensions>\r\n");
                    m_resultBuffer.append(rec.getRoomDimensions(false,true,false));
                    //fl = rec.getSTDFFieldList(TCSStandardResultFields.STDF_STDFROOMDIM, null, true);
                    //try
                    //{
                    //    if (fl != null && fl.Length > 0)
                    //    {
                    //        prefix = "";
                    //        for (int j = 0; j < fl.Length; j++)
                    //        {
                    //            if (fl[j] != null && fl[j].Length > 0)
                    //            {
                    //                if (fl[j].IndexOf(":") > -1)
                    //                {
                    //                    string[] dim = fl[j].Split(':');
                    //                    prefix = dim[0];
                    //                    string[] subValues = dim[1].ToLower().Split('x');
                    //                    if (subValues.Length > 1)
                    //                        m_resultBuffer.Append("<Room Name=\"" + Util.convertStringToXML(prefix) + "\" Width=\"" + Util.convertStringToXML(subValues[0]).Trim() + "\" Length=\"" + Util.convertStringToXML(subValues[1]).Trim() + "\"/>\r\n");
                    //                }
                    //            }
                    //        }
                    //    }
                    //}
                    //catch (System.Exception e)
                    //{
                    //    //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                    //    m_connector.WriteLine("Error on retrieving room dimensions" + e.Message);
                    //}
                    m_resultBuffer.append("</RoomDimensions>\r\n");
                }
                 
                if (m_includeView)
                {
                    m_resultBuffer.append("<Views>\r\n");
                    try
                    {
                        fl = rec.getViewsList();
                        if (fl != null && fl.length > 0)
                        {
                            for (int j = 0;j < fl.length;j++)
                                m_resultBuffer.append("<View Name=\"" + Tcs.Mls.Util.convertStringToXML(StringSupport.Trim(fl[j])) + "\"/>\r\n");
                        }
                         
                    }
                    catch (Exception e)
                    {
                        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                        m_connector.writeLine("Error on retrieving view" + e.getMessage());
                    }

                    m_resultBuffer.append("</Views>\r\n");
                }
                 
            }
            else
            {
                fieldValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID,true);
                m_resultBuffer.append(" " + Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID) + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\"");
                m_resultBuffer.append(">\r\n");
            } 
            if (m_connector.getInputPictureFlag() == FLAG_MULTIPLE_PICTURE || m_connector.getInputPictureFlag() == FLAG_ONE_PICTURE)
            {
                String picFileName = "";
                String objectId = "";
                if (m_engine.getMulitiPictures().isGetMultiPicture() && !isDemo)
                {
                    String[] arrPicFileName = rec.getMultiPictureFileName();
                    if (arrPicFileName != null)
                    {
                        for (int i = 0;i < arrPicFileName.length;i++)
                        {
                            int pos1 = arrPicFileName[i].lastIndexOf("\\");
                            int pos2 = arrPicFileName[i].lastIndexOf(".");
                            objectId = arrPicFileName[i].Substring(pos1 + 1, (pos2)-(pos1 + 1));
                            m_connector.saveTaskFileResult(objectId,arrPicFileName[i]);
                            m_resultBuffer.append("<Picture");
                            //sb.append( " Url=" + "\"" + picFileName + "\"" );
                            if (m_tcs.getIsRealTime())
                                m_resultBuffer.append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?lp=" + Tcs.Mls.Util.base64Encode(arrPicFileName[i])));
                            else
                            {
                                if (!isReturnNetworkPath)
                                    m_resultBuffer.append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?message_header=" + m_connector.getMessageHeader() + "&object_id=" + objectId) + "\"");
                                else
                                    m_resultBuffer.append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(arrPicFileName[i]) + "\""); 
                            } 
                            m_resultBuffer.append("/>\r\n");
                        }
                    }
                     
                }
                else
                {
                    picFileName = rec.getPictureFileName();
                    if (picFileName != null && picFileName.length() > 0 && m_engine.getEnvironment().isPictureFileSupported(picFileName))
                    {
                        int pos1 = picFileName.lastIndexOf("\\");
                        int pos2 = picFileName.lastIndexOf(".");
                        objectId = picFileName.Substring(pos1 + 1, (pos2)-(pos1 + 1));
                        m_connector.saveTaskFileResult(objectId,picFileName);
                        m_resultBuffer.append("<Picture");
                        //sb.append( " Url=" + "\"" + picFileName + "\"" );
                        if (m_tcs.getIsRealTime())
                            m_resultBuffer.append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?lp=" + Tcs.Mls.Util.base64Encode(picFileName)));
                        else
                        {
                            if (!isReturnNetworkPath)
                            {
                                m_resultBuffer.append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?message_header=" + m_connector.getMessageHeader() + "&object_id=" + objectId) + "\"");
                            }
                            else
                            {
                                m_resultBuffer.append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(picFileName) + "\"");
                            } 
                        } 
                        m_resultBuffer.append("/>\r\n");
                    }
                     
                } 
            }
             
            m_resultBuffer.append("</Listing>\r\n");
            rec = m_records.getNextRecord();
            recordCount++;
            m_overrideLimitCount++;
            checkResultSize();
            if (getIsOverrideLimit() && m_overrideLimitCount > getOverrideRecordLimit())
                break;
             
            if (isDemo)
            {
                boolean stopGettingMoreRecords = false;
                String __dummyScrutVar13 = m_status;
                if (__dummyScrutVar13.equals("A") || __dummyScrutVar13.equals("S"))
                {
                    if (recordCount >= 120 * zipCount)
                        stopGettingMoreRecords = true;
                     
                }
                else if (__dummyScrutVar13.equals("P") || __dummyScrutVar13.equals("E"))
                {
                    if (recordCount >= 30 * zipCount)
                        stopGettingMoreRecords = true;
                     
                }
                  
                if (stopGettingMoreRecords)
                    break;
                 
            }
             
        }
        if (isDemo && !foundDemoListing && !m_tcs.isGetPicture())
        {
            addXMLNote(TCSException.MLS_ERROR_NO_RECORD_FOUND,Tcs.Mls.Util.convertStringToXML(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND)));
        }
         
    }

    private void getOutputXmlForReturnMlsNumberOnlyRequeset(MLSRecords records, MLSCmaFields fields) throws Exception {
        MLSRecord rec = records.getFirstRecord();
        String fieldName = Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID);
        while (rec != null)
        {
            String fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID,true);
            if (StringSupport.isNullOrEmpty(fieldValue))
                continue;
             
            if (fieldValue.startsWith("TP#"))
                continue;
             
            m_resultBuffer.append("<Listing");
            m_resultBuffer.append(" " + fieldName + "=\"" + Tcs.Mls.Util.convertStringToXML(!StringSupport.isNullOrEmpty(fieldValue) ? fieldValue : "") + "\"");
            m_resultBuffer.append("/>\r\n");
            rec = m_records.getNextRecord();
            m_overrideLimitCount++;
            checkResultSize();
            if (getIsOverrideLimit() && m_overrideLimitCount > getOverrideRecordLimit())
                break;
             
        }
    }

    protected String convertToInt(String value) throws Exception {
        if (StringSupport.isNullOrEmpty(value))
            return "";
         
        try
        {
            double doubleValue;
            boolean boolVar___2 = DoubleSupport.tryParse(value, refVar___4);
            if (boolVar___2)
            {
                RefSupport<Double> refVar___4 = new RefSupport<Double>();
                value = String.valueOf((int)Math.round(doubleValue + 0.001));
                doubleValue = refVar___4.getValue();
            }
            else
                value = ""; 
        }
        catch (Exception e)
        {
            value = "";
        }

        return value;
    }

    protected boolean isMatchSearchCriteria(int stField, String fieldValue) throws Exception {
        if (m_isSearchByMLSNumber)
            return true;
         
        String searchValue = (String)m_searchFields.get(STD_SEARCH_FIELD[stField]);
        if (StringSupport.isNullOrEmpty(searchValue))
            return true;
         
        if (StringSupport.isNullOrEmpty(fieldValue))
            return false;
         
        try
        {
            int min = 0;
            int max = 0;
            if (searchValue.indexOf('-') > -1)
            {
                min = ((int)((Double.valueOf(StringSupport.Split(searchValue, '-')[0]))));
                max = ((int)((Double.valueOf(StringSupport.Split(searchValue, '-')[1]))));
            }
            else
            {
                min = max = Integer.valueOf(searchValue);
            } 
            int val = ((int)((Double.valueOf(fieldValue))));
            return val <= max && val >= min;
        }
        catch (Exception exc)
        {
        }

        return true;
    }

    protected void checkNormFieldDecoupleDataFlag(MLSCmaFields cmaFields) throws Exception {
        for (int i = 0;i < NormDecoupleData.length;i++)
        {
            if (NormDecoupleData[i] > -1)
            {
                net.toppro.components.mls.engine.CmaField field = cmaFields.getStdField(NormDecoupleData[i]);
                if (field != null && !field.getRecordPosition().equals("\\\\"))
                    NormDecoupleData[i] = -11;
                else
                    NormDecoupleData[i] = -10; 
            }
             
        }
    }

    protected String getDemoListingNumber(MLSRecord rec, int stField, int i) throws Exception {
        String val = (String)m_connector.getSearchFields().get(STD_SEARCH_FIELD[stField]);
        if (StringSupport.isNullOrEmpty(val))
            val = "";
         
        if (val.indexOf("-") > -1 && !val.equals("10000-99000000"))
        {
            val = Tcs.Mls.Util.getRandomNumber(val);
            switch(stField)
            {
                case ST_LISTPRICE: 
                case ST_SALEPRICE: 
                case ST_SEARCHPRICE: 
                    val = ((Integer.valueOf(val) / 1000) * 1000) + "";
                    break;
                case ST_SQFT: 
                    val = ((Integer.valueOf(val) / 10) * 10) + "";
                    break;
            
            }
        }
        else if (val.length() == 0 || val.equals("10000-99000000"))
        {
            val = rec.getTCSStdFieldValue(getResultField()[i],true);
            switch(stField)
            {
                case ST_TOTALBATHS: 
                case ST_SQFT: 
                    String mlsValue = rec.getStdFieldValue(getResultField()[i],false);
                    val = rec.getTCSStdFieldValue(getResultField()[i],mlsValue);
                    if (StringSupport.isNullOrEmpty(val))
                        val = ((Integer.valueOf(Tcs.Mls.Util.getRandomNumber("1000-10000")) / 10) * 10) + "";
                     
                    break;
            
            }
        }
          
        if (val != null && val.length() > 0)
            val = (int)Math.round(Double.valueOf(val)) + "";
         
        return val;
    }

    protected String getDemoDate(String demoDate) throws Exception {
        //System.Collections.Hashtable ht = m_connector.getSearchFields();
        //string demoListDate = "";
        //string demoSaleDate = "";
        //switch (status)
        //{
        //    case "A":
        //    case "E":
        //    case "P":
        //        demoListDate = (string)ht[STD_SEARCH_FIELD[ST_STATUSDATE]];
        //        if (string.IsNullOrEmpty(demoListDate))
        //            demoListDate = (string)ht[STD_SEARCH_FIELD[ST_LISTDATE]];
        //        break;
        //    case "S":
        //        demoSaleDate = (string)ht[STD_SEARCH_FIELD[ST_STATUSDATE]];
        //        if (string.IsNullOrEmpty(demoSaleDate))
        //            demoSaleDate = (string)ht[STD_SEARCH_FIELD[ST_SALEDATE]];
        //        break;
        //}
        if (demoDate == null)
            demoDate = "";
         
        if (demoDate.indexOf("-") > -1)
        {
            String[] dates = StringSupport.Split(demoDate, '-');
            demoDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.parse(dates[0]),DateTimeSupport.parse(dates[1]));
        }
        else //if (status.Equals("S"))
        //    demoDate = Util.GetRandomDate(DateTime.Parse(demoDate).AddDays(-60), DateTime.Parse(demoDate));
        if (demoDate.length() == 0)
        {
            demoDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.add(Calendar.getInstance().getTime(),Calendar.MONTH,-6),Calendar.getInstance().getTime());
        }
          
        return demoDate;
    }

    protected String addResultValidationNote(int stdfField) throws Exception {
        if (m_dataStandardNote[stdfField] == null || m_dataStandardNote[stdfField].length() == 0)
        {
            String text = MLSUtil.formatString(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.DATA_STANDARDIZATION_APPLIED),Tcs.Mls.TCSStandardResultFields.getXmlName(stdfField));
            m_resultNote.append("<Note ID=\"" + m_noteId + "\" ReplyCode=\"" + TCSException.DATA_STANDARDIZATION_APPLIED + "\" " + "ReplyText=\"" + text + "\" Comment=\"\"/>\r\n");
            m_dataStandardNote[stdfField] = m_noteId + "";
            m_noteId++;
        }
         
        return m_dataStandardNote[stdfField];
    }

    public String getStandardStatus(int type) throws Exception {
        String status = "";
        switch(type)
        {
            case MLSRecord.TYPE_ACTIVE: 
                status = STANDERD_STATUS[0];
                break;
            case MLSRecord.TYPE_SOLD: 
                status = STANDERD_STATUS[1];
                break;
            case MLSRecord.TYPE_PENDING: 
                status = STANDERD_STATUS[2];
                break;
            case MLSRecord.TYPE_EXPIRED: 
                status = STANDERD_STATUS[3];
                break;
            default: 
                status = "";
                break;
        
        }
        return status;
    }

    // STANDERD_STATUS[0];
    protected public void processSearchFields() throws Exception {
        //String dateFormat = m_engine.getDateFormat();
        m_searchFields = Hashtable.Synchronized(new Hashtable());
        //bool isSoldStatus = System.Convert.ToString("S").IndexOf(m_status) != - 1;
        boolean isSoldStatus = StringSupport.Compare("S", m_status, true) == 0;
        String field = "";
        String value_Renamed = "";
        String defValue = "";
        String iniValue = "";
        Hashtable ht = m_connector.getSearchFields();
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_STATUSDATE]))
            ht.put(STD_SEARCH_FIELD[ST_STATUSDATE], "");
         
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_LISTDATE]))
            ht.put(STD_SEARCH_FIELD[ST_LISTDATE], "");
         
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_SALEDATE]))
            ht.put(STD_SEARCH_FIELD[ST_SALEDATE], "");
         
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_SEARCHPRICE]))
            ht.put(STD_SEARCH_FIELD[ST_SEARCHPRICE], "");
         
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_LISTPRICE]))
            ht.put(STD_SEARCH_FIELD[ST_LISTPRICE], "");
         
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_SALEPRICE]))
            ht.put(STD_SEARCH_FIELD[ST_SALEPRICE], "");
         
        String statusDate = (String)ht.get(STD_SEARCH_FIELD[ST_STATUSDATE]);
        if (statusDate == null)
            statusDate = "";
         
        if (StringSupport.isNullOrEmpty(statusDate))
        {
            if (isOverrideLimitWithoutDate())
            {
                m_chunkingParameter = getChunkingParameterInDef();
                if (m_chunkingParameter == null || m_chunkingParameter.size() == 0)
                {
                    if (StringSupport.isNullOrEmpty((String)ht.get(STD_SEARCH_FIELD[ST_LISTDATE])) && StringSupport.isNullOrEmpty((String)ht.get(STD_SEARCH_FIELD[ST_SALEDATE])))
                    {
                        statusDate = Calendar.getInstance().getTime().AddYears(-10).ToUniversalTime().ToString("MM/dd/yyyy") + "-" + DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), "MM/dd/yyyy");
                        ht.put(STD_SEARCH_FIELD[ST_STATUSDATE], statusDate);
                        addXMLNote(TCSException.OVERRIDERECLIMIT_DATENOTSUPPLIED,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DATENOTSUPPLIED));
                    }
                     
                }
                 
            }
             
        }
         
        String searchPrice = (String)ht.get(STD_SEARCH_FIELD[ST_SEARCHPRICE]);
        if (searchPrice == null)
            searchPrice = "";
         
        boolean isSupportSearchPrice = m_engine.isSupportSearchPrice() && searchPrice.length() > 0;
        boolean isSupportStatusDate = m_engine.isSupportStatusDate() && statusDate.length() > 0;
        String postalCode = (String)ht.get(STD_SEARCH_FIELD[ST_ZIP]);
        IEnumerator names;
        names = EnumeratorSupport.mk(ht.keySet().iterator());
        StringBuilder sb = new StringBuilder();
        while (names.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            field = ((String)names.getCurrent());
            value_Renamed = ((String)ht.get(field));
            iniValue = value_Renamed;
            defValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,field);
            //if( value.length() == 0 && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_LISTDATE]) && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_SALEDATE]) && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_LISTPRICE]) && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_SALEPRICE]))
            //	continue;
            if (!field.startsWith("ST_"))
                continue;
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUSDATE].toUpperCase()))
            {
            }
             
            //if (isSupportStatusDate)
            //	value_Renamed = getDateFormat(STD_SEARCH_FIELD[ST_STATUSDATE], value_Renamed);
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTDATE].toUpperCase()))
            {
                if (isSupportStatusDate)
                    value_Renamed = "";
                else if (statusDate.length() > 0)
                    if (!isSoldStatus)
                        value_Renamed = statusDate;
                    else
                        value_Renamed = ""; 
                  
            }
             
            //if (defValue.Length > 0 && value_Renamed.Length > 0)
            //	value_Renamed = getDateFormat(STD_SEARCH_FIELD[ST_LISTDATE], value_Renamed);
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SALEDATE].toUpperCase()))
            {
                if (!isSoldStatus)
                    value_Renamed = "";
                 
                if (isSupportStatusDate)
                    value_Renamed = "";
                else if (statusDate.length() > 0)
                    if (isSoldStatus)
                        value_Renamed = statusDate;
                     
                  
            }
             
            //if (defValue.Length > 0 && value_Renamed.Length > 0)
            //	value_Renamed = getDateFormat(STD_SEARCH_FIELD[ST_SALEDATE], value_Renamed);
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SEARCHPRICE].toUpperCase()))
            {
                //Nothing need to do here
                if (!isSupportSearchPrice)
                    value_Renamed = "";
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTPRICE].toUpperCase()))
            {
                if (isSupportSearchPrice)
                    value_Renamed = "";
                else if (searchPrice.length() > 0)
                {
                    if (!isSoldStatus)
                        value_Renamed = searchPrice;
                    else
                        value_Renamed = ""; 
                }
                  
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SALEPRICE].toUpperCase()))
            {
                if (isSupportSearchPrice)
                    value_Renamed = "";
                else if (searchPrice.length() > 0)
                {
                    if (isSoldStatus)
                        value_Renamed = searchPrice;
                    else
                        value_Renamed = ""; 
                }
                else if (!isSoldStatus)
                    value_Renamed = "";
                   
            }
             
            if (value_Renamed.length() == 0)
                continue;
             
            net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(defValue);
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_MLSNUMBER].toUpperCase()))
            {
                if (pf != null)
                {
                    value_Renamed = removeSpace(m_mlsNumber,pf.getDelimiter());
                    m_mlsNumber = value_Renamed;
                }
                 
            }
             
            if (pf != null && !field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUS].toUpperCase()))
            {
                if (pf.getFormat().length() > 0)
                {
                    if (pf.getControlType() == 3 && value_Renamed.indexOf(",") > -1 && !value_Renamed.endsWith(","))
                        addXMLNote(TCSException.NOT_ALLOW_MULTIPLE_VALUES,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_ALLOW_MULTIPLE_VALUES) + field);
                     
                    //value_Renamed = pf.getLookupCode(value_Renamed);
                    m_connector.writeLine("Look Up Code = " + value_Renamed);
                }
                else
                {
                    if (!field.toUpperCase().equals("ST_PType".toUpperCase()) && (pf.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST || pf.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST_TYPEABLE))
                    {
                        value_Renamed = "";
                        addXMLNote(TCSException.LOOKUP_CODE_FORMAT_NOT_EXIST,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.LOOKUP_CODE_FORMAT_NOT_EXIST) + field);
                    }
                     
                } 
            }
             
            /*if( field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_BEDS]) )
                            {
            				
                            }
            				
                            if( field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_FULLBATHS]) )
                            {
            				
                            }
            				
                            if ( field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_SQFT]) )
                            {
            				
                            }
                            */
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PROPERTYTYPE].toUpperCase()))
            {
                value_Renamed = "";
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PostalFSA].toUpperCase()))
            {
                if (!StringSupport.isNullOrEmpty(postalCode))
                    value_Renamed = "";
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LAT].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LONG].toUpperCase()))
            {
                value_Renamed = "";
            }
             
            if (value_Renamed.length() > 0 && defValue.length() == 0)
            {
                if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PostalFSA].toUpperCase()))
                {
                    throw new TCSException(null,TCSException.getTCSMessage(TCSException.NOT_SURPPORT_POSTALFSA_FIELD),"",0,TCSException.NOT_SURPPORT_POSTALFSA_FIELD);
                }
                else
                {
                    //addXMLNote(TCSException.NOT_SURPPORT_POSTALFSA_FIELD, TCSException.getTCSMessage(TCSException.NOT_SURPPORT_POSTALFSA_FIELD));
                    //m_warningNoteId = m_noteId - 1;
                    //m_isAddWarningNote = true;
                    sb.append(field).append(", ");
                } 
            }
             
            //if (defValue.Length > 0 && value_Renamed.Length > 0 && field.ToUpper().EndsWith("DATE"))
            //    value_Renamed = getDateFormat(pf, value_Renamed);
            m_searchFields.put(field, value_Renamed);
        }
        if (sb.length() > 0)
        {
            sb.insert(0, Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_SEARCH_FIELD));
            addXMLNote(TCSException.NOT_SURPPORT_SEARCH_FIELD,sb.toString().substring(0, (0) + ((sb.toString().lastIndexOf(",")) - (0))));
            m_warningNoteId = m_noteId - 1;
            m_isAddWarningNote = true;
            String clientName = m_connector.getClientName();
            if (!StringSupport.isNullOrEmpty(clientName) && StringSupport.Compare(clientName, "TMKMarketData", true) == 0)
            {
                String sbString = sb.toString();
                if (sbString.indexOf("ST_PendingDate") > -1 || sbString.indexOf("ST_ExpiredDate") > -1)
                {
                    throw TCSException.produceTCSException(70001);
                }
                 
            }
             
        }
        else
            m_isAddWarningNote = false; 
        setRecordLimit();
    }

    public void setRecordLimit() throws Exception {
        int recordLimit = m_engine.getMaxRecordsLimit();
        Hashtable searchFields = m_connector.getSearchFields();
        String lowRecordLimit = "";
        if (searchFields.containsKey(MLSConnector.ATTRIBUTE_RECORD_LIMIT))
            lowRecordLimit = searchFields.get(MLSConnector.ATTRIBUTE_RECORD_LIMIT).toString();
         
        if (!StringSupport.isNullOrEmpty(lowRecordLimit))
        {
            recordLimit = Integer.valueOf(lowRecordLimit);
            if (recordLimit > m_engine.getMaxRecordsLimit())
            {
                recordLimit = m_engine.getMaxRecordsLimit();
                addXMLNote(TCSException.RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.RECORDLIMIT_ISGREATERTHAN_MAXRECORDLIMIT));
            }
             
        }
         
        String overrideRecordLimit = "";
        if (searchFields.containsKey(MLSConnector.ATTRIBUTE_OVERRIDE_RECORD_LIMIT))
            overrideRecordLimit = searchFields.get(MLSConnector.ATTRIBUTE_OVERRIDE_RECORD_LIMIT).toString();
         
        if (!StringSupport.isNullOrEmpty(overrideRecordLimit))
        {
            recordLimit = Integer.valueOf(overrideRecordLimit);
            if (recordLimit < m_engine.getMaxRecordsLimit())
            {
                addXMLNote(TCSException.OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_ISLOWERTHAN_MAXRECLIMIT));
                recordLimit = m_engine.getMaxRecordsLimit();
            }
             
            if (!m_engine.isOverrideAllowed())
            {
                setOverrideLimit();
                if (!getIsOverrideLimit() && (this instanceof GetComparableListings || this instanceof GetMLSPlusStandardListings))
                    if (StringSupport.isNullOrEmpty(getSearchDate()[1]))
                    {
                        m_chunkingParameter = getChunkingParameterInDef();
                        if (m_chunkingParameter == null || m_chunkingParameter.size() == 0)
                            addXMLNote(TCSException.OVERRIDERECLIMIT_DATENOTSUPPLIED,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DATENOTSUPPLIED));
                         
                    }
                     
                 
                if (!(this instanceof GetComparableListings || this instanceof GetMLSPlusStandardListings || this instanceof GetDataAggListings || this instanceof GetAgentRoster))
                    addXMLNote(TCSException.OVERRIDERECLIMIT_ISNOTALLOWED,Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_ISNOTALLOWED));
                 
                recordLimit = m_engine.getMaxRecordsLimit();
            }
             
        }
         
        if (!StringSupport.isNullOrEmpty(lowRecordLimit) && !StringSupport.isNullOrEmpty(overrideRecordLimit))
            throw TCSException.produceTCSException(TCSException.BOTH_RECORDLIMIT_OVERRIDERECORDLIMIT_USED);
         
        m_engine.setRecordsLimit(recordLimit);
    }

    public int getOverrideRecordLimit() throws Exception {
        if (m_overrideLimit > -1)
            return m_overrideLimit;
         
        Hashtable searchFields = m_connector.getSearchFields();
        String overrideRecordLimit = "";
        if (searchFields.containsKey(MLSConnector.ATTRIBUTE_OVERRIDE_RECORD_LIMIT))
            overrideRecordLimit = searchFields.get(MLSConnector.ATTRIBUTE_OVERRIDE_RECORD_LIMIT).toString();
         
        if (!StringSupport.isNullOrEmpty(overrideRecordLimit))
        {
            m_overrideLimit = Integer.valueOf(overrideRecordLimit);
            return m_overrideLimit;
        }
        else
            return 0; 
    }

    public boolean getIsOverrideLimit() throws Exception {
        return m_isOverrideLimit;
    }

    public void setIsOverrideLimit(boolean value) throws Exception {
        m_isOverrideLimit = value;
    }

    protected void setOverrideLimit() throws Exception {
        if (getOverrideRecordLimit() > m_engine.getMaxRecordsLimit() && !m_engine.isOverrideAllowed() && (this instanceof GetComparableListings || this instanceof GetMLSPlusStandardListings || this instanceof GetDataAggListings || this instanceof GetAgentRoster))
        {
            m_chunkingParameter = getChunkingParameterInDef();
            if (m_chunkingParameter == null || m_chunkingParameter.size() == 0)
            {
                if (!StringSupport.isNullOrEmpty(getSearchDate()[1]))
                    setIsOverrideLimit(true);
                 
            }
            else
                setIsOverrideLimit(true); 
        }
         
    }

    protected boolean isOverrideLimitWithoutDate() throws Exception {
        if (getOverrideRecordLimit() > m_engine.getMaxRecordsLimit() && !m_engine.isOverrideAllowed())
            return true;
         
        return false;
    }

    //protected virtual string getDateFormat(PropertyField pf, string value_Renamed)
    //{
    //    string date = "";
    //    try
    //    {
    //        string dateFormat = "";
    //        string dateDelimeter = "";
    //        if (pf != null)
    //        {
    //            dateFormat = pf.getDateFormat();
    //            dateDelimeter = pf.getDelimiter();
    //        }
    //        if (dateFormat == null || dateFormat.Length == 0)
    //        {
    //            dateFormat = STANDARD_DATEFORMAT;
    //            m_connector.WriteLine("Can not get date format for field - " + pf.getName());
    //        }
    //        //}
    //        if (dateDelimeter == null || dateDelimeter.Length == 0)
    //            dateDelimeter = "-";
    //        string[] dates = Util.stringSplit(value_Renamed, "-");
    //        for (int i = 0; i < dates.Length; i++)
    //        {
    //            if (i == 0)
    //                date = MLSUtil.FormatDate(STANDARD_DATEFORMAT, dateFormat, dates[i]);
    //            else
    //                date = date + dateDelimeter + MLSUtil.FormatDate(STANDARD_DATEFORMAT, dateFormat, dates[i]);
    //        }
    //        if (date == null)
    //            return value_Renamed;
    //    }
    //    catch (Exception exc)
    //    { }
    //    return date;
    //}
    protected public void validateOutputResult() throws Exception {
    }

    protected public void getResult() throws Exception {
    }

    protected public void saveResult() throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        if (m_useStreamWriter)
        {
            m_resultStream.print(m_resultBuffer.toString());
            m_resultStream.close();
            m_resultStream = null;
            m_connector.saveTaskResult("",getResultPath().get(0).toString());
        }
        else
        {
            //m_resultBuffer.Insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
            //checkResultSize();
            m_connector.saveTaskResult("",m_resultBuffer.toString());
        } 
        if (MLSConnector.RUN_LOCAL > 0)
            File.WriteAllText("c:\\dataagg.xml", m_resultBuffer.toString());
         
        m_connector.writeLine("Save Task Result = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
    }

    //protected internal virtual void checkResultSize()
    //{
    //if( m_resultBuffer.length() > MAXIMUM_RESUTL_SIZE )
    //	throw TCSException.produceTCSException( TCSException.RESULT_EXCEED_LIMITED_SIZE );
    //}
    protected public void setLoginInfo() throws Exception {
        setLoginInfo(m_engine);
    }

    protected public void setLoginInfo(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        try
        {
            BoardSetup setup = engine.getBoardSetup();
            net.toppro.components.mls.engine.BoardSetup.SecFieldIterator iterator = setup.getSecFieldIterator();
            BoardSetupField field = iterator.first();
            XmlNode loginInfo = m_connector.getLoginInfo();
            if (loginInfo == null || loginInfo.getChildNodes().size() == 0)
                return ;
             
            ArrayList passwords = ArrayList.Synchronized(new ArrayList(10));
            for (int i = 0;i < loginInfo.getChildNodes().size();i++)
            {
                XmlNode node = loginInfo.getChildNodes().get(i);
                int password_num = 0;
                if ("ip".equals(node.getName()))
                    setup.setIPAddress(node.getInnerText());
                else if ("port".equals(node.getName()))
                    setup.setIPPort(node.getInnerText());
                else if ("password".equals(node.getName()))
                    passwords.add(node.getInnerText());
                   
            }
            int i2 = 0;
            while (field != null)
            {
                if (field.getInputType() != BoardSetupField.INPUT_TYPE_FINAL)
                {
                    //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Object.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                    field.setValue(passwords.get(i2++).toString());
                }
                 
                field = iterator.next();
            }
            if (passwords.size() > i2 && passwords.get(i2).toString().length() > 0)
            {
                Hashtable ht = new Hashtable();
                ht.put("1", passwords.get(i2));
                engine.setAdditionalParams(ht);
            }
             
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
                ;
        }
    
    }

    protected public void setSearchPropertyField(String status) throws Exception {
        try
        {
            m_searchFields.put(STD_SEARCH_FIELD[ST_STATUS], status);
            m_searchFields.put("ST_PType", m_connector.getPropertySubType());
            m_engine.setPropertyFields(m_searchFields);
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
                ;
        }
    
    }

    public String getSTStatus(String status) throws Exception {
        if (getFormatFlag() == MATCH_NONE)
            return "";
         
        String statusField = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,STD_SEARCH_FIELD[ST_STATUS]);
        net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFieldGroup(1).getField(statusField);
        if (pf == null)
            return "";
         
        String StatusDelimiter = getStatusDelimiter();
        try
        {
            String[] uiValue = Tcs.Mls.Util.stringSplit(getStatusFromSorting(status),StatusDelimiter);
            String result = "";
            String initValue = "";
            for (int i = 0;i < pf.getInitValueCount();i++)
            {
                initValue = pf.getInitValue(i);
                for (int j = 0;j < uiValue.length;j++)
                {
                    if (compareString(initValue,uiValue[j]))
                        result = result + initValue + StatusDelimiter;
                     
                }
            }
            if (result.endsWith(StatusDelimiter))
                result = result.substring(0, (0) + ((result.length() - 1) - (0)));
             
            return result;
        }
        catch (Exception exc)
        {
            if (exc instanceof TCSException)
            {
                throw exc;
            }
             
            return "";
        }
    
    }

    protected String getStatusFromSorting(String status) throws Exception {
        String delimiter = getStatusDelimiter();
        String sortStatus = "";
        if (status.indexOf("A") > -1)
            sortStatus = sortStatus + m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[0]) + delimiter;
         
        if (status.indexOf("S") > -1)
            sortStatus = sortStatus + m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[1]) + delimiter;
         
        if (status.indexOf("E") > -1)
            sortStatus = sortStatus + m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[3]) + delimiter;
         
        if (status.indexOf("P") > -1)
            sortStatus = sortStatus + m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[2]) + delimiter;
         
        return sortStatus;
    }

    protected String getStatusDelimiter() throws Exception {
        String StatusDelimiter = m_engine.getDefParser().getMappedValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,STD_SEARCH_FIELD[ST_STATUS],net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DELIMITER,net.toppro.components.mls.engine.MLSEngine.SECTION_FIELD_);
        // gets status delimiter value
        if (StatusDelimiter == null || StatusDelimiter.length() == 0)
            StatusDelimiter = ",";
         
        return StatusDelimiter;
    }

    private boolean compareString(String s1, String s2) throws Exception {
        try
        {
            s1 = StringSupport.Trim(s1);
            //removeSpace( s1 );
            s2 = StringSupport.Trim(s2);
            //removeSpace( s2 );
            boolean result = false;
            String delimiter = getFormatDelimiter();
            int pos = s1.indexOf(delimiter);
            switch(getFormatFlag())
            {
                case MATCH_ALL: 
                    result = s1.toUpperCase().equals(s2.toUpperCase());
                    break;
                case MATCH_FIRST: 
                    s1 = s1.Substring(0, (pos)-(0));
                    result = s1.toUpperCase().equals(s2.toUpperCase()) ? true : false;
                    break;
                case MATCH_SECOND: 
                    s1 = s1.substring(pos + delimiter.length());
                    result = s1.toUpperCase().equals(s2.toUpperCase()) ? true : false;
                    break;
                case MATCH_NONE: 
                    break;
            
            }
            return result;
        }
        catch (Exception e)
        {
            return false;
        }
    
    }

    private int getFormatFlag() throws Exception {
        String status = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,STD_SEARCH_FIELD[ST_STATUS]);
        String statusFormat = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELD_ + status,ATTRIBUTE_FORMAT);
        String sortingFormat = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING,ATTRIBUTE_FORMAT);
        statusFormat = Tcs.Mls.Util.removeSpace(statusFormat);
        sortingFormat = Tcs.Mls.Util.removeSpace(sortingFormat);
        int pos = 0;
        pos = statusFormat.toUpperCase().indexOf(sortingFormat.toUpperCase());
        if (statusFormat.length() == 0 || sortingFormat.length() == 0 || pos == -1)
            return MATCH_NONE;
         
        if (statusFormat.toUpperCase().equals(sortingFormat.toUpperCase()))
            return MATCH_ALL;
         
        if (pos == 0)
            return MATCH_FIRST;
        else
            return MATCH_SECOND; 
    }

    private String getFormatDelimiter() throws Exception {
        String status = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,STD_SEARCH_FIELD[ST_STATUS]);
        String statusFormat = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELD_ + status,ATTRIBUTE_FORMAT);
        String delimiter = "";
        if (statusFormat.length() > 2)
            delimiter = statusFormat.substring(1, (1) + ((statusFormat.length() - 1) - (1)));
         
        return delimiter;
    }

    protected void checkResultSize() throws Exception {
        setResultListingCount(getResultListingCount() + 1);
        if (getResultListingCount() > MaxResultListingNumber)
        {
            if (!(this instanceof GetDataAggListings || this instanceof GetAgentRoster))
                throw TCSException.produceTCSException(TCSException.EXCEED_MAX_LISITNG_NUMBER);
            else
            {
                if (m_connector.isReturnDataAggListingsXml())
                    throw TCSException.produceTCSException(TCSException.EXCEED_MAX_LISITNG_NUMBER);
                 
            } 
        }
         
        if (m_resultBuffer.length() > 3600000)
        {
            if (m_resultStream == null)
            {
                m_useStreamWriter = true;
                if (!((this instanceof GetDataAggListings || this instanceof GetAgentRoster)))
                    getResultPath().add(m_connector.getSearchTempFolder() + "\\tcslistings.tcs");
                else
                    getResultPath().add(m_connector.getSearchTempFolder() + (m_connector.isReturnDataAggListingsXml() ? "\\tcslistings.tcs" : "\\tcslistings.xml")); 
                m_resultStream = new PrintWriter(new FileWriter(getResultPath().get(0).toString()), true);
            }
             
            m_resultStream.print(m_resultBuffer.toString());
            setResultListingSize(getResultListingSize() + m_resultBuffer.length());
            m_resultBuffer = new StringBuilder();
            if ((this instanceof GetDataAggListings || this instanceof GetAgentRoster) && !m_connector.isReturnDataAggListingsXml() && getResultListingSize() > 10000000)
            {
                String tag = "Listings";
                if (this instanceof GetOfficeRoster)
                    tag = "OfficeRoster";
                else if (this instanceof GetOpenHouse)
                    tag = "OpenHouses";
                else if (this instanceof GetAgentRoster)
                    tag = "AgentRoster";
                   
                setResultListingSize(0);
                getResultPath().add(m_connector.getSearchTempFolder() + "\\tcslistings" + getResultPath().size() + ".xml");
                m_resultBuffer.append("\r\n</" + tag + ">\r\n</TCResult>");
                m_resultStream.print(m_resultBuffer.toString());
                m_resultStream.close();
                m_resultStream = new PrintWriter(new FileWriter(getResultPath().get(getResultPath().size() - 1).toString()), true);
                m_resultBuffer = new StringBuilder();
                m_resultBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult>\r\n<" + tag + ">\r\n");
            }
             
        }
         
    }

    protected public void setLogMode() throws Exception {
        setLogMode(m_engine);
    }

    protected public void setLogMode(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        try
        {
            if (m_connector.isLogOnMode())
            {
                engine.setLogMode(net.toppro.components.mls.engine.MLSEngine.LOG_APP_LOG);
                engine.setLogMode(net.toppro.components.mls.engine.MLSEngine.LOG_COMM_LOG);
            }
            else
                engine.setLogMode(net.toppro.components.mls.engine.MLSEngine.LOG_APP_LOG); 
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }
    
    }

    protected public void setStatus(String status) throws Exception {
        m_status = status;
        //m_connector.setSearchStatus( status );
        if (status.length() > 0)
            m_engine.setCurrentSearchStatus(status);
         
    }

    public String getCurrentStandardPublicStatus() throws Exception {
        return StringSupport.isNullOrEmpty(m_status) ? "" : m_status;
    }

    public String validateSearchParams(String field, String value_Renamed) throws Exception {
        if (value_Renamed == null || value_Renamed.length() == 0)
            return "";
         
        if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUS].toUpperCase()))
        {
            String[] values = Tcs.Mls.Util.stringSplit(value_Renamed,",");
            boolean bFound = false;
            for (int i = 0;i < values.length;i++)
            {
                for (int j = 0;j < STANDERD_STATUS.length;j++)
                    if (STANDERD_STATUS[j].equals(values[i]))
                    {
                        bFound = true;
                        break;
                    }
                     
                if (!bFound)
                    return null;
                 
            }
            return value_Renamed;
        }
         
        if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PROPERTYTYPE].toUpperCase()))
        {
            if (STANDERD_PROPERTY_TYPE.indexOf(value_Renamed + ",") != -1)
                return value_Renamed;
            else
                return null; 
        }
         
        if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_ZIP].toUpperCase()))
        {
            if (value_Renamed.length() == 5 || value_Renamed.length() == 7)
            {
                char[] values = value_Renamed.toCharArray();
                for (int i = 0;i < values.length;i++)
                {
                    if (i == 3 && field.length() == 7 && values[i] == ' ')
                        continue;
                    else if (Character.isLetterOrDigit(values[i]) == false)
                        return null;
                      
                }
            }
            else
                return null; 
            return value_Renamed;
        }
         
        if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_BEDS].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_FULLBATHS].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_TOTALBATHS].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SQFT].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTPRICE].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SALEPRICE].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SEARCHPRICE].toUpperCase()))
        {
            String _value = value_Renamed;
            _value = MLSUtil.removeSpace(_value);
            _value = MLSUtil.replaceSubStr(_value,"-","");
            if (MLSUtil.isNumber(_value))
                return value_Renamed;
            else
                return null; 
        }
         
        if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTDATE].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SALEDATE].toUpperCase()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUSDATE].toUpperCase()))
        {
            String[] values = Tcs.Mls.Util.stringSplit(value_Renamed,"-");
            for (int i = 0;i < values.length;i++)
            {
                if (!(values[i].length() == 10 && values[i].indexOf("/") == 2 && values[i].lastIndexOf("/") == 5 && MLSUtil.isDate(values[i])))
                    return null;
                 
            }
            return value_Renamed;
        }
         
        return value_Renamed;
    }

    protected void removeMLSNumber(String recordID) throws Exception {
        if (m_mlsNumber.length() > 0)
        {
            m_mlsNumber = Tcs.Mls.Util.replaceString(m_mlsNumber,recordID,"");
            if (StringSupport.Trim(m_mlsNumber).startsWith(","))
                m_mlsNumber = m_mlsNumber.Substring(1, (m_mlsNumber.Length)-(1));
             
            if (StringSupport.Trim(m_mlsNumber).endsWith(","))
                m_mlsNumber = m_mlsNumber.substring(0, (0) + ((m_mlsNumber.length() - 1) - (0)));
             
            if (m_mlsNumber.indexOf(",,") > -1)
                m_mlsNumber = Tcs.Mls.Util.replaceString(m_mlsNumber,",,",",");
             
        }
         
        m_mlsNumber = StringSupport.Trim(m_mlsNumber);
    }

    protected String removeSpace(String value, String delimiter) throws Exception {
        String result = "";
        if (StringSupport.isNullOrEmpty(value))
            return "";
         
        if (StringSupport.isNullOrEmpty(delimiter))
            delimiter = ",";
         
        String[] list = value.split(StringSupport.charAltsToRegex(delimiter.toCharArray()));
        for (int i = 0;i < list.length;i++)
        {
            result += StringSupport.Trim(list[i]) + delimiter;
        }
        if (result.endsWith(delimiter))
            result = result.substring(0, (0) + (result.length() - 1));
         
        return result;
    }

    protected boolean isDEFSupportedSearchField(String field) throws Exception {
        String defValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,field);
        net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(defValue);
        return pf == null ? false : true;
    }

    private void loadLTSMapping() throws Exception {
        if (m_ltsTypeMapping != null)
            return ;
        else
        {
            String delimiter = "";
            boolean useFirstPart = true;
            String stPtype = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,"ST_PType");
            if (!StringSupport.isNullOrEmpty(stPtype))
            {
                String format = m_engine.getDefParser().getValue("field_" + stPtype,"format");
                if (!StringSupport.isNullOrEmpty(format))
                {
                    if (format.indexOf(':') > -1)
                        format = format.substring(0, (0) + (format.indexOf(':')));
                     
                    format = StringSupport.Trim(format).toUpperCase();
                    if (format.startsWith("S"))
                        useFirstPart = false;
                     
                    delimiter = format.replace("S", "").replace("L", "");
                }
                 
            }
             
            boolean useLTSResult = false;
            String[] valueList = m_engine.getDefParser().getAttributiesFor(net.toppro.components.mls.engine.MLSEngine.SECTION_LTS_TYPE_RESULT);
            if (valueList == null || valueList.length == 0)
                valueList = m_engine.getDefParser().getAttributiesFor(net.toppro.components.mls.engine.MLSEngine.SECTION_LTS_TYPE);
            else
            {
                useLTSResult = true;
                useFirstPart = true;
                delimiter = "";
            } 
            m_ltsTypeMapping = new HashMap<String,String>();
            if (valueList == null)
                return ;
             
            for (int i = 0;i < valueList.length;i++)
            {
                String value = "";
                if (useLTSResult)
                {
                    value = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_LTS_TYPE_RESULT,valueList[i]);
                }
                else
                {
                    value = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_LTS_TYPE,valueList[i]);
                } 
                try
                {
                    if (value.startsWith(":"))
                    {
                        m_ltsTypeMapping.put("@", replaceToLongStandardPropertyType(StringSupport.Trim(value.replace(":", "")).toUpperCase()));
                        continue;
                    }
                     
                    String[] s = StringSupport.Split(value, ':');
                    s[1] = replaceToLongStandardPropertyType(s[1]);
                    if (!StringSupport.isNullOrEmpty(delimiter))
                    {
                        if (s[0].indexOf(delimiter) < 0)
                        {
                            useFirstPart = true;
                            delimiter = "";
                        }
                         
                        String[] sp = StringSupport.Split(s[0], new String[]{ delimiter }, StringSplitOptions.None);
                        if (useFirstPart)
                            m_ltsTypeMapping.put(StringSupport.Trim(sp[0]).toUpperCase(), s[1]);
                        else
                            m_ltsTypeMapping.put(StringSupport.Trim(sp[1]).toUpperCase(), s[1]); 
                    }
                    else
                    {
                        m_ltsTypeMapping.put(StringSupport.Trim(s[0]).toUpperCase(), s[1]);
                    } 
                }
                catch (Exception exc)
                {
                    m_connector.writeLine("Key exists." + exc.getMessage());
                }
            
            }
        } 
    }

    private String replaceToLongStandardPropertyType(String s) throws Exception {
        if (StringSupport.isNullOrEmpty(s))
            return "";
         
        String result = "";
        String[] values = StringSupport.Split(s, ',');
        for (int i = 0;i < values.length;i++)
        {
            if (m_propertyTypeHashTable.containsKey(values[i].toUpperCase()))
                result = result + (result.length() == 0 ? "" : ",") + m_propertyTypeHashTable.get(values[i].toUpperCase());
             
        }
        return result;
    }

    protected String getStandardPropertyType(String mlsPropertyType) throws Exception {
        String result = "";
        if (StringSupport.isNullOrEmpty(mlsPropertyType))
            mlsPropertyType = "";
         
        loadLTSMapping();
        if (m_ltsTypeMapping == null || m_ltsTypeMapping.size() == 0)
            return "";
         
        String[] value = StringSupport.Split(mlsPropertyType, ',');
        if (m_ltsTypeMapping.containsKey("@"))
            result = m_ltsTypeMapping.get("@");
         
        if (m_ltsTypeMapping.containsKey(value[0].toUpperCase()))
            result = result + (StringSupport.isNullOrEmpty(result) ? "" : ",") + m_ltsTypeMapping.get(value[0].toUpperCase());
         
        return result;
    }

    protected public String getCompatibilityID() throws Exception {
        return "";
    }

}


