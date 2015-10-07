//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:57 PM
//

package Tcs.Mls.Request;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.JavaSupport.language.RefSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DoubleSupport;
import CS2JNet.System.StringSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.StandardStreetName;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class TPOnlineGetComparableListings  extends TPAppRequest 
{
    //UPGRADE_NOTE: Final was removed from the declaration of 'm_searchField '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    protected public static final String[] m_searchField = new String[]{ STD_SEARCH_FIELD[ST_MLSNUMBER], STD_SEARCH_FIELD[ST_PROPERTYSUBTYPE], STD_SEARCH_FIELD[ST_STATUS], STD_SEARCH_FIELD[ST_ZIP], STD_SEARCH_FIELD[ST_BEDS], STD_SEARCH_FIELD[ST_FULLBATHS], STD_SEARCH_FIELD[ST_SQFT], STD_SEARCH_FIELD[ST_LISTDATE], STD_SEARCH_FIELD[ST_LISTPRICE], STD_SEARCH_FIELD[ST_SALEDATE], STD_SEARCH_FIELD[ST_SALEPRICE], STD_SEARCH_FIELD[ST_SEARCHPRICE], STD_SEARCH_FIELD[ST_STATUSDATE] };
    //UPGRADE_NOTE: Final was removed from the declaration of 'REQUIRED_FIELD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] REQUIRED_FIELD = new String[]{ STD_SEARCH_FIELD[ST_STATUS], STD_SEARCH_FIELD[ST_PROPERTYTYPE] };
    private static final String COMPATIBILITY_ID = "8";
    protected String m_mlsNumberFieldName = "Mls";
    public static final String[] MLS_NUMBER_NAMES = new String[]{ "MLSNumbers", "MLS", "MLSNos", "MLSNo", "MLS_Number", "MLSNumber", "IndvMLS", "MLS_Numbers", "List_Number", "List_Numbers", "ClientID" };
    public TPOnlineGetComparableListings(TCServer tcs) throws Exception {
        super(tcs);
    }

    protected public void checkSearchFields() throws Exception {
        m_isSearchByMLSNumber = m_connector.IsSearchByMLSNumber();
        m_connector.setSearchByMLSNumber(m_isSearchByMLSNumber);
        if (m_isSearchByMLSNumber)
        {
            Hashtable ht = m_connector.getSearchFields();
            IEnumerator enumerator = EnumeratorSupport.mk(ht.keySet().iterator());
            boolean isFound = false;
            while (enumerator.moveNext())
            {
                m_mlsNumberFieldName = (String)enumerator.getCurrent();
                for (int i = 0;i < MLS_NUMBER_NAMES.length;i++)
                {
                    if (StringSupport.Compare(m_mlsNumberFieldName, MLS_NUMBER_NAMES[i], true) == 0)
                    {
                        isFound = true;
                        break;
                    }
                     
                }
                if (isFound)
                    break;
                 
            }
            if (StringSupport.isNullOrEmpty(m_mlsNumberFieldName))
                m_mlsNumberFieldName = "Mls";
             
            m_mlsNumber = (String)ht.get(m_mlsNumberFieldName);
            if (String.IsNullOrEmpty(m_mlsNumber))
                m_mlsNumber = "";
             
            return ;
        }
         
    }

    protected public void getResult() throws Exception {
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
                processSearchFields();
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
    protected public void processSearchFields() throws Exception {
        if (m_engine.getPropertyFieldGroups().Length > 1)
            m_engine.setCurrentGroup(m_connector.IsSearchByMLSNumber() ? 0 : 1);
        else
            m_engine.setCurrentGroup(0); 
        m_searchFields = Hashtable.Synchronized(new Hashtable());
        Hashtable ht = m_connector.getSearchFields();
        IEnumerator names;
        names = EnumeratorSupport.mk(ht.keySet().iterator());
        StringBuilder sb = new StringBuilder();
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
        if (m_isSearchByMLSNumber)
        {
            net.toppro.components.mls.engine.PropertyField pf1 = m_engine.getPropertyFields().getField(m_mlsNumberFieldName);
            m_mlsNumber = RemoveSpace(m_mlsNumber, pf1.getDelimiter());
            m_searchFields[m_mlsNumberFieldName] = m_mlsNumber;
        }
         
        SetRecordLimit();
    }

    protected public void getOutputXML() throws Exception {
        m_connector.WriteLine("GetOutPutXML");
        StringBuilder sb = new StringBuilder();
        boolean isDemo = m_engine.isDemoClient();
        boolean isSearchByMLSNumber = m_connector.IsSearchByMLSNumber();
        int[] randomNumberArray = new int[0];
        MLSCmaFields fields = null;
        fields = m_engine.getCmaFields();
        CheckNormFieldDecoupleDataFlag(fields);
        boolean addTPOState = fields.getStdField(Tcs.Mls.TCSStandardResultFields.STDF_TPOSTATE) == null;
        boolean addYearBuildSTDF = fields.getField("YearBuilt_STDF") == null;
        m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        if (isDemo)
        {
            if (!isSearchByMLSNumber)
            {
                int max = m_engine.MaxDemoRecords;
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
                randomNumberArray = CreateRandom(m_records.size() > max ? max : m_records.size(), m_records.size());
            }
            else
                randomNumberArray = CreateRandom(1, m_records.size()); 
        }
         
        MLSRecord rec = m_records.getFirstRecord();
        String fieldValue = "";
        String fieldName = "";
        boolean bAddField = true;
        boolean isFileImport = m_engine.isFileImport();
        int recordCount = 0;
        boolean foundDemoListing = false;
        while (rec != null)
        {
            if (isDemo)
            {
                if (isSearchByMLSNumber)
                {
                    if (m_mlsNumber.IndexOf(rec.getRecordID(), 0, StringComparison.CurrentCultureIgnoreCase) < 0)
                    {
                        rec = m_records.getNextRecord();
                        continue;
                    }
                    else
                        foundDemoListing = true; 
                }
                else if (!isExistInArray(randomNumberArray,recordCount))
                {
                    rec = m_records.getNextRecord();
                    recordCount++;
                    continue;
                }
                  
            }
             
            String demoListDate = "";
            String saleDate = "";
            String statusDate = "";
            String listPrice = "";
            String salePrice = "";
            String searchPrice = "";
            String bathRooms = "";
            String sqft = "";
            m_resultBuffer.Append("<Listing");
            String[] fl = new String[]{ "" };
            String dataStandardNoteId = "";
            String statusSTDFNorm = "";
            if (NormDecoupleData[(int)NormFields.CMAIDENTIFIER] == -10)
                statusSTDFNorm = getStandardStatus(rec.getMLSRecordType());
            else
            {
                statusSTDFNorm = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAIDENTIFIERNORM,true);
                if (!statusSTDFNorm.equals("A") && !statusSTDFNorm.equals("S") && !statusSTDFNorm.equals("P") && !statusSTDFNorm.equals("E"))
                    statusSTDFNorm = "";
                 
            } 
            m_resultBuffer.Append(" Status_STDF=\"" + statusSTDFNorm + "\"");
            for (int i = 0;i < fields.size();i++)
            {
                fieldValue = "";
                net.toppro.components.mls.engine.CmaField field = fields.getField(i);
                // fieldName = HEADER_COLUMNS[getResultField()[i]]; //]field.getName();
                bAddField = true;
                String mlsValue = "";
                fieldValue = rec.getFieldValue(i);
                // getTCSStdFieldValue(getResultField()[i], STD_FIELD_ID[getResultField()[i]][1] == STANDARD);
                switch(field.getFieldType())
                {
                    case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_NUMBER: 
                        double num;
                        RefSupport<Double> refVar___0 = new RefSupport<Double>();
                        fieldValue = DoubleSupport.tryParse(fieldValue, refVar___0) ? fieldValue : "";
                        num = refVar___0.getValue();
                        break;
                    case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_CURRENCY: 
                        double num1;
                        boolean boolVar___0 = DoubleSupport.tryParse(fieldValue, refVar___1);
                        if (boolVar___0)
                        {
                            RefSupport<Double> refVar___1 = new RefSupport<Double>();
                            fieldValue = String.format(StringSupport.CSFmtStrToJFmtStr("{0:0}"),num1);
                            num1 = refVar___1.getValue();
                        }
                        else
                            fieldValue = ""; 
                        break;
                    case net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATE: 
                        fieldValue = MLSUtil.formatDate("","MM/DD/YYYY",fieldValue);
                        break;
                
                }
                if (fieldValue == null)
                    fieldValue = "";
                 
                String caption = field.getCaption();
                if (caption == null)
                    caption = "";
                 
                switch(field.getStdId())
                {
                    case Tcs.Mls.TCSStandardResultFields.STDF_STDFSTREETTYPE: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_STDFSTREETDIRPREFIX: 
                    case Tcs.Mls.TCSStandardResultFields.STDF_STDFSTREETDIRSUFFIX: 
                        String[] str;
                        if (field.getStdId() == Tcs.Mls.TCSStandardResultFields.STDF_STDFSTREETTYPE)
                        {
                            str = StandardStreetName.getStreetType(fieldValue);
                        }
                        else
                        {
                            str = StandardStreetName.getStreetDirection(fieldValue);
                        } 
                        if (str == null)
                            str = new String[]{ "", "" };
                         
                        m_resultBuffer.Append(" " + field.getName() + "LONG=\"" + Tcs.Mls.Util.convertStringToXML(str[0]) + "\"");
                        m_resultBuffer.Append(" " + field.getName() + "SHORT=\"" + Tcs.Mls.Util.convertStringToXML(str[1]) + "\"");
                        continue;
                    case Tcs.Mls.TCSStandardResultFields.STDF_STDFSTREETNAMEPARSED: 
                        m_resultBuffer.Append(" " + field.getName() + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\"");
                        continue;
                    case Tcs.Mls.TCSStandardResultFields.STDF_CMAFEATURE: 
                        if (m_connector.IsRemoveRoomDimFromFeatures())
                        {
                            String prefix;
                            try
                            {
                                fl = rec.getFeatureNoRoomDimension(true);
                                if (fl != null)
                                {
                                    fieldValue = "";
                                    for (int j = 0;j < fl.length;j++)
                                    {
                                        if (fl[j] != null && fl[j].length() > 0 && fl[j].indexOf(":") > -1)
                                        {
                                            if (!StringSupport.isNullOrEmpty(fieldValue))
                                                fieldValue = fieldValue + ",  " + fl[j].replace(",", ", ");
                                            else
                                                fieldValue = fl[j].replace(",", ", "); 
                                        }
                                         
                                    }
                                }
                                 
                            }
                            catch (Exception e)
                            {
                                //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                                m_connector.WriteLine("Error on retrieving features" + e.getMessage());
                            }
                        
                        }
                         
                        m_resultBuffer.Append(" " + field.getName() + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\"");
                        continue;
                
                }
                if (!isDemo)
                {
                    if (caption.length() > 0 || MLSCmaFields.isCMAFeild(field.getStdId()))
                    {
                        if (field.getStdId() == Tcs.Mls.TCSStandardResultFields.STDF_TPOSTATE)
                        {
                            fieldValue = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_TPOSTATE,true);
                            m_resultBuffer.Append(" " + field.getName() + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\"");
                        }
                        else
                            m_resultBuffer.Append(" " + field.getName() + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\""); 
                    }
                     
                }
                else
                {
                    String searchValue = "";
                    if (caption.length() > 0 || MLSCmaFields.isCMAFeild(field.getStdId()))
                    {
                        try
                        {
                            switch(field.getStdId())
                            {
                                case Tcs.Mls.TCSStandardResultFields.STDF_CMASQUAREFEET: 
                                    if (StringSupport.isNullOrEmpty(fieldValue))
                                        fieldValue = ((Integer.valueOf(Tcs.Mls.Util.getRandomNumber("1000-10000")) / 10) * 10) + "";
                                     
                                        ;
                                    break;
                                case Tcs.Mls.TCSStandardResultFields.STDF_CMALISTINGDATE: 
                                    fieldValue = getDemoListingDate();
                                    String searchListDate = GetSearchValue(ST_LISTDATE);
                                    String searchStatusDate = GetSearchValue(ST_STATUSDATE);
                                    String searchSaleDate = GetSearchValue(ST_SALEDATE);
                                    if (!StringSupport.isNullOrEmpty(searchStatusDate))
                                    {
                                        statusDate = GetDemoDate(searchStatusDate);
                                        Tcs.Mls.Request.TPOnlineGetComparableListings.APPLY __dummyScrutVar4 = getStandardStatus(rec.getMLSRecordType());
                                        if (__dummyScrutVar4.equals("A"))
                                        {
                                            demoListDate = statusDate;
                                            saleDate = "";
                                        }
                                        else if (__dummyScrutVar4.equals("E") || __dummyScrutVar4.equals("P"))
                                        {
                                            demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(statusDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                            saleDate = "";
                                        }
                                        else if (__dummyScrutVar4.equals("S"))
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
                                        Tcs.Mls.Request.TPOnlineGetComparableListings.APPLY __dummyScrutVar5 = getStandardStatus(rec.getMLSRecordType());
                                        if (__dummyScrutVar5.equals("A"))
                                        {
                                            statusDate = demoListDate;
                                            saleDate = "";
                                        }
                                        else if (__dummyScrutVar5.equals("E") || __dummyScrutVar5.equals("P"))
                                        {
                                            statusDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.parse(demoListDate), "MM/dd/yyyy"),60);
                                            saleDate = "";
                                        }
                                        else if (__dummyScrutVar5.equals("S"))
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
                            
                            }
                        }
                        catch (Exception exc)
                        {
                            System.out.println("Error" + exc.getMessage());
                        }

                        m_resultBuffer.Append(" " + field.getName() + "=\"" + Tcs.Mls.Util.convertStringToXML(fieldValue != null ? fieldValue : "") + "\"");
                    }
                     
                } 
            }
            //m_resultBuffer.Append(" CMAFEATURE=\"" + Util.convertStringToXML(rec.getStdFieldValue(TCSStandardResultFields.STDF_CMAFEATURE, false)) + "\"");
            //m_resultBuffer.Append(" NOTES=\"" + Util.convertStringToXML(rec.getStdFieldValue(MLSCmaFields.STDF_NOTES, false)) + "\"");
            m_resultBuffer.Append(" PicID=\"" + Tcs.Mls.Util.convertStringToXML(rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_PICID,false)) + "\"");
            if (addTPOState)
                m_resultBuffer.Append(" TPOSTATE=\"" + Tcs.Mls.Util.convertStringToXML(rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_TPOSTATE,true)) + "\"");
             
            if (addYearBuildSTDF)
            {
                String yearBuildNorm = "";
                if (NormDecoupleData[(int)NormFields.CMAAGE] == -10)
                {
                    yearBuildNorm = rec.getStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGE,false);
                    yearBuildNorm = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM,yearBuildNorm);
                }
                else
                {
                    yearBuildNorm = rec.getTCSStdFieldValue(Tcs.Mls.TCSStandardResultFields.STDF_CMAAGENORM,true);
                    yearBuildNorm = ConvertToInt(yearBuildNorm);
                    if (yearBuildNorm.length() != 4)
                        yearBuildNorm = "";
                     
                } 
                m_resultBuffer.Append(" YearBuilt_STDF=\"" + Tcs.Mls.Util.convertStringToXML(yearBuildNorm) + "\"");
            }
             
            m_resultBuffer.Append(">\r\n");
            //Todo: Add Roomdimension for 8i
            m_resultBuffer.Append("<RoomDimensions>\r\n");
            m_resultBuffer.Append(rec.getRoomDimensions(false,true,false));
            //fl = rec.getSTDFFieldList(TCSStandardResultFields.STDF_STDFROOMDIM, null, true);
            //try
            //{
            //    if (fl != null && fl.Length > 0)
            //    {
            //        string prefix = "";
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
            m_resultBuffer.Append("</RoomDimensions>\r\n");
            if (isFileImport || m_connector.getInputPictureFlag() == FLAG_MULTIPLE_PICTURE || m_connector.getInputPictureFlag() == FLAG_ONE_PICTURE)
            {
                m_tcs.NeedDeleteTempFolder = false;
                String picFileName = "";
                String objectId = "";
                if (m_engine.getMulitiPictures().isGetMultiPicture())
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
                            m_resultBuffer.Append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?message_header=" + m_connector.getMessageHeader() + "&object_id=" + objectId) + "\"");
                            m_resultBuffer.Append("/>\r\n");
                        }
                    }
                     
                }
                else
                {
                    picFileName = rec.getPictureFileName();
                    if (picFileName != null && picFileName.length() > 0)
                    {
                        int pos1 = picFileName.lastIndexOf("\\");
                        int pos2 = picFileName.lastIndexOf(".");
                        objectId = picFileName.Substring(pos1 + 1, (pos2)-(pos1 + 1));
                        m_connector.SaveTaskFileResult(objectId, picFileName);
                        m_resultBuffer.Append("<Picture");
                        //sb.append( " Url=" + "\"" + picFileName + "\"" );
                        m_resultBuffer.Append(" Url=\"" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.getPicUrl() + "getpicture.asp?message_header=" + m_connector.getMessageHeader() + "&object_id=" + objectId) + "\"");
                        m_resultBuffer.Append("/>\r\n");
                    }
                     
                } 
            }
             
            m_resultBuffer.Append("</Listing>\r\n");
            rec = m_records.getNextRecord();
            recordCount++;
            CheckResultSize();
        }
        if (isDemo && isSearchByMLSNumber && !foundDemoListing)
            addXMLNote(TCSException.MLS_ERROR_NO_RECORD_FOUND, Tcs.Mls.Util.convertStringToXML(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.NO_RECORD_FOUND)));
         
    }

    private int getFilterFlag() throws Exception {
        int flag = 0;
        String searchStatus = GetSearchValue(ST_STATUS);
        String[] searchStatusArray = StringSupport.Split(searchStatus, ',');
        boolean[] boolStatus = new boolean[4];
        for (int i = 0;i < STANDERD_STATUS.Length;i++)
        {
            String[] sortStatus = getStatusFromSorting(STANDERD_STATUS[i]).Split(',');
            for (int j = 0;j < searchStatusArray.length;j++)
            {
                for (int n = 0;n < sortStatus.length;n++)
                {
                    if (StringSupport.isNullOrEmpty(sortStatus[n]))
                        continue;
                     
                    if (searchStatusArray[j].indexOf(sortStatus[n]) > -1)
                    {
                        boolStatus[i] = true;
                        break;
                    }
                     
                }
            }
        }
        if (boolStatus[0])
            flag |= MLSRecords.FILTER_ACTIVE;
         
        if (boolStatus[1])
            flag |= MLSRecords.FILTER_SOLD;
         
        if (boolStatus[2])
            flag |= MLSRecords.FILTER_PENDING;
         
        if (boolStatus[3])
            flag |= MLSRecords.FILTER_EXPIRED;
         
        return flag;
    }

    private String getSearchValue(int stField) throws Exception {
        String fname = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH, STD_SEARCH_FIELD[stField]);
        if (StringSupport.isNullOrEmpty(fname))
            return "";
         
        net.toppro.components.mls.engine.PropertyField pf = m_engine.getPropertyFields().getField(fname);
        if (pf != null)
            return pf.getValue();
         
        return "";
    }

    protected boolean isMatchSearchCriteria(int stField, String fieldValue) throws Exception {
        String searchValue = getSearchValue(stField);
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

    private String getDemoListingDate() throws Exception {
        String demoListDate = GetSearchValue(ST_LISTDATE);
        if (StringSupport.isNullOrEmpty(demoListDate))
            demoListDate = GetSearchValue(ST_STATUSDATE);
         
        if (demoListDate == null)
            demoListDate = "";
         
        if (demoListDate.indexOf("-") > -1)
        {
            String[] dates = StringSupport.Split(demoListDate, '-');
            demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.parse(dates[0]),DateTimeSupport.parse(dates[1]));
        }
        else if (demoListDate.length() == 0)
        {
            demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.add(Calendar.getInstance().getTime(),Calendar.MONTH,-6),Calendar.getInstance().getTime());
        }
          
        return demoListDate;
    }

    private String getDemoListingNumber(int stField, String fieldValue) throws Exception {
        String val = getSearchValue(stField);
        try
        {
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
                val = fieldValue;
                switch(stField)
                {
                    case ST_TOTALBATHS: 
                        val = MLSRecord.validateSTDFTBath(fieldValue);
                        break;
                    case ST_SQFT: 
                        val = MLSRecord.validateSTDFSQFT(fieldValue);
                        break;
                
                }
            }
              
            if (!StringSupport.isNullOrEmpty(val))
                val = (int)Math.round(Double.valueOf(val)) + "";
             
        }
        catch (Exception exc)
        {
            System.out.println("Error" + exc.getMessage());
        }

        return val;
    }

    public int[] getResultField() throws Exception {
        return m_resultField;
    }

    protected public String[] getRequiredField() throws Exception {
        return REQUIRED_FIELD;
    }

    protected public String getCompatibilityID() throws Exception {
        return COMPATIBILITY_ID;
    }

    public int[] createRandom(int passedIntno, int max) throws Exception {
        // Remember: C# Requires all variables Initialized.... so lets initialize variable used
        int lowerBound = 0;
        int upperBound = passedIntno;
        boolean firstTime = true;
        int starti = 0;
        int[] vararray;
        vararray = new int[upperBound];
        // Random Class used here
        Random randomGenerator = new Random(Calendar.getInstance().getTime().Millisecond);
        do
        {
            int nogenerated = randomGenerator.Next(lowerBound, max);
            // Note: randomGenerator.Next generates no. to UpperBound - 1 hence +1
            // .... i got stuck at this pt & had to use the debugger.
            if (firstTime)
            {
                // if (firsttime == true)
                vararray[starti] = nogenerated;
                // we simply store the nogenerated in vararray
                firstTime = false;
                starti++;
            }
            else
            {
                // if (firsttime == false)
                boolean duplicate_flag = checkDuplicate(nogenerated,starti,vararray);
                // call to check in array
                if (!duplicate_flag)
                {
                    // duplicate_flag == false
                    vararray[starti] = nogenerated;
                    starti++;
                }
                 
            } 
        }
        while (starti < upperBound);
        return vararray;
    }

    private boolean isExistInArray(int[] functionArray, int num) throws Exception {
        return checkDuplicate(num,functionArray.length,functionArray);
    }

    private boolean checkDuplicate(int newrandomNum, int loopCount, int[] functionArray) throws Exception {
        boolean tempDuplicate = false;
        for (int j = 0;j < loopCount;j++)
        {
            if (functionArray[j] == newrandomNum)
            {
                tempDuplicate = true;
                break;
            }
             
        }
        return tempDuplicate;
    }

}


