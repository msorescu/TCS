//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:32 PM
//

package net.toppro.components.mls.engine;


public class BoardSetupField  extends Object 
{
    public static final int INPUT_TYPE_STANDARD = 0;
    public static final int INPUT_TYPE_FILE = 1;
    public static final int INPUT_TYPE_DIRECTORY = 2;
    public static final int INPUT_TYPE_FINAL = 3;
    private String m_caption;
    private int m_inputType;
    private int m_layout;
    private boolean m_optional;
    private String m_name;
    private boolean m_visible;
    private boolean m_maskPassword;
    public boolean getMaskPassword() throws Exception {
        return m_maskPassword;
    }

    public void setMaskPassword(boolean value) throws Exception {
        m_maskPassword = value;
    }

    private String m_value = "";
    public BoardSetupField(String name, String caption, int inputType, int layout, boolean optional, boolean visible, boolean maskPassword) throws Exception {
        m_name = name;
        m_caption = caption;
        m_inputType = inputType;
        m_layout = layout;
        m_optional = optional;
        m_visible = visible;
        m_maskPassword = maskPassword;
    }

    public BoardSetupField(String name, String caption, int inputType, int layout, boolean optional, boolean visible) throws Exception {
        this(name, caption, inputType, layout, optional, visible, false);
    }

    public BoardSetupField(String name, String caption, int inputType, boolean optional) throws Exception {
        this(name, caption, inputType, 0, optional, true);
    }

    public BoardSetupField(String name, String caption, boolean optional) throws Exception {
        this(name, caption, INPUT_TYPE_STANDARD, 0, optional, true);
    }

    public BoardSetupField(String name, String caption, int inputType, int layout) throws Exception {
        this(name, caption, inputType, layout, false, true);
    }

    public BoardSetupField(String name, String caption, int inputType) throws Exception {
        this(name, caption, inputType, 0, false, true);
    }

    public BoardSetupField(String name, String caption) throws Exception {
        this(name, caption, INPUT_TYPE_STANDARD, 0, false, true);
    }

    public String getCaption() throws Exception {
        return m_caption;
    }

    public void setCaption(String caption) throws Exception {
        m_caption = caption;
    }

    public int getInputType() throws Exception {
        return m_inputType;
    }

    public void setInputType(int type) throws Exception {
        m_inputType = type;
    }

    public int getLayout() throws Exception {
        return m_layout;
    }

    public void setLayout(int layout) throws Exception {
        m_layout = layout;
    }

    public String getName() throws Exception {
        return m_name;
    }

    public void setName(String name) throws Exception {
        m_name = name;
    }

    public String getValue() throws Exception {
        return m_value;
    }

    public void setValue(String value_Renamed) throws Exception {
        m_value = value_Renamed;
    }

    public boolean isOptional() throws Exception {
        return m_optional;
    }

    public void setOptional(boolean optional) throws Exception {
        m_optional = optional;
    }

    public boolean isVisible() throws Exception {
        return m_visible;
    }

}


