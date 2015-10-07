//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:49 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.ArgumentException;
import CS2JNet.System.StringSupport;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import net.toppro.components.mls.engine.Board;
import net.toppro.components.mls.engine.DataAggResultFieldType;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.PropertyClass;

public class RosterResultTable   
{
    protected HashMap<String,DataAggResultFieldType> m_resultFieldTypeDict = new HashMap<String,DataAggResultFieldType>();
    protected net.toppro.components.mls.engine.MLSEngine m_engine;
    protected Board m_board;
    protected boolean m_isAgent = true;
    public RosterResultTable(Board board, net.toppro.components.mls.engine.MLSEngine engine, boolean isAgent) throws Exception {
        m_board = board;
        m_engine = engine;
        m_isAgent = isAgent;
        populateData();
    }

    protected int[] getResultFields() throws Exception {
        return m_isAgent ? Tcs.Mls.TCSStandardResultFields.getAgentRosterResultFields() : Tcs.Mls.TCSStandardResultFields.getOfficeRosterResultFields();
    }

    public void populateData() throws Exception {
        int groupSize = 1;
        //m_mlsEngine.getPropertyFieldGroups().Length;
        MLSCmaFields mlsCmaFields = m_engine.getCmaFields();
        String caption = "";
        String displayName = "";
        String typeID = "2";
        String displayRule = "";
        int[] resultFiled = getResultFields();
        for (int x = 0;x < resultFiled.length;x++)
        {
            net.toppro.components.mls.engine.CmaField cmaField = mlsCmaFields.getStdField(resultFiled[x]);
            if (cmaField != null)
            {
                if (resultFiled[x] != Tcs.Mls.TCSStandardResultFields.STDF_DEFTYPE_NODEFNAME)
                    caption = cmaField.getCaption();
                else
                    caption = "DEF type"; 
                displayName = cmaField.getDisplayName();
                if (StringSupport.isNullOrEmpty(displayName))
                {
                    displayName = Tcs.Mls.TCSStandardResultFields.getXmlName(resultFiled[x]);
                    if (resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD)
                    {
                        displayName = "Last Modified Date Time";
                    }
                     
                }
                 
                if (StringSupport.isNullOrEmpty(displayName))
                    displayName = StringSupport.Trim(caption);
                 
                typeID = String.valueOf(cmaField.type);
                displayRule = cmaField.getDisplayRule();
                if (StringSupport.isNullOrEmpty(displayRule))
                {
                    displayRule = Tcs.Mls.TCSStandardResultFields.getDisplayRule(resultFiled[x]);
                    if (resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD || resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFSTATUSDATE)
                        displayRule = "5";
                     
                }
                 
                if (StringSupport.isNullOrEmpty(displayRule))
                    displayRule = "4";
                 
            }
            else
            {
                displayName = Tcs.Mls.TCSStandardResultFields.getXmlName(resultFiled[x]);
                typeID = String.valueOf(PropertyClass.getDataTypeID(Tcs.Mls.TCSStandardResultFields.getDataType(resultFiled[x])));
                displayRule = Tcs.Mls.TCSStandardResultFields.getDisplayRule(resultFiled[x]);
                if (resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD)
                {
                    displayRule = "5";
                    displayName = "Last Modified Date Time";
                }
                 
                if (resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFSTATUSDATE)
                {
                    displayRule = "5";
                }
                 
            } 
            if (resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD)
            {
                typeID = "7";
            }
             
            if (resultFiled[x] == Tcs.Mls.TCSStandardResultFields.STDF_STDFSTATUSDATE)
            {
                typeID = "3";
            }
             
            DataAggResultFieldType resultFieldType = new DataAggResultFieldType();
            resultFieldType.setSystemName(Tcs.Mls.TCSStandardResultFields.getXmlName(resultFiled[x]));
            resultFieldType.setReferenceName(resultFieldType.getSystemName().toUpperCase());
            resultFieldType.setDisplayName(displayName);
            resultFieldType.setDataTypeID(typeID);
            resultFieldType.setDataTypeDescription(PropertyClass.getDataTypeDescription(Integer.valueOf(typeID)));
            //if (x != TCSStandardResultFields.STDF_CMAFEATURE && x != TCSStandardResultFields.STDF_STDFROOMDIM)
            resultFieldType.setIsStandard("1");
            //else
            //    resultFieldType.IsStandard = "0";
            //resultFieldType.VisibleFlag = caption.Length == 0 ? "N" : "Y";
            resultFieldType.setDisplayRule(displayRule);
            resultFieldType.setRetsLongName(((cmaField != null) ? cmaField.retsLongName : ""));
            addToResultTable(resultFieldType);
        }
    }

    private String addToResultTable(DataAggResultFieldType resultFieldType) throws Exception {
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
                 
                return keyName;
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
        String elementName1 = m_isAgent ? MC.AGENT_ROSTER_METADATA_RESULT_TABLE : MC.OFFICE_ROSTER_METADATA_RESULT_TABLE;
        String elementName2 = m_isAgent ? MC.AGENT_ROSTER_RESULT_TABLE : MC.OFFICE_ROSTER_RESULT_TABLE;
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


