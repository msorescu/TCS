//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:54 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.StringSupport;
import CS2JNet.System.Text.StringBuilderSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import net.toppro.components.mls.engine.MLSRecord;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class GetMLSFieldsCriteria  extends TPAppRequest 
{
    private static final String COMPATIBILITY_ID = "8";
    private static final String FIELD_XML_ELEMENT = "<{0} SystemName=\"{1}\" Value=\"{2}\"/>";
    public GetMLSFieldsCriteria(TCServer tcs) throws Exception {
        super(tcs);
    }

    public void run() throws Exception {
        long time;
        m_resultBuffer = new StringBuilder();
        m_resultBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\">");
        try
        {
            time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
            try
            {
                m_defList = m_connector.getDEFList(getCompatibilityID());
            }
            catch (TCSException ex)
            {
                if (ex.getErrorCode() == TCSException.BLL_ERROR_GET_DEF)
                    throw TCSException.produceTCSException(TCSException.BOARD_NOT_EXIST);
                else
                    throw ex; 
            }

            m_engine = m_connector.createEngine(m_defList.getChildNodes().get(0),this);
            m_connector.setCurrentDEF(m_defList.getChildNodes().get(0));
            m_engine.setCurrentGroup(1);
            Hashtable fields = m_connector.getSearchFields();
            String field = "";
            String systemName = "";
            String value = "";
            net.toppro.components.mls.engine.PropertyField pf = null;
            for (Object __dummyForeachVar0 : fields.entrySet())
            {
                Map.Entry de = (Map.Entry)__dummyForeachVar0;
                field = (String)de.getKey();
                value = (String)de.getValue();
                systemName = m_engine.getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_AUTOSEARCH,field);
                if (!StringSupport.isNullOrEmpty(systemName))
                {
                    pf = m_engine.getPropertyFields().getField(systemName);
                    if (pf != null)
                    {
                        if (StringSupport.Compare(field, "ST_PType", true) == 0)
                        {
                            //TCSMLSSync.CCMA_ROClass ro = new TCSMLSSync.CCMA_ROClass();
                            //value = ro.GetPropertySubType(m_connector.getBoardID(), m_connector.getConnectionName(), value);
                            value = m_connector.getPropertySubType();
                        }
                        else
                        {
                            if (StringSupport.Compare(field, "ST_Zip", true) == 0)
                            {
                                String[] zips = StringSupport.Split(value, ',');
                                value = "";
                                for (int i = 0;i < zips.length;i++)
                                {
                                    value += MLSRecord.validateZipCode(zips[i]);
                                    if (i != (zips.length - 1))
                                        value += ",";
                                     
                                }
                            }
                             
                            if (StringSupport.Compare(field, "ST_Status", true) == 0)
                            {
                                String[] status = StringSupport.Split(value, ',');
                                value = "";
                                for (int i = 0;i < status.length;i++)
                                {
                                    value += getSTStatus(status[i]);
                                    if (i != (status.length - 1))
                                        value += ",";
                                     
                                }
                            }
                            else if (pf.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST || pf.getControlType() == net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICKLIST_TYPEABLE)
                            {
                                value = pf.getLookupCode(value);
                                if (value == null)
                                    value = "";
                                 
                            }
                              
                        } 
                    }
                     
                }
                 
                m_resultBuffer.append(String.format(StringSupport.CSFmtStrToJFmtStr(FIELD_XML_ELEMENT),field,systemName,Tcs.Mls.Util.convertStringToXML(value)));
            }
            m_resultBuffer.append("</TCResult>\r\n");
        }
        catch (Exception exc)
        {
            TCSException e = TCSException.produceTCSException(exc);
            m_resultBuffer.delete(0, (0)+(m_resultBuffer.length())).append(e.getOutputErrorXml());
        }
    
    }

    protected public String getCompatibilityID() throws Exception {
        return COMPATIBILITY_ID;
    }

}


