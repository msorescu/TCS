//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import net.toppro.components.mls.engine.DataAggResultFieldType;

public class DataAggResultFieldType   
{
    private String m_referenceName;
    private String m_systemName;
    private String m_displayName;
    private String m_retsLongName;
    private String m_standardName;
    private String m_dataTypeID;
    private String m_dataTypeDescription;
    private String m_isStandard;
    private String m_displayRule;
    private String m_normalized;
    private String m_category;
    public String getCategory() throws Exception {
        return m_category;
    }

    public void setCategory(String value) throws Exception {
        m_category = value;
    }

    private String m_orderInCategory;
    public String getOrderInCategory() throws Exception {
        return m_orderInCategory;
    }

    public void setOrderInCategory(String value) throws Exception {
        m_orderInCategory = value;
    }

    private String m_displayFormatString;
    public String getDisplayFormatString() throws Exception {
        return m_displayFormatString;
    }

    public void setDisplayFormatString(String value) throws Exception {
        m_displayFormatString = value;
    }

    private String m_attributeExposure;
    public String getAttributeExposure() throws Exception {
        return m_attributeExposure;
    }

    public void setAttributeExposure(String value) throws Exception {
        m_attributeExposure = value;
    }

    private String m_displayBehaviorBitmask;
    public String getDisplayBehaviorBitmask() throws Exception {
        return m_displayBehaviorBitmask;
    }

    public void setDisplayBehaviorBitmask(String value) throws Exception {
        m_displayBehaviorBitmask = value;
    }

    public String getNormalized() throws Exception {
        return m_normalized;
    }

    public void setNormalized(String value) throws Exception {
        m_normalized = value;
    }

    public DataAggResultFieldType() throws Exception {
    }


    //&&
    //sFT1.DisplayName == sFT2.DisplayName &&
    //sFT1.StandardName == sFT2.StandardName &&
    //sFT1.DataTypeID == sFT2.DataTypeID &&
    //sFT1.DataTypeDescription == sFT2.DataTypeDescription

    public String getReferenceName() throws Exception {
        return m_referenceName;
    }

    public void setReferenceName(String value) throws Exception {
        m_referenceName = value;
    }

    public String getSystemName() throws Exception {
        return m_systemName;
    }

    public void setSystemName(String value) throws Exception {
        m_systemName = value;
    }

    public String getDisplayName() throws Exception {
        return m_displayName;
    }

    public void setDisplayName(String value) throws Exception {
        m_displayName = value;
    }

    public String getStandardName() throws Exception {
        return m_standardName;
    }

    public void setStandardName(String value) throws Exception {
        m_standardName = value;
    }

    public String getRetsLongName() throws Exception {
        return m_retsLongName;
    }

    public void setRetsLongName(String value) throws Exception {
        m_retsLongName = value;
    }

    public String getDataTypeID() throws Exception {
        return m_dataTypeID;
    }

    public void setDataTypeID(String value) throws Exception {
        m_dataTypeID = value;
    }

    public String getDataTypeDescription() throws Exception {
        return m_dataTypeDescription;
    }

    public void setDataTypeDescription(String value) throws Exception {
        m_dataTypeDescription = value;
    }

    public String getIsStandard() throws Exception {
        return m_isStandard;
    }

    public void setIsStandard(String value) throws Exception {
        m_isStandard = value;
    }

    public String getDisplayRule() throws Exception {
        return m_displayRule;
    }

    public void setDisplayRule(String value) throws Exception {
        m_displayRule = value;
    }

}


