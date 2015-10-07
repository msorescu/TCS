//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:57 PM
//

package Tcs.Mls;

import CS2JNet.System.Xml.XmlDocument;
import CS2JNet.System.Xml.XmlElement;
import CS2JNet.System.Xml.XmlNode;
import java.util.Calendar;
import java.util.Date;
import net.toppro.components.mls.engine.Log;
import net.toppro.components.mls.engine.MLSRecords;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.CheckCredential;
import Tcs.Mls.Request.GetAdditionalData;
import Tcs.Mls.Request.GetAgentListingInventory;
import Tcs.Mls.Request.GetAgentRoster;
import Tcs.Mls.Request.GetComparableListings;
import Tcs.Mls.Request.GetDataAggListings;
import Tcs.Mls.Request.GetIDXListings;
import Tcs.Mls.Request.GetMetaData;
import Tcs.Mls.Request.GetMLSFieldsCriteria;
import Tcs.Mls.Request.GetMLSMetadata;
import Tcs.Mls.Request.GetMLSPlusStandardListings;
import Tcs.Mls.Request.GetOfficeListingInventory;
import Tcs.Mls.Request.GetOfficeRoster;
import Tcs.Mls.Request.GetOpenHouse;
import Tcs.Mls.Request.GetPictures;
import Tcs.Mls.Request.GetRecordCount;
import Tcs.Mls.Request.GetRecordCountAgentRoster;
import Tcs.Mls.Request.GetRecordCountDataagg;
import Tcs.Mls.Request.GetRecordCountOfficeRoster;
import Tcs.Mls.Request.GetRecordCountOpenHouse;
import Tcs.Mls.Request.GetRecordCountPlus;
import Tcs.Mls.Request.LoadAutoCategoryData;
import Tcs.Mls.Request.TPOnlineGetComparableListings;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class TCServer   
{
    public static final int FUNCTION_GET_AGENT_LISTING_INVENTORY_ID = 1;
    public static final int FUNCTION_GET_OFFICE_LISTING_INVENTORY_ID = 2;
    public static final int FUNCTION_GET_COMPARABLE_LISTINGS_ID = 3;
    public static final int FUNCTION_GET_PICTURES_ID = 4;
    public static final int FUNCTION_CHECK_CREDENTIAL_ID = 5;
    public static final int FUNCTION_TP_GET_COMPARABLE_LISTINGS_ID = 6;
    public static final int FUNCTION_GET_META_DATA_ID = 7;
    public static final int FUNCTION_GET_MAPPING_FIELDS_ID = 8;
    public static final int FUNCTION_GET_MLS_PLUS_STANDARD_LISTINGS_ID = 9;
    public static final int FUNCTION_GET_DATA_AGG_LISTINGS_ID = 10;
    public static final int FUNCTION_GET_MLS_METADATA_ID = 11;
    public static final int FUNCTION_GET_AGENT_ROSTER_ID = 12;
    public static final int FUNCTION_GET_OFFICE_ROSTER_ID = 13;
    public static final int FUNCTION_GET_OPEN_HOUSE_ID = 14;
    public static final int FUNCTION_GET_IDX_LISTINGS_ID = 15;
    public static final int FUNCTION_LOAD_AUTOCATEGORY_DATA_ID = 16;
    public static final int FUNCTION_GET_ADDITIONAL_DATA_ID = 17;
    private static final String FUNCTION_GET_AGENT_LISTING_INVENTORY_NAME = "GETAGENTLISTINGINVENTORY";
    //"GetAgentListingInventory";
    private static final String FUNCTION_GET_OFFICE_LISTING_INVENTORY_NAME = "GETOFFICELISTINGINVENTORY";
    //"GetOfficeListingInventory";
    private static final String FUNCTION_GET_COMPARABLE_LISTINGS_NAME = "GETCOMPARABLELISTINGS";
    //"GetComparableListings";
    private static final String FUNCTION_GET_PICTURES_NAME = "GETPICTURES";
    //"GetPictures";
    private static final String FUNCTION_GET_EXTRADATA_NAME = "GETADDITIONALDATA";
    //"GETADDITIONALDATA";
    public static final String FUNCTION_CHECK_CREDENTIAL_NAME = "CHECKCREDENTIAL";
    //"CheckCredential";
    private static final String FUNCTION_TP_GET_COMPARABLE_LISTINGS_NAME = "GETMLSSPECIFICLISTINGS";
    //"GetMLSSpecificListings";
    public static final String FUNCTION_GET_META_DATA = "GETMETADATA";
    //"GetMetadata";
    public static final String FUNCTION_GET_MAPPING_FIELDS = "GETMLSFIELDSANDCRITERIA";
    //"GetMLSFieldsAndCriteria";
    public static final String FUNCTION_GET_MLS_PLUS_STANDARD_LISTINGS = "GETMLSPLUSSTANDARDLISTINGS";
    //"GetMLSPlusStandardListings";
    public static final String FUNCTION_GET_DATA_AGG_LISTINGS = "GETDATAAGGLISTINGS";
    //"GetDataAggListings";
    public static final String FUNCTION_GET_MLS_METADATA = "GETMLSMETADATA";
    //"GetDataAggListings";
    public static final String FUNCTION_GET_AGENT_ROSTER = "GETAGENTROSTER";
    //"GetAgentRoster";
    public static final String FUNCTION_GET_OFFICE_ROSTER = "GETOFFICEROSTER";
    //"GetOfficeRoster";
    public static final String FUNCTION_GET_OPEN_HOUSE = "GETOPENHOUSE";
    //"GetOpenHouse";
    public static final String FUNCTION_GET_IDX_LISTINGS = "GETIDXLISTINGS";
    //"GetIDXListings";
    public static final String FUNCTION_LOAD_AUTOCATEGORY_DATA = "LOADAUTOCATEGORYDATA";
    //"LoadAutoCategoryData";
    private static final String FAKE_PROFILE_ID = "{88888888-8888-8888-8888-888888888888}";
    private static final int CALL_BACK_QUEUE_ID = 30004;
    public static final int FLAG_NONE_PICTURE = 0;
    public static final int FLAG_ONE_PICTURE = 1;
    public static final int FLAG_MULTIPLE_PICTURE = 2;
    private MLSConnector m_connector = null;
    private net.toppro.components.mls.engine.MLSEngine m_engine = null;
    private MLSRecords m_records = null;
    private TPAppRequest m_tpRequest = null;
    private int m_currentFunctionId = -1;
    private int m_errorCode = 0;
    private boolean m_isRealTime = false;
    private String m_requestXml = "";
    private String m_requestId = "";
    private boolean needDeleteTempFolder = true;
    private boolean isDataAggListing = false;
    private int m_retryNumber = 0;
    private int m_totalRetriedNumber = 0;
    private Log m_log = null;
    private String m_messageHeader = "";
    private boolean m_refreshMetadata = false;
    public boolean getRefreshMetadata() throws Exception {
        return m_refreshMetadata;
    }

    public void setRefreshMetadata(boolean value) throws Exception {
        m_refreshMetadata = value;
    }

    public int getTotalRetriedNumber() throws Exception {
        return m_totalRetriedNumber;
    }

    private int m_retryWait = 0;
    public int getRetryWait() throws Exception {
        return m_retryWait;
    }

    public void setRetryWait(int value) throws Exception {
        if (value > 3600)
            value = 3600;
         
        m_retryWait = value;
    }

    public int getRetryNumber() throws Exception {
        return m_retryNumber;
    }

    public void setRetryNumber(int value) throws Exception {
        if (value > 20)
            value = 20;
         
        m_retryNumber = value;
    }

    public boolean getIsDataAggListing() throws Exception {
        return isDataAggListing;
    }

    public void setIsDataAggListing(boolean value) throws Exception {
        isDataAggListing = value;
    }

    public boolean getNeedDeleteTempFolder() throws Exception {
        return needDeleteTempFolder;
    }

    public void setNeedDeleteTempFolder(boolean value) throws Exception {
        needDeleteTempFolder = value;
    }

    public String getRequestId() throws Exception {
        return m_requestId;
    }

    public void setRequestId(String value) throws Exception {
        m_requestId = value;
    }

    public String getRequestXml() throws Exception {
        return m_requestXml;
    }

    public void setRequestXml(String value) throws Exception {
        m_requestXml = value;
    }

    public int getErrorCode() throws Exception {
        return m_errorCode;
    }

    public void setErrorCode(int value) throws Exception {
        m_errorCode = value;
    }

    private String m_errorMessage = "";
    public String getErrorMessage() throws Exception {
        return m_errorMessage;
    }

    public void setErrorMessage(String value) throws Exception {
        m_errorMessage = value;
    }

    public boolean getIsRealTime() throws Exception {
        return m_isRealTime;
    }

    public void setIsRealTime(boolean value) throws Exception {
        m_isRealTime = value;
    }

    public String getResult() throws Exception {
        return m_tpRequest.getResult();
    }

    private String m_moduleID;
    public String getModuleID() throws Exception {
        return m_moduleID;
    }

    public void setModuleID(String value) throws Exception {
        m_moduleID = value;
    }

    private String messageHeaderGuid;
    public String getMessageHeaderGuid() throws Exception {
        return messageHeaderGuid;
    }

    public void setMessageHeaderGuid(String value) throws Exception {
        messageHeaderGuid = value;
    }

    public TCServer(String messageHeader, Log log) throws Exception {
        m_log = log;
        m_messageHeader = messageHeader;
        m_connector = new MLSConnector(messageHeader,log,this);
    }

    public boolean runServer() throws Exception {
        LableRetry:boolean needDeleteTempFolder = true;
        try
        {
            long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            boolean canGetCount = false;
            boolean isGetMLSPlusStandardListings = false;
            switch(getFunctionId())
            {
                case FUNCTION_GET_AGENT_LISTING_INVENTORY_ID: 
                    m_tpRequest = new GetAgentListingInventory(this);
                    canGetCount = true;
                    break;
                case FUNCTION_GET_OFFICE_LISTING_INVENTORY_ID: 
                    m_tpRequest = new GetOfficeListingInventory(this);
                    canGetCount = true;
                    break;
                case FUNCTION_GET_COMPARABLE_LISTINGS_ID: 
                    m_tpRequest = new GetComparableListings(this);
                    canGetCount = true;
                    break;
                case FUNCTION_TP_GET_COMPARABLE_LISTINGS_ID: 
                    m_tpRequest = new TPOnlineGetComparableListings(this);
                    canGetCount = false;
                    break;
                case FUNCTION_GET_PICTURES_ID: 
                    m_tpRequest = new GetPictures(this);
                    needDeleteTempFolder = false;
                    break;
                case FUNCTION_CHECK_CREDENTIAL_ID: 
                    m_tpRequest = new CheckCredential(this);
                    break;
                case FUNCTION_GET_META_DATA_ID: 
                    m_tpRequest = new GetMetaData(this);
                    needDeleteTempFolder = false;
                    break;
                case FUNCTION_GET_MAPPING_FIELDS_ID: 
                    m_tpRequest = new GetMLSFieldsCriteria(this);
                    needDeleteTempFolder = false;
                    break;
                case FUNCTION_GET_MLS_PLUS_STANDARD_LISTINGS_ID: 
                    m_tpRequest = new GetMLSPlusStandardListings(this);
                    canGetCount = true;
                    isGetMLSPlusStandardListings = true;
                    break;
                case FUNCTION_GET_DATA_AGG_LISTINGS_ID: 
                    m_tpRequest = new GetDataAggListings(this);
                    setIsDataAggListing(true);
                    canGetCount = true;
                    break;
                case FUNCTION_GET_MLS_METADATA_ID: 
                    m_tpRequest = new GetMLSMetadata(this);
                    canGetCount = false;
                    break;
                case FUNCTION_GET_AGENT_ROSTER_ID: 
                    m_tpRequest = new GetAgentRoster(this);
                    setIsDataAggListing(true);
                    canGetCount = true;
                    break;
                case FUNCTION_GET_OFFICE_ROSTER_ID: 
                    m_tpRequest = new GetOfficeRoster(this);
                    setIsDataAggListing(true);
                    canGetCount = true;
                    break;
                case FUNCTION_GET_OPEN_HOUSE_ID: 
                    m_tpRequest = new GetOpenHouse(this);
                    setIsDataAggListing(true);
                    canGetCount = true;
                    break;
                case FUNCTION_GET_IDX_LISTINGS_ID: 
                    m_tpRequest = new GetIDXListings(this);
                    setIsDataAggListing(true);
                    canGetCount = true;
                    break;
                case FUNCTION_LOAD_AUTOCATEGORY_DATA_ID: 
                    m_tpRequest = new LoadAutoCategoryData(this);
                    canGetCount = false;
                    break;
                case FUNCTION_GET_ADDITIONAL_DATA_ID: 
                    m_tpRequest = new GetAdditionalData(this);
                    needDeleteTempFolder = false;
                    break;
                default: 
                    throw TCSException.produceTCSException("Funciton not support","",60090);
            
            }
            if (m_connector.isGetCount() && canGetCount)
            {
                switch(getFunctionId())
                {
                    case FUNCTION_GET_DATA_AGG_LISTINGS_ID: 
                        m_tpRequest = new GetRecordCountDataagg(this);
                        break;
                    case FUNCTION_GET_MLS_PLUS_STANDARD_LISTINGS_ID: 
                        m_tpRequest = new GetRecordCountPlus(this);
                        break;
                    case FUNCTION_GET_AGENT_ROSTER_ID: 
                        m_tpRequest = new GetRecordCountAgentRoster(this);
                        break;
                    case FUNCTION_GET_OFFICE_ROSTER_ID: 
                        m_tpRequest = new GetRecordCountOfficeRoster(this);
                        break;
                    case FUNCTION_GET_OPEN_HOUSE_ID: 
                        m_tpRequest = new GetRecordCountOpenHouse(this);
                        break;
                    default: 
                        m_tpRequest = new GetRecordCount(this);
                        break;
                
                }
            }
             
            m_connector.writeLine("Create Request Client Time = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
            m_connector.setClient(m_tpRequest);
            m_tpRequest.run();
        }
        catch (Exception exc)
        {
            TCSException tcsExc = TCSException.produceTCSException(exc);
            setErrorCode(tcsExc.getErrorCode());
            if (m_totalRetriedNumber < getRetryNumber() && getErrorCode() != TCSException.METADATA_VERSION_NOT_MATCH)
            {
                m_totalRetriedNumber++;
                if (getRetryWait() > 0)
                    Thread.sleep(getRetryWait() * 1000);
                 
                setErrorCode(0);
                m_connector = new MLSConnector(m_messageHeader,m_log,this);
                goto LableRetry
            }
             
            if (getErrorCode() < TCSException.INTERNAL_ERROR)
                setErrorCode(TCSException.INTERNAL_ERROR);
             
            setErrorMessage(tcsExc.getOutputErrorXml());
            m_connector.saveTaskResult("",tcsExc.getOutputErrorXml());
        }
        finally
        {
            if (needDeleteTempFolder)
            {
                m_connector.addFilesToDelete(m_connector.getSearchTempFolder());
            }
             
        }
        try
        {
            XmlNode Callback = m_connector.getCallback();
            if (Callback != null)
            {
                XmlDocument doc = new XmlDocument();
                doc.loadXml("<TCSCallback Url=\"\"></TCSCallback>");
                XmlElement node = (XmlElement)doc.selectSingleNode(".//TCSCallback");
                node.setAttribute("Url", Callback.selectSingleNode(MLSConnector.NODE_URL).getInnerXml());
                XmlElement requestID = doc.createElement("RequestID");
                requestID.setInnerXml(m_connector.getMessageHeader());
                node.appendChild(requestID);
                // ("RequestID", m_connector.getMessageHeader());
                XmlNode dataNode = Callback.selectSingleNode(MLSConnector.NODE_DATA);
                if (dataNode != null)
                    node.AppendChild(doc.ImportNode(dataNode, true));
                 
                TCSBllWrapper dbmq = new TCSBllWrapper();
                dbmq.CreateTask(CALL_BACK_QUEUE_ID, FAKE_PROFILE_ID, doc.getOuterXml(), 15);
            }
             
        }
        catch (Exception exc)
        {
            TCSException tcsExc = TCSException.produceTCSException(exc);
            setErrorCode(tcsExc.getErrorCode());
            if (getErrorCode() < TCSException.INTERNAL_ERROR)
                setErrorCode(TCSException.INTERNAL_ERROR);
             
            setErrorMessage(tcsExc.getOutputErrorXml());
            m_connector.writeLine(tcsExc.getOutputErrorXml());
        }

        if (!getIsRealTime())
            m_connector.closeCtcsBll();
         
        return true;
    }

    // m_connector = null;
    public MLSConnector getConnector() throws Exception {
        return m_connector;
    }

    public boolean isGetPicture() throws Exception {
        return (m_connector.getInputPictureFlag() == FLAG_MULTIPLE_PICTURE || m_connector.getInputPictureFlag() == FLAG_ONE_PICTURE);
    }

    //public virtual bool isGetExtraNote()
    //{
    //    return (m_connector.IsExtraNoteRequest());
    //}
    public int getFunctionId() throws Exception {
        String functionName = m_connector.getFunctionName().toUpperCase();
        String __dummyScrutVar2 = functionName;
        if (__dummyScrutVar2.equals(FUNCTION_GET_AGENT_LISTING_INVENTORY_NAME))
        {
            m_currentFunctionId = FUNCTION_GET_AGENT_LISTING_INVENTORY_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_OFFICE_LISTING_INVENTORY_NAME))
        {
            m_currentFunctionId = FUNCTION_GET_OFFICE_LISTING_INVENTORY_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_COMPARABLE_LISTINGS_NAME))
        {
            m_currentFunctionId = FUNCTION_GET_COMPARABLE_LISTINGS_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_PICTURES_NAME))
        {
            m_currentFunctionId = FUNCTION_GET_PICTURES_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_CHECK_CREDENTIAL_NAME))
        {
            m_currentFunctionId = FUNCTION_CHECK_CREDENTIAL_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_TP_GET_COMPARABLE_LISTINGS_NAME))
        {
            m_currentFunctionId = FUNCTION_TP_GET_COMPARABLE_LISTINGS_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_META_DATA))
        {
            m_currentFunctionId = FUNCTION_GET_META_DATA_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_MAPPING_FIELDS))
        {
            m_currentFunctionId = FUNCTION_GET_MAPPING_FIELDS_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_MLS_PLUS_STANDARD_LISTINGS))
        {
            m_currentFunctionId = FUNCTION_GET_MLS_PLUS_STANDARD_LISTINGS_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_DATA_AGG_LISTINGS))
        {
            m_currentFunctionId = FUNCTION_GET_DATA_AGG_LISTINGS_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_MLS_METADATA))
        {
            m_currentFunctionId = FUNCTION_GET_MLS_METADATA_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_AGENT_ROSTER))
        {
            m_currentFunctionId = FUNCTION_GET_AGENT_ROSTER_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_OFFICE_ROSTER))
        {
            m_currentFunctionId = FUNCTION_GET_OFFICE_ROSTER_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_OPEN_HOUSE))
        {
            m_currentFunctionId = FUNCTION_GET_OPEN_HOUSE_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_IDX_LISTINGS))
        {
            m_currentFunctionId = FUNCTION_GET_IDX_LISTINGS_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_LOAD_AUTOCATEGORY_DATA))
        {
            m_currentFunctionId = FUNCTION_LOAD_AUTOCATEGORY_DATA_ID;
        }
        else if (__dummyScrutVar2.equals(FUNCTION_GET_EXTRADATA_NAME))
        {
            m_currentFunctionId = FUNCTION_GET_ADDITIONAL_DATA_ID;
        }
                         
        return m_currentFunctionId;
    }

    public String getMetadataRequest() throws Exception {
        return "<TCService><Function>GetMetadata</Function><Board BoardId=\"" + m_connector.getBoardID() + "\"/><Search Type=\"METADATA-Board\" Format=\"XML\" Recursive=\"1\"/></TCService>";
    }

}


