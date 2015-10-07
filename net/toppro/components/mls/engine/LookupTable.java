//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.Lookup;
import net.toppro.components.mls.engine.LookupType;

public class LookupTable   
{
    private HashMap<String,Lookup> m_lookupList = new HashMap<String,Lookup>();
    //private ArrayList m_lookupList = new ArrayList();
    private Board m_board;
    public LookupTable(Board board) throws Exception {
        m_board = board;
    }

    public String addLookup(Lookup lookup) throws Exception {
        for (Object __dummyForeachVar0 : m_lookupList.entrySet())
        {
            Entry<String,Lookup> kvp = (Entry<String,Lookup>)__dummyForeachVar0;
            if (lookup == kvp.getValue())
                return kvp.getKey();
             
        }
        for (int i = 1;i < 100;i++)
        {
            if (m_lookupList.containsKey(lookup.getLookupName()))
            {
                lookup.setLookupName(m_board.getIncrementalFieldName(lookup.getLookupName()));
                continue;
            }
             
            m_lookupList.put(lookup.getLookupName(), lookup);
            break;
        }
        return lookup.getLookupName();
    }

    public void toXml() throws Exception {
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        HashMap<String,Lookup>.ValueCollection valueColl = m_lookupList.values();
        w.WriteStartElement(MC.METADATA_LOOKUP);
        for (Object __dummyForeachVar1 : valueColl)
        {
            Lookup lookup = (Lookup)__dummyForeachVar1;
            w.WriteStartElement(MC.LOOKUP);
            w.WriteAttributeString(MC.NAME, lookup.getLookupName());
            for (int j = 0;j < lookup.getCount();j++)
            {
                LookupType lookupType = (LookupType)lookup.getLookupTypeList().get(j);
                w.WriteStartElement(MC.LOOKUP_TYPE);
                w.WriteElementString(MC.LOOKUP_VALUE, lookupType.getLookupValue());
                w.WriteElementString(MC.DISPLAY_ORDER, lookupType.getDisplayOrder());
                w.writeEndElement();
            }
            w.writeEndElement();
        }
        w.writeEndElement();
    }

}


