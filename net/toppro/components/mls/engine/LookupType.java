//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import net.toppro.components.mls.engine.LookupType;

public class LookupType   
{
    private String m_lookupValue;
    private String m_displayOrder;
    public LookupType() throws Exception {
    }

    public String getLookupValue() throws Exception {
        return m_lookupValue;
    }

    public void setLookupValue(String value) throws Exception {
        m_lookupValue = value;
    }

    public String getDisplayOrder() throws Exception {
        return m_displayOrder;
    }

    public void setDisplayOrder(String value) throws Exception {
        m_displayOrder = value;
    }



}


