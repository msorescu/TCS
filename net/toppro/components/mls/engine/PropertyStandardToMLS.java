//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Xml.XmlDocument;
import CS2JNet.System.Xml.XmlElement;
import CS2JNet.System.Xml.XmlNode;
import CS2JNet.System.Xml.XmlNodeList;
import CS2JNet.System.Xml.XmlWriter;
import java.util.HashMap;
import net.toppro.components.mls.engine.Board;

public class PropertyStandardToMLS   
{
    private HashMap<String,String> m_searchFieldDict = new HashMap<String,String>();
    private Board m_board;
    public PropertyStandardToMLS(Board board) throws Exception {
        m_board = board;
        m_searchFieldDict = new HashMap<String,String>();
    }

    public void addSearchFieldByDef(String defPath, String searchField) throws Exception {
        if (!m_searchFieldDict.containsKey(defPath))
        {
            m_searchFieldDict.put(defPath, searchField);
        }
         
    }

    public void toXml() throws Exception {
        String mappings = m_board.getMLSSystem().getEnvironment().getStandardPropertyMappings();
        XmlDocument doc = new XmlDocument();
        doc.loadXml(mappings);
        XmlNodeList nList = doc.getDocumentElement().getChildNodes();
        String lastStandardProperty = "";
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        w.WriteStartElement(MC.PROPERTY_STANDARD_TO_MLS);
        for (Object __dummyForeachVar0 : nList)
        {
            XmlNode node = (XmlNode)__dummyForeachVar0;
            XmlElement xEle = (XmlElement)node;
            String standardProperty = xEle.getAttribute("type_description");
            String propertyClass = xEle.getAttribute("connection_type");
            String searchValues = xEle.getAttribute("property_sub_type");
            String defPath = xEle.getAttribute("definition_file");
            if (defPath.toLowerCase().endsWith("da.sql"))
                continue;
             
            if (!lastStandardProperty.equals(standardProperty))
            {
                if (!lastStandardProperty.equals(""))
                    w.writeEndElement();
                 
                w.WriteStartElement(MC.STANDARD_PROPERTY);
                w.WriteAttributeString(MC.NAME, standardProperty);
                lastStandardProperty = standardProperty;
            }
             
            w.WriteStartElement(MC.PROPERTY_CLASS);
            w.WriteAttributeString(MC.NAME, propertyClass);
            w.WriteStartElement(MC.SEARCH_FIELD);
            w.writeString((m_searchFieldDict.containsKey(defPath)) ? m_searchFieldDict.get(defPath) : "");
            w.writeEndElement();
            w.WriteStartElement(MC.SEARCH_VALUSES);
            w.writeString(searchValues);
            w.writeEndElement();
            w.writeEndElement();
        }
        if (!lastStandardProperty.equals(""))
            w.writeEndElement();
         
        w.writeEndElement();
    }

    //FakeData();
    private void fakeData() throws Exception {
        //            m_board.MLSSystem.Environment.getStandardPropertyMappingsByBoardId();
        //
        /*
                     * 
                    <PropertyStandardToMLS>
                      <StandardProperty Name="Single-family">
                        <PropertyClass Name="Residential">
                          <SearchField>PType</SearchField>
                          <SearchValues>7 - Detached,8 - Duet</SearchValues>
                        </PropertyClass>
                        <PropertyClass Name="Residential Income">
                          <SearchField>PType</SearchField>
                          <SearchValues>9 - Patio Home/Villa</SearchValues>
                        </PropertyClass>
                      </StandardProperty>
                      <StandardProperty Name="Town-Home">
                        <PropertyClass Name="Residential">
                          <SearchField>PType</SearchField>
                          <SearchValues>11 - Townhouse</SearchValues>
                        </PropertyClass>
                      </StandardProperty>
                    </PropertyStandardToMLS>
                     * 
                     */
        XmlWriter w = m_board.getMLSSystem().getSystemXmlWriter();
        String fakeNode = "<PropertyStandardToMLS>" + "<StandardProperty Name=\"Single-family\">" + "<PropertyClass Name=\"Residential\">" + "<SearchField>PType</SearchField>" + "<SearchValues>7 - Detached,8 - Duet</SearchValues>" + "</PropertyClass>" + "<PropertyClass Name=\"Residential Income\">" + "<SearchField>PType</SearchField>" + "<SearchValues>9 - Patio Home/Villa</SearchValues>" + "</PropertyClass>" + "</StandardProperty>" + "<StandardProperty Name=\"Town-Home\">" + "<PropertyClass Name=\"Residential\">" + "<SearchField>PType</SearchField>" + "<SearchValues>11 - Townhouse</SearchValues>" + "</PropertyClass>" + "</StandardProperty>" + "</PropertyStandardToMLS>";
        w.WriteRaw(fakeNode);
    }

}


