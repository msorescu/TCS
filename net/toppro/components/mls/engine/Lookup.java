//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import java.util.ArrayList;
import net.toppro.components.mls.engine.Lookup;
import net.toppro.components.mls.engine.LookupType;

public class Lookup   
{
    private ArrayList m_lookupTypeList = new ArrayList();
    private String m_lookupName = "";
    public Lookup() throws Exception {
    }

    public String getLookupName() throws Exception {
        return m_lookupName;
    }

    public void setLookupName(String value) throws Exception {
        m_lookupName = value;
    }

    public void addType(LookupType lookupType) throws Exception {
        m_lookupTypeList.add(lookupType);
    }

    public int getCount() throws Exception {
        return m_lookupTypeList.size();
    }

    public ArrayList getLookupTypeList() throws Exception {
        return m_lookupTypeList;
    }



}


