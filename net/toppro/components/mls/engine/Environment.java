//
// Translated by CS2J (http://www.cs2j.com): 10/5/2015 1:33:41 PM
//

package net.toppro.components.mls.engine;

import CS2JNet.System.Xml.XmlNode;
import java.net.URI;

import com.sun.deploy.xml.XMLNode;
import net.toppro.components.mls.engine.Environment;
import net.toppro.components.mls.engine.MLSException;

/**
* This class performs MLSEngine communication with the rest of the application.
* MLSEngine can call the Environment functions to get the def-file, application directories names,
* board id in the database, etc.
* Application has to extend this class and implement all the functions providing data for
* MLSEngine to create an instance of MLSEngine.
*/
public abstract class Environment   
{
    /**
    * This flag makes MLSEngine not to cache the def-file on local machine.
    * This is usable when environment already takes it from the local machine (rather than
    * from the network).
    */
    public static final int FLAG_NO_LOCAL_CACHE = 1;
    /**
    * When the constructor MLSEngine ( IEnvironment env, String def_file ) is used,
    * normally MLSEngine doesn't cache the def-file (because usually this is just a test version).
    * This flag makes MLSEngine to cache it anyway.
    */
    public static final int FLAG_CACHE_ON_EXPLICIT_DEF_FILE_SET = 2;
    /**
    * This flag makes MLSEngine to download multiple pictures if the DEF file
    * support multiple picture downloading.
    */
    public static final int FLAG_GET_MULTI_PICTURE = 4;
    /**
    * This flag makes MLSEngine to get record count from REST client.
    */
    public static final int FLAG_GET_RECORD_COUNT = 8;
    /**
    * This flag makse MLSEngine to get record count from REST client.
    */
    public static final int FLAG_INCLUDE_HIDDEN_FIELD = 16;
    /**
    * This flag makes MLSEngine not use a seperate request to recieve data from MLS boards.
    */
    public static final int FLAG_USE_CURRENT_THREAD_RECIEVE_DATA = 32;
    /**
    * This flag makes MLSEngine to know this is a check credential request from TCS.
    */
    public static final int FLAG_CHECK_CREDENTIAL = 64;
    /**
    * This flag makes MLSEngine to know this is a request from TCS server side.
    */
    public static final int FLAG_TCS_SERVER = 128;
    /**
    * //  This flag makes MLSEngine to know this is a request from TCS server side.
    */
    //public const int FLAG_GET_ADDITIONAL_DATA = 256;
    /**
    * @return  the combination of FLAG_XXX constants that specify different aspects
    * of MLSEngine behaviour.
    */
    public abstract int getFlags() throws Exception ;

    /**
    * @param fileURL graphic file URL.
    * 
    *  @return  true if an application is able to work with the file filePath
    * as with graphic file.
    */
    public abstract boolean isPictureFileSupported(String fileURL) throws Exception ;

    /**
    * @return  local path where MLSEngine may create it's files.
    * This function must ensure that the path exists. If it fails to create it, it should return null.
    */
    public abstract String getLocalHomePath() throws Exception ;

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
    public abstract URI getServerRoot() throws Exception ;

    /**
    * This function should replace some specific tags in str with the brand names for
    * the application:
    * {ApplicationName}      - Application name. Parameter "ApplicationName". Default value "TOP PRODUCER®".{ProductName}          - Product name. Parameter "ProductName". Default value "TOP PRODUCER®".{CompanyName}          - Company name. Parameter "CompanyName". Default value "TOP PRODUCER".{BrandName}            - Brand name. Parameter "BrandName". Default value "Standard".{TechSupportName}      - Tech support name. Parameter "TechSupportName". Default value "TOP PRODUCER Technical Support".{TechSupportPhone}     - Tech support phone. Parameter "TechSupportPhone". Default value "1-800-830-6047".{TechSupportEmail}     - Tech support email. Parameter "TechSupportEmail". Default value "support@topproducer.com".{WirelessProductName}  - Wireless product name. Parameter "WirelessProductName". Default value "TOP PRODUCER SellPhone™".{PalmProductName}      - Palm product name. Parameter "PalmProductName". Default value "Top Producer for Palm Computing Connected Organizers".{BrandDomainName}      - Brand domain name. Parameter "BrandDomainName". Default value "cp-demo.cp.net".{DesktopProductName}   - Desktop product name. Parameter "DesktopProductName". Default value "System 6/6i".{MLSProductName}       - MLS product name. Parameter "MLSProductName". Default value "TOP CONNECTOR 7i".{AccountProductName}   - Account manager product name. Parameter "AccountProductName". Default value "My Account".
    *  @param string with brand tags.
    * 
    *  @return  a string with tag brands replaced with the application specific values.
    */
    public abstract String formatBrands(String str) throws Exception ;

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
    public abstract boolean installZipFile(URI sourceURLBase, String sourceFile, String destPath) throws Exception ;

    /**
    * Downloads lookup table.
    * Lookup table contains an information, which other name may be used for cma field in
    * the result data. If MLSEngine fails to find the field using name specified in a def-file,
    * it tries to find the field using names enumerated in lookup table.
    * This function should download lookup table.
    */
    public abstract String downloadLookupTable() throws Exception ;

    /**
    * Downloads the def-file.
    *  @return  the content of the def-file.
    */
    public abstract String downloadDefFile() throws Exception ;

    /**
    * Get a list of the def-files.
    *  @return  a list of the def-files path.
    */
    public abstract XMLNode getMetaDataDEFList() throws Exception ;

    /**
    * Get Standard Property Mappings By BoardId
    *  @return  a xml in string.
    */
    public abstract String getStandardPropertyMappings() throws Exception ;

    /**
    * @return  board id. A string that uniquely identifies MLS board.
    */
    public abstract String getBoardId() throws Exception ;

    /**
    * @return  connection module name.
    */
    public abstract String getModuleName() throws Exception ;

    /**
    * @return  the version of the def-file.
    */
    public abstract String getDefFileVersion() throws Exception ;

    /**
    * MLSEngine calls this function when it finds some serious def-file issues.
    * Usually this means that def-file is not consistent with the data, or corrupted.
    * This function should send this info to MLS department for fixing the problem.
    * 
    *  @param message a message for MLS department.
    * 
    *  @param data the data received from MLS. It can be null, if unapplicable or no data received.
    */
    public abstract void notifyTechSupport(String message, byte[] data) throws Exception ;

    /**
    * Downloads demo board data to the specified directory.
    * If an application doesn't have demo board this function can just be void.
    * 
    *  @param resultsDir the directory to create results file.
    */
    public abstract void downloadDemoData(String resultsDir) throws Exception ;

    /**
    * @return  state or province name for the MLS board. MLSEngine uses it to set the state
    * of the listing, if it is missing in the result data. The logic is: if state is missing
    * in the result data, we assume it is from the same state as the MLS board.
    */
    public abstract String getStateProvince() throws Exception ;

    /**
    * @return  result folder name for server side top connector. For 7i, it should be null or empty.
    */
    public String getResultFolder() throws Exception {
        return null;
    }

    /**
    * @return  result picture binary data for server side top connector. For 7i, it should be null or empty.
    */
    public byte[] getPictureData() throws Exception {
        return null;
    }

    /**
    * @return  helper dll or exe folder path on TCS server.
    */
    public String getBinFolder() throws Exception {
        return "";
    }

    public String getSearchTempFolder() throws Exception {
        return "";
    }

    public void addNote(String note) throws Exception {
        return ;
    }

    public void writeLine(String line) throws Exception {
        return ;
    }

    public void writeLine(String line, boolean isCompactCommunicationLog) throws Exception {
        return ;
    }

    public void write(String line) throws Exception {
        return ;
    }

    public void writeLine() throws Exception {
        return ;
    }

    public String getUploadFolder() throws Exception {
        return "";
    }

    public String getUserAgent() throws Exception {
        return "";
    }

    public String getRETSUAPwd() throws Exception {
        return "";
    }

    public void addRawDataFile(String path) throws Exception {
        return ;
    }

    public boolean isBypassAgentRosterAuth() throws Exception {
        return false;
    }

    public boolean isGetPictureRequest() throws Exception {
        return false;
    }

    public String[] getLoginInfoFromRequest() throws Exception {
        return null;
    }

    public void addXmlNote(int code, String note) throws Exception {
        return ;
    }

    public String getRETSQueryParameter() throws Exception {
        return "";
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
    public abstract void setProgress(int value_Renamed, int max) throws Exception ;

    /**
    * MLSEngine calls this function to show users the status message.
    * Application may show the status of the long operation implementing this function.
    * It may throw (or not throw) exceptions to signal that user tries
    * to stop the execution, or timeout occured.
    * 
    *  @param message a message to show.
    */
    public abstract void setMessage(String message) throws Exception ;

    /**
    * Checks if users try to stop the long operation executed by MLSEngine.
    * It should throw exception to signal that user tries to stop the process. It also may throw
    * exception on timeout.
    */
    public abstract void checkStop() throws Exception ;

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
    public abstract void setTimeout(long timeout) throws Exception ;

    /// <summary> Resets the timeout of an operation.
    /// <p>There should be no timeout (reset state) by default.
    /// <p>If the process was interrupted by user (press Cancel) throws exception. Type of the
    /// exception doesn't matter, although application must know it and work with it properly.
    /// <p>Every call of setTimeout in MLSEngine has the corresponding call of resetTimeout.
    /// </summary>
    public abstract void resetTimeout() throws Exception ;

    //UPGRADE_NOTE: Field 'EnclosingInstance' was added to class 'StandardMLSException' to access its enclosing instance. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1019'"
    //+-----------------------------------------------------------------------------------+
    //|                             Error handling functions                              |
    //+-----------------------------------------------------------------------------------+
    private static class StandardMLSException  extends MLSException 
    {
        private void initBlock(Environment enclosingInstance) throws Exception {
            this.enclosingInstance = enclosingInstance;
        }

        private Environment enclosingInstance;
        public Environment getEnclosing_Instance() throws Exception {
            return enclosingInstance;
        }

        public StandardMLSException(Environment enclosingInstance, int code) throws Exception {
            super(code);
            initBlock(enclosingInstance);
        }

        public StandardMLSException(Environment enclosingInstance, Exception exc, String message, String developer_message, int format) throws Exception {
            super(exc, message, developer_message, format);
            initBlock(enclosingInstance);
        }

        public StandardMLSException(Environment enclosingInstance, Exception exc, int code, String message, String developer_message, int format) throws Exception {
            super(exc, code, message, developer_message, format);
            initBlock(enclosingInstance);
        }
    
    }

    /// <summary> Creates IMLSException with the specific code.
    /// <p>This function usually creates exceptions that should be catched by an application. Application
    /// then decides what to do with these errors.
    /// <p>An application may overwrite this function to provide more informative exceptions.
    /// For example, iCMA provides an exception that inherits from MLSException and also implements
    /// IExtendedException interface. This interface hepls iCMA ErrorHandler to handle MLSException.
    /// ErrorHandler doesn't know anything about MLSException, but it knows about IExtendedException
    /// interface. The idea is to make MLSEngine throw something that implements this interface.
    /// <p>Although normally an application doesn't need to overwrite this function.
    /// </summary>
    /// <code>  exception code. This is one of the integer constants CODE_XXX defined in IMLSException. </code>
    public MLSException createException(int code) throws Exception {
        return new StandardMLSException(this,code);
    }

    /// <summary> Creates IMLSException.
    /// <p>This function usually creates exceptions that should simply be shown to users.
    /// <p>An application may overwrite this function to provide more informative exceptions.
    /// For example, iCMA provides an exception that inherits from MLSException and also implements
    /// IExtendedException interface. This interface hepls iCMA ErrorHandler to handle MLSException.
    /// ErrorHandler doesn't know anything about MLSException, but it knows about IExtendedException
    /// interface. The idea is to make MLSEngine throw something that implements this interface.
    /// <p>Although normally an application doesn't need to overwrite this function.
    /// </summary>
    /// <param name="exc">exception which caused throwing of this MLSException, or null if not applicable.
    /// </param>
    /// <param name="message">error message. This also may be null. The implementation should decide, what
    /// to use as a message in this case. It may use exc.getMessage(), or some predefined string, or
    /// whatever.
    /// </param>
    /// <param name="developer_message">an info that should be added to developer message. See addDeveloper
    /// message function. Can be null. Then no developer message is added.
    /// </param>
    /// <param name="format">format of the message. One of IMLSException.FORMAT_XXX constants.
    /// </param>
    public MLSException createException(Exception exc, String message, String developer_message, int format) throws Exception {
        return new StandardMLSException(this,exc,message,developer_message,format);
    }

    public MLSException createException(Exception exc, int code, String message, String developer_message, int format) throws Exception {
        return new StandardMLSException(this,exc,code,message,developer_message,format);
    }

}


