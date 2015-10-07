//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:55 PM
//

package Tcs.Mls.Request;

import Tcs.Mls.Request.GetAgentRoster;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;

public class GetRecordCountAgentRoster  extends GetAgentRoster 
{
    protected public String m_resultFileName = "";
    protected public int[] m_recordCount = new int[]{ 0, 0, 0, 0 };
    protected int m_totalCount = 0;
    public GetRecordCountAgentRoster(TCServer tcs) throws Exception {
        super(tcs);
    }

    protected public void getResult() throws Exception {
        if (m_engine.isRETSClient())
            getListings();
        else
            throw TCSException.produceTCSException(TCSException.NOT_SURPPORT_RECORD_COUNT); 
    }

    public void setRecordLimit() throws Exception {
    }

    protected public void getOutputXML() throws Exception {
        String content = Tcs.Mls.Util.getFileContent(getResultFileName());
        System.out.println(getResultFileName());
        try
        {
            m_totalCount = Integer.valueOf(Tcs.Mls.Util.getRecordCount(content));
        }
        catch (Exception exc)
        {
        }
    
    }

    protected public void saveResult() throws Exception {
        m_resultBuffer = new StringBuilder();
        m_resultBuffer.Append("</Listings>\r\n");
        m_resultBuffer.Insert(0, "<Listings TotalCount=\"" + m_totalCount + "\">\r\n");
        m_resultBuffer.Insert(0, "<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n");
        m_resultBuffer.Append("</TCResult>");
        m_resultBuffer.Insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
        super.saveResult();
    }

    protected public void addXMLHeader() throws Exception {
    }

    protected public void addXMLFooter() throws Exception {
    }

    protected String getResultFileName() throws Exception {
        try
        {
            m_resultFileName = m_engine.getResultsFilename();
        }
        catch (Exception exc)
        {
            System.out.println("Can not get result file");
        }

        return m_resultFileName;
    }

}


