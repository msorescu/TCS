//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:53 PM
//

package Tcs.Mls.Request;

import CS2JNet.JavaSupport.language.RefSupport;
import CS2JNet.System.DateTimeSupport;
import CS2JNet.System.DoubleSupport;
import CS2JNet.System.StringSupport;
import java.util.Calendar;
import java.util.Date;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.Request.GetDataAggListings;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;

public class GetIDXListings  extends GetDataAggListings 
{
    public GetIDXListings(TCServer tcs) throws Exception {
        super(tcs);
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
            m_status __dummyScrutVar0 = m_status;
            if (__dummyScrutVar0.equals("A"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ACTIVE);
            }
            else if (__dummyScrutVar0.equals("S"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_SOLD);
            }
            else if (__dummyScrutVar0.equals("P"))
            {
                m_records = m_engine.getMLSRecords(MLSRecords.FILTER_PENDING);
            }
            else if (__dummyScrutVar0.equals("E"))
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
                        case Tcs.Mls.TCSStandardResultFields.STDF_CMABEDROOMS: 
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
                             
                            break;
                        case Tcs.Mls.TCSStandardResultFields.STDF_STDFLISTOFFICEIDDATAAGG: 
                            fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                            if (StringSupport.isNullOrEmpty(fieldValue))
                                fieldValue = listBrokerID;
                             
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
                                    m_status __dummyScrutVar3 = m_status;
                                    if (__dummyScrutVar3.equals("A") || __dummyScrutVar3.equals("E") || __dummyScrutVar3.equals("P"))
                                    {
                                        fieldValue = listDate;
                                    }
                                    else if (__dummyScrutVar3.equals("S"))
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
                                    m_status __dummyScrutVar4 = m_status;
                                    if (__dummyScrutVar4.equals("A") || __dummyScrutVar4.equals("E") || __dummyScrutVar4.equals("P"))
                                    {
                                        fieldValue = listDate;
                                    }
                                    else if (__dummyScrutVar4.equals("S"))
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
                        default: 
                            //case TCSStandardResultFields.STDF_STDFLAT:
                            //case TCSStandardResultFields.STDF_STDFLONG:
                            //    fieldValue = "";
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
                            m_status __dummyScrutVar6 = m_status;
                            if (__dummyScrutVar6.equals("A") || __dummyScrutVar6.equals("E") || __dummyScrutVar6.equals("P"))
                            {
                                searchSaleDate = "";
                            }
                             
                            if (!StringSupport.isNullOrEmpty(searchStatusDate))
                            {
                                statusDate = GetDemoDate(searchStatusDate);
                                Tcs.Mls.Request.GetIDXListings.APPLY __dummyScrutVar7 = getStandardStatus(rec.getMLSRecordType());
                                if (__dummyScrutVar7.equals("A"))
                                {
                                    demoListDate = statusDate;
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar7.equals("E") || __dummyScrutVar7.equals("P"))
                                {
                                    demoListDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.add(DateTimeSupport.parse(statusDate),Calendar.DAY_OF_YEAR,-60), "MM/dd/yyyy"),60);
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar7.equals("S"))
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
                                Tcs.Mls.Request.GetIDXListings.APPLY __dummyScrutVar8 = getStandardStatus(rec.getMLSRecordType());
                                if (__dummyScrutVar8.equals("A"))
                                {
                                    statusDate = demoListDate;
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar8.equals("E") || __dummyScrutVar8.equals("P"))
                                {
                                    statusDate = Tcs.Mls.Util.getRandomDate(DateTimeSupport.ToString(DateTimeSupport.parse(demoListDate), "MM/dd/yyyy"),60);
                                    saleDate = "";
                                }
                                else if (__dummyScrutVar8.equals("S"))
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
                    fieldValue = Tcs.Mls.Util.filterJunkValue(fieldValue);
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
             
            if (!SelectPicFieldsOnly)
            {
                //Add Features, Room Dimensions, Views
                m_resultBuffer.Append("<Features>\r\n");
                try
                {
                    //long time2 = (System.DateTime.Now.Ticks - 621355968000000000);
                    if (FeatureFieldListNoRoomDimensionAndViews == null)
                        FeatureFieldListNoRoomDimensionAndViews = rec.getFeatureFieldList(new int[]{ Tcs.Mls.TCSStandardResultFields.STDF_STDFROOMDIM, Tcs.Mls.TCSStandardResultFields.STDF_STDFVIEWS });
                     
                    //fl = rec.getSTDFFieldList(TCSStandardResultFields.STDF_CMAFEATURE, new int[] { TCSStandardResultFields.STDF_STDFROOMDIM, TCSStandardResultFields.STDF_STDFVIEWS }, true, true);
                    // m_connector.WriteLine("Get feature= " + ((System.DateTime.Now.Ticks - 621355968000000000) - time2));
                    String prefix = "";
                    String featureValue = "";
                    for (int j = 0;j < FeatureFieldListNoRoomDimensionAndViews.Length;j++)
                    {
                        net.toppro.components.mls.engine.CmaField cfd = fields.getField(FeatureFieldListNoRoomDimensionAndViews[j]);
                        if (cfd != null)
                        {
                            featureValue = rec.getFieldValue(FeatureFieldListNoRoomDimensionAndViews[j]);
                            featureValue = net.toppro.components.mls.engine.CmaField.getValidFeature(featureValue);
                            if (!StringSupport.isNullOrEmpty(featureValue))
                            {
                                if (featureValue.indexOf(":") > -1)
                                {
                                    boolean hasPrefixInValue = false;
                                    if (!StringSupport.isNullOrEmpty(cfd.prefix))
                                    {
                                        hasPrefixInValue = true;
                                    }
                                     
                                    if (hasPrefixInValue)
                                        featureValue = featureValue.substring(featureValue.indexOf(":") + 1);
                                     
                                }
                                 
                            }
                             
                            if (StringSupport.isNullOrEmpty(featureValue))
                                continue;
                             
                            String[] subValues = StringSupport.Split(featureValue, ',');
                            prefix = cfd.getDisplayName();
                            if (StringSupport.isNullOrEmpty(prefix))
                                prefix = cfd.getPrefix();
                            else
                                prefix = prefix + ": "; 
                            for (int n = 0;n < subValues.length;n++)
                            {
                                m_resultBuffer.Append("<Feature Value=\"" + Tcs.Mls.Util.convertStringToXML(prefix) + Tcs.Mls.Util.convertStringToXML(StringSupport.Trim(subValues[n])) + "\"/>\r\n");
                            }
                        }
                         
                    }
                    m_resultBuffer.Append(rec.getRoomDimensions(false,true,true));
                }
                catch (Exception e)
                {
                    //UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
                    m_connector.WriteLine("Error on retrieving features" + e.getMessage());
                }

                m_resultBuffer.Append("</Features>\r\n");
                //m_resultBuffer.Append(rec.GetUnits(false));
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

}


