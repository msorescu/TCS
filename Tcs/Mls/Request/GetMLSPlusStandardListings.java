//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:54 PM
//

package Tcs.Mls.Request;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DateTZ;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.GetRecordCountPlus;
import Tcs.Mls.Request.STRINGS;
import Tcs.Mls.Request.TPOnlineGetComparableListings;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class GetMLSPlusStandardListings  extends TPAppRequest 
{
    private String m_noteFields = "";
    private static final String COMPATIBILITY_ID = "3";
    public GetMLSPlusStandardListings(TCServer tcs) throws Exception {
        super(tcs);
    }

    protected public void getResult() throws Exception {
        getListings();
    }

    protected public void getListingsTPonline() throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        //string[] status = m_connector.getStatus();
        // string defStatus = "";
        if (m_engine.isMrisClient())
            m_connector.setOverallProgress(3);
         
        m_connector.setCurrentSearchNumber(m_currentSearch);
        String zipForLog = "";
        setLogMode();
        try
        {
            //processSearchFields();
            //if (m_defStatus[i] == null || m_defStatus[i].Length == 0)
            //    m_defStatus[i] = getSTStatus(status[i]);
            if (!m_engine.isDemoClient())
            {
                processSearchFieldsTP();
                m_engine.setPropertyFields(m_searchFields);
                String standardZip = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH, "ST_Zip");
                if (!StringSupport.isNullOrEmpty(standardZip))
                {
                    net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(standardZip);
                    if (pf != null)
                        zipForLog = pf.getValue();
                     
                }
                 
                try
                {
                    RequestLog requestLog = new RequestLog();
                    requestLog.UpdateRequestLog("<Input RequestId=\"" + Tcs.Mls.Util.GetMessageId(m_connector.getMessageHeader()) + "\" Zip=\"" + zipForLog + "\"/>");
                }
                catch (Exception excp)
                {
                }

                if (!m_engine.isFileImport())
                    CheckMLSRequiredSearchFeilds();
                 
                m_connector.updateTaskProgress(MLSConnector.PROGRESS_START_VALUE);
                m_connector.WriteLine("Get Listings before Run Main Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                m_engine.runMainScript(0);
                MLSCmaFields fields = m_engine.getCmaFields();
                m_records = m_engine.getMLSRecords(0x0FFF);
                MLSRecord rec = m_records.getFirstRecord();
                int n = m_connector.getMaxListingNumber();
                while (rec != null && n != 0)
                {
                    if (m_isSearchByMLSNumber)
                        removeMLSNumber(rec.getRecordID());
                     
                    rec.setChecked(true);
                    rec = m_records.getNextRecord();
                    n--;
                }
                //if (m_engine.isMrisClient())
                //{
                //    // m_connector.setCurrentSearchNumber(2);
                //    setStatus(m_status + "Pic");
                //    setLogMode();
                //    m_engine.runPicScript(m_engine.getMLSRecords(MLSRecords.FILTER_CHECKED));
                //}
                m_connector.WriteLine("Run Main Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
            }
            else
            {
                m_connector.updateTaskProgress(MLSConnector.PROGRESS_START_VALUE);
                m_engine.GetDemoRecordSet();
            } 
            m_connector.WriteLine("Start generating Output XML ");
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            getOutputXML();
            if (m_engine.isFileImport())
                m_connector.AddFilesToDelete(m_connector.getUploadFolder());
             
            m_requestSucess = true;
            m_connector.WriteLine("Generate Output String = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
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
                    int tcsCode = GetTCSErrorCode(mlsExc.getMessage());
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
            //TCSException exc = TCSException.produceTCSException(e);
            code = exc.getErrorCode();
            if (isRETSError(exc.getDeveloperMessage()))
            {
                throw TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MISCELLANEOUS_MLS_ERROR) + exc.getMessage(),"",TCSException.MISCELLANEOUS_MLS_ERROR);
            }
             
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            if (code < TCSException.INTERNAL_ERROR + 1)
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
                    throw exc;
                case TCSException.MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED: 
                    break;
                default: 
                    m_requestSucess = true;
                    break;
            
            }
            //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
            String errorMsg = exc.getMessage();
            if (code == TCSException.MLS_ERROR_NO_RECORD_FOUND)
                errorMsg = Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND);
             
            addXMLNote(exc.getErrorCode(), Tcs.Mls.Util.convertStringToXML(errorMsg));
        }
    
    }

    //checkResultSize();
    protected public void processSearchFieldsTP() throws Exception {
        if (m_engine.getPropertyFieldGroups().Length > 1)
            m_engine.setCurrentGroup(m_connector.getSearchByMLSNumber() ? 0 : 1);
        else
            m_engine.setCurrentGroup(0); 
        m_searchFields = Hashtable.Synchronized(new Hashtable());
        Hashtable ht = m_connector.getSearchFields();
        IEnumerator names;
        names = EnumeratorSupport.mk(ht.keySet().iterator());
        while (names.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            String field = ((String)names.getCurrent());
            String value = ((String)ht.get(field));
            net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(field);
            //if (value.Length > 0 && pf != null && pf.getControlType() == 9)
            //    value = getDateFormat(pf, value);
            m_searchFields[field] = value;
        }
        Hashtable ht1 = m_connector.getStandardSearchFields();
        names = EnumeratorSupport.mk(ht1.keySet().iterator());
        StringBuilder sb = new StringBuilder();
        while (names.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            String field = ((String)names.getCurrent());
            String mlsField = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH, field);
            String value = "";
            if (!StringSupport.isNullOrEmpty(mlsField))
                value = ((String)ht1.get(field));
             
            if (!StringSupport.isNullOrEmpty(value))
            {
                m_searchFields.Add(field, value);
                if (m_searchFields.ContainsKey(mlsField.toUpperCase()))
                {
                    m_searchFields.Remove(mlsField.toUpperCase());
                    sb.append(mlsField).append(", ");
                }
                 
            }
             
        }
        //m_searchFields[field] = value;
        if (sb.length() > 0)
        {
            sb.insert(0, STRINGS.get_Renamed(STRINGS.SEARCH_FIELD_DUPLICATE));
            addXMLNote(TCSException.SEARCH_FIELD_DUPLICATE, sb.toString().substring(0, (0) + ((sb.toString().lastIndexOf(",")) - (0))));
            m_warningNoteId = m_noteId - 1;
            m_isAddWarningNote = true;
        }
        else
            m_isAddWarningNote = false; 
        if (m_searchFields.ContainsKey("ST_Status"))
        {
            String status = (String)m_searchFields["ST_Status"];
            String mlsStatus = "";
            String delimiter = getStatusDelimiter();
            if (status.indexOf('A') > -1)
                mlsStatus = getSTStatus("A") + delimiter;
             
            if (status.indexOf('S') > -1)
                mlsStatus = getSTStatus("S") + delimiter;
             
            if (status.indexOf('P') > -1)
                mlsStatus = getSTStatus("P") + delimiter;
             
            if (status.indexOf('E') > -1)
                mlsStatus = getSTStatus("E") + delimiter;
             
            if (mlsStatus.endsWith(delimiter))
                mlsStatus = mlsStatus.substring(0, (0) + (mlsStatus.length() - delimiter.length()));
             
            m_searchFields["ST_Status"] = mlsStatus;
        }
         
        //if (m_isSearchByMLSNumber)
        //    m_searchFields[m_mlsNumberFieldName] = m_mlsNumber;
        SetRecordLimit();
    }

    protected public void getListings() throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        if (!m_connector.getDefFileVersion().equals("0") && String.Compare(m_connector.GetSystemPlusMetadataVersion(), m_connector.getMetadataVersion()) != 0)
        {
            addXMLNote(TCSException.METADATA_VERSION_NOT_MATCH, TCSException.getTCSMessage(TCSException.METADATA_VERSION_NOT_MATCH));
        }
         
        if (!isOverrideLimitSearch())
        {
            getListingsTPonline();
            return ;
        }
         
        String[] status = m_connector.getStandardStatus();
        String defStatus = "";
        m_connector.setOverAllSearchNumber(status.length * m_defList.ChildNodes.Count);
        m_engine.setCurrentGroup(m_isSearchByMLSNumber ? 0 : 1);
        int mlsSearchNotSuppliedCount = status.length;
        for (int i = 0;i < status.length;i++)
        {
            m_connector.setCurrentSearchNumber(m_currentSearch++);
            if (m_isSearchByMLSNumber)
            {
                if (!isDEFSupportedSearchField(STD_SEARCH_FIELD[ST_MLSNUMBER]))
                    throw TCSException.produceTCSException(MLSUtil.formatString(ParentSTRINGS.get_Renamed(ParentSTRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD), STD_SEARCH_FIELD[ST_MLSNUMBER], m_connector.getModuleName()), "", TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
                 
            }
            else
            {
                String defValue = "";
            } 
            //string[] reqField = getRequiredField();
            //for (int j = 0; j < reqField.Length; j++)
            //{
            //    if (!reqField[j].ToUpper().Equals(STD_SEARCH_FIELD[ST_PROPERTYTYPE].ToUpper()))
            //    {
            //        defValue = m_engine.getDefParser().getValue(MLSEngine.SECTION_AUTOSEARCH, reqField[j]);
            //        if (defValue == null || defValue.Length == 0)
            //            throw TCSException.produceTCSException(MLSUtil.formatString(ParentSTRINGS.get_Renamed(ParentSTRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD), reqField[j], m_connector.getModuleName()), "", TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
            //    }
            //}
            //System.Collections.Hashtable ht = m_connector.getStandardSearchFields();
            //string id = (string)ht[STD_SEARCH_FIELD[ST_AGENTID]];
            //if (id != null && id.Length > 0)
            //    if (!isDEFSupportedSearchField(STD_SEARCH_FIELD[ST_AGENTID]))
            //        throw TCSException.produceTCSException(MLSUtil.formatString(ParentSTRINGS.get_Renamed(ParentSTRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD), STD_SEARCH_FIELD[ST_AGENTID], m_connector.getModuleName()), "", TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
            //id = ((string)ht[STD_SEARCH_FIELD[ST_OFFICEID]]);
            //if (id != null && id.Length > 0)
            //    if (!isDEFSupportedSearchField(STD_SEARCH_FIELD[ST_OFFICEID]))
            //        TCSException.produceTCSException(MLSUtil.formatString(ParentSTRINGS.get_Renamed(ParentSTRINGS.NOT_SURPPORT_REQUEST_SEARCH_FIELD), STD_SEARCH_FIELD[ST_OFFICEID], m_connector.getModuleName()), "", TCSException.NOT_SURPPORT_REQUEST_SEARCH_FIELD);
            setStatus(status[i]);
            setLogMode();
            try
            {
                processSearchFields();
                //if (m_defStatus[i] == null || m_defStatus[i].Length == 0)
                //    m_defStatus[i] = getSTStatus(status[i]);
                setSearchPropertyField(getSTStatus(status[i]));
                CheckMLSRequiredSearchFeilds();
                if (m_tcs.isGetPicture())
                    m_connector.setOverallProgress(MLSConnector.OVERALL_PROGRESS_SEARCH_PREPARE_PICTURE);
                 
                m_connector.updateTaskProgress(MLSConnector.PROGRESS_START_VALUE);
                m_connector.WriteLine("Get Listings before Run Main Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                if (IsOverrideLimit && !m_isSearchByMLSNumber)
                {
                    m_overrideLimitCount = 1;
                    m_chunkingParameter = GetChunkingParameterInDef();
                    if (m_engine.isRETSClient() && m_chunkingParameter != null && m_chunkingParameter.Count > 0)
                        GetListingForOverrideLimtEx(GetSearchParameter(m_chunkingParameter[0]));
                    else
                        GetListingForOverrideLimt(GetSearchDate()); 
                }
                else
                {
                    if (!m_engine.isDemoClient())
                        m_engine.runMainScript(0);
                    else
                        m_engine.GetDemoRecordSet(); 
                    MLSCmaFields fields = m_engine.getCmaFields();
                    m_records = m_engine.getMLSRecords(0x0FFF);
                    MLSRecord rec = m_records.getFirstRecord();
                    if (rec == null && !(this instanceof GetRecordCountPlus))
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
                     
                    //SetAgentOfficeID();
                    m_connector.WriteLine("Run Main Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                    try
                    {
                        if (m_tcs.isGetPicture())
                        {
                            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                            m_engine.runPicScript(m_engine.getMLSRecords(MLSRecords.FILTER_CHECKED));
                            m_connector.WriteLine("\r\nRun Picture Script = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
                        }
                         
                    }
                    catch (Exception e)
                    {
                        TCSException exc = TCSException.produceTCSException(e);
                        //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                        addXMLNote(exc.getErrorCode(), exc.getMessage());
                    }

                    m_connector.WriteLine("Start generating Output XML ");
                    time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
                    getOutputXML();
                } 
                m_requestSucess = true;
                m_connector.WriteLine("Generate Output String = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
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
                        int tcsCode = GetTCSErrorCode(mlsExc.getMessage());
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
                    throw TCSException.produceTCSException(ParentSTRINGS.get_Renamed(ParentSTRINGS.MISCELLANEOUS_MLS_ERROR) + exc.getMessage(), "", TCSException.MISCELLANEOUS_MLS_ERROR);
                }
                 
                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                if (code < TCSException.INTERNAL_ERROR + 1)
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
                    case TCSException.EXCEED_MAX_MLSNO_SEARCH_NUMBER: 
                    case TCSException.CHUNKING_FIELD_NOTDEFINED: 
                    case TCSException.BOTH_STATUS_PUBLICSTATUS_USED: 
                    case TCSException.OFFMARKET_STATUS_NOT_DEFINED_INDEF: 
                    case TCSException.CHUNKING_FAILED: 
                        throw exc;
                    case TCSException.MINIMUM_MLS_SEARCH_PARAMETER_NOT_SUPPLIED: 
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
                    errorMsg = ParentSTRINGS.get_Renamed(ParentSTRINGS.NO_RECORD_FOUND);
                 
                addXMLNote(exc.getErrorCode(), Tcs.Mls.Util.convertStringToXML(errorMsg));
            }

            //CheckResultSize();
            if (m_isSearchByMLSNumber)
                break;
             
        }
    }

    private boolean isOverrideLimitSearch() throws Exception {
        //if (GetOverrideRecordLimit() > m_engine.getMaxRecordsLimit())
        //{
        Hashtable ht = m_connector.getStandardSearchFields();
        if (!StringSupport.isNullOrEmpty((String)ht.get(STD_SEARCH_FIELD[ST_STATUS])))
            return true;
         
        return false;
    }

    //if(!string.IsNullOrEmpty((string)ht[STD_SEARCH_FIELD[ST_STATUS]]) &&
    //    (!string.IsNullOrEmpty((string)ht[STD_SEARCH_FIELD[ST_LISTDATE]])
    //|| !string.IsNullOrEmpty((string)ht[STD_SEARCH_FIELD[ST_SALEDATE]])
    //|| !string.IsNullOrEmpty((string)ht[STD_SEARCH_FIELD[ST_STATUSDATE]])))
    //    return true;
    // }
    protected public void checkSearchFields() throws Exception {
        Hashtable ht = m_connector.getStandardSearchFields();
        String mlsNum = (String)ht.get(STD_SEARCH_FIELD[ST_MLSNUMBER]);
        m_isSearchByMLSNumber = (mlsNum != null && mlsNum.length() > 0);
        if (m_isSearchByMLSNumber || m_connector.IsSearchByMLSNumber())
        {
            m_isSearchByMLSNumber = true;
        }
         
        if (m_connector.IsSearchByMLSNumber())
        {
            ht = m_connector.getSearchFields();
            IEnumerator enumerator = EnumeratorSupport.mk(ht.keySet().iterator());
            boolean isFound = false;
            String mlsnumberFieldName = "";
            while (enumerator.moveNext())
            {
                mlsnumberFieldName = (String)enumerator.getCurrent();
                for (int i = 0;i < TPOnlineGetComparableListings.MLS_NUMBER_NAMES.length;i++)
                {
                    if (StringSupport.Compare(mlsnumberFieldName, TPOnlineGetComparableListings.MLS_NUMBER_NAMES[i], true) == 0)
                    {
                        isFound = true;
                        break;
                    }
                     
                }
                if (isFound)
                    break;
                 
            }
            if (StringSupport.isNullOrEmpty(mlsnumberFieldName))
                mlsnumberFieldName = "Mls";
             
            String mlsNumber = (String)ht.get(mlsnumberFieldName);
            if (StringSupport.isNullOrEmpty(mlsNum))
                mlsNum = mlsNumber;
             
        }
         
        if (m_isSearchByMLSNumber)
        {
            m_mlsNumber = mlsNum;
            m_connector.setSearchByMLSNumber(true);
        }
         
        super.CheckReturnMlsNumberOnlyFlag(m_connector.getStandardSearchFields());
    }

    protected public void processSearchFields() throws Exception {
        Hashtable ht = m_connector.getSearchFields();
        IEnumerator names = EnumeratorSupport.mk(ht.keySet().iterator());
        while (names.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            String fld = ((String)names.getCurrent());
            String value = ((String)ht.get(fld));
            if (StringSupport.isNullOrEmpty(value))
                continue;
             
            net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(fld);
            //if (value.Length > 0 && pf != null && pf.getControlType() == 9)
            //    value = getDateFormat(pf, value);
            m_searchFields[fld] = value;
        }
        //bool isSoldStatus = System.Convert.ToString("S").IndexOf(m_status) != - 1;
        boolean isSoldStatus = String.Compare("S", m_status, true) == 0;
        String field = "";
        String value_Renamed = "";
        String defValue = "";
        String iniValue = "";
        ht = m_connector.getStandardSearchFields();
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
            if (IsOverrideLimitWithoutDate())
            {
                if (StringSupport.isNullOrEmpty((String)ht.get(STD_SEARCH_FIELD[ST_LISTDATE])) && StringSupport.isNullOrEmpty((String)ht.get(STD_SEARCH_FIELD[ST_SALEDATE])))
                {
                    statusDate = Calendar.getInstance().getTime().AddYears(-10).ToUniversalTime().ToString("MM/dd/yyyy") + "-" + DateTimeSupport.ToString((new DateTZ(Calendar.getInstance().getTime().getTime(), TimeZone.getTimeZone("UTC"))), "MM/dd/yyyy");
                    ht.put(STD_SEARCH_FIELD[ST_STATUSDATE], statusDate);
                    addXMLNote(TCSException.OVERRIDERECLIMIT_DATENOTSUPPLIED, Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.OVERRIDERECLIMIT_DATENOTSUPPLIED));
                }
                 
            }
             
        }
         
        String searchPrice = (String)ht.get(STD_SEARCH_FIELD[ST_SEARCHPRICE]);
        if (searchPrice == null)
            searchPrice = "";
         
        boolean isSupportSearchPrice = m_engine.isSupportSearchPrice() && searchPrice.length() > 0;
        boolean isSupportStatusDate = m_engine.isSupportStatusDate() && statusDate.length() > 0;
        String postalCode = (String)ht.get(STD_SEARCH_FIELD[ST_ZIP]);
        ht = m_connector.getStandardSearchFields();
        names = EnumeratorSupport.mk(ht.keySet().iterator());
        StringBuilder sb = new StringBuilder();
        StringBuilder sbDupField = new StringBuilder();
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
             
            boolean isOverrideField = false;
            boolean isRemoveSTField = false;
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUSDATE].ToUpper()))
            {
                //if (isSupportStatusDate)
                //	value_Renamed = getDateFormat(STD_SEARCH_FIELD[ST_STATUSDATE], value_Renamed);
                isOverrideField = true;
                if (StringSupport.isNullOrEmpty(statusDate))
                    isRemoveSTField = true;
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTDATE].ToUpper()))
            {
                if (isSupportStatusDate)
                {
                    value_Renamed = "";
                }
                else if (statusDate.length() > 0)
                    if (!isSoldStatus)
                        value_Renamed = statusDate;
                    else
                        value_Renamed = ""; 
                  
                if (!isSupportStatusDate)
                {
                    if (StringSupport.isNullOrEmpty(statusDate) && StringSupport.isNullOrEmpty(value_Renamed))
                        isRemoveSTField = true;
                     
                }
                 
                isOverrideField = true;
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
                     
                  
                if (!isSupportStatusDate)
                {
                    if (StringSupport.isNullOrEmpty(statusDate) && StringSupport.isNullOrEmpty(value_Renamed) && isSoldStatus)
                        isRemoveSTField = true;
                     
                }
                 
                isOverrideField = true;
            }
             
            //if (defValue.Length > 0 && value_Renamed.Length > 0)
            //	value_Renamed = getDateFormat(STD_SEARCH_FIELD[ST_SALEDATE], value_Renamed);
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SEARCHPRICE].ToUpper()))
            {
                //Nothing need to do here
                isOverrideField = true;
                if (!isSupportSearchPrice)
                    value_Renamed = "";
                 
                if (StringSupport.isNullOrEmpty(searchPrice))
                    isRemoveSTField = true;
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LISTPRICE].ToUpper()))
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
                  
                if (!isSupportSearchPrice)
                {
                    if (StringSupport.isNullOrEmpty(searchPrice) && StringSupport.isNullOrEmpty(value_Renamed))
                        isRemoveSTField = true;
                     
                }
                 
                isOverrideField = true;
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_SALEPRICE].ToUpper()))
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
                   
                if (!isSupportSearchPrice)
                {
                    if (StringSupport.isNullOrEmpty(searchPrice) && StringSupport.isNullOrEmpty(value_Renamed) && isSoldStatus)
                        isRemoveSTField = true;
                     
                }
                 
                isOverrideField = true;
            }
             
            if (value_Renamed.length() == 0 && !isOverrideField)
            {
                m_searchFields.Remove(field);
                continue;
            }
             
            if (m_searchFields.Contains(defValue.toUpperCase()))
            {
                if (isRemoveSTField)
                {
                    m_searchFields.Remove(field);
                    continue;
                }
                else
                {
                    m_searchFields.Remove(defValue.toUpperCase());
                    sbDupField.append(defValue.toUpperCase()).append(", ");
                } 
            }
             
            net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(defValue);
            if (pf != null && !field.toUpperCase().equals(STD_SEARCH_FIELD[ST_STATUS].ToUpper()))
            {
                if (pf.getFormat().length() > 0)
                {
                    if (pf.getControlType() == 3 && value_Renamed.indexOf(",") > -1 && !value_Renamed.endsWith(","))
                        addXMLNote(TCSException.NOT_ALLOW_MULTIPLE_VALUES, ParentSTRINGS.get_Renamed(ParentSTRINGS.NOT_ALLOW_MULTIPLE_VALUES) + field);
                     
                    //value_Renamed = pf.getLookupCode(value_Renamed);
                    m_connector.WriteLine("Look Up Code = " + value_Renamed);
                }
                else
                {
                    if (!field.toUpperCase().equals("ST_PType".toUpperCase()) && (pf.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST || pf.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST_TYPEABLE))
                    {
                        value_Renamed = "";
                        addXMLNote(TCSException.LOOKUP_CODE_FORMAT_NOT_EXIST, ParentSTRINGS.get_Renamed(ParentSTRINGS.LOOKUP_CODE_FORMAT_NOT_EXIST) + field);
                    }
                     
                } 
            }
             
            /*if( field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_BEDS]) )
                            {
            				
                            }
            				
                            if( field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_FULLBATHS]) )
                            {
            				
                            }
            				
                            if( field.equalsIgnoreCase(STD_SEARCH_FIELD[ST_SQFT]) )
                            {
            				
                            }*/
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PROPERTYTYPE].ToUpper()))
            {
                value_Renamed = "";
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PostalFSA].ToUpper()))
            {
                if (!StringSupport.isNullOrEmpty(postalCode))
                    value_Renamed = "";
                 
            }
             
            if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LAT].ToUpper()) || field.toUpperCase().equals(STD_SEARCH_FIELD[ST_LONG].ToUpper()))
            {
                value_Renamed = "";
            }
             
            if (value_Renamed.length() > 0 && defValue.length() == 0)
            {
                if (field.toUpperCase().equals(STD_SEARCH_FIELD[ST_PostalFSA].ToUpper()))
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
            m_searchFields[field] = value_Renamed;
        }
        if (sb.length() > 0)
        {
            sb.insert(0, ParentSTRINGS.get_Renamed(ParentSTRINGS.NOT_SURPPORT_SEARCH_FIELD));
            addXMLNote(TCSException.NOT_SURPPORT_SEARCH_FIELD, sb.toString().substring(0, (0) + ((sb.toString().lastIndexOf(",")) - (0))));
            m_warningNoteId = m_noteId - 1;
            m_isAddWarningNote = true;
        }
        else
            m_isAddWarningNote = false; 
        if (sbDupField.length() > 0)
        {
            sbDupField.insert(0, STRINGS.get_Renamed(STRINGS.SEARCH_FIELD_DUPLICATE));
            addXMLNote(TCSException.SEARCH_FIELD_DUPLICATE, sbDupField.toString().substring(0, (0) + ((sbDupField.toString().lastIndexOf(",")) - (0))));
            m_warningNoteId = m_noteId - 1;
            m_isAddWarningNote = true;
        }
        else
            m_isAddWarningNote = false; 
        SetRecordLimit();
    }

    protected public void setSearchPropertyField(String status) throws Exception {
        try
        {
            m_searchFields[STD_SEARCH_FIELD[ST_STATUS]] = status;
            //m_searchFields["ST_PType"] = m_connector.getPropertySubType();
            m_engine.setPropertyFields(m_searchFields);
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
                ;
        }
    
    }

    //protected internal override void processSearchFields()
    //{
    //    if (m_engine.getPropertyFieldGroups().Length > 1)
    //        m_engine.setCurrentGroup(m_connector.IsSearchByMLSNumber() ? 0 : 1);
    //    else
    //        m_engine.setCurrentGroup(0);
    //    m_searchFields = System.Collections.Hashtable.Synchronized(new System.Collections.Hashtable());
    //    System.Collections.Hashtable ht = m_connector.getSearchFields();
    //    System.Collections.IEnumerator names;
    //    names = ht.Keys.GetEnumerator();
    //    System.Text.StringBuilder sb = new System.Text.StringBuilder();
    //    //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
    //    while (names.MoveNext())
    //    {
    //        //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
    //        string field = ((string)names.Current);
    //        string value = ((string)ht[field]);
    //        PropertyField pf = m_engine.getPropertyFields().getField(field);
    //        if (value.Length > 0 && pf != null && pf.getControlType() == 9)
    //            value = getDateFormat(pf, value);
    //        m_searchFields[field] = value;
    //    }
    //    Hashtable ht1 = m_connector.getStandardSearchFields();
    //    names = ht1.Keys.GetEnumerator();
    //    while (names.MoveNext())
    //    {
    //        //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
    //        string field = ((string)names.Current);
    //        string mlsField = m_engine.getDefParser().getValue(MLSEngine.SECTION_AUTOSEARCH, field);
    //        string value = "";
    //        if(!string.IsNullOrEmpty(mlsField))
    //            value = ((string)ht1[field]);
    //        if (!string.IsNullOrEmpty(value))
    //        {
    //            if (!m_searchFields.ContainsKey(mlsField.ToUpper()))
    //                m_searchFields.Add(field, value);
    //        }
    //        //m_searchFields[field] = value;
    //    }
    //    if (m_searchFields.ContainsKey("ST_Status"))
    //    {
    //        string status = (string) m_searchFields["ST_Status"];
    //        string mlsStatus = "";
    //        string delimiter = getStatusDelimiter();
    //        if (status.IndexOf('A') > -1)
    //            mlsStatus = getSTStatus("A") + delimiter;
    //        if (status.IndexOf('S') > -1)
    //            mlsStatus = getSTStatus("S") + delimiter;
    //        if (status.IndexOf('P') > -1)
    //            mlsStatus = getSTStatus("P") + delimiter;
    //        if (status.IndexOf('E') > -1)
    //            mlsStatus = getSTStatus("E") + delimiter;
    //        if (mlsStatus.EndsWith(delimiter))
    //            mlsStatus = mlsStatus.Substring(0, mlsStatus.Length - delimiter.Length);
    //        m_searchFields["ST_Status"] = mlsStatus;
    //    }
    //    //if (m_isSearchByMLSNumber)
    //    //    m_searchFields[m_mlsNumberFieldName] = m_mlsNumber;
    //    SetRecordLimit();
    //}
    protected public void getOutputXML() throws Exception {
        super.getOutputXML();
    }

    public int[] getResultField() throws Exception {
        return m_resultField;
    }

    protected public String getCompatibilityID() throws Exception {
        return COMPATIBILITY_ID;
    }

}


