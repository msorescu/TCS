//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:50 PM
//

package Tcs.Mls;

import CS2JNet.System.Xml.XmlNode;
import java.net.URI;
import Tcs.Mls.MLSConnector;
import Tcs.Mls.MLSEnvironment;
import Tcs.Mls.Picture;
import Tcs.Mls.TCSException;

public class MLSEnvironment  extends Environment 
{
    protected public MLSConnector m_connector = null;
    protected public Picture m_picture = null;
    public MLSEnvironment(MLSConnector connector) throws Exception {
        m_connector = connector;
    }

    /**
    * ***********************************************
    * *  Stuff below is an
    * *  net.toppro.components.mls.engine.Environment
    * *  implementation
    * ************************************************
    * 
    *  @return  the combination of FLAG_XXX constants that specify different aspects
    * of MLSEngine behaviour.
    */
    public int getFlags() throws Exception {
        try
        {
            if (m_connector == null)
                return MLSEnvironment.FLAG_NO_LOCAL_CACHE;
             
            return m_connector.getFlags();
        }
        catch (Exception e)
        {
            System.out.println("\r\n-------------- Error on Get Environment falgs ---------------\r\n");
            return 0;
        }
    
    }

    /**
    * @return  local path where MLSEngine may create it's files.
    * This function must ensure that the path exists. If it fails to create it, it should return null.
    */
    public String getLocalHomePath() throws Exception {
        String result = "";
        try
        {
            result = m_connector.getLocalHomePath();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public String getSearchTempFolder() throws Exception {
        String result = "";
        try
        {
            result = m_connector.getSearchTempFolder();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public String getUploadFolder() throws Exception {
        String result = "";
        try
        {
            result = m_connector.getUploadFolder();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    /**
    * @param fileURL graphic file URL.
    * 
    *  @return  true if an application is able to work with the file filePath
    * as with graphic file.
    */
    public boolean isPictureFileSupported(String filePath) throws Exception {
        try
        {
            m_picture = new Picture(filePath);
            switch(m_picture.getType())
            {
                case Picture.GIF: 
                case Picture.IPX: 
                case Picture.JPG: 
                case Picture.PNG: 
                    return true;
                default: 
                    return false;
            
            }
        }
        catch (Exception e)
        {
            return false;
        }
    
    }

    public byte[] getPictureData() throws Exception {
        if (m_picture != null)
            return m_picture.getBinaryData();
        else
            return null; 
    }

    /// <returns> the root URL for helper files (for example, zip-files containing some helper DLLs,
    /// executables or class-files for some of the clients).
    /// </returns>
    /// <deprecated> it is too specific. Theoretically engine may work in the environment where Internet
    /// is not available. getServerRoot is used in two places:
    /// <br>1. For downloading zip file (see installZipFile).
    /// <br>We can pass resource name instead of URL to installZipFile, so installZipFile should map
    /// the resource name to the actual file name and make all necessary downloads.
    /// <br>2. For downloading "no picture" image.
    /// <br>We can present the new function Environment.downloadNoPictureImage and do all the logic there.
    /// BTW: MLSEngine initialises directory names as singletons in a thread unsafe way. This should
    /// be fixed also.
    /// </deprecated>
    public URI getServerRoot() throws Exception {
        try
        {
            return new URI("file://c:/temp/tpsdata");
        }
        catch (System.UriFormatException e)
        {
        }

        return null;
    }

    /* TBI */
    //UPGRADE_TODO: Class 'java.net.URL' was converted to a 'System.Uri' which does not throw an exception if a URL specifies an unknown protocol. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1132'"
    /**
    * This function should replace some specific tags in str with the brand names for
    * the application:
    * {ApplicationName}      - Application name. Parameter "ApplicationName". Default value "TOP PRODUCER®".{ProductName}          - Product name. Parameter "ProductName". Default value "TOP PRODUCER®".{CompanyName}          - Company name. Parameter "CompanyName". Default value "TOP PRODUCER".{BrandName}            - Brand name. Parameter "BrandName". Default value "Standard".{TechSupportName}      - Tech support name. Parameter "TechSupportName". Default value "TOP PRODUCER Technical Support".{TechSupportPhone}     - Tech support phone. Parameter "TechSupportPhone". Default value "1-800-830-6047".{TechSupportEmail}     - Tech support email. Parameter "TechSupportEmail". Default value "support@topproducer.com".{WirelessProductName}  - Wireless product name. Parameter "WirelessProductName". Default value "TOP PRODUCER SellPhone™".{PalmProductName}      - Palm product name. Parameter "PalmProductName". Default value "Top Producer for Palm Computing Connected Organizers".{BrandDomainName}      - Brand domain name. Parameter "BrandDomainName". Default value "cp-demo.cp.net".{DesktopProductName}   - Desktop product name. Parameter "DesktopProductName". Default value "System 6/6i".{MLSProductName}       - MLS product name. Parameter "MLSProductName". Default value "TOP CONNECTOR 7i".{AccountProductName}   - Account manager product name. Parameter "AccountProductName". Default value "My Account".
    *  @param string with brand tags.
    * 
    *  @return  a string with tag brands replaced with the application specific values.
    */
    public String formatBrands(String str) throws Exception {
        return null;
    }

    /* TBI */
    /**
    * Downloads a zip-file from the server and unpacks it to the specified directory.
    *  @param sourceURLBase server URL base.
    * 
    *  @param sourceFile file name on server (the actual URL should be constructed by adding sourceFile
    * to sourceURLBase).
    * 
    *  @param destPath local path for unpacked downloaded files.
    * 
    *  @return  true on success and false on failture.
    */
    public boolean installZipFile(URI sourceURLBase, String sourceFile, String destPath) throws Exception {
        return false;
    }

    /* TBI */
    /**
    * Downloads lookup table.
    * Lookup table contains an information, which other name may be used for cma field in
    * the result data. If MLSEngine fails to find the field using name specified in a def-file,
    * it tries to find the field using names enumerated in lookup table.
    * This function should download lookup table.
    */
    public String downloadLookupTable() throws Exception {
        return "";
    }

    /* TBI */
    /**
    * Downloads the def-file.
    *  @return  the content of the def-file.
    */
    public String downloadDefFile() throws Exception {
        String result;
        try
        {
            result = m_connector.getDefFile();
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return result;
    }

    /**
    * Get a list of the def-files.
    *  @return  a list of the def-files path.
    */
    public XmlNode getMetaDataDEFList() throws Exception {
        XmlNode result;
        try
        {
            result = m_connector.getMetaDataDEFList();
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return result;
    }

    /**
    * Get a list of the def-files.
    *  @return  a list of the def-files path.
    */
    public String getStandardPropertyMappings() throws Exception {
        String request = "<Input board_id=\"" + m_connector.getBoardID() + "\" gate_request_type=\"StandardPropertyMappings\" />";
        String result;
        try
        {
            writeLine("getDEFInfo() request");
            writeLine(request);
            result = m_connector.getCtcsBll().GetDEF(request);
            writeLine(result);
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e,TCSException.BLL_ERROR_GET_DEF);
        }

        return result;
    }

    /**
    * @return  board id. A string that uniquely identifies MLS board.
    */
    public String getBoardId() throws Exception {
        String result;
        try
        {
            result = m_connector.getBoardID();
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return result;
    }

    /**
    * board id. A string that uniquely identifies MLS board. public void setBoardId ( String id ) throws MLSException
    * {
    * m_boardId = id;
    * }
    * 
    *  @return  connection module name.
    */
    public String getModuleName() throws Exception {
        /* TBI */
        String result;
        try
        {
            result = m_connector.getModuleName();
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return result;
    }

    /**
    * @return  the version of the def-file.
    */
    public String getDefFileVersion() throws Exception {
        /* TBI */
        String result;
        try
        {
            result = m_connector.getDefFileVersion();
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return result;
    }

    public void addXmlNote(int code, String note) throws Exception {
        m_connector.addXmlNote(code,note);
    }

    /**
    * MLSEngine calls this function when it finds some serious def-file issues.
    * Usually this means that def-file is not consistent with the data, or corrupted.
    * This function should send this info to MLS department for fixing the problem.
    * 
    *  @param message a message for MLS department.
    * 
    *  @param data the data received from MLS. It can be null, if unapplicable or no data received.
    */
    public void notifyTechSupport(String message, byte[] data) throws Exception {
    }

    /* TBI */
    /**
    * Downloads demo board data to the specified directory.
    * If an application doesn't have demo board this function can just be void.
    * 
    *  @param resultsDir the directory to create results file.
    */
    public void downloadDemoData(String resultsDir) throws Exception {
    }

    /* TBI */
    /**
    * @return  state or province name for the MLS board. MLSEngine uses it to set the state
    * of the listing, if it is missing in the result data. The logic is: if state is missing
    * in the result data, we assume it is from the same state as the MLS board.
    */
    public String getStateProvince() throws Exception {
        /* TBI */
        String result;
        try
        {
            result = m_connector.getStateProvince();
        }
        catch (Exception e)
        {
            throw TCSException.produceTCSException(e);
        }

        return result;
    }

    public String getResultFolder() throws Exception {
        return m_connector.getMessageHeader();
    }

    public String getURLContent(String path, String method, String header, String httpVersion) throws Exception {
        return m_connector.getURLContent(path,method,header,httpVersion);
    }

    public void addNote(String note) throws Exception {
        m_connector.addMLSEngineNote(note);
    }

    public String getBinFolder() throws Exception {
        return m_connector.getBinFolder();
    }

    public void writeLine(String line) throws Exception {
        m_connector.writeLine(line);
    }

    public void writeLine(String line, boolean isCompactCommunicationLog) throws Exception {
        m_connector.writeLine(line,isCompactCommunicationLog);
    }

    public void write(String line) throws Exception {
        m_connector.writeLine(line);
    }

    public void writeLine() throws Exception {
        return ;
    }

    public String getUserAgent() throws Exception {
        if (m_connector == null)
            return "";
         
        return m_connector.getUserAgent();
    }

    public String getRETSUAPwd() throws Exception {
        if (m_connector == null)
            return "";
         
        return m_connector.getRETSUAPwd();
    }

    public void addRawDataFile(String path) throws Exception {
        if (m_connector == null)
            return ;
         
        m_connector.addRawDataFile(path);
        return ;
    }

    public boolean isBypassAgentRosterAuth() throws Exception {
        if (m_connector == null)
            return false;
         
        return m_connector.isBypassAgentRosterAuth();
    }

    public boolean isGetPictureRequest() throws Exception {
        if (m_connector == null)
            return false;
         
        return m_connector.isGetPictureRequest();
    }

    public String[] getLoginInfoFromRequest() throws Exception {
        return m_connector.getLoginInfoFromRequest();
    }

    public String getRETSQueryParameter() throws Exception {
        return m_connector.getRETSQueryParameter();
    }

    //+-----------------------------------------------------------------------------------+
    //|                             Process control functions                             |
    //+-----------------------------------------------------------------------------------+
    /**
    * MLSEngine calls this function to set the state of progress bar if exists.
    * Application may show the status of the long operation implementing this function.
    * It may throw (or not throw) exceptions to signal that user tries
    * to stop the execution, or timeout occured.
    * 
    *  @param value a value of the progress.
    * 
    *  @param max maximum value of the progress.
    */
    public void setProgress(int value_Renamed, int max) throws Exception {
        m_connector.updateTaskProgress(value_Renamed / 100);
    }

    /**
    * MLSEngine calls this function to show users the status message.
    * Application may show the status of the long operation implementing this function.
    * It may throw (or not throw) exceptions to signal that user tries
    * to stop the execution, or timeout occured.
    * 
    *  @param message a message to show.
    */
    public void setMessage(String message) throws Exception {
        m_connector.setMessage(message);
    }

    /**
    * Checks if users try to stop the long operation executed by MLSEngine.
    * It should throw exception to signal that user tries to stop the process. It also may throw
    * exception on timeout.
    */
    public void checkStop() throws Exception {
        m_connector.checkStop();
    }

    /// <summary> This function should set a timeout of an operation. The time starts after the call of this
    /// function.
    /// <p>If timeout is expired, any call of progress control functions must throw timeout exception
    /// MLSEception.CODE_TIMEOUT.
    /// <p>There should be no timeout (reset state) by default.
    /// <p>If the process was interrupted by user (press Cancel) throws exception. Type of the
    /// exception doesn't matter, although application must know it and work with it properly.
    /// <p>Every call of setTimeout in MLSEngine has the corresponding call of resetTimeout.
    /// </summary>
    /// <param name="timeout">timeout in milliseconds. Negative or 0 values shall cause immediate timeout
    /// (timeout exception MLSException.CODE_TIMEOUT shall be thrown).
    /// </param>
    public void setTimeout(long timeout) throws Exception {
        /* TBI */
        long time = timeout;
    }

    /// <summary> Resets the timeout of an operation.
    /// <p>There should be no timeout (reset state) by default.
    /// <p>If the process was interrupted by user (press Cancel) throws exception. Type of the
    /// exception doesn't matter, although application must know it and work with it properly.
    /// <p>Every call of setTimeout in MLSEngine has the corresponding call of resetTimeout.
    /// </summary>
    public void resetTimeout() throws Exception {
        /* TBI */
        long time = 1;
    }

}


/*public MLSException createException ( Exception exc, String message, String developer_message, int format )
		{
		if( exc != null )
		return TCSException.produceTCSException( exc );
		
		return TCSException.produceTCSException( message, developer_message, TCSException.MLS_ERROR_UNABLE_COMPLETE_SEARCH );
		}
		
		public MLSException createException ( Exception exc, int code, String message, String developer_message, int format )
		{
		return TCSException.produceTCSException( message, developer_message, code );
		}
		*/