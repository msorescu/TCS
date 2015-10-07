//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:54 PM
//

package Tcs.Mls.Request;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.JavaSupport.Collections.Generic.LCC.CollectionSupport;
import CS2JNet.JavaSupport.Collections.Generic.LCC.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.CSList;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DateTZ;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.TimeZone;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.GetAgentRoster;
import Tcs.Mls.Request.STRINGS;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;

public class GetOpenHouse  extends GetAgentRoster 
{
    private HashMap<String,CSList<String>> m_resultListByMLSNumber = new HashMap<String,CSList<String>>();
    private HashMap<String,String> m_prepertyIDToListingID = new HashMap<String,String>();
    private HashMap<String,Boolean> m_FoundPropertyID = new HashMap<String,Boolean>();
    private boolean m_stopSearchForListingID = false;
    private String m_searchTypeOpenHouse = "0";
    StringBuilder m_sbNotFoundListingIDS = new StringBuilder();
    public String getSearchTypeOpenHouse() throws Exception {
        return m_searchTypeOpenHouse;
    }

    public void setSearchTypeOpenHouse(String value) throws Exception {
        m_searchTypeOpenHouse = value;
    }

    public GetOpenHouse(TCServer tcs) throws Exception {
        super(tcs);
    }

    protected public void getResult() throws Exception {
        checkMetadataVersion();
        String openHouseClasses = m_engine.GetOpenHouseClasses();
        if (!StringSupport.isNullOrEmpty(openHouseClasses))
        {
            String[] classes = StringSupport.Split(openHouseClasses, ',');
            for (int i = 0;i < classes.length;i++)
            {
                m_engine.CurrentOpenHouseClass = classes[i];
                getListings();
                CurrentSearchNumber = CurrentSearchNumber - 1;
            }
        }
        else
        {
            getListings();
        } 
    }

    protected public void checkSearchFields() throws Exception {
        Hashtable ht = m_connector.getSearchFields();
        String mlsNum = (String)ht.get(STD_SEARCH_FIELD[ST_MLSNUMBER]);
        String searchDate = (String)ht.get(STD_SEARCH_FIELD[ST_SearchDate]);
        if (StringSupport.isNullOrEmpty(searchDate) && StringSupport.isNullOrEmpty(mlsNum))
        {
            throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED) + "ST_MLSNo or ST_SearchDate.","",TCSException.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED);
        }
         
    }

    protected public void processSearchFields() throws Exception {
        if (m_mlsSearchTimeZoneInfo == null)
            m_mlsSearchTimeZoneInfo = m_engine.GetMLSSearchTimeZone();
         
        if (m_mlsResultsTimeZoneInfo == null)
            m_mlsResultsTimeZoneInfo = m_engine.GetMLSResultsTimeZone();
         
        m_searchFields = new Hashtable();
        String field = "";
        String value_Renamed = "";
        String iniValue = "";
        String defValue = "";
        Hashtable ht = m_connector.getSearchFields();
        String mlsNum = (String)ht.get(STD_SEARCH_FIELD[ST_MLSNUMBER]);
        boolean isSearchByMLSNumber = (mlsNum != null && mlsNum.length() > 0);
        if (isSearchByMLSNumber)
        {
            if (!m_engine.isSupportOpenHouseMLSNoSearch())
                throw TCSException.produceTCSException(TCSException.OpenHouse_NotAvailableTo_MlsNumberSearch);
             
            m_mlsNumber = mlsNum;
            String[] mlsNumArray = StringSupport.Split(mlsNum, ',');
            if (mlsNumArray.length > MAX_MLS_NO_SEARCH)
                throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.EXCEED_MAX_MLSNO_SEARCH_NUMBER),"",TCSException.EXCEED_MAX_MLSNO_SEARCH_NUMBER);
             
        }
         
        if (!ht.containsKey(STD_SEARCH_FIELD[ST_SearchDate]))
            ht.put(STD_SEARCH_FIELD[ST_SearchDate], "");
         
        String searchDate = (String)ht.get(STD_SEARCH_FIELD[ST_SearchDate]);
        if (!isSearchByMLSNumber && StringSupport.isNullOrEmpty(searchDate))
        {
            searchDate = DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), "MM/dd/yyyyTHH:mm:ss") + "-" + Calendar.getInstance().getTime().AddYears(+1).ToUniversalTime().ToString("MM/dd/yyyyTHH:mm:ss");
            ht.put(STD_SEARCH_FIELD[ST_SearchDate], searchDate);
            addXMLNote(TCSException.OVERRIDERECLIMIT_DATENOTSUPPLIED, Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DATENOTSUPPLIED));
        }
         
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

    protected public void checkMLSRequiredSearchFeilds() throws Exception {
        return ;
    }

    protected public void getOutputXML() throws Exception {
        m_connector.WriteLine("GetOutPutXML");
        if (m_engine.IsDownloadIDs)
        {
            AddIDsForDownloadIDOnlySearch();
            return ;
        }
         
        boolean isDemo = m_engine.isDemoClient();
        boolean foundDemoListing = false;
        MLSCmaFields fields = null;
        fields = m_engine.getCmaFields();
        m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        //m_connector.WriteLine("MLS Rercords: Records=" + m_records.size());
        MLSRecord rec = m_records.getFirstRecord();
        String fieldValue = "";
        String fieldName = "";
        boolean bAddField = true;
        int recordCount = 0;
        setSearchTypeOpenHouse(m_engine.SearchType);
        if (StringSupport.isNullOrEmpty(getSearchTypeOpenHouse()))
            setSearchTypeOpenHouse("0");
         
        while (rec != null)
        {
            StringBuilder buffer = new StringBuilder();
            buffer.append("<Event>");
            String listingID = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_OH_MLSNUMBER);
            if (getSearchTypeOpenHouse().equals("21"))
                listingID = rec.getFieldValue("PropertyID");
             
            String[] fl = new String[]{ "" };
            String dataStandardNoteId = "";
            for (int i = 0;i < getResultField().length;i++)
            {
                fieldValue = "";
                fieldName = Tcs.Mls.TCSStandardResultFields.getXmlName(getResultField()[i]);
                //]field.getName();
                bAddField = true;
                String mlsValue = "";
                switch(getResultField()[i])
                {
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_MLSNUMBER: 
                        continue;
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_ATTENDEDYN: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_REFRESHMENTYN: 
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
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_STARTDATETIME: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_ENDDATETIME: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_STARTDATETIME2: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_ENDDATETIME2: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_STARTDATETIME3: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_ENDDATETIME3: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        if (fieldValue.indexOf('T') < 0)
                            fieldValue = MLSUtil.FormatDate(STANDARD_DATEFORMAT, STANDARD_DATETIMEFORMAT, fieldValue);
                         
                        break;
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_DATE: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_DATE2: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_OH_DATE3: 
                        //if (fieldValue.IndexOf("00:00:00") > -1)
                        //    fieldValue = fieldValue.Replace("00:00:00", "23:59:59");
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        if (!Tcs.Mls.Util.isValidTCSDateFormat(fieldValue))
                            fieldValue = "";
                         
                        break;
                    default: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        break;
                
                }
                if (bAddField && !(StringSupport.isNullOrEmpty(fieldValue)))
                    buffer.append("<" + fieldName + ">" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.filterJunkValue(fieldValue)) + "</" + fieldName + ">");
                 
            }
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
              
            if (!StringSupport.isNullOrEmpty(footNote))
                buffer.append("<NoteID>").append(footNote + "</NoteID>");
             
            buffer.append("</Event>\r\n");
            if (m_resultListByMLSNumber.containsKey(listingID))
                m_resultListByMLSNumber.get(listingID).add(buffer.toString());
            else
                m_resultListByMLSNumber.put(listingID, new CSList<String>()); 
            rec = m_records.getNextRecord();
            recordCount++;
            m_overrideLimitCount++;
            if (IsOverrideLimit && m_overrideLimitCount > GetOverrideRecordLimit())
                break;
             
            CheckResultSize();
        }
    }

    protected public void addXMLHeader() throws Exception {
        if (m_connector.IsReturnDataAggListingsXml())
        {
            m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n<OpenHouses>\r\n");
        }
        else
            m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult>\r\n<OpenHouses>\r\n"); 
    }

    protected public void addXMLFooter() throws Exception {
        generateResult();
        //if (m_sbNotFoundListingIDS.Length > 0)
        //    addXMLNote(TCSException.OpenHouse_MLSNumber_NotFound, TCSException.getTCSMessage(TCSException.OpenHouse_MLSNumber_NotFound) + m_sbNotFoundListingIDS.ToString().Trim());
        if (!m_connector.IsReturnDataAggListingsXml())
        {
            m_resultBuffer.Append("</OpenHouses>\r\n");
            addNotes();
            m_resultBuffer.Append("</TCResult>\r\n");
        }
        else
        {
            m_resultBuffer.Append("</OpenHouses>\r\n");
            addNotes();
        } 
    }

    protected public void generateResult() throws Exception {
        //MLSConnector.RUN_LOCAL = 0;
        if (getSearchTypeOpenHouse().equals("21"))
            getListingNumberFromPropertyClass();
         
        ResultListingCount = 0;
        String listingID = "";
        for (Object __dummyForeachVar0 : m_resultListByMLSNumber.entrySet())
        {
            Entry<String,CSList<String>> pair = (Entry<String,CSList<String>>)__dummyForeachVar0;
            if (getSearchTypeOpenHouse().equals("21"))
            {
                listingID = "";
                if (m_prepertyIDToListingID.containsKey(pair.getKey()))
                    listingID = m_prepertyIDToListingID.get(pair.getKey());
                 
                if (StringSupport.isNullOrEmpty(listingID))
                {
                    m_sbNotFoundListingIDS.append(pair.getKey() + ",");
                    continue;
                }
                 
            }
             
            m_resultBuffer.Append("<OpenHouse>\r\n<OH_MLSNumber>");
            if (getSearchTypeOpenHouse().equals("21"))
                m_resultBuffer.Append(listingID);
            else
                m_resultBuffer.Append(pair.getKey()); 
            m_resultBuffer.Append("</OH_MLSNumber><Events>");
            for (int i = 0;i < pair.getValue().size();i++)
            {
                m_resultBuffer.Append(pair.getValue().get(i));
            }
            m_resultBuffer.Append("</Events></OpenHouse>");
            CheckResultSize();
        }
    }

    private void getListingNumberFromPropertyClass() throws Exception {
        m_propertyDefList = m_connector.getDEFList(getCompatibilityID());
        for (int i = 0;i < m_propertyDefList.ChildNodes.Count;i++)
        {
            if (m_stopSearchForListingID)
                break;
             
            String defPath = m_propertyDefList.ChildNodes[i].Attributes[MLSConnector.ATTRIBUTE_DEFINITION_FILE].Value;
            if (defPath.toLowerCase().endsWith(".sql"))
                continue;
             
            m_engine = m_connector.createEngine(m_propertyDefList.ChildNodes[i], this);
            String defNotAvailableTo = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_COMMON, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DEFNOTAVAILABLETO);
            boolean isNotAvailableTo = false;
            if (!StringSupport.isNullOrEmpty(defNotAvailableTo))
            {
                String[] notAvailables = StringSupport.Split(defNotAvailableTo, ',');
                for (int x = 0;x < notAvailables.length;x++)
                {
                    if (String.Compare(StringSupport.Trim(notAvailables[x]), m_connector.GetClientName().Trim(), StringComparison.CurrentCultureIgnoreCase) == 0)
                    {
                        isNotAvailableTo = true;
                        break;
                    }
                     
                }
            }
             
            //Todo, add a warning message;
            if (isNotAvailableTo)
                continue;
             
            setLoginInfo();
            m_status = "A";
            m_engine.setCurrentGroup(m_engine.getPropertyFieldGroups().Length > 1 ? 1 : 0);
            downloadDataByIDList();
        }
    }

    protected void downloadDataByIDList() throws Exception {
        IEnumerator<String> en = new EnumeratorSupport<T>(CollectionSupport.mk(m_resultListByMLSNumber.keySet()).iterator());
        int limit = m_engine.SearchIDsLimit;
        String id = "";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (en.moveNext())
        {
            id = en.getCurrent();
            if (!m_FoundPropertyID.containsKey(id))
                m_FoundPropertyID.put(id, false);
             
            if (!m_FoundPropertyID.get(id))
            {
                sb.append(id).append(",");
                i++;
            }
             
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
        else
        {
            //Last chunk
            m_stopSearchForListingID = true;
        } 
    }

    protected void runMainscriptToDownloadByIDs(StringBuilder sbIDs) throws Exception {
        String ids = sbIDs.delete(sbIDs.length() - 1, (sbIDs.length() - 1)+(1)).toString();
        Hashtable ht = new Hashtable();
        ht.put("ReferencedPropertyID", ids);
        m_engine.setPropertyFields(ht);
        int retry = 3;
        LableRetry:try
        {
            m_engine.setCurrentSearchStatus(m_status + (Environment.TickCount & Integer.MAX_VALUE).toString());
            m_engine.runMainScript(0);
        }
        catch (Exception exc)
        {
            if (retry > 0)
            {
                retry--;
                m_connector.WriteLine("One chunk failed. Retry...");
                goto LableRetry
            }
             
        }

        m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        MLSRecord rec = m_records.getFirstRecord();
        String propertyID = "";
        String listingID = "";
        while (rec != null)
        {
            //propertyID = rec.getFieldValue("ReferencedPropertyID");
            propertyID = rec.getFieldValue("ReferencedPropertyID");
            listingID = rec.getFieldValue("RecordID");
            m_prepertyIDToListingID.put(propertyID, listingID);
            m_FoundPropertyID.put(propertyID, true);
            rec = m_records.getNextRecord();
        }
    }

    public int[] getResultField() throws Exception {
        return Tcs.Mls.TCSStandardResultFields.getOpenHouseResultFields();
    }

    protected public String[] getRequiredField() throws Exception {
        return REQUIRED_FIELD;
    }

}


