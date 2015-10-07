//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.ArgumentException;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.ResultFieldType;

public class ResultTable   
{
    private HashMap<String,ResultFieldType> m_resultFieldTypeDict = new HashMap<String,ResultFieldType>();
    private Board m_board;
    public ResultTable(Board board) throws Exception {
        m_board = board;
    }

    public String addType(ResultFieldType resultFieldType) throws Exception {
        String result = "";
        String keyName = resultFieldType.getSystemName();
        boolean bFound = false;
        try
        {
            for (int i = 0;i < m_resultFieldTypeDict.size();i++)
            {
                if (m_resultFieldTypeDict.containsKey(keyName))
                {
                    ResultFieldType existedFieldType = m_resultFieldTypeDict.get(keyName);
                    if (resultFieldType == existedFieldType)
                        return keyName;
                    else
                        keyName = m_board.getIncrementalFieldName(keyName); 
                        ;
                }
                else
                    break; 
            }
            resultFieldType.setReferenceName(keyName);
            m_resultFieldTypeDict.put(keyName, resultFieldType);
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
        w.WriteStartElement(MC.METADATA_RESULT_TABLE);
        w.WriteStartElement(MC.RESULT_TABLE);
        for (ResultFieldType value : m_resultFieldTypeDict.values())
        {
            w.WriteStartElement(MC.RESULT_FIELD);
            w.WriteAttributeString(MC.NAME, value.getReferenceName());
            w.WriteElementString(MC.SYSTEM_NAME, value.getSystemName());
            w.WriteElementString(MC.DISPLAY_NAME, value.getDisplayName());
            w.WriteElementString(MC.STANDARD_NAME, value.getStandardName());
            w.WriteElementString(MC.DATA_TYPE_ID, value.getDataTypeID());
            w.WriteElementString(MC.DATA_TYPE_DESCRIPTION, value.getDataTypeDescription());
            w.WriteElementString(MC.VISIBLE_FLAG, value.getVisibleFlag());
            w.WriteElementString(MC.DISPLAY_RULE, value.getDisplayRule());
            w.writeEndElement();
        }
        w.writeEndElement();
        w.writeEndElement();
    }

}


