//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:51 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.StringSupport;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.TCSException;
import Tcs.Mls.TCServer;
import Tcs.Mls.TPAppRequest;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;

import java.util.Hashtable;

public class CheckCredential  extends TPAppRequest 
{
    public CheckCredential(TCServer tcs) throws Exception {
        super(tcs);
        m_isSearchOneDef = true;
    }

    protected public void getResult() throws Exception {
        checkCredential_Renamed_Method();
    }

    public void setRecordLimit() throws Exception {
        m_engine.RecordsLimit = 1;
    }

    protected public void checkCredential_Renamed_Method() throws Exception {
        try
        {
            m_resultBuffer = new StringBuilder();
            if (m_engine.isRETSClient())
            {
                setLogMode();
                m_connector.setOverallProgress(MLSConnector.OVERALL_PROGRESS_CHECKCREDENTIAL);
                MLSCmaFields fields = m_engine.getCmaFields();
                String[] tempRecord = new String[fields.size()];
                int index = fields.getStdFieldIndex(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID);
                tempRecord[index] = FAKE_RECORDID;
                index = fields.getStdFieldIndex(Tcs.Mls.TCSStandardResultFields.STDF_PICID);
                if (index > -1)
                    tempRecord[index] = FAKE_RECORDID;
                 
                MLSRecord record = new MLSRecord(fields,tempRecord);
                record.setChecked(true);
                m_engine.addMLSRecord(record);
                m_engine.runPicScript(m_engine.getMLSRecords(MLSRecords.FILTER_CHECKED));
                m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\"/>");
                String agentID = m_connector.GetAgentIDForCheckCredential();
                String defAgentID = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH, "ST_ListAgentID");
                if (!StringSupport.isNullOrEmpty(agentID) && !StringSupport.isNullOrEmpty(defAgentID))
                {
                    Hashtable ht = new Hashtable();
                    ht.put(STD_SEARCH_FIELD[ST_AGENTID], agentID);
                    ht.put(STD_SEARCH_FIELD[ST_STATUS], getSTStatus("A"));
                    try
                    {
                        setRecordLimit();
                        m_engine.setCurrentGroup(m_engine.getPropertyFieldGroups().Length > 1 ? 1 : 0);
                        m_engine.setPropertyFields(ht);
                        //m_engine.setSearchByRecordCount(true);
                        m_connector.setOverallProgress(100);
                        m_engine.runMainScript(0);
                    }
                    catch (MLSException texc)
                    {
                        if (texc.getCode() != MLSException.CODE_NO_RECORD_FOUND)
                            throw TCSException.produceTCSException(TCSException.INVALID_AGENTID);
                         
                    }
                
                }
                 
            }
            else if (m_engine.isDemoClient())
                m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\"/>");
            else
                throw TCSException.produceTCSException(STRINGS.get_Renamed(STRINGS.NOT_SURPPORT_CREDENTIAL_CHECK),"",TCSException.NOT_SURPPORT_CREDENTIAL_CHECK);  
        }
        catch (Exception e)
        {
            TCSException exc = TCSException.produceTCSException(e);
            throw exc;
        }
    
    }

    protected public void addXMLHeader() throws Exception {
    }

    protected public void addXMLFooter() throws Exception {
    }

    protected public void checkSearchFields() throws Exception {
    }

    protected public void checkDEFRequiredSearchFeilds() throws Exception {
    }

}


