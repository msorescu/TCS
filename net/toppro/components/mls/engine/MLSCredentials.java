//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:42 PM
//

package net.toppro.components.mls.engine;


public class MLSCredentials   
{
    private String m_systemName = "";
    private String m_displayName = "";
    private String m_storeFlag = "";
    private String m_storeFlagDesc = "";
    public MLSCredentials() throws Exception {
    }

    public String getSystemName() throws Exception {
        return m_systemName;
    }

    public void setSystemName(String value) throws Exception {
        m_systemName = value;
    }

    public String getDisplayName() throws Exception {
        return m_displayName;
    }

    public void setDisplayName(String value) throws Exception {
        m_displayName = value;
    }

    public String getStoreFlag() throws Exception {
        return m_storeFlag;
    }

    public void setStoreFlag(String value) throws Exception {
        m_storeFlag = value;
    }

    public String getStoreFlagDesc() throws Exception {
        return m_storeFlagDesc;
    }

    public void setStoreFlagDesc(String value) throws Exception {
        m_storeFlagDesc = value;
    }

}


