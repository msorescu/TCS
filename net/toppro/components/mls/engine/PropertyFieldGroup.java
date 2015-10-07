//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:48 PM
//

package net.toppro.components.mls.engine;

import java.util.ArrayList;
import net.toppro.components.mls.engine.DefParser;

public class PropertyFieldGroup  extends Object 
{
    private static final String FIELD_PREFIX = "__CUST__";
    private boolean m_bIsDefault = false;
    private String m_Name = "";
    private String m_Caption = "";
    private ArrayList m_PropertyFields = ArrayList.Synchronized(new ArrayList(10));
    /**
    * @return  size of group
    */
    public int size() throws Exception {
        return m_PropertyFields.size();
    }

    /**
    * @return  array of fields
    */
    public net.toppro.components.mls.engine.PropertyField[] toArray() throws Exception {
        net.toppro.components.mls.engine.PropertyField[] pf = new net.toppro.components.mls.engine.PropertyField[m_PropertyFields.size()];
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            pf[i] = f;
        }
        return pf;
    }

    /**
    * Sets this group as default
    */
    public void setDefault(boolean _isDefault) throws Exception {
        m_bIsDefault = _isDefault;
    }

    /**
    * Gets default property
    */
    public boolean getDefault() throws Exception {
        return m_bIsDefault;
    }

    /**
    * Sets name of group
    */
    public void setName(String _Name) throws Exception {
        m_Name = _Name;
    }

    /**
    * Gets name of group
    */
    public String getName() throws Exception {
        return m_Name;
    }

    /**
    * Sets caption of group
    */
    public void setCaption(String _Caption) throws Exception {
        m_Caption = _Caption;
    }

    /**
    * Gets caption of group
    */
    public String getCaption() throws Exception {
        return m_Caption;
    }

    /**
    * remove field
    */
    public void removeField(String _name) throws Exception {
        int index = getFieldIndex(_name);
        if (index >= 0)
            m_PropertyFields.remove(index);
         
    }

    /**
    * This function should be called from the constructors of some communication clients
    * (see ASCIIClient)
    */
    public void addField(net.toppro.components.mls.engine.PropertyField field) throws Exception {
        m_PropertyFields.add(field);
    }

    /**
    * @param _name name of field
    * 
    *  @return  field by name
    */
    public net.toppro.components.mls.engine.PropertyField getField(String _name) throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField field = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (field.getName().toUpperCase().equals(_name.toUpperCase()))
                return field;
             
        }
        return null;
    }

    /**
    * @param _name name of field
    * 
    *  @return  field's index  by name
    */
    public int getFieldIndex(String _name) throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField field = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (field.getName().toUpperCase().equals(_name.toUpperCase()))
                return i;
             
        }
        return -1;
    }

    /**
    * @param item index of field
    * 
    *  @return  field by index
    */
    public net.toppro.components.mls.engine.PropertyField getField(int item) throws Exception {
        if (item >= 0 && item < m_PropertyFields.size())
            return (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(item);
         
        return null;
    }

    /**
    * Inits a group
    *  @param GroupName - Group's name which we need to initialize
    * 
    *  @param MLSEngine - engine, needs for exceptions
    */
    public void init(String GroupName, String Caption, net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        DefParser parser = engine.getDefParser();
        if (parser == null)
            return ;
         
        setCaption(Caption);
        String[] Fields = parser.getAttributiesFor(GroupName);
        //For TCS to have agent_id, office_id which should not appear on 6i and 7i
        if (engine.isAddHiddenSearchFields())
        {
            String[] tcsFields = parser.getAttributiesFor(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELD_TCS);
            if (tcsFields != null && tcsFields.length > 0)
            {
                String[] array = new String[Fields.length + tcsFields.length];
                Array.Copy(Fields, 0, array, 0, Fields.length);
                Array.Copy(tcsFields, 0, array, Fields.length, tcsFields.length);
                Fields = array;
            }
             
        }
         
        if (Fields == null)
            return ;
         
        String Value = "";
        String prop = "";
        int i, j;
        net.toppro.components.mls.engine.PropertyField pf;
        for (i = 0;i < Fields.length;i++)
        {
            Value = parser.getValue(GroupName,Fields[i]);
            pf = new net.toppro.components.mls.engine.PropertyField();
            if (!Value.startsWith(FIELD_PREFIX))
                pf.setPrefix(Value);
             
            for (j = 0;j < net.toppro.components.mls.engine.PropertyField.PROPERTIES_NAMES.length;j++)
            {
                prop = net.toppro.components.mls.engine.PropertyField.PROPERTIES_NAMES[j];
                pf.setPropertyValue(engine,prop,parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELD_ + Fields[i],prop));
            }
            pf.setName(Fields[i]);
            pf.setValue(Value);
            Value = " ";
            j = 1;
            while (Value.length() != 0)
            {
                Value = parser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_FIELD_ + Fields[i],net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_VALUE + (j++));
                if (Value.length() != 0)
                    pf.addInitValue(Value);
                 
            }
            if (pf.getCaption().length() == 0)
                pf.setCaption(Fields[i]);
             
            m_PropertyFields.add(pf);
        }
    }

    // end of init
    public void clearValues() throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
            ((net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i)).setValue("");
    }

    /**
    * 
    */
    public boolean checkRequeredFields() throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (f.isRequired() && f.getValue().length() == 0)
                return false;
             
        }
        return true;
    }

    /**
    * 
    */
    public String checkRequiredFields() throws Exception {
        for (int i = 0;i < m_PropertyFields.size();i++)
        {
            net.toppro.components.mls.engine.PropertyField f = (net.toppro.components.mls.engine.PropertyField)m_PropertyFields.get(i);
            if (f.isRequired() && f.getValue().length() == 0)
                return f.getName();
             
        }
        return "";
    }

}


