//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:36 PM
//

package net.toppro.components.mls.engine.client;

import CS2JNet.System.StringSupport;
import java.util.ArrayList;
import net.toppro.components.mls.engine.client.Digest;
import net.toppro.components.mls.engine.client.HttpHeaderItem;
import net.toppro.components.mls.engine.MLSUtil;

/**
* Http Header.
* This is implementation work Http Headers.
*/
//UPGRADE_NOTE: The access modifier for this class or class field has been changed in order to prevent compilation errors due to the visibility level. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1296'"
public class HttpHeader   
{
    public static final String HTTP_HEAD_AUTHEN = "WWW-Authenticate";
    public static final String HTTP_AUTHENTICATION_BASIC = "BASIC";
    public static final String HTTP_AUTHENTICATION_DIGEST = "DIGEST";
    public static final String HTTP_CONTENT_LENGTH = "CONTENT-LENGTH";
    public static final String HTTP_TRANSFER_ENCODING = "TRANSFER-ENCODING";
    /* Vector of HttpHeaderItems */
    private ArrayList headers = null;
    /* Current position in Vector */
    private int iterator = 0;
    /**
    * Http digest related
    * Added by Lawrence Zhou
    */
    private Digest m_digest;
    public Digest getDigest() throws Exception {
        return m_digest;
    }

    public boolean isDigestAuthentication() throws Exception {
        String sAuthenticate = get_Renamed(HTTP_HEAD_AUTHEN);
        if (sAuthenticate != null && StringSupport.Trim(sAuthenticate).toUpperCase().startsWith(HTTP_AUTHENTICATION_DIGEST))
            return true;
         
        return false;
    }

    public boolean isBasicAuthentication() throws Exception {
        String sAuthenticate = get_Renamed(HTTP_HEAD_AUTHEN);
        if (sAuthenticate != null && StringSupport.Trim(sAuthenticate).toUpperCase().startsWith(HTTP_AUTHENTICATION_BASIC))
            return true;
         
        return false;
    }

    public void parseDigest() throws Exception {
        if (!isDigestAuthentication())
            return ;
         
        String sAuthenticate = get_Renamed(HTTP_HEAD_AUTHEN);
        if (sAuthenticate != null)
        {
            m_digest = new Digest();
            m_digest.m_sQop = MLSUtil.getPara(sAuthenticate,"qop","=",",");
            m_digest.m_sRealm = MLSUtil.getPara(sAuthenticate,"realm","=",",");
            m_digest.m_sNonce = MLSUtil.getPara(sAuthenticate,"nonce","=",",");
            m_digest.m_sOpaque = MLSUtil.getPara(sAuthenticate,"opaque","=",",");
        }
         
    }

    //End of digest related
    /**
    * Empty Constructor
    */
    public HttpHeader() throws Exception {
        headers = ArrayList.Synchronized(new ArrayList(10));
        iterator = 0;
    }

    /**
    * Deletes the Item at the specified index.
    *  @param index   the index of the Item to remove.
    */
    public void removeItem(int index) throws Exception {
        if (index < headers.size())
            headers.remove(index);
         
    }

    /**
    * Deletes all items.
    */
    public void clear() throws Exception {
        headers.clear();
        iterator = 0;
    }

    /**
    * Returns the first Item of this Item's vector.
    */
    public HttpHeaderItem getFirst() throws Exception {
        iterator = 0;
        if (headers.size() != 0)
            return (HttpHeaderItem)headers.get(iterator);
         
        return null;
    }

    /**
    * Returns the last Item of this Item's vector.
    */
    public HttpHeaderItem getLast() throws Exception {
        if (headers.size() != 0)
        {
            iterator = headers.size();
            return (HttpHeaderItem)headers.get(iterator);
        }
        else
            iterator = 0; 
        return null;
    }

    /**
    * Returns the next Item of this vector.
    */
    public HttpHeaderItem getNext() throws Exception {
        iterator++;
        if (iterator < headers.size())
            return (HttpHeaderItem)headers.get(iterator);
         
        return null;
    }

    /**
    * Returns the prevous Item of this vector.
    */
    public HttpHeaderItem getPrev() throws Exception {
        iterator--;
        if (iterator >= 0)
            return (HttpHeaderItem)headers.get(iterator);
         
        return null;
    }

    /**
    * Adds the specified HttpHeaderItem to the end of this Item's vector,
    * 
    * 
    *  @param _keyName Item's KeyName
    * 
    *  @param _itemValue Item's value
    */
    public void add(String _keyName, String _itemValue) throws Exception {
        if (headers.size() > 0)
        {
            HttpHeaderItem item = null;
            for (int i = headers.size() - 1;i >= 0;i--)
            {
                item = (HttpHeaderItem)headers.get(i);
                if (item.getKeyName().equals(_keyName))
                {
                    ((HttpHeaderItem)headers.get(i)).changeValue(_itemValue);
                    return ;
                }
                 
            }
        }
         
        HttpHeaderItem hi = new HttpHeaderItem(_keyName,_itemValue);
        headers.add(hi);
    }

    /**
    * Gets Item's value at Item's KeyName
    *  @param _keyName Item's KeyName
    * 
    *  @return  if Item found returns value, else null
    */
    public String get_Renamed(String _keyName) throws Exception {
        String value_Renamed = null;
        for (int i = 0;i < headers.size();i++)
            if (((HttpHeaderItem)headers.get(i)).getKeyName().toUpperCase().equals(_keyName.toUpperCase()) == true)
            {
                if (value_Renamed == null)
                    value_Renamed = new StringBuilder().toString();
                 
                if (value_Renamed.length() != 0)
                    value_Renamed += "\r\n";
                 
                value_Renamed += ((HttpHeaderItem)headers.get(i)).getValue();
            }
             
        if (value_Renamed != null)
            value_Renamed = StringSupport.Trim(value_Renamed);
         
        return value_Renamed;
    }

    /**
    * Gets Item's value at special Index
    *  @param _keyNameIndex Item's Index in KeyName's vector
    * 
    *  @return  if ItemIndex valid - returns value, else null
    */
    public String get_Renamed(int _keyNameIndex) throws Exception {
        if (_keyNameIndex >= headers.size())
            return null;
         
        return ((HttpHeaderItem)headers.get(_keyNameIndex)).getValue();
    }

    public int count() throws Exception {
        return headers.size();
    }

}


