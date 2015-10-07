//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import net.toppro.components.mls.engine.MLSUtil;

public class FieldBase  extends Object 
{
    public String caption;
    public String delimiter;
    public String name;
    public String xmlname;
    public String value_Renamed;
    public int type;
    public int width;
    public FieldBase() throws Exception {
        caption = "";
        delimiter = "";
        name = "";
        value_Renamed = "";
        type = 0;
        width = 0;
    }

    public void setCaption(String _caption) throws Exception {
        caption = _caption;
    }

    public String getCaption() throws Exception {
        return caption;
    }

    public void setDelimiter(String _delimiter) throws Exception {
        delimiter = _delimiter;
    }

    public String getDelimiter() throws Exception {
        return delimiter;
    }

    public void setName(String _name) throws Exception {
        name = _name.toUpperCase();
    }

    public void setXmlName(String _xmlname) throws Exception {
        xmlname = _xmlname;
    }

    public String getName() throws Exception {
        return name;
    }

    public String getXmlName() throws Exception {
        return xmlname;
    }

    public void setValue(String _value) throws Exception {
        value_Renamed = _value;
    }

    public String getValue() throws Exception {
        return value_Renamed;
    }

    public void setType(int _type) throws Exception {
        type = _type;
    }

    public void setType(String _type) throws Exception {
        new MLSUtil();
        if (MLSUtil.isNumber(_type) == true)
            type = Integer.valueOf(_type);
        else
            type = 0; 
    }

    public int getFieldType() throws Exception {
        return type;
    }

    public void setWidth(int _width) throws Exception {
        width = _width;
    }

    public void setWidth(String _width) throws Exception {
        new MLSUtil();
        if (MLSUtil.isNumber(_width) == true)
            width = Integer.valueOf(_width);
        else
            width = 0; 
    }

    public int getWidth() throws Exception {
        return width;
    }

}


