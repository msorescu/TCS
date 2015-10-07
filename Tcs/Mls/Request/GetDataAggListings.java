//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:53 PM
//

package Tcs.Mls.Request;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.JavaSupport.language.RefSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DateTZ;
import CS2JNet.System.DoubleSupport;
import CS2JNet.System.LCC.Disposable;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import CS2JNet.System.Xml.XmlDocument;
import CS2JNet.System.Xml.XmlNode;
import CS2JNet.System.Xml.XmlNodeList;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.TimeZone;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.GetAgentRoster;
import Tcs.Mls.Request.GetIDXListings;
import Tcs.Mls.Request.GetOfficeRoster;
import Tcs.Mls.Request.GetRecordCountAgentRoster;
import Tcs.Mls.Request.GetRecordCountDataagg;
import Tcs.Mls.Request.GetRecordCountOpenHouse;
import Tcs.Mls.Request.STRINGS;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TimeZoneInfoEx;
import Tcs.Mls.TPAppRequest;

public class GetDataAggListings  extends TPAppRequest 
{
    protected String m_noteFields = "";
    protected static final String COMPATIBILITY_ID = "3";
    public String[] STANDERD_STATUS = new String[]{ "A", "S", "O" };
    private static String[] STANDARD_PUBLIC_STATUS_RESULT = new String[]{ "Active", "Sold", "OffMarketOrOther" };
    public static final String[] REQUIRED_FIELD = new String[]{ STD_SEARCH_FIELD[ST_STATUS] };
    protected boolean isReturnListingsInXmls = false;
    private int m_searchByPublicStatus = -1;
    protected String m_metadataVersion = "";
    protected boolean m_includeCDATA = true;
    private boolean m_includeTlBlob = false;
    protected boolean m_useStatusDate = false;
    protected boolean m_lastModifiedMapped = true;
    protected boolean m_picLastModifiedMapped = true;
    //private TimeZoneInfo m_timeZoneInfo;
    protected TimeZoneInfoEx m_mlsSearchTimeZoneInfo;
    protected TimeZoneInfoEx m_mlsResultsTimeZoneInfo;
    protected boolean m_isSearchByLastModified = false;
    private ArrayList m_metadataFiles = new ArrayList();
    private HashSet<String> m_blockedFields = null;
    public ArrayList getMetadataFiles() throws Exception {
        return m_metadataFiles;
    }

    public void setMetadataFiles(ArrayList value) throws Exception {
        m_metadataFiles = value;
    }

    HashMap<String,Integer> m_columnDict = new HashMap<String,Integer>();
    public HashMap<String,Integer> getColumnDict() throws Exception {
        return m_columnDict;
    }

    public void setColumnDict(HashMap<String,Integer> value) throws Exception {
        m_columnDict = value;
    }

    protected HashMap<String,Integer> m_xMARTCount = new HashMap<String,Integer>();
    protected HashMap<String,Integer> getXMARTCount() throws Exception {
        return m_xMARTCount;
    }

    protected void setXMARTCount(HashMap<String,Integer> value) throws Exception {
        m_xMARTCount = value;
    }

    protected HashMap<String,Integer> TCSCount = new HashMap<String,Integer>();
    protected HashMap<String,HashSet<String>> XMARTMLSNumbers = new HashMap<String,HashSet<String>>();
    protected HashMap<String,HashSet<String>> TCSMLSNumbers = new HashMap<String,HashSet<String>>();
    private boolean _useCompactFormat = false;
    public static boolean IsDataAggListing = false;
    /**
    * @param tcs
    */
    //private string m_timeZone = "";
    public GetDataAggListings(TCServer tcs) throws Exception {
        super(tcs);
        if (m_connector.IsDACount())
            m_xMARTCount = m_connector.GetXMARTCount();
         
        if (m_connector.IsUsingCompactFormat())
            _useCompactFormat = true;
         
        TCSCount.put("Residential", 0);
        TCSCount.put("Condo", 0);
        TCSCount.put("Mobile", 0);
        TCSCount.put("MultiFamily", 0);
        TCSCount.put("Farm", 0);
        TCSCount.put("Land", 0);
        TCSCount.put("Rental", 0);
        TCSMLSNumbers.Add("Residential", new HashSet<String>());
        TCSMLSNumbers.Add("Condo", new HashSet<String>());
        TCSMLSNumbers.Add("Mobile", new HashSet<String>());
        TCSMLSNumbers.Add("MultiFamily", new HashSet<String>());
        TCSMLSNumbers.Add("Farm", new HashSet<String>());
        TCSMLSNumbers.Add("Land", new HashSet<String>());
        TCSMLSNumbers.Add("Rental", new HashSet<String>());
        IsDataAggListing = true;
        m_includeTlBlob = m_connector.IsIncludingTlBlobField();
    }

    protected public void getResult() throws Exception {
        checkMetadataVersion();
        getListings();
    }

    protected public void checkMetadataVersion() throws Exception {
        if (!(m_connector.getMetadataVersion().equals("0") || this instanceof GetIDXListings))
        {
            if (!m_connector.getDefFileVersion().equals("0") && String.Compare(m_connector.GetSystemPlusMetadataVersion(), m_connector.getMetadataVersion()) != 0 && !m_tcs.IsRealTime)
            {
                try
                {
                    String systemVersion = m_connector.getMetadataVersion();
                    if (!StringSupport.isNullOrEmpty(systemVersion))
                    {
                        if (systemVersion.length() < 8)
                        {
                            m_tcs.RefreshMetadata = true;
                        }
                        else
                        {
                            systemVersion = systemVersion.substring(0, (0) + (4));
                            String version = "1" + StringSupport.PadLeft(net.toppro.components.mls.engine.MLSEngine.MLS_ENGINE_VERSION, 3, '0');
                            if (StringSupport.Compare(systemVersion, version, false) != 0)
                            {
                                m_tcs.RefreshMetadata = true;
                            }
                             
                        } 
                    }
                     
                }
                catch (Exception __dummyCatchVar0)
                {
                }

                throw TCSException.produceTCSException(TCSException.METADATA_VERSION_NOT_MATCH);
            }
             
        }
         
    }

    //addXMLNote(TCSException.METADATA_VERSION_NOT_MATCH, TCSException.getTCSMessage(TCSException.METADATA_VERSION_NOT_MATCH));
    protected public void checkSearchFields() throws Exception {
        Hashtable ht = m_connector.getSearchFields();
        String includeCDATA = "1";
        if (ht.containsKey("IncludeCDATA"))
            includeCDATA = (String)ht.get("IncludeCDATA");
         
        String conditionCode = "";
        if (ht.containsKey("ST_ConditionCode"))
            conditionCode = (String)ht.get("ST_ConditionCode");
         
        if (!StringSupport.isNullOrEmpty(conditionCode) && !String.IsNullOrEmpty(m_connector.GetRETSQueryParameter()))
            throw TCSException.produceTCSException(TCSException.BOTH_RETSQUERYPARAMETER_CONDITIONCODE_USED);
         
        if (!StringSupport.isNullOrEmpty(includeCDATA))
        {
            m_includeCDATA = includeCDATA.equals("1");
        }
         
        String selectPicFieldsOnly = "0";
        String returnIDsOnly = "0";
        if (ht.containsKey("SelectPicFieldsOnly"))
            selectPicFieldsOnly = (String)ht.get("SelectPicFieldsOnly");
         
        if (ht.containsKey("ReturnIDsOnly"))
            returnIDsOnly = (String)ht.get("ReturnIDsOnly");
         
        if (!StringSupport.isNullOrEmpty(selectPicFieldsOnly) && !StringSupport.isNullOrEmpty(selectPicFieldsOnly))
        {
            SelectPicFieldsOnly = selectPicFieldsOnly.equals("1") || returnIDsOnly.equals("1");
        }
         
        //m_timeZone = (string)ht["TimeZone"];
        //if (string.IsNullOrEmpty(m_timeZone))
        //{
        //    m_timeZone = "+00:00";
        //}
        //else if(string.Compare(m_timeZone, "z", true) == 0)
        //{
        //    m_timeZone = "+00:00";
        //}
        String lastMod = (String)ht.get(STD_SEARCH_FIELD[ST_LastModified]);
        String picMod = (String)ht.get(STD_SEARCH_FIELD[ST_PicModified]);
        String status = (String)ht.get(STD_SEARCH_FIELD[ST_STATUS]);
        String publicStatus = (String)ht.get(STD_SEARCH_FIELD[ST_PublicListingStatus]);
        if (!StringSupport.isNullOrEmpty(status) && !StringSupport.isNullOrEmpty(publicStatus) && !m_connector.getSearchByMLSNumber())
            throw TCSException.produceTCSException(TCSException.BOTH_STATUS_PUBLICSTATUS_USED);
         
        if (!StringSupport.isNullOrEmpty(publicStatus))
        {
            m_connector.setSearchFields(STD_SEARCH_FIELD[ST_STATUS], publicStatus);
            m_connector.setSearchFields(STD_SEARCH_FIELD[ST_PublicListingStatus], "");
            if (m_searchByPublicStatus == -1)
                m_searchByPublicStatus = 1;
             
        }
        else
        {
            STANDERD_STATUS = new String[]{ "A", "S", "P", "E" };
        } 
        super.checkSearchFields();
        if (!StringSupport.isNullOrEmpty(lastMod) && !StringSupport.isNullOrEmpty(picMod) && !m_connector.getSearchByMLSNumber())
            throw TCSException.produceTCSException(TCSException.BOTH_LASTMODIFIED_PICMODIFIED_USED);
         
    }

    protected public void processSearchFields() throws Exception {
        //String dateFormat = m_engine.getDateFormat();
        if (m_mlsSearchTimeZoneInfo == null)
            m_mlsSearchTimeZoneInfo = m_engine.GetMLSSearchTimeZone();
         
        if (m_mlsResultsTimeZoneInfo == null)
            m_mlsResultsTimeZoneInfo = m_engine.GetMLSResultsTimeZone();
         
        m_searchFields = Hashtable.Synchronized(new Hashtable());
        boolean isSoldStatus = String.Compare("S", m_status, true) == 0;
        String field = "";
        String value_Renamed = "";
        String iniValue = "";
        Hashtable ht = m_connector.getSearchFields();
        String statusDate = "";
        m_lastModifiedMapped = m_engine.isSupportLastModifiedDate();
        String defValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_LASTMODIFIDATE);
        //PropertyField pf = m_engine.getPropertyFields().getField(defValue);
        //string sDateFormat = "";
        // string delimiter = "";
        //if (pf != null)
        //{
        //    sDateFormat = pf.getDateFormat();
        //    delimiter = pf.getDelimiter();
        //}
        //if (string.IsNullOrEmpty(sDateFormat))
        //    sDateFormat = LastModifiedDateFormat;
        m_picLastModifiedMapped = m_engine.isSupportPicLastModifiedDate();
        String lastModifiedDate = (String)ht.get(STD_SEARCH_FIELD[ST_LastModified]);
        String picLastModifiedDate = (String)ht.get(STD_SEARCH_FIELD[ST_PicModified]);
        if (StringSupport.isNullOrEmpty(lastModifiedDate) && StringSupport.isNullOrEmpty(picLastModifiedDate))
        {
            if (IsOverrideLimitWithoutDate())
            {
                m_chunkingParameter = GetChunkingParameterInDef();
                if (m_chunkingParameter == null || m_chunkingParameter.Count == 0)
                {
                    lastModifiedDate = Calendar.getInstance().getTime().AddYears(-10).ToUniversalTime().ToString("MM/dd/yyyyTHH:mm:ss") + "-" + DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), "MM/dd/yyyyTHH:mm:ss");
                    ht.put(STD_SEARCH_FIELD[ST_LastModified], lastModifiedDate);
                    addXMLNote(TCSException.OVERRIDERECLIMIT_DATENOTSUPPLIED, Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DATENOTSUPPLIED));
                }
                 
            }
            else
            {
                if (!m_connector.getSearchByMLSNumber())
                    throw TCSException.produceTCSException(TCSException.BOTH_LASTMODIFIED_PICMODIFIED_EMPTY);
                 
            } 
        }
         
        if (!m_lastModifiedMapped && !StringSupport.isNullOrEmpty(lastModifiedDate))
        {
            m_isSearchByLastModified = true;
            statusDate = lastModifiedDate;
        }
        else if (!m_picLastModifiedMapped && !StringSupport.isNullOrEmpty(picLastModifiedDate))
            statusDate = picLastModifiedDate;
          
        if (!StringSupport.isNullOrEmpty(statusDate))
        {
            m_useStatusDate = true;
            if (statusDate.indexOf('T') > -1)
            {
                String statusDateFrom = statusDate.substring(0, (0) + (statusDate.indexOf('T')));
                statusDate = statusDateFrom + "-" + statusDate.substring(statusDate.indexOf('-') + 1, (statusDate.indexOf('-') + 1) + (statusDate.lastIndexOf('T') - statusDate.indexOf('-') - 1));
            }
             
            ht.put(STD_SEARCH_FIELD[ST_STATUSDATE], statusDate);
            ht.put(STD_SEARCH_FIELD[ST_LISTDATE], "");
            ht.put(STD_SEARCH_FIELD[ST_SALEDATE], "");
        }
         
        //else
        //{
        //    if (!string.IsNullOrEmpty(lastModifiedDate))
        //    {
        //        //lastModifiedDate = TimeZoneInfo.ConvertUtcToTimeZone(DateTime.ParseExact(lastModifiedDate, LastModifiedDateFormat, null), m_mlsTimeZoneInfo).ToString(sDateFormat);
        //        string lastModifiedDateFrom = lastModifiedDate.Substring(0, lastModifiedDate.IndexOf('-'));
        //        string lastModifiedDateTo = lastModifiedDate.Substring(lastModifiedDate.IndexOf('-') + 1, lastModifiedDate.Length - lastModifiedDate.IndexOf('-') -1);
        //        lastModifiedDateFrom = TimeZoneInfo.ConvertTimeZoneToTimeZone(DateTime.ParseExact(lastModifiedDateFrom, STANDARD_DATETIMEFORMAT, null), m_engine.GetGMTTimeZone(), m_mlsSearchTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        lastModifiedDateTo = TimeZoneInfo.ConvertTimeZoneToTimeZone(DateTime.ParseExact(lastModifiedDateTo, STANDARD_DATETIMEFORMAT, null), m_engine.GetGMTTimeZone(), m_mlsSearchTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        lastModifiedDate = lastModifiedDateFrom + "-" + lastModifiedDateTo;
        //        ht[STD_SEARCH_FIELD[ST_LastModified]] = lastModifiedDate;
        //    }
        //    if (!string.IsNullOrEmpty(picLastModifiedDate))
        //    {
        //        //picLastModifiedDate = TimeZoneInfo.ConvertUtcToTimeZone(DateTime.ParseExact(picLastModifiedDate, STANDARD_DATETIMEFORMAT, null), m_mlsTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        string picLastModifiedDateFrom = picLastModifiedDate.Substring(0, picLastModifiedDate.IndexOf('-'));
        //        string picLastModifiedDateTo = picLastModifiedDate.Substring(picLastModifiedDate.IndexOf('-') + 1, picLastModifiedDate.Length - picLastModifiedDate.IndexOf('-') - 1);
        //        picLastModifiedDateFrom = TimeZoneInfo.ConvertTimeZoneToTimeZone(DateTime.ParseExact(picLastModifiedDateFrom, STANDARD_DATETIMEFORMAT, null), m_engine.GetGMTTimeZone(), m_mlsSearchTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        //picLastModifiedDateFrom = TimeZoneInfo.ConvertUtcToTimeZone(DateTime.ParseExact(picLastModifiedDateFrom, STANDARD_DATETIMEFORMAT, null), m_mlsSearchTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        picLastModifiedDateTo = TimeZoneInfo.ConvertTimeZoneToTimeZone(DateTime.ParseExact(picLastModifiedDateTo, STANDARD_DATETIMEFORMAT, null), m_engine.GetGMTTimeZone(), m_mlsSearchTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        //picLastModifiedDateTo = TimeZoneInfo.ConvertUtcToTimeZone(DateTime.ParseExact(picLastModifiedDateTo, STANDARD_DATETIMEFORMAT, null), m_mlsSearchTimeZoneInfo).ToString(STANDARD_DATETIMEFORMAT);
        //        picLastModifiedDate = picLastModifiedDateFrom + "-" + picLastModifiedDateTo;
        //        ht[STD_SEARCH_FIELD[ST_PicModified]] = picLastModifiedDate;
        //    }
        //}
        boolean isSupportStatusDate = m_engine.isSupportStatusDate() && statusDate.length() > 0;
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
            defValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH, field);
            //if( value.length() == 0 && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_LISTDATE]) && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_SALEDATE]) && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_LISTPRICE]) && !field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_SALEPRICE]))
            //	continue;
            if (!field.startsWith("ST_"))
                continue;
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LastModified].ToUpper()))
            {
                if (m_useStatusDate)
                    value_Renamed = "";
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PicModified].ToUpper()))
            {
                if (m_useStatusDate)
                    value_Renamed = "";
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUSDATE].ToUpper()))
            {
            }
             
            //if (isSupportStatusDate)
            //	value_Renamed = getDateFormat(STD_SEARCH_FIELD[ST_STATUSDATE], value_Renamed);
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTDATE].ToUpper()))
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
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SALEDATE].ToUpper()))
            {
                if (!isSoldStatus)
                    value_Renamed = "";
                 
                if (isSupportStatusDate)
                    value_Renamed = "";
                else if (statusDate.length() > 0)
                    if (isSoldStatus)
                        value_Renamed = statusDate;
                     
                  
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_MLSNUMBER].ToUpper()))
            {
                value_Renamed = RemoveSpace(m_mlsNumber, ",");
                m_mlsNumber = value_Renamed;
            }
             
            m_searchFields[field] = value_Renamed;
        }
        if (sb.length() > 0)
        {
            sb.insert(0, STRINGS.get_Renamed(Tcs.Mls.STRINGS.NOT_SURPPORT_SEARCH_FIELD));
            addXMLNote(TCSException.NOT_SURPPORT_SEARCH_FIELD, sb.toString().substring(0, (0) + ((sb.toString().lastIndexOf(",")) - (0))));
            m_warningNoteId = m_noteId - 1;
            m_isAddWarningNote = true;
        }
        else
            m_isAddWarningNote = false; 
        SetRecordLimit();
    }

    public String getPublicStandardStatus(int type) throws Exception {
        if (_useCompactFormat)
            return getPublicStatusResultFromSearchField();
         
        String status = "";
        switch(type)
        {
            case MLSRecord.TYPE_ACTIVE: 
                status = STANDARD_PUBLIC_STATUS_RESULT[0];
                break;
            case MLSRecord.TYPE_SOLD: 
                status = STANDARD_PUBLIC_STATUS_RESULT[1];
                break;
            case MLSRecord.TYPE_PENDING: 
                status = STANDARD_PUBLIC_STATUS_RESULT[2];
                break;
            default: 
                status = "";
                break;
        
        }
        return status;
    }

    // STANDERD_STATUS[0];
    private String getPublicStatusResultFromSearchField() throws Exception {
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ status = GetCurrentStandardPublicStatus();
        System.Object __dummyScrutVar1 = status;
        if (__dummyScrutVar1.equals("A"))
        {
            status = STANDARD_PUBLIC_STATUS_RESULT[0];
        }
        else if (__dummyScrutVar1.equals("S"))
        {
            status = STANDARD_PUBLIC_STATUS_RESULT[1];
        }
        else if (__dummyScrutVar1.equals("O"))
        {
            status = STANDARD_PUBLIC_STATUS_RESULT[2];
        }
        else
        {
            status = "";
        }   
        return status;
    }

    protected public void getOutputXML() throws Exception {
        m_connector.WriteLine("GetOutPutXML");
        if (m_engine.IsDownloadIDs)
        {
            AddIDsForDownloadIDOnlySearch();
            return ;
        }
         
        //System.Text.StringBuilder sb = new System.Text.StringBuilder();
        boolean isDemo = m_engine.isDemoClient();
        boolean foundDemoListing = false;
        if (m_blockedFields == null)
            m_blockedFields = m_connector.GetDomFilteredFields();
         
        MLSCmaFields fields = null;
        fields = m_engine.getCmaFields();
        CheckNormFieldDecoupleDataFlag(fields);
        net.toppro.components.mls.engine.CmaField cf = fields.getField("STDFLastMod");
        boolean resultLastModMapped = true;
        String lastModifiedDateFormat = "";
        if (cf != null)
        {
            lastModifiedDateFormat = cf.getDateFormat();
            if (StringSupport.isNullOrEmpty(lastModifiedDateFormat))
            {
                if (cf.type == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME)
                    lastModifiedDateFormat = STANDARD_DATETIMEFORMAT;
                else
                    lastModifiedDateFormat = STANDARD_DATEFORMAT; 
            }
             
        }
        else
            resultLastModMapped = false; 
        boolean resultPicLastModMapped = true;
        String lastPicModifiedDateFormat = "";
        cf = fields.getField("STDFPicMod");
        if (cf != null)
        {
            lastPicModifiedDateFormat = cf.getDateFormat();
            if (StringSupport.isNullOrEmpty(lastPicModifiedDateFormat))
            {
                if (cf.type == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME)
                    lastPicModifiedDateFormat = STANDARD_DATETIMEFORMAT;
                else
                    lastPicModifiedDateFormat = STANDARD_DATEFORMAT; 
            }
             
        }
        else
            resultPicLastModMapped = false; 
        if (StringSupport.isNullOrEmpty(lastModifiedDateFormat))
            lastModifiedDateFormat = "";
         
        if (!isDemo || m_isSearchByMLSNumber)
            m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        else
        {
            m_status __dummyScrutVar2 = m_status;
            if (__dummyScrutVar2.equals("A"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ACTIVE);
            }
            else if (__dummyScrutVar2.equals("S"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_SOLD);
            }
            else if (__dummyScrutVar2.equals("P"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_PENDING);
            }
            else if (__dummyScrutVar2.equals("E"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_EXPIRED);
            }
                
        } 
        MLSRecord rec = m_records.getFirstRecord();
        String fieldValue = "";
        String fieldName = "";
        boolean bAddField = true;
        int recordCount = 0;
        MLSRecord buyerAgentRecord = null;
        MLSRecord listAgentRecord = null;
        MLSRecord buyerOfficeRecord = null;
        MLSRecord listOfficeRecord = null;
        String buyerAgentID = "";
        String listAgentID = "";
        String buyerOfficeID = "";
        String listOfficeID = "";
        String listBrokerID = "";
        String buyerBrokerID = "";
        boolean needAgentOfficeInfo = false;
        while (rec != null)
        {
            //needAgentOfficeInfo = m_connector.NeedAgentOfficeSearch;
            m_resultBuffer.Append("<Listing>");
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
            String standardPropertyType = "";
            String saleOrLease = "";
            String displayListingOnRDC = "";
            String recordID = "";
            String propertyTypeMls = "";
            String dataaggOfficeID = "";
            String dataaggAgentID = "";
            boolean skipjunkfilter = false;
            boolean goNextRec = false;
            for (int i = 0;i < getResultField().length;i++)
            {
                if (goNextRec)
                    break;
                 
                fieldValue = "";
                fieldName = Tcs.Mls.TCSStandardResultFields.getXmlName(getResultField()[i]);
                //]field.getName();
                net.toppro.components.mls.engine.CmaField standardCmaField = fields.getStdField(getResultField()[i]);
                bAddField = true;
                String mlsValue = "";
                if (!isDemo)
                {
                    switch(getResultField()[i])
                    {
                        case Tcs.Mls.TCSStandardResultFields.STDF_RECORDID: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            recordID = fieldValue;
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
                            if (NormDecoupleData[(int)NormFields.CMAIDENTIFIER] == -10)
                            {
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYTYPEMLS,false);
                                fieldValue = GetStandardPropertyType(fieldValue);
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (!StandardPropertyTypeSet.Contains(fieldValue))
                                    fieldValue = "";
                                 
                            } 
                            standardPropertyType = fieldValue;
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFPROPERTYSUBTYPE: 
                            mlsValue = rec.getStdFieldValue(getResultField()[i],false);
                            fieldValue = rec.validateSTDFPropertySubtype(mlsValue,standardPropertyType,!(fields.getField("STDFPropertySubtype") == null));
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM: 
                            if (NormDecoupleData[(int)NormFields.CMAIDENTIFIER] == -10)
                                fieldValue = getStandardStatus(rec.getMLSRecordType());
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                if (!fieldValue.equals("A") && !fieldValue.equals("S") && !fieldValue.equals("P") && !fieldValue.equals("E"))
                                    fieldValue = "";
                                 
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_PUBLICLISTINGSTATUS_NODEFNAME: 
                            fieldValue = getPublicStandardStatus(rec.getPublicType());
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
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFFULLBATHROOMS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFTHREEQBATHROOMS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFHALFBATHROOMS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFQBATHROOMS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGPRICE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMASALEPRICE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFSEARCHPRICE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFNUMBEROFGARAGESPACES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFNUMBEROFFIREPLACES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFNUMBEROFPHOTOS: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            fieldValue = ConvertToInt(fieldValue);
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMABEDROOMS: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            fieldValue = ConvertToInt(fieldValue);
                            if (StringSupport.equals(fieldValue, "0"))
                            {
                                skipjunkfilter = true;
                            }
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM: 
                            if (NormDecoupleData[(int)NormFields.CMABATHROOMS] == -10)
                            {
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMS,false);
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMABATHROOMSNORM,mlsValue);
                                if (!mlsValue.toUpperCase().equals(fieldValue.toUpperCase()))
                                    dataStandardNoteId = (dataStandardNoteId.length() == 0 ? "" : (dataStandardNoteId + ",")) + addResultValidationNote(getResultField()[i]);
                                 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                fieldValue = ConvertToInt(fieldValue);
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                            if (NormDecoupleData[(int)NormFields.CMASQUAREFEET] == -10)
                            {
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET,false);
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM,mlsValue);
                                if (!mlsValue.toUpperCase().equals(fieldValue.toUpperCase()))
                                    dataStandardNoteId = (dataStandardNoteId.length() == 0 ? "" : (dataStandardNoteId + ",")) + addResultValidationNote(getResultField()[i]);
                                 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                fieldValue = ConvertToInt(fieldValue);
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM: 
                            if (NormDecoupleData[(int)NormFields.CMAAGE] == -10)
                            {
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE,false);
                                fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM,mlsValue);
                                if (!mlsValue.toUpperCase().equals(fieldValue.toUpperCase()))
                                    dataStandardNoteId = (dataStandardNoteId.length() == 0 ? "" : (dataStandardNoteId + ",")) + addResultValidationNote(getResultField()[i]);
                                 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                fieldValue = ConvertToInt(fieldValue);
                                if (fieldValue.length() != 4)
                                    fieldValue = "";
                                 
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMALOTSIZENORM: 
                            if (NormDecoupleData[(int)NormFields.CMALOTSIZE] == -10)
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
                                boolean boolVar___0 = !DoubleSupport.tryParse(fieldValue, refVar___0);
                                if (boolVar___0)
                                {
                                    RefSupport<Double> refVar___0 = new RefSupport<Double>();
                                    fieldValue = "";
                                    Num = refVar___0.getValue();
                                }
                                 
                                if (!StringSupport.isNullOrEmpty(fieldValue))
                                    fieldValue = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0.#########}"),Num);
                                 
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIESNORM: 
                            if (NormDecoupleData[(int)NormFields.CMASTORIES] == -10)
                            {
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASTORIES,false);
                                fieldValue = rec.validateSTDFStories(mlsValue);
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                double Num;
                                boolean boolVar___1 = !DoubleSupport.tryParse(fieldValue, refVar___1);
                                if (boolVar___1)
                                {
                                    RefSupport<Double> refVar___1 = new RefSupport<Double>();
                                    fieldValue = "";
                                    Num = refVar___1.getValue();
                                }
                                else
                                    fieldValue = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0.0}"),Num); 
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNTNORM: 
                            if (NormDecoupleData[(int)NormFields.CMATAXAMOUNT] == -10)
                            {
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMATAXAMOUNT,false);
                                fieldValue = rec.validateSTDFTax(mlsValue);
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                fieldValue = ConvertToInt(fieldValue);
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENTNORM: 
                            if (NormDecoupleData[(int)NormFields.CMAASSESSMENT] == -10)
                            {
                                mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAASSESSMENT,false);
                                fieldValue = rec.validateSTDFTax(mlsValue);
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                                fieldValue = ConvertToInt(fieldValue);
                            } 
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMAAREA: 
                            fieldValue = rec.getCMAArea();
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTID: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTFNAME: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTLNAME: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTEMAIL: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTPHONE: 
                            if (needAgentOfficeInfo && (m_agentEngine.getCmaFields().getStdField(getResultField()[i]) != null) && m_searchID[0])
                            {
                                if (buyerAgentRecord != null)
                                    fieldValue = buyerAgentRecord.getTCSStdFieldValue(getResultField()[i],true);
                                else
                                    fieldValue = ""; 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            } 
                            if (getResultField()[i] == Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTID)
                                buyerAgentID = fieldValue;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTID: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTFNAME: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTLNAME: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTPHONE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTEMAIL: 
                            if (needAgentOfficeInfo && (m_agentEngine.getCmaFields().getStdField(getResultField()[i]) != null) && m_searchID[1])
                            {
                                if (listAgentRecord != null)
                                    fieldValue = listAgentRecord.getTCSStdFieldValue(getResultField()[i],true);
                                else
                                    fieldValue = ""; 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            } 
                            if (getResultField()[i] == Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTID)
                                listAgentID = fieldValue;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERBROKERID: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYEROFFICENAME: 
                            net.toppro.components.mls.engine.MLSEngine engine = m_officeEngine;
                            if (m_officeEngine == null)
                                engine = m_agentEngine;
                             
                            if (needAgentOfficeInfo && engine.getCmaFields().getStdField(getResultField()[i]) != null && m_searchID[2])
                            {
                                if (buyerOfficeRecord != null)
                                    fieldValue = buyerOfficeRecord.getTCSStdFieldValue(getResultField()[i],true);
                                else
                                    fieldValue = ""; 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            } 
                            if (getResultField()[i] == Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERBROKERID)
                                buyerBrokerID = fieldValue;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTBROKERID: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICENAME: 
                            engine = m_officeEngine;
                            if (m_officeEngine == null)
                                engine = m_agentEngine;
                             
                            if (needAgentOfficeInfo && (engine.getCmaFields().getStdField(getResultField()[i]) != null) && m_searchID[3])
                            {
                                if (listOfficeRecord != null)
                                    fieldValue = listOfficeRecord.getTCSStdFieldValue(getResultField()[i],true);
                                else
                                    fieldValue = ""; 
                            }
                            else
                            {
                                fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            } 
                            if (getResultField()[i] == Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTBROKERID)
                                listBrokerID = fieldValue;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTIDDATAAGG: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue))
                                fieldValue = listAgentID;
                             
                            dataaggAgentID = fieldValue;
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICEIDDATAAGG: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue))
                                fieldValue = listBrokerID;
                             
                            dataaggOfficeID = fieldValue;
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYEROFFICEIDDATAAGG: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue))
                                fieldValue = buyerBrokerID;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFBUYERAGENTIDDATAAGG: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue))
                                fieldValue = buyerAgentID;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTIDTIGERLEAD: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue) && (fields.getField("STDFListAgentIDTigerLead") == null))
                                fieldValue = dataaggAgentID;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTAGENTIDSHOWINGTIME: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue) && (fields.getField("STDFListAgentIDShowingTime") == null))
                                fieldValue = dataaggAgentID;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICEIDTIGERLEAD: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue) && (fields.getField("STDFListOfficeIDTigerLead") == null))
                                fieldValue = dataaggOfficeID;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICEIDSHOWINGTIME: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue) && (fields.getField("STDFListOfficeIDShowingTime") == null))
                                fieldValue = dataaggOfficeID;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFDISPLAYLISTINGONRDC: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDFORECLOSEDREO: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDAUCTION: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISTRESSEDSHORTSALE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCARPORT: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHLAUNDRYROOM: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDININGROOM: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHGAMEROOM: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHFAMILYROOM: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHBASEMENT: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCENTRALAIR: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCENTRALHEAT: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHFORCEDAIR: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHHARDWOODFLOORS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHFIREPLACE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHSWIMMINGPOOL: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHRVBOATPARKING: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHSPAHOTTUB: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHHORSEFACILITIES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHTENNISCOURTS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDISABILITYFEATURES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHPETSALLOWED: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHENERGYEFFICIENTHOME: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHDEN: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHOFFICE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHOCEANVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHANYVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHWATERVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYSWIMMINGPOOL: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHLAKEVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHGOLFCOURSEVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYSECURITYFEATURES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHSENIORCOMMUNITY: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHGOLFCOURSELOTORFRONTAGE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCULDESAC: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCITYVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHHILLMOUNTAINVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHRIVERVIEW: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYSPAHOTTUB: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYCLUBHOUSE: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYRECREATIONFACILITIES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYTENNISCOURTS: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCORNERLOT: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYGOLF: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYHORSEFACILITIES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYBOATFACILITIES: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHCOMMUNITYPARK: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_SRCHLEASEOPTION: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (!StringSupport.isNullOrEmpty(fieldValue))
                            {
                                fieldValue = StringSupport.Trim(fieldValue);
                                if (!(fieldValue.equals("Y") || fieldValue.equals("N")))
                                    fieldValue = "";
                                 
                            }
                            else
                                fieldValue = ""; 
                            if (getResultField()[i] == Tcs.Mls.TCSStandardResultFields.STDF_STDFDISPLAYLISTINGONRDC)
                                displayListingOnRDC = fieldValue;
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFEXCLUDEADDRESSFROMRDC: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (!StringSupport.isNullOrEmpty(fieldValue))
                            {
                                if (!fieldValue.equals("NoStreetAddress"))
                                {
                                    if (fieldValue.startsWith("PartialAddress"))
                                    {
                                        if (!char.IsDigit(fieldValue, fieldValue.length() - 1))
                                            fieldValue = "";
                                         
                                    }
                                    else
                                        fieldValue = ""; 
                                }
                                 
                            }
                             
                            if (StringSupport.isNullOrEmpty(fieldValue))
                                fieldValue = "NoRestrictions";
                             
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
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
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
                             
                            // STANDERD_STATUS[0];
                            statusDate = fieldValue;
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (!resultLastModMapped)
                            {
                                if (!StringSupport.isNullOrEmpty(statusDate))
                                    fieldValue = statusDate;
                                else
                                {
                                    m_status __dummyScrutVar5 = m_status;
                                    if (__dummyScrutVar5.equals("A") || __dummyScrutVar5.equals("E") || __dummyScrutVar5.equals("P"))
                                    {
                                        fieldValue = listDate;
                                    }
                                    else if (__dummyScrutVar5.equals("S"))
                                    {
                                        fieldValue = saleDate;
                                    }
                                      
                                } 
                                if (!StringSupport.isNullOrEmpty(fieldValue))
                                    fieldValue = MLSUtil.FormatDate(STANDARD_DATEFORMAT, STANDARD_DATETIMEFORMAT, fieldValue);
                                 
                            }
                             
                            if (fieldValue.indexOf('T') < 0)
                                fieldValue = MLSUtil.FormatDate(STANDARD_DATEFORMAT, STANDARD_DATETIMEFORMAT, fieldValue);
                             
                            if (fieldValue.indexOf("00:00:00") > -1)
                                fieldValue = fieldValue.replace("00:00:00", "23:59:59");
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFPICMOD: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (!resultPicLastModMapped)
                            {
                                if (!StringSupport.isNullOrEmpty(statusDate))
                                    fieldValue = statusDate;
                                else
                                {
                                    m_status __dummyScrutVar6 = m_status;
                                    if (__dummyScrutVar6.equals("A") || __dummyScrutVar6.equals("E") || __dummyScrutVar6.equals("P"))
                                    {
                                        fieldValue = listDate;
                                    }
                                    else if (__dummyScrutVar6.equals("S"))
                                    {
                                        fieldValue = saleDate;
                                    }
                                      
                                } 
                                if (!StringSupport.isNullOrEmpty(fieldValue))
                                    fieldValue = MLSUtil.FormatDate(STANDARD_DATEFORMAT, STANDARD_DATETIMEFORMAT, fieldValue);
                                 
                            }
                             
                            if (fieldValue.indexOf('T') < 0)
                                fieldValue = MLSUtil.FormatDate(STANDARD_DATEFORMAT, STANDARD_DATETIMEFORMAT, fieldValue);
                             
                            if (fieldValue.indexOf("00:00:00") > -1)
                                fieldValue = fieldValue.replace("00:00:00", "23:59:59");
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM1: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM2: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM3: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM4: 
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFIDXCUSTOM5: 
                            //case TCSStandardResultFields.STDF_STDFLAT:
                            //case TCSStandardResultFields.STDF_STDFLONG:
                            //    fieldValue = "";
                            //    break;
                            fieldValue = "";
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFSALEORLEASE: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            saleOrLease = fieldValue;
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMADAYSONMARKET: 
                            // Remove the value of DOM from DataAgg requests!!
                            fieldValue = "";
                            break;
                        default: 
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
                            mlsValue = rec.getStdFieldValue(getResultField()[i],false);
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],mlsValue);
                            if (!IsMatchSearchCriteria(ST_TOTALBATHS, fieldValue))
                            {
                                goNextRec = true;
                                continue;
                            }
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM: 
                            mlsValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET,false);
                            fieldValue = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEETNORM,mlsValue);
                            if (!IsMatchSearchCriteria(ST_SQFT, fieldValue))
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

                            if (!IsMatchSearchCriteria(ST_BEDS, fieldValue))
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
                            if (!IsMatchSearchCriteria(ST_LISTPRICE, fieldValue))
                            {
                                goNextRec = true;
                                continue;
                            }
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMASALEPRICE: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (!IsMatchSearchCriteria(ST_SALEPRICE, fieldValue))
                            {
                                goNextRec = true;
                                continue;
                            }
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFSEARCHPRICE: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (!IsMatchSearchCriteria(ST_SEARCHPRICE, fieldValue))
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
                            String searchListDate = (String)m_connector.getSearchFields()[STD_SEARCH_FIELD[ST_LISTDATE]];
                            String searchStatusDate = (String)m_connector.getSearchFields()[STD_SEARCH_FIELD[ST_STATUSDATE]];
                            String searchSaleDate = (String)m_connector.getSearchFields()[STD_SEARCH_FIELD[ST_SALEDATE]];
                            m_status __dummyScrutVar8 = m_status;
                            if (__dummyScrutVar8.equals("A") || __dummyScrutVar8.equals("E") || __dummyScrutVar8.equals("P"))
                            {
                                searchSaleDate = "";
                            }
                             
                            if (!StringSupport.isNullOrEmpty(searchStatusDate))
                            {
                                statusDate = GetDemoDate(searchStatusDate);
                                Tcs.Mls.Request.GetDataAggListings.APPLY __dummyScrutVar9 = getStandardStatus(rec.getMLSRecordType());
                                if (__dummyScrutVar9.equals("A"))
                                {
                                    demoListDate = statusDate;
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar9.equals("E") || __dummyScrutVar9.equals("P"))
                                {
                                    demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(statusDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar9.equals("S"))
                                {
                                    saleDate = statusDate;
                                    demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(statusDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                }
                                   
                            }
                            else if (!StringSupport.isNullOrEmpty(searchSaleDate))
                            {
                                saleDate = GetDemoDate(searchSaleDate);
                                demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(saleDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                statusDate = saleDate;
                            }
                            else
                            {
                                demoListDate = GetDemoDate(searchListDate);
                                Tcs.Mls.Request.GetDataAggListings.APPLY __dummyScrutVar10 = getStandardStatus(rec.getMLSRecordType());
                                if (__dummyScrutVar10.equals("A"))
                                {
                                    statusDate = demoListDate;
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar10.equals("E") || __dummyScrutVar10.equals("P"))
                                {
                                    statusDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.parse(demoListDate), "MM/dd/yyyy"),60);
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar10.equals("S"))
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
                        default: 
                            //case TCSStandardResultFields.STDF_STDFLAT:
                            //case TCSStandardResultFields.STDF_STDFLONG:
                            //    fieldValue = "";
                            //    break;
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            break;
                    
                    }
                } 
                if (bAddField && !StringSupport.isNullOrEmpty(fieldValue))
                {
                    if (!skipjunkfilter)
                    {
                        fieldValue = Tcs.Mls.Util.filterJunkValue(fieldValue);
                    }
                    else
                    {
                        skipjunkfilter = false;
                    } 
                    if (standardCmaField != null && standardCmaField.displaybehaviorbitmask.equals("8"))
                        fieldValue = getStringFormat(fieldValue,standardCmaField,false);
                    else
                    {
                        fieldValue = Tcs.Mls.Util.convertStringToXML(fieldValue);
                    } 
                    if (!(StringSupport.isNullOrEmpty(fieldValue)))
                        // || fieldValue.Equals("0")))
                        m_resultBuffer.Append("<" + fieldName + ">" + fieldValue + "</" + fieldName + ">");
                     
                }
                 
            }
            if (goNextRec)
            {
                int pos = m_resultBuffer.toString().lastIndexOf("<Listing");
                m_resultBuffer.Remove(pos, m_resultBuffer.Length - pos);
                rec = m_records.getNextRecord();
                continue;
            }
             
            if (!m_includeTlBlob)
            {
                if (!SelectPicFieldsOnly)
                {
                    String views = "";
                    try
                    {
                        fl = rec.getViewsList();
                        if (fl != null && fl.length > 0)
                        {
                            for (int j = 0;j < fl.length;j++)
                            {
                                if (j == fl.length - 1)
                                    views = views + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.filterJunkValue(StringSupport.Trim(fl[j])));
                                else
                                    views = views + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.filterJunkValue(StringSupport.Trim(fl[j]))) + "!#!"; 
                            }
                        }
                         
                    }
                    catch (Exception e)
                    {
                        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                        m_connector.WriteLine("Error on retrieving view" + e.getMessage());
                        views = "";
                    }

                    if (!StringSupport.isNullOrEmpty(views))
                    {
                        m_resultBuffer.Append("<Views>");
                        m_resultBuffer.Append(views);
                        m_resultBuffer.Append("</Views>");
                    }
                     
                    m_resultBuffer.Append(rec.getRoomDimensions(true,true,false));
                    m_resultBuffer.Append(rec.getUnits(false));
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
                      
                    if (!m_tcs.IsRealTime && !StringSupport.isNullOrEmpty(footNote))
                        m_resultBuffer.Append("<NoteID>").Append(footNote + "</NoteID>");
                     
                    //m_resultBuffer.Append("\r\n");
                    //Add Features, Room Dimensions, Views
                    if (m_includeCDATA)
                    {
                        m_resultBuffer.Append("<OtherListingFields><![CDATA[");
                    }
                    else
                    {
                        m_resultBuffer.Append("<OtherListingFields>");
                    } 
                    //m_resultBuffer.Append("<OtherListingFields>");
                    net.toppro.components.mls.engine.CmaField stdfRoomdim = fields.getStdField(Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM);
                    String roomDimFields = "";
                    if (stdfRoomdim != null)
                    {
                        roomDimFields = StringSupport.Trim(stdfRoomdim.getRecordPosition().toUpperCase()) + ",";
                        if (StringSupport.isNullOrEmpty(roomDimFields))
                            roomDimFields = "";
                         
                    }
                     
                    int[] nonStandardFields = fields.getNonStdFields();
                    for (int m = 0;m < nonStandardFields.length;m++)
                    {
                        if (nonStandardFields[m] == 0)
                            break;
                         
                        net.toppro.components.mls.engine.CmaField f = fields.getField(nonStandardFields[m]);
                        if (f != null)
                        {
                            String nonStandardValue = rec.getFieldValue(nonStandardFields[m]);
                            if (!StringSupport.isNullOrEmpty(nonStandardValue))
                            {
                                String temp = getStringFormat(nonStandardValue,f,roomDimFields.indexOf(f.name + ",") > -1);
                                if (!StringSupport.isNullOrEmpty(temp))
                                {
                                    String fieldXmlName = Tcs.Mls.Util.convertStringToXMLNode(f.name) + "NS";
                                    if (!m_blockedFields.Contains(fieldXmlName))
                                    {
                                        m_resultBuffer.Append("<" + fieldXmlName + ">" + temp + "</" + fieldXmlName + ">");
                                    }
                                     
                                }
                                 
                            }
                             
                        }
                         
                    }
                    String prefix = "";
                    try
                    {
                        for (int m = 0;m < fields.getFeatureGroupList().size();m++)
                        {
                            for (int fieldIndex : fields.getFeatureGroupList().get(m))
                            {
                                //fl = rec.getSTDFFieldList(TCSStandardResultFields.STDF_CMAFEATURE, new int[] { TCSStandardResultFields.STDF_STDFROOMDIM, TCSStandardResultFields.STDF_STDFVIEWS }, true);
                                net.toppro.components.mls.engine.CmaField cfield = fields.getField(fieldIndex);
                                if (cfield == null || cfield.getStdId() > Tcs.Mls.TCSStandardResultFields.CUSTOM_FIELD)
                                    continue;
                                 
                                String featDisplayName = cfield.displayname;
                                if (StringSupport.isNullOrEmpty(featDisplayName))
                                    continue;
                                 
                                featDisplayName = Tcs.Mls.Util.convertStringToXMLNode(cfield.xmlname);
                                String featureValue = StringSupport.Trim(rec.getFieldValue(fieldIndex));
                                if (!StringSupport.isNullOrEmpty(featureValue))
                                {
                                    String temp = getStringFormat(featureValue,cfield,roomDimFields.indexOf(cfield.name + ",") > -1);
                                    if (!StringSupport.isNullOrEmpty(temp))
                                    {
                                        String fieldXmlName = "MLS_" + featDisplayName;
                                        if (!m_blockedFields.Contains(fieldXmlName))
                                        {
                                            m_resultBuffer.Append("<" + fieldXmlName + ">");
                                            m_resultBuffer.Append(temp);
                                            m_resultBuffer.Append("</" + fieldXmlName + ">");
                                        }
                                         
                                    }
                                     
                                }
                                 
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                        m_connector.WriteLine("Error on retrieving features" + e.getMessage());
                    }

                    if (m_includeCDATA)
                    {
                        m_resultBuffer.Append("]]></OtherListingFields>");
                    }
                    else
                    {
                        m_resultBuffer.Append("</OtherListingFields>");
                    } 
                }
                 
            }
             
            if (m_includeTlBlob)
            {
                m_resultBuffer.Append("<RETSBlob><![CDATA[{\"PropertyClass\":");
                m_resultBuffer.Append("\"").Append(m_engine.PropertyClassNameFromMainScript);
                m_resultBuffer.Append("\",\"ListingData\": {");
                Boolean firstBlobField = true;
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ retsBlobFieldList = m_engine.GetFieldListForRetsBlob();
                for (/* [UNSUPPORTED] 'var' as type is unsupported "var" */ blogField : retsBlobFieldList.Keys)
                {
                    /* [UNSUPPORTED] 'var' as type is unsupported "var" */ blobFieldIndex = retsBlobFieldList[blogField];
                    net.toppro.components.mls.engine.CmaField f = fields.getField(blobFieldIndex);
                    if (f != null)
                    {
                        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ systemName = fields.getFieldPositions()[blobFieldIndex];
                        if (!String.IsNullOrEmpty(systemName) && systemName.StartsWith("@"))
                        {
                            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ fieldNameForBlob = systemName.Remove(0, 1).Replace("\"", "");
                            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ fieldValueForBlob = rec.GetFieldRawValue(blobFieldIndex);
                            if (!String.IsNullOrEmpty(fieldValueForBlob))
                            {
                                String fieldXmlName = Tcs.Mls.Util.convertStringToXMLNode(f.name) + "NS";
                                if (!m_blockedFields.Contains(fieldXmlName))
                                {
                                    if (firstBlobField)
                                    {
                                        firstBlobField = false;
                                    }
                                    else
                                    {
                                        m_resultBuffer.Append(",");
                                    } 
                                    m_resultBuffer.Append("\"" + fieldNameForBlob + "\":" + Tcs.Mls.Util.EncodeJsString(fieldValueForBlob));
                                }
                                 
                            }
                             
                        }
                         
                    }
                     
                }
                m_resultBuffer.Append("}}]]></RETSBlob>");
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
                            m_connector.SaveTaskFileResult(objectId, arrPicFileName[i]);
                            m_resultBuffer.Append("<Picture");
                            //sb.append( " Url=" + "\"" + picFileName + "\"" );
                            if (m_tcs.IsRealTime)
                                m_resultBuffer.Append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?lp=" + Tcs.Mls.Util.base64Encode(arrPicFileName[i])));
                            else
                                m_resultBuffer.Append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?message_header=" + m_connector.getMessageHeader() + "&object_id=" + objectId) + "\""); 
                            m_resultBuffer.Append("/>\r\n");
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
                        m_connector.SaveTaskFileResult(objectId, picFileName);
                        m_resultBuffer.Append("<Picture");
                        //sb.append( " Url=" + "\"" + picFileName + "\"" );
                        if (m_tcs.IsRealTime)
                            m_resultBuffer.Append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?lp=" + Tcs.Mls.Util.base64Encode(picFileName)));
                        else
                            m_resultBuffer.Append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?message_header=" + m_connector.getMessageHeader() + "&object_id=" + objectId) + "\""); 
                        m_resultBuffer.Append("/>\r\n");
                    }
                     
                } 
            }
             
            m_resultBuffer.Append("</Listing>\r\n");
            if (!displayListingOnRDC.equals("N"))
            {
                if (StringSupport.Compare(saleOrLease, "Lease", true) == 0)
                {
                    TCSCount.put("Rental", TCSCount.get("Rental") + 1);
                    TCSMLSNumbers.get("Rental").Add(recordID);
                }
                else
                {
                    if (!StringSupport.isNullOrEmpty(standardPropertyType))
                    {
                        if (standardPropertyType.indexOf(",") > -1)
                            standardPropertyType = standardPropertyType.substring(0, (0) + (standardPropertyType.indexOf(",")));
                         
                        String __dummyScrutVar11 = standardPropertyType;
                        if (__dummyScrutVar11.equals("Single-family"))
                        {
                            TCSCount.put("Residential", TCSCount.get("Residential") + 1);
                            TCSMLSNumbers.get("Residential").Add(recordID);
                        }
                        else if (__dummyScrutVar11.equals("Condo") || __dummyScrutVar11.equals("Town-Home") || __dummyScrutVar11.equals("Co-Op"))
                        {
                            TCSCount.put("Condo", TCSCount.get("Condo") + 1);
                            TCSMLSNumbers.get("Condo").Add(recordID);
                        }
                        else if (__dummyScrutVar11.equals("Multifamily"))
                        {
                            TCSCount.put("MultiFamily", TCSCount.get("MultiFamily") + 1);
                            TCSMLSNumbers.get("MultiFamily").Add(recordID);
                        }
                        else if (__dummyScrutVar11.equals("Manufactured/Mobile"))
                        {
                            TCSCount.put("Mobile", TCSCount.get("Mobile") + 1);
                            TCSMLSNumbers.get("Mobile").Add(recordID);
                        }
                        else if (__dummyScrutVar11.equals("Land") || __dummyScrutVar11.equals("Lot") || __dummyScrutVar11.equals("Land,Lot"))
                        {
                            TCSCount.put("Land", TCSCount.get("Land") + 1);
                            TCSMLSNumbers.get("Land").Add(recordID);
                        }
                        else if (__dummyScrutVar11.equals("Farm or Ranch"))
                        {
                            TCSCount.put("Farm", TCSCount.get("Farm") + 1);
                            TCSMLSNumbers.get("Farm").Add(recordID);
                        }
                              
                    }
                     
                } 
            }
             
            rec = m_records.getNextRecord();
            recordCount++;
            m_overrideLimitCount++;
            if (IsOverrideLimit && m_overrideLimitCount > GetOverrideRecordLimit())
                break;
             
            if (rec != null)
                CheckResultSize();
            else
                ResultListingCount++; 
        }
        if (isDemo && !foundDemoListing && !m_tcs.isGetPicture())
        {
            addXMLNote(TCSException.MLS_ERROR_NO_RECORD_FOUND, Tcs.Mls.Util.convertStringToXML(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND)));
        }
         
    }

    protected String getStringFormat(String value, net.toppro.components.mls.engine.CmaField cmaField, boolean isRoomDimension) throws Exception {
        if (StringSupport.isNullOrEmpty(value) || cmaField == null)
            return "";
         
        String[] subValues;
        boolean hasPrefixInValue = false;
        if (value.indexOf(":") > -1)
        {
            if (!StringSupport.isNullOrEmpty(cmaField.prefix))
            {
                hasPrefixInValue = true;
            }
             
            if (hasPrefixInValue)
                subValues = StringSupport.Split(value.substring(value.indexOf(":") + 1), ',');
            else
                subValues = StringSupport.Split(value, ','); 
        }
        else
            subValues = StringSupport.Split(value, ','); 
        String result = "";
        String seperator = ", ";
        if (cmaField.displaybehaviorbitmask.equals("8"))
        {
            seperator = "!#!";
        }
        else
        {
            //If entire value is only integers and commas, with a non-zero value at beginning, also may/may not start with '$'
            //eg $15,000 or 17,000,001
            //If this matches, then return just the value
            if (hasPrefixInValue)
            {
                String subValue = StringSupport.Trim(value.substring(value.indexOf(":") + 1));
                if ((Pattern.compile("^\\$?[1-9]([0-9]+)(,[0-9]+)*$").matcher(subValue).matches()) || (Pattern.compile("^\\$?[1-9](,[0-9]+)*$").matcher(subValue).matches()))
                {
                    return subValue;
                }
                 
            }
            else
            {
                if ((Pattern.compile("^\\$?[1-9]([0-9]+)(,[0-9]+)*$").matcher(value).matches()) || (Pattern.compile("^\\$?[1-9](,[0-9]+)*$").matcher(value).matches()))
                {
                    return value;
                }
                 
            } 
        } 
        for (int n = 0;n < subValues.length;n++)
        {
            if (isRoomDimension)
                subValues[n] = Tcs.Mls.Util.filterJunkValueForRoomDimensions(StringSupport.Trim(subValues[n]));
            else
                subValues[n] = Tcs.Mls.Util.filterJunkValue(StringSupport.Trim(subValues[n])); 
            if (StringSupport.isNullOrEmpty(subValues[n]))
                continue;
             
            result = result + Tcs.Mls.Util.convertStringToXML(subValues[n]) + seperator;
        }
        if (!StringSupport.isNullOrEmpty(result))
            result = result.substring(0, (0) + (result.lastIndexOf(seperator)));
         
        return result;
    }

    protected String getStatusFromSortingOldLoigc(String status) throws Exception {
        if (m_searchByPublicStatus != 1)
            return super.getStatusFromSorting(status);
         
        String delimiter = getStatusDelimiter();
        String sortStatus = "";
        String sortValue = "";
        if (status.indexOf("A") > -1)
        {
            sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PUBLIC_SORTING_Old_LOGIC, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PUBLIC_SORTING_TYPE[0]);
            if (StringSupport.isNullOrEmpty(sortValue))
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[0]);
             
            sortStatus = sortStatus + sortValue + delimiter;
        }
         
        if (status.indexOf("S") > -1)
        {
            sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PUBLIC_SORTING_Old_LOGIC, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PUBLIC_SORTING_TYPE[1]);
            if (StringSupport.isNullOrEmpty(sortValue))
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[1]);
             
            sortStatus = sortStatus + sortValue + delimiter;
        }
         
        if (status.indexOf("O") > -1)
        {
            sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PUBLIC_SORTING_Old_LOGIC, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PUBLIC_SORTING_TYPE[2]);
            if (StringSupport.isNullOrEmpty(sortValue))
            {
                String sortValueOffMarket = "";
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[2]);
                if (!StringSupport.isNullOrEmpty(sortValue))
                    sortValueOffMarket = sortValue;
                 
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[3]);
                if (!StringSupport.isNullOrEmpty(sortValueOffMarket))
                {
                    if (!StringSupport.isNullOrEmpty(sortValue))
                        sortValue = sortValueOffMarket + delimiter + sortValue;
                    else
                        sortValue = sortValueOffMarket; 
                }
                 
            }
             
            //throw TCSException.produceTCSException(TCSException.OFFMARKET_STATUS_NOT_DEFINED_INDEF);
            sortStatus = sortStatus + sortValue + delimiter;
        }
         
        return sortStatus;
    }

    protected String getStatusFromSorting(String status) throws Exception {
        if (m_searchByPublicStatus != 1)
            return super.getStatusFromSorting(status);
         
        //if (m_connector.IsUsingOldLogicForPublicStatus())
        //{
        //    return getStatusFromSortingOldLoigc(status);
        //}
        String delimiter = getStatusDelimiter();
        String sortStatus = "";
        String sortValue = "";
        if (status.indexOf("A") > -1)
        {
            sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PUBLIC_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PUBLIC_SORTING_TYPE[0]);
            if (StringSupport.isNullOrEmpty(sortValue))
            {
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[0]);
                /* [UNSUPPORTED] 'var' as type is unsupported "var" */ pendingSortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[2]);
                if (!String.IsNullOrEmpty(pendingSortValue))
                    sortValue += delimiter + pendingSortValue;
                 
            }
             
            sortStatus = sortStatus + sortValue + delimiter;
        }
         
        if (status.indexOf("S") > -1)
        {
            sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PUBLIC_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PUBLIC_SORTING_TYPE[1]);
            if (StringSupport.isNullOrEmpty(sortValue))
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[1]);
             
            sortStatus = sortStatus + sortValue + delimiter;
        }
         
        if (status.indexOf("O") > -1)
        {
            sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PUBLIC_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PUBLIC_SORTING_TYPE[2]);
            if (StringSupport.isNullOrEmpty(sortValue))
            {
                sortValue = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SORTING, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_SORTING_TYPE[3]);
            }
             
            sortStatus = sortStatus + sortValue + delimiter;
        }
         
        return sortStatus;
    }

    protected public void addXMLHeader() throws Exception {
        if (m_connector.IsReturnDataAggListingsXml())
            super.addXMLHeader();
        else
            m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult>\r\n<Listings>\r\n"); 
    }

    protected public void addXMLFooter() throws Exception {
        if (!m_connector.IsReturnDataAggListingsXml())
            super.addXMLFooter();
        else
        {
            m_resultBuffer.Append("</Listings>\r\n");
            addNotes();
        } 
    }

    protected public void addNotes() throws Exception {
        if (!String.IsNullOrEmpty(m_connector.GetMLSMetadataVersion()) && String.Compare(m_engine.MlsMetadataVersion, m_connector.GetMLSMetadataVersion()) != 0)
        {
            addXMLNote(TCSException.MLS_METADATA_VERSION_NOT_MATCH, Tcs.Mls.Util.convertStringToXML(String.format(StringSupport.CSFmtStrToJFmtStr(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MLS_METADATA_VERSION_NOT_MATCH)),m_connector.GetMLSMetadataVersion(),m_engine.MlsMetadataVersion)));
            try
            {
                m_engine.runMainScript(net.toppro.components.mls.engine.MLSEngine.MSFLAG_METADATA_ONLY | net.toppro.components.mls.engine.MLSEngine.MSFLAG_DATAAGG_METADATA_ONLY);
                String path = Tcs.Mls.Util.getMetadataFolder() + m_connector.ModuleID + "_System.xml";
                File.Copy(m_engine.getResultsFolder() + "Metadata.xml", path, true);
                try
                {
                    MLSBoard mlsBoard = new MLSBoard();
                    mlsBoard.AddMLSMetadataFile(m_connector.ModuleID, m_engine.MlsMetadataVersion, path);
                }
                catch (Exception ex)
                {
                    m_connector.WriteLine("Failed to add MLS metadata to database table." + ex.getMessage());
                }

                parseMLSMetadataPropertyClass(path);
            }
            catch (Exception exc)
            {
                TCSException tcsExc = TCSException.produceTCSException(exc);
                addMetadataXMLNote(tcsExc.getErrorCode(), tcsExc.getErrorMessage());
                m_connector.WriteLine("Failed to download MLS metadata." + exc.getMessage());
            }
        
        }
         
    }

    public void parseMLSMetadataPropertyClass(String path) throws Exception {
        String data = Tcs.Mls.Util.loadXmlFile(path);
        String resource = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PROPERTYRESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "Property";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"PROPERTY"))
                if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"PropertyResource"))
                    if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"PROPERTYRESOURCE"))
                        addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing Property resource");
                     
                 
             
         
        resource = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_AGENTRESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "ActiveAgent";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"ACTIVEAGENT"))
                if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"Agent"))
                    if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"AGENT"))
                        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"User"))
                            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"USER"))
                                addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing Agent resource");
                             
                         
                     
                 
             
         
        resource = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_OFFICERESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "Office";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"OFFICE"))
                addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing Office resource");
             
         
        resource = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TCPIP, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_OPENHOUSERESOURCE);
        if (StringSupport.isNullOrEmpty(resource))
            resource = "OpenHouse";
         
        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,resource))
            if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"OPEN_HOUSE"))
                if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"open_house"))
                    if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"OPENHOUSE"))
                        if (!parseMLSMetadataPropertyClassCompactFormat(data,path,"openhouse"))
                            addMetadataXMLNote(TCSException.MLS_METADATA_NOT_FOUND, "Failed on parsing OpenHouse resource");
                         
                     
                 
             
         
        getMetadataFiles().add(new String[]{ "System", "System", "", "", path });
    }

    //else
    //    ParseMLSMetadataPropertyClassXmlFormat(data, path);
    public boolean parseMLSMetadataPropertyClassCompactFormat(String data, String path, String resource) throws Exception {
        XmlDocument doc = new XmlDocument();
        doc.PreserveWhitespace = true;
        doc.loadXml(data);
        XmlNode rets = doc.selectSingleNode("RETS");
        XmlNode prop = rets.selectSingleNode(".//METADATA-CLASS[@Resource='" + resource + "']");
        if (prop == null)
            return false;
         
        XmlNode columnNode = prop.selectSingleNode("./COLUMNS");
        if (columnNode == null)
            columnNode = prop.selectSingleNode("./Columns");
         
        String[] columns = StringSupport.Split(StringSupport.Trim(columnNode.getInnerXml()), '\t');
        int i = 0;
        getColumnDict().clear();
        for (i = 0;i < columns.length;i++)
        {
            getColumnDict().put(columns[i], i);
        }
        XmlNodeList classNodes = prop.selectNodes("./DATA");
        String[][] matrix = new String[classNodes.size(), i];
        int n = 0;
        for (Object __dummyForeachVar2 : classNodes)
        {
            XmlNode node = (XmlNode)__dummyForeachVar2;
            String[] classAttributes = StringSupport.Split(StringSupport.Trim(node.getInnerXml()), '\t');
            for (int m = 0;m < classAttributes.length;m++)
            {
                matrix[n, m] = classAttributes[m];
            }
            String xpath = ".//METADATA-TABLE[@Class='" + matrix[n, getColumnDict().get("ClassName")] + "']";
            XmlNode classNode = rets.selectSingleNode(xpath);
            if (classNode == null)
                continue;
             
            String filePath = path.Insert(path.length() - 4, "_" + resource + "_" + matrix[n, getColumnDict().get("ClassName")]).Replace("_System", "");
            Tcs.Mls.Util.writeFile(filePath,classNode.getOuterXml());
            getMetadataFiles().add(new String[]{ matrix[n, getColumnDict().get("ClassName")], matrix[n, getColumnDict().get("VisibleName")], matrix[n, getColumnDict().get("Description")], resource, filePath });
            n++;
        }
        return true;
    }

    public void parseMLSMetadataPropertyClassXmlFormat(String data, String path) throws Exception {
    }

    protected public void saveResult() throws Exception {
        if (this instanceof GetRecordCountDataagg || this instanceof GetRecordCountAgentRoster || this instanceof GetRecordCountOpenHouse)
        {
            super.saveResult();
            return ;
        }
         
        String resultPath = m_connector.getSearchTempFolder() + (m_connector.IsReturnDataAggListingsXml() ? "\\tcslistings.tcs" : "\\tcslistings.xml");
        if (ResultListingCount != 0)
        {
            if (!m_connector.IsReturnDataAggListingsXml())
            {
                if (m_useStreamWriter)
                {
                    m_resultStream.Write(m_resultBuffer.toString());
                }
                else
                {
                    ResultPath.Add(resultPath);
                    m_resultStream = new PrintWriter(new FileWriter(ResultPath[0].toString()), true);
                    m_resultStream.Write(m_resultBuffer.toString());
                } 
                m_resultStream.Close();
                m_resultStream = null;
            }
             
        }
         
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        StringBuilder result = new StringBuilder();
        if (!m_connector.IsReturnDataAggListingsXml())
        {
            result.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n");
        }
         
        if (!(this instanceof GetIDXListings))
        {
            result.append("<MLSRETSMetadata Version=\"");
            result.append(m_engine.MlsMetadataVersion);
            result.append("\"/>\r\n");
        }
         
        if (!m_connector.IsReturnDataAggListingsXml())
        {
            result.append("<TcsListingFiles>\r\n");
            for (int i = 0;i < ResultPath.Count;i++)
            {
                result.append("<File Path=\"" + ResultPath[i].toString() + "\"/>\r\n");
            }
            result.append("</TcsListingFiles>\r\n");
        }
         
        if (!(this instanceof GetIDXListings))
        {
            result.append("<RawDataFiles>\r\n");
            for (int i = 0;i < m_connector.RetsRawDataFiles.Count;i++)
            {
                result.append("<File Path=\"" + m_connector.RetsRawDataFiles[i].toString() + "\"/>\r\n");
            }
            result.append("</RawDataFiles>\r\n");
            result.append("<MLSMetadataFiles>\r\n");
            for (int i = 0;i < getMetadataFiles().size();i++)
            {
                String[] fileInfo = (String[])getMetadataFiles().get(i);
                result.append("<File ClassName=\"" + fileInfo[0] + "\" VisibleName=\"" + fileInfo[1] + "\" Description=\"" + fileInfo[2] + "\" Resource=\"" + fileInfo[3] + "\" Path=\"" + fileInfo[4] + "\"/>\r\n");
            }
            result.append("</MLSMetadataFiles>\r\n");
            result.append(addDaCount("Property"));
        }
         
        result.append("<Notes>\r\n" + m_resultNote.toString() + "</Notes>\r\n");
        if (!m_connector.IsReturnDataAggListingsXml())
        {
            result.append("</TCResult>");
            m_connector.saveTaskResult("", result.toString());
        }
        else
        {
            m_resultBuffer.Append(result.toString());
            m_resultBuffer.Append("</TCResult>");
            super.saveResult();
        } 
        if (MLSConnector.RUN_LOCAL > 0)
            File.WriteAllText("c:\\dataagg.xml", m_resultBuffer.toString());
         
        m_connector.WriteLine("TotalCount = " + ResultListingCount);
        m_connector.WriteLine("Save Task Result = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
    }

    protected String addDaCount(String countType) throws Exception {
        if (!m_connector.IsDACount())
            return "";
         
        String dataSourceID = m_connector.GetDataSourceID();
        if (this instanceof GetOfficeRoster)
            XMARTMLSNumbers.put("Office", getXMartMLSNumbers(dataSourceID,"12"));
        else if (this instanceof GetAgentRoster)
            XMARTMLSNumbers.put("Agent", getXMartMLSNumbers(dataSourceID,"11"));
        else
        {
            XMARTMLSNumbers.put("Residential", getXMartMLSNumbers(dataSourceID,"1"));
            XMARTMLSNumbers.put("Condo", getXMartMLSNumbers(dataSourceID,"2"));
            XMARTMLSNumbers.put("Mobile", getXMartMLSNumbers(dataSourceID,"3"));
            XMARTMLSNumbers.put("MultiFamily", getXMartMLSNumbers(dataSourceID,"4"));
            XMARTMLSNumbers.put("Farm", getXMartMLSNumbers(dataSourceID,"5"));
            XMARTMLSNumbers.put("Land", getXMartMLSNumbers(dataSourceID,"6"));
            XMARTMLSNumbers.put("Rental", getXMartMLSNumbers(dataSourceID,"7"));
        }  
        StringBuilder sb = new StringBuilder();
        StringBuilder sbMLSNumberFiles = new StringBuilder();
        sb.append("<DACount>\r\n");
        sbMLSNumberFiles.append("<DACountDifference>\r\n");
        for (Object __dummyForeachVar5 : TCSCount.entrySet())
        {
            Entry<String,Integer> pair = (Entry<String,Integer>)__dummyForeachVar5;
            sb.append("<PropertyType Name=\"" + pair.getKey() + "\" xMARTCounts=\"").append(getXMARTCount().get(pair.getKey()));
            sb.append("\" TCSCount=\"" + pair.getValue() + "\" Difference=\"").append(Math.abs(getXMARTCount().get(pair.getKey()) - pair.getValue()));
            sb.append("\" DifferencePercentage=\"");
            if (getXMARTCount().get(pair.getKey()) > 0 || pair.getValue() > 0)
                sb.append(((int)(Math.abs(getXMARTCount().get(pair.getKey()) - pair.getValue()))) * 100 / (getXMARTCount().get(pair.getKey()) > pair.getValue() ? getXMARTCount().get(pair.getKey()) : pair.getValue()));
            else
                sb.append("0"); 
            sb.append("\"/>\r\n");
            String filePath = m_connector.getSearchTempFolder() + "\\" + pair.getKey().replace(" ", "") + ".csv";
            boolean hasDifferences = false;
            PrintWriter sw = new PrintWriter(new FileWriter(filePath), true);
            try
            {
                {
                    HashSet<String> diffHashset = new HashSet<String>();
                    diffHashset.UnionWith(XMARTMLSNumbers.get(pair.getKey()));
                    diffHashset.UnionWith(TCSMLSNumbers.get(pair.getKey()));
                    diffHashset.ExceptWith(TCSMLSNumbers.get(pair.getKey()));
                    if (this instanceof GetOfficeRoster)
                        sw.println("OfficeID,In XMart,In TCS");
                    else if (this instanceof GetAgentRoster)
                        sw.println("AgentID,In XMart,In TCS");
                    else
                        sw.println("MLSNumber,In XMart,In TCS");  
                    for (Object __dummyForeachVar3 : diffHashset)
                    {
                        String item = (String)__dummyForeachVar3;
                        hasDifferences = true;
                        sw.println(item + ",1,0");
                    }
                    diffHashset.UnionWith(XMARTMLSNumbers.get(pair.getKey()));
                    diffHashset.UnionWith(TCSMLSNumbers.get(pair.getKey()));
                    diffHashset.ExceptWith(XMARTMLSNumbers.get(pair.getKey()));
                    for (Object __dummyForeachVar4 : diffHashset)
                    {
                        String item = (String)__dummyForeachVar4;
                        hasDifferences = true;
                        sw.println(item + ",0,1");
                    }
                }
            }
            finally
            {
                if (sw != null)
                    Disposable.mkDisposable(sw).dispose();
                 
            }
            if (hasDifferences)
                sbMLSNumberFiles.append("<File Path=\"" + filePath + "\"/>\r\n");
             
        }
        sb.append("</DACount>\r\n");
        sbMLSNumberFiles.append("</DACountDifference>\r\n");
        return sb.append(sbMLSNumberFiles).toString();
    }

    protected HashSet<String> getXMartMLSNumbers(String dataSourceID, String propertyTypeID) throws Exception {
        HashSet<String> hashSet = new HashSet<String>();
        CategorizationDal xmartMLSNumberDal = new CategorizationDal("DACount");
        IDataReader dr = xmartMLSNumberDal.SpDAListOfSourceIdsByPropTypeSel(dataSourceID, propertyTypeID);
        try
        {
            {
                while (dr.Read())
                {
                    hashSet.Add((String)dr[0]);
                }
            }
        }
        finally
        {
            if (dr != null)
                Disposable.mkDisposable(dr).dispose();
             
        }
        return hashSet;
    }

    public int[] getResultField() throws Exception {
        if (SelectPicFieldsOnly)
            return MLSCmaFields.ReturnIDsOnlyFieldList;
        else
            return Tcs.Mls.TCSStandardResultFields.getDataAggResultFields(); 
    }

    protected public String[] getRequiredField() throws Exception {
        return REQUIRED_FIELD;
    }

    protected public String getCompatibilityID() throws Exception {
        return COMPATIBILITY_ID;
    }

}


