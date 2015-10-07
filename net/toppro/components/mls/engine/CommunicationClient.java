//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.JavaSupport.Collections.Generic.EnumeratorSupport;
import CS2JNet.System.Collections.LCC.IEnumerator;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import net.toppro.components.mls.engine.BoardSetup;
import net.toppro.components.mls.engine.BoardSetupField;
import net.toppro.components.mls.engine.BoardSetupGroup;
import net.toppro.components.mls.engine.DefParser;
import net.toppro.components.mls.engine.FileFilter;
import net.toppro.components.mls.engine.MLSCmaFields;
import net.toppro.components.mls.engine.MLSRecord;
import net.toppro.components.mls.engine.MLSRecords;
import net.toppro.components.mls.engine.MLSUtil;
import net.toppro.components.mls.engine.PropertyFieldGroups;
import net.toppro.components.mls.engine.STRINGS;
import net.toppro.components.mls.engine.StringTokenizerEx;
import Tcs.Mls.TCSStandardResultFields;

/**
* Main interface for communication's clients
*/
public abstract class CommunicationClient   
{
    private void initBlock() throws Exception {
        m_CommonSect = net.toppro.components.mls.engine.MLSEngine.SECTION_COMMON;
        m_RcvDataSect = net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA;
    }

    //+-----------------------------------------------------------------------------------+
    //|                                        Members                                    |
    //+-----------------------------------------------------------------------------------+
    public static final int FLAG_PICSCRIPT_INSIDE_MAINSCRIPT = 0x0001;
    public static final int FLAG_EXREMSCRIPT_INSIDE_MAINSCRIPT = 0x0002;
    public static final int FLAG_REMSCRIPT_INSIDE_MAINSCRIPT = 0x0004;
    //protected internal const bool debugLog = true;
    public static final String CMA_MLSRECORDID_PRE = "TP#";
    public static final String EOF = "EOF";
    private net.toppro.components.mls.engine.MLSEngine m_Engine = null;
    /**
    * Name of current common section. It can be changed by script.
    */
    //UPGRADE_NOTE: The initialization of  'm_CommonSect' was moved to method 'InitBlock'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1005'"
    private String m_CommonSect;
    /**
    * Name of current Receive section. It can be changed by script.
    */
    //UPGRADE_NOTE: The initialization of  'm_RcvDataSect' was moved to method 'InitBlock'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1005'"
    private String m_RcvDataSect;
    /**
    * Launch process of downloading of data(records) from MLS Board. Initialises MLSRecords object of
    * MLSEngine.
    */
    public abstract void runMainScript() throws Exception ;

    /**
    * Launch process of downloading of pictures  from MLS Board. Creates picture files in client folder.
    */
    public abstract void runPicScript(MLSRecords records) throws Exception ;

    /**
    * Launch process of downloading of notes  from MLS Board.
    */
    public abstract void runRemScript(MLSRecords records) throws Exception ;

    /**
    * Launch process of downloading of extra notes/remarks  from MLS Board.
    */
    public void runExRemScript(MLSRecords records) throws Exception {
    }

    /**
    * This function is called by MLSEngine right after client constructor. Client can overload
    * it to make some specific initializations of search fields (we call them property fields).
    * 
    *  @param fields object containing property fields info. Implementation should use
    * MLSEngine.addPropertyField function to initialize it.
    * 
    *  @see MLSPropertyFields, PropertyField
    */
    public void initPropertyFields(PropertyFieldGroups groups) throws Exception {
        groups.init(getEngine());
    }

    protected public BoardSetupGroup createIPSetupGroup() throws Exception {
        BoardSetupGroup group = new BoardSetupGroup(STRINGS.get_Renamed(STRINGS.COMM_CLIENT_LOGIN_SETUP_SERVER_INFO_CAPTION),BoardSetupGroup.GROUP_TYPE_IP);
        group.addField(new BoardSetupField(BoardSetup.ADDRESS_FIELD_NAME,STRINGS.get_Renamed(STRINGS.COMM_CLIENT_LOGIN_SETUP_ADDRESS_CAPTION)));
        group.addField(new BoardSetupField(BoardSetup.PORT_FIELD_NAME,STRINGS.get_Renamed(STRINGS.COMM_CLIENT_LOGIN_SETUP_PORT_CAPTION)));
        return group;
    }

    protected public BoardSetupGroup createSecFieldsSetupGroup() throws Exception {
        BoardSetupGroup group = new BoardSetupGroup(STRINGS.get_Renamed(STRINGS.COMM_CLIENT_LOGIN_SETUP_LOGIN_INFO_CAPTION),BoardSetupGroup.GROUP_TYPE_SECLIST);
        String passPrompt = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_TPOPASSPROMPT);
        String maskPassword = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_MASKPASSWORD);
        if (passPrompt == null)
            passPrompt = "";
         
        if (maskPassword == null)
            maskPassword = "";
         
        int visual_item_number = 1;
        String buffer = StringSupport.Trim(getEngine().getDefParser().getSecFieldsContent());
        if (buffer.length() > 0)
        {
            StringTokenizerEx tokenizer = new StringTokenizerEx(buffer,"\n");
            while (tokenizer.hasMoreElements())
            {
                String line = tokenizer.nextElement();
                int index = MLSUtil.stringDefFind(line,';');
                if (index >= 0)
                    line = line.Substring(0, (index)-(0));
                 
                index = MLSUtil.stringDefFind(line,'=');
                if (index >= 0)
                {
                    String name = line.Substring(0, (index)-(0)).Trim();
                    String value_Renamed = StringSupport.Trim(line.substring(index + 1));
                    BoardSetupField field;
                    boolean isMaskPassword = false;
                    if (value_Renamed.startsWith("__PASS"))
                    {
                        String label = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_SECLIST,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_LABELPASS + visual_item_number);
                        if (label == null || label.length() == 0)
                            label = STRINGS.get_Renamed(STRINGS.COMM_CLIENT_LOGIN_SETUP_PASSWORD_CAPTION) + visual_item_number;
                         
                        boolean visible = true;
                        if (passPrompt.equals("1") || passPrompt.indexOf(StringSupport.Trim(value_Renamed.replace('_', ' '))) > -1)
                            visible = false;
                         
                        if (maskPassword.indexOf(StringSupport.Trim(value_Renamed.replace('_', ' '))) > -1)
                            isMaskPassword = true;
                         
                        field = new BoardSetupField(name,label,BoardSetupField.INPUT_TYPE_STANDARD,0,false,visible,isMaskPassword);
                        visual_item_number++;
                    }
                    else
                    {
                        field = new BoardSetupField(name,"",BoardSetupField.INPUT_TYPE_FINAL);
                        field.setValue(value_Renamed);
                    } 
                    group.addField(field);
                }
                 
            }
        }
         
        return group;
    }

    public abstract void initBoardSetup(BoardSetup setup) throws Exception ;

    /**
    * Read raw data file and cut or remove garbage from.
    *  @param datFileName path to the data-file on local machine
    * 
    *  @return  good file's content
    * See the function StartProcStr in XCOMPS.bas
    * It uses StartProcessingString in this function.
    */
    //UPGRADE_NOTE: Access modifiers of method 'prepareDataFile' were changed to 'public'. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1204'"
    public abstract String prepareDataFile(String filename) throws Exception ;

    /**
    * Reads an Extra MLS Import's data file and prepares data for updating its record.
    * 
    *  @param datFileName
    */
    public void prepareAdditionalMlsRecord(MLSRecord record, String datFileName) throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        DefParser defParser = getEngine().getDefParser();
        String sRecMark = getEngine().getDefParser().getValue(getRcvDataSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK);
        if (sRecMark.length() != 0)
            sRecMark = MLSUtil.toStr(sRecMark).replace('~', ' ');
         
        String sRecDlm = getEngine().getDefParser().getValue(getCommonSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDDELIMITER);
        if (sRecDlm.length() != 0)
            sRecDlm = MLSUtil.toStr(sRecDlm);
         
        if (sRecDlm.length() == 0 && sRecMark.length() == 0)
        {
        }
         
        //sRecMark = gEnv.MLSFields(1).sAlias;
        String fieldDelimiter = getEngine().getDefParser().getValue(getCommonSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_FIELDDELIMITER);
        if (fieldDelimiter.length() != 0)
            fieldDelimiter = MLSUtil.toStr(fieldDelimiter);
        else
            fieldDelimiter = ","; 
        boolean isMLSRecordsEx = false;
        Hashtable m_Headers = null;
        String StartHeaderString = getEngine().getDefParser().getValue(getRcvDataSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTHEADERSTRING);
        if (m_Headers == null)
        {
            String HeaderFile = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HEADERINFOFILE);
            String HeaderDelimiter = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HEADERDELIMITER);
            if (HeaderDelimiter.length() == 0)
                HeaderDelimiter = fieldDelimiter;
            else
                HeaderDelimiter = MLSUtil.toStr(HeaderDelimiter); 
            if (HeaderFile.length() != 0)
            {
                if (HeaderFile.indexOf("*") >= 0 || HeaderFile.indexOf("?") >= 0)
                {
                    FileFilter filter = new FileFilter(getEngine().getResultsFolder(),FileFilter.FLAG_FIND_FILES);
                    HeaderFile = filter.getFirstMatch(HeaderFile);
                    if (HeaderFile == null || HeaderFile.length() == 0)
                        HeaderFile = "";
                     
                }
                 
            }
             
            String endHeaderInfo = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_ENDHEADERSTRING);
            if (endHeaderInfo.length() == 0)
            {
                endHeaderInfo = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING);
                if (endHeaderInfo.length() == 0)
                    endHeaderInfo = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK);
                 
            }
             
            if (endHeaderInfo.length() != 0)
            {
                if (!endHeaderInfo.toUpperCase().equals(EOF.toUpperCase()))
                    endHeaderInfo = MLSUtil.toStr(endHeaderInfo);
                else
                    endHeaderInfo = null; 
                if (HeaderFile.length() != 0)
                    m_Headers = getHeaders(getEngine().getResultsFolder() + HeaderFile,MLSUtil.toStr(StartHeaderString),endHeaderInfo,HeaderDelimiter);
                else
                    m_Headers = getHeaders(datFileName,MLSUtil.toStr(StartHeaderString),endHeaderInfo,HeaderDelimiter); 
            }
            else if (HeaderFile.length() != 0)
            {
                if (!endHeaderInfo.toUpperCase().equals(EOF.toUpperCase()))
                    endHeaderInfo = MLSUtil.toStr(endHeaderInfo);
                else
                    endHeaderInfo = null; 
                m_Headers = getHeaders(getEngine().getResultsFolder() + HeaderFile,MLSUtil.toStr(StartHeaderString),endHeaderInfo,HeaderDelimiter);
            }
              
        }
         
        if (m_Headers != null && getEngine().isUseMLSRecordsEx())
            isMLSRecordsEx = true;
        else
            isMLSRecordsEx = false; 
        // Let's see function StartProcStr in XCOMPS.bas
        // It uses StartProcessingString in this function.
        String fileData = prepareDataFile(datFileName);
        //getEngine().WriteLine("parsing 1= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        String TmpStr1 = "", TmpStr2 = "";
        String TmpRawStr = "";
        String MlsNumberValue = "";
        MLSCmaFields m_CmaFields = null;
        m_CmaFields = getEngine().getCmaFields();
        // Let's see function CountRecords in XCOMPS.bas
        StringTokenizerEx strToken = null;
        int TotalRec = 0;
        if (sRecDlm.length() != 0)
        {
            strToken = new StringTokenizerEx(fileData,sRecDlm);
            strToken.setMode(StringTokenizerEx.MODE_TAIL_INCLUDE);
            TotalRec = strToken.countTokens();
        }
        else
        {
            if (sRecMark.length() != 0)
            {
                strToken = new StringTokenizerEx(fileData,sRecMark);
                strToken.setMode(StringTokenizerEx.MODE_HEAD_INCLUDE);
                TotalRec = strToken.countTokens();
            }
             
        } 
        //getEngine().WriteLine("parsing 2= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        int DelLastRecord = 0;
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DELLASTRECSTRING).length() != 0)
        {
            try
            {
                DelLastRecord = Integer.valueOf(getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DELLASTRECSTRING));
            }
            catch (Exception e)
            {
                DelLastRecord = 0;
            }
        
        }
         
        //getEngine().WriteLine("parsing 3= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        int MlsNumber = 0;
        int CurrentRec = 0;
        int NumRepsPos = 0;
        int Inplength = 0;
        String CutBy = "";
        String Combine = "";
        int CutByPosition = 0;
        int i, j, k, nr;
        int m = 1;
        int subIndex1, subIndex2, NumReps;
        int fieldValuePos;
        int[] ofsets = new int[0];
        String[] ContFieldNames = null;
        net.toppro.components.mls.engine.CmaField cmaField = null;
        DefParser LookUpTable = null;
        String[] LookUpTableArray = null;
        int MaxListingNumber = 0;
        if (!getEngine().isRETSClient() && !getEngine().isFileImport() && !getEngine().isDemoClient())
            MaxListingNumber = getEngine().getRecordsLimit();
         
        boolean ErrorFlag = false;
        boolean subIndex = false;
        boolean isDonePrepareValue = false;
        boolean isVaildRecord = false;
        boolean bDownloadLookUpTable = false;
        int LastErrorRecord = -1;
        boolean[] ErrorFields = new boolean[m_CmaFields.size()];
        //System.Globalization.Calendar calendar = new System.Globalization.GregorianCalendar(); // today
        //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
        int currentYear = Calendar.getInstance().getTime().Year;
        // This is position of field in record's string
        String[] fieldValuePositions = new String[0];
        String fieldValuePosition = "";
        String RecordPosition = "";
        int fieldValuePositionEx = 0;
        int NextFieldPosition = 0;
        // We'll keep here our record
        String recordBuffer = "";
        SupportClass.Tokenizer strToken1 = null;
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        engine.setMessage(STRINGS.get_Renamed(STRINGS.COMM_CLIENT_PREPARING_RECORDS));
        engine.enableBufferedNotification();
        //getEngine().WriteLine("parsing 4= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        Boolean isIncludingTlBlob = engine.isIncludingTlBlobField();
        String[] extraTempRecord = new String[m_CmaFields.size()];
        while (strToken.hasMoreElements())
        {
            recordBuffer = ((String)strToken.nextElement());
            if (strToken.getMode() == StringTokenizerEx.MODE_TAIL_INCLUDE)
            {
                if (recordBuffer.endsWith(sRecDlm))
                    recordBuffer = recordBuffer.substring(0, (0) + ((recordBuffer.length() - sRecDlm.length()) - (0)));
                 
            }
             
            if (StringSupport.isNullOrEmpty(StringSupport.Trim(recordBuffer)))
                continue;
             
            CurrentRec++;
            if (MaxListingNumber > 0 && CurrentRec > MaxListingNumber)
            {
                getEngine().getEnvironment().addNote(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.EXCEED_MAX_PARSE_LISINGS),Integer.toString(MaxListingNumber)));
                break;
            }
             
            engine.setProgress(CurrentRec,TotalRec);
            if (CurrentRec > TotalRec - DelLastRecord)
                break;
             
            String[] tempRecord = new String[m_CmaFields.size()];
            //string[] tempRecordRawData = null;
            //if (isIncludingTlBlob)
            //    tempRecordRawData = new string[m_CmaFields.size()];
            if (fieldValuePositions.length == 0)
                fieldValuePositions = m_CmaFields.getFieldPositions();
             
            ofsets = null;
            isVaildRecord = false;
            for (i = 0;i < tempRecord.length;i++)
            {
                // main loop for record
                cmaField = m_CmaFields.getField(i);
                Inplength = cmaField.getInpLength();
                CutBy = MLSUtil.toStr(cmaField.getCutBy()).replace('~', ' ');
                Combine = cmaField.getCombine();
                ContFieldNames = null;
                if (fieldValuePositions != null && fieldValuePositions.length > 0 && i < fieldValuePositions.length)
                    fieldValuePosition = fieldValuePositions[i];
                else
                    fieldValuePosition = cmaField.getRecordPosition(); 
                ErrorFlag = false;
                subIndex = false;
                isDonePrepareValue = false;
                subIndex1 = subIndex2 = 1;
                CutByPosition = -1;
                NumReps = 1;
                TmpRawStr = "";
                if (fieldValuePosition.charAt(0) == '$')
                {
                    if (m_Headers != null)
                    {
                        //isMLSRecordsEx ) // TODO: should be replaced with if( m_Headers != null )
                        if ((fieldValuePos = cmaField.getRecordPositionInt()) == -1)
                        {
                            RecordPosition = cmaField.getRecordPosition();
                            if (RecordPosition.length() >= 5 && RecordPosition.charAt(0) == '$' && RecordPosition.charAt(RecordPosition.length() - 1) == '$')
                            {
                                RecordPosition = RecordPosition.replace("$", "");
                                RecordPosition = StringSupport.Trim((RecordPosition.indexOf(",") != -1 ? StringSupport.Split(RecordPosition, ',')[1] : ""));
                            }
                             
                            try
                            {
                                if (RecordPosition.indexOf("*") == -1 && RecordPosition.indexOf("?") == -1)
                                {
                                    if (cmaField.getRank() <= 0)
                                    {
                                        if (m_Headers.containsKey(RecordPosition.toUpperCase()))
                                            fieldValuePositionEx = (int)(m_Headers.get(RecordPosition.toUpperCase()));
                                        else
                                            fieldValuePositionEx = 0; 
                                    }
                                    else
                                        fieldValuePositionEx = getHeader(m_Headers,RecordPosition.toUpperCase(),cmaField.getRank()); 
                                }
                                else
                                    fieldValuePositionEx = getHeader(m_Headers,RecordPosition.toUpperCase(),cmaField.getRank()); 
                            }
                            catch (Exception exc)
                            {
                                fieldValuePositionEx = 0;
                            }

                            //UPGRADE_TODO: The 'System.Int32' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
                            if (fieldValuePositionEx != 0)
                                fieldValuePosition = String.valueOf(fieldValuePositionEx);
                            else
                            {
                                if (LookUpTable == null && !bDownloadLookUpTable)
                                {
                                    if ((LookUpTable = getEngine().getLookUpTable()) == null)
                                    {
                                        bDownloadLookUpTable = true;
                                        getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_DOWNLOAD_LT);
                                        tempRecord[i] = "";
                                        continue;
                                    }
                                     
                                }
                                else // cannot load lookup table
                                if (LookUpTable == null)
                                {
                                    tempRecord[i] = "";
                                    continue;
                                }
                                  
                                // cannot load lookup table
                                LookUpTableArray = LookUpTable.getAttributiesFor(cmaField.getRecordPosition());
                                if (LookUpTableArray == null)
                                {
                                    ErrorFlag = true;
                                    //if( LastErrorRecord == -1 || LastErrorRecord <= CurrentRec )
                                    if (ErrorFields[i] == false)
                                    {
                                        LastErrorRecord = CurrentRec;
                                        ErrorFields[i] = true;
                                        getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_HEADER_NOT_FOUND,cmaField.getName(),cmaField.getRecordPosition());
                                    }
                                     
                                    tempRecord[i] = "";
                                    continue;
                                }
                                 
                                if (LookUpTableArray != null)
                                {
                                    for (j = 0;j < LookUpTableArray.length;j++)
                                    {
                                        fieldValuePositionEx = (int)(m_Headers.get(LookUpTableArray[j]));
                                        //UPGRADE_TODO: The 'System.Int32' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
                                        if (fieldValuePositionEx != 0)
                                        {
                                            // cmaField.setRecordPosition( "@" + LookUpTableArray[j] );
                                            fieldValuePosition = String.valueOf(fieldValuePositionEx);
                                            if (LastErrorRecord == -1 || LastErrorRecord <= CurrentRec)
                                                getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_HEADER_LOOKUP,cmaField.getName(),cmaField.getRecordPosition(),"@" + LookUpTableArray[j]);
                                             
                                            break;
                                        }
                                         
                                    }
                                }
                                 
                            } 
                            fieldValuePos = Integer.valueOf(fieldValuePosition) - 1;
                        }
                         
                    }
                    else
                        // cmaField.setRecordPosition( fieldValuePos );
                        fieldValuePos = cmaField.getRecordPositionInt(); 
                    if (cmaField.getSubIndex1() != -1 || cmaField.getSubIndex2() != -1)
                    {
                        subIndex = true;
                        subIndex1 = cmaField.getSubIndex1();
                        subIndex2 = cmaField.getSubIndex2();
                    }
                     
                    if (ofsets == null)
                    {
                        ofsets = parseRecord(recordBuffer,fieldDelimiter.toString());
                        if (ofsets == null)
                        {
                            CurrentRec--;
                            break;
                        }
                         
                    }
                     
                    if (fieldValuePos < ofsets.length)
                    {
                        int item = ofsets[fieldValuePos];
                        if (fieldValuePos + 1 < ofsets.length)
                            NextFieldPosition = ofsets[fieldValuePos + 1];
                        else
                            NextFieldPosition = recordBuffer.length(); 
                        if (NextFieldPosition - item <= 0)
                            // field value is empty
                            TmpStr1 = "";
                        else
                        {
                            if (CutBy.length() != 0)
                            {
                                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                                CutByPosition = SupportClass.getIndex(recordBuffer,CutBy,item);
                            }
                             
                            if (!isIncludingTlBlob)
                            {
                                if (CutByPosition == -1 || CutByPosition > item + (subIndex2 - 1) + Inplength)
                                {
                                    if (item + Inplength + subIndex2 - 1 >= recordBuffer.length())
                                        TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (recordBuffer.Length)-(item + subIndex2 - 1));
                                    else
                                        TmpStr1 = recordBuffer.substring(item + subIndex2 - 1, (item + subIndex2 - 1) + ((item + Inplength + subIndex2 - 1) - (item + subIndex2 - 1))); 
                                }
                                else
                                {
                                    if (item + subIndex2 - 1 < CutByPosition)
                                    {
                                        if (item + subIndex2 - 1 > 0)
                                            TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (CutByPosition)-(item + subIndex2 - 1));
                                        else
                                            TmpStr1 = recordBuffer.Substring(0, (CutByPosition)-(0)); 
                                    }
                                    else
                                        TmpStr1 = ""; 
                                } 
                            }
                            else
                            {
                                if (CutByPosition == -1)
                                {
                                    if (item + Inplength + subIndex2 - 1 >= recordBuffer.length())
                                        TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (recordBuffer.Length)-(item + subIndex2 - 1));
                                    else
                                        TmpStr1 = recordBuffer.substring(item + subIndex2 - 1, (item + subIndex2 - 1) + ((item + Inplength + subIndex2 - 1) - (item + subIndex2 - 1))); 
                                }
                                else
                                {
                                    if (item + subIndex2 - 1 < CutByPosition)
                                    {
                                        if (item + subIndex2 - 1 > 0)
                                            TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (CutByPosition)-(item + subIndex2 - 1));
                                        else
                                            TmpStr1 = recordBuffer.Substring(0, (CutByPosition)-(0)); 
                                    }
                                    else
                                        TmpStr1 = ""; 
                                } 
                            } 
                        } 
                    }
                    else
                    {
                        TmpStr1 = "";
                    } 
                    // Some def-files use incorrect indexes to create empty column.
                    // Usually this column is used to construct features.
                    // We think this is a bad practice, but we can not do anything with it right now.
                    // So we disable error notification for this case for a while (the next line is commented).
                    //getEngine().notifyTechSupport( MLSEngine.SUPPORT_CODE_HEADER_BAD_INDEX );
                    if (fieldDelimiter != null)
                    {
                        if (TmpStr1.equals(fieldDelimiter.toString()))
                            TmpStr1 = "";
                         
                    }
                     
                    if (TmpStr1.length() != 0 && isDonePrepareValue == false)
                    {
                        TmpStr1 = cmaField.prepareValueEx(TmpStr1,defParser,getEngine());
                    }
                     
                    //Fix defect #67871, if age or year build = 0, year build will be the current year.
                    //if ( cmaField.isStd ( TCSStandardResultFields.STDF_CMAAGE  ) && TmpStr1.trim().equalsIgnoreCase("0") )
                    //	TmpStr1 = "";
                    if (cmaField.isStd(TCSStandardResultFields.STDF_CMAFEATURE))
                    {
                        new MLSUtil();
                        TmpStr1 = MLSUtil.replaceSubStr(TmpStr1,":",": ");
                    }
                     
                    if (cmaField.isStd(TCSStandardResultFields.STDF_CMAAGE))
                    {
                        if ((cmaField.caption.toLowerCase().indexOf("yr") != -1 || cmaField.caption.toLowerCase().indexOf("year") != -1) && cmaField.caption.toLowerCase().indexOf("age") == -1 && TmpStr1.length() == 2)
                        {
                            try
                            {
                                if (currentYear - 1999 >= ((int)((Integer.valueOf(TmpStr1.toString())))))
                                    TmpStr1 = "20" + TmpStr1;
                                else
                                    TmpStr1 = "19" + TmpStr1; 
                            }
                            catch (Exception e)
                            {
                            }
                        
                        }
                         
                    }
                     
                    if (cmaField.isStd(TCSStandardResultFields.STDF_RECORDID))
                    {
                        TmpStr1 = StringSupport.Trim(TmpStr1);
                        MlsNumber = i;
                        //TmpStr1 = new MLSUtil().removeSpace(TmpStr1);
                        if (TmpStr1.length() == 0)
                        {
                            MlsNumberValue = CMA_MLSRECORDID_PRE + Integer.toString(m);
                            //ErrorFlag = true;
                            TmpStr1 = MlsNumberValue;
                            m++;
                        }
                        else
                        {
                            MlsNumberValue = TmpStr1;
                            isVaildRecord = true;
                        } 
                    }
                     
                    if (ErrorFlag == true)
                        break;
                     
                    if (StringSupport.Trim(TmpStr1).length() > 0 && !cmaField.isStd(TCSStandardResultFields.STDF_RECORDID))
                    {
                        isVaildRecord = true;
                    }
                     
                    if (StringSupport.Trim(TmpStr1).length() == 0 && cmaField.defaultvalue.length() > 0)
                        TmpStr1 = cmaField.defaultvalue;
                     
                    tempRecord[i] = StringSupport.Trim(TmpStr1);
                }
                 
            }
            //for(i = 1; i < cmaFields.size()-1; i++ )
            if (isVaildRecord == true)
            {
                for (j = 0;j < tempRecord.length;j++)
                    if (!StringSupport.isNullOrEmpty(tempRecord[j]))
                    {
                        extraTempRecord[j] = extraTempRecord[j] + ((!StringSupport.isNullOrEmpty(extraTempRecord[j])) ? String.format(StringSupport.CSFmtStrToJFmtStr(",{0}"),tempRecord[j]) : tempRecord[j]);
                        net.toppro.components.mls.engine.CmaField field = m_CmaFields.getFieldByRecordPosition(fieldValuePositions[j]);
                        if (field.getStdId() == -1)
                            // non standard field
                            record.setFieldValue(j,extraTempRecord[j]);
                        else
                            record.setStdFieldValue(field.getStdId(),extraTempRecord[j]); 
                    }
                     
            }
             
        }
        //getEngine().WriteLine("parsing 5= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        engine.disableBufferedNotification();
        engine.AddParsingRawDataTime((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time);
        getEngine().writeLine("Time for parsing raw-data= " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
    }

    /**
    * Reads a MLS Import's data file and prepares data for displaind in the grid.
    *  @param datFileName the path to data file
    */
    public void prepareMlsRecords(String datFileName) throws Exception {
        if (getEngine().isGetRecordCount())
            return ;
         
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        DefParser defParser = getEngine().getDefParser();
        String sRecMark = getEngine().getDefParser().getValue(getRcvDataSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK);
        if (sRecMark.length() != 0)
            sRecMark = MLSUtil.toStr(sRecMark).replace('~', ' ');
         
        String sRecDlm = getEngine().getDefParser().getValue(getCommonSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDDELIMITER);
        if (sRecDlm.length() != 0)
            sRecDlm = MLSUtil.toStr(sRecDlm);
         
        if (sRecDlm.length() == 0 && sRecMark.length() == 0)
        {
        }
         
        //sRecMark = gEnv.MLSFields(1).sAlias;
        String fieldDelimiter = getEngine().getDefParser().getValue(getCommonSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_FIELDDELIMITER);
        if (fieldDelimiter.length() != 0)
            fieldDelimiter = MLSUtil.toStr(fieldDelimiter);
        else
            fieldDelimiter = ","; 
        boolean isMLSRecordsEx = false;
        Hashtable m_Headers = null;
        String StartHeaderString = getEngine().getDefParser().getValue(getRcvDataSectionName(),net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTHEADERSTRING);
        if (m_Headers == null)
        {
            String HeaderFile = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HEADERINFOFILE);
            String HeaderDelimiter = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HEADERDELIMITER);
            if (HeaderDelimiter.length() == 0)
                HeaderDelimiter = fieldDelimiter;
            else
                HeaderDelimiter = MLSUtil.toStr(HeaderDelimiter); 
            if (HeaderFile.length() != 0)
            {
                if (HeaderFile.indexOf("*") >= 0 || HeaderFile.indexOf("?") >= 0)
                {
                    FileFilter filter = new FileFilter(getEngine().getResultsFolder(),FileFilter.FLAG_FIND_FILES);
                    HeaderFile = filter.getFirstMatch(HeaderFile);
                    if (HeaderFile == null || HeaderFile.length() == 0)
                        HeaderFile = "";
                     
                }
                 
            }
             
            String endHeaderInfo = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_ENDHEADERSTRING);
            if (endHeaderInfo.length() == 0)
            {
                endHeaderInfo = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING);
                if (endHeaderInfo.length() == 0)
                    endHeaderInfo = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK);
                 
            }
             
            if (endHeaderInfo.length() != 0)
            {
                if (!endHeaderInfo.toUpperCase().equals(EOF.toUpperCase()))
                    endHeaderInfo = MLSUtil.toStr(endHeaderInfo);
                else
                    endHeaderInfo = null; 
                if (HeaderFile.length() != 0)
                    m_Headers = getHeaders(getEngine().getResultsFolder() + HeaderFile,MLSUtil.toStr(StartHeaderString),endHeaderInfo,HeaderDelimiter);
                else
                    m_Headers = getHeaders(datFileName,MLSUtil.toStr(StartHeaderString),endHeaderInfo,HeaderDelimiter); 
            }
            else if (HeaderFile.length() != 0)
            {
                if (!endHeaderInfo.toUpperCase().equals(EOF.toUpperCase()))
                    endHeaderInfo = MLSUtil.toStr(endHeaderInfo);
                else
                    endHeaderInfo = null; 
                m_Headers = getHeaders(getEngine().getResultsFolder() + HeaderFile,MLSUtil.toStr(StartHeaderString),endHeaderInfo,HeaderDelimiter);
            }
              
        }
         
        if (m_Headers != null && getEngine().isUseMLSRecordsEx())
            isMLSRecordsEx = true;
        else
            isMLSRecordsEx = false; 
        // Let's see function StartProcStr in XCOMPS.bas
        // It uses StartProcessingString in this function.
        String fileData = prepareDataFile(datFileName);
        //getEngine().WriteLine("parsing 1= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        String TmpStr1 = "", TmpStr2 = "";
        String TmpRawStr = "";
        String MlsNumberValue = "";
        MLSCmaFields m_CmaFields = null;
        m_CmaFields = getEngine().getCmaFields();
        // Let's see function CountRecords in XCOMPS.bas
        StringTokenizerEx strToken = null;
        int TotalRec = 0;
        if (sRecDlm.length() != 0)
        {
            strToken = new StringTokenizerEx(fileData,sRecDlm);
            strToken.setMode(StringTokenizerEx.MODE_TAIL_INCLUDE);
            TotalRec = strToken.countTokens();
        }
        else
        {
            if (sRecMark.length() != 0)
            {
                strToken = new StringTokenizerEx(fileData,sRecMark);
                strToken.setMode(StringTokenizerEx.MODE_HEAD_INCLUDE);
                TotalRec = strToken.countTokens();
            }
             
        } 
        //getEngine().WriteLine("parsing 2= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        int DelLastRecord = 0;
        if (getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DELLASTRECSTRING).length() != 0)
        {
            try
            {
                DelLastRecord = Integer.valueOf(getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DELLASTRECSTRING));
            }
            catch (Exception e)
            {
                DelLastRecord = 0;
            }
        
        }
         
        //getEngine().WriteLine("parsing 3= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        int MlsNumber = 0;
        int CurrentRec = 0;
        int NumRepsPos = 0;
        int Inplength = 0;
        String CutBy = "";
        String Combine = "";
        int CutByPosition = 0;
        int i, j, k, nr;
        int m = 1;
        int subIndex1, subIndex2, NumReps;
        int fieldValuePos;
        int[] ofsets = new int[0];
        String[] ContFieldNames = null;
        net.toppro.components.mls.engine.CmaField cmaField = null;
        DefParser LookUpTable = null;
        String[] LookUpTableArray = null;
        int MaxListingNumber = 0;
        if (!getEngine().isRETSClient() && !getEngine().isFileImport() && !getEngine().isDemoClient())
            MaxListingNumber = getEngine().getRecordsLimit();
         
        boolean ErrorFlag = false;
        boolean subIndex = false;
        boolean isDonePrepareValue = false;
        boolean isVaildRecord = false;
        boolean bDownloadLookUpTable = false;
        int LastErrorRecord = -1;
        boolean[] ErrorFields = new boolean[m_CmaFields.size()];
        //System.Globalization.Calendar calendar = new System.Globalization.GregorianCalendar(); // today
        //UPGRADE_TODO: Method 'java.util.Calendar.get' was converted to 'SupportClass.CalendarManager.Get' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilCalendarget_int'"
        int currentYear = Calendar.getInstance().getTime().Year;
        // This is position of field in record's string
        String[] fieldValuePositions = new String[0];
        String fieldValuePosition = "";
        String RecordPosition = "";
        int fieldValuePositionEx = 0;
        int NextFieldPosition = 0;
        // We'll keep here our record
        String recordBuffer = "";
        SupportClass.Tokenizer strToken1 = null;
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        engine.setMessage(STRINGS.get_Renamed(STRINGS.COMM_CLIENT_PREPARING_RECORDS));
        engine.enableBufferedNotification();
        //getEngine().WriteLine("parsing 4= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        Boolean isIncludingTlBlob = engine.isIncludingTlBlobField();
        while (strToken.hasMoreElements())
        {
            recordBuffer = ((String)strToken.nextElement());
            if (strToken.getMode() == StringTokenizerEx.MODE_TAIL_INCLUDE)
            {
                if (recordBuffer.endsWith(sRecDlm))
                    recordBuffer = recordBuffer.substring(0, (0) + ((recordBuffer.length() - sRecDlm.length()) - (0)));
                 
            }
             
            if (StringSupport.isNullOrEmpty(StringSupport.Trim(recordBuffer)))
                continue;
             
            CurrentRec++;
            if (MaxListingNumber > 0 && CurrentRec > MaxListingNumber)
            {
                getEngine().getEnvironment().addNote(MLSUtil.formatString(STRINGS.get_Renamed(STRINGS.EXCEED_MAX_PARSE_LISINGS),Integer.toString(MaxListingNumber)));
                break;
            }
             
            engine.setProgress(CurrentRec,TotalRec);
            if (CurrentRec > TotalRec - DelLastRecord)
                break;
             
            String[] tempRecord = new String[m_CmaFields.size()];
            String[] tempRecordRawData = null;
            if (isIncludingTlBlob)
                tempRecordRawData = new String[m_CmaFields.size()];
             
            if (fieldValuePositions.length == 0)
                fieldValuePositions = m_CmaFields.getFieldPositions();
             
            ofsets = null;
            isVaildRecord = false;
            for (i = 0;i < tempRecord.length;i++)
            {
                // main loop for record
                cmaField = m_CmaFields.getField(i);
                Inplength = cmaField.getInpLength();
                CutBy = MLSUtil.toStr(cmaField.getCutBy()).replace('~', ' ');
                Combine = cmaField.getCombine();
                ContFieldNames = null;
                if (fieldValuePositions != null && fieldValuePositions.length > 0 && i < fieldValuePositions.length)
                    fieldValuePosition = fieldValuePositions[i];
                else
                    fieldValuePosition = cmaField.getRecordPosition(); 
                ErrorFlag = false;
                subIndex = false;
                isDonePrepareValue = false;
                subIndex1 = subIndex2 = 1;
                CutByPosition = -1;
                NumReps = 1;
                TmpRawStr = "";
                if (fieldValuePosition.charAt(0) == '@')
                {
                    // Case #1
                    if (m_Headers != null)
                    {
                        //isMLSRecordsEx ) // TODO: should be replaced with if( m_Headers != null )
                        if ((fieldValuePos = cmaField.getRecordPositionInt()) == -1)
                        {
                            RecordPosition = cmaField.getRecordPosition();
                            if (RecordPosition.length() >= 2 && RecordPosition.charAt(0) == '"')
                                RecordPosition = RecordPosition.substring(1, (1) + ((RecordPosition.length() - 1) - (1)));
                             
                            try
                            {
                                if (RecordPosition.indexOf("*") == -1 && RecordPosition.indexOf("?") == -1)
                                {
                                    if (cmaField.getRank() <= 0)
                                    {
                                        if (m_Headers.containsKey(RecordPosition.toUpperCase()))
                                            fieldValuePositionEx = (int)(m_Headers.get(RecordPosition.toUpperCase()));
                                        else
                                            fieldValuePositionEx = 0; 
                                    }
                                    else
                                        fieldValuePositionEx = getHeader(m_Headers,RecordPosition.toUpperCase(),cmaField.getRank()); 
                                }
                                else
                                    fieldValuePositionEx = getHeader(m_Headers,RecordPosition.toUpperCase(),cmaField.getRank()); 
                            }
                            catch (Exception exc)
                            {
                                fieldValuePositionEx = 0;
                            }

                            //UPGRADE_TODO: The 'System.Int32' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
                            if (fieldValuePositionEx != 0)
                                fieldValuePosition = String.valueOf(fieldValuePositionEx);
                            else
                            {
                                if (LookUpTable == null && !bDownloadLookUpTable)
                                {
                                    if ((LookUpTable = getEngine().getLookUpTable()) == null)
                                    {
                                        bDownloadLookUpTable = true;
                                        getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_DOWNLOAD_LT);
                                        tempRecord[i] = "";
                                        continue;
                                    }
                                     
                                }
                                else // cannot load lookup table
                                if (LookUpTable == null)
                                {
                                    tempRecord[i] = "";
                                    continue;
                                }
                                  
                                // cannot load lookup table
                                LookUpTableArray = LookUpTable.getAttributiesFor(cmaField.getRecordPosition());
                                if (LookUpTableArray == null)
                                {
                                    ErrorFlag = true;
                                    //if( LastErrorRecord == -1 || LastErrorRecord <= CurrentRec )
                                    if (ErrorFields[i] == false)
                                    {
                                        LastErrorRecord = CurrentRec;
                                        ErrorFields[i] = true;
                                        getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_HEADER_NOT_FOUND,cmaField.getName(),cmaField.getRecordPosition());
                                    }
                                     
                                    tempRecord[i] = "";
                                    continue;
                                }
                                 
                                if (LookUpTableArray != null)
                                {
                                    for (j = 0;j < LookUpTableArray.length;j++)
                                    {
                                        fieldValuePositionEx = (int)(m_Headers.get(LookUpTableArray[j]));
                                        //UPGRADE_TODO: The 'System.Int32' structure does not have an equivalent to NULL. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1291'"
                                        if (fieldValuePositionEx != 0)
                                        {
                                            // cmaField.setRecordPosition( "@" + LookUpTableArray[j] );
                                            fieldValuePosition = String.valueOf(fieldValuePositionEx);
                                            if (LastErrorRecord == -1 || LastErrorRecord <= CurrentRec)
                                                getEngine().notifyTechSupport(net.toppro.components.mls.engine.MLSEngine.SUPPORT_CODE_HEADER_LOOKUP,cmaField.getName(),cmaField.getRecordPosition(),"@" + LookUpTableArray[j]);
                                             
                                            break;
                                        }
                                         
                                    }
                                }
                                 
                            } 
                            fieldValuePos = Integer.valueOf(fieldValuePosition) - 1;
                        }
                         
                    }
                    else
                        // cmaField.setRecordPosition( fieldValuePos );
                        fieldValuePos = cmaField.getRecordPositionInt(); 
                    if (cmaField.getSubIndex1() != -1 || cmaField.getSubIndex2() != -1)
                    {
                        subIndex = true;
                        subIndex1 = cmaField.getSubIndex1();
                        subIndex2 = cmaField.getSubIndex2();
                    }
                     
                    if (ofsets == null)
                    {
                        ofsets = parseRecord(recordBuffer,fieldDelimiter.toString());
                        if (ofsets == null)
                        {
                            CurrentRec--;
                            break;
                        }
                         
                    }
                     
                    if (fieldValuePos < ofsets.length)
                    {
                        int item = ofsets[fieldValuePos];
                        if (fieldValuePos + 1 < ofsets.length)
                            NextFieldPosition = ofsets[fieldValuePos + 1];
                        else
                            NextFieldPosition = recordBuffer.length(); 
                        if (NextFieldPosition - item <= 0)
                            // field value is empty
                            TmpStr1 = "";
                        else
                        {
                            if (CutBy.length() != 0)
                            {
                                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                                CutByPosition = SupportClass.getIndex(recordBuffer,CutBy,item);
                            }
                             
                            if (!isIncludingTlBlob)
                            {
                                if (CutByPosition == -1 || CutByPosition > item + (subIndex2 - 1) + Inplength)
                                {
                                    if (item + Inplength + subIndex2 - 1 >= recordBuffer.length())
                                        TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (recordBuffer.Length)-(item + subIndex2 - 1));
                                    else
                                        TmpStr1 = recordBuffer.substring(item + subIndex2 - 1, (item + subIndex2 - 1) + ((item + Inplength + subIndex2 - 1) - (item + subIndex2 - 1))); 
                                }
                                else
                                {
                                    if (item + subIndex2 - 1 < CutByPosition)
                                    {
                                        if (item + subIndex2 - 1 > 0)
                                            TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (CutByPosition)-(item + subIndex2 - 1));
                                        else
                                            TmpStr1 = recordBuffer.Substring(0, (CutByPosition)-(0)); 
                                    }
                                    else
                                        TmpStr1 = ""; 
                                } 
                            }
                            else
                            {
                                if (CutByPosition == -1)
                                {
                                    if (item + Inplength + subIndex2 - 1 >= recordBuffer.length())
                                        TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (recordBuffer.Length)-(item + subIndex2 - 1));
                                    else
                                        TmpStr1 = recordBuffer.substring(item + subIndex2 - 1, (item + subIndex2 - 1) + ((item + Inplength + subIndex2 - 1) - (item + subIndex2 - 1))); 
                                }
                                else
                                {
                                    if (item + subIndex2 - 1 < CutByPosition)
                                    {
                                        if (item + subIndex2 - 1 > 0)
                                            TmpStr1 = recordBuffer.Substring(item + subIndex2 - 1, (CutByPosition)-(item + subIndex2 - 1));
                                        else
                                            TmpStr1 = recordBuffer.Substring(0, (CutByPosition)-(0)); 
                                    }
                                    else
                                        TmpStr1 = ""; 
                                } 
                            } 
                        } 
                    }
                    else
                    {
                        TmpStr1 = "";
                    } 
                    // Some def-files use incorrect indexes to create empty column.
                    // Usually this column is used to construct features.
                    // We think this is a bad practice, but we can not do anything with it right now.
                    // So we disable error notification for this case for a while (the next line is commented).
                    //getEngine().notifyTechSupport( MLSEngine.SUPPORT_CODE_HEADER_BAD_INDEX );
                    if (fieldDelimiter != null)
                    {
                        if (TmpStr1.equals(fieldDelimiter.toString()))
                            TmpStr1 = "";
                         
                    }
                     
                }
                else // end of case #1 - @
                if (fieldValuePosition.charAt(0) == '\\')
                {
                    // Case #2 - \FIELDNAME\
                    ContFieldNames = cmaField.getContainedFields();
                    TmpStr1 = " ";
                    boolean isPrevValue = false;
                    for (j = 0;j < ContFieldNames.length;j++)
                    {
                        //get field name
                        fieldValuePosition = ContFieldNames[j];
                        if (!(fieldValuePosition.IndexOf("DB.AG", StringComparison.CurrentCultureIgnoreCase) > -1 || fieldValuePosition.IndexOf("DB.OF", StringComparison.CurrentCultureIgnoreCase) > -1))
                        {
                            int pos = m_CmaFields.getFieldIndex(fieldValuePosition);
                            if (pos >= 0 && pos < tempRecord.length)
                                TmpStr2 = tempRecord[pos];
                            else
                                TmpStr2 = ""; 
                        }
                        else
                        {
                            //Get data value from DB
                            int posReferenceID = fieldValuePosition.indexOf(":");
                            String referenceID = fieldValuePosition.substring(posReferenceID + 1);
                            String dbFieldName = fieldValuePosition.substring(0, (0) + (posReferenceID));
                            int pos = m_CmaFields.getFieldIndex(referenceID);
                            if (pos >= 0 && pos < tempRecord.length)
                                TmpStr2 = tempRecord[pos];
                            else
                                TmpStr2 = ""; 
                            if (!StringSupport.isNullOrEmpty(TmpStr2))
                            {
                                if (fieldValuePosition.StartsWith("DB.AG", StringComparison.CurrentCultureIgnoreCase))
                                    TmpStr2 = engine.getTpAppRequest().m_connector.getDataFromDB(dbFieldName,TmpStr2,"AG");
                                else if (fieldValuePosition.StartsWith("DB.OF", StringComparison.CurrentCultureIgnoreCase))
                                    TmpStr2 = engine.getTpAppRequest().m_connector.getDataFromDB(dbFieldName,TmpStr2,"OF");
                                  
                            }
                             
                        } 
                        if (cmaField.isStd(TCSStandardResultFields.STDF_CMAFEATURE))
                            TmpStr2 = net.toppro.components.mls.engine.CmaField.getValidFeature(TmpStr2);
                         
                        TmpStr2 = cmaField.prepareValueEx(TmpStr2,defParser,getEngine());
                        isDonePrepareValue = true;
                        if (isPrevValue == true && TmpStr2 != null && TmpStr2.length() != 0)
                        {
                            if (StringSupport.Trim(TmpStr1).length() != 0 || StringSupport.Trim(TmpStr2).length() != 0)
                                TmpStr1 += MLSUtil.toStr(cmaField.getDelimiter());
                             
                            if (cmaField.isStd(TCSStandardResultFields.STDF_CMAFEATURE))
                                TmpStr1 += " ";
                             
                        }
                         
                        if (TmpStr2 != null && TmpStr2.length() != 0 && cmaField.getCombine().length() == 0)
                        {
                            if (TmpStr1.length() != 0)
                            {
                                if (TmpStr1.length() == 1 && TmpStr1.charAt(0) == ' ')
                                    TmpStr1 = "";
                                 
                                TmpStr1 += TmpStr2;
                                isPrevValue = true;
                            }
                            else if (TmpStr1.length() == 0 && isPrevValue == true)
                                TmpStr1 += TmpStr2;
                            else
                                isPrevValue = false;  
                        }
                        else
                        {
                            if (Combine.length() != 0)
                            {
                                TmpStr1 = cmaField.combineEx(TmpStr2,TmpStr1);
                                if (TmpStr1.length() != 0)
                                {
                                    isPrevValue = true;
                                    if (Combine.toUpperCase().equals(net.toppro.components.mls.engine.CmaField.COMBINE_FNB.toUpperCase()))
                                        break;
                                     
                                }
                                 
                            }
                             
                        } 
                        if (StringSupport.Trim(TmpStr1).length() != 0)
                            isDonePrepareValue = true;
                         
                    }
                }
                else //processing
                // end of case #2 - /FIELDNAME/
                if (fieldValuePosition.charAt(0) == '{')
                {
                    // case #3 - {
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    if ((k = SupportClass.getIndex(fieldValuePosition,"}",1)) == -1)
                    {
                        ErrorFlag = true;
                        break;
                    }
                     
                    //Error
                    strToken1 = new SupportClass.Tokenizer(fieldValuePosition.Substring(1, (k)-(1)), ",");
                    subIndex1 = Integer.valueOf(new StringBuilder((String)strToken1.nextToken()).toString());
                    subIndex2 = Integer.valueOf(new StringBuilder((String)strToken1.nextToken()).toString());
                    if (cmaField.getInpLength() > 0)
                        TmpStr1 = recordBuffer.substring(subIndex2 - 1, (subIndex2 - 1) + ((cmaField.getInpLength()) - (subIndex2 - 1)));
                    else
                        TmpStr1 = ""; 
                }
                else // end of case #3 - {
                if (fieldValuePosition.charAt(0) == '"')
                {
                    // case #4
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    if ((j = SupportClass.getIndex(fieldValuePosition,"\"",1)) != -1)
                    {
                        fieldValuePosition = fieldValuePosition.Substring(1, (j)-(1));
                        NumReps = cmaField.getNumReps();
                        if (NumReps <= 0)
                            NumReps = 1;
                         
                        NumRepsPos = 0;
                        TmpStr1 = "";
                        for (nr = 0;nr < NumReps;nr++)
                        {
                            CutByPosition = -1;
                            if (NumRepsPos >= recordBuffer.length())
                                break;
                             
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            if ((j = SupportClass.getIndex(recordBuffer,fieldValuePosition,NumRepsPos)) != -1)
                            {
                                if (j != 0 && MLSUtil.isAlpha(recordBuffer.charAt(j - 1)) && cmaField.getNumReps() > -1)
                                {
                                    j++;
                                    NumRepsPos = j + 1;
                                    nr--;
                                    continue;
                                }
                                 
                                NumRepsPos = j + 1;
                                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                                k = SupportClass.getIndex(recordBuffer,"\n",j + 1);
                                if (MLSUtil.toStr(cmaField.getCutBy()).length() > 0)
                                {
                                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                                    CutByPosition = SupportClass.getIndex(recordBuffer,MLSUtil.toStr(cmaField.getCutBy()),j + fieldValuePosition.length());
                                }
                                 
                                Inplength = cmaField.getInpLength();
                                if (CutByPosition != -1)
                                    // && CutByPosition < k) fix bug #50348
                                    k = CutByPosition;
                                 
                                if (Inplength == 0)
                                    TmpStr1 += "";
                                else
                                {
                                    if (fieldValuePosition.charAt(0) != '{' && cmaField.getRecordPosition().indexOf("{") != -1 && cmaField.getRecordPosition().indexOf("}") != -1)
                                    {
                                        //if in def file, the schema looks like "keyword"{2,4} or  "keyword"{0,-11}
                                        String keyword = cmaField.getRecordPosition();
                                        int lineNumber = MLSUtil.getLineNumber(keyword);
                                        int beginOffset = MLSUtil.getOffset(keyword);
                                        if (lineNumber == 0)
                                        {
                                            if (beginOffset > 0)
                                                beginOffset--;
                                             
                                            TmpStr1 = recordBuffer.substring(j + fieldValuePosition.length() + beginOffset, (j + fieldValuePosition.length() + beginOffset) + ((j + fieldValuePosition.length() + beginOffset + Inplength) - (j + fieldValuePosition.length() + beginOffset)));
                                        }
                                        else
                                        {
                                            String destLine = MLSUtil.toDestLine(recordBuffer,keyword,lineNumber);
                                            if ((beginOffset + Inplength) <= destLine.length())
                                                TmpStr1 = destLine.substring(beginOffset, (beginOffset) + ((beginOffset + Inplength) - (beginOffset)));
                                            else if (destLine.length() >= beginOffset)
                                                TmpStr1 = destLine.Substring(beginOffset, (destLine.Length)-(beginOffset));
                                            else
                                                TmpStr1 = "";  
                                        } 
                                    }
                                    else
                                    {
                                        if (k != -1 && k < j + fieldValuePosition.length() + Inplength)
                                            TmpStr2 = recordBuffer.Substring(j + fieldValuePosition.length(), (k)-(j + fieldValuePosition.length()));
                                        else if (j + fieldValuePosition.length() + Inplength >= recordBuffer.length())
                                            TmpStr2 = recordBuffer.Substring(j + fieldValuePosition.length(), (recordBuffer.Length)-(j + fieldValuePosition.length()));
                                        else
                                        {
                                            TmpStr2 = recordBuffer.substring(j + fieldValuePosition.length(), (j + fieldValuePosition.length()) + ((j + fieldValuePosition.length() + Inplength) - (j + fieldValuePosition.length())));
                                            if (CutByPosition < j + fieldValuePosition.length() + Inplength && CutByPosition > j + fieldValuePosition.length())
                                                NumRepsPos = CutByPosition - 1;
                                            else
                                                // if(CutByPosition > j + fieldValuePosition.length() + Inplength || CutByPosition = -1)
                                                NumRepsPos = j + fieldValuePosition.length() + Inplength - 1; 
                                        }  
                                        if (StringSupport.Trim(TmpStr2).length() != 0)
                                        {
                                            TmpStr2 = cmaField.prepareValueEx(TmpStr2,defParser,getEngine());
                                            isDonePrepareValue = true;
                                            if (Combine.length() == 0)
                                            {
                                                if (StringSupport.Trim(TmpStr1).length() != 0 && StringSupport.Trim(TmpStr2).length() != 0)
                                                    TmpStr1 += cmaField.getDelimiter();
                                                 
                                                TmpStr1 += TmpStr2;
                                            }
                                            else
                                            {
                                                if (StringSupport.Trim(TmpStr1).length() != 0 && StringSupport.Trim(TmpStr2).length() != 0)
                                                    TmpStr1 += MLSUtil.toStr(cmaField.getDelimiter());
                                                 
                                                TmpStr1 = cmaField.combineEx(TmpStr2,TmpStr1);
                                                if (TmpStr1.length() != 0 && Combine.toUpperCase().equals(net.toppro.components.mls.engine.CmaField.COMBINE_FNB.toUpperCase()))
                                                    break;
                                                 
                                            } 
                                        }
                                         
                                        if (TmpStr1.length() != 0 && NumReps > 1)
                                        {
                                            //TmpStr1 = cmaField.prepareValue2(TmpStr1);
                                            isDonePrepareValue = true;
                                        }
                                         
                                    } 
                                } 
                            }
                            else
                                TmpStr1 += ""; 
                        }
                    }
                     
                }
                else
                {
                    //for(nr = 0; nr < NumReps; nr++)
                    // end of case#4
                    j = 0;
                    TmpStr1 = "";
                    NumReps = cmaField.getNumReps();
                    if (NumReps <= 0)
                        NumReps = 1;
                     
                    for (nr = 0;nr < NumReps;nr++)
                    {
                        //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                        if ((j = SupportClass.getIndex(recordBuffer,fieldValuePosition,j)) != -1)
                        {
                            TmpStr2 = "";
                            if (j != 0 && recordBuffer.charAt(j - 1) != 10)
                            {
                            }
                             
                            //Fix defect #47471 #47478 KevinY
                            if (j != 0 && MLSUtil.isAlpha(recordBuffer.charAt(j - 1)))
                            {
                                j++;
                                nr--;
                                continue;
                            }
                             
                            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                            k = SupportClass.getIndex(recordBuffer,"\n",j + 1);
                            if (MLSUtil.toStr(cmaField.getCutBy()).length() > 0)
                            {
                                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                                CutByPosition = SupportClass.getIndex(recordBuffer,MLSUtil.toStr(cmaField.getCutBy()),j + 1);
                            }
                             
                            Inplength = cmaField.getInpLength();
                            if (CutByPosition != -1 && CutByPosition < k)
                                k = CutByPosition;
                             
                            if (Inplength == 0 && k == -1)
                                TmpStr1 += "";
                            else if (Inplength == 0)
                            {
                                if (k != -1 && k > (j + fieldValuePosition.length()))
                                    TmpStr2 = recordBuffer.substring(j + fieldValuePosition.length(), (j + fieldValuePosition.length()) + ((k + 1) - (j + fieldValuePosition.length())));
                                else
                                    TmpStr2 = ""; 
                            }
                            else //recordBuffer.substring(j + fieldValuePosition.length(), j + fieldValuePosition.length() + Inplength - 1);
                            if (k == -1)
                            {
                                if (j + fieldValuePosition.length() < j + fieldValuePosition.length() + Inplength)
                                {
                                    if ((j + fieldValuePosition.length() + Inplength) < recordBuffer.length())
                                        TmpStr2 = recordBuffer.substring(j + fieldValuePosition.length(), (j + fieldValuePosition.length()) + ((j + fieldValuePosition.length() + Inplength) - (j + fieldValuePosition.length())));
                                    else
                                        TmpStr2 = recordBuffer.Substring(j + fieldValuePosition.length(), (recordBuffer.Length)-(j + fieldValuePosition.length())); 
                                }
                                else
                                    TmpStr1 += ""; 
                            }
                            else
                            {
                                if (k > j + fieldValuePosition.length() && k < j + fieldValuePosition.length() + Inplength - 1)
                                {
                                    TmpStr2 = recordBuffer.Substring(j + fieldValuePosition.length(), (k)-(j + fieldValuePosition.length()));
                                }
                                else if (j + fieldValuePosition.length() < j + fieldValuePosition.length() + Inplength && k > j + fieldValuePosition.length() + Inplength)
                                {
                                    TmpStr2 = recordBuffer.substring(j + fieldValuePosition.length(), (j + fieldValuePosition.length()) + ((j + fieldValuePosition.length() + Inplength) - (j + fieldValuePosition.length())));
                                }
                                else
                                {
                                    //in some situation, the Inplength will too enough
                                    TmpStr2 = recordBuffer.substring(j + fieldValuePosition.length(), (j + fieldValuePosition.length()) + ((k + 1) - (j + fieldValuePosition.length())));
                                }  
                            }   
                            //k+1 = "\n\r"
                            if (cmaField.isStd(TCSStandardResultFields.STDF_CMAFEATURE))
                                TmpStr2 = net.toppro.components.mls.engine.CmaField.getValidFeature(TmpStr2);
                             
                            if (StringSupport.Trim(TmpStr2).length() != 0)
                            {
                                TmpStr2 = cmaField.prepareValueEx(TmpStr2,defParser,getEngine());
                                isDonePrepareValue = true;
                                if (Combine.length() == 0)
                                {
                                    if (StringSupport.Trim(TmpStr1).length() != 0 && StringSupport.Trim(TmpStr2).length() != 0)
                                        TmpStr1 += MLSUtil.toStr(cmaField.getDelimiter());
                                     
                                    TmpStr1 += TmpStr2;
                                }
                                else
                                {
                                    if (StringSupport.Trim(TmpStr1).length() != 0 && StringSupport.Trim(TmpStr2).length() != 0)
                                        TmpStr1 += MLSUtil.toStr(cmaField.getDelimiter());
                                     
                                    TmpStr1 = cmaField.combineEx(TmpStr2,TmpStr1);
                                    if (TmpStr1.length() != 0 && Combine.toUpperCase().equals(net.toppro.components.mls.engine.CmaField.COMBINE_FNB.toUpperCase()))
                                        break;
                                     
                                } 
                            }
                             
                            if (TmpStr1.length() != 0 && NumReps > 1)
                            {
                                //TmpStr1 = cmaField.prepareValue2(TmpStr1);
                                isDonePrepareValue = true;
                            }
                             
                            j++;
                            if (j >= recordBuffer.length())
                                break;
                             
                        }
                        else
                            break; 
                    }
                }    
                //if
                // end of else
                if (TmpStr1.length() != 0 && isDonePrepareValue == false)
                {
                    if (isIncludingTlBlob)
                        TmpRawStr = cmaField.prepareRawValueEx(TmpStr1,defParser,getEngine());
                     
                    TmpStr1 = cmaField.prepareValueEx(TmpStr1,defParser,getEngine());
                }
                 
                //Fix defect #67871, if age or year build = 0, year build will be the current year.
                //if ( cmaField.isStd ( TCSStandardResultFields.STDF_CMAAGE  ) && TmpStr1.trim().equalsIgnoreCase("0") )
                //	TmpStr1 = "";
                if (cmaField.isStd(TCSStandardResultFields.STDF_CMAFEATURE))
                {
                    new MLSUtil();
                    TmpStr1 = MLSUtil.replaceSubStr(TmpStr1,":",": ");
                }
                 
                if (cmaField.isStd(TCSStandardResultFields.STDF_CMAAGE))
                {
                    if ((cmaField.caption.toLowerCase().indexOf("yr") != -1 || cmaField.caption.toLowerCase().indexOf("year") != -1) && cmaField.caption.toLowerCase().indexOf("age") == -1 && TmpStr1.length() == 2)
                    {
                        try
                        {
                            if (currentYear - 1999 >= ((int)((Integer.valueOf(TmpStr1.toString())))))
                                TmpStr1 = "20" + TmpStr1;
                            else
                                TmpStr1 = "19" + TmpStr1; 
                        }
                        catch (Exception e)
                        {
                        }
                    
                    }
                     
                }
                 
                if (cmaField.isStd(TCSStandardResultFields.STDF_RECORDID))
                {
                    TmpStr1 = StringSupport.Trim(TmpStr1);
                    MlsNumber = i;
                    //TmpStr1 = new MLSUtil().removeSpace(TmpStr1);
                    if (TmpStr1.length() == 0)
                    {
                        MlsNumberValue = CMA_MLSRECORDID_PRE + Integer.toString(m);
                        //ErrorFlag = true;
                        TmpStr1 = MlsNumberValue;
                        m++;
                    }
                    else
                    {
                        MlsNumberValue = TmpStr1;
                        isVaildRecord = true;
                    } 
                }
                 
                if (ErrorFlag == true)
                    break;
                 
                if (StringSupport.Trim(TmpStr1).length() > 0 && !cmaField.isStd(TCSStandardResultFields.STDF_RECORDID))
                {
                    isVaildRecord = true;
                }
                 
                if (StringSupport.Trim(TmpStr1).length() == 0 && cmaField.defaultvalue.length() > 0)
                    TmpStr1 = cmaField.defaultvalue;
                 
                tempRecord[i] = StringSupport.Trim(TmpStr1);
                if (isIncludingTlBlob)
                    tempRecordRawData[i] = StringSupport.Trim(TmpRawStr);
                 
            }
            //for(i = 1; i < cmaFields.size()-1; i++ )
            if (i >= tempRecord.length && isVaildRecord == true)
            {
                MLSRecord record = new MLSRecord(m_CmaFields,tempRecord);
                if (isIncludingTlBlob)
                    record.addRawData(tempRecordRawData);
                 
                getEngine().addMLSRecord(record);
            }
             
        }
        //getEngine().WriteLine("parsing 5= " + ((System.DateTime.Now.Ticks - 621355968000000000) / 10000 - time));
        engine.disableBufferedNotification();
        if (!engine.getIsDownloadIDs())
            engine.getEnvironment().addRawDataFile(datFileName);
         
        engine.AddParsingRawDataTime((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time);
        getEngine().writeLine("Time for parsing raw-data= " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
    }

    /**
    * Gets Default ToopTip for MLS Board. It may be overwritten by some special clients with special
    * tool tips.
    */
    public String getDefaultToolTip() throws Exception {
        return STRINGS.get_Renamed(STRINGS.COMM_CLIENT_PROPERTY_SEARCH_DEFAULT_TIP);
    }

    /**
    * @param _Engine MLS engine.
    */
    protected public CommunicationClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        initBlock();
        m_Engine = _Engine;
    }

    /**
    * @return  MLS engine.
    */
    protected public net.toppro.components.mls.engine.MLSEngine getEngine() throws Exception {
        return m_Engine;
    }

    /**
    * @param _data content of dataFile
    * 
    *  @param _delimiter delimiter of records
    * 
    *  @return  records count in data file
    */
    protected public int getRecordCount(String _data, String _delimiter) throws Exception {
        int count = 0;
        int i = 0;
        int k = 0;
        while (i != -1)
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            i = SupportClass.getIndex(_data,_delimiter,k);
            if (i != -1)
            {
                k = i + 1;
                count++;
                if (k >= _data.length())
                    i = -1;
                 
            }
             
        }
        return count;
    }

    /**
    * Sets section's name for [COMMON]-section. For example:
    * [<b>Common</b>], [<b>Common_Alt</b>]
    * 
    *  @param section section's name
    */
    protected public void setCommonSectionName(String section) throws Exception {
        m_CommonSect = section;
    }

    /**
    * @return  common section's name
    */
    protected public String getCommonSectionName() throws Exception {
        return m_CommonSect;
    }

    /**
    * Sets section's name for [RCVDATA]-section. For example:
    * [<b>RcvData</b>], [<b>RcvData_Alt</b>]
    * 
    *  @param section section's name
    */
    protected public void setRcvDataSectionName(String section) throws Exception {
        m_RcvDataSect = section;
    }

    /**
    * @return  common section's name
    */
    protected public String getRcvDataSectionName() throws Exception {
        return m_RcvDataSect;
    }

    /**
    * @return  combination of FLAG_PICSCRIPT_INSIDE_MAINSCRIPT and FLAG_REMSCRIPT_INSIDE_MAINSCRIPT
    */
    public int getFlags() throws Exception {
        return 0;
    }

    private static int[] parseRecord(String str, String delimiter) throws Exception {
        int ARRAY_LENGHT = 256;
        int[] ofsets = new int[ARRAY_LENGHT];
        int ptr = 0;
        int index = 0;
        if (!delimiter.equals(","))
        {
            for (int i = str.indexOf(delimiter);i >= 0;i = SupportClass.getIndex(str,delimiter,ptr))
            {
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                ofsets[index++] = ptr;
                if (index >= ARRAY_LENGHT)
                {
                    int[] new_buffer = new int[ofsets.length * 2];
                    Array.Copy(ofsets, 0, new_buffer, 0, ofsets.length);
                    ofsets = new_buffer;
                    ARRAY_LENGHT *= 2;
                }
                 
                ptr = i + delimiter.length();
            }
        }
        else
        {
            int beg = 0;
            boolean inside_quotes = false;
            char[] szStr = str.toCharArray();
            for (int i = str.indexOf(delimiter);i >= 0;i = SupportClass.getIndex(str,delimiter,beg))
            {
                for (int j = beg;j < i;j++)
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    if (szStr[j] == '"')
                        inside_quotes = !inside_quotes;
                     
                beg = i + delimiter.length();
                if (!inside_quotes)
                {
                    ofsets[index++] = ptr;
                    if (index >= ARRAY_LENGHT)
                    {
                        int[] new_buffer = new int[ofsets.length * 2];
                        Array.Copy(ofsets, 0, new_buffer, 0, ofsets.length);
                        ofsets = new_buffer;
                        ARRAY_LENGHT *= 2;
                    }
                     
                    ptr = beg;
                }
                 
            }
        } 
        if (ofsets.length <= 0)
            ofsets = null;
        else
        {
            ofsets[index++] = ptr;
            int[] new_buffer = new int[index];
            Array.Copy(ofsets, 0, new_buffer, 0, index);
            return new_buffer;
        } 
        return ofsets;
    }

    private static ArrayList _parseRecord(String str, String delimiter) throws Exception {
        ArrayList ofsets = ArrayList.Synchronized(new ArrayList(10));
        int ptr = 0;
        if (!delimiter.equals(","))
        {
            for (int i = str.indexOf(delimiter);i >= 0;i = SupportClass.getIndex(str,delimiter,ptr))
            {
                //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                ofsets.add(((int)(ptr)));
                ptr = i + delimiter.length();
            }
        }
        else
        {
            int beg = 0;
            boolean inside_quotes = false;
            for (int i = str.indexOf(delimiter);i >= 0;i = SupportClass.getIndex(str,delimiter,beg))
            {
                for (int j = beg;j < i;j++)
                    //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
                    if (str.charAt(j) == '"')
                        inside_quotes = !inside_quotes;
                     
                beg = i + delimiter.length();
                if (!inside_quotes)
                {
                    ofsets.add(((int)(ptr)));
                    ptr = beg;
                }
                 
            }
        } 
        if (ofsets.size() <= 0)
            ofsets = null;
        else
            ofsets.add(((int)(ptr))); 
        return ofsets;
    }

    /**
    * serialization
    */
    //UPGRADE_TODO: Class 'java.io.ObjectOutputStream' was converted to 'System.IO.BinaryWriter' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectOutputStream'"
    public void save(System.IO.BinaryWriter out_Renamed) throws Exception {
    }

    // empty
    /**
    * 
    */
    //UPGRADE_TODO: Class 'java.io.ObjectInputStream' was converted to 'System.IO.BinaryReader' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioObjectInputStream'"
    public void load(System.IO.BinaryReader in_Renamed) throws Exception {
    }

    /**
    * Create hashTable by header information in result file
    *  @return  Hashtable with:
    * key - Column's name
    * value - Column's Index
    */
    private Hashtable getHeaders(String datFileName, String beginHeaderInfo, String endHeaderInfo, String fieldDelimiter) throws Exception {
        Hashtable headers = Hashtable.Synchronized(new Hashtable());
        //read file to stream
        FileStreamSupport stream = null;
        StringBuilder file_context = new StringBuilder();
        System.IO.BufferedStream reader = null;
        try
        {
            stream = new FileStreamSupport(datFileName, FileMode.Open, FileAccess.Read);
            int b;
            reader = new System.IO.BufferedStream(stream);
            while ((b = reader.ReadByte()) > -1)
            file_context.append((char)b);
        }
        catch (Exception ioe)
        {
            return null;
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    //UPGRADE_TODO: Method 'java.io.FilterInputStream.close' was converted to 'System.IO.BinaryReader.Close' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFilterInputStreamclose'"
                    reader.Close();
                }
                 
            }
            catch (Exception exc)
            {
                //UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
                SupportClass.WriteStackTrace(exc, Console.Error);
            }

            try
            {
                if (stream != null)
                    stream.close();
                 
            }
            catch (Exception exc)
            {
                //UPGRADE_NOTE: Exception 'java.lang.Throwable' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
                SupportClass.WriteStackTrace(exc, Console.Error);
            }
        
        }
        int bhi, ehi = 0;
        if (beginHeaderInfo.length() != 0)
            bhi = file_context.toString().indexOf(beginHeaderInfo);
        else
            bhi = 0; 
        if (bhi != -1)
        {
            if (beginHeaderInfo.length() != 0)
                bhi += beginHeaderInfo.length();
             
            if (endHeaderInfo != null && (ehi = file_context.toString().indexOf(endHeaderInfo)) == -1)
                return null;
            else if (endHeaderInfo == null)
                ehi = file_context.toString().length();
              
            StringTokenizerEx header_info = new StringTokenizerEx(file_context.toString().Substring(bhi, (ehi)-(bhi)), fieldDelimiter);
            String Header = "";
            int Count = 0;
            while (header_info.hasMoreElements())
            {
                Header = header_info.nextElement();
                if (Header.endsWith(fieldDelimiter))
                    Header = Header.substring(0, (0) + ((Header.lastIndexOf(fieldDelimiter)) - (0))).toUpperCase();
                 
                Count++;
                if (Header.length() == 0)
                    continue;
                 
                headers.put(Header.toUpperCase(), ((int)(Count)));
            }
        }
        else
            return null; 
        return headers;
    }

    private int getHeader(Hashtable Headers, String HeaderName, int rank) throws Exception {
        int position = -1;
        if (Headers == null || Headers.size() == 0)
            return position;
         
        if (HeaderName.length() == 0)
            return position;
         
        String name;
        IEnumerator enumA = EnumeratorSupport.mk(Headers.keySet().iterator());
        int i = 0;
        while (enumA.moveNext())
        {
            //UPGRADE_TODO: Method 'java.util.Enumeration.hasMoreElements' was converted to 'System.Collections.IEnumerator.MoveNext' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationhasMoreElements'"
            //UPGRADE_TODO: Method 'java.util.Enumeration.nextElement' was converted to 'System.Collections.IEnumerator.Current' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javautilEnumerationnextElement'"
            name = ((String)enumA.getCurrent());
            if (FileFilter.match(name,HeaderName) == true)
            {
                position = (int)(Headers.get(name));
                if (rank == i)
                    return position;
                 
                i++;
            }
             
        }
        return position;
    }

}


