//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:36 PM
//

package net.toppro.components.mls.engine.client;


// return ResponseText;
// http://gee.cs.oswego.edu/dl/cpj/cancel.html
/**
* Http Header Item.
*/
//UPGRADE_NOTE: The access modifier for this class or class field has been changed in order to prevent compilation errors due to the visibility level. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1296'"
public class HttpHeaderItem   
{
    /* Item's key Name */
    private String key = "";
    /*  Item's value */
    private String value_Renamed = "";
    /**
    * Constructor HttpHeaderItem with Item's keyName and Item's value
    *  @param _key Item's KeyName
    * 
    *  @param _value Item's value
    */
    public HttpHeaderItem(String _key, String _value) throws Exception {
        key = _key;
        value_Renamed = _value;
    }

    /**
    * Gets the Item's key Name
    */
    public String getKeyName() throws Exception {
        return key;
    }

    /**
    * Gets the Item's Value
    */
    public String getValue() throws Exception {
        return value_Renamed;
    }

    /**
    * 
    */
    public void changeValue(String _newValue) throws Exception {
        value_Renamed = _newValue;
    }

}


