//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:32 PM
//

package net.toppro.components.mls.engine;

import java.util.ArrayList;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.BoardSetupGroup;
import net.toppro.components.mls.engine.STRINGS;

public class BoardSetup   
{
    private void initBlock() throws Exception {
        m_caption = STRINGS.get_Renamed(STRINGS.BOARD_SETUP_LOGIN_SETUP_CAPTION);
    }

    public static final String ADDRESS_FIELD_NAME = "Address";
    public static final String PORT_FIELD_NAME = "Port";
    private static final int IP_ADDRESS_IN_GROUP = 0;
    private static final int IP_PORT_IN_GROUP = 1;
    private net.toppro.components.mls.engine.MLSEngine m_engine;
    private ArrayList m_groups = ArrayList.Synchronized(new ArrayList(10));
    //UPGRADE_NOTE: The initialization of  'm_caption' was moved to method 'InitBlock'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1005'"
    private String m_caption;
    private String m_notes = null;
    public BoardSetup(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        initBlock();
        m_engine = engine;
    }

    public String getCaption() throws Exception {
        return m_caption;
    }

    public void setCaption(String caption) throws Exception {
        m_caption = caption;
    }

    public String getNotes() throws Exception {
        if (m_notes == null)
            m_notes = m_engine.getSetupNotes();
         
        return m_notes;
    }

    public void setNotes(String notes) throws Exception {
        m_notes = notes;
    }

    public int getGroupsSize() throws Exception {
        return m_groups.size();
    }

    public BoardSetupGroup getGroup(int i) throws Exception {
        return (BoardSetupGroup)m_groups.get(i);
    }

    public BoardSetupField getSecField(String name) throws Exception {
        for (int i = 0;i < m_groups.size();i++)
        {
            BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
            if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_SECLIST)
            {
                BoardSetupField field = group.getField(name);
                if (field != null)
                    return field;
                 
            }
             
        }
        return null;
    }

    public BoardSetupField getSecField(int index) throws Exception {
        if (index >= 0)
        {
            int groups = m_groups.size();
            for (int i = 0;i < groups;i++)
            {
                BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
                if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_SECLIST)
                {
                    int fields = group.getFieldsSize();
                    if (fields > index)
                        return group.getField(index);
                    else
                        index -= fields; 
                }
                 
            }
        }
         
        return null;
    }

    public net.toppro.components.mls.engine.BoardSetup.SecFieldIterator getSecFieldIterator() throws Exception {
        return new net.toppro.components.mls.engine.BoardSetup.SecFieldIterator(this);
    }

    public void insertGroup(int i, BoardSetupGroup group) throws Exception {
        m_groups.add(i, group);
    }

    public void addGroup(BoardSetupGroup group) throws Exception {
        m_groups.add(group);
    }

    public void removeGroup(int i) throws Exception {
        m_groups.remove(i);
    }

    public boolean removeGroup(BoardSetupGroup group) throws Exception {
        boolean tempBoolean;
        tempBoolean = m_groups.contains(group);
        m_groups.remove(group);
        return tempBoolean;
    }

    public boolean checkSecValues() throws Exception {
        for (int i = m_groups.size() - 1;i >= 0;i--)
        {
            BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
            if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_SECLIST)
            {
                for (int j = group.getFieldsSize() - 1;j >= 0;j--)
                {
                    BoardSetupField field = group.getField(j);
                    if (!field.isOptional() && field.getInputType() != BoardSetupField.INPUT_TYPE_FINAL)
                    {
                        String value_Renamed = field.getValue();
                        if ((value_Renamed == null || value_Renamed.length() <= 0))
                            return false;
                         
                    }
                     
                }
            }
             
        }
        return true;
    }

    public String getIPAddress() throws Exception {
        for (int i = m_groups.size() - 1;i >= 0;i--)
        {
            BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
            if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_IP && group.getFieldsSize() > IP_ADDRESS_IN_GROUP)
                return group.getField(IP_ADDRESS_IN_GROUP).getValue();
             
        }
        return "";
    }

    public boolean setIPAddress(String address) throws Exception {
        for (int i = m_groups.size() - 1;i >= 0;i--)
        {
            BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
            if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_IP && group.getFieldsSize() > IP_ADDRESS_IN_GROUP)
            {
                group.getField(IP_ADDRESS_IN_GROUP).setValue(address);
                return true;
            }
             
        }
        return false;
    }

    public String getIPPort() throws Exception {
        for (int i = m_groups.size() - 1;i >= 0;i--)
        {
            BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
            if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_IP && group.getFieldsSize() > IP_PORT_IN_GROUP)
                return group.getField(IP_PORT_IN_GROUP).getValue();
             
        }
        return "" + net.toppro.components.mls.engine.MLSEngine.DEFAULT_PORT_NUMBER;
    }

    public boolean setIPPort(String port) throws Exception {
        for (int i = m_groups.size() - 1;i >= 0;i--)
        {
            BoardSetupGroup group = (BoardSetupGroup)m_groups.get(i);
            if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_IP && group.getFieldsSize() > IP_PORT_IN_GROUP)
            {
                group.getField(IP_PORT_IN_GROUP).setValue(port);
                return true;
            }
             
        }
        return false;
    }

    //UPGRADE_NOTE: Field 'EnclosingInstance' was added to class 'SecFieldIterator' to access its enclosing instance. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1019'"
    public static class SecFieldIterator   
    {
        public SecFieldIterator(BoardSetup enclosingInstance) throws Exception {
            initBlock(enclosingInstance);
        }

        private void initBlock(BoardSetup enclosingInstance) throws Exception {
            this.enclosingInstance = enclosingInstance;
        }

        private BoardSetup enclosingInstance;
        public BoardSetup getEnclosing_Instance() throws Exception {
            return enclosingInstance;
        }

        private int m_group = -1;
        private int m_field = -1;
        public BoardSetupField get_Renamed() throws Exception {
            if (m_group >= 0 && m_group < getEnclosing_Instance().getGroupsSize())
            {
                BoardSetupGroup group = getEnclosing_Instance().getGroup(m_group);
                if (m_field >= 0 && m_field < group.getFieldsSize())
                    return group.getField(m_field);
                 
            }
             
            return null;
        }

        public BoardSetupField first() throws Exception {
            int groups = getEnclosing_Instance().getGroupsSize();
            for (int i = 0;i < groups;i++)
            {
                BoardSetupGroup group = getEnclosing_Instance().getGroup(i);
                if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_SECLIST && group.getFieldsSize() > 0)
                {
                    m_group = i;
                    m_field = 0;
                    return group.getField(0);
                }
                 
            }
            m_group = -1;
            m_field = -1;
            return null;
        }

        public BoardSetupField next() throws Exception {
            int groups = getEnclosing_Instance().getGroupsSize();
            if (m_group >= 0 && m_group < groups)
            {
                BoardSetupGroup group = getEnclosing_Instance().getGroup(m_group);
                m_field++;
                if (m_field >= 0 && m_field < group.getFieldsSize())
                {
                    return group.getField(m_field);
                }
                else
                {
                    for (int i = m_group + 1;i < groups;i++)
                    {
                        group = getEnclosing_Instance().getGroup(i);
                        if (group.getGroupType() == BoardSetupGroup.GROUP_TYPE_SECLIST && group.getFieldsSize() > 0)
                        {
                            m_group = i;
                            m_field = 0;
                            return group.getField(0);
                        }
                         
                    }
                    m_group = -1;
                    m_field = -1;
                } 
            }
             
            return null;
        }
    
    }

    //+-----------------------------------------------------------------------------------+
    //|								  Serialization										  |
    //+-----------------------------------------------------------------------------------+
    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
    public void save(System.IO.BinaryWriter out_Renamed) throws Exception {
        try
        {
            out_Renamed.Write(m_groups.size());
            for (int i = 0;i < m_groups.size();i++)
            {
                //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
                SupportClass.serialize(out_Renamed,(BoardSetupGroup)m_groups.get(i));
            }
            //UPGRADE_TODO: Method 'java.io.ObjectOutputStream.writeObject' was converted to 'SupportClass.Serialize' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStreamwriteObject_javalangObject'"
            SupportClass.serialize(out_Renamed,m_notes == null ? "" : m_notes);
        }
        catch (Exception e)
        {
            throw m_engine.createException(e);
        }
    
    }

    /**
    * 
    */
    //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
    public void load(System.IO.BinaryReader in_Renamed) throws Exception {
        try
        {
            int size = in_Renamed.Read();
            for (int i = 0;i < size;i++)
            {
                //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                m_groups.add((BoardSetupGroup)SupportClass.deserialize(in_Renamed));
            }
            //UPGRADE_WARNING: Method 'java.io.ObjectInputStream.readObject' was converted to 'SupportClass.Deserialize' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            m_notes = ((String)SupportClass.deserialize(in_Renamed));
        }
        catch (Exception e)
        {
            throw m_engine.createException(e);
        }
    
    }

}


