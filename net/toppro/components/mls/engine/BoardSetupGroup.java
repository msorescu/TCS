//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:32 PM
//

package net.toppro.components.mls.engine;

import java.util.ArrayList;
import net.toppro.components.mls.engine.BoardSetupField;

public class BoardSetupGroup  extends Object 
{
    public static final int GROUP_TYPE_IP = 0;
    public static final int GROUP_TYPE_SECLIST = 1;
    private String m_caption;
    private int m_type;
    private ArrayList m_fields = ArrayList.Synchronized(new ArrayList(10));
    public BoardSetupGroup(String caption, int type) throws Exception {
        m_caption = caption;
        m_type = type;
    }

    public BoardSetupGroup(String caption) throws Exception {
        this(caption, GROUP_TYPE_SECLIST);
    }

    public String getCaption() throws Exception {
        return m_caption;
    }

    public void setCaption(String caption) throws Exception {
        m_caption = caption;
    }

    public int getGroupType() throws Exception {
        return m_type;
    }

    public void setType(int type) throws Exception {
        m_type = type;
    }

    public int getFieldsSize() throws Exception {
        return m_fields.size();
    }

    public BoardSetupField getField(int i) throws Exception {
        return (BoardSetupField)m_fields.get(i);
    }

    public BoardSetupField getField(String name) throws Exception {
        for (int i = 0;i < m_fields.size();i++)
        {
            BoardSetupField field = (BoardSetupField)m_fields.get(i);
            if (field.getName().toUpperCase().equals(name.toUpperCase()))
                return field;
             
        }
        return null;
    }

    public void insertField(int i, BoardSetupField field) throws Exception {
        m_fields.add(i, field);
    }

    public void addField(BoardSetupField field) throws Exception {
        m_fields.add(field);
    }

    public void removeField(int i) throws Exception {
        m_fields.remove(i);
    }

    public boolean removeField(BoardSetupField field) throws Exception {
        boolean tempBoolean;
        tempBoolean = m_fields.contains(field);
        m_fields.remove(field);
        return tempBoolean;
    }

}


