//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:33 PM
//
package net.toppro.components.mls.engine.client;
import CS2JNet.System.IO.FileAccess;
import CS2JNet.System.IO.FileMode;
import CS2JNet.System.IO.FileNotFoundException;
import CS2JNet.System.IO.FileStreamSupport;
import CS2JNet.System.StringSupport;
import net.toppro.components.mls.engine.*;
import net.toppro.components.mls.engine.client.STRINGS;

import java.io.File;
import java.util.Calendar;

public class ASCIIClient  extends net.toppro.components.mls.engine.CommunicationClient 
{

    public static final String FIELDNAME_PICTUREFILESELECT = "PictureFileSelect";
    private static final String FIELDNAME_FOLDERSELECT = "FolderSelect";
    private static final String ZIPPICTURE_FLAG = "1";
    private static final String PHOTOS_ZIP = "photos.zip";
    private static final String IMPORT_DATA_TEXT = "ImportData.txt";
    private static final String IMPORT_DATA_ZIP = "ImportData.zip";
    private static final String SELECT_PICTURE_FOLDER = "Select MLS pictures folder";
    private static final String SELECT_PICTURE_FILE = "Select MLS pictures file";
    private static final String SELECT_DATA_FILE = "Select MLS data file";
    /**
    * @return  combination of FLAG_PICSCRIPT_INSIDE_MAINSCRIPT
    */
    public ASCIIClient(net.toppro.components.mls.engine.MLSEngine _Engine) throws Exception {
        super(_Engine);
    }

    public void initBoardSetup(BoardSetup setup) throws Exception {
    }

    public int getFlags() throws Exception {
        return FLAG_PICSCRIPT_INSIDE_MAINSCRIPT;
    }

    /**
    * 
    */
    public String getSourceFolderName() throws Exception {
        return getEngine().getEnvironment().getUploadFolder();
    }

    //+ field.getValue();
    /**
    * 
    */
    public String getSourceFileName() throws Exception {
        String filePath = getEngine().getEnvironment().getUploadFolder() + IMPORT_DATA_TEXT;
        if ((new File(filePath)).exists())
            return filePath;
        else
            return getEngine().getEnvironment().getUploadFolder() + IMPORT_DATA_ZIP; 
    }

    public String getDefaultToolTip() throws Exception {
        return getEngine().getBoardSetup().getNotes();
    }

    /**
    * This function is called by MLSEngine right after client constructor. Client must implement
    * it to initialize search fields (we call them property fields).
    * 
    *  @param fields object containing property fields info. Implementation should use
    * MLSPropertyFields.addField, or MLSPropertyFields.initPropetiesFields functions to
    * initialize it.
    * 
    *  @see MLSPropertyFields, PropertyField
    */
    public void initPropertyFields(PropertyFieldGroups groups) throws Exception {
        net.toppro.components.mls.engine.PropertyField f = null;
        //f = new PropertyField();
        //f.setName("Label1");
        //f.setControlType(PropertyField.CONTROL_TYPE_LABELPANEL);
        //getEngine().addPropertyField(f);
        f = new net.toppro.components.mls.engine.PropertyField();
        f.setName(FIELDNAME_FILESELECT);
        f.setControlType(net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_FILESELECT_PANEL);
        f.setCaption(SELECT_DATA_FILE);
        f.setRequired(true);
        f.setDelimiter("\r");
        f.setLayout(75);
        getEngine().addPropertyField(f);
        // now show all pic selection folder for all board for r8.#48831
        //String picFlag = getEngine().getDefParser().getValue(getEngine().SECTION_PICTURES,getEngine().ATTRIBUTE_PICIMPFLAG);
        //if (picFlag!=null && !picFlag.equalsIgnoreCase("0") && picFlag.trim().length()!=0)
        //{
        f = new net.toppro.components.mls.engine.PropertyField();
        if (isPicZipFile())
        {
            f.setName(FIELDNAME_PICTUREFILESELECT);
            f.setDelimiter("\r");
            f.setCaption(SELECT_PICTURE_FILE);
            f.setControlType(net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_PICTUREFILESELECT_PANEL);
        }
        else
        {
            f.setName(FIELDNAME_FOLDERSELECT);
            f.setDelimiter("\r");
            f.setCaption(SELECT_PICTURE_FOLDER);
            f.setControlType(net.toppro.components.mls.engine.PropertyField.CONTROL_TYPE_FOLDERSELECT_PANEL);
        } 
        f.setLayout(75);
        getEngine().addPropertyField(f);
    }

    //}
    /**
    * Launch process of downloading of data(records) from MLS Board.
    */
    public void runMainScript() throws Exception {
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        try
        {
            String commandName = "";
            String paramValue = "";
            int i = 0, j = 0, result = 0;
            StringBuilder Str = null;
            StringBuilder TimeOut = null;
            String DataFileName = engine.getResultsFilename();
            //if (!engine.getPropertyFields().CheckRequeredFields())
            //{
            //    throw engine.createException(STRINGS.get_Renamed(STRINGS.S31));
            //}
            String tmpDataFileName = getSourceFileName();
                ;
            File f = new File(tmpDataFileName);
            boolean tmpBool;
            if (File.Exists(f.FullName))
                tmpBool = true;
            else
                tmpBool = File.Exists(f.FullName); 
            if (!tmpBool)
            {
                throw engine.createException(STRINGS.get_Renamed(STRINGS.S36));
            }
             
            String[] files = new String[0];
            FileFilter remove_filter = null;
            if (tmpDataFileName.indexOf(".zip") != -1)
            {
                String DataFileFromDef = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_DATAFILENAME);
                if (DataFileFromDef.indexOf("*") >= 0 || DataFileFromDef.indexOf("?") >= 0)
                {
                    remove_filter = new FileFilter(getEngine().getResultsFolder(), FileFilter.FLAG_FIND_FILES);
                    files = remove_filter.getFiles(DataFileFromDef);
                    for (i = files.length - 1;i >= 0;i--)
                    {
                        if (!files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RESULTS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.FIELDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RECORDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.ENGINE_FILE.toUpperCase()))
                        {
                            File rf = new File(getEngine().getResultsFolder() + files[i]);
                            boolean tmpBool2;
                            if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool2 = true;
                            }
                            else if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool2 = true;
                            }
                            else
                                tmpBool2 = false;  
                            boolean generatedAux = tmpBool2;
                        }
                         
                    }
                }
                 
                String HeaderFile = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_HEADERINFOFILE);
                if (HeaderFile.indexOf("*") >= 0 || HeaderFile.indexOf("?") >= 0)
                {
                    files = remove_filter.getFiles(HeaderFile);
                    for (i = files.length - 1;i >= 0;i--)
                    {
                        if (!files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RESULTS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.FIELDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.RECORDS_FILE.toUpperCase()) && !files[i].toUpperCase().equals(net.toppro.components.mls.engine.MLSEngine.ENGINE_FILE.toUpperCase()))
                        {
                            File rf = new File(getEngine().getResultsFolder() + files[i]);
                            boolean tmpBool3;
                            if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool3 = true;
                            }
                            else if (File.Exists(rf.FullName))
                            {
                                File.Delete(rf.FullName);
                                tmpBool3 = true;
                            }
                            else
                                tmpBool3 = false;  
                            boolean generatedAux2 = tmpBool3;
                        }
                         
                    }
                }
                 
                MLSUtil.extractTo(tmpDataFileName, getEngine().getResultsFolder());
                tmpDataFileName = DataFileFromDef;
                if (tmpDataFileName.length() != 0)
                {
                    DataFileName = getEngine().getResultsFolder() + tmpDataFileName;
                    if (tmpDataFileName.indexOf("*") >= 0 || tmpDataFileName.indexOf("?") >= 0)
                    {
                        FileFilter filter = new FileFilter(getEngine().getResultsFolder(), FileFilter.FLAG_FIND_FILES);
                        files = filter.getFiles(tmpDataFileName);
                        if (filter.getSize() > 1)
                        {
                            j = 0;
                            for (i = files.length - 1;i >= 0;i--)
                            {
                                if (files[i].toUpperCase().contains(net.toppro.components.mls.engine.MLSEngine.RESULTS_FILE.toUpperCase()) || files[i].toUpperCase().contains(net.toppro.components.mls.engine.MLSEngine.FIELDS_FILE.toUpperCase()) || files[i].toUpperCase().contains(net.toppro.components.mls.engine.MLSEngine.RECORDS_FILE.toUpperCase()) || files[i].toUpperCase().contains(net.toppro.components.mls.engine.MLSEngine.ENGINE_FILE.toUpperCase()))
                                    j++;
                                else
                                    DataFileName = files[i]; 
                            }
                            //getEngine().getResultsFolder() +
                            if (files.length - j > 1)
                                throw engine.createException(STRINGS.get_Renamed(STRINGS.ERR_BAD_RESULTS_FORMAT));
                             
                        }
                        else
                            DataFileName = filter.getFirstMatch(tmpDataFileName); 
                    }
                     
                    File tmpFile = new File(DataFileName);
                    boolean tmpBool4;
                    if (File.Exists(tmpFile.FullName))
                        tmpBool4 = true;
                    else
                        tmpBool4 = File.Exists(tmpFile.FullName); 
                    if (!tmpBool4)
                        throw engine.createException(STRINGS.get_Renamed(STRINGS.ERR_BAD_RESULTS_FORMAT));
                     
                    //tmpFile.renameTo(new File(getEngine().getResultsFilename()));
                    DataFileName = engine.getResultsFilename();
                    MLSUtil.CopyFile(engine, tmpFile.FullName, DataFileName);
                }
                 
            }
            else
                MLSUtil.copyFile(engine,tmpDataFileName,DataFileName); 
        }
        catch (MLSException exc)
        {
            throw exc;
        }
        catch (Exception exc)
        {
            throw engine.createException(exc);
        }
    
    }

    /**
    * Launch process of downloading of pictures  from MLS Board.
    */
    public void runPicScript(MLSRecords records) throws Exception {
        long time = (Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000;
        net.toppro.components.mls.engine.MLSEngine engine = getEngine();
        try
        {
            Environment environment = engine.getEnvironment();
            int i = 0;
            //process of connection...
            environment.setMessage(STRINGS.get_Renamed(STRINGS.S11));
            String tmpPicsFolderName = "";
            String picPrefix = "";
            String picFileName = "";
            String picFilesExt = "";
            String columnData = "";
            tmpPicsFolderName = getSourceFolderName();
            boolean tmpBool = false;
            if (tmpPicsFolderName.length() > 0)
            {
                File f = new File(tmpPicsFolderName);
                if (File.Exists(f.FullName))
                    tmpBool = true;
                else
                    tmpBool = File.Exists(f.FullName); 
            }
             
            if (tmpPicsFolderName.length() > 0 && !tmpBool)
            {
                throw engine.createException(STRINGS.get_Renamed(STRINGS.S37));
            }
             
            File photos_zip_file = new File(tmpPicsFolderName + "\\" + PHOTOS_ZIP);
            boolean tmpBool2;
            if (File.Exists(photos_zip_file.FullName))
                tmpBool2 = true;
            else
                tmpBool2 = File.Exists(photos_zip_file.FullName); 
            if (tmpBool2)
            {
                MLSUtil.extractTo(tmpPicsFolderName + "\\" + PHOTOS_ZIP, getEngine().getResultsFolder());
                tmpPicsFolderName = getEngine().getResultsFolder();
            }
            else if ((new File(tmpPicsFolderName + "\\" + IMPORT_DATA_ZIP)).exists())
            {
                //Import data zip fiel could have pictures
                MLSUtil.extractTo(tmpPicsFolderName + "\\" + IMPORT_DATA_ZIP,tmpPicsFolderName);
            }
              
            if (tmpPicsFolderName.length() == 0)
                return ;
             
            picPrefix = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICPREFIX);
            picFilesExt = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICFILESEXT);
            if (picFilesExt.length() == 0)
                picFilesExt = "*";
            else
                picFilesExt = picFilesExt.toLowerCase(); 
            String newPicFilesExt = "";
            String picFileFilter = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICFILERULER);
            columnData = getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICRELFLDNAME);
            if (columnData.charAt(0) == '\\')
            {
                columnData = StringSupport.Trim(columnData.replace('\\', ' '));
            }
            else
            {
                net.toppro.components.mls.engine.CmaField field = getEngine().getCmaFields().getFieldByRecordPosition("\"" + columnData + "\"");
                if (field != null)
                    columnData = field.getName();
                 
            } 
            String recValue = "";
            int j;
            MLSRecord record = records.getFirstRecord();
            FileFilter filter = new FileFilter(tmpPicsFolderName,FileFilter.FLAG_FIND_FILES);
            String[] fl;
            while (record != null)
            {
                try
                {
                    //environment.checkStop();
                    String id = record.getRecordID();
                    newPicFilesExt = "";
                    picFileName = picPrefix;
                    if (picFileFilter.length() != 0 && !picFileFilter.equals("@") && picFileFilter.indexOf("@") != -1)
                    {
                        picFileName += picFileFilter;
                        picFileName = MLSUtil.replaceSubStr(picFileName,"@",record.getFieldValue(columnData));
                    }
                    else if (picFileFilter.length() != 0 && picFileFilter.equals("@"))
                    {
                        picFileName += picFileFilter;
                        picFileName = MLSUtil.replaceSubStr(picFileName,"@",record.getFieldValue(columnData));
                        picFileName += ".";
                        picFileName += picFilesExt;
                    }
                    else
                        recValue = record.getFieldValue(columnData).toLowerCase();  
                    if (recValue != null && recValue.length() != 0)
                        picFileName += recValue;
                     
                    if (picFileName.length() != 0)
                    {
                        if (picFileFilter.length() != 0 || picPrefix.indexOf("*") >= 0 || picPrefix.indexOf("?") >= 0 || picFilesExt.indexOf("*") >= 0 || picFilesExt.indexOf("?") >= 0)
                        {
                            String file;
                            if (picFileFilter.equals("@"))
                                file = filter.getFirstMatch(picFileName);
                            else
                                file = filter.getFirstMatch(picFileName + "." + picFilesExt); 
                            if (file != null && (j = file.lastIndexOf('.')) > 0)
                            {
                                if (file.substring(j + 1).equals(picFilesExt))
                                    picFileName = file.Substring(0, (j)-(0));
                                else
                                {
                                    picFileName = file.Substring(0, (j)-(0));
                                    newPicFilesExt = file.substring(j + 1);
                                } 
                            }
                             
                        }
                         
                        if (tmpPicsFolderName.charAt(tmpPicsFolderName.length() - 1) != '\\')
                            tmpPicsFolderName += "\\";
                         
                        File picFile;
                        if (newPicFilesExt.length() == 0)
                        {
                            if (picFileName.lastIndexOf("." + picFilesExt) == -1)
                                picFile = new File(tmpPicsFolderName + getFileName(picFileName) + "." + picFilesExt);
                            else
                                picFile = new File(tmpPicsFolderName + getFileName(picFileName)); 
                        }
                        else
                            picFile = new File(tmpPicsFolderName + getFileName(picFileName) + "." + newPicFilesExt); 
                        boolean tmpBool3;
                        if (File.Exists(picFile.FullName))
                            tmpBool3 = true;
                        else
                            tmpBool3 = File.Exists(picFile.FullName); 
                        if (tmpBool3)
                            MLSUtil.CopyFile(engine, picFile.FullName, record.getDedicatedPictureFileName());
                         
                    }
                     
                }
                catch (Exception exc)
                {
                }

                // We ignore exceptions. If we're unable to copy picture file - so be it.
                // Missing picture is quite possible situation.
                record = records.getNextRecord();
            }
        }
        catch (MLSException exc)
        {
            throw exc;
        }
        catch (Exception exc)
        {
            throw engine.createException(exc);
        }

        System.out.println("Time for ASCII-Client runPicScript = " + ((Calendar.getInstance().getTime().Ticks - 621355968000000000L) / 10000 - time));
    }

    private String getFileName(String file) throws Exception {
        String result = file;
        int beg = file.lastIndexOf('\\');
        int end = file.length();
        if (beg < 0)
            beg = 0;
        else
            beg++; 
        if (end > beg)
            result = file.Substring(beg, (end)-(beg));
        else
            result = file.substring(beg); 
        return result;
    }

    /**
    * Launch process of downloading of notes  from MLS Board.
    */
    public void runRemScript(MLSRecords records) throws Exception {
    }

    /**
    * Read raw data file and cut or remove garbage from.
    *  @param datFileName path to the data-file on local machine
    * 
    *  @return  good file's content
    */
    public String prepareDataFile(String datFileName) throws Exception {
        StringBuilder BeginDataDelimiter = null;
        DefParser m_DefParser = getEngine().getDefParser();
        boolean bCutStartProcessingString = false;
        String EndProcessingString = MLSUtil.toStr(m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_ENDPROCESSINGSTRING));
        BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_STARTPROCESSINGSTRING)));
        if (BeginDataDelimiter.length() == 0 && m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK).length() != 0)
            BeginDataDelimiter = new StringBuilder(MLSUtil.toStr(m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_RCVDATA,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_RECORDMARK)));
        else
            bCutStartProcessingString = true; 
        //read file to stream
        FileStreamSupport stream;
        StringBuilder file_context = new StringBuilder();
        try
        {
            //UPGRADE_TODO: Constructor 'java.io.FileInputStream.FileInputStream' was converted to 'System.IO.FileStream.FileStream' which has a different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1073_javaioFileInputStreamFileInputStream_javalangString'"
            stream = new FileStreamSupport(datFileName, FileMode.Open, FileAccess.Read);
        }
        catch (FileNotFoundException fnfe)
        {
            throw getEngine().createException(fnfe, STRINGS.get_Renamed(STRINGS.ERR_DATA_FILE_NOT_FOUND));
        }

        System.IO.BufferedStream reader = null;
        try
        {
            int b;
            reader = new System.IO.BufferedStream(stream);
            String sUnicode = m_DefParser.getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_TPONLINE,net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_IMPORTDATAUNICODE);
            boolean isUnicode = false;
            if (sUnicode != null && sUnicode.equals("1"))
                isUnicode = true;
             
            while ((b = reader.ReadByte()) > -1)
            {
                if (isUnicode || b != 0)
                    file_context.append((char)b);
                 
            }
        }
        catch (Exception ioe)
        {
            throw getEngine().createException(ioe);
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
        int i, j = 0;
        if ((i = file_context.toString().indexOf("<title>")) != -1)
        {
            String error = file_context.toString().substring(i + new StringBuilder("<title>").toString().length(), (i + new StringBuilder("<title>").toString().length()) + ((file_context.toString().indexOf("</title>")) - (i + new StringBuilder("<title>").toString().length())));
            String fileMsg = file_context.toString();
            fileMsg = MLSUtil.replaceSubStr(fileMsg,"<hr>","<hr><br>");
            if (fileMsg.length() > 1024)
                fileMsg = fileMsg.substring(1024);
             
            throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg, MLSException.FORMAT_HTML);
        }
         
        //find begin of data
        if (BeginDataDelimiter.length() != 0)
        {
            //UPGRADE_WARNING: Method 'java.lang.String.indexOf' was converted to 'System.String.IndexOf' which may throw an exception. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1101'"
            i = file_context.toString().indexOf(BeginDataDelimiter.toString(), 0);
        }
        else
            i = 0; 
        if (i == -1)
        {
            String fileMsg = StringSupport.Trim(file_context.toString());
            if (fileMsg.length() != 0)
            {
                fileMsg = MLSUtil.replaceSubStr(fileMsg,"\r\n","<br>");
                if (fileMsg.length() > 1024)
                    fileMsg = fileMsg.substring(1024);
                 
                throw getEngine().createException(STRINGS.get_Renamed(STRINGS.S32) + "<br><br>" + fileMsg, MLSException.FORMAT_HTML);
            }
            else
                return ""; 
        }
        else
        {
            String data = "";
            if (bCutStartProcessingString)
                data = file_context.toString().Substring(i + BeginDataDelimiter.toString().length(), (file_context.Length)-(i + BeginDataDelimiter.toString().length()));
            else
                data = file_context.toString(); 
            if (EndProcessingString.length() != 0 && (j = data.lastIndexOf(EndProcessingString)) != -1)
                return data.Substring(0, (j)-(0));
             
            return data;
        } 
    }

    protected public boolean isPicZipFile() throws Exception {
        return getEngine().getDefParser().getValue(net.toppro.components.mls.engine.MLSEngine.SECTION_PICTURES, net.toppro.components.mls.engine.MLSEngine.ATTRIBUTE_PICZIP).ToUpper().equals(ZIPPICTURE_FLAG.toUpperCase());
    }

}


//net.toppro.components.mls.engine.MLSEngine generatedAux = getEngine();
//net.toppro.components.mls.engine.MLSEngine generatedAux2 = getEngine();