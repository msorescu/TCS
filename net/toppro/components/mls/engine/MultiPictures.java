//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:47 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.StringSupport;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSUtil;

public class MultiPictures   
{
    public net.toppro.components.mls.engine.MLSEngine m_engine = null;
    private boolean m_isInitialized = false;
    private boolean m_isGetMultiPicture = false;
    private boolean m_isComparePics = false;
    private byte[] m_previousPicture = null;
    private byte[] m_currentPicture = null;
    private MPictureFormat[] m_MPictureFormat = null;
    public MultiPictures(net.toppro.components.mls.engine.MLSEngine engine) throws Exception {
        m_engine = engine;
        init();
    }

    private void init() throws Exception {
        boolean b = false;
        b = (m_engine.getEnvironment().getFlags() & Environment.FLAG_GET_MULTI_PICTURE) != 0;
        String clientType = m_engine.getClientType();
        String[] picFormat = new String[10];
        int i;
        for (i = 0;i < 10;i++)
        {
            if (i == 0)
                picFormat[i] = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_MPICFORMAT);
            else
                picFormat[i] = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_MPICFORMAT + i); 
            if (picFormat[i] == null || picFormat[i].length() == 0)
                break;
             
        }
        if (b && ((picFormat[0] != null && picFormat[0].length() > 0) || Integer.valueOf(clientType) == net.toppro.components.mls.engine.MLSEngine.CLIENT_TYPE_MRISRETS))
            m_isGetMultiPicture = true;
        else
            m_isGetMultiPicture = false; 
        String compareFlag = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_MPICCOMPARE);
        if (compareFlag != null && compareFlag.length() > 0)
            m_isComparePics = compareFlag.equals("1");
         
        m_MPictureFormat = new MPictureFormat[i];
        for (int j = 0;j < i;j++)
        {
            m_MPictureFormat[j] = new MPictureFormat(picFormat[j]);
        }
        m_isInitialized = true;
    }

    public boolean isComparePictures() throws Exception {
        return m_isComparePics;
    }

    public boolean isGetMultiPicture() throws Exception {
        return m_isGetMultiPicture;
    }

    //UPGRADE_NOTE: Field 'EnclosingInstance' was added to class 'MPictureFormat' to access its enclosing instance. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1019'"
    private static class MPictureFormat   
    {
        public String searchSubstring;
        public String searchPosition;
        public String beginFrom;
        public String startPrefix = "";
        public int searchDirection;
        public String picFormat = "";
        public boolean isGetMultiPicture = false;
        public int valueAdjustment = 0;
        public boolean isSkipFirstURL = false;
        private String PRE_POST_FIX = "<>";
        public int positionOffset = 0;
        public int selectedLength = 0;
        public int incrementValue = 0;
        public MPictureFormat(String format) throws Exception {
            //InitBlock(enclosingInstance);
            String[] arrayFormat;
            //String s = getPicFormatFromDEF();
            arrayFormat = MLSUtil.stringSplit(format,",");
            searchSubstring = StringSupport.Trim(arrayFormat[0]);
            searchPosition = StringSupport.Trim(arrayFormat[1]);
            if (searchPosition.contains(":"))
            {
                int pos = searchPosition.indexOf(":");
                selectedLength = Integer.valueOf(searchPosition.substring(pos + 1));
                searchPosition = searchPosition.substring(0, (0) + (pos));
                if ((searchPosition.startsWith("<") || searchPosition.startsWith(">")) && searchPosition.length() > 1)
                {
                    positionOffset = Integer.valueOf(searchPosition.substring(1));
                    searchPosition = searchPosition.substring(0, (0) + (1));
                }
                 
            }
             
            beginFrom = arrayFormat[2];
            if (beginFrom.startsWith("_"))
            {
                isSkipFirstURL = true;
                beginFrom = beginFrom.substring(1, (1) + (beginFrom.length() - 1));
                valueAdjustment = -1;
                if (beginFrom.startsWith("_"))
                {
                    startPrefix = "_";
                    beginFrom = beginFrom.substring(1, (1) + (beginFrom.length() - 1));
                }
                 
            }
            else if (beginFrom.startsWith("+"))
            {
                incrementValue = Integer.valueOf(beginFrom.substring(1));
            }
              
            if (arrayFormat.length == 3)
                searchDirection = 1;
            else if (arrayFormat.length == 4)
                searchDirection = Integer.valueOf(arrayFormat[3]);
            else
                System.out.println("Invalid multiple picture format in DEF");  
        }
    
    }

    public String[] getURLAndFilePath(String url, String filePath, int value_Renamed) throws Exception {
        String[] result = new String[]{ "", "" };
        for (int i = 0;i < m_MPictureFormat.length;i++)
        {
            try
            {
                int pos = 0;
                String beginFrom = m_MPictureFormat[i].beginFrom;
                if (m_MPictureFormat[i].searchDirection == 1)
                    pos = url.lastIndexOf(m_MPictureFormat[i].searchSubstring);
                else
                    pos = url.indexOf(m_MPictureFormat[i].searchSubstring); 
                if (m_MPictureFormat[i].searchPosition.equals("<"))
                {
                    pos = pos - m_MPictureFormat[i].positionOffset;
                }
                else if (m_MPictureFormat[i].searchPosition.equals(">"))
                    pos = pos + m_MPictureFormat[i].searchSubstring.length() + m_MPictureFormat[i].positionOffset;
                else
                    pos += Integer.valueOf(m_MPictureFormat[i].searchPosition) - 1;  
                String prefix = url.substring(0, (0) + (pos));
                if (beginFrom.contains("+"))
                    beginFrom = url.substring(pos, (pos) + (m_MPictureFormat[i].selectedLength));
                 
                String postfix = "";
                if (pos < url.length() - 1)
                {
                    if (m_MPictureFormat[i].searchPosition.equals("<") || m_MPictureFormat[i].searchPosition.equals(">"))
                        postfix = url.substring(pos + m_MPictureFormat[i].selectedLength);
                    else
                        postfix = url.substring(pos + m_MPictureFormat[i].selectedLength + 1); 
                }
                 
                String str = "";
                if (value_Renamed == 0 && m_MPictureFormat[i].isSkipFirstURL)
                {
                    result[0] = url;
                    result[1] = filePath + ".jpg";
                }
                else
                {
                    value_Renamed = value_Renamed + m_MPictureFormat[i].valueAdjustment;
                    if (MLSUtil.isNumber(beginFrom))
                    {
                        str = Integer.toString(Integer.valueOf(beginFrom) + (m_MPictureFormat[i].incrementValue > 0 ? m_MPictureFormat[i].incrementValue : 1) * value_Renamed);
                        if (m_MPictureFormat[i].selectedLength > 0 && str.length() < m_MPictureFormat[i].selectedLength)
                        {
                            while (m_MPictureFormat[i].selectedLength > str.length())
                            {
                                str = "0" + str;
                            }
                        }
                         
                    }
                    else
                    {
                        char c = beginFrom.charAt(0);
                        c = (char)((int)c + value_Renamed);
                        str = Convert.ToString(c);
                    } 
                    result[0] = prefix + m_MPictureFormat[i].startPrefix + str + postfix;
                    result[1] = filePath + "_" + str + ".jpg";
                } 
                url = result[0];
            }
            catch (Exception exc)
            {
                throw m_engine.createException(exc,"Invalid multiple picture format in DEF");
            }
        
        }
        return result;
    }

    public boolean isSamePicture() throws Exception {
        if (m_previousPicture == null || m_currentPicture == null)
            return false;
        else
        {
            if (m_previousPicture.length != m_currentPicture.length)
                return false;
            else
            {
                int diff = 0;
                int same = 0;
                int sameMax = m_currentPicture.length / 5;
                for (int i = 0;i < m_previousPicture.length;i++)
                {
                    if (m_previousPicture[i] != m_currentPicture[i])
                        diff++;
                    else
                        same++; 
                    if (same > sameMax)
                        return true;
                     
                    if (diff > sameMax)
                        return false;
                     
                }
            } 
        } 
        return true;
    }

    public void setCurrentPicture(byte[] data) throws Exception {
        m_previousPicture = m_currentPicture;
        m_currentPicture = data;
    }

    public void clearPictureData() throws Exception {
        m_previousPicture = null;
        m_currentPicture = null;
    }

}


