//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:47 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Xml.XmlWriter;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.DataAggResultFieldType;
import net.toppro.components.mls.engine.RosterResultTable;
import Tcs.Mls.TCSStandardResultFields;

public class OpenHouseResutlTable  extends RosterResultTable 
{
    public OpenHouseResutlTable(Board board, net.toppro.components.mls.engine.MLSEngine engine, boolean isAgent) throws Exception {
        super(board, engine, isAgent);
    }

    protected int[] getResultFields() throws Exception {
        return TCSStandardResultFields.getOpenHouseResultFields();
    }

    public void toXml() throws Exception {
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        String elementName1 = MC.OPENHOUSE_METADATA_RESULT_TABLE;
        String elementName2 = MC.OPENHOUSE_RESULT_TABLE;
        w.writeStartElement(elementName1);
        w.writeStartElement(elementName2);
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
            w.WriteElementString(MC.RETS_LONG_NAME, value.getRetsLongName());
            //w.WriteElementString(MC.VISIBLE_FLAG, value.VisibleFlag);
            w.writeEndElement();
        }
        w.writeEndElement();
        w.writeEndElement();
    }

}


