//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Xml.XmlWriter;
import java.util.ArrayList;
import net.toppro.components.mls.engine.LayoutType;
import net.toppro.components.mls.engine.PropertyClass;

public class Layout   
{
    private String m_layoutName;
    private ArrayList m_layouType = new ArrayList();
    private PropertyClass m_propertyClass;
    private int m_layoutType = 0;
    public static final int LAYOUT_TYPE_SEARCH = 0;
    public static final int LAYOUT_TYPE_RESULT = 1;
    public Layout(PropertyClass propertyClass, String layoutName, int layoutType) throws Exception {
        m_layoutName = layoutName;
        m_propertyClass = propertyClass;
        m_layoutType = layoutType;
    }

    public String getLayoutName() throws Exception {
        return m_layoutName;
    }

    public void setLayoutName(String value) throws Exception {
        m_layoutName = value;
    }

    public void addLayoutType(LayoutType layoutType) throws Exception {
        m_layouType.add(layoutType);
    }

    public void toXml() throws Exception {
        XmlWriter w = m_propertyClass.getBoard().getMLSSystem().getSystemXmlWriter();
        switch(m_layoutType)
        {
            case LAYOUT_TYPE_SEARCH: 
                w.WriteStartElement(MC.SEARCH_LAYOUT);
                break;
            case LAYOUT_TYPE_RESULT: 
                w.WriteStartElement(MC.RESULT_LAYOUT);
                break;
        
        }
        w.WriteAttributeString(MC.LAYOUT_NAME, getLayoutName());
        for (int i = 0;i < m_layouType.size();i++)
        {
            switch(m_layoutType)
            {
                case LAYOUT_TYPE_SEARCH: 
                    w.WriteStartElement(MC.SEARCH_LAYOUT_TYPE);
                    w.WriteElementString(MC.SEARCH_FIELD_NAME, ((LayoutType)m_layouType.get(i)).getFieldName());
                    w.WriteElementString(MC.DISPLAY_ORDER, ((LayoutType)m_layouType.get(i)).getDisplyOrder());
                    w.writeEndElement();
                    break;
                case LAYOUT_TYPE_RESULT: 
                    w.WriteStartElement(MC.RESULT_LAYOUT_TYPE);
                    w.WriteElementString(MC.RESULT_FIELD_NAME, ((LayoutType)m_layouType.get(i)).getFieldName());
                    w.WriteElementString(MC.DISPLAY_ORDER, ((LayoutType)m_layouType.get(i)).getDisplyOrder());
                    w.writeEndElement();
                    break;
            
            }
        }
        w.writeEndElement();
    }

}


