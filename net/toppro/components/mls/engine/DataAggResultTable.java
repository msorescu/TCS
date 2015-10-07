//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.ArgumentException;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.DataAggResultFieldType;

public class DataAggResultTable   
{
    private HashMap<String,DataAggResultFieldType> m_resultFieldTypeDict = new HashMap<String,DataAggResultFieldType>();
    private Board m_board;
    public DataAggResultTable(Board board) throws Exception {
        m_board = board;
    }

    public String addType(DataAggResultFieldType resultFieldType) throws Exception {
        String result = "";
        String keyName = resultFieldType.getReferenceName();
        boolean bFound = false;
        try
        {
            if (m_resultFieldTypeDict.containsKey(keyName))
            {
                DataAggResultFieldType existedType = m_resultFieldTypeDict.get(keyName);
                if (StringSupport.isNullOrEmpty(existedType.getDisplayName()) && !StringSupport.isNullOrEmpty(resultFieldType.getDisplayName()))
                    existedType.setDisplayName(resultFieldType.getDisplayName());
                 
                return "";
            }
             
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
        w.WriteStartElement(MC.DATAAGG_METADATA_RESULT_TABLE);
        w.WriteStartElement(MC.DATAAGG_RESULT_TABLE);
        for (DataAggResultFieldType value : m_resultFieldTypeDict.values())
        {
            w.WriteStartElement(MC.RESULT_FIELD);
            //w.WriteAttributeString(MC.NAME, value.ReferenceName);
            w.WriteElementString(MC.NAME, value.getSystemName());
            w.WriteElementString(MC.DISPLAY_NAME, value.getDisplayName());
            //w.WriteElementString(MC.STANDARD_NAME, value.StandardName);
            w.WriteElementString(MC.DATA_TYPE_ID, value.getDataTypeID());
            w.WriteElementString(MC.DATA_TYPE_DESCRIPTION, value.getDataTypeDescription());
            w.WriteElementString(MC.STANDARD, value.getIsStandard());
            w.WriteElementString(MC.DISPLAY_RULE, value.getDisplayRule());
            w.WriteElementString(MC.NORMALIZED, value.getNormalized());
            w.WriteElementString(MC.RETS_LONG_NAME, value.getRetsLongName());
            w.WriteElementString(MC.CATEGORY, value.getCategory());
            if (!StringSupport.isNullOrEmpty(value.getCategory()) && m_board.getCategoryOrder().get(value.getCategory()).containsKey(value.getSystemName()))
                w.WriteElementString(MC.ORDER_IN_CATEGORY, m_board.getCategoryOrder().get(value.getCategory()).get(value.getSystemName()));
            else
                w.WriteElementString(MC.ORDER_IN_CATEGORY, ""); 
            w.WriteElementString(MC.DISPLAY_FORMAT_STRING, value.getDisplayFormatString());
            w.WriteElementString(MC.ATTRIBUTE_EXPOSURE, value.getAttributeExposure());
            w.WriteElementString(MC.DISPLAY_BEHAVIOR_BITMASK, value.getDisplayBehaviorBitmask());
            //w.WriteElementString(MC.VISIBLE_FLAG, value.VisibleFlag);
            w.writeEndElement();
        }
        w.writeEndElement();
        w.writeEndElement();
    }

}


