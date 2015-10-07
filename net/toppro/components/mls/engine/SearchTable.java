//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:49 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.ArgumentException;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.SearchFieldType;

public class SearchTable   
{
    private HashMap<String,SearchFieldType> m_searchFieldTypeDict = new HashMap<String,SearchFieldType>();
    private Board m_board;
    public SearchTable(Board board) throws Exception {
        m_board = board;
    }

    public String addType(SearchFieldType searchFieldType) throws Exception {
        String result = "";
        String keyName = searchFieldType.getSystemName();
        boolean bFound = false;
        try
        {
            for (int i = 0;i < m_searchFieldTypeDict.size();i++)
            {
                if (m_searchFieldTypeDict.containsKey(keyName))
                {
                    SearchFieldType existedFieldType = m_searchFieldTypeDict.get(keyName);
                    if (searchFieldType == existedFieldType)
                        return keyName;
                    else
                        keyName = m_board.getIncrementalFieldName(keyName); 
                }
                else
                    break; 
            }
            searchFieldType.setReferenceName(keyName);
            m_searchFieldTypeDict.put(keyName, searchFieldType);
        }
        catch (ArgumentNullException exc)
        {
            System.out.println("Key is null");
        }
        catch (ArgumentException exc)
        {
            System.out.println("Key is already existed");
        }

        return keyName;
            ;
    }

    public void toXml() throws Exception {
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.METADATA_SEARCH_TABLE);
        w.WriteStartElement(MC.SEARCH_TABLE);
        for (SearchFieldType value : m_searchFieldTypeDict.values())
        {
            //Dictionary<string, SearchFieldType>.ValueCollection valColl = m_searchFieldTypeDict.Values;
            w.WriteStartElement(MC.SEARCH_FIELD);
            w.WriteAttributeString(MC.NAME, value.getReferenceName());
            w.WriteElementString(MC.SYSTEM_NAME, value.getSystemName());
            w.WriteElementString(MC.DISPLAY_NAME, value.getDisplayName());
            w.WriteElementString(MC.STANDARD_NAME, value.getStandardName());
            w.WriteElementString(MC.CONTROL_TYPE_ID, value.getControlTypeID());
            w.WriteElementString(MC.CONTROL_TYPE_DESCRIPTION, value.getControlTypeDescription());
            w.WriteElementString(MC.LOOKUP_NAME, value.getLookupName());
            w.WriteElementString(MC.DELIMETER, value.getDelimeter());
            w.WriteElementString(MC.HELP_TEXT, value.getHelpText());
            w.WriteElementString(MC.REQUIRED_FLAG, value.getRequiredFlag());
            w.writeEndElement();
        }
        w.writeEndElement();
        w.writeEndElement();
    }

}


