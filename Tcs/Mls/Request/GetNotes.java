//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:54 PM
//

package Tcs.Mls.Request;

import CS2JNet.System.Xml.XmlNode;
import java.io.File;
import java.util.Properties;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSException;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.Request.STRINGS;
import Tcs.Mls.TCServer;
import Tcs.Mls.TCSException;
import Tcs.Mls.TPAppRequest;

public class GetNotes  extends TPAppRequest 
{
    private static final String DemoFloder = "\\TOPPDEMO\\";
    public GetNotes(TCServer tcs) throws Exception {
        super(tcs);
        m_isSearchOneDef = true;
    }

    protected public void getResult() throws Exception {
        getNotes();
    }

    protected public void getNotes() throws Exception {
        try
        {
            setLogMode();
            m_connector.setOverallProgress(MLSConnector.OVERALL_PROGRESS_PICTURE);
            XmlNode node = m_connector.getListingsNode();
            MLSCmaFields fields = m_engine.getCmaFields();
            MLSRecord record = null;
            boolean isDemo = m_engine.isDemoClient();
            String fromFolder = "";
            String toFolder = "";
            if (isDemo)
            {
                Properties settings;
                String filePath = "";
                try
                {
                    settings = (Properties)NONE.GetSection("GeneralConfiguration");
                    fromFolder = (String)settings["DefFilePath"] + DemoFloder;
                    toFolder = m_engine.getResultsFolder();
                }
                catch (Exception ex)
                {
                }
            
            }
             
            for (int i = 0;i < node.getChildNodes().size();i++)
            {
                String[] tempRecord = new String[fields.size()];
                //String[] atts = ( ( XMLElement ) node ).getAttributes();
                //String fName = "";
                XmlNode subNode = node.getChildNodes().get(i);
                String attrValue = "";
                int index = fields.getStdFieldIndex(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID);
                attrValue = subNode.getAttributes().get(Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID)).getValue();
                if (attrValue == null || attrValue.length() == 0)
                    throw TCSException.produceTCSException(STRINGS.get_Renamed(STRINGS.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED) + Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.STDF_RECORDID),"",TCSException.MINIMUM_TCS_SEARCH_PARAMETER_NOT_SUPPLIED);
                 
                tempRecord[index] = attrValue;
                if (isDemo)
                {
                    if ((new File(fromFolder + attrValue + ".txt")).exists())
                        MLSUtil.CopyFile(m_engine, fromFolder + attrValue + ".dat", toFolder + attrValue + ".dat");
                     
                }
                 
                index = fields.getStdFieldIndex(Tcs.Mls.TCSStandardResultFields.STDF_PICID);
                boolean addSelectedRecord = true;
                if (index > -1)
                {
                    String picID = subNode.getAttributes().get(Tcs.Mls.TCSStandardResultFields.getXmlName(Tcs.Mls.TCSStandardResultFields.STDF_PICID)).getValue();
                    if (picID != null && picID.length() > 0)
                        tempRecord[index] = picID;
                    else
                        addSelectedRecord = false; 
                }
                 
                record = new MLSRecord(m_engine.getCmaFields(), tempRecord);
                if (addSelectedRecord)
                    record.setChecked(true);
                 
                m_engine.addMLSRecord(record);
            }
            m_connector.updateTaskProgress(MLSConnector.PROGRESS_START_VALUE);
            //try
            //{
            if (m_engine.getMLSRecords(MLSRecords.FILTER_CHECKED).size() != 0)
                m_engine.runExRemScript(m_engine.getMLSRecords(MLSRecords.FILTER_CHECKED));
             
            //}
            //catch (Exception exc)
            //{
            //    m_connector.WriteLine(exc.Message);
            //}
            m_records = m_engine.getMLSRecords((~MLSRecords.FILTER_FLAG_ANY_PICTURE_STATE) | MLSRecords.FILTER_FLAG_MISSING_PICTURES);
            if (m_records.getFirstRecord() != null)
                addXMLNote(TCSException.ONE_OR_MORE_PICTURE_NOT_FOUND, STRINGS.get_Renamed(STRINGS.ONE_OR_MORE_PICTURE_NOT_FOUND));
             
            m_records = m_engine.getMLSRecords(0x0FFF);
            getOutputXML();
            m_requestSucess = true;
        }
        catch (Exception e)
        {
            TCSException exc;
            if (e instanceof MLSException)
            {
                MLSException mlsExc = (MLSException)e;
                if (isRETSError(mlsExc.getDeveloperMessage()))
                    exc = TCSException.produceTCSException(Tcs.Mls.STRINGS.get_Renamed(Tcs.Mls.STRINGS.MISCELLANEOUS_MLS_ERROR) + mlsExc.getMessage(),"",TCSException.MISCELLANEOUS_MLS_ERROR);
                else
                {
                    int errorCode = mlsExc.getCode();
                    if (errorCode == 4)
                    {
                        String errMessage = mlsExc.getDeveloperMessage();
                        if (errMessage != null && errMessage.length() > 0)
                            exc = TCSException.produceTCSException(mlsExc.getDeveloperMessage(),"",TCSException.INTERNAL_ERROR);
                        else
                            exc = TCSException.produceTCSException(mlsExc.getMessage(),"",TCSException.INTERNAL_ERROR); 
                    }
                    else
                        exc = TCSException.produceTCSException(mlsExc); 
                } 
            }
            else
                exc = new TCSException(null,e.getMessage(),e.getStackTrace().toString(),TCSException.TCSEXCEPTION_SOURCE_EXCEPTION,TCSException.ERROR_CODE_JAVA_EXCEPTION); 
            throw exc;
        }
    
    }

    protected public void checkSearchFields() throws Exception {
    }

    protected public void checkDEFRequiredSearchFeilds() throws Exception {
    }

}


