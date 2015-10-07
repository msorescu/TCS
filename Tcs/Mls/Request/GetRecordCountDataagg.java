//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:55 PM
//

package Tcs.Mls.Request;

import Tcs.Mls.Request.GetAgentListingInventory;
import Tcs.Mls.Request.GetComparableListings;
import Tcs.Mls.Request.GetDataAggListings;
import Tcs.Mls.Request.GetOfficeListingInventory;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;

public class GetRecordCountDataagg  extends GetDataAggListings 
{
    protected public String m_resultFileName = "";
    protected public int[] m_recordCount = new int[]{ 0, 0, 0, 0 };
    private int m_totalCount = 0;
    public GetRecordCountDataagg(TCServer tcs) throws Exception {
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
        int n = 0;
        try
        {
            n = Integer.valueOf(Tcs.Mls.Util.getRecordCount(content));
        }
        catch (Exception exc)
        {
        }

        for (int i = 0;i < m_recordCount.length;i++)
        {
            if (String.IsNullOrEmpty(m_status))
            {
                m_totalCount += n;
                break;
            }
             
            if (m_status.EndsWith(STANDERD_STATUS[i]))
            {
                m_recordCount[i] = m_recordCount[i] + n;
                break;
            }
             
        }
    }

    protected public void saveResult() throws Exception {
        m_resultBuffer = new StringBuilder();
        String[] status = m_connector.getStatus();
        int totalCount = 0;
        if (m_totalCount == 0)
        {
            for (int i = 0;i < status.length;i++)
            {
                for (int j = 0;j < m_recordCount.length;j++)
                {
                    if (status[i].endsWith(STANDERD_STATUS[j]))
                    {
                        m_resultBuffer.Append("<Listing Status=\"" + status[i] + "\" Count=\"" + m_recordCount[j] + "\"/>\r\n");
                        totalCount += m_recordCount[j];
                        break;
                    }
                     
                }
            }
        }
        else
        {
            totalCount = m_totalCount;
        } 
        m_resultBuffer.Append("</Listings>\r\n");
        m_resultBuffer.Insert(0, "<Listings TotalCount=\"" + totalCount + "\">\r\n");
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

    protected public String[] getRequiredField() throws Exception {
        String[] requestField = new String[0];
        m_tcs.APPLY __dummyScrutVar0 = m_tcs.getFunctionId();
        if (__dummyScrutVar0.equals(TCServer.FUNCTION_GET_AGENT_LISTING_INVENTORY_ID))
        {
            requestField = GetAgentListingInventory.REQUIRED_FIELD;
        }
        else if (__dummyScrutVar0.equals(TCServer.FUNCTION_GET_OFFICE_LISTING_INVENTORY_ID))
        {
            requestField = GetOfficeListingInventory.REQUIRED_FIELD;
        }
        else if (__dummyScrutVar0.equals(TCServer.FUNCTION_GET_COMPARABLE_LISTINGS_ID))
        {
            requestField = GetComparableListings.REQUIRED_FIELD;
        }
           
        return requestField;
    }

    protected public String getCompatibilityID() throws Exception {
        String id = "";
        m_tcs.APPLY __dummyScrutVar1 = m_tcs.getFunctionId();
        if (__dummyScrutVar1.equals(TCServer.FUNCTION_GET_AGENT_LISTING_INVENTORY_ID))
        {
            id = "1";
        }
        else if (__dummyScrutVar1.equals(TCServer.FUNCTION_GET_OFFICE_LISTING_INVENTORY_ID))
        {
            id = "2";
        }
        else if (__dummyScrutVar1.equals(TCServer.FUNCTION_GET_COMPARABLE_LISTINGS_ID))
        {
            id = "3";
        }
           
        return id;
    }

}


