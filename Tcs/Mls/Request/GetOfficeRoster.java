//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:54 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.StringSupport;
import java.util.HashMap;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.Request.GetAgentRoster;
import Tcs.Mls.TCServer;

public class GetOfficeRoster  extends GetAgentRoster 
{
    public GetOfficeRoster(TCServer tcs) throws Exception {
        super(tcs);
        TCSCount = new HashMap<String,Integer>();
        TCSCount.put("Office", 0);
        TCSMLSNumbers.Add("Office", new HashSet<String>());
    }

    protected public void getOutputXML() throws Exception {
        m_connector.WriteLine("GetOutPutXML");
        if (m_engine.IsDownloadIDs)
        {
            AddIDsForDownloadIDOnlySearch();
            return ;
        }
         
        //System.Text.StringBuilder sb = new System.Text.StringBuilder();
        boolean isDemo = m_engine.isDemoClient();
        boolean foundDemoListing = false;
        MLSCmaFields fields = null;
        fields = m_engine.getCmaFields();
        net.toppro.components.mls.engine.CmaField cf = fields.getField("STDFLastMod");
        boolean resultLastModMapped = true;
        String lastModifiedDateFormat = "";
        if (cf != null)
        {
            lastModifiedDateFormat = cf.getDateFormat();
            if (StringSupport.isNullOrEmpty(lastModifiedDateFormat))
            {
                if (cf.type == net.toppro.components.mls.engine.CmaField.CMA_FLDTYPE_DATETIME)
                    lastModifiedDateFormat = STANDARD_DATETIMEFORMAT;
                else
                    lastModifiedDateFormat = STANDARD_DATEFORMAT; 
            }
             
        }
        else
            resultLastModMapped = false; 
        if (StringSupport.isNullOrEmpty(lastModifiedDateFormat))
            lastModifiedDateFormat = "";
         
        m_records = m_engine.getMLSRecords(MLSRecords.FILTER_ALL);
        MLSRecord rec = m_records.getFirstRecord();
        String fieldValue = "";
        String fieldName = "";
        boolean bAddField = true;
        int recordCount = 0;
        while (rec != null)
        {
            m_resultBuffer.Append("<Office>");
            String[] fl = new String[]{ "" };
            String dataStandardNoteId = "";
            String demoListDate = "";
            String saleDate = "";
            String listDate = "";
            String statusDate = "";
            String listPrice = "";
            String salePrice = "";
            String searchPrice = "";
            String bathRooms = "";
            String sqft = "";
            String stStatus = "";
            String recordID = "";
            String OfficeID = "";
            boolean goNextRec = false;
            for (int i = 0;i < getResultField().length;i++)
            {
                if (goNextRec)
                    break;
                 
                fieldValue = "";
                fieldName = Tcs.Mls.TCSStandardResultFields.getXmlName(getResultField()[i]);
                //]field.getName();
                bAddField = true;
                String mlsValue = "";
                switch(getResultField()[i])
                {
                    case Tcs.Mls.TCSStandardResultFields.STDF_OF_OFFICEID: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        recordID = fieldValue;
                        OfficeID = fieldValue;
                        break;
                    case Tcs.Mls.TCSStandardResultFields.STDF_OF_OFFICEIDTIGERLEAD: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        if (StringSupport.isNullOrEmpty(fieldValue) && (fields.getField("OF_OfficeIDTigerlead") == null))
                            fieldValue = OfficeID;
                         
                        break;
                    case Tcs.Mls.TCSStandardResultFields.STDF_OF_OFFICEIDSHOWINGTIME: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        if (StringSupport.isNullOrEmpty(fieldValue) && (fields.getField("OF_OfficeIDShowingTime") == null))
                            fieldValue = OfficeID;
                         
                        break;
                    case Tcs.Mls.TCSStandardResultFields.STDF_STDFLASTMOD: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        if (fieldValue.indexOf('T') < 0)
                            fieldValue = MLSUtil.FormatDate(STANDARD_DATEFORMAT, STANDARD_DATETIMEFORMAT, fieldValue);
                         
                        if (fieldValue.indexOf("00:00:00") > -1)
                            fieldValue = fieldValue.replace("00:00:00", "23:59:59");
                         
                        break;
                    default: 
                        fieldValue = rec.getTCSStdFieldValue(getResultField()[i],true);
                        break;
                
                }
                if (bAddField && !(StringSupport.isNullOrEmpty(fieldValue)))
                    m_resultBuffer.Append("<" + fieldName + ">" + Tcs.Mls.Util.convertStringToXML(Tcs.Mls.Util.filterJunkValue(fieldValue)) + "</" + fieldName + ">");
                 
            }
            if (goNextRec)
            {
                int pos = m_resultBuffer.toString().lastIndexOf("<Office");
                m_resultBuffer.Remove(pos, m_resultBuffer.Length - pos);
                rec = m_records.getNextRecord();
                continue;
            }
             
            String footNote = "";
            if (m_isAddWarningNote)
            {
                if (dataStandardNoteId.length() > 0)
                    footNote = m_warningNoteId + "," + dataStandardNoteId;
                else
                    footNote = m_warningNoteId + ""; 
            }
            else if (dataStandardNoteId.length() > 0)
                footNote = dataStandardNoteId;
              
            if (!StringSupport.isNullOrEmpty(footNote))
                m_resultBuffer.Append("<NoteID>").Append(footNote + "</NoteID>");
             
            m_resultBuffer.Append("</Office>\r\n");
            TCSCount.put("Office", TCSCount.get("Office") + 1);
            TCSMLSNumbers.get("Office").Add(recordID);
            rec = m_records.getNextRecord();
            recordCount++;
            m_overrideLimitCount++;
            if (IsOverrideLimit && m_overrideLimitCount > GetOverrideRecordLimit())
                break;
             
            CheckResultSize();
        }
    }

    protected public void addXMLHeader() throws Exception {
        if (m_connector.IsReturnDataAggListingsXml())
        {
            m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult ReplyCode=\"0\" ReplyText=\"Success\">\r\n<OfficeRoster>\r\n");
        }
        else
            m_resultBuffer.Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<TCResult>\r\n<OfficeRoster>\r\n"); 
    }

    protected public void addXMLFooter() throws Exception {
        if (!m_connector.IsReturnDataAggListingsXml())
        {
            m_resultBuffer.Append("</OfficeRoster>\r\n");
            addNotes();
            m_resultBuffer.Append("</TCResult>\r\n");
        }
        else
        {
            m_resultBuffer.Append("</OfficeRoster>\r\n");
            addNotes();
        } 
    }

    public int[] getResultField() throws Exception {
        return Tcs.Mls.TCSStandardResultFields.getOfficeRosterResultFields();
    }

}


