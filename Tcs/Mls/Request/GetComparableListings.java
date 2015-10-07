//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:51 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.StringSupport;
import java.util.HashMap;
import Tcs.Mls.TCServer;
import Tcs.Mls.TPAppRequest;

public class GetComparableListings  extends TPAppRequest 
{
    //UPGRADE_NOTE: Final was removed from the declaration of 'm_searchField '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    protected public static final String[] m_searchField = new String[]{ STD_SEARCH_FIELD[ST_MLSNUMBER], STD_SEARCH_FIELD[ST_PROPERTYSUBTYPE], STD_SEARCH_FIELD[ST_STATUS], STD_SEARCH_FIELD[ST_ZIP], STD_SEARCH_FIELD[ST_BEDS], STD_SEARCH_FIELD[ST_FULLBATHS], STD_SEARCH_FIELD[ST_SQFT], STD_SEARCH_FIELD[ST_LISTDATE], STD_SEARCH_FIELD[ST_LISTPRICE], STD_SEARCH_FIELD[ST_SALEDATE], STD_SEARCH_FIELD[ST_SALEPRICE], STD_SEARCH_FIELD[ST_SEARCHPRICE], STD_SEARCH_FIELD[ST_STATUSDATE] };
    //UPGRADE_NOTE: Final was removed from the declaration of 'REQUIRED_FIELD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] REQUIRED_FIELD = new String[]{ STD_SEARCH_FIELD[ST_STATUS] };
    private static final String COMPATIBILITY_ID = "3";
    private HashMap<String,Integer> m_resultFieldNameList = null;
    public GetComparableListings(TCServer tcs) throws Exception {
        super(tcs);
    }

    protected public void getResult() throws Exception {
        getListings();
    }

    public int[] getResultField() throws Exception {
        if (m_connector.getSelectFields().Length == 0)
            return m_resultField;
        else
        {
            if (m_resultFieldNameList == null)
            {
                m_resultFieldNameList = new HashMap<String,Integer>(m_resultField.Length);
                for (int i = 0;i < m_resultField.Length;i++)
                {
                    m_resultFieldNameList.Add(Tcs.Mls.TCSStandardResultFields.GetXmlName(m_resultField[i]), m_resultField[i]);
                }
                String selectFieldsList = m_connector.getSelectFields();
                selectFieldsList = selectFieldsList.replace(" ", "");
                if (selectFieldsList.IndexOf("Features", StringComparison.CurrentCultureIgnoreCase) == -1)
                {
                    m_includeFeature = false;
                    selectFieldsList = selectFieldsList.replace(",Features", "");
                }
                 
                if (selectFieldsList.IndexOf("RoomDimensions", StringComparison.CurrentCultureIgnoreCase) == -1)
                {
                    m_includeRoomDimention = false;
                    selectFieldsList = selectFieldsList.replace(",RoomDimensions", "");
                }
                 
                if (selectFieldsList.IndexOf("Views", StringComparison.CurrentCultureIgnoreCase) == -1)
                {
                    m_includeView = false;
                    selectFieldsList = selectFieldsList.replace(",Views", "");
                }
                 
                String[] selectFileds = StringSupport.Split(selectFieldsList, ',');
                m_resultField = new int[selectFileds.length];
                for (int j = 0;j < selectFileds.length;j++)
                {
                    m_resultField[j] = m_resultFieldNameList.get(selectFileds[j]);
                }
            }
             
            return m_resultField;
        } 
    }

    protected public String[] getRequiredField() throws Exception {
        return REQUIRED_FIELD;
    }

    protected public String getCompatibilityID() throws Exception {
        return COMPATIBILITY_ID;
    }

}


