//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:51 PM
//

package Tcs.Mls.Request;

import Tcs.Mls.TCServer;
import Tcs.Mls.TPAppRequest;

public class GetAgentListingInventory  extends TPAppRequest 
{
    //UPGRADE_NOTE: Final was removed from the declaration of 'm_searchField '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    protected public static final String[] m_searchField = new String[]{ STD_SEARCH_FIELD[ST_MLSNUMBER], STD_SEARCH_FIELD[ST_PROPERTYTYPE], STD_SEARCH_FIELD[ST_STATUS], STD_SEARCH_FIELD[ST_LISTDATE], STD_SEARCH_FIELD[ST_SALEDATE], STD_SEARCH_FIELD[ST_STATUSDATE], STD_SEARCH_FIELD[ST_AGENTID] };
    //UPGRADE_NOTE: Final was removed from the declaration of 'REQUIRED_FIELD '. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1003'"
    public static final String[] REQUIRED_FIELD = new String[]{ STD_SEARCH_FIELD[ST_STATUS], STD_SEARCH_FIELD[ST_AGENTID] };
    private static final String COMPATIBILITY_ID = "1";
    public GetAgentListingInventory(TCServer tcs) throws Exception {
        super(tcs);
    }

    protected public String[] getRequiredField() throws Exception {
        return REQUIRED_FIELD;
    }

    protected public void getResult() throws Exception {
        getListings();
    }

    //public override void SetRecordLimit()
    //{ }
    public int[] getResultField() throws Exception {
        return m_resultField;
    }

    protected public String getCompatibilityID() throws Exception {
        return COMPATIBILITY_ID;
    }

}


