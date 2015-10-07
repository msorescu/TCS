//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:55 PM
//

package Tcs.Mls.Request;

import Tcs.Mls.Request.GetOpenHouse;
import Tcs.Mls.TCServer;

public class GetRecordCountOpenHouse  extends GetOpenHouse 
{
    protected public String m_resultFileName = "";
    protected public int[] m_recordCount = new int[]{ 0, 0, 0, 0 };
    private int m_totalCount = 0;
    public GetRecordCountOpenHouse(TCServer tcs) throws Exception {
        super(tcs);
    }

    public void setRecordLimit() throws Exception {
    }

    protected public void getOutputXML() throws Exception {
        String content = Tcs.Mls.Util.getFileContent(getResultFileName());
        System.out.println(getResultFileName());
        int n = 0;
        try
        {
            m_totalCount += Integer.valueOf(Tcs.Mls.Util.getRecordCount(content));
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

    private String getResultFileName() throws Exception {
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


