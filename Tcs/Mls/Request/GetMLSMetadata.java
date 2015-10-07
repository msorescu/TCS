//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:54 PM
//

package Tcs.Mls.Request;

import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.STRINGS;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class GetMLSMetadata  extends TPAppRequest 
{
    public GetMLSMetadata(TCServer tcs) throws Exception {
        super(tcs);
        m_isSearchOneDef = true;
    }

    protected public void getResult() throws Exception {
        downloadMLSMetadata();
    }

    public void setRecordLimit() throws Exception {
    }

    protected public void downloadMLSMetadata() throws Exception {
        try
        {
            m_resultBuffer = new StringBuilder();
            if (m_engine.isRETSClient())
            {
                setLogMode();
                m_connector.setOverallProgress(MLSConnector.OVERALL_PROGRESS_CHECKCREDENTIAL);
                MLSCmaFields fields = m_engine.getCmaFields();
                m_engine.runMainScript(MLSEngine.MSFLAG_METADATA_ONLY);
                m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\"/>");
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

    //String outErrXml = exc.getOutputErrorXml();
    //m_connector.saveTaskResult( "", outErrXml );
    protected public void addXMLHeader() throws Exception {
    }

    protected public void addXMLFooter() throws Exception {
    }

    protected public void checkSearchFields() throws Exception {
    }

    protected public void checkDEFRequiredSearchFeilds() throws Exception {
    }

}


